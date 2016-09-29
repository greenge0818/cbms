package com.prcsteel.platform.kuandao.model.dto.spdb;

import java.io.Serializable;

public class MCDBTransaction extends BaseTransaction implements Serializable{
	private static final long serialVersionUID = -1642538114321346784L;
	
	private String mercDtTm;
	private String termSsn;
	private String oAcqSsn;
	private String oSttDate;
	private String tranAmt;
	private String subMerId;
	private String termCode;
	private String remark1;
	private String remark2;
	
	private String acqSsn;
	private String respCode;
	private String settDate;
	public String getMercDtTm() {
		return mercDtTm;
	}
	public void setMercDtTm(String mercDtTm) {
		this.mercDtTm = mercDtTm;
	}
	public String getTermSsn() {
		return termSsn;
	}
	public void setTermSsn(String termSsn) {
		this.termSsn = termSsn;
	}
	public String getoAcqSsn() {
		return oAcqSsn;
	}
	public void setoAcqSsn(String oAcqSsn) {
		this.oAcqSsn = oAcqSsn;
	}
	public String getoSttDate() {
		return oSttDate;
	}
	public void setoSttDate(String oSttDate) {
		this.oSttDate = oSttDate;
	}
	public String getTranAmt() {
		return tranAmt;
	}
	public void setTranAmt(String tranAmt) {
		this.tranAmt = tranAmt;
	}
	public String getSubMerId() {
		return subMerId;
	}
	public void setSubMerId(String subMerId) {
		this.subMerId = subMerId;
	}
	public String getTermCode() {
		return termCode;
	}
	public void setTermCode(String termCode) {
		this.termCode = termCode;
	}
	public String getRemark1() {
		return remark1;
	}
	public void setRemark1(String remark1) {
		this.remark1 = remark1;
	}
	public String getRemark2() {
		return remark2;
	}
	public void setRemark2(String remark2) {
		this.remark2 = remark2;
	}
	public String getAcqSsn() {
		return acqSsn;
	}
	public void setAcqSsn(String acqSsn) {
		this.acqSsn = acqSsn;
	}
	public String getRespCode() {
		return respCode;
	}
	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}
	public String getSettDate() {
		return settDate;
	}
	public void setSettDate(String settDate) {
		this.settDate = settDate;
	}
	
	
	
	
}
