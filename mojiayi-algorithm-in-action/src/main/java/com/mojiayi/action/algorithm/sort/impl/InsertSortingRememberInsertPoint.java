package com.mojiayi.action.algorithm.sort.impl;

import com.mojiayi.action.algorithm.sort.ISorting;

/**
 * 记住每次插入点，待排序数值如果大于上次插入的值，就从上一个插入点开始往后查找新插入点，如果小于，就从上一个插入点开始往前查找新插入点
 *
 * @author liguangri
 */
public class InsertSortingRememberInsertPoint implements ISorting {
    @Override
    public int[] sort(int[] dataList) {
        int[] sortedList = new int[dataList.length];
        int length = sortedList.length;
        int insertPoint = -1;
        for (int m = 0; m < length; m++) {
            // 第一个元素，不需要比较，直接放在已排序列表末尾
            if (m == 0) {
                sortedList[length - 1] = dataList[m];
                insertPoint = length - 1;
                continue;
            }
            int sortedStartPoint = length - m - 1;
            if (dataList[m] >= sortedList[insertPoint]) {
                // 如果待排序元素的数值大于或等于上一个被排序的，从上次的插入点往后查找新插入点
                int newInsertPoint = -1;
                for (int n = insertPoint; n < length; n++) {
                    // 找到第一个大于待排序元素后，新插入点是这个元素的前一个位置
                    if (sortedList[n] > dataList[m]) {
                        newInsertPoint = n - 1;
                        break;
                    }
                }
                // 如果找到最后还没有大于待排序元素的，列表末尾就是新插入点
                if (newInsertPoint < 0) {
                    newInsertPoint = length - 1;
                }
                insertPoint = newInsertPoint;
            } else {
                // 如果待排序元素的数值小于上一个被排序的，从上次的插入点往前查找新插入点
                int newInsertPoint = -1;
                for (int n = insertPoint; n > sortedStartPoint - 1; n--) {
                    // 找到第一个小于待排序元素后，新插入点就是这个元素所在位置
                    if (sortedList[n] < dataList[m]) {
                        newInsertPoint = n;
                        break;
                    }
                }
                // 如果找到最前面还没有小于待排序元素的，就插入点是已排序下标的前一个位置
                if (newInsertPoint < 0) {
                    newInsertPoint = sortedStartPoint - 1;
                }
                insertPoint = newInsertPoint;
            }
            // 当插入
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
