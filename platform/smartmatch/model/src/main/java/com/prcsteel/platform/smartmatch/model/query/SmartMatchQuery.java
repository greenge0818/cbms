
    /**  
    * @Title: SmartMatchQuery.java
    * @Package com.prcsteel.platform.order.model.query
    * @Description: 
    * @author Green.Ge
    * @date 2015年11月27日
    * @version V1.0  
    */
    
package com.prcsteel.platform.smartmatch.model.query;

import java.util.List;

/**
    * @ClassName: SmartMatchQuery
    * @Description: 用于查询智能匹配卖家权重
    * @author Green.Ge
    * @date 2015年11月27日
    *
    */

public class SmartMatchQuery {
	//采购单ID
	Long purchaseOrderId;
	//卖家范围（1,2,3,4,5）
	String sellers;
	//指定采购资源列表
//	List<Long> itemIds;
	List<Long> purchaseOrderItemIds;
	//指定不查询某个卖家
	List<Long> noSpecificSellerList;
	//指定查询某个卖家
	Long specificSellerId;
	public Long getPurchaseOrderId() {
		return purchaseOrderId;
	}
	public void setPurchaseOrderId(Long purchaseOrderId) {
		this.purchaseOrderId = purchaseOrderId;
	}
	public String getSellers() {
		return sellers;
	}
	public void setSellers(String sellers) {
		this.sellers = sellers;
	}
//	public List<Long> getItemIds() {
//		return itemIds;
//	}
//	public void setItemIds(List<Long> itemIds) {
//		this.itemIds = itemIds;
//	}
	public List<Long> getNoSpecificSellerList() {
		return noSpecificSellerList;
	}
	public void setNoSpecificSellerList(List<Long> noSpecificSellerList) {
		this.noSpecificSellerList = noSpecificSellerList;
	}
	public Long getSpecificSellerId() {
		return specificSellerId;
	}
	public void setSpecificSellerId(Long specificSellerId) {
		this.specificSellerId = specificSellerId;
	}
	
	
	public List<Long> getPurchaseOrderItemIds() {
		return purchaseOrderItemIds;
	}
	public void setPurchaseOrderItemIds(List<Long> purchaseOrderItemIds) {
		this.purchaseOrderItemIds = purchaseOrderItemIds;
	}
	public SmartMatchQuery(Long purchaseOrderId, String sellers, List<Long> purchaseOrderItemIds,
			List<Long> noSpecificSellerList, Long specificSellerId) {
		super();
		this.purchaseOrderId = purchaseOrderId;
		this.sellers = sellers;
		this.purchaseOrderItemIds = purchaseOrderItemIds;
		this.noSpecificSellerList = noSpecificSellerList;
		this.specificSellerId = specificSellerId;
	}
	
	
}
