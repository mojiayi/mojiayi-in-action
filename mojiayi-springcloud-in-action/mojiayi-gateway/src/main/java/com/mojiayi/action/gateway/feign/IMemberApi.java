package com.mojiayi.action.gateway.feign;

import com.mojiayi.action.member.api.MemberApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * <p>
 * δΌεζε‘
 * </p>
 *
 * @author guangri.li
 * @since 2022/5/30 20:30
 */
@FeignClient(value = "member-service", contextId = "MemberApi", path = "/api/member-service/members")
public interface IMemberApi extends MemberApi {
}
