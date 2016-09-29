package com.prcsteel.platform.smartmatch.model.model;

/**
 *  询价详情联系人信息
 * @author tangwei
 *
 */
public class InquiryDetailContact {
	private String contactName;//联系人姓名
	private String contactTel;//联系人电话
	private Long accountId;//客户id
	
	public InquiryDetailContact(String contactName,String contactTel,Long accountId){
		this.contactName = contactName;
		this.contactTel = contactTel;
	}
	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	public String getContactTel() {
		return contactTel;
	}
	public void setContactTel(String contactTel) {
		this.contactTel = contactTel;
	}
	public Long getAccountId() {
		return accountId;
	}
	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}
}
