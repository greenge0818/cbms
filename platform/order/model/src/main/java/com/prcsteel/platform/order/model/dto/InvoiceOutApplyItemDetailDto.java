package com.prcsteel.platform.order.model.dto;

import java.math.BigDecimal;

import com.prcsteel.platform.order.model.model.InvoiceOutItemDetail;

/** 开票申请详情项Dto
 * Created by lcw36 on 2015/9/15.
 */
public class InvoiceOutApplyItemDetailDto extends InvoiceOutItemDetail {
    private Long ownerId;
    private Long buyerId;
    private String buyerName;
    private Long applyId;
    private Long orgId;
    private String orgName;
    private Long orderId;
    private String code;
    
    //tuxianming 20160513
    private BigDecimal balanceSecondSettlement; //二次结算欠款
    private Boolean credentialStatus;		//凭证审核状态
    private String credentialStatusStr;	//凭证审核状态: 审核通过，审核未通过
    
    private String sellerCredentialCode;		//卖家凭证
    private String sellerCredentialStatus;		//卖家凭证状态
    private String batchSellerCredentialCode;	//批量卖家凭证
    private String batchSellerCredentialStatus;	//批量卖家凭证状态
    private String buyerCredentialCode;			//买家凭证
    private String buyerCredentialStatus;		//买家凭证状态
    private String batchBuyerCredentialCode;	//批量买家凭证
    private String batchBuyerCredentialStatus;	//批量买家凭证状态
    private String buyerCheckValue;				//买家是不是需要审核： 0：不需要审核通过， 1： 必须审核通过 
    private String sellerCheckValue;			//卖家是不是需要审核： 0：不需要审核通过， 1： 必须审核通过 
    //end tuxianming
    
    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }
    public Long getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(Long buyerId) {
        this.buyerId = buyerId;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public Long getApplyId() {
        return applyId;
    }

    public void setApplyId(Long applyId) {
        this.applyId = applyId;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

	public BigDecimal getBalanceSecondSettlement() {
		return balanceSecondSettlement;
	}

	public void setBalanceSecondSettlement(BigDecimal balanceSecondSettlement) {
		this.balanceSecondSettlement = balanceSecondSettlement;
	}

	public Boolean getCredentialStatus() {
		return credentialStatus;
	}

	public void setCredentialStatus(Boolean credentialStatus) {
		this.credentialStatus = credentialStatus;
	}

	public String getSellerCredentialCode() {
		return sellerCredentialCode;
	}

	public void setSellerCredentialCode(String sellerCredentialCode) {
		this.sellerCredentialCode = sellerCredentialCode;
	}

	public String getSellerCredentialStatus() {
		return sellerCredentialStatus;
	}

	public void setSellerCredentialStatus(String sellerCredentialStatus) {
		this.sellerCredentialStatus = sellerCredentialStatus;
	}

	public String getBatchSellerCredentialCode() {
		return batchSellerCredentialCode;
	}

	public void setBatchSellerCredentialCode(String batchSellerCredentialCode) {
		this.batchSellerCredentialCode = batchSellerCredentialCode;
	}

	public String getBatchSellerCredentialStatus() {
		return batchSellerCredentialStatus;
	}

	public void setBatchSellerCredentialStatus(String batchSellerCredentialStatus) {
		this.batchSellerCredentialStatus = batchSellerCredentialStatus;
	}

	public String getBuyerCredentialCode() {
		return buyerCredentialCode;
	}

	public void setBuyerCredentialCode(String buyerCredentialCode) {
		this.buyerCredentialCode = buyerCredentialCode;
	}

	public String getBuyerCredentialStatus() {
		return buyerCredentialStatus;
	}

	public void setBuyerCredentialStatus(String buyerCredentialStatus) {
		this.buyerCredentialStatus = buyerCredentialStatus;
	}

	public String getBatchBuyerCredentialCode() {
		return batchBuyerCredentialCode;
	}

	public void setBatchBuyerCredentialCode(String batchBuyerCredentialCode) {
		this.batchBuyerCredentialCode = batchBuyerCredentialCode;
	}

	public String getBatchBuyerCredentialStatus() {
		return batchBuyerCredentialStatus;
	}

	public void setBatchBuyerCredentialStatus(String batchBuyerCredentialStatus) {
		this.batchBuyerCredentialStatus = batchBuyerCredentialStatus;
	}

	public String getBuyerCheckValue() {
		return buyerCheckValue;
	}

	public void setBuyerCheckValue(String buyerCheckValue) {
		this.buyerCheckValue = buyerCheckValue;
	}

	public String getSellerCheckValue() {
		return sellerCheckValue;
	}

	public void setSellerCheckValue(String sellerCheckValue) {
		this.sellerCheckValue = sellerCheckValue;
	}

	public String getCredentialStatusStr() {
		return credentialStatusStr;
	}

	public void setCredentialStatusStr(String credentialStatusStr) {
		this.credentialStatusStr = credentialStatusStr;
	}

}
