package com.prcsteel.platform.order.model.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by rolyer on 15-9-18.
 */
public class InvoiceOutOrderDetailDto {
    private String contractCode;                //代运营交易单号
    private Date created;                       //开单时间
    private BigDecimal totalActualPickWeight;   //实提总重量
    private BigDecimal totalAmount;             //合同总金额
    private BigDecimal totalActualPickAmount;   //实提总金额
    private String nsortName;                   //品名
    private String material;                    //材质
    private String spec;                        //规格
    private BigDecimal actualPickWeight;        //实提重量
    private BigDecimal invoiceWeight;           //开票重量
    private BigDecimal dealPrice;               //成交价
    private BigDecimal amount;                  //合同金额
    private BigDecimal actualPickAmount;        //实提金额
    private BigDecimal invoiceAmount;           //开票金额
    private Long orderId;                 //订单编号

    private List<InvoiceOutOrderDetailDto> items;

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public BigDecimal getTotalActualPickWeight() {
        return totalActualPickWeight;
    }

    public void setTotalActualPickWeight(BigDecimal totalActualPickWeight) {
        this.totalActualPickWeight = totalActualPickWeight;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTotalActualPickAmount() {
        return totalActualPickAmount;
    }

    public void setTotalActualPickAmount(BigDecimal totalActualPickAmount) {
        this.totalActualPickAmount = totalActualPickAmount;
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

    public BigDecimal getActualPickWeight() {
        return actualPickWeight;
    }

    public void setActualPickWeight(BigDecimal actualPickWeight) {
        this.actualPickWeight = actualPickWeight;
    }

    public BigDecimal getInvoiceWeight() {
        return invoiceWeight;
    }

    public void setInvoiceWeight(BigDecimal invoiceWeight) {
        this.invoiceWeight = invoiceWeight;
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

    public BigDecimal getActualPickAmount() {
        return actualPickAmount;
    }

    public void setActualPickAmount(BigDecimal actualPickAmount) {
        this.actualPickAmount = actualPickAmount;
    }

    public BigDecimal getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(BigDecimal invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public List<InvoiceOutOrderDetailDto> getItems() {
        return items;
    }

    public void setItems(List<InvoiceOutOrderDetailDto> items) {
        this.items = items;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
}
