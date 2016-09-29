package com.prcsteel.platform.smartmatch.model.dto;

import com.prcsteel.platform.smartmatch.model.model.CategoryWeight;

public class CategoryWeightDto {
	private CategoryWeight categoryWeight;
	private String factoryName;
	private String categoryName;
	private String materialName;
	public CategoryWeight getCategoryWeight() {
		return categoryWeight;
	}
	public void setCategoryWeight(CategoryWeight categoryWeight) {
		this.categoryWeight = categoryWeight;
	}
	public String getFactoryName() {
		return factoryName;
	}
	public void setFactoryName(String factoryName) {
		this.factoryName = factoryName;
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
	
	

}
