package com.prcsteel.platform.acl.model.query;

import com.prcsteel.platform.common.query.PagedQuery;

/**
 * 票据查询
 * 
 * @author Rabbit
 *
 */
public class BaseDeliverQuery extends PagedQuery {

	private String dateStartStr;// 起始时间

	private String dateEndStr;// 终止时间

	private Long id;//ID

	private String name;// .快递名称

	private  String createdBy;


	public String getDateStartStr() {
		return dateStartStr;
	}

	public void setDateStartStr(String dateStartStr) {
		this.dateStartStr = dateStartStr;
	}

	public String getDateEndStr() {
		return dateEndStr;
	}

	public void setDateEndStr(String dateEndStr) {
		this.dateEndStr = dateEndStr;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String addName) {
		this.createdBy = addName;
	}
}
