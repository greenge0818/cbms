package com.prcsteel.platform.order.model.dto;

import java.math.BigDecimal;

/**
 * Created by dq on 15-9-14.
 */
public class InvoiceOutApplyDetailItemsListVoDto {

	// 订单详情ID
	private Long orderDetailId;
	// 进项票ID
	private Long invoiceInId;
	// 进项票详情ID
	private Long invoiceInDetailId;
	// 进项票详情与订单详情关联表ID
	private Long invoiceOrderitemId;
	// 品名
	private String nsortName;
	// 材质
	private String material;
	// 规格
	private String spec;
	// 实提重量
	private BigDecimal actualWeight = BigDecimal.ZERO;
	// 实提金额
	private BigDecimal actualAmount = BigDecimal.ZERO;
	// 开票重量
	private BigDecimal weight = BigDecimal.ZERO;
	// 成交价
	private BigDecimal dealPrice = BigDecimal.ZERO;
	// 合同金额
	private BigDecimal contractAmount = BigDecimal.ZERO;
	// 不含税金额
	private BigDecimal noTaxAmount = BigDecimal.ZERO;
	// 税额
	private BigDecimal taxAmount = BigDecimal.ZERO;
	// 含税金额
	private BigDecimal amount = BigDecimal.ZERO;
	// 未开票金额
	private BigDecimal unAmount = BigDecimal.ZERO;
	// 未开票重量
	private BigDecimal unWeight = BigDecimal.ZERO;
	
	public Long getOrderDetailId() {
		return orderDetailId;
	}
	public void setOrderDetailId(Long orderDetailId) {
		this.orderDetailId = orderDetailId;
	}
	public Long getInvoiceInId() {
		return invoiceInId;
	}
	public void setInvoiceInId(Long invoiceInId) {
		this.invoiceInId = invoiceInId;
	}
	public Long getInvoiceInDetailId() {
		return invoiceInDetailId;
	}
	public void setInvoiceInDetailId(Long invoiceInDetailId) {
		this.invoiceInDetailId = invoiceInDetailId;
	}
	public Long getInvoiceOrderitemId() {
		return invoiceOrderitemId;
	}
	public void setInvoiceOrderitemId(Long invoiceOrderitemId) {
		this.invoiceOrderitemId = invoiceOrderitemId;
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
	public BigDecimal getContractAmount() {
		return contractAmount;
	}
	public void setContractAmount(BigDecimal contractAmount) {
		this.contractAmount = contractAmount;
	}
	public BigDecimal getNoTaxAmount() {
		return noTaxAmount;
	}
	public void setNoTaxAmount(BigDecimal noTaxAmount) {
		this.noTaxAmount = noTaxAmount;
	}
	public BigDecimal getTaxAmount() {
		return taxAmount;
	}
	public void setTaxAmount(BigDecimal taxAmount) {
		this.taxAmount = taxAmount;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public BigDecimal getUnAmount() {
		return unAmount;
	}
	public void setUnAmount(BigDecimal unAmount) {
		this.unAmount = unAmount;
	}
	public BigDecimal getUnWeight() {
		return unWeight;
	}
	public void setUnWeight(BigDecimal unWeight) {
		this.unWeight = unWeight;
	}


}
