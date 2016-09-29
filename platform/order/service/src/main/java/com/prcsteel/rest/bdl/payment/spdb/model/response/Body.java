package com.prcsteel.rest.bdl.payment.spdb.model.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by ericlee on 7/18/15.
 */
@XmlRootElement(name="Body")
@XmlAccessorType(XmlAccessType.FIELD)
public class Body implements Serializable {
    /*
    <totalCount>1</totalCount>
    <acctNo>077402321243215</acctNo>
    <acctName></acctName>
    <currency>01</currency>
    <lists name="HistoryList">
    */
    private String totalCount;
    private String acctNo;
    private String acctName;
    private String currency;
    private Lists lists;

    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    public String getAcctNo() {
        return acctNo;
    }

    public void setAcctNo(String acctNo) {
        this.acctNo = acctNo;
    }

    public String getAcctName() {
        return acctName;
    }

    public void setAcctName(String acctName) {
        this.acctName = acctName;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Lists getLists() {
        return lists;
    }

    public void setLists(Lists lists) {
        this.lists = lists;
    }
}
