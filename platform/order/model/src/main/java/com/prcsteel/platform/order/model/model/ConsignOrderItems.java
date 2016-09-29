package com.prcsteel.platform.order.model.model;

import java.math.BigDecimal;
import java.util.Date;

public class ConsignOrderItems {
    private Long id;

    private Long orderId;

    private Long sellerId;

    private String sellerName;

    private Long departmentId;

    private String departmentName;

    private Long contactId;

    private String contactName;

    private String nsortName;

    private String material;

    private String spec;

    private String factory;

    private String city;

    private String warehouse;

    private Integer quantity;

    private BigDecimal weight;

    private String weightConcept;

    private BigDecimal costPrice;

    private BigDecimal dealPrice;

    private BigDecimal amount;

    private BigDecimal invoiceAmount;
    
    private BigDecimal invoiceWeight;

    private BigDecimal usedAmount;

    private BigDecimal usedWeight;
    
    private BigDecimal allowanceAmount;

    private BigDecimal allowanceWeight;
    
    private BigDecimal allowanceBuyerAmount;

    private BigDecimal actualPickWeightSalesman;

    private BigDecimal actualPickWeightServer;

    private Integer actualPickQuantitySalesman;

    private Integer actualPickQuantityServer;

    private Integer status;

    private Boolean isPayedByAcceptDraft;

    private Long acceptDraftId;

    private String acceptDraftCode;

    private Long sellerOrgId;

    private String sellerOrgName;

    private Long sellerOwnerId;

    private String sellerOwnerName;

    private Date created;

    private String createdBy;

    private Date lastUpdated;

    private String lastUpdatedBy;

    private Integer modificationNumber;

    private String rowId;

    private String parentRowId;

    private String sellerJobNumber;

    private String strappingNum; //捆包号


    public ConsignOrderItems() {
    }

    public ConsignOrderItems(ConsignOrderItems items) {
        this.id = items.id;
        this.orderId = items.orderId;
        this.sellerId = items.sellerId;
        this.sellerName = items.sellerName;
        this.departmentId = items.departmentId;
        this.departmentName = items.departmentName;
        this.contactId = items.contactId;
        this.contactName = items.contactName;
        this.nsortName = items.nsortName;
        this.material = items.material;
        this.spec = items.spec;
        this.factory = items.factory;
        this.city = items.city;
        this.warehouse = items.warehouse;
        this.quantity = items.quantity;
        this.weight = items.weight;
        this.weightConcept = items.weightConcept;
        this.costPrice = items.costPrice;
        this.dealPrice = items.dealPrice;
        this.amount = items.amount;
        this.invoiceAmount = items.invoiceAmount;
        this.invoiceWeight = items.invoiceWeight;
        this.usedAmount = items.usedAmount;
        this.usedWeight = items.usedWeight;
        this.allowanceAmount = items.allowanceAmount;
        this.allowanceWeight = items.allowanceWeight;
        this.allowanceBuyerAmount = items.allowanceBuyerAmount;
        this.actualPickWeightSalesman = items.actualPickWeightSalesman;
        this.actualPickWeightServer = items.actualPickWeightServer;
        this.actualPickQuantitySalesman = items.actualPickQuantitySalesman;
        this.actualPickQuantityServer = items.actualPickQuantityServer;
        this.status = items.status;
        this.isPayedByAcceptDraft = items.isPayedByAcceptDraft;
        this.acceptDraftId = items.acceptDraftId;
        this.acceptDraftCode = items.acceptDraftCode;
        this.sellerOrgId = items.sellerOrgId;
        this.sellerOrgName = items.sellerOrgName;
        this.sellerOwnerId = items.sellerOwnerId;
        this.sellerOwnerName = items.sellerOwnerName;
        this.created = items.created;
        this.createdBy = items.createdBy;
        this.lastUpdated = items.lastUpdated;
        this.lastUpdatedBy = items.lastUpdatedBy;
        this.modificationNumber = items.modificationNumber;
        this.rowId = items.rowId;
        this.parentRowId = items.parentRowId;
        this.sellerJobNumber = items.sellerJobNumber;
    }

    public ConsignOrderItems(ConsignOrderItemsChange itemsChange, boolean allAttributes) {
        if(allAttributes){
            this.id = itemsChange.getOrderItemId();
            this.orderId = itemsChange.getOrderId();
            this.sellerId = itemsChange.getSellerId();
            this.sellerName = itemsChange.getSellerName();
            this.departmentId = itemsChange.getDepartmentId();
            this.departmentName = itemsChange.getDepartmentName();
            this.contactId = itemsChange.getContactId();
            this.contactName = itemsChange.getContactName();
        }
        this.nsortName = itemsChange.getNsortName();
        this.material = itemsChange.getMaterial();
        this.spec = itemsChange.getSpec();
        this.factory = itemsChange.getFactory();
        this.city = itemsChange.getCity();
        this.warehouse = itemsChange.getWarehouse();
        this.quantity = itemsChange.getQuantity();
        this.weight = itemsChange.getWeight();
        this.weightConcept = itemsChange.getWeightConcept();
        this.costPrice = itemsChange.getCostPrice();
        this.dealPrice = itemsChange.getDealPrice();
        this.amount = itemsChange.getSaleAmount();
        this.isPayedByAcceptDraft = itemsChange.getIsPayedByAcceptDraft();
        this.acceptDraftId = itemsChange.getAcceptDraftId();
        this.acceptDraftCode = itemsChange.getAcceptDraftCode();
        this.strappingNum = itemsChange.getStrappingNum();
        this.lastUpdated = itemsChange.getLastUpdated();
        this.lastUpdatedBy = itemsChange.getLastUpdatedBy();
    }

    private String sellerCredentialCode;
    private String batchSellerCredentialCode;
    

    public String getSellerJobNumber() {
        return sellerJobNumber;
    }

    public void setSellerJobNumber(String sellerJobNumber) {
        this.sellerJobNumber = sellerJobNumber;
    }

    public BigDecimal getAllowanceBuyerAmount() {
		return allowanceBuyerAmount;
	}

	public void setAllowanceBuyerAmount(BigDecimal allowanceBuyerAmount) {
		this.allowanceBuyerAmount = allowanceBuyerAmount;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
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

    public String getFactory() {
        return factory;
    }

    public void setFactory(String factory) {
        this.factory = factory;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public String getWeightConcept() {
        return weightConcept;
    }

    public void setWeightConcept(String weightConcept) {
        this.weightConcept = weightConcept;
    }

    public BigDecimal getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    public BigDecimal getDealPrice() {
        return dealPrice;
    }

    public void setDealPrice(BigDecimal dealPrice) {
        this.dealPrice = dealPrice;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(BigDecimal invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public BigDecimal getActualPickWeightSalesman() {
        return actualPickWeightSalesman;
    }

    public void setActualPickWeightSalesman(BigDecimal actualPickWeightSalesman) {
        this.actualPickWeightSalesman = actualPickWeightSalesman;
    }

    public BigDecimal getActualPickWeightServer() {
        return actualPickWeightServer;
    }

    public void setActualPickWeightServer(BigDecimal actualPickWeightServer) {
        this.actualPickWeightServer = actualPickWeightServer;
    }

    public Integer getActualPickQuantitySalesman() {
        return actualPickQuantitySalesman;
    }

    public void setActualPickQuantitySalesman(Integer actualPickQuantitySalesman) {
        this.actualPickQuantitySalesman = actualPickQuantitySalesman;
    }

    public Integer getActualPickQuantityServer() {
        return actualPickQuantityServer;
    }

    public void setActualPickQuantityServer(Integer actualPickQuantityServer) {
        this.actualPickQuantityServer = actualPickQuantityServer;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }


    public Boolean getIsPayedByAcceptDraft() {
        return isPayedByAcceptDraft;
    }

    public void setIsPayedByAcceptDraft(Boolean isPayedByAcceptDraft) {
        this.isPayedByAcceptDraft = isPayedByAcceptDraft;
    }

    public Long getAcceptDraftId() {
        return acceptDraftId;
    }

    public void setAcceptDraftId(Long acceptDraftId) {
        this.acceptDraftId = acceptDraftId;
    }

    public String getAcceptDraftCode() {
        return acceptDraftCode;
    }

    public void setAcceptDraftCode(String acceptDraftCode) {
        this.acceptDraftCode = acceptDraftCode;
    }

    public Long getSellerOrgId() {
        return sellerOrgId;
    }

    public void setSellerOrgId(Long sellerOrgId) {
        this.sellerOrgId = sellerOrgId;
    }

    public String getSellerOrgName() {
        return sellerOrgName;
    }

    public void setSellerOrgName(String sellerOrgName) {
        this.sellerOrgName = sellerOrgName;
    }

    public Long getSellerOwnerId() {
        return sellerOwnerId;
    }

    public void setSellerOwnerId(Long sellerOwnerId) {
        this.sellerOwnerId = sellerOwnerId;
    }

    public String getSellerOwnerName() {
        return sellerOwnerName;
    }

    public void setSellerOwnerName(String sellerOwnerName) {
        this.sellerOwnerName = sellerOwnerName;
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

    public BigDecimal getInvoiceWeight() {
		return invoiceWeight;
	}

	public void setInvoiceWeight(BigDecimal invoiceWeight) {
		this.invoiceWeight = invoiceWeight;
	}

    public BigDecimal getUsedAmount() {
        return usedAmount;
    }

    public void setUsedAmount(BigDecimal usedAmount) {
        this.usedAmount = usedAmount;
    }

    public BigDecimal getUsedWeight() {
        return usedWeight;
    }

    public void setUsedWeight(BigDecimal usedWeight) {
        this.usedWeight = usedWeight;
    }
    
    public BigDecimal getAllowanceAmount() {
		return allowanceAmount;
	}

	public void setAllowanceAmount(BigDecimal allowanceAmount) {
		this.allowanceAmount = allowanceAmount;
	}

	public BigDecimal getAllowanceWeight() {
		return allowanceWeight;
	}

	public void setAllowanceWeight(BigDecimal allowanceWeight) {
		this.allowanceWeight = allowanceWeight;
	}

	public BigDecimal actualSellerAmount(){
        return this.actualPickWeightServer.multiply(this.costPrice).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal actualBuyerAmount(){
        return this.actualPickWeightServer.multiply(this.dealPrice).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getCostAmount(){
        return this.weight.multiply(this.costPrice).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

	public String getSellerCredentialCode() {
		return sellerCredentialCode;
	}

	public void setSellerCredentialCode(String sellerCredentialCode) {
		this.sellerCredentialCode = sellerCredentialCode;
	}

	public String getBatchSellerCredentialCode() {
		return batchSellerCredentialCode;
	}

	public void setBatchSellerCredentialCode(String batchSellerCredentialCode) {
		this.batchSellerCredentialCode = batchSellerCredentialCode;
	}

    public String getStrappingNum() {
        return strappingNum;
    }

    public void setStrappingNum(String strappingNum) {
        this.strappingNum = strappingNum;
    }
}