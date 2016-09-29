package com.prcsteel.platform.smartmatch.model.dto;

import java.util.Date;

/** 
 * 历史资源数据dto
 * @author peanut <p>2016年2月18日 下午1:58:54</p>  
 */
public class HistoryResourceDto {
	
	/**** 序号  *****/
	private  Long no;
	
	/**** 卖家ID  *****/
	private  Long accountId;
	
	/**** 卖家名称  *****/
	private  String accountName;
	
	/**** 资源条数  *****/
	private  Long resourceCount;
	
	/**** 日常正常更新条数  *****/
	private  Long dailyNormalUpdateCount;
	
	/**** 日常询价更新条数  *****/
	private  Long dailyInqueryUpdateCount;
	
	/**** 日常询价更新时间  *****/
	private Date lastUpdated;
	
	/**** 日常正常更新时间  *****/
	private Date mgtLastUpdated;
	
	
	public HistoryResourceDto() {
		super();
	}
	
	public Long getNo() {
		return no;
	}
	public void setNo(Long no) {
		this.no = no;
	}
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
	public Long getDailyNormalUpdateCount() {
		return dailyNormalUpdateCount;
	}
	public void setDailyNormalUpdateCount(Long dailyNormalUpdateCount) {
		this.dailyNormalUpdateCount = dailyNormalUpdateCount;
	}
	public Long getDailyInqueryUpdateCount() {
		return dailyInqueryUpdateCount;
	}
	public void setDailyInqueryUpdateCount(Long dailyInqueryUpdateCount) {
		this.dailyInqueryUpdateCount = dailyInqueryUpdateCount;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public Date getMgtLastUpdated() {
		return mgtLastUpdated;
	}

	public void setMgtLastUpdated(Date mgtLastUpdated) {
		this.mgtLastUpdated = mgtLastUpdated;
	}

	public Long getResourceCount() {
		return resourceCount;
	}

	public void setResourceCount(Long resourceCount) {
		this.resourceCount = resourceCount;
	}
	
}
