package com.prcsteel.platform.order.model.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.prcsteel.platform.account.model.enums.AccountInvoiceType;

/**
 * 
 * @desc 留存销项报表dto
 * @date 20160621
 * @author tuxianming
 *
 */
public class UnSendInvoiceOutDto {
	private Long checklistId;		//清单号
	private Long checklistDetailId;	//清单详情号
	private Long buyerId;			//买家id
    private String buyerName;		//买家名称
    private String orderCode;		//订单编号
    private String nsortName;		//货物或应税劳务、服务名称
    private String spec;			//规格
    private BigDecimal weight;		//数量
    private BigDecimal amount;		//价税合计
    private Long  orgId;			//服务中心
    private String orgName;			//发票类型
    private String created;			//申请时间
    private String buyerCredentialStatus;		//买家凭证审核状态
    private String sellerCredentialStatus;		//卖家凭证审核状态
    private BigDecimal balanceSecondSettlement;	//二结余额
    private String invoiceType;		//发票类型
    private Boolean isLimitDebet; 
    private Boolean isSend;			//是不是已经寄出
    
    
	public Boolean getIsLimitDebet() {
		return isLimitDebet;
	}
	public void setIsLimitDebet(Boolean isLimitDebet) {
		this.isLimitDebet = isLimitDebet;
	}
	public Long getBuyerId() {
		return buyerId;
	}
	public Long getChecklistId() {
		return checklistId;
	}
	public void setChecklistId(Long checklistId) {
		this.checklistId = checklistId;
	}
	public void setBuyerId(Long buyerId) {
		this.buyerId = buyerId;
	}
	public String getBuyerName() {
		return buyerName;
	}
	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}
	public String getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	public String getNsortName() {
		return nsortName;
	}
	public void setNsortName(String nsortName) {
		this.nsortName = nsortName;
	}
	public String getSpec() {
		return spec;
	}
	public void setSpec(String spec) {
		this.spec = spec;
	}
	public BigDecimal getWeight() {
		return weight;
	}
	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	
	public String getCreated() {
		return created;
	}
	public void setCreated(String created) {
		this.created = created;
	}
	public String getBuyerCredentialStatus() {
		return buyerCredentialStatus;
	}
	public void setBuyerCredentialStatus(String buyerCredentialStatus) {
		this.buyerCredentialStatus = buyerCredentialStatus;
	}
	public String getSellerCredentialStatus() {
		return sellerCredentialStatus;
	}
	public void setSellerCredentialStatus(String sellerCredentialStatus) {
		this.sellerCredentialStatus = sellerCredentialStatus;
	}
	public BigDecimal getBalanceSecondSettlement() {
		return balanceSecondSettlement;
	}
	public void setBalanceSecondSettlement(BigDecimal balanceSecondSettlement) {
		this.balanceSecondSettlement = balanceSecondSettlement;
	}
	public String getInvoiceType() {
		return AccountInvoiceType.getValue(invoiceType);
		//return invoiceType;
	}
	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}
	public Long getChecklistDetailId() {
		return checklistDetailId;
	}
	public void setChecklistDetailId(Long checklistDetailId) {
		this.checklistDetailId = checklistDetailId;
	}
	public Boolean getIsSend() {
		return isSend;
	}
	public void setIsSend(Boolean isSend) {
		this.isSend = isSend;
	}
    
}
