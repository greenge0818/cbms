package com.prcsteel.platform.smartmatch.model.dto;

import java.util.Date;
import java.util.List;

/** 
 * 资源批量操作query
 * @author  peanut
 * @date 创建时间：2015年12月7日 下午7:55:41   
 */
public class ResourceBatchDto {
	/*** id集***/
	private List<Long> ids;
	/*** 调整方式***/
	private String adjustWay;
	/*** 调整值***/
	private String adjustValue;
	/*** 调整列***/
	private String column;
	
	//add by caosulin 2016.6.3 增加时间字段，记录调价的更新时间和人
	//最后资源更新时间
	private Date lastUpdated;
	//最后资源更新人
	private String lastUpdatedBy;
	// 资源管理更新时间
	private Date mgtLastUpdated;
	// 资源管理更新人
	private String mgtLastUpdatedBy;
	
	public List<Long> getIds() {
		return ids;
	}
	public void setIds(List<Long> ids) {
		this.ids = ids;
	}
	public String getAdjustWay() {
		return adjustWay;
	}
	public void setAdjustWay(String adjustWay) {
		this.adjustWay = adjustWay;
	}
	public String getAdjustValue() {
		return adjustValue;
	}
	public void setAdjustValue(String adjustValue) {
		this.adjustValue = adjustValue;
	}
	public String getColumn() {
		return column;
	}
	public void setColumn(String column) {
		this.column = column;
	}
	public Date getLastUpdated() {
		return lastUpdated;
	}
	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}
	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}
	public Date getMgtLastUpdated() {
		return mgtLastUpdated;
	}
	public void setMgtLastUpdated(Date mgtLastUpdated) {
		this.mgtLastUpdated = mgtLastUpdated;
	}
	public String getMgtLastUpdatedBy() {
		return mgtLastUpdatedBy;
	}
	public void setMgtLastUpdatedBy(String mgtLastUpdatedBy) {
		this.mgtLastUpdatedBy = mgtLastUpdatedBy;
	}
	
	
}
