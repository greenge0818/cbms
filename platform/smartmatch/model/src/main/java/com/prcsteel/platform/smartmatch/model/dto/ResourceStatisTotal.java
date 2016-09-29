package com.prcsteel.platform.smartmatch.model.dto;

/**
 * 资源更新统计 
 * 
 * @author tangwei
 *
 */
public class ResourceStatisTotal {
	
	private int total;//总记录数
	
	private int dailyTotalCount;//日常资源总更新条数
	
	private int inquiryTotalCount;//询价资源总更新条数
	
	private int historyTotalCount;//历史成交资源总更新条数

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getDailyTotalCount() {
		return dailyTotalCount;
	}

	public void setDailyTotalCount(int dailyTotalCount) {
		this.dailyTotalCount = dailyTotalCount;
	}

	public int getInquiryTotalCount() {
		return inquiryTotalCount;
	}

	public void setInquiryTotalCount(int inquiryTotalCount) {
		this.inquiryTotalCount = inquiryTotalCount;
	}

	public int getHistoryTotalCount() {
		return historyTotalCount;
	}

	public void setHistoryTotalCount(int historyTotalCount) {
		this.historyTotalCount = historyTotalCount;
	}
}
