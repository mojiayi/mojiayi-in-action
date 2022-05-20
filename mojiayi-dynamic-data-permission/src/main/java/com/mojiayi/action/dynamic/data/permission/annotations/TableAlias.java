package com.mojiayi.action.dynamic.data.permission.annotations;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * 用于配置字段所属的表别名
 * </p>
 *
 * @author mojiayi
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface TableAlias {
    @AliasFor("alias")
    String value() default "";

    @AliasFor("value")
    String alias() default "";
}
