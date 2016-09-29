package com.prcsteel.platform.smartmatch.model.query;

import com.prcsteel.platform.common.query.PagedQuery;

public class WarehouseQuery extends PagedQuery {

	private String name;
	private String city;


	public String getName() {
		return name;
	}

	public String getCity() {
		return city;
	}



	public void setName(String name) {
		this.name = name;
	}

	public void setCity(String city) {
		this.city = city;
	}
	
}
