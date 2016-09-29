package com.prcsteel.platform.order.model.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by rolyer on 15-9-15.
 */
public class AuditDetailDto {
    private Integer ownerId;
    private String ownerName;
    private Long invoiceOutApplyId;
    private BigDecimal totalApplyAmount;
    private BigDecimal totalUnApplyAmount;
    private List<InvoiceOutApplyDetailDto> details;

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public Long getInvoiceOutApplyId() {
        return invoiceOutApplyId;
    }

    public void setInvoiceOutApplyId(Long invoiceOutApplyId) {
        this.invoiceOutApplyId = invoiceOutApplyId;
    }

    public BigDecimal getTotalApplyAmount() {
        return totalApplyAmount;
    }

    public void setTotalApplyAmount(BigDecimal totalApplyAmount) {
        this.totalApplyAmount = totalApplyAmount;
    }

    public BigDecimal getTotalUnApplyAmount() {
        return totalUnApplyAmount;
    }

    public void setTotalUnApplyAmount(BigDecimal totalUnApplyAmount) {
        this.totalUnApplyAmount = totalUnApplyAmount;
    }

    public List<InvoiceOutApplyDetailDto> getDetails() {
        return details;
    }

    public void setDetails(List<InvoiceOutApplyDetailDto> details) {
        this.details = details;
    }
}