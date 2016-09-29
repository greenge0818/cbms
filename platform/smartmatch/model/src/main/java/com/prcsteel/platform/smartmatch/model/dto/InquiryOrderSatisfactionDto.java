package com.prcsteel.platform.smartmatch.model.dto;

import java.math.BigDecimal;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 订单满足率统计
 * 
 * @author yjx
 *
 */
public class InquiryOrderSatisfactionDto {
	private Long purchaseOrderItemId;// 订单item详情ID
	private BigDecimal totalWeight;// 订单详情item价的总质量
	private Integer totalInquiryCount;// 订单详情item的总询价次数
	private String categoryName;
	private String materialName;
	private String spec;
	private Double rate;

	public Long getPurchaseOrderItemId() {
		return purchaseOrderItemId;
	}

	public void setPurchaseOrderItemId(Long purchaseOrderItemId) {
		this.purchaseOrderItemId = purchaseOrderItemId;
	}

	

	public BigDecimal getTotalWeight() {
		return totalWeight;
	}

	public void setTotalWeight(BigDecimal totalWeight) {
		this.totalWeight = totalWeight;
	}

	public Integer getTotalInquiryCount() {
		return totalInquiryCount;
	}

	public void setTotalInquiryCount(Integer totalInquiryCount) {
		this.totalInquiryCount = totalInquiryCount;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getMaterialName() {
		return materialName;
	}

	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

}
