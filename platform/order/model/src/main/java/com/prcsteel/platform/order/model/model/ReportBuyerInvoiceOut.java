package com.prcsteel.platform.order.model.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 客户销项报表
 * @author tuxianming
 *
 */
public class ReportBuyerInvoiceOut {
    private Long id;

    private Long buyerId;		//买家名id

    private String buyName;		//买家公司名

    private Long orderId;		//订单名

    private String orderCode;	//订单编号

    private String contractCode;	//合同单号

    private BigDecimal orderAmount; //交易发生额

    private BigDecimal invoiceOutAmount;//销项票发生额

    private BigDecimal invoiceOutBalance; //应开销项余额

    private String operationType;

    private String remark;

    private String invoiceNo;  //单据号

    private Date created;

    private String createdBy;

    private Date lastUpdated;

    private String lastUpdatedBy;

    private Integer modificationNumber;

    private String rowId;

    private String parentRowId;

    public ReportBuyerInvoiceOut(){}
    public ReportBuyerInvoiceOut(Long orderId, BigDecimal amount, String djh, Long buyerId){
    	this.orderId = orderId;
    	this.buyerId = buyerId;
    	this.invoiceNo = djh;
    	this.invoiceOutAmount = amount;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(Long buyerId) {
        this.buyerId = buyerId;
    }

    public String getBuyName() {
        return buyName;
    }

    public void setBuyName(String buyName) {
        this.buyName = buyName;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public BigDecimal getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }

    public BigDecimal getInvoiceOutAmount() {
        return invoiceOutAmount;
    }

    public void setInvoiceOutAmount(BigDecimal invoiceOutAmount) {
        this.invoiceOutAmount = invoiceOutAmount;
    }

    public BigDecimal getInvoiceOutBalance() {
        return invoiceOutBalance;
    }

    public void setInvoiceOutBalance(BigDecimal invoiceOutBalance) {
        this.invoiceOutBalance = invoiceOutBalance;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
}