package com.prcsteel.platform.smartmatch.model.dto;

import java.util.List;

import com.prcsteel.platform.core.model.dto.CategoryDto;
import com.prcsteel.platform.smartmatch.model.model.ReportResourceInventory;

/**
 * Created by chenchen on 2015/8/6.
 */
public class CategoryGroupDto {
	private Integer id;
	private String categoryGroupUuid;
	private String categoryGroupName;
	private String parentUuid;
	private List<CategoryDto> CategoryDtoList;
	/**
	 * 资源
	 */
	private List<ReportResourceInventory>  resources;

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

	public List<ReportResourceInventory> getResources() {
		return resources;
	}

	public void setResources(List<ReportResourceInventory> resources) {
		this.resources = resources;
	}
}
