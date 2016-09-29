package com.prcsteel.platform.order.model.dto;


/**
 * 区域城市Dto
 *  
 * @author peanut on 2015-11-17
 *
 */
public class AreaCityDto {
	
	/***区域id***/
	Long areaId;
	
	/***区域名称***/
	String areaName;
	
	/***中心城市id***/
	Long centerCityId;
	
	/***中心城市名称***/
	String centerCityName;
	
	/***周边城市id集***/
	String refCityIds;
	
	/***周边城市名称集***/
	String refCityNames;

	public Long getAreaId() {
		return areaId;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public Long getCenterCityId() {
		return centerCityId;
	}

	public void setCenterCityId(Long centerCityId) {
		this.centerCityId = centerCityId;
	}

	public String getCenterCityName() {
		return centerCityName;
	}

	public void setCenterCityName(String centerCityName) {
		this.centerCityName = centerCityName;
	}

	public String getRefCityIds() {
		return refCityIds;
	}

	public void setRefCityIds(String refCityIds) {
		this.refCityIds = refCityIds;
	}

	public String getRefCityNames() {
		return refCityNames;
	}

	public void setRefCityNames(String refCityNames) {
		this.refCityNames = refCityNames;
	}
	
}
