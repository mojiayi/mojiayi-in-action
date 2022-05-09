package com.mojiayi.action.commission.service;

import com.mojiayi.action.commission.domain.CommissionDetail;

/**
 * @author mojiayi
 */
public interface ICommissionService {
    /**
     * 查询订单的佣金详情
     * @param orderId 订单id
     * @return
     */
    CommissionDetail queryCommissionDetailByOrderId(Long orderId);
}
