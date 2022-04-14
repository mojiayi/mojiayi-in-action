package com.mojiayi.action.algorithm.sort.impl;

import com.mojiayi.action.algorithm.sort.ISorting;

/**
 * 使用递归实现的快速排序算法
 *
 * @author liguangri
 */
public class QuickSortingTraditional implements ISorting {
    @Override
    public int[] sort(int[] dataList) {
        int length = dataList.length;
        if (length == 0) {
            return dataList;
        }

        // 1. 从数组头往后找大于基数的元素，从数组尾往前找小于基数的元素
        quickSort(dataList, 0, length - 1);

        return dataList;
    }

    private void quickSort(int[] dataList, int leftIndex, int rightIndex) {
        if (leftIndex >= rightIndex) {
            // 2. 如果左右两个下标值相遇，说明已经完成本轮递归
            return;
        }
        int m = leftIndex;
        int n = rightIndex;
        // 3. 取数组的第一个元素作为基准值
        int benchmark = dataList[m];
        while (m < n) {
            while (m < n && dataList[n] >= benchmark) {
                // 4. 从右往左查找大于等于基准值的元素
                n--;
            }
            if (m < n) {
                // 5. 找到大于等于基准值的元素后，把这个元素填充到取得基准值的位置
                dataList[m] = dataList[n];
                // 6. 左起查找的下标值+1，不再比较刚刚被重新设置的值
                m++;
            }
            while (m < n && dataList[m] < benchmark) {
                // 7. 从左往右查找小于基准值的元素
                m++;
            }
            if (m < n) {
                // 8. 找到小于基准值的元素后，把这个元素值填充到第5步里被迁移了的元素位置
                dataList[n] = dataList[m];
                // 9. 右起查找的下标值-1，不再比较刚刚被重新设置的值
                n--;
            }
        }
        // 10. 把基准值填入第8步里被迁移了的元素位置
        dataList[m] = benchmark;
        // 11. 对本轮基准元素左侧的元素排序
        quickSort(dataList, leftIndex, m -1);
        // 12. 对本轮基准元素右侧的元素排序
        quickSort(dataList, m + 1, rightIndex);
    }
}
