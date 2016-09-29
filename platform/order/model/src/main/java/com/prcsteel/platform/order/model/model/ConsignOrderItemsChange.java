package com.prcsteel.platform.order.model.model;

import com.prcsteel.platform.order.model.enums.ConsignOrderAlterStatus;

import java.math.BigDecimal;
import java.util.Date;

public class ConsignOrderItemsChange {

    public ConsignOrderItemsChange(){}

    public ConsignOrderItemsChange(ConsignOrderItems item) {
        this.orderItemId = item.getId();
        this.orderId = item.getOrderId();
        this.sellerId = item.getSellerId();
        this.sellerName = item.getSellerName();
        this.departmentId = item.getDepartmentId();
        this.departmentName = item.getDepartmentName();
        this.contactId = item.getContactId();
        this.contactName = item.getContactName();
        this.statusName =  ConsignOrderAlterStatus.getNameByCode(this.status);
    }

    private Integer id;

    private Long orderItemId;

    private Long orderId;

    private Integer changeOrderId;

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

    private BigDecimal purchaseAmount;

    private BigDecimal saleAmount;

    private Boolean isPayedByAcceptDraft;

    private Long acceptDraftId;

    private String acceptDraftCode;

    private String strappingNum;

    private String status;

    private String statusName;

    private Date created;

    private String createdBy;

    private Date lastUpdated;

    private String lastUpdatedBy;

    private Integer modificationNumber;

    public Integer getChangeOrderId() {
        return changeOrderId;
    }

    public void setChangeOrderId(Integer changeOrderId) {
        this.changeOrderId = changeOrderId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Long orderItemId) {
        this.orderItemId = orderItemId;
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

    public BigDecimal getPurchaseAmount() {
        return purchaseAmount;
    }

    public void setPurchaseAmount(BigDecimal purchaseAmount) {
        this.purchaseAmount = purchaseAmount;
    }

    public BigDecimal getSaleAmount() {
        return saleAmount;
    }

    public void setSaleAmount(BigDecimal saleAmount) {
        this.saleAmount = saleAmount;
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

    public String getStrappingNum() {
        return strappingNum;
    }

    public void setStrappingNum(String strappingNum) {
        this.strappingNum = strappingNum;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusName() {
        return ConsignOrderAlterStatus.getNameByCode(this.status);
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