package com.mojiayi.action.designpattern.responsibilitychain;

import cn.hutool.http.HttpStatus;
import com.mojiayi.action.common.tool.response.CommonResp;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * 校验库存是否充足，作为例子，生成一个随机的库存量数值，与每次传入的购买数量相比较
 *
 * @author mojiayi
 */
@Component
@Order(2)
public class CheckInventoryHandler extends AbstractHandler {
    @Override
    CommonResp<Boolean> doFilter(InputParam inputParam) {
        long mockInventory = new Random().nextInt(100);
        if (inputParam.getAmount() > mockInventory) {
            return CommonResp.error(HttpStatus.HTTP_FORBIDDEN, "库存不足，请重新购买", Boolean.FALSE);
        }
        return CommonResp.success(Boolean.TRUE);
    }
}
