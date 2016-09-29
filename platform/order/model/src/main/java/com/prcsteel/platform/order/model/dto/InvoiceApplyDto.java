package com.prcsteel.platform.order.model.dto;

import java.math.BigDecimal;

/**
 * Created by kongbinheng on 2015/8/9.
 */
public class InvoiceApplyDto {

    private Long applyId;
    private Long ownerId;
    private String ownerName;
    private Long buyerId;
    private String buyerName;
    private Long monthDiff;
    private BigDecimal leftUninvoiceAmount;
    private BigDecimal applyAmount;
    private BigDecimal actualAmount;

    public Long getApplyId() {
        return applyId;
    }

    public void setApplyId(Long applyId) {
        this.applyId = applyId;
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

    public Long getMonthDiff() {
        return monthDiff;
    }

    public void setMonthDiff(Long monthDiff) {
        this.monthDiff = monthDiff;
    }

    public BigDecimal getLeftUninvoiceAmount() {
        return leftUninvoiceAmount;
    }

    public void setLeftUninvoiceAmount(BigDecimal leftUninvoiceAmount) {
        this.leftUninvoiceAmount = leftUninvoiceAmount;
    }

    public BigDecimal getApplyAmount() {
        return applyAmount;
    }

    public void setApplyAmount(BigDecimal applyAmount) {
        this.applyAmount = applyAmount;
    }

    public BigDecimal getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(BigDecimal actualAmount) {
        this.actualAmount = actualAmount;
    }

    public InvoiceApplyDto(Long applyId, Long ownerId, String ownerName, Long buyerId, String buyerName, Long monthDiff,
                           BigDecimal leftUninvoiceAmount, BigDecimal applyAmount, BigDecimal actualAmount){
        this.applyId = applyId;
        this.ownerId = ownerId;
        this.ownerName = ownerName;
        this.buyerId = buyerId;
        this.buyerName = buyerName;
        this.monthDiff = monthDiff;
        this.leftUninvoiceAmount = leftUninvoiceAmount;
        this.applyAmount = applyAmount;
        this.actualAmount = actualAmount;
       }

}
