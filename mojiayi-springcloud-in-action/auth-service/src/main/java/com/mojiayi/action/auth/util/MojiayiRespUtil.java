package com.mojiayi.action.auth.util;

import com.google.gson.Gson;
import com.mojiayi.action.common.tool.response.CommonResp;

import javax.servlet.ServletResponse;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * @author mojiayi
 */
public class MojiayiRespUtil {
    public static void response(ServletResponse servletResponse, CommonResp<?> respContent) {
        Gson gson = new Gson();
        PrintWriter printWriter = null;
        try {
            servletResponse.setCharacterEncoding(StandardCharsets.UTF_8.name());
            servletResponse.setContentType("application/json");
            printWriter = servletResponse.getWriter();
            printWriter.println(gson.toJson(respContent));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (printWriter != null) {
                printWriter.flush();
                printWriter.close();
            }
        }
    }
}
