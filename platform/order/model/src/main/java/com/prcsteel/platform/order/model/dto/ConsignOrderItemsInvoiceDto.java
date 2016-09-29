package com.prcsteel.platform.order.model.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by prcsteel on 2016/1/4.
 */
public class ConsignOrderItemsInvoiceDto {
    private String created;
    private String code;
    private String contractCode;
    private String accountName;
    private String nsortName;

    public String getNsortName() {
        return nsortName;
    }

    public void setNsortName(String nsortName) {
        this.nsortName = nsortName;
    }

    public BigDecimal getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    public List<OrderItemsInvoiceInInfoDto> getOrderItemsInvoiceInInfoList() {
        return orderItemsInvoiceInInfoList;
    }

    public void setOrderItemsInvoiceInInfoList(List<OrderItemsInvoiceInInfoDto> orderItemsInvoiceInInfoList) {
        this.orderItemsInvoiceInInfoList = orderItemsInvoiceInInfoList;
    }

    private String material;
    private String spec;
    private BigDecimal costPrice;
    private BigDecimal actualPickWeightServer;
    private BigDecimal actualPickAmount;
    private BigDecimal unarriveInvoiceWeight;
    private BigDecimal unarriveInvoiceAmount;
    private List<OrderItemsInvoiceInInfoDto> orderItemsInvoiceInInfoList;

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
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

    public BigDecimal getActualPickWeightServer() {
        return actualPickWeightServer;
    }

    public void setActualPickWeightServer(BigDecimal actualPickWeightServer) {
        this.actualPickWeightServer = actualPickWeightServer;
    }

    public BigDecimal getActualPickAmount() {
        return actualPickAmount;
    }

    public void setActualPickAmount(BigDecimal actualPickAmount) {
        this.actualPickAmount = actualPickAmount;
    }

    public BigDecimal getUnarriveInvoiceWeight() {
        return unarriveInvoiceWeight;
    }

    public void setUnarriveInvoiceWeight(BigDecimal unarriveInvoiceWeight) {
        this.unarriveInvoiceWeight = unarriveInvoiceWeight;
    }

    public BigDecimal getUnarriveInvoiceAmount() {
        return unarriveInvoiceAmount;
    }

    public void setUnarriveInvoiceAmount(BigDecimal unarriveInvoiceAmount) {
        this.unarriveInvoiceAmount = unarriveInvoiceAmount;
    }
}
