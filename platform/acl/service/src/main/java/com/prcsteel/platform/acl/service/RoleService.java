package com.prcsteel.platform.acl.service;

import com.prcsteel.platform.acl.model.model.Role;

import java.util.List;

/**
 * Created by Rabbit Mao on 2015/7/12.
 */
public interface RoleService {
    public Integer add(Role role);

    public boolean disable(Role role);

    public boolean enabled(Role role);

    public Role queryById(Long id);

    public List<Role> query();

    public Integer updateKeyFieldById(Role role);

    public List<Long> queryRoleIds(Long parentId);

}
