package com.prcsteel.platform.order.model.model;
/** 
 * 品名材质关联表对象
 * created by peanut on ：2016年1月14日 下午12:32:05   
 */

import java.util.Date;

public class CategoryMaterials {
	/**
	 * 记录id
	 */
    private Integer id;
    /**
	 * 品名uuid
	 */
    private String categoryUuid;
    /**
     * 材质uuid
     */
    private String materialsUuid;
    /**
     * 是否删除(逻辑删除)
     */
    private Boolean isDeleted;
    /**
     * 创建时间
     */
    private Date created;
    /**
     * 创建人
     */
    private String createdBy;
    /**
     * 最后更新时间
     */
    private Date lastUpdated;
    /**
     * 最后更新人
     */
    private String lastUpdatedBy;

    private Integer modificationNumber;

    private String rowId;

    private String parentRowId;

    private String remark;

    public CategoryMaterials() {
		super();
	}

	public CategoryMaterials(Integer id, String categoryUuid,
			String materialsUuid, Boolean isDeleted, Date created,
			String createdBy, Date lastUpdated, String lastUpdatedBy,
			Integer modificationNumber, String rowId, String parentRowId,
			String remark) {
		this.id = id;
		this.categoryUuid = categoryUuid;
		this.materialsUuid = materialsUuid;
		this.isDeleted = isDeleted;
		this.created = created;
		this.createdBy = createdBy;
		this.lastUpdated = lastUpdated;
		this.lastUpdatedBy = lastUpdatedBy;
		this.modificationNumber = modificationNumber;
		this.rowId = rowId;
		this.parentRowId = parentRowId;
		this.remark = remark;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategoryUuid() {
        return categoryUuid;
    }

    public void setCategoryUuid(String categoryUuid) {
        this.categoryUuid = categoryUuid == null ? null : categoryUuid.trim();
    }

    public String getMaterialsUuid() {
        return materialsUuid;
    }

    public void setMaterialsUuid(String materialsUuid) {
        this.materialsUuid = materialsUuid == null ? null : materialsUuid.trim();
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
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

    public Integer getModificationNumber() {
        return modificationNumber;
    }

    public void setModificationNumber(Integer modificationNumber) {
        this.modificationNumber = modificationNumber;
    }

    public String getRowId() {
        return rowId;
    }

    public void setRowId(String rowId) {
        this.rowId = rowId == null ? null : rowId.trim();
    }

    public String getParentRowId() {
        return parentRowId;
    }

    public void setParentRowId(String parentRowId) {
        this.parentRowId = parentRowId == null ? null : parentRowId.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
}
