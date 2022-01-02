package com.mojiayi.action.algorithm.sort.impl;

import com.mojiayi.action.algorithm.sort.ISorting;

/**
 * 传统的插入排序算法实现方案，使用2个数组
 *
 * @author liguangri
 */
public class TraditionalInsertSorting implements ISorting {
    @Override
    public int[] sort(int[] dataList) {
        int[] sortedList = new int[dataList.length];
        int length = sortedList.length;
        for (int m = 0; m < length; m++) {
            if (m == 0) {
                sortedList[length - 1] = dataList[m];
                continue;
            }
            int insertPoint = -1;
            int sortedStartPoint = length - m - 1;
            for (int n = length - 1; n >= sortedStartPoint; n--) {
                if (sortedList[n] < dataList[m]) {
                    insertPoint = n;
                    break;
                }
            }
            if (insertPoint - sortedStartPoint >= 0) {
                System.arraycopy(sortedList, sortedStartPoint + 1, sortedList, sortedStartPoint, insertPoint - sortedStartPoint);
            }
            if (insertPoint >= 0) {
                sortedList[insertPoint] = dataList[m];
            }
        }
        return sortedList;
    }
}
