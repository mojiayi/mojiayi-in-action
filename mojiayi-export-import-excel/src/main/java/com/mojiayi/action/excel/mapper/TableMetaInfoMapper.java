package com.mojiayi.action.excel.mapper;

import com.mojiayi.action.excel.dto.FieldNameValue;
import com.mojiayi.action.excel.dto.TableMetaInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * TODO
 * </p>
 *
 * @author mojiayi
 */
@Mapper
public interface TableMetaInfoMapper {
    List<TableMetaInfo> selectFieldList(@Param("tableName") String tableName);

    List<Long> selectByUniqueKey(@Param("tableName") String tableName, @Param("fieldNameValueList") List<FieldNameValue> fieldNameValueList);

    int insertNewData(@Param("tableName") String tableName, @Param("fieldNameValueList") List<FieldNameValue> fieldNameValueList);

    int logicDeleteData(@Param("tableName") String tableName, @Param("id") long id, @Param("updateBy") long updateBy, @Param("updateTime") Date updateTime);
}
