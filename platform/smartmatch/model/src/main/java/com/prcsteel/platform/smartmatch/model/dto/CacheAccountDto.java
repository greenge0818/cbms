package com.prcsteel.platform.smartmatch.model.dto;

import java.io.Serializable;

/**
 * 缓存客户信息实体
 * @author tangwei
 *
 */
public class CacheAccountDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8261458419102767300L;
	
	private Long accountId;
	private String accountName;//客户名称
	private Long accountTag;//客户标记
	private String contactName;//客户联系人(多个以","号分割)
	private String contactTel;//客户联系人电话(多个以","号分割)
	
	public Long getAccountId() {
		return accountId;
	}
	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public Long getAccountTag() {
		return accountTag;
	}
	public void setAccountTag(Long accountTag) {
		this.accountTag = accountTag;
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
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
