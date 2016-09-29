package com.prcsteel.platform.order.model.model;

import java.math.BigDecimal;
import java.util.Date;

public class ConsignOrderContract {
    private Long id;

    private Long consignOrderId;

    private Integer changeOrderId;

    private String contractCodeAuto;

    private String contractCodeCust;

    private String bankNameMain;

    private String bankNameBranch;

    private String bankAccountCode;

    private Long customerId;

    private String customerName;

    private Long customerDepartmentId;

    private String customerDepartmentName;

    private String customerAddr;

    private String customerTel;

    private BigDecimal orderAmount;

    private BigDecimal balanceSecondSettlement;

    private BigDecimal applyPayAmount;

    private Integer status;

    private Date created;

    private String createdBy;

    private Date lastUpdated;

    private String lastUpdatedBy;

    private Integer modificationNumber;

    private String rowId;

    private String parentRowId;

    public Integer getChangeOrderId() {
        return changeOrderId;
    }

    public void setChangeOrderId(Integer changeOrderId) {
        this.changeOrderId = changeOrderId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getConsignOrderId() {
        return consignOrderId;
    }

    public void setConsignOrderId(Long consignOrderId) {
        this.consignOrderId = consignOrderId;
    }

    public String getContractCodeAuto() {
        return contractCodeAuto;
    }

    public void setContractCodeAuto(String contractCodeAuto) {
        this.contractCodeAuto = contractCodeAuto;
    }

    public String getContractCodeCust() {
        return contractCodeCust;
    }

    public void setContractCodeCust(String contractCodeCust) {
        this.contractCodeCust = contractCodeCust;
    }

    public String getBankNameMain() {
        return bankNameMain;
    }

    public void setBankNameMain(String bankNameMain) {
        this.bankNameMain = bankNameMain;
    }

    public String getBankNameBranch() {
        return bankNameBranch;
    }

    public void setBankNameBranch(String bankNameBranch) {
        this.bankNameBranch = bankNameBranch;
    }

    public String getBankAccountCode() {
        return bankAccountCode;
    }

    public void setBankAccountCode(String bankAccountCode) {
        this.bankAccountCode = bankAccountCode;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Long getCustomerDepartmentId() {
        return customerDepartmentId;
    }

    public void setCustomerDepartmentId(Long customerDepartmentId) {
        this.customerDepartmentId = customerDepartmentId;
    }

    public String getCustomerDepartmentName() {
        return customerDepartmentName;
    }

    public void setCustomerDepartmentName(String customerDepartmentName) {
        this.customerDepartmentName = customerDepartmentName;
    }

    public String getCustomerAddr() {
        return customerAddr;
    }

    public void setCustomerAddr(String customerAddr) {
        this.customerAddr = customerAddr;
    }

    public String getCustomerTel() {
        return customerTel;
    }

    public void setCustomerTel(String customerTel) {
        this.customerTel = customerTel;
    }

    public BigDecimal getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }

    public BigDecimal getBalanceSecondSettlement() {
        return balanceSecondSettlement;
    }

    public void setBalanceSecondSettlement(BigDecimal balanceSecondSettlement) {
        this.balanceSecondSettlement = balanceSecondSettlement;
    }

    public BigDecimal getApplyPayAmount() {
        return applyPayAmount;
    }

    public void setApplyPayAmount(BigDecimal applyPayAmount) {
        this.applyPayAmount = applyPayAmount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public Integer getModificationNumber() {
        return modificationNumber;
    }

    public void setModificationNumber(Integer modificationNumber) {
        this.modificationNumber = modificationNumber;
    }

    public String getRowId() {
        return rowId;
    }

    public void setRowId(String rowId) {
        this.rowId = rowId;
    }

    public String getParentRowId() {
        return parentRowId;
    }

    public void setParentRowId(String parentRowId) {
        this.parentRowId = parentRowId;
    }
}