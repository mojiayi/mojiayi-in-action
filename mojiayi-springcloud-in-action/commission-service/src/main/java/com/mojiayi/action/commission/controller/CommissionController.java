package com.mojiayi.action.commission.controller;

import cn.hutool.http.HttpStatus;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.google.common.base.Preconditions;
import com.mojiayi.action.commission.domain.CommissionDetail;
import com.mojiayi.action.commission.response.QueryCommissionDetailResp;
import com.mojiayi.action.commission.service.ICommissionService;
import com.mojiayi.action.common.tool.param.PagingParam;
import com.mojiayi.action.common.tool.response.CommonResp;
import com.mojiayi.action.common.tool.response.PagingResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liguangri
 */
@ApiSupport(author = "liguangri")
@Api(tags = "佣金")
@RestController
@RequestMapping("/commissions")
@Slf4j
public class CommissionController {
    @Autowired
    private ICommissionService commissionService;

    @ApiOperation(value = "查订单的佣金详情")
    @ApiImplicitParam(name = "orderId", value = "订单id", dataType = "Long", required = true)
    @GetMapping("/order-id/{orderId}")
    public CommonResp<QueryCommissionDetailResp> getDetailByOrderId(@PathVariable("orderId") Long orderId) {
        Preconditions.checkArgument(orderId != null && orderId > 0, "订单id不能为空且必须大于0");
        CommissionDetail commissionDetail = commissionService.queryCommissionDetailByOrderId(orderId);
        if (commissionDetail == null) {
            return CommonResp.error(HttpStatus.HTTP_NOT_FOUND, "没有查到佣金详情，请重试");
        }
        QueryCommissionDetailResp queryCommissionDetailResp = new QueryCommissionDetailResp();
        BeanUtils.copyProperties(commissionDetail, queryCommissionDetailResp);
        return CommonResp.success(queryCommissionDetailResp);
    }

    @ApiOperation(value = "查佣金明细列表", notes = "查佣金明细列表的接口直接返回null，不要调用，不要调用")
    @GetMapping("/list")
    public CommonResp<PagingResp<QueryCommissionDetailResp>> getDetailByOrderdd(PagingParam param) {
        return null;
    }
}
