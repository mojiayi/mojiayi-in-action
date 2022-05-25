package com.mojiayi.action.gateway.auth;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * <p>
 * 登录认证、鉴权和设置必要的当前用户信息到请求头
 * </p>
 *
 * @author guangri.li
 * @since 2022/5/25 21:45
 */
@Component
public class AuthorizationFilter implements Ordered, GlobalFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpRequest.Builder mutate = request.mutate();
        // TODO 这里应该写一些登录认证和授权的代码

        // TODO 设置用户数据权限信息到请求头

        return chain.filter(exchange.mutate().request(mutate.build()).build());
    }

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE + 1;
    }
}
