package com.prcsteel.platform.smartmatch.model.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by zhoucai on 2016/6/24.
 */
public class ResourceDailyDtoResult extends ResourceDailyDto implements Serializable {

	private Long cityId;
	private Date lastUpdated;

	public ResourceDailyDtoResult() {
		super();
	}

	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

}