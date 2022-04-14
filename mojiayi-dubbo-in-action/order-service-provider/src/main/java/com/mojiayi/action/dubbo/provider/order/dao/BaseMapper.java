package com.mojiayi.action.dubbo.provider.order.dao;

import java.io.Serializable;

public interface BaseMapper<O extends Serializable, T> {

    int deleteByPrimaryKey(O id);

    int insert(T record);

    int insertSelective(T record);

    T selectByPrimaryKey(O id);

    int updateByPrimaryKeySelective(T record);

    int updateByPrimaryKey(T record);
}
