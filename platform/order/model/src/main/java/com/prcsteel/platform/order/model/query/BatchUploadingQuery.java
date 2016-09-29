package com.prcsteel.platform.order.model.query;

import com.prcsteel.platform.common.query.PagedQuery;

import java.util.List;

/**
 * @author wangxiao
 * @version 1.0
 * @description 批量上传凭证图片 tab页 查询对象
 * @date 2016/5/23 17:56
 */
public class BatchUploadingQuery extends PagedQuery{

    /**
     * 凭证号
     */
    private String credentialCode;

    /*****交易单号*****/
    private String code;

    /*****凭证类型：买家buyer、卖家seller*****/
    private String type;

    /*****买家名称*****/
    private String buyerName;

    /*****卖家名称*****/
    private String sellerName;


    /*****交易员Id*****/
    private String ownerId;

    /*****交易员名称*****/
    private String ownerName;

    /*****上传状态*****/
    private String status;

	private List<Long> userIds;
	public List<Long> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<Long> userIds) {
		this.userIds = userIds;
	}


	public String getCredentialCode() {
		return credentialCode;
	}

	public void setCredentialCode(String credentialCode) {
		this.credentialCode = credentialCode;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getBuyerName() {
		return buyerName;
	}

	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}

	public String getSellerName() {
		return sellerName;
	}

	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}




}
