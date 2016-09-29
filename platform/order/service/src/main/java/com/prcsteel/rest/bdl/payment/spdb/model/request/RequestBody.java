package com.prcsteel.rest.bdl.payment.spdb.model.request;

/**
 * Created by kongbinheng on 2015/7/13.
 */
//@XmlRootElement(name="RequestBody")
public class RequestBody {

    private String acctNo;  //账号
    private String beginDate;   //开始日期YYYYMMDD
    private String endDate; //结束日期开始日期YYYYMMDD
    private Integer queryNumber; //查询的笔数，默认为20
    private Integer beginNumber; //查询的起始笔数，默认为1
    private Double transAmount; //交易金额
    private String subAccount;  //对方帐号
    private String subAcctName; //对方户名

    public String getAcctNo() {
        return acctNo;
    }

    public void setAcctNo(String acctNo) {
        this.acctNo = acctNo;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Integer getQueryNumber() {
        return queryNumber;
    }

    public void setQueryNumber(Integer queryNumber) {
        this.queryNumber = queryNumber;
    }

    public Integer getBeginNumber() {
        return beginNumber;
    }

    public void setBeginNumber(Integer beginNumber) {
        this.beginNumber = beginNumber;
    }

    public Double getTransAmount() {
        return transAmount;
    }

    public void setTransAmount(Double transAmount) {
        this.transAmount = transAmount;
    }

    public String getSubAccount() {
        return subAccount;
    }

    public void setSubAccount(String subAccount) {
        this.subAccount = subAccount;
    }

    public String getSubAcctName() {
        return subAcctName;
    }

    public void setSubAcctName(String subAcctName) {
        this.subAcctName = subAcctName;
    }

}
