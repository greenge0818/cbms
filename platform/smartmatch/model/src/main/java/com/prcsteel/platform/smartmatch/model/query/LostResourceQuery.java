package com.prcsteel.platform.smartmatch.model.query;

import com.prcsteel.platform.common.query.PagedQuery;

/** 
 * 缺失资源查询对象
 * @author peanut <p>2016年2月25日 上午11:26:50</p>  
 */
public class LostResourceQuery extends PagedQuery {
	/**
	 * 缺失资源来源
	 */
	private String sourceType;
	/**
	 * 区域ID
	 */
	private Long areaId;
	/**
     * 时间起
     */
    private String tStart;
    /**
     * 时间止
     */
    private String tEnd;
    
	public String getSourceType() {
		return sourceType;
	}
	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}
	public Long getAreaId() {
		return areaId;
	}
	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}
	public String gettStart() {
		return tStart;
	}
	public void settStart(String tStart) {
		this.tStart = tStart;
	}
	public String gettEnd() {
		return tEnd;
	}
	public void settEnd(String tEnd) {
		this.tEnd = tEnd;
	}
	
}
