package com.prcsteel.platform.account.model.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.prcsteel.platform.account.model.model.Account;
import com.prcsteel.platform.account.model.model.AccountBusinessType;
import com.prcsteel.platform.account.model.model.AccountExt;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.core.model.model.City;
import com.prcsteel.platform.core.model.model.District;
import com.prcsteel.platform.core.model.model.Province;

/**   
 * @Title: Account.java 
 * @Package com.prcsteel.cbms.persist.dto 
 * @Description: 客户封装类 
 * @author Green.Ge   
 * @date 2015年7月15日 上午9:25:25 
 * @version V1.0   
 */
/** 
* @ClassName: Account 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author A18ccms a18ccms_gmail_com 
* @date 2015年7月15日 下午4:56:18 
*  
*/
public class AccountDto {
	private Account account;

	private Map<String, Object> attachments;

	private AccountBusinessType businessType;
	private Province province;
	private City city;
	private District district;
	private Province bankProvince;
	private City bankCity;
	private AccountExt accountExt;
	private String customerProperty; // 客户性质（除临采，寄售）
	
	/**
	 * 客户公司部门
	 */
	private List<DepartmentDto> department;
	
	private String departmentName;//客户部门
	
	private Long departmentCount;//部门记录数
	
	private String name;//客户名
	
	private BigDecimal balanceSecondSettlement;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getBalanceSecondSettlement() {
		return balanceSecondSettlement;
	}

	public void setBalanceSecondSettlement(BigDecimal balanceSecondSettlement) {
		this.balanceSecondSettlement = balanceSecondSettlement;
	}

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

	public Province getBankProvince() {
		return bankProvince;
	}

	public void setBankProvince(Province bankProvince) {
		this.bankProvince = bankProvince;
	}

	public City getBankCity() {
		return bankCity;
	}

	public void setBankCity(City bankCity) {
		this.bankCity = bankCity;
	}

	private User manager;
	public Account getAccount() {
		return account;
	}

	public Map<String, Object> getAttachments() {
		return attachments;
	}

	public AccountBusinessType getBusinessType() {
		return businessType;
	}

	public User getManager() {
		return manager;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public void setAttachments(Map<String, Object> attachments) {
		this.attachments = attachments;
	}

	public void setBusinessType(AccountBusinessType businessType) {
		this.businessType = businessType;
	}

	public Province getProvince() {
		return province;
	}

	public void setProvince(Province province) {
		this.province = province;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public District getDistrict() {
		return district;
	}

	public void setDistrict(District district) {
		this.district = district;
	}

	public void setManager(User manager) {
		this.manager = manager;
	}

	public AccountExt getAccountExt() {
		return accountExt;
	}

	public void setAccountExt(AccountExt accountExt) {
		this.accountExt = accountExt;
	}

	public List<DepartmentDto> getDepartment() {
		return department;
	}

	public void setDepartment(List<DepartmentDto> department) {
		this.department = department;
	}

	public String getCustomerProperty() {
		return customerProperty;
	}

	public void setCustomerProperty(String customerProperty) {
		this.customerProperty = customerProperty;
	}

}
 