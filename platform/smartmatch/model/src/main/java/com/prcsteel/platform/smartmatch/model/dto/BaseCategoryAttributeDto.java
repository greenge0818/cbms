package com.prcsteel.platform.smartmatch.model.dto;
/**
 * @description:品名id对应的属性集合
 * @author :zhoucai
 * @date 2016-6-23
 */
public class BaseCategoryAttributeDto {
	private String id;
	
	private String name;

	private String options;

	private String type;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOptions() {
		return options;
	}

	public void setOptions(String options) {
		this.options = options;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
