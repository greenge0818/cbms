package com.prcsteel.platform.kuandao.web.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController extends BaseController {

	@Value("${logoutUrl}")
	private String logoutUrl;

	@RequestMapping("index")
	public String index(){
		return "index";
	}

	/**
	 * 退出
	 * @return
	 */
	@RequestMapping("logout")
	public String logout() {
		// 注销用户
		Subject subject = SecurityUtils.getSubject();
		subject.logout();
		return "redirect:" + logoutUrl;
	}
	
	@RequestMapping("warning.html")
	public void warning() {
		//Do nothing
	}

	@RequestMapping("fail.html")
	public void fail() {
		//Do nothing
	}

	@RequestMapping("unauth.html")
	public void unauth() {
		//Do nothing
	}

	@RequestMapping("login.html")
	public void login() {
		//Do nothing
	}
}
