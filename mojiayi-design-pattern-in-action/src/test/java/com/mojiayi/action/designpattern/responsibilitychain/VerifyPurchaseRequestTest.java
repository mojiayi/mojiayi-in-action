package com.mojiayi.action.designpattern.responsibilitychain;

import cn.hutool.http.HttpStatus;
import com.mojiayi.action.common.tool.response.CommonResp;
import com.mojiayi.action.designpattern.Application;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class VerifyPurchaseRequestTest {
    @Autowired
    private ChainBuilder chainBuilder;

    @Test
    public void testVerifyPurchaseRequest() {
        InputParam inputParam = null;
        CommonResp<Boolean> outputResponse = chainBuilder.exec(inputParam);
        Assert.assertEquals(HttpStatus.HTTP_FORBIDDEN, outputResponse.getCode().intValue());
        Assert.assertFalse(outputResponse.getData());

        inputParam = new InputParam();
        outputResponse = chainBuilder.exec(inputParam);
        Assert.assertEquals(HttpStatus.HTTP_FORBIDDEN, outputResponse.getCode().intValue());
        Assert.assertFalse(outputResponse.getData());

        inputParam = new InputParam();
        inputParam.setUserId(50L);
        outputResponse = chainBuilder.exec(inputParam);
        Assert.assertEquals(HttpStatus.HTTP_FORBIDDEN, outputResponse.getCode().intValue());
        Assert.assertFalse(outputResponse.getData());

        inputParam = new InputParam();
        inputParam.setAmount(50L);
        outputResponse = chainBuilder.exec(inputParam);
        Assert.assertEquals(HttpStatus.HTTP_FORBIDDEN, outputResponse.getCode().intValue());
        Assert.assertFalse(outputResponse.getData());

        for (int i = 0; i < 200; i++) {
            inputParam = new InputParam();
            inputParam.setUserId((long) i);
            inputParam.setAmount((long) i + 30);
            outputResponse = chainBuilder.exec(inputParam);
            System.out.println("code=" + outputResponse.getCode() + ",message=" + outputResponse.getMessage());
        }
    }
}
