package com.prcsteel.platform.account.model.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.prcsteel.platform.account.model.model.CustGroupingInforAccount;

public class GroupInforAccountDto extends CustGroupingInforAccount{
	private Long groupInfoId;//分组id
	
	private String groupName;//分组名称
	
	private String groupNameAudit;//分组名称未生效待审核
	
	private BigDecimal groupCreditLimit;//分组已生效组信用额度
	
	private BigDecimal groupCreditLimitAudit;//分组未生效待审核组信用额度
	
	private String groupInfoSerial;//分组流水号
	
	private String groupInfoStatus;//分组状态
	
	private Date groupInfoCreated;//分组创建时间
	
	private int departmentCount;//部门记录数

	public int getDepartmentCount() {
		return departmentCount;
	}

	public void setDepartmentCount(int departmentCount) {
		this.departmentCount = departmentCount;
	}

	public Long getGroupInfoId() {
		return groupInfoId;
	}

	public void setGroupInfoId(Long groupInfoId) {
		this.groupInfoId = groupInfoId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupNameAudit() {
		return groupNameAudit;
	}

	public void setGroupNameAudit(String groupNameAudit) {
		this.groupNameAudit = groupNameAudit;
	}

	public BigDecimal getGroupCreditLimit() {
		return groupCreditLimit;
	}

	public void setGroupCreditLimit(BigDecimal groupCreditLimit) {
		this.groupCreditLimit = groupCreditLimit;
	}

	public BigDecimal getGroupCreditLimitAudit() {
		return groupCreditLimitAudit;
	}

	public void setGroupCreditLimitAudit(BigDecimal groupCreditLimitAudit) {
		this.groupCreditLimitAudit = groupCreditLimitAudit;
	}

	public String getGroupInfoSerial() {
		return groupInfoSerial;
	}

	public void setGroupInfoSerial(String groupInfoSerial) {
		this.groupInfoSerial = groupInfoSerial;
	}

	public String getGroupInfoStatus() {
		return groupInfoStatus;
	}

	public void setGroupInfoStatus(String groupInfoStatus) {
		this.groupInfoStatus = groupInfoStatus;
	}

	public Date getGroupInfoCreated() {
		return groupInfoCreated;
	}

	public void setGroupInfoCreated(Date groupInfoCreated) {
		this.groupInfoCreated = groupInfoCreated;
	}
}
