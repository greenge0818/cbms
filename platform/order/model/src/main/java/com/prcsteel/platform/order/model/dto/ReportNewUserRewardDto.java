package com.prcsteel.platform.order.model.dto;

import java.math.BigDecimal;

public class ReportNewUserRewardDto {
    private Integer id;

    private BigDecimal newSeller;

    private BigDecimal newBuyer;

    private BigDecimal sellerAmount;

    private BigDecimal buyerAmount;

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the newSeller
	 */
	public BigDecimal getNewSeller() {
		return newSeller;
	}

	/**
	 * @param newSeller the newSeller to set
	 */
	public void setNewSeller(BigDecimal newSeller) {
		this.newSeller = newSeller;
	}

	/**
	 * @return the newBuyer
	 */
	public BigDecimal getNewBuyer() {
		return newBuyer;
	}

	/**
	 * @param newBuyer the newBuyer to set
	 */
	public void setNewBuyer(BigDecimal newBuyer) {
		this.newBuyer = newBuyer;
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

 
}