package com.mojiayi.action.auth.handler;

import com.mojiayi.action.auth.domain.SelfUserEntity;
import com.mojiayi.action.auth.properties.JwtProperties;
import com.mojiayi.action.auth.util.MojiayiJwtUtil;
import com.mojiayi.action.auth.util.MojiayiRespUtil;
import com.mojiayi.action.common.tool.response.CommonResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author liguangri
 */
@Component
public class UserLoginSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    private MojiayiJwtUtil jwtUtil;
    @Autowired
    private JwtProperties jwtProperties;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        SelfUserEntity userEntity = (SelfUserEntity) authentication.getPrincipal();
        String token = jwtProperties.tokenPrefix + jwtUtil.generateToken(userEntity);
        MojiayiRespUtil.response(httpServletResponse, CommonResp.success(token));
    }
}
