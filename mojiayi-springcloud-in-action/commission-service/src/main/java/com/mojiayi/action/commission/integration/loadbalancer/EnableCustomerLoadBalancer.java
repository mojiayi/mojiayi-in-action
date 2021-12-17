package com.mojiayi.action.commission.integration.loadbalancer;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author liguangri
 */
@Configuration
@LoadBalancerClient(name = "member-service", configuration = RandomLoadBalancerConfiguration.class)
public class EnableCustomerLoadBalancer {
    @Bean
    @LoadBalanced
    public WebClient.Builder loadBalancedWebClientBuilder() {
        return WebClient.builder();
    }
}
