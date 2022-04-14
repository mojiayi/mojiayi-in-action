package com.mojiayi.action.gravity.controller;

import com.mojiayi.action.common.tool.response.CommonResp;
import com.mojiayi.action.gravity.enums.PlanetEnum;
import com.mojiayi.action.gravity.service.ICalculateGravityService;
import com.mojiayi.action.gravity.vo.PlanetGravityVO;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author liguangri
 */
@RestController
@RequestMapping("/gravity")
public class CalculateGravityController implements ApplicationContextAware {
    private Map<PlanetEnum, ICalculateGravityService> strategyMap = new ConcurrentHashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, ICalculateGravityService> tempMap = applicationContext.getBeansOfType(ICalculateGravityService.class);
        tempMap.values().forEach(strategy -> strategyMap.put(strategy.gainPlanet(), strategy));
    }

    @GetMapping("/{weightOnEarth}")
    public CommonResp<List<PlanetGravityVO>> calculateAllPlanetGavity(@PathVariable("weightOnEarth") double weightOnEarth) {
        List<PlanetGravityVO> voList = new ArrayList<>(8);
        PlanetEnum[] planetEnums = PlanetEnum.values();
        PlanetGravityVO vo = null;
        for (PlanetEnum planetEnum : planetEnums) {
            vo = strategyMap.get(planetEnum).calculateGravity(weightOnEarth);
            voList.add(vo);
        }

        return CommonResp.success(voList);
    }
}
