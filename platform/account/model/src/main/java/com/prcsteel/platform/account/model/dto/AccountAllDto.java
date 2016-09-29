package com.prcsteel.platform.account.model.dto;

/**
 * 查询所有卖家时，返回所有卖家的ID与名称
 * 
 * @author yjx
 *
 */
public class AccountAllDto {

	private Long id;

	private String name;

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
}
