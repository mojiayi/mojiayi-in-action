package com.mojiayi.action.cloud.loadbalancer;

import com.google.common.hash.Hashing;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.*;
import org.springframework.cloud.loadbalancer.core.NoopServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.RandomLoadBalancer;
import org.springframework.cloud.loadbalancer.core.SelectedInstanceCallback;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * <p>
 * 针对指定的url，使用一致性哈希负载算法分配请求
 * </p>
 *
 * @author mojiayi
 */
@Slf4j
public class ConsistentHashingLoadBalancer extends RandomLoadBalancer {
    private static final String MEMBER_ID = "memberId";
    @Autowired
    private ConsistentHashingProperties consistentHashingProperties;

    private final String serviceId;

    private final ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider;

    /**
     * @param serviceInstanceListSupplierProvider a provider of
     *                                            {@link ServiceInstanceListSupplier} that will be used to get available instances
     * @param serviceId                           id of the service for which to choose an instance
     */
    public ConsistentHashingLoadBalancer(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider, String serviceId) {
        super(serviceInstanceListSupplierProvider, serviceId);
        this.serviceId = serviceId;
        this.serviceInstanceListSupplierProvider = serviceInstanceListSupplierProvider;
    }


    @Override
    public Mono<Response<ServiceInstance>> choose(Request request) {
        DefaultRequestContext requestContext = (DefaultRequestContext) request.getContext();
        RequestData clientRequest = (RequestData) requestContext.getClientRequest();
        String uri = clientRequest.getUrl().getPath();
        if (!consistentHashingProperties.getConsistentHashingUrls().contains(uri)) {
            return super.choose(request);
        }
        String query = clientRequest.getUrl().getQuery();
        if (!StringUtils.hasLength(query)) {
            return super.choose(request);
        }
        String[] pairs = query.split("&");
        String[] keyAndValue = null;
        String memberId = null;
        for (String pair : pairs) {
            keyAndValue = pair.split("=");
            if (MEMBER_ID.equals(keyAndValue[0])) {
                memberId = keyAndValue[1];
                break;
            }
        }
        if (!StringUtils.hasLength(memberId)) {
            return super.choose(request);
        }

        var supplier = serviceInstanceListSupplierProvider
                .getIfAvailable(NoopServiceInstanceListSupplier::new);
        String finalMemberId = memberId;
        return supplier.get(request).next()
                .map(serviceInstances -> processInstanceResponse(supplier, serviceInstances, finalMemberId));
    }

    private Response<ServiceInstance> processInstanceResponse(ServiceInstanceListSupplier supplier,
                                                              List<ServiceInstance> serviceInstances, String memberId) {
        Response<ServiceInstance> serviceInstanceResponse = getInstanceResponse(serviceInstances, memberId);
        if (supplier instanceof SelectedInstanceCallback && serviceInstanceResponse.hasServer()) {
            ((SelectedInstanceCallback) supplier).selectedServiceInstance(serviceInstanceResponse.getServer());
        }
        return serviceInstanceResponse;
    }

    private Response<ServiceInstance> getInstanceResponse(List<ServiceInstance> instances, String memberId) {
        if (CollectionUtils.isEmpty(instances)) {
            log.info("No servers available for service: " + serviceId);
            return new EmptyResponse();
        }

        int memberIdHashCode = memberId.hashCode();
        int mode = Hashing.consistentHash(memberIdHashCode, instances.size());
        return new DefaultResponse(instances.get(mode));
    }
}
