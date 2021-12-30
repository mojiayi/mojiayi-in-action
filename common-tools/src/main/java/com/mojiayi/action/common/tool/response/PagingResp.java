package com.mojiayi.action.common.tool.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * 分页查询时的返回结果，放在{@code CommonResp}中的data返回。
 *
 * @author liguangri
 */
@ApiModel(value = "分页查询返回结果集")
public class PagingResp<T> implements Serializable {
    @ApiModelProperty(name = "list", value = "分页查询的业务数据")
    private List<T> list;

    @ApiModelProperty(name = "pageIndex", value = "当前页码，默认为1", dataType = "Integer")
    private Integer pageIndex;

    @ApiModelProperty(name = "pageSize", value = "每页条数，默认为10", dataType = "Integer")
    private Integer pageSize;

    @ApiModelProperty(name = "totalItem", value = "总条数", dataType = "Integer")
    private Integer totalItem;

    @ApiModelProperty(name = "totalPage", value = "总页数", dataType = "Integer")
    private Integer totalPage;

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotalItem() {
        return totalItem;
    }

    public void setTotalItem(Integer totalItem) {
        this.totalItem = totalItem;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }
}
