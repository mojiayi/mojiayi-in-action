package com.mojiayi.action.common.tool.response;

import java.io.Serializable;
import java.util.List;

/**
 * 分页查询时的返回结果，放在{@code CommonResp}中的data返回。
 *
 * @author liguangri
 */
public class PagingResp<T> implements Serializable {
    private List<T> list;

    private Integer pageIndex;

    private Integer pageSize;

    private Integer totalItem;

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
