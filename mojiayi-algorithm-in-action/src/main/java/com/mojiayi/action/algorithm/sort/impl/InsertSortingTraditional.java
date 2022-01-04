package com.mojiayi.action.algorithm.sort.impl;

import com.mojiayi.action.algorithm.sort.ISorting;

/**
 * 传统的插入排序算法实现方案，使用2个数组
 *
 * @author liguangri
 */
public class InsertSortingTraditional implements ISorting {
    @Override
    public int[] sort(int[] dataList) {
        int[] sortedList = new int[dataList.length];
        int length = sortedList.length;
        for (int m = 0; m < length; m++) {
            // 第一个元素，不需要比较，直接放在已排序列表末尾
            if (m == 0) {
                sortedList[length - 1] = dataList[m];
                continue;
            }
            int insertPoint = -1;
            int sortedStartPoint = length - m - 1;
            // 从已排序列表末尾开始，从后往前比较，找到第一个比待排序数值小的，就找到了要被插入的下标
            for (int n = length - 1; n >= sortedStartPoint; n--) {
                if (sortedList[n] < dataList[m]) {
                    insertPoint = n;
                    break;
                }
            }
            // 要被移动的元素个数大于0时，才需要复制一遍数据
            int copySize = insertPoint - sortedStartPoint;
            if (copySize > 0) {
                System.arraycopy(sortedList, sortedStartPoint + 1, sortedList, sortedStartPoint, copySize);
            }
            if (insertPoint >= 0) {
                sortedList[insertPoint] = dataList[m];
            }
        }
        return sortedList;
    }
}
