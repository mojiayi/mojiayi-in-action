package com.mojiayi.action.algorithm.stock.bean;

import lombok.Data;

import java.util.List;

/**
 * 购买或出售股票的方案
 *
 * @author mojiayi
 */
@Data
public class StockSolution {
    /**
     * 利润
     */
    private Integer profit;
    /**
     * 购买或出售的顺序
     */
    private List<SellOrBuyMetadata> sellOrBuyList;
}
