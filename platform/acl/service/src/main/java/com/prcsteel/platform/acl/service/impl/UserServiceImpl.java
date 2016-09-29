package com.prcsteel.platform.acl.service.impl;

import com.prcsteel.platform.acl.model.model.BaseFriendlyLink;
import com.prcsteel.platform.acl.persist.dao.BaseFriendlyLinkDao;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.acl.model.dto.UserDto;
import com.prcsteel.platform.acl.model.dto.UserOrgDto;
import com.prcsteel.platform.acl.model.enums.RoleAuthType;
import com.prcsteel.platform.acl.model.enums.RoleType;
import com.prcsteel.platform.acl.model.model.Role;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.acl.persist.dao.OrganizationDao;
import com.prcsteel.platform.acl.persist.dao.RoleDao;
import com.prcsteel.platform.acl.persist.dao.UserDao;
import com.prcsteel.platform.acl.service.RoleService;
import com.prcsteel.platform.acl.service.UserService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Rabbit Mao on 2015/7/12.
 */
@Service("userService")
public class UserServiceImpl implements UserService {
    @Resource
    private UserDao userDao;
    @Resource
    private RoleDao roleDao;
    @Resource
    private OrganizationDao organizationDao;
    @Resource
    private RoleService roleService;
    @Resource
    private BaseFriendlyLinkDao baseFriendlyLinkDao;

    /**
     * 用用户名搜索
     *
     * @param loginId
     * @return
     */
    @Override
    public User queryByLoginId(String loginId) {
        return userDao.queryByLoginId(loginId);
    }

    /**
     * 通过条件查找
     *
     * @param
     * @return
     */
    @Override
    public List<User> queryByParam(Map<String, Object> param) {
        return userDao.queryByParam(param);
    }

    /**
     * 条件搜索用户列表
     *
     * @param param
     * @return
     */
    @Override
    public List<UserDto> queryUserForShow(Map<String, Object> param) {
        return userDao.queryUserForShow(param);
    }
  //统计服务中心所有人员个数
    @Override
    public int countUserForShow(Map<String, Object> param){
    	return userDao.countUserForShow(param);
    }
    /**
     * 用用户id查找
     *
     * @param id
     * @return
     */
    @Override
    public User queryById(Long id) {
        return userDao.queryById(id);
    }

    /**
     * 查找用户的组织角色
     *
     * @param paramMap 查询参数
     * @return
     */
    @Override
    public List<UserOrgDto> queryUserOrg(Map<String, Object> paramMap) {
        return userDao.queryUserOrg(paramMap);
    }

    /**
     * 更新用户
     *
     * @param user
     * @return
     */
    @Override
    @Transactional
    public boolean updateByPrimaryKeySelective(User user, String curUser) {
        User userTel = userDao.queryByTel(user.getTel());
        if (null != userTel && !userTel.getId().equals(user.getId())) {
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "电话不能重复");
        }
        user.setLoginId(null);   //loginId和jobNumber不允许更改 add by Rabbit
        user.setJobNumber(null); //loginId和jobNumber不允许更改
        user.setLastUpdatedBy(curUser);
        user.setLastUpdated(new Date());
        return userDao.updateByPrimaryKeySelective(user) > 0;
    }

    /**
     * 添加用户
     *
     * @param user
     * @return
     */
    @Override
    @Transactional
    public void add(User user, String curUser) {
        if (null != userDao.queryByLoginId(user.getLoginId())) {
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "用户名不能重复");
        }
        if (null != userDao.queryByTel(user.getTel())) {
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "电话不能重复");
        }
        if (null != userDao.queryByJobNumber(user.getJobNumber())) {
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "工号不能重复");
        }
        user.setLastUpdated(new Date());
        user.setLastUpdatedBy(curUser);
        user.setModificationNumber(0);
        user.setCreated(new Date());
        user.setCreatedBy(curUser);
        user.setStatus(Integer.valueOf(Constant.YES));
        if(userDao.insert(user) != 1){
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "插入用户数据失败");
        }
    }

    /**
     * 通过角色查找
     *
     * @param roleIds
     * @return
     */
    @Override
    public List<Long> queryUserIdsByRoleIds(List<Long> roleIds) {
        List<Long> ids = new ArrayList<Long>();
        List<User> users = userDao.queryByRoleIds(roleIds);
        for (User user : users) {
            ids.add(user.getId());
        }
        return ids;
    }

    /**
     * 通过角色查找有权限的服务中心
     *
     * @param roleIds
     * @return
     */
    public List<Long> queryOrgIdsByRoleIds(List<Long> roleIds) {
        List<Long> ids = new ArrayList<Long>();
        List<User> users = userDao.queryByRoleIds(roleIds);
        for (User user : users) {
            if(!ids.contains(user.getOrgId())){
                ids.add(user.getOrgId());
            }
        }
        return ids;
    }

    /**
     * 通过角色查找有权限的服务中心
     *
     * @Param 当前登录人role
     * @Param 当前登录用户user
     * @return
     */
    @Override
    public List<Long> getOrgIdsByRoleIds(Role role,User user) {
        List<Long> ids = new ArrayList<Long>();
        if (RoleAuthType.ALL.getValue() == role.getType()) { //所有数据
            return null;
        } else if (RoleAuthType.SAME_LEVEL.getValue() == role.getType()) { //同级＋下级
            List<Long> roles = roleService.queryRoleIds(role.getParentId());
            if (roles.size() > 0) {
                ids = queryOrgIdsByRoleIds(roles);
            }
            return ids;
        } else if (RoleAuthType.LOWER_LEVEL.getValue() == role.getType()) { //下级
            List<Long> roles = roleService.queryRoleIds(role.getId());
            if (roles.size() > 0) {
                ids = queryOrgIdsByRoleIds(roles);
            }
            if(!ids.contains(user.getOrgId())){
                ids.add(user.getOrgId());
            }
            return ids;
        } else { //仅自己
            if(!ids.contains(user.getOrgId())){
                ids.add(user.getOrgId());
            }
            return ids;
        }
    }

    /**
     * 根据权限code获取拥有该权限的用户
     * @param code 权限code
     * @param userIds UserId 范围
     * @return 拥有该权限的用户集合
     */
    @Override
    public List<User> queryByPermissionCode(String code,List<Long> userIds){
        return userDao.queryByPermissionCode(code,userIds);
    }

	@Override
	public boolean isManager(User user) {
//		Organization org = organizationDao.queryById(user.getOrgId());
//		if(org.getCharger().equals(user.getId())){
//			return true;
//		}
		Role role = roleDao.queryById(user.getRoleId());
		String roleName = RoleType.Manager.toString();
		if(roleName.equals(role.getRoleType())){
			return true;
		}
		return false;
	}

	/**
	 * 根据角色查询用户列表
	 */
	@Override
	public List<User> queryByRoleType(Map<String, Object> paramMap) {
		return userDao.queryByRoleType(paramMap);
	}
    //获取所有友情链接给超市
    @Override
    public List<BaseFriendlyLink> selectAllLink(){
        return baseFriendlyLinkDao.selectAllLink();
    }
}
