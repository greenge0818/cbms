package com.prcsteel.platform.kuandao.model.dto;

import java.io.Serializable;
import java.util.List;

public class BasesetDto implements Serializable{

	private static final long serialVersionUID = 209092743805831241L;

	private KuandaoJobCronDto jobCron;
	
	private List emailList;
	
	private List mobileList;
	
	private List limitBankList;

	public KuandaoJobCronDto getJobCron() {
		return jobCron;
	}

	public void setJobCron(KuandaoJobCronDto jobCron) {
		this.jobCron = jobCron;
	}

	public List getEmailList() {
		return emailList;
	}

	public void setEmailList(List emailList) {
		this.emailList = emailList;
	}

	public List getMobileList() {
		return mobileList;
	}

	public void setMobileList(List mobileList) {
		this.mobileList = mobileList;
	}

	public List getLimitBankList() {
		return limitBankList;
	}

	public void setLimitBankList(List limitBankList) {
		this.limitBankList = limitBankList;
	}
	
}
