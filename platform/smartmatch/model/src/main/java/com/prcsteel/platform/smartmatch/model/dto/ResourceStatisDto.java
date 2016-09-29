package com.prcsteel.platform.smartmatch.model.dto;


/**
 * 资源更新统计列表数据
 * @author tangwei
 *
 */
public class ResourceStatisDto {
	
	private Long accountId;
	
	private String name;//卖方名称
	
	private int dailyCount;//日常资源更新条数
	
	private int inquiryCount;//询价资源更新条数
	
	private int historyCount;//历史成交资源更新条数
	
	private ResourceStatisTotal statisTotal;// 统计汇总对象

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getDailyCount() {
		return dailyCount;
	}

	public void setDailyCount(int dailyCount) {
		this.dailyCount = dailyCount;
	}

	public int getInquiryCount() {
		return inquiryCount;
	}

	public void setInquiryCount(int inquiryCount) {
		this.inquiryCount = inquiryCount;
	}

	public int getHistoryCount() {
		return historyCount;
	}

	public void setHistoryCount(int historyCount) {
		this.historyCount = historyCount;
	}

	public ResourceStatisTotal getStatisTotal() {
		return statisTotal;
	}

	public void setStatisTotal(ResourceStatisTotal statisTotal) {
		this.statisTotal = statisTotal;
	}
}
