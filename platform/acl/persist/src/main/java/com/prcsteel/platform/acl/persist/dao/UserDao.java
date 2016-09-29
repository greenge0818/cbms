package com.prcsteel.platform.acl.persist.dao;

import com.prcsteel.platform.acl.model.dto.UserDto;
import com.prcsteel.platform.acl.model.dto.UserOrgDto;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.acl.model.query.ReportOrgQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface UserDao {

    int insert(User user);

    User queryByLoginId(String loginId);

    List<User> queryByParam(Map<String, Object> param);

    User queryById(Long id);

    User queryByTel(String tel);

    User queryByJobNumber(String jobNumber);

    int updateRoleById(User user);

    /**
     * 查找用户的组织角色
     *
     * @param paramMap 查询参数
     * @return
     */
    List<UserOrgDto> queryUserOrg(Map<String, Object> paramMap);

    int updateByPrimaryKeySelective(User user);

    User queryByRoleId(Long roleId);

    /**
     * 获取指定Role范围内的用户
     * @param ids
     * @return
     */
    List<User> queryByRoleIds(List<Long> ids);

    List<UserDto> queryUserForShow(Map<String, Object> param);
    //统计服务中心所有人员个数
    int countUserForShow(Map<String, Object> param);

    /**
     * 根据权限code获取拥有该权限的用户 
     * @param code 权限code
     * @param userIds UserId 范围
     * @return 拥有该权限的用户集合
     */
    List<User> queryByPermissionCode(@Param("code") String code, @Param("userIds") List<Long> userIds);

    /**
     * 查找业务服务中心及服务中心的所有交易员
     *
     * @param orgId 指定的业务服务中心id
     * @return
     */
    List<UserOrgDto> queryTradersBusinessOrgByOrgIdSelective(@Param(value="orgId")Long orgId);

    //查找服务中心下所有用户
    List<UserOrgDto> queryUsersByOrgId(ReportOrgQuery queryParam);
    
    List<User> queryByRoleType(Map<String, Object> param);

    /***
     * 查询直接上级列表
     * @param roleId
     * @return
     */
    List<User> queryDirectManagersByRoleId(Long roleId);

    /***
     * 获取同部门同级别用户
     * @param userId
     * @return
     */
    List<User> querySiblingById(Long userId);
}