package com.prcsteel.platform.order.model.dto;

import java.math.BigDecimal;

/**
 * Created by rolyer on 15-8-3.
 */
public class InvoiceApplicationDto {

    private Long ownerId;                           //业务员编号
    private String ownerName;                       //业务员姓名
    private Long orgId;                             //服务中心编号
    private String orgName;                         //服务中心名称
    private Long buyerId;                           //公司ID
    private String buyerName;                       //公司名称
    private Integer accountStatus;                  //公司审核状态
    private boolean hasApplied;                     //已申请
    private String invoiceDataStatus;				//买家开票资料状态1.审核通过 2.待审核 3.资料不全 4。审核失败 
    private BigDecimal balanceSecondSettlement = BigDecimal.ZERO;  		//二次结算应收金额
    private Long departmentId;                           //部门ID
    private String departmentName;                       //部门名称

    public String getInvoiceDataStatus() {
		return invoiceDataStatus;
	}

	public void setInvoiceDataStatus(String invoiceDataStatus) {
		this.invoiceDataStatus = invoiceDataStatus;
	}

	public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
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

    public Long getBuyerId() {
        return buyerId;
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

    public BigDecimal getBalanceSecondSettlement() {
        return balanceSecondSettlement;
    }

    public void setBalanceSecondSettlement(BigDecimal balanceSecondSettlement) {
        this.balanceSecondSettlement = balanceSecondSettlement;
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

    public Integer getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(Integer accountStatus) {
        this.accountStatus = accountStatus;
    }

    public boolean isHasApplied() {
        return hasApplied;
    }

    public void setHasApplied(boolean hasApplied) {
        this.hasApplied = hasApplied;
    }
    
}
