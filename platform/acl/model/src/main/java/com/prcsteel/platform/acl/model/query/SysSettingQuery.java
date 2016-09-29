package com.prcsteel.platform.acl.model.query;

import com.prcsteel.platform.common.query.PagedQuery;

/**
 * 
 * @author zhoukun
 */
public class SysSettingQuery extends PagedQuery {

	String type;
	
	String value;

	String name;

	String displayName;
	/*
	 * 用来sql排序：sort = column desc  / column asc
	 * eg: sort =  last_updated desc;
	 * 如果sort为空，则默认使用id desc排序
	 */
	private String sort;

	private String rowId;
	
	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getRowId() {
		return rowId;
	}

	public void setRowId(String rowId) {
		this.rowId = rowId;
	}
	
}
