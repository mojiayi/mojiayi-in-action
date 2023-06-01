package com.mojiayi.action.flink.kafka.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

/**
 * <p>
 * TODO
 * </p>
 *
 * @author guangri.li
 * @since 2023/5/30 15:17
 */
public class GsonUtil {
    private final static Gson gson = new Gson();

    private final static Gson disableHtmlEscapingGson = new GsonBuilder().disableHtmlEscaping().create();

    public static <T> T fromJson(String value, Class<T> type) {
        return gson.fromJson(value, type);
    }

    public static <T> T fromJson(String value, Type type) {
        return gson.fromJson(value, type);
    }

    public static String toJson(Object value) {
        return gson.toJson(value);
    }

    public static String toJsonDisableHtmlEscaping(Object value) {
        return disableHtmlEscapingGson.toJson(value);
    }
}
