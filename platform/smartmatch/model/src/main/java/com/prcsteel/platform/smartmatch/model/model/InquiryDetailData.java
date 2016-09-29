package com.prcsteel.platform.smartmatch.model.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 询价详情界面展示Data
 * @author tangwei
 *
 */
public class InquiryDetailData {
	private String accountName;//客户名称
	private Long accountId;//客户id
	private String createDateStr;//创建时间
	private long date;
	private List<InquiryDetailContact> contactList = new ArrayList<InquiryDetailContact>();//联系人列表
	private List<InquiryDetailRes> resList = new ArrayList<InquiryDetailRes>();//资源列表
	
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public Long getAccountId() {
		return accountId;
	}
	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}
	public String getCreateDateStr() {
		return createDateStr;
	}
	public void setCreateDateStr(String createDateStr) {
		this.createDateStr = createDateStr;
	}
	public long getDate() {
		return date;
	}
	public void setDate(long date) {
		this.date = date;
	}
	public List<InquiryDetailContact> getContactList() {
		return contactList;
	}
	public void setContactList(List<InquiryDetailContact> contactList) {
		this.contactList = contactList;
	}
	public List<InquiryDetailRes> getResList() {
		return resList;
	}
	public void setResList(List<InquiryDetailRes> resList) {
		this.resList = resList;
	}
}
