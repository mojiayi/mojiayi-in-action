package com.mojiayi.action.common.tool.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author liguangri
 */
@ApiModel(value = "分页查询公共参数")
public class PagingParam implements Serializable {
    @ApiModelProperty(name = "pageIndex", value = "当前页码，默认为1", dataType = "Integer")
    private Integer pageIndex;

    @ApiModelProperty(name = "pageSize", value = "每页条数，默认为10", dataType = "Integer")
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

    @ApiModelProperty(hidden = true)
    public int getOffset() {
        return (this.getPageIndex() - 1) * this.getPageSize();
    }
}
