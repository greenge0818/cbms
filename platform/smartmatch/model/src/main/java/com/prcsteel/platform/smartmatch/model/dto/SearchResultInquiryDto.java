package com.prcsteel.platform.smartmatch.model.dto;

import java.util.List;

/**
 * 询价之卖家集
 */
public class SearchResultInquiryDto {
	/**
	 * 卖家信息列表
	 */
	List<SearchResultSellerDto> sellerList;
	
	private String isInquiry;
	
	private String isSave;
	
	private Long inquiryOrderId;
	
	public Long getInquiryOrderId() {
		return inquiryOrderId;
	}

	public void setInquiryOrderId(Long inquiryOrderId) {
		this.inquiryOrderId = inquiryOrderId;
	}

	public String getIsSave() {
		return isSave;
	}

	public void setIsSave(String isSave) {
		this.isSave = isSave;
	}

	public String getIsInquiry() {
		return isInquiry;
	}

	public void setIsInquiry(String isInquiry) {
		this.isInquiry = isInquiry;
	}

	public List<SearchResultSellerDto> getSellerList() {
		return sellerList;
	}

	public void setSellerList(List<SearchResultSellerDto> sellerList) {
		this.sellerList = sellerList;
	}

	public SearchResultInquiryDto(List<SearchResultSellerDto> sellerList, String isInquiry, String isSave, Long inquiryOrderId) {
		super();
		this.sellerList = sellerList;
		this.isInquiry = isInquiry;
		this.isSave = isSave;
		this.inquiryOrderId = inquiryOrderId;
	}
	
}
