package com.prcsteel.platform.smartmatch.model.dto;

import java.util.List;

/**
 * 询价结果集
 */
public class SearchResultDto {
	/**
	 * 询价信息列表
	 */
	List<SearchResultInquiryDto> inquiryList;

	public List<SearchResultInquiryDto> getInquiryList() {
		return inquiryList;
	}

	public void setInquiryList(List<SearchResultInquiryDto> inquiryList) {
		this.inquiryList = inquiryList;
	}

	public SearchResultDto(List<SearchResultInquiryDto> inquiryList) {
		super();
		this.inquiryList = inquiryList;
	}
	
}
