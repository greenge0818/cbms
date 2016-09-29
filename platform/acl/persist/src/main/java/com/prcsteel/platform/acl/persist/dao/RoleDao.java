package com.prcsteel.platform.acl.persist.dao;

import com.prcsteel.platform.acl.model.model.Role;

import java.util.List;

public interface RoleDao {

    public int insert(Role role);

    public int updateStatusById(Role role);

    public List<Role> queryAll();

    public Role queryById(Long id);

    public List<Role> queryByParentId(Long id);

    public Role queryByCode(String code);

    public int updateKeyFieldById(Role role);
}