package com.prcsteel.platform.account.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.prcsteel.platform.common.aspect.OpAction;
import org.prcsteel.rest.sdk.activiti.enums.WorkFlowEnum;
import org.prcsteel.rest.sdk.activiti.pojo.ActivitiVariable;
import org.prcsteel.rest.sdk.activiti.pojo.TaskActionInfo;
import org.prcsteel.rest.sdk.activiti.pojo.TaskInfo;
import org.prcsteel.rest.sdk.activiti.query.ListTaskQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.prcsteel.platform.account.model.dto.AccountDto;
import com.prcsteel.platform.account.model.dto.AccountExtDtoForUpdate;
import com.prcsteel.platform.account.model.enums.AccountContractTemplateType;
import com.prcsteel.platform.account.model.enums.SellerConsignAgreementStatus;
import com.prcsteel.platform.account.model.model.AccountContractTemplate;
import com.prcsteel.platform.account.model.model.AccountExt;
import com.prcsteel.platform.account.model.query.CompanyQuery;
import com.prcsteel.platform.account.service.AccountContractTemplateService;
import com.prcsteel.platform.account.service.AccountService;
import com.prcsteel.platform.account.service.SellerConsignAgreementService;
import com.prcsteel.platform.account.service.WorkFlowService;
import com.prcsteel.platform.acl.model.model.Organization;
import com.prcsteel.platform.acl.model.model.Role;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.enums.OpType;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.common.service.OpLog;
import com.prcsteel.platform.common.service.OpParam;
import com.prcsteel.platform.common.vo.PageResult;
import com.prcsteel.platform.common.vo.Result;

import constant.WorkFlowConstant;


/**
 * Created by dengxiyan on 2016/4/29.
 * 卖家代运营协议审核控制器
 */
@Controller
@RequestMapping("/flow/consignagreement")
public class AccountConsignAgreementController extends AccountDocumentController {

    @Resource
    protected AccountContractTemplateService accountContractTemplateService;

    @Resource
    protected SellerConsignAgreementService sellerConsignAgreementService;

    @Resource
    protected AccountService accountService;
    
    @Resource
    protected WorkFlowService workFlowService;

    private static final Logger logger = LoggerFactory.getLogger(AccountConsignAgreementController.class);

    /**
     * 审核代运营协议资料
     *
     * @return
     */
    @RequestMapping("/{accountId}/{auditType}/toauditconsignagree")
    public String toAuditConsignAgree(ModelMap out, @PathVariable("accountId") Long accountId, @PathVariable("auditType") Integer auditType) {
        AccountDto accountdto = contactService.getCompanyById(accountId);
        AccountExt ext = accountdto.getAccountExt();
        //如果是协议状态为不是已上传待审核，则审核协议内容，否则审核图片
        if (ext != null && !SellerConsignAgreementStatus.Uploaded.getCode().equals(ext.getSellerConsignAgreementStatus())) {
            AccountContractTemplate act = new AccountContractTemplate();
            act.setAccountId(accountId);
            act.setType(AccountContractTemplateType.CONSIGN.getCode());
            String content = accountContractTemplateService.fetchTemplateContent(act);
            out.put("agreementcontent", content);
        }
        out.put("accountdto", accountdto);
        out.put("auditType", auditType);
        return "company/workflow/sellerConsignAgreement";
    }

    /**
     * 审核列表界面
     * @param out
     * @return
     */
    @RequestMapping("/{taskAuditAuth}/list")
    public String list(ModelMap out,@PathVariable("taskAuditAuth") Integer taskAuditAuth) {
        List<Organization> orgList = organizationService.queryAllBusinessOrg();
        User loginUser = getLoginUser();
    	StringBuffer ids = new StringBuffer();
    	//根据状态查询对应的task列表
    	ListTaskQuery taskQuery =  workFlowService.createAuditTaskQuery(taskAuditAuth,null);
    	List<TaskInfo> taskList = workFlowService.getAllTaskList(taskQuery);
    	List<Long> orgIdList = null;
    	Role userRole = getUserRole();
		for(TaskInfo task:taskList){
			var:for(ActivitiVariable var:task.getVariables()){
				if(!WorkFlowConstant.VAR_ID.equals(var.getName())){
					continue;
				}
				if(userRole.getType().intValue() == Constant.ROLE_TYPE.ALL.getValue()){
					ids.append(var.getValue());
					ids.append(",");
					continue;
				}
				//总经理权限审核,筛选服务中心
				if(WorkFlowEnum.ACCOUNT_AUDIT_ANNUAL_PURCHASE_MANAGER.getValue().intValue() == taskAuditAuth.intValue() ||
						WorkFlowEnum.ACCOUNT_AUDIT_SELLER_CONSIGN_MANAGER.getValue().intValue() == taskAuditAuth.intValue()){
					orgIdList = accountService.queryOrgIdsByAccountId(Long.parseLong(var.getValue()));
					for(Long orgId:orgIdList){
						if(orgId.longValue() == loginUser.getOrgId().longValue()){
							ids.append(var.getValue());
							ids.append(",");
							break var;
						}
					}
				}else{
					ids.append(var.getValue());
					ids.append(",");
				}

			}
		}
		if(ids.length() > 0){
			ids.delete(ids.length()-1, ids.length());
		}
        out.put("orgs", orgList);
        out.put("ids", ids.toString());
        out.put("queryStatus", taskAuditAuth);
        return "company/workflow/cardInfoList";
    }

    /**
     * 加载列表数据
     *
     * @return
     */
    @RequestMapping(value = "/loadlist", method = RequestMethod.POST)
    @ResponseBody
    public PageResult loadList(CompanyQuery query) {
        return super.loadList(query);
    }


    /**
     * 审核
     * @param dto
     * @param auditType 审核类型
     * @return
     */
    @OpAction( key = "accountId")
    @OpLog(OpType.AuditConsignAgreement)
    @OpParam(value = "auditSeller", name = "dto")
    @OpParam(value = "auditType", name = "auditType")
    @RequestMapping(value = "/doaudit", method = RequestMethod.POST)
    @ResponseBody
    public Result doAudit(AccountExtDtoForUpdate dto, Integer auditType,Long accountId) {
        Result result = new Result();
        dto.setUser(getLoginUser());
        try{
            //获取当前任务并设置下一个任务的信息
            List<ActivitiVariable> variables = new ArrayList<>();
            variables.add(new ActivitiVariable(WorkFlowConstant.VAR_AUDIT_PASS,dto.getAuditPass()));
            TaskActionInfo taskActionInfo = getNextTaskInfo(auditType, variables, dto.getAccountId());

            //执行业务操作及流程
            sellerConsignAgreementService.auditConsignAgreement(dto,taskActionInfo);
        }catch (BusinessException b){
            logger.error(b.getMsg(),b);
            result.setData(b.getMsg());
            result.setSuccess(false);
            return result;
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            result.setData(e.getMessage());
            result.setSuccess(false);
            return result;
        }
        return result;
    }

    /*
     * 执行下一步代运营协议审核任务
     * @param taskAuditAuth 任务id权限标识
     * @param variables  变量
     * @param accountId  客户id
     * @return
     */
    private TaskActionInfo getNextTaskInfo(Integer taskAuditAuth, List<ActivitiVariable> variables, Long accountId) {
        //根据任务节点id查询当前节点的所有任务
        ListTaskQuery taskQuery = workFlowService.createAuditTaskQuery(taskAuditAuth,accountId.toString());
        List<TaskInfo> taskList = workFlowService.getAllTaskList(taskQuery);
        //查找并设置下一个任务的参数
        return workFlowService.getNextTaskInfo(taskList,variables, accountId.toString());
    }

}
