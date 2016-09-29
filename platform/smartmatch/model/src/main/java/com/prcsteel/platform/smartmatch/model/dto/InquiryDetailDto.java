package com.prcsteel.platform.smartmatch.model.dto;

import java.util.Date;

/**
 * 询价详情Dto
 * @author tangwei
 *
 */
public class InquiryDetailDto {
	private long inquiryOrderId;//询价单Id
	private long accountId;//客户Id
	private String accountName;//客户名称
	private String contactNames;//联系人姓名(多个以后,分割)
	private String contactTels;//联系人电话(多个以后,分割)
	private String categoryName;//品名
	private String materialName;//材质
	private String spec;//规格
	private String factoryName;//厂家
	private String warehouseName;//仓库
	private double weight;//重量
	private double price;//单价
	private Date created;//创建时间
	

	public long getInquiryOrderId() {
		return inquiryOrderId;
	}
	public void setInquiryOrderId(long inquiryOrderId) {
		this.inquiryOrderId = inquiryOrderId;
	}
	public long getAccountId() {
		return accountId;
	}
	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getContactNames() {
		return contactNames;
	}
	public void setContactNames(String contactNames) {
		this.contactNames = contactNames;
	}
	public String getContactTels() {
		return contactTels;
	}
	public void setContactTels(String contactTels) {
		this.contactTels = contactTels;
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
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
}
