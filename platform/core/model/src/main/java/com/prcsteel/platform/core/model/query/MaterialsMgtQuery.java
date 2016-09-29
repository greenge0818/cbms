package com.prcsteel.platform.core.model.query;

import com.prcsteel.platform.common.query.PagedQuery;

/** 
 * 材质管理查询对象
 * created by peanut on ：2016年1月14日 下午3:17:31   
 */
public class MaterialsMgtQuery extends PagedQuery {
	/**
	 * 材质表id
	 */
	private Long materialId;
	/**
	 * 品名材质关联表id
	 */
	private Long categoryMaterialId;
	/**
	 * 品名uuid
	 */
	private String categoryUuid;
	/**
	 * 品名名称
	 */
    private String categoryName;
    /**
	 * 材质uuid
	 */
    private String materialUuid;
    /**
	 * 材质名称
	 */
    private String materialName;
    /**
	 * 是否模糊匹配
	 */
    private String isVague;
    
	public MaterialsMgtQuery() {
		super();
	}
	
	public MaterialsMgtQuery(Long materialId,Long categoryMaterialId,String categoryUuid, String categoryName,
			String materialUuid, String materialName, String isVague) {
		this.materialId = materialId;
		this.categoryMaterialId = categoryMaterialId;
		this.categoryUuid = categoryUuid;
		this.categoryName = categoryName;
		this.materialUuid = materialUuid;
		this.materialName = materialName;
		this.isVague = isVague;
	}
	
	
	public Long getMaterialId() {
		return materialId;
	}

	public void setMaterialId(Long materialId) {
		this.materialId = materialId;
	}

	public String getCategoryUuid() {
		return categoryUuid;
	}
	public void setCategoryUuid(String categoryUuid) {
		this.categoryUuid = categoryUuid;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getMaterialUuid() {
		return materialUuid;
	}
	public void setMaterialUuid(String materialUuid) {
		this.materialUuid = materialUuid;
	}
	public String getMaterialName() {
		return materialName;
	}
	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}
	public String getIsVague() {
		return isVague;
	}
	public void setIsVague(String isVague) {
		this.isVague = isVague;
	}

	public Long getCategoryMaterialId() {
		return categoryMaterialId;
	}

	public void setCategoryMaterialId(Long categoryMaterialId) {
		this.categoryMaterialId = categoryMaterialId;
	}
}
