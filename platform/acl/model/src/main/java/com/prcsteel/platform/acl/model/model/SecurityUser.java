package com.prcsteel.platform.acl.model.model;

import java.io.Serializable;

/**
 * Created by rolyer on 15-6-25.
 */
public class SecurityUser implements Serializable{

    private static final long serialVersionUID = 1L;

    private long id;
    private String username;
    private String password;
    private boolean locked;
    private long roleId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }
}