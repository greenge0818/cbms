package com.prcsteel.platform.order.model.dto;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by kongbinheng on 2015/8/9.
 */
public class InvoiceOrderItemsDto {
    private Long orderId;
    private Long orderItemsId;
    private Long ownerId;
    private String ownerName;
    private Long buyerId;
    private String buyerName;
    private String nsortName;
    private String material;
    private String spec;
    private Integer count;  //计数
    private Date created;   //订单明细时间
    private BigDecimal amount;  //金额
    private BigDecimal invoiceAmount;  //已开票金额
    private BigDecimal billableAmount;  //可开票金额
    private BigDecimal weight;  //重量
    private BigDecimal dealPrice;   //成交价
    private BigDecimal applyAmount; //申请金额
    private BigDecimal surplusAmount; //剩余申请金额
    private BigDecimal actualAmount; //实开金额
    private Integer nsortSpecCount;  //买家成交的品规的品种数量

    public Integer getNsortSpecCount() {
		return nsortSpecCount;
	}

	public void setNsortSpecCount(Integer nsortSpecCount) {
		this.nsortSpecCount = nsortSpecCount;
	}

	public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getOrderItemsId() {
        return orderItemsId;
    }

    public void setOrderItemsId(Long orderItemsId) {
        this.orderItemsId = orderItemsId;
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

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
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

    public BigDecimal getBillableAmount() {
        return billableAmount;
    }

    public void setBillableAmount(BigDecimal billableAmount) {
        this.billableAmount = billableAmount;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public BigDecimal getDealPrice() {
        return dealPrice;
    }

    public void setDealPrice(BigDecimal dealPrice) {
        this.dealPrice = dealPrice;
    }

    public BigDecimal getApplyAmount() {
        return applyAmount;
    }

    public void setApplyAmount(BigDecimal applyAmount) {
        this.applyAmount = applyAmount;
    }

    public BigDecimal getSurplusAmount() {
        return surplusAmount;
    }

    public void setSurplusAmount(BigDecimal surplusAmount) {
        this.surplusAmount = surplusAmount;
    }

    public BigDecimal getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(BigDecimal actualAmount) {
        this.actualAmount = actualAmount;
    }
}
