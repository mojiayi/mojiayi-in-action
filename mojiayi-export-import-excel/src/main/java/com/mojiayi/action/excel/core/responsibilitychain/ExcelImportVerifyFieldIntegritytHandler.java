package com.mojiayi.action.excel.core.responsibilitychain;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.mojiayi.action.excel.core.ExcelImportTool;
import com.mojiayi.action.excel.dto.ExcelTemplateDTO;
import com.mojiayi.action.excel.dto.FixedTableHeader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 校验导入文件表格中的列和目标表业务字段是否匹配
 * </p>
 *
 * @author mojiayi
 */
@Slf4j
@Component
@Order(0)
public class ExcelImportVerifyFieldIntegritytHandler extends ExcelImportResponsibilityChainAbstractHandler {
    @Resource
    public ExcelImportTool excelImportTool;

    /**
     * 校验导入文件表格中的列和目标表业务字段是否匹配
     *
     * @param tableName      表名
     * @param headerNameList 导入文件中的表头
     * @param dataRowList    导入文件中的业务数据
     * @param response       HTTP请求的servlet返回对象
     * @return 返回导入操作结果
     */
    @Override
    String doFilter(String tableName, List<String> headerNameList, List<List<String>> dataRowList, HttpServletResponse response) {
        log.info("开始校验导入文件表格中的列和目标表业务字段是否匹配,tableName={}", tableName);
        if (CollUtil.isEmpty(headerNameList)) {
            String errMsg = String.format("表%s的导入文件中没有表头", tableName);
            log.warn(errMsg);
            return errMsg;
        }
        ExcelTemplateDTO excelTemplateDTO = excelImportTool.getTemplateHeader(tableName);
        if (headerNameList.size() != excelTemplateDTO.getHeaderList().size()) {
            log.warn("表{}的导入文件列数错误,headerNameList.size={},tableFieldList.size={}", tableName, headerNameList.size(), excelTemplateDTO.getHeaderList().size());
            return String.format("表%s的导入文件列数错误", tableName);
        }
        boolean allMatch = excelTemplateDTO.getHeaderList().stream().allMatch(v -> headerNameList.contains(v.getName()));
        if (allMatch) {
            log.info("通过校验，导入文件表格中的列和目标表业务字段都匹配,tableName={}", tableName);
            return null;
        } else {
            log.warn("校验不通过，表{}的导入文件列与表字段不匹配,headerNameList={},tableFieldList={}", tableName, JSON.toJSONString(headerNameList),
                    JSON.toJSONString(excelTemplateDTO.getHeaderList().stream().map(FixedTableHeader::getName)));
            return String.format("表%s的导入文件列与表字段不匹配", tableName);
        }
    }
}
