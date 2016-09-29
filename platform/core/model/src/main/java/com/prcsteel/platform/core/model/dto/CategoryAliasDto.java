package com.prcsteel.platform.core.model.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.prcsteel.platform.core.model.model.CategoryAlias;

public class CategoryAliasDto {

	private String categoryName;
	private Long categoryId;
	private List<CategoryAlias> alias;
	private List<String> aliasNames;

	public List<String> getAliasNames() {
		return aliasNames;
	}

	public void setAliasNames(String aliasNames) {
		if(StringUtils.isNotBlank(aliasNames)){
			this.aliasNames = Arrays.asList(aliasNames.split(","));
		}
	}

	public void setAliasNames(List<String> aliasNames) {
		this.aliasNames = aliasNames;
	}
	
	public CategoryAliasDto( String categoryName, Long categoryId, List<CategoryAlias> alias) {
		super();
		this.categoryName = categoryName;
		this.categoryId = categoryId;
		this.alias = alias;
	}

	public CategoryAliasDto() {
		super();
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public List<CategoryAlias> getAlias() {
		return alias;
	}

	public void setAlias(List<CategoryAlias> alias) {
		this.alias = alias;
	}

	public void addAlias(CategoryAlias as){
		if(this.alias==null){
			this.alias = new ArrayList<>();
		}
		
		if(as.getId()!=null)
			this.alias.add(as);
	}

}
