package com.mojiayi.action.commission.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author liguangri
 */
@Data
@ApiModel("更新佣金获取明细的参数")
public class UpdateCommissionDetailParam implements Serializable {
    @ApiModelProperty("会员id")
    private Long memberId;
    @ApiModelProperty("订单id")
    private Long orderId;
    @ApiModelProperty("佣金金额")
    private BigDecimal amount;
    @ApiModelProperty("佣金获得时间")
    private Date obtainTime;
}
