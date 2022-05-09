package com.mojiayi.action.commission.integration.fallback;

import cn.hutool.http.HttpStatus;
import com.mojiayi.action.commission.integration.MemberApiClient;
import com.mojiayi.action.common.tool.response.CommonResp;
import com.mojiayi.action.member.response.MemberDetailResp;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author mojiayi
 */
@Component
public class MemberApiClientFallback implements MemberApiClient {
    @Override
    public CommonResp<MemberDetailResp> getDetailByMemberId(@RequestParam("memberId")Long memberId) {
        return CommonResp.error(HttpStatus.HTTP_INTERNAL_ERROR, "查询会员详情失败");
    }
}
