package com.mojiayi.action.commission.integration;

import com.mojiayi.action.commission.integration.fallback.MemberServiceIntegrationFallback;
import com.mojiayi.action.common.tool.response.CommonResp;
import com.mojiayi.action.member.response.MemberDetailResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author liguangri
 */
@FeignClient(value = "member-service", fallback = MemberServiceIntegrationFallback.class)
public interface MemberServiceIntegration {
    /**
     *
     * @param memberId 会员id
     * @return
     */
    @GetMapping("api/member-service/members/{memberId}")
    CommonResp<MemberDetailResp> getDetailByMemberId(@PathVariable("memberId") Long memberId);
}
