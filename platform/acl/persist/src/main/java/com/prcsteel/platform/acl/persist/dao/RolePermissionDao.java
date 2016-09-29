package com.prcsteel.platform.acl.persist.dao;

import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.acl.model.model.RolePermission;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

public interface RolePermissionDao {

    @CacheEvict(value = Constant.CACHE_NAME, key = "'"+ Constant.CACHE_ROLE_PERMISSION +"' + #p0.roleId")
    public int insert(RolePermission rolePermission);

    @CacheEvict(value = Constant.CACHE_NAME, key = "'"+ Constant.CACHE_ROLE_PERMISSION +"' + #p1")
    public int delete(Long permId, Long roleId);

    public int count(Long permId, Long roleId);

    @Cacheable(value = Constant.CACHE_NAME, key = "'"+ Constant.CACHE_ROLE_PERMISSION +"' + #p0")
    public List<RolePermission> queryByRoleId(Long roleId);
}