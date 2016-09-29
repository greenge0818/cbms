package com.prcsteel.platform.account.model.dto;

/**
 * Created by lixiang on 2015/7/16.
 */
public class AccountAssignDto {
	private Long id;
	private String name;
	private Long managerId;
	private String uName;
	private String type;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getManagerId() {
		return managerId;
	}

	public void setManagerId(Long managerId) {
		this.managerId = managerId;
	}

	public String getuName() {
		return uName;
	}

	public void setuName(String uName) {
		this.uName = uName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type == null ? null : type.trim();
	}
	
	public static void main(String[] args) {
		Integer a = 126;
		Integer b = 126;
		System.out.println(a==b);
	}
}
