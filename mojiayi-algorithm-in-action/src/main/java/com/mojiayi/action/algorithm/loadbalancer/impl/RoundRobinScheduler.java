package com.mojiayi.action.algorithm.loadbalancer.impl;

import com.mojiayi.action.algorithm.loadbalancer.IServerScheduler;
import com.mojiayi.action.algorithm.loadbalancer.bean.ServerInfo;

import java.util.List;

/**
 * 普通轮询
 *
 * @author mojiayi
 */
public class RoundRobinScheduler implements IServerScheduler {
    private int currentServerIndex = -1;

    @Override
    public ServerInfo choose(List<ServerInfo> serverList) {
        synchronized (this) {
            int nextServerIndex = currentServerIndex;
            if (nextServerIndex < 0) {
                nextServerIndex = 0;
                currentServerIndex = 0;
            } else {
                currentServerIndex++;
                if (currentServerIndex == serverList.size()) {
                    nextServerIndex = 0;
                    currentServerIndex = 0;
                } else {
                    nextServerIndex = currentServerIndex;
                }
            }
            return serverList.get(nextServerIndex);
        }
    }
}
