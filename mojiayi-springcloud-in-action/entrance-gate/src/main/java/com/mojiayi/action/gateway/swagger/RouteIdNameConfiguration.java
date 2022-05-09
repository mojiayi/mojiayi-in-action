package com.mojiayi.action.gateway.swagger;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author liguangri
 */
@Component
@Data
@ConfigurationProperties
public class RouteIdNameConfiguration {
    private Map<String, String> routeIdNameMap;
}
