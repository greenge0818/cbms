package com.prcsteel.platform.account.web.controller;

import com.prcsteel.platform.account.model.dto.AccountAssignLogDto;
import com.prcsteel.platform.account.model.dto.ContactAssignDto;
import com.prcsteel.platform.account.model.query.AccountContactAssignLogQuery;
import com.prcsteel.platform.account.service.AccountAssignService;
import com.prcsteel.platform.common.aspect.OpAction;
import com.prcsteel.platform.acl.model.model.Organization;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.acl.service.OrganizationService;
import com.prcsteel.platform.common.enums.OpType;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.common.service.OpLog;
import com.prcsteel.platform.common.service.OpParam;
import com.prcsteel.platform.common.vo.PageResult;
import com.prcsteel.platform.common.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chengui on 2016/3/16.
 */
@Controller
@RequestMapping("/account/contact")
public class AccountContactController extends BaseController{
	
	@Autowired
	private AccountAssignService accountAssignService;
	@Autowired
	private OrganizationService organizationService;
	
	@RequestMapping("userIds.html")
	@ResponseBody
	public Result userIds(Long orgId) {
		Result result = new Result();
		result.setData(accountAssignService.queryalls(orgId));
		result.setSuccess(true);
		return result;
	}
	
	/**
	 * 联系人划转列表
	 * 
	 * @param out
	 */
	@RequestMapping("accountcontact.html")
	public void indexContact(ModelMap out) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("userIds", getUserIds());
		List<ContactAssignDto> list = accountAssignService.findByIds(paramMap);
		out.put("list", list);
	}
	
	/**
	 * 划转联系人
	 * 
	 * @param contactIds
	 *            联系人ID
	 * @param accountIds
	 *            客户ID
	 * @param contactName
	 *            联系人
	 * @param uid
	 *            现任交易员ID
	 * @param managerExIds
	 *            前任交易员ID
	 * @param nameNext
	 *            现任交易员
	 * @param nameExs
	 *            前任交易员
	 * @return
	 */
	@OpAction( key = "uid")
	@RequestMapping(value = "updcontact.html")
	@OpLog(OpType.UpdateContact)
	@OpParam("contactIds")
	@OpParam("accountIds")
	@OpParam("managerExIds")
	@OpParam("nameExs")
	@OpParam("nameNext")
	@ResponseBody
	public Result updateAdd(@RequestParam("contactIds") String contactIds,
			@RequestParam("accountIds") String accountIds,
			@RequestParam("contactName") String contactName,
			@RequestParam("uid") Long uid,
			@RequestParam("managerExIds") String managerExIds,
			@RequestParam("nameNext") String nameNext,
			@RequestParam("nameExs") String nameExs,
			@RequestParam("type") String type) {
		Result result = new Result();
		// 获取当前登录用户
		User user = getLoginUser();
		try {
			accountAssignService.updateContact(contactIds, managerExIds, uid,
					accountIds, nameExs, nameNext, contactName, user, type);
		} catch (BusinessException e) {
			result.setSuccess(false);
			result.setData(e.getMsg());
			return result;
		}
		result.setSuccess(true);
		return result;
	}
	
	/**
	 * 进入 联系人划转历史 页面
	 * 
	 * @param out
	 */
	@RequestMapping("accountcontactassignlog.html")
	public void contactAssignLog(ModelMap out) {
		List<Organization> list = new ArrayList<Organization>();
		if(CollectionUtils.isEmpty(getUserIds())){
			list = organizationService.queryAllBusinessOrg(); 
		}else{
			list.add(getOrganization());
		}
		out.put("orgs",list);
	}
	
	 /**
     * 加载联系人划转历史数据
     * @return
     */
    @RequestMapping("/loadAccountContactAssignLog.html")
    @ResponseBody
    public PageResult loadAccountContactAssignLog(AccountContactAssignLogQuery query){
    	query.setUserIds(getUserIds());
        int total = accountAssignService.queryAccountContactAssignLogTotalByParam(query);
        List<AccountAssignLogDto> list = accountAssignService.queryAccountContactAssignLogByParam(query);
        return new PageResult(list.size(),total,list);
    }

	
}
