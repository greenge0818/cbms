package com.prcsteel.platform.core.model.query;

public class CategoryAliasQuery {
	private String aliasName;
	private Long categoryId;
	private Boolean like;
	public String getAliasName() {
		return aliasName;
	}
	public CategoryAliasQuery setAliasName(String aliasName) {
		this.aliasName = aliasName;
		return this;
	}
	public Long getCategoryId() {
		return categoryId;
	}
	public CategoryAliasQuery setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
		return this;
	}
	public Boolean getLike() {
		return like;
	}
	public CategoryAliasQuery setLike(Boolean like) {
		this.like = like;
		return this;
	}
	
}
