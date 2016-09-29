package com.prcsteel.platform.kuandao.model.dto.spdb;

import java.io.Serializable;

public class MemeberQueryTransaction extends BaseTransaction implements Serializable{
	
	private static final long serialVersionUID = 8088759652911007296L;

	private String subMerId;
	
	private String subMerName;
	
	private String mobileNo;
	
	private String subMerType;
	
	private String idType;
	
	private String idNo;
	
	private String subMerAcctType;
	
	private String subMerBranchId;
	
	private String deptId;
	
	private String subMerAcctNo;
	
	private String drawAmt;
	
	private String subMerVirAcctNo;
	
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

	public String getSubMerAcctType() {
		return subMerAcctType;
	}

	public void setSubMerAcctType(String subMerAcctType) {
		this.subMerAcctType = subMerAcctType;
	}

	public String getSubMerBranchId() {
		return subMerBranchId;
	}

	public void setSubMerBranchId(String subMerBranchId) {
		this.subMerBranchId = subMerBranchId;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getSubMerAcctNo() {
		return subMerAcctNo;
	}

	public void setSubMerAcctNo(String subMerAcctNo) {
		this.subMerAcctNo = subMerAcctNo;
	}

	public String getDrawAmt() {
		return drawAmt;
	}

	public void setDrawAmt(String drawAmt) {
		this.drawAmt = drawAmt;
	}

	public String getSubMerVirAcctNo() {
		return subMerVirAcctNo;
	}

	public void setSubMerVirAcctNo(String subMerVirAcctNo) {
		this.subMerVirAcctNo = subMerVirAcctNo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	

}
