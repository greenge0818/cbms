package com.prcsteel.platform.account.model.enums;

import java.util.EnumSet;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * 
 * @author zhoukun
 */
public enum AccountTransApplyType {
	CHARGE("1", "充值"),//两种情况 ：1、资金直接充值到公司资金账户  2、公司单部门情况下先充到公司账户再划转至部门账户
    PAY_THE_CONTRACT_PAYMENT("2", "支付合同货款"),
    SECOND_PAY("3", "二次结算"),
    CAPITAL_ACCOUNT_TRANSFER("4", "资金账户转出"),
    SECONDARY_SETTLEMENT_ACCOUNT_BALANCES("5", "抵扣二次结算账户欠款"),
    INTO_THE_CAPITAL_ACCOUNT("6", "二次结算账户余额转入资金账户"),
    GETONGKUAN_TO_ACCOUNT("8", "合同款到账"),
    SECONDARY_SETTLEMENT_LOCK("10", "锁定二次结算账户余额"),
    SECONDARY_SETTLEMENT_UNLOCK("11", "解锁二次结算账户余额"),
    BALANCES_LOCK("12", "锁定资金账户余额"),
    BALANCES_UNLOCK("13", "解锁资金账户余额"),
    SECONDARY_BACK("14", "二次结算回退"),
    BALANCES_BACK("15", "资金账户回退"),
    ACCEPT_DRAFT_RECHARGE("16", "银票充值到资金账户"),
    ACCEPT_DRAFT_BACK("17", "取消已充值到资金账户的银票"),
    ACCEPT_DRAFT_LOCK("18", "锁定资金账户余额"),
    ACCEPT_DRAFT_UNLOCK("19", "解锁资金账户余额"),
    REBATE("20", "二次结算(折让金额)"),
    ROLLBACK_REBATE("21", "二次结算(折让金额回滚)"),
    ADVANCE_PAYMENT("22","二结账户(预付款)"),
    SYS_DEAL("0", "系统平账处理"),

    CREDITLIMIT_TRANSTO_ACCOUNT("23","信用额度金额划转到资金账户"),
    DEPARMONEY_BACKTO_COMPANY("24","部门资金撤回到公司"),
    COMPANYMONEY_TRANSTO_DEPART("25","公司资金划拨到部门"),
    DOWN_CREDITLIMI("26","下调信用额度"),
    UP_CREDITLIMI("27","上调信用额度"),
    CREDITLIMI_TREPAYMENT("28","信用额度还款"),

    CREDITLIMI_BACK("29","信用额度回退"),
    KUANDAO_PAYMENT("30","款道充值");



    private String code;
    private String name;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    AccountTransApplyType(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getName(String code) {
        for (AccountTransApplyType item : EnumSet.allOf(AccountTransApplyType.class)) {
            if (item.getCode().equals(code))
                return item.getName();
        }
        return "";
    }

    public static AccountTransApplyType getItemByCode(String code) {
        Optional<AccountTransApplyType> res = Stream.of(AccountTransApplyType.values()).filter(a -> a.getCode().equals(code)).findFirst();
        return res.isPresent() ? res.get() : null;
    }
}

