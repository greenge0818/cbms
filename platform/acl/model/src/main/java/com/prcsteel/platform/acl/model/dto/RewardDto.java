package com.prcsteel.platform.acl.model.dto;

import java.math.BigDecimal;

/**
 * Created by chenchen on 2015/8/3.
 */
public class RewardDto {
    private Long id;
    private String categoryUuid;
    private String categoryName;
    private BigDecimal sellerLimit;
    private BigDecimal rewardRole;
	private BigDecimal buyRadio;

	public BigDecimal getBuyRadio() {
		return buyRadio;
	}

	public void setBuyRadio(BigDecimal buyRadio) {
		this.buyRadio = buyRadio;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public BigDecimal getRewardRole() {
		return rewardRole;
	}

	public void setRewardRole(BigDecimal rewardRole) {
		this.rewardRole = rewardRole;
	}

	public BigDecimal getSellerLimit() {
		return sellerLimit;
	}

	public void setSellerLimit(BigDecimal sellerLimit) {
		this.sellerLimit = sellerLimit;
	}

	public String getCategoryUuid() {
		return categoryUuid;
	}

	public void setCategoryUuid(String categoryUuid) {
		this.categoryUuid = categoryUuid;
	}

   
}
