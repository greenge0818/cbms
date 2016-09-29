package com.prcsteel.platform.smartmatch.model.dto;

import java.util.List;

/** 
 * excel模板dto
 * @author  peanut
 * @date 创建时间：2015年11月28日 下午3:33:20   
 */
public class ExcelTempletDto {
	/***数据错误信息****/
	private String errorMsg;
	
	/****卖家id*****/
	private String accountId;
	
	/****卖家全称*****/
	private String accountName;
	
	/****品名uuid*****/
	private String categoryUuid;
	
	/****品名名称*****/
	private String categoryName;
	
	/****材质uuid*****/
	private String materialUuid;
	
	/****材质名称*****/
	private String materialName;
	
	/****规格uuid*****/
	private List<String> normUuidList;
	
	/****规格名称*****/
	private List<String> normName;
	
	/****厂家id*****/
	private String factoryId;
	
	/****厂家名称*****/
	private String factoryName;
	
	/****提货仓库id*****/
	private String warehouseId;
	
	/****提货仓库名称*****/
	private String warehouseName;
	
	/****计重方式*****/
	private String weightConcept;
	
	/****件数*****/
	private String quantity;
	
	/****总重(吨)*****/
	private String weight;
	
	/****单价(元/吨)*****/
	private String price;
	
	/****状态:是否挂牌****/
	private String status;
	
	/****备注*****/
	private String remark;
	
	//add by caosulin@prcsteel.com 2016.6.2 增加异常
	/****异常状态，exception 异常，normal 正常*****/
	private String isException;
	
	//所在城市
	private String cityName;
	

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
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


	public List<String> getNormUuidList() {
		return normUuidList;
	}

	public void setNormUuidList(List<String> normUuidList) {
		this.normUuidList = normUuidList;
	}

	public List<String> getNormName() {
		return normName;
	}

	public void setNormName(List<String> normName) {
		this.normName = normName;
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

	public String getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(String warehouseId) {
		this.warehouseId = warehouseId;
	}

	public String getWarehouseName() {
		return warehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}

	public String getWeightConcept() {
		return weightConcept;
	}

	public void setWeightConcept(String weightConcept) {
		this.weightConcept = weightConcept;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getIsException() {
		return isException;
	}

	public void setIsException(String isException) {
		this.isException = isException;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	
	
}
