package com.mojiayi.action.excel;


import com.mojiayi.action.excel.annotation.FixedColumn;

import java.io.Serializable;

/**
 * <p>
 * 导出数据的示例对象，不要用于正式业务中
 * </p>
 *
 * @author mojiayi
 */
public class DemoFixedSingleHeader implements Serializable {
    @FixedColumn(name = "A列", index = 0)
    private String columnA;
    @FixedColumn(name = "B列", index = 1)
    private String columnB;
    @FixedColumn(name = "C列", index = 2)
    private String columnC;

    public DemoFixedSingleHeader(String columnA, String columnB, String columnC) {
        this.columnA = columnA;
        this.columnB = columnB;
        this.columnC = columnC;
    }

    public DemoFixedSingleHeader() {
    }

    public String getColumnA() {
        return columnA;
    }

    public void setColumnA(String columnA) {
        this.columnA = columnA;
    }

    public String getColumnB() {
        return columnB;
    }

    public void setColumnB(String columnB) {
        this.columnB = columnB;
    }

    public String getColumnC() {
        return columnC;
    }

    public void setColumnC(String columnC) {
        this.columnC = columnC;
    }
}
