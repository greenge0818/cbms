package com.prcsteel.platform.order.model.query;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.prcsteel.platform.acl.model.model.UserOrg;

/**
 * 交易凭证审核查询条件
 *
 */
public class TradeCredentialQuery {
	private Long buyerId;
	private Long sellerId;
	private String code;  //订单号
	private Long orderId; //订单id
	private Boolean status;
	private Date startTime;
	private Date endTime;
	private Integer start;
	private Integer length;
	private Boolean check;  //true: 必须审核通过才能开票， false: 不须审核通过也能开票
	private String credentialCode;  //凭证号
	
	private List<String> orgIds;
	
	public String getCredentialCode() {
		return credentialCode;
	}
	public void setCredentialCode(String credentialCode) {
		this.credentialCode = credentialCode;
	}
	public Long getBuyerId() {
		return buyerId;
	}
	public void setBuyerId(Long buyerId) {
		this.buyerId = buyerId;
	}
	public Long getSellerId() {
		return sellerId;
	}
	public TradeCredentialQuery setSellerId(Long sellerId) {
		this.sellerId = sellerId;
		return this;
	}
	public String getCode() {
		return code;
	}
	public TradeCredentialQuery setCode(String code) {
		this.code = code;
		return this;
	}
	public Long getOrderId() {
		return orderId;
	}
	public TradeCredentialQuery setOrderId(Long orderId) {
		this.orderId = orderId;
		return this;
	}
	public Boolean getStatus() {
		return status;
	}
	public TradeCredentialQuery setStatus(Boolean status) {
		this.status = status;
		return this;
	}
	public Date getStartTime() {
		return startTime;
	}
	public TradeCredentialQuery setStartTime(Date startTime) {
		this.startTime = startTime;
		return this;
	}
	public Date getEndTime() {
		return endTime;
	}
	public TradeCredentialQuery setEndTime(Date endTime) {
		this.endTime = endTime;
		return this;
	}
	public Integer getStart() {
		return start;
	}
	public void setStart(Integer start) {
		this.start = start;
	}
	public Integer getLength() {
		return length;
	}
	public void setLength(Integer length) {
		this.length = length;
	}
	public Boolean getCheck() {
		return check;
	}
	public void setCheck(Boolean check) {
		this.check = check;
	}
	
	public List<String> getOrgIds() {
		return orgIds;
	}
	public void setOrgIds(List<String> orgIds) {
		this.orgIds = orgIds;
	}
	
	public void setOrgIds(String orgIds) {
		if(orgIds!=null){
			this.orgIds = Arrays.asList(orgIds.split(","));
		}
	}
	
}
