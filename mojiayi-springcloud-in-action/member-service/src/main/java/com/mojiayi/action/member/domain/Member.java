package com.mojiayi.action.member.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @author mojiayi
 */
@Data
public class Member implements Serializable {
    private Long id;
    private String username;
    private String password;
    private byte gender;
}
