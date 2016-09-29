package com.prcsteel.platform.order.model.dto;

import java.math.BigDecimal;

/**
 * Created by chenchen on 2015/8/3.
 */
public class RebateDto {
    private Long id;
    private String categoryUuid;
    private String categoryName;
    private BigDecimal rebateRole;
    

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

	public BigDecimal getRebateRole() {
		return rebateRole;
	}

	public void setRebateRole(BigDecimal rebateRole) {
		this.rebateRole = rebateRole;
	}

	public String getCategoryUuid() {
		return categoryUuid;
	}

	public void setCategoryUuid(String categoryUuid) {
		this.categoryUuid = categoryUuid;
	}

	

   
}
