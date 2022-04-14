package com.mojiayi.action.algorithm.stock.bean;

import lombok.Data;

/**
 * 股票元数据
 *
 * @author liguangri
 */
@Data
public class StockMetadata {
    /**
     * 天数值，从0开始
     */
    private Integer index;
    /**
     * 当天价格
     */
    private Integer price;
}
