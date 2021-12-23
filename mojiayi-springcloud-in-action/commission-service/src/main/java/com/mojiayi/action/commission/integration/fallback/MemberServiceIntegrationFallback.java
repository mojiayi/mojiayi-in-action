package com.mojiayi.action.commission.integration.fallback;

import cn.hutool.http.HttpStatus;
import com.mojiayi.action.commission.integration.MemberServiceIntegration;
import com.mojiayi.action.common.tool.response.CommonResp;
import com.mojiayi.action.member.response.MemberDetailResp;
import org.springframework.stereotype.Component;

/**
 * @author liguangri
 */
@Component
public class MemberServiceIntegrationFallback implements MemberServiceIntegration {
    @Override
    public CommonResp<MemberDetailResp> getDetailByMemberId(Long memberId) {
        return new CommonResp<>("查询会员详情失败", HttpStatus.HTTP_INTERNAL_ERROR);
    }
}
