package com.mojiayi.action.commission.response;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author liguangri
 */
@Data
public class QueryCommissionDetailResp implements Serializable {
    private Long memberId;
    private String memberName;
    private Long orderId;
    private BigDecimal amount;
    private Date obtainTime;
}
