package com.prcsteel.platform.order.web.controller.wechat;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.prcsteel.platform.account.model.dto.AccountContactOrgUserDto;
import com.prcsteel.platform.acl.service.OrganizationService;
import com.prcsteel.platform.order.model.wechat.dto.CategoryGroup;
import com.prcsteel.platform.core.service.CategoryGroupService;
import com.prcsteel.platform.account.service.AccountContactService;
import com.prcsteel.platform.order.service.point.PointService;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.acl.service.UserService;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.common.utils.StringUtil;
import com.prcsteel.platform.order.model.query.WXContractQuery;
import com.prcsteel.platform.order.service.CacheService;
import com.prcsteel.platform.account.service.AccountService;

import com.prcsteel.platform.acl.utils.LdapOperUtils;
import com.prcsteel.platform.order.web.vo.APPResult;

@RestController
@RequestMapping("/wechat")
public class WeChatController {
	private static final String SUCCESSCODE = "200";
	private static final String FAILCODE = "201";
	private static final String INVALIDCODE = "203";
	@Resource
	private CacheService cacheServiceWeChat;
	@Resource
	private UserService userService;

	@Resource
	private AccountService accountService;
	@Resource
	private AccountContactService accountContactService;
	@Resource
	private CategoryGroupService categoryGroupService;
	@Resource
	private PointService pointService;
	@Resource
	OrganizationService organizationService;


	private static Logger logger = LoggerFactory.getLogger(WeChatController.class);
	//刷新缓存
	@ModelAttribute
	public void touchToken(String authToken,String username) {
		if(StringUtils.isNotEmpty(authToken)){
			cacheServiceWeChat.set(authToken, Constant.MEMCACHESESSIONOUTTOWECHAT, username);
		}
	}
	//验证token是否在缓存中存在
	@ModelAttribute
	public boolean IsTokenInMem(String authToken) {
		boolean isMem = false;
		if(StringUtils.isNotEmpty(authToken)){
			Object obj =cacheServiceWeChat.get(authToken);
			if(obj!=null){
				isMem = true;
			}else {
				isMem = true;
			}
		}
		return isMem;
	}
	// 授权认证
	@RequestMapping(value="/account/authorize.html",method=RequestMethod.GET)
	@ResponseBody
	public APPResult authorize(String username, String password) {
		APPResult result = new APPResult();
		if (StringUtils.isEmpty(username)) {
			result.setStatusCode(FAILCODE);
			result.setMessage("请输入用户名");
		}else if (StringUtils.isEmpty(password)) {
			result.setStatusCode(FAILCODE);
			result.setMessage("请输入密码");
		} else {
			try {

				if (new LdapOperUtils().login(username, password)) {//账号密码校验通过
					logger.info("authorize start..");

					logger.info("query user from datatable..");
					User user = userService.queryByLoginId(username);
					logger.info("query user from datatable done");
					//数据库里也查不到,返回错误
					if (user == null) {
						result.setStatusCode(FAILCODE);
						result.setMessage("该用户在数据库不存在");
						return result;
					}
					String token = StringUtil.build(username + System.currentTimeMillis(), "utf-8");
					logger.info("token built!");
					logger.info("setting token to cache");
					touchToken(token, username);
					logger.info("setting token to cache done");
					result.setStatusCode(SUCCESSCODE);
					result.setMessage("success");
					Map<String,String> map = new HashMap<String,String>();
					map.put("token",token);
					map.put("expires_in",Constant.MEMCACHESESSIONOUTTOWECHAT + "");
					result.setData(map);
					logger.info("authorize success!");
				}else {
					result.setStatusCode(FAILCODE);
					result.setMessage("用户名或密码错误");
				}
				}catch (BusinessException e) {
						result.setStatusCode(FAILCODE);
						result.setMessage(e.getMsg());
					}
		}
		logger.info("authorize complete");
		return result;
	}
	
	
	/**
	 * 绑定微信
	 * @return
	 */
	@RequestMapping(value="/customer/bind.html", method=RequestMethod.POST)
	@ResponseBody
	public APPResult bindWechat(@RequestParam("token") String token, @RequestParam("phone") String phone, @RequestParam("openid") String openId, @RequestParam("timestamp") String timestamp){
		APPResult result = new APPResult();
		if (StringUtils.isEmpty(phone)) {
			result.setStatusCode(FAILCODE);
			result.setMessage("手机号码为空");
		}else if (StringUtils.isEmpty(openId)) {
			result.setStatusCode(FAILCODE);
			result.setMessage("open_id不存在");
		} else {
			HashMap<String,String> params = new HashMap<String,String>();
			params.put("phone",phone);
			params.put("openid",openId);
			params.put("timestamp",timestamp);
			if(StringUtil.verifySignature(params,token)) {
				logger.info("REST request to change phone:{}" + params);
				try {
					accountService.wxBindContract(new WXContractQuery(token, openId, phone));
					AccountContactOrgUserDto accountContactOrgUserDto = accountContactService.queryAccountContactByOpenId(openId);
					pointService.syncMemberInfo(phone,openId,accountContactOrgUserDto.getAccountName());
					result.setMessage("success");
					result.setStatusCode(SUCCESSCODE);
				} catch (BusinessException e) {
					result.setStatusCode(e.getCode());
					result.setMessage(e.getMsg());
					logger.info(e.getMsg());
				}
			}else {
				result.setStatusCode(INVALIDCODE);
				result.setMessage("token无效！");
			}
		}
		return result;
	}
	
	/**
	 * 解绑微信
	 * @return
	 */
	@RequestMapping(value = "/customer/unbind.html", method=RequestMethod.POST)
	@ResponseBody
	public APPResult unbindWechat(@RequestParam("token") String token, @RequestParam("phone") String phone, @RequestParam("openid") String openId, @RequestParam("timestamp") String timestamp){
		APPResult result = new APPResult();
		if (StringUtils.isEmpty(phone)) {
			result.setStatusCode(FAILCODE);
			result.setMessage("手机号码为空");
		}else {
			HashMap<String,String> params = new HashMap<String,String>();
			params.put("phone",phone);
			params.put("openid",openId);
			params.put("timestamp",timestamp);
			if(StringUtil.verifySignature(params,token)) {
				logger.info("REST request to change phone:{}" + params);
				try {
					accountService.wxUnbindContract(new WXContractQuery(token, openId, phone));
					result.setMessage("success");
					result.setStatusCode(SUCCESSCODE);
				} catch (BusinessException e) {
					result.setStatusCode(e.getCode());
					result.setMessage(e.getMsg());
					logger.info(e.getMsg());
				}
			}else {
				result.setStatusCode(INVALIDCODE);
				result.setMessage("token无效！");
			}
		}
				
		return result;
	}
	// 通过open_id或手机号查询联系人
	@RequestMapping(value="/consumer/getcontacts.html",method=RequestMethod.POST)
	@ResponseBody
	public APPResult getContacts(@RequestParam("token") String token,  @RequestParam("code") String openId, @RequestParam("timestamp") String timestamp) {
		APPResult result = new APPResult();
		if (StringUtils.isEmpty(openId)) {
			result.setStatusCode(FAILCODE);
			result.setMessage("open_id不存在");
		} else {
			HashMap<String,String> params = new HashMap<String,String>();
			params.put("code",openId);
			params.put("timestamp",timestamp);
			if(StringUtil.verifySignature(params,token)) {
				logger.info("REST request to change phone:{}" + params);
				try {
					AccountContactOrgUserDto accountContactOrgUserDto = accountContactService.queryAccountContactByOpenId(openId);
					result.setStatusCode(SUCCESSCODE);
					result.setMessage("success");
					result.setData(accountContactOrgUserDto);
				}catch (BusinessException e) {
					result.setStatusCode(e.getCode());
					result.setMessage(e.getMsg());
					logger.info(e.getMsg());
				}
			} else {
				result.setStatusCode(INVALIDCODE);
				result.setMessage("token失效！");
			}
		}
		return result;
	}
	// 查找所有CBMS大类给积分系统
	@RequestMapping(value="/consumer/getcategorygroups.html",method=RequestMethod.POST)
	@ResponseBody
	public APPResult getCategoryGroups(@RequestParam("token") String token,  @RequestParam("timestamp") String timestamp) {
		APPResult result = new APPResult();

			HashMap<String,String> params = new HashMap<String,String>();
			params.put("timestamp",timestamp);
			if(StringUtil.verifySignature(params, token)) {
				logger.info("REST request to change token:{}" + params);
				try {
					List<CategoryGroup> cateGroupList = categoryGroupService.queryAllCategoryGroupToWechat();
					result.setStatusCode(SUCCESSCODE);
					result.setMessage("success");
					result.setData(cateGroupList);
				}catch (BusinessException e) {
					result.setStatusCode(e.getCode());
					result.setMessage(e.getMsg());
					logger.info(e.getMsg());
				}
			} else {
				result.setStatusCode(INVALIDCODE);
				result.setMessage("token失效！");
			}
		return result;
	}
	// 查找所有CBMS服务中心对应的城市给积分系统
	@RequestMapping(value="/consumer/queryAllBusinessOrg.html",method=RequestMethod.POST)
	@ResponseBody
	public APPResult queryAllBusinessOrg(@RequestParam("token") String token,  @RequestParam("timestamp") String timestamp) {
		APPResult result = new APPResult();

		HashMap<String,String> params = new HashMap<String,String>();
		params.put("timestamp",timestamp);
		if(StringUtil.verifySignature(params,token)) {
			logger.info("REST request to change token:{}" + params);
			try {
				List<com.prcsteel.platform.acl.model.dto.Organization> orgList= organizationService.queryBusinessOrgToWeChat();
				result.setStatusCode(SUCCESSCODE);
				result.setMessage("success");
				result.setData(orgList);
			}catch (BusinessException e) {
				result.setStatusCode(e.getCode());
				result.setMessage(e.getMsg());
				logger.info(e.getMsg());
			}
		} else {
			result.setStatusCode(INVALIDCODE);
			result.setMessage("token失效！");
		}
		return result;
	}


}
