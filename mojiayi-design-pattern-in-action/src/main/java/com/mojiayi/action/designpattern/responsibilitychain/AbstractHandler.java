package com.mojiayi.action.designpattern.responsibilitychain;

import com.mojiayi.action.common.tool.response.CommonResp;

/**
 * @author liguangri
 */
public abstract class AbstractHandler {
    private AbstractHandler nextHandler;

    public AbstractHandler getNextHandler() {
        return nextHandler;
    }

    public void setNextHandler(AbstractHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    abstract CommonResp<Boolean> doFilter(InputParam inputParam);

    public CommonResp<Boolean> filter(InputParam inputParam) {
        CommonResp<Boolean> commonResp = doFilter(inputParam);
        if (!commonResp.isSuccess()) {
            return commonResp;
        }
        if (this.getNextHandler() != null) {
            return this.getNextHandler().filter(inputParam);
        }
        return commonResp;
    }
}
