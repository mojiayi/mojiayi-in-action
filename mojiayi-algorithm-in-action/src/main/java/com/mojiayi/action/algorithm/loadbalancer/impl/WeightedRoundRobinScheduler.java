package com.mojiayi.action.algorithm.loadbalancer.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.mojiayi.action.algorithm.loadbalancer.IServerScheduler;
import com.mojiayi.action.algorithm.loadbalancer.bean.ServerInfo;

import java.util.Comparator;
import java.util.List;

/**
 * 加权轮询，不管有多少个服务节点，权重值分别是多少，每运行@{code sum(server1.weight+server2.weight+...+serverN.weight)/2}个轮次，各服务节点的动态权重恢复默认值，从头开始分配
 *
 * @author liguangri
 */
public class WeightedRoundRobinScheduler implements IServerScheduler {
    @Override
    public ServerInfo choose(List<ServerInfo> serverList) {
        if (CollectionUtil.isEmpty(serverList)) {
            return null;
        }
        boolean needInitCurrentWeight = serverList.stream().allMatch(v -> v.getCurrentWeight() == 0);
        if (needInitCurrentWeight) {
            serverList.forEach(v -> v.setCurrentWeight(v.getWeight()));
        }
        ServerInfo chosenServer = serverList.stream().max(Comparator.comparing(ServerInfo::getCurrentWeight)).orElse(serverList.get(0));
        chosenServer.setCurrentWeight(chosenServer.getCurrentWeight() - 1);
        return chosenServer;
    }
}
