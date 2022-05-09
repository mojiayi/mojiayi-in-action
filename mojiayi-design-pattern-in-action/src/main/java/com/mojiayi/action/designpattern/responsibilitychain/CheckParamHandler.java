package com.mojiayi.action.designpattern.responsibilitychain;

import cn.hutool.http.HttpStatus;
import com.mojiayi.action.common.tool.response.CommonResp;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 校验传入的参数是否合法，作为例子，只简单地校验用户id和购买数量
 *
 * @author mojiayi
 */
@Component
@Order(1)
public class CheckParamHandler extends AbstractHandler {
    @Override
    CommonResp<Boolean> doFilter(InputParam inputParam) {
        if (inputParam == null || inputParam.getUserId() == null || inputParam.getUserId() <= 0) {
            return CommonResp.error(HttpStatus.HTTP_FORBIDDEN, "用户id不能为空且必须大于0", Boolean.FALSE);
        }
        if (inputParam.getAmount() == null || inputParam.getAmount() <= 0) {
            return CommonResp.error(HttpStatus.HTTP_FORBIDDEN, "购买数量不能为空且必须大于0", Boolean.FALSE);
        }
        return CommonResp.success(Boolean.TRUE);
    }
}
