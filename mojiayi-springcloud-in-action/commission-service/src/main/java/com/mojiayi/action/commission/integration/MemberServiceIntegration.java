package com.mojiayi.action.commission.integration;

import com.mojiayi.action.commission.integration.fallback.MemberServiceIntegrationFallback;
import com.mojiayi.action.common.tool.response.CommonResp;
import com.mojiayi.action.member.response.MemberDetailResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    @GetMapping("api/member/queryDetail")
    CommonResp<MemberDetailResp> queryMemberDetailByMemberId(@RequestParam Long memberId);
}
