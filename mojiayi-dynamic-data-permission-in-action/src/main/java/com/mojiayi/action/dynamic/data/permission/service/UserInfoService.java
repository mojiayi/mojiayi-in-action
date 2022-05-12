package com.mojiayi.action.dynamic.data.permission.service;

import com.baomidou.dynamic.datasource.annotation.DS;
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

    @DS("datasource-sys")
    public UserInfo selectByUserId(Long userId) {
        return userInfoMapper.selectById(userId);
    }

    @DS("datasource-world")
    public UserInfo selectByUserIdFromWorld(Long userId) {
        return userInfoMapper.selectById(userId);
    }
}
