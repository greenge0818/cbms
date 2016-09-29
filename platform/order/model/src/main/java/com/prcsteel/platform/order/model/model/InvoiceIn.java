package com.prcsteel.platform.order.model.model;

import java.math.BigDecimal;
import java.util.Date;

public class InvoiceIn {
    private Long id;

    private String code;

    private String areaCode;

    private Long orgId;

    private String orgName;

    private Long sellerId;

    private String sellerName;

    private BigDecimal totalAmount;

    private BigDecimal totalWeight;

    private BigDecimal checkTotalAmount;

    private BigDecimal checkTotalWeight;

    private Date invoiceDate;

    private Date checkDate;

    private Long checkUserId;

    private String checkUserName;

    private String checkUserMobil;

    private Long inputUserId;

    private String inputUserName;

    private String inputUserMobil;

    private String status;

    private  Integer isDefer;

    private String relationStatus;

    private Long expressId;

    private String printStatus;

    private String memo;
    
    private Long departmentId;		//部门id
    private String departmentName;  //部门名称 
    
    private Date created;

    private String createdBy;

    private Date lastUpdated;

    private String lastUpdatedBy;

    private Integer modificationNumber;

    private String rowId;

    private String parentRowId;

    private String ext1;

    private String ext2;

    private String ext3;

    private Integer ext4;

    private Integer ext5;

    private Integer ext6;

    private Date ext7;

    private Long ext8;
    
    private int totalDepartment;//部门总数

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(BigDecimal totalWeight) {
        this.totalWeight = totalWeight;
    }

    public BigDecimal getCheckTotalAmount() {
        return checkTotalAmount;
    }

    public void setCheckTotalAmount(BigDecimal checkTotalAmount) {
        this.checkTotalAmount = checkTotalAmount;
    }

    public BigDecimal getCheckTotalWeight() {
        return checkTotalWeight;
    }

    public void setCheckTotalWeight(BigDecimal checkTotalWeight) {
        this.checkTotalWeight = checkTotalWeight;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public Date getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(Date checkDate) {
        this.checkDate = checkDate;
    }

    public Long getCheckUserId() {
        return checkUserId;
    }

    public void setCheckUserId(Long checkUserId) {
        this.checkUserId = checkUserId;
    }

    public String getCheckUserName() {
        return checkUserName;
    }

    public void setCheckUserName(String checkUserName) {
        this.checkUserName = checkUserName;
    }

    public String getCheckUserMobil() {
        return checkUserMobil;
    }

    public void setCheckUserMobil(String checkUserMobil) {
        this.checkUserMobil = checkUserMobil;
    }

    public Long getInputUserId() {
        return inputUserId;
    }

    public void setInputUserId(Long inputUserId) {
        this.inputUserId = inputUserId;
    }

    public String getInputUserName() {
        return inputUserName;
    }

    public void setInputUserName(String inputUserName) {
        this.inputUserName = inputUserName;
    }

    public String getInputUserMobil() {
        return inputUserMobil;
    }

    public void setInputUserMobil(String inputUserMobil) {
        this.inputUserMobil = inputUserMobil;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRelationStatus() {
        return relationStatus;
    }

    public void setRelationStatus(String relationStatus) {
        this.relationStatus = relationStatus;
    }

    public Long getExpressId() {
        return expressId;
    }

    public void setExpressId(Long expressId) {
        this.expressId = expressId;
    }

    public String getPrintStatus() {
        return printStatus;
    }

    public void setPrintStatus(String printStatus) {
        this.printStatus = printStatus;
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

    public Integer getIsDefer() {
        return isDefer;
    }

    public void setIsDefer(Integer isDefer) {
        this.isDefer = isDefer;
    }

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
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

	public int getTotalDepartment() {
		return totalDepartment;
	}

	public void setTotalDepartment(int totalDepartment) {
		this.totalDepartment = totalDepartment;
	}

	@Override
	public String toString() {
		return "InvoiceIn [id=" + id + ", code=" + code + ", areaCode=" + areaCode + ", orgId=" + orgId + ", orgName="
				+ orgName + ", sellerId=" + sellerId + ", sellerName=" + sellerName + ", totalAmount=" + totalAmount
				+ ", totalWeight=" + totalWeight + ", checkTotalAmount=" + checkTotalAmount + ", checkTotalWeight="
				+ checkTotalWeight + ", invoiceDate=" + invoiceDate + ", checkDate=" + checkDate + ", checkUserId="
				+ checkUserId + ", checkUserName=" + checkUserName + ", checkUserMobil=" + checkUserMobil
				+ ", inputUserId=" + inputUserId + ", inputUserName=" + inputUserName + ", inputUserMobil="
				+ inputUserMobil + ", status=" + status + ", isDefer=" + isDefer + ", relationStatus=" + relationStatus
				+ ", expressId=" + expressId + ", printStatus=" + printStatus + ", created=" + created + ", createdBy="
				+ createdBy + ", lastUpdated=" + lastUpdated + ", lastUpdatedBy=" + lastUpdatedBy
				+ ", modificationNumber=" + modificationNumber + ", rowId=" + rowId + ", parentRowId=" + parentRowId
				+ ", ext1=" + ext1 + ", ext2=" + ext2 + ", ext3=" + ext3 + ", ext4=" + ext4 + ", ext5=" + ext5
				+ ", ext6=" + ext6 + ", ext7=" + ext7 + ", ext8=" + ext8 + "]";
	}
    
}