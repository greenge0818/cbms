package com.prcsteel.platform.order.model.query;

public class WXContractQuery {
	private String token;  //令牌
	private String openId; //微信过来的绑定号
	private String phone; //手机号码

	public WXContractQuery() {
		super();
	}
	public WXContractQuery(String token, String openId, String phone) {
		super();
		this.token = token;
		this.openId = openId;
		this.phone = phone;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
}
