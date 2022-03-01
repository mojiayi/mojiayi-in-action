package com.mojiayi.action.auth.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author liguangri
 */
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    public String secret;
    public String tokenHeader;
    public String tokenPrefix;
    public long expiration;
    public String antMatchers;
}
