package com.prcsteel.platform.account.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.prcsteel.platform.account.model.enums.AccountBankDataStatus;
import com.prcsteel.platform.account.model.model.AccountBank;
import com.prcsteel.platform.account.model.query.CompanyQuery;
import com.prcsteel.platform.common.vo.PageResult;
import com.prcsteel.platform.common.vo.Result;

import constant.WorkFlowConstant;

/**
 * 打款资料审核控制器
 * @author tangwei
 *
 */
@Controller
@RequestMapping("/flow/bank")
public class AccountBankController extends AccountDocumentController{

    
	/**
	 * 列表
	 * @param out
	 * @param ids
	 * @param queryType
	 * @return
	 */
    @RequestMapping("/list")
    public String list(ModelMap out) {
    	return queryList(out, getQueryStatus());
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
     * 打款资料审核
     * @param id
     * @return
     */
    @RequestMapping("{accountId}/toauditbank")
    public String toAuditInvoice(ModelMap out,@PathVariable("accountId") Long accountId) {
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("accountId", accountId);
        paramMap.put("bankDataStatus", AccountBankDataStatus.Requested.getCode());
        List<AccountBank> bankList = accountBankService.query(paramMap);
        AccountDto accountdto = contactService.getCompanyById(accountId);
        out.put("bankList",bankList);
        out.put("accountId",accountId);
        out.put("accountdto", accountdto);
        return "company/workflow/bank";
    }
    
    /**
     * 执行审核操作
     * @param accountDoc
     * @return
     */
    @RequestMapping(value="doauditbank",method=RequestMethod.POST)
    @ResponseBody
    public Result doAudit(AccountDocumentDto accountDoc) {
		Result result = new Result(null,false);
		//设置保存客户打款资料状态,银行卡状态
		List<String> bankIdList = batchUpdateBank(accountDoc);
		int hasRequested = 0;
    	if(accountDoc.getIsApproved()){
        	//判断该客户下其他银行信息状态来确定客户打款资料状态
        	Map<String,Object> paramMap = new HashMap<String,Object>();
            paramMap.put("accountId", accountDoc.getId());
            List<AccountBank> bankList = accountBankService.query(paramMap);
            for(AccountBank accountBank: bankList){
            	//过滤当前审核数据
            	if(bankIdList.contains(accountBank.getId().toString())){
            		continue;
            	}
            	//存在审核未通过数据,设置客户打款资料未审核通过
            	if(AccountBankDataStatus.Declined.getCode().equals(accountBank.getBankDataStatus())){
            		accountDoc.setBankDataStatus(AccountBankDataStatus.Declined.getCode());
            		break;
            	}
            	//存在待审核数据,设置客户打款资料待审核
            	if(AccountBankDataStatus.Requested.getCode().equals(accountBank.getBankDataStatus())){
            		accountDoc.setBankDataStatus(AccountBankDataStatus.Requested.getCode());
            		hasRequested++;
            	}
            }
    	}
    	//找到对应的任务并执行下一步
		String auditPass = "1";
    	List<TaskInfo>taskList = new ArrayList<TaskInfo>();
    	if(StringUtils.isNotEmpty(accountDoc.getBankDataStatus())){
        	ListTaskQuery taskQuery =  workFlowService.createAuditTaskQuery(WorkFlowEnum.ACCOUNT_AUDIT_BANK.getValue(),accountDoc.getId().toString());
        	taskList = workFlowService.getAllTaskList(taskQuery);
    		if(AccountBankDataStatus.Declined.getCode().equals(accountDoc.getBankDataStatus())){
    			auditPass = "0";
    		}
    	}
		List<ActivitiVariable> variables = new ArrayList<ActivitiVariable>();
		ActivitiVariable createVar = new ActivitiVariable(WorkFlowConstant.VAR_AUDIT_PASS,auditPass);
		variables.add(createVar);
    	TaskActionInfo actionInfo =workFlowService.getNextTaskInfo(taskList, variables, accountDoc.getId().toString());
    	if(actionInfo.getRequest() != null){
        	//更新客户主表状态,子表状态
    		accountBankService.batchUpdate(accountDoc.getBankList());
        	result = updateStatus(accountDoc);
        	if(!result.isSuccess()){
        		return result;
        	}
        	//全部审核任务执行完 后执行任务
        	if(hasRequested == 0){
        		taskService.invokeTaskAction(actionInfo.getTaskId(),actionInfo.getRequest());
        	}
    	}
    	return result; 
    }
    
    @Override
    protected int getQueryStatus() {
    	return WorkFlowEnum.ACCOUNT_AUDIT_BANK.getValue();
    }
    
    /**
     * 设置批量银行卡保存信息
     * @return
     */
    private List<String> batchUpdateBank(AccountDocumentDto accountDoc){
    	List<AccountBank> list = new ArrayList<AccountBank>();
    	List<String> bankIdList = new ArrayList<String>();
    	AccountBank bank = null;
    	Boolean isApproved = true;
   		int i=0;
    	//更新客户银行信息审核状态
    	if(accountDoc.getDisagreeDescs() != null && accountDoc.getBankIds() != null){
    		for(;i<accountDoc.getBankIds().length;i++){
    			bank = new AccountBank();
    			if(accountDoc.getDisagreeDescs().length <= i || StringUtils.isEmpty(accountDoc.getDisagreeDescs()[i])){
    				bank.setBankDataStatus(AccountBankDataStatus.Approved.getCode());
    			}else{
    				bank.setBankDataStatus(AccountBankDataStatus.Declined.getCode());
    				bank.setDisagreeDesc(accountDoc.getDisagreeDescs()[i]);
    				isApproved = false;
    			}
    			bank.setId(Long.parseLong(accountDoc.getBankIds()[i]));
    			list.add(bank);
    			bankIdList.add(accountDoc.getBankIds()[i]);
    		}
    	}
    	accountDoc.setBankList(list);
    	accountDoc.setIsApproved(isApproved);
    	accountDoc.setBankDataStatus(isApproved ? AccountBankDataStatus.Approved.getCode():AccountBankDataStatus.Declined.getCode());
    	return bankIdList;
    }
}
