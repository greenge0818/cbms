package com.prcsteel.platform.order.model.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by chenchen on 2015/8/20.
 */
public class OrganizationRewardRecordDto {
    private Long organizationId;
    private String orgName;
    private BigDecimal addSellerAmount;
    private BigDecimal addBuyerAmount;
    private BigDecimal buyerAmount;
    private BigDecimal sellerAmount;
    private List<RewardReportDto> rewardReportDtoList;
    private ReportNewUserRewardDto reportNewUserRewardDto;
	/**
	 * @return the organizationId
	 */
	public Long getOrganizationId() {
		return organizationId;
	}
	/**
	 * @param organizationId the organizationId to set
	 */
	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
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
	 * @return the addSellerAmount
	 */
	public BigDecimal getAddSellerAmount() {
		return addSellerAmount;
	}
	/**
	 * @param addSellerAmount the addSellerAmount to set
	 */
	public void setAddSellerAmount(BigDecimal addSellerAmount) {
		this.addSellerAmount = addSellerAmount;
	}
	/**
	 * @return the addBuyerAmount
	 */
	public BigDecimal getAddBuyerAmount() {
		return addBuyerAmount;
	}
	/**
	 * @param addBuyerAmount the addBuyerAmount to set
	 */
	public void setAddBuyerAmount(BigDecimal addBuyerAmount) {
		this.addBuyerAmount = addBuyerAmount;
	}
	/**
	 * @return the buyerAmount
	 */
	public BigDecimal getBuyerAmount() {
		return buyerAmount;
	}
	/**
	 * @param buyerAmount the buyerAmount to set
	 */
	public void setBuyerAmount(BigDecimal buyerAmount) {
		this.buyerAmount = buyerAmount;
	}
	/**
	 * @return the sellerAmount
	 */
	public BigDecimal getSellerAmount() {
		return sellerAmount;
	}
	/**
	 * @param sellerAmount the sellerAmount to set
	 */
	public void setSellerAmount(BigDecimal sellerAmount) {
		this.sellerAmount = sellerAmount;
	}
	/**
	 * @return the rewardReportDtoList
	 */
	public List<RewardReportDto> getRewardReportDtoList() {
		return rewardReportDtoList;
	}
	/**
	 * @param rewardReportDtoList the rewardReportDtoList to set
	 */
	public void setRewardReportDtoList(List<RewardReportDto> rewardReportDtoList) {
		this.rewardReportDtoList = rewardReportDtoList;
	}
	public ReportNewUserRewardDto getReportNewUserRewardDto() {
		return reportNewUserRewardDto;
	}
	public void setReportNewUserRewardDto(ReportNewUserRewardDto reportNewUserRewardDto) {
		this.reportNewUserRewardDto = reportNewUserRewardDto;
	}

    
}
