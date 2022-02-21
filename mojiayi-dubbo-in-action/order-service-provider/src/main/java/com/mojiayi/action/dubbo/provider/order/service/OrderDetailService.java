package com.mojiayi.action.dubbo.provider.order.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mojiayi.action.common.tool.response.PagingResp;
import com.mojiayi.action.dubbo.provider.order.dao.LogiReviewMapper;
import com.mojiayi.action.dubbo.provider.order.domain.LogiReview;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author liguangri
 */
@Service
public class OrderDetailService {
    @Resource
    private LogiReviewMapper logiReviewMapper;

    public LogiReview selectByOrderId(String orderId) {
        return logiReviewMapper.selectByOrderId(orderId);
    }

    public PagingResp<LogiReview> selectAvailableRecord(int pageIndex, int pageSize) {
        Page<LogiReview> pageInfo = PageHelper.startPage(pageIndex, pageSize);
        List<LogiReview> logiReviewList = logiReviewMapper.selectAvailableRecord();
        PagingResp<LogiReview> resp = new PagingResp<>();
        resp.setList(logiReviewList);
        resp.setPageIndex(pageInfo.getPageNum());
        resp.setPageSize(pageInfo.getPageSize());
        resp.setTotalItem((int) pageInfo.getTotal());
        resp.setTotalPage(pageInfo.getPages());
        return resp;
    }
}
