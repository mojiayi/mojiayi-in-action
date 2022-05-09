package com.mojiayi.action.algorithm.sort;

/**
 * 排序算法
 *
 * @author mojiayi
 */
public interface ISorting {
    /**
     * 对传入的列表进行排序
     *
     * @param dataList 待排序列表
     * @return 返回已排序列表
     */
    int[] sort(int[] dataList);
}
