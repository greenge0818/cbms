
    /**  
    * @Title: CategoryGroup.java
    * @Package com.prcsteel.platform.order.model.wechat.dto
    * @Description: TODO(用一句话描述该文件做什么)
    * @author wangxianjun
    * @date 2016年3月14日
    * @version V1.0  
    */
    
package com.prcsteel.platform.order.model.wechat.dto;

import java.util.List;

/**
    * @ClassName: CategoryGroup
    * @Description: 大类
    * @author wangxianjun
    * @date 2016年3月14日
    *
    */

public class CategoryGroup {
	String uuid;
	
	String name;
	
	List<Category> categoryList;
	
	Integer count;
	
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Category> getCategoryList() {
		return categoryList;
	}
	public void setCategoryList(List<Category> categoryList) {
		this.categoryList = categoryList;
	}
	public Integer getCount() {
		return categoryList==null?0:categoryList.size();
	}
	
}
