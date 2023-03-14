package com.mojiayi.action.excel.dto;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 列数固定的表格表头定义
 * </p>
 *
 * @author mojiayi
 */
public class FixedTableHeader implements Serializable {
    /**
     * 表头名称
     */
    private String name;
    /**
     * 表头对应的属性
     */
    private String prop;
    /**
     * 从左到右的顺序值
     */
    private int index;
    /**
     * 子级表头
     */
    private List<FixedTableHeader> children;

    public FixedTableHeader() {
    }

    public FixedTableHeader(String prop, String name, int index) {
        this.name = name;
        this.prop = prop;
        this.index = index;
    }

    public FixedTableHeader(String prop, String name, int index, List<FixedTableHeader> children) {
        this.name = name;
        this.prop = prop;
        this.index = index;
        this.children = children;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProp() {
        return prop;
    }

    public void setProp(String prop) {
        this.prop = prop;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public List<FixedTableHeader> getChildren() {
        return children;
    }

    public void setChildren(List<FixedTableHeader> children) {
        this.children = children;
    }
}
