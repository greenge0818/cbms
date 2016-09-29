package com.prcsteel.platform.order.model.nido;

import com.prcsteel.framework.nido.context.NidoContext;

/**
 * 
 * @author zhoukun
 */
public class AddRebateInMarketContext extends NidoContext {
	
	private static final long serialVersionUID = 6430644830124461237L;

	private String key;
	
	private Double money;
	
	private String phone;
	
	private String userName;
	
	private String companyName;
	
	private String provinceName;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	
}
