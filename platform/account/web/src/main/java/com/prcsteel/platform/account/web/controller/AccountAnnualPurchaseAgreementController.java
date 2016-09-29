package com.prcsteel.platform.account.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.prcsteel.platform.common.aspect.OpAction;
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
import com.prcsteel.platform.account.model.enums.AnnualPurchaseAgreementStatus;
import com.prcsteel.platform.account.model.model.AccountContractTemplate;
import com.prcsteel.platform.account.model.model.AccountExt;
import com.prcsteel.platform.account.model.query.CompanyQuery;
import com.prcsteel.platform.account.service.AnnualPurchaseAgreementService;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.common.service.OpParam;
import com.prcsteel.platform.common.vo.PageResult;
import com.prcsteel.platform.common.vo.Result;

import constant.WorkFlowConstant;

/**
 * Created by dengxiyan on 2016/5/6.
 * 年度采购协议审核控制器
 */
@Controller
@RequestMapping("/flow/annual")
public class AccountAnnualPurchaseAgreementController extends AccountConsignAgreementController{

    private static final Logger logger = LoggerFactory.getLogger(AccountAnnualPurchaseAgreementController.class);

    @Resource
    AnnualPurchaseAgreementService  annualPurchaseAgreementService;
    

    /**
     * 审核买家年度采购协议界面
     *
     * @return
     */
    @RequestMapping("/{accountId}/{auditType}/toauditpurchaseagree")
    public String toAuditPurchaseAgree(ModelMap out, @PathVariable("accountId") Long accountId, @PathVariable("auditType") Integer auditType) {
        AccountDto accountdto = contactService.getCompanyById(accountId);
        AccountExt ext = accountdto.getAccountExt();
        //如果是协议状态为不是已上传待审核，则审核协议内容，否则审核图片
        if (ext != null && !AnnualPurchaseAgreementStatus.Uploaded.getCode().equals(ext.getAnnualPurchaseAgreementStatus())) {
            AccountContractTemplate act = new AccountContractTemplate();
            act.setAccountId(accountId);
            act.setType(AccountContractTemplateType.YEAR_PURCHASE.getCode());
            String content = accountContractTemplateService.fetchTemplateContent(act);
            out.put("agreementcontent", content);
        }
        out.put("accountdto", accountdto);
        out.put("auditType", auditType);
        return "company/workflow/annualPurchaseAgreement";
    }


    /**
     * 审核列表界面
     *
     * @param out
     * @param taskAuditAuth
     * @return
     */
    @RequestMapping("/{taskAuditAuth}/list")
    public String list(ModelMap out, @PathVariable("taskAuditAuth") Integer taskAuditAuth) {
        return super.list(out, taskAuditAuth);
    }

    /**
     * 加载列表数据
     *
     * @param query
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
    @OpParam(value = "auditBuyer", name = "dto")
    @OpParam(value = "auditType", name = "auditType")
    @RequestMapping(value = "/doaudit", method = RequestMethod.POST)
    @ResponseBody
    public Result doAudit(AccountExtDtoForUpdate dto,Integer auditType, Long accountId) {
        Result result = new Result();
        dto.setUser(getLoginUser());
        try{
            //获取当前任务并设置下一个任务的信息
            List<ActivitiVariable> variables = new ArrayList<>();
            variables.add(new ActivitiVariable(WorkFlowConstant.VAR_AUDIT_PASS,dto.getAuditPass()));
            TaskActionInfo taskActionInfo = getNextTaskInfo(auditType, variables, dto.getAccountId());

            //执行业务操作及流程
            annualPurchaseAgreementService.auditAnnualPurchaseAgreement(dto,taskActionInfo);
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