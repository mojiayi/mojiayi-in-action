package com.mojiayi.action.member.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author liguangri
 */
@Data
@ApiModel("会员详情")
public class MemberDetailResp implements Serializable {
    @ApiModelProperty("会员id")
    private Long memberId;
    @ApiModelProperty("会员名")
    private String memberName;
    @ApiModelProperty("性别")
    private byte gender;
}
