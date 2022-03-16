package com.mojiayi.action.gravity.utils;

public class CommonMethod {
    private static final double G = 6.673E-11;

    private static double getG(double weight, double radius) {
        return (G * weight) / Math.pow(radius, 2);
    }

    public static double getF(double weightOnEarth, double planetWeight, double planetRadius) {
        return weightOnEarth * getG(planetWeight, planetRadius);
    }
}
