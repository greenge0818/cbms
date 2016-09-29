package com.prcsteel.platform.order.model.query;

import com.prcsteel.platform.common.query.PagedQuery;

import java.util.List;

/**
 * @author Green.Ge
 * @version V1.1
 * @Title: PoolInListQuery
 * @Package com.prcsteel.platform.order.model.query
 * @Description: 进项发票池清单查询model
 * @date 2015/9/18
 */
public class PoolInListQuery extends PagedQuery {
	private String material;
    private String nsortName;
	private Long orgId;
	private List<Long> ownerIdList;
    private String ownerName;
    private String parentUuid;
	private Long poolInId;
	private Long sellerId;
    private String sellerName;
    private String spec;
    private String status;
    private Boolean checkAmount;
    public String getMaterial() {
		return material;
	}
	public String getNsortName() {
		return nsortName;
	}
	public Long getOrgId() {
		return orgId;
	}
    public List<Long> getOwnerIdList() {
		return ownerIdList;
	}
	public String getOwnerName() {
		return ownerName;
	}
	public String getParentUuid() {
		return parentUuid;
	}
    public Long getPoolInId() {
		return poolInId;
	}
    public Long getSellerId() {
		return sellerId;
	}
	public String getSellerName() {
		return sellerName;
	}
	public String getSpec() {
		return spec;
	}
	public String getStatus() {
		return status;
	}
	public void setMaterial(String material) {
		this.material = material;
	}
	public void setNsortName(String nsortName) {
		this.nsortName = nsortName;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	public void setOwnerIdList(List<Long> ownerIdList) {
		this.ownerIdList = ownerIdList;
	}
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	public void setParentUuid(String parentUuid) {
		this.parentUuid = parentUuid;
	}
	public void setPoolInId(Long poolInId) {
		this.poolInId = poolInId;
	}
	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}
	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}
	public void setSpec(String spec) {
		this.spec = spec;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Boolean getCheckAmount() {
		return checkAmount;
	}
	public void setCheckAmount(Boolean checkAmount) {
		this.checkAmount = checkAmount;
	}

}
