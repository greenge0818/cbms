package com.prcsteel.platform.account.model.query;

import com.prcsteel.platform.common.query.PagedQuery;

import java.util.Date;
import java.util.List;

/**
 * 买家账户报表
 * 
 * @author lixiang
 *
 */
public class AccountStateQuery extends PagedQuery {

	private String dateStartStr;// 起始时间

	private Date dateStart;

	private String dateEndStr;// 终止时间

	private Date dateEnd;

	private String accountName;// 客户名称

	private String orgName;// 服务中心

	private Long accountId;// 客户ID

	private String id;

	private String type; // 类型，买家还是卖家

	private List<Long> userIds; // 当前用户所能查看用户数据的ID集合
	
	private String setToStatus;// 设置成状态
	
	private String applyType;//申请类型
	
	private List<Long> accountIds;
	
	public List<Long> getAccountIds() {
		return accountIds;
	}

	public void setAccountIds(List<Long> accountIds) {
		this.accountIds = accountIds;
	}

	public String getSetToStatus() {
		return setToStatus;
	}

	public void setSetToStatus(String setToStatus) {
		this.setToStatus = setToStatus;
	}

	public String getApplyType() {
		return applyType;
	}

	public void setApplyType(String applyType) {
		this.applyType = applyType;
	}

	public List<Long> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<Long> userIds) {
		this.userIds = userIds;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public String getDateStartStr() {
		return dateStartStr;
	}

	public void setDateStartStr(String dateStartStr) {
		this.dateStartStr = dateStartStr;
	}

	public Date getDateStart() {
		return dateStart;
	}

	public void setDateStart(Date dateStart) {
		this.dateStart = dateStart;
	}

	public String getDateEndStr() {
		return dateEndStr;
	}

	public void setDateEndStr(String dateEndStr) {
		this.dateEndStr = dateEndStr;
	}

	public Date getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(Date dateEnd) {
		this.dateEnd = dateEnd;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public Long getAccountId() {
		return accountId;
	}

}
