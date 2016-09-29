package com.prcsteel.platform.acl.service.impl;

import com.prcsteel.platform.common.enums.OperateStatus;
import com.prcsteel.platform.acl.model.model.Role;
import com.prcsteel.platform.acl.persist.dao.RoleDao;
import com.prcsteel.platform.acl.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Rabbit Mao on 2015/7/12.
 */
@Service("roleService")
public class RoleServiceImpl implements RoleService {
    private static final Integer ENABLED_ROLE_STATUS = 1;


    @Autowired
    private RoleDao roleDao;

    /**
     *
     * @param role
     * @return
     */
    public Integer add(Role role) {
        Integer flag = OperateStatus.FAIL.ordinal();

        Role r = roleDao.queryByCode(role.getCode());
        if(r == null) {
            role.setLastUpdated(new Date());
            role.setModificationNumber(0);
            role.setCreated(new Date());

            if (roleDao.insert(role) > 0) {
                flag = OperateStatus.SUCCESS.ordinal();
            }
        } else {
            if (r.getStatus() != ENABLED_ROLE_STATUS) {
                enabled(r);

                flag = OperateStatus.SUCCESS.ordinal();
            } else {
                flag = OperateStatus.DUPLICATE.ordinal();
            }
        }

        return flag;
    }

    public boolean disable(Role role) {

        // disable child
        List<Role> childs = roleDao.queryByParentId(role.getId());
        for (Role child : childs) {
            Role r = new Role();
            r.setId(child.getId());
            r.setLastUpdatedBy(role.getLastUpdatedBy());
            disable(r);
        }

        role.setStatus(0);
        if (updateRoleStatus(role))
            return true;
        else
            return false;
    }

    public boolean enabled(Role role) {
        role.setStatus(1);
        if (updateRoleStatus(role))
            return true;
        else
            return false;
    }

    public List<Role> query() {
        return roleDao.queryAll();
    }

    private boolean updateRoleStatus(Role role) {
        role.setLastUpdated(new Date());
        role.setModificationNumber(role.getModificationNumber() == null ? 1 : role.getModificationNumber() + 1);

        if (roleDao.updateStatusById(role) > 0)
            return true;
        else
            return false;
    }

    public Role queryById(Long id) {
        return roleDao.queryById(id);
    }

    public Integer updateKeyFieldById(Role role) {
//        Role r = roleDao.queryByCode(role.getCode());
//        if(r!=null && !role.getId().equals(role.getId())) {
//            return Constant.OperateStatus.DUPLICATE.ordinal();
//        }

        if (roleDao.updateKeyFieldById(role)>0){
            return OperateStatus.SUCCESS.ordinal();
        }

        return OperateStatus.FAIL.ordinal();
    }

    public List<Long> queryRoleIds(Long parentId) {
        List<Long> ids = new ArrayList<Long>();
        List<Role> list = roleDao.queryByParentId(parentId);
        for (Role role : list) {
            ids.add(role.getId());
        }

        for (Role role : list) {
            ids.addAll(queryRoleIds(role.getId()));
        }

        return ids;
    }
}