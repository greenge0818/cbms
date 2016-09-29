package com.prcsteel.platform.order.model.dto;

import java.math.BigDecimal;


import com.prcsteel.platform.order.model.model.ConsignOrder;

/**
 * Created by wangxianjun on 2016/5/11.
 */
public class ContractListDto extends ConsignOrder {

    private String type;

    private String contractCode;
    
    private String accountName;
    
    private String orderOrgName;
    
    private String ownerName;

    private String createdTime;

    private BigDecimal totalAmount;
    
    private String note;

	private String custLabel;

	public String getCustLabel() {
		return custLabel;
	}

	public void setCustLabel(String custLabel) {
		this.custLabel = custLabel;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getContractCode() {
		return contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getOrderOrgName() {
		return orderOrgName;
	}

	public void setOrderOrgName(String orderOrgName) {
		this.orderOrgName = orderOrgName;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

}
