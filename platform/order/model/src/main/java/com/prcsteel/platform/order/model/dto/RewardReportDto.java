package com.prcsteel.platform.order.model.dto;

import java.math.BigDecimal;

/**
 * Created by chenchen on 2015/8/19.
 */
public class RewardReportDto {
    private Long id;
    private String categoryGroupUuid;
    private String categoryGroupName;
    private Long orgId;
    private String orgName; 
    private Long managerId;
    private String managerName; 
    private BigDecimal buyerWeightSum;
    private BigDecimal buyerAmountSum;
    private BigDecimal sellerWeightSum;
    private BigDecimal sellerAmountSum;
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return the categoryGroupUuid
	 */
	public String getCategoryGroupUuid() {
		return categoryGroupUuid;
	}
	/**
	 * @param categoryGroupUuid the categoryGroupUuid to set
	 */
	public void setCategoryGroupUuid(String categoryGroupUuid) {
		this.categoryGroupUuid = categoryGroupUuid;
	}
	/**
	 * @return the categoryGroupName
	 */
	public String getCategoryGroupName() {
		return categoryGroupName;
	}
	/**
	 * @param categoryGroupName the categoryGroupName to set
	 */
	public void setCategoryGroupName(String categoryGroupName) {
		this.categoryGroupName = categoryGroupName;
	}
	/**
	 * @return the orgId
	 */
	public Long getOrgId() {
		return orgId;
	}
	/**
	 * @param orgId the orgId to set
	 */
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	/**
	 * @return the orgName
	 */
	public String getOrgName() {
		return orgName;
	}
	/**
	 * @param orgName the orgName to set
	 */
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	/**
	 * @return the buyerWeightSum
	 */
	public BigDecimal getBuyerWeightSum() {
		return buyerWeightSum;
	}
	/**
	 * @param buyerWeightSum the buyerWeightSum to set
	 */
	public void setBuyerWeightSum(BigDecimal buyerWeightSum) {
		this.buyerWeightSum = buyerWeightSum;
	}
	/**
	 * @return the buyerAmountSum
	 */
	public BigDecimal getBuyerAmountSum() {
		return buyerAmountSum;
	}
	/**
	 * @param buyerAmountSum the buyerAmountSum to set
	 */
	public void setBuyerAmountSum(BigDecimal buyerAmountSum) {
		this.buyerAmountSum = buyerAmountSum;
	}
	/**
	 * @return the sellerWeightSum
	 */
	public BigDecimal getSellerWeightSum() {
		return sellerWeightSum;
	}
	/**
	 * @param sellerWeightSum the sellerWeightSum to set
	 */
	public void setSellerWeightSum(BigDecimal sellerWeightSum) {
		this.sellerWeightSum = sellerWeightSum;
	}
	/**
	 * @return the sellerAmountSum
	 */
	public BigDecimal getSellerAmountSum() {
		return sellerAmountSum;
	}
	/**
	 * @param sellerAmountSum the sellerAmountSum to set
	 */
	public void setSellerAmountSum(BigDecimal sellerAmountSum) {
		this.sellerAmountSum = sellerAmountSum;
	}
	public Long getManagerId() {
		return managerId;
	}
	public void setManagerId(Long managerId) {
		this.managerId = managerId;
	}
	public String getManagerName() {
		return managerName;
	}
	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}
	
   
   
}
