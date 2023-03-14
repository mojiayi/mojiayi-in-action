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
public class DemoFixedDoubleHeader implements Serializable {
    @FixedColumn(name = "B列", index = 0)
    private String columnB;
    @FixedColumn(name = "C列", index = 1)
    private String columnC;
    @FixedColumn(name = "D列", index = 2, parentProp = "columnG")
    private String columnD;
    @FixedColumn(name = "E列", index = 3, parentProp = "columnG")
    private String columnE;
    @FixedColumn(name = "F列", index = 4, parentProp = "columnG")
    private String columnF;
    @FixedColumn(name = "G列", index = 5)
    private String columnG;
    @FixedColumn(name = "H列", index = 6, parentProp = "columnJ")
    private String columnH;
    @FixedColumn(name = "I列", index = 7, parentProp = "columnJ")
    private String columnI;
    @FixedColumn(name = "J列", index = 8)
    private String columnJ;
    @FixedColumn(name = "合计", index = 9)
    private String columnTotal;

    public DemoFixedDoubleHeader() {
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

    public String getColumnD() {
        return columnD;
    }

    public void setColumnD(String columnD) {
        this.columnD = columnD;
    }

    public String getColumnE() {
        return columnE;
    }

    public void setColumnE(String columnE) {
        this.columnE = columnE;
    }

    public String getColumnF() {
        return columnF;
    }

    public void setColumnF(String columnF) {
        this.columnF = columnF;
    }

    public String getColumnG() {
        return columnG;
    }

    public void setColumnG(String columnG) {
        this.columnG = columnG;
    }

    public String getColumnH() {
        return columnH;
    }

    public void setColumnH(String columnH) {
        this.columnH = columnH;
    }

    public String getColumnI() {
        return columnI;
    }

    public void setColumnI(String columnI) {
        this.columnI = columnI;
    }

    public String getColumnJ() {
        return columnJ;
    }

    public void setColumnJ(String columnJ) {
        this.columnJ = columnJ;
    }

    public String getColumnTotal() {
        return columnTotal;
    }

    public void setColumnTotal(String columnTotal) {
        this.columnTotal = columnTotal;
    }
}
