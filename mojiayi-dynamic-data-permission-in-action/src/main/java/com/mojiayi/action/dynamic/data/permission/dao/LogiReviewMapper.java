package com.mojiayi.action.dynamic.data.permission.dao;

import com.mojiayi.action.dynamic.data.permission.domain.LogiReview;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LogiReviewMapper extends BaseMapper<Long, LogiReview> {
    /**
     * 按订单id查物流评论
     *
     * @param orderId 订单id
     * @return
     */
    LogiReview selectByOrderId(@Param("orderId") String orderId);

    List<LogiReview> selectAvailableRecord();
}