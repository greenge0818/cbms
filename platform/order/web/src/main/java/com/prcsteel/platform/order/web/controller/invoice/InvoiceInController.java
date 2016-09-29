package com.prcsteel.platform.order.web.controller.invoice;

import com.prcsteel.platform.account.model.dto.AccountDto;
import com.prcsteel.platform.account.model.dto.DepartmentDto;
import com.prcsteel.platform.account.model.model.Account;
import com.prcsteel.platform.account.service.AccountService;
import com.prcsteel.platform.acl.model.model.*;
import com.prcsteel.platform.acl.model.query.BaseDeliverQuery;
import com.prcsteel.platform.acl.model.query.SysSettingQuery;
import com.prcsteel.platform.acl.service.ExpressFirmSetService;
import com.prcsteel.platform.acl.service.OrganizationService;
import com.prcsteel.platform.acl.service.SysSettingService;
import com.prcsteel.platform.api.RestCategoryService;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.constants.Constant.ControlPinSettings;
import com.prcsteel.platform.common.enums.OpType;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.common.service.OpLog;
import com.prcsteel.platform.common.service.OpParam;
import com.prcsteel.platform.common.utils.DateUtil;
import com.prcsteel.platform.common.utils.PageData;
import com.prcsteel.platform.common.utils.Tools;
import com.prcsteel.platform.common.vo.PageResult;
import com.prcsteel.platform.common.vo.Result;
import com.prcsteel.platform.core.model.dto.CategoryGroupDto;
import com.prcsteel.platform.core.model.model.CategoryAlias;
import com.prcsteel.platform.core.model.model.CategoryGroup;
import com.prcsteel.platform.core.service.CategoryGroupService;
import com.prcsteel.platform.order.model.dto.*;
import com.prcsteel.platform.order.model.enums.AllowanceStatus;
import com.prcsteel.platform.order.model.enums.AllowanceType;
import com.prcsteel.platform.order.model.enums.InvoiceInDetailStatus;
import com.prcsteel.platform.order.model.enums.InvoiceInStatus;
import com.prcsteel.platform.order.model.model.*;
import com.prcsteel.platform.order.model.query.AccountInvoiceNoPassQuery;
import com.prcsteel.platform.order.model.query.AllowanceDetailItemQuery;
import com.prcsteel.platform.order.model.query.InvoiceDetailQuery;
import com.prcsteel.platform.order.model.query.OrderItemsForInvoiceInQuery;
import com.prcsteel.platform.order.service.SysConfigService;
import com.prcsteel.platform.order.service.allowance.AllowanceOrderDetailItemService;
import com.prcsteel.platform.order.service.invoice.*;
import com.prcsteel.platform.order.service.order.ConsignOrderItemsService;
import com.prcsteel.platform.order.web.controller.BaseController;
import com.prcsteel.platform.order.web.support.ObjectExcelView;
import com.prcsteel.platform.order.web.support.ShiroVelocity;
import com.prcsteel.platform.order.web.vo.AdvancedResult;
import com.prcsteel.platform.order.web.vo.InvoiceInDetailVo;
import com.prcsteel.platform.order.web.vo.SaveInvoiceInVo;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 进项票管理
 * Created by lcw on 2015/8/1.
 */
@Controller
@RequestMapping("/invoice/in/")
public class InvoiceInController extends BaseController {
	
	private static final Logger logger = LoggerFactory.getLogger(InvoiceInController.class);
	
    @Autowired
    PoolInService poolInService;
    @Autowired
    PoolInDetailService poolInDetailService;
    @Autowired
    CategoryGroupService categoryGroupService;
    @Autowired
    InvoiceInService invoiceInService;
    @Autowired
    InvoiceInDetailService invoiceInDetailService;
    @Autowired
    ExpressService expressService;
    @Resource
    ConsignOrderItemsService consignOrderItemsService;
    @Resource
    SysConfigService sysConfigService;
    @Resource
    SysSettingService sysSettingService;
    @Resource
    AllowanceOrderDetailItemService allowanceDetailItemService;
    @Resource
    InvoiceInAllowanceService invoiceInAllowanceService;
    @Resource
    InvoiceInAllowanceItemService invoiceInAllowanceItemService;
    @Resource
    AccountService accountService;
    @Resource
    OrganizationService organizationService;
    @Resource
    InvoiceInProcessService invoiceInProcessService;
    @Resource
    InvoiceInFlowLogService invoiceInFlowLogService;
    @Resource
    ExpressFirmSetService expressFirmSetService;
    @Resource
    RestCategoryService restCategoryService;
    
    ShiroVelocity permissionLimit = new ShiroVelocity();

//    private static final String PERMISSION_INVOICEINBILLINPUT = "invoice:in:inputinvoice:submitInvoice";            // 录入发票信息
//    private static final String PERMISSION_INVOICEINBILLINPUT2 = "invoice:in:inputinvoice:2submitInvoice";          // 录入发票信息

    private static final String PERMISSION_INVOICEINSEND = "invoice:in:send:input";                 // 登记快递单号
    private static final String PERMISSION_INVOICEINPRINT = "invoice:in:confirm:print";             // 打印快递单号
    private static final String PERMISSION_INVOICEINCONFIRM = "invoice:in:inputinvoice:confirm:page";         // 确认发票信息
    private final String PERMISSION_INVOICEIN_AUTHENTICATION = "invoice:in:authentication:inauthentication";            	// 是否通过认证
    private final String PERMISSION_INVOICEIN_AUTHENTICATIONED = "invoice:in:authenticationed:outauthentication";           // 取消认证
    private final String PERMISSION_INVOICEIN_CANCEL = "invoice:in:cancel:outcancel";
    private final String PERMISSION_INVOICEIN_DELETE = "invoice:in:cancel:delete";
    private static final String AUDIT_PASS = "pass";
    private static final String AUDIT_REFUSE = "refuse";
    private static final String AUDIT_ROLLBACK = "rollback";
    private static final String FROM_CONFIRM_PAGE = "confirm";
    
    public static final Map<String, String> map = new HashMap<String, String>();
    
    public static final Map<String, String> getInvoiceInFlowLogMap() {
		if (map.size() <= 0) {
			map.put("RECEIVED", "已录入");
			map.put("SENT", "已寄出");
			map.put("WAIT", "已确认");
			map.put("ALREADY", "已认证");
			map.put("ROLLBACK", "已打回待关联");
		}
		return map;
	}

    /**
     * 待收票（按公司查询）
     */
    @RequestMapping("awaits")
    public void awaits(ModelMap out) {
        getShouldTotal(out);
        getTabTotal(out, InvoiceInStatus.AWAITS);
        // 当前登录人为交易员角色时，前台交易员显示框隐藏
        out.put("hidden", permissionLimit.hasPermission(Constant.PERMISSION_TRADER_HIDDEN));
    }

    /**
     * 查询待收票数据（按公司查询）
     */
    @ResponseBody
    @RequestMapping(value = "searchbycompany.html", method = RequestMethod.POST)
    public PageResult searchBycompany(
            @RequestParam("start") Integer start,
            @RequestParam("length") Integer length,
            @RequestParam("ownerName") String ownerName,
            @RequestParam("sellerName") String sellerName) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("start", start);
        paramMap.put("length", length);
        paramMap.put("ownerName", ownerName);
        paramMap.put("sellerName", sellerName);
        paramMap.put("ownerIdList", getUserIds());
        paramMap.put("orgIdList", getOrgIds());
        List<PoolIn> list = poolInService.queryByCompany(paramMap);
        Integer total = poolInService.queryTotalByCompany(paramMap);
        PageResult result = new PageResult();
        result.setData(list);
        result.setRecordsFiltered(total);
        result.setRecordsTotal(list.size());
        return result;
    }

    /**
     * 待收票（按品类查询）
     */
    @RequestMapping("nsort")
    public void nsort(ModelMap out) {
        String parentUuid = "0";    // 查找所有大类
        List<CategoryGroup> list = categoryGroupService.queryByParentUuid(parentUuid);
        out.put("list", list);
        getShouldTotal(out);
        getTabTotal(out, InvoiceInStatus.AWAITS);
    }

    /**
     * 查询待收票数据（按品类查询）
     */
    @ResponseBody
    @RequestMapping(value = "searchbynsort.html", method = RequestMethod.POST)
    public PageResult searchByNsort(
            @RequestParam("start") Integer start,
            @RequestParam("length") Integer length,
            @RequestParam("uuid") String uuid) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("start", start);
        paramMap.put("length", length);
        paramMap.put("uuid", uuid);
        paramMap.put("ownerIdList", getUserIds());
        paramMap.put("orgIdList", getOrgIds());
        List<PoolInDetail> list = poolInDetailService.queryByNsort(paramMap);
        Integer total = poolInDetailService.queryTotalByNsort(paramMap);
        PageResult result = new PageResult();
        result.setData(list);
        result.setRecordsFiltered(total);
        result.setRecordsTotal(list.size());
        return result;
    }

    /**
     * 按公司查询详情
     */
    @RequestMapping("{departmentId}/detailbyseller")
    public String detailBySeller(ModelMap out, @PathVariable("departmentId") Long departmentId) {
    	Account department = accountService.queryById(departmentId);
        Account account = accountService.queryById(department.getParentId());
        InvoiceDetailQuery invoiceDetailQuery = new InvoiceDetailQuery();
        invoiceDetailQuery.setSellerId(department.getParentId());
        invoiceDetailQuery.setDepartmentId(departmentId);
        invoiceDetailQuery.setOwnerIdList(getUserIds());
        invoiceDetailQuery.setOrgIdList(getOrgIds());
        PoolInDetailDto poolInDetailDto = poolInDetailService.queryCombined(invoiceDetailQuery);
        int departmentTotal = accountService.queryDepartmentByName(department.getParentId());
        out.put("poolInDetailDto", poolInDetailDto);
        out.put("account", account);
        out.put("department", department);
        out.put("departmentTotal", departmentTotal);
        return "/invoice/in/detailbyseller";
    }

    /**
     * 按品名查询详情
     */
    @RequestMapping("detailbynsort")
    public String detailByNsort(ModelMap out, @RequestParam("nsortName") String nsortName,
                                @RequestParam("material") String material,
                                @RequestParam("spec") String spec,@RequestParam("uuid") String uuid) {
		try {
			nsortName = URLDecoder.decode(nsortName,"UTF-8");
			material = URLDecoder.decode(material,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("decode nsortName failed",e);
		}
        if (StringUtils.isNotEmpty(nsortName) && StringUtils.isNotEmpty(material) && StringUtils.isNotEmpty(spec)) {
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("nsortName", nsortName);
            paramMap.put("material", material);
            paramMap.put("spec", spec);
            paramMap.put("uuid", uuid);
            paramMap.put("ownerIdList", getUserIds());
            paramMap.put("orgIdList", getOrgIds());
            PoolInDetailDto detailDto = poolInDetailService.queryStatisDetailByNsort(paramMap);
            out.put("shouldWeight", detailDto.getTotalWeight());
            out.put("shouldAmount", detailDto.getTotalAmount());
            out.put("nsortName", nsortName);
            out.put("material", material);
            out.put("spec", spec);
            out.put("uuid", uuid);
        }
        return "/invoice/in/detailbynsort";
    }

    /**
     * 已打回待关联
     */
    @RequestMapping("returntoberelation")
    public void returnTobeRelation(ModelMap out) {
        getShouldTotal(out);
        getTabTotal(out, InvoiceInStatus.WAIT);
        setDefaultTime(out);
    }
    
    
    
    @ResponseBody
    @RequestMapping("query/total.html")
   	public Result getTotal(InvoiceDetailQuery invoiceDetailQuery) {
		Result result = new Result();
		Date dateStart = null;// 起始时间
		Date dateEnd = null;// 起始时间
		try {
			dateStart = getDateStart(invoiceDetailQuery);
            dateEnd = getDateEnd(invoiceDetailQuery);
        } catch (ParseException e) {
			logger.error(e.toString());
		}
		invoiceDetailQuery.setStartDate(dateStart);
        invoiceDetailQuery.setEndDate(dateEnd);
        invoiceDetailQuery.setOwnerIdList(getUserIds());
        if(StringUtils.isEmpty(invoiceDetailQuery.getOrgName()) || "无".equals(invoiceDetailQuery.getOrgName())){
        	invoiceDetailQuery.setOrgIdList(getOrgIds());
        	invoiceDetailQuery.setOrgName(null);
        }
		PoolInDetailDto poolInDetailDto = poolInDetailService.queryCombined(invoiceDetailQuery);
		result.setData(poolInDetailDto);
       	return result;
   	}
    /**
     * 查询详情
     */
    @ResponseBody
    @RequestMapping(value = "searchdetail.html", method = RequestMethod.POST)
    public PageResult searchDetail(InvoiceDetailQuery invoiceDetailQuery) {
        Date dateStart = null;// 起始时间
        Date dateEnd = null;// 起始时间
        try {
			dateStart = getDateStart(invoiceDetailQuery);
			dateEnd = getDateEnd(invoiceDetailQuery);
		} catch (ParseException e) {
			logger.error(e.toString());
		}
        invoiceDetailQuery.setOwnerIdList(getUserIds());
        invoiceDetailQuery.setStartDate(dateStart);
        invoiceDetailQuery.setEndDate(dateEnd);
        if(StringUtils.isEmpty(invoiceDetailQuery.getOrgName()) || "无".equals(invoiceDetailQuery.getOrgName())){
        	invoiceDetailQuery.setOrgIdList(getOrgIds());
        	invoiceDetailQuery.setOrgName(null);
        }
        List<PoolInDetailDto> list = poolInDetailService.query(invoiceDetailQuery);
        Integer total = poolInDetailService.queryTotal(invoiceDetailQuery);
        PageResult result = new PageResult();
        result.setData(list);
        result.setRecordsFiltered(total);
        result.setRecordsTotal(list.size());
        return result;
    }
    
	/**
	 * 起始日期
	 * 
	 * @param invoiceDetailQuery
	 * @return
	 * @throws ParseException
	 */
	private Date getDateStart(InvoiceDetailQuery invoiceDetailQuery) throws ParseException {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date dateStart = null;
		if (StringUtils.isNotEmpty(invoiceDetailQuery.getStartTime())) {
			dateStart = format.parse(invoiceDetailQuery.getStartTime());
		}
		return dateStart;
	}

	/**
	 * 终止日期
	 * 
	 * @param invoiceDetailQuery
	 * @return
	 * @throws ParseException
	 */
	private Date getDateEnd(InvoiceDetailQuery invoiceDetailQuery) throws ParseException {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date dateEnd = null;
		if (StringUtils.isNotEmpty(invoiceDetailQuery.getEndTime())) {
			dateEnd = new Date(
					format.parse(invoiceDetailQuery.getEndTime()).getTime() + 24 * 3600 * 1000);
		} 
		return dateEnd;
	}

    /**
     * 查询Tab总数
     *
     * @param out 输出
     */
    public void getTabTotal(ModelMap out, InvoiceInStatus invoiceInStatus) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("ownerIdList", getUserIds());
        paramMap.put("orgIdList", getOrgIds());
        // 待收票数量
        Integer receiveTotal = poolInService.queryTotalByCompany(paramMap);

        // 待寄出数量
        paramMap.put("status", InvoiceInStatus.RECEIVED.toString());
        Integer sendTotal = invoiceInService.queryTotal(paramMap);

        // 待确认数量
        paramMap.put("status", InvoiceInStatus.SENT.toString());
        Integer confirmTotal = invoiceInService.queryTotal(paramMap);
        
        // 待认证数量
        paramMap.put("status", InvoiceInStatus.WAIT.toString());
        paramMap.put("relationStatus", InvoiceInDetailStatus.HasRelation.getCode());
        Integer authenticationTotal = invoiceInService.queryTotal(paramMap);

        out.put("receiveTotal", receiveTotal);
        out.put("sendTotal", sendTotal);
        out.put("confirmTotal", confirmTotal);
        out.put("authenticationTotal", authenticationTotal);
        out.put("status", invoiceInStatus != null ? invoiceInStatus.toString() : "ALL");
    }

    /**
     * 查询应收总数
     *
     * @param out 输出
     */
    private void getShouldTotal(ModelMap out) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("ownerIdList", getUserIds());
        paramMap.put("orgIdList", getOrgIds());
        BigDecimal shouldTotalAmount = poolInDetailService.queryShouldTotalAmount(paramMap);
        BigDecimal shouldTotalWeight = poolInDetailService.queryShouldTotalWeight(paramMap);
        out.put("shouldAmount", shouldTotalAmount);
        out.put("shouldWeight", shouldTotalWeight);
    }

    /**
     * 录入发票
     */
    //@OpAction(key="invoiceId", isAjax = false)
    @RequestMapping("{from}/{departmentId}/inputinvoice")
    public String inputInvoice(ModelMap out, 
    		@PathVariable("departmentId") Long departmentId,
            @PathVariable("from") String from,
    		@RequestParam(value="invoiceId",required=false) Long invoiceId) {
        //todo： 新增注释 from
        if (StringUtils.isEmpty(from)) {
            out.put("from", from);
        }

        SysSettingQuery query = new SysSettingQuery();
        Long sellerId = 0L;
        query.setType(Constant.TYPE_OF_SPEC_SETTING_TYPE);
        List<SysSetting> specSignList = sysSettingService.selectByParam(query);
        specSignList.sort((a, b) -> a.getSequence().compareTo(b.getSequence()));
        //TODO: 可加缓存 sysSettingService
        PoolIn poolIn = poolInService.selectByDepartmentId(departmentId);
        if (poolIn != null && poolIn.getSellerId() != null) {
            sellerId = poolIn.getSellerId();
        }
        out.put("poolIn", poolIn);
        out.put("specSignList", specSignList);
        if(invoiceId != null && invoiceId > 0){
        	InvoiceIn invoiceIn = invoiceInService.selectByPrimaryKey(invoiceId);
            sellerId = invoiceIn.getSellerId();
        	if(invoiceIn == null){
        		return "notfound";
        	}

            //如果是来源于待确认页面，且当前状态不是待确认，则跳转至待认证页面
            if (StringUtils.equals(FROM_CONFIRM_PAGE,from) && !StringUtils.equals(InvoiceInStatus.SENT.getCode(),invoiceIn.getStatus())){
                return "redirect:/invoice/in/authentication.html";
            }
            // 查询折让单信息
            InvoiceInAllowance inAllowance = invoiceInAllowanceService.selectByInvoiceInId(invoiceId);
            out.put("inAllowance", inAllowance);

            out.put("invoiceId", invoiceId);
        	out.put("invoiceAreaCode", invoiceIn.getAreaCode());
        	out.put("invoiceIn", invoiceIn);

        }else{
        	//查询最后一张发票
            AccountDto department = accountService.selectByPrimaryKey(departmentId);
            InvoiceIn iv = invoiceInService.selectLastBySellerId(department.getAccount().getParentId());
            if (iv != null) {
                out.put("invoiceAreaCode", iv.getAreaCode());
        	}
        }
        
        //查询品类
        List<CategoryGroupDto> categories = categoryGroupService.queryAllCategoryGroupInner();

        //查询部门
        List<DepartmentDto> departments = accountService.queryDeptByCompanyId(sellerId);

        out.put("categories", categories);
        out.put("departments", departments);
        out.put("allowAmountDeviation", sysConfigService.getDouble(Constant.INVOICE_IN_ALLOW_AMOUNT_DEVIATION));
        out.put("allowWeightDeviation", sysConfigService.getDouble(Constant.INVOICE_IN_ALLOW_WEIGHT_DEVIATION));
        out.put("filterText", sysConfigService.getString(Constant.INVOICE_IN_FILTER_TEXT));
        return "/invoice/in/inputinvoice";
    }
    
    /**
     * 开票流程图
     * @param invoiceId
     * @return
     */
    @RequestMapping("getstepdata.html")
    @ResponseBody
	public  Result getStepData(@RequestParam("invoiceId") Long invoiceId) {
		Result result = new Result();
		if (invoiceId == null) {
			return null;
		}
		InvoiceIn invoiceIn = invoiceInService.selectByPrimaryKey(invoiceId);

		List<InvoiceInProcess> invoiceInProcess = invoiceInProcessService
				.queryByOperatorId(invoiceIn.getInputUserId());
		List<InvoiceInFlowDto> invoiceInFlowDto = invoiceInFlowLogService
				.queryInvoiceInlogById(invoiceId);
		if (invoiceInProcess != null && invoiceInProcess.size() > 0) {
			if (invoiceInFlowDto != null) {
				for (InvoiceInFlowDto dto : invoiceInFlowDto) {
					for (InvoiceInProcess item : invoiceInProcess) {
						if (dto.getInvoiceStatus().equals(item.getInvoiceInStatus())) {
							item.setOperatorName(dto.getOperatorName());
							item.setCreated(dto.getCreated());
							item.setOperatorMobile(dto.getTel());							
							item.setInvoiceInStatusName(getInvoiceInFlowLogMap().get(item.getInvoiceInStatus()));							
							break;
						}
					}
				}
			}
			result.setData(invoiceInProcess);
			result.setSuccess(true);
		} else {
			result.setData("暂无数据");
			result.setSuccess(false);
		}
		return result;
	}
    
    /**
     * 查询进项票详情
     */
    @ResponseBody
    @RequestMapping("detail")
    public Result queryInvoiceInInfo(ModelMap out, @RequestParam("invoiceId") Long invoiceId) {
    	InvoiceIn invoiceIn = invoiceInService.selectByPrimaryKey(invoiceId);
    	List<InvoiceInDetailAndOrdItemDto> list = invoiceInDetailService.selectDetailAndOrdItemByInvId(invoiceId);
    	InvoiceInDetailVo data = new InvoiceInDetailVo();
    	data.setInvoiceIn(invoiceIn);
    	data.setDetails(list);
    	return new Result(data);
    }

    /**
     * 查询进项票折让单详情
     */
    @ResponseBody
    @RequestMapping("allowancedetail")
    public Result queryAllowanceInfo(ModelMap out, @RequestParam("invoiceId") Long invoiceId) {
        List<InvoiceInAllowanceItemDto> inAllowanceItemList = invoiceInAllowanceItemService.selectByInvoiceInId(invoiceId);
        return new Result(inAllowanceItemList);
    }

    /**
     * 保存发票数据
     */
    @ResponseBody
    @RequestMapping(value = "saveinvoice", method = RequestMethod.POST)
    @OpLog(OpType.SaveInvoice)
    @OpParam("invoice")
    @OpParam("opt")
    public Result saveInvoice(SaveInvoiceInVo invoice) {
        Result result = new Result();
//        Long invoiceId = invoice.getInvoiceIn().getId();
//
//        if ((!permissionLimit.hasPermission(PERMISSION_INVOICEINBILLINPUT) && (invoiceId == null) ||
//                (!permissionLimit.hasPermission(PERMISSION_INVOICEINBILLINPUT2) && (invoiceId != null && invoiceId.intValue() > 0)))) {
//            result.setData("您没有作该操作的权限");
//            result.setSuccess(Boolean.FALSE);
//            return result;
//        }

        User user = getLoginUser();
        try{
        	invoiceInService.inputInvoice(user, invoice.getInvoiceIn(), invoice.getInvoiceDetails(),
                    invoice.getInvoiceInAllowance(),invoice.getInAllowanceItems(),invoice.isCheck());
        	result.setSuccess(true);
        }catch(BusinessException e){
        	result.setSuccess(false);
        	result.setData(e.getMsg());
        }
        return result;
    }

    /**
     * 设置默认时间
     */
    private void setDefaultTime(ModelMap out) {
        // 开始时间、结束时间(当前月第一天、当前时间)
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        // 界面默认显示的开单时间
        out.put("startTime", format.format(cal.getTime()));
        out.put("endTime", format.format(new Date()));
    }

    /**
     * 待寄出
     */
    @RequestMapping("send")
    public void send(ModelMap out) {
        getTabTotal(out, InvoiceInStatus.RECEIVED);

        //tuxianming 20160517 添加控制到前台
        List<SysSetting> syss = sysSettingService.getControlPinSettings();
        syss.forEach(setting -> {
            if (ControlPinSettings.with_consume_apply_control.toString().equals(setting.getSettingName())) {
                out.put("ConsumeApplyControlSwitch", setting.getSettingValue());
            }
        });

        List<Organization> orgs = organizationService.queryAllBusinessOrg();
        out.put("orgs", orgs);
        setDefaultTime(out);
    }

    /**
     * 待确认
     */
    @RequestMapping("confirm")
    public void confirm(ModelMap out) {
        getTabTotal(out, InvoiceInStatus.SENT);
        setDefaultTime(out);
    }
    
    /**
	 * 待认证
	 *
	 * @return
	 * @author DQ
	 */
    @RequestMapping("authentication")
    public void authentication(ModelMap out) {
    	getTabTotal(out, InvoiceInStatus.WAIT);
        setDefaultTime(out);
    }

    /**
	 * 认证发票
	 *
	 * @return
	 * @author DQ
	 */
    @ResponseBody
    @RequestMapping(value = "inauthentication", method = RequestMethod.POST)
    @OpLog(OpType.InAuthentication)
    @OpParam("paramsJson")
    public AdvancedResult inAuthentication(@RequestParam("paramsJson") String paramsJson) {
        if (!permissionLimit.hasPermission(PERMISSION_INVOICEIN_AUTHENTICATION)) {
        	return new AdvancedResult(false,"您没有作该操作的权限","");
        }
        String auditType = "";
        Long[] codes = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
        	JsonNode expressNode = mapper.readTree(paramsJson);
        	// 发票Code集合
        	JsonNode idsNode = expressNode.path("invoiceIds");
        	// 是否认证发票 true 认证不通过/ false认证通过
        	auditType = expressNode.path("flag").asText();
        	codes = mapper.readValue(idsNode, Long[].class);
        } catch (IOException e) {
        	logger.error("can't parse request data.",e);
            return new AdvancedResult(false, "无法解析请求内容", "");
        }
        User user = getLoginUser();
        if(AUDIT_PASS.equals(auditType)) {
            try {
                // 发票入池并更新发票状态为已认证
                invoiceInService.authInvoiceAccept(user, Arrays.asList(codes));
            } catch (BusinessException ex) {
                return new AdvancedResult(false, ex.getMsg(), null);
            }
        }else if(AUDIT_REFUSE.equals(auditType)){
            try {
                // 修改认证不通过的发票状态为已作废
                invoiceInService.authInvoiceFailed(user, Arrays.asList(codes));
            } catch (BusinessException ex) {
                logger.info(ex.toString());
                List<Long> alreadyOutList = invoiceInService.checkInvoiceOut(Arrays.asList(codes));
                return new AdvancedResult(false, ex.getMsg(), alreadyOutList);
            }
        }else if(AUDIT_ROLLBACK.equals(auditType)){
            try {
                //发票打回
                invoiceInService.rollbackStatus(user, Arrays.asList(codes));
            } catch (BusinessException ex) {
                logger.info(ex.toString());
                List<Long> alreadyOutList = invoiceInService.checkInvoiceOut(Arrays.asList(codes));
                return new AdvancedResult(false, ex.getMsg(), alreadyOutList);
            }
        }
        return new AdvancedResult();
    }
    
    /**
	 * 已认证
	 *
	 * @return
	 * @author DQ
	 */
    @RequestMapping("authenticationed")
    public void authenticationed(ModelMap out) {
    	getTabTotal(out, InvoiceInStatus.ALREADY);
        setDefaultTime(out);
    }
    
    /**
	 * 取消已认证
	 *
	 * @return
	 * @author DQ
	 */
    @ResponseBody
    @RequestMapping(value = "outauthentication", method = RequestMethod.POST)
    @OpLog(OpType.OutAuthentication)
    @OpParam("idsJson")
    public AdvancedResult outAuthentication(@RequestParam("idsJson") String idsJson) {
        if (!permissionLimit.hasPermission(PERMISSION_INVOICEIN_AUTHENTICATIONED)) {
        	return new AdvancedResult(false,"您没有作该操作的权限","");
        }
        Long[] codes = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
        	JsonNode expressNode = mapper.readTree(idsJson);
        	// 发票Code集合
        	JsonNode idsNode = expressNode.path("invoiceIds");
        	codes = mapper.readValue(idsNode, Long[].class);
        } catch (IOException e) {
        	logger.error("can't parse request data.",e);
            return new AdvancedResult(false,"无法解析请求内容","");
        }
        try {
            invoiceInService.deauthentication(getLoginUser(), Arrays.asList(codes));
        }
        catch (BusinessException ex) {
            logger.info(ex.toString());
            List<Long> alreadyOutList=invoiceInService.checkInvoiceOut(Arrays.asList(codes));
            return new AdvancedResult(false,ex.getMsg(),alreadyOutList);
        }
        return new AdvancedResult();
    }
    
    /**
	 * 已作废
	 *
	 * @return
	 * @author DQ
	 */
    @RequestMapping("cancel")
    public void cancel(ModelMap out) {
    	getTabTotal(out, InvoiceInStatus.CANCEL);
        setDefaultTime(out);
    }

    /**
   	 * 取消作废
   	 * @return
   	 * @author DQ
   	 */
    @ResponseBody
    @RequestMapping(value = "outcancel", method = RequestMethod.POST)
    @OpLog(OpType.OutCancel)
    @OpParam("idsJson")
    public Result outCancel(@RequestParam("idsJson") String idsJson) {
        if (!permissionLimit.hasPermission(PERMISSION_INVOICEIN_CANCEL)) {
        	return new Result("您没有作该操作的权限",false);
        }
        Long[] codes = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
        	JsonNode expressNode = mapper.readTree(idsJson);
        	// 发票Code集合
        	JsonNode idsNode = expressNode.path("invoiceIds");
        	codes = mapper.readValue(idsNode, Long[].class);
        } catch (IOException e) {
        	logger.error("can't parse request data.",e);
            return new Result("无法解析请求内容",false);
        }
        try{
            invoiceInService.restoreInvoice(getLoginUser(), Arrays.asList(codes));
        } catch (BusinessException ex) {
            logger.error(ex.toString());
            return new Result(ex.getMsg(),false);
        }
        return new Result();
    }
    
    /**
   	 * 删除发票
   	 * @return
   	 * @author DQ
   	 */
    @ResponseBody
    @RequestMapping(value = "deleteinvoice", method = RequestMethod.POST)
    @OpLog(OpType.DeleteInvoice)
    @OpParam("idsJson")
    public Result deleteInvoice(@RequestParam("idsJson") String idsJson) {
        if (!permissionLimit.hasPermission(PERMISSION_INVOICEIN_DELETE)) {
        	return new Result("您没有作该操作的权限",false);
        }
        Long[] codes = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
        	JsonNode expressNode = mapper.readTree(idsJson);
        	// 发票Code集合
        	JsonNode idsNode = expressNode.path("invoiceIds");
        	codes = mapper.readValue(idsNode, Long[].class);
        } catch (IOException e) {
        	logger.error("can't parse request data.",e);
            return new Result("无法解析请求内容",false);
        }
        invoiceInService.deleteInvoice(getLoginUser(), Arrays.asList(codes));
        return new Result();
    }

    
    /**
     * 查询发票数据
     */
    @ResponseBody
    @RequestMapping(value = "searchinvoice.html", method = RequestMethod.POST)
    public PageResult searchInvoice(
            @RequestParam("start") Integer start,
            @RequestParam("length") Integer length,
            @RequestParam("status") String status,
            @RequestParam("sellerName") String sellerName,
            @RequestParam(value = "orgid", required = false) Long orgId,
            @RequestParam(value = "uid", required = false) Long uid,
            @RequestParam(value = "expressName", required = false) String expressName,
            @RequestParam(value = "relationStatus", required = false) String relationStatus,
            @RequestParam(value = "invoiceCode", required = false) String invoiceCode,
            @RequestParam(value = "startTime", required = false) String startTime,
            @RequestParam(value = "endTime", required = false) String endTime,
            @RequestParam(value = "sendStartTime", required = false) String sendStartTime,
            @RequestParam(value = "sendEndTime", required = false) String sendEndTime,
            @RequestParam(value = "selectsend", required = false) String selectsend,
            @RequestParam(value = "orgIds", required = false) String orgIds,
            @RequestParam(value = "userName", required = false) String userName,
            @RequestParam(value = "inputUserId", required = false) Long inputUserId) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("start", start);//  sellerName  endTime status startTime
        paramMap.put("length", length);
        if (orgId!=null && orgId > 0) {
            paramMap.put("orgid", orgId);
        }
        if (uid!=null && uid > 0) {
            paramMap.put("uid", uid);
        }
        paramMap.put("sellerName", (sellerName == null || sellerName.equals("null") || StringUtils.isBlank(sellerName))?null:sellerName.trim());
        paramMap.put("expressName", expressName);
        paramMap.put("code", (invoiceCode ==null || invoiceCode.equals("null") || StringUtils.isBlank(invoiceCode))?null:invoiceCode.trim());
        paramMap.put("startTime", startTime);
        paramMap.put("endTime", endTime);
        paramMap.put("sendStartTime", sendStartTime);
        paramMap.put("sendEndTime", sendEndTime);
        paramMap.put("status", status);
        paramMap.put("ownerIdList", getUserIds());
    	paramMap.put("relationStatus", relationStatus);
        paramMap.put("inputUserId", inputUserId);

        List<String> orgIdArr = null;
        if(org.apache.commons.lang.StringUtils.isNotBlank(orgIds)){
            orgIdArr = Arrays.asList(orgIds.split(","));
        }
        paramMap.put("orgIds", orgIdArr);
        paramMap.put("inputUserName", userName);

        List<InvoiceInDto> list=null;
        if(selectsend==null||selectsend.trim().equals("")){
            list = invoiceInService.query(paramMap);
        } else {
            list = invoiceInService.query(paramMap).stream().filter(a -> a.getInvoiceIsSend().equals(selectsend.equals("nosend")?"不可寄出":"可寄出")).collect(Collectors.toList());
        }

        //List<InvoiceInDto> list = invoiceInService.query(paramMap);
        Integer total = invoiceInService.queryTotal(paramMap);

        PageResult result = new PageResult();
        result.setData(list);
        result.setRecordsFiltered(total);
        result.setRecordsTotal(list.size());
        return result;
    }

    /**
     *  进项票 待寄出and 待确认 Excel 导出
     * @param status
     * @param whichName 待寄出页面传 是否寄出类型 ， 待确认页面传值， 确认是导出“待确认”Excel表 还是“待寄出”Excel表
     * @param endTime
     * @param startTime
     * @param codeIds
     * @return
     *   wangxiao
     */
    @RequestMapping(value ="/exportexcel.html", method =RequestMethod.POST )
    @ResponseBody
    public ModelAndView exportExcel(@RequestParam("status") String status,
                                    @RequestParam(value = "whichName", required = false)String whichName,
                                    @RequestParam("endTime") String endTime,
                                    @RequestParam("startTime") String startTime,
                                    @RequestParam("codeIds") String codeIds ){
        Map<String, Object> paramMap = new HashMap<String, Object>();

        List<Long> ids = null;
        if(!StringUtils.isBlank(codeIds)){
            ids = new ArrayList<Long>();
            String[] idsArr = codeIds.split(",");
            for (int i = 0; i < idsArr.length; i++) {
                ids.add(Long.parseLong(idsArr[i]));
            }
        }
        paramMap.put("status", status);
       // paramMap.put("sellerName", (sellerName == null || sellerName.equals("null") || StringUtils.isBlank(sellerName))?null:sellerName.trim());
        paramMap.put(whichName.equals("confirm")?"sendStartTime":"startTime", startTime);
        paramMap.put(whichName.equals("confirm")? "sendEndTime" : "endTime", endTime);
//        paramMap.put("startTime", startTime);
//        paramMap.put("endTime", endTime);
        paramMap.put("ownerIdList", getUserIds());
        paramMap.put("ids", ids);
        ModelAndView mv = null;
        try {
            List<InvoiceInDto> list = invoiceInService.query(paramMap);
            if(whichName.equals("nosend")|| whichName.equals("yessend"))
                list = invoiceInService.query(paramMap).stream().filter(a -> a.getInvoiceIsSend().equals(whichName.equals("nosend")?"不可寄出":"可寄出")).collect(Collectors.toList());
            Map<String, Object> dataMap = new HashMap<>();
            List<String> titles = new ArrayList<>();
            titles.add("开票时间");
            titles.add("进项发票号");
            titles.add("卖家全称");
            titles.add("发票金额");
            titles.add("录入人员");
            titles.add("联系电话");
            titles.add(whichName.equals("confirm")?"寄出时间":"录入时间");
            titles.add(whichName.equals("confirm")?"快递单号":"发票状态");
            titles.add(whichName.equals("confirm")?"发票状态":"是否可寄出");
            dataMap.put("titles", titles);
            List<PageData> varList = new ArrayList<>();
            for (InvoiceInDto invoiceInList : list) {//
                PageData vpd = new PageData();
                vpd.put("var1",  Tools.dateToStr(invoiceInList.getInvoiceDate(),"yyyy-MM-dd"));// 开票时间
                vpd.put("var2", invoiceInList.getCode());//进项发票号
                vpd.put("var3",invoiceInList.getSellerName());//卖家全称
                vpd.put("var4", Tools.formatBigDecimal(invoiceInList.getTotalAmount(), 2));//发票金额
                vpd.put("var5", invoiceInList.getInputUserName());//录入人员
                vpd.put("var6", invoiceInList.getInputUserMobil());//联系电话
                vpd.put("var7", Tools.dateToStr(whichName.equals("confirm") ? invoiceInList.getSendTime():invoiceInList.getCreated(), "yyyy-MM-dd HH:mm:ss"));//录入时间
                vpd.put("var8", whichName.equals("confirm")?invoiceInList.getExpressName():"待寄出");//发票状态
                vpd.put("var9", whichName.equals("confirm")?"待确认":invoiceInList.getInvoiceIsSend());//是否可寄出
                varList.add(vpd);
            }
            dataMap.put("varList", varList);
            ObjectExcelView erv = new ObjectExcelView();//执行excel操作
            mv = new ModelAndView(erv, dataMap);

        }catch (Exception e){
            logger.info("导出EXCEL出错：", e);
        }
        return mv;
    }

    @RequestMapping(value = "invoiceinissend.html", method = RequestMethod.GET)
    public void invoiceinissend (ModelMap out, Long invoiceId) {
    	PageResult result = new PageResult();
    	List<AccountSendDto> list = invoiceInService.invoiceInIsSend(invoiceId);
        result.setData(list);
        out.put("list", list);
    }
    
    /**
     * 登记发票快递单号
     */
    @ResponseBody
    @RequestMapping(value = "checkinexpress", method = RequestMethod.POST)
    @OpLog(OpType.CheckInExpress)
    @OpParam("expressJson")
    public Result checkInExpress(@RequestParam("expressJson") String expressJson) {
        Result result = new Result();
        if (!permissionLimit.hasPermission(PERMISSION_INVOICEINSEND)) {
            result.setData("您没有作该操作的权限");
            result.setSuccess(Boolean.FALSE);
            return result;
        }
        Boolean success = Boolean.FALSE;
        User user = getLoginUser();
        ObjectMapper mapper = new ObjectMapper();
        Express express = new Express();
        try {
            JsonNode expressNode = mapper.readTree(expressJson);
            // 快递单信息
            express.setCompany(expressNode.path("expressCompany").asText());
            express.setExpressName(expressNode.path("expressName").asText());
            // 发票Code集合
            JsonNode idsNode = expressNode.path("invoiceIds");
            Long[] codes = mapper.readValue(idsNode, Long[].class);
            // 登记快递单号
            success = invoiceInService.checkInExpress(user, express, codes);
        }catch (IOException e){

        } catch (BusinessException e){
            result.setSuccess(Boolean.FALSE);
            result.setData(e.getMsg());
            return result;
        }
        result.setSuccess(success);
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "getexpressfate", method = RequestMethod.POST)
    public Result getexpressfate(@RequestParam("expressName") String expressName,
                                 @RequestParam("orgid")Long orgid) {
        Result result = new Result();
        int daye=2;
        try{
            daye=invoiceInService.queryExpressDeliverDays(expressName,orgid);
            result.setData(daye);
            return result;
        }catch (Exception e){
            result.setData(daye);
            return result;
        }
    }

    /**
     * 修改打印状态
     */
    @ResponseBody
    @RequestMapping(value = "updateprintstatus", method = RequestMethod.POST)
    public Result updatePrintStatus(@RequestParam("invoiceIdsJson") String invoiceIdsJson) {
        Result result = new Result();
        if (!permissionLimit.hasPermission(PERMISSION_INVOICEINPRINT)) {
            result.setData("您没有作该操作的权限");
            result.setSuccess(Boolean.FALSE);
            return result;
        }
        Boolean success = Boolean.FALSE;
        User user = getLoginUser();
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode idsNode = mapper.readTree(invoiceIdsJson);
            // 发票Code集合
            Long[] ids = mapper.readValue(idsNode, Long[].class);
            // 更新打印状态
            success = invoiceInService.updatePrintStatus(user, ids);
        } catch (IOException e) {
            // handle error
        }
        result.setSuccess(success);
        return result;
    }

    /**
     * 确认进项发票
     */
    //@OpAction(key="invoiceInId", isAjax = false)
    @RequestMapping("{invoiceInId}/checkinvoicein")
    public String checkInvoiceIn(ModelMap out, @PathVariable("invoiceInId") Long invoiceInId) {
        Boolean success = Boolean.FALSE;
        InvoiceIn invoiceIn = invoiceInService.selectByPrimaryKey(invoiceInId);
        if (invoiceIn != null) {
            out.put("invoiceIn", invoiceIn);
            success = Boolean.TRUE;

            // 获取下一条发票记录
            Express express = expressService.selectByPrimaryKey(invoiceIn.getExpressId());
            Integer start = 0, length = 1;
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("start", start);
            paramMap.put("length", length);
            paramMap.put("notId", invoiceInId);
            paramMap.put("expressName", express.getExpressName());
            paramMap.put("status", invoiceIn.getStatus());
            List<InvoiceInDto> nextList = invoiceInService.query(paramMap);
            InvoiceInDto nextInvoiceIn = new InvoiceInDto();
            if (nextList != null && nextList.size() > 0)
                nextInvoiceIn = nextList.get(0);
            out.put("nextInvoiceIn", nextInvoiceIn);
        }
        out.put("success", success);

        return "/invoice/in/checkinvoicein";
    }

    /**
     * 查询待确认发票数据
     */
    @ResponseBody
    @RequestMapping(value = "checkinvoiceindetail.html", method = RequestMethod.POST)
    public PageResult checkInvoiceInDetail(@RequestParam("invoiceInId") Long invoiceInId) {
        List<InvoiceInDetailDto> list = invoiceInDetailService.selectByInvoiceInId(invoiceInId);
        PageResult result = new PageResult();
        result.setData(list);
        result.setRecordsFiltered(list.size());
        result.setRecordsTotal(list.size());
        return result;
    }

    /**
     * 保存进项发票核对数据
     */
    @ResponseBody
    @RequestMapping(value = "savecheck", method = RequestMethod.POST)
    public Result saveCheck(@RequestParam("invoiceJson") String invoiceJson) {
        Result result = new Result();
        if (!permissionLimit.hasPermission(PERMISSION_INVOICEINCONFIRM)) {
            result.setData("您没有作该操作的权限");
            result.setSuccess(Boolean.FALSE);
            return result;
        }
        Boolean success = Boolean.FALSE;
        User user = getLoginUser();
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode jsonNode = mapper.readTree(invoiceJson);
            // 发票数据
            InvoiceInDto invoiceInDto = jsonToInvoiceInDto(jsonNode);
            // 发票详情数据
            List<InvoiceInDetailDto> list = jsonToInvoiceInDetailDtoList(jsonNode);
            // 核对
            success = invoiceInService.checkInvoice(user, invoiceInDto, list);
        } catch (IOException e) {
            // handle error
        }
        result.setSuccess(success);
        return result;
    }

    /**
     * json转InvoiceInDto
     */
    private InvoiceInDto jsonToInvoiceInDto(JsonNode jsonNode) {
        InvoiceInDto invoiceInDto = new InvoiceInDto();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            invoiceInDto.setCode(jsonNode.path("code").asText());
            invoiceInDto.setId(jsonNode.path("invoiceInId").asLong());
            invoiceInDto.setSellerId(jsonNode.path("sellerId").asLong());
            invoiceInDto.setCheckTotalAmount(new BigDecimal(jsonNode.path("checkTotalAmount").asText()));
            invoiceInDto.setCheckTotalWeight(new BigDecimal(jsonNode.path("checkTotalWeight").asText()));
            invoiceInDto.setInvoiceDate(sdf.parse(jsonNode.path("invoiceDate").asText()));
        } catch (ParseException pex) {
            // handle error
        }
        return invoiceInDto;
    }

    /**
     * json转InvoiceInDetailDto集合
     */
    private List<InvoiceInDetailDto> jsonToInvoiceInDetailDtoList(JsonNode jsonNode) {
        List<InvoiceInDetailDto> list = new ArrayList<InvoiceInDetailDto>();
        JsonNode arrayNode = jsonNode.path("invoiceDetailArray");
        InvoiceInDetailDto item;
        for (JsonNode node : arrayNode) {
            item = new InvoiceInDetailDto();
            item.setId(node.path("id").asLong());
            item.setPoolInId(node.path("poolInId").asLong());
            item.setPoolInDetailId(node.path("poolInDetailId").asLong());
            item.setNsortName(node.path("nsortName").asText());
            item.setMaterial(node.path("material").asText());
            item.setSpec(node.path("spec").asText());
            item.setCheckWeight(new BigDecimal(node.path("checkWeight").asText()));
            item.setCheckNoTaxAmount(new BigDecimal(node.path("checkNoTaxAmount").asText()));
            item.setCheckTaxAmount(new BigDecimal(node.path("checkTaxAmount").asText()));
            item.setCheckAmount(new BigDecimal(node.path("checkAmount").asText()));
            list.add(item);
        }
        return list;
    }

    /**
     * json转InvoiceInAllowance
     */
    private InvoiceInAllowance jsonToInvoiceInAllowance(JsonNode jsonNode) {
        InvoiceInAllowance invoiceInAllowance = new InvoiceInAllowance();
        if (jsonNode.path("invoiceInId") != null) {
            invoiceInAllowance.setInvoiceInId(jsonNode.path("invoiceInId").asLong());
        }
        invoiceInAllowance.setCargoName(jsonNode.path("cargoName").asText());
        invoiceInAllowance.setNoTaxAmount(new BigDecimal(jsonNode.path("noTaxAmount").asText()));
        invoiceInAllowance.setTaxAmount(new BigDecimal(jsonNode.path("taxAmount").asText()));
        invoiceInAllowance.setAmount(invoiceInAllowance.getNoTaxAmount().add(invoiceInAllowance.getNoTaxAmount()));
        return invoiceInAllowance;
    }

    /**
     * json转InvoiceInAllowanceItem集合
     */
    private List<InvoiceInAllowanceItem> jsonToInvoiceInAllowanceItemList(JsonNode jsonNode) {
        List<InvoiceInAllowanceItem> list = new ArrayList<InvoiceInAllowanceItem>();
        JsonNode arrayNode = jsonNode;//.path("invoiceDetailArray");
        InvoiceInAllowanceItem item;
        for (JsonNode node : arrayNode) {
            item = new InvoiceInAllowanceItem();
            //item.setId(node.path("id").asLong());
            item.setAllowanceId(node.path("allowanceId").asLong());
            item.setAllowanceOrderDetailItemId(node.path("allowanceOrderDetailItemId").asLong());
            item.setAllowanceAmount(new BigDecimal(node.path("allowanceAmount").asText()));
            list.add(item);
        }
        return list;
    }

    /**
     * 导出excel
     */
    @RequestMapping(value = "importexcel", method = RequestMethod.POST)
    public ModelAndView importExcel(@RequestParam("invoiceIdsJson") String invoiceIdsJson) {
        ModelAndView mv = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode idsNode = mapper.readTree(invoiceIdsJson);
            // 发票id集合
            Long[] ids = mapper.readValue(idsNode, Long[].class);
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("ids", ids);
            paramMap.put("ownerIdList", getUserIds());
            List<InvoiceInDto> list = invoiceInService.query(paramMap);

            Map<String, Object> dataMap = new HashMap<String, Object>();
            List<String> titles = new ArrayList<String>();
            //titles.add("ID");
            titles.add("进项发票号");
            titles.add("供应商");
            titles.add("发票金额");
            titles.add("录入人员");
            titles.add("联系电话");
            titles.add("寄回总部时间");
            titles.add("确认人员");
            titles.add("确认时间");
            titles.add("发票状态");
            dataMap.put("titles", titles);
            List<PageData> varList = new ArrayList<PageData>();
            for (InvoiceInDto item : list) {
                PageData vpd = new PageData();
                //vpd.put("var1", String.valueOf(item.getId()));
                vpd.put("var1", item.getCode());
                vpd.put("var2", item.getSellerName());
                vpd.put("var3", String.valueOf(item.getTotalAmount()));
                vpd.put("var4", item.getInputUserName());
                vpd.put("var5", item.getInputUserMobil());
                vpd.put("var6", format.format(item.getSendTime()));
                vpd.put("var7", item.getCheckUserName());
                vpd.put("var8", format.format(item.getCheckDate()));
                vpd.put("var9", "已确认");
                varList.add(vpd);
            }
            dataMap.put("varList", varList);
            ObjectExcelView erv = new ObjectExcelView();                //执行excel操作
            mv = new ModelAndView(erv, dataMap);
        } catch (IOException e) {
            // handle error
        }
        return mv;
    }
    
    @ResponseBody
    @RequestMapping(value="relation/unbind",method=RequestMethod.POST)
    public Result removeOrderitemRelation(@RequestParam("invoiceDetailId[]") Long[] invoiceDetailId){
    	invoiceInDetailService.unbindOrderitemByDetailIds(Arrays.asList(invoiceDetailId),getLoginUser());
    	return new Result();
    }
    
    @ResponseBody
    @RequestMapping("assign")
    public Result assignOrderItems(OrderItemsForInvoiceInQuery query,ModelMap model){
    	Result res = new Result();
    	assignOrderItemsParamsCheck(query);
    	query.setOrderEndDate(DateUtil.toEndDate(query.getOrderEndDate()));
    	List<OrderItemsInvoiceInDto> list = consignOrderItemsService.queryOrderItemsForInvoiceIn(query);
    	res.setData(list);
    	return res;
    }
    
    private void assignOrderItemsParamsCheck(OrderItemsForInvoiceInQuery query){
    	if(query == null){
    		throw new BusinessException(Constant.ERROR_MISSING_REQUIRED_PARAM, "订单编号不能为空");
    	}
    	if(query.getWeight() == null){
    		throw new BusinessException(Constant.ERROR_MISSING_REQUIRED_PARAM, "开票重量不能为空");
    	}
    	if(query.getPriceAndTaxAmount() == null){
    		throw new BusinessException(Constant.ERROR_MISSING_REQUIRED_PARAM, "价税合计不能为空");
    	}
    	if(query.getSellerAccountId() == null){
    		throw new BusinessException(Constant.ERROR_MISSING_REQUIRED_PARAM, "卖家编号不能为空");
    	}
    	if(query.getNsortName() == null){
    		throw new BusinessException(Constant.ERROR_MISSING_REQUIRED_PARAM, "品名不能为空");
    	}
    	if(query.getMaterials() == null){
    		throw new BusinessException(Constant.ERROR_MISSING_REQUIRED_PARAM, "材质不能为空");
    	}
    	if(query.getSpec() == null){
    		throw new BusinessException(Constant.ERROR_MISSING_REQUIRED_PARAM, "规格不能为空");
    	}
    }

    /**
     * 全部
     *
     * @param out
     */
    @RequestMapping("index")
    public void index(ModelMap out) {
        getTabTotal(out, null);
        setDefaultTime(out);
    }

    /**
     * 发票详情页（核对进项发票）
     * @param out
     */
    @RequestMapping("{id}/details")
    public String details(ModelMap out, @PathVariable("id") String id) {
        if(StringUtils.isNumeric(id)) {
            InvoiceInDto details = invoiceInService.fetchInvoiceInDetailById(Long.parseLong(id));
            out.put("data", details);
        }

        return "/invoice/in/details";
    }
    
    /**
     * 待认证发票详情页
     * @param out
     */
    @RequestMapping("{id}/certificatedetails")
    public String certificate(ModelMap out, @PathVariable("id") String id) {
        if(StringUtils.isNumeric(id)) {
            InvoiceInDto details = invoiceInService.fetchInvoiceInDetailById(Long.parseLong(id));
            out.put("data", details);
        }
        return "/invoice/in/certificatedetails";
    }

    /**
     * 查询折让单
     */
    @ResponseBody
    @RequestMapping(value = "searchallowance.html", method = RequestMethod.POST)
    public PageResult searchAllowance(AllowanceDetailItemQuery query) {
        if (query == null) {
            throw new BusinessException(Constant.ERROR_MISSING_REQUIRED_PARAM, "查询参数不能为空");
        }
        if (query.getAccountId() == null) {
            throw new BusinessException(Constant.ERROR_MISSING_REQUIRED_PARAM, "卖家编号不能为空");
        }
        query.setAllowanceType(AllowanceType.Seller.getKey());
        query.setListStatus(Arrays.asList(AllowanceStatus.Approved.getKey()));
        query.setAllowanceUnused(Boolean.TRUE);
        List<AllowanceOrderItemsDto> list = allowanceDetailItemService.queryNoInvoiceDetails(query);
        PageResult result = new PageResult();
        result.setData(list);
        result.setRecordsFiltered(list.size());
        result.setRecordsTotal(list.size());
        return result;
    }
    /**
     * 发票预警
     *
     * @return
     * @author wangxianjun
     */
    @RequestMapping("invoicewarn")
    public void warn(ModelMap out) {
        List<Organization> orgList = organizationService.queryAllBusinessOrg();
        out.put("orgList", orgList);
        getTabTotal(out, InvoiceInStatus.WARN);
        //out.put("status", InvoiceInStatus.WARN != null ? InvoiceInStatus.WARN.toString() : "ALL");
    }
    /**
     * 发票预警 查询客户销项票审核未通过原因
     *
     * @return
     * @author wangxianjun
     */
    @RequestMapping("getWarnReason")
    @ResponseBody
    public PageResult getWarnReason(@RequestParam(value = "buyerid", required = false) Long buyerId,
                                @RequestParam(value = "invoiceDataStatus", required = false) String invoiceDataStatus,
                                @RequestParam(value = "orgs", required = false) String orgs,
                                @RequestParam(value = "start", required = false) Integer start,
                                @RequestParam(value = "length", required = false) Integer length) {

        PageResult result = new PageResult();
        AccountInvoiceNoPassQuery query = buildInvoiceNoPassQuery(buyerId, invoiceDataStatus, orgs);
        int total = invoiceInService.queryNoPassReasonCount(query);
        result.setRecordsFiltered(total);
        if (total > 0) {
            if (start != null)
                query.setStart(start);
            else {
                query.setStart(0);
            }
            if (length != null) {
                query.setLength(length);
            } else {
                query.setLength(50);
            }
            List<AccountInvoiceNoPassDto> list = invoiceInService.queryNoPassInvoiceReason(query);
            result.setData(list);
            result.setRecordsTotal(list.size());
        } else {
            result.setData(new ArrayList<>());
            result.setRecordsTotal(0);
        }
        return result;
    }
    private AccountInvoiceNoPassQuery buildInvoiceNoPassQuery(Long buyerId,String invoiceDataStatus, String orgs) {
        AccountInvoiceNoPassQuery query = new AccountInvoiceNoPassQuery();
        User user = getLoginUser();
        Role role = getUserRole();
        List<Long> orgList = new ArrayList<Long>();
        if (buyerId != null) {
            if (buyerId == 0)
                buyerId = -1l;//为0不查全部
            query.setBuyerId(buyerId);
        }
        if(!"".equals(invoiceDataStatus))
            query.setInvoiceStatus(invoiceDataStatus);
        if("Server".equals(role.getRoleType())){
            //内勤只能查看内勤所在服务中心的数据
            orgList.add(user.getOrgId());
        }else{
            //不是内勤可以查看所有服务中心的数据
            if (StringUtils.isNotEmpty(orgs)) {
                String[] multOrgs = orgs.split(",");
                for(String org:multOrgs){
                    orgList.add(Long.valueOf(org));
                }
            }
        }
        query.setOrgList(orgList);
        return query;
    }
    /**
     * 当卖家设置成进项票黑名单，不能提交进项票
     * author wangxianjunn 20160302
     */
    @RequestMapping("checksellersubmit.html")
    @ResponseBody
    public Result checkSellerSumbit(@RequestParam("sellerName") String sellerName) {
        Result result = new Result();
        SysSettingQuery query = new SysSettingQuery();
        query.setType("in_invoice_blacklist");
        query.setValue(sellerName);
        if(!invoiceInService.invoiceIsSubmit(query)){
            result.setSuccess(false);
            result.setData("该卖家已设置成进项票黑名单，不能提交进项票！");
        }else{
            result.setSuccess(true);
        }
        return result;
    }
    
    /**
	 *根据发票名称模糊查询发票列表，已发票日期排序
	 *@auth：zhoucai@prcsteel.com
	 *@date:2016-3-24
	 */
	@RequestMapping(value = "/searchinvoicecodelist", method = RequestMethod.POST)
	public @ResponseBody PageResult searchAccount(@RequestParam("code") String code){			
		PageResult result = new PageResult();
		Integer start = 0;
		Integer length = 10;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("code", code);		
		paramMap.put("start", start);
		paramMap.put("length", length);
        List<InvoiceIn> list = invoiceInService.queryInvoiceCodeList(paramMap);
		Integer total = 0;
		result.setData(list);
		result.setRecordsFiltered(total);
		result.setRecordsTotal(list.size());
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
    
    /**
     * 查询详情(品名,材质,规格)
     */
    @ResponseBody
    @RequestMapping(value = "searchdetailbynsort.html", method = RequestMethod.POST)
    public PageResult searchDetailByNsort(@ModelAttribute InvoiceDetailQuery invoiceDetailQuery,@RequestParam("start") Integer start,
            @RequestParam("length") Integer length) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("nsortName", invoiceDetailQuery.getNsortName());
        paramMap.put("material", invoiceDetailQuery.getMaterial());
        paramMap.put("spec", invoiceDetailQuery.getSpec());
        paramMap.put("uuid", invoiceDetailQuery.getUuid());
        paramMap.put("start", start);
        paramMap.put("length", length);
        paramMap.put("ownerIdList", getUserIds());
        paramMap.put("orgIdList", getOrgIds());
        List<PoolInDetailDto> list = poolInDetailService.queryDetailByNsort(paramMap);
        Integer total = poolInDetailService.queryTotalDetailByNsort(paramMap);
        PageResult result = new PageResult();
        result.setData(list);
        result.setRecordsFiltered(total);
        result.setRecordsTotal(list.size());
        return result;
    }


    /**
     * tuxianming
     * 进项票品名映射设置：根据别名模糊查找所有的品类名
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "loadcategoryaliaslikealias.html", method = RequestMethod.POST)
    public Result loadCategoryaliasLikeAlias(String aliasName) {
        Result result = new Result();
        try{
            List<CategoryAlias> ass = restCategoryService.queryCategoryByAliasName(aliasName);
            result.setData(ass);}
        catch (Exception ex){
            result.setData(ex.getMessage());
            result.setSuccess(false);
            ex.printStackTrace();
        }
        return result;
    }
}
