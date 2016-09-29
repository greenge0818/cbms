package com.prcsteel.platform.account.model.dto;

import com.prcsteel.platform.account.model.model.Contact;
import com.prcsteel.platform.acl.model.model.User;

import java.util.List;

/**
 * Created by dengxiyan on 2016/1/15.
 */
public class ContactDto extends Contact{
    private String managerName;
    private Long deptId;
    private Long managerId;
    private boolean  hiddenBtn = false;
    private String managerIds;
    private Integer status;

    private List<User> users;

    private List<Long> contactTags;

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Long getManagerId() {
        return managerId;
    }

    public void setManagerId(Long managerId) {
        this.managerId = managerId;
    }

    public boolean isHiddenBtn() {
        return hiddenBtn;
    }

    public void setHiddenBtn(boolean hiddenBtn) {
        this.hiddenBtn = hiddenBtn;
    }

    public List<Long> getContactTags() {
        return contactTags;
    }

    public void setContactTags(List<Long> contactTags) {
        this.contactTags = contactTags;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public String getManagerIds() {
        return managerIds;
    }

    public void setManagerIds(String managerIds) {
        this.managerIds = managerIds;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
