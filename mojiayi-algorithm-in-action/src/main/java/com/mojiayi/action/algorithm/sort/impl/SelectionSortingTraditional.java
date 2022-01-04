package com.mojiayi.action.algorithm.sort.impl;

import com.mojiayi.action.algorithm.sort.ISorting;

/**
 * 传统的选择排序算法实现方案，有两个for循环，使用2个数组
 *
 * @author liguangri
 */
public class SelectionSortingTraditional implements ISorting {
    @Override
    public int[] sort(int[] dataList) {
        int[] sortedList = new int[dataList.length];
        int length = sortedList.length;
        for (int n = 0; n < length; n++) {
            int minValueIndex = -1;
            int minValue = Integer.MAX_VALUE;
            for (int m = 0; m < length; m++) {
                if (dataList[m] > Integer.MIN_VALUE && dataList[m] < minValue) {
                    minValue = dataList[m];
                    minValueIndex = m;
                }
            }
            if (minValueIndex >= 0) {
                sortedList[n] = minValue;
                dataList[minValueIndex] = Integer.MIN_VALUE;
            }
        }
        return sortedList;
    }
}
