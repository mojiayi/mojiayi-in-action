package com.mojiayi.action.commission.controller;

import cn.hutool.http.HttpStatus;
import com.google.common.base.Preconditions;
import com.mojiayi.action.commission.domain.CommissionDetail;
import com.mojiayi.action.commission.response.QueryCommissionDetailResp;
import com.mojiayi.action.commission.service.ICommissionService;
import com.mojiayi.action.common.tool.response.CommonResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liguangri
 */
@RestController
@RequestMapping("/commission")
@Slf4j
public class CommissionController {
    @Autowired
    private ICommissionService commissionService;

    @GetMapping("/queryDetailByOrderId")
    public CommonResp<QueryCommissionDetailResp> queryCommissionDetailByOrderId(Long orderId) {
        Preconditions.checkArgument(orderId != null && orderId > 0, "订单id不能为空且必须大于0");
        CommissionDetail commissionDetail = commissionService.queryCommissionDetailByOrderId(orderId);
        if (commissionDetail == null) {
            return new CommonResp<>("没有查到佣金详情，请重试", HttpStatus.HTTP_NOT_FOUND);
        }
        QueryCommissionDetailResp queryCommissionDetailResp = new QueryCommissionDetailResp();
        BeanUtils.copyProperties(commissionDetail, queryCommissionDetailResp);
        return new CommonResp<>(queryCommissionDetailResp);
    }
}
