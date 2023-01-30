package com.mojiayi.action.mybatis.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mojiayi.action.mybatis.constants.MyConstants;
import com.mojiayi.action.mybatis.dao.UserInfoMapper;
import com.mojiayi.action.mybatis.domain.UserInfo;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author mojiayi
 */
@DS("datasource-sys")
@Service
public class UserInfoService {
    @Resource
    private UserInfoMapper userInfoMapper;

    public UserInfo registerNewUser(String username) {
        UserInfo userInfo = userInfoMapper.selectByUsername(username);
        if (userInfo != null) {
            return null;
        }
        String tenantId = MDC.get(MyConstants.TENANT_ID);
        userInfo = new UserInfo();
        userInfo.setUsername(username);
        // 为演示方便，租户名简单地这样拼写
        userInfo.setTenantName("租户" + tenantId);
        userInfoMapper.insert(userInfo);
        return userInfoMapper.selectByUsername(username);
    }

    public UserInfo deleteUser(String username) {
        UserInfo userInfo = userInfoMapper.selectByUsername(username);
        if (userInfo == null) {
            return null;
        }

        userInfoMapper.deleteByUsername(username);
        return userInfo;
    }

    public UserInfo modifyUsernameByUserId(Long userId, String username) {
        UserInfo userInfo = userInfoMapper.selectById(userId);
        if (userInfo == null) {
            return null;
        }
        userInfo.setUsername(username);
        userInfoMapper.updateById(userInfo);
        return userInfoMapper.selectById(userId);
    }

    public UserInfo selectByUserId(Long userId) {
        return userInfoMapper.selectById(userId);
    }

    @DS("datasource-world")
    public List<UserInfo> pagination(int pageIndex, int pageSize) {
        IPage<UserInfo> pageInfo = new Page<>(pageIndex, pageSize);
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserInfo::getUsername, "用户2-1world");
        userInfoMapper.selectPage(pageInfo, queryWrapper);
        return pageInfo.getRecords();
    }

    @DS("datasource-world")
    public UserInfo selectByUserIdFromWorld(Long userId) {
        return userInfoMapper.selectById(userId);
    }
}
