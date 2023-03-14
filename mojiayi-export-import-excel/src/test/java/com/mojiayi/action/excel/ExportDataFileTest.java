package com.mojiayi.action.excel;

import com.mojiayi.action.excel.core.ExcelExportTool;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * TODO
 * </p>
 *
 * @author mojiayi
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ExportDataFileTest {
    @Resource
    private ExcelExportTool excelExportTool;

    @Test
    public void testExportSingleLevelHeaderTable() throws IllegalAccessException, IOException, InvocationTargetException {
        List<DemoFixedSingleHeader> dataList = new ArrayList<>();
        dataList.add(new DemoFixedSingleHeader("A列一行", "B列一行", "C列一行"));
        dataList.add(new DemoFixedSingleHeader("A列二行", "B列二行", "C列二行"));
        dataList.add(new DemoFixedSingleHeader("A列三行", "B列三行", "C列三行"));

        String filepath = "d:\\tmp\\单层表头有3列.xlsx";
        excelExportTool.exportLocalExcel(null, dataList, filepath);
    }

    @Test
    public void testExportDoubleLevelHeaderTable() throws IllegalAccessException, IOException, InvocationTargetException {
        List<DemoFixedDoubleHeader> dataList = new ArrayList<>();
        DemoFixedDoubleHeader item = new DemoFixedDoubleHeader();
        item.setColumnB("B列一行");
        item.setColumnC("C列一行");
        item.setColumnD("D列一行");
        item.setColumnE("E列一行");
        item.setColumnF("F列一行");
        item.setColumnH("H列一行");
        item.setColumnI("I列一行");
        item.setColumnJ("J列一行");
        item.setColumnTotal("合计一行");
        dataList.add(item);

        DemoFixedDoubleHeader item2 = new DemoFixedDoubleHeader();
        item2.setColumnB("B列二行");
        item2.setColumnC("C列二行");
        item2.setColumnD("D列二行");
        item2.setColumnE("E列二行");
        item2.setColumnF("F列二行");
        item2.setColumnH("H列二行");
        item2.setColumnI("I列二行");
        item2.setColumnJ("J列二行");
        item2.setColumnTotal("合计二行");
        dataList.add(item2);

        DemoFixedDoubleHeader item3 = new DemoFixedDoubleHeader();
        item3.setColumnB("B列三行");
        item3.setColumnC("C列三行");
        item3.setColumnD("D列三行");
        item3.setColumnE("E列三行");
        item3.setColumnF("F列三行");
        item3.setColumnH("H列三行");
        item3.setColumnI("I列三行");
        item3.setColumnJ("J列三行");
        item3.setColumnTotal("合计三行");
        dataList.add(item3);

        String filepath = "d:\\tmp\\两层表头.xlsx";
        excelExportTool.exportLocalExcel(null, dataList, filepath);
    }
}
