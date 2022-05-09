package com.mojiayi.action.algorithm.sort.impl;

import com.mojiayi.action.algorithm.sort.ISorting;

/**
 * 不新建一个数组的选择排序算法实现方案
 *
 * @author mojiayi
 */
public class SelectionSortingOnlyOneArray implements ISorting {
    @Override
    public int[] sort(int[] dataList) {
        int length = dataList.length;
        for (int m = 0; m < length; m++) {
            int minValueIndex = -1;
            int minValue = Integer.MAX_VALUE;
            for (int n = m; n < length; n++) {
                if (dataList[n] < minValue) {
                    minValue = dataList[n];
                    minValueIndex = n;
                }
            }
            if (minValueIndex >= 0) {
                System.arraycopy(dataList, m, dataList, (m + 1), (minValueIndex-m));
                dataList[m] = minValue;
            }
        }
        return dataList;
    }
}
