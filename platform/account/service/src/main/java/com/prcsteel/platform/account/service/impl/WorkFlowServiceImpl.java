package com.prcsteel.platform.account.service.impl;

import com.prcsteel.platform.account.model.enums.AccountBankDataStatus;
import com.prcsteel.platform.account.model.enums.CardInfoStatus;
import com.prcsteel.platform.account.model.enums.InvoiceDataStatus;
import com.prcsteel.platform.account.model.enums.SellerConsignAgreementStatus;
import com.prcsteel.platform.account.model.model.AccountExt;
import com.prcsteel.platform.account.service.WorkFlowService;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.acl.service.UserService;
import com.prcsteel.platform.common.constants.Constant;
import constant.WorkFlowConstant;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.prcsteel.rest.sdk.activiti.enums.TaskAction;
import org.prcsteel.rest.sdk.activiti.enums.WorkFlowEnum;
import org.prcsteel.rest.sdk.activiti.pojo.ActivitiVariable;
import org.prcsteel.rest.sdk.activiti.pojo.TaskActionInfo;
import org.prcsteel.rest.sdk.activiti.pojo.TaskInfo;
import org.prcsteel.rest.sdk.activiti.query.ListTaskQuery;
import org.prcsteel.rest.sdk.activiti.request.StartProcessRequest;
import org.prcsteel.rest.sdk.activiti.request.TaskActionRequest;
import org.prcsteel.rest.sdk.activiti.result.PagedResult;
import org.prcsteel.rest.sdk.activiti.service.TaskService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 工作流辅助服务类
 * @author tangwei
 *
 */
@Service("workFlowService")
public class WorkFlowServiceImpl implements WorkFlowService{

	@Resource
	private TaskService taskService;

	@Resource
	private UserService userService;

	@Value("${quartz.job.spdb.systemCode}")
	private String systemCode;
	/**
	 * 客户资料审核工作流启动参数设置
	 * @param accountId
	 * @param accountExt
	 * @return
	 */
	public List<StartProcessRequest> startWorkFlow(Long accountId,AccountExt accountExt){
		List<StartProcessRequest> processList = new ArrayList<StartProcessRequest>();
		StartProcessRequest startRequest = null;
		if(accountExt == null || accountId == null){
			return processList;
		}
		//证件资料审核
		if(CardInfoStatus.Requested.getCode().equals(accountExt.getCardInfoStatus())){
			startRequest = createStartRequest(WorkFlowEnum.ACCOUNT_AUDIT_CARDINFO.getValue().toString(),accountId,
					WorkFlowEnum.CARD_INFO,WorkFlowConstant.DOC_PROCESS_DEFINITION_KEY);
			processList.add(startRequest);
		}
		//开票资料审核
		if(InvoiceDataStatus.Requested.getCode().equals(accountExt.getInvoiceDataStatus())){
			startRequest = createStartRequest(WorkFlowEnum.ACCOUNT_AUDIT_INVOICE.getValue().toString(),
					accountId, WorkFlowEnum.INVOICE_DATA,WorkFlowConstant.INV_PROCESS_DEFINITION_KEY);
			processList.add(startRequest);
		}
		//打款资料审核
		if(AccountBankDataStatus.Requested.getCode().equals(accountExt.getBankDataStatus())){
			startRequest = createStartRequest(WorkFlowEnum.ACCOUNT_AUDIT_BANK.getValue().toString(),
					accountId, WorkFlowEnum.BANK_DATA,WorkFlowConstant.BAN_PROCESS_DEFINITION_KEY);
			processList.add(startRequest);
		}

		// 代运营协议审核（已上传待审核） 启动流程
		if (SellerConsignAgreementStatus.Uploaded.getCode().equals(accountExt.getSellerConsignAgreementStatus())){
			String isUpload = "1";//已上传
			startRequest = createSellerConsignAgreementWorkFlow(accountId,isUpload);
			processList.add(startRequest);
		}

		return processList;
	}

	/**
	 * 审核task列表查询
	 * @param candidateUse 节点权限value
	 * @return
	 */
	public ListTaskQuery createAuditTaskQuery(Integer candidateUse,String processInstanceBusinessKey){
		String taskDefinitionKey = "";
		String processDefinitionKey ="";
		switch (candidateUse.intValue()) {
			case 10000:
				taskDefinitionKey =WorkFlowConstant.DOC_AUDIT_DOC;
				processDefinitionKey = WorkFlowConstant.DOC_PROCESS_DEFINITION_KEY;
				break;
			case 10001:
				taskDefinitionKey =WorkFlowConstant.INV_AUDIT_INVOICE;
				processDefinitionKey = WorkFlowConstant.INV_PROCESS_DEFINITION_KEY;
				break;
			case 10002:
				taskDefinitionKey =WorkFlowConstant.BAN_AUDIT_BANK;
				processDefinitionKey = WorkFlowConstant.BAN_PROCESS_DEFINITION_KEY;
				break;
			case 10003:
				taskDefinitionKey =WorkFlowConstant.BUYER_AGREEMENT_MANAGER_AUDIT;
				processDefinitionKey = WorkFlowConstant.BUYER_AGREEMENT_PROCESS_DEFINITION_KEY;
				break;
			case 10004:
				taskDefinitionKey =WorkFlowConstant.BUYER_AGREEMENT_FINANCE_AUDIT;
				processDefinitionKey = WorkFlowConstant.BUYER_AGREEMENT_PROCESS_DEFINITION_KEY;
				break;
			case 10005:
				taskDefinitionKey =WorkFlowConstant.BUYER_AGREEMENT_SERVICE_AUDIT;
				processDefinitionKey = WorkFlowConstant.BUYER_AGREEMENT_PROCESS_DEFINITION_KEY;
				break;
			case 10006:
				taskDefinitionKey =WorkFlowConstant.SELLER_AGREEMENT_MANAGER_AUDIT;
				processDefinitionKey = WorkFlowConstant.SELLER_AGREEMENT_PROCESS_DEFINITION_KEY;
				break;
			case 10007:
				taskDefinitionKey =WorkFlowConstant.SELLER_AGREEMENT_FINANCE_AUDIT;
				processDefinitionKey = WorkFlowConstant.SELLER_AGREEMENT_PROCESS_DEFINITION_KEY;
				break;
			case 10008:
				taskDefinitionKey =WorkFlowConstant.SELLER_AGREEMENT_SERVICE_AUDIT;
				processDefinitionKey = WorkFlowConstant.SELLER_AGREEMENT_PROCESS_DEFINITION_KEY;
				break;
			case 10009:
				taskDefinitionKey =WorkFlowConstant.TEMPLATE_AUDIT_TEMPLATE;
				processDefinitionKey = WorkFlowConstant.TEMPLATE_PROCESS_DEFINITION_KEY;
				break;
			default:
				break;
		}
		ListTaskQuery query = new ListTaskQuery();
		query.setIncludeProcessVariables(true);
		if(candidateUse != null){
			query.setCandidateUser(candidateUse.toString());
		}
		if(StringUtils.isNotEmpty(taskDefinitionKey)){
			query.setTaskDefinitionKey(taskDefinitionKey);
		}
		if(StringUtils.isNotEmpty(processInstanceBusinessKey)){
			query.setProcessInstanceBusinessKey(processInstanceBusinessKey);
		}
		if(StringUtils.isNotEmpty(processDefinitionKey)){
			query.setProcessDefinitionKey(processDefinitionKey);
		}
		return query;
	}

	/**
	 * 编辑Task查询列表
	 * @param taskDefinitionKey
	 * @return
	 */
	public ListTaskQuery createEditTaskQuery(String  taskDefinitionKey,String processInstanceBusinessKey){
		ListTaskQuery query = new ListTaskQuery();
		query.setIncludeProcessVariables(true);
		if(StringUtils.isNotEmpty(taskDefinitionKey)){
			query.setTaskDefinitionKey(taskDefinitionKey);
		}
		if(StringUtils.isNotEmpty(processInstanceBusinessKey)){
			query.setProcessInstanceBusinessKey(processInstanceBusinessKey);
		}
		return query;
	}

	/**
	 * 执行下一步客户审核任务Task参数
	 * @param list
	 * @param variables
	 * @param accountId
	 * @return
	 */
	public TaskActionInfo getNextTaskInfo(List<TaskInfo> list,List<ActivitiVariable> variables,String accountId){
		TaskActionInfo actionInfo = new TaskActionInfo();
		TaskActionRequest actionRequest = new TaskActionRequest();
		if(list.size() > 0){
			actionRequest.setAction(TaskAction.complete.toString());
			if(variables != null){
				actionRequest.setVariables(variables);
			}
			actionInfo.setTaskId(list.get(0).getId());
			actionInfo.setRequest(actionRequest);
		}
		return actionInfo;
	}

	/**
	 * 启动流程参数
	 * @param auditFlag
	 * @param accountId
	 * @param flowType
	 * @param processDefKey
	 * @return
	 */
	public StartProcessRequest createStartRequest (String auditFlag,Long accountId,WorkFlowEnum flowType,String processDefKey){
		StartProcessRequest startRequest = new StartProcessRequest();
		User loginUser = getLoginUser();
		if (loginUser == null) {
			loginUser = new User();
			loginUser.setId(0L);
			loginUser.setLoginId(systemCode);
			loginUser.setName(Constant.SYSTEMNAME);
		}
		startRequest.addVariable(WorkFlowConstant.VAR_AUDIT_USER,auditFlag);//审核人员权限标记
		startRequest.addVariable(flowType.getName(),flowType.getValue().toString());//流程类型
		startRequest.addVariable(WorkFlowConstant.VAR_ID,accountId.toString());//记录Id
		startRequest.addVariable(WorkFlowConstant.VAR_USER_ID,loginUser.getId().toString());//流程发起人姓名
		startRequest.addVariable(WorkFlowConstant.VAR_LOGIN_NAME,loginUser.getName());//流程发起人姓名
		startRequest.addVariable(WorkFlowConstant.VAR_LOGIN_ID,loginUser.getLoginId());//流程发起人loginId
		startRequest.setProcessDefinitionKey(processDefKey);
		startRequest.setBusinessKey(accountId.toString());
		return startRequest;
	}

	/**
	 * 客户协议启动流程参数
	 * @param managerAuditFlag 总经理审核权限标记
	 * @param financeAuditFlag 财务审核人员权限标记
	 * @param serviceAuditFlag 综服部审核人员权限标记
	 * @param loginUser   用户
	 * @param accountId 客户id
	 * @param flowType 流程类型
	 * @param processDefKey 流程定义id
	 * @return
	 */
	public StartProcessRequest createAccountAgreementStartRequest (String managerAuditFlag,String financeAuditFlag,String serviceAuditFlag,String isUploadFlag,User loginUser,Long accountId,WorkFlowEnum flowType,String processDefKey){
		StartProcessRequest startRequest = new StartProcessRequest();
		startRequest.addVariable(WorkFlowConstant.VAR_AGREEMENT_ISUPLOAD,isUploadFlag);//初始时是否上传图片
		startRequest.addVariable(WorkFlowConstant.VAR_MANAGER_AUDIT_USER,managerAuditFlag);//总经理审核人员权限标记
		startRequest.addVariable(WorkFlowConstant.VAR_FINANCE_AUDIT_USER,financeAuditFlag);//财务审核人员权限标记
		startRequest.addVariable(WorkFlowConstant.VAR_SERVICE_AUDIT_USER,serviceAuditFlag);//综服部审核人员权限标记
		startRequest.addVariable(flowType.getName(),flowType.getValue().toString());//流程类型
		startRequest.addVariable(WorkFlowConstant.VAR_ID,accountId.toString());//业务记录Id
		startRequest.addVariable(WorkFlowConstant.VAR_USER_ID,loginUser.getId().toString());//流程发起人ID
		startRequest.addVariable(WorkFlowConstant.VAR_LOGIN_NAME,loginUser.getName());//流程发起人姓名
		startRequest.addVariable(WorkFlowConstant.VAR_LOGIN_ID,loginUser.getLoginId());//流程发起人loginId
		startRequest.setProcessDefinitionKey(processDefKey);
		startRequest.setBusinessKey(accountId.toString());
		return startRequest;
	}

	/**
	 * 卖家代运营协议启动流程参数
	 * @param accountId 客户id
	 * @param isUpload 是否上传图片
	 * @return
	 */
	public StartProcessRequest createSellerConsignAgreementWorkFlow(Long accountId,String isUpload){
		return createAccountAgreementStartRequest(WorkFlowEnum.ACCOUNT_AUDIT_SELLER_CONSIGN_MANAGER.getValue().toString(),
				WorkFlowEnum.ACCOUNT_AUDIT_SELLER_CONSIGN_FINANCE.getValue().toString()
				, WorkFlowEnum.ACCOUNT_AUDIT_SELLER_CONSIGN_SERVICE.getValue().toString(),
				isUpload,getLoginUser(), accountId, WorkFlowEnum.SELLER_CONSIGN_AGREEMENT, WorkFlowConstant.SELLER_AGREEMENT_PROCESS_DEFINITION_KEY);
	}


	/**
	 * 买家年度采购协议启动流程参数
	 * @param accountId 客户id
	 * @param isUpload 是否上传图片
	 * @return
	 */
	public StartProcessRequest createAnnualPurchaseAgreementWorkFlow(Long accountId,String isUpload){
		return createAccountAgreementStartRequest(WorkFlowEnum.ACCOUNT_AUDIT_ANNUAL_PURCHASE_MANAGER.getValue().toString(),
				WorkFlowEnum.ACCOUNT_AUDIT_ANNUAL_PURCHASE_FINANCE.getValue().toString()
				, WorkFlowEnum.ACCOUNT_AUDIT_ANNUAL_PURCHASE_SERVICE.getValue().toString(),
				isUpload, getLoginUser(), accountId, WorkFlowEnum.ANNUAL_PURCHASE_AGREEMENT, WorkFlowConstant.BUYER_AGREEMENT_PROCESS_DEFINITION_KEY);
	}

	/**
	 * 获取所有task任务
	 * @param query
	 */
	public List<TaskInfo> getAllTaskList(ListTaskQuery query){
		List<TaskInfo> taskList = new ArrayList<TaskInfo>();
		addAllTaskList(taskList, query);
		return taskList;
	}

	private void addAllTaskList(List<TaskInfo> taskList,ListTaskQuery query){
		query.setStart(taskList.size());
		query.setSize(9999);
		PagedResult<TaskInfo> page = taskService.listTasks(query);
		List<TaskInfo> list = page.getData(TaskInfo.class);
		taskList.addAll(list);
		if(page.getTotal() > taskList.size()){
			addAllTaskList(taskList, query);
		}
	}

	private User getLoginUser(){
		Object o = SecurityUtils.getSubject().getPrincipal();
		User user = userService.queryByLoginId((String) o);
		return user;
	}
}
