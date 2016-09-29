package com.prcsteel.platform.kuandao.model.dto;

import java.io.Serializable;

public class ChargResultResponse implements Serializable {

	private static final long serialVersionUID = 7902080373631010769L;

	/**款道支付单号*/
	private	Integer kuandaoPayorderId;
	
	/**充值结果 00：成功 01：失败*/
	private String status;

	public Integer getKuandaoPayorderId() {
		return kuandaoPayorderId;
	}

	public void setKuandaoPayorderId(Integer kuandaoPayorderId) {
		this.kuandaoPayorderId = kuandaoPayorderId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
