package com.mojiayi.action.gateway.log;

import com.mojiayi.action.cloud.constant.MyConstant;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * <p>
 * 为每个请求生成全局唯一的traceId，放到request header中，执行顺序指定为 Integer.MIN_VALUE，在所有过滤器的第1个执行
 * </p>
 *
 * @author guangri.li
 * @since 2022/5/25 21:47
 */
@Component
public class TraceIdFilter implements Ordered, GlobalFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        var traceId = MDC.get(MyConstant.TRACE_ID);
        if (StringUtils.isBlank(traceId)) {
            traceId = getTraceId();
            MDC.put(MyConstant.TRACE_ID, traceId);
        }
        ServerHttpRequest request = exchange.getRequest();
        Map<String, String> headerMap = new HashMap<>(request.getHeaders().size() + 1);
        headerMap.putAll(request.getHeaders().toSingleValueMap());
        headerMap.put(MyConstant.TRACE_ID, traceId);

        Consumer<HttpHeaders> httpHeaders = httpHeader -> {
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                httpHeader.set(entry.getKey(), entry.getValue());
            }
        };

        ServerHttpRequest newRequest = exchange.getRequest().mutate().headers(httpHeaders).build();
        ServerWebExchange newExchange = exchange.mutate().request(newRequest).build();

        return chain.filter(newExchange);
    }

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE;
    }

    private String getTraceId() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
