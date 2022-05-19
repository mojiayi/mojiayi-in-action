package com.mojiayi.action.mybatis.domain;

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

    private Byte deleteFlag;
}