package com.prcsteel.platform.account.web.controller;

import java.util.ArrayList;
import java.util.List;

import com.prcsteel.platform.common.aspect.OpAction;
import org.apache.commons.lang.StringUtils;
import org.prcsteel.rest.sdk.activiti.enums.WorkFlowEnum;
import org.prcsteel.rest.sdk.activiti.pojo.ActivitiVariable;
import org.prcsteel.rest.sdk.activiti.pojo.TaskActionInfo;
import org.prcsteel.rest.sdk.activiti.pojo.TaskInfo;
import org.prcsteel.rest.sdk.activiti.query.ListTaskQuery;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.prcsteel.platform.account.model.dto.AccountDocumentDto;
import com.prcsteel.platform.account.model.dto.AccountDto;
import com.prcsteel.platform.account.model.enums.InvoiceDataStatus;
import com.prcsteel.platform.account.model.query.CompanyQuery;
import com.prcsteel.platform.common.vo.PageResult;
import com.prcsteel.platform.common.vo.Result;

import constant.WorkFlowConstant;

/**
 * 开票资料审核控制器
 * @author tangwei 
 *
 */
@Controller
@RequestMapping("/flow/invoice")
public class AccountInvoiceController extends AccountDocumentController{

	/**
	 * 列表
	 * @param out
	 * @param ids
	 * @param queryType
	 * @return
	 */
    @RequestMapping("/list")
    public String list(ModelMap out) {
    	return super.queryList(out, getQueryStatus());
    }
    
    
    /**
     * 加载列表数据
     *
     * @return
     */
    @RequestMapping(value = "loadlist.html", method = RequestMethod.POST)
    @ResponseBody
    public PageResult loadList(CompanyQuery query) {
        return super.loadList(query);
    }
    
    /**
     * 审核证件资料
     * @param id
     * @return
     */
    @RequestMapping("{accountId}/toauditinvoice")
    public String toAuditInvoice(ModelMap out,@PathVariable("accountId") Long accountId) {
        AccountDto accountdto = contactService.getCompanyById(accountId);
        out.put("accountdto", accountdto);
        return "company/workflow/invoice";
    }
    
    /**
     * 执行审核操作
     * @param accountDoc
     * @return
     */
	@OpAction( key = "accountId")
	@RequestMapping(value="doauditinvoice",method=RequestMethod.POST)
    @ResponseBody
    public Result doAudit(AccountDocumentDto accountDoc ,Long accountId) {
    	Result result = new Result();
    	result.setSuccess(false);
    	//找到对应的任务并设置参数
		String auditPass = "1";
    	List<TaskInfo>taskList = new ArrayList<TaskInfo>();
    	if(StringUtils.isNotEmpty(accountDoc.getInvoiceDataStatus())){
        	ListTaskQuery taskQuery =  workFlowService.createAuditTaskQuery(WorkFlowEnum.ACCOUNT_AUDIT_INVOICE.getValue(),accountDoc.getId().toString());
        	taskList = workFlowService.getAllTaskList(taskQuery);
    		if(InvoiceDataStatus.Declined.getCode().equals(accountDoc.getInvoiceDataStatus())){
    			auditPass = "0";
    		}
    	}
		List<ActivitiVariable> variables = new ArrayList<ActivitiVariable>();
		ActivitiVariable createVar = new ActivitiVariable(WorkFlowConstant.VAR_AUDIT_PASS,auditPass);
		variables.add(createVar);
    	TaskActionInfo actionInfo =workFlowService.getNextTaskInfo(taskList,variables,accountDoc.getId().toString());
    	if(actionInfo.getRequest() != null){
    		//更新状态成功后,执行任务
    		result = updateStatus(accountDoc);
    		if(result.isSuccess()){
    			taskService.invokeTaskAction(actionInfo.getTaskId(),actionInfo.getRequest());
    		}
    	}
		return result;
    }
    
    @Override
    protected int getQueryStatus() {
    	return WorkFlowEnum.ACCOUNT_AUDIT_INVOICE.getValue();
    }
}
