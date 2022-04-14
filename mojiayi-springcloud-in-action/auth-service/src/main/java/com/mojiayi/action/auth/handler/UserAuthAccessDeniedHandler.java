package com.mojiayi.action.auth.handler;

import com.mojiayi.action.auth.util.MojiayiRespUtil;
import com.mojiayi.action.common.tool.response.CommonResp;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author liguangri
 */
@Component
public class UserAuthAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        MojiayiRespUtil.response(httpServletResponse, CommonResp.error(HttpStatus.FORBIDDEN.value(), "未授权"));
    }
}
