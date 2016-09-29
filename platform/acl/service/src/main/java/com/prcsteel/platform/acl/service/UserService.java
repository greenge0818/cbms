package com.prcsteel.platform.acl.service;

import com.prcsteel.platform.acl.model.dto.UserDto;
import com.prcsteel.platform.acl.model.dto.UserOrgDto;
import com.prcsteel.platform.acl.model.model.BaseFriendlyLink;
import com.prcsteel.platform.acl.model.model.Role;
import com.prcsteel.platform.acl.model.model.User;

import java.util.List;
import java.util.Map;

/**
 * Created by Rabbit Mao on 2015/7/12.
 */
public interface UserService {
    void add(User user, String curUser);

    User queryByLoginId(String loginId);

    User queryById(Long id);

    List<User> queryByParam(Map<String, Object> param);

    List<UserDto> queryUserForShow(Map<String, Object> param);
  //统计服务中心所有人员个数
    int countUserForShow(Map<String, Object> param);

    boolean updateByPrimaryKeySelective(User user, String curUser);

    /**
     * 查找用户的组织角色
     *
     * @param paramMap 查询参数
     * @return
     */
    List<UserOrgDto> queryUserOrg(Map<String, Object> paramMap);


    /**
     * 获取指定Role范围内的用户的编号集
     * @param roleIds
     * @return
     */
    List<Long> queryUserIdsByRoleIds(List<Long> roleIds);

    /**
     * 获取指定Role范围内的用户的服务中心集
     * @Param role
     * @Param user
     * @return
     */
    List<Long> getOrgIdsByRoleIds(Role role, User user);

    /**
     * 根据权限code获取拥有该权限的用户
     *
     * @param code    权限code
     * @param userIds UserId 范围
     * @return 拥有该权限的用户集合
     */
    List<User> queryByPermissionCode(String code, List<Long> userIds);
    //判断用户是否是经理
    boolean isManager(User user);
    //获取所有友情链接给超市
    List<BaseFriendlyLink> selectAllLink();
    
    /**
     * 根据角色查询用户列表
     * @param paramMap
     * @return
     */
    List<User>queryByRoleType(Map<String, Object> paramMap);
}