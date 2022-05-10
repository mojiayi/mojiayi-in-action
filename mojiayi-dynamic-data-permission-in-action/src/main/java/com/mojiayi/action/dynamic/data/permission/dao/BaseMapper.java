package com.mojiayi.action.dynamic.data.permission.dao;

import java.io.Serializable;

public interface BaseMapper<O extends Serializable, T> {

    int deleteByPrimaryKey(O id);

    int insert(T record);

    int insertSelective(T record);

    T selectByPrimaryKey(O id);

    int updateByPrimaryKeySelective(T record);

    int updateByPrimaryKey(T record);
}
