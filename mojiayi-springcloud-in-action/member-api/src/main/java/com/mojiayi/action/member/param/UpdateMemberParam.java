package com.mojiayi.action.member.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author mojiayi
 */
@ApiModel("更新会员信息的参数")
@Data
public class UpdateMemberParam implements Serializable {
    @ApiModelProperty("会员id")
    private Long memberId;
    @ApiModelProperty("会员名")
    private String memberName;
    @ApiModelProperty("性别")
    private byte gender;
}
