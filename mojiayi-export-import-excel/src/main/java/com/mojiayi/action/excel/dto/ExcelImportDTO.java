package com.mojiayi.action.excel.dto;

import lombok.Data;

import java.util.List;

/**
 * <p>
 * 导入Excel文件过程中的信息传递对象
 * </p>
 *
 * @author mojiayi
 */
@Data
public class ExcelImportDTO {
    /**
     * 导入文件中的表头
     */
    List<String> headerNameList;

    /**
     * 导入文件中的业务数据
     */
    List<List<String>> dataRowList;

    /**
     * 除文件内容不合法之外的其它导入过程错误
     */
    private String errMsg;
}
