package com.prcsteel.platform.order.model.query;

import com.prcsteel.platform.common.query.PagedQuery;

import java.util.Date;
import java.util.List;

/**
 * 
* @Title: InvoiceDetailQuery.java 
* @Package com.prcsteel.platform.order.model.query
* @Description: 待收票详情参数
* @author lixiang   
* @date 2015年12月11日 下午3:42:28 
* @version V1.0
 */
public class InvoiceDetailQuery extends PagedQuery {
	
	private Long sellerId;

	private Long departmentId;
	
    private String orgName;
    
    private String accountName;
    
    private Date startDate;
    
    private Date  endDate;
    
    private String startTime;
    
    private String endTime;
    
    private List<Long> ownerIdList;
    
    private String code;
    
    private String nsortName;//品名

    private String material;//材质

    private String spec;//规格

    private String uuid;
    
    private List<Long> orgIdList;//服务中心id集合

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<Long> getOwnerIdList() {
		return ownerIdList;
	}

	public void setOwnerIdList(List<Long> ownerIdList) {
		this.ownerIdList = ownerIdList;
	}

	public Long getSellerId() {
		return sellerId;
	}

	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getNsortName() {
		return nsortName;
	}

	public void setNsortName(String nsortName) {
		this.nsortName = nsortName;
	}

	public String getMaterial() {
		return material;
	}

	public void setMaterial(String material) {
		this.material = material;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public List<Long> getOrgIdList() {
		return orgIdList;
	}

	public void setOrgIdList(List<Long> orgIdList) {
		this.orgIdList = orgIdList;
	}
    
}
