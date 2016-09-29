package com.prcsteel.platform.kuandao.model.dto;

import java.io.Serializable;

public class KuandaoJobCronDto implements Serializable {

	private static final long serialVersionUID = 981132584061494703L;

	private JobCronDto depositJobCron;
	
	private JobCronDto accountSyncJobCron;
	
	private JobCronDto dailyBillJobCron;

	public JobCronDto getDepositJobCron() {
		return depositJobCron;
	}

	public void setDepositJobCron(JobCronDto depositJobCron) {
		this.depositJobCron = depositJobCron;
	}

	public JobCronDto getAccountSyncJobCron() {
		return accountSyncJobCron;
	}

	public void setAccountSyncJobCron(JobCronDto accountSyncJobCron) {
		this.accountSyncJobCron = accountSyncJobCron;
	}

	public JobCronDto getDailyBillJobCron() {
		return dailyBillJobCron;
	}

	public void setDailyBillJobCron(JobCronDto dailyBillJobCron) {
		this.dailyBillJobCron = dailyBillJobCron;
	}

	
}
