package com.prcsteel.platform.core.model.dto;

import java.util.List;

/**
 * Created by chenchen on 2015/8/6.
 */
public class CategoryGroupDto {
	private Integer id;
	private String categoryGroupUuid;
	private String categoryGroupName;
	private String parentUuid;
	private List<CategoryDto> CategoryDtoList;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCategoryGroupUuid() {
		return categoryGroupUuid;
	}

	public void setCategoryGroupUuid(String categoryGroupUuid) {
		this.categoryGroupUuid = categoryGroupUuid;
	}

	public String getCategoryGroupName() {
		return categoryGroupName;
	}

	public void setCategoryGroupName(String categoryGroupName) {
		this.categoryGroupName = categoryGroupName;
	}

	public String getParentUuid() {
		return parentUuid;
	}

	public void setParentUuid(String parentUuid) {
		this.parentUuid = parentUuid;
	}

	public List<CategoryDto> getCategoryDtoList() {
		return CategoryDtoList;
	}

	public void setCategoryDtoList(List<CategoryDto> categoryDtoList) {
		CategoryDtoList = categoryDtoList;
	}
}
