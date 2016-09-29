package com.prcsteel.platform.kuandao.web.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rolyer on 15-7-14.
 */
public class PageResult implements Serializable {


	private static final long serialVersionUID = -6708878718882240787L;
	private Integer draw;
    private Integer recordsTotal = 0;
    private Integer recordsFiltered = 0;
    private Integer pageIndex;
    private Integer pageSize;
    private List<?> data = new ArrayList<>();

    public PageResult(){}

    public PageResult(Integer recordsTotal, Integer recordsFiltered,List<?> data) {
        this.recordsTotal = recordsTotal;
        this.recordsFiltered = recordsFiltered;
        this.data = data;
    }

    public Integer getDraw() {
        return draw;
    }

    public void setDraw(Integer draw) {
        this.draw = draw;
    }

    public Integer getRecordsTotal() {
        return recordsTotal;
    }

    public void setRecordsTotal(Integer recordsTotal) {
        this.recordsTotal = recordsTotal;
    }

    public Integer getRecordsFiltered() {
        return recordsFiltered;
    }

    public void setRecordsFiltered(Integer recordsFiltered) {
        this.recordsFiltered = recordsFiltered;
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

    public List<?> getData() {
        return data;
    }

    public void setData(List<?> data) {
        this.data = data;
    }
}
