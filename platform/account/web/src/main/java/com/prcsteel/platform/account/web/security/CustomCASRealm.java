package com.prcsteel.platform.account.web.security;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.service.impl.CommonCacheServiceImpl;

import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cas.CasRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

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

    private static final String CACHE_KEY_PRE = "_accountpri_";
    
    @Autowired
    private PermissionService permissionService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Resource
    private CommonCacheServiceImpl commonCacheServiceImpl = null;
    
    @Value("${memcached.cacheAuth}")
    public Boolean isCacheAuth = true;//是否启用授权缓存
    
    @Value("${memcached.loginOverCache}")
    public Boolean isLoginOverrideCache = true;//登陆是否覆盖缓存
    /*
	 * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.
	 * @see org.apache.shiro.realm.AuthorizingRealm#doGetAuthorizationInfo(org.apache.shiro.subject.PrincipalCollection)
	 */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String account =  (String)principals.getPrimaryPrincipal();
        
        //菜单跳转之前判断是否缓存中已经有权限配置 modify by caosulin 20160718 
        if(isCacheAuth){
	        String key  = CACHE_KEY_PRE + account;
	        if(commonCacheServiceImpl.get(key) != null){
	        	return (AuthorizationInfo) commonCacheServiceImpl.get(key);
	        }
        }
        return initAccountAuthorizationInfo(account);
    }
    /**
     * 登录初始化权限配置
     * @param account
     * @return
     */
	public AuthorizationInfo initAccountAuthorizationInfo(String account) {
		//登录加载之前判断是否缓存中已经有权限配置 modify by caosulin 20160718 
		if(isCacheAuth && !isLoginOverrideCache){
			String key = CACHE_KEY_PRE + account;
			if (commonCacheServiceImpl.get(key) != null) {
				return (AuthorizationInfo) commonCacheServiceImpl.get(key);
			}
		}
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
            //放入缓存 modify by caosulin 20160718 
            if(isCacheAuth){
            	String key = CACHE_KEY_PRE + account;
	            if (this.commonCacheServiceImpl != null) {
	    			this.commonCacheServiceImpl.set(key,2*60*60 ,info);
	    		}
            }
            return info;
        } else {
            logger.warn("用户不存在");

            return null;
        }
		
	}

	/**
	 * 退出删除权限配置
	 * @param loginId
	 */
	public void clearAuthorizationInfoCache(String loginId) {
		//删除权限 modify by caosulin 20160718 
		String key  = CACHE_KEY_PRE + loginId;
		if (this.commonCacheServiceImpl != null) {
			this.commonCacheServiceImpl.delete(key);
		}
	}
}
