package com.mojiayi.action.algorithm.sort.impl;

import com.mojiayi.action.algorithm.sort.ISorting;

/**
 * 没有中间值存储被交换的数值，通过加减法交换两个数值
 *
 * @author mojiayi
 */
public class BubbleSortingNoTempDataSwap implements ISorting {
    @Override
    public int[] sort(int[] dataList) {
        int size = dataList.length;
        for (int m = 0; m < size; m++) {
            for (int n = m + 1; n < size; n++) {
                if (dataList[m] > dataList[n]) {
                    dataList[m] += dataList[n];
                    dataList[n] = dataList[m] - dataList[n];
                    dataList[m] -= dataList[n];
                }
            }
        }
        return dataList;
    }
}
