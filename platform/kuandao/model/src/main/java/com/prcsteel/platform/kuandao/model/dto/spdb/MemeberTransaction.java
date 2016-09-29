package com.prcsteel.platform.kuandao.model.dto.spdb;

import java.io.Serializable;

public class MemeberTransaction extends BaseTransaction implements Serializable {

	private static final long serialVersionUID = 5901601545865494182L;

	private String mercDtTm;
	
	private String transType;
	
	private String virAcctFlag;
	
	private String virAcctNo;
	
	private String subMerId;
	
	private String subMerName;
	
	private String acctNo;
	
	private String mobileNo;
	
	private String subMerType;
	
	private String idType;
	
	private String idNo;
	
	private String mercUrl;
	
	private String respCode;
	
	public String getMercDtTm() {
		return mercDtTm;
	}

	public void setMercDtTm(String mercDtTm) {
		this.mercDtTm = mercDtTm;
	}

	public String getTransType() {
		return transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
	}
	
	public String getVirAcctFlag() {
		return virAcctFlag;
	}

	public void setVirAcctFlag(String virAcctFlag) {
		this.virAcctFlag = virAcctFlag;
	}

	public String getVirAcctNo() {
		return virAcctNo;
	}

	public void setVirAcctNo(String virAcctNo) {
		this.virAcctNo = virAcctNo;
	}

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

	public String getAcctNo() {
		return acctNo;
	}

	public void setAcctNo(String acctNo) {
		this.acctNo = acctNo;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getSubMerType() {
		return subMerType;
	}

	public void setSubMerType(String subMerType) {
		this.subMerType = subMerType;
	}

	public String getIdType() {
		return idType;
	}

	public void setIdType(String idType) {
		this.idType = idType;
	}

	public String getIdNo() {
		return idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}

	public String getMercUrl() {
		return mercUrl;
	}

	public void setMercUrl(String mercUrl) {
		this.mercUrl = mercUrl;
	}

	public String getRespCode() {
		return respCode;
	}

	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}

}
