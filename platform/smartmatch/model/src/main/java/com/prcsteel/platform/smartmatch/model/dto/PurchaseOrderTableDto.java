package com.prcsteel.platform.smartmatch.model.dto;

import java.util.Date;

/**
 * Created by prcsteel on 2015/11/27.
 */
public class PurchaseOrderTableDto {
    private Long id;
    private String code;
    private String status;
    private String buyerName;
    private Date createdTime;
    private String contact;
    private String tel;
    private String remark;
    private String quoRemark;
    private String deliveryCityName;
    private String purchaseCitys;
    private String ownerName;
    private String quotationOrderCreatedTime;
    private Integer pushNumber;
    //modify by zhoucai@presteel.com 新增需求单号 20160818
    private String requirementCode;

    public String getRequirementCode() {
        return requirementCode;
    }

    public void setRequirementCode(String requirementCode) {
        this.requirementCode = requirementCode;
    }



    public Integer getPushNumber() {
		return pushNumber;
	}

	public void setPushNumber(Integer pushNumber) {
		this.pushNumber = pushNumber;
	}

	public String getQuotationOrderCreatedTime() {
        return quotationOrderCreatedTime;
    }

    public void setQuotationOrderCreatedTime(String quotationOrderCreatedTime) {
        this.quotationOrderCreatedTime = quotationOrderCreatedTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCode() {
        return code;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public String getContact() {
        return contact;
    }

    public String getTel() {
        return tel;
    }

    public String getRemark() {
        return remark;
    }

    public String getQuoRemark() {
        return quoRemark;
    }

    public void setQuoRemark(String quoRemark) {
        this.quoRemark = quoRemark;
    }

    public String getDeliveryCityName() {
        return deliveryCityName;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDeliveryCityName(String deliveryCityName) {
        this.deliveryCityName = deliveryCityName;
    }

    public String getPurchaseCitys() {
        return purchaseCitys;
    }

    public void setPurchaseCitys(String purchaseCitys) {
        this.purchaseCitys = purchaseCitys;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
}
