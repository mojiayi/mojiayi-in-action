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
    public StockSolution findBestSolutionTransactMutilTimes(List<StockMetadata> stockList) {
        int stockSize = stockList.size();
        int chosenX = 0;
        int chosenY = 0;
        int profit = 0;
        List<SellOrBuyMetadata> sellOrBuyList = new ArrayList<>();
        for (int x = 0; x < stockSize; x++) {
            int y = x + 1;
            for (; y < stockSize; y++) {
                int differ = stockList.get(y).getPrice() - stockList.get(x).getPrice();
                if (differ <= 0) {
                    x++;
                    continue;
                }
                boolean matchFirstTime = chosenX == 0 && chosenY == 0;
                if (matchFirstTime) {
                    chosenX = x;
                    chosenY = y;
                    profit += differ;
                    x++;
                    addSellOrBuyMetadata(stockList, x, y, sellOrBuyList);
                    continue;
                }
                boolean skipNextDay = chosenX + 1 <= x && chosenY + 1 <= y;
                if (skipNextDay) {
                    chosenX = x;
                    chosenY = y;
                    profit += differ;
                    x++;
                    addSellOrBuyMetadata(stockList, x, y, sellOrBuyList);
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

    @Override
    public int findBestSolutionTransactMutilTimes(int[] prices) {
        int stockSize = prices.length;
        int chosenX = 0;
        int chosenY = 0;
        int profit = 0;
        for (int x = 0; x < stockSize; x++) {
            int y = x + 1;
            for (; y < stockSize; y++) {
                int differ = prices[y] - prices[x];
                if (differ <= 0) {
                    x++;
                    continue;
                }
                boolean matchFirstTime = chosenX == 0 && chosenY == 0;
                if (matchFirstTime) {
                    chosenX = x;
                    chosenY = y;
                    profit += differ;
                    x++;
                    continue;
                }
                boolean skipNextDay = chosenX + 1 <= x && chosenY + 1 <= y;
                if (skipNextDay) {
                    chosenX = x;
                    chosenY = y;
                    profit += differ;
                    x++;
                }
            }
        }
        return profit;
    }

    @Override
    public int findBestSolutionTransactSingleTime(int[] prices) {
        // 这个实现是从leetcode上抄的，我自己之前写的是用两个for循环实现的，时间复杂度太高
        int minPrice = Integer.MAX_VALUE;
        int profit = 0;
        for (int price : prices) {
            if (price < minPrice) {
                minPrice = price;
            } else {
                int differ = price - minPrice;
                if (differ > profit) {
                    profit = differ;
                }
            }
        }
        return profit;
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
