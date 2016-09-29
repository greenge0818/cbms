package com.prcsteel.platform.smartmatch.model.dto;

import com.prcsteel.platform.core.model.dto.BaseDataDto;

public class AttributeDataDto extends BaseDataDto{
	private String type;

	private String options;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getOptions() {
		return options;
	}

	public void setOptions(String options) {
		this.options = options;
	}
}
