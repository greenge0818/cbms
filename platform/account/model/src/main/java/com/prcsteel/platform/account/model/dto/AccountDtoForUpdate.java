package com.prcsteel.platform.account.model.dto;

import com.prcsteel.platform.acl.model.model.User;

import java.util.List;

/**
 * Created by dengxiyan on 2016/3/2.
 */
public class AccountDtoForUpdate {
    private List<Long> ids;
    private Integer status;
    private User user;

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
