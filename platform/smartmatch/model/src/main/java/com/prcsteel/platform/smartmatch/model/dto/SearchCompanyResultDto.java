package com.prcsteel.platform.smartmatch.model.dto;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

public class SearchCompanyResultDto {

	public SearchCompanyResultDto() {

	}

	/**
	 * 询价单ID
	 */
	private Long inqueryOrderId;
	// 卖家所在城市ID
	private Long cityId;
	// 卖家id与名称
	private Long sellerId;
	private String sellerName;
	// 仓库ID
	private Long wareHouseId;
	// 是否银票预付 : 1 银票预付 2 现金预付 3 无预付 cust_account的payment_label字段
	private String payMentLaybel;
	// 是否白名单 :卖家客户标示 白名单 非白名单 非供应商等 ，cust_account的supplier_label字段
	private String supplierLabel;
	// 包销返佣 : 字段暂时使用不到，数据库无字段对应
	private String returnBrokerage;

	/**
	 * 最后更新时间
	 */
	private Date lastUpdatedTime;

	/**
	 * 最后一次交易时间
	 */
	private Date lastTradeTime;

	/**
	 * 最后一次询价时间
	 */
	private Date lastInquiryedTime;

	/**
	 * 资源列表
	 */

	// Map<PurchaseOrderItems, List<SearchResourceDtoTwo>> resourceMap;

	private List<SearchItermResultDto> itermResourceList;
	/**
	 * 资源满足率， 满足item数量/求购单总数，查询出来后计算
	 */
	private Double rate;
	private List<SearchCompanyContactDto> contactList;

	/**
	 * 是否已询价,0 false，1 true
	 */
	private Integer isInquired = 0;

	public String getSellerName() {
		return sellerName;
	}

	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}

	public Long getSellerId() {
		return sellerId;
	}

	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}

	public String getPayMentLaybel() {
		return payMentLaybel;
	}

	public void setPayMentLaybel(String payMentLaybel) {
		this.payMentLaybel = payMentLaybel;
	}

	public String getSupplierLabel() {
		return supplierLabel;
	}

	public void setSupplierLabel(String supplierLabel) {
		this.supplierLabel = supplierLabel;
	}

	public String getReturnBrokerage() {
		return returnBrokerage;
	}

	public void setReturnBrokerage(String returnBrokerage) {
		this.returnBrokerage = returnBrokerage;
	}

	public List<SearchItermResultDto> getItermResourceList() {
		return itermResourceList;
	}

	public void setItermResourceList(List<SearchItermResultDto> itermResourceList) {
		this.itermResourceList = itermResourceList;
	}

	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	public Long getWareHouseId() {
		return wareHouseId;
	}

	public void setWareHouseId(Long wareHouseId) {
		this.wareHouseId = wareHouseId;
	}

	public Date getLastUpdatedTime() {
		return lastUpdatedTime;
	}

	public void setLastUpdatedTime(Date lastUpdatedTime) {
		this.lastUpdatedTime = lastUpdatedTime;
	}

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

	public Long getInqueryOrderId() {
		return inqueryOrderId;
	}

	public void setInqueryOrderId(Long inqueryOrderId) {
		this.inqueryOrderId = inqueryOrderId;
	}

	public Integer getIsInquired() {
		return isInquired;
	}

	public void setIsInquired(Integer isInquired) {
		this.isInquired = isInquired;
	}

	public Date getLastInquiryedTime() {
		return lastInquiryedTime;
	}

	public void setLastInquiryedTime(Date lastInquiryedTime) {
		this.lastInquiryedTime = lastInquiryedTime;
	}

	public Date getLastTradeTime() {
		return lastTradeTime;
	}

	public void setLastTradeTime(Date lastTradeTime) {
		this.lastTradeTime = lastTradeTime;
	}

	public List<SearchCompanyContactDto> getContactList() {
		return contactList;
	}

	public void setContactList(List<SearchCompanyContactDto> contactList) {
		this.contactList = contactList;
	}

}
