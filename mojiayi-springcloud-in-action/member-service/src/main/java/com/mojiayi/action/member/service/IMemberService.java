package com.mojiayi.action.member.service;

import com.mojiayi.action.member.domain.Member;

/**
 * @author liguangri
 */
public interface IMemberService {
    /**
     *
     * @param memberId
     * @return
     */
    Member getByMemberId(Long memberId);
}
