package com.prcsteel.platform.order.model.changecontract.dto;

import java.util.Date;
import java.util.List;

/**
 * Created by prcsteel on 2016/8/11.
 */
public class ChangeOrderDto {
    private String code;

    private String accountName;

    private String sellerName;

    private String ownerName;

    private String startTime;

    private String endTime;

    private Date beginTime;

    private Date eTime;

    private String alterStatus;

    private Boolean isPayedByAcceptDraft;  //是否为银票支付：  null：不作用条件,  0： 现金支付, 1： 银票支付

    private Integer start;

    private Integer length;

    private List alterStatusList;

    private Boolean canModify;//二结以后修改订单

    public Boolean isCanModify() {
        return canModify;
    }

    public void setCanModify(Boolean canModify) {
        this.canModify = canModify;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public List getAlterStatusList() {
        return alterStatusList;
    }

    public void setAlterStatusList(List alterStatusList) {
        this.alterStatusList = alterStatusList;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date geteTime() {
        return eTime;
    }

    public void seteTime(Date eTime) {
        this.eTime = eTime;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Boolean isPayedByAcceptDraft() {
        return isPayedByAcceptDraft;
    }

    public void setIsPayedByAcceptDraft(Boolean isPayedByAcceptDraft) {
        this.isPayedByAcceptDraft = isPayedByAcceptDraft;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getAlterStatus() {
        return alterStatus;
    }

    public void setAlterStatus(String alterStatus) {
        this.alterStatus = alterStatus;
    }
}
