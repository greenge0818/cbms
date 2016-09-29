package com.prcsteel.platform.order.web.controller;

import com.prcsteel.platform.account.service.AccountService;
import com.prcsteel.platform.acl.model.model.Organization;
import com.prcsteel.platform.acl.model.model.Role;
import com.prcsteel.platform.acl.model.model.SysSetting;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.acl.service.SysSettingService;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.utils.Tools;
import com.prcsteel.platform.common.vo.Result;
import com.prcsteel.platform.core.model.model.City;
import com.prcsteel.platform.order.model.dto.InvoiceOutProcessCountDto;
import com.prcsteel.platform.order.model.enums.InvoiceInStatus;
import com.prcsteel.platform.order.service.invoice.InvoiceOutService;
import com.prcsteel.platform.order.service.order.SecondSettlementLogService;
import com.prcsteel.platform.order.web.controller.invoice.InvoiceInController;
import com.prcsteel.platform.order.web.controller.order.QueryOrderController;
import com.prcsteel.platform.order.web.security.CustomCASRealm;
import com.prcsteel.platform.order.web.support.ProjectEnv;
import com.prcsteel.platform.order.web.support.ShiroVelocity;
import com.prcsteel.platform.smartmatch.model.model.CustBasePrice;
import com.prcsteel.platform.smartmatch.model.query.PurchaseOrderQuery;
import com.prcsteel.platform.smartmatch.model.query.ResourceBusinessQuery;
import com.prcsteel.platform.smartmatch.service.PurchaseOrderService;
import com.prcsteel.platform.smartmatch.service.QuotationService;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.prcsteel.rest.sdk.activiti.enums.WorkFlowEnum;
import org.prcsteel.rest.sdk.activiti.pojo.ActivitiVariable;
import org.prcsteel.rest.sdk.activiti.pojo.TaskInfo;
import org.prcsteel.rest.sdk.activiti.query.ListTaskQuery;
import org.prcsteel.rest.sdk.activiti.result.PagedResult;
import org.prcsteel.rest.sdk.activiti.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
//import com.prcsteel.platform.smartmatch.model.query.PurchaseOrderQuery;
//import com.prcsteel.platform.smartmatch.service.PurchaseOrderService;

@Controller
@RequestMapping("/")
public class HomeController extends BaseController {

	@Value("${logoutUrl}")
	private String logoutUrl;

	@Autowired
	private QueryOrderController queryOrderController;

	@Resource
	private InvoiceInController invoiceInController;
	@Resource
	private InvoiceOutService invoiceOutService;
	@Resource
	private SecondSettlementLogService logService;

	@Resource
	private SysSettingService sysSettingService;

	@Resource
	private ProjectEnv projectEnv;

	@Resource
	private TaskService taskService;

	@Resource
	private AccountService accountService;
	
	@Resource
	QuotationService quotationService;
	
	@Resource
	PurchaseOrderService purchaseOrderService;

	@Resource(name = "casRealm")
	private CustomCASRealm customCASRealm = null;
	
	//@Resource
	//private PurchaseOrderService purchaseOrderService;

	@Value("${account.domain}")
	private String accountDomain;

	@RequestMapping("index.html")
	public void index(ModelMap out) {
		//add by caosulin 增加缓存菜单权限
		//初始化菜单权限
		if(customCASRealm.isCacheAuth){
			customCASRealm.initAccountAuthorizationInfo(getLoginUser().getLoginId());
		}
		//采购单管理数量
		//PurchaseOrderQuery purchaseOrderQuery = new PurchaseOrderQuery();
		//purchaseOrderQuery.setUserIds(getUserIds());
		int purchaseOrderListSize = 0;//purchaseOrderService.selectPurchaseList(purchaseOrderQuery).size();

		out.put("purchaseOrder", Tools.processTabNumber(purchaseOrderListSize));
		
		//待处理 add lixiang
		List<Long> userIdsList = new ArrayList<Long>();
		userIdsList=getUserIds();
		User user =getLoginUser();
		PurchaseOrderQuery pendingrQuery = new PurchaseOrderQuery();
		List<String> pendingList = new ArrayList<String>();
		pendingList.add("PENDING_ACCEPTE");
		pendingList.add("PENDING_QUOTE");
		pendingList.add("PENDING_CLERK_ACCEPTE");
		pendingList.add("QUOTED");
		pendingrQuery.setStatusList(pendingList);
		pendingrQuery.setUserIds(userIdsList);
		pendingrQuery.setUser(user);
		int purchaseNum = purchaseOrderService.countPurchaseOrder(pendingrQuery);
		out.put(Constant.ConsignOrderTab.PURCHASEORDER.toString(), Tools.processTabNumber(purchaseNum));
		//待指派 add lixiang
		PurchaseOrderQuery appointed = new PurchaseOrderQuery();
		List<String> statusList = new ArrayList<String>();
		statusList.add("PENDING_DIRECTOR_ASSIGNED");
		statusList.add("PENDING_SERVER_MANAGER_ASSIGNED");
		appointed.setStatusList(statusList);
		appointed.setUserIds(userIdsList);
		appointed.setUser(user);
		int appointedNum = purchaseOrderService.countPurchaseOrder(appointed);
		out.put(Constant.ConsignOrderTab.APPOINTED.toString(), Tools.processTabNumber(appointedNum));

		//待开单 add by zhoucai@prcsteel.com 20160822

		PurchaseOrderQuery  prebilling= new PurchaseOrderQuery();
		List<String> preStatusList = new ArrayList<String>();

		//设置待开单状态
		preStatusList.add("PENDING_OPEN_BILL");
		prebilling.setStatusList(preStatusList);
		prebilling.setUserIds(userIdsList);
		prebilling.setUser(user);
		int preBillingNum = purchaseOrderService.countPurchaseOrder(prebilling);
		out.put(Constant.ConsignOrderTab.PREBILLING.toString(), Tools.processTabNumber(preBillingNum));
		
		//代运营交易单管理、提货管理导航数字
		queryOrderController.setTabTotal(out);
		//进项票导航栏数字
		invoiceInController.getTabTotal(out, InvoiceInStatus.RECEIVED);
		//应付应收金额
		Map<String, Float> map = logService.findByUserId(getUserIds());
		if (map != null) {
			out.put("minusBalanceSecond", map.get("minusBalanceSecond")==null?0:map.get("minusBalanceSecond"));
			out.put("positiveBalanceSecond", map.get("positiveBalanceSecond")==null?0:map.get("positiveBalanceSecond"));
		} else {
			out.put("minusBalanceSecond",0);
			out.put("positiveBalanceSecond",0);
		}
		//销项票导航栏数字
		InvoiceOutProcessCountDto dto = invoiceOutService.getTabCount(getUserIds());
		out.put("express", dto.getExpress()>0?dto.getExpress():"");
		out.put("confirm", dto.getConfirm()>0?dto.getConfirm():"");
		out.put("confirmed", dto.getConfirmed() > 0 ? dto.getConfirmed() : "");
		//工作流审核待办事项

		int cardInfoCount = getAuditPermissionsCount(WorkFlowEnum.ACCOUNT_AUDIT_CARDINFO,null);
		int invoiceCount = getAuditPermissionsCount(WorkFlowEnum.ACCOUNT_AUDIT_INVOICE,null);
		int backCount = getAuditPermissionsCount(WorkFlowEnum.ACCOUNT_AUDIT_BANK,null);

		int annualCount = getAuditPermissionsCount(WorkFlowEnum.ACCOUNT_AUDIT_ANNUAL_PURCHASE_MANAGER,user.getOrgId());
		int annualFinanceCount = getAuditPermissionsCount(WorkFlowEnum.ACCOUNT_AUDIT_ANNUAL_PURCHASE_FINANCE,null);
		int annualServiceCount = getAuditPermissionsCount(WorkFlowEnum.ACCOUNT_AUDIT_ANNUAL_PURCHASE_SERVICE,null);
		int consignCount = getAuditPermissionsCount(WorkFlowEnum.ACCOUNT_AUDIT_SELLER_CONSIGN_MANAGER,user.getOrgId());
		int consignFinanceCount = getAuditPermissionsCount(WorkFlowEnum.ACCOUNT_AUDIT_SELLER_CONSIGN_FINANCE,null);
		int consignServiceCount = getAuditPermissionsCount(WorkFlowEnum.ACCOUNT_AUDIT_SELLER_CONSIGN_SERVICE,null);
		int contractTemplateCount = getAuditPermissionsCount(WorkFlowEnum.ACCOUNT_AUDIT_CONTRACT_TEMPLATE,null);

		out.put("cardInfoCount",cardInfoCount);
		out.put("invoiceCount",invoiceCount);
		out.put("backCount",backCount);
		out.put("annualCount",annualCount);
		out.put("annualFinanceCount",annualFinanceCount);
		out.put("annualServiceCount",annualServiceCount);
		out.put("consignCount",consignCount);
		out.put("consignFinanceCount",consignFinanceCount);
		out.put("consignServiceCount",consignServiceCount);
		out.put("contractTemplateCount",contractTemplateCount);
		out.put("accountDomain",accountDomain);
	}

	@RequestMapping("version.html")
	public void version(ModelMap out){
		SysSetting setting = sysSettingService.queryBySettingType(Constant.DATABASE_VERSION_KEY);
		out.put("databaseVersion", setting.getSettingValue());
		out.put("projectVersion", projectEnv.getProjectVersion());
		out.put("requiredDbVersion", projectEnv.getRequiredDbVersion());
	}

	@RequestMapping("warning.html")
	public void warning() {
	}

	@RequestMapping("fail.html")
	public void fail() {
	}

	@RequestMapping("unauth.html")
	public void unauth() {
	}

	@RequestMapping("login.html")
	public void login() {
		System.out.print("test");
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

	@RequestMapping(value="ajaxLogin.html", method=RequestMethod.POST)
	public @ResponseBody Result ajaxLogin(String username, String password) {
		Result result = new Result();

		Subject subject = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(username, password);
		subject.login(token);

		result.setSuccess(true);

		return result;
	}
	/**
	 * 获取用户审核个数
	 * @param userOrgId
	 * @return
	 */
	private int getAuditPermissionsCount(WorkFlowEnum permissionsEnum,Long userOrgId){
		ShiroVelocity permissionLimit = new ShiroVelocity();
		int count = 0;
		ListTaskQuery query = new ListTaskQuery();
		if(permissionLimit.hasPermission(permissionsEnum.getName())){
			query.setCandidateUser(permissionsEnum.getValue().toString());
			query.setIncludeProcessVariables(true);
			List<TaskInfo>list = getAllTaskList(query, taskService);
			count = list.size();
			List<Long> orgList;
			Role role = getUserRole();
			if(userOrgId == null || count == 0 || role.getType().intValue() == Constant.ROLE_TYPE.ALL.getValue()){
				return count;
			}
			//服务中心总经理审核,筛选跟自己同一服务中心的客户
			count = 0;
			for(TaskInfo task:list){
				var:for(ActivitiVariable var:task.getVariables()){
					if(!"id".equals(var.getName())){
						continue;
					}
					orgList = accountService.queryOrgIdsByAccountId(Long.parseLong(var.getValue()));
					for(Long orgId:orgList){
						if(orgId.longValue() == userOrgId.longValue()){
							count++;
							break var;
						}
					}
				}
			}
		}
		return count;
	}

	private List<TaskInfo> getAllTaskList(ListTaskQuery query,TaskService taskService){
		List<TaskInfo> taskList = new ArrayList<TaskInfo>();
		addAllTaskList(taskList, query, taskService);
		return taskList;
	}

	private void addAllTaskList(List<TaskInfo> taskList,ListTaskQuery query,TaskService taskService){
		query.setStart(taskList.size());
		query.setSize(9999);
		PagedResult<TaskInfo> page = taskService.listTasks(query);
		List<TaskInfo> list = page.getData(TaskInfo.class);
		taskList.addAll(list);
		if(page.getTotal() > taskList.size()){
			addAllTaskList(taskList, query,taskService);
		}
	}
}
