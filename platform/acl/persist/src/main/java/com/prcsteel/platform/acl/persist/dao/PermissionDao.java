package com.prcsteel.platform.acl.persist.dao;

import com.prcsteel.platform.acl.model.model.Permission;
import com.prcsteel.platform.common.constants.Constant;
import org.apache.ibatis.annotations.Param;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;
import java.util.Map;

public interface PermissionDao {

    @Cacheable(value = Constant.CACHE_NAME, key = "'" + Constant.CACHE_PERMISSION_BY_PARENTID + "' + #p0")
    public List<Permission> queryByParentId(Long parentId);

    public Permission queryById(Long id);

    public List<Permission> queryByUserIdAndType(Map<String, Object> map);

    public List<Permission> queryByRoleId(Long roleId);

    public List<Permission> queryAll();

    public List<Permission> query();

    public int insert(Permission permission);

    public int update(Permission permission);

    public int deleteById(Long id);

    public Permission deleteByParentId(Long parentId);

    public Permission queryByCodeAndUrl(@Param("code") String code, @Param("url") String url);

    public List<Permission> queryPermissionByUserId(@Param("userId") Long userId);

    /**
     * 查询父资源为0的权限url
     * @param roleId
     * @author chengui
     */
    public List<Permission> queryParentPermissionByRoleId(Long roleId);
}