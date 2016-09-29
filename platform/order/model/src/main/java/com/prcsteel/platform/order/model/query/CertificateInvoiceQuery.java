package com.prcsteel.platform.order.model.query;

import com.prcsteel.platform.common.query.PagedQuery;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;

/**
 * @author peanut
 * @version 1.0
 * @description 需补齐买、卖家凭证的已开票订单查询对象
 * @date 2016/4/12 9:27
 */
public class CertificateInvoiceQuery extends PagedQuery{

    /**
     * 凭证号
     */
    private String credentialCode;

    /*****交易单号*****/
    private String code;

    /*****类型：买家buyer、卖家seller*****/
    private String type;

    /*****买家名称*****/
    private String buyerName;

    /*****卖家名称*****/
    private String sellerName;


    /*****交易员Id*****/
    private String ownerId;

    /*****交易员名称*****/
    private String ownerName;

    /*****凭证状态*****/
    private String status;

    /*****凭证创建时间起*****/
    private String credentialStartTime;

    /*****凭证创建时间止*****/
    private String credentialEndTime;

    /*****开单时间起*****/
    private String startTime;

    /*****开单时间止*****/
    private String endTime;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getCredentialCode() { return credentialCode;}

    public String getBuyerName() { return buyerName; }

    public void setBuyerName(String buyerName) {this.buyerName = buyerName;}

    public String getSellerName() { return sellerName;}

    public void setSellerName(String sellerName) {this.sellerName = sellerName;}

    public void setCredentialCode(String credentialCode) {this.credentialCode = credentialCode;}

    public String getOwnerId() {return ownerId; }

    public void setOwnerId(String ownerId) { this.ownerId = ownerId; }

    public String getStatus() { return status;}

    public void setStatus(String status) {this.status = status; }

    public String getCredentialStartTime() { return credentialStartTime;}

    public void setCredentialStartTime(String credentialStartTime) {this.credentialStartTime = credentialStartTime; }

    public String getCredentialEndTime() {return credentialEndTime; }

    public void setCredentialEndTime(String credentialEndTime) { this.credentialEndTime = credentialEndTime; }
}
