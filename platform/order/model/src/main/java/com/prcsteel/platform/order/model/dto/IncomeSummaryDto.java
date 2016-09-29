package com.prcsteel.platform.order.model.dto;

import java.math.BigDecimal;

public class IncomeSummaryDto {
	private Long orgId;				//服务中心id

	private String name;			//服务中心名字

	private String accountName;		//客户名称：买家、卖家

	private String nsortName;		//品名

	private BigDecimal amount;		//采购金额 

	private BigDecimal weight;		//采购重量
	
	private BigDecimal saleAmount; 	//销售金额
	
	private BigDecimal incomeAmount;//税前利润 
	
	private String departmentName;//增加部门名称 add lixiang
    
    private Long departmentCount;//部门个数
	
	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public Long getDepartmentCount() {
		return departmentCount;
	}

	public void setDepartmentCount(Long departmentCount) {
		this.departmentCount = departmentCount;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getNsortName() {
		return nsortName;
	}

	public void setNsortName(String nsortName) {
		this.nsortName = nsortName;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getWeight() {
		return weight;
	}

	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}

	public BigDecimal getSaleAmount() {
		return saleAmount;
	}

	public void setSaleAmount(BigDecimal saleAmount) {
		this.saleAmount = saleAmount;
	}

	public BigDecimal getIncomeAmount() {
		return incomeAmount;
	}

	public void setIncomeAmount(BigDecimal incomeAmount) {
		this.incomeAmount = incomeAmount;
	}
}
