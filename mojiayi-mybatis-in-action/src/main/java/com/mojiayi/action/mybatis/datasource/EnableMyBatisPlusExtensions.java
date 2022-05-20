package com.mojiayi.action.mybatis.datasource;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.mojiayi.action.mybatis.constants.MyConstants;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.NullValue;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 启用多租户插件
 * </p>
 *
 * @author mojiayi
 */
@Configuration
@MapperScan("com.mojiayi.action.dynamic.data.permission.dao")
public class EnableMyBatisPlusExtensions {
    /**
     * 启用多租户插件
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new TenantLineInnerInterceptor(() -> {
            String tenantId = MDC.get(MyConstants.TENANT_ID);
            if (!StringUtils.hasLength(tenantId)) {
                return new NullValue();
            }
            return new LongValue(tenantId);
        }));
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }
}
