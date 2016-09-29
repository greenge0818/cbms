package com.prcsteel.platform.core.model.query;

import com.prcsteel.platform.common.query.PagedQuery;

/**
 * 区域管理查询Query
 * 
 * @author peanut
 *
 */
public class AreaQuery  extends PagedQuery {
	/**区域名称**/
	private String name;
	/**中心城市名称**/
	private String centerCityName;
	/**辐射城市名称**/
	private String refsCityName;
	/**中心城市id**/
	private String centerCityId;
	/**辐射城市id**/
	private String refsCityId;
	
	/**
	 * 是否启用
	 */
	private String isEnable;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCenterCityName() {
		return centerCityName;
	}
	public void setCenterCityName(String centerCityName) {
		this.centerCityName = centerCityName;
	}
	public String getRefsCityName() {
		return refsCityName;
	}
	public void setRefsCityName(String refsCityName) {
		this.refsCityName = refsCityName;
	}
	public String getCenterCityId() {
		return centerCityId;
	}
	public void setCenterCityId(String centerCityId) {
		this.centerCityId = centerCityId;
	}
	public String getRefsCityId() {
		return refsCityId;
	}
	public void setRefsCityId(String refsCityId) {
		this.refsCityId = refsCityId;
	}
	public String getIsEnable() {
		return isEnable;
	}
	public void setIsEnable(String isEnable) {
		this.isEnable = isEnable;
	}
	
	
}
