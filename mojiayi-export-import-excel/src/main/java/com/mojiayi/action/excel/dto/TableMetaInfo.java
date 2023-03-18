package com.mojiayi.action.excel.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 数据库表无数据信息
 * </p>
 *
 * @author mojiayi
 */
@Data
public class TableMetaInfo implements Serializable {
    /**
     * 表名
     */
    private String tableName;
    /**
     * 字段名
     */
    @TableId
    private String columnName;
    /**
     * 字段注释
     */
    private String columnComment;
    /**
     * 字段数据类型
     */
    private String dataType;
}
