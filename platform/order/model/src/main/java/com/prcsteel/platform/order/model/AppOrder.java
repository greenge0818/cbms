package com.prcsteel.platform.order.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class AppOrder {
    private Long id;//订单ID

    private String code;//订单编号

    private String accountName;//卖家名称

    private String status;//订单状态

    private String statusName;//订单状态名称

    private String time;//订单创建日期

    private String contactName;//联系人

    private String contactTel;//联系人电话

    private String payType;//支付类型

    private BigDecimal totalAmount;//已付金额

    private BigDecimal totalWeight;//总重

    private BigDecimal actualPickWeight;//实提重量

    private String closeReason;//关闭理由

    private String transArea;//交货地
    
    private String userTel;//交易员联系电话

	public String getUserTel() {
		return userTel;
	}

	public void setUserTel(String userTel) {
		this.userTel = userTel;
	}

	public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    private List<AppOrderItems> items;

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getActualPickWeight() {
        return actualPickWeight;
    }

    public void setActualPickWeight(BigDecimal actualPickWeight) {
        this.actualPickWeight = actualPickWeight;
    }

    public String getTransArea() {
        return transArea;
    }

    public void setTransArea(String transArea) {
        this.transArea = transArea;
    }

    public List<AppOrderItems> getItems() {
        return items;
    }

    public void setItems(List<AppOrderItems> items) {
        this.items = items;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getContactTel() {
        return contactTel;
    }

    public void setContactTel(String contactTel) {
        this.contactTel = contactTel;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public BigDecimal getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(BigDecimal totalWeight) {
        this.totalWeight = totalWeight;
    }

    public String getCloseReason() {
        return closeReason;
    }

    public void setCloseReason(String closeReason) {
        this.closeReason = closeReason;
    }
}