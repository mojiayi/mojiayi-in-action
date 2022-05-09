package com.mojiayi.action.gravity.vo;

/**
 * 星球信息和重力值
 * @author mojiayi
 */
public class PlanetGravityVO {
    /**
     * 星球名
     */
    private String name;
    /**
     * 重力值
     */
    private double gravity;

    public PlanetGravityVO(String name, double gravity) {
        this.name = name;
        this.gravity = gravity;
    }

    public PlanetGravityVO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getGravity() {
        return gravity;
    }

    public void setGravity(double gravity) {
        this.gravity = gravity;
    }
}
