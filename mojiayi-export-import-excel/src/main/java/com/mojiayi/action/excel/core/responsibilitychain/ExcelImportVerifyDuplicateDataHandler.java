package com.mojiayi.action.excel.core.responsibilitychain;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import com.mojiayi.action.excel.constant.ExcelConstant;
import com.mojiayi.action.excel.core.ExcelExportTool;
import com.mojiayi.action.excel.dto.FixedTableHeader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 校验导入文件表格中的业务内容是否重复，通过注解{@code FixedColumn}标记的唯一键去重
 * </p>
 *
 * @author mojiayi
 */
@Slf4j
@Component
@Order(2)
public class ExcelImportVerifyDuplicateDataHandler extends ExcelImportResponsibilityChainAbstractHandler {
    @Resource
    private ExcelExportTool excelExportTool;

    /**
     * @param tableName      表名
     * @param headerNameList 导入文件中的表头
     * @param dataRowList    导入文件中的业务数据
     * @param response       HTTP请求的servlet返回对象
     * @return 返回导入操作结果
     */
    @Override
    String doFilter(String tableName, List<String> headerNameList, List<List<String>> dataRowList, HttpServletResponse response) {
        log.info("开始校验导入文件表格中的业务内容是否重复,tableName={}", tableName);
        if (CollUtil.isEmpty(dataRowList)) {
            String errMsg = String.format("表%s的导入文件中没有业务数据", tableName);
            log.warn(errMsg);
            return errMsg;
        }
        Class<?> dataClass = extBasicDataClassMap.get(tableName);
        List<String> uniqueKeyFieldNameList = getUniqueKeyFieldList(tableName, dataClass);
        if (CollUtil.isEmpty(uniqueKeyFieldNameList)) {
            String errMsg = String.format("表%s没有定义唯一键", tableName);
            log.warn(errMsg);
            return errMsg;
        }

        List<List<String>> errMsgList = verifyDuplicateData(tableName, headerNameList, dataRowList, uniqueKeyFieldNameList);
        if (CollUtil.isNotEmpty(errMsgList)) {
            log.info("校验不通过，文件表格中的业务内容有重复,tableName={}", tableName);
            List<FixedTableHeader> headerList = List.of(new FixedTableHeader("导入错误", "导入错误", 0));
            String filename = "导入" + tableName + "错误原因.xlsx";
            try {
                excelExportTool.exportCustomizeHeaderHttpResponseExcel(headerList, errMsgList, filename, response);
            } catch (IOException | InvocationTargetException | IllegalAccessException e) {
                log.error("返回{}给用户失败", filename, e);
                return String.format("校验表%s的导入数据重复性时出错", tableName);
            }
            return String.format("表%s的导入数据中有重复的", tableName);
        }

        log.info("通过校验，文件表格中的业务内容都没有重复,tableName={}", tableName);
        return null;
    }

    /**
     * 校验文件表格中的业务内容是否重复
     *
     * @param tableName              表名
     * @param headerNameList         导入文件中的表头
     * @param dataRowList            导入文件中的业务数据
     * @param uniqueKeyFieldNameList 组成唯一索引的字段
     * @return 返回校验发现的错误信息
     */
    private List<List<String>> verifyDuplicateData(String tableName, List<String> headerNameList, List<List<String>> dataRowList,
                                                   List<String> uniqueKeyFieldNameList) {
        Map<String, String> fieldNameAndPropMap = getFieldNameAndPropMap(tableName);

        BloomFilter<CharSequence> boomFilter = BloomFilter.create(Funnels.stringFunnel(StandardCharsets.UTF_8), ExcelConstant.EXPECTED_INSERTIONS, ExcelConstant.FPP);

        List<List<String>> errMsgList = new ArrayList<>();
        String fieldProp = null;
        int rowIndex = 0;
        StringBuilder uniqueKey = new StringBuilder();
        for (List<String> oneRowData : dataRowList) {
            int cellIndex = 0;
            uniqueKey.delete(0, uniqueKey.length());
            for (String cellValue : oneRowData) {
                fieldProp = fieldNameAndPropMap.get(headerNameList.get(cellIndex));
                if (uniqueKeyFieldNameList.contains(fieldProp)) {
                    uniqueKey.append(cellValue);
                    uniqueKey.append(StringPool.HASH);
                }

                cellIndex++;
            }
            if (boomFilter.mightContain(uniqueKey.toString())) {
                addErrMsg(rowIndex, errMsgList);
            } else {
                boomFilter.put(uniqueKey.toString());
            }
            rowIndex++;
        }
        return errMsgList;
    }

    /**
     * 添加错误信息到错误列表
     *
     * @param rowIndex   行数
     * @param errMsgList 错误列表
     */
    private void addErrMsg(int rowIndex, List<List<String>> errMsgList) {
        String stringBuilder = "第" +
                (rowIndex + 2) +
                "行的数据与其他行重复";

        errMsgList.add(List.of(stringBuilder));
    }
}
