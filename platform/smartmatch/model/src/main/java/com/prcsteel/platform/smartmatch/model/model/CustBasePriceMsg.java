package com.prcsteel.platform.smartmatch.model.model;

import java.util.Date;

public class CustBasePriceMsg {
    private Long id;

    private Date created;

    private String tel;

    private String msgContent;

    private String status;

    private Date lastUpdated;
   //modify by zhoucai@prcsteel.com 2016-9-14 公司id
    private Long accountId;
    //modify by zhoucai@prcsteel.com 2016-9-14 联系人id
    private Long contactId;
    //modify by zhoucai@prcsteel.com 2016-9-14 公司名称
    private String accountName;
    //modify by zhoucai@prcsteel.com 2016-9-14 联系人名称
    private String contactName;

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getContactId() {
        return contactId;
    }

    public void setContactId(Long contactId) {
        this.contactId = contactId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel == null ? null : tel.trim();
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent == null ? null : msgContent.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}