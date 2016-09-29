package com.prcsteel.platform.account.web.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.prcsteel.platform.common.aspect.OpAction;
import org.prcsteel.rest.sdk.activiti.service.ProcessService;
import org.prcsteel.rest.sdk.activiti.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.prcsteel.platform.account.model.dto.AccountDto;
import com.prcsteel.platform.account.model.dto.AccountDtoForUpdate;
import com.prcsteel.platform.account.model.dto.OrderRelationDto;
import com.prcsteel.platform.account.model.dto.OrderStatusDto;
import com.prcsteel.platform.account.model.dto.SaveAccountDto;
import com.prcsteel.platform.account.model.dto.SellerTradeDto;
import com.prcsteel.platform.account.model.enums.AccountBankDataStatus;
import com.prcsteel.platform.account.model.enums.AccountBusinessType;
import com.prcsteel.platform.account.model.enums.AccountTabId;
import com.prcsteel.platform.account.model.enums.AccountTag;
import com.prcsteel.platform.account.model.enums.AccountTagForSearch;
import com.prcsteel.platform.account.model.enums.AccountTransApplyType;
import com.prcsteel.platform.account.model.enums.AnnualPurchaseAgreementStatus;
import com.prcsteel.platform.account.model.enums.CardInfoStatus;
import com.prcsteel.platform.account.model.enums.ConsignOrderStatus;
import com.prcsteel.platform.account.model.enums.ConsignOrderStatusForBuyer;
import com.prcsteel.platform.account.model.enums.ConsignOrderStatusForSeller;
import com.prcsteel.platform.account.model.enums.InvoiceDataStatus;
import com.prcsteel.platform.account.model.enums.SellerConsignAgreementStatus;
import com.prcsteel.platform.account.model.model.AccountAttachment;
import com.prcsteel.platform.account.model.model.AccountBank;
import com.prcsteel.platform.account.model.model.AccountExt;
import com.prcsteel.platform.account.model.query.CompanyQuery;
import com.prcsteel.platform.account.model.query.OrderRelationQuery;
import com.prcsteel.platform.account.model.query.SellerTradeQuery;
import com.prcsteel.platform.account.service.AccountAttachmentService;
import com.prcsteel.platform.account.service.AccountBankService;
import com.prcsteel.platform.account.service.AccountDocumentService;
import com.prcsteel.platform.account.service.AccountService;
import com.prcsteel.platform.account.service.AccountTransLogService;
import com.prcsteel.platform.account.service.ContactService;
import com.prcsteel.platform.account.service.OrderRelationService;
import com.prcsteel.platform.account.service.SellerTradeService;
import com.prcsteel.platform.account.service.WorkFlowService;
import com.prcsteel.platform.account.web.support.ShiroVelocity;
import com.prcsteel.platform.acl.model.enums.RoleAuthType;
import com.prcsteel.platform.acl.model.model.Organization;
import com.prcsteel.platform.acl.model.model.Role;
import com.prcsteel.platform.acl.model.model.SysSetting;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.acl.service.OrganizationService;
import com.prcsteel.platform.acl.service.SysSettingService;
import com.prcsteel.platform.common.enums.OpType;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.common.service.OpLog;
import com.prcsteel.platform.common.service.OpParam;
import com.prcsteel.platform.common.utils.Tools;
import com.prcsteel.platform.common.vo.PageResult;
import com.prcsteel.platform.common.vo.Result;
import com.prcsteel.platform.order.model.enums.ConsignOrderPayStatus;
/**
 * Created by lichaowei on 2016/2/26.
 */

@Controller
@RequestMapping("/company/")
public class CompanyController extends BaseController {
    @Resource
    ContactService contactService;
    @Resource
    AccountService accountService;
    @Resource
    AccountTransLogService accountTransLogService;
    @Resource
    OrganizationService organizationService;
    @Resource
    AccountBankService accountBankService;
    @Resource
    AccountAttachmentService attachmentService;
    @Resource
    SellerTradeService sellerTradeService;
    @Resource
    OrderRelationService orderRelationService;
    @Resource
    TaskService taskService;
	@Resource
	ProcessService processService;

 //迁移风控代码，增加5个字段 add by zhoucai@prcsteel.com 2016-5-6
    @Resource
    SysSettingService sysSettingService;
    
    @Resource
    AccountDocumentService accountDocumentService;

    @Resource
    WorkFlowService workFlowService;

    ShiroVelocity permissionLimit = new ShiroVelocity();
    final String PERMISSION_QUALIFICATE_EDIT = "account:qualificate:edit"; //客户资质权限
    @Value("${order.domain}")
    private String orderDomain;

    private static final Logger logger = LoggerFactory.getLogger(CompanyController.class);

    /**
     * 企业基本信息-证件资料
     *
     * @param out
     * @param accountId
     * @return
     */
    @RequestMapping("{accountId}/credentialsinfo")
    public String credentialsInfo(ModelMap out, @PathVariable("accountId") Long accountId) {
        AccountDto accountdto = contactService.getCompanyById(accountId);
        String dbPurchaseStatus = accountdto.getAccountExt().getAnnualPurchaseAgreementStatus();
        if (dbPurchaseStatus != null) {
            // 当状态为 一审不通过，二审不通过 的时候，界面统一显示 审核不通过
            if (AnnualPurchaseAgreementStatus.FirstDeclined.getCode().equals(dbPurchaseStatus)
                    || AnnualPurchaseAgreementStatus.SecondDeclined.getCode().equals(dbPurchaseStatus)) {
                dbPurchaseStatus = AnnualPurchaseAgreementStatus.Declined.getCode();
            }
            String purchaseStatus = AnnualPurchaseAgreementStatus.getNameByCode(dbPurchaseStatus);
            out.put("purchaseStatus", purchaseStatus);
        }
        String dbConsignStatus = accountdto.getAccountExt().getSellerConsignAgreementStatus();
        if (dbConsignStatus != null) {
            // 当状态为 一审不通过，二审不通过 的时候，界面统一显示 审核不通过
            if (SellerConsignAgreementStatus.FirstDeclined.getCode().equals(dbConsignStatus)
                    || SellerConsignAgreementStatus.SecondDeclined.getCode().equals(dbConsignStatus)) {
                dbConsignStatus = SellerConsignAgreementStatus.Declined.getCode();
            }
            String consignStatus = SellerConsignAgreementStatus.getNameByCode(dbConsignStatus);
            out.put("consignStatus", consignStatus);
        }
        out.put("accountdto", accountdto);
        if ((accountdto.getAccount().getAccountTag() & AccountTag.buyer.getCode()) == AccountTag.buyer.getCode()) {
            out.put("isBuyer", true);
        }
        if ((accountdto.getAccount().getAccountTag() & AccountTag.seller.getCode()) == AccountTag.seller.getCode()) {
            out.put("isSeller", true);
        }
        out.put("tabId", AccountTabId.credentialsInfo);
        return "company/credentialsinfo";
    }

    /**
     * 企业基本信息-销售信息
     * auth：zhoucai@prcsteel。com
     * date：2016-3-23
     * @param out
     * @param accountId
     * @return
     */
    @RequestMapping("{accountId}/salesrecords")
    public String saleInfo(ModelMap out, @PathVariable("accountId") Long accountId) {
    	  AccountDto accountdto = contactService.getCompanyById(accountId);
          out.put("accountdto", accountdto);
          out.put("orderDomain",orderDomain);
		  out.put("accountApplyType", AccountTransApplyType.values());
		  Calendar cal = Calendar.getInstance(); 
		  cal.setTime(new Date()); 
		  cal.set(Calendar.DAY_OF_MONTH, 1); 
		  //默认时间从当月1号到当前时间
		  out.put("startTime",  new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime()));
		  out.put("endTime",  Tools.dateToStr(new Date(), "yyyy-MM-dd"));
        return "company/salesrecords";
    }

    /**
     * 企业基本信息-采购信息
     * auth：zhoucai@prcsteel。com
     * date：2016-3-30
     * @param out
     * @param accountId
     * @return
     */
    @RequestMapping("{accountId}/buyerrecords")
    public String buyerInfo(ModelMap out, @PathVariable("accountId") Long accountId) {
        AccountDto accountdto = contactService.getCompanyById(accountId);
        Role role = getUserRole();
        if (role.getParentId()==2||role.getParentId()==0){
            out.put("isAdmin",'1');
        }else{
            out.put("isAdmin",'0');
        }
        OrderRelationQuery query = new OrderRelationQuery();
        query.setCompanyId(accountdto.getAccount().getId());
        int isShow = orderRelationService.queryIsShow(query);
        out.put("isShow", isShow);
        out.put("accountdto", accountdto);
        out.put("orderDomain",orderDomain);
        out.put("accountApplyType", AccountTransApplyType.values());
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.DAY_OF_MONTH, 1);
        //默认时间从当月1号到当前时间
        out.put("startTime",  new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime()));
        out.put("endTime",  Tools.dateToStr(new Date(), "yyyy-MM-dd"));
        return "company/buyerrecords";
    }
    /**
     * 保存公司基本信息-证件资料
     *
     * @param saveAccount 账号信息
     * @return 成功返回true，是否返回false
     */
    @OpLog(OpType.SaveAccount)
    @OpParam(name = "saveAccount", value = "saveCredentials")
    @ResponseBody
    @RequestMapping(value = "savecredentials", method = RequestMethod.POST)
    public Result saveCredentials(SaveAccountDto saveAccount) {
        Result result = new Result();
        try {
            saveAccount.setUser(getLoginUser());
            accountService.saveCredentials(saveAccount);
            result.setData("保存成功");
        } catch (BusinessException be) {
            result.setSuccess(Boolean.FALSE);
            result.setData(be.getMsg());
        }
        return result;
    }

    /**
     * 保存协议上传的图片 chengui
     *
     * @param saveAccount 账号信息
     * @return 成功返回true，是否返回false
     */
    @OpLog(OpType.SaveAccount)
    @OpParam(name = "saveAccount", value = "saveAgreementUploadFiles")
    @ResponseBody
    @RequestMapping(value = "saveagreementuploadfiles", method = RequestMethod.POST)
    public Result saveAgreementUploadFiles(SaveAccountDto saveAccount) {
        Result result = new Result();
        try {
            accountService.saveAgreementUploadFiles(getLoginUser(), saveAccount);
            result.setData("保存成功");
        } catch (BusinessException be) {
            result.setSuccess(Boolean.FALSE);
            result.setData(be.getMsg());
        }
        return result;
    }

    /**
     * 修改卖家代运营协议状态
     *
     * @param accountExt 修改对象
     * @return 成功返回true，是否返回false
     */
    @OpLog(OpType.SaveAccount)
    @OpParam(name = "accountExt", value = "saveCredentials")
    @ResponseBody
    @RequestMapping(value = "updatesellerconsignstatus", method = RequestMethod.POST)
    public Result updateSellerConsignStatus(AccountExt accountExt) {
        Result result = new Result();
        Integer flag = accountService.updateByAccountIdSelective(accountExt, getLoginUser());
        if (flag > 0) {
            result.setData("修改成功");
        } else {
            result.setSuccess(Boolean.FALSE);
        }
        return result;
    }

    /**
     * 企业基本信息-修改
     *
     * @param out
     * @param accountId
     * @return
     */
    @RequestMapping("{accountId}/edit")
    public String edit(ModelMap out, @PathVariable("accountId") Long accountId) {
        AccountDto accountdto = contactService.getCompanyById(accountId);
        User user = getLoginUser();
        Role role = getUserRole();
        List<Organization> orgList;
        int totalTransLog = -1;
        if (RoleAuthType.ALL.getValue() == role.getType()) {
            orgList = organizationService.queryAllBusinessOrg();
            // 查询交易资金流水记录，没有可以修改
            totalTransLog = accountTransLogService.totalTransLog(accountId, "", "", "", "");
        }
        else {

            orgList = new ArrayList<>();
            orgList.add(organizationService.queryById(user.getOrgId()));
        }
       //迁移风控代码，增加5个字段 add by zhoucai@prcsteel.com 2016-5-6
        List<SysSetting> customerLabel= sysSettingService.queryCustomerLabel();
        out.put("sellerLabel", customerLabel.size() == 0 ? null : customerLabel.get(0).getSettingValue().split("、"));
        out.put("buyerLabel", customerLabel.size()==0?null:customerLabel.get(1).getSettingValue().split("、"));
        out.put("defaultSellerLabel", customerLabel.size() == 0 ? null : customerLabel.get(0).getDefaultValue());
        out.put("defaultBuyerLabel", customerLabel.size()==0?null:customerLabel.get(1).getDefaultValue());
        out.put("hasQualificate", permissionLimit.hasPermission(PERMISSION_QUALIFICATE_EDIT));
        out.put("user", user);
        out.put("orgList", orgList);
        out.put("businessTypes", AccountBusinessType.values());
        out.put("accountTag", AccountTag.values());
        out.put("accountdto", accountdto);
        out.put("totalTransLog", totalTransLog);

        return "company/edit";
    }

    /**
     * 保存账号基本数据
     *
     * @param saveAccount 账号信息
     * @return 成功返回true，是否返回false
     */
    @OpLog(OpType.CreateConsignOrder)
    @OpParam(name = "account", value = "EditAccount")
    @ResponseBody
    @RequestMapping(value = "saveedit", method = RequestMethod.POST)
    public Result saveEdit(SaveAccountDto saveAccount) {
        Result result = new Result();
        try {
            //2016-5-7移植风控上线代码，公司名称不能为空
            if(saveAccount.getAccount().getName()!=null)
                saveAccount.getAccount().setName(saveAccount.getAccount().getName().replaceAll(" ", ""));
            saveAccount.setUser(getLoginUser());
            accountService.saveEdit(saveAccount);
            result.setData("保存成功");
        } catch (BusinessException be) {
            result.setSuccess(Boolean.FALSE);
            result.setData(be.getMsg());
        }
        return result;
    }

    /**
     * 企业列表页面
     */
    @RequestMapping("list")
    public void list(ModelMap out) {
        List<Organization> list = organizationService.queryAllBusinessOrg();
        out.put("orgs", list);
        out.put("accountTags", AccountTagForSearch.getList());
    }


    /**
     * 批量锁定或解锁公司
     *
     * @param dto
     * @return
     */
    @RequestMapping("/lockAndUnlockCompany")
    @ResponseBody
    public Result lockAndUnlockCompany(AccountDtoForUpdate dto) {
        try {
            dto.setUser(getLoginUser());
            accountService.batchUpateStatusByPrimaryKeys(dto);
            return new Result("锁定/解锁成功", Boolean.TRUE);
        } catch (BusinessException e) {
            logger.error(e.getMsg(), e);
            return new Result(e.getMsg(), Boolean.FALSE);
        }
    }

    /**
     * 获取证件资料状态数据
     *
     * @return
     */
    @RequestMapping("/getCardInfoStatus")
    @ResponseBody
    public Result getCardInfoStatus() {
        return new Result(CardInfoStatus.getList(), Boolean.TRUE);
    }


    /**
     * 获取开票资料状态数据
     *
     * @return
     */
    @RequestMapping("/getInvoiceDataStatus")
    @ResponseBody
    public Result getInvoiceDataStatus() {
        return new Result(InvoiceDataStatus.getList(), Boolean.TRUE);
    }


    /**
     * 获取打款资料状态数据
     *
     * @return
     */
    @RequestMapping("/getAccountBankDataStatus")
    @ResponseBody
    public Result getAccountBankDataStatus() {
        return new Result(AccountBankDataStatus.getList(), Boolean.TRUE);
    }

    /**
     * 获取年度采购协议状态数据
     *
     * @return
     */
    @RequestMapping("/getAnnualPurchaseAgreementStatus")
    @ResponseBody
    public Result getAnnualPurchaseAgreementStatus() {
        return new Result(AnnualPurchaseAgreementStatus.getList(), Boolean.TRUE);
    }

    /**
     * auth:zhoucai@prcsteel.com
     * date:2016-3-23
     * 获取卖家订单状态数据     *
     * @return Result
     */
    @RequestMapping("/getsellerconsignorderstatus")
    @ResponseBody
    public Result getSellerConsignOrderStatus() {
        return new Result(ConsignOrderStatusForSeller.getList(), Boolean.TRUE);
    }

    /**
     * auth:caochao@prcsteel.com
     * date:2016-4-29
     * 获取买家状态数据     *
     * @return Result
     */
    @RequestMapping("/getbuyerconsignorderstatus")
    @ResponseBody
    public Result getBuyerConsignOrderStatus() {
        return new Result(ConsignOrderStatusForBuyer.getList(), Boolean.TRUE);
    }
    
    /**
     * @auth:zhoucai@prcsteel.com
     * @date:2016-3-23
     * @descrition:获取销售记录流水信息     
     * @return Result
     */
    @RequestMapping("/loadsellertradelist")
    @ResponseBody
    public PageResult loadSellerTradeList(SellerTradeQuery query)
    {
         List <SellerTradeDto> list = new ArrayList<>();
         buildOrderStatus(query);
         int total = sellerTradeService.countSellerFlow(query);
         if(total>0){
             list=sellerTradeService.querySellerTradeListByDto(query);
         }
         return new PageResult(list.size(),total,list);
    }

    /**
     * @auth:zhoucai@prcsteel.com
     * @date:2016-3-30
     * @descrition:查询项目列表
     * @return Result
     */
    @RequestMapping("/queryallprojectlist")
    @ResponseBody
    public Result queryallProjectList(OrderRelationQuery query)
    {
        List <OrderRelationDto> list = new ArrayList<>();

        Role role = getUserRole();

        if (role.getParentId()==2) {
            query.setUserId(getLoginUser().getLoginId());
        }
        list=orderRelationService.queryAllProjectList(query);
        return new Result(list,true);
    }
    /**
     * @auth:zhoucai@prcsteel.com
     * @date:2016-3-30
     * @descrition:为订单增加项目依赖
     * @return Result
     */
    @RequestMapping("/changeorderproject")
    @ResponseBody
    public Result changeOrderProject(SellerTradeQuery query)
    {

        Result result = new Result();
        Integer flag = sellerTradeService.changeOrderProject(query);
        if (flag > 0) {
            result.setSuccess(Boolean.TRUE);
        } else {
            result.setSuccess(Boolean.FALSE);
        }
        return result;
    }
    /**
     * @auth:zhoucai@prcsteel.com
     * @date:2016-3-30
     * @descrition:编辑项目信息，并修改订单关联状态
     * @return Result
     */
    @OpAction( key = "companyId")
    @RequestMapping("/editorderproject")
    @ResponseBody
    public Result editOrderProject(OrderRelationQuery query,Long companyId)
    {

        Result result = new Result();
        SellerTradeQuery projectQry= new SellerTradeQuery();
        projectQry.setProjectId(query.getProjectId());
        projectQry.setProjectName(query.getProjectName());
        projectQry.setBeforeId(query.getBeforeId());

        try {
            query.setUserId(getLoginUser().getLoginId());
            if (query.getProjectId()==null){
                orderRelationService.editProjectInfo(query);
            }else{
                sellerTradeService.changeOrderProject(projectQry);
                orderRelationService.editProjectInfo(query);
            }

            result.setSuccess(Boolean.TRUE);
            return result;
        } catch (BusinessException e) {
            result.setSuccess(Boolean.FALSE);
            result.setData(e.getMsg());
            return result;
        }

    }

    /**
     * @auth:zhoucai@prcsteel.com
     * @date:2016-3-30
     * @descrition:删除项目信息，并删除订单关联状态
     * @return Result
     */
    @RequestMapping("/deleteorderproject")
    @ResponseBody
    public Result deleteProjectInfo(OrderRelationQuery query)
    {

        Result result = new Result();
        SellerTradeQuery projectQry= new SellerTradeQuery();
        projectQry.setProjectId(query.getProjectId());
        projectQry.setProjectName(query.getProjectName());
        projectQry.setBeforeId(query.getBeforeId());

        try {
            query.setUserId(getLoginUser().getLoginId());
            sellerTradeService.changeOrderProject(projectQry);
            orderRelationService.deleteProjectInfo(query);
            result.setSuccess(Boolean.TRUE);
            return result;
        } catch (BusinessException e) {
            result.setSuccess(Boolean.FALSE);
            result.setData(e.getMsg());
            return result;
        }

    }



    /**
     * @auth:zhoucai@prcsteel.com
     * @date:2016-3-23
     * @descrition:获取销售记录流水汇总信息     
     * @return Result
     */
    @RequestMapping("/countsellertradeflow")
    @ResponseBody
    public SellerTradeDto countSellerTradeFlow(
            @RequestParam("sellerId") String sellerId,
            @RequestParam("sellerName") String sellerName,
            @RequestParam("accountId") String accountId,
            @RequestParam("departId") String departId,
            @RequestParam("strStartTime") String strStartTime,
            @RequestParam("strEndTime") String strEndTime,
            @RequestParam("projectId") String projectId,
            @RequestParam("statusList") List<String> statusList,
            @RequestParam("accountName") String accountName

    )
    {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("sellerId", sellerId);
        paramMap.put("sellerName", sellerName);
        paramMap.put("accountId", accountId);
        paramMap.put("departId", departId);
        paramMap.put("strStartTime", strStartTime);
        paramMap.put("strEndTime", strEndTime);
        paramMap.put("statusList", statusList);
       paramMap.put("projectId", projectId);

        paramMap.put("accountName", accountName);

        SellerTradeDto list=sellerTradeService.countSellerTradeFlow(paramMap);
        return list;
    }
    /**
     * @description：采购记录-创建项目
     * @author： zhoucai@prcsteel.com
     * @date :2016-3-31
     * @return Result
     */
    @ResponseBody
    @RequestMapping("/creatproject")
    public Result creatProject(OrderRelationQuery query) {
        Result result = new Result();
        try {
            query.setUserName(getLoginUser().getName());
            query.setUserId(getLoginUser().getLoginId());
            orderRelationService.creatProject(query);
            result.setSuccess(Boolean.TRUE);
            return result;
        } catch (BusinessException e) {
            result.setSuccess(Boolean.FALSE);
            result.setData(e.getMsg());
            return result;
        }
    }
    /**
     * 获取卖家代运营协议状态数据
     *
     * @return
     */
    @RequestMapping("/getSellerConsignAgreementStatus")
    @ResponseBody
    public Result getSellerConsignAgreementStatus() {
        return new Result(SellerConsignAgreementStatus.getList(), Boolean.TRUE);
    }

    /**
     * 加载公司列表数据
     *
     * @return
     */
    @RequestMapping("/loadCompanyList")
    @ResponseBody
    public PageResult loadCompanyList(CompanyQuery query) {
        query.setUserIdList(this.getUserIds());
        int total = accountService.queryCompanyTotalByCompanyQuery(query);
        List list = new ArrayList<>();
        if (total > 0) {
             list = accountService.queryCompanyByCompanyQuery(query);
        }
        return new PageResult(list.size(),total,list);
    }

    @RequestMapping("imagesupload")
    public String imagesUpload(ModelMap out, Long accountId, String type,String fileUrl) {
        List<AccountAttachment> list = attachmentService.selectByAccountIdAndType(accountId, type);
        out.put("attachmentlist", list);
        out.put("fileUrl", fileUrl);
        out.put("accountId", accountId);
        return "company/imagesupload";
    }

    @ResponseBody
    @RequestMapping("deleteattachment")
    public Result deleteAttachment(Long id) {
        Integer flag = attachmentService.deleteByPrimaryKey(id);
        return new Result(flag > 0 ? "删除成功" : "删除失败", flag > 0);
    }

    /**
     * 企业基本信息-银行信息
     * @param out
     * @param accountId
     * @return
     */
    @RequestMapping("/{accountId}/bankinfo")
    public String bankInfo(ModelMap out, @PathVariable Long accountId) {
        AccountDto accountdto = contactService.getCompanyById(accountId);
        out.put("accountdto", accountdto);
        out.put("tabId", AccountTabId.bankInfo);
        return "company/bankinfo";
    }


    /**
     * 加载银行信息数据
     * @param accountId
     * @return
     */
    @RequestMapping(value = "/loadbankinfo")
    @ResponseBody
    public  Result loadBankInfo(@RequestParam("accountId") Long accountId) {
        Result result = new Result();
        List<AccountBank> list = accountBankService.queryByAccountId(accountId);
        result.setData(list);
        return result;
    }

    /**
     * 设置默认银行
     * @param bankId
     * @return
     */
    @RequestMapping(value = "/setdefaultbank")
    @ResponseBody
    public  Result setDefaultBank(@RequestParam("bankId") Long bankId,@RequestParam("accountId") Long accountId) {
        Result result = new Result();
        try{
            accountBankService.setDefaultBank(accountId, bankId, getLoginUser());
            result.setSuccess(Boolean.TRUE);
        }catch (BusinessException b){
            logger.error("错误提示信息：" + b.getMsg(),b);
            result.setSuccess(Boolean.FALSE);
            result.setData(b.getMsg());
        }
        return result;
    }


    /**
     * 保存银行信息银行
     * @param request
     * @param bank
     * @return
     */
    @RequestMapping(value = "/savebankinfo")
    @ResponseBody
    public Result saveBankInfo(MultipartHttpServletRequest request,AccountBank bank){
        Result result = new Result();
        MultipartFile image = request.getFile("image");
        try{
            accountBankService.saveBankInfo(image, bank, getLoginUser());
        }catch (BusinessException b){
            logger.error(b.getMsg(),b);
            result.setSuccess(Boolean.FALSE);
            result.setData(b.getMsg());
        }
        return result;
    }


    /**
     * 获取银行信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}/getbankinfo")
    @ResponseBody
    public Result getBankInfo(@PathVariable Long id){
        Result result = new Result();
        try{
            AccountBank bank = accountBankService.selectByPrimaryKey(id);
            result.setData(bank);
            result.setSuccess(Boolean.TRUE);
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            result.setSuccess(Boolean.FALSE);
        }
        return result;
    }


    /**
     * 逻辑删除银行信息
     * @param id
     * @return
     */
    @RequestMapping(value = "{accountId}/{id}/deletelogicallybankinfo")
    @ResponseBody
    public Result deleteLogicallyBankInfo(@PathVariable Long accountId,@PathVariable Long id){
        Result result = new Result();
        try{
            accountBankService.updateDeletedById(id,accountId,getLoginUser());
            result.setSuccess(Boolean.TRUE);
        }catch (BusinessException e){
            logger.error(e.getMsg(),e);
            result.setSuccess(Boolean.FALSE);
        }
        return result;
    }

    /**
     * 客户资料页面
     */
    @RequestMapping("tocustinfolist")
    public void toCustInfoList(ModelMap out) {
        List<Organization> list = organizationService.queryAllBusinessOrg();
        out.put("orgs", list);
        int permission = 0;
       	ShiroVelocity permissionLimit = new ShiroVelocity();
       	if(permissionLimit.hasPermission("account:audit:cancel")){
       		permission = 1;
       	}
        out.put("hasPermission", permission);
    }

    /**
     * 加载客户资料列表数据
     */
    @RequestMapping("loadcustinfolist")
    @ResponseBody
    public PageResult loadCustInfoList(CompanyQuery query) {
        query.setUserIdList(this.getUserIds());
        int total = accountService.queryCompanyTotalByCompanyQuery(query);
        List list = new ArrayList<>();
        if (total > 0) {
            list = accountService.queryCompanyByCompanyQuery(query);
        }
        return new PageResult(list.size(),total,list);
    }

    private void buildOrderStatus(SellerTradeQuery query) {
        List<String> statusList=query.getStatusList();
        if(statusList==null){
            return;
        }
        List<OrderStatusDto> orderStatusList=new ArrayList<>();
        statusList.forEach(status -> {
            OrderStatusDto statusItem=new OrderStatusDto();
            if (ConsignOrderStatusForSeller.NEW.getCode().equals(status)) {
                //待审核
                List<String> tempList=new ArrayList<String>();
                tempList.add(ConsignOrderStatus.NEW.getCode());
                statusItem.setOrderStatus(tempList);
            } else if (ConsignOrderStatusForSeller.NEWAPPROVED.getCode().equals(status)) {
                //待关联
                List<String> tempList=new ArrayList<String>();
                tempList.add(ConsignOrderStatus.NEWAPPROVED.getCode());
                statusItem.setOrderStatus(tempList);
            } else if (ConsignOrderStatusForSeller.TOBEPICKUP.getCode().equals(status)) {
                //待提货
                List<String> tempList=new ArrayList<String>();
                tempList.add(ConsignOrderStatus.RELATED.getCode());
                tempList.add(ConsignOrderStatus.SECONDSETTLE.getCode());
                statusItem.setOrderStatus(tempList);
            } else if (ConsignOrderStatusForSeller.TOBEDELINERY.getCode().equals(status)) {
                //待放货
                List<String> tempList=new ArrayList<String>();
                tempList.add(ConsignOrderStatus.RELATED.getCode());
                tempList.add(ConsignOrderStatus.SECONDSETTLE.getCode());
                statusItem.setOrderStatus(tempList);
            } else if (ConsignOrderStatusForSeller.TOBEPAYREQUEST.getCode().equals(status)) {
                //待申请付款
                List<String> tempList=new ArrayList<String>();
                tempList.add(ConsignOrderStatus.RELATED.getCode());
                tempList.add(ConsignOrderStatus.SECONDSETTLE.getCode());
                statusItem.setOrderStatus(tempList);
                statusItem.setPayStatus(ConsignOrderPayStatus.APPLY.getCode());
            } else if (ConsignOrderStatusForSeller.PAYREQUEST.getCode().equals(status)) {
                //待审核付款
                List<String> tempList=new ArrayList<String>();
                tempList.add(ConsignOrderStatus.RELATED.getCode());
                tempList.add(ConsignOrderStatus.SECONDSETTLE.getCode());
                statusItem.setOrderStatus(tempList);
                statusItem.setPayStatus(ConsignOrderPayStatus.REQUESTED.getCode());
            } else if (ConsignOrderStatusForSeller.TOBEPRINTPAYREQUEST.getCode().equals(status)) {
                //待打印付款申请单
                List<String> tempList=new ArrayList<String>();
                tempList.add(ConsignOrderStatus.RELATED.getCode());
                tempList.add(ConsignOrderStatus.SECONDSETTLE.getCode());
                statusItem.setOrderStatus(tempList);
                statusItem.setPayStatus(ConsignOrderPayStatus.APPROVED.getCode());
            } else if (ConsignOrderStatusForSeller.COMFIRMEDPAY.getCode().equals(status)) {
                //待确认已付款
                List<String> tempList=new ArrayList<String>();
                tempList.add(ConsignOrderStatus.RELATED.getCode());
                tempList.add(ConsignOrderStatus.SECONDSETTLE.getCode());
                statusItem.setOrderStatus(tempList);
                statusItem.setPayStatus(ConsignOrderPayStatus.APPLYPRINTED.getCode());
            } else if (ConsignOrderStatusForSeller.SECONDSETTLE.getCode().equals(status)) {
                //待二次结算
                List<String> tempList=new ArrayList<String>();
                tempList.add(ConsignOrderStatus.SECONDSETTLE.getCode());
                statusItem.setOrderStatus(tempList);
            } else if (ConsignOrderStatusForBuyer.TOBEINVOICEREQUEST.getCode().equals(status)) {
                //待开票申請
                List<String> tempList=new ArrayList<String>();
                tempList.add(ConsignOrderStatus.INVOICEREQUEST.getCode());
                statusItem.setOrderStatus(tempList);
            }
            else if (ConsignOrderStatusForSeller.INVOICEREQUEST.getCode().equals(status)) {
                //待开票
                List<String> tempList=new ArrayList<String>();
                tempList.add(ConsignOrderStatus.INVOICE.getCode());
                statusItem.setOrderStatus(tempList);
            } else if (ConsignOrderStatusForSeller.FINISH.getCode().equals(status)) {
                //交易完成
                List<String> tempList=new ArrayList<String>();
                tempList.add(ConsignOrderStatus.FINISH.getCode());
                statusItem.setOrderStatus(tempList);
            } else if (ConsignOrderStatusForSeller.CLOSEREQUEST.getCode().equals(status)) {
                //交易关闭待审核
                List<String> tempList=new ArrayList<String>();
                tempList.add(ConsignOrderStatus.CLOSEREQUEST1.getCode());
                tempList.add(ConsignOrderStatus.CLOSEREQUEST2.getCode());
                tempList.add(ConsignOrderStatus.CLOSESECONDARYREQUEST.getCode());
                tempList.add(ConsignOrderStatus.CLOSESECONDARYAPPROVED.getCode());
                statusItem.setOrderStatus(tempList);
            } else if (ConsignOrderStatusForSeller.CLOSED.getCode().equals(status)) {
                //交易关闭
                List<String> tempList=new ArrayList<String>();
                tempList.add(ConsignOrderStatus.NEWDECLINED.getCode());
                tempList.add(ConsignOrderStatus.CLOSEAPPROVED.getCode());
                tempList.add(ConsignOrderStatus.SYSCLOSED.getCode());
                tempList.add(ConsignOrderStatus.CLOSEPAY.getCode());
                tempList.add(ConsignOrderStatus.CLOSESECONDARY.getCode());
                tempList.add(ConsignOrderStatus.CLOSEPAYED.getCode());
                statusItem.setOrderStatus(tempList);
            }
            if(statusItem.getOrderStatus()!=null || statusItem.getPayStatus()!=null) {
                orderStatusList.add(statusItem);
            }
        });
        query.setOrderStatusList(orderStatusList);
    }
}
