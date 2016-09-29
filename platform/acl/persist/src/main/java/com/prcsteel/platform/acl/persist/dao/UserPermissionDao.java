package com.prcsteel.platform.acl.persist.dao;

import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.acl.model.model.UserPermission;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

public interface UserPermissionDao {

    @CacheEvict(value = Constant.CACHE_NAME, key = "'"+ Constant.CACHE_USER_ERMISSION +"' + #p0.userId")
    public int insert(UserPermission userPermission);

    @CacheEvict(value = Constant.CACHE_NAME, key = "'"+ Constant.CACHE_USER_ERMISSION +"' + #p0")
    public int delete(Long userId, Long permissionId);

    @CacheEvict(value = Constant.CACHE_NAME, key = "'"+ Constant.CACHE_USER_ERMISSION +"' + #p0")
    public int deleteByUserId(Long userId);

    public int count(Long userId, Long permissionId);

    @Cacheable(value = Constant.CACHE_NAME, key = "'"+ Constant.CACHE_USER_ERMISSION +"' + #p0")
    public List<UserPermission> queryByUserId(Long userId);

    public List<UserPermission> queryByPermissionId(Long permissionId);
    
    //用户是否包含某个权限Geen.Ge for app
    int hasPermission(Long userId, Long roleId, String permissionCode);
}