package com.prcsteel.platform.kuandao.model.dto.spdb;

import java.io.Serializable;

public class MatchNotifyTransaction extends BaseTransaction implements Serializable{
	
	private static final long serialVersionUID = -808677383652426100L;
	
	private String termSsn;
	
	private String tranAmt;
	
	private String status;
	
	private String transDtTm;
	
	public String getTermSsn() {
		return termSsn;
	}
	
	public void setTermSsn(String termSsn) {
		this.termSsn = termSsn;
	}
	
	public String getTranAmt() {
		return tranAmt;
	}

	public void setTranAmt(String tranAmt) {
		this.tranAmt = tranAmt;
	}

	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getTransDtTm() {
		return transDtTm;
	}
	
	public void setTransDtTm(String transDtTm) {
		this.transDtTm = transDtTm;
	}
	
}
