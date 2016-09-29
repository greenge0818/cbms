package com.prcsteel.platform.order.model.dto;

import java.math.BigDecimal;

/**
 * Created by caochao on 2015/7/27.
 */
public class ConsignOrderSellerInfoDto {
    private Long contractId;
    private Long sellerId;
    private String companyName;
    //add by rabbit增加部门信息
    private Long sellerDepartmentId;
    private String sellerDepartmentName;
    private String mobile;
    private String address;
    //green add for app
    private String tel;
    private String regAddress;
    
    private String bankNameMain;
    private String bankNameBranch;
    private String bankCode;
    private BigDecimal orderAmount;
    private BigDecimal contractAmount;// 合同金额
    private BigDecimal balanceSecondSettlement;// 总金额
    private BigDecimal creditUsed;
    private Integer contractStatus;
    private BigDecimal pedingAmount;//待付款金额
    private Long orderId;
    
    public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public BigDecimal getPedingAmount() {
		return pedingAmount;
	}

	public void setPedingAmount(BigDecimal pedingAmount) {
		this.pedingAmount = pedingAmount;
	}

	public BigDecimal getContractAmount() {
		return contractAmount;
	}

	public void setContractAmount(BigDecimal contractAmount) {
		this.contractAmount = contractAmount;
	}

	public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Long getSellerDepartmentId() {
        return sellerDepartmentId;
    }

    public void setSellerDepartmentId(Long sellerDepartmentId) {
        this.sellerDepartmentId = sellerDepartmentId;
    }

    public String getSellerDepartmentName() {
        return sellerDepartmentName;
    }

    public void setSellerDepartmentName(String sellerDepartmentName) {
        this.sellerDepartmentName = sellerDepartmentName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBankNameMain() {
        return bankNameMain;
    }

    public void setBankNameMain(String bankNameMain) {
        this.bankNameMain = bankNameMain;
    }

    public String getBankNameBranch() {
        return bankNameBranch;
    }

    public void setBankNameBranch(String bankNameBranch) {
        this.bankNameBranch = bankNameBranch;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public BigDecimal getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }

    public BigDecimal getBalanceSecondSettlement() {
        return balanceSecondSettlement;
    }

    public void setBalanceSecondSettlement(BigDecimal balanceSecondSettlement) {
        this.balanceSecondSettlement = balanceSecondSettlement;
    }

    public Integer getContractStatus() {
        return contractStatus;
    }

    public void setContractStatus(Integer contractStatus) {
        this.contractStatus = contractStatus;
    }

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getRegAddress() {
		return regAddress;
	}

	public void setRegAddress(String regAddress) {
		this.regAddress = regAddress;
	}

    public BigDecimal getCreditUsed() {
        return creditUsed;
    }

    public void setCreditUsed(BigDecimal creditUsed) {
        this.creditUsed = creditUsed;
    }

}
