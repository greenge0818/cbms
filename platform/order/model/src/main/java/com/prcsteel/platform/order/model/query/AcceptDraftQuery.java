package com.prcsteel.platform.order.model.query;

import com.prcsteel.platform.common.query.PagedQuery;

/**
 * 票据查询
 * 
 * @author Rabbit
 *
 */
public class AcceptDraftQuery extends PagedQuery {

	private String dateStartStr;// 起始时间

	private String dateEndStr;// 终止时间

	private Long accountId;//客户ID

	private String accountName;// 客户名称

	private String status;// 状态

	private String code; //银票号

	private Long orgId;  //服务中心

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDateStartStr() {
		return dateStartStr;
	}

	public void setDateStartStr(String dateStartStr) {
		this.dateStartStr = dateStartStr;
	}

	public String getDateEndStr() {
		return dateEndStr;
	}

	public void setDateEndStr(String dateEndStr) {
		this.dateEndStr = dateEndStr;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

}
