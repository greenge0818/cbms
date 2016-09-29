package com.prcsteel.platform.order.web.controller.cust;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.prcsteel.platform.account.model.dto.AccountAssignDto;
import com.prcsteel.platform.account.model.dto.ContactAssignDto;
import com.prcsteel.platform.account.service.AccountAssignService;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.common.enums.OpType;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.common.service.OpLog;
import com.prcsteel.platform.common.service.OpParam;
import com.prcsteel.platform.common.vo.Result;
import com.prcsteel.platform.order.web.controller.BaseController;

/**
 * Created by lixiang on 2015/7/17.
 */
@Controller
@RequestMapping("/account/")
public class AccountAssignController extends BaseController {

	@Autowired
	private AccountAssignService accountAssignService;

	@RequestMapping("userIds.html")
	@ResponseBody
	public Result userIds(Long orgId) {
		Result result = new Result();
		result.setData(accountAssignService.queryalls(orgId));
		result.setSuccess(true);
		return result;
	}

	/**
	 * 划转买家客户列表
	 * 
	 * @param out
	 * @param id
	 */
	@RequestMapping("seller/accountassign.html")
	public void index(ModelMap out, Long id) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("userIds", getUserIds());
		List<AccountAssignDto> list = accountAssignService.findAll(paramMap);
		if (id != null) {
			for (AccountAssignDto dto : list) {
				if (dto.getId().equals(id)) {
					list.clear();
					list.add(dto);
					break;
				}
			}
		}
		List<User> userList = accountAssignService.queryalls(null);
		out.put("list", list);
		out.put("userList", userList);
	}

	/**
	 * 划转卖家列表
	 * 
	 * @param out
	 * @param id
	 */
	@RequestMapping("buyer/accountassign.html")
	public void indexbuyer(ModelMap out, Long id) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("userIds", getUserIds());
		List<AccountAssignDto> list = accountAssignService.findByType(paramMap);
		if (id != null) {
			for (AccountAssignDto dto : list) {
				if (dto.getId().equals(id)) {
					list.clear();
					list.add(dto);
					break;
				}
			}
		}
		List<User> userList = accountAssignService.queryalls(null);
		out.put("list", list);
		out.put("userList", userList);
	}

	/**
	 * 划转买家卖家客户
	 * 
	 * @param accountIds
	 *            客户ID
	 * @param uid
	 *            现任交易员ID
	 * @param managerExIds
	 *            前任交易员ID
	 * @param nameNext
	 *            现任交易员
	 * @param nameExs
	 *            前任交易员
	 * @param type
	 *            分配类型(buyer or seller or both)
	 * @return
	 */
	@RequestMapping("updtransfer.html")
	@OpLog(OpType.UpdateAccount)
	@OpParam("accountIds")
	@OpParam("uid")
	@OpParam("managerExIds")
	@OpParam("nameExs")
	@OpParam("nameNext")
	@OpParam("type")
	@ResponseBody
	public Result indexs(@RequestParam("accountIds") String accountIds,
			@RequestParam("uid") Long uid,
			@RequestParam("managerExIds") String managerExIds,
			@RequestParam("nameNext") String nameNext,
			@RequestParam("nameExs") String nameExs,
			@RequestParam("type") String type) {
		Result result = new Result();
		// 获取当前登录用户
		User user = getLoginUser();
		try {
			accountAssignService.transferForBuyerCustomer(accountIds,
					managerExIds, nameExs, uid, nameNext, user);
		} catch (BusinessException e) {
			result.setSuccess(false);
			result.setData(e.getMsg());
			return result;
		}
		result.setSuccess(true);
		return result;
	}

	/**
	 * 买家联系人列表
	 * 
	 * @param out
	 */
	@RequestMapping("contact/accountcontact.html")
	public void indexcontact(ModelMap out) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("userIds", getUserIds());
		List<ContactAssignDto> list = accountAssignService.findByIds(paramMap);
		List<User> userList = accountAssignService.queryalls(null);
		out.put("list", list);
		out.put("userList", userList);
	}

	/**
	 * 划转联系人
	 * 
	 * @param contactIds
	 *            联系人ID
	 * @param accountId
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
	@RequestMapping(value = "contact/updcontact.html")
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

}
