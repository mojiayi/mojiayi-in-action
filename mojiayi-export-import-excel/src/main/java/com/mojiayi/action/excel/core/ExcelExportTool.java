package com.mojiayi.action.excel.core;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.google.common.base.CaseFormat;
import com.mojiayi.action.excel.annotation.FixedColumn;
import com.mojiayi.action.excel.constant.ExcelConstant;
import com.mojiayi.action.excel.dto.ExcelTemplateDTO;
import com.mojiayi.action.excel.dto.FixedTableHeader;
import com.mojiayi.action.excel.dto.TableMetaInfo;
import com.mojiayi.action.excel.mapper.TableMetaInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 导出excel表格的工具
 * </p>
 *
 * @author mojiayi
 */
@Slf4j
@Component
public class ExcelExportTool {
    @Resource
    public TableMetaInfoMapper tableMetaInfoMapper;

    @Resource
    public ExcelImportTool excelImportTool;

    /**
     * 导出自定义表头的excel表格到本地磁盘
     *
     * @param tableName  表名
     * @param headerList 要导出的文件表头
     * @param filepath   输出文件的绝对路径
     * @throws FileNotFoundException 文件不存在的异常
     */
    public void exportCustomizeHeaderLocalExcel(String tableName, List<FixedTableHeader> headerList, String filepath) throws FileNotFoundException, InvocationTargetException, IllegalAccessException {
        exportCustomizeHeaderLocalExcel(tableName, headerList, Collections.emptyList(), filepath);
    }

    /**
     * 导出自定义表头的excel表格到本地磁盘
     *
     * @param tableName  表名
     * @param headerList 要导出的文件表头
     * @param dataList   要被导出的数据，可为空
     * @param filepath   输出文件的绝对路径
     * @param <T>        被导出数据的类型
     * @throws FileNotFoundException 文件不存在的异常
     */
    public <T> void exportCustomizeHeaderLocalExcel(String tableName, List<FixedTableHeader> headerList, List<?> dataList, String filepath) throws FileNotFoundException, InvocationTargetException, IllegalAccessException {
        if (CollUtil.isEmpty(headerList)) {
            log.info("传入表头为空不需要导出表格");
            return;
        }

        List<String> headerPropList = new ArrayList<>();
        List<String> headerNameList = outputHeader(headerList, headerPropList);

        List<List<String>> rows = new ArrayList<>();
        if (CollUtil.isNotEmpty(dataList)) {
            rows = outputDataRow(tableName, dataList, headerPropList);
        }
        rows.add(0, headerNameList);

        ExcelWriter excelWriter = ExcelUtil.getWriter(true);
        mergeHeaderCell(headerList, excelWriter);
        excelWriter.write(rows);

        OutputStream outputStream = new FileOutputStream(filepath);
        excelWriter.flush(outputStream, true);
        excelWriter.close();
        IoUtil.close(outputStream);
    }

    /**
     * 导出自定义表头的excel表格到http请求的返回结果
     *
     * @param headerList 要导出的文件表头
     * @param filename   导出文件的名称
     * @param response   HTTP请求的servlet返回对象
     * @throws IOException 文件输出异常
     */
    public void exportCustomizeHeaderHttpResponseExcel(List<FixedTableHeader> headerList, String filename, HttpServletResponse response) throws IOException, InvocationTargetException, IllegalAccessException {
        exportCustomizeHeaderHttpResponseExcel(headerList, Collections.emptyList(), filename, response);
    }

    /**
     * 导出自定义表头的excel表格到http请求的返回结果
     *
     * @param headerList 要导出的文件表头
     * @param dataList 要被导出的数据，可为空
     * @param filename   导出文件的名称
     * @param response   HTTP请求的servlet返回对象
     * @param <T>      被导出数据的类型
     * @throws IOException 文件输出异常
     */
    public <T> void exportCustomizeHeaderHttpResponseExcel(List<FixedTableHeader> headerList, List<?> dataList, String filename, HttpServletResponse response) throws IOException, InvocationTargetException, IllegalAccessException {
        if (CollUtil.isEmpty(headerList)) {
            log.info("传入表头为空不需要导出表格");
            return;
        }

        List<String> headerPropList = new ArrayList<>();
        List<String> headerNameList = outputHeader(headerList, headerPropList);

        List<List<String>> rows = new ArrayList<>();
        if (CollUtil.isNotEmpty(dataList)) {
            rows = outputDataRow(null, dataList, headerPropList);
        }
        rows.add(0, headerNameList);

        ExcelWriter excelWriter = ExcelUtil.getWriter(true);
        mergeHeaderCell(headerList, excelWriter);
        excelWriter.write(rows);

        exportHttpResponseExcel(excelWriter, filename, response);
    }

    /**
     * 把传入的数据导出成excel表格到本地磁盘
     *
     * @param tableName  表名
     * @param dataList 要被导出的数据
     * @param filepath 输出文件的绝对路径
     * @param <T>      被导出数据的类型，字段上要用注解{@code FixedColumn}配置所在列的表头信息
     * @throws IllegalAccessException    通过反射获取导出元素上数据时可能会抛出非法访问异常
     * @throws InvocationTargetException 通过反射获取导出元素上数据时可能会抛出对象访问异常
     * @throws FileNotFoundException     文件不存在的异常
     */
    public <T> void exportLocalExcel(String tableName, List<T> dataList, String filepath) throws IllegalAccessException, InvocationTargetException, FileNotFoundException {
        if (Strings.isEmpty(tableName) && CollUtil.isEmpty(dataList)) {
            log.info("导出excel表格到本地磁盘时，表信息或表中数据列表不能同时为空");
            return;
        }
        if (CollUtil.isEmpty(dataList)) {
            ExcelTemplateDTO excelTemplateDTO = excelImportTool.getTemplateHeader(tableName);
            if (excelTemplateDTO.getErrMsg() != null) {
                return;
            }

            try {
                exportCustomizeHeaderLocalExcel(tableName, excelTemplateDTO.getHeaderList(), filepath);
            } catch (IOException | InvocationTargetException | IllegalAccessException e) {
                log.info("导出表{}的空数据表格失败", tableName, e);
            }
        } else {
            ExcelWriter excelWriter = exportExcel(tableName, dataList);

            OutputStream outputStream = new FileOutputStream(filepath);
            excelWriter.flush(outputStream, true);
            excelWriter.close();
            IoUtil.close(outputStream);
        }
    }

    /**
     * 把传入的数据导出成excel表格到http请求的返回结果
     *
     * @param tableName  表名
     * @param dataList 要被导出的数据
     * @param filename 导出文件的名称
     * @param response HTTP请求的servlet返回对象
     * @param <T>      被导出数据的类型，字段上要用注解{@code FixedColumn}配置所在列的表头信息
     * @throws IOException               文件输出异常
     * @throws InvocationTargetException 通过反射获取导出元素上数据时可能会抛出对象访问异常
     * @throws IllegalAccessException    通过反射获取导出元素上数据时可能会抛出非法访问异常
     */
    public <T> void exportHttpResponseExcel(String tableName, List<T> dataList, String filename, HttpServletResponse response) throws IOException, InvocationTargetException, IllegalAccessException {
        if (Strings.isEmpty(tableName) && CollUtil.isEmpty(dataList)) {
            log.info("导出excel表格到http返回结果时，表信息或表中数据列表不能同时为空");
            return;
        }
        if (CollUtil.isEmpty(dataList)) {
            ExcelTemplateDTO excelTemplateDTO = excelImportTool.getTemplateHeader(tableName);
            if (excelTemplateDTO.getErrMsg() != null) {
                return;
            }

            try {
                exportCustomizeHeaderHttpResponseExcel(excelTemplateDTO.getHeaderList(), filename, response);
            } catch (IOException | InvocationTargetException | IllegalAccessException e) {
                log.info("导出表{}的空数据表格失败", tableName, e);
            }
        } else {
            ExcelWriter excelWriter = exportExcel(tableName, dataList);

            exportHttpResponseExcel(excelWriter, filename, response);
        }
    }

    /**
     * 把excel文件内容输出到http请求的返回结果
     *
     * @param excelWriter excel文件写对象
     * @param filename    导出文件的名称
     * @param response    HTTP请求的servlet返回对象
     * @throws IOException 文件输出异常
     */
    private void exportHttpResponseExcel(ExcelWriter excelWriter, String filename, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String contentDisposition = "attachment; filename=%s; filename*=utf-8''%s";
        response.setHeader("Content-Disposition", String.format(contentDisposition, filename, URLEncoder.encode(filename, StandardCharsets.UTF_8)));
        ServletOutputStream outputStream = response.getOutputStream();
        excelWriter.flush(outputStream, true);
        excelWriter.close();
        IoUtil.close(outputStream);
    }

    /**
     * 把传入的数据导出到excel表格，表头信息通过传入数据各个字段上的注解指定，支持单级表头和两级表头
     *
     * @param tableName 表名
     * @param dataList  要被导出的数据
     * @param <T>       被导出数据的类型，字段上要用注解{@code FixedColumn}配置所在列的表头信息
     * @return 拼装好的excel文件写对象
     * @throws IllegalAccessException    通过反射获取导出元素上数据时可能会抛出非法访问异常
     * @throws InvocationTargetException 通过反射获取导出元素上数据时可能会抛出对象访问异常
     */
    private <T> ExcelWriter exportExcel(String tableName, List<T> dataList) throws IllegalAccessException, InvocationTargetException {
        // 传入参数中每个列表元素都是相同类型，取第一个元素的字段
        Field[] fields = dataList.get(0).getClass().getDeclaredFields();
        // 建立以字段名为key，字段为value的映射关系
        Map<String, Field> fieldMap = new HashMap<>();
        FixedColumn annotation = null;
        List<String> parentPropList = new ArrayList<>();
        for (Field field : fields) {
            fieldMap.put(field.getName(), field);
            annotation = field.getAnnotation(FixedColumn.class);
            if (annotation != null && StringUtils.isNotEmpty(annotation.parentProp())) {
                // 当字段上有动态列注解，且指定了父字段时，把父字段存放在一起
                parentPropList.add(annotation.parentProp());
            }
        }

        if (CollUtil.isNotEmpty(parentPropList)) {
            // 对父字段去重，因为多个字段的父字段可能相同
            parentPropList = parentPropList.stream().distinct().collect(Collectors.toList());
        }

        List<FixedTableHeader> headerList = buildHeaderList(fieldMap, parentPropList);

        List<String> headerPropList = new ArrayList<>();
        List<String> headerNameList = outputHeader(headerList, headerPropList);

        List<List<String>> rows = outputDataRow(tableName, dataList, headerPropList);
        rows.add(0, headerNameList);

        ExcelWriter writer = ExcelUtil.getWriter(true);
        mergeHeaderCell(headerList, writer);
        writer.write(rows);

        return writer;
    }

    /**
     * 根据传入数据各个字段上的注解，生成动态表头列表
     * @param fieldMap 传入数据的字段信息
     * @param parentPropList 传入数据中作为父字段的字段列表
     * @return 返回动态表头列表
     */
    private List<FixedTableHeader> buildHeaderList(Map<String, Field> fieldMap, List<String> parentPropList) {
        Map<String, FixedTableHeader> headerMap = new HashMap<>();
        String fieldName = null;
        FixedColumn annotation = null;
        for (Field field : fieldMap.values()) {
            annotation = field.getAnnotation(FixedColumn.class);
            if (annotation == null) {
                continue;
            }
            fieldName = field.getName();
            boolean isSubHeader = StringUtils.isNotEmpty(annotation.parentProp());
            if (isSubHeader) {
                // 有父字段时，当作二级表头处理
                buildDoubleLevelHeaderMap(fieldMap, field, headerMap);
            }  else {
                if (!parentPropList.contains(fieldName)) {
                    // 本身不是父字段，也没有指定父字段时，当作一级表头处理
                    headerMap.put(fieldName, new FixedTableHeader(fieldName, annotation.name(), annotation.index()));
                }
            }
        }

        return sortHeaderByIndex(headerMap);
    }

    /**
     * 构建二级表头，添加到动态表头中
     * @param fieldMap 传入数据的字段信息，在本方法内只用于获取父字段
     * @param field 当前处理的字段
     * @param headerMap 包含所有字段的动态表头映射关系
     */
    private void buildDoubleLevelHeaderMap(Map<String, Field> fieldMap, Field field, Map<String, FixedTableHeader> headerMap) {
        String fieldName = field.getName();
        FixedColumn annotation = field.getAnnotation(FixedColumn.class);
        FixedTableHeader fixedTableHeader = new FixedTableHeader(fieldName, annotation.name(), annotation.index());

        String parentProp = annotation.parentProp();
        Field parentField = fieldMap.get(parentProp);
        FixedColumn parentColumnAnnotation = parentField.getAnnotation(FixedColumn.class);
        if (headerMap.containsKey(parentProp)) {
            if (CollUtil.isNotEmpty(headerMap.get(parentProp).getChildren())) {
                headerMap.get(parentProp).getChildren().add(fixedTableHeader);
            } else {
                List<FixedTableHeader> subHeaderList = new ArrayList<>();
                subHeaderList.add(fixedTableHeader);
                headerMap.get(parentProp).setChildren(subHeaderList);
            }
        } else {
            List<FixedTableHeader> subHeaderList = new ArrayList<>();
            subHeaderList.add(fixedTableHeader);
            FixedTableHeader parentHeader = new FixedTableHeader(parentProp, parentColumnAnnotation.name(), parentColumnAnnotation.index());
            parentHeader.setChildren(subHeaderList);
            headerMap.put(parentProp, parentHeader);
        }
    }

    /**
     * 对动态表头按指定的索引排序，属于同一父字段的多个子字段之间排序，和父字段平级的字段之间排序
     * @param headerMap 包含所有字段的动态表头映射关系
     * @return 返回排序后的动态表头列表
     */
    private List<FixedTableHeader> sortHeaderByIndex(Map<String, FixedTableHeader> headerMap) {
        for (Map.Entry<String, FixedTableHeader> entry : headerMap.entrySet()) {
            if (CollUtil.isEmpty(entry.getValue().getChildren())) {
                continue;
            }
            entry.getValue().getChildren().sort(Comparator.comparing(FixedTableHeader::getIndex));
        }

        List<FixedTableHeader> headerList = new ArrayList<>(headerMap.values());
        headerList.sort(Comparator.comparing(FixedTableHeader::getIndex));
        return headerList;
    }

    private List<String> outputHeader(List<FixedTableHeader> headerList, List<String> headerPropList) {
        List<String> row = new ArrayList<>();
        for (FixedTableHeader header : headerList) {
            if (CollUtil.isEmpty(header.getChildren())) {
                row.add(header.getName());
                headerPropList.add(header.getProp());
            } else {
                for (FixedTableHeader child : header.getChildren()) {
                    row.add(child.getName());
                    headerPropList.add(child.getProp());
                }
            }
        }
        return row;
    }

    /**
     * 把表头和数据内容输出成双层列表格式
     *
     * @param tableName      表名
     * @param dataList       要被导出的数据
     * @param headerPropList 作为父字段的表头
     * @param <T>            被导出数据的类型，字段上要用注解{@code FixedColumn}配置所在列的表头信息
     * @return 返回双层列表格式的数据
     * @throws IllegalAccessException    通过反射获取导出元素上数据时可能会抛出非法访问异常
     * @throws InvocationTargetException 通过反射获取导出元素上数据时可能会抛出对象访问异常
     */
    private <T> List<List<String>> outputDataRow(String tableName, List<T> dataList, List<String> headerPropList) throws InvocationTargetException, IllegalAccessException {
        List<TableMetaInfo> tableFieldList = tableMetaInfoMapper.selectFieldList(tableName);
        // 建立字段名和数据类型的映射关系，字段名转换成驼峰命名风格的
        Map<String, String> columnNameAndDataTypeMap = tableFieldList.stream().collect(Collectors.toMap(v -> CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, v.getColumnName()), TableMetaInfo::getDataType));

        Method[] methods = dataList.get(0).getClass().getMethods();

        List<List<String>> rows = new ArrayList<>(dataList.size() + 2);
        List<String> row = null;

        Map<String, Method> getterMethodMap = Arrays.stream(methods).filter(v -> v.getName().startsWith("get"))
                .collect(Collectors.toMap(v -> CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, v.getName().replace("get", "")), Function.identity()));
        Method getterMethod = null;
        for (T dataRecord : dataList) {
            row = new ArrayList<>();

            if (dataRecord == null) {
                continue;
            }
            for (String headerProp : headerPropList) {
                getterMethod = getterMethodMap.get(headerProp);
                if (getterMethod != null) {
                    row.add(convertToCellValue(Optional.ofNullable(getterMethod.invoke(dataRecord)).orElse(StringPool.EMPTY), columnNameAndDataTypeMap.get(headerProp)));
                } else {
                    row.addAll((List<String>) dataRecord);
                }
            }
            rows.add(row);
        }
        return rows;
    }

    /**
     * 把要导出的数据转换成合适的格式，目前只有日期和时间类型的要专门处理
     *
     * @param value      要导出的原始值
     * @param dbDataType 要导出数据对应字段的数据类型
     * @return 以字符串形式返回转换后的值
     */
    private String convertToCellValue(Object value, String dbDataType) {
        if (Strings.isEmpty(dbDataType)) {
            return value.toString();
        }
        if ("timestamp".equals(dbDataType) || "datetime".equals(dbDataType) || "date".equals(dbDataType)) {
            return ExcelConstant.DB_DATE_TIME_FORMAT_MAP.get(dbDataType).format((Date) value);
        }

        return value.toString();
    }

    /**
     * 如果有两级表头，合并单元格
     *
     * @param headerList 动态表头列表
     * @param writer     excel文件写对象
     */
    private void mergeHeaderCell(List<FixedTableHeader> headerList, ExcelWriter writer) {
        boolean isSingleLevelHeader = headerList.stream().allMatch(v -> CollUtil.isEmpty(v.getChildren()));
        if (isSingleLevelHeader) {
            return;
        }

        int firstRow = 0;
        int lastRow = 0;
        int firstColumn = 0;
        int lastColumn = 0;
        for (FixedTableHeader header : headerList) {
            if (CollUtil.isEmpty(header.getChildren())) {
                lastRow = 1;
                if (firstColumn > 0) {
                    lastColumn = firstColumn;
                }
            } else {
                lastRow = 0;
                lastColumn = firstColumn + header.getChildren().size() - 1;
            }
            writer.merge(firstRow, lastRow, firstColumn, lastColumn, header.getName(), true);
            firstColumn = lastColumn + 1;
        }
        writer.passCurrentRow();
    }
}
