package com.prcsteel.platform.account.model.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by lcw on 2016/9/18.
 */
public class AccountFundAllocationsDto {
    private Long accountId;

    private BigDecimal balance;

    private String associationType;

    private String accountTransApplyType;

    private Boolean isDraftAllocation;

    private Long acceptDraftId;

    private BigDecimal acceptDraftAmount;

    private List<AccountFundAllocationsDto> allocationsDtoList;

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getAssociationType() {
        return associationType;
    }

    public void setAssociationType(String associationType) {
        this.associationType = associationType;
    }

    public String getAccountTransApplyType() {
        return accountTransApplyType;
    }

    public void setAccountTransApplyType(String accountTransApplyType) {
        this.accountTransApplyType = accountTransApplyType;
    }

    public Boolean isDraftAllocation() {
        return isDraftAllocation;
    }

    public void setIsDraftAllocation(Boolean isDraftAllocation) {
        this.isDraftAllocation = isDraftAllocation;
    }

    public Long getAcceptDraftId() {
        return acceptDraftId;
    }

    public void setAcceptDraftId(Long acceptDraftId) {
        this.acceptDraftId = acceptDraftId;
    }

    public BigDecimal getAcceptDraftAmount() {
        return acceptDraftAmount;
    }

    public void setAcceptDraftAmount(BigDecimal acceptDraftAmount) {
        this.acceptDraftAmount = acceptDraftAmount;
    }

    public List<AccountFundAllocationsDto> getAllocationsDtoList() {
        return allocationsDtoList;
    }

    public void setAllocationsDtoList(List<AccountFundAllocationsDto> allocationsDtoList) {
        this.allocationsDtoList = allocationsDtoList;
    }
}
