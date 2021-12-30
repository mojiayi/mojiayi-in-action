package com.mojiayi.action.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author liguangri
 */
@Component
@Data
@ConfigurationProperties
public class RouteIdNameConfiguration {
    private List<Map<String, String>> lists;

    private Map<String, String> routeIdNameMap;
}
