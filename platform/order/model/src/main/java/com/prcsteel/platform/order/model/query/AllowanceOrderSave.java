package com.prcsteel.platform.order.model.query;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.prcsteel.platform.acl.model.model.User;

public class AllowanceOrderSave {
	
	private Long id = null;
	
	private Long allowanceId = null;
	
	private String paramJson;

	private String allowanceType;
	
	private String allowanceManner;
	
	private String imgUrl = null;
	
	private String status;
	
	private User user;
	
	private Date date;

	private String oldStatus;
	
	private List<String> buyerIds;
	
	private String note;

	private List<String> buyerDeptIds;
	
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
	public List<String> getBuyerIds() {
		return buyerIds;
	}

	public void setBuyerIds(String buyerIds) {
		if("".equals(buyerIds)) {
			this.buyerIds = null;
		}else {
			this.buyerIds = Arrays.asList(buyerIds.split(","));
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAllowanceId() {
		return allowanceId;
	}

	public void setAllowanceId(Long allowanceId) {
		this.allowanceId = allowanceId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getParamJson() {
		return paramJson;
	}

	public void setParamJson(String paramJson) {
		this.paramJson = paramJson;
	}

	public String getAllowanceType() {
		return allowanceType;
	}

	public void setAllowanceType(String allowanceType) {
		this.allowanceType = allowanceType;
	}

	public String getAllowanceManner() {
		return allowanceManner;
	}

	public void setAllowanceManner(String allowanceManner) {
		this.allowanceManner = allowanceManner;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOldStatus() {
		return oldStatus;
	}

	public void setOldStatus(String oldStatus) {
		this.oldStatus = oldStatus;
	}

	public void setBuyerIds(List<String> buyerIds) {
		this.buyerIds = buyerIds;
	}

	public List<String> getBuyerDeptIds() {
		return buyerDeptIds;
	}

	public void setBuyerDeptIds(String buyerDeptIds) {
		if("".equals(buyerDeptIds)) {
			this.buyerDeptIds = null;
		}else {
			this.buyerDeptIds = Arrays.asList(buyerDeptIds.split(","));
		}
	}
}
