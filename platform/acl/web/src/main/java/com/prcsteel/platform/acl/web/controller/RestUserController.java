package com.prcsteel.platform.acl.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.acl.model.model.UserOrg;
import com.prcsteel.platform.acl.service.UserOrgService;
import com.prcsteel.platform.acl.service.UserService;

/**
 * @author kongbinheng
 * @version v2.0_account
 * @Description: 用户api
 * @date 2016/1/14
 */
@RestController
@RequestMapping("/api/user/")
public class RestUserController {

    @Resource
    private UserService userService;
    @Resource
    private UserOrgService userOrgService;

    /**
     * 根据登录用户login_id查询用户
     * @param loginId
     * @return
     */
    @RequestMapping(value = "queryByLoginId/{loginId}.html", method = RequestMethod.GET)
    public User queryByLoginId(@PathVariable String loginId){
        return userService.queryByLoginId(loginId);
    }

    /**
     * 根据登录用户主键id查询用户
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "queryById/{id}.html", method = RequestMethod.GET)
    public User queryById(@PathVariable Long id){
        return userService.queryById(id);
    }

    /**
     * 获取指定Role范围内的用户的编号集
     * @param roleIds
     * @return
     */
    @RequestMapping(value = "queryUserIdsByRoleIds.html", method = RequestMethod.POST)
    public List<Long> queryUserIdsByRoleIds(@PathVariable List<Long> roleIds){
        return userService.queryUserIdsByRoleIds(roleIds);
    }

    /**
     * 判断用户是否是经理
     * @param user
     * @return
     */
    @RequestMapping(value = "isManager.html", method = RequestMethod.POST)
    public boolean isManager(@PathVariable User user){
        return userService.isManager(user);
    }

    /**
     * 根据userId查询配置
     */
    @RequestMapping(value = "getConfigByUserId/{userId}.html", method = RequestMethod.GET)
    public List<UserOrg> getConfigByUserId(@PathVariable Long userId){
        return userOrgService.getConfigByUserId(userId);
    }
    
    /**
     * 根据角色获取用户列表
     * @param paramMap
     * @return
     */

    @RequestMapping(value = "querybyroletype/{roleType}.html",method = RequestMethod.GET)
    public List<User> queryByRoleType(@PathVariable String roleType) {
		Map<String,Object>paramMap = new HashMap<String, Object>();
		paramMap.put("roleType",roleType);
    	return userService.queryByRoleType(paramMap);
    }
}