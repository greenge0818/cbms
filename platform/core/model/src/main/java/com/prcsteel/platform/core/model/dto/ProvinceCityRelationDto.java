package com.prcsteel.platform.core.model.dto;

import com.prcsteel.platform.core.model.model.City;
import com.prcsteel.platform.core.model.model.Province;

public class ProvinceCityRelationDto {
	//城市id
    private int  id;
	//城市名称
    private String name;
	//省份id
	private int  provinceId ;
	//省份名称
	private String  provinceName ;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(int provinceId) {
		this.provinceId = provinceId;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
}