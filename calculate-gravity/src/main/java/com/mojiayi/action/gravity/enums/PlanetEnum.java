package com.mojiayi.action.gravity.enums;

import com.mojiayi.action.gravity.utils.CommonMethod;
import com.mojiayi.action.gravity.vo.PlanetGravityVO;

/**
 * 各星球代号和名称
 *
 * @author liguangri
 */
public enum PlanetEnum {
    /**
     * 微信公众号推送
     */
    MERCURY(1, "水星", "Mercury", 3.302E+23, 2.439E+06),
    /**
     * 手机短信
     */
    VENUS(2, "金星", "Venus", 4.969E+24, 6.052E+06),
    /**
     * APP推送
     */
    EARTH(3, "地球", "Earth", 5.975E+24, 6.378E+06),
    /**
     * 语音呼叫
     */
    MARS(4, "火星", "Mars", 6.419E+23, 3.393E+06),
    /**
     * 语音呼叫
     */
    JUPITER(5, "木星", "Jupiter", 1.899E+27, 7.149E+07),
    /**
     * 语音呼叫
     */
    SATURN(6, "土星", "Saturn", 5.685E+26, 6.027E+07),
    /**
     * 语音呼叫
     */
    URANUS(7, "天王星", "Uranus", 8.683E+25, 2.556E+07),
    /**
     * 语音呼叫
     */
    NEPTUNE(8, "海王星", "Neptune", 1.024E+26, 2.477E+07);

    PlanetEnum(int code, String name, String desc, double weight, double radius) {
        this.code = code;
        this.name = name;
        this.desc = desc;
        this.weight = weight;
        this.radius = radius;
    }

    private int code;
    private String name;
    private String desc;
    private double weight;
    private double radius;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public double calculateGravity(double weightOnEarth) {
        return CommonMethod.getF(weightOnEarth, this.getWeight(), this.getRadius());
    }
}
