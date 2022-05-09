package com.mojiayi.action.gateway.loadbalancer;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 一致性哈希负载算法的配置
 * </p>
 *
 * @author mojiayi
 * @since 2022/4/29 14:55
 */
@Component
@Data
@ConfigurationProperties(prefix = "customize")
public class ConsistentHashingProperties {
    /**
     * 使用一致性哈希负载算法分配服务节点的uri
     */
    private List<String> consistentHashingUrls;
}
