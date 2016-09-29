package com.prcsteel.platform.smartmatch.model.dto;

import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

public class SearchItermResultDto {

	private PurchaseOrderItemsDtoTwo item;
	private List<SearchResourceDtoTwo> resourceList;

	public SearchItermResultDto(PurchaseOrderItemsDtoTwo item2, List<SearchResourceDtoTwo> cpResList) {
		this.item = item2;
		this.resourceList = cpResList;
	}

	
	public PurchaseOrderItemsDtoTwo getItem() {
		return item;
	}


	public void setItem(PurchaseOrderItemsDtoTwo item) {
		this.item = item;
	}


	public List<SearchResourceDtoTwo> getResourceList() {
		return resourceList;
	}

	public void setResourceList(List<SearchResourceDtoTwo> resourceList) {
		this.resourceList = resourceList;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

}
