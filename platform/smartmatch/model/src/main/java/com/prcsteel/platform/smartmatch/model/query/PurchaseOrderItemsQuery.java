package com.prcsteel.platform.smartmatch.model.query;

/**
 * 用于采购单填写时选择联系人号码自动弹出最近采购记录&&采购单重新询价Query
 * 
 * @author Rabbit
 *
 */
public class PurchaseOrderItemsQuery {
	String tel;
	String buyerName;

	public PurchaseOrderItemsQuery() {
	}

	public PurchaseOrderItemsQuery(String tel, String buyerName) {
		this.tel = tel;
		this.buyerName = buyerName;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getBuyerName() {
		return buyerName;
	}

	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}
}
