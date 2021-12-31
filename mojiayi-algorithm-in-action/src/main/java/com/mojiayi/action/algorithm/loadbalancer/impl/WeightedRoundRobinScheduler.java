package com.mojiayi.action.algorithm.loadbalancer.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.mojiayi.action.algorithm.loadbalancer.IServerScheduler;
import com.mojiayi.action.algorithm.loadbalancer.bean.ServerInfo;

import java.util.List;

/**
 * 以平滑权重轮询的方式分配服务节点
 *
 * @author liguangri
 */
public class WeightedRoundRobinScheduler implements IServerScheduler {
    @Override
    public ServerInfo choose(List<ServerInfo> serverList) {
        if (CollectionUtil.isEmpty(serverList)) {
            return null;
        }
        // 把所有服务节点的权重值相加，用于后续的计算
        double weightSum = serverList.stream().mapToDouble(ServerInfo::getWeight).sum();
        ServerInfo chosenServer = null;
        for (ServerInfo serverInfo : serverList) {
            // 如果初始化serverList时，把currentWeight设置为和weight相同的值，第一次就会返回权重值最大的服务节点
            // 如果初始化serverList时，currentWeight默认设置为0，第一次只会返回serverList数组中的第一个元素
            if (chosenServer == null || serverInfo.getCurrentWeight() > chosenServer.getCurrentWeight()) {
                chosenServer = serverInfo;
            }
            // 动态权重值和固定权重值相加，用于提升服务节点被选中的概率
            // 因为固定权重值更大的服务节点，计算后的新动态权重值变得更大的概率更高，所以权重值大的在每一轮中被选中的概率也更高
            serverInfo.setCurrentWeight(serverInfo.getCurrentWeight() + serverInfo.getWeight());
        }
        assert chosenServer != null;
        // 被选中节点的动态权重值和所有节点权重值相减，用于降低本轮选中的节点在下一轮被选中的概率
        // 因为固定权重值更大的服务节点，计算后的新动态权重值仍然比较大的概率更高，所以权重值大的在下一轮中继续被选中的概率仍然更高
        chosenServer.setCurrentWeight(chosenServer.getCurrentWeight() - weightSum);
        return chosenServer;
    }
}
