package com.prcsteel.platform.smartmatch.model.dto;

import java.util.List;

import com.prcsteel.platform.smartmatch.model.model.QuotationOrderItems;

/**
 * 报价详情页面DTO
 * @author prcsteel
 *
 */
public class QuotationInfoDto extends QuotationOrderItems {

	private String sellerName;//卖家名称
	private String categoryName;//品名
	private String materialName;//材质
	private String spec;//规格
	private String factoryName;//钢厂
	private String cityName;//所在城市
	private String warehouseName;//仓库名称


	List<DepartmentDto> departments = null;//当前卖家所有的部门
	public String getSellerName() {
		return sellerName;
	}
	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
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
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getWarehouseName() {
		return warehouseName;
	}
	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}
	public void setDepartments(List<DepartmentDto> departments) {
		this.departments = departments;
	}
	public List<DepartmentDto> getDepartments() {
		return departments;
	}
}
