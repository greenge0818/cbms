package com.prcsteel.platform.smartmatch.model.query;

import com.prcsteel.platform.common.query.PagedQuery;

/**
 * 区域管理查询Query
 * 
 * create by peanut on 2015-11-25 
 *
 */
public class AttributeEditQuery extends PagedQuery {
	/****属性名称****/
	private String name;
	/****属性类型****/
	private String type;
	/****属性值****/
	private String options;
	/****uuid****/
	private String uuid;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getOptions() {
		return options;
	}
	public void setOptions(String options) {
		this.options = options;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
}
