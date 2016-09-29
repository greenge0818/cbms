package com.prcsteel.platform.account.web.controller;

import com.prcsteel.platform.account.service.AccountService;
import com.prcsteel.platform.account.web.security.CustomCASRealm;

import com.prcsteel.platform.acl.model.model.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

/**
 * 
 * @author zhoukun
 */
@Controller
@RequestMapping("/")
public class HomeController extends BaseController {
	
	@Resource(name = "casRealm")
	private CustomCASRealm customCASRealm = null;

	@Value("${logoutUrl}")
	private String logoutUrl;

	@Resource
	AccountService accountService;

	@RequestMapping("index")
	public String index(){
		//初始化菜单权限 modify by caosulin 20160718
		if(customCASRealm.isCacheAuth){
			customCASRealm.initAccountAuthorizationInfo(getLoginUser().getLoginId());
		}
		return "index";
	}

	/**
	 * 退出
	 * @return
	 */
	@RequestMapping("logout")
	public String logout() {
		// 注销缓存 modify by caosulin 20160718
		if(customCASRealm.isCacheAuth){
			User user = getLoginUser();
			if (user != null) {
				customCASRealm.clearAuthorizationInfoCache(getLoginUser().getLoginId());
			}
		}
		// 注销用户
		Subject subject = SecurityUtils.getSubject();
		subject.logout();
		return "redirect:" + logoutUrl;
	}

	/**
	 * @auth zhoucai
	 * @date 2016-5-19
	 * 没有权限
	 * @return
	 */
	@RequestMapping("unauth.html")
	public void unauth() {
	}
}
