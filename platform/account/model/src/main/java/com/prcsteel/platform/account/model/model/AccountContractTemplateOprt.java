package com.prcsteel.platform.account.model.model;

import java.io.Serializable;

/**
 * @description: 对账户合同模板操作时所需要的参数对象
 * @title: AccountContractTemplateOprt.java
 * @package com.prcsteel.platform.account.model.model
 * @author peanut
 * @version V1.0
 * @date  2016年3月21日 下午3:32:32
 */
public class AccountContractTemplateOprt implements Serializable{
	/**
	 *
	 */
	private static final long serialVersionUID = 6130386287294478570L;
	/**
	 * 合同模板id
	 */
	private Long id;
	/**
	 * 模板名称
	 */
	private String name;
	/**
	 * 账户id(部门id)
	 */
	private Long accountId;
	/**
	 * 公司id
	 */
	private Long companyId;
	/**
	 * 操作类型:添加（add）,查看（view）,编辑（edit）,删除（del）
	 */
	private String action;
	/**
	 * 模板类型
	 */
	private String type;
	/**
	 * 公司名称
	 */
	private String companyName;

	public AccountContractTemplateOprt() {
	}

	public AccountContractTemplateOprt(Long id, String name, Long accountId, Long companyId, String action, String type, String companyName) {
		this.id = id;
		this.name = name;
		this.accountId = accountId;
		this.companyId = companyId;
		this.action = action;
		this.type = type;
		this.companyName = companyName;
	}

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
	public Long getAccountId() {
		return accountId;
	}
	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}
	public Long getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

}
