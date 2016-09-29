package com.prcsteel.platform.order.model.enums;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author lixiang
 * @version V1.0
 * @Title: ApplyType.java
 * @Package com.prcsteel.cbms.persist.model
 * @date 2015年7月26日 下午17:22:27
 */

public enum ApplyType {

    TO_CAPITAL_ACCOUNT("1", "充值到资金账户"),
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
    ADVANCE_PAYMENT("22", "二结账户(预付款)"),
    SYS_DEAL("0", "系统平账处理");

    // 成员变量
    private String code;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    // 构造方法
    ApplyType(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getNameByCode(String code) {
        Optional<ApplyType> res = Stream.of(ApplyType.values()).filter(a -> a.getCode().equals(code)).findFirst();
        return res.isPresent() ? res.get().getName() : "";
    }

    /**
     * 返回往来单位账务报表申请列表集合
     * 1,2,3,4,8,14,15,16,17,20,21
     *
     * @return
     */
    public static List<String> getReportAccountFinancialApplyTypeList() {
        return Arrays.asList(TO_CAPITAL_ACCOUNT.getCode(), PAY_THE_CONTRACT_PAYMENT.getCode(), SECOND_PAY.getCode()
                , CAPITAL_ACCOUNT_TRANSFER.getCode(), GETONGKUAN_TO_ACCOUNT.getCode(), SECONDARY_BACK.getCode()
                , BALANCES_BACK.getCode(), ACCEPT_DRAFT_RECHARGE.getCode(), ACCEPT_DRAFT_BACK.getCode()
                , REBATE.getCode(), ROLLBACK_REBATE.getCode());
    }

}