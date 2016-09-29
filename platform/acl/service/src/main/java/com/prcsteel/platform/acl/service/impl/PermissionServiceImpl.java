package com.prcsteel.platform.acl.service.impl;

import com.prcsteel.platform.acl.model.dto.PermissionDto;
import com.prcsteel.platform.acl.model.enums.PermissionOperateType;
import com.prcsteel.platform.acl.model.model.Permission;
import com.prcsteel.platform.acl.model.model.RolePermission;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.acl.model.model.UserPermission;
import com.prcsteel.platform.acl.persist.dao.PermissionDao;
import com.prcsteel.platform.acl.persist.dao.RoleDao;
import com.prcsteel.platform.acl.persist.dao.RolePermissionDao;
import com.prcsteel.platform.acl.persist.dao.UserDao;
import com.prcsteel.platform.acl.persist.dao.UserPermissionDao;
import com.prcsteel.platform.acl.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Rabbit Mao on 2015/7/12.
 */
@Service("permissionService")
public class PermissionServiceImpl implements PermissionService {

    private static final String FIELD_USERID = "userId";
    private static final String FIELD_TYPE = "type";

    @Autowired
    private PermissionDao permissionDao;

    @Autowired
    private RolePermissionDao rolePermissionDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserPermissionDao userPermissionDao;

    public List<Permission> queryByParenId(Long parentId) {
        return permissionDao.queryByParentId(parentId);
    }

    public Permission queryById(Long id) {
        return permissionDao.queryById(id);
    }

    /**
     * 展示所有权限信息
     *
     * @param parentId
     * @return
     */
    public List<PermissionDto> queryPermissionForShow(Long parentId) {
        List<PermissionDto> result = new ArrayList<PermissionDto>();

        List<Permission> parents = queryByParenId(parentId);
        for (Permission parent : parents) {
            PermissionDto dto = new PermissionDto();
            dto.setPermission(parent);

            List<PermissionDto> childs = queryPermissionForShow(parent.getId());
            if (childs != null && childs.size() > 0) {
                dto.setHasChild(true);
                dto.setChild(childs);
            } else {
                dto.setHasChild(false);
            }

            result.add(dto);
        }

        return result;
    }

    /**
     * 保存用户权限
     *
     * @param prams
     * @param userId
     * @param roleId
     * @param curUser
     * @return
     */
    public boolean saveToUser(List<Long> prams, Long userId, Long roleId, String curUser) {
        userPermissionDao.deleteByUserId(userId);   //删除特殊权限并重新赋值

        User user = userDao.queryById(userId);
        if (roleDao.queryById(roleId) == null) {
            return false;
        }
        if (!user.getRoleId().equals(roleId)) {   //如果用户角色被改变，更新用户角色
            user.setRoleId(roleId);
            user.setLastUpdatedBy(curUser);
            user.setLastUpdated(new Date());
            userDao.updateRoleById(user);
        }

        if (prams == null) {  //防止空指针
            prams = new ArrayList<Long>();
        }
        List<RolePermission> rolePermissionList = rolePermissionDao.queryByRoleId(roleId);   //获取角色下所有权限
        List<Long> rolePermissionIdList = rolePermissionList.stream().collect(Collectors.mapping(RolePermission::getPermissionId, Collectors.toList()));

        for (int i = 0; i < prams.size(); i++) {
            if (rolePermissionIdList.contains(prams.get(i))) {
                rolePermissionIdList.remove(prams.get(i));   //权限已存在，则从待删除列表中移除
            }else {
                UserPermission userPermission = new UserPermission();//如果权限不存在，则新增权限
                userPermission.setUserId(userId);
                userPermission.setPermissionId(Long.valueOf(prams.get(i)));
                userPermission.setType(1);
                userPermission.setCreatedBy(curUser);
                userPermission.setCreated(new Date());
                userPermission.setLastUpdatedBy(curUser);
                userPermission.setLastUpdated(new Date());
                userPermissionDao.insert(userPermission);
            }
        }
        for (int i = 0; i < rolePermissionIdList.size(); i++) {   //如果提交的权限中没有匹配到这些权限，则删除
            UserPermission userPermission = new UserPermission();
            userPermission.setUserId(userId);
            userPermission.setPermissionId(rolePermissionIdList.get(i));
            userPermission.setType(0);    //减少
            userPermission.setCreatedBy(curUser);
            userPermission.setCreated(new Date());
            userPermission.setLastUpdatedBy(curUser);
            userPermission.setLastUpdated(new Date());
            userPermissionDao.insert(userPermission);
        }
        return true;
    }

    /**
     * 保存角色权限
     *
     * @param prams
     * @param roleId
     * @param curUser
     * @return
     */
    public boolean save(List<Long> prams, Long roleId, String curUser) {
        if (roleDao.queryById(roleId) != null) {
            List<RolePermission> rolePermissionList = rolePermissionDao.queryByRoleId(roleId);   //获取角色下所有权限
            List<Long> rolePermissionIdList = rolePermissionList.stream().collect(Collectors.mapping(RolePermission::getPermissionId, Collectors.toList()));

            if (prams == null) {    //防止空指针
                prams = new ArrayList<Long>();
            }
            for (int i = 0; i < prams.size(); i++) {
                if (rolePermissionIdList.contains(Long.valueOf(prams.get(i)))) {
                    rolePermissionIdList.remove(Long.valueOf(prams.get(i)));   //权限已存在，则从待删除列表中移除
                } else {
                    RolePermission rolePermission = new RolePermission();  //如果权限不存在，则新增权限
                    rolePermission.setRoleId(roleId);
                    rolePermission.setPermissionId(Long.valueOf(prams.get(i)));
                    rolePermission.setCreated(new Date());
                    rolePermission.setCreatedBy(curUser);
                    rolePermission.setLastUpdated(new Date());
                    rolePermission.setLastUpdatedBy(curUser);
                    rolePermission.setModificationNumber(0);

                    rolePermissionDao.insert(rolePermission);
                }
            }
            for (int i = 0; i < rolePermissionIdList.size(); i++) {   //如果提交的权限中没有匹配到这些权限，则删除
                rolePermissionDao.delete(rolePermissionIdList.get(i), roleId);
            }
            return true;
        }
        return false;
    }

    /**
     * 获取用户权限
     *
     * @param userId 用户编号
     * @param roleId 用户角色
     * @return
     */
    public List<Permission> findAllPermissionsForUser(Long userId, Long roleId) {

        List<Permission> perms = permissionDao.queryByRoleId(roleId);

        Map<String, Object> addmap = new HashMap<String, Object>();
        addmap.put(FIELD_USERID, userId);
        addmap.put(FIELD_TYPE, PermissionOperateType.ADD.ordinal());
        List<Permission> addPerms = permissionDao.queryByUserIdAndType(addmap);

        Map<String, Object> delmap = new HashMap<String, Object>();
        delmap.put(FIELD_USERID, userId);
        delmap.put(FIELD_TYPE, PermissionOperateType.DEL.ordinal());
        List<Permission> delPerms = permissionDao.queryByUserIdAndType(delmap);

        for (Permission p : addPerms) {
            if (!perms.contains(p)) {
                perms.add(p);
            }
        }

        for (Permission p : delPerms) {
            Iterator<Permission> itPerms = perms.iterator();
            while (itPerms.hasNext()) {
                Permission per = itPerms.next();
                if (per.getId().equals(p.getId())) {
                    itPerms.remove();
                    break;
                }
            }
        }

        //chengui 过滤掉没有子菜单的主菜单
        List<Permission> toDelParentPerms = new ArrayList<Permission>();
        List<Permission> parentPerms = permissionDao.queryParentPermissionByRoleId(roleId);
        for(int i = 0; i < parentPerms.size(); i++){
            Permission parentPerm = parentPerms.get(i);
            boolean flag = false; //主菜单是否有子菜单标记
            for(int j = 0; j < perms.size(); j++){
                if(perms.get(j).getParentId().longValue() == parentPerm.getId().longValue()){
                    flag = true;
                    break;
                }
            }

            if(!flag){
                toDelParentPerms.add(parentPerm);
            }

        }
        for (Permission p : toDelParentPerms) {
            Iterator<Permission> itPerms = perms.iterator();
            while (itPerms.hasNext()) {
                Permission per = itPerms.next();
                if (per.getId().equals(p.getId())) {
                    itPerms.remove();
                    break;
                }
            }
        }


        return perms;
    }

    public List<Permission> queryAllPermissionOnlyWithCodeAndUrl() {
        return permissionDao.queryAll();
    }

    public List<Permission> query() {
        return permissionDao.query();
    }

    public int insert(Long parentId, String name, String code, String url, String createdBy) {

        Permission permission = new Permission();
        permission.setParentId(parentId);
        permission.setName(name);
        permission.setCode(code);
        permission.setUrl(url);
        permission.setCreatedBy(createdBy);
        permission.setLastUpdatedBy(createdBy);

        return permissionDao.insert(permission);
    }

    public int update(Long id, String name, String code, String url) {

        Permission permission = new Permission();
        permission.setId(id);
        permission.setName(name);
        permission.setCode(code);
        permission.setUrl(url);

        return permissionDao.update(permission);
    }

    public boolean delete(Long id) {
        boolean flag = true;
        List<Permission> childs = permissionDao.queryByParentId(id);
        for (Permission child : childs) {
            if (!delete(child.getId())) {
                flag = false;
            }
        }

        if (permissionDao.deleteById(id) == 0) {
            flag = false;
        }

        return flag;
    }

    public boolean hasExsistCode(String code, Long id) {
        Permission permission = permissionDao.queryByCodeAndUrl(code, null);
        if (permission != null) {
            if (id == null) {
                return true;
            } else {
                if (!id.equals(permission.getId())) {
                    return true;
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
    }

    public boolean hasExsistUrl(String url, Long id) {
        Permission permission = permissionDao.queryByCodeAndUrl(null, url);
        if (permission != null) {
            if (id == null) {
                return true;
            } else {
                if (!id.equals(permission.getId())) {
                    return true;
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
    }

    @Override
    public Permission queryByCodeAndUrl(String code, String url) {
        return permissionDao.queryByCodeAndUrl(code, url);
    }

    /**
     * 根据用户id和操作类型查询权限url
     * @param userId
     * @return
     */
    @Override
    public List<Permission> queryPermissionByUserId(Long userId){
        return permissionDao.queryPermissionByUserId(userId);
    }

}
