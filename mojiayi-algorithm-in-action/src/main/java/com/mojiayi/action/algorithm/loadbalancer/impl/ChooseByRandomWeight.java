package com.mojiayi.action.algorithm.loadbalancer.impl;

import com.mojiayi.action.algorithm.loadbalancer.IChooseServer;
import com.mojiayi.action.algorithm.loadbalancer.bean.ServerInfo;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author mojiayi
 */
public class ChooseByRandomWeight implements IChooseServer {
    @Override
    public ServerInfo choose(List<ServerInfo> serverList) {
        // 把所有服务节点的权重值相加，用于后续的计算
        double weightSum = serverList.stream().mapToDouble(ServerInfo::getWeight).sum();
        double[] exactWeights = new double[serverList.size()];
        int index = 0;
        for (ServerInfo serverInfo : serverList) {
            exactWeights[index++] = serverInfo.getWeight() / weightSum;
        }
        double[] weights = new double[serverList.size()];
        for (int i = 0; i < index; i++) {
            weights[i] = exactWeights[i];
            if (i > 0) {
                weights[i] = weights[i] + weights[i -1];
            }
        }
        double random = ThreadLocalRandom.current().nextDouble(0, 1);
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
