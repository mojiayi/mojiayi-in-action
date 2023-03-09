package com.mojiayi.action.common.tool.dataexport;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
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
    /**
     * 把传入的数据导出到excel表格，表头信息通过传入数据各个字段上的注解指定，支持单级表头和两级表头
     * @param dataList 要被导出的数据
     * @return 拼装好的excel文件写对象
     * @param <T> 被导出数据的类型，字段上要用注解{@code FixedColumn}配置所在列的表头信息
     * @throws IllegalAccessException 通过反射获取导出元素上数据时可能会抛出非法访问异常
     * @throws InvocationTargetException 通过反射获取导出元素上数据时可能会抛出对象访问异常
     */
    public <T> ExcelWriter exportExcel(List<T> dataList) throws IllegalAccessException, InvocationTargetException {
        if (CollUtil.isEmpty(dataList)) {
            log.info("传入数据为空不需要导出表格");
            return null;
        }
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

        List<List<String>> rows = outputHeaderAndDataRow(dataList, headerList);

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

    /**
     * 把表头和数据内容输出成双层列表格式
     * @param dataList 要被导出的数据
     * @param headerList 动态表头列表
     * @return 返回双层列表格式的数据
     * @param <T> 被导出数据的类型，字段上要用注解{@code FixedColumn}配置所在列的表头信息
     * @throws IllegalAccessException 通过反射获取导出元素上数据时可能会抛出非法访问异常
     * @throws InvocationTargetException 通过反射获取导出元素上数据时可能会抛出对象访问异常
     */
    private <T> List<List<String>> outputHeaderAndDataRow(List<T> dataList, List<FixedTableHeader> headerList) throws InvocationTargetException, IllegalAccessException {
        List<String> row = new ArrayList<>();
        List<String> headerPropList = new ArrayList<>();
        for (FixedTableHeader header : headerList) {
            if (CollUtil.isEmpty(header.getChildren())) {
                row.add(header.getName());
                headerPropList.add(header.getProp().toLowerCase());
            } else {
                for (FixedTableHeader child : header.getChildren()) {
                    row.add(child.getName());
                    headerPropList.add(child.getProp().toLowerCase());
                }
            }
        }

        Method[] methods = dataList.get(0).getClass().getMethods();

        List<List<String>> rows = new ArrayList<>(dataList.size() + 2);
        rows.add(row);

        for (T dataRecord : dataList) {
            row = new ArrayList<>();

            for (Method method : methods) {
                if (headerPropList.contains(method.getName().replace("get", "").toLowerCase())) {
                    row.add(method.invoke(dataRecord).toString());
                }
            }
            rows.add(row);
        }
        return rows;
    }

    /**
     * 如果有两级表头，合并单元格
     * @param headerList 动态表头列表
     * @param writer excel文件写对象
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
