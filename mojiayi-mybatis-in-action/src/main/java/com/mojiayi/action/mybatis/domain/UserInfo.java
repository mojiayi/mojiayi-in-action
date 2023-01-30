package com.mojiayi.action.mybatis.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;

/**
 * @author mojiayi
 */
@Data
public class UserInfo {
    private Long id;

    private String username;

    private String tenantName;

    private Long tenantId;

    private Date createTime;

    private Date updateTime;

    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;

    private Byte deleteFlag;
}