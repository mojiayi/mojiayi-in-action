package com.mojiayi.action.dynamic.data.permission.annotations;

import com.baomidou.mybatisplus.core.enums.SqlKeyword;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * 用于配置条件表达式
 * </p>
 *
 * @author mojiayi
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ConditionExpression {
    @AliasFor("type")
    SqlKeyword value() default SqlKeyword.EQ;

    @AliasFor("value")
    SqlKeyword type() default SqlKeyword.EQ;
}
