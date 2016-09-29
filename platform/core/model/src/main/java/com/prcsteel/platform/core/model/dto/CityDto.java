package com.prcsteel.platform.core.model.dto;

import com.prcsteel.platform.core.model.model.City;
import com.prcsteel.platform.core.model.model.Province;

public class CityDto {
    private City city;

    private Province province;

    public Province getProvince() {
		return province;
	}

	public void setProvince(Province province) {
		this.province = province;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

}