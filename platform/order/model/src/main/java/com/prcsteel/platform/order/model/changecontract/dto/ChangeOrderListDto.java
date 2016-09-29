package com.prcsteel.platform.order.model.changecontract.dto;

import java.math.BigDecimal;

/**
 * Created by prcsteel on 2016/8/15.
 */
public class ChangeOrderListDto {
    private Long id;
    private Long orderId;
    private String code;

    private String createTime;

    private String accountName;

    private String ownerName;

    private String sellerName;

    private Integer totalQuantity;

    private String alterStatus;

    private String alterStatusName;

    private BigDecimal totalWeight;

    private BigDecimal totalAmount;

    private String payStatus;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public Long getId() {
        return id;
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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public Integer getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Integer totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public String getAlterStatus() {
        return alterStatus;
    }

    public void setAlterStatus(String alterStatus) {
        this.alterStatus = alterStatus;
    }

    public String getAlterStatusName() {
        return alterStatusName;
    }

    public void setAlterStatusName(String alterStatusName) {
        this.alterStatusName = alterStatusName;
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
}
