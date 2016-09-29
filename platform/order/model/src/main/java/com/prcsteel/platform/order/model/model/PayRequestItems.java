package com.prcsteel.platform.order.model.model;

import java.math.BigDecimal;
import java.util.Date;

public class PayRequestItems {
    private Long id;

    private Long requestId;

    private Long receiverId;

    private String receiverName;

    //add by rabbit 增加部门字段
    private Long receiverDepartmentId;

    private String receiverDepartmentName;
    
    private String receiverBankCode;

	private String receiverBankName;

    private String receiverBankCity;

    private String receiverBranchBankName;

    private String receiverAccountCode;

    private String receiverTel;

    private String receiverRegAddr;

    private BigDecimal payAmount;

    private BigDecimal secondBalanceTakeout;

    private Date created;

    private String createdBy;

    private Date lastUpdated;

    private String lastUpdatedBy;

    private Integer modificationNumber;

    private String rowId;

    private String parentRowId;
    
    private String paymentBank;

    private String ext1;

    private String ext2;

    private String ext3;

    private Integer ext4;

    private Integer ext5;

    private Integer ext6;

    private Date ext7;

    private Long ext8;
    
    private String payCode;

    private BigDecimal creditUsedRepay;

    private Date bankAccountTime; //银行出账时间

    public String getPayCode() {
		return payCode;
	}

	public void setPayCode(String payCode) {
		this.payCode = payCode;
	}

	public String getPaymentBank() {
		return paymentBank;
	}

	public void setPaymentBank(String paymentBank) {
		this.paymentBank = paymentBank;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public Long getReceiverDepartmentId() {
        return receiverDepartmentId;
    }

    public void setReceiverDepartmentId(Long receiverDepartmentId) {
        this.receiverDepartmentId = receiverDepartmentId;
    }

    public String getReceiverDepartmentName() {
        return receiverDepartmentName;
    }

    public void setReceiverDepartmentName(String receiverDepartmentName) {
        this.receiverDepartmentName = receiverDepartmentName;
    }

    public String getReceiverBankCode() {
		return receiverBankCode;
	}

	public void setReceiverBankCode(String receiverBankCode) {
		this.receiverBankCode = receiverBankCode;
	}
	
    public String getReceiverBankName() {
        return receiverBankName;
    }

    public void setReceiverBankName(String receiverBankName) {
        this.receiverBankName = receiverBankName;
    }

    public String getReceiverBankCity() {
        return receiverBankCity;
    }

    public void setReceiverBankCity(String receiverBankCity) {
        this.receiverBankCity = receiverBankCity;
    }

    public String getReceiverBranchBankName() {
        return receiverBranchBankName;
    }

    public void setReceiverBranchBankName(String receiverBranchBankName) {
        this.receiverBranchBankName = receiverBranchBankName;
    }

    public String getReceiverAccountCode() {
        return receiverAccountCode;
    }

    public void setReceiverAccountCode(String receiverAccountCode) {
        this.receiverAccountCode = receiverAccountCode;
    }

    public String getReceiverTel() {
        return receiverTel;
    }

    public void setReceiverTel(String receiverTel) {
        this.receiverTel = receiverTel;
    }

    public String getReceiverRegAddr() {
        return receiverRegAddr;
    }

    public void setReceiverRegAddr(String receiverRegAddr) {
        this.receiverRegAddr = receiverRegAddr;
    }

    public BigDecimal getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }

    public BigDecimal getSecondBalanceTakeout() {
        return secondBalanceTakeout;
    }

    public void setSecondBalanceTakeout(BigDecimal secondBalanceTakeout) {
        this.secondBalanceTakeout = secondBalanceTakeout;
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

    public String getExt1() {
        return ext1;
    }

    public void setExt1(String ext1) {
        this.ext1 = ext1;
    }

    public String getExt2() {
        return ext2;
    }

    public void setExt2(String ext2) {
        this.ext2 = ext2;
    }

    public String getExt3() {
        return ext3;
    }

    public void setExt3(String ext3) {
        this.ext3 = ext3;
    }

    public Integer getExt4() {
        return ext4;
    }

    public void setExt4(Integer ext4) {
        this.ext4 = ext4;
    }

    public Integer getExt5() {
        return ext5;
    }

    public void setExt5(Integer ext5) {
        this.ext5 = ext5;
    }

    public Integer getExt6() {
        return ext6;
    }

    public void setExt6(Integer ext6) {
        this.ext6 = ext6;
    }

    public Date getExt7() {
        return ext7;
    }

    public void setExt7(Date ext7) {
        this.ext7 = ext7;
    }

    public Long getExt8() {
        return ext8;
    }

    public void setExt8(Long ext8) {
        this.ext8 = ext8;
    }

    public BigDecimal getCreditUsedRepay() {
        return creditUsedRepay;
    }

    public void setCreditUsedRepay(BigDecimal creditUsedRepay) {
        this.creditUsedRepay = creditUsedRepay;
    }

    public Date getBankAccountTime() {
        return bankAccountTime;
    }

    public void setBankAccountTime(Date bankAccountTime) {
        this.bankAccountTime = bankAccountTime;
    }
}