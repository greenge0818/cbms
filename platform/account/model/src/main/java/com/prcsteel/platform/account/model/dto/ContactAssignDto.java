package com.prcsteel.platform.account.model.dto;

import com.prcsteel.platform.account.model.enums.AccountTag;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 
 * Created by lixiang on 2015/7/19.
 *
 */
public class ContactAssignDto {
	private Long cid;

	private Long accountId;

	private String bName;

	private String bTel;

	private Long manager;

	private Long aid;

	private Long uid;

	private String uName;

	private String deptName;
	
	private String accountName;
	
	private Long accountTag;

	
	public Long getCid() {
		return cid;
	}

	public void setCid(Long cid) {
		this.cid = cid;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public String getbName() {
		return bName;
	}

	public void setbName(String bName) {
		this.bName = bName;
	}

	public String getbTel() {
		return bTel;
	}

	public void setbTel(String bTel) {
		this.bTel = bTel;
	}

	public Long getManager() {
		return manager;
	}

	public void setManager(Long manager) {
		this.manager = manager;
	}

	public Long getAid() {
		return aid;
	}

	public void setAid(Long aid) {
		this.aid = aid;
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
	
	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
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

	public String getTypeName(){
		Long code = getAccountTag();
		if (code != null && code > 0){
	        List<AccountTag> tags = AccountTag.getNameByCode(code);
	        tags.remove(AccountTag.temp);
	        tags.remove(AccountTag.consign);
	        return tags.stream().map(a->a.getName()).collect(Collectors.joining(","));
        }
        return "";
	}

}
