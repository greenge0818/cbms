package com.prcsteel.platform.order.model.dto;

import java.math.BigDecimal;

import com.prcsteel.platform.order.model.model.ReportRebateRecord;

/**
 * @author dengxiyan
 * @version V1.0
 * @Title: ReportRebateDto
 * @Package com.prcsteel.cbms.persist.dto
 * @Description: 返利/提现流水dto
 * @date 2015/9/1
 */
public class ReportRebateDto extends ReportRebateRecord {

    /** 提现金额 */
    private BigDecimal withdrawAmount;

    /** 上期余额 */
    private BigDecimal previousPeriodBalance;

    /** 本期余额 */
    private BigDecimal thisPeriodBalance;

    /** 联系人号码 */
    private String tel;

    /** 类型 默认返利 */
    private String type = "返利";

    /** 金额减少 */
    private BigDecimal amountReduce;

    /** 金额增加 */
    private BigDecimal amountIncrease;

    public BigDecimal getWithdrawAmount() {
        return withdrawAmount;
    }

    public void setWithdrawAmount(BigDecimal withdrawAmount) {
        this.withdrawAmount = withdrawAmount;
    }

    public BigDecimal getPreviousPeriodBalance() {
        return previousPeriodBalance;
    }

    public void setPreviousPeriodBalance(BigDecimal previousPeriodBalance) {
        this.previousPeriodBalance = previousPeriodBalance;
    }

    public BigDecimal getThisPeriodBalance() {
        return thisPeriodBalance;
    }

    public void setThisPeriodBalance(BigDecimal thisPeriodBalance) {
        this.thisPeriodBalance = thisPeriodBalance;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getAmountReduce() {
        return amountReduce;
    }

    public void setAmountReduce(BigDecimal amountReduce) {
        this.amountReduce = amountReduce;
    }

    public BigDecimal getAmountIncrease() {
        return amountIncrease;
    }

    public void setAmountIncrease(BigDecimal amountIncrease) {
        this.amountIncrease = amountIncrease;
    }
}
