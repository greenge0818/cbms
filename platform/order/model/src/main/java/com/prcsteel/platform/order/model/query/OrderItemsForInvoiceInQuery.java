package com.prcsteel.platform.order.model.query;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.prcsteel.platform.common.query.PagedQuery;
import com.prcsteel.platform.order.model.dto.InputInvoiceInAssigned;

/**
 * 根据发票信息匹配订单资源信息
 * @author zhoukun
 */
public class OrderItemsForInvoiceInQuery extends PagedQuery {

	Long invoiceDetailId;// 如果不为空，则直接从从inv_invoce_in_detail_orderitem表中读数
	
	Long sellerAccountId;
	
	Date orderBeginDate;
	
	Date orderEndDate;
	
	String nsortName;
	
	Long departmentId;  //部门id
	
	String materials;
	
	String spec;
	
	String contractCode;//合同号
	
	Double weight;//发票重量
	
	Double priceAndTaxAmount;// 价税合计

	BigDecimal purchasePrice;//采购单价

	Double actualWeight;//订单实提重量
	
	List<InputInvoiceInAssigned> assigned;
	List<InputInvoiceInAssigned> cancelAssigned;//在客户端取消的关联，还未入库的
	
	public String getContractCode() {
		return contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}

	public Long getInvoiceDetailId() {
		return invoiceDetailId;
	}

	public void setInvoiceDetailId(Long invoiceDetailId) {
		this.invoiceDetailId = invoiceDetailId;
	}

	public Long getSellerAccountId() {
		return sellerAccountId;
	}

	public void setSellerAccountId(Long sellerAccountId) {
		this.sellerAccountId = sellerAccountId;
	}

	public Date getOrderBeginDate() {
		return orderBeginDate;
	}

	public void setOrderBeginDate(Date orderBeginDate) {
		this.orderBeginDate = orderBeginDate;
	}

	public Date getOrderEndDate() {
		return orderEndDate;
	}

	public void setOrderEndDate(Date orderEndDate) {
		this.orderEndDate = orderEndDate;
	}

	public String getNsortName() {
		return nsortName;
	}

	public void setNsortName(String nsortName) {
		this.nsortName = nsortName;
	}

	public String getMaterials() {
		return materials;
	}

	public void setMaterials(String materials) {
		this.materials = materials;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public Double getPriceAndTaxAmount() {
		return priceAndTaxAmount;
	}

	public void setPriceAndTaxAmount(Double priceAndTaxAmount) {
		this.priceAndTaxAmount = priceAndTaxAmount;
	}

	public List<InputInvoiceInAssigned> getAssigned() {
		return assigned;
	}

	public void setAssigned(List<InputInvoiceInAssigned> assigned) {
		this.assigned = assigned;
	}

	public List<InputInvoiceInAssigned> getCancelAssigned() {
		return cancelAssigned;
	}

	public void setCancelAssigned(List<InputInvoiceInAssigned> cancelAssigned) {
		this.cancelAssigned = cancelAssigned;
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public BigDecimal getPurchasePrice() {
		return purchasePrice;
	}

	public void setPurchasePrice(BigDecimal purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	public Double getActualWeight() {
		return actualWeight;
	}

	public void setActualWeight(Double actualWeight) {
		this.actualWeight = actualWeight;
	}

}
