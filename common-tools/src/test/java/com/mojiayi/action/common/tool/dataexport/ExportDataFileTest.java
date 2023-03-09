package com.mojiayi.action.common.tool.dataexport;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelWriter;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class ExportDataFileTest {
    private final ExcelExportTool excelExportTool = new ExcelExportTool();

    @Test
    public void testExportSingleLevelHeaderTable() throws IllegalAccessException, IOException, InvocationTargetException {
        List<DemoFixedSingleHeader> dataList = new ArrayList<>();
        dataList.add(new DemoFixedSingleHeader("A列一行", "B列一行", "C列一行"));
        dataList.add(new DemoFixedSingleHeader("A列二行", "B列二行", "C列二行"));
        dataList.add(new DemoFixedSingleHeader("A列三行", "B列三行", "C列三行"));

        ExcelWriter excelWriter = excelExportTool.exportExcel(dataList);
        OutputStream outputStream = new FileOutputStream(new File("d:\\tmp\\单层表头有3列.xlsx"));
        excelWriter.flush(outputStream, true);
        excelWriter.close();
        IoUtil.close(outputStream);
    }

    @Test
    public void testExportDoubleLevelHeaderTable() throws IllegalAccessException, IOException, InvocationTargetException {
        List<DemoExportData> demoExportDataList = new ArrayList<>();
        DemoExportData item = new DemoExportData();
        item.setColumnB("B列一行");
        item.setColumnC("C列一行");
        item.setColumnD("D列一行");
        item.setColumnE("E列一行");
        item.setColumnF("F列一行");
        item.setColumnH("H列一行");
        item.setColumnI("I列一行");
        item.setColumnJ("J列一行");
        item.setColumnTotal("合计一行");
        demoExportDataList.add(item);

        DemoExportData item2 = new DemoExportData();
        item2.setColumnB("B列二行");
        item2.setColumnC("C列二行");
        item2.setColumnD("D列二行");
        item2.setColumnE("E列二行");
        item2.setColumnF("F列二行");
        item2.setColumnH("H列二行");
        item2.setColumnI("I列二行");
        item2.setColumnJ("J列二行");
        item2.setColumnTotal("合计二行");
        demoExportDataList.add(item2);

        DemoExportData item3 = new DemoExportData();
        item3.setColumnB("B列三行");
        item3.setColumnC("C列三行");
        item3.setColumnD("D列三行");
        item3.setColumnE("E列三行");
        item3.setColumnF("F列三行");
        item3.setColumnH("H列三行");
        item3.setColumnI("I列三行");
        item3.setColumnJ("J列三行");
        item3.setColumnTotal("合计三行");
        demoExportDataList.add(item3);

        List<DemoFixedDoubleHeader> demoFixedDoubleHeaderList = BeanUtil.copyToList(demoExportDataList, DemoFixedDoubleHeader.class);

        ExcelWriter excelWriter = excelExportTool.exportExcel(demoFixedDoubleHeaderList);
        OutputStream outputStream = new FileOutputStream(new File("d:\\tmp\\两层表头.xlsx"));
        excelWriter.flush(outputStream, true);
        excelWriter.close();
        IoUtil.close(outputStream);
    }
}
