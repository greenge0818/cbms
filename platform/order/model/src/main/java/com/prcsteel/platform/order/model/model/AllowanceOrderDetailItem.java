package com.prcsteel.platform.order.model.model;

import java.math.BigDecimal;
import java.util.Date;

public class AllowanceOrderDetailItem {
    private Long id;

    private Long allowanceOrderId;

    private Long orderDetailId;
    
    private Long buyerId;
    
    private String buyerName;

    private String nsortName;

    private String material;

    private String spec;

    private BigDecimal actualWeight = BigDecimal.ZERO;

    private BigDecimal actualAmount = BigDecimal.ZERO;

    private BigDecimal allowanceWeight = BigDecimal.ZERO;

    private BigDecimal allowanceAmount = BigDecimal.ZERO;

    private Byte isDeleted;

    private Date created;

    private String createdBy;

    private Date lastUpdated;

    private String lastUpdatedBy;

    private Integer modificationNumber;
    
    private Long buyerDepartmentId;
    
    private String buyerDepartmentName;

    public Long getBuyerDepartmentId() {
		return buyerDepartmentId;
	}

	public void setBuyerDepartmentId(Long buyerDepartmentId) {
		this.buyerDepartmentId = buyerDepartmentId;
	}

	public String getBuyerDepartmentName() {
		return buyerDepartmentName;
	}

	public void setBuyerDepartmentName(String buyerDepartmentName) {
		this.buyerDepartmentName = buyerDepartmentName;
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

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAllowanceOrderId() {
        return allowanceOrderId;
    }

    public void setAllowanceOrderId(Long allowanceOrderId) {
        this.allowanceOrderId = allowanceOrderId;
    }

    public Long getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(Long orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public String getNsortName() {
        return nsortName;
    }

    public void setNsortName(String nsortName) {
        this.nsortName = nsortName;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public BigDecimal getActualWeight() {
        return actualWeight;
    }

    public void setActualWeight(BigDecimal actualWeight) {
        this.actualWeight = actualWeight;
    }

    public BigDecimal getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(BigDecimal actualAmount) {
        this.actualAmount = actualAmount;
    }

    public BigDecimal getAllowanceWeight() {
        return allowanceWeight;
    }

    public void setAllowanceWeight(BigDecimal allowanceWeight) {
        this.allowanceWeight = allowanceWeight;
    }

    public BigDecimal getAllowanceAmount() {
        return allowanceAmount;
    }

    public void setAllowanceAmount(BigDecimal allowanceAmount) {
        this.allowanceAmount = allowanceAmount;
    }

    public Byte getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Byte isDeleted) {
        this.isDeleted = isDeleted;
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
}