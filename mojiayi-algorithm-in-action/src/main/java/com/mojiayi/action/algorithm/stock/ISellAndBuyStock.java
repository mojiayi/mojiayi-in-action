package com.mojiayi.action.algorithm.stock;

import com.mojiayi.action.algorithm.stock.bean.StockMetadata;
import com.mojiayi.action.algorithm.stock.bean.StockSolution;

import java.util.List;

/**
 * leetcode上的最佳股票买卖方案
 *
 * @author liguangri
 */
public interface ISellAndBuyStock {
    /**
     * 从给定的股票价格变化趋势数据中，找出利润最大的买卖方案
     *
     * @param stockList 预设的股票价格变化趋势数据
     * @return 如果有利润大于0的买卖方案，返回得出的方案，否则返回null
     */
    StockSolution findBestSolution(List<StockMetadata> stockList);

    /**
     * 按leetcode中定义的传入参数和返回结果，找出股票买卖的最大利润
     * @param prices 预设的股票价格变化趋势数据
     * @return 返回利润最大的买卖方案，如果没有利润就返回0
     */
    int findBestSolution(int[] prices);
}
