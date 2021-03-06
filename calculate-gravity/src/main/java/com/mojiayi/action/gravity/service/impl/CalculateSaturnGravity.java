package com.mojiayi.action.gravity.service.impl;

import com.mojiayi.action.gravity.enums.PlanetEnum;
import com.mojiayi.action.gravity.service.ICalculateGravityService;
import com.mojiayi.action.gravity.vo.PlanetGravityVO;
import org.springframework.stereotype.Service;

/**
 * @author mojiayi
 */
@Service
public class CalculateSaturnGravity implements ICalculateGravityService {
    @Override
    public PlanetEnum gainPlanet() {
        return PlanetEnum.SATURN;
    }

    @Override
    public PlanetGravityVO calculateGravity(double weightOnEarth) {
        PlanetEnum planetEnum = this.gainPlanet();
        double weight = planetEnum.calculateGravity(weightOnEarth);
        return new PlanetGravityVO(planetEnum.getName(), weight);
    }
}
