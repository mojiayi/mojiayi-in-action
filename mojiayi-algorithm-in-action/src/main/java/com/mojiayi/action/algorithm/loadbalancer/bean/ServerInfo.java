package com.mojiayi.action.algorithm.loadbalancer.bean;

import lombok.Data;

/**
 * 服务节点基本信息，仅用于试验各种负载均衡分配算法，所以信息很少。
 *
 * @author mojiayi
 */
@Data
public class ServerInfo {
    /**
     * 服务id
     */
    private int serviceId;
    /**
     * 服务节点名
     */
    private String serviceName;
    /**
     * 服务节点固定权重值，在分配过程中不变
     */
    private int weight;
    /**
     * 专门用于按权重轮询的动态权重值，在{@code ChooseByRoundRobinWeight}的分配过程中变化
     */
    private int currentWeight;
}
