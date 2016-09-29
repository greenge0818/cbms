package com.prcsteel.platform.order.model.query;

import com.prcsteel.platform.common.query.PagedQuery;

import java.util.Date;
import java.util.List;

/**
 * 
* @Title: OrderItemsQuery.java 
* @Package com.prcsteel.platform.order.model.query
* @Description: 销项票清单参数
* @author lixiang   
* @date 2015年12月15日 下午2:42:28 
* @version V1.0
 */
public class OrderItemsQuery extends PagedQuery {
	
	private Long sellerId;
	
    private String orgName;
    
    private String accountName;
    
    private Date startDate;
    
    private Date  endDate;
    
    private String startTime;
    
    private String endTime;
    
    private List<Long> userIds;
    
    private String code;
    
    private List<String> state;

	private String allStatus;

	public List<String> getState() {
		return state;
	}

	public void setState(List<String> state) {
		this.state = state;
	}

	public String getAllStatus() {
		return allStatus;
	}

	public void setAllStatus(String allStatus) {
		this.allStatus = allStatus;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	
	public List<Long> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<Long> userIds) {
		this.userIds = userIds;
	}

	public Long getSellerId() {
		return sellerId;
	}

	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
    
}
