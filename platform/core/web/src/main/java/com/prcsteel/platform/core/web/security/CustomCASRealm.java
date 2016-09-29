package com.prcsteel.platform.core.web.security;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.prcsteel.platform.common.constants.Constant;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cas.CasRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.prcsteel.platform.acl.model.model.Permission;
import com.prcsteel.platform.acl.model.model.Role;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.acl.service.PermissionService;
import com.prcsteel.platform.acl.service.RoleService;
import com.prcsteel.platform.acl.service.UserService;

/**
 * Created by rolyer on 15-6-26.
 */
public class CustomCASRealm extends CasRealm {

    private static final Logger logger = LoggerFactory.getLogger(CustomCASRealm.class);

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    /*
	 * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.
	 * @see org.apache.shiro.realm.AuthorizingRealm#doGetAuthorizationInfo(org.apache.shiro.subject.PrincipalCollection)
	 */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String account =  (String)principals.getPrimaryPrincipal();


        User user = userService.queryByLoginId(account);
        if(user != null && Constant.USER_STATUS.LOCKED.getValue() != user.getStatus()) {
            logger.debug("设置用户'" + account + "'的权限");

            Set<String> roleNames = new HashSet<String>();
            Set<String> permissions = new HashSet<String>();
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo(roleNames);

            // set role
            Role role = roleService.queryById(user.getRoleId());
            if(role!= null && role.getStatus().intValue() == Constant.ROLE_STATUS.NORMAL.ordinal()) {
                if (role != null && role.getCode()!=null) {
                    roleNames.add(role.getCode());
                }

                // set perms
                List<Permission> perms = permissionService.findAllPermissionsForUser(user.getId(), user.getRoleId());
                for (Permission p : perms) {
                	if(p != null){
                		permissions.add(p.getCode());
                	}else{
                		logger.error("Found null permission,userId:{},roleId:{}",user.getId(),user.getRoleId());
                	}
                }
            }

            info.setStringPermissions(permissions);

            return info;
        } else {
            logger.warn("用户不存在");

            return null;
        }
    }
}
