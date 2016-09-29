package com.prcsteel.platform.acl.service;

import java.util.List;

import com.prcsteel.platform.acl.model.dto.PermissionDto;
import com.prcsteel.platform.acl.model.model.Permission;

/**
 * Created by Rabbit Mao on 2015/7/12.
 */
public interface PermissionService {
    public List<Permission> queryByParenId(Long parentId);

    public Permission queryById(Long id);
    
    /**
     * 根据code和URL查询
     * @param code 可选，不做为条件传null
     * @param url 可选，不做为条件传null
     * @return
     */
    public Permission queryByCodeAndUrl(String code,String url);

    public List<PermissionDto> queryPermissionForShow(Long parentId);

    public boolean saveToUser(List<Long> prams, Long userId, Long roleId, String curUser);

    public boolean save(List<Long> prams, Long roleId, String curUser);

    public List<Permission> findAllPermissionsForUser(Long userId, Long roleId);

    public List<Permission> queryAllPermissionOnlyWithCodeAndUrl();
    /**
     * 查询全选列表（全字段）
     * @return
     */
    public List<Permission> query();

    /**
     * 新增权限
     * @param parentId
     * @param name
     * @param code
     * @param url
     * @param createdBy
     * @return
     */
    public int insert(Long parentId, String name, String code, String url, String createdBy);

    /**
     * 更新权限
     * @param id　权限编号
     * @param name　名称
     * @param code　权限值
     * @param url URL
     * @return
     */
    public int update(Long id, String name, String code, String url);

    /**
     * 删除权限（包括所有子节点）
     * @param id
     * @return
     */
    public boolean delete(Long id);

    /**
     * 权限值(code)是否存在
     * @param code
     * @return
     */
    public boolean hasExsistCode(String code, Long id);

    /**
     * URL是否存在
     * @param url
     * @return
     */
    public boolean hasExsistUrl(String url, Long id);

    /**
     * 根据用户id查询权限url
     * @param userId
     * @return
     */
    public List<Permission> queryPermissionByUserId(Long userId);
}
