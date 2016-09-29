package com.prcsteel.platform.order.model.dto;

import java.math.BigDecimal;
import java.util.Date;


/**
 * Created by dq on 15-9-23.
 */
public class InvoiceApplicationDetailDto {
	
	private Long invoiceOrderitemId;
	
    private Long orderId;
    
    private Long orderDetailId;
    
    private String orderCode;
    
    private Date created;

    private Long sellerId;

    private String sellerName;

    private String nsortName;

    private String material;

    private String spec;

    private String factory;

    private String city;

    private String warehouse;

    private Integer quantity;

    private BigDecimal costPrice;

    private BigDecimal dealPrice;
    
    private Long invoiceInId;
    
    private Long invoiceInDetailId;
	
    private BigDecimal weight;
    
    private BigDecimal amount;
    
    private BigDecimal contractAmount;
    
    private BigDecimal contractWeight;
    
    private BigDecimal actualAmount;
    
    private BigDecimal actualWeight;
    
    private BigDecimal unAmount;
    
    private BigDecimal unWeight;
    
    private BigDecimal allowanceWeight;
    
    private BigDecimal  allowanceBuyerAmount;
    
    private BigDecimal usedWeight;
    
    private BigDecimal  usedAmount;


	private Long departmentId;           //部门ID

	private String departmentName;		//部门名称


    
    private String sellerCredentialCode;		//卖家凭证
    private String sellerCredentialStatus;		//卖家凭证状态
    private String batchSellerCredentialCode;	//批量卖家凭证
    private String batchSellerCredentialStatus;	//批量卖家凭证状态
    private String buyerCredentialCode;			//买家凭证
    private String buyerCredentialStatus;			//买家凭证状态
    private String batchBuyerCredentialCode;	//批量买家凭证
    private String batchBuyerCredentialStatus;	//批量买家凭证状态
    private String buyerCheckValue;					//买家是不是需要审核： 0：不需要审核通过， 1： 必须审核通过 
    private String sellerCheckValue;				//卖家是不是需要审核： 0：不需要审核通过， 1： 必须审核通过 
    
    private int buyerCheck;
    private int sellerCheck;
    

	public BigDecimal getUsedWeight() {
		return usedWeight;
	}

	public void setUsedWeight(BigDecimal usedWeight) {
		this.usedWeight = usedWeight;
	}

	public BigDecimal getUsedAmount() {
		return usedAmount;
	}

	public void setUsedAmount(BigDecimal usedAmount) {
		this.usedAmount = usedAmount;
	}

	public BigDecimal getAllowanceWeight() {
		return allowanceWeight;
	}

	public void setAllowanceWeight(BigDecimal allowanceWeight) {
		this.allowanceWeight = allowanceWeight;
	}

	public BigDecimal getAllowanceBuyerAmount() {
		return allowanceBuyerAmount;
	}

	public void setAllowanceBuyerAmount(BigDecimal allowanceBuyerAmount) {
		this.allowanceBuyerAmount = allowanceBuyerAmount;
	}

	public void setInvoiceOrderitemId(Long invoiceOrderitemId) {
		this.invoiceOrderitemId = invoiceOrderitemId;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getOrderDetailId() {
		return orderDetailId;
	}

	public void setOrderDetailId(Long orderDetailId) {
		this.orderDetailId = orderDetailId;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
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

	public BigDecimal getWeight() {
		return weight;
	}

	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getContractAmount() {
		return contractAmount;
	}

	public void setContractAmount(BigDecimal contractAmount) {
		this.contractAmount = contractAmount;
	}

	public BigDecimal getContractWeight() {
		return contractWeight;
	}

	public void setContractWeight(BigDecimal contractWeight) {
		this.contractWeight = contractWeight;
	}

	public BigDecimal getActualAmount() {
		return actualAmount;
	}

	public void setActualAmount(BigDecimal actualAmount) {
		this.actualAmount = actualAmount;
	}

	public BigDecimal getActualWeight() {
		return actualWeight;
	}

	public void setActualWeight(BigDecimal actualWeight) {
		this.actualWeight = actualWeight;
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

	public Long getInvoiceOrderitemId() {
		return invoiceOrderitemId;
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

	public String getSellerCredentialCode() {
		return sellerCredentialCode;
	}

	public void setSellerCredentialCode(String sellerCredentialCode) {
		this.sellerCredentialCode = sellerCredentialCode;
	}

	public String getBatchSellerCredentialCode() {
		return batchSellerCredentialCode;
	}

	public void setBatchSellerCredentialCode(String batchSellerCredentialCode) {
		this.batchSellerCredentialCode = batchSellerCredentialCode;
	}

	public String getBuyerCredentialCode() {
		return buyerCredentialCode;
	}

	public void setBuyerCredentialCode(String buyerCredentialCode) {
		this.buyerCredentialCode = buyerCredentialCode;
	}

	public String getBatchBuyerCredentialCode() {
		return batchBuyerCredentialCode;
	}

	public void setBatchBuyerCredentialCode(String batchBuyerCredentialCode) {
		this.batchBuyerCredentialCode = batchBuyerCredentialCode;
	}

	public String getSellerCredentialStatus() {
		return sellerCredentialStatus;
	}

	public void setSellerCredentialStatus(String sellerCredentialStatus) {
		this.sellerCredentialStatus = sellerCredentialStatus;
	}

	public String getBatchSellerCredentialStatus() {
		return batchSellerCredentialStatus;
	}

	public void setBatchSellerCredentialStatus(String batchSellerCredentialStatus) {
		this.batchSellerCredentialStatus = batchSellerCredentialStatus;
	}

	public String getBuyerCredentialStatus() {
		return buyerCredentialStatus;
	}

	public void setBuyerCredentialStatus(String buyerCredentialStatus) {
		this.buyerCredentialStatus = buyerCredentialStatus;
	}

	public String getBatchBuyerCredentialStatus() {
		return batchBuyerCredentialStatus;
	}

	public void setBatchBuyerCredentialStatus(String batchBuyerCredentialStatus) {
		this.batchBuyerCredentialStatus = batchBuyerCredentialStatus;
	}

	public String getBuyerCheckValue() {
		return buyerCheckValue;
	}

	public void setBuyerCheckValue(String buyerCheckValue) {
		this.buyerCheckValue = buyerCheckValue;
	}

	public String getSellerCheckValue() {
		return sellerCheckValue;
	}

	public void setSellerCheckValue(String sellerCheckValue) {
		this.sellerCheckValue = sellerCheckValue;
	}

	public int getBuyerCheck() {
		return buyerCheck;
	}

	public void setBuyerCheck(int buyerCheck) {
		this.buyerCheck = buyerCheck;
	}

	public int getSellerCheck() {
		return sellerCheck;
	}

	public void setSellerCheck(int sellerCheck) {
		this.sellerCheck = sellerCheck;
	}


}
