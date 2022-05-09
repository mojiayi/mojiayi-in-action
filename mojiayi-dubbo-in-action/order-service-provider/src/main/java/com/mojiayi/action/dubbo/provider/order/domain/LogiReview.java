package com.mojiayi.action.dubbo.provider.order.domain;

import lombok.Data;

import java.util.Date;

/**
 * @author mojiayi
 */
@Data
public class LogiReview {
    private Long id;

    private String reviewId;

    private String orderId;

    private String orderNo;

    private String conNo;

    private Integer logiSpeedStar;

    private Integer logiPackStar;

    private Long version;

    private Byte status;

    private Byte enabled;

    private Date createTime;

    private String createBy;

    private Date updateTime;

    private String updateBy;

    private String remark;
}