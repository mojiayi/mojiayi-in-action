package com.mojiayi.action.excel.constant;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.format.FastDateFormat;

import java.util.*;

/**
 * <p>
 * excel导入导出过程中用的常量
 * </p>
 *
 * @author mojiayi
 */
public class ExcelConstant {
    /**
     * 布隆过滤器的槽位数
     */
    public static final int EXPECTED_INSERTIONS = 1000000;

    /**
     * 在布隆过滤器中允许的错误匹配率
     */
    public static final double FPP = 0.01D;

    public static final String USER_ID = "userId";

    public static final Set<String> EXCLUDE_COLUMN_SET = Set.of("id", "create_by", "create_time", "update_by", "update_time", "delete_flag");

    public static final Map<String, String> DATETIME_PATTERN_MAP = new HashMap<>(4);

    static {
        DATETIME_PATTERN_MAP.put(DatePattern.NORM_MONTH_PATTERN, DatePattern.NORM_MONTH_PATTERN);
        DATETIME_PATTERN_MAP.put(DatePattern.NORM_DATE_PATTERN, DatePattern.NORM_DATE_PATTERN);
        DATETIME_PATTERN_MAP.put(DatePattern.NORM_DATETIME_MINUTE_PATTERN, DatePattern.NORM_DATETIME_MINUTE_PATTERN);
        DATETIME_PATTERN_MAP.put(DatePattern.NORM_DATETIME_PATTERN, DatePattern.NORM_DATETIME_PATTERN);
    }

    public static final Map<String, FastDateFormat> DB_DATE_TIME_FORMAT_MAP = new HashMap<>();

    static {
        DB_DATE_TIME_FORMAT_MAP.put("datetime", DatePattern.NORM_DATETIME_FORMAT);
        DB_DATE_TIME_FORMAT_MAP.put("date", DatePattern.NORM_DATE_FORMAT);
        DB_DATE_TIME_FORMAT_MAP.put("timestamp", DatePattern.NORM_DATETIME_FORMAT);
    }


    /**
     * 2003版Excel后缀
     */
    public static final String EXCEL_2003_SUFFIX = ".xls";
    /**
     * Excel后缀
     */
    public static final String EXCEL_SUFFIX = ".xlsx";

    private ExcelConstant() {

    }
}
