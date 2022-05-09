package com.mojiayi.action.algorithm.loadbalancer.impl;

import com.mojiayi.action.algorithm.loadbalancer.IServerScheduler;
import com.mojiayi.action.algorithm.loadbalancer.bean.ServerInfo;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 加权随机，实现思路来自Ribbon源码。
 *
 * @author mojiayi
 */
public class WeightedRandomScheduler implements IServerScheduler {
    @Override
    public ServerInfo choose(List<ServerInfo> serverList) {
        // 把所有服务节点的权重值相加，用于后续的计算
        double weightSum = serverList.stream().mapToDouble(ServerInfo::getWeight).sum();
        double[] exactWeights = new double[serverList.size()];
        int index = 0;
        for (ServerInfo serverInfo : serverList) {
            // 计算得出每个节点的权重值在总权重值之和中占多少比例
            exactWeights[index++] = serverInfo.getWeight() / weightSum;
        }
        double[] weights = new double[serverList.size()];
        for (int i = 0; i < index; i++) {
            // 从第2个服务节点起，后一个服务节点本身的权重值+前一个，形成类似等差数列的一个权重值数组
            // 最后一个节点的权重值应该是1，或者小于1且无限接近1的数值
            weights[i] = exactWeights[i];
            if (i > 0) {
                weights[i] = weights[i] + weights[i -1];
            }
        }
        // 生成一个0~1，不包含1的伪随机数
        double random = ThreadLocalRandom.current().nextDouble(0, 1);
        // 利用二分法查找，找出上一步的随机数在权重值数组中的下标
        // 因为二分法查找的特性，原始权重值越大的，在类似等差数列中占据的空间更大，随机数落在它的范围内的概率越大，被选中的概率也就更大
        index = Arrays.binarySearch(weights, random);
        if (index < 0) {
            index = -index - 1;
        } else {
            return serverList.get(index);
        }

        if (index < weights.length) {
            return serverList.get(index);
        }

        // 这行代码一般不会被运行到，除非前面的计算过程中发生了浮点数精度问题
        // 为防止这种极小概率的事件发生，默认返回最后一个服务节点
        return serverList.get(serverList.size() - 1);
    }
}
