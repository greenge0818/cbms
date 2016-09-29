package com.prcsteel.platform.account.web.controller;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.prcsteel.platform.account.api.RestAcceptDraftService;
import com.prcsteel.platform.account.api.RestPayRequestService;
import com.prcsteel.platform.account.api.RestSettlementService;
import com.prcsteel.platform.account.model.dto.AccountDto;
import com.prcsteel.platform.account.model.dto.AccountTransLogDto;
import com.prcsteel.platform.account.model.dto.ContactAssignDto;
import com.prcsteel.platform.account.model.dto.ContactDto;
import com.prcsteel.platform.account.model.dto.ContactWithPotentialCustomerDto;
import com.prcsteel.platform.account.model.dto.CustAccountDto;
import com.prcsteel.platform.account.model.dto.UpdateAssignPotentialcustomer;
import com.prcsteel.platform.account.model.enums.AccountBankDataStatus;
import com.prcsteel.platform.account.model.enums.AccountTransApplyType;
import com.prcsteel.platform.account.model.model.Account;
import com.prcsteel.platform.account.model.model.AccountBank;
import com.prcsteel.platform.account.model.model.AccountContact;
import com.prcsteel.platform.account.model.model.Contact;
import com.prcsteel.platform.account.model.query.AccountInfoFlowSearchQuery;
import com.prcsteel.platform.account.model.query.ContactWithPotentialCustomerQuery;
import com.prcsteel.platform.account.service.AccountAssignService;
import com.prcsteel.platform.account.service.AccountBankService;
import com.prcsteel.platform.account.service.AccountContactService;
import com.prcsteel.platform.account.service.AccountFundService;
import com.prcsteel.platform.account.service.AccountService;
import com.prcsteel.platform.account.service.AccountTransLogService;
import com.prcsteel.platform.account.service.ContactService;
import com.prcsteel.platform.account.web.mq.MarketAddUserSender;
import com.prcsteel.platform.account.web.mq.MarketDisableUserSender;
import com.prcsteel.platform.account.web.mq.MarketUpdateUserSender;
import com.prcsteel.platform.acl.model.model.Organization;
import com.prcsteel.platform.common.aspect.OpAction;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.common.utils.Tools;
import com.prcsteel.platform.common.vo.PageResult;
import com.prcsteel.platform.common.vo.Result;
import com.prcsteel.platform.order.model.enums.AcceptDraftStatus;
import com.prcsteel.platform.order.model.model.AcceptDraft;

/** 
 * 账户信息controller
 * 
 * @author peanut <p>2016年3月11日 上午9:06:12</p>  
 */
@Controller
@RequestMapping("/accountinfo")
public class AccountInfoController extends BaseController{
	private final int MAX_RECORDS_LENGTH = 999999;
	@Resource
	private AccountService accountService;
	@Resource
	private ContactService contactService;
	@Resource
	private AccountBankService accountBankService;
	@Resource
	private RestPayRequestService restPayRequestService;
	@Resource
	private AccountTransLogService accountTransLogService;
	@Resource
	private RestSettlementService restSettlementService;
	@Resource
	private AccountFundService accountFundService;
	@Resource
	private RestAcceptDraftService restAcceptDraftService;
	@Resource
	private AccountAssignService accountAssignService;
	@Resource
	private AccountContactService accountContactService;

	@Resource
	private MarketAddUserSender marketAddUserSender;
	@Resource
	private MarketDisableUserSender marketDisableUserSender;
	@Resource
	private MarketUpdateUserSender marketUpdateUserSender;
	
	private static final Logger logger = LoggerFactory.getLogger(AccountInfoController.class);
	
	/**
	 * 保存分配资金和撤回
	 * @param accountList
	 * @return
	 */
	@OpAction( key = "accountId")
	@ResponseBody
	@RequestMapping(value = "saveSum", method = RequestMethod.POST)
	public Result saveInquiryOrder(String accountList,Long accountId){
		Result result = new Result();
        try {
        	List<Account> accounts = accountService.saveFundAllocations(accountList,getLoginUser(),accountId);
        	result.setData(accounts);
        } catch (BusinessException e) {
            result.setSuccess(false);
            result.setData(e.getMsg());
        }
        return result;
	}
	
    /**
     * 企业基本信息-账户信息
     *
     * @param out
     * @param accountId
     * @return
     */
    @RequestMapping("{accountId}/accountinfo")
    public String accountInfo(ModelMap out, @PathVariable("accountId") Long accountId) {
    	  AccountDto accountdto = contactService.getCompanyById(accountId);
		  // 查询操作员有权限的 归属服务中心的部门
		if(!CollectionUtils.isEmpty(getUserIds())){
			  accountdto.setDepartment(accountService.queryDeptByCompanyIdAndBelongOrg(accountId, getOrganization().getId()));
		  }
		  List<AcceptDraft> acceptDraftList = restAcceptDraftService.query(accountId, AcceptDraftStatus.CHARGED.getCode());
		  out.put("accountdto", accountdto);
		  out.put("acceptDraftList", acceptDraftList);
		  out.put("accountApplyType", AccountTransApplyType.values());
		  
		  Calendar cal = Calendar.getInstance(); 
		  cal.setTime(new Date()); 
		  cal.set(Calendar.DAY_OF_MONTH, 1); 
		  //默认时间从当月1号到当前时间
		  out.put("startTime",  new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime()));
		  out.put("endTime",  Tools.dateToStr(new Date(), "yyyy-MM-dd"));
		  return "accountinfo/accountInfo";
    }

	/**
	 * 资金账户提现
	 * @param out
	 * @param departmentId
	 * @return
	 */
	@RequestMapping("/{id}/withdrawal")
	public String withdrawal(ModelMap out, @PathVariable("id") Long departmentId){
		Account department = accountService.queryById(departmentId);  //部门
		Account company = accountService.queryById(department.getParentId());  //公司
		out.put("department", department);
		out.put("balance", department.getBalance());   //TODO: 此处需要减掉已使用信用额度
		out.put("company", company);
		out.put("bankInfo", accountBankService.queryAllEnabledBankByAccountId(company.getId()));   //银行信息用公司id去查
		return "accountinfo/withdrawal";
	}

	/**
	 * 资金账户提现申请
	 * @return
	 */
	@OpAction( key = "departmentId")
	@ResponseBody
	@RequestMapping("/withdrawalapply")
	public Result withdrawalApply(Long departmentId, Long bankId, Double amount, Double balance){
		// 资金账户提现针对已信用额度还款 lixiang
		Result result = new Result();
		try {
			accountFundService.payForCredit(departmentId, null, null, 0L, Constant.SYSTEMNAME, new Date());
		} catch (BusinessException e) {
			result.setSuccess(false);
			result.setData(e.getMsg());
			return result;
		}catch (Exception e) {
			result.setSuccess(false);
			result.setData(e.getMessage());
			return result;
		}
		return restPayRequestService.saveApply(departmentId, bankId, amount, balance, getLoginUser().getId());
	}

	/**
	 * 账户信息流水列表查询
	 * @param accountInfoFlowSearchQuery
	 * @return
	 */
	@ResponseBody
	@RequestMapping("search")
	public PageResult search(AccountInfoFlowSearchQuery accountInfoFlowSearchQuery){
		PageResult result= new PageResult();
		Integer total=accountTransLogService.countFlowByQuery(accountInfoFlowSearchQuery);
		result.setRecordsFiltered(total);
		if (total > 0) {
			List<AccountTransLogDto> list = accountTransLogService.searchFlowByQuery(accountInfoFlowSearchQuery);
			result.setData(list);
			result.setRecordsTotal(list.size());
		} else {
			result.setData(new ArrayList<>());
			result.setRecordsTotal(0);
		}
			/*pr.setData(list);
			pr.setRecordsFiltered(total);
			pr.setRecordsTotal(list.size());*/
		//}
		return result;
	}

	/**
	 * 账户信息流水导出
	 * @param accountInfoFlowSearchQuery
	 * @return
	 */
	@ResponseBody
	@RequestMapping("exportexcel")
	public ModelAndView exportExcel(AccountInfoFlowSearchQuery accountInfoFlowSearchQuery) {
		ModelAndView mav=null;
		accountInfoFlowSearchQuery.setLength(MAX_RECORDS_LENGTH);
		try {
			mav =accountTransLogService.doExportExcel(accountInfoFlowSearchQuery);
		} catch (Exception e) {
			logger.debug("账户信息导出EXCEL出错：", e);
		}
		return mav;
	}
	
	/**
	 * 设置默认时间段：当前月
	 * 
	 * @param out
	 **/
	private void setDefaultTime(ModelMap out) {
		// 日期:开始时间、结束时间(当前月第一天、当前时间)
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, 1);
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		// 界面默认显示的开单时间
		out.put("startTime", format.format(cal.getTime()));
		out.put("endTime", format.format(new Date()));
	}
	
	/**
	 * 访问二次结算应付详情
	 * @return
	 */
	@RequestMapping("/{id}/settlementpayabledetail")
	public String settlementPayableDetail(ModelMap out, @PathVariable("id") Long id) {
		CustAccountDto custAccount = accountService.queryForAccountId(id);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("accountId", custAccount.getAccountId());
		map.put("bankDataStatus", AccountBankDataStatus.Approved.getCode());
 		List<AccountBank> bankList = accountBankService.query(map);
		if (custAccount != null) {
			out.put("custAccount", custAccount);
		}
		out.put("bankList", bankList);
		setDefaultTime(out);
		return "accountinfo/settlementpayabledetail";
	}
	
	/**
	 * 账户信息流水列表查询
	 * @param accountInfoFlowSearchQuery
	 * @return
	 */
	@ResponseBody
	@RequestMapping("settlementsearch")
	public PageResult settlementSearch(AccountInfoFlowSearchQuery accountInfoFlowSearchQuery){
		List<AccountTransLogDto> list = accountTransLogService.getAccountInfoSettlementFlows(accountInfoFlowSearchQuery);
		PageResult pageResult= new PageResult();
		if (list != null && list.size() > 1) {
			Integer total = list.get(list.size() - 1).getTotal();
			// 去掉最后一行统计数据
			list.remove(list.size() - 1);
			pageResult.setData(list);
			pageResult.setRecordsFiltered(total);
			pageResult.setRecordsTotal(total);
		}
		return pageResult;
	}
	
	/**
	 * 二结账户提现申请
	 * @return
	 */
	@OpAction( key = "accountId")
	@ResponseBody
	@RequestMapping("/settlementalapply")
	public Result settlementAlapply(Long accountId, Long departmentId, Long bankId, String accountName, BigDecimal deductionPay, 
			BigDecimal balanceSecondSettlemento){
		return restSettlementService.savePayRequst(accountId, departmentId, deductionPay, balanceSecondSettlemento, bankId, getLoginUser().getId());
	}

	/**
	 * 信用额度抵扣或还款
	 * @return
	 */
	@OpAction( key = "accountId")
	@ResponseBody
	@RequestMapping(value = "payForCredit", method = RequestMethod.POST)
	public Result payForCredit(Long accountId, Double amount, String option){
		Result result = new Result();
		try {
			result.setData(accountService.payForCredit(accountId, BigDecimal.valueOf(amount), option, getLoginUser()));
		} catch (BusinessException e) {
			result.setSuccess(false);
			result.setData(e.getMsg());
		}
		return result;
	}

	/**
	 * 获取客户的银票信息
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getAcceptDrafts", method = RequestMethod.POST)
	public Result getAcceptDrafts(Long accountId){
		Result result = new Result();
		try {
			result.setData(restAcceptDraftService.query(accountId, AcceptDraftStatus.CHARGED.getCode()));
		} catch (BusinessException e) {
			result.setSuccess(false);
			result.setData(e.getMsg());
		}
		return result;
	}
	
	/**
	 * 入口用户管理
	 * @ahtuor tuxianming
	 * @data 20160701
	 * @param out
	 */
	@RequestMapping("/potentialcustomer.html")
	public String marketAccounts(ModelMap out){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("userIds", getUserIds());
		
		List<ContactAssignDto> list = accountAssignService.findByIds(paramMap);
		out.put("list", list);
		
		List<Organization> orgs = organizationService.queryAllBusinessOrg();
    	out.put("orgs", orgs);
    	
		return "/account/contact/potentialcustomer";
	}
	
	/**
	 * 入口用户管理列表
	 * @ahtuor tuxianming
	 * @data 20160701
	 * @param out
	 */
	@ResponseBody
	@RequestMapping("/potentialcustomer/load.html")
	public PageResult loadPotentialcustomer(ContactWithPotentialCustomerQuery query){
		
		List<ContactWithPotentialCustomerDto> list = contactService.queryContactWithPotentialCustomer(query);
		int total = contactService.totalContactWithPotentialCustomer(query);
		PageResult result = new PageResult();
		result.setData(list);
		result.setRecordsFiltered(total);
		result.setRecordsTotal(list.size());
		
		return result;
	}
	
	/**
	 * 入口用户管理 -- 新增用户
	 * @ahtuor tuxianming
	 * @data 20160701
	 * @param 
	 */
	
	@RequestMapping("/potentialcustomer/add.html")
	public Result addPotentialcustomer(ContactDto dto){
		try {
			marketAddUserSender.withText(dto.getTel(), dto.getName());
		} catch (Exception e) {
			logger.error("将新增的用户推送到超市系统失败", e);
			return new Result("将新增的用户推送到超市系统失败", false);
		}
		return new Result();
	}
	
	/**
	 * 入口用户管理列表 -- edit ctrl
	 * @ahtuor tuxianming
	 * @data 20160701
	 * @param 
	 */
	@ResponseBody
	@RequestMapping("/potentialcustomer/edit.html")
	public Result editPotentialcustomer(Long contactId, Long ecId, String phone, String name){
			
		try {
			Contact contact = new Contact();
			contact.setId(contactId.intValue());
			contact.setTel(phone);
			contact.setName(name);
			contact.setLastUpdated(new Date());
			contact.setLastUpdatedBy(getLoginUser().getName());
			contactService.updateById(contact);
			marketUpdateUserSender.withText(ecId, phone, name);
		} catch (Exception e) {
			return new Result(e.getMessage(), false);	
		}
		return new Result();
	}
	
	/**
	 * 入口用户管理列表 -- 是不是停用 ctrl
	 * @ahtuor tuxianming
	 * @data 20160701
	 * @param 
	 */
	@ResponseBody
	@RequestMapping("/potentialcustomer/disable.html")
	public Result disablePotentialcustomer(Long accountId, Integer contactId, Integer ecId, String disable){
		
		try {

			AccountContact ac = new AccountContact();
			ac.setAccountId(accountId);
			ac.setContactId(contactId);
			ac.setStatus(Integer.parseInt(disable));
			ac.setLastUpdated(new Date());
			ac.setLastUpdatedBy(getLoginUser().getName());
			
			accountContactService.updateStatusByIds(ac);
			
			marketDisableUserSender.withText(ecId, disable);
			
			return new Result();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new Result(e.getMessage(), false);
		}
		
	}
	
	/**
	 * 入口用户管理列表 -- 加载已经关联的公司
	 * @ahtuor tuxianming
	 * @data 20160711
	 * @param 
	 */
	@ResponseBody
	@RequestMapping("/potentialcustomer/assign/load.html")
	public Result loadAssignPotentialcustomer(Long id){
		try {
			return new Result(accountContactService.queryByContactId(id));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new Result(e.getMessage(),false);
		}
	}
	
	/**
	 * 入口用户管理列表 -- 更新关联
	 * @ahtuor tuxianming
	 * @data 20160711
	 * @param 
	 */
	@ResponseBody
	@RequestMapping("/potentialcustomer/assign.html")
	public Result assignPotentialcustomer(UpdateAssignPotentialcustomer dto){
		
		try {
			accountContactService.assignPotentialcustomer(dto, getLoginUser());
			return new Result("更新成功！");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new Result(e.getMessage(),false);
		}
		
	}
	
}