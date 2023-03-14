package com.mojiayi.action.excel.dto;

import lombok.Data;

/**
 * <p>
 * 导入数据转换成domain类中相应字段类型的值
 * </p>
 *
 * @author mojiayi
 */
@Data
public class FieldNameValue {
    /**
     * domain类中的字段名
     */
    private String name;

    /**
     * 字段值
     */
    private Object value;

    public FieldNameValue() {
    }

    public FieldNameValue(String name, Object value) {
        this.name = name;
        this.value = value;
    }
}
