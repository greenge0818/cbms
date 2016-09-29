package com.prcsteel.platform.smartmatch.model.query;

import com.prcsteel.platform.common.query.PagedQuery;

import java.util.List;

/**
 * Created by wucong on 2015/11/24.
 */
public class ResourceBaseQuery extends PagedQuery {
	private Long accountId;
	private String accountName;
	private String area;
	private Long warehouseId;
	private String warehouseName;
	private String lastUpdatedBy;
	private String categoryUuid;
	private String categoryName;
	private String materialUuid;
	private String materialName;
	private String spec;
	private Long factoryId;
	private String factoryName;
	private List<Long> userIds;
	private Long orgId;//服务中心Id
	
	/**
	 * 空：全部资源 ， inquiry询价资源; daily_common 日常资源 ；history_transaction 历史成交资源
	 */
	private String sourceType;

	/**
	 * 更新时间起
	 */
	private String upStart;
	/**
	 * 更新时间止
	 */
	private String upEnd;

	/**
	 * 资源状态
	 */
	private String status;

	public ResourceBaseQuery() {
	}

	public ResourceBaseQuery(Long accountId, String categoryUuid, String materialUuid, String materialName,
			Long factoryId, String factoryName, Long warehouseId, String warehouseName, String status, String spec) {
		this.accountId = accountId;
		this.categoryUuid = categoryUuid;
		this.materialUuid = materialUuid;
		this.materialName = materialName;
		this.factoryId = factoryId;
		this.factoryName = factoryName;
		this.warehouseId = warehouseId;
		this.warehouseName = warehouseName;
		this.status = status;
		this.spec = spec;
	}

	public List<Long> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<Long> userIds) {
		this.userIds = userIds;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public void setFactoryName(String factoryName) {
		this.factoryName = factoryName;
	}

	public String getAccountName() {
		return accountName;
	}

	public String getArea() {
		return area;
	}

	public String getWarehouseName() {
		return warehouseName;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public String getMaterialName() {
		return materialName;
	}

	public String getSpec() {
		return spec;
	}

	public String getFactoryName() {
		return factoryName;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public Long getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(Long warehouseId) {
		this.warehouseId = warehouseId;
	}

	public String getCategoryUuid() {
		return categoryUuid;
	}

	public void setCategoryUuid(String categoryUuid) {
		this.categoryUuid = categoryUuid;
	}

	public String getMaterialUuid() {
		return materialUuid;
	}

	public void setMaterialUuid(String materialUuid) {
		this.materialUuid = materialUuid;
	}

	public Long getFactoryId() {
		return factoryId;
	}

	public void setFactoryId(Long factoryId) {
		this.factoryId = factoryId;
	}

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public String getUpStart() {
		return upStart;
	}

	public void setUpStart(String upStart) {
		this.upStart = upStart;
	}

	public String getUpEnd() {
		return upEnd;
	}

	public void setUpEnd(String upEnd) {
		this.upEnd = upEnd;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

}
