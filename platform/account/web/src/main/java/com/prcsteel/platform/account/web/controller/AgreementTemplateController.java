package com.prcsteel.platform.account.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.prcsteel.rest.sdk.activiti.pojo.ActivitiVariable;
import org.prcsteel.rest.sdk.activiti.pojo.TaskActionInfo;
import org.prcsteel.rest.sdk.activiti.pojo.TaskInfo;
import org.prcsteel.rest.sdk.activiti.query.ListTaskQuery;
import org.prcsteel.rest.sdk.activiti.request.StartProcessRequest;
import org.prcsteel.rest.sdk.activiti.request.TaskActionRequest;
import org.prcsteel.rest.sdk.activiti.service.ProcessService;
import org.prcsteel.rest.sdk.activiti.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.prcsteel.platform.account.model.dto.AgreementTemplateDto;
import com.prcsteel.platform.account.model.enums.AccountContractTemplateType;
import com.prcsteel.platform.account.model.enums.AnnualPurchaseAgreementStatus;
import com.prcsteel.platform.account.model.enums.SellerConsignAgreementStatus;
import com.prcsteel.platform.account.model.model.AccountContractTemplate;
import com.prcsteel.platform.account.model.model.AccountContractTemplateBarcodeHistory;
import com.prcsteel.platform.account.model.model.AccountExt;
import com.prcsteel.platform.account.service.AccountContractTemplateService;
import com.prcsteel.platform.account.service.AccountService;
import com.prcsteel.platform.account.service.WorkFlowCommonService;
import com.prcsteel.platform.account.service.WorkFlowService;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.common.vo.Result;

import constant.WorkFlowConstant;

/**
 * 协议模板
 * Created by dengxiyan on 2016/3/31.
 */
@Controller
@RequestMapping("/agreementTemplate")
public class AgreementTemplateController extends BaseController{
    @Resource
    private AccountContractTemplateService accountContractTemplateService;

    @Resource
    private AccountService accountService;

    @Resource
    private TaskService taskService;
    
	@Resource
	private WorkFlowService workFlowService;

    private static final Logger logger = LoggerFactory.getLogger(AgreementTemplateController.class);

    //代运营协议打印和导出需替换原编辑的内容格式
    private static final String CONSIGN_REPLACED_TEXT = "此处可修改";//代运营协议被替换的文本
    private static final String CONSIGN_REPLACE_TEXT = "&#12288;&#12288;&#12288;&#12288;&#12288;&#12288;";//代运营协议替换的文本

    @Resource
    ProcessService processService;

    @Resource
    WorkFlowCommonService workFlowCommonService;

    /**
     * 进入代运营协议编辑界面
     * @return
     */
    @RequestMapping("/editconsign")
    public String editConsign(ModelMap out,AccountContractTemplate param,@RequestParam(value="isReload",required=false,defaultValue = "false") Boolean isReload){
        //请求参数：当前客户id
        Long companyId = param.getAccountId();
        if (companyId == null){
            return null ;
        }

        try{
            AccountExt account = accountService.queryAccountExtByAccountId(companyId);
            if (account == null){
                return null;
            }

            String status = account.getSellerConsignAgreementStatus();
            // 协议状态为“待打印”、“审核未通过”、“一审未通过”、“二审未通过”时才能编辑，否则返回null
            if (!SellerConsignAgreementStatus.ToPrint.getCode().equals(status) && !SellerConsignAgreementStatus.Declined.getCode().equals(status)
                    && !SellerConsignAgreementStatus.FirstDeclined.getCode().equals(status) && !SellerConsignAgreementStatus.SecondDeclined.getCode().equals(status)){
                return null;
            }

            //协议状态为 审核不通过、一审不通过、二审不通过 时显示“恢复系统默认模板”按钮，否则不显示
            out.put("showRecoverBtn", SellerConsignAgreementStatus.Declined.getCode().equals(status) || SellerConsignAgreementStatus.FirstDeclined.getCode().equals(status) ||SellerConsignAgreementStatus.SecondDeclined.getCode().equals(status));

            //代运营类型协议
            param.setType(AccountContractTemplateType.CONSIGN.getCode());
            //如果是恢复系统默认模板的请求，则获取默认模板，否则获取（已经编辑过协议则获取自定义的协议，否则获取系统默认的）
            if(isReload){
                param.setAccountId(0l);//用于查询系统默认模板
            }else{
                param.setAccountId(getCompanyId(param));//用于查询模板 已经编辑过协议则获取自定义的协议，否则获取系统默认的
            }
            AgreementTemplateDto info = accountContractTemplateService.getConsignAgreementTemplateInfo(param, companyId);

            out.put("info",info);
            out.put("isReload",isReload);
        }catch(Exception e){
            logger.error("读取代运营协议模板错误",e);
        }
        return "account/contracttemplate/editConsign";
    }

    /**
     * 进入年度采购协议编辑界面
     * @return
     */
    @RequestMapping("/edityearpurchase")
    public String editYearPurchase(ModelMap out, AccountContractTemplate param){
        //请求参数：当前客户id
        Long companyId = param.getAccountId();
        if (companyId == null){
            return null ;
        }

        try{

            AccountExt account = accountService.queryAccountExtByAccountId(companyId);
            if (account == null){
                return null;
            }

            String status = account.getAnnualPurchaseAgreementStatus();
            // 协议状态为“待打印”、“审核未通过”、“一审未通过”、“二审未通过”时才能编辑，否则返回null
            if (!AnnualPurchaseAgreementStatus.ToPrint.getCode().equals(status) && !AnnualPurchaseAgreementStatus.Declined.getCode().equals(status)
                    && !AnnualPurchaseAgreementStatus.FirstDeclined.getCode().equals(status) && !AnnualPurchaseAgreementStatus.SecondDeclined.getCode().equals(status)){
                return null;
            }

            //协议状态为 审核不通过、一审不通过、二审不通过 时显示“恢复系统默认模板”按钮，否则不显示
            out.put("showRecoverBtn", AnnualPurchaseAgreementStatus.Declined.getCode().equals(status) || AnnualPurchaseAgreementStatus.FirstDeclined.getCode().equals(status) || AnnualPurchaseAgreementStatus.SecondDeclined.getCode().equals(status));

            AccountContractTemplate tempate = accountContractTemplateService.getYearPurchaseTemplate(param);
            out.put("contractInfo", accountContractTemplateService.getYearPurchaseContractInfoDto(companyId));
            out.put("companyId", companyId);//需要编辑的客户id
            out.put("renewAfterExpriration", tempate.getRenewAfterExpriration());//到期后续签
            out.put("content", tempate.getContent());
        }catch(Exception e){
            logger.error("读取模板错误", e);
        }
        return "account/contracttemplate/editYearPurchase";
    }

    /**
     * 保存年度采购协议
     */
    @RequestMapping("/saveyearpurchase")
    @ResponseBody
    public Result saveYearPurchase(AccountContractTemplate param){
        Result result = new Result();
        AccountExt oldAccountExt = accountService.queryAccountExtByAccountId(param.getAccountId());
        String oldStatus = oldAccountExt == null? "":oldAccountExt.getAnnualPurchaseAgreementStatus();
        try{
            accountContractTemplateService.saveYearPurchaseTemplate(param, getLoginUser());
            AccountExt newAccountExt = accountService.queryAccountExtByAccountId(param.getAccountId());
            //工作流启动判断
            StartProcessRequest startRequest;
            String isUpload = "0";//编辑未上传
            //待审核
            if (AnnualPurchaseAgreementStatus.Requested.getCode().equals(newAccountExt.getAnnualPurchaseAgreementStatus())){
                //待打印->待审核 启动流程 判断流程是否结束 ，未结束执行下一个任务 结束，启动新的
                if (AnnualPurchaseAgreementStatus.ToPrint.getCode().equals(oldStatus)){
                    if (workFlowCommonService.isProcessEnded(WorkFlowConstant.BUYER_AGREEMENT_PROCESS_DEFINITION_KEY,newAccountExt.getCustAccountId().toString())){
                        startRequest = workFlowService.createAnnualPurchaseAgreementWorkFlow(param.getAccountId(), isUpload);
                        processService.startProcess(startRequest);
                    }else{
                        List<ActivitiVariable> variables = new ArrayList<>();
                        variables.add(new ActivitiVariable(WorkFlowConstant.VAR_AGREEMENT_ISUPLOAD,isUpload));
                        //推动到下一个任务
                        invokeNextTaskSellerConsignAgreement(WorkFlowConstant.BUYER_AGREEMENT_EDIT_AGREEMENT, variables, newAccountExt.getCustAccountId());
                    }

                    //一审未通过、二审未通过、审核未通过->待审核，继续执行任务
                }else if(AnnualPurchaseAgreementStatus.FirstDeclined.getCode().equals(oldStatus)
                        || AnnualPurchaseAgreementStatus.SecondDeclined.getCode().equals(oldStatus)
                        || AnnualPurchaseAgreementStatus.Declined.getCode().equals(oldStatus)){
                    List<ActivitiVariable> variables = new ArrayList<>();
                    variables.add(new ActivitiVariable(WorkFlowConstant.VAR_AGREEMENT_ISUPLOAD, isUpload));
                    //推动到下一个任务
                    invokeNextTaskSellerConsignAgreement(WorkFlowConstant.BUYER_AGREEMENT_EDIT_AGREEMENT,variables,param.getAccountId());
                }
            }
            result.setSuccess(Boolean.TRUE);
        }catch (BusinessException b){
            result.setData(b.getMsg());
            result.setSuccess(Boolean.FALSE);
            logger.error(b.getMsg(),b);
        }catch (Exception e){
            result.setData("提交年度采购协议失败");
            result.setSuccess(Boolean.FALSE);
            logger.error("提交年度采购协议失败", e);
        }
        return result;
    }

    /**
     * 提交代运营协议
     * @param param
     * @return
     */
    @RequestMapping("/submitconsign")
    @ResponseBody
    public Result submitConsign(AccountContractTemplate param){
        Result result = new Result();
        AccountExt oldAccountExt = accountService.queryAccountExtByAccountId(param.getAccountId());
        String oldStatus = oldAccountExt == null?"":oldAccountExt.getSellerConsignAgreementStatus();
        try{
            accountContractTemplateService.saveConsignTemplate(param, getLoginUser());
            AccountExt newAccountExt = accountService.queryAccountExtByAccountId(param.getAccountId());
            //工作流启动判断
            StartProcessRequest startRequest;
            String isUpload = "0";//编辑未上传

            //待审核
            if (SellerConsignAgreementStatus.Requested.getCode().equals(newAccountExt.getSellerConsignAgreementStatus())){
                //待打印->待审核 启动流程
                if (SellerConsignAgreementStatus.ToPrint.getCode().equals(oldStatus)){
                    if (workFlowCommonService.isProcessEnded(WorkFlowConstant.SELLER_AGREEMENT_PROCESS_DEFINITION_KEY,newAccountExt.getCustAccountId().toString())){
                        startRequest = workFlowService.createSellerConsignAgreementWorkFlow(param.getAccountId(),isUpload);
                        processService.startProcess(startRequest);
                    }else{
                        List<ActivitiVariable> variables = new ArrayList<>();
                        variables.add(new ActivitiVariable(WorkFlowConstant.VAR_AGREEMENT_ISUPLOAD,isUpload));
                        //推动到下一个任务
                        invokeNextTaskSellerConsignAgreement(WorkFlowConstant.SELLER_AGREEMENT_EDIT_AGREEMENT, variables, newAccountExt.getCustAccountId());
                    }

                    //一审未通过、二审未通过、审核未通过->待审核，继续执行任务
                }else if(SellerConsignAgreementStatus.FirstDeclined.getCode().equals(oldStatus)
                        || SellerConsignAgreementStatus.SecondDeclined.getCode().equals(oldStatus)
                        || SellerConsignAgreementStatus.Declined.getCode().equals(oldStatus)){
                    List<ActivitiVariable> variables = new ArrayList<>();
                    variables.add(new ActivitiVariable(WorkFlowConstant.VAR_AGREEMENT_ISUPLOAD,isUpload));
                    //推动到下一个任务
                    invokeNextTaskSellerConsignAgreement(WorkFlowConstant.SELLER_AGREEMENT_EDIT_AGREEMENT,variables,param.getAccountId());
                }
            }
            result.setSuccess(Boolean.TRUE);
        }catch (BusinessException b){
            result.setData(b.getMsg());
            result.setSuccess(Boolean.FALSE);
            logger.error(b.getMsg(), b);
        }catch (Exception e){
            result.setData("提交代运营协议失败");
            result.setSuccess(Boolean.FALSE);
            logger.error("提交代运营协议失败", e);
        }
        return result;
    }

    /**
     * 恢复 年度采购协议 到系统默认模板
     */
    @RequestMapping("/recoveryearpurchase")
    @ResponseBody
    public Result recoverYearPurchase(AccountContractTemplate param){
        Result result = new Result();
        try{
            //协议内容 恢复到默认模板
            AccountContractTemplate defaultQuery = new AccountContractTemplate();
            defaultQuery.setAccountId(0l);
            param.setContent(accountContractTemplateService.getYearPurchaseTemplate(defaultQuery).getContent());

            accountContractTemplateService.recoverYearPurchaseTemplate(param, getLoginUser());
            result.setSuccess(Boolean.TRUE);
        }catch (BusinessException b){
            result.setData(b.getMsg());
            result.setSuccess(Boolean.FALSE);
            logger.error(b.getMsg(), b);
        }catch (Exception e){
            result.setData("恢复 年度采购协议 到系统默认的模板失败");
            result.setSuccess(Boolean.FALSE);
            logger.error("恢复 年度采购协议 到系统默认的模板失败", e);
        }
        return result;
    }


    /**
     * 恢复 代运营协议 到系统默认模板
     */
    @RequestMapping("/recoverconsign")
    @ResponseBody
    public Result recoverConsign(AccountContractTemplate param){
        Result result = new Result();
        try{
            //协议内容 恢复到默认模板
            AccountContractTemplate defaultQuery = new AccountContractTemplate();
            defaultQuery.setAccountId(0l);
            defaultQuery.setType((AccountContractTemplateType.CONSIGN.getCode()));
            param.setContent(accountContractTemplateService.queryOneAccountContractTemplate(defaultQuery).getContent());

            accountContractTemplateService.recoverConsignTemplate(param, getLoginUser());
            result.setSuccess(Boolean.TRUE);
        }catch (BusinessException b){
            result.setData(b.getMsg());
            result.setSuccess(Boolean.FALSE);
            logger.error(b.getMsg(), b);
        }catch (Exception e){
            result.setData("恢复 代运营协议 到系统默认的模板失败");
            result.setSuccess(Boolean.FALSE);
            logger.error("恢复 代运营协议 到系统默认的模板失败", e);
        }
        return result;
    }

    /**
     * 进入 打印年度采购协议 页面
     */
    @RequestMapping("/toprintyearpurchase")
    public String toPrintYearPurchase(ModelMap out, AccountContractTemplate param) {
        //请求参数：当前客户id
        Long companyId = param.getAccountId();
        if (companyId == null) {
            return null;
        }

        try {

            AccountExt account = accountService.queryAccountExtByAccountId(companyId);
            if (account == null){
                return null;
            }
            // 协议状态为“待打印”、“审核未通过”、“一审未通过”、“二审未通过”、“二审通过”时才能打印
            String status = account.getAnnualPurchaseAgreementStatus();
            if (!AnnualPurchaseAgreementStatus.ToPrint.getCode().equals(status) && !AnnualPurchaseAgreementStatus.Declined.getCode().equals(status) && !AnnualPurchaseAgreementStatus.SecondApproved.getCode().equals(status)
                    && !AnnualPurchaseAgreementStatus.FirstDeclined.getCode().equals(status) && !AnnualPurchaseAgreementStatus.SecondDeclined.getCode().equals(status)){
                return null;
            }

            AgreementTemplateDto info = accountContractTemplateService.getYearPurchaseAgreementTemplateInfo(param, companyId);
            String barCode = account.getAnnualPurchaseBarCode();
            //判断是是否要生成新的条形码，如果要生成则更新条形码并记录条码历史记录
            if (StringUtils.isEmpty(barCode)){
                barCode = accountContractTemplateService.updateYearPurchaseBarCode(companyId, getLoginUser());
            }
            info.setBarCode(barCode);

            //条码的打印信息
            AccountContractTemplateBarcodeHistory printed = accountContractTemplateService.queryBarCodeHistoryByBarcode(barCode);
            info.setPrintTimes(printed.getPrintTimes());
            info.setLastPrintDate(printed.getLastPrintDate());

            out.put("contractInfo", accountContractTemplateService.getYearPurchaseContractInfoDto(companyId));
            out.put("info", info);
        } catch (Exception e) {
            logger.error("获取年度采购协议错误", e);
        }

        return "account/contracttemplate/toPrintYearPurchase";
    }

    /**
     * 打印年度采购协议
     * @param barCode
     * @return
     */
    @RequestMapping("/doprintyearpurchase")
    @ResponseBody
    public Result doPrintYearPurchase(String barCode){
        Result result = new Result();
        try{
            //更新打印信息
            accountContractTemplateService.updateTemplateBarCodeHisitoryByBarcode(barCode, getLoginUser());

            //获取打印后的信息
            AccountContractTemplateBarcodeHistory printed = accountContractTemplateService.queryBarCodeHistoryByBarcode(barCode);
            result.setData(printed);
            result.setSuccess(Boolean.TRUE);
        }catch (BusinessException b){
            result.setData(b.getMsg());
            result.setSuccess(Boolean.FALSE);
            logger.error(b.getMsg(), b);
        }catch (Exception e){
            result.setData("打印更新信息失败");
            result.setSuccess(Boolean.FALSE);
            logger.error("打印更新信息失败", e);
        }
        return result;
    }

    /**
     * 进入代运营协议打印界面
     * @return
     */
    @RequestMapping("/printconsignpage")
    public String printConsignPage(ModelMap out,AccountContractTemplate param){
        //请求参数：当前客户id
        Long companyId = param.getAccountId();
        if (companyId == null){
            return null ;
        }
        try{

            AccountExt account = accountService.queryAccountExtByAccountId(companyId);
            if (account == null){
                return null;
            }

            String status = account.getSellerConsignAgreementStatus();
            // 协议状态为“待打印”、“审核未通过”、“一审未通过”、“二审未通过”、“二审通过”时才能编辑，否则返回null
            if (!SellerConsignAgreementStatus.ToPrint.getCode().equals(status) && !SellerConsignAgreementStatus.Declined.getCode().equals(status) && !SellerConsignAgreementStatus.SecondApproved.getCode().equals(status)
                    && !SellerConsignAgreementStatus.FirstDeclined.getCode().equals(status) && !SellerConsignAgreementStatus.SecondDeclined.getCode().equals(status)){
                return null;
            }

            //代运营类型协议
            param.setType(AccountContractTemplateType.CONSIGN.getCode());
            //获取代运营协议的模板（已经编辑过协议则获取自定义的协议，否则获取系统默认的）
            param.setAccountId(getCompanyId(param));//用于查询模板
            AgreementTemplateDto info = accountContractTemplateService.getConsignAgreementTemplateInfo(param, companyId);
            info.setContent(replaceConsignText(info.getContent()));

            String barCode = account.getSellerConsignBarCode();
            //判断是是否要生成新的条形码，如果要生成则更新条形码并记录条码历史记录
            if (StringUtils.isEmpty(account.getSellerConsignBarCode())){
                barCode = accountContractTemplateService.updateConsignBarCode(companyId, getLoginUser());
            }
            info.setBarCode(barCode);

            //条码的打印信息
            AccountContractTemplateBarcodeHistory printed = accountContractTemplateService.queryBarCodeHistoryByBarcode(barCode);
            info.setPrintTimes(printed.getPrintTimes());
            info.setLastPrintDate(printed.getLastPrintDate());

            out.put("info", info);
        }catch(Exception e){
            logger.error("读取代运营协议模板错误",e);
        }
        return "account/contracttemplate/printConsign";
    }


    /**
     *
     * 如果有公司自定义模板 返回companyId是当前公司id,否则查询系统自定义模板 返回companyId=0
     * @param param
     * @return
     */
    private Long getCompanyId(AccountContractTemplate param){
        String content = accountContractTemplateService.fetchTemplateContent(param);
        if(content == null){
            return 0l;
        }
        return param.getAccountId();
    }


    /**
     * 打印代运营协议
     * 根据条码累计打印次数
     * @param barCode 条码
     * @return
     */
    @RequestMapping("/{barCode}/doprintconsign")
    @ResponseBody
    public Result doPrintConsign(@PathVariable String barCode){
        Result result = new Result();
        try{
            //更新打印信息
            accountContractTemplateService.updateTemplateBarCodeHisitoryByBarcode(barCode, getLoginUser());

            //获取打印后的信息
            AccountContractTemplateBarcodeHistory printed = accountContractTemplateService.queryBarCodeHistoryByBarcode(barCode);
            result.setData(printed);
            result.setSuccess(Boolean.TRUE);
        }catch (BusinessException b){
            result.setData(b.getMsg());
            result.setSuccess(Boolean.FALSE);
            logger.error(b.getMsg(),b);
        }catch (Exception e){
            result.setData("打印更新信息失败");
            result.setSuccess(Boolean.FALSE);
            logger.error("打印更新信息失败",e);
        }
        return result;
    }


    private String replaceConsignText(String content){
        if (content != null){
            return content.replace(CONSIGN_REPLACED_TEXT, CONSIGN_REPLACE_TEXT);
        }
        return null;
    }

    /**
     * 执行下一步代运营协议审核任务
     * @param taskDefinitionKey 任务id
     * @param variables 变量
     * @param accountId 客户id
     * @return
     */
    private void invokeNextTaskSellerConsignAgreement(String taskDefinitionKey,List<ActivitiVariable> variables,Long accountId) {
        try{
            //根据任务节点id查询当前节点的所有任务
            ListTaskQuery taskQuery =  workFlowService.createEditTaskQuery(taskDefinitionKey,accountId.toString());
            List<TaskInfo> taskList = workFlowService.getAllTaskList(taskQuery);
            //查找并设置下一个任务的参数
            TaskActionInfo actionInfo =workFlowService.getNextTaskInfo(taskList, variables, accountId.toString());
            TaskActionRequest taskActionRequest = actionInfo.getRequest();
            if(taskActionRequest != null){
                //流转到下个任务
                taskService.invokeTaskAction(actionInfo.getTaskId(), taskActionRequest);
            }
        }catch (Exception e){
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, e.getMessage());
        }
    }

}
