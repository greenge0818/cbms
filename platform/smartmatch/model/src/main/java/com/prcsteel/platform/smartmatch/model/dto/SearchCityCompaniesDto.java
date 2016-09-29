package com.prcsteel.platform.smartmatch.model.dto;

import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import com.prcsteel.platform.core.model.model.City;

public class SearchCityCompaniesDto {

	private Integer cpCount;
	private City city;
	private List<SearchCompanyResultDto> compamies;

	public SearchCityCompaniesDto(City currcity, List<SearchCompanyResultDto> currcityCompanies) {
		this.city = currcity;
		this.compamies = currcityCompanies;
		this.cpCount = currcityCompanies.size();
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public List<SearchCompanyResultDto> getCompamies() {
		return compamies;
	}

	public void setCompamies(List<SearchCompanyResultDto> compamies) {
		this.compamies = compamies;
	}

	public Integer getCpCount() {
		return cpCount;
	}

	public void setCpCount(Integer cpCount) {
		this.cpCount = cpCount;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

}
