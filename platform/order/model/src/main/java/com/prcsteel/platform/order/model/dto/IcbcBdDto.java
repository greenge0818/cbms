package com.prcsteel.platform.order.model.dto;

import java.util.Date;

import com.prcsteel.platform.order.model.model.IcbcBdlDetail;

public class IcbcBdDto extends IcbcBdlDetail {
	private Date transTime;

	public Date getTransTime() {
		return transTime;
	}

	public void setTransTime(Date transTime) {
		this.transTime = transTime;
	}
}
