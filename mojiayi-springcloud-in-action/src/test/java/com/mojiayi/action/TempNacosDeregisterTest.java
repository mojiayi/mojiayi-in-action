package com.mojiayi.action;

import cn.hutool.core.date.DatePattern;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
import cn.hutool.http.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class TempNacosDeregisterTest {
    @Test
    public void testnacosDeregister() throws URISyntaxException, InterruptedException {
        Map<String, String> headers = new HashMap<>(3);
        headers.put("channel", "0");
        headers.put("tag", "4");
        headers.put("Authorization", "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ7XCJpZFwiOjIyMixcIm5hbWVcIjpcIueuoeeQhuWRmFwiLFwidXNlcm5hbWVcIjpcInRyZ2ZcIn0iLCJjcmVhdGVkIjoxNjM5MTIxMTU3MTkwLCJleHAiOjE2NDA0MTcxNTd9.CU0ev4nAibWkVXEx6JxtmuzGsO4crqe4S-lXTt_Tkm0UuIbNjMPcxQib6wwWPfzTWowXSzmu3iMVuhFkAAKXuw");

        // 创建查询素材信息的请求
        String queryUrl = "https://dev-manage.hntrgf.com.cn/m-material/m/material/tag?params=%7B%22tagCategoryId%22%3A%22339906963481559040%22%7D&pageNum=1&pageSize=10";
        HttpRequest queryRequest = HttpUtil.createGet(queryUrl);
        queryRequest.headerMap(headers, true);

        // 创建下线nacos服务的请求
        String deregisterUrl = "https://dev-manage.hntrgf.com.cn/m-material/nouser/nacos/deregister";
        HttpRequest deregisterRequest = HttpUtil.createGet(deregisterUrl);
        deregisterRequest.headerMap(headers, true);

        int count = 0;
        Gson gson = new Gson();
        String offlineNodeInfo = "";
        int notMatchOfflineNodeCount = 0;
        long deregisterTime = 0L;
        long lastDispatchTime = 0L;
        while (true) {
            HttpResponse httpResponse = queryRequest.execute();
            count++;
            int status = httpResponse.getStatus();
            if (status == HttpStatus.HTTP_OK) {
                JsonObject jsonObject = gson.fromJson(httpResponse.body(), JsonObject.class);
                String nodeInfo = jsonObject.getAsJsonObject("data").getAsJsonPrimitive("resultDataExt").getAsString().split("ip:")[1].split(", port")[0];
                // 把一个服务下线后，如果响应节点不是被下线的节点，计数器加1，否则，归零
                if (StringUtils.isNotEmpty(offlineNodeInfo)) {
                    if (offlineNodeInfo.equals(nodeInfo)) {
                        notMatchOfflineNodeCount = 0;
                        lastDispatchTime = System.currentTimeMillis();
                    } else {
                        notMatchOfflineNodeCount++;
                    }
                }
                // 打印查询素材信息请求的响应ip地址和端口
//                System.out.println("执行第" + count + "轮,成功,node info=" + nodeInfo + ",time=" + DatePattern.NORM_DATETIME_FORMAT.format(System.currentTimeMillis()));
            } else {
                System.out.println("执行第" + count + "轮,失败,status=" + httpResponse.getStatus());
            }
            if (count == 200) {
                // 执行200次查询后，下线多个服务节点中的一台
                httpResponse = deregisterRequest.execute();
                offlineNodeInfo = httpResponse.body().split("ip:")[1].split(", port")[0];
                deregisterTime = System.currentTimeMillis();
//                System.out.println("deregister " + offlineNodeInfo + ",time=" + DatePattern.NORM_DATETIME_FORMAT.format(deregisterTime));
            }
            if (notMatchOfflineNodeCount == 200) {
                // 如果连续200次查询的响应ip都相同，证明前一个服务已经彻底下线，退出本单元测试
                System.out.println("连续200次查询的响应节点都不是被下线的那个，证明前一个服务已经下线，退出本单元测试,"
                        + "注销时间=" + DatePattern.NORM_DATETIME_FORMAT.format(deregisterTime)
                        + "最后一次调用时间=" + DatePattern.NORM_DATETIME_FORMAT.format(lastDispatchTime)
                        + ",下线等待时长=" + (lastDispatchTime - deregisterTime) + " ms");
                System.exit(0);
            }
        }
    }
}
