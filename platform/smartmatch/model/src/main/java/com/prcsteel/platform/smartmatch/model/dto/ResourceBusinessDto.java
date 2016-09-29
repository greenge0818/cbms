package com.prcsteel.platform.smartmatch.model.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 业务找货列表
 * @author tangwei
 *
 */
public class ResourceBusinessDto extends ResourceBaseDto{

	private String supplierLabel;//卖家客户标示 白名单 非白名单 非供应商等
	
	private String payMentLaybel;//'预付款标示 1 银票预付 2 现金预付 3 无预付'
	
	private String timeStr = "";//询价时间和当前时间的时间差
	
	private String orgName;//服务中心
	
	private Double totalWeight;
	
	private Boolean isComplete = true;//标记客户货物是否齐全
	
	private double lowPrice;//同一个客户最低的资源
	
	private List<ResourceBusinessChildDto> childList = new ArrayList<ResourceBusinessChildDto>();//子项

	public ResourceBusinessDto() {
		super();
	}
	
	public String getSupplierLabel() {
		return supplierLabel;
	}

	public void setSupplierLabel(String supplierLabel) {
		this.supplierLabel = supplierLabel;
	}

	public String getPayMentLaybel() {
		return payMentLaybel;
	}

	public void setPayMentLaybel(String payMentLaybel) {
		this.payMentLaybel = payMentLaybel;
	}

	public String getTimeStr() {
		return timeStr;
	}

	public void setTimeStr(String timeStr) {
		this.timeStr = timeStr;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public Double getTotalWeight() {
		return totalWeight;
	}

	public void setTotalWeight(Double totalWeight) {
		this.totalWeight = totalWeight;
	}

	public List<ResourceBusinessChildDto> getChildList() {
		return childList;
	}

	public void setChildList(List<ResourceBusinessChildDto> childList) {
		this.childList = childList;
	}
	

	public Boolean getIsComplete() {
		return isComplete;
	}

	public void setIsComplete(Boolean isComplete) {
		this.isComplete = isComplete;
	}

	public double getLowPrice() {
		return lowPrice;
	}

	public void setLowPrice(double lowPrice) {
		this.lowPrice = lowPrice;
	}

}
