package com.mojiayi.action.commission.service.mock;

import cn.hutool.core.date.DatePattern;
import com.mojiayi.action.commission.domain.CommissionDetail;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mojiayi
 */
@Slf4j
public class MockCommissionData {
    public static Map<Long, CommissionDetail> mockDataMap = new HashMap<>();

    static {
        try {
            CommissionDetail detail = new CommissionDetail();
            detail.setOrderId(1L);
            detail.setAmount(new BigDecimal(100));
            detail.setMemberId(123L);
            // 会员名从会员服务获取
            detail.setObtainTime(DatePattern.NORM_DATETIME_FORMAT.parse("2021-11-11 10:00:00"));
            mockDataMap.put(detail.getOrderId(), detail);

            CommissionDetail detail2 = new CommissionDetail();
            detail2.setOrderId(2L);
            detail2.setAmount(new BigDecimal(200));
            detail2.setMemberId(234L);
            // 会员名从会员服务获取
            detail2.setObtainTime(DatePattern.NORM_DATETIME_FORMAT.parse("2021-11-12 10:00:00"));
            mockDataMap.put(detail2.getOrderId(), detail2);

            CommissionDetail detail3 = new CommissionDetail();
            detail3.setOrderId(3L);
            detail3.setAmount(new BigDecimal(300));
            detail3.setMemberId(345L);
            // 会员名从会员服务获取
            detail3.setObtainTime(DatePattern.NORM_DATETIME_FORMAT.parse("2021-11-13 10:00:00"));
            mockDataMap.put(detail3.getOrderId(), detail3);
        } catch (Exception e) {
            log.error("生成模拟佣金数据报错", e);
            System.exit(1);
        }
    }
}
