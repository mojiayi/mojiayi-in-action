package com.mojiayi.action.common.tool.param;

import java.io.Serializable;

/**
 * @author liguangri
 */
public class PagingParam implements Serializable {
    private Integer pageIndex;

    private Integer pageSize;

    public Integer getPageIndex() {
        if (pageIndex == null || pageIndex == 0) {
            pageIndex = 1;
        }
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Integer getPageSize() {
        if (pageSize == null || pageSize == 0) {
            pageSize = 10;
        }
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
