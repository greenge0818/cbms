package com.prcsteel.platform.core.model.dto;

import com.prcsteel.platform.core.model.model.District;

public class DistrictDto {
    private District district;

    private CityDto city;

    public CityDto getCity() {
		return city;
	}

	public void setCity(CityDto city) {
		this.city = city;
	}

	public District getDistrict() {
		return district;
	}

	public void setDistrict(District district) {
		this.district = district;
	}

}