package com.mojiayi.action.designpattern.responsibilitychain;

import com.mojiayi.action.common.tool.response.CommonResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author mojiayi
 */
@Component
public class ChainBuilder {
    @Autowired
    private List<AbstractHandler> handlerList;

    private AbstractHandler handler;

    public AbstractHandler getHandler() {
        return handler;
    }

    public void setHandler(AbstractHandler handler) {
        this.handler = handler;
    }

    @PostConstruct
    public void initChainHandlers() {
        if (CollectionUtils.isEmpty(handlerList)) {
            System.exit(1);
            return;
        }
        for (int index = 0; index < handlerList.size(); index++) {
            if (index == 0) {
                handler = handlerList.get(index);
            } else {
                AbstractHandler currentHandler = handlerList.get(index - 1);
                AbstractHandler nextHandler = handlerList.get(index);
                currentHandler.setNextHandler(nextHandler);
            }
        }
    }

    public CommonResp<Boolean> exec(InputParam inputParam) {
        return handler.filter(inputParam);
    }
}
