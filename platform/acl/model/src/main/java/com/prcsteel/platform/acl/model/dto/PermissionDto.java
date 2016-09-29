package com.prcsteel.platform.acl.model.dto;

import java.util.List;

import com.prcsteel.platform.acl.model.model.Permission;

/**
 * Created by rolyer on 15-7-13.
 */
public class PermissionDto {
    private Permission permission;
    private List<PermissionDto> child;

    private Boolean hasChild;

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    public List<PermissionDto> getChild() {
        return child;
    }

    public void setChild(List<PermissionDto> child) {
        this.child = child;
    }

    public Boolean getHasChild() {
        return hasChild;
    }

    public void setHasChild(Boolean hasChild) {
        this.hasChild = hasChild;
    }
}
