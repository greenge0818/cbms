package com.prcsteel.platform.smartmatch.model.dto;

import java.util.List;

import com.prcsteel.platform.smartmatch.model.model.PurchaseOrderItems;

public class PurchaseOrderItemsDtoTwo extends PurchaseOrderItems {
	private String categoryName;
	private String materialName;
	private String spec;
	private String factoryNames;
	private String itemweight;
	private String extendAttributes;

	private List<PurchaseOrderItemsAttributeDto> attributeList;

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

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public String getFactoryNames() {
		return factoryNames;
	}

	public void setFactoryNames(String factoryNames) {
		this.factoryNames = factoryNames;
	}

	public String getItemweight() {
		return itemweight;
	}

	public void setItemweight(String itemweight) {
		this.itemweight = itemweight;
	}

	public String getExtendAttributes() {
		return extendAttributes;
	}

	public void setExtendAttributes(String extendAttributes) {
		this.extendAttributes = extendAttributes;
	}

	public List<PurchaseOrderItemsAttributeDto> getAttributeList() {
		return attributeList;
	}

	public void setAttributeList(List<PurchaseOrderItemsAttributeDto> attributeList) {
		this.attributeList = attributeList;
	}

}
