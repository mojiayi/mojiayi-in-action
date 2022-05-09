package com.mojiayi.action.algorithm.utils;

/**
 * @author mojiayi
 */
public class Pair<T> {
    private final T item;
    private final double weight;

    public Pair(T item, double weight) {
        this.item = item;
        this.weight = weight;
    }

    public T item() {
        return this.item;
    }

    public double weight() {
        return this.weight;
    }
}