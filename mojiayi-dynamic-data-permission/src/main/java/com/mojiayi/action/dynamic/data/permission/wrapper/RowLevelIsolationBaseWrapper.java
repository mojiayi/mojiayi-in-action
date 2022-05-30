package com.mojiayi.action.dynamic.data.permission.wrapper;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.enums.SqlKeyword;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mojiayi.action.dynamic.data.permission.annotations.TableAlias;
import com.mojiayi.action.dynamic.data.permission.beans.DataPermissionConfig;
import com.mojiayi.action.dynamic.data.permission.constants.MyConstant;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 行级数据隔离 SQL 组装工具的基类。<br/>
 * 详细设计方案请查看 <a href="https://rv5ng7gbiu.feishu.cn/docs/doccnEDq4Gr2TQSLfMnsMim3sWg#">IDEAS动态行级数据隔离方案</a>。
 * </p>
 *
 * @author mojiayi
 */
public class RowLevelIsolationBaseWrapper {
    /**
     * 主键字段
     */
    protected static final String PRIMARY_ID = "id";

    /**
     * 不需要参与数据隔离的字段
     */
    protected static final List<String> USELESS_FIELDS = new ArrayList<>(2);

    static {
        USELESS_FIELDS.add("class");
        USELESS_FIELDS.add("deleteFlag");
    }

    /**
     * 判断是不是不参与数据隔离的字段
     *
     * @param fieldName 被判断的字段名
     * @return 是不参与数据隔离的字段返回true，否则返回false
     */
    protected boolean isUselessField(String fieldName) {
        return USELESS_FIELDS.contains(fieldName);
    }

    /**
     * 判断是否在变量上加了注解 @TableField，而且 exist = false
     *
     * @param field 变量
     * @return 有注解 @TableField ，而且 exist = false 时，返回 true，否则默认返回 false
     */
    protected boolean isNotExistField(Field field) {
        boolean exist = true;
        if (field.isAnnotationPresent(TableField.class)) {
            TableField tableField = field.getAnnotation(TableField.class);
            exist = tableField.exist();
        }
        return !exist;
    }

    /**
     * 获取变量在数据库表中的字段名，优先使用通过注解{@code @TableField}配置的字段名，如果有配置表别名，还要加上表别名
     *
     * @param field 变量
     * @return 返回变量在数据库表中的字段名
     */
    protected String getTableFieldName(Field field) {
        TableField tableField = field.getAnnotation(TableField.class);
        String fieldName = null;
        if (tableField != null) {
            // 优先使用通过注解 @TableField 配置的字段名
            if (!tableField.exist()) {
                // 如果设置了 exist == false，这个字段不需要处理
                return null;
            } else {
                fieldName = tableField.value();
            }
        }
        if (StringUtils.isEmpty(fieldName)) {
            // 注解 @TableField 配置的字段名是空字符串时，把变量名转换成下划线形式，作为字段名
            fieldName = CharSequenceUtil.toUnderlineCase(field.getName());
        }
        TableAlias tableAlias = field.getAnnotation(TableAlias.class);
        if (tableAlias != null && StringUtils.isNotEmpty(tableAlias.alias())) {
            // 注解 @TableAlias 配置了表别名时，在字段名前面加上表别名
            return tableAlias.alias() + "." + fieldName;
        }
        return fieldName;
    }

    /**
     * 获取指定类中的变量，包括父类的。
     *
     * @param clazz 类型
     * @return 以列表形式返回指定类中的变量
     */
    protected List<Field> getClassFields(Class<?> clazz) {
        List<Field> fieldList = new ArrayList<>();
        do {
            Collections.addAll(fieldList, clazz.getDeclaredFields());
            clazz = clazz.getSuperclass();
        } while (clazz != null && clazz != Object.class);
        return fieldList;
    }

    /**
     * 获取当前用户拥有角色所配置的数据属性
     */
    private List<DataPermissionConfig> getAllIsolationFields() {
        // 模拟自测时，可以不从本地线程空间获取，直接用 mockIsolationFields 临时提供数据属性配置
        String dataPermissionStr = MDC.get(MyConstant.DATA_PERMISSION);
        if (StringUtils.isBlank(dataPermissionStr)) {
            // 上游没有传递用户数据权限配置
            return Collections.emptyList();
        }
        Gson gson = new Gson();
        List<DataPermissionConfig> dataPermissionFieldList = gson.fromJson(dataPermissionStr, new TypeToken<ArrayList<DataPermissionConfig>>(){}.getType());
        dataPermissionFieldList.forEach(v -> v.setSqlKeyword(SqlKeyword.valueOf(v.getExpression())));
        return dataPermissionFieldList;
    }

    /**
     * 上游没有完成开发时，可以临时模拟自测
     * @return 返回模拟的数据属性
     */
    private List<DataPermissionConfig> mockIsolationFields() {
        List<DataPermissionConfig> dataPermissionFieldList = new ArrayList<>();

        DataPermissionConfig countryCodeConfig = new DataPermissionConfig();
        countryCodeConfig.setColumn("country_code");
        countryCodeConfig.setExpression("EQ");
        countryCodeConfig.setSqlKeyword(SqlKeyword.valueOf("EQ"));
        countryCodeConfig.setValue("China");
        dataPermissionFieldList.add(countryCodeConfig);

        DataPermissionConfig cityCodeConfig = new DataPermissionConfig();
        cityCodeConfig.setColumn("city_code");
        cityCodeConfig.setExpression("LIKE");
        cityCodeConfig.setSqlKeyword(SqlKeyword.valueOf("LIKE"));
        cityCodeConfig.setValue("Chang");
        dataPermissionFieldList.add(cityCodeConfig);

        return dataPermissionFieldList;
    }

    /**
     * 把从角色数据权限表获得的数据属性与传入参数对象的变量取交集，作为本次更新可用的数据隔离字段
     * @param classFields 传入参数对象的变量
     * @return 返回传入数据的交集
     */
    protected List<DataPermissionConfig> getAvailableIsolationFields(List<Field> classFields) {
        if (CollUtil.isEmpty(classFields)) {
            return Collections.emptyList();
        }
        List<DataPermissionConfig> allIsolationFields = getAllIsolationFields();
        if (CollUtil.isEmpty(allIsolationFields)) {
            return Collections.emptyList();
        }
        List<String> fieldNames = new ArrayList<>(classFields.size());
        // 配置了注解 @TableAlias 的，临时存储字段名和所属表别名映射关系，后面拼接到可用的数据隔离字段名上
        Map<String, String> tableAliasMap = new HashMap<>(classFields.size());
        for (Field field : classFields) {
            TableField tableField = field.getAnnotation(TableField.class);
            String fieldName = null;
            if (tableField != null) {
                fieldName = tableField.value();
            }
            if (StringUtils.isEmpty(fieldName)) {
                fieldName = CharSequenceUtil.toUnderlineCase(field.getName());
            }
            fieldNames.add(fieldName);
            TableAlias tableAlias = field.getAnnotation(TableAlias.class);
            if (tableAlias == null || StringUtils.isEmpty(tableAlias.alias())) {
                continue;
            }
            tableAliasMap.put(fieldName, tableAlias.alias());
        }
        // 把从角色数据权限表获得的数据属性与传入参数对象的变量取交集
        List<DataPermissionConfig> availableIsolationFields = allIsolationFields.stream().filter(v -> fieldNames.contains(v.getColumn())).collect(Collectors.toList());
        // 如果变量用注解 @TableAlias 标记了表别名，把表别名和字段名拼接起来，用于最终拼装SQL
        String fieldName = null;
        for (DataPermissionConfig dataPermissionField : availableIsolationFields) {
            fieldName = dataPermissionField.getColumn();
            if (tableAliasMap.containsKey(fieldName)) {
                dataPermissionField.setColumn(tableAliasMap.get(fieldName) + "." + fieldName);
            }
        }
        return availableIsolationFields;
    }
}
