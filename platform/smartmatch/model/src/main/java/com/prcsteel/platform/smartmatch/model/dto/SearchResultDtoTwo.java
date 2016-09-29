package com.prcsteel.platform.smartmatch.model.dto;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

public class SearchResultDtoTwo {

	public SearchResultDtoTwo() {
		inquiryList = new ArrayList<SearchCityCompaniesDto>();
	}

	List<SearchCityCompaniesDto> inquiryList;

	public List<SearchCityCompaniesDto> getInquiryList() {
		return inquiryList;
	}

	public void setInquiryList(List<SearchCityCompaniesDto> inquiryList) {
		this.inquiryList = inquiryList;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

}
