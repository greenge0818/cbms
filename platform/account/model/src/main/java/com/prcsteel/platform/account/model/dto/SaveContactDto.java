package com.prcsteel.platform.account.model.dto;

import com.prcsteel.platform.account.model.model.Contact;
import com.prcsteel.platform.acl.model.model.User;

import java.util.List;

/**
 * Created by dengxiyan on 2016/1/25.
 */
public class SaveContactDto {
    private User user;
    private Contact contact;
    private List<Long> managerIdList;
    private Long deptId;
    private boolean isInEditMode;
    private Integer status;

    public Contact getContact() {
        return contact;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public List<Long> getManagerIdList() {
        return managerIdList;
    }

    public void setManagerIdList(List<Long> managerIdList) {
        this.managerIdList = managerIdList;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public boolean isInEditMode() {
        return isInEditMode;
    }

    public void setIsInEditMode(boolean isInEditMode) {
        this.isInEditMode = isInEditMode;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
