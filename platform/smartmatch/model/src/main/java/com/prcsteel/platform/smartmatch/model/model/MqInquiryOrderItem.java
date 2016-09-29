package com.prcsteel.platform.smartmatch.model.model;

/**
 * 分检系统推送过来的询价单资源信息
 * 
 * @author tangwei
 *
 */
public class MqInquiryOrderItem {
	private String categoryId; // 品类id
	private String categoryName; // 品类名称
	private String materialId; // 材质id
	private String materialName;// 材质名称
	private String spec1;// 规格1
	private String spec2;// 规格2
	private String spec3;// 规格3
	private String factoryId;// 工厂id
	private String factoryName;// 工厂名称
	private String cityId;// 城市id
	private String cityName;// 城市名称
	private Double price;// 价格
	private Double weight;// 重量
	private String saleNumber;// 数量
	private String remark;// 备注
	private String nsortAttribute;// 属性（json对象）


	//add by zhoucai@prcsteel.com  2016-9-13 分拣推送过来的计重方式
	private String weightConcept;// 计重方式

	public String getWeightConcept() {
		return weightConcept;
	}

	public void setWeightConcept(String weightConcept) {
		this.weightConcept = weightConcept;
	}


	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getMaterialId() {
		return materialId;
	}

	public void setMaterialId(String materialId) {
		this.materialId = materialId;
	}

	public String getMaterialName() {
		return materialName;
	}

	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}

	public String getSpec1() {
		return spec1;
	}

	public void setSpec1(String spec1) {
		this.spec1 = spec1;
	}

	public String getSpec2() {
		return spec2;
	}

	public void setSpec2(String spec2) {
		this.spec2 = spec2;
	}

	public String getSpec3() {
		return spec3;
	}

	public void setSpec3(String spec3) {
		this.spec3 = spec3;
	}

	public String getFactoryId() {
		return factoryId;
	}

	public void setFactoryId(String factoryId) {
		this.factoryId = factoryId;
	}

	public String getFactoryName() {
		return factoryName;
	}

	public void setFactoryName(String factoryName) {
		this.factoryName = factoryName;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public String getSaleNumber() {
		return saleNumber;
	}

	public void setSaleNumber(String saleNumber) {
		this.saleNumber = saleNumber;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getNsortAttribute() {
		return nsortAttribute;
	}

	public void setNsortAttribute(String nsortAttribute) {
		this.nsortAttribute = nsortAttribute;
	}
}
