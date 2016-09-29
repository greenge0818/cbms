package com.prcsteel.platform.kuandao.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class ConsignOrderDto implements Serializable {
    
	private static final long serialVersionUID = -1588216676222942255L;

	private Long id;

    private String code;

    private Long accountId;

    private String accountName;

    private Long departmentId;

    private String departmentName;

    private Long ownerId;

    private String ownerName;

    private Long contactId;

    private String contactName;

    private String contactTel;

    private String deliveryType;

    private String feeTaker;

    private BigDecimal shipFee;

    private String outboundTaker;

    private BigDecimal outboundFee;

    private Integer totalQuantity;

    private BigDecimal totalWeight;

    private BigDecimal totalAmount;

    private BigDecimal secondBalanceTakeout;

    private BigDecimal totalContractReletedAmount;

    private String status;

    private String changeStatus;

    private String originStatus;

    private String note;

    private String contractCode;

    private String payStatus;

    private Integer pickupStatus;

    private Integer fillupStatus;

    private String callBackStatus;

    private String callBackNote;

    private String consignType;

    private Long operatorId;

    private String operatorName;

    private String contractAddress;

    private Long orderOrgId;

    private String orderOrgName;

    private Long buyerOrgId;

    private String buyerOrgName;

    private String createdBy;

    private String lastUpdatedBy;

    private Integer modificationNumber;

    private String rowId;

    private String parentRowId;

    private String paymentType;

    private Integer delayNum;

    private String isCountAchievement;

    private String ownerJobNumber;

    private String secondaryTimeStr;
    
    private String buyerCredentialCode;   //买家凭证号
    private String batchBuyerCredentialCode;   //批量买家凭证号

    private Long departmentCount;

    public String getSecondaryTimeStr() {
		return secondaryTimeStr;
	}

	public void setSecondaryTimeStr(String secondaryTimeStr) {
		this.secondaryTimeStr = secondaryTimeStr;
	}

    public String getOwnerJobNumber() {
        return ownerJobNumber;
    }

    public void setOwnerJobNumber(String ownerJobNumber) {
        this.ownerJobNumber = ownerJobNumber;
    }

    public Long getId() {
        return id;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public Integer getDelayNum() {
        return delayNum;
    }

    public void setDelayNum(Integer delayNum) {
        this.delayNum = delayNum;
    }

    public String getIsCountAchievement() {
        return isCountAchievement;
    }

    public void setIsCountAchievement(String isCountAchievement) {
        this.isCountAchievement = isCountAchievement;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
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

    public Long getContactId() {
        return contactId;
    }

    public void setContactId(Long contactId) {
        this.contactId = contactId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactTel() {
        return contactTel;
    }

    public void setContactTel(String contactTel) {
        this.contactTel = contactTel;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    public String getFeeTaker() {
        return feeTaker;
    }

    public void setFeeTaker(String feeTaker) {
        this.feeTaker = feeTaker;
    }

    public BigDecimal getShipFee() {
        return shipFee;
    }

    public void setShipFee(BigDecimal shipFee) {
        this.shipFee = shipFee;
    }

    public String getOutboundTaker() {
        return outboundTaker;
    }

    public void setOutboundTaker(String outboundTaker) {
        this.outboundTaker = outboundTaker;
    }

    public BigDecimal getOutboundFee() {
        return outboundFee;
    }

    public void setOutboundFee(BigDecimal outboundFee) {
        this.outboundFee = outboundFee;
    }

    public Integer getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Integer totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public BigDecimal getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(BigDecimal totalWeight) {
        this.totalWeight = totalWeight;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getSecondBalanceTakeout() {
        return secondBalanceTakeout;
    }

    public void setSecondBalanceTakeout(BigDecimal secondBalanceTakeout) {
        this.secondBalanceTakeout = secondBalanceTakeout;
    }

    public BigDecimal getTotalContractReletedAmount() {
        return totalContractReletedAmount;
    }

    public void setTotalContractReletedAmount(BigDecimal totalContractReletedAmount) {
        this.totalContractReletedAmount = totalContractReletedAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getChangeStatus() {
        return changeStatus;
    }

    public void setChangeStatus(String changeStatus) {
        this.changeStatus = changeStatus;
    }

    public String getOriginStatus() {
        return originStatus;
    }

    public void setOriginStatus(String originStatus) {
        this.originStatus = originStatus;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public Integer getPickupStatus() {
        return pickupStatus;
    }

    public void setPickupStatus(Integer pickupStatus) {
        this.pickupStatus = pickupStatus;
    }

    public Integer getFillupStatus() {
        return fillupStatus;
    }

    public void setFillupStatus(Integer fillupStatus) {
        this.fillupStatus = fillupStatus;
    }

    public String getCallBackStatus() {
        return callBackStatus;
    }

    public void setCallBackStatus(String callBackStatus) {
        this.callBackStatus = callBackStatus;
    }

    public String getCallBackNote() {
        return callBackNote;
    }

    public void setCallBackNote(String callBackNote) {
        this.callBackNote = callBackNote;
    }

    public String getConsignType() {
        return consignType;
    }

    public void setConsignType(String consignType) {
        this.consignType = consignType;
    }

    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getContractAddress() {
        return contractAddress;
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }

    public Long getOrderOrgId() {
        return orderOrgId;
    }

    public void setOrderOrgId(Long orderOrgId) {
        this.orderOrgId = orderOrgId;
    }

    public String getOrderOrgName() {
        return orderOrgName;
    }

    public void setOrderOrgName(String orderOrgName) {
        this.orderOrgName = orderOrgName;
    }

    public Long getBuyerOrgId() {
        return buyerOrgId;
    }

    public void setBuyerOrgId(Long buyerOrgId) {
        this.buyerOrgId = buyerOrgId;
    }

    public String getBuyerOrgName() {
        return buyerOrgName;
    }

    public void setBuyerOrgName(String buyerOrgName) {
        this.buyerOrgName = buyerOrgName;
    }


    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
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


    public Long getDepartmentCount() {
        return departmentCount;
    }

    public void setDepartmentCount(Long departmentCount) {
        this.departmentCount = departmentCount;
    }

	public String getBuyerCredentialCode() {
		return buyerCredentialCode;
	}

	public void setBuyerCredentialCode(String buyerCredentialCode) {
		this.buyerCredentialCode = buyerCredentialCode;
	}

	public String getBatchBuyerCredentialCode() {
		return batchBuyerCredentialCode;
	}

	public void setBatchBuyerCredentialCode(String batchBuyerCredentialCode) {
		this.batchBuyerCredentialCode = batchBuyerCredentialCode;
	}

}