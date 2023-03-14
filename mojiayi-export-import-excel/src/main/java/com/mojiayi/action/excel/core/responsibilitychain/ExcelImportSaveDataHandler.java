package com.mojiayi.action.excel.core.responsibilitychain;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.google.common.base.CaseFormat;
import com.mojiayi.action.excel.constant.ExcelConstant;
import com.mojiayi.action.excel.dto.FieldNameValue;
import com.mojiayi.action.excel.mapper.TableMetaInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 保存数据入库
 * </p>
 *
 * @author mojiayi
 */
@Slf4j
@Component
@Order(9999)
public class ExcelImportSaveDataHandler extends ExcelImportResponsibilityChainAbstractHandler {
    @Resource
    public TableMetaInfoMapper tableMetaInfoMapper;

    /**
     * 保存数据入库
     *
     * @param tableName      表名
     * @param headerNameList 导入文件中的表头
     * @param dataRowList    导入文件中的业务数据
     * @param response       HTTP请求的servlet返回对象
     * @return 返回导入操作结果
     */
    @Override
    String doFilter(String tableName, List<String> headerNameList, List<List<String>> dataRowList, HttpServletResponse response) {
        log.info("开始保存数据入库,tableName={}", tableName);
        if (CollUtil.isEmpty(dataRowList)) {
            String errMsg = String.format("表%s的导入文件中没有业务数据", tableName);
            log.warn(errMsg);
            return errMsg;
        }

        Class<?> dataClass = extBasicDataClassMap.get(tableName);

        Field[] fields = dataClass.getDeclaredFields();
        Map<String, Class<?>> fieldDataTypeMap = new HashMap<>(fields.length);
        for (Field field : fields) {
            fieldDataTypeMap.put(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, field.getName()), field.getType());
        }

        List<String> uniqueKeyFieldNameList = getUniqueKeyFieldList(tableName, dataClass);
        if (CollUtil.isEmpty(uniqueKeyFieldNameList)) {
            String errMsg = String.format("表%s没有定义唯一键", tableName);
            log.warn(errMsg);
            return errMsg;
        }

        uniqueKeyFieldNameList = uniqueKeyFieldNameList.stream().map(v -> CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, v)).collect(Collectors.toList());

        saveImportData(tableName, headerNameList, dataRowList, uniqueKeyFieldNameList, fieldDataTypeMap);

        log.info("完成保存数据入库,tableName={}", tableName);
        return null;
    }

    /**
     * @param tableName              表名
     * @param headerNameList         导入文件中的表头
     * @param dataRowList            导入文件中的业务数据
     * @param uniqueKeyFieldNameList 组成唯一索引的字段
     * @param fieldDataTypeMap       domain类中的字段和字段数据类型
     */
    private void saveImportData(String tableName, List<String> headerNameList, List<List<String>> dataRowList,
                                List<String> uniqueKeyFieldNameList, Map<String, Class<?>> fieldDataTypeMap) {
        Map<String, String> fieldNameAndPropMap = getFieldNameAndPropMap(tableName);
        String filedProp = null;
        long operatorUid = Long.parseLong(Optional.ofNullable(MDC.get(ExcelConstant.USER_ID)).orElse("99999"));
        Date updateTime = new Date();
        List<FieldNameValue> fieldNameValueList = new ArrayList<>(headerNameList.size() + 2);
        String columnName = null;
        for (List<String> oneRowData : dataRowList) {
            for (int index = 0; index < headerNameList.size(); index++) {
                filedProp = fieldNameAndPropMap.get(headerNameList.get(index));

                columnName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, filedProp);
                fieldNameValueList.add(new FieldNameValue(columnName, formatValue(oneRowData.get(index), fieldDataTypeMap.get(columnName))));
            }
            fieldNameValueList.add(new FieldNameValue("create_by", operatorUid));
            fieldNameValueList.add(new FieldNameValue("update_by", operatorUid));

            List<FieldNameValue> uniqueKeyNameValueList = fieldNameValueList.stream().filter(v -> uniqueKeyFieldNameList.contains(v.getName())).collect(Collectors.toList());
            // 根据唯一键查询已存在的数据，如果存在就逻辑删除
            List<Long> duplicateRecordIdList = tableMetaInfoMapper.selectByUniqueKey(tableName, uniqueKeyNameValueList);
            if (CollUtil.isNotEmpty(duplicateRecordIdList)) {
                duplicateRecordIdList.forEach(id -> tableMetaInfoMapper.logicDeleteData(tableName, id, operatorUid, updateTime));
            }

            tableMetaInfoMapper.insertNewData(tableName, fieldNameValueList);

            fieldNameValueList.clear();
        }
    }

    /**
     * 把单元格数据转换成domain类相应字段的数据类型的值
     *
     * @param source   单元格原始值
     * @param dataType domain类相应字段的数据类型
     * @return 返回domain类相应字段的数据类型的值
     */
    private static Object formatValue(Object source, Class<?> dataType) {
        String sourceStr = (String) source;
        if (dataType.isAssignableFrom(Date.class)) {
            // 目标字段是日期
            if (sourceStr.matches("^\\d{4}-\\d{1,2}$")) {
                return DateUtil.parse(sourceStr, ExcelConstant.DATETIME_PATTERN_MAP.get(DatePattern.NORM_MONTH_PATTERN));
            } else if (sourceStr.matches("^\\d{4}-\\d{1,2}-\\d{1,2}$")) {
                return DateUtil.parse(sourceStr, ExcelConstant.DATETIME_PATTERN_MAP.get(DatePattern.NORM_DATE_PATTERN));
            } else if (sourceStr.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}$")) {
                return DateUtil.parse(sourceStr, ExcelConstant.DATETIME_PATTERN_MAP.get(DatePattern.NORM_DATETIME_MINUTE_PATTERN));
            } else if (sourceStr.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}:\\d{1,2}$")) {
                return DateUtil.parse(sourceStr, ExcelConstant.DATETIME_PATTERN_MAP.get(DatePattern.NORM_DATETIME_PATTERN));
            }
        }
        if (dataType.isAssignableFrom(Boolean.class)) {
            // 目标字段是布尔值
            return Boolean.valueOf(sourceStr);
        }
        // 目标字段是整数
        if (dataType.isAssignableFrom(Integer.class)) {
            return Integer.valueOf(sourceStr);
        }
        if (dataType.isAssignableFrom(Long.class)) {
            return Long.valueOf(sourceStr);
        }
        if (dataType.isAssignableFrom(Byte.class)) {
            return Byte.valueOf(sourceStr);
        }
        if (dataType.isAssignableFrom(Float.class)) {
            return Float.valueOf(sourceStr);
        }
        // 目标字段是浮点数
        if (dataType.isAssignableFrom(Double.class)) {
            return Double.valueOf(sourceStr);
        }
        if (dataType.isAssignableFrom(BigDecimal.class)) {
            return new BigDecimal(sourceStr);
        }
        // 以上类型都不是，默认为普通字符串 String
        return sourceStr;
    }
}
