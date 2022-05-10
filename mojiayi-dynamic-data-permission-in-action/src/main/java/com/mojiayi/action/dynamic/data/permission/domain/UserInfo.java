package com.mojiayi.action.dynamic.data.permission.domain;

import lombok.Data;

import java.util.Date;

/**
 * @author mojiayi
 */
@Data
public class UserInfo {
    private Long id;

    private String username;

    private String tenantsName;

    private Long tenantsId;

    private Date createTime;

    private Date updateTime;

    private Byte deleteFlag;
}