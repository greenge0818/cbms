package com.prcsteel.platform.smartmatch.model.dto;

import java.util.Date;

public class InquiryOrderSellersDto {
	private Date lastUpdated;
	private String lastUserName;
	private Long sellerId;
	//最近询价时间
	private String lastInquirytime;
	public Date getLastUpdated() {
		return lastUpdated;
	}
	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	public String getLastUserName() {
		return lastUserName;
	}
	public void setLastUserName(String lastUserName) {
		this.lastUserName = lastUserName;
	}
	public Long getSellerId() {
		return sellerId;
	}
	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}
	public String getLastInquirytime() {
		return lastInquirytime;
	}
	public void setLastInquirytime(String lastInquirytime) {
		this.lastInquirytime = lastInquirytime;
	}
}
