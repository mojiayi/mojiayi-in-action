package com.mojiayi.action.cloud.feign;

import com.mojiayi.action.cloud.constant.MyConstant;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * <p>
 * 通过 feign 调用各服务接口时，把上游请求的 header 传递到下游请求中
 * </p>
 *
 * @author mojiayi
 */
@Component
public class FeignRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        var requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return;
        }
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();

        Map<String, String> headers = getHeaders(request);
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            if (HttpHeaders.CONTENT_TYPE.equalsIgnoreCase(entry.getKey())) {
                continue;
            }
            if (StringUtils.hasLength(entry.getValue()) && !"{}".equals(entry.getValue())) {
                requestTemplate.header(entry.getKey(), entry.getValue());
            }
        }
        var contentType = requestTemplate.headers().get(HttpHeaders.CONTENT_TYPE);
        if (contentType == null || !contentType.toString().contains(MediaType.MULTIPART_FORM_DATA_VALUE)) {
            requestTemplate.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        }
        var traceId = MDC.get(MyConstant.TRACE_ID);
        if (!StringUtils.hasLength(traceId)) {
            // 网关层没有传递traceId过来，就生成一个新的
            traceId = UUID.randomUUID().toString().replace("-", "");
            ;
            MDC.put(MyConstant.TRACE_ID, traceId);
        }
        requestTemplate.header(MyConstant.TRACE_ID, traceId);
    }

    private Map<String, String> getHeaders(HttpServletRequest request) {
        Enumeration<String> enumeration = request.getHeaderNames();
        Map<String, String> map = new LinkedHashMap<>();
        String key = null;
        while (enumeration.hasMoreElements()) {
            key = enumeration.nextElement();
            map.put(key, request.getHeader(key));
        }
        return map;
    }
}
