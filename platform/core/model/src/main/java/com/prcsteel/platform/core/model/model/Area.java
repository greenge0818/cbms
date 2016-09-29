package com.prcsteel.platform.core.model.model;

import java.util.Date;

public class Area {
	private Long id;

	private String name;

	/**
	 * 大区名称
	 */

	private String zoneName;
	private Long centerCityId;

	private String refCityIds;

	/**
	 * 是否启用
	 */

	private String isEnable;

	private Date created;

	private String createdBy;

	private Date lastUpdated;

	private String lastUpdatedBy;

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
		this.name = name == null ? null : name.trim();
	}

	public Long getCenterCityId() {
		return centerCityId;
	}

	public void setCenterCityId(Long centerCityId) {
		this.centerCityId = centerCityId;
	}

	public String getRefCityIds() {
		return refCityIds;
	}

	public void setRefCityIds(String refCityIds) {
		this.refCityIds = refCityIds == null ? null : refCityIds.trim();
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy == null ? null : createdBy.trim();
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy == null ? null : lastUpdatedBy.trim();
	}

	public String getZoneName() {
		return zoneName;
	}

	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}

	public String getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(String isEnable) {
		this.isEnable = isEnable;
	}
}