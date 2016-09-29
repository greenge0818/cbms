package com.prcsteel.platform.account.model.dto;

import java.math.BigDecimal;
import java.util.List;

public class CustGroupingInforDto {
	private Long groupId;
	
	private String groupName;//分组生效名称
	
	private String groupNameAudit;//分组未生效名称
	
	private BigDecimal groupLimit;//组信用额度

	private BigDecimal groupCreditUsed;//组已用额度

	private BigDecimal groupCreditBalance;//组可用额度
	
	private String groupInfoStatus;//分组状态
	
	private String groupInfoSerial;//分组流水号
	
	private int num = 0;//客户数
	
	private List<GroupInforAccountDto> companyList;

	public String getGroupInfoSerial() {
		return groupInfoSerial;
	}

	public void setGroupInfoSerial(String groupInfoSerial) {
		this.groupInfoSerial = groupInfoSerial;
	}

	public String getGroupNameAudit() {
		return groupNameAudit;
	}

	public void setGroupNameAudit(String groupNameAudit) {
		this.groupNameAudit = groupNameAudit;
	}

	public String getGroupInfoStatus() {
		return groupInfoStatus;
	}

	public void setGroupInfoStatus(String groupInfoStatus) {
		this.groupInfoStatus = groupInfoStatus;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public BigDecimal getGroupLimit() {
		return groupLimit;
	}

	public void setGroupLimit(BigDecimal groupLimit) {
		this.groupLimit = groupLimit;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public List<GroupInforAccountDto> getCompanyList() {
		return companyList;
	}

	public void setCompanyList(List<GroupInforAccountDto> companyList) {
		this.companyList = companyList;
	}

	public BigDecimal getGroupCreditUsed() {
		return groupCreditUsed;
	}

	public void setGroupCreditUsed(BigDecimal groupCreditUsed) {
		this.groupCreditUsed = groupCreditUsed;
	}

	public BigDecimal getGroupCreditBalance() {
		return groupCreditBalance;
	}

	public void setGroupCreditBalance(BigDecimal groupCreditBalance) {
		this.groupCreditBalance = groupCreditBalance;
	}
}
