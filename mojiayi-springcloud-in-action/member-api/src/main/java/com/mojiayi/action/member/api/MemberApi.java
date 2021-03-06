package com.mojiayi.action.member.api;

import com.mojiayi.action.common.tool.response.CommonResp;
import com.mojiayi.action.member.response.MemberDetailResp;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <p>
 * δΌεζδ½
 * </p>
 *
 * @author mojiayi
 */
public interface MemberApi {
    @GetMapping("/detail")
    CommonResp<MemberDetailResp> getDetailByMemberId(@RequestParam("memberId") Long memberId);
}
