package com.prcsteel.platform.account.web.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import com.prcsteel.platform.common.aspect.OpAction;
import org.apache.commons.lang.StringUtils;
import org.prcsteel.rest.sdk.activiti.enums.WorkFlowEnum;
import org.prcsteel.rest.sdk.activiti.pojo.ActivitiVariable;
import org.prcsteel.rest.sdk.activiti.pojo.TaskActionInfo;
import org.prcsteel.rest.sdk.activiti.pojo.TaskInfo;
import org.prcsteel.rest.sdk.activiti.query.ListTaskQuery;
import org.prcsteel.rest.sdk.activiti.request.StartProcessRequest;
import org.prcsteel.rest.sdk.activiti.service.ProcessService;
import org.prcsteel.rest.sdk.activiti.service.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.prcsteel.platform.account.model.dto.AccountDocumentDto;
import com.prcsteel.platform.account.model.dto.AccountDto;
import com.prcsteel.platform.account.model.dto.CompanyDto;
import com.prcsteel.platform.account.model.enums.AccountTag;
import com.prcsteel.platform.account.model.enums.CardInfoStatus;
import com.prcsteel.platform.account.model.model.AccountExt;
import com.prcsteel.platform.account.model.query.CompanyQuery;
import com.prcsteel.platform.account.service.AccountBankService;
import com.prcsteel.platform.account.service.AccountDocumentService;
import com.prcsteel.platform.account.service.AccountService;
import com.prcsteel.platform.account.service.ContactService;
import com.prcsteel.platform.account.service.WorkFlowService;
import com.prcsteel.platform.acl.model.model.Organization;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.common.vo.PageResult;
import com.prcsteel.platform.common.vo.Result;

import constant.WorkFlowConstant;

/**
 * 资料审核控制器
 * @author tangwei
 *
 */
@Controller
@RequestMapping("/flow/cardinfo")
public class AccountDocumentController extends BaseController{

	@Resource
	protected AccountDocumentService accountDocumentService;
	
    @Resource
    protected ContactService contactService;
    
	@Resource
	protected TaskService taskService;
	
	@Resource
	protected ProcessService processService;
	
	@Resource
	protected AccountBankService accountBankService;
	
	@Resource
	protected AccountService accountService;
	
	@Resource
	protected WorkFlowService workFlowService;

	/**
	 * 列表
	 * @param out
	 * @param ids
	 * @param queryType
	 * @return
	 */
    @RequestMapping("/list")
    public String list(ModelMap out) {
    	int queryStatus = getQueryStatus();
    	return queryList(out, queryStatus);
    }
    
    public String queryList(ModelMap out,int queryStatus){
        List<Organization> orgList = organizationService.queryAllBusinessOrg();
    	StringBuffer ids = new StringBuffer();
    	//根据状态查询对应的task列表
    	ListTaskQuery taskQuery =  workFlowService.createAuditTaskQuery(queryStatus,null);
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
        out.put("queryStatus", queryStatus);
        return "company/workflow/cardInfoList";
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
    	List<CompanyDto> list = new ArrayList<CompanyDto>();
    	if(idList == null){
    		return new PageResult(0,0,list); 
    	}
    	query.setIdList(idList);
        int total = accountDocumentService.queryTotalCompanyByStatus(query);
        if (total > 0) {
             list = accountDocumentService.queryCompanyByStatus(query);
        }
        return new PageResult(list.size(),total,list);
    }
    
    /**
     * 审核证件资料
     * @param id
     * @return
     */
    @RequestMapping("{accountId}/toauditdoc")
    public String toAuditDoc(ModelMap out,@PathVariable("accountId") Long accountId) {
        AccountDto accountdto = contactService.getCompanyById(accountId);
        out.put("accountdto", accountdto);
        return "company/workflow/cardInfo";
    }
    
    /**
     * 执行审核操作
     * @param accountDoc
     * @return
     */
	@OpAction( key = "id")
	@RequestMapping(value="doauditdoc",method=RequestMethod.POST)
    @ResponseBody
    public Result doAudit(AccountDocumentDto accountDoc,Long id) {
    	Result result = new Result();
    	result.setSuccess(false);
    	//找到对应的任务并设置参数
		String auditPass = "1";
    	List<TaskInfo>taskList = new ArrayList<TaskInfo>();
    	if(StringUtils.isNotEmpty(accountDoc.getCardInfoStatus())){
        	ListTaskQuery taskQuery =  workFlowService.createAuditTaskQuery(WorkFlowEnum.ACCOUNT_AUDIT_CARDINFO.getValue(),accountDoc.getId().toString());
        	taskList = workFlowService.getAllTaskList(taskQuery);
    		if(CardInfoStatus.Declined.getCode().equals(accountDoc.getCardInfoStatus())){
    			auditPass = "0";
    		}
    	}
		List<ActivitiVariable> variables = new ArrayList<ActivitiVariable>();
		ActivitiVariable createVar = new ActivitiVariable(WorkFlowConstant.VAR_AUDIT_PASS,auditPass);
		variables.add(createVar);
    	TaskActionInfo actionInfo =workFlowService.getNextTaskInfo(taskList,variables, accountDoc.getId().toString());
    	if(actionInfo.getRequest() != null){
    		//更新状态成功后,执行任务
    		result = updateStatus(accountDoc);
    		if(result.isSuccess()){
    			taskService.invokeTaskAction(actionInfo.getTaskId(),actionInfo.getRequest());
    		}
    	}
		return result;
    }
    
    /**
     * 取消审核
     * @param accountDoc
     * @return
     */
	@OpAction( key = "id")
	@RequestMapping(value="docancelaudit",method=RequestMethod.POST)
    @ResponseBody
    public Result doCancelAudit(AccountDocumentDto accountDoc,Long id) {
    	//更新状态,客户性质
    	Result result = updateStatus(accountDoc);
    	if(StringUtils.isNotEmpty(accountDoc.getConsignAgreementStatus())){
    		AccountDto dto = accountService.selectByPrimaryKey(accountDoc.getId());
    		long tag = accountService.reCaluteAccountTag(dto.getAccount().getAccountTag(), AccountTag.consign.getCode(), AccountTag.temp.getCode());
    		dto.getAccount().setAccountTag(tag);
    		accountDocumentService.updateAccountTag(dto.getAccount(),getLoginUser().getName());
    	}
    	if(result.isSuccess()){
    		//撤销审核后启动新的流程
    		AccountExt ext  = new AccountExt();
    		ext.setCardInfoStatus(accountDoc.getCardInfoStatus());
    		ext.setInvoiceDataStatus(accountDoc.getInvoiceDataStatus());
    		ext.setSellerConsignAgreementStatus(accountDoc.getConsignAgreementStatus());
    		List<StartProcessRequest> processList = workFlowService.startWorkFlow(accountDoc.getId(),ext);
    		for(StartProcessRequest process:processList){
        		processService.startProcess(process);
    		}
    	}
    	return result;
    }
    
    /**
     * 更新数据状态
     * @param accountDoc
     * @return
     */
    protected Result updateStatus(AccountDocumentDto accountDoc){
    	Result result = new Result();
    	int resultCount = 0;
    	User user = getLoginUser();
    	accountDoc.setLastUpdateBy(user.getName());
    	resultCount = accountDocumentService.updateAccountExtStatus(accountDoc);
    	if(resultCount <=0){
    		result.setSuccess(false);
    	}
    	return result;
    }
    protected List<String> getIdList(String ids){
    	if(StringUtils.isEmpty(ids)){
    		return null;
    	}
    	String []idList = ids.split(",");
    	return Arrays.asList(idList);
    }
    
    protected int getQueryStatus(){
    	return WorkFlowEnum.ACCOUNT_AUDIT_CARDINFO.getValue();
    }
}
