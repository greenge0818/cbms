package com.prcsteel.platform.acl.service;

import com.prcsteel.platform.acl.model.model.RolePermission;

import java.util.List;

/**
 * Created by Rabbit Mao on 2015/7/12.
 */
public interface RolePermissionService {
    public boolean add(RolePermission rolePermission);

    public boolean delete(Long permId, Long roleId);

    public List<RolePermission> queryByRoleId(Long roleId);
}
