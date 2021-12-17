package com.mojiayi.action.member.controller;

import cn.hutool.http.HttpStatus;
import com.google.common.base.Preconditions;
import com.mojiayi.action.common.tool.response.CommonResp;
import com.mojiayi.action.member.domain.Member;
import com.mojiayi.action.member.response.MemberDetailResp;
import com.mojiayi.action.member.service.IMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liguangri
 */
@RestController
@RequestMapping("/member")
public class MemberController {
    @Autowired
    private IMemberService memberService;

    @GetMapping("/queryDetail")
    public CommonResp<MemberDetailResp> queryDetailByMemberId(@RequestParam Long memberId) {
        Preconditions.checkArgument(memberId != null && memberId > 0, "会员id不能为空且必须大于0");

        Member member = memberService.getByMemberId(memberId);
        if (member == null) {
            return new CommonResp<>("没有查到会员详情，请重试", HttpStatus.HTTP_NOT_FOUND);
        }
        MemberDetailResp memberDetailResp = new MemberDetailResp();
        memberDetailResp.setMemberId(member.getId());
        memberDetailResp.setMemberName(member.getUsername());
        return new CommonResp<>(memberDetailResp);
    }
}
