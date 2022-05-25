package com.mojiayi.action.member.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.mojiayi.action.member.domain.UserInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author mojiayi
 */
public interface UserInfoMapper extends BaseMapper<UserInfo> {
    List<UserInfo> fuzzySearchByUsername(@Param("username") String username, @Param(Constants.WRAPPER) QueryWrapper<UserInfo> wrapper);
}