package com.prcsteel.platform.account.web.controller;

import com.prcsteel.platform.account.model.dto.ContractTemplateAuditDto;
import com.prcsteel.platform.account.model.dto.ContractTemplateDto;
import com.prcsteel.platform.account.model.enums.AccountContractTemplateType;
import com.prcsteel.platform.account.model.enums.ContractTemplateStatus;
import com.prcsteel.platform.account.model.model.AccountContractTemplateOprt;
import com.prcsteel.platform.account.model.query.CompanyQuery;
import com.prcsteel.platform.account.service.AccountContractTemplateService;
import com.prcsteel.platform.account.service.AccountDocumentService;
import com.prcsteel.platform.account.service.WorkFlowService;
import com.prcsteel.platform.acl.model.model.Organization;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.acl.service.OrganizationService;
import com.prcsteel.platform.common.vo.PageResult;
import com.prcsteel.platform.common.vo.Result;
import constant.WorkFlowConstant;
import org.apache.commons.lang.StringUtils;
import org.prcsteel.rest.sdk.activiti.enums.WorkFlowEnum;
import org.prcsteel.rest.sdk.activiti.pojo.ActivitiVariable;
import org.prcsteel.rest.sdk.activiti.pojo.TaskActionInfo;
import org.prcsteel.rest.sdk.activiti.pojo.TaskInfo;
import org.prcsteel.rest.sdk.activiti.query.ListTaskQuery;
import org.prcsteel.rest.sdk.activiti.service.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 合同模板审核控制器
 * @author chengui 2016/6/21
 *
 */
@Controller
@RequestMapping("/flow/contracttemplate")
public class AccountContractTemplateController extends BaseController{

	@Resource
	AccountDocumentService accountDocumentService;
	@Resource
	TaskService taskService;
	@Resource
	WorkFlowService workFlowService;
	@Resource
	OrganizationService organizationService;
	@Resource
	AccountContractTemplateService accountContractTemplateService;

	/**
	 * 列表
	 * @param out
	 * @return
	 */
	@RequestMapping("/list")
	public String list(ModelMap out) {
		List<Organization> orgList = organizationService.queryAllBusinessOrg();
		StringBuffer ids = new StringBuffer();
		//根据状态查询对应的task列表
		ListTaskQuery taskQuery =  workFlowService.createAuditTaskQuery(WorkFlowEnum.ACCOUNT_AUDIT_CONTRACT_TEMPLATE.getValue(),null);
		List<TaskInfo> taskList = workFlowService.getAllTaskList(taskQuery);

		for(TaskInfo task:taskList){
			for(ActivitiVariable var:task.getVariables()){
				if(!WorkFlowConstant.VAR_ID.equals(var.getName())){
					continue;
				}
				ids.append(var.getValue());
				ids.append(",");
			}
		}

		if(ids.length() > 0){
			ids.delete(ids.length()-1, ids.length());
		}
		out.put("orgs", orgList);
		out.put("ids", ids.toString());
		return "company/workflow/contractTemplateList";
	}


	/**
	 * 加载列表数据
	 *
	 * @return
	 */
	@RequestMapping(value = "loadlist.html", method = RequestMethod.POST)
	@ResponseBody
	public PageResult loadList(CompanyQuery query) {
		List<String> idList = getIdList(query.getIds());
		List<ContractTemplateDto> list = new ArrayList<ContractTemplateDto>();
		if(idList == null){
			return new PageResult(0,0,list);
		}
		query.setIdList(idList);
		int total = accountContractTemplateService.queryTotalContractTemplateByStatus(query);
		if (total > 0) {
			list = accountContractTemplateService.queryContractTemplateByStatus(query);
		}
		return new PageResult(list.size(),total,list);
	}

	/**
	 * 审核合同模板
	 * @param id
	 * @return
	 */
	@RequestMapping("toauditcontracttemplate")
	public String toAuditInvoice(ModelMap out, @RequestParam("id") Long id, @RequestParam("name") String name, @RequestParam("accountId") Long accountId,
								 @RequestParam("companyId") Long companyId, @RequestParam("type") String type, @RequestParam("companyName") String companyName) {

		try {
			name = URLDecoder.decode(name, "UTF-8");
		} catch (UnsupportedEncodingException e) {

		}
		AccountContractTemplateOprt templateOprt = new AccountContractTemplateOprt(id, name, accountId, companyId, "view", type, companyName);
		out.put("actOprt", templateOprt);
		//合同编号
		out.put("templateNo", "20 160323 00001");
		out.put("detail",accountContractTemplateService.doTemplateOprt(templateOprt));
		return "company/workflow/contractTemplate";
	}

	/**
	 * 执行审核操作
	 * @param contractTemplateAuditDto
	 * @return
	 */
	@RequestMapping(value="doauditcontracttemplate",method=RequestMethod.POST)
	@ResponseBody
	public Result doAudit(ContractTemplateAuditDto contractTemplateAuditDto) {
		Result result = new Result();
		result.setSuccess(false);
		//找到对应的任务并设置参数
		String auditPass = "1";
		List<TaskInfo>taskList = new ArrayList<TaskInfo>();
		if(StringUtils.isNotEmpty(contractTemplateAuditDto.getStatus())){
			ListTaskQuery taskQuery =  workFlowService.createAuditTaskQuery(WorkFlowEnum.ACCOUNT_AUDIT_CONTRACT_TEMPLATE.getValue(),contractTemplateAuditDto.getId().toString());
			taskList = workFlowService.getAllTaskList(taskQuery);
			if(ContractTemplateStatus.DISAPPROVED.getValue().equals(contractTemplateAuditDto.getStatus())){
				auditPass = "0";
			}
		}
		List<ActivitiVariable> variables = new ArrayList<ActivitiVariable>();
		ActivitiVariable createVar = new ActivitiVariable(WorkFlowConstant.VAR_AUDIT_PASS,auditPass);
		variables.add(createVar);
		TaskActionInfo actionInfo =workFlowService.getNextTaskInfo(taskList,variables,contractTemplateAuditDto.getId().toString());
		if(actionInfo.getRequest() != null){
			//更新状态成功后,执行任务
			result = updateStatus(contractTemplateAuditDto);
			if(result.isSuccess()){
				taskService.invokeTaskAction(actionInfo.getTaskId(),actionInfo.getRequest());
			}
		}
		return result;
	}

	/**
	 * 获取合同性质数据
	 * @return
	 */
	@RequestMapping("/getAccountContractTemplateType")
	@ResponseBody
	public Result getAccountContractTemplateType() {
		return new Result(AccountContractTemplateType.getList(), Boolean.TRUE);
	}

	/**
	 * 更新数据状态
	 * @param contractTemplateAuditDto
	 * @return
	 */
	private Result updateStatus(ContractTemplateAuditDto contractTemplateAuditDto){
		Result result = new Result();
		int resultCount = 0;
		User user = getLoginUser();

		resultCount = accountContractTemplateService.updateContractTemplateStatus(contractTemplateAuditDto.getId(), contractTemplateAuditDto.getStatus(), user.getName(), contractTemplateAuditDto.getDisagreeDesc());
		if(resultCount <=0){
			result.setSuccess(false);
		}
		return result;
	}

	private List<String> getIdList(String ids){
		if(StringUtils.isEmpty(ids)){
			return null;
		}
		String []idList = ids.split(",");
		return Arrays.asList(idList);
	}
}
