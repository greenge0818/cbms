package com.prcsteel.platform.smartmatch.model.query;

import com.prcsteel.platform.common.query.PagedQuery;

public class CategoryAttributeQuery  extends PagedQuery {
	
	/**属性名称**/
	private String attrName;
	/**品名名称**/
	private String categoryName;
	/**大类名称**/
	private String groupName;
	/**品名uuid**/
	private String categoryUuid;
	
	public String getAttrName() {
		return attrName;
	}
	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getCategoryUuid() {
		return categoryUuid;
	}
	public void setCategoryUuid(String categoryUuid) {
		this.categoryUuid = categoryUuid;
	}
	
}
