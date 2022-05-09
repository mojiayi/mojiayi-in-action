package com.mojiayi.action.auth.handler;

import com.mojiayi.action.auth.util.MojiayiRespUtil;
import com.mojiayi.action.common.tool.response.CommonResp;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author mojiayi
 */
@Component
public class UserAuthenticationEntryPointHandler implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        MojiayiRespUtil.response(httpServletResponse, CommonResp.error(HttpStatus.UNAUTHORIZED.value(), "未登录"));
    }
}
