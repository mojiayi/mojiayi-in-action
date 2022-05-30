package com.mojiayi.action.dynamic.data.permission.interceptor;

import com.mojiayi.action.dynamic.data.permission.constants.MyConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 从 request header 中提取上游传递过来的用户数据权限配置，放到本线线程空间
 * </p>
 *
 * @author mojiayi
 */
@Slf4j
@Component
public class DataPermissionConfigInterceptor implements Ordered, HandlerInterceptor {

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 清理掉线程空间里的数据权限值，避免多线程并发时的干扰
        MDC.remove(MyConstant.DATA_PERMISSION);
        // 从header中获取用户数据权限配置
        var dataPermissionConfig = request.getHeader(MyConstant.DATA_PERMISSION);
        if (StringUtils.isBlank(dataPermissionConfig)) {
            // 上游没有传递用户数据权限配置
            return true;
        }
        // 把从request header获得的角色数据权限配置存入本地线程空间，方便一次请求中执行多条 SQL 时可复用
        MDC.put(MyConstant.DATA_PERMISSION, dataPermissionConfig);
        if (log.isDebugEnabled()) {
            log.debug("当前用户的数据权限={}", dataPermissionConfig);
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
