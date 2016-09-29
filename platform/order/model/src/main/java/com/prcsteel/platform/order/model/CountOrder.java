package com.prcsteel.platform.order.model;



public class CountOrder {

    private  Integer  allSum;//交易员

    private Integer  relatedSum;//交易员手机号

    private Integer secondSettleSum;//交货地

    private Integer finishSum;

    private Integer closedSum;

    public Integer getAllSum() {
        return allSum;
    }

    public void setAllSum(Integer allSum) {
        this.allSum = allSum;
    }

    public Integer getRelatedSum() {
        return relatedSum;
    }

    public void setRelatedSum(Integer relatedSum) {
        this.relatedSum = relatedSum;
    }

    public Integer getSecondSettleSum() {
        return secondSettleSum;
    }

    public void setSecondSettleSum(Integer secondSettleSum) {
        this.secondSettleSum = secondSettleSum;
    }

    public Integer getFinishSum() {
        return finishSum;
    }

    public void setFinishSum(Integer finishSum) {
        this.finishSum = finishSum;
    }

    public Integer getClosedSum() {
        return closedSum;
    }

    public void setClosedSum(Integer closedSum) {
        this.closedSum = closedSum;
    }
}