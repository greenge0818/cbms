package com.prcsteel.platform.acl.web.support;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.prcsteel.platform.acl.model.model.Organization;
import com.prcsteel.platform.acl.model.model.Role;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.acl.service.OrganizationService;
import com.prcsteel.platform.acl.service.RoleService;
import com.prcsteel.platform.acl.service.UserService;
import com.prcsteel.platform.common.constants.Constant;

/**
 * 
 * @author zhoukun
 */
public class WebAppContextUtil {
	
	private static UserService userService;
	private static RoleService roleService;
	private static OrganizationService organizationService;
	public void setUserService(UserService userService){
		WebAppContextUtil.userService = userService;
	}
	
	public void setRoleService(RoleService roleService) {
		WebAppContextUtil.roleService = roleService;
	}

	public void setOrganizationService(OrganizationService organizationService) {
		WebAppContextUtil.organizationService = organizationService;
	}

	public static User getLoginUser() {
		RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) ra).getRequest();
        HttpSession session = request.getSession();
        User user;
        // session中是否存在
        if (session.getAttribute(Constant.LOGINUSER) != null) {
            // 直接取
            user = (User) session.getAttribute(Constant.LOGINUSER);
        } else {
            // 数据库取
            Object o = SecurityUtils.getSubject().getPrincipal();
            user = userService.queryByLoginId((String) o);
            session.setAttribute(Constant.LOGINUSER, user);
        }
        return user;
    }
	
	public static HttpSession getSession() {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) ra)
                .getRequest();
        return request.getSession();
    }
	
	public static Role getUserRole() {
        HttpSession session = getSession();
        User user = getLoginUser();
        Role role;
        // session中是否存在
        if (session.getAttribute(Constant.USER_ROLE) != null) {
            // 直接取
            role = (Role) session.getAttribute(Constant.USER_ROLE);
        } else {
            role = roleService.queryById(user.getRoleId());
            session.setAttribute(Constant.USER_ROLE, role);
        }
        return role;
    }

	public static Organization getOrganization(){
        HttpSession session = getSession();
        User user = getLoginUser();
        Organization organization;
        // session中是否存在
        if (session.getAttribute(Constant.USER_ORGANIZATION) != null) {
            // 直接取
            organization = (Organization) session.getAttribute(Constant.USER_ORGANIZATION);
        } else {
            organization = organizationService.queryById(user.getOrgId());
            session.setAttribute(Constant.USER_ORGANIZATION, organization);
        }
        return organization;
    }
}
