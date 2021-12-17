package com.mojiayi.action.member.response;

import lombok.Data;

import java.io.Serializable;

/**
 * @author liguangri
 */
@Data
public class MemberDetailResp implements Serializable {
    private Long memberId;
    private String memberName;
    private byte gender;
}
