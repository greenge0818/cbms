package com.prcsteel.platform.order.model;

import java.io.Serializable;

import com.prcsteel.platform.acl.model.model.User;

public class AppUser implements Serializable{

	private static final long serialVersionUID = 1L;
	String authToken;
	String deviceNo;
	String deviceType;
	User user;

	public String getDeviceNo() {
		return deviceNo;
	}

	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	public User getUser() {
		return user;
	}
	public void setDeviceNo(String deviceNo) {
		this.deviceNo = deviceNo;
	}
	
	public void setUser(User user) {
		this.user = user;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
}
