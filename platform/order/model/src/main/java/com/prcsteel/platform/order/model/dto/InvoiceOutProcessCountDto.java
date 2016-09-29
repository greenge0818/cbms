package com.prcsteel.platform.order.model.dto;

/**
 * Created by Rabbit Mao on 2015/8/12.
 */
public class InvoiceOutProcessCountDto {
    Integer uncheck;  //开票资料待审核
    Integer apply;    //待申请开票
    Integer audit;    //待开票审核
    Integer waitinginvoice;   //待开票
    Integer inputinvoice;   //待录入
    Integer express;  //待寄出
    Integer confirm;  //待确认
    Integer confirmed;  //已确认

    public Integer getUncheck() {
        return uncheck;
    }

    public void setUncheck(Integer uncheck) {
        this.uncheck = uncheck;
    }

    public Integer getApply() {
        return apply;
    }

    public void setApply(Integer apply) {
        this.apply = apply;
    }

    public Integer getAudit() {
        return audit;
    }

    public void setAudit(Integer audit) {
        this.audit = audit;
    }

    public Integer getWaitinginvoice() {
        return waitinginvoice;
    }

    public void setWaitinginvoice(Integer waitinginvoice) {
        this.waitinginvoice = waitinginvoice;
    }

    public Integer getInputinvoice() {
        return inputinvoice;
    }

    public void setInputinvoice(Integer inputinvoice) {
        this.inputinvoice = inputinvoice;
    }

    public Integer getExpress() {
        return express;
    }

    public void setExpress(Integer express) {
        this.express = express;
    }

    public Integer getConfirm() {
        return confirm;
    }

    public void setConfirm(Integer confirm) {
        this.confirm = confirm;
    }

    public Integer getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(Integer confirmed) {
        this.confirmed = confirmed;
    }
}
