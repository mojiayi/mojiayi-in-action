package com.mojiayi.action.excel.core.responsibilitychain;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.mojiayi.action.excel.annotation.FixedColumn;
import com.mojiayi.action.excel.core.ExcelExportTool;
import com.mojiayi.action.excel.dto.FixedTableHeader;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 校验导入文件表格中的业务内容是否完整，通过注解{@code FixedColumn}标记不能为空的字段不能为空，指定了数据格式的字段内容格式必须合法
 * </p>
 *
 * @author mojiayi
 */
@Slf4j
@Component
@Order(1)
public class ExcelImportVerifyDataValidityHandler extends ExcelImportResponsibilityChainAbstractHandler {
    @Resource
    private ExcelExportTool excelExportTool;

    /**
     * 校验导入文件表格中的业务内容是否完整
     *
     * @param tableName      表名
     * @param headerNameList 导入文件中的表头
     * @param dataRowList    导入文件中的业务数据
     * @param response       HTTP请求的servlet返回对象
     * @return 返回导入操作结果
     */
    @Override
    String doFilter(String tableName, List<String> headerNameList, List<List<String>> dataRowList, HttpServletResponse response) {
        log.info("开始校验导入文件表格中的业务内容是否完整,tableName={}", tableName);
        if (CollUtil.isEmpty(dataRowList)) {
            String errMsg = String.format("表%s的导入文件中没有业务数据", tableName);
            log.warn(errMsg);
            return errMsg;
        }
        Class<?> dataClass = extBasicDataClassMap.get(tableName);
        if (dataClass == null) {
            String errMsg = String.format("表%s没有对应的Domain类定义", tableName);
            log.warn(errMsg);
            return errMsg;
        }

        List<String> requiredFieldNameList = new ArrayList<>();
        Map<String, String> specifyPatternFieldMap = new HashMap<>();
        FixedColumn annotation = null;
        for (Field field : dataClass.getDeclaredFields()) {
            annotation = field.getAnnotation(FixedColumn.class);
            if (annotation == null) {
                continue;
            }
            if (annotation.required()) {
                requiredFieldNameList.add(field.getName());
            }
            if (StringUtils.isNotEmpty(annotation.pattern())) {
                specifyPatternFieldMap.put(field.getName(), annotation.pattern());
            }
        }

        List<List<String>> errMsgList = verifyDataValidity(tableName, headerNameList, dataRowList, requiredFieldNameList, specifyPatternFieldMap);
        if (CollUtil.isNotEmpty(errMsgList)) {
            log.info("校验不通过，导入文件表格中的业务内容不完整,tableName={},errMsgList={}", tableName, JSON.toJSONString(errMsgList));
            List<FixedTableHeader> headerList = List.of(new FixedTableHeader("导入错误", "导入错误", 0));
            String filename = "导入" + tableName + "错误原因.xlsx";
            try {
                excelExportTool.exportCustomizeHeaderHttpResponseExcel(headerList, errMsgList, filename, response);
            } catch (IOException | InvocationTargetException | IllegalAccessException e) {
                log.error("表{}的数据不完整，返回错误信息给用户失败", filename, e);
                return String.format("校验表%s的数据完整性时出错", tableName);
            }
        }

        log.info("通过校验，导入文件表格中的业务内容完整,tableName={}", tableName);
        return null;
    }

    /**
     * 校验数据是否合法
     *
     * @param tableName              表名
     * @param headerNameList         导入文件中的表头
     * @param dataRowList            导入文件中的业务数据
     * @param requiredFieldNameList  非空字段列表
     * @param specifyPatternFieldMap 指定了特殊格式的字段列表
     * @return 返回非法数据信息
     */
    private List<List<String>> verifyDataValidity(String tableName, List<String> headerNameList, List<List<String>> dataRowList,
                                                  List<String> requiredFieldNameList, Map<String, String> specifyPatternFieldMap) {
        Map<String, String> fieldNameAndPropMap = getFieldNameAndPropMap(tableName);

        List<List<String>> errMsgList = new ArrayList<>();
        String fieldProp = null;
        int rowIndex = 0;
        for (List<String> oneRowData : dataRowList) {
            int cellIndex = 0;
            for (String cellValue : oneRowData) {
                fieldProp = fieldNameAndPropMap.get(headerNameList.get(cellIndex));
                verifyRequiredCell(cellValue, requiredFieldNameList.contains(fieldProp), rowIndex, cellIndex, errMsgList);
                verifySpecifyPatternCell(cellValue, specifyPatternFieldMap.get(fieldProp), rowIndex, cellIndex, errMsgList);

                cellIndex++;
            }
            rowIndex++;
        }
        return errMsgList;
    }

    /**
     * 校验非空字段是否为空
     *
     * @param cellValue  字段值
     * @param isRequired 是否必须
     * @param rowIndex   行数
     * @param cellIndex  列数
     * @param errMsgList 错误信息
     */
    private void verifyRequiredCell(String cellValue, boolean isRequired, int rowIndex, int cellIndex, List<List<String>> errMsgList) {
        if (!isRequired) {
            return;
        }
        if (StringUtils.isBlank(cellValue)) {
            addErrMsg(rowIndex, cellIndex, "不能为空", errMsgList);
        }
    }

    /**
     * 校验指定格式的数据是否合法
     *
     * @param cellValue  字段值
     * @param regexp     数据格式
     * @param rowIndex   行数
     * @param cellIndex  列数
     * @param errMsgList 错误信息
     */
    private void verifySpecifyPatternCell(String cellValue, String regexp, int rowIndex, int cellIndex, List<List<String>> errMsgList) {
        if (regexp == null) {
            return;
        }

        if (!cellValue.matches(regexp)) {
            addErrMsg(rowIndex, cellIndex, "格式错误", errMsgList);
        }
    }

    /**
     * 添加错误信息到错误列表
     *
     * @param rowIndex     行数
     * @param cellIndex    列数
     * @param errMsgSuffix 错误信息后缀
     * @param errMsgList   错误列表
     */
    private void addErrMsg(int rowIndex, int cellIndex, String errMsgSuffix, List<List<String>> errMsgList) {
        String stringBuilder = "第" +
                (rowIndex + 2) +
                "行第" +
                (cellIndex + 1) +
                "列的数据" +
                errMsgSuffix;

        errMsgList.add(List.of(stringBuilder));
    }
}
