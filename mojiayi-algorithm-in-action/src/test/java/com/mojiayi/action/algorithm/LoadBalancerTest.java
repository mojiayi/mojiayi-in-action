package com.mojiayi.action.algorithm;

import com.mojiayi.action.algorithm.loadbalancer.IServerScheduler;
import com.mojiayi.action.algorithm.loadbalancer.bean.ServerInfo;
import com.mojiayi.action.algorithm.loadbalancer.impl.RoundRobinScheduler;
import com.mojiayi.action.algorithm.loadbalancer.impl.WeightedRandomScheduler;
import com.mojiayi.action.algorithm.loadbalancer.impl.SmoothWeightedRoundRobinScheduler;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 测试负载均衡算法，指定的分配轮数越多，越能体现具体算法的分配效果正确。
 */
public class LoadBalancerTest {
    private static final List<ServerInfo> hosts = new ArrayList<>(4);
    private static final int serverSize = 5;

    static {
        for (int index = 0; index < serverSize; index++) {
            ServerInfo instance = new ServerInfo();
            instance.setServiceId(index);
            instance.setWeight(index + 1);
            instance.setServiceName("testService" + index);
            hosts.add(instance);
        }
    }

    @Test
    public void testWeightedRandomScheduler() {
        IServerScheduler serverScheduler = new WeightedRandomScheduler();
        int cycle = 1000;
        int[] matchRecord = choose(serverScheduler, cycle);
        System.out.println(Arrays.toString(matchRecord));
    }

    @Test
    public void testWeightedRoundRobinScheduler() {
        IServerScheduler serverScheduler = new SmoothWeightedRoundRobinScheduler();
        int cycle = 1000;
        int[] matchRecord = choose(serverScheduler, cycle);
        System.out.println(Arrays.toString(matchRecord));
    }

    @Test
    public void testRoundRobinScheduler() {
        IServerScheduler serverScheduler = new RoundRobinScheduler();
        int cycle = 10;
        for (int count = 0; count < cycle; count++) {
            ServerInfo chosenInstance = serverScheduler.choose(hosts);
            int expectedIndex = count % serverSize;
            Assert.assertEquals(expectedIndex, chosenInstance.getServiceId());
        }
    }

    private int[] choose(IServerScheduler serverScheduler, int cycle) {
        int[] matchRecord = new int[hosts.size()];
        for (int count = 0; count < cycle; count++) {
            ServerInfo chosenInstance = serverScheduler.choose(hosts);
            String serviceName = chosenInstance.getServiceName();
            int index = Integer.parseInt(serviceName.substring(serviceName.length() - 1));
            matchRecord[index]++;
        }
        return matchRecord;
    }
}
