package com.mojiayi.action.gravity.utils;

public class CommonMethod {
    private static final double G = 6.673E-11;
    /**
     * 一千克约等于9.8牛顿
     */
    private static final double N = 9.8d;

    private static double getG(double weight, double radius) {
        return (G * weight) / Math.pow(radius, 2);
    }

    /**
     * 计算地球上一定质量的物体，在另外一个星球上相应的重力值，注意是重力值，不是质量
     *
     * @param weightOnEarth 地球上的质量
     * @param planetWeight  目标星球的质量
     * @param planetRadius  目标星球的半径
     * @return 返回在目标星球上的重力值
     */
    public static double getF(double weightOnEarth, double planetWeight, double planetRadius) {
        return weightOnEarth * getG(planetWeight, planetRadius);
    }

    /**
     * 计算地球上一定质量的物体，在另外一个星球上相应的质量，注意是质量，不是重力值
     *
     * @param weightOnEarth 地球上的质量
     * @param planetWeight  目标星球的质量
     * @param planetRadius  目标星球的半径
     * @return 返回在目标星球上的质量
     */
    public static double getM(double weightOnEarth, double planetWeight, double planetRadius) {
        return weightOnEarth * getG(planetWeight, planetRadius) / N;
    }
}
