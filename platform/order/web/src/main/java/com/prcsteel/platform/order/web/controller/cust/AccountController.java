package com.prcsteel.platform.order.web.controller.cust;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.prcsteel.platform.common.utils.Tools;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.prcsteel.platform.account.model.dto.AccountDto;
import com.prcsteel.platform.account.model.dto.SellerAccountDto;
import com.prcsteel.platform.account.model.enums.AccountBusinessType;
import com.prcsteel.platform.account.model.enums.AccountTag;
import com.prcsteel.platform.account.model.enums.AccountType;
import com.prcsteel.platform.account.model.enums.AttachmentType;
import com.prcsteel.platform.account.model.model.Account;
import com.prcsteel.platform.account.model.model.AccountBank;
import com.prcsteel.platform.account.model.model.AccountContact;
import com.prcsteel.platform.account.model.model.AccountContractTemplate;
import com.prcsteel.platform.account.model.model.AssignLog;
import com.prcsteel.platform.account.service.AccountBankService;
import com.prcsteel.platform.account.service.AccountContactService;
import com.prcsteel.platform.account.service.AccountContractTemplateService;
import com.prcsteel.platform.account.service.AccountService;
import com.prcsteel.platform.account.service.AssignLogService;
import com.prcsteel.platform.acl.model.model.SysSetting;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.acl.service.SysSettingService;
import com.prcsteel.platform.api.RestTemplateApiInvoker;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.enums.OpType;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.common.service.OpLog;
import com.prcsteel.platform.common.service.OpParam;
import com.prcsteel.platform.common.utils.FileUtil;
import com.prcsteel.platform.common.utils.StringToReplace;
import com.prcsteel.platform.common.vo.PageResult;
import com.prcsteel.platform.common.vo.Result;
import com.prcsteel.platform.order.web.controller.BaseController;
import com.prcsteel.platform.order.web.support.ShiroVelocity;

/**
 * @author Green.Ge
 * @version V1.0
 * @Title: AccountController.java
 * @Package com.prcsteel.cbms.web.controller
 * @Description: 客户模块操作
 * @date 2015年7月13日 上午11:32:27
 */
@Controller
@RequestMapping("/account")
public class AccountController extends BaseController {
	@Resource
	AccountService accountService;
	@Resource
	AccountBankService accountBankService;
	@Resource
	AssignLogService assignLogService;
	@Resource
	AccountContactService accountContactService;
	@Resource
	private SysSettingService sysSettingService;

	@Resource
	private AccountContractTemplateService accountContractTemplateService;
	@Resource
	private RestTemplateApiInvoker restTemplateApiInvoker;


	ShiroVelocity permissionLimit = new ShiroVelocity();
	final String PERMISSION_QUALIFICATE_EDIT = "account:qualificate:edit"; //客户资质权限


	@RequestMapping("/{type}")
	public String index(ModelMap out, @PathVariable String type) {
		out.put("type", Constant.ACCOUNT_TYPE.BUYER.toString());
		return "account/" + type + "/list";
	}

	@RequestMapping("/search")
	public @ResponseBody PageResult loadAccountData(
			@RequestParam("type") String type,
			@RequestParam("accountName") String accountName,
			@RequestParam(value = "contactName", required = false) String contactName,
			@RequestParam("contactTel") String contactTel,
			@RequestParam("start") Integer start,
			@RequestParam("length") Integer length) {

		// 请求参数封装
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String[] types = { AccountType.both.toString(), type };
		paramMap.put("types", types);
		paramMap.put("accountName", accountName);
		paramMap.put("contactName", contactName);
		paramMap.put("contactTel", contactTel);
		paramMap.put("start", start);
		paramMap.put("length", length);
		paramMap.put("managerIds", getUserIds());

		List<Map<String, Object>> list = accountService
				.selectByAccountNameAndContactName(paramMap);
		Integer total = accountService.totalAccount(paramMap);

		PageResult result = new PageResult();
		result.setData(list);
		result.setRecordsFiltered(total);
		result.setRecordsTotal(list.size());

		return result;
	}

	// 创建客户页面
	@RequestMapping("/{type}/create")
	public String createAccount(ModelMap out, @PathVariable String type) {
		List<SysSetting> customerLabel= sysSettingService.queryCustomerLabel();
		out.put("type", type);
		out.put("sellerLabel", customerLabel.size() == 0 ? null : customerLabel.get(0).getSettingValue().split("、"));
		out.put("buyerLabel", customerLabel.size()==0?null:customerLabel.get(1).getSettingValue().split("、"));
		out.put("defaultSellerLabel", customerLabel.size() == 0 ? null : customerLabel.get(0).getDefaultValue());
		out.put("defaultBuyerLabel", customerLabel.size()==0?null:customerLabel.get(1).getDefaultValue());
		out.put("businessTypes", AccountBusinessType.values());
		out.put("hasQualificate", permissionLimit.hasPermission(PERMISSION_QUALIFICATE_EDIT));
		return "account/create";
	}

	// 显示客户
	@RequestMapping("/{type}/{id}")
	public String view(ModelMap out, @PathVariable Long id,
					   @PathVariable String type) {
		AccountDto account = accountService.selectByPrimaryKey(id);
		List<SysSetting> customerLabel= sysSettingService.queryCustomerLabel();
		out.put("sellerLabel", customerLabel.size()==0?null:customerLabel.get(0).getSettingValue().split("、"));
		out.put("buyerLabel",customerLabel.size()==0?null:customerLabel.get(1).getSettingValue().split("、"));
		out.put("businessTypes", AccountBusinessType.values());
		out.put("account", account);
		out.put("type", type);

		out.put("hasQualificate", permissionLimit.hasPermission(PERMISSION_QUALIFICATE_EDIT));

		return "account/view";
	}

	/**
	 * 判断客户是否存在
	 *
	 * @param out
	 * @param account
	 * @return
	 */
	@RequestMapping("/selectAcoount")
	@ResponseBody
	public HashMap<String, Object> selectAcoount(ModelMap out, Account account) {
		HashMap<String, Object> result = new HashMap<String, Object>();
		if (StringUtils.isNotEmpty(account.getName())) {
			Account accountModl = accountService.selectAccountByName(account
					.getName().replaceAll(" ", ""));
			if (accountModl != null) {
				if (account.getType().equals(accountModl.getType())) {
					result.put("data",
							"【" + account.getName().replaceAll(" ", "")
									+ "】已存在！请更换公司名称再试");
					return result;
				}
				if (AccountType.buyer.toString().equals(accountModl.getType())) {
					result.put("data",
							"【" + account.getName().replaceAll(" ", "")
									+ "】已存在买家客户中，是否为其添加卖家身份并完善买家资料？");
					result.put("code", 0);
					return result;
				} else if (AccountType.seller.toString().equals(
						accountModl.getType())) {
					result.put("data",
							"【" + account.getName().replaceAll(" ", "")
									+ "】已存在卖家客户中，是否为其添加买家身份？");
					result.put("code", 1);
					return result;
				} else {
					result.put("data",
							"【" + account.getName().replaceAll(" ", "")
									+ "】已经是买家和卖家！请更换公司名称再试");
					return result;
				}

			}
		}
		return result;
	}

	/**
	 * 判断联系人是否存在
	 *
	 * @return
	 */
	@RequestMapping("/selectContact")
	@ResponseBody
	public Result selectContact(String name, String tel) {
		Result result = new Result();
		AccountContact contact = accountContactService.queryByTel(tel.replaceAll(" ",
				""));
		if (contact != null
				&& name.replaceAll(" ", "").equals(contact.getName())) {
			result.setSuccess(true);
			result.setData(contact);
		} else {
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 添加买家客户时已存在卖家客户中，修改其类型并跳转页面
	 *
	 * @param account
	 * @return
	 */
	@RequestMapping("/update/buyer")
	@ResponseBody
	public HashMap<String, Object> updateAccount(Account account) {
		HashMap<String, Object> result = new HashMap<String, Object>();
		try {
			Long accountId = accountContactService.updateAccount(account,
					getLoginUser());
			result.put("success", true);
			result.put("data", accountId);
		} catch (BusinessException e) {
			result.put("success", false);
			result.put("data", e.getMsg());
		}
		return result;
	}

	/**
	 * 添加卖家客户时已存在买家客户中，将买家资料填充到页面中
	 *
	 * @param account
	 * @return
	 */
	@RequestMapping("/update/seller")
	@ResponseBody
	public HashMap<String, Object> updateAccountSeller(Account account) {
		HashMap<String, Object> result = new HashMap<String, Object>();
		Account accountModel = accountService.selectAccountByName(account
				.getName().trim());
		result.put("success", true);
		result.put("data", accountModel);
		return result;
	}

	// 保存客户
	@OpLog(OpType.SaveAccount)
	@OpParam("account")
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public HashMap<String, Object> saveAccount(ModelMap out,
											   MultipartHttpServletRequest request, Account account) {
		HashMap<String, Object> result = new HashMap<String, Object>();
		if(account.getName()!=null)
			account.setName(account.getName().replaceAll(" ", ""));
		if (account.getTaxCode().length() > 20) {
			result.put("data", "税号长度超过20个字符，请重新填写！");
			return result;
		}
		try {
			AccountType.valueOf(account.getType());
		} catch (Exception e) {
			result.put("data", "客户类型提交有误");
			return result;
		}

		if (account.getId() == null) {
			if ("".equals(account.getName().replaceAll(" ", ""))) {
				result.put("data", "请输入公司全称");
				return result;
			} else {
				account.setName(account.getName().replaceAll(" ", ""));
			}
			if (accountService.selectAccountByName(account.getName()
					.replaceAll(" ", "")) != null) {
				result.put("data", "【" + account.getName().replaceAll(" ", "")
						+ "】已存在！请更换公司名称再试");
				return result;
			}
		}
		AccountDto dto = new AccountDto();
		MultipartFile license = request.getFile("pic_license");
		MultipartFile orgCode = request.getFile("pic_org_code");
		MultipartFile taxReg = request.getFile("pic_tax_reg");
		MultipartFile taxpayerEvidence = request
				.getFile("pic_taxpayer_evidence");
		MultipartFile invoiceData = request.getFile("pic_invoice_data");
		MultipartFile legalID = request.getFile("pic_legal_ID");
		MultipartFile paymentData = request.getFile("pic_payment_data");
		List<MultipartFile> consignContract = request
				.getFiles("pic_consign_contract");
		MultipartFile enterpriseSurvey = request
				.getFile("pic_enterprise_survey");
		MultipartFile openAccountLicense = request
				.getFile("pic_open_account_license");

		List<MultipartFile> attachmentList = new ArrayList<MultipartFile>();
		if (license != null && !license.isEmpty()) {
			if (!checkUploadAttachment(license, result, attachmentList)) {
				return result;
			}
		}
		if (orgCode != null && !orgCode.isEmpty()) {
			if (!checkUploadAttachment(orgCode, result, attachmentList)) {
				return result;
			}
		}
		if (taxpayerEvidence != null && !taxpayerEvidence.isEmpty()) {
			if (!checkUploadAttachment(taxpayerEvidence, result, attachmentList)) {
				return result;
			}
		}
		if (taxReg != null && !taxReg.isEmpty()) {
			if (!checkUploadAttachment(taxReg, result, attachmentList)) {
				return result;
			}
		}
		if (invoiceData != null && !invoiceData.isEmpty()) {
			if (!checkUploadAttachment(invoiceData, result, attachmentList)) {
				return result;
			}
		}
		if (legalID != null && !legalID.isEmpty()) {
			if (!checkUploadAttachment(legalID, result, attachmentList)) {
				return result;
			}
		}
		if (paymentData != null && !paymentData.isEmpty()) {
			if (!checkUploadAttachment(paymentData, result, attachmentList)) {
				return result;
			}
		}
		if (consignContract != null && !consignContract.isEmpty()) {
			// 代运营合同可以上传多张
			for (MultipartFile file : consignContract) {
				if (!checkUploadAttachment(file, result, attachmentList)) {
					return result;
				}
			}
			account.setConsignType("consign");// 代运营客户
		} else {
			if (account.getId() == null) {
				account.setConsignType("temp");// 临采客户
			}
		}
		if (enterpriseSurvey != null && !enterpriseSurvey.isEmpty()) {
			if (!checkUploadAttachment(enterpriseSurvey, result, attachmentList)) {
				return result;
			}
		}
		if (openAccountLicense != null && !openAccountLicense.isEmpty()) {
			if (!checkUploadAttachment(openAccountLicense, result,
					attachmentList)) {
				return result;
			}
		}
		dto.setManager(getLoginUser());
		account.setLicenseCode(StringToReplace.toReplaceAll(account
				.getLicenseCode()));
		account.setRegAddress(StringToReplace.toReplaceAll(account
				.getRegAddress()));
		account.setOrgCode(StringToReplace.toReplaceAll(account.getOrgCode()));
		account.setBankCode(StringToReplace.toReplaceAll(account.getBankCode()));
		account.setBankNameMain(StringToReplace.toReplaceAll(account
				.getBankNameMain()));
		account.setBankNameBranch(StringToReplace.toReplaceAll(account
				.getBankNameBranch()));
		account.setAccountCode(StringToReplace.toReplaceAll(account
				.getAccountCode()));
		account.setTaxCode(StringToReplace.toReplaceAll(account
				.getTaxCode()));

		account.setName(Tools.toDBC(account.getName()));//客户名称全角转换到半角 modify by wangxianjun
		dto.setAccount(account);
		// 判断新增还是修改
		try {
			AccountContact contact = null;
			if (account.getId() == null
					|| account.getType().equals(AccountType.both.toString())) {
				contact = new AccountContact();
				contact.setIsMain(Integer.valueOf(Constant.YES));
				// contact.setAccountId(account.getId());
				contact.setManager(getLoginUser().getId());
				contact.setStatus(Integer.valueOf(Constant.YES));
				if (account.getId() == null) {
					contact.setType(account.getType());
				} else {
					contact.setType(AccountType.seller.toString());
				}
				contact.setName(request.getParameter("contactName").toString());
				contact.setTel(request.getParameter("contactTel").toString());
				contact.setDeptName(StringUtils.isBlank(request.getParameter(
						"contactDeptName").toString()) ? null : request
						.getParameter("contactDeptName").toString());
				contact.setEmail(StringUtils.isBlank(request.getParameter(
						"contactEmail").toString()) ? null : request
						.getParameter("contactEmail").toString());
				contact.setQq(StringUtils.isBlank(request.getParameter(
						"contactQq").toString()) ? null : request.getParameter(
						"contactQq").toString());
				contact.setNote(StringUtils.isBlank(request.getParameter(
						"contactNote").toString()) ? null : request
						.getParameter("contactNote").toString());
			}
			if (account.getId() == null) {
				result.put("success", true);
				result = accountService.insert(dto, attachmentList, contact);
			} else {
				result = accountService.updateByPrimaryKeySelective(dto,
						attachmentList, contact);
			}
		} catch (BusinessException e) {
			result.put("success", false);
			result.put("data", e.getMsg());
		}

		return result;
	}

	private boolean checkUploadAttachment(MultipartFile file,
										  HashMap<String, Object> result, List<MultipartFile> attachmentList) {
		String suffix = FileUtil.getFileSuffix(file.getOriginalFilename());

		if (suffix == null
				|| !Constant.IMAGE_SUFFIX.contains(suffix.toLowerCase())) {
			result.put("data", AttachmentType.valueOf(file.getName()).getName()
					+ "文件格式不正确");
			return false;
		}
		if (file.getSize() / Constant.M_SIZE > Constant.MAX_IMG_SIZE) {
			result.put("data", AttachmentType.valueOf(file.getName()).getName()
					+ "超过" + Constant.MAX_IMG_SIZE + "M");
			return false;
		}
		attachmentList.add(file);
		return true;
	}

	// 合同模板list
	@RequestMapping("/{type}/{accountId}/contracttemplate")
	public String contractTemplateList(ModelMap out, @PathVariable String type,
									   @PathVariable Long accountId) {
		//TODO：
		//1. 读取部门信息
		//2. 读取系统模板
		//3. 读取自定义模板

		List<List<AccountContractTemplate>> templates = new LinkedList<>();
		List<AccountContractTemplate> sysTemp = accountContractTemplateService.querySysTemplate();
		for (AccountContractTemplate temp : sysTemp){
			List<AccountContractTemplate> list = new LinkedList<>();

			AccountContractTemplate act = new AccountContractTemplate();
			act.setType(temp.getType());
			act.setAccountId(accountId);
			List<AccountContractTemplate> customTemplates = accountService.selectCTByModel(act);

			boolean b = hasDefaultTemplate(customTemplates);
			if(customTemplates.isEmpty() && !b) {
				temp.setEnabled(1);//置为默认
			}

			list.add(temp);//系统模板
			list.addAll(customTemplates);//自定义模板

			templates.add(list);
		}

		out.put("templates", templates);
		out.put("accountId", accountId);

		return "account/contracttemplate/list";
	}

	private boolean hasDefaultTemplate(List<AccountContractTemplate> templates){
		for (AccountContractTemplate template : templates){
			if (template.getEnabled().equals(1)){
				return true;
			}
		}

		return false;
	}

	// 合同模板新建页
	@RequestMapping("/{type}/{accountId}/contracttemplate/create")
	public String createContractTemplate(ModelMap out,
										 @PathVariable String type, @PathVariable Long accountId) {
		out.put("type", type);
		out.put("accountId", accountId);
		out.put("account", accountService.selectByPrimaryKey(accountId)
				.getAccount());
		return "account/contracttemplate/create";
	}

	// 合同模板保存
	@OpLog(OpType.SaveContractTemplate)
	@OpParam("act")
	@RequestMapping(value = "/contracttemplate/save", method = RequestMethod.POST)
	@ResponseBody
	public Result templateDetail(Long id, String name, String content) {
		AccountContractTemplate act = new AccountContractTemplate();
		act.setId(id);
		act.setName(name);
		act.setContent(content);

		User user = getLoginUser();

		act.setLastUpdatedBy(user.getLoginId());
		act.setLastUpdated(new Date());
		if (act.getId()==null){
			act.setCreatedBy(user.getLoginId());
			act.setCreated(new Date());
		}

		return restTemplateApiInvoker.save(act);
	}

	// 合同模板修改页
	@OpLog(OpType.ContractTemplateEdit)
	@OpParam("type")
	@OpParam("id")
	@RequestMapping(value = "/{action}/{type}/{accountId}/contracttemplate/{id}", method = RequestMethod.GET)
	public String contractTemplateEdit(ModelMap out, @PathVariable Long accountId, @PathVariable String action, @PathVariable String type,
									   @PathVariable Long id) {
		Result act = restTemplateApiInvoker.query(id);
		Result detail = restTemplateApiInvoker.resolve(accountId, id, type);

		out.put("account", accountService.selectByPrimaryKey(accountId).getAccount());
		out.put("act", act.getData());
		out.put("detail", detail.getData());
		out.put("action", action);

		return "account/contracttemplate/edit";
	}

	@RequestMapping("/contracttemplate/enabled/{id}")
	public Result enabledContractTemplate(@PathVariable Long id, @RequestParam("p[]") List<Long> ids, Boolean isEnabled) {
		Result res = restTemplateApiInvoker.enabled(id, isEnabled);

		return res;
	}

	// 买家银行信息
	@RequestMapping("/buyer/{accountId}/bankinfo")
	public String buyerBankInfo(ModelMap out, @PathVariable Long accountId) {
		AccountDto account = accountService.selectByPrimaryKey(accountId);
		account.getAccount().setType(Constant.ACCOUNT_TYPE.BUYER.toString()); // 卖家也是买家
		out.put("account", account);
		out.put("type", AccountType.buyer.toString()); // 买家
		return "account/bankinfo";
	}

	// 卖家银行信息
	@RequestMapping("/seller/{accountId}/bankinfo")
	public String sellerBankInfo(ModelMap out, @PathVariable Long accountId) {
		AccountDto account = accountService.selectByPrimaryKey(accountId);
		out.put("account", account);
		out.put("type", AccountType.seller.toString()); // 卖家
		return "account/bankinfo";
	}

	// 银行信息
	@RequestMapping(value = "/bankinfo", method = { RequestMethod.POST })
	public @ResponseBody PageResult bankInfo(
			@RequestParam("accountId") Long accountId) {
		PageResult result = new PageResult();
		List<AccountBank> list = accountBankService.queryByAccountId(accountId);
		Integer total = list.size();
		result.setData(list);
		result.setRecordsFiltered(total);
		result.setRecordsTotal(total);
		return result;
	}

	@RequestMapping("/{type}/{accountId}/assignlog")
	public String assignlog(ModelMap out, @PathVariable Long accountId,
							@PathVariable String type) {
		out.put("account", accountService.selectByPrimaryKey(accountId));
		out.put("type", type);
		return "account/buyer/assignlog";
	}

	@RequestMapping("/update/bank/info.html")
	@ResponseBody
	public Result updateBank(@RequestParam("accountId") Long accountId,
							 @RequestParam("bankId") Long bankId) {
		Result result = new Result();
		try {
			accountBankService.updateBankById(accountId, bankId);
		} catch (BusinessException e) {
			result.setSuccess(false);
			result.setData(e.getMsg());
			return result;
		}
		result.setSuccess(true);
		return result;
	}

	@RequestMapping(value = "/ajaxassignlog", method = RequestMethod.POST)
	public @ResponseBody PageResult ajaxAssignLog(
			@RequestParam("accountId") Long accountId,
			@RequestParam("start") int start, @RequestParam("length") int length) {
		PageResult result = new PageResult();
		List<AssignLog> list = assignLogService.query(accountId, start, length);
		Integer total = assignLogService.count(accountId);
		result.setData(list);
		// result.setDraw(1);
		result.setRecordsFiltered(total);
		result.setRecordsTotal(total);
		return result;
	}

	@RequestMapping("/{accountId}/lockandunlock")
	@OpLog(OpType.LockandunlockAccount)
	@OpParam("accountId")
	@OpParam("status")
	public @ResponseBody Result lockAndUnlock(
			@RequestParam("status") Integer status, @PathVariable Long accountId) {
		Result result = new Result();
		boolean updateResult = accountService.updateStatusByPrimaryKey(
				accountId, status, getLoginUser().getLoginId());
		result.setSuccess(updateResult);
		return result;
	}

	// 卖家客户分组查询
	@RequestMapping("/sellersearch")
	public String sellerSearch() {
		return "account/sellersearch";
	}

	// 卖家客户分组查询数据
	@RequestMapping(value = "/sellersearchdata", method = RequestMethod.POST)
	public @ResponseBody PageResult sellerSearchData(
			@RequestParam("start") Integer start,
			@RequestParam("length") Integer length,
			@RequestParam("companyName") String companyName,
			@RequestParam("business") String business,
			@RequestParam("provinceId") Integer provinceId,
			@RequestParam("cityId") Integer cityId,
			@RequestParam("status") Integer status) {
		PageResult result = new PageResult();

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("companyName", companyName);
		paramMap.put("business", business);
		paramMap.put("provinceId", provinceId);
		paramMap.put("cityId", cityId);
		paramMap.put("status", status);

		paramMap.put("start", start);
		paramMap.put("length", length);

		List<SellerAccountDto> list = accountService.listSellerSearch(paramMap,
				getUserIds());
		Integer total = accountService.totalSellerSearch(paramMap);
		result.setData(list);
		result.setRecordsFiltered(total);
		result.setRecordsTotal(list.size());
		return result;
	}

	/**
	 * 根据公司名称模糊查询
	 */
	@RequestMapping(value = "/searchaccount", method = RequestMethod.POST)
	public @ResponseBody PageResult searchAccount(
			@RequestParam("accountName") String accountName,
			@RequestParam("cardInfoStatus") String cardInfoStatus,
			@RequestParam("type") String type) {
		PageResult result = new PageResult();

		Integer start = 0;
		Integer length = 10;
		Long accountTag=0L;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("name", accountName);
		if (StringUtils.isNotEmpty(type)) {
			// 根据accountTag去查下客户类型，而不是type
//			String[] types = {AccountType.both.toString(), type};
//			paramMap.put("types", types);
			if (AccountType.buyer.toString().equals(type)) {
				accountTag = AccountTag.buyer.getCode();
			}
			if (AccountType.seller.toString().equals(type)) {
				accountTag = AccountTag.seller.getCode() | accountTag;
			}
			paramMap.put("accountTag", accountTag);
		}
		if (StringUtils.isNotEmpty(cardInfoStatus)) {
			paramMap.put("cardInfoStatus", cardInfoStatus);
		}
		paramMap.put("start", start);
		paramMap.put("length", length);

		List<Account> list = accountService.listAccountByName(paramMap);
		Integer total = 0;// accountService.totalListAccountByName(paramMap);
		result.setData(list);
		result.setRecordsFiltered(total);
		result.setRecordsTotal(list.size());
		return result;
	}
}
