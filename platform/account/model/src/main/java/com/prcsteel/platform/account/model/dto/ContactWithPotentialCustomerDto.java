package com.prcsteel.platform.account.model.dto;

public class ContactWithPotentialCustomerDto {
	private Integer contactId;		//联系人id
	private Integer accountId;		//客户id
	private Integer ecUserId;		//联系人：超市id
	private String name;			//联系人名称
	private String mobile;			//电话号码
	private String company;			//公司名字
	private String regdate;			//注册时间
	private String lastUpdated; 	//最后修改时间
	private String origin;			//来源
	private String status;			//状态: 关联、未关联
	
	public Integer getContactId() {
		return contactId;
	}
	public void setContactId(Integer contactId) {
		this.contactId = contactId;
	}
	public Integer getAccountId() {
		return accountId;
	}
	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getEcUserId() {
		return ecUserId;
	}
	public void setEcUserId(Integer ecUserId) {
		this.ecUserId = ecUserId;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getRegdate() {
		return regdate;
	}
	public void setRegdate(String regdate) {
		this.regdate = regdate;
	}
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getLastUpdated() {
		return lastUpdated;
	}
	public void setLastUpdated(String lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	
}
