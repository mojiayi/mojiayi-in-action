package com.mojiayi.action.algorithm;

import com.mojiayi.action.algorithm.stock.ISellAndBuyStock;
import com.mojiayi.action.algorithm.stock.bean.SellOrBuyMetadata;
import com.mojiayi.action.algorithm.stock.bean.StockMetadata;
import com.mojiayi.action.algorithm.stock.bean.StockSolution;
import com.mojiayi.action.algorithm.stock.impl.SellAndBuySotckSolution;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class SellAndBuyStockTest {
    private static List<StockMetadata> stockList = new ArrayList<>(6);
    private static List<StockMetadata> stockList2 = new ArrayList<>(5);
    private static List<StockMetadata> stockList3 = new ArrayList<>(5);
    private static int[] priceArray = new int[]{7, 1, 5, 3, 6, 4};
    private static int[] priceArray2 = new int[]{1, 2, 3, 4, 5};
    private static int[] priceArray3 = new int[]{7, 6, 4, 3, 1};
    private static int[] priceArray4 = new int[]{2, 1, 4};
    private static int[] priceArray5 = new int[]{3,2,6,5,0,3};

    static {
        for (int index = 0; index < priceArray.length; index++) {
            StockMetadata stockMetadata = new StockMetadata();
            stockMetadata.setIndex(index);
            stockMetadata.setPrice(priceArray[index]);
            stockList.add(stockMetadata);
        }
        for (int index = 0; index < priceArray2.length; index++) {
            StockMetadata stockMetadata = new StockMetadata();
            stockMetadata.setIndex(index);
            stockMetadata.setPrice(priceArray2[index]);
            stockList2.add(stockMetadata);
        }
        for (int index = 0; index < priceArray3.length; index++) {
            StockMetadata stockMetadata = new StockMetadata();
            stockMetadata.setIndex(index);
            stockMetadata.setPrice(priceArray3[index]);
            stockList3.add(stockMetadata);
        }
    }

    @Test
    public void testFindBestSolution() {
        ISellAndBuyStock sellAndBuyStock = new SellAndBuySotckSolution();

        // 输入: prices = [7,1,5,3,6,4]
        StockSolution solution = sellAndBuyStock.findBestSolutionTransactMutilTimes(stockList);
        assert solution != null;
        assert solution.getProfit() == 7;
        verifySolution(solution, stockList);

        // 输入: prices = [1,2,3,4,5]
        solution = sellAndBuyStock.findBestSolutionTransactMutilTimes(stockList2);
        assert solution != null;
        assert solution.getProfit() == 4;
        verifySolution(solution, stockList2);

        // 输入: prices = [7,6,4,3,1]
        solution = sellAndBuyStock.findBestSolutionTransactMutilTimes(stockList3);
        assert solution == null;
    }

    private void verifySolution(StockSolution solution, List<StockMetadata> stockList) {
        assert solution != null;
        List<SellOrBuyMetadata> chosenStockList = solution.getSellOrBuyList();
        assert chosenStockList != null && chosenStockList.size() > 0;
        int chosenStockSize = chosenStockList.size();
        for (int index = 0;index< chosenStockSize;index++) {
            SellOrBuyMetadata chosenStock = chosenStockList.get(index);
            assert chosenStock.getIndex().intValue() == stockList.get(chosenStock.getIndex()).getIndex();
            assert chosenStock.getPrice().intValue() == stockList.get(chosenStock.getIndex()).getPrice();
            if (index % 2 == 0) {
                assert chosenStock.getBuyFlag();
            } else {
                assert !chosenStock.getBuyFlag();
            }
        }
    }

    @Test
    public void testFindBestSolutionTransactSingleTime() {
        ISellAndBuyStock sellAndBuyStock = new SellAndBuySotckSolution();
        int profit = sellAndBuyStock.findBestSolutionTransactSingleTime(priceArray);
        Assert.assertEquals(5, profit);
        profit = sellAndBuyStock.findBestSolutionTransactSingleTime(priceArray2);
        Assert.assertEquals(4, profit);
        profit = sellAndBuyStock.findBestSolutionTransactSingleTime(priceArray3);
        Assert.assertEquals(0, profit);
        profit = sellAndBuyStock.findBestSolutionTransactSingleTime(priceArray4);
        Assert.assertEquals(3, profit);
        profit = sellAndBuyStock.findBestSolutionTransactSingleTime(priceArray5);
        Assert.assertEquals(4, profit);
    }
}
