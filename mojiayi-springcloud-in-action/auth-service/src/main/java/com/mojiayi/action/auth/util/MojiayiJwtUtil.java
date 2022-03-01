package com.mojiayi.action.auth.util;

import com.google.gson.Gson;
import com.mojiayi.action.auth.domain.SelfUserEntity;
import com.mojiayi.action.auth.properties.JwtProperties;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author liguangri
 */
@Component
public class MojiayiJwtUtil {
    @Autowired
    private JwtProperties jwtProperties;

    public String generateToken(SelfUserEntity userEntity) {
        Gson gson = new Gson();
        long now = System.currentTimeMillis();
        JwtBuilder jwtBuilder = Jwts.builder();
        jwtBuilder.setId(userEntity.getUserId().toString())
                .setSubject(userEntity.getUsername())
                .setIssuedAt(new Date(now))
                .setIssuer("mojiayi")
                .claim("authorities", gson.toJson(userEntity.getAuthorities()))
                .setExpiration(new Date(now + jwtProperties.expiration))
                .signWith(SignatureAlgorithm.HS512, jwtProperties.secret);
        return jwtBuilder.compact();
    }
}
