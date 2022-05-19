package com.mojiayi.action.mybatis.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mojiayi.action.mybatis.dao.UserInfoMapper;
import com.mojiayi.action.mybatis.domain.UserInfo;
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
