package com.mojiayi.action.member.service.impl;

import com.mojiayi.action.member.domain.Member;
import com.mojiayi.action.member.service.IMemberService;
import com.mojiayi.action.member.service.mock.MockMemberData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author liguangri
 */
@Component
@Slf4j
public class MemberServiceImpl implements IMemberService {
    @Override
    public Member getByMemberId(Long memberId) {
        return MockMemberData.mockDataMap.get(memberId);
    }
}
