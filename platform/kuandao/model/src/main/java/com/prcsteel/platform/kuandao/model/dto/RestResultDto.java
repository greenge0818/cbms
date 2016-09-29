package com.prcsteel.platform.kuandao.model.dto;

import java.io.Serializable;

public class RestResultDto implements Serializable {

	private static final long serialVersionUID = -8615320264448183003L;

	private String status;
	
	private String data;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	
}
