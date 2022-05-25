package com.mojiayi.action.member.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mojiayi.action.member.domain.UserInfo;

import java.util.List;

/**
 * <p>
 * 用户信息服务
 * </p>
 *
 * @author guangri.li
 * @since 2022/5/25 22:11
 */
public interface IUserInfoService {
    /**
     * 根据用户账号模糊查询用户信息
     * @param username 用户名
     * @return 返回符合条件的用户信息
     */
    List<UserInfo> fuzzySearchByUsername(String username);

    /**
     * 分页查询用户信息
     * @param pageIndex 当前页码
     * @param pageSize 每页条数
     * @return 返回符合条件的用户信息
     */
    IPage<UserInfo> paging(int pageIndex, int pageSize);

    /**
     * 物理删除用户信息
     * @param username 用户名
     * @return 删除成功返回 1，否则返回 0
     */
    int deleteByUsername(String username);

    /**
     * 修改用户信息
     * @param userInfo 新信息
     * @return 修改成功返回 1，否则返回 0
     */
    int modify(UserInfo userInfo);
}
