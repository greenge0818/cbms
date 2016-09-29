package com.prcsteel.platform.kuandao.model.dto.spdb;

import java.io.Serializable;

public class BoundNotifyTransaction extends BaseTransaction implements Serializable {

	private static final long serialVersionUID = 7163977227718638073L;

	private String subMerId;
	
	private String subMerName;
	
	private String subMerCifNo;
	
	private String operType;
	
	private String transDtTm;
	
	private String status;

	public String getSubMerId() {
		return subMerId;
	}

	public void setSubMerId(String subMerId) {
		this.subMerId = subMerId;
	}

	public String getSubMerName() {
		return subMerName;
	}

	public void setSubMerName(String subMerName) {
		this.subMerName = subMerName;
	}

	public String getSubMerCifNo() {
		return subMerCifNo;
	}

	public void setSubMerCifNo(String subMerCifNo) {
		this.subMerCifNo = subMerCifNo;
	}

	public String getOperType() {
		return operType;
	}

	public void setOperType(String operType) {
		this.operType = operType;
	}

	public String getTransDtTm() {
		return transDtTm;
	}

	public void setTransDtTm(String transDtTm) {
		this.transDtTm = transDtTm;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
