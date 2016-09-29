package com.prcsteel.platform.acl.service.impl;

import com.prcsteel.platform.acl.model.model.RolePermission;
import com.prcsteel.platform.acl.persist.dao.RolePermissionDao;
import com.prcsteel.platform.acl.service.RolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Rabbit Mao on 2015/7/12.
 */
@Service("rolePermissionService")
public class RolePermissionServiceImpl implements RolePermissionService{
    @Autowired
    private RolePermissionDao rolePermissionDao;

    public boolean add(RolePermission rolePermission)
    {
        if(!isExisted(rolePermission.getPermissionId(), rolePermission.getRoleId())){
            if (rolePermissionDao.insert(rolePermission) > 0)
                return true;
        }
        return false;
    }

    public boolean delete(Long permId, Long roleId){
        if(rolePermissionDao.delete(permId, roleId) > 0)
        {
            return true;
        }
        return false;
    }

    public List<RolePermission> queryByRoleId(Long roleId) {
        return  rolePermissionDao.queryByRoleId(roleId);
    }

    private boolean isExisted(Long permId, Long roleId) {
        if (rolePermissionDao.count(permId, roleId) > 0)
            return true;
        else
            return false;
    }
}
