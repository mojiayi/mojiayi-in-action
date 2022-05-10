package com.mojiayi.action.dynamic.data.permission.service;

import com.mojiayi.action.dynamic.data.permission.dao.UserInfoMapper;
import com.mojiayi.action.dynamic.data.permission.domain.UserInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author mojiayi
 */
@Service
public class UserInfoService {
    @Resource
    private UserInfoMapper userInfoMapper;

    public UserInfo selectByUserId(Long userId) {
        return userInfoMapper.selectById(userId);
    }
}
