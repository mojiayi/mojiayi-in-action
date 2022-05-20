package com.mojiayi.action.dynamic.data.permission.wrapper;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.enums.SqlKeyword;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.mojiayi.action.dynamic.data.permission.annotations.ConditionExpression;
import com.mojiayi.action.dynamic.data.permission.beans.DataPermissionConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtils;

import java.lang.reflect.Field;
import java.util.List;

/**
 * <p>
 * 行级数据隔离更新 SQL 组装工具，只有需要数据隔离的SQL才使用本工具包装，只支持单表操作。
 * </p>
 *
 * @author mojiayi
 */
@Slf4j
public class RowLevelUpdateIsolationWrapper extends RowLevelIsolationBaseWrapper {
    /**
     * 组装Mybatis Plus 更新条件
     *
     * @param updateObj 更新条件对象
     * @param <T>       更新参数对象
     * @return 返回组装好的更新条件
     */
    public static <T> UpdateWrapper<T> initUpdateWrapper(T updateObj) {
        UpdateWrapper<T> updateWrapper = new UpdateWrapper<>();
        installMplus(updateWrapper, updateObj);
        return updateWrapper;
    }

    /**
     * 组装Mybatis Plus 更新条件
     *
     * @param updateWrapper Entity 对象封装操作类
     * @param updateObject  更新条件对象
     */
    private static void installMplus(UpdateWrapper<?> updateWrapper, Object updateObject) {
        // 利用反射机制获取类中的属性
        List<Field> classFields = getClassFields(updateObject.getClass());
        // 根据业务接口传入的普通业务字段，拼装where条件
        wrapperNormalBusinessCondition(updateWrapper, updateObject, classFields);
        // 拼装行级数据隔离专用的where条件
        wrapperIsolationCondition(updateWrapper, classFields);
    }

    /**
     * 根据业务接口传入的普通业务字段，拼装where条件
     *
     * @param updateWrapper Entity 对象封装操作类
     * @param searchObj     更新条件对象
     * @param classFields   更新条件对象的属性
     */
    private static void wrapperNormalBusinessCondition(UpdateWrapper<?> updateWrapper, Object searchObj, List<Field> classFields) {
        for (Field field : classFields) {
            try {
                String fieldName = field.getName();
                // 如果通过注解标记字段不存在，跳过该字段
                // 如果字段在无用字段中，跳过该字段
                // 如果字段不可读取数值，跳过该字段
                if (isNotExistField(field) || isUselessField(fieldName) || !PropertyUtils.isReadable(searchObj, fieldName)) {
                    continue;
                }
                field.setAccessible(true);
                String column = getTableFieldName(field);
                Object value = field.get(searchObj);
                // 如果字段名不存在，或者字段值为空，跳过该字段
                if (column == null || value == null || CharSequenceUtil.isEmpty(value.toString())) {
                    continue;
                }
                // 判断是否含有@ConditionExpression注解
                // 如果有，则进行注解类型的相关查询
                // 如果没有，默认进行精确匹配
                if (field.isAnnotationPresent(ConditionExpression.class)) {
                    addUpdateConditUpion(updateWrapper, column, field.getAnnotation(ConditionExpression.class).type(), value);
                } else {
                    updateWrapper.eq(column, value);
                }
            } catch (Exception e) {
                log.warn("拼装更新SQL where 子句失败", e);
            }
        }
    }

    /**
     * 拼装行级数据隔离专用的where条件
     *
     * @param updateWrapper Entity 对象封装操作类
     * @param classFields   更新条件对象的属性
     */
    private static void wrapperIsolationCondition(UpdateWrapper<?> updateWrapper, List<Field> classFields) {
        // 获得本次更新可用的数据隔离字段
        List<DataPermissionConfig> isolationFields = getAvailableIsolationFields(classFields);
        if (CollUtil.isEmpty(isolationFields)) {
            return;
        }
        for (DataPermissionConfig field : isolationFields) {
            addUpdateConditUpion(updateWrapper, field.getColumn(), field.getSqlKeyword(), field.getValue());
        }
    }

    /**
     * 拼接更新条件
     *
     * @param updateWrapper Entity 对象封装操作类
     * @param column        被拼接字段名
     * @param sqlKeyword    拼接条件类型
     * @param value         被拼接字段值
     */
    private static void addUpdateConditUpion(UpdateWrapper<?> updateWrapper, String column, SqlKeyword sqlKeyword, Object value) {
        switch (sqlKeyword) {
            case LIKE: {
                updateWrapper.like(column, value);
                break;
            }
            case NOT_LIKE: {
                updateWrapper.notLike(column, value);
                break;
            }
            case IN: {
                if (value instanceof String) {
                    updateWrapper.in(column, (Object) value.toString().split(StringPool.COMMA));
                } else if (value.getClass().isArray()) {
                    updateWrapper.in(column, (Object[]) value);
                } else {
                    updateWrapper.in(column, value);
                }
                break;
            }
            case NOT_IN: {
                if (value instanceof String) {
                    updateWrapper.notIn(column, (Object) value.toString().split(StringPool.COMMA));
                } else if (value.getClass().isArray()) {
                    updateWrapper.notIn(column, (Object[]) value);
                } else {
                    updateWrapper.notIn(column, value);
                }
                break;
            }
            case GT: {
                updateWrapper.gt(column, value);
                break;
            }
            case GE: {
                updateWrapper.ge(column, value);
                break;
            }
            case LT: {
                updateWrapper.lt(column, value);
                break;
            }
            case LE: {
                updateWrapper.le(column, value);
                break;
            }
            default: {
                updateWrapper.eq(column, value);
                break;
            }
        }
    }
}
