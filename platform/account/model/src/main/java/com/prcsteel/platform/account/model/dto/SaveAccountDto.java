package com.prcsteel.platform.account.model.dto;

import com.prcsteel.platform.account.model.model.*;
import com.prcsteel.platform.acl.model.model.User;

import java.util.List;

/**
 * Created by lichaowei on 2016/1/15.
 */
public class SaveAccountDto {

    private User user;  // 添加人

    private Account account;    // 账号信息

    private Long departmentId;    // 部门Id（新增账号的第一个默认部门）

    private Contact contact;    // 联系人信息

    private AccountExt accountExt;    // 账号其他信息

    private List<ProxyFactory> proxyFactoryList; // 代理钢厂信息

    private List<AccountAttachment> attachmentList; // 附件信息

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public AccountExt getAccountExt() {
        return accountExt;
    }

    public void setAccountExt(AccountExt accountExt) {
        this.accountExt = accountExt;
    }

    public List<ProxyFactory> getProxyFactoryList() {
        return proxyFactoryList;
    }

    public void setProxyFactoryList(List<ProxyFactory> proxyFactoryList) {
        this.proxyFactoryList = proxyFactoryList;
    }

    public List<AccountAttachment> getAttachmentList() {
        return attachmentList;
    }

    public void setAttachmentList(List<AccountAttachment> attachmentList) {
        this.attachmentList = attachmentList;
    }
}
