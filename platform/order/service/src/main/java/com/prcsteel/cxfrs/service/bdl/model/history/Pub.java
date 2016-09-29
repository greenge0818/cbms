package com.prcsteel.cxfrs.service.bdl.model.history;


public class Pub implements java.io.Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -3022683658514696095L;
	private String transCode;
	private String CIS;
	private String bankCode;
	private String ID;
	private String tranDate;
	private String tranTime;
	private String fSeqno;
	private String retCode;
	private String retMsg;

	public Pub() {

	}



	public Pub(String transCode, String cIS, String bankCode, String iD,
			String tranDate, String tranTime, String fSeqno,
			String retCode, String retMsg) {
		super();
		this.transCode = transCode;
		CIS = cIS;
		this.bankCode = bankCode;
		ID = iD;
		this.tranDate = tranDate;
		this.tranTime = tranTime;
		this.fSeqno = fSeqno;
		this.retCode = retCode;
		this.retMsg = retMsg;
	}

	public String getTransCode() {
		return transCode;
	}

	public void setTransCode(String transCode) {
		this.transCode = transCode;
	}

	public String getCIS() {
		return CIS;
	}

	public void setCIS(String cIS) {
		CIS = cIS;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getTranDate() {
		return tranDate;
	}

	public void setTranDate(String tranDate) {
		this.tranDate = tranDate;
	}

	public String getTranTime() {
		return tranTime;
	}

	public void setTranTime(String tranTime) {
		this.tranTime = tranTime;
	}

	public String getfSeqno() {
		return fSeqno;
	}

	public void setfSeqno(String fSeqno) {
		this.fSeqno = fSeqno;
	}

	public String getRetCode() {
		return retCode;
	}

	public void setRetCode(String retCode) {
		this.retCode = retCode;
	}

	public String getRetMsg() {
		return retMsg;
	}

	public void setRetMsg(String retMsg) {
		this.retMsg = retMsg;
	}



}
