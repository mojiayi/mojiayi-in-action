package com.mojiayi.action.member.controller;

import cn.hutool.http.HttpStatus;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.google.common.base.Preconditions;
import com.mojiayi.action.common.tool.response.CommonResp;
import com.mojiayi.action.member.domain.Member;
import com.mojiayi.action.member.response.MemberDetailResp;
import com.mojiayi.action.member.service.IMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liguangri
 */
@Api(tags = "会员信息")
@RestController
@RequestMapping("/members")
public class MemberController {
    @Autowired
    private IMemberService memberService;

    @ApiOperationSupport(author = "liguangri")
    @ApiOperation(value = "查会员详情")
    @ApiImplicitParam(name = "memberId", value = "会员id", dataType = "Long", required = true)
    @GetMapping("/{memberId}")
    public CommonResp<MemberDetailResp> getDetailByMemberId(@PathVariable("memberId") Long memberId) {
        Preconditions.checkArgument(memberId != null && memberId > 0, "会员id不能为空且必须大于0");
        Member member = memberService.getByMemberId(memberId);
        if (member == null) {
            return CommonResp.error(HttpStatus.HTTP_NOT_FOUND, "没有查到会员详情，请重试");
        }
        MemberDetailResp memberDetailResp = new MemberDetailResp();
        memberDetailResp.setMemberId(member.getId());
        memberDetailResp.setMemberName(member.getUsername());
        return CommonResp.success(memberDetailResp);
    }
}
