package com.mojiayi.action.springcloud.controller;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.NacosServiceManager;
import com.alibaba.nacos.api.exception.NacosException;
import com.mojiayi.action.springcloud.domain.CommonResult;
import com.mojiayi.action.springcloud.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author mojiayi
 */
@RefreshScope
@RestController
@RequestMapping("/justDoIt")
public class JustDoItController {
    @Value("${config.info}")
    private String configInfo;

    @Autowired
    private RestTemplate restTemplate;
    @Value("${service-url.nacos-user-service}")
    private String userServiceUrl;

    @Autowired
    private NacosDiscoveryProperties nacosDiscoveryProperties;

    @Autowired
    private NacosServiceManager nacosServiceManager;

    @GetMapping("/{id}")
    public CommonResult<User> getUser(@PathVariable Long id) {
        CommonResult<?> commonResult = restTemplate.getForObject(userServiceUrl + "/user/{1}", CommonResult.class, id);
        CommonResult<User> userResult = new CommonResult<>();
        if (commonResult == null) {
            userResult.setMessage("没有查到用户数据，查询操作失败：" + configInfo);
            return userResult;
        }
        if (commonResult.getData() == null) {
            userResult.setMessage("查到用户数据：" + configInfo);
            return userResult;
        }
        userResult.setMessage("没有查到用户数据，用户对象为空：" + configInfo);
        return userResult;
    }

    @GetMapping(value = "/deregister")
    public String deregisterInstance() {
        String serviceName = nacosDiscoveryProperties.getService();
        String groupName = nacosDiscoveryProperties.getGroup();
        String clusterName = nacosDiscoveryProperties.getClusterName();
        String ip = nacosDiscoveryProperties.getIp();
        int port = nacosDiscoveryProperties.getPort();
        System.out.printf(String.format("deregister from nacos, serviceName:{}, groupName:{}, clusterName:{}, ip:{}, port:{}", serviceName, groupName, clusterName, ip, port));
        try {
            nacosServiceManager.getNamingService(nacosDiscoveryProperties.getNacosProperties()).deregisterInstance(serviceName, groupName, ip, port, clusterName);
        } catch (NacosException e) {
            e.printStackTrace();
            return "error";
        }
        return "success";
    }

    @GetMapping(value = "/register")
    public String registerInstance() {
        String serviceName = nacosDiscoveryProperties.getService();
        String groupName = nacosDiscoveryProperties.getGroup();
        String clusterName = nacosDiscoveryProperties.getClusterName();
        String ip = nacosDiscoveryProperties.getIp();
        int port = nacosDiscoveryProperties.getPort();
        System.out.printf(String.format("deregister from nacos, serviceName:{}, groupName:{}, clusterName:{}, ip:{}, port:{}", serviceName, groupName, clusterName, ip, port));
        try {
            nacosServiceManager.getNamingService(nacosDiscoveryProperties.getNacosProperties()).registerInstance(serviceName, groupName, ip, port, clusterName);
        } catch (NacosException e) {
            e.printStackTrace();
            return "error";
        }
        return "success";
    }
}
