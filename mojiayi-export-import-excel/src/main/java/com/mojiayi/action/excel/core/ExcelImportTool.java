package com.mojiayi.action.excel.core;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.mojiayi.action.excel.constant.ExcelConstant;
import com.mojiayi.action.excel.core.responsibilitychain.ExcelImportResponsibilityChainAbstractHandler;
import com.mojiayi.action.excel.dto.ExcelImportDTO;
import com.mojiayi.action.excel.dto.ExcelTemplateDTO;
import com.mojiayi.action.excel.dto.FixedTableHeader;
import com.mojiayi.action.excel.dto.TableMetaInfo;
import com.mojiayi.action.excel.mapper.TableMetaInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 导入excel文件的工具
 * </p>
 *
 * @author mojiayi
 */
@Slf4j
@Component
public class ExcelImportTool implements ApplicationContextAware {
    @Resource
    public TableMetaInfoMapper tableMetaInfoMapper;

    private ExcelImportResponsibilityChainAbstractHandler handler;

    /**
     * 构建导入数据校验和保存入库的责任链
     *
     * @param applicationContext the ApplicationContext object to be used by this object
     * @throws BeansException 实例不存在的异常
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, ExcelImportResponsibilityChainAbstractHandler> handlerMap = applicationContext.getBeansOfType(ExcelImportResponsibilityChainAbstractHandler.class);
        if (handlerMap.isEmpty()) {
            log.warn("没有ExcelImportResponsibilityChainAbstractHandler类型的责任链处理器");
            return;
        }
        // 把责任链上的多个环节按注解{@code Order}的值升序排序
        List<ExcelImportResponsibilityChainAbstractHandler> handlerList = new ArrayList<>(handlerMap.values());
        handlerList.sort(Comparator.comparing(v -> v.getClass().getAnnotation(Order.class).value()));

        for (int index = 0; index < handlerList.size(); index++) {
            if (index == 0) {
                handler = handlerList.get(0);
            } else {
                ExcelImportResponsibilityChainAbstractHandler currentHandler = handlerList.get(index - 1);
                ExcelImportResponsibilityChainAbstractHandler nextHandler = handlerList.get(index);
                currentHandler.setNextHandler(nextHandler);
            }
        }
    }

    /**
     * 使用责任链模式，实现数据校验和保存入库
     *
     * @param tableName      表名
     * @param headerNameList 导入文件中的表头
     * @param dataRowList    导入文件中的业务数据
     * @param response       HTTP请求的servlet返回对象
     * @return 返回导入操作结果
     */
    public String verifyImportData(String tableName, List<String> headerNameList, List<List<String>> dataRowList, HttpServletResponse response) {
        return handler.filter(tableName, headerNameList, dataRowList, response);
    }

    /**
     * 根据表名查询数据库表的元数据，获取其中的字段名和字段注释
     *
     * @param tableName 表名
     * @return 返回表的字段名和字段注释
     */
    public ExcelTemplateDTO getTemplateHeader(String tableName) {
        List<TableMetaInfo> tableFieldList = tableMetaInfoMapper.selectFieldList(tableName);
        tableFieldList.removeIf(v -> ExcelConstant.EXCLUDE_COLUMN_SET.contains(v.getColumnName()));
        ExcelTemplateDTO excelTemplateDTO = new ExcelTemplateDTO();
        if (CollUtil.isEmpty(tableFieldList)) {
            String errMsg = String.format("表%s中没有要导出到模板的字段", tableName);
            log.info(errMsg);
            excelTemplateDTO.setErrMsg(errMsg);
            return excelTemplateDTO;
        }

        List<FixedTableHeader> headerList = new ArrayList<>(tableFieldList.size());
        int index = 0;
        for (TableMetaInfo fieldEntity : tableFieldList) {
            headerList.add(new FixedTableHeader(fieldEntity.getColumnName(), fieldEntity.getColumnComment(), index));
            index++;
        }
        excelTemplateDTO.setHeaderList(headerList);
        return excelTemplateDTO;
    }

    /**
     * 从第一个表单提取表头列表，默认取第一行作为表头
     *
     * @param tableName     表名
     * @param multipartFile 上传文件
     * @return 返回导入表格的表头列表
     */
    public ExcelImportDTO getImportHeaderNameList(String tableName, MultipartFile multipartFile) {
        log.info("开始从表单提取表头列表,tableName={}", tableName);
        ExcelImportDTO excelImportDTO = new ExcelImportDTO();
        try (Workbook workbook = new XSSFWorkbook(multipartFile.getInputStream())) {
            if (workbook.getNumberOfSheets() <= 0) {
                String errMsg = String.format("表%s的导入文件没有可读取的表单", tableName);
                log.warn(errMsg);
                excelImportDTO.setErrMsg(errMsg);
                return excelImportDTO;
            }
            // 默认取第一个表单
            Sheet sheet = workbook.getSheetAt(0);
            // 取第一行作为表头
            Row headerRow = sheet.getRow(0);
            if (headerRow.getLastCellNum() <= 0) {
                String errMsg = String.format("表%s的导入文件没有可读取的表头", tableName);
                log.warn(errMsg);
                excelImportDTO.setErrMsg(errMsg);
                return excelImportDTO;
            }
            List<String> headerNameList = new ArrayList<>(headerRow.getLastCellNum());
            for (int index = 0; index < headerRow.getLastCellNum(); index++) {
                if (headerRow.getCell(index) != null) {
                    headerNameList.add(headerRow.getCell(index).getStringCellValue());
                }
            }
            excelImportDTO.setHeaderNameList(headerNameList);

            log.info("完成从表单提取表头列表,tableName={},headerNameList.size={}", tableName, headerNameList.size());
        } catch (IOException e) {
            String errMsg = String.format("从表%s的导入文件获取表头时报错", tableName);
            log.warn(errMsg, e);
            excelImportDTO.setErrMsg(errMsg);
        }
        return excelImportDTO;
    }

    /**
     * 从第一个表单提取业务数据
     *
     * @param tableName     表名
     * @param multipartFile 上传文件
     * @param cellCount     导入文件中的表头数量
     * @return 返回导入文件中的业务数据
     */
    public ExcelImportDTO getImportDataList(String tableName, MultipartFile multipartFile, int cellCount) {
        log.info("开始从表单提取数据内容,tableName={}", tableName);
        ExcelImportDTO excelImportDTO = new ExcelImportDTO();
        try (Workbook workbook = new XSSFWorkbook(multipartFile.getInputStream())) {
            if (workbook.getNumberOfSheets() <= 0) {
                String errMsg = String.format("表%s的导入文件没有可读取的表单", tableName);
                log.warn(errMsg);
                excelImportDTO.setErrMsg(errMsg);
                return excelImportDTO;
            }
            // 默认取第一个表单
            Sheet sheet = workbook.getSheetAt(0);
            List<List<String>> dataRowList = new ArrayList<>(sheet.getLastRowNum());
            List<String> oneRowDataList = null;
            // 第一行作为表头，在读取数据内容时要跳过
            Row row = null;
            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                row = sheet.getRow(rowIndex);
                if (row == null) {
                    continue;
                }
                oneRowDataList = new ArrayList<>();
                for (int cellIndex = 0; cellIndex < cellCount; cellIndex++) {
                    convertCell2String(row.getCell(cellIndex), oneRowDataList);
                }
                dataRowList.add(oneRowDataList);
            }
            excelImportDTO.setDataRowList(dataRowList);

            log.info("完成从表单提取数据内容,tableName={},dataRowList.size={}", tableName, dataRowList.size());
        } catch (IOException e) {
            String errMsg = String.format("从表%s的导入文件获取数据内容时报错", tableName);
            log.warn(errMsg, e);
            excelImportDTO.setErrMsg(errMsg);
        }

        return excelImportDTO;
    }

    /**
     * 把单元格内容按对应类型转换成字符串
     *
     * @param cell           单元格
     * @param oneRowDataList 转换后的同一行的所有单元格值
     */
    private void convertCell2String(Cell cell, List<String> oneRowDataList) {
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setGroupingUsed(false);
        if (cell == null) {
            oneRowDataList.add(StringPool.EMPTY);
        } else if (cell.getCellType().equals(CellType.STRING)) {
            // 字符串
            oneRowDataList.add(cell.getRichStringCellValue().getString());
        } else if (cell.getCellType().equals(CellType.NUMERIC)) {
            // 数值
            if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)) {
                // 日期类型
                oneRowDataList.add(DateUtil.format(cell.getDateCellValue(), "yyyy-MM-dd HH:mm:ss"));
            } else {
                // 普通数值类型
                oneRowDataList.add((numberFormat.format(cell.getNumericCellValue())));
            }
        } else if (cell.getCellType().equals(CellType.BOOLEAN)) {
            // 布尔类型
            oneRowDataList.add(cell.getBooleanCellValue() ? "TRUE" : "FALSE");
        } else {
            oneRowDataList.add("");
        }
    }
}
