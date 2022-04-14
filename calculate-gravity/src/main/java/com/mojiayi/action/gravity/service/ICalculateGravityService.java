package com.mojiayi.action.gravity.service;

import com.mojiayi.action.gravity.enums.PlanetEnum;
import com.mojiayi.action.gravity.vo.PlanetGravityVO;

/**
 * 计算各星球重力的服务
 *
 * @author liguangri
 */
public interface ICalculateGravityService {
    /**
     * 获得当前要计算的星球
     *
     * @return
     */
    PlanetEnum gainPlanet();

    /**
     * 计算指定星球的重力
     *
     * @param weightOnEarth 地球上的重力值
     * @return
     */
    PlanetGravityVO calculateGravity(double weightOnEarth);
}
