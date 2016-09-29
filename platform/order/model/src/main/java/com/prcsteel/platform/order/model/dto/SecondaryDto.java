package com.prcsteel.platform.order.model.dto;

import com.prcsteel.platform.account.model.enums.AccountType;

import java.math.BigDecimal;

/**
 * Created by Rabbit Mao on 2015/7/28.
 */
public class SecondaryDto {
	
	public SecondaryDto(){
	}
	public SecondaryDto(Long cusId,String cusName, Long departmentId, String departmentName, AccountType cusType,BigDecimal secondaryMoney, Long managerId, String managerName){
		this.cusId = cusId;
		this.cusName = cusName;
		this.cusType = cusType;
		this.secondaryMoney = secondaryMoney;
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.managerId = managerId;
        this.managerName = managerName;
    }
    Long cusId;
    String cusName;
    Long departmentId;
    String departmentName;
    AccountType cusType;
    BigDecimal secondaryMoney;
    Long managerId;
    String managerName;

    public Long getCusId() {
        return cusId;
    }

    public void setCusId(Long cusId) {
        this.cusId = cusId;
    }

    public String getCusName() {
        return cusName;
    }

    public void setCusName(String cusName) {
        this.cusName = cusName;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public AccountType getCusType() {
        return cusType;
    }

    public void setCusType(AccountType cusType) {
        this.cusType = cusType;
    }

    public Double getSecondaryMoney() {
    	if(secondaryMoney == null) return 0d;
        return secondaryMoney.doubleValue();
    }

    public BigDecimal getSecondaryMoneyBigDecimal(){
        return secondaryMoney;
    }

    public Long getManagerId() {
        return managerId;
    }

    public void setManagerId(Long managerId) {
        this.managerId = managerId;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }
}