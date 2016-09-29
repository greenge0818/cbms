package com.prcsteel.platform.order.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.prcsteel.platform.common.constants.Constant;

/**
 * 订单详情与发票对应项
 * @author zhoukun
 */
public class OrderItemsInvoiceInDto implements Serializable {

	private static final long serialVersionUID = 8201409878276576948L;

	private Long orderId;
	
	private Long orderItemId;
	
	private Date orderDateTime;
	
	private String orderNumber;
	
	private String contractCode;
	
	private String buyerName;
	private String departmentName;
	private Long departmentId;
	private String alterStatus; // 订单合同变更状态
	
	private String nsortName;
	
	private String spec;
	
	private String materials;
	
	private Double invoiceAmount;//已开金额
	
	private Double invoiceWeight;//已开重量
	
	private Double costPrice; // 底价，进项价
	
	private Double actualPickWeightServer;// 实提量
	
	private boolean mathced = false;// 是否匹配
	
	private Double increaseWeight;//到票重量
	
	private Double increaseAmount;//到票金额

	private BigDecimal allowanceAmount; // 折让金额

	private BigDecimal allowanceWeight; // 折让重量

	private Boolean isShowDept;		// 是否显示买家部门

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getBuyerName() {
		return buyerName;
	}

	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}

	/**
	 * 未开票金额
	 * @return
	 */
	public Double getUnInvoiceAmount(){
		// 价税合计
		BigDecimal priceTaxAmount = BigDecimal.valueOf(costPrice).setScale(Constant.MONEY_PRECISION,BigDecimal.ROUND_HALF_UP)
				.multiply(BigDecimal.valueOf(actualPickWeightServer).setScale(Constant.WEIGHT_PRECISION,BigDecimal.ROUND_HALF_UP))
				.setScale(Constant.MONEY_PRECISION,BigDecimal.ROUND_HALF_UP);
		if(invoiceAmount == null){
			invoiceAmount = 0d;
		}
		return priceTaxAmount.subtract(BigDecimal.valueOf(invoiceAmount)).setScale(Constant.MONEY_PRECISION,BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	/**
	 * 未开重量
	 * @return
	 */
	public Double getUnInvoiceWeight(){
		return BigDecimal.valueOf(actualPickWeightServer).setScale(Constant.WEIGHT_PRECISION,BigDecimal.ROUND_HALF_UP)
			.subtract(BigDecimal.valueOf(invoiceWeight).setScale(Constant.WEIGHT_PRECISION,BigDecimal.ROUND_HALF_UP))
			.setScale(Constant.WEIGHT_PRECISION,BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	//包含折让的未开票金额
	public Double getUnInvoiceAmountOfAllowance(){
		return getUnInvoiceAmount()+(allowanceAmount!=null?allowanceAmount:BigDecimal.ZERO).doubleValue();
	}
	//包含折让的未开票金额
	public Double getUnInvoiceWeightOfAllowance(){
		return getUnInvoiceWeight()+(allowanceWeight!=null?allowanceWeight:BigDecimal.ZERO).doubleValue();
	}
		

	public Long getOrderItemId() {
		return orderItemId;
	}

	public void setOrderItemId(Long orderItemId) {
		this.orderItemId = orderItemId;
	}

	public Date getOrderDateTime() {
		return orderDateTime;
	}

	public void setOrderDateTime(Date orderDateTime) {
		this.orderDateTime = orderDateTime;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getContractCode() {
		return contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}

	public String getNsortName() {
		return nsortName;
	}

	public void setNsortName(String nsortName) {
		this.nsortName = nsortName;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public String getMaterials() {
		return materials;
	}

	public void setMaterials(String materials) {
		this.materials = materials;
	}

	public Double getInvoiceAmount() {
		return invoiceAmount;
	}

	public void setInvoiceAmount(Double invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}

	public Double getInvoiceWeight() {
		return invoiceWeight;
	}

	public void setInvoiceWeight(Double invoiceWeight) {
		this.invoiceWeight = invoiceWeight;
	}

	public Double getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(Double costPrice) {
		this.costPrice = costPrice;
	}

	public Double getActualPickWeightServer() {
		return actualPickWeightServer;
	}

	public void setActualPickWeightServer(Double actualPickWeightServer) {
		this.actualPickWeightServer = actualPickWeightServer;
	}

	public boolean isMathced() {
		return mathced;
	}

	public void setMathced(boolean mathced) {
		this.mathced = mathced;
	}

	public Double getIncreaseWeight() {
		return increaseWeight;
	}

	public void setIncreaseWeight(Double increaseWeight) {
		this.increaseWeight = increaseWeight;
	}

	public Double getIncreaseAmount() {
		return increaseAmount;
	}

	public void setIncreaseAmount(Double increaseAmount) {
		this.increaseAmount = increaseAmount;
	}

	public BigDecimal getAllowanceAmount() {
		return allowanceAmount;
	}

	public void setAllowanceAmount(BigDecimal allowanceAmount) {
		this.allowanceAmount = allowanceAmount;
	}

	public BigDecimal getAllowanceWeight() {
		return allowanceWeight;
	}

	public void setAllowanceWeight(BigDecimal allowanceWeight) {
		this.allowanceWeight = allowanceWeight;
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public Boolean getIsShowDept() {
		return isShowDept;
	}

	public void setIsShowDept(Boolean isShowDept) {
		this.isShowDept = isShowDept;
	}

	public String getAlterStatus() {
		return alterStatus;
	}

	public void setAlterStatus(String alterStatus) {
		this.alterStatus = alterStatus;
	}

}
