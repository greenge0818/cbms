package com.prcsteel.platform.smartmatch.model.query;

import com.prcsteel.platform.common.query.PagedQuery;

/**
 * 基价关联设置列表查询条件
 * @author tangwei
 *
 */
public class CustBasePriceRelationQuery  extends PagedQuery{
	private String accountName;//客户名称
	private String basePriceName;//基价名称
	private String cityName;//地区
	private String categoryName;//品名
	private String factoryName;//工厂名
	
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getBasePriceName() {
		return basePriceName;
	}
	public void setBasePriceName(String basePriceName) {
		this.basePriceName = basePriceName;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getFactoryName() {
		return factoryName;
	}
	public void setFactoryName(String factoryName) {
		this.factoryName = factoryName;
	}
}
