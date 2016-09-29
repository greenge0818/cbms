package com.prcsteel.platform.core.model.dto;

/**
 * Created by chenchen on 2015/8/6.
 */
public class CategoryGroupRecordDto {
   private Integer id;
   private Integer categoryId;
   private String categoryUuid;
   private String categoryName;
   private String categoryGroupUuid;
   private String categoryGroupName;
   private String siteUuid;
    public Integer getId() {
	return id;
}
public void setId(Integer id) {
	this.id = id;
}
public String getCategoryUuid() {
	return categoryUuid;
}
public void setCategoryUuid(String categoryUuid) {
	this.categoryUuid = categoryUuid;
}
public String getCategoryGroupUuid() {
	return categoryGroupUuid;
}
public void setCategoryGroupUuid(String categoryGroupUuid) {
	this.categoryGroupUuid = categoryGroupUuid;
}
public String getCategoryName() {
	return categoryName;
}
public void setCategoryName(String categoryName) {
	this.categoryName = categoryName;
}
public String getCategoryGroupName() {
	return categoryGroupName;
}
public void setCategoryGroupName(String categoryGroupName) {
	this.categoryGroupName = categoryGroupName;
}
public String getSiteUuid() {
	return siteUuid;
}
public void setSiteUuid(String siteUuid) {
	this.siteUuid = siteUuid;
}
public Integer getCategoryId() {
	return categoryId;
}
public void setCategoryId(Integer categoryId) {
	this.categoryId = categoryId;
}

}
