package com.prcsteel.platform.account.model.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dengxiyan on 2016/1/15.
 */
public class DepartmentDto {
    private Long id;
    private String name;
    /**
     * 现金余额
     */
    private BigDecimal balance;
    private List<ContactDto> contacts = new ArrayList<>();
    /**
     * 冻结资金账户金额
     */
    private BigDecimal balanceFreeze;
    /**
     * 二结账户余额
     */
    private BigDecimal balanceSecondSettlement;
    /**
     * 冻结二次结算账户金额
     */
    private BigDecimal balanceSecondSettlementFreeze;
    /**
     * 信用额度
     */
    private BigDecimal creditAmount;
    /**
     * 已用信用额度
     */
    private BigDecimal creditUsed;
    /**
     * 信用额度余额
     */
    private BigDecimal creditBalance;

    private Integer status;

    private Integer isAutoSecondPayment;

    private String fax;

    private String belongOrg; // chengui 部门归属服务中心

    private String deptFax; //chengui 部门传真

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ContactDto> getContacts() {
        return contacts;
    }

    public void setContacts(List<ContactDto> contacts) {
        this.contacts = contacts;
    }

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public BigDecimal getBalanceFreeze() {
		return balanceFreeze;
	}

	public void setBalanceFreeze(BigDecimal balanceFreeze) {
		this.balanceFreeze = balanceFreeze;
	}

	public BigDecimal getBalanceSecondSettlement() {
		return balanceSecondSettlement;
	}

	public void setBalanceSecondSettlement(BigDecimal balanceSecondSettlement) {
		this.balanceSecondSettlement = balanceSecondSettlement;
	}

	public BigDecimal getBalanceSecondSettlementFreeze() {
		return balanceSecondSettlementFreeze;
	}

	public void setBalanceSecondSettlementFreeze(
			BigDecimal balanceSecondSettlementFreeze) {
		this.balanceSecondSettlementFreeze = balanceSecondSettlementFreeze;
	}

	public BigDecimal getCreditAmount() {
		return creditAmount;
	}

	public void setCreditAmount(BigDecimal creditAmount) {
		this.creditAmount = creditAmount;
	}

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public BigDecimal getCreditUsed() {
        return creditUsed;
    }

    public void setCreditUsed(BigDecimal creditUsed) {
        this.creditUsed = creditUsed;
    }

    public BigDecimal getCreditBalance() {
        return creditBalance;
    }

    public void setCreditBalance(BigDecimal creditBalance) {
        this.creditBalance = creditBalance;
    }

    public Integer getIsAutoSecondPayment() {
        return isAutoSecondPayment;
    }

    public void setIsAutoSecondPayment(Integer isAutoSecondPayment) {
        this.isAutoSecondPayment = isAutoSecondPayment;
    }

    public String getBelongOrg() {
        return belongOrg;
    }

    public void setBelongOrg(String belongOrg) {
        this.belongOrg = belongOrg;
    }

    public String getDeptFax() {
        return deptFax;
    }

    public void setDeptFax(String deptFax) {
        this.deptFax = deptFax;
    }
}
