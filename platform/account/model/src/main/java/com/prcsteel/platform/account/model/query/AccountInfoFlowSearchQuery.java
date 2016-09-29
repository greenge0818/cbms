package com.prcsteel.platform.account.model.query;


import java.util.List;

import com.prcsteel.platform.common.query.PagedQuery;

/** 
 * 账户信息流水查询 query
 * @author peanut <p>2016年3月11日 上午9:53:40</p>  
 */
public class AccountInfoFlowSearchQuery extends PagedQuery{
	/**
	 * 关联单号
	 */
	private String consignOrderCode;
	/**
	 * 账户支付类型
	 */
	private String applyType;
	/**
	 * 流水时间起
	 */
	private String startTime;
	/**
	 * 流水时间止
	 */
	private String endTime;
	/**
	 * 部门id
	 */
	private Long departmentId;
	/**
	 * 公司id
	 */
	private Long accountId;
	/**
	 * 付款类型
	 */
	private String payType;


	public String getPayType() {
		return payType;
	}


	public void setPayType(String payType) {
		this.payType = payType;
	}


	public AccountInfoFlowSearchQuery() {
		super();
	}


	public String getConsignOrderCode() {
		return consignOrderCode;
	}


	public void setConsignOrderCode(String consignOrderCode) {
		this.consignOrderCode = consignOrderCode;
	}


	public String getApplyType() {
		return applyType;
	}


	public void setApplyType(String applyType) {
		this.applyType = applyType;
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


	public Long getDepartmentId() {
		return departmentId;
	}


	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}


	public Long getAccountId() {
		return accountId;
	}


	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}
}
