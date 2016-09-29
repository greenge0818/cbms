package org.prcsteel.rest.sdk.activiti.query;

import java.io.Serializable;

import org.prcsteel.rest.sdk.activiti.enums.QueryOrder;

/**
 * 
 * @author zhoukun
 */
public class BaseQuery implements Serializable {

	private static final long serialVersionUID = -3779530406481121857L;

	private String sort;
	
	private QueryOrder order = QueryOrder.asc;
	
	private Integer start = 0;
	
	private Integer size = 10;

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public QueryOrder getOrder() {
		return order;
	}

	public void setOrder(QueryOrder order) {
		this.order = order;
	}

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}
	
}
