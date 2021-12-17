package com.mojiayi.action.member.service.mock;

import com.mojiayi.action.member.domain.Member;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @author liguangri
 */
@Slf4j
public class MockMemberData {
    public static Map<Long, Member> mockDataMap = new HashMap<>();

    static {
        try {
            Member detail = new Member();
            detail.setId(123L);
            detail.setUsername("1号会员");
            mockDataMap.put(detail.getId(), detail);

            Member detail2 = new Member();
            detail2.setId(234L);
            detail2.setUsername("2号会员");
            mockDataMap.put(detail2.getId(), detail2);

            Member detail3 = new Member();
            detail3.setId(345L);
            detail3.setUsername("3号会员");
            mockDataMap.put(detail3.getId(), detail3);
        } catch (Exception e) {
            log.error("生成模拟会员数据报错", e);
            System.exit(1);
        }
    }
}
