package com.prcsteel.platform.acl.service.impl;

import com.prcsteel.platform.acl.model.model.UserPermission;
import com.prcsteel.platform.acl.persist.dao.UserPermissionDao;
import com.prcsteel.platform.acl.service.UserPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Rabbit Mao on 2015/7/17.
 */
@Service("userPermissionService")
public class UserPermissionServiceImpl implements UserPermissionService {

    @Autowired
    UserPermissionDao userPermissionDao;

    @Override
    public List<UserPermission> queryByUserId(Long userId){
        return userPermissionDao.queryByUserId(userId);
    }
    
    @Override
	public boolean hasPermission(Long userId, Long roleId,String permissionCode) {
		
		return userPermissionDao.hasPermission(userId, roleId, permissionCode)>0;
	}
}
