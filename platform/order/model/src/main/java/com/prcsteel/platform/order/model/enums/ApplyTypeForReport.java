package com.prcsteel.platform.order.model.enums;

/**
 * @author dengxiyan
 * @version V1.0
 * @Title: ApplyTypeForReport
 * @Package com.prcsteel.platform.order.model.enums
 * @Description: ApplyType 除了RELETED_CONTRACT值外 其他值和ApplyType保持一致
 * @date 2015/12/28
 */
public enum ApplyTypeForReport{
    //1,-1,3,4,8,14,15,16,17,20,21
    TO_CAPITAL_ACCOUNT("1", "充值到资金账户"),
    RELETED_CONTRACT("-1", "关联合同"),//区别于2在申请类型中是销售 新增的类型
    SECOND_PAY("3", "二次结算"),
    CAPITAL_ACCOUNT_TRANSFER("4", "资金账户转出"),
    GETONGKUAN_TO_ACCOUNT("8", "合同款到账"),
    BALANCES_UNLOCK("13", "解锁资金账户余额"),//买家关联订单后的关闭
    SECONDARY_BACK("14", "二次结算回退"),
    BALANCES_BACK("15", "资金账户回退"),
    ACCEPT_DRAFT_RECHARGE("16", "银票充值到资金账户"),
    ACCEPT_DRAFT_BACK("17", "取消已充值到资金账户的银票"),
    REBATE("20", "二次结算(折让金额)"),
    ROLLBACK_REBATE("21", "二次结算(折让金额回滚)");


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
    ApplyTypeForReport(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
