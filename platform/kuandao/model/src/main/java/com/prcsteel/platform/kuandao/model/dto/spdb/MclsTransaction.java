package com.prcsteel.platform.kuandao.model.dto.spdb;

import java.io.Serializable;

public class MclsTransaction extends BaseTransaction implements Serializable {

	private static final long serialVersionUID = -1607489856492362022L;
	
	private String impDate;
	
	private String virAcctName;
	
	private String virAcctNo;
	
	private String impStatus;
	
	private String tranAmt;

	private String impAcqSsn;
	
	private String payMerName;
	
	private String payMerBranchId;
	
	private String payMerAcctNo;
	
	private String purpose;
	
	private String remitDetails;
	
	

	public String getRemitDetails() {
		return remitDetails;
	}

	public void setRemitDetails(String remitDetails) {
		this.remitDetails = remitDetails;
	}

	public String getImpDate() {
		return impDate;
	}

	public void setImpDate(String impDate) {
		this.impDate = impDate;
	}

	public String getVirAcctName() {
		return virAcctName;
	}

	public void setVirAcctName(String virAcctName) {
		this.virAcctName = virAcctName;
	}

	public String getVirAcctNo() {
		return virAcctNo;
	}

	public void setVirAcctNo(String virAcctNo) {
		this.virAcctNo = virAcctNo;
	}

	public String getImpStatus() {
		return impStatus;
	}

	public void setImpStatus(String impStatus) {
		this.impStatus = impStatus;
	}

	public String getTranAmt() {
		return tranAmt;
	}

	public void setTranAmt(String tranAmt) {
		this.tranAmt = tranAmt;
	}

	public String getImpAcqSsn() {
		return impAcqSsn;
	}

	public void setImpAcqSsn(String impAcqSsn) {
		this.impAcqSsn = impAcqSsn;
	}

	public String getPayMerName() {
		return payMerName;
	}

	public void setPayMerName(String payMerName) {
		this.payMerName = payMerName;
	}

	public String getPayMerBranchId() {
		return payMerBranchId;
	}

	public void setPayMerBranchId(String payMerBranchId) {
		this.payMerBranchId = payMerBranchId;
	}

	public String getPayMerAcctNo() {
		return payMerAcctNo;
	}

	public void setPayMerAcctNo(String payMerAcctNo) {
		this.payMerAcctNo = payMerAcctNo;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	
	
	
}
