package com.prcsteel.platform.smartmatch.model.query;

import com.prcsteel.platform.common.query.PagedQuery;

/**
 * 资源更新统计查询参数
 * @author tangwei
 *
 */
public class ResourceStatisQuery extends PagedQuery {
	
	private String cityName;//城市名
	
	private String userId;//操作员Id
	
	private String sourceType;//资源类型
	
	private String beginDate;//更新时间-开始
	
	private String endDate;//更新时间-结束

	private String orgId;//服务中心Id
	
	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
}
