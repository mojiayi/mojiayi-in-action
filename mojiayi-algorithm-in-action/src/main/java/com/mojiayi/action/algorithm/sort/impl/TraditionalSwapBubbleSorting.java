package com.mojiayi.action.algorithm.sort.impl;

import com.mojiayi.action.algorithm.sort.ISorting;

/**
 * 传统的冒泡排序算法实现方案，使用一个中间值存储被交换的数值
 *
 * @author liguangri
 */
public class TraditionalSwapBubbleSorting implements ISorting {
    @Override
    public int[] sort(int[] dataList) {
        int size = dataList.length;
        for (int m = 0; m < size; m++) {
            for (int n = m + 1; n < size; n++) {
                if (dataList[m] > dataList[n]) {
                    int temp = dataList[m];
                    dataList[m] = dataList[n];
                    dataList[n] = temp;
                }
            }
        }
        return dataList;
    }
}
