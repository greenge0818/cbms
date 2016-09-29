package com.prcsteel.platform.acl.web.controller;

import com.prcsteel.platform.acl.model.model.Role;
import com.prcsteel.platform.acl.service.RoleService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author kongbinheng
 * @version v2.0_account
 * @Description: 角色api
 * @date 2016/2/18
 */
@RestController
@RequestMapping("/api/role/")
public class RestRoleController extends BaseController {

    @Resource
    RoleService roleService;

    /**
     * 根据角色id查询角色
     * @param id
     * @return
     */
    @RequestMapping(value = "queryById/{id}.html", method = RequestMethod.GET)
    public Role queryById(@PathVariable Long id){
        return roleService.queryById(id);
    }

    /**
     * 根据角色父id查询角色
     * @param parentId
     * @return
     */
    @RequestMapping(value = "queryRoleIds/{parentId}.html", method = RequestMethod.GET)
    public List<Long> queryRoleIds(@PathVariable Long parentId){
        return roleService.queryRoleIds(parentId);
    }

}