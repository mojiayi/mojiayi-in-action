package com.mojiayi.action.algorithm;

import com.mojiayi.action.algorithm.loadbalancer.IServerScheduler;
import com.mojiayi.action.algorithm.loadbalancer.bean.ServerInfo;
import com.mojiayi.action.algorithm.loadbalancer.impl.WeightedRandomScheduler;
import com.mojiayi.action.algorithm.loadbalancer.impl.WeightedRoundRobinScheduler;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 测试负载均衡算法，指定的分配轮数越多，越能体现具体算法的分配效果正确。
 */
public class LoadBalancerTest {
    private static final List<ServerInfo> hosts = new ArrayList<>(4);

    static {
        for (int index = 0;index < 5;index++) {
            ServerInfo instance = new ServerInfo();
            instance.setWeight(index+1);
            instance.setServiceName("testService" + index);
            hosts.add(instance);
        }
    }

    @Test
    public void testChooseByRandomWeight() {
        IServerScheduler chooseByRandomWeight = new WeightedRandomScheduler();
        int cycle = 1000;
        int[] matchRecord = choose(chooseByRandomWeight, cycle);
        System.out.println(Arrays.toString(matchRecord));
    }

    @Test
    public void testChooseByRoundRobinWeight() {
        IServerScheduler chooseServer = new WeightedRoundRobinScheduler();
        int cycle = 1000;
        int[] matchRecord = choose(chooseServer, cycle);
        System.out.println(Arrays.toString(matchRecord));
    }

    private int[] choose(IServerScheduler chooseServer, int cycle) {
        int[] matchRecord = new int[hosts.size()];
        for (int count = 0; count < cycle; count++) {
            ServerInfo chosenInstance = chooseServer.choose(hosts);
            String serviceName = chosenInstance.getServiceName();
            int index = Integer.parseInt(serviceName.substring(serviceName.length() - 1));
            matchRecord[index]++;
        }
        return matchRecord;
    }
}
