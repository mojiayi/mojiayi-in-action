package com.mojiayi.action.gateway.loadbalancer;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

/**
 * <p>
 * 启用一致性哈希负载算法
 * </p>
 *
 * @author mojiayi
 */
public class ConsistentHashingConfig {
    @Bean
    public ReactorLoadBalancer<ServiceInstance> serviceInstanceReactorLoadBalancer(Environment environment, LoadBalancerClientFactory loadBalancerClientFactory) {
        var clientName = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);
        return new ConsistentHashingLoadBalancer(loadBalancerClientFactory.getLazyProvider(clientName, ServiceInstanceListSupplier.class), clientName);
    }
}
