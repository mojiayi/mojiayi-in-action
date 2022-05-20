package com.mojiayi.action.commission.api;

import com.mojiayi.action.commission.response.QueryCommissionDetailResp;
import com.mojiayi.action.common.tool.param.PagingParam;
import com.mojiayi.action.common.tool.response.CommonResp;
import com.mojiayi.action.common.tool.response.PagingResp;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * <p>
 * 佣金操作
 * </p>
 *
 * @author mojiayi
 */
public interface CommissionApi {
    @GetMapping("/order-id/{orderId}")
    CommonResp<QueryCommissionDetailResp> getDetailByOrderId(@PathVariable("orderId") Long orderId);

    @GetMapping("/list")
    CommonResp<PagingResp<QueryCommissionDetailResp>> getDetailByOrderdd(PagingParam param);
}
