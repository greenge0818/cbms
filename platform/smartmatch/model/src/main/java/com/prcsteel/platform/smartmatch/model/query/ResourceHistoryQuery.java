package com.prcsteel.platform.smartmatch.model.query;

/**
 * 资源历史表查询
 * @author prcsteel
 *
 */
public class ResourceHistoryQuery extends ResourceQuery{
	//版本日期 
	private String versionDate;

	public String getVersionDate() {
		return versionDate;
	}

	public void setVersionDate(String versionDate) {
		this.versionDate = versionDate;
	}

}
