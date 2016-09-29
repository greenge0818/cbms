package com.prcsteel.platform.order.model.dto;

import java.util.Date;

import com.prcsteel.platform.order.model.model.BankOriginalDetail;

public class BankOriginalDto extends BankOriginalDetail {
	private Date transDateTime;

	public Date getTransDateTime() {
		return transDateTime;
	}

	public void setTransDateTime(Date transDateTime) {
		this.transDateTime = transDateTime;
	}
}
