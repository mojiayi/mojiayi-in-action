package com.mojiayi.action.member.domain;

import lombok.Data;

import java.util.Date;

/**
 * @author mojiayi
 */
@Data
public class UserInfo {
    private Long id;

    private String username;

    private String countryCode;

    private String cityCode;

    private Date createTime;

    private Date updateTime;

    private Byte deleteFlag;
}