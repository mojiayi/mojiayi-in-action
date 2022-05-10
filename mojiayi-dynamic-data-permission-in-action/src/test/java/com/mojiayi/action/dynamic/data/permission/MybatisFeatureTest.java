package com.mojiayi.action.dynamic.data.permission;


import com.mojiayi.action.dynamic.data.permission.constants.MyConstants;
import com.mojiayi.action.dynamic.data.permission.domain.UserInfo;
import com.mojiayi.action.dynamic.data.permission.service.UserInfoService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.MDC;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MojiayiDynamicDataPermissionProvider.class)
public class MybatisFeatureTest {
    @Resource
    private UserInfoService userInfoService;

    @Test
    public void testSelectByUserId() {
        String tenantId = "1";
        MDC.put(MyConstants.TENANT_ID, tenantId);
        long userId = 2;
        UserInfo userInfo = userInfoService.selectByUserId(userId);
        Assert.assertNotNull(userInfo);
        Assert.assertEquals("用户1-1", userInfo.getUsername());
        Assert.assertEquals("租户1", userInfo.getTenantName());

        tenantId = "2";
        MDC.put(MyConstants.TENANT_ID, tenantId);

        userId = 3;
        userInfo = userInfoService.selectByUserId(userId);
        Assert.assertNull(userInfo);

        userId = 8;
        userInfo = userInfoService.selectByUserId(userId);
        Assert.assertNotNull(userInfo);
        Assert.assertEquals("用户2-4", userInfo.getUsername());
        Assert.assertEquals("租户2", userInfo.getTenantName());

        userId = 10;
        userInfo = userInfoService.selectByUserId(userId);
        Assert.assertNull(userInfo);
    }
}
