package com.mojiayi.action.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mojiayi.action.dynamic.data.permission.wrapper.RowLevelQueryIsolationWrapper;
import com.mojiayi.action.dynamic.data.permission.wrapper.RowLevelUpdateIsolationWrapper;
import com.mojiayi.action.member.dao.UserInfoMapper;
import com.mojiayi.action.member.domain.UserInfo;
import com.mojiayi.action.member.service.IUserInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 用户信息服务实现类
 * </p>
 *
 * @author guangri.li
 * @since 2022/5/25 22:12
 */
@Service
public class UserInfoServiceImpl implements IUserInfoService {
    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private RowLevelQueryIsolationWrapper rowLevelQueryIsolationWrapper;

    @Resource
    private RowLevelUpdateIsolationWrapper rowLevelUpdateIsolationWrapper;

    @Override
    public List<UserInfo> fuzzySearchByUsername(String username) {
        if (!username.endsWith(StringPool.PERCENT)) {
            username += StringPool.PERCENT;
        }

        QueryWrapper<UserInfo> queryWrapper = rowLevelQueryIsolationWrapper.initQueryWrapper(new UserInfo());
        return userInfoMapper.fuzzySearchByUsername(username, queryWrapper);
    }

    @Override
    public IPage<UserInfo> paging(int pageIndex, int pageSize) {
        QueryWrapper<UserInfo> queryWrapper = rowLevelQueryIsolationWrapper.initQueryWrapper(new UserInfo());
        return userInfoMapper.selectPage(new Page<>(pageIndex, pageSize), queryWrapper);
    }

    @Override
    public int deleteByUsername(String username) {
        UserInfo searchObj = new UserInfo();
        searchObj.setUsername(username);
        QueryWrapper<UserInfo> deleteWrapper = rowLevelQueryIsolationWrapper.initQueryWrapper(searchObj);
        return userInfoMapper.delete(deleteWrapper);
    }

    @Override
    public int modify(UserInfo userInfo) {
        UserInfo updateObj = new UserInfo();
        updateObj.setId(updateObj.getId());
        UpdateWrapper<UserInfo> updateWrapper = rowLevelUpdateIsolationWrapper.initUpdateWrapper(updateObj);
        return userInfoMapper.update(userInfo, updateWrapper);
    }
}
