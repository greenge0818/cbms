package com.prcsteel.platform.smartmatch.model.query;

/**
 * 屋子单间重量查询Query
 * 
 * @author Rabbit
 *
 */
public class SingleWeightQuery {
	Long factoryId;
	String categoryUuid;
	String norms;

	public Long getFactoryId() {
		return factoryId;
	}

	public void setFactoryId(Long factoryId) {
		this.factoryId = factoryId;
	}

	public String getCategoryUuid() {
		return categoryUuid;
	}

	public void setCategoryUuid(String categoryUuid) {
		this.categoryUuid = categoryUuid;
	}

	public String getNorms() {
		return norms;
	}

	public void setNorms(String norms) {
		this.norms = norms;
	}
}
