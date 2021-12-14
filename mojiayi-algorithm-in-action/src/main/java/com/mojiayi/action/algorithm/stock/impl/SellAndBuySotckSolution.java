package com.mojiayi.action.algorithm.stock.impl;

import com.mojiayi.action.algorithm.stock.ISellAndBuyStock;
import com.mojiayi.action.algorithm.stock.bean.SellOrBuyMetadata;
import com.mojiayi.action.algorithm.stock.bean.StockMetadata;
import com.mojiayi.action.algorithm.stock.bean.StockSolution;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liguangri
 */
public class SellAndBuySotckSolution implements ISellAndBuyStock {
    @Override
    public StockSolution findBestSolution(List<StockMetadata> stockList) {
        boolean isDiminishing = isDiminishingList(stockList);
        // 没有利润，直接返回空
        if (isDiminishing) {
            return null;
        }
        return sellAndBuyStock(stockList);
    }

    @Override
    public int findBestSolution(int[] prices) {
        int stockSize = prices.length;
        boolean isDiminishing = true;
        for (int index = 0; index < stockSize; index++) {
            if (index == stockSize - 1) {
                continue;
            }
            isDiminishing = prices[index + 1] <= prices[index];
            if (!isDiminishing) {
                break;
            }
        }
        if (isDiminishing) {
            return 0;
        }

        Integer[][] differArray = new Integer[stockSize][stockSize];
        for (int x = 0; x < stockSize; x++) {
            int y = x + 1;
            for (; y < stockSize; y++) {
                int differ = prices[y] - prices[x];
                if (differ > 0) {
                    differArray[x][y] = differ;
                }
            }
        }
        int chosenX = 0;
        int chosenY = 0;
        int profit = 0;
        for (int x = 0; x < stockSize; x++) {
            int y = x + 1;
            for (; y < stockSize; y++) {
                if (differArray[x][y] == null) {
                    x++;
                    continue;
                }
                if (chosenX == 0 && chosenY == 0) {
                    chosenX = x;
                    chosenY = y;
                    profit += differArray[x][y];
                    x++;
                    continue;
                }
                if (chosenX + 1 <= x && chosenY + 1 <= y) {
                    chosenX = x;
                    chosenY = y;
                    profit += differArray[x][y];
                    x++;
                }
            }
        }
        return profit;
    }

    /**
     * 从下标0开始比较相邻两支股票的价格，如果每组股票的后面一支都小于或等于前一支，
     * 股票列表在价格上是一个递减数组，或新价格等于旧价格，利润不可能大于0
     *
     * @param stockList 预设的股票价格变化趋势数据
     * @return 返回true表示不可能有利润，否则返回false
     */
    private boolean isDiminishingList(List<StockMetadata> stockList) {
        int stockSize = stockList.size();
        boolean isDiminishing = true;
        for (int index = 0; index < stockSize; index++) {
            if (index == stockSize - 1) {
                continue;
            }
            isDiminishing = stockList.get(index + 1).getPrice() <= stockList.get(index).getPrice();
            if (!isDiminishing) {
                break;
            }
        }
        return isDiminishing;
    }

    /**
     * 找出利润最大的股票买卖方案
     *
     * @param stockList 预设的股票价格变化趋势数据
     * @return 返回润最大的股票买卖方案
     */
    private StockSolution sellAndBuyStock(List<StockMetadata> stockList) {
        int stockSize = stockList.size();
        Integer[][] differArray = new Integer[stockSize][stockSize];
        for (int x = 0; x < stockSize; x++) {
            int y = x + 1;
            for (; y < stockSize; y++) {
                int differ = stockList.get(y).getPrice() - stockList.get(x).getPrice();
                if (differ > 0) {
                    differArray[x][y] = differ;
                }
            }
        }
        int chosenX = 0;
        int chosenY = 0;
        int profit = 0;
        List<SellOrBuyMetadata> sellOrBuyList = new ArrayList<>();
        for (int x = 0; x < stockSize; x++) {
            int y = x + 1;
            for (; y < stockSize; y++) {
                if (differArray[x][y] == null) {
                    x++;
                    continue;
                }
                if (chosenX == 0 && chosenY == 0) {
                    addSellOrBuyMetadata(stockList, x, y, sellOrBuyList);
                    chosenX = x;
                    chosenY = y;
                    profit += differArray[x][y];
                    x++;
                    continue;
                }
                if (chosenX + 1 <= x && chosenY + 1 <= y) {
                    addSellOrBuyMetadata(stockList, x, y, sellOrBuyList);
                    chosenX = x;
                    chosenY = y;
                    profit += differArray[x][y];
                    x++;
                }
            }
        }

        StockSolution solution = null;
        if (profit > 0) {
            solution = new StockSolution();
            solution.setProfit(profit);
            solution.setSellOrBuyList(sellOrBuyList);
        }
        return solution;
    }

    private void addSellOrBuyMetadata(List<StockMetadata> stockList, int x, int y,
                                      List<SellOrBuyMetadata> sellOrBuyList) {
        SellOrBuyMetadata buyMetadata = new SellOrBuyMetadata();
        buyMetadata.setBuyFlag(true);
        buyMetadata.setIndex(x);
        buyMetadata.setPrice(stockList.get(x).getPrice());
        sellOrBuyList.add(buyMetadata);

        SellOrBuyMetadata sellMetadata = new SellOrBuyMetadata();
        sellMetadata.setBuyFlag(false);
        sellMetadata.setIndex(y);
        sellMetadata.setPrice(stockList.get(y).getPrice());
        sellOrBuyList.add(sellMetadata);
    }
}
