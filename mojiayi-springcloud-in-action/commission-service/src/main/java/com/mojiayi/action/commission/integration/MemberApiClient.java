package com.mojiayi.action.commission.integration;

import com.mojiayi.action.commission.integration.fallback.MemberApiClientFallback;
import com.mojiayi.action.common.tool.response.CommonResp;
import com.mojiayi.action.member.api.MemberApi;
import com.mojiayi.action.member.response.MemberDetailResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author mojiayi
 */
@FeignClient(value = "member-service", path = "/api/member-service/members", fallback = MemberApiClientFallback.class)
public interface MemberApiClient extends MemberApi {
}
