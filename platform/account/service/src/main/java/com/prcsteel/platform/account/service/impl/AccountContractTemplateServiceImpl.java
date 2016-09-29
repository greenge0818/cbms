package com.prcsteel.platform.account.service.impl;

import com.prcsteel.platform.account.model.dto.AgreementTemplateDto;
import com.prcsteel.platform.account.model.dto.ContractInfoDto;
import com.prcsteel.platform.account.model.dto.ContractTemplateDto;
import com.prcsteel.platform.account.model.enums.AccountContractTemplateBarCodeType;
import com.prcsteel.platform.account.model.enums.AccountContractTemplateType;
import com.prcsteel.platform.account.model.enums.AnnualPurchaseAgreementStatus;
import com.prcsteel.platform.account.model.enums.ContractTemplateStatus;
import com.prcsteel.platform.account.model.enums.SellerConsignAgreementStatus;
import com.prcsteel.platform.account.model.model.Account;
import com.prcsteel.platform.account.model.model.AccountContractTemplate;
import com.prcsteel.platform.account.model.model.AccountContractTemplateBarcodeHistory;
import com.prcsteel.platform.account.model.model.AccountContractTemplateOprt;
import com.prcsteel.platform.account.model.model.AccountExt;
import com.prcsteel.platform.account.model.query.CompanyQuery;
import com.prcsteel.platform.account.persist.dao.AccountContractTemplateBarcodeHistoryDao;
import com.prcsteel.platform.account.persist.dao.AccountContractTemplateDao;
import com.prcsteel.platform.account.persist.dao.AccountDao;
import com.prcsteel.platform.account.persist.dao.AccountExtDao;
import com.prcsteel.platform.account.service.AccountContractTemplateService;
import com.prcsteel.platform.account.service.WorkFlowService;
import com.prcsteel.platform.acl.model.model.Organization;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.acl.persist.dao.OrganizationDao;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.common.utils.NumberToCNUtils;
import com.prcsteel.platform.common.utils.NumberTool;
import com.prcsteel.platform.common.utils.Tools;
import constant.WorkFlowConstant;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.prcsteel.rest.sdk.activiti.enums.WorkFlowEnum;
import org.prcsteel.rest.sdk.activiti.pojo.TaskActionInfo;
import org.prcsteel.rest.sdk.activiti.pojo.TaskInfo;
import org.prcsteel.rest.sdk.activiti.query.ListTaskQuery;
import org.prcsteel.rest.sdk.activiti.request.StartProcessRequest;
import org.prcsteel.rest.sdk.activiti.service.ProcessService;
import org.prcsteel.rest.sdk.activiti.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Rolyer on 2016/1/21.
 */
@Service("accountContractTemplateService")
public class AccountContractTemplateServiceImpl implements AccountContractTemplateService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    public static final String FILE_SEPARATOR = System.getProperties().getProperty("file.separator");
    public static final String TEMPLATE_NAME_CONSIGN = "卖家代运营协议";
    public static final String TEMPLATE_NAME_YEAR_PURCHASE = "买家年度采购协议";



    @Resource
    private AccountContractTemplateDao accountContractTemplateDao;
    @Resource
    private AccountDao accountDao;
    @Resource
    private OrganizationDao organizationDao;

    @Resource
    private AccountExtDao accountExtDao;

    @Resource
    private AccountContractTemplateBarcodeHistoryDao accountContractTemplateBarcodeHistoryDao;

    @Resource
    WorkFlowService workFlowService;
    @Resource
    private TaskService taskService;
    @Resource
    ProcessService processService;

    @Override
    public List<AccountContractTemplate> querySysTemplate() {
        AccountContractTemplate act = new AccountContractTemplate();
        act.setAccountId(0L);
        return accountContractTemplateDao.selectByModel(act);
    }

    @Override
    @Transactional
    public Boolean saveContractTemplate(AccountContractTemplate act) {
        int result;
        //工作流参数
        StartProcessRequest startRequest;
        ListTaskQuery taskQuery;
        List<TaskInfo> taskList;

        if (act.getId()!=null && act.getId().intValue()>0) {
            result = updateSysContractTemplate(act);
            //编辑合同模板，继续工作流任务
            taskQuery =  workFlowService.createEditTaskQuery(WorkFlowConstant.TEMPLATE_EDIT_TEMPLATE,act.getId().toString());
            taskList = workFlowService.getAllTaskList(taskQuery);

            //合同模板已审核通过后（之前的流程已结束）再编辑，启用新的工作流任务
            if(CollectionUtils.isEmpty(taskList)){
                startRequest = workFlowService.createStartRequest(WorkFlowEnum.ACCOUNT_AUDIT_CONTRACT_TEMPLATE.getValue().toString(),
                        act.getId(),
                        WorkFlowEnum.CONTRACT_TEMPLATE,WorkFlowConstant.TEMPLATE_PROCESS_DEFINITION_KEY);
                processService.startProcess(startRequest);
            }
            //继续流程
            else{
                TaskActionInfo actionInfo =workFlowService.getNextTaskInfo(taskList, null, act.getAccountId().toString());
                if(actionInfo.getRequest() != null){
                    taskService.invokeTaskAction(actionInfo.getTaskId(),actionInfo.getRequest());
                }
            }

        } else {
            result = addSysContractTemplate(act);

            //新增合同模板，启用新的工作流任务
            startRequest = workFlowService.createStartRequest(WorkFlowEnum.ACCOUNT_AUDIT_CONTRACT_TEMPLATE.getValue().toString(),
                    act.getId(),
                    WorkFlowEnum.CONTRACT_TEMPLATE,WorkFlowConstant.TEMPLATE_PROCESS_DEFINITION_KEY);
            processService.startProcess(startRequest);
        }

        return result>0;
    }

    private int addSysContractTemplate(AccountContractTemplate act){

        act.setStatus(ContractTemplateStatus.PENDING.getValue());
        act.setEnabled(0);
        act.setCreated(new Date());
        act.setLastUpdated(new Date());

        return accountContractTemplateDao.insertSelective(act);
    }

    private int updateSysContractTemplate(AccountContractTemplate act) {
        act.setStatus(ContractTemplateStatus.PENDING.getValue());
        act.setLastUpdated(new Date());

        return accountContractTemplateDao.updateByPrimaryKeySelective(act);
    }

    @Override
    public AccountContractTemplate queryAccountContractTemplate(Long id) {
        AccountContractTemplate act = new AccountContractTemplate();
        act.setId(id);
        return queryOneAccountContractTemplate(act);
    }

    @Override
    public AccountContractTemplate queryOneAccountContractTemplate(AccountContractTemplate act) {
        List<AccountContractTemplate> list = accountContractTemplateDao.selectByModel(act);
        if (list!=null && list.size()>0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    @Override
    public Boolean approvedContractTemplate(Long id, Boolean approved) {
        AccountContractTemplate act = new AccountContractTemplate();
        act.setId(id);
        act.setStatus(approved ? ContractTemplateStatus.APPROVED.getValue() : ContractTemplateStatus.DISAPPROVED.getValue());
        act.setLastUpdated(new Date());
        return accountContractTemplateDao.updateByPrimaryKeySelective(act) > 0;
    }

    @Override
    public Boolean enabledContractTemplate(Long id, Boolean enabled) {
        AccountContractTemplate act = new AccountContractTemplate();
        act.setId(id);
        act.setEnabled(enabled ? 1 : 0);
        act.setLastUpdated(new Date());
        return accountContractTemplateDao.updateByPrimaryKeySelective(act) > 0;
    }

    @Override
    public String fetchTemplateContent(AccountContractTemplate act) {
        AccountContractTemplate template = queryOneAccountContractTemplate(act);
        if (template!=null) {
            return template.getContent();
        } else {
            return null;
        }
    }

    @Override
    public String resolveTemplate(Long id, String type, Long accountId) {
        if(type.equals(AccountContractTemplateType.CONSIGN.getCode()) || type.equals(AccountContractTemplateType.YEAR_PURCHASE.getCode())){
            return fetchTemplateContent(id);
        }else{
            String template = readTemplate(type, false);
            String content = fetchTemplateContent(id);
            ContractInfoDto info = fakeContractInfo();
            //指定了客户ID
            if (accountId != null && accountId.intValue() > 0) {
                info = restContractInfo(accountId, info);
            }

            return fillSysTemplate(content, template, info);
        }
    }

    @Override
    public Boolean deleteTemplateById(Long id) {
        return accountContractTemplateDao.deleteByPrimaryKey(id) > 0;
    }

    /**
     * 读取模板
     * @param type
     * @param isChangeContract
     * @return
     */
    public String readTemplate(String type, boolean isChangeContract){
        String filename = isChangeContract ? buildChangeTemplateFileName(type) : buildTemplateFileName(type);
        InputStream in = this.getClass().getClassLoader().getResourceAsStream(filename);

        StringBuffer sb = new StringBuffer();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));

            String line;

            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            return sb.toString();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        return null;
    }

    /**
     * 组装（系统模板）文件名
     * @param type
     * @return
     */
    private String buildTemplateFileName(String type){
        return type+".html";
    }

    /**
     * 组装（合同变更 系统模板）文件名
     * @param type
     * @return
     */
    private String buildChangeTemplateFileName(String type){
        return type+"ChangeContract.html";
    }

    /**
     * 获取模板内容
     * @param id
     * @return
     */
    private String fetchTemplateContent(Long id){
        if (id == null) return null;
        AccountContractTemplate act = queryAccountContractTemplate(id);
        if (act!=null && StringUtils.isNotBlank(act.getContent())) {
            if(StringUtils.isNotBlank(act.getPreContent())){
                return act.getContent()+"\r\n 预发布内容 : \r\n"+act.getPreContent();
            }
            return act.getContent();
        }

        return null;
    }

    /**
     * 填充（系统模板）内容
     * @param content
     * @param template
     * @param info 合同信息
     * @return
     */
    private String fillSysTemplate(String content, String template, ContractInfoDto info){

        template = template.replaceAll("\\r", "");
        template = template.replaceAll("\\n", "");
        template = template.replaceAll("\\t", "");

        template = template.replaceAll("#companyName#", info.getCompanyName());
        template = template.replace("#contractNo#", info.getContractNo());
        template = template.replace("<tbody id=\"items\"></tbody>", "<tbody id=\"items\"></tbody>");
        template = template.replace("#totalQuantity#", info.getTotalQuantity());
        template = template.replace("#totalWeight#", Tools.formatBigDecimal(info.getTotalWeight(), Constant.WEIGHT_PRECISION));
        template = template.replace("#totalAmount#", NumberTool.toThousandth(info.getTotalAmount()));
        template = template.replace("#totalCapital#", NumberToCNUtils.number2CNMontrayUnit(info.getTotalAmount())+"（ ￥ "+NumberTool.toThousandth(info.getTotalAmount())+" 元）");
        template = template.replace("#total#", Tools.formatBigDecimal(info.getTotalAmount()));
        template = template.replace("#fax#", info.getFax() == null ? "" : info.getFax());
        template = template.replace("#addr#", info.getAddr() != null ? info.getAddr() : "");
        template = template.replace("#legalPersonName#", info.getLegalPersonName() == null ? "" : info.getLegalPersonName());
        template = template.replace("#deliveryType#", info.getDeliveryType());
        template = template.replace("#feeTaker#", info.getFeeTaker().equals("seller") ? "乙方" : "甲方");
        template = template.replace("#payTypeFirst#", info.getPayTypeFirst());
        template = template.replace("#payTypeSecond#", info.getPayTypeSecond());
        template = template.replace("#payTypeThird#", info.getPayTypeThird());
        template = template.replace("#payTypeFourth#", info.getPayTypeFourth());
        template = template.replace("#outboundTaker#", info.getOutboundTaker());
        template = template.replace("#contractAddress#", (StringUtils.isBlank(info.getContractAddress())?"":info.getContractAddress()));
        template = template.replace("#year#", info.getYear());
        template = template.replace("#month#", info.getMonth());
        template = template.replace("#date#", info.getDate());
        template = template.replace("#orgName#", (StringUtils.isNotBlank(info.getOrgName())?"（"+info.getOrgName()+"）":""));
        template = template.replace("#orgAddr#", StringUtils.isEmpty(info.getOrgAddr()) ? "" : info.getOrgAddr());
        template = template.replace("#orgFax#", StringUtils.isEmpty(info.getOrgFax()) ? "" : info.getOrgFax());
        template = template.replace("#clause#", "<textarea class=\"clause\">"+(StringUtils.isBlank(content)?"":content)+"</textarea>");
        template = template.replace("#delayDays#", info.getDelayDays());
        template = template.replace("#transArea#", "货物所在仓库");
        template = template.replace("#deliveryYear#", "  ");
        template = template.replace("#deliveryMonth#", "  ");
        template = template.replace("#deliveryDate#", "  ");

        return template;
    }

    private ContractInfoDto fakeContractInfo(){
        ContractInfoDto info = new ContractInfoDto();

        info.setCompanyName("长沙中天钢铁贸易有限公司");
        info.setContractNo("XS-CPB-000005-1502-0001");
        info.setContractAddress("湖南省长沙市");
        info.setTotalQuantity("10");
        info.setTotalWeight(new BigDecimal("100000"));
        info.setTotalAmount(new BigDecimal("100000"));
        info.setTotalCapital(NumberToCNUtils.number2CNMontrayUnit(info.getTotalAmount()));
        info.setPayTypeFirst("display:none");
        info.setPayTypeSecond("");
        info.setPayTypeThird("");
        info.setPayTypeFourth("");
        info.setDeliveryType("自提");
        info.setOutboundTaker("甲方");
        info.setFeeTaker("乙方");
        info.setAddr("");
        info.setOrgAddr("");
        info.setOrgName("");
        info.setFax("");
        info.setOrgFax("0571-89718790");
        info.setLegalPersonName("");
        info.setDelayDays("5");

        Calendar cal = Calendar.getInstance();

        info.setYear(cal.get(Calendar.YEAR)+"");
        info.setMonth(cal.get(Calendar.MONTH)+"");
        info.setDate(cal.get(Calendar.DAY_OF_MONTH)+"");

        return info;
    }

    private ContractInfoDto restContractInfo(Long accountId, ContractInfoDto info){
        Account account = accountDao.selectByPrimaryKey(accountId);
        info.setCompanyName(account.getName());
        Organization org=organizationDao.queryById(account.getOrgId());

        if (org !=null && !StringUtils.isBlank(org.getAddress())){
            info.setContractAddress(org.getAddress());
        }
        return info;
    }

    /**
     * 根据公司Id获取公司部门下的合同
     * @param accountId
     * @return
     */
    @Override
    @Transactional
    public Map<String, List<AccountContractTemplate>> selectAllContractTemplateById(Long accountId) {
        if(accountId == null){
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "传入的参数不正确");
        }
        List<AccountContractTemplate> accountContractTemplateList = accountContractTemplateDao.selectContractTemplateById(accountId);
        Map<String, List<AccountContractTemplate>> accountContractTemplates = accountContractTemplateList.stream().collect(Collectors.groupingBy(AccountContractTemplate::getType));
        return accountContractTemplates;
    }

    /**
     * 设置默认合同模板
     * @param contractTemplateId
     * @param contractTemplateType
     * @param accountId
     */
    @Override
    @Transactional
    public void setDefaultContractTemplate(Long contractTemplateId,String contractTemplateType, Long accountId) {
        if(contractTemplateId != null && contractTemplateType!=null && contractTemplateType!="" && accountId != null){
            accountContractTemplateDao.disableOtherByAccountIdAndType(accountId, contractTemplateType);
            accountContractTemplateDao.setDefaultById(contractTemplateId);
        }else{
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "传入的参数不正确");
        }
    }

    /**
     * 模版处理操作
     * @param actOprt  对账户合同模板操作时所需要的参数对象
     * @return
     */
    @Override
    public String doTemplateOprt(AccountContractTemplateOprt actOprt) {
        if(actOprt==null){
            return null;
        }
        if(actOprt.getCompanyId() ==null){
            throw new BusinessException(Constant.ERROR_MISSING_REQUIRED_PARAM, "公司id不为空");
        }
        if(actOprt.getAccountId() ==null){
            throw new BusinessException(Constant.ERROR_MISSING_REQUIRED_PARAM, "账户id为空");
        }
        if(StringUtils.isBlank(actOprt.getType())){
            throw new BusinessException(Constant.ERROR_MISSING_REQUIRED_PARAM, "模版类型为空");
        }
        if(StringUtils.isBlank(actOprt.getAction())){
            throw new BusinessException(Constant.ERROR_MISSING_REQUIRED_PARAM, "模版处理动作为空");
        }
        if(!StringUtils.isBlank(actOprt.getAction()) && !"add".equals(actOprt.getAction()) && actOprt.getId()==null){
            throw new BusinessException(Constant.ERROR_MISSING_REQUIRED_PARAM, "合同模板不存在");
        }

        switch(actOprt.getAction()){
            case "add":
                return resolveTemplate(null,actOprt.getType(),actOprt.getCompanyId());
            default:
                return resolveTemplate(actOprt.getId(),actOprt.getType(),actOprt.getCompanyId());
        }
    }

    /**
     * 发布系统默认合同模板
     * @param id
     * @param type
     */
    @Override
    public Boolean releaseTemplate(Long id, String type) {
        //更新的系统模板为 卖家代运营协议 或 买家年度采购协议，则将打印过系统默认协议的客户 的打印条码置空
        if(AccountContractTemplateType.YEAR_PURCHASE.getCode().equals(type) || AccountContractTemplateType.CONSIGN.getCode().equals(type)){
            accountExtDao.updateAccountExtByType(type);
        }
        return accountContractTemplateDao.releaseTemplate(id) > 0;
    }


    /**
     * 不发布修改的系统默认合同模板
     * @param id
     */
    @Override
    public Boolean doNotReleaseTemplate(Long id) {
        return accountContractTemplateDao.doNotReleaseTemplate(id) > 0;
    }


    /**
     * 获取年度采购协议信息
     * @return
     */
    @Override
    public  AgreementTemplateDto getYearPurchaseAgreementTemplateInfo(AccountContractTemplate param,Long currentCompanyId){
        AgreementTemplateDto info = new AgreementTemplateDto();

        //获取模板信息
        AccountContractTemplate template = getYearPurchaseTemplate(param);
        info.setContent(template.getContent());
        info.setTemplateId(template.getId());

        //设置当前客户id
        info.setCurrentCompanyId(currentCompanyId);

        //系统默认的公司名称和合同编号
        info.setCompanyName(Constant.SYS_TEMPLATE_COMPANY_NAME);
        info.setContractNo(Constant.SYS_TEMPLATE_CONTRACT_NO);

        //指定了客户ID 查询公司名称
        if (currentCompanyId != null && currentCompanyId.intValue() > 0) {
            Account account = accountDao.selectByPrimaryKey(currentCompanyId);
            if (account != null){
                info.setCompanyName(account.getName());
            }
        }
        return info;
    }

    /**
     * 获取年度采购协议
     * @param  param 查询模板的条件参数
     * @return
     */
    @Override
    public AccountContractTemplate getYearPurchaseTemplate(AccountContractTemplate param) {
        AccountContractTemplate oldTemplate = queryYearPurchaseTemplateByAccountId(param.getAccountId());
        if(null == oldTemplate){
            param.setAccountId(0l);
            param.setType(AccountContractTemplateType.YEAR_PURCHASE.getCode());
            return queryOneAccountContractTemplate(param);//获取模板年度采购协议
        }else{
            return oldTemplate;
        }

    }

    /**
     * 获取当前客户信息
     * @param  currentCompanyId 当前公司id
     * @return
     */
    @Override
    public ContractInfoDto getYearPurchaseContractInfoDto(Long currentCompanyId){
        ContractInfoDto info = new ContractInfoDto();
        info.setCompanyName("长沙中天钢铁贸易有限公司");
        info.setContractAddress("湖南省长沙市");
        Calendar cal = Calendar.getInstance();
        info.setYear(cal.get(Calendar.YEAR)+"");
        info.setMonth((cal.get(Calendar.MONTH) + 1) +"");
        info.setDate(cal.get(Calendar.DAY_OF_MONTH) + "");


        //指定了客户ID
        if (currentCompanyId != null && currentCompanyId.intValue() > 0) {
            Account account = accountDao.selectByPrimaryKey(currentCompanyId);
            info.setCompanyName(account.getName());
        }
        return info;
    }


    /**
     * 保存年度采购协议
     * @param act
     * @param user
     * @return
     */
    @Override
    public void saveYearPurchaseTemplate(AccountContractTemplate act, User user) {
        AccountContractTemplate oldTemplate = queryYearPurchaseTemplateByAccountId(act.getAccountId());
        act.setLastUpdatedBy(user.getLoginId());
        act.setName(TEMPLATE_NAME_YEAR_PURCHASE);
        act.setType(AccountContractTemplateType.YEAR_PURCHASE.getCode());
        int result;
        if(null == oldTemplate){
            act.setCreatedBy(user.getLoginId());
            result = addSysContractTemplate(act);
        }else{
            act.setId(oldTemplate.getId());
            result = updateSysContractTemplate(act);
        }
        if(result == 0){
            throw  new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,"提交失败");
        }

        //更新买家年度采购协议状态
        AccountExt accountExt = new AccountExt();
        accountExt.setCustAccountId(act.getAccountId());
        accountExt.setAnnualPurchaseBarCode("");//更新条码为空 条码失效
        //协议状态待审核
        accountExt.setAnnualPurchaseAgreementStatus(AnnualPurchaseAgreementStatus.Requested.getCode());
        updateAccountExtByAccountId(accountExt, user);
    }


    /**
     * 恢复 年度采购协议 到系统默认模板，协议状态更新为待打印
     * @param act
     * @param user
     * @return
     */
    @Override
    public void recoverYearPurchaseTemplate(AccountContractTemplate act, User user) {
        AccountContractTemplate oldTemplate = queryYearPurchaseTemplateByAccountId(act.getAccountId());
        oldTemplate.setContent(act.getContent());
        oldTemplate.setLastUpdatedBy(user.getLoginId());
        if(0 == updateSysContractTemplate(oldTemplate)){
            throw  new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,"恢复 年度采购协议 到系统默认的模板失败");
        }

        //更新买家年度采购协议状态
        AccountExt accountExt = new AccountExt();
        accountExt.setCustAccountId(oldTemplate.getAccountId());
        accountExt.setAnnualPurchaseBarCode("");//更新条码为空 条码失效
        //协议状态待打印
        accountExt.setAnnualPurchaseAgreementStatus(AnnualPurchaseAgreementStatus.ToPrint.getCode());
        updateAccountExtByAccountId(accountExt, user);
    }

    /**
     * 查询客户的年度采购协议
     * @param accountId
     * @return
     */
    private AccountContractTemplate queryYearPurchaseTemplateByAccountId(Long accountId){
        AccountContractTemplate queryAct = new AccountContractTemplate();
        queryAct.setAccountId(accountId);
        queryAct.setType(AccountContractTemplateType.YEAR_PURCHASE.getCode());
        return queryOneAccountContractTemplate(queryAct);
    }

    /**
     * 查询客户的代运营协议
     * @param accountId
     * @return
     */
    private AccountContractTemplate queryConsignTemplateByAccountId(Long accountId){
        AccountContractTemplate queryAct = new AccountContractTemplate();
        queryAct.setAccountId(accountId);
        queryAct.setType(AccountContractTemplateType.CONSIGN.getCode());
        return queryOneAccountContractTemplate(queryAct);
    }

    /**
     * 获取代运营协议信息
     * @return
     */
    @Override
    public  AgreementTemplateDto getConsignAgreementTemplateInfo(AccountContractTemplate param,Long currentCompanyId){
        AgreementTemplateDto info = new AgreementTemplateDto();

        //获取模板信息
        AccountContractTemplate template = queryOneAccountContractTemplate(param);

        if (template != null) {
            //获取代运营协议模板内容(该模板的内容是html)
            String content = template.getContent();
            if (content != null){
                //去掉特殊符号
                content = content.replaceAll("\\r", "");
                content = content.replaceAll("\\n", "");
                content = content.replaceAll("\\t", "");
                info.setContent(content);
                info.setTemplateId(template.getId());
            }
        }

        //设置当前客户id
        info.setCurrentCompanyId(currentCompanyId);

        //系统默认的公司名称和合同编号
        info.setCompanyName(Constant.SYS_TEMPLATE_COMPANY_NAME);
        info.setContractNo(Constant.SYS_TEMPLATE_CONTRACT_NO);

        //指定了客户ID 查询公司名称
        if (currentCompanyId != null && currentCompanyId.intValue() > 0) {
            Account account = accountDao.selectByPrimaryKey(currentCompanyId);
            if (account != null){
                info.setCompanyName(account.getName());
            }
        }
        return info;
    }

    /**
     * 保存公司自定义卖家代运营协议模板（保存失败抛出异常）
     * 1、添加模板 2、更新公司代运营协议状态为“待审核”
     * @param saveInfo
     * @param user
     */
    @Override
    @Transactional
    public void saveConsignTemplate(AccountContractTemplate saveInfo, User user) {

        //存在自定义的公司模板则修改，否则添加
        saveInfo.setType(AccountContractTemplateType.CONSIGN.getCode());
        AccountContractTemplate dbTempate = queryOneAccountContractTemplate(saveInfo);
        if (dbTempate != null){
            saveInfo.setId(dbTempate.getId());
            saveInfo.setModificationNumber(dbTempate.getModificationNumber() + 1);
            updateConsignTemplateById(saveInfo,user);
        }else {
            //添加公司自定义代运营协议模板
            addConsignTemplate(saveInfo, user);
        }

        //更新公司代运营协议状态
        AccountExt accountExt = new AccountExt();
        accountExt.setCustAccountId(saveInfo.getAccountId());
        accountExt.setSellerConsignBarCode("");//更新条码为空 条码失效
        //协议状态待审核
        accountExt.setSellerConsignAgreementStatus(SellerConsignAgreementStatus.Requested.getCode());
        updateAccountExtByAccountId(accountExt, user);
    }

    /**
     * 恢复 代运营协议 到系统默认模板，协议状态更新为待打印
     * @param act
     * @param user
     * @return
     */
    @Override
    public void recoverConsignTemplate(AccountContractTemplate act, User user) {
        AccountContractTemplate oldTemplate = queryConsignTemplateByAccountId(act.getAccountId());
        oldTemplate.setContent(act.getContent());
        oldTemplate.setLastUpdatedBy(user.getLoginId());
        updateConsignTemplateById(oldTemplate,user);

        //更新公司代运营协议状态
        AccountExt accountExt = new AccountExt();
        accountExt.setCustAccountId(oldTemplate.getAccountId());
        accountExt.setSellerConsignBarCode("");//更新条码为空 条码失效
        //协议状态代运营
        accountExt.setSellerConsignAgreementStatus(SellerConsignAgreementStatus.ToPrint.getCode());
        updateAccountExtByAccountId(accountExt, user);
    }


    private void addConsignTemplate(AccountContractTemplate saveInfo, User user){
        saveInfo.setName(TEMPLATE_NAME_CONSIGN);
        saveInfo.setStatus(ContractTemplateStatus.APPROVED.getValue());//审核通过
        saveInfo.setSysTemplateStatus(1);//已发布
        saveInfo.setEnabled(0);//不启用
        saveInfo.setCreatedBy(user.getLoginId());
        saveInfo.setCreated(new Date());
        saveInfo.setLastUpdatedBy(user.getLoginId());
        saveInfo.setLastUpdated(new Date());
        saveInfo.setModificationNumber(0);

        if(accountContractTemplateDao.insertSelective(saveInfo) == 0){
            throw  new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,"提交失败");
        }
    }

    private void updateAccountExtByAccountId(AccountExt accountExt,User user){
        AccountExt dbAccountExt = accountExtDao.selectByAccountId(accountExt.getCustAccountId());
        if (dbAccountExt == null) throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,"公司不存在");
        accountExt.setLastUpdatedBy(user.getLoginId());
        accountExt.setLastUpdated(new Date());
        accountExt.setModificationNumber(dbAccountExt.getModificationNumber() + 1);
        if(accountExtDao.updateByAccountIdSelective(accountExt) == 0){
            throw  new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,"公司更新失败");
        }
    }

    @Override
    public void updateTemplateBarCodeHisitoryByBarcode(String barCode, User user){
        AccountContractTemplateBarcodeHistory update = accountContractTemplateBarcodeHistoryDao.queryRecordByBarcode(barCode);
        update.setLastUpdatedBy(user.getLoginId());
        update.setLastUpdated(new Date());
        update.setPrintTimes(update.getPrintTimes() + 1);
        update.setLastPrintDate(new Date());
        update.setModificationNumber(update.getModificationNumber() + 1);
        if(accountContractTemplateBarcodeHistoryDao.updateByPrimaryKeySelective(update) == 0){
            throw  new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,"打印信息更新失败");
        }
    }


    private void updateConsignTemplateById(AccountContractTemplate saveInfo, User user){
        AccountContractTemplate update = new AccountContractTemplate();
        update.setContent(saveInfo.getContent());
        update.setId(saveInfo.getId());
        update.setLastUpdatedBy(user.getLoginId());
        update.setLastUpdated(new Date());
        update.setModificationNumber(saveInfo.getModificationNumber());
        if(accountContractTemplateDao.updateByPrimaryKeySelective(update) == 0){
            throw  new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,"提交失败");
        }
    }

    /**
     * 生成新的条形码，用于合同和协议模板 条形码中间没任何空格
     * 新的条形码生成规则：类型：90 + 6位年月日 + 5位流水号 流水号按类型按天递增
     * 例如：2016年03月03日生成的第5个代运营协议号，后台编号为9016030300005
     * @param barCodeType 条形码类型
     * @param templateType 模板类型
     * xiyan
     */
    private  synchronized String generateBarCodeByBarCodeType(String barCodeType,String templateType) {
        Date date = new Date();

        //得到最后的一条记录，并解析条形码，如果是当天，则在原有的基本上加+1
        //如果不是当天，则重新生成。
        AccountContractTemplateBarcodeHistory lastRecord  = accountContractTemplateBarcodeHistoryDao.queryLastRecordByType(templateType);
        List<Object> lastBarCodeChips = null;
        if (lastRecord != null){
            lastBarCodeChips = parseBarCode(lastRecord.getBarCode(),barCodeType);
        }

        int number = 1;
        String currDateStr = Tools.dateToStr(date, "yyMMdd");
        if(lastBarCodeChips != null){
            //如果时间相等，则在原有的流水号上面+1,
            if(currDateStr.equals(lastBarCodeChips.get(1))){
                number = ((int) lastBarCodeChips.get(2)) + 1;
            }

        }

        //新的code
        String newCode = barCodeType + currDateStr + String.format("%05d", number);
        return newCode;
    }


    /**
     * 解析条形码并返回一个数组
     * 条形码格式：2位类型 + 6位年月日 + 5位流水号 流水号按类型按天递增
     * @param lastBarCode
     * @return List<Object> 为三条：
     * 		index=0 : String	2位类型
     * 		index=1 : String	6位年月日yyMMdd
     * 		index=2 : Integer	5位流水号
     */
    private List<Object> parseBarCode(String lastBarCode,String type) {
        if(lastBarCode==null || lastBarCode.length()<13)
            return null;

        List<Object> chips =  new ArrayList<Object>();

        //日期 从第3位开始截取到第8位
        String dateStr = lastBarCode.substring(2,8);

        //流水号 从第9位开始截取到最后
        int number = Integer.parseInt(lastBarCode.substring(8));

        chips.add(0, type);
        chips.add(1, dateStr);
        chips.add(2, number);

        return chips;
    }


    /**
     * 更新代运营协议条码并保存条码历史记录
     *
     * @param currentCompanyId
     * @param user
     * @return
     */
    @Override
    @Transactional
    public String updateConsignBarCode(Long currentCompanyId, User user) {
        //根据条码类型生成相应的条码 根据模板的类型和日期来累加流水号
        String newCode = generateBarCodeByBarCodeType(AccountContractTemplateBarCodeType.CONSIGN.getCode(),AccountContractTemplateType.CONSIGN.getCode());

        //更新当前最新条码
        AccountExt accountExt = new AccountExt();
        accountExt.setCustAccountId(currentCompanyId);
        accountExt.setSellerConsignBarCode(newCode);
        updateAccountExtByAccountId(accountExt, user);

        //插入条码历史记录
        addBarCodeHistory(AccountContractTemplateType.CONSIGN.getCode(), currentCompanyId, newCode, user);

        return newCode;
    }

    /**
     * 更新年度采购协议条码并保存条码历史记录
     *
     * @param currentCompanyId
     * @param user
     * @return
     */
    @Override
    @Transactional
    public String updateYearPurchaseBarCode(Long currentCompanyId, User user) {
        //根据条码类型生成相应的条码 根据模板的类型和日期来累加流水号
        String newCode = generateBarCodeByBarCodeType(AccountContractTemplateBarCodeType.YEAR_PURCHASE.getCode(),AccountContractTemplateType.YEAR_PURCHASE.getCode());

        //更新当前最新条码
        AccountExt accountExt = new AccountExt();
        accountExt.setCustAccountId(currentCompanyId);
        accountExt.setAnnualPurchaseBarCode(newCode);
        updateAccountExtByAccountId(accountExt, user);

        //插入条码历史记录
        addBarCodeHistory(AccountContractTemplateType.YEAR_PURCHASE.getCode(), currentCompanyId, newCode, user);

        return newCode;
    }


    /*
     * 插入条码历史记录
     * @param type 类型
     * @param accountId 客户id
     * @param barCode 条码
     * @param user 用户
     */
    private void addBarCodeHistory(String type,Long accountId,String barCode, User user){
        AccountContractTemplateBarcodeHistory history = new AccountContractTemplateBarcodeHistory();
        history.setType(type);
        history.setAccountId(accountId);
        history.setBarCode(barCode);
        history.setCreated(new Date());
        history.setCreatedBy(user.getLoginId());
        history.setLastUpdated(new Date());
        history.setLastUpdatedBy(user.getLoginId());
        history.setModificationNumber(0);

        if(accountContractTemplateBarcodeHistoryDao.insertSelective(history) == 0){
            throw  new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,"条码历史记录插入失败");
        }
    }

    /**
     * 根据条码查询条码历史记录
     *
     * @param barCode
     * @return
     */
    @Override
    public AccountContractTemplateBarcodeHistory queryBarCodeHistoryByBarcode(String barCode) {
        return accountContractTemplateBarcodeHistoryDao.queryRecordByBarcode(barCode);
    }


    /**
     * 查询公司待审核合同模板列表
     * @param query
     * @return
     */
    @Override
    public List<ContractTemplateDto> queryContractTemplateByStatus(CompanyQuery query){
        return accountContractTemplateDao.queryContractTemplateByStatus(query);
    }

    /**
     * 统计公司待审核合同模板列表
     * @param query
     * @return
     */
    @Override
    public int queryTotalContractTemplateByStatus(CompanyQuery query){
        return accountContractTemplateDao.queryTotalContractTemplateByStatus(query);
    }


    /**
     * 更新合同模板状态
     * @param id
     * @param status
     * @param lastUpdateBy
     * @param disagreeDesc
     * @return
     */
    @Override
    public int updateContractTemplateStatus(Long id, String status,String lastUpdateBy, String disagreeDesc){
        return accountContractTemplateDao.updateContractTemplateStatus(id, status, lastUpdateBy, disagreeDesc);
    }
}
