package com.prcsteel.platform.order.model.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dq on 15-9-14.
 */
public class InvoiceOutApplyDetailListVoDto {
	private Long buyerId;
	private String buyerName;
	// 已申请开票金额
	private BigDecimal amount = BigDecimal.ZERO;
	// 未开票金额
	private BigDecimal unAmount = BigDecimal.ZERO;
	// hidden 未开票重量
	private BigDecimal unWeight = BigDecimal.ZERO;
	// 自动分配开票金额
	private BigDecimal automaticAmount = BigDecimal.ZERO;
	// 手动分配开票金额
	private BigDecimal manualAmount = BigDecimal.ZERO;
	// 二结应收金额
	private BigDecimal balanceSecondSettlement = BigDecimal.ZERO;
	// 未开发票超期月数
	private int countMonth;
	// 开票资料是否审核通过; 0/否、1/是
	private String isOkId;
	private String isOkName;
	// 分配状态 ; 0/可分配、1/已分配、-1/不可分配
	private String isAllotId;
	private String isAllotName;
	//部门ID
	private Long departmentId;
	//部门名称
	private String departmentName;

	private String sellerCredential;  	//卖家凭证是否审核通过：是，否，不需要审核.审核不通过
	private String buyerCredential;		//买家凭证是否审核通过:是，否，不需要审核，审核不通过
	
	private List<InvoiceOutApplyDetailItemsVoDto> itemsList = new ArrayList<InvoiceOutApplyDetailItemsVoDto>();

	public void addItems(InvoiceOutApplyDetailItemsVoDto items) {
		this.itemsList.add(items);
	}

	public void setItemsList(List<InvoiceOutApplyDetailItemsVoDto> itemsList) {
		this.itemsList = itemsList;
	}

	public List<InvoiceOutApplyDetailItemsVoDto> getItemsList() {
		return itemsList;
	}

	public String getBuyerName() {
		return buyerName;
	}

	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getAutomaticAmount() {
		return automaticAmount;
	}

	public void setAutomaticAmount(BigDecimal automaticAmount) {
		this.automaticAmount = automaticAmount;
	}

	public BigDecimal getManualAmount() {
		return manualAmount;
	}

	public void setManualAmount(BigDecimal manualAmount) {
		this.manualAmount = manualAmount;
	}

	public BigDecimal getBalanceSecondSettlement() {
		return balanceSecondSettlement;
	}

	public void setBalanceSecondSettlement(BigDecimal balanceSecondSettlement) {
		this.balanceSecondSettlement = balanceSecondSettlement;
	}

	public int getCountMonth() {
		return countMonth;
	}

	public void setCountMonth(int countMonth) {
		this.countMonth = countMonth;
	}

	public String getIsOkId() {
		return isOkId;
	}

	public void setIsOkId(String isOkId) {
		this.isOkId = isOkId;
	}

	public String getIsOkName() {
		return isOkName;
	}

	public void setIsOkName(String isOkName) {
		this.isOkName = isOkName;
	}

	public String getIsAllotId() {
		return isAllotId;
	}

	public void setIsAllotId(String isAllotId) {
		this.isAllotId = isAllotId;
	}

	public String getIsAllotName() {
		return isAllotName;
	}

	public void setIsAllotName(String isAllotName) {
		this.isAllotName = isAllotName;
	}

	public BigDecimal getUnAmount() {
		return unAmount;
	}

	public void setUnAmount(BigDecimal unAmount) {
		this.unAmount = unAmount;
	}

	public BigDecimal getUnWeight() {
		return unWeight;
	}

	public void setUnWeight(BigDecimal unWeight) {
		this.unWeight = unWeight;
	}

	public Long getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(Long buyerId) {
		this.buyerId = buyerId;
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	public String getSellerCredential() {
		return sellerCredential;
	}

	public void setSellerCredential(String sellerCredential) {
		this.sellerCredential = sellerCredential;
	}

	public String getBuyerCredential() {
		return buyerCredential;
	}

	public void setBuyerCredential(String buyerCredential) {
		this.buyerCredential = buyerCredential;

	}
}
