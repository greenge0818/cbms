package com.prcsteel.platform.order.web.controller.acceptdraft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.google.common.collect.Lists;
import com.prcsteel.platform.account.model.enums.AccountTag;
import com.prcsteel.platform.acl.model.dto.Organization;
import com.prcsteel.platform.acl.service.OrganizationService;
import com.prcsteel.platform.account.service.AccountService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.prcsteel.platform.account.model.enums.AttachmentType;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.acl.model.model.UserOrg;
import com.prcsteel.platform.acl.service.UserOrgService;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.common.utils.FileUtil;
import com.prcsteel.platform.common.utils.PageData;
import com.prcsteel.platform.common.utils.Tools;
import com.prcsteel.platform.common.vo.PageResult;
import com.prcsteel.platform.common.vo.Result;
import com.prcsteel.platform.order.model.enums.AcceptDraftStatus;
import com.prcsteel.platform.order.model.model.AcceptDraft;
import com.prcsteel.platform.order.model.model.AcceptDraftAttachment;
import com.prcsteel.platform.order.model.model.AcceptDraftList;
import com.prcsteel.platform.order.model.query.AcceptDraftQuery;
import com.prcsteel.platform.order.service.acceptdraft.AcceptDraftAttachmentService;
import com.prcsteel.platform.order.service.acceptdraft.AcceptDraftListService;
import com.prcsteel.platform.order.service.acceptdraft.AcceptDraftService;
import com.prcsteel.platform.order.web.controller.BaseController;
import com.prcsteel.platform.order.web.controller.invoice.InvoiceOutController;
import com.prcsteel.platform.order.web.support.ObjectExcelRead;
import com.prcsteel.platform.order.web.support.ObjectExcelView;
import com.prcsteel.platform.order.web.support.ShiroVelocity;

/**
 * @author Rabbit
 * @Title: AcceptDraftController.java
 * @Package com.prcsteel.cbms.web.controller
 * @Description: 承兑汇票模块
 * @date 2015-11-11 10:11:08
 */
@Controller
@RequestMapping("/acceptdraft")
public class AcceptDraftController extends BaseController {
    private static final int AMOUNT_LIMIT = 2;
    @Resource
    AcceptDraftService acceptDraftService;
    @Resource
    AcceptDraftListService acceptDraftListService;
    @Resource
    AcceptDraftAttachmentService attachmentService;
    @Resource
    UserOrgService userOrgService;
    @Resource
    AccountService accountService;
    @Resource
    OrganizationService organizationService;


    private static Logger logger = LogManager.getLogger(InvoiceOutController.class);

    ShiroVelocity permissionLimit = new ShiroVelocity();

    private static final String PERMISSION_AUDITRECHARGE_WITHDRAW = "acceptdraft:auditrecharge:withdraw";  // 撤回充值申请
    private static final String ACCEPTDRAFT_CHECK_SUBMIT = "acceptdraft:check:submit";  // 银票充值审核

    private static final String PERMISSION_BUTTON_CHARGED_APPLYCANCLECHARGED = "acceptdraft:charged:applycanclecharged";  //申请取消充值


    @RequestMapping("/list")
    public void index(ModelMap out) {
        out.put("status", AcceptDraftStatus.values());
        List<UserOrg> setting = userOrgService.getConfigByUserId(getLoginUser().getId());
        List<Organization> organizationList = organizationService.queryDraftedOrg();
        List<UserOrg> userOrgList = Lists.newArrayList();
        for(Organization organization : organizationList){
            for(UserOrg userOrg : setting){
                if(userOrg.getOrgId().compareTo(Long.parseLong(organization.getId())) == 0){
                    userOrgList.add(userOrg);
                }
            }
        }
        int size = userOrgList.size();
        if(size == 0 || size == 4){
            out.put("org","");
        }else if(size == 1){
            out.put("org",setting.get(0).getOrgId());
        }
    }
    // 银票清单
    @RequestMapping("/taelsList")
    public void taelsList(ModelMap out) {
        out.put("status", AcceptDraftStatus.values());
        List<UserOrg> setting = userOrgService.getConfigByUserId(getLoginUser().getId());
        List<Organization> organizationList = organizationService.queryDraftedOrg();
        List<UserOrg> userOrgList = Lists.newArrayList();
        for(Organization organization : organizationList){
            for(UserOrg userOrg : setting){
                if(userOrg.getOrgId().compareTo(Long.parseLong(organization.getId())) == 0){
                    userOrgList.add(userOrg);
                }
            }
        }
        int size = userOrgList.size();
        if(size == 0 || size == 4){
            out.put("org","");
        }else if(size == 1){
            out.put("org",setting.get(0).getOrgId());
        }
    }

    @RequestMapping("/create")
    public void create(ModelMap out, @RequestParam(value = "id", required = false) Long id) {
        if (id != null) {
            out.put("acceptdraft", acceptDraftService.selectByPrimaryKey(id));
            out.put("attachments", attachmentService.queryMainByAcceptDraftId(id));
        }else {
            out.put("orgId", out.get("orgName").equals("无锡服务中心")?11:10);
        }
    }

    @RequestMapping("/getData")
    @ResponseBody
    public PageResult getData(AcceptDraftQuery query) {
        PageResult result = new PageResult();
        List<AcceptDraft> draftList = acceptDraftService.queryByParam(query).stream().peek(a -> a.setOrgName(accountService.queryById(a.getAccountId()).getAccountTag() == 1 ? "" : a.getOrgName())).collect(Collectors.toList());
        result.setData(draftList);
        result.setRecordsFiltered(acceptDraftService.countByParam(query));
        result.setRecordsTotal(draftList.size());
        return result;
    }

    @RequestMapping("/getTaelsData")
    @ResponseBody
    public PageResult getTaelsData(AcceptDraftQuery query) {
        PageResult result = new PageResult();
        List<AcceptDraftList> draftList = acceptDraftListService.queryByParam(query);
        result.setData(draftList);
        result.setRecordsFiltered(acceptDraftListService.countByParam(query));
        result.setRecordsTotal(draftList.size());
        return result;
    }

    /**
     * 银票充值页面（待审核状态）
     *
     * @param out 输出
     * @param id  id
     */
    @RequestMapping("/{id}/auditrecharge.html")
    public String auditRecharge(ModelMap out, @PathVariable("id") Long id) {
        AcceptDraft acceptDraft = acceptDraftService.selectByPrimaryKey(id);
        if (acceptDraft != null && AcceptDraftStatus.SUBMITTED.getCode().equals(acceptDraft.getStatus())) {
            out.put("acceptDraft", acceptDraft);

            List<AcceptDraftAttachment>   acceptDraftAttachments = attachmentService.queryMainByAcceptDraftId(id);
            if (acceptDraftAttachments != null) {
                out.put("attachments", acceptDraftAttachments);
            }
        }
        return "acceptdraft/auditrecharge";
    }

    /**
     * 撤回充值申请
     *
     * @param id id
     */
    @RequestMapping("/withdrawaudit.html")
    @ResponseBody
    public Result withdrawAudit(@RequestParam("id") Long id) {
        if ((!permissionLimit.hasPermission(PERMISSION_AUDITRECHARGE_WITHDRAW))) {
            return new Result("您没有作该操作的权限", Boolean.FALSE);
        }
        try {
            acceptDraftService.withdrawAudit(id, getLoginUser());
            return new Result("撤回充值申请成功。", Boolean.TRUE);
        } catch (BusinessException ex) {
            logger.error("撤回充值申请失败。", ex);
            return new Result(ex.getMsg(), Boolean.FALSE);
        }

    }

    /**
     * 银票充值审核页面（待审核状态）
     * modify by wangxianjun 20151204 迭代8 银票由只能上传一张照片改为银票可以最多上传10张图片
     * @param out 输出
     * @param id  id
     */
    @RequestMapping("/{id}/check.html")
    public String check(ModelMap out, @PathVariable("id") Long id) {
        AcceptDraft acceptDraft = acceptDraftService.selectByPrimaryKey(id);
        if (acceptDraft != null && AcceptDraftStatus.SUBMITTED.getCode().equals(acceptDraft.getStatus())) {
            out.put("acceptDraft", acceptDraft);

            List<AcceptDraftAttachment> acceptDraftAttachments = attachmentService.queryMainByAcceptDraftId(id);
            if (acceptDraftAttachments != null) {
                out.put("attachments", acceptDraftAttachments);
            }
        }
        return "acceptdraft/check";
    }

    /**
     * 确认充值页面
     *
     * @param acceptDraft 审核数据
     */
    @RequestMapping("/confirmrecharge.html")
    @ResponseBody
    public Result confirmRecharge(AcceptDraft acceptDraft) {
        if ((!permissionLimit.hasPermission(ACCEPTDRAFT_CHECK_SUBMIT))) {
            return new Result("您没有作该操作的权限", Boolean.FALSE);
        }
        try {
            User user = getLoginUser();
            acceptDraftService.confirmRecharge(acceptDraft, user);
            return new Result("确认充值成功。", Boolean.TRUE);
        } catch (BusinessException ex) {
            logger.error("确认充值失败。", ex);
            return new Result(ex.getMsg(), Boolean.FALSE);
        }

    }


    /**
     * 银票充值页面（已完成充值）
     * modify by wangxianjun 20151204 迭代8 银票由只能上传一张照片改为银票可以最多上传10张图片
     * @param out 界面输出model对象
     * @param id  票据单id
     * @return
     */
    @RequestMapping("/{id}/viewcharged")
    public String viewCharged(ModelMap out, @PathVariable Long id) {
        if (id != null) {
            AcceptDraft acceptDraft = acceptDraftService.selectByPrimaryKey(id);
            if (acceptDraft != null && StringUtils.equals(AcceptDraftStatus.CHARGED.getCode(), acceptDraft.getStatus())) {
                out.put("acceptDraft", acceptDraft);

                //按钮权限
                out.put("isShow", permissionLimit.hasPermission(PERMISSION_BUTTON_CHARGED_APPLYCANCLECHARGED));
                out.put("isDisabled", acceptDraftService.checkAcceptDarftIsPayed(id));
                List<AcceptDraftAttachment> attachments = attachmentService.queryMainByAcceptDraftId(id);
                if (attachments != null) {
                    out.put("attachments", attachments);
                }
            }
        }
        return "acceptdraft/charged";
    }
    /**
     * 银票保存和提交审核页面
     * modify by wangxianjun 20151204 迭代8 银票由只能上传一张照片改为银票可以最多上传10张图片
     * 新增输入参数imgsId，保存删除图片的ID
     * @return
     */
    @RequestMapping("/save")
    @ResponseBody
    public Result save(AcceptDraft acceptDraft, String operation, MultipartHttpServletRequest request,@RequestParam("imgsId") String imgsId) {
        Result result = new Result();
        //String checkResult;
        try {
            //modify by wangxianjun 迭代8 银票由只能上传一张照片改为银票可以最多上传10张图片
            List<MultipartFile> acceptDraftPicList = request
                    .getFiles("pic_accept_draft");
            List<MultipartFile> attachmentList = new ArrayList<MultipartFile>();
            if (acceptDraftPicList != null && !acceptDraftPicList.isEmpty()) {
                for (MultipartFile file : acceptDraftPicList) {
                    if (!checkUploadAttachment(file, result, attachmentList)) {
                        return result;
                    }
                }

            }

            result.setData(acceptDraftService.save(acceptDraft, attachmentList, operation, getLoginUser(),imgsId));
            result.setSuccess(true);
        } catch (BusinessException e) {
            result.setData(e.getMsg());
            result.setSuccess(false);
        }
        return result;
    }


    //modify by wangxianjun 迭代8 银票由只能上传一张照片改为银票可以最多上传10张图片
    private boolean checkUploadAttachment(MultipartFile file, Result result,List<MultipartFile> attachmentList) {
        String suffix = FileUtil.getFileSuffix(file.getOriginalFilename());

        if (suffix == null
                || !Constant.IMAGE_SUFFIX.contains(suffix.toLowerCase())) {
            result.setData(AttachmentType.valueOf(file.getName()).getName() + "文件格式不正确");
            result.setSuccess(false);
            return false;
        }
        if (file.getSize() / Constant.M_SIZE > Constant.MAX_IMG_SIZE) {
            result.setData(AttachmentType.valueOf(file.getName()).getName() + "超过" + Constant.MAX_IMG_SIZE + "M");
            result.setSuccess(false);
            return false;
        }
        attachmentList.add(file);
        return true;
    }

    /**
     * 申请取消充值
     *
     * @param id 票据id
     * @return
     */
    @RequestMapping("/{id}/applycanclecharged")
    @ResponseBody
    public Result applyCancleCharged(@PathVariable Long id, String reason) {
        //权限判断
        if (!permissionLimit.hasPermission(PERMISSION_BUTTON_CHARGED_APPLYCANCLECHARGED)) {
            return new Result("您没有该操作的权限。", Boolean.FALSE);
        }
        try {
            acceptDraftService.applyCancleCharged(id, reason, this.getLoginUser());
            return new Result("已成功提交取消充值申请，待核算会计审核。", Boolean.TRUE);
        } catch (BusinessException e) {
            logger.error("request param id：" + id + "  error info：" + e.getMsg(), e);
            return new Result(e.getMsg(), Boolean.FALSE);
        }
    }

    /**
     * 银票取消充值审核页面（申请取消充值状态）
     * modify by wangxianjun 20151204 迭代8 银票由只能上传一张照片改为银票可以最多上传10张图片
     * @param out 输出
     * @param id  id
     */
    @RequestMapping("/{id}/cancelcharge.html")
    public String cancelcharge(ModelMap out, @PathVariable("id") Long id) {
        AcceptDraft acceptDraft = acceptDraftService.selectByPrimaryKey(id);
        if (acceptDraft != null && AcceptDraftStatus.ROLLBACKREQUEST.getCode().equals(acceptDraft.getStatus())) {
            out.put("acceptDraft", acceptDraft);

            List<AcceptDraftAttachment> acceptDraftAttachments = attachmentService.queryMainByAcceptDraftId(id);
            if (acceptDraftAttachments != null) {
                out.put("attachments", acceptDraftAttachments);
            }
        }
        return "acceptdraft/cancelcharge";
    }

    /**
     * 审核银票取消充值（申请取消充值状态）
     *
     * @param isPass 是否通过申请
     * @param id  id
     */
    @RequestMapping("audit.html")
    @ResponseBody
    public Result audit(Long id, boolean isPass) {
        Result result = new Result();
        try {
            if (isPass) {
                acceptDraftService.rollback(id, getLoginUser());
            } else {
                acceptDraftService.refuseRollback(id, getLoginUser());
            }
            result.setSuccess(true);
        }catch (BusinessException e){
            result.setSuccess(false);
            result.setData(e.getMsg());
        }
        return result;
    }

    /**
     * 银票清单  导出表格
     * @param code
     * @param startTime
     * @param endTime
     * @param codeIds
     * @param rowstr
     * @return
     */
    @RequestMapping("/exportexcel.html")
    @ResponseBody
    public ModelAndView exportExcel(@RequestParam("code") String code,
                                    @RequestParam("startTime") String startTime,
                                    @RequestParam("endTime") String endTime,
                                    @RequestParam("codeIds") String codeIds,
                                    @RequestParam("rowstr") String rowstr )  {
        ModelAndView mv = null;
        String [] stringArr= rowstr.split("-");

        try {
            List<AcceptDraftList> list = acceptDraftListService.findByParam(code, startTime, endTime, codeIds);
            Map<String, Object> dataMap = new HashMap<>();
            List<String> titles = new ArrayList<>();
            titles.add("票号");
            if(stringArr[0].equals("1"))
                titles.add("金额(元)");
            if(stringArr[1].equals("1"))
                titles.add("承兑日");
            if(stringArr[2].equals("1"))
                titles.add("到期日");
            if(stringArr[3].equals("1"))
                titles.add("承兑行行号");
            if(stringArr[4].equals("1"))
                titles.add("承兑行行全称");
            if(stringArr[5].equals("1"))
                titles.add("出票人名称");
            if(stringArr[6].equals("1"))
                titles.add("出票人账号");
            if(stringArr[7].equals("1"))
                titles.add("出票人开户行行号");
            if(stringArr[8].equals("1"))
                titles.add("出票人开户行全称");
            if(stringArr[9].equals("1"))
                titles.add("收款人名称");
            if(stringArr[10].equals("1"))
                titles.add("收款人账户");
            if(stringArr[11].equals("1"))
                titles.add("收款人开户全称");
            if(stringArr[12].equals("1"))
                titles.add("调整天数");
            if(stringArr[13].equals("1"))
                titles.add("背书次数");
            if(stringArr[14].equals("1"))
                titles.add("合同号");
            if(stringArr[15].equals("1"))
                titles.add("是否我行代开他行签发票据");
            dataMap.put("titles", titles);
            List<PageData> varList = new ArrayList<>();
            PageData vpd = null;

            for (AcceptDraftList draftList : list) {
                int var=1;
                vpd = new PageData();
                vpd.put("var"+var, draftList.getCode());//票号
                var++;
                if(stringArr[0].equals("1")) {
                    vpd.put("var"+var, Tools.formatBigDecimal(draftList.getAmount(), AMOUNT_LIMIT));// 余额
                    var++;
                }
                if(stringArr[1].equals("1")) {
                    vpd.put("var"+var, Tools.dateToStr(draftList.getAcceptanceDate(), "yyyy-MM-dd"));//承兑日
                    var++;
                }
                if(stringArr[2].equals("1")) {
                    vpd.put("var"+var, Tools.dateToStr(draftList.getEndDate(), "yyyy-MM-dd"));//到期日
                    var++;
                }
                if(stringArr[3].equals("1")) {
                    vpd.put("var"+var, draftList.getAcceptanceBankCode());//承兑行行号
                    var++;
                }
                if(stringArr[4].equals("1")) {
                    vpd.put("var"+var, draftList.getAcceptanceBankFullName());//承兑行全称
                    var++;
                }
                if(stringArr[5].equals("1")) {
                    vpd.put("var"+var, draftList.getDrawerName());//出票人名称
                    var++;
                }
                if(stringArr[6].equals("1")) {
                    vpd.put("var"+var, draftList.getDrawerAccountCode());//出票人账号
                    var++;
                }
                if(stringArr[7].equals("1")) {
                    vpd.put("var"+var, draftList.getDrawerBankCode());//出票人开户行行号 drawer_bank_code
                    var++;
                }
                if(stringArr[8].equals("1")) {
                    vpd.put("var"+var, draftList.getDrawerBankFullName());//出票人开户行全称
                    var++;
                }
                if(stringArr[9].equals("1")) {
                    vpd.put("var"+var, draftList.getReceiverName());//收款人名称
                    var++;
                }
                if(stringArr[10].equals("1")) {
                    vpd.put("var"+var, draftList.getReceiverAccountCode());//收款人账号
                    var++;
                }
                if(stringArr[11].equals("1")) {
                    vpd.put("var"+var, draftList.getReceiverBankFullName());//收款人开户行全称
                    var++;
                }
                if(stringArr[12].equals("1")) {
                    vpd.put("var"+var, String.valueOf(draftList.getAdjustDateCount()));//调整天数
                    var++;
                }
                if(stringArr[13].equals("1")) {
                    vpd.put("var"+var, String.valueOf(draftList.getReadTimes()));//背书次数
                    var++;
                }
                if(stringArr[14].equals("1")) {
                    vpd.put("var"+var, draftList.getAcceptanceAgreementNumber());//合同号
                    var++;
                }
                if(stringArr[15].equals("1")) {
                    String isDifferentBank = draftList.getIsDifferentBank();
                    if (isDifferentBank == null) {
                        isDifferentBank = "否";
                    }
                    vpd.put("var"+var, isDifferentBank);//是否我行代开他行签发票据
                }
                varList.add(vpd);
            }
            dataMap.put("varList", varList);

            ObjectExcelView erv = new ObjectExcelView();//执行excel操作
            mv = new ModelAndView(erv, dataMap);
        } catch (Exception e) {
            logger.debug("账户信息导出EXCEL出错：", e);
        }
        return mv;
    }




    /**
     * 银票清单 上传表格
     *
     * @param acceptDraftExcel
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/uploadingExcel")
    @ResponseBody
    public Result uploadingExcel(@RequestParam("acceptDraftExcel") MultipartFile acceptDraftExcel) throws Exception{
        Result result = new Result();
        if (acceptDraftExcel == null || acceptDraftExcel.isEmpty()) {
            result.setSuccess(false);
            result.setData("请上传EXCEL文件！");
            return result;
        }
        try{
            //执行读EXCEL操作,读出的数据导入List 2:从第3行开始；0:从第A列开始；0:第0个sheet
            List<PageData> listPageData = (List) ObjectExcelRead.readExcel(
                    acceptDraftExcel,1,0,0);

            if(acceptDraftListService.uploadingExcel(listPageData,getLoginUser())){
                result.setSuccess(true);
                result.setData("上传EXCEL文件成功！" );
            }else{
                result.setSuccess(false);
                result.setData("上传EXCEL文件出错！");
            }
        }catch (Exception e){
            logger.error("读取excel出错：", e);
            result.setSuccess(false);
            result.setData("上传EXCEL文件出错！!");
        }
        return result;
    }
    /**
     * 银票清单 表格上传
     *
     */
    @RequestMapping(value="/uploading")
    @ResponseBody
    public Result uploading(@RequestParam("acceptDraftExcel") MultipartFile acceptDraftExcel) throws Exception{
        Result result = new Result();
        if (acceptDraftExcel == null || acceptDraftExcel.isEmpty()) {
            result.setSuccess(false);
            result.setData("请上传EXCEL文件！");
            return result;
        }
        try{
            //执行读EXCEL操作,读出的数据导入List 2:从第3行开始；0:从第A列开始；0:第0个sheet
            List<PageData> listPageData = (List) ObjectExcelRead.readExcel(
                    acceptDraftExcel,1,0,0);
            String[]codeArr=new String[listPageData.size()];
            int   repeatCode=0;
            int   codeNumber=0;
            for(PageData pageData : listPageData){
                String code = pageData.getString("var0");
                if(acceptDraftListService.findById(code) > 0) {
                    repeatCode++;
                }
                codeArr[codeNumber]=code;
                codeNumber++;
            }
            if(!findRepetition(codeArr).equals("")){
                result.setSuccess(false);
                result.setData("Excel表格【票号】不能重复！\n以下重复票号：\n"+findRepetition(codeArr));
                return result;
            }
            if(repeatCode>0){
                result.setSuccess(true);
                result.setData(repeatCode);
                return result;
            }
            if(repeatCode==0){
                if (acceptDraftListService.uploadingExcel(listPageData, getLoginUser())){
                    result.setSuccess(true);
                    result.setData(repeatCode);
                }else{
                    result.setSuccess(false);
                    result.setData("上传EXCEL文件出错！");
                }
            }
        }catch (Exception e){
            logger.error("读取excel出错：", e);
            result.setSuccess(false);
            result.setData("上传EXCEL文件出错！!");
        }
        return result;
    }
    /**
     * 判断Excel表格里是否有重复数据
     *
     * @param codeArr
     * @return
     */
    public String  findRepetition(String[] codeArr) {
        //从第一个元素开始比较元素是不是有相同的出现
        String codeStr ="";
        for (int i = 0; i < codeArr.length; i++) {
            for (int j = i + 1; j < codeArr.length; j++) {
                if (codeArr[i].equals(codeArr[j])) {
                    codeStr += codeArr[i].toString()+",";
                }
            }
        }
        return codeStr;
    }

    /**
     * 获取可以做银票业务的服务中心
     * @return
     */
    @RequestMapping("queryDraftedOrg.html")
    @ResponseBody
    public Result queryDraftedOrg(){
        Result result = new Result();
        result.setSuccess(true);
        try{
            result.setData(organizationService.queryDraftedOrg());
        }catch(BusinessException e){
            result.setSuccess(false);
            result.setData(e.getMsg());
        }
        return result;
    }


}
