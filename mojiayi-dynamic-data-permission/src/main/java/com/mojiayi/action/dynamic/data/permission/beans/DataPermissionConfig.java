package com.mojiayi.action.dynamic.data.permission.beans;

import com.baomidou.mybatisplus.core.enums.SqlKeyword;
import lombok.Data;

/**
 * <p>
 * 用户数据权限配置
 * </p>
 *
 * @author mojiayi
 */
@Data
public class DataPermissionConfig {
    /**
     * 数据权限字段
     */
    private String column;

    /**
     * 数据权限表达式，比如 EQ、LIKE、IN
     */
    private String expression;

    /**
     * 数据权限的值，多个数据用逗号分割
     */
    private String value;

    /**
     * 字符串类型的 expression 转换成 {@code SqlKeyword} 类型
     */
    private SqlKeyword sqlKeyword;
}
