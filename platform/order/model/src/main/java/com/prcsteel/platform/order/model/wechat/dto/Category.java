
    /**  
    * @Title: Category.java
    * @Package com.prcsteel.platform.order.model.wechat.dto
    * @Description: TODO(用一句话描述该文件做什么)
    * @author wangxianjun
    * @date 2016年3月21日
    * @version V1.0  
    */
    
package com.prcsteel.platform.order.model.wechat.dto;


    /**
    * @ClassName: Category
    * @Description: 名品
    * @author wangxianjun
    * @date 2016年3月21日
    *
    */

public class Category {
	String uuid;
	String name;
	
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
	
}
