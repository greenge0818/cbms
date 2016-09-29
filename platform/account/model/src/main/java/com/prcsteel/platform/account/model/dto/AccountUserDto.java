package com.prcsteel.platform.account.model.dto;

import java.math.BigDecimal;

public class AccountUserDto {
	private Long departmentId;//部门id
	
	private String accountName;
	
	private String departmentName;// 公司部门
	
	private String managerName;
	
	private Long cid;

	private String cName;

	private Long managerId;

	private BigDecimal balanceSecondSettlement;

	private Long uid;

	private String uName;

	private String oName;
	
	private BigDecimal minusBalanceSecond;
	
    private BigDecimal positiveBalanceSecond;

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getManagerName() {
		return managerName;
	}

	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}

	public BigDecimal getMinusBalanceSecond() {
		return minusBalanceSecond;
	}

	public void setMinusBalanceSecond(BigDecimal minusBalanceSecond) {
		this.minusBalanceSecond = minusBalanceSecond;
	}

	public BigDecimal getPositiveBalanceSecond() {
		return positiveBalanceSecond;
	}

	public void setPositiveBalanceSecond(BigDecimal positiveBalanceSecond) {
		this.positiveBalanceSecond = positiveBalanceSecond;
	}

	public String getoName() {
		return oName;
	}

	public void setoName(String oName) {
		this.oName = oName;
	}

	public Long getCid() {
		return cid;
	}

	public void setCid(Long cid) {
		this.cid = cid;
	}

	public String getcName() {
		return cName;
	}

	public void setcName(String cName) {
		this.cName = cName;
	}

	public Long getManagerId() {
		return managerId;
	}

	public void setManagerId(Long managerId) {
		this.managerId = managerId;
	}

	public BigDecimal getBalanceSecondSettlement() {
		return balanceSecondSettlement;
	}

	public void setBalanceSecondSettlement(BigDecimal balanceSecondSettlement) {
		this.balanceSecondSettlement = balanceSecondSettlement;
	}

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public String getuName() {
		return uName;
	}

	public void setuName(String uName) {
		this.uName = uName;
	}

}
