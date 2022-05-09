package com.mojiayi.action.designpattern.responsibilitychain;

import cn.hutool.http.HttpStatus;
import com.mojiayi.action.common.tool.response.CommonResp;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * 校验用户是否有购买资格，作为例子，生成一个随机的用户id，与每次传入的用户id相比较，只有小于本次所生成的随机id才算有资格
 *
 * @author mojiayi
 */
@Component
@Order(3)
public class CheckQualificationHandler extends AbstractHandler {
    @Override
    CommonResp<Boolean> doFilter(InputParam inputParam) {
        long mockUserId = new Random().nextInt(100);
        if (inputParam.getUserId() > mockUserId) {
            return CommonResp.error(HttpStatus.HTTP_FORBIDDEN, "很抱歉您不能购买本商品", Boolean.FALSE);
        }
        return CommonResp.success(Boolean.TRUE);
    }
}
