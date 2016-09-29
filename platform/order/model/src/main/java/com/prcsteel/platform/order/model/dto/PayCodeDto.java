package com.prcsteel.platform.order.model.dto;

import java.util.Date;


public class PayCodeDto {
    private Long orgId;
    
    private String payCode;
    
    private Date created;

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public String getPayCode() {
		return payCode;
	}

	public void setPayCode(String payCode) {
		this.payCode = payCode;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}
}
