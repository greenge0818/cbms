package com.prcsteel.platform.order.model.dto;

import java.math.BigDecimal;

/**
 * 
    * @ClassName: OrderTradeCertificateForUploadDto
    * @Description: 凭证上传列表明细DTO
    * @author Green.Ge
    * @date 2016年4月8日
    *
 */
public class OrderTradeCertificateForUploadDto{
	private Long certificateId;		//凭证id
	private String certificateNO;	//凭证编号
	private String certificateCreated; // 凭证生成时间
	private Long orderId;			//订单id
	private String code;			//订单code
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	private Long buyerId;			//买家id
	private String buyerName;		//买家名字
	private Long sellerId;			//卖家id
	private String sellerName;		//卖家名字
	private BigDecimal totalWeight;	//总重量
	private BigDecimal totalAmount;	//总金额
	private String orderCreated;	//开单日期
	private String ownerName;		//交易员
	private int quantity;			//总数量
	private int actualPickTotalQuantity; 		//实提总数量
	private BigDecimal actualPickTotalWeight;	//实提总重量
	private BigDecimal actualPickTotalAmount;	//实提总金额
	private String status; //凭证状态
	private Boolean approvalRequired ; 	//开票类型,是否需要审核才能开票
	private String type;			//凭证类型，买家/卖家
	private String name;			//凭证名称，
	private String orderOrgName; //订单服务中心名称
	
	
	public Long getCertificateId() {
		return certificateId;
	}
	public void setCertificateId(Long certificateId) {
		this.certificateId = certificateId;
	}
	public String getCertificateNO() {
		return certificateNO;
	}
	public void setCertificateNO(String certificateNO) {
		this.certificateNO = certificateNO;
	}
	
	public String getCertificateCreated() {
		return certificateCreated;
	}
	public void setCertificateCreated(String certificateCreated) {
		this.certificateCreated = certificateCreated;
	}
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getBuyerName() {
		return buyerName;
	}
	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}
	public String getSellerName() {
		return sellerName;
	}
	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}
	public BigDecimal getTotalWeight() {
		return totalWeight;
	}
	public void setTotalWeight(BigDecimal totalWeight) {
		this.totalWeight = totalWeight;
	}
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
	
	public String getOrderCreated() {
		return orderCreated;
	}
	public void setOrderCreated(String orderCreated) {
		this.orderCreated = orderCreated;
	}
	public String getOwnerName() {
		return ownerName;
	}
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public int getActualPickTotalQuantity() {
		return actualPickTotalQuantity;
	}
	public void setActualPickTotalQuantity(int actualPickTotalQuantity) {
		this.actualPickTotalQuantity = actualPickTotalQuantity;
	}
	public BigDecimal getActualPickTotalWeight() {
		return actualPickTotalWeight;
	}
	public void setActualPickTotalWeight(BigDecimal actualPickTotalWeight) {
		this.actualPickTotalWeight = actualPickTotalWeight;
	}
	public BigDecimal getActualPickTotalAmount() {
		return actualPickTotalAmount;
	}
	public void setActualPickTotalAmount(BigDecimal actualPickTotalAmount) {
		this.actualPickTotalAmount = actualPickTotalAmount;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Boolean getApprovalRequired() {
		return approvalRequired;
	}
	public void setApprovalRequired(Boolean approvalRequired) {
		this.approvalRequired = approvalRequired;
	}
	public Long getBuyerId() {
		return buyerId;
	}
	public void setBuyerId(Long buyerId) {
		this.buyerId = buyerId;
	}
	public Long getSellerId() {
		return sellerId;
	}
	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}

	public String getOrderOrgName() {
		return orderOrgName;
	}

	public void setOrderOrgName(String orderOrgName) {
		this.orderOrgName = orderOrgName;
	}
}
