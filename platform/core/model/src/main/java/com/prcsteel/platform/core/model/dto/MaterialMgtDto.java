package com.prcsteel.platform.core.model.dto;
/** 
 * 材质管理Dto
 * @author  peanut
 * @date 创建时间：2016年1月14日 上午11:20:02   
 */
public class MaterialMgtDto {
	/**
	 * 材质表记录id
	 */
	private Long materialId;
	/**
	 * 品名材质关联表记录id
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
	 * 材质名称
	 */
	private String materialName;
	/**
	 * 材质uuid
	 */
	private String materialUuid;
	/**
	 * 备注
	 */
	private String remark;
	
	public MaterialMgtDto() {
		super();
	}
	
	public MaterialMgtDto(Long materialId,Long categoryMaterialId, String categoryUuid, String categoryName,
			String materialName, String remark) {
		this.materialId = materialId;
		this.categoryMaterialId = categoryMaterialId;
		this.categoryUuid = categoryUuid;
		this.categoryName = categoryName;
		this.materialName = materialName;
		this.remark = remark;
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
	public String getMaterialName() {
		return materialName;
	}
	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getMaterialUuid() {
		return materialUuid;
	}
	public void setMaterialUuid(String materialUuid) {
		this.materialUuid = materialUuid;
	}

	public Long getCategoryMaterialId() {
		return categoryMaterialId;
	}

	public void setCategoryMaterialId(Long categoryMaterialId) {
		this.categoryMaterialId = categoryMaterialId;
	}
	
}
