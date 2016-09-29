package com.prcsteel.platform.acl.service;

import com.prcsteel.platform.acl.model.model.UserPermission;

import java.util.List;

/**
 * Created by Rabbit Mao on 2015/7/17.
 */
public interface UserPermissionService {

    public List<UserPermission> queryByUserId(Long userId);

    //用户是否包含某个权限Geen.Ge for app
    public boolean hasPermission(Long userId, Long roleId, String permissionCode);
}

