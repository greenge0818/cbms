package com.prcsteel.platform.acl.web.controller;

import com.prcsteel.platform.account.model.enums.AccountContractTemplateType;
import com.prcsteel.platform.account.model.model.AccountContractTemplate;
import com.prcsteel.platform.acl.api.RestTemplateApiInvoker;
import com.prcsteel.platform.acl.model.dto.BaseOrganizationDeliverDto;
import com.prcsteel.platform.acl.model.dto.BaseOrganizationDto;
import com.prcsteel.platform.acl.model.dto.ReportMailSettingDto;
import com.prcsteel.platform.acl.model.dto.TargetWeightForUpdateDto;
import com.prcsteel.platform.acl.model.enums.ReportMailAttachmentType;
import com.prcsteel.platform.acl.model.enums.ReportMailPeriodType;
import com.prcsteel.platform.acl.model.enums.SysSettingType;
import com.prcsteel.platform.acl.model.model.BaseDeliver;
import com.prcsteel.platform.acl.model.model.BaseOrganizationDeliver;
import com.prcsteel.platform.acl.model.model.Organization;
import com.prcsteel.platform.acl.model.model.OrganizationTargetWeight;
import com.prcsteel.platform.acl.model.model.SysSetting;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.acl.model.query.BaseDeliverQuery;
import com.prcsteel.platform.acl.model.query.SysSettingQuery;
import com.prcsteel.platform.acl.service.ExpressFirmSetService;
import com.prcsteel.platform.acl.service.OrganizationTargetWeightService;
import com.prcsteel.platform.acl.service.ReportMailSettingService;
import com.prcsteel.platform.acl.service.SysSettingService;
import com.prcsteel.platform.acl.web.support.ShiroVelocity;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.common.service.FileService;
import com.prcsteel.platform.common.utils.FileUtil;
import com.prcsteel.platform.common.vo.PageResult;
import com.prcsteel.platform.common.vo.Result;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.ws.rs.PathParam;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author jiangnan on 15-8-7.
 */

@Controller
@RequestMapping("/sys/")
public class SysSettingController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(SysSettingController.class);

    private static final String TEMPLATE_PATH = "template/";

    @Autowired
    SysSettingService sysSettingService;

    @Autowired
    ExpressFirmSetService expressFirmSetService;
    @Autowired
    ReportMailSettingService reportMailSettingService;
    
    @Resource
    private FileService fileService;
   /* @Resource
    private OrganizationService organizationService;*/

    @Resource
    private RestTemplateApiInvoker restTemplateApiInvoker;

    ShiroVelocity permissionLimit = new ShiroVelocity();

    private static final String SYS_SETTINGLIST_EDIT = "sys:settinglist:edit";   // 修改系统设置
  //迁移风控代码，增加5个字段 add by zhoucai@prcsteel.com 2016-5-6
    @Resource
    OrganizationTargetWeightService targetWeightService;

    //获取数据库中的settingvalue（发票上限额度）
    @RequestMapping("setInvoice.html")
    public void sysSettingController(ModelMap out) {

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("type", "invoice");
        out.put("sysSetting", sysSettingService.queryBySettingType("invoice"));

    }

    @RequestMapping("creditlimit.html")
    public void loadCreditLimit(ModelMap out){
    	
    	SysSettingQuery query  = new SysSettingQuery();
    	query.setType(SysSettingType.CreditLimit.getCode());
        query.setSort("last_updated desc");
        List<SysSetting> settings = sysSettingService.selectByParam(query);
       
        if(settings!=null && !settings.isEmpty()){
        	SysSetting sysSetting = settings.get(0);
        	out.put("sysSetting",  sysSetting);
        }
        
    }
    
    @ResponseBody
    @RequestMapping("updatecreditlimit.html")
    public Result updateCreditLimit(@RequestParam("settingvalue") String limit){
        SysSettingQuery query  = new SysSettingQuery();
        query.setType(SysSettingType.CreditLimit.getCode());
        query.setSort("last_updated desc");
        
        List<SysSetting> settings = sysSettingService.selectByParam(query);
        
        if(settings!=null && !settings.isEmpty()){
        	SysSetting sysSetting = settings.get(0);
        	int ModificationNumberOld = sysSetting.getModificationNumber();
        	//验证是不是数字
        	String reg = "(\\d+\\.?\\d{0,2})|([0-9]*[1-9][0-9]*)";
        	if(limit.matches(reg)){
        		String settingvalue = limit;
        		if(limit.endsWith("."))
        			settingvalue = limit.substring(0, limit.length()-1);
            	//封装
            	sysSetting.setSettingValue(settingvalue);
            	sysSetting.setLastUpdated(new Date());
            	sysSetting.setLastUpdatedBy(this.getLoginUser().getName());
            	sysSetting.setModificationNumber(ModificationNumberOld + 1);
            	//更新数据
            	try {
            		sysSettingService.insertSelective(sysSetting);
    			} catch (BusinessException e) {
    				new Result(e.getMessage(), false);
    			}
        	}else{
        		return new Result("非法输入", false);
        	}
        }
        return new Result();
    }
    
    //更新数据库中的settingvalue（发票上限额度）
    @ResponseBody
    @RequestMapping(value = "updatesettingvalue.html", method = RequestMethod.POST)
    public void updateSettingValue(@RequestParam("settingvalue") String value) {
        //获取当前修改次数
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("type", "invoice");
        SysSetting sysSettingOld = sysSettingService.queryBySettingType("invoice");
        int ModificationNumberOld = sysSettingOld.getModificationNumber();
        //去除发票上限额度的逗号
        String settingvalue = value.replace(",", "");
        //封装
        SysSetting sysSetting = new SysSetting();
        sysSetting.setSettingValue(settingvalue);
        sysSetting.setSettingType("invoice");
        sysSetting.setLastUpdated(new Date());
        sysSetting.setLastUpdatedBy(this.getLoginUser().getName());
        sysSetting.setModificationNumber(ModificationNumberOld + 1);
        //更新数据
        sysSettingService.updateBySettingTypeSelective(sysSetting);
    }

    /**
     * 合同模板
     *
     * @param out
     */
    @RequestMapping("template.html")
    public void template(ModelMap out) {
        List<AccountContractTemplate> templates = restTemplateApiInvoker.list("0");
        out.put("templates", templates);
        SysSetting buyer = sysSettingService.queryBySettingType(Constant.TEMPLATE_BUYER);
        SysSetting seller = sysSettingService.queryBySettingType(Constant.TEMPLATE_SELLER);
        SysSetting buyerParam = sysSettingService.queryBySettingType(Constant.TEMPLATE_BUYER_PARAM);
        SysSetting attachment = sysSettingService.queryBySettingType(Constant.TEMPLATE_PRINT_PIC);

        out.put("buyer", buyer != null ? buyer.getSettingValue() : "");
        out.put("seller", seller != null ? seller.getSettingValue() : "");
        out.put("buyerParam", buyerParam != null ? buyerParam.getSettingValue() : "");
        out.put("attachment", attachment != null ? attachment.getSettingValue() : "");
    }

    /**
     * 合同模板
     *
     * @param out
     */
    @RequestMapping(value = "template/detail.html", method = RequestMethod.GET)
    public String templateDetail(ModelMap out, Long id, String type, String action) {
        Result detail = restTemplateApiInvoker.resolve(0L,id, type);
        
        out.put("id", id);
        out.put("action", action);
        out.put("type", type);
        out.put("detail", detail.getData());
        //合同编号
		out.put("templateNo", "20 160323 00001");
        if(type.equals(AccountContractTemplateType.BUYER.getCode()) || type.equals(AccountContractTemplateType.SELLER.getCode())){
            return "sys/template_detail";
        }else{
            return "sys/agreement_detail";
        }

    }

    @RequestMapping(value = "template/save.html", method = RequestMethod.POST)
    @ResponseBody
    public Result templateDetail(Long id, String preContent,Integer sysTemplateStatus) {
        AccountContractTemplate act = new AccountContractTemplate();
        act.setId(id);
        act.setPreContent(preContent);
        act.setSysTemplateStatus(sysTemplateStatus);
        return restTemplateApiInvoker.save(act);
    }

    /**
     * 合同模板
     *
     * @param request
     * @return isStartNewtemp
     * @throws IOException
     */
    @RequestMapping("savetemplate.html")
    public String savetemplate(MultipartHttpServletRequest request,@RequestParam("isStartNewtemp") String isStartNewTemp) throws IOException {
       try{
    	 
    	 MultipartFile mf = request.getFile("pic_cert");
    	 checkUploadAttachment(mf);
    	 updatePic(mf,Constant.TEMPLATE_PRINT_PIC);
    	 mf = request.getFile(Constant.TEMPLATE_BUYER);
         saveTemplates(mf, Constant.TEMPLATE_BUYER);
         saveParams(isStartNewTemp, Constant.TEMPLATE_BUYER_PARAM);
         mf = request.getFile(Constant.TEMPLATE_SELLER);
         saveTemplates(mf, Constant.TEMPLATE_SELLER);
       }catch(BusinessException e){
       	throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, e.getMessage());
       }

        return "redirect:template.html";
    }

    private void checkUploadAttachment(MultipartFile file){
		String suffix = FileUtil.getFileSuffix(file.getOriginalFilename());

		if (suffix == null
				|| !Constant.IMAGE_SUFFIX.contains(suffix.toLowerCase())) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "图片格式不正确");
		}
		if (file.getSize() / Constant.M_SIZE > Constant.MAX_IMG_SIZE) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "图片超过" + Constant.MAX_IMG_SIZE + "M");
		}
	}
    /**
     * 上传打印图片
     * modify by wangxianjun 20160420
     *
     * @return
     */
    public void updatePic(MultipartFile attachmentfile,String type) {
    	try{
    		
        if (attachmentfile != null) {   //如果没上传图片，则不执行插入
        	SysSetting template = sysSettingService.queryBySettingType(type);
        	String picName = "";
        	try{
        	 picName = saveMultipartPic(attachmentfile,type);
        	}catch(IOException e){
        		throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, e.getMessage());
        	}
        	if(template != null){
        		 template.setSettingValue(picName);
                 template.setCreatedBy(getLoginUser().getLoginId());
                 template.setLastUpdatedBy(getLoginUser().getLoginId());
                 template.setLastUpdated(new Date());
                 template.setModificationNumber(template.getModificationNumber() + 1);
        		 if (sysSettingService.updateByPrimaryKey(template) == 0) {
                     throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "保存附件失败");
                 }
        	}else{
        		template = new SysSetting();
        		template.setSettingType(type);
        		template.setSettingName("图片打印模板");
        		template.setDefaultValue("");
        		template.setDescription("图片打印模板");
        		template.setDisplayName("图片打印模板");
        		template.setModificationNumber(0);
        		template.setSettingValue(picName);
        		template.setCreatedBy(getLoginUser().getLoginId());
        		template.setCreated(new Date());
        		template.setLastUpdatedBy(getLoginUser().getLoginId());
        		template.setLastUpdated(new Date());
        		template.setModificationNumber(0);
        		if (sysSettingService.insertSelective(template) == 0) {
                    throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "保存附件失败");
                }
        	}
        }
        }catch(BusinessException e){
        	throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, e.getMessage());
        }
      
    }
    /**
     * 保存文件
     *
     * @param
     * @return
     * @throws IOException
     */
    private String saveMultipartPic(MultipartFile attachmentfile,String type) throws IOException {
    	String saveKey = "";
        String savePath  = "";

        try {
                    savePath= TEMPLATE_PATH
                            +  Constant.TEMPLATE_PRINT_PIC
                            + File.separator
                            + type
                            + "."
                            + FileUtil.getFileSuffix(attachmentfile.getOriginalFilename());
            saveKey = fileService.saveFile(
            		attachmentfile.getInputStream(),
                    savePath
                    );
            if (StringUtils.isEmpty(saveKey)) {
                throw new BusinessException(Constant.EXCEPTIONCODE_SYSTEM, "保存附件" + "出错");
            }

        } catch (IOException e) {
            throw new BusinessException(Constant.EXCEPTIONCODE_SYSTEM, "保存附件" + "出错");
        }
        return saveKey;
    }

    /**
     * 保存模板
     *
     * @param mf   模板文件
     * @param type 模板文件
     * @throws IOException
     */
    private void saveTemplates(MultipartFile mf, String type) throws IOException {
        if (mf != null && mf.getSize() != 0) {
            String filename = saveMultipartFile(mf);
            SysSetting template = sysSettingService.queryBySettingType(type);
            if (template != null) {
                //update
                template.setSettingValue(filename);
                template.setCreatedBy(getLoginUser().getLoginId());
                template.setLastUpdatedBy(getLoginUser().getLoginId());
                template.setLastUpdated(new Date());
                template.setModificationNumber(template.getModificationNumber() + 1);

                sysSettingService.updateByPrimaryKey(template);
            } else {
                //insert
                SysSetting ss = buildSysSetting(type, filename);
                ss.setCreatedBy(getLoginUser().getLoginId());
                ss.setCreated(new Date());
                ss.setLastUpdatedBy(getLoginUser().getLoginId());
                ss.setLastUpdated(new Date());
                ss.setModificationNumber(0);

                sysSettingService.insertSelective(ss);
            }
        }
    }

    private SysSetting buildSysSetting(String type, String filename) {
        SysSetting ss = new SysSetting();
        ss.setSettingType(type);
        ss.setSettingName("合同模板");
        ss.setSettingValue(filename);
        ss.setDefaultValue("");
        ss.setDescription("合同模板");
        ss.setDisplayName("合同模板");
        ss.setModificationNumber(0);

        return ss;
    }
    /**
     * 保存模板参数
     *  add by wangxianjun 20151216 迭代9 销售合同付款方式修改
     * @param isStart   是否开启新的买家模板
     * @param type 模板文件
     * @throws IOException
     */
    private void saveParams(String isStart, String type) throws IOException {
        SysSetting template = sysSettingService.queryBySettingType(type);
        if (template != null) {
            //update
            template.setSettingValue(isStart);
            template.setCreatedBy(getLoginUser().getLoginId());
            template.setLastUpdatedBy(getLoginUser().getLoginId());
            template.setLastUpdated(new Date());
            template.setModificationNumber(template.getModificationNumber() + 1);

            sysSettingService.updateByPrimaryKey(template);
        } else {
            //insert
            SysSetting ss = buildSysParamSetting(isStart, type);
            ss.setCreatedBy(getLoginUser().getLoginId());
            ss.setCreated(new Date());
            ss.setLastUpdatedBy(getLoginUser().getLoginId());
            ss.setLastUpdated(new Date());
            ss.setModificationNumber(0);

            sysSettingService.insertSelective(ss);
        }
    }
    private SysSetting buildSysParamSetting(String isStart, String type) {
        SysSetting ss = new SysSetting();
        ss.setSettingType(type);
        ss.setSettingName("合同模板参数");
        ss.setSettingValue(isStart);
        ss.setDefaultValue("");
        ss.setDescription("合同模板参数");
        ss.setDisplayName("合同模板参数");
        ss.setModificationNumber(0);

        return ss;
    }
    /**
     * 保存文件
     *
     * @param mf
     * @return
     * @throws IOException
     */
    private String saveMultipartFile(MultipartFile mf) throws IOException {
        String fname = mf.getOriginalFilename().replace("\\", "/");
        if (fname.contains("/")) {
            fname = fname.substring(fname.lastIndexOf("/") + 1);
        }
        fname = "template/" + fname;
        String res = fileService.saveFile(mf.getInputStream(), fname);

        logger.info(res);

        return res;
    }

    /**
     * 金额容差设置界面
     * 获取数据库中的settingvalue（买家金额容差额）
     *
     * @param out
     */
    @RequestMapping("toleranceamountsetting.html")
    public void setToleranceAmount(ModelMap out) {
        out.put("sysSetting", sysSettingService.queryBySettingType(SysSettingType.BuyerAllowAmountTolerance.getCode()));
        out.put("sysSettingAfterContractChanged", sysSettingService.queryBySettingType(SysSettingType.BuyerAllowAmountToleranceAfterContractChanged.getCode()));
    }

    /**
     * 更新数据库中的settingvalue（买家金额容差额）
     *
     * @param value
     */
    @ResponseBody
    @RequestMapping(value = "updatetoleranceamountsettingvalue.html", method = RequestMethod.POST)
    public void updateToleranceAmountSettingValue(@RequestParam("settingvalue") String value, @RequestParam("settingValueAfterContractChanged") String settingValueAfterContractChanged) {
        /** 买家金额容差设置 **/
        //获取当前修改次数
        SysSetting sysSettingOld = sysSettingService.queryBySettingType(SysSettingType.BuyerAllowAmountTolerance.getCode());
        int modificationNumberOld = sysSettingOld.getModificationNumber();
        //封装
        SysSetting sysSetting = new SysSetting();
        sysSetting.setSettingValue(value);
        sysSetting.setSettingType(SysSettingType.BuyerAllowAmountTolerance.getCode());
        sysSetting.setLastUpdated(new Date());
        sysSetting.setLastUpdatedBy(getLoginUser().getName());
        sysSetting.setModificationNumber(modificationNumberOld + 1);
        //更新数据
        sysSettingService.updateBySettingTypeSelective(sysSetting);


        /** chengui 合同变更后是否重新关联买家金额容差设置 **/
        SysSetting sysSettingContractOld = sysSettingService.queryBySettingType(SysSettingType.BuyerAllowAmountToleranceAfterContractChanged.getCode());
        int modificationNumberContractOld = sysSettingContractOld.getModificationNumber();
        //封装
        SysSetting sysSettingContract = new SysSetting();
        sysSettingContract.setSettingValue(settingValueAfterContractChanged);
        sysSettingContract.setSettingType(SysSettingType.BuyerAllowAmountToleranceAfterContractChanged.getCode());
        sysSettingContract.setLastUpdated(new Date());
        sysSettingContract.setLastUpdatedBy(getLoginUser().getName());
        sysSettingContract.setModificationNumber(modificationNumberContractOld + 1);
        //更新数据
        sysSettingService.updateBySettingTypeSelective(sysSettingContract);
    }
    /**
     * 配置列表
     */
    @RequestMapping("settinglist.html")
    public void settingList() {
    }

    /**
     * 服务中心快递时间表
     * author wangxianjun
     */
    @RequestMapping("expresstime.html")
    public String expresstime(ModelMap out) {
        List<Organization> orgList = organizationService.queryAllBusinessOrg();
        List<BaseOrganizationDto> baseOrgList = new ArrayList<BaseOrganizationDto>();
        for(Organization org : orgList){
            List<BaseOrganizationDeliverDto> orgDeliverList = expressFirmSetService.selectByOrgId(org.getId());
            BaseOrganizationDto baseOrganizationDto = new BaseOrganizationDto();
            baseOrganizationDto.setId(org.getId());
            baseOrganizationDto.setName(org.getName());
            baseOrganizationDto.setOrgDeliverList(orgDeliverList);
            baseOrgList.add(baseOrganizationDto);
        }
        BaseDeliverQuery query = new BaseDeliverQuery();
        query.setLength(100);
        List<BaseDeliver> baseDeliverList =  expressFirmSetService.findByPrimary(query);
        out.put("baseOrgList", baseOrgList);
        out.put("baseDeliverList", baseDeliverList);
        return "sys/expresstime";
    }
    /**
     * 快递公司设置
     */
    @RequestMapping("expressfirmset.html")
    public void expressfirmset() {
    }

    /**
     * 配置列表
     *
     * @param start       开始索引
     * @param length      长度
     * @param type        类型
     * @param name        名称
     * @param displayName 显示名称
     * @return 配置对象集合
     */
    @ResponseBody
    @RequestMapping(value = "searchsettinglist.html", method = RequestMethod.POST)
    public PageResult searchSettingList(@RequestParam("start") Integer start,
                                        @RequestParam("length") Integer length,
                                        @RequestParam("type") String type,
                                        @RequestParam("name") String name,
                                        @RequestParam(value = "displayName", required = false) String displayName) {
        SysSettingQuery query = new SysSettingQuery();
        query.setStart(start);
        query.setLength(length);
        query.setType(type);
        query.setName(name);
        query.setDisplayName(displayName);
        List<SysSetting> list = sysSettingService.selectByParam(query);
        Integer total = sysSettingService.selectByParamTotal(query);
        PageResult result = new PageResult();
        result.setData(list);
        result.setRecordsFiltered(total);
        result.setRecordsTotal(list.size());
        return result;
    }

    /**
     * 编辑配置
     *
     * @param out 输出
     */
    @RequestMapping("{id}/editsetting.html")
    public String editSetting(ModelMap out, @PathVariable("id") Long id) {
        SysSetting sysSetting = sysSettingService.selectByPrimaryKey(id);
        out.put("sysSetting", sysSetting);
        return "sys/editsetting";
    }

    /**
     * 保存编辑的配置
     *
     * @param sysSetting 修改的对象
     */
    @ResponseBody
    @RequestMapping(value ="saveeditsetting.html", method = RequestMethod.POST)
    public Result saveEditSetting(SysSetting sysSetting) {
        if (!permissionLimit.hasPermission(SYS_SETTINGLIST_EDIT)) {
            return new Result("您没有作该操作的权限", false);
        }

        sysSetting.setLastUpdated(new Date());
        sysSetting.setLastUpdatedBy(getLoginUser().getLoginId());
        try
        {
            Integer flag = sysSettingService.updateByPrimaryKeySelective(sysSetting);
            if (flag > 0) {
                return new Result("修改成功！", true);
            } else {
                return new Result("修改失败！", false);
            }
        }
        catch(BusinessException ex){
            return new Result(ex.getMsg(), false);
        }
    }
    
    /**
     * 日报Job系统设置
     * @param out
     * @author lixiang
     */
    
    @RequestMapping("createdate.html")
    public void createdate(ModelMap out){
    	 out.put("sysSetting", sysSettingService.queryByType(SysSettingType.ReportOrgDay.getCode()));
    }
    
    @ResponseBody
    @RequestMapping("update/date.html")
    public Result sysTime(@RequestParam("id") Long id, @RequestParam("settingValue") String settingValue,
    		@RequestParam("reportOrgDay") Integer reportOrgDay) {
    	Result result = new Result();
    	User user = getLoginUser();
    	if (sysSettingService.modificationTime(id, settingValue, reportOrgDay, user)) {
    		result.setData("设置成功!");
    		result.setSuccess(true);
    	} else {
    		result.setData("设置失败!");
    		result.setSuccess(false);
    	}
    	return result;
    }
    
    /****
     * 以下为邮件设置，以及修改
     * @author tuxianming
     */
    
    @RequestMapping("reportmail.html")
    public void reportmail(ModelMap out){
    }
    
    /**
     * 得到一个指定的邮件设置
     * @param id
     * @author tuxianming
     * @return
     */
    @RequestMapping("editreportmail.html")
    public void editReportmail(ModelMap out, String id){
    	
    	//初始化小时间00-23
    	List<String> hours = new ArrayList<String>();
    	for(int i=0; i<24; i++){
    		String hour = i+"";
    		if(i<10)
    			hour = "0"+hour;
    		hours.add(hour);
    	}
    	out.put("hours", hours);
    	
    	//初始化分钟
    	List<String> minutes = new ArrayList<String>();
    	for(int i=0; i<60; i++){
    		String minute = i+"";
    		if(i<10)
    			minute = "0"+minute;
    		minutes.add(minute);
    	}
    	out.put("minutes", minutes);
    	
    	//初始化附件
    	List<ReportMailAttachmentType> attachmentTypes = new ArrayList<ReportMailAttachmentType>();
    	attachmentTypes.add(ReportMailAttachmentType.DAILY);
    	attachmentTypes.add(ReportMailAttachmentType.MONTHLY);
    	out.put("attachmentTypes", attachmentTypes);
    	
    	if(StringUtils.isNotBlank(id)){
    		ReportMailSettingDto setting = reportMailSettingService.selectByKey(Long.parseLong(id));
        	
        	out.put("setting", setting);
        	String[] times = setting.getSendTime().split(":");
        	setting.setHour(times[0]);
        	setting.setMinute(times[1]);
        	
        	if(setting.getWeeks()!=null){
        		out.put("Monday", setting.getWeeks().contains("2")); 
        		out.put("Tuesday", setting.getWeeks().contains("3")); 
        		out.put("Wednesday", setting.getWeeks().contains("4")); 
        		out.put("Thursday", setting.getWeeks().contains("5")); 
        		out.put("Friday", setting.getWeeks().contains("6")); 
        		out.put("Saturday", setting.getWeeks().contains("7")); 
        		out.put("Sunday", setting.getWeeks().contains("1")); 
        	}

    	}else{
    		out.put("model", "create");
    	}
    }
    
    
    /**
     * 返回所有的邮件记录
     * @author tuxianming
     */
    @ResponseBody
    @RequestMapping("reportmaillist.html")
    public PageResult loadReportmail(ReportMailSettingDto dto,
			@RequestParam("start") Integer start,
			@RequestParam("length") Integer length){
    	
    	dto.setLength(length);
    	dto.setStart(start);
    	List<ReportMailSettingDto> reportmailSettings = reportMailSettingService.selectByParam(dto);
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	reportmailSettings = reportmailSettings.stream()
    			.map(setting -> {
    				setting.setPeriod(buildPeriod(setting));
    				setting.setCreateTimeStr(sdf.format(setting.getCreated()));
    				return setting;
    			}).collect(Collectors.toList());
    	
    	PageResult result = new PageResult();
    	int total = reportMailSettingService.selectByParamTotal(dto);
    	result.setRecordsFiltered(total);
		result.setRecordsTotal(reportmailSettings.size());
    	result.setData(reportmailSettings);
    	return result;
    }
    
    /**
     * 修改或添加一个email设置
     * @param dto
     * @return
     * @author tuxianming
     */
    @ResponseBody
    @RequestMapping("updatereportmail")
    public Result udpateReportmail(ReportMailSettingDto dto){
    	//add
    	try {
	    	if(dto.getId()==null){
	    		dto.setCreatedBy(this.getLoginUser().getName());
	    		Date date = new Date();
	    		dto.setCreated(date);
	    		dto.setLastUpdated(date);
	    		dto.setLastUpdatedBy(this.getLoginUser().getName());
	    		dto.setModificationNumber(0);
	    		
	    		reportMailSettingService.add(dto);
	    		
	    	}else{
	    		//update
	    		if(ReportMailPeriodType.MONTH1.equals(dto.getPeriodType())){
	    			dto.setWeeks("");
	    		}
	    		
	    		ReportMailSettingDto setting = reportMailSettingService.selectByKey(dto.getId());
	    		
	    		Date date = new Date();
	    		dto.setLastUpdated(date);
	    		dto.setLastUpdatedBy(this.getLoginUser().getName());
	    		dto.setModificationNumber(setting.getModificationNumber()+1);
	    		reportMailSettingService.update(dto);
	    	}
    	} catch (BusinessException e) {
			return new Result(e.getMessage(), false);
		}
    	return new Result();
    }
    
    /**
     * 删除email设置
     * @param id
     * @return
     * @author tuxianming
     */
    @ResponseBody
    @RequestMapping("deletereportmail")
    public Result deleteReportmail(@RequestParam("id")Long id){
    	try {
    		reportMailSettingService.delete(id);
    		return new Result();
		} catch (BusinessException e) {
			return new Result(e.getMessage(), false); 
		}
    }
 
    /**
     * @param dto
     * @return
     * @author tuxianming
     */
    private String buildPeriod(ReportMailSettingDto dto){
    	String period = "";
    	if(dto.getPeriodType().equals(ReportMailPeriodType.WEEK)){
    		period = "每周的星期："+buildWeeks(dto.getWeeks());
    	}else if(dto.getPeriodType().equals(ReportMailPeriodType.MONTH1)){
    		period = "每"+dto.getMonthDuration()+"个月的第："+dto.getDay()+"天";
    	}else {
    		period = "每"+dto.getMonthDuration()+"个月的第"+dto.getWeekDuration()+buildWeeks(dto.getWeeks()+"");
    	}
    	dto.setPeriod(period);
    	return period;
    }
    
    /**
     * 用于前台显示：1-7表示：日-六
     * @author tuxianming
     * @return
     */
    private String buildWeeks(String weeks){
    	String show = "星期";
    	if(StringUtils.isNotBlank(weeks)){
    		String[] weekss = weeks.split(",");
    		for(int i=0; i<weekss.length; i++){
    			String week = weekss[i];
    			if(week.equals("1")){
					show += "日"; 
				}else if(week.equals("2")){
					show += "一 ";
				}else if(week.equals("3")){
					show += "二 ";
				}else if(week.equals("4")){
					show += "三 ";
				}else if(week.equals("5")){
					show += "四 ";
				}else if(week.equals("6")){
					show += "五 ";
				}else if(week.equals("7")){
					show += "六 ";
				}
    			if((i+1)<weekss.length){
    				show+="， ";
    			}
    		}
    	}
    	return show;
    }

    /**
     * 智能匹配重量容差报警设置
     *
     */
    @RequestMapping(value = "smartweightwarningsetting.html")
    public void smartweightwarningsetting(ModelMap map) {
        String[] settingArray = sysSettingService.queryBySettingType(SysSettingType.SMART_WEIGHT_WARNING.getCode()).getSettingValue().split("-");
        map.put("least", settingArray[0]);
        map.put("highest", settingArray[1]);
    }

    /**
     * 更新智能匹配重量容差报警设置
     *
     */
    @ResponseBody
    @RequestMapping(value = "setsmartweightwarning.html")
    public Result setsmartweightwarning(String leastvalue, String highestvalue) {
        String settingValue = leastvalue + "-" + highestvalue;
        //获取当前修改次数
        SysSetting sysSettingOld = sysSettingService.queryBySettingType(SysSettingType.SMART_WEIGHT_WARNING.getCode());
        int modificationNumberOld = sysSettingOld.getModificationNumber();
        //封装
        SysSetting sysSetting = new SysSetting();
        sysSetting.setSettingValue(settingValue);
        sysSetting.setSettingType(SysSettingType.SMART_WEIGHT_WARNING.getCode());
        sysSetting.setLastUpdated(new Date());
        sysSetting.setLastUpdatedBy(getLoginUser().getName());
        sysSetting.setModificationNumber(modificationNumberOld + 1);
        //更新数据
        try {
            sysSettingService.updateBySettingTypeSelective(sysSetting);
            return new Result("修改成功！", true);
        } catch (BusinessException ex) {
            return new Result(ex.getMsg(), false);
        }
    }

    /**
     *
     *快递公司列表
     * @param query
     * @return
     */


    @ResponseBody
    @RequestMapping(value = "expressfirmsetlist.html", method = RequestMethod.POST)
    public PageResult expressFirmSetList(BaseDeliverQuery query) {
        List<BaseDeliver> list = expressFirmSetService.findByPrimary(query);
        PageResult result = new PageResult();
        result.setData(list);
        return result;
    }
    /**
     * 删除 快递公司
     *addexpress
     *
     */
    @ResponseBody
    @RequestMapping("{id}/delexpress.html")
    public Result deleteExpress(@PathVariable("id") Long id) {
        Result result = new Result();
        Boolean sysSetting = expressFirmSetService.deleteByPrimaryKey(id);
       if(sysSetting){
           result.setSuccess(true);
           return result;
       }else {
           result.setSuccess(false);
           return result;
       }

    }

    /**
     * 添加 快递公司
     *
     *
     */
    @ResponseBody
    @RequestMapping("addexpress.html")
    public Result addExpress( @PathParam("name") String  name) {
        Result result = new Result();
        if(expressFirmSetService.selectByPrima(name)!=null){
            result.setData("该快递公司已存在！");
            result.setSuccess(false);
            return result;
        }
        BaseDeliver baseDeliver=new BaseDeliver();
        baseDeliver.setName(name);
        baseDeliver.setCreated(new Date());
        baseDeliver.setCreatedBy(getLoginUser().getName());
        baseDeliver.setLastUpdated(new Date());
        baseDeliver.setLastUpdatedBy(getLoginUser().getName());
        boolean sysSetting = expressFirmSetService.insertSelective(baseDeliver);
        if(sysSetting){
            result.setSuccess(true);
            return result;
        }
        return result;
    }
    /**
     * 通过服务中心id查询服务中心对应的快递时间
     *
     *
     */
    @ResponseBody
    @RequestMapping("queryorgdeliver.html")
    public Result queryOrgDeliver(@RequestParam("orgId") Long  orgId) {
        Result result = new Result();
        List<BaseOrganizationDeliverDto> orgDeliverList = expressFirmSetService.selectByOrgId(orgId);
        result.setData(orgDeliverList);
        result.setSuccess(true);
        return result;
    }
    /**
     * 通过服务中心id查询服务中心对应的快递时间
     *
     *
     */
    @ResponseBody
    @RequestMapping("updateorgdeliver.html")
    public Result updateOrgDeliver(@RequestParam("orgId") Long  orgId,@RequestParam("deliDaysId") String  deliDaysId) {
        Result result = new Result();
        User user = getLoginUser();
        Date date = new Date();
        List<BaseOrganizationDeliver> orgDeliverList = new ArrayList<BaseOrganizationDeliver>();
        String[] deiliverIds = deliDaysId.split("\\|");
        try {
            for (String str : deiliverIds) {
                String[] ids = str.split(",");
                BaseOrganizationDeliver baseOrganizationDeliver = new BaseOrganizationDeliver();
                baseOrganizationDeliver.setDeliverId(Long.valueOf(ids[0]));
                baseOrganizationDeliver.setDeliverDays(Integer.valueOf(ids[1]));
                baseOrganizationDeliver.setOrgId(orgId);
                baseOrganizationDeliver.setLastUpdated(date);
                baseOrganizationDeliver.setLastUpdatedBy(user.getLoginId());
                orgDeliverList.add(baseOrganizationDeliver);
            }
            if (expressFirmSetService.batchUpdateOrgDeliver(orgDeliverList) > 0) {
                result.setData("更新服务中心快递时间表成功！");
                result.setSuccess(true);
            } else {
                result.setData("更新服务中心快递时间表失败！");
                result.setSuccess(false);
            }
        }catch (Exception e){
                result.setData("更新服务中心快递时间表失败:" + e.getMessage());
                result.setSuccess(false);
        }

        return result;
    }

    /**
     * 查询所有快递
     *
     *
     */
    @ResponseBody
    @RequestMapping("queryalldeliver.html")
    public Result queryAllDeliver() {
        Result result = new Result();
        BaseDeliverQuery query = new BaseDeliverQuery();
        query.setLength(100);
        List<BaseDeliver> baseDeliverList =  expressFirmSetService.findByPrimary(query);
        result.setData(baseDeliverList);
        result.setSuccess(true);
        return result;
    }


  //迁移风控代码，增加5个字段 add by zhoucai@prcsteel.com 2016-5-6

/**
     * 服务中心目标交易量设置
     */
    @RequestMapping("targetweight.html")
    public void targetWeight(ModelMap out){
        out.put("years", Calendar.getInstance().get(Calendar.YEAR));
    }

    /**
     * 服务中心目标交易量设置
     * @author peanut
     * @date 2016/04/08
     *
     *@param  years 年份
     */
    @ResponseBody
    @RequestMapping("targetweight/search.html")
    public PageResult searchForTargetWeight(String years){
        List<OrganizationTargetWeight> list=targetWeightService.getWeightByYear(years);
        PageResult pr=new PageResult();
        pr.setData(list);
        pr.setRecordsTotal(list.size());
        pr.setRecordsFiltered(list.size());
        return pr;
    }

    /**
     * 服务中心目标交易量更新
     * @author peanut
     * @date 2016/04/11
     *@param  list
     */
    @ResponseBody
    @RequestMapping("targetweight/updateweight.html")
    public Result updateWeight(@RequestBody List<TargetWeightForUpdateDto> list){
        return new Result(targetWeightService.doBatchUpdateWeight(list)>0?"更新成功":false);
    }

    /**
     * 设置买家银票支付方式：1（开启）；0（关闭）
     * @param enabled
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "template/enabled", method = RequestMethod.POST)
    public Result setEnabled(Long id, Boolean enabled){
    	return restTemplateApiInvoker.enabled(id, enabled);
    }
    
    /**
     * 发布系统默认合同、协议模板
     * @param id
     * @param type
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "template/release", method = RequestMethod.POST)
    public Result releaseTemplate(Long id, String type){
    	return restTemplateApiInvoker.release(id, type);
    }

    /**
     * 不发布编辑提交的合同、协议模板
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "template/doNotRelease", method = RequestMethod.POST)
    public Result doNotReleaseTemplate(Long id){
        return restTemplateApiInvoker.doNotRelease(id);
    }

    /**
     * 提交协议
     * @param param
     * @return
     */
    @RequestMapping("agreement/save")
    @ResponseBody
    public Result submitConsign(AccountContractTemplate param){
        Result result = new Result();
        try{
            param.setSysTemplateStatus(0);
            return restTemplateApiInvoker.save(param);
        }catch (BusinessException b){
            result.setData(b.getMsg());
            result.setSuccess(Boolean.FALSE);
            logger.debug(b.getMsg(),b);
        }catch (Exception e){
            result.setData("提交失败");
            result.setSuccess(Boolean.FALSE);
            logger.debug("提交失败", e);
        }
        return result;
    }
    /**
     * 凭证打印图片上传
     * modify by wangxianjun 20160615
     *
     * @return
     */
    @RequestMapping("template/uploadPic")
    @ResponseBody
    public Result uploadPic(MultipartHttpServletRequest req) {
        Result result = new Result();
        try {
            MultipartFile mf = req.getFile("file");
            checkUploadAttachment(mf);//检查图片格式
            updatePic(mf, Constant.TEMPLATE_PRINT_PIC);
            result.setData(sysSettingService.queryBySettingType(Constant.TEMPLATE_PRINT_PIC));
            result.setSuccess(true);
        } catch (BusinessException e) {
            result.setData(e.getMsg());
            result.setSuccess(false);
        }
        return result;
    }
}

