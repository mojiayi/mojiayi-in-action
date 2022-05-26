package com.mojiayi.action.dynamic.data.permission.wrapper;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlKeyword;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.mojiayi.action.dynamic.data.permission.annotations.ConditionExpression;
import com.mojiayi.action.dynamic.data.permission.beans.DataPermissionConfig;
import com.mojiayi.action.dynamic.data.permission.constants.MyConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 行级数据隔离查询 SQL 组装工具，只有需要数据隔离的SQL才使用本工具包装。<br/>
 * 使用说明：<br/>
 * 1. 支持单表查询、删除和更新；<br/>
 * 2. 支持联表查询、删除和更新，但是一定要在传入参数对象字段上用注解{@code @TableAlias}指定表别名，且与原始SQL中的表别名保持一致；<br/>
 * 3. 不支持在子查询SQL的子句中拼接数据隔离的where条件，只能拼接到外层主表的where条件中。
 * </p>
 *
 * @author mojiayi
 */
@Slf4j
public class RowLevelQueryIsolationWrapper extends RowLevelIsolationBaseWrapper {
    /**
     * 组装Mybatis Plus 查询条件
     *
     * @param searchObj 查询条件对象
     * @param <T>       查询参数对象
     * @return 返回组装好的查询条件
     */
    public static <T> QueryWrapper<T> initQueryWrapper(T searchObj) {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        installMplus(queryWrapper, searchObj);
        return queryWrapper;
    }

    /**
     * 组装Mybatis Plus 查询条件
     *
     * @param queryWrapper Entity 对象封装操作类
     * @param searchObj    查询条件对象
     */
    private static void installMplus(QueryWrapper<?> queryWrapper, Object searchObj) {
        // 利用反射机制获取类中的属性
        List<Field> classFields = getClassFields(searchObj.getClass());
        // 根据业务接口传入的普通业务字段，拼装where条件
        wrapperNormalBusinessCondition(queryWrapper, searchObj, classFields);
        // 拼装行级数据隔离专用的where条件
        wrapperIsolationCondition(queryWrapper, classFields);
    }

    /**
     * 根据业务接口传入的普通业务字段，拼装where条件
     *
     * @param queryWrapper Entity 对象封装操作类
     * @param searchObj    查询条件对象
     * @param classFields  查询条件对象的属性
     */
    private static void wrapperNormalBusinessCondition(QueryWrapper<?> queryWrapper, Object searchObj, List<Field> classFields) {
        for (Field field : classFields) {
            try {
                // 如果通过注解标记字段不存在，跳过该字段
                // 如果字段在无用字段中，跳过该字段
                // 如果字段不可读取数值，跳过该字段
                if (isNotExistField(field) || isUselessField(field.getName()) || !PropertyUtils.isReadable(searchObj, field.getName())) {
                    continue;
                }
                field.setAccessible(true);
                String fieldName = getTableFieldName(field);
                Object value = field.get(searchObj);
                // 如果字段名不存在，或者字段值为空，跳过该字段
                if (fieldName == null || value == null || CharSequenceUtil.isEmpty(value.toString())) {
                    continue;
                }
                // 判断是否含有@ConditionExpression注解
                // 如果有，则进行注解类型的相关查询
                // 如果没有，默认进行精确匹配
                if (field.isAnnotationPresent(ConditionExpression.class)) {
                    addQueryCondition(queryWrapper, fieldName, field.getAnnotation(ConditionExpression.class).type(), value);
                } else {
                    queryWrapper.eq(fieldName, value);
                }
            } catch (Exception e) {
                log.warn("拼装查询SQL where 子句失败", e);
            }
        }
    }

    /**
     * 拼装行级数据隔离专用的where条件
     *
     * @param queryWrapper Entity 对象封装操作类
     * @param classFields  查询条件对象的属性
     */
    private static void wrapperIsolationCondition(QueryWrapper<?> queryWrapper, List<Field> classFields) {
        // 获得本次查询可用的数据隔离字段
        List<DataPermissionConfig> isolationFields = getAvailableIsolationFields(classFields);
        if (CollUtil.isEmpty(isolationFields)) {
            return;
        }
        for (DataPermissionConfig field : isolationFields) {
            addQueryCondition(queryWrapper, field.getColumn(), field.getSqlKeyword(), field.getValue());
        }
    }

    /**
     * 拼接查询条件
     *
     * @param queryWrapper Entity 对象封装操作类
     * @param column       被拼接字段名
     * @param sqlKeyword   拼接条件类型
     * @param value        被拼接字段值
     */
    private static void addQueryCondition(QueryWrapper<?> queryWrapper, String column, SqlKeyword sqlKeyword, Object value) {
        switch (sqlKeyword) {
            case LIKE: {
                queryWrapper.like(column, value);
                break;
            }
            case NOT_LIKE: {
                queryWrapper.notLike(column, value);
                break;
            }
            case IN: {
                if (value instanceof String) {
                    queryWrapper.in(column, Arrays.asList(value.toString().split(StringPool.COMMA)));
                } else if (value.getClass().isArray()) {
                    queryWrapper.in(column, Arrays.asList((Object[]) value));
                } else {
                    queryWrapper.in(column, Collections.singleton(value));
                }
                break;
            }
            case NOT_IN: {
                if (value instanceof String) {
                    queryWrapper.notIn(column, Arrays.asList(value.toString().split(StringPool.COMMA)));
                } else if (value.getClass().isArray()) {
                    queryWrapper.notIn(column, Arrays.asList((Object[]) value));
                } else {
                    queryWrapper.notIn(column, Collections.singleton(value));
                }
                break;
            }
            case GT: {
                queryWrapper.gt(column, value);
                break;
            }
            case GE: {
                queryWrapper.ge(column, value);
                break;
            }
            case LT: {
                queryWrapper.lt(column, value);
                break;
            }
            case LE: {
                queryWrapper.le(column, value);
                break;
            }
            default: {
                queryWrapper.eq(column, value);
                break;
            }
        }
    }
}
