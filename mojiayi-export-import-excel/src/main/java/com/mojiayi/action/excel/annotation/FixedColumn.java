package com.mojiayi.action.excel.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * 配置列数固定的表格中每列的名称和属性，导出或在页面上展示表格内容时，根据配置的名称和属性把表头和数据内容建立映射关系
 * </p>
 *
 * @author mojiayi
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FixedColumn {
    /**
     * 列的名称，强烈建议手工设置业务识别度高的名称
     *
     * @return 返回列的名称
     */
    String name() default "";

    /**
     * 表格里列从左到右的显示顺序，强烈建议每个列的顺序值不要相同
     *
     * @return 返回列的显示顺序
     */
    int index() default 0;

    /**
     * 两级表头中的父字段属性值，实际上是父字段变量名
     *
     * @return 返回当前列的父列属性值
     */
    String parentProp() default "";

    /**
     * 是否必填字段，主要用在导入数据时的校验逻辑
     *
     * @return 是否必填字段
     */
    boolean required() default true;

    /**
     * 指定数据的正则表达式，主要用在导入数据时的校验逻辑
     *
     * @return 指定数据的正则表达式
     */
    String pattern() default "";

    /**
     * 是否唯一键的组成字段，主要用在导入数据时的校验逻辑
     *
     * @return 是否唯一键的组成字段
     */
    boolean uniqueKeyFlag() default false;
}
