package com.mojiayi.action.gateway.auth;

import com.alibaba.fastjson.JSON;
import com.mojiayi.action.cloud.constant.MyConstant;
import com.mojiayi.action.common.tool.response.CommonResp;
import com.mojiayi.action.gateway.feign.IMemberApi;
import com.mojiayi.action.member.response.MemberDetailResp;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * <p>
 * 登录认证、鉴权和设置必要的当前用户信息到请求头
 * </p>
 *
 * @author guangri.li
 * @since 2022/5/25 21:45
 */
@Slf4j
@Component
public class AuthorizationFilter implements Ordered, GlobalFilter {
    @Autowired
    private IMemberApi memberApi;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpRequest.Builder mutate = request.mutate();
        HttpHeaders headers = request.getHeaders();
        String token = headers.getFirst(MyConstant.X_ACCESS_TOKEN);
        long memberId = 0L;
        // TODO 补充解码token获取member id的工具类
//        memberId = JWTUtil.getMemberId(token);

        Mono<Void> result = transferMemberInfo(memberId, token, exchange, mutate);
        if (result != null) {
            return result;
        }

        transferDataPermissionConfig(memberId, token, mutate);

        return chain.filter(exchange.mutate().request(mutate.build()).build());
    }

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE + 1;
    }

    private void addHeader(ServerHttpRequest.Builder mutate, String name, Object value) {
        if (value == null) {
            return;
        }
        String valueEncode = URLEncoder.encode(value.toString(), StandardCharsets.UTF_8);
        mutate.header(name, valueEncode);
    }

    private Mono<Void> transferMemberInfo(long memberId, String token, ServerWebExchange exchange, ServerHttpRequest.Builder mutate) {
        CompletableFuture<CommonResp<MemberDetailResp>> future = CompletableFuture.supplyAsync(() -> {
            MDC.put(MyConstant.X_ACCESS_TOKEN, token);
            try {
                return memberApi.getDetailByMemberId(memberId);
            } catch (FeignException e) {
                log.info("调用memberApi.getDetailByMemberId({})发生异常", memberId, e);
            }
            return null;
        });

        try {
            CommonResp<MemberDetailResp> commonResp = future.get();
            if (commonResp == null || !commonResp.isSuccess() || commonResp.getData() == null) {
                return unauthorizedResponse(exchange, "token已过期或验证不正确");
            }
            addHeader(mutate, MyConstant.MEMBER_ID, commonResp.getData().getMemberId());
            addHeader(mutate, MyConstant.MEMBER_NAME, commonResp.getData().getMemberName());
        } catch (InterruptedException | ExecutionException e) {
            log.info("获取memberApi.getDetailByMemberId({})结果发生异常", memberId, e);
            Thread.currentThread().interrupt();
            return unauthorizedResponse(exchange, e.getMessage());
        }
        return null;
    }

    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange, String msg) {
        log.info("[鉴权异常处理]请求路径:{}", exchange.getRequest().getPath());
        return writeWebFluxResponse(exchange.getResponse(), msg, HttpStatus.UNAUTHORIZED.value());
    }

    private Mono<Void> writeWebFluxResponse(ServerHttpResponse response, Object value, int code) {
        response.setStatusCode(HttpStatus.OK);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        String msg = null;
        if (value != null && StringUtils.hasLength(value.toString())) {
            msg = value.toString();
        } else {
            msg = "token已过期或验证不正确";
        }
        var dataBuffer = response.bufferFactory().wrap(JSON.toJSONString(CommonResp.error(code, msg)).getBytes());
        return response.writeWith(Mono.just(dataBuffer));
    }

    private void transferDataPermissionConfig(long memberId, String token, ServerHttpRequest.Builder mutate) {
        MDC.remove(MyConstant.DATA_PERMISSION);
        // TODO 这里应该写一些登录认证和授权的代码，获得当前用户的数据隔离配置
//        List<DataPermissionConfig> dataPermissionFieldList = getUserDataPermission(memberId, token);
//        String dataPermission = JSON.toJSONString(dataPermissionFieldList);

        // 设置用户数据权限信息到请求头
        addHeader(mutate, MyConstant.DATA_PERMISSION, "dataPermission");
    }
}
