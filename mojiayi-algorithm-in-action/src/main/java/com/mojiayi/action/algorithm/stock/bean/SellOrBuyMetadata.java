package com.mojiayi.action.algorithm.stock.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 购买或出售股票的元数据
 * @author liguangri
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SellOrBuyMetadata extends StockMetadata {
    /**
     * 买卖股票标记，true表示购买，false表示出售
     */
    private Boolean buyFlag;
}
