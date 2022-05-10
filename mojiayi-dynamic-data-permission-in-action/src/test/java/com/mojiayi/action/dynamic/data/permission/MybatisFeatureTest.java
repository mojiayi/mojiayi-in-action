package com.mojiayi.action.dynamic.data.permission;


import com.mojiayi.action.common.tool.response.PagingResp;
import com.mojiayi.action.dynamic.data.permission.domain.LogiReview;
import com.mojiayi.action.dynamic.data.permission.service.OrderDetailService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MojiayiDynamicDataPermissionProvider.class)
public class MybatisFeatureTest {
    @Resource
    private OrderDetailService orderDetailService;

    @Test
    public void testSelectByOrderId() {
        String orderId = "988A45122D7760E344069E794C42D90D";
        LogiReview logiReview = orderDetailService.selectByOrderId(orderId);
        Assert.assertNotNull(logiReview);
        Assert.assertEquals("00EA17C9DA7642E9A8004C925C338981", logiReview.getReviewId());
    }

    @Test
    public void testSelectAvailableRecord() {
        int pageIndex = 1;
        int pageSize = 10;
        PagingResp<LogiReview> resp = orderDetailService.selectAvailableRecord(pageIndex, pageSize);
        Assert.assertNotNull(resp);
        Assert.assertEquals(20, resp.getTotalItem().longValue());
        Assert.assertEquals("7DF473E758594DE8B19B7459C6299076", resp.getList().get(0).getReviewId());
        Assert.assertEquals("2A34916BD66E456FB46319276934290D", resp.getList().get(8).getReviewId());
    }
}
