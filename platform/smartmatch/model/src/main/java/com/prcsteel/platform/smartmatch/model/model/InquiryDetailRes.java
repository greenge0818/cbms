package com.prcsteel.platform.smartmatch.model.model;

/**
 * 询价详情资源信息
 * @author Administrator
 *
 */
public class InquiryDetailRes {
	private String categoryName;//品名
	private String displayCategoryName;//品名(前台显示,品名相同不显示)
	private String materialName;//材质
	private String spec;//规格
	private String factoryName;//厂家
	private String warehouseName;//仓库
	private double weight;//重量
	private double price;//单价
	public InquiryDetailRes(String categoryName,String materialName,String spec,String factoryName,
			String warehouseName,double weight,double price){
		this.categoryName = categoryName;
		this.displayCategoryName = categoryName;
		this.materialName = materialName;
		this.spec = spec;
		this.factoryName = factoryName;
		this.warehouseName = warehouseName;
		this.weight = weight;
		this.price = price;
	}
	
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getDisplayCategoryName() {
		return displayCategoryName;
	}

	public void setDisplayCategoryName(String displayCategoryName) {
		this.displayCategoryName = displayCategoryName;
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
	public String getFactoryName() {
		return factoryName;
	}
	public void setFactoryName(String factoryName) {
		this.factoryName = factoryName;
	}
	public String getWarehouseName() {
		return warehouseName;
	}
	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
}
