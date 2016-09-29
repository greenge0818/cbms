package com.prcsteel.platform.smartmatch.model.dto;

import java.math.BigDecimal;

public class ResourceBusinessChildDto extends ResourceBaseDto{
	//前台展示标记
	private String showIndex;

	public ResourceBusinessChildDto(String showIndex,Long accountId){
		this.showIndex = showIndex;
		setAccountId(accountId);
	}
	public ResourceBusinessChildDto(String showIndex,Long id,Long accountId,String accountName,String categoryName,String materialName,String spec,
			String factoryName,String cityName,String weightConcept,BigDecimal price, String categoryUuid,BigDecimal singleWeight){
		this.showIndex = showIndex;
		setId(id);
		setAccountId(accountId);
		setAccountName(accountName);
		setCategoryName(categoryName);
		setMaterialName(materialName);
		setSpec(spec);
		setFactoryName(factoryName);
		setCityName(cityName);
		setWeightConcept(weightConcept);
		setPrice(price);
		setCategoryUuid(categoryUuid);
		setSingleWeight(singleWeight);
	}
	
	public String getShowIndex() {
		return showIndex;
	}
	public void setShowIndex(String showIndex) {
		this.showIndex = showIndex;
	}
}
