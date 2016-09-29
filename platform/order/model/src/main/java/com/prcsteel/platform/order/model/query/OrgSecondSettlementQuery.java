package com.prcsteel.platform.order.model.query;

import com.prcsteel.platform.common.query.PagedQuery;

import java.util.Date;
import java.util.List;

/**
 * 服务中心二次结算储备金日报
 * 
 * @author lixiang
 *
 */
public class OrgSecondSettlementQuery extends PagedQuery {

	private List<String> applyTypes;// 申请类型集合

	private String serialTimeStr;// 流水时间

	private Date serialTimeStart;// 流水开始时间

	private Date serialTimeEnd;// 流水终止时间

	private List<Long> userIds;// 当前用户所能查看用户数据的ID集合

	private List<Long> orgIds;// 服务中心ID集合

	public String getSerialTimeStr() {
		return serialTimeStr;
	}

	public void setSerialTimeStr(String serialTimeStr) {
		this.serialTimeStr = serialTimeStr;
	}

	public List<String> getApplyTypes() {
		return applyTypes;
	}

	public void setApplyTypes(List<String> applyTypes) {
		this.applyTypes = applyTypes;
	}

	public List<Long> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<Long> userIds) {
		this.userIds = userIds;
	}

	public List<Long> getOrgIds() {
		return orgIds;
	}

	public void setOrgIds(List<Long> orgIds) {
		this.orgIds = orgIds;
	}

	public Date getSerialTimeStart() {
		return serialTimeStart;
	}

	public void setSerialTimeStart(Date serialTimeStart) {
		this.serialTimeStart = serialTimeStart;
	}

	public Date getSerialTimeEnd() {
		return serialTimeEnd;
	}

	public void setSerialTimeEnd(Date serialTimeEnd) {
		this.serialTimeEnd = serialTimeEnd;
	}

}
