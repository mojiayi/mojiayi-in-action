package com.mojiayi.action.excel.dto;

import lombok.Data;

import java.util.List;

/**
 * <p>
 * 导出模板文件过程中的信息传递对象
 * </p>
 *
 * @author mojiayi
 */
@Data
public class ExcelTemplateDTO {
    /**
     * 表头
     */
    List<FixedTableHeader> headerList;

    /**
     * 导出模板过程中的错误信息
     */
    private String errMsg;
}
