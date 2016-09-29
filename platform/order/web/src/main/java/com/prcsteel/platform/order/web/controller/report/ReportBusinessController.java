package com.prcsteel.platform.order.web.controller.report;

import com.prcsteel.platform.acl.model.dto.UserOrgsDto;
import com.prcsteel.platform.acl.model.enums.SysSettingType;
import com.prcsteel.platform.acl.model.model.Organization;
import com.prcsteel.platform.acl.model.model.SysSetting;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.acl.service.OrganizationService;
import com.prcsteel.platform.acl.service.SysSettingService;
import com.prcsteel.platform.acl.service.UserOrgService;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.dto.BuyerOrderDetailDto;
import com.prcsteel.platform.common.utils.PageData;
import com.prcsteel.platform.common.utils.Tools;
import com.prcsteel.platform.common.vo.PageResult;
import com.prcsteel.platform.common.vo.Result;
import com.prcsteel.platform.core.model.dto.CategoryGroupDto;
import com.prcsteel.platform.core.service.CategoryGroupService;
import com.prcsteel.platform.order.model.datatable.Column;
import com.prcsteel.platform.order.model.dto.BuyerTradeStatisticsDto;
import com.prcsteel.platform.order.model.dto.ChecklistDetailDto;
import com.prcsteel.platform.order.model.dto.ConsignOrderItemsInfoDto;
import com.prcsteel.platform.order.model.dto.ConsignOrderWithDetailsDto;
import com.prcsteel.platform.order.model.dto.ContractListDto;
import com.prcsteel.platform.order.model.dto.InvoiceInBordereauxDto;
import com.prcsteel.platform.order.model.dto.InvoiceKeepingDto;
import com.prcsteel.platform.order.model.dto.NsortBusinessReportDto;
import com.prcsteel.platform.order.model.dto.OrderItemsDto;
import com.prcsteel.platform.order.model.dto.ReportRebateDto;
import com.prcsteel.platform.order.model.dto.SellerOrderBusinessReportDto;
import com.prcsteel.platform.order.model.dto.SellerTurnoverStatisticsDto;
import com.prcsteel.platform.order.model.dto.TradeStatisticsDetailDto;
import com.prcsteel.platform.order.model.dto.TradeStatisticsWithDetailDto;
import com.prcsteel.platform.order.model.dto.UnSendInvoiceOutDto;
import com.prcsteel.platform.order.model.enums.ConsignOrderStatus;
import com.prcsteel.platform.order.model.enums.InvoiceInBordereauxStatus;
import com.prcsteel.platform.order.model.enums.InvoiceInStatus;
import com.prcsteel.platform.order.model.enums.ReportDayRowName;
import com.prcsteel.platform.order.model.query.BuyerRebateQuery;
import com.prcsteel.platform.order.model.query.BuyerTradeStatisticsQuery;
import com.prcsteel.platform.order.model.query.ChecklistDetailQuery;
import com.prcsteel.platform.order.model.query.ConsignOrderDetailQuery;
import com.prcsteel.platform.order.model.query.InvoiceInBordereauxQuery;
import com.prcsteel.platform.order.model.query.InvoiceKeepingQuery;
import com.prcsteel.platform.order.model.query.OrderItemsQuery;
import com.prcsteel.platform.order.model.query.ReportOrgDayQuery;
import com.prcsteel.platform.order.model.query.TradeStatisticsQuery;
import com.prcsteel.platform.order.service.invoice.InvoiceInService;
import com.prcsteel.platform.order.service.invoice.InvoiceOutCheckListDetailService;
import com.prcsteel.platform.order.service.invoice.InvoiceOutCheckListService;
import com.prcsteel.platform.order.service.order.ConsignOrderItemsService;
import com.prcsteel.platform.order.service.report.ReportBusinessService;
import com.prcsteel.platform.order.service.report.ReportOrgDayService;
import com.prcsteel.platform.order.service.report.ReportPrecipitationFundsService;
import com.prcsteel.platform.order.web.controller.BaseController;
import com.prcsteel.platform.order.web.support.ObjectExcelStyle;
import com.prcsteel.platform.order.web.support.ObjectExcelView;
import com.prcsteel.platform.order.web.support.ShiroVelocity;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.util.HSSFColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author dengxiyan
 * @version V1.0
 * @Title: ReportBusinessController
 * @Package com.prcsteel.cbms.web.controller.report
 * @Description: 业务报表
 * @date 2015/8/19
 */
@Controller
@RequestMapping("/report/business")
public class ReportBusinessController extends BaseController {
	
	private static final Logger logger = LoggerFactory.getLogger(ReportBusinessController.class);
	
	private final int AMOUNT_LIMIT = 2;
	
	static String CHECKOUT_LIST_ALL_ORG = "report:business:invoiceoutcheckoutlistpage:allorg";
	
	@Resource
    ConsignOrderItemsService consignOrderItemsService;
	
    @Resource(name = "reportBusinessService")
    private ReportBusinessService reportBusinessService;

    @Resource
    private ReportOrgDayService reportOrgDayService;

    @Resource
    ReportPrecipitationFundsService reportPrecipitationFundsService;

    @Resource
    SysSettingService sysSettingService;
    
    @Resource
    InvoiceInService invoiceInService;

    @Resource
    CategoryGroupService categoryGroupService;
    
    @Resource
    InvoiceOutCheckListDetailService invoiceOutCheckListDetailService;

    @Resource
    OrganizationService organizationService;
    
    @Resource
    UserOrgService userOrgService;
    
    @Resource
    private InvoiceOutCheckListService checklistService;
    @Resource
    private SysSettingService settingService;
    
    /**
     * 代运营卖家交易报表
     *
     * @param out
     */
    @RequestMapping("/sellerorder")
    public void sellerTradeReport(ModelMap out) {
        setDefaultTime(out);
    }

    private final String ORDERSTATUSPREFIX_PAY = "PAY_";
    private final String ORDERSTATUSPREFIX_FillUP = "FillUP_";
    private final int AMOUNT_DIGIT = 2;
    private final int WEIGHT_DIGIT = 6;
    private final int MAX_RECORD_LENGTH = 10000;
    private final int MAX_RECORDS_LENGTH = 999999;
    
    final String PERMISSION_INVOICEKEEPING = "report:business:invoicekeepingdata:showText"; //进项票记账报表所有数据权限

    /**
     * 加载代运营卖家交易报表数据
     *
     * @param dto
     * @return
     */
    @RequestMapping("/loadsellertradedata")
    @ResponseBody
    public PageResult loadSellerTradeData(SellerOrderBusinessReportDto dto) {
        //设置数据权限条件
        dto.setUserIdList(getUserIds());

        //设置卖家名称模糊查询条件
        if (dto != null && StringUtils.isNotEmpty(dto.getSellerName())) {
            dto.setSellerName("%" + dto.getSellerName().trim() + "%");
        }

        Integer recordsFiltered = reportBusinessService.countSellerTradeByDto(dto);
        List<SellerOrderBusinessReportDto> list = reportBusinessService.querySellerTradeListByDto(dto);
        return buildPageResult(list, recordsFiltered);
    }

    ShiroVelocity permissionLimit = new ShiroVelocity();

    /**
     * 卖家成交统计报表
     *
     * @return
     */
    @RequestMapping("/sellerStatistics")
    public void sellerStatistics(ModelMap out) {
        setDefaultTime(out);
        DateFormat format = new SimpleDateFormat("yyyyMM");
        Date now = new Date();
        Date start = Tools.strToDate(Constant.SYS_START_TIME, Constant.TIME_FORMAT_DAY);    //1900开始算年份？   //2015-8-1
        List<String> months = new ArrayList<>();
        while (!start.after(now)) {
            months.add(0, format.format(start));
            //月份+1
            Calendar temp = Calendar.getInstance();
            temp.setTime(start);
            temp.add(Calendar.MONTH, 1);
            start = temp.getTime();
        }
        out.put("timeList", months);
    }

    @RequestMapping("/sellerStatistics/list.html")
    @ResponseBody
    public PageResult listSellerStatistics(String accountName,
                                           String ownerName,
                                           int organization,
                                           String timeFrom,
                                           String timeTo,
                                           Integer start,
                                           Integer length) {
        Map<String, Object> param = new HashMap<>();
        if (StringUtils.isNotBlank(timeFrom)) {
            param.put("timeFrom", timeFrom);
        }
        if (StringUtils.isNotBlank(timeTo)) {
            param.put("timeTo", timeTo);
        }
        if (StringUtils.isNotBlank(accountName)) {
            param.put("accountName", accountName);
        }

        //设置交易员查询条件：如果屏蔽交易员查询条件，默认当前用户
        if (permissionLimit.hasPermission(Constant.PERMISSION_TRADER_HIDDEN)) {
            ownerName = getLoginUser().getName();
        }

        if (StringUtils.isNotBlank(ownerName)) {
            param.put("ownerName", ownerName);
        }

        //屏蔽服务中心查询条件,默认查询条件值为当前服务中心
        if (permissionLimit.hasPermission(Constant.PERMISSION_ORG_LIMIT)) {
            User user = getLoginUser();
            organization = (user.getOrgId() != null ? user.getOrgId().intValue() : 0);
        }

        if (organization != 0 && organization != -1) {
            param.put("organization", organization);
        }

        param.put("userIds", getUserIds());
        param.put("start", start);
        param.put("length", length);
        List<SellerTurnoverStatisticsDto> list = reportBusinessService.querySellerTurnoverStatisticsByParams(param);
        int count = reportBusinessService.countSellerTurnoverStatisticsByParams(param);
        return buildPageResult(list, count);
    }

    @RequestMapping("/sellerStatistics/output.html")
    @ResponseBody
    public ModelAndView importExcel(String timeFrom,
                                    String timeTo,
                                    String accountName,
                                    String ownerName,
                                    int organization
    ) throws UnsupportedEncodingException {
        ModelAndView mv = null;
        DecimalFormat amountFormat = new DecimalFormat("0.00");// 构造方法的字符格式这里如果小数不足2位,会以0补足.
        DecimalFormat weightFormat = new DecimalFormat("0.0000");// 构造方法的字符格式这里如果小数不足4位,会以0补足.

        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("userIds", getUserIds());
        if (StringUtils.isNotBlank(timeFrom)) {
            paramMap.put("timeFrom", timeFrom);
        }
        if (StringUtils.isNotBlank(timeTo)) {
            paramMap.put("timeTo", timeTo);
        }
        if (StringUtils.isNotBlank(accountName)) {
            paramMap.put("accountName", new String(accountName.getBytes("ISO-8859-1"), "UTF-8"));
        }

        //设置交易员查询条件：如果屏蔽交易员查询条件，默认当前用户
        if (permissionLimit.hasPermission(Constant.PERMISSION_TRADER_HIDDEN)) {
            ownerName = getLoginUser().getName();
            paramMap.put("ownerName", ownerName);
        } else if (StringUtils.isNotBlank(ownerName)) {
            paramMap.put("ownerName", new String(ownerName.getBytes("ISO-8859-1"), "UTF-8"));
        }

        //屏蔽服务中心查询条件,默认查询条件值为当前服务中心
        if (permissionLimit.hasPermission(Constant.PERMISSION_ORG_LIMIT)) {
            User user = getLoginUser();
            organization = (user.getOrgId() != null ? user.getOrgId().intValue() : 0);
        }

        if (organization != 0 && organization != -1) {
            paramMap.put("organization", organization);
        }
        List<SellerTurnoverStatisticsDto> list = reportBusinessService.querySellerTurnoverStatisticsByParams(paramMap);

        Map<String, Object> dataMap = new HashMap<String, Object>();
        List<String> titles = new ArrayList<String>();
        titles.add("时间周期");
        titles.add("卖家全称");
        titles.add("交易员");
        titles.add("卖家类型");
        titles.add("销售重量合计（吨）");
        titles.add("销售金额合计（元）");
        titles.add("当月交易重量占比");
        titles.add("当月交易总笔数");
        titles.add("累计销售重量（吨）");
//        titles.add("本期挂牌次数");
        dataMap.put("titles", titles);
        List<PageData> varList = new ArrayList<PageData>();
        for (SellerTurnoverStatisticsDto item : list) {
            PageData vpd = new PageData();
            vpd.put("var1", timeFrom + "至" + timeTo);
            vpd.put("var2", item.getSellerName());
            vpd.put("var3", item.getOwnerName());
            vpd.put("var4", item.getConsignType().equals("consign") ? "代运营" : item.getConsignType().equals("temp") ? "临采" : "");
            vpd.put("var5", weightFormat.format(item.getWeightRange()));
            vpd.put("var6", amountFormat.format(item.getAmountRange()));
            vpd.put("var7", amountFormat.format(item.getWeightRange().divide(item.getWeightAll(), RoundingMode.HALF_UP).multiply(new BigDecimal(100))) + "%");
            vpd.put("var8", item.getDealCount().toString());
            vpd.put("var9", weightFormat.format(item.getWeightAll()));
            vpd.put("var10", item.getPublishCount() != null ? item.getPublishCount().toString() : String.valueOf(0));
            varList.add(vpd);
        }
        dataMap.put("varList", varList);
        ObjectExcelView erv = new ObjectExcelView();
        mv = new ModelAndView(erv, dataMap);
        return mv;
    }

    /**
     * 品类交易报表界面
     *
     * @param out
     */
    @RequestMapping(value = "/nsort")
    public void nsortTradeReport(ModelMap out, @RequestParam(value = "init", required = false, defaultValue = "true") boolean blInit,
                                 NsortBusinessReportDto dto) {

        //初始进入界面时需设置默认时间为当月
        if (blInit) {
            dto.setStrStartTime(getFirstDayOfMonth());
            dto.setStrEndTime(Tools.dateToStr(new Date(), Constant.TIME_FORMAT_DAY));
        }

        //查询条件:权限userIdList设置
        dto.setUserIdList(getUserIds());

        //屏蔽服务中心查询条件,默认查询条件值为当前服务中心
        if (permissionLimit.hasPermission(Constant.PERMISSION_ORG_LIMIT)) {
            User user = getLoginUser();
            dto.setOrgId(user.getOrgId());
        }

        //各服务中心的品类交易数据
        Map<String, List<NsortBusinessReportDto>> orgMap = reportBusinessService.getNsortTradeListOfOrg(dto);
        out.put("orgMap", orgMap);
        out.put("dto", dto);
    }

    /**
     * 买家采购明细报表
     *
     * @param out
     */
    @RequestMapping(value = "/buyerdetail")
    public void buyerDetail(ModelMap out,
                            @RequestParam(value = "accountId", required = false) Long accountId,
                            @RequestParam(value = "strStartTime", required = false) String strStartTime,
                            @RequestParam(value = "strEndTime", required = false) String strEndTime) {


        BuyerOrderDetailDto dto = new BuyerOrderDetailDto();

        //主界面传来的sellerId必须有值
        if (accountId != null && accountId > 0) {
            dto = reportBusinessService.queryAccountAndManagerInfoByAccountId(accountId);
        }

        //设置从主界面传来的默认条件
        out.put("dto", dto);
        out.put("startTime", strStartTime);
        out.put("endTime", strEndTime);
    }

    /**
     * 加载买家采购明细数据
     *
     * @param dto
     */
    @RequestMapping(value = "/loadbuyerorderdetail")
    @ResponseBody
    public PageResult loadBuyerOrderDetail(BuyerOrderDetailDto dto) {
        List<BuyerOrderDetailDto> list = null;
        int recordsFiltered = 0;

        //设置数据权限条件
        dto.setUserIdList(getUserIds());

        //主界面传来的sellerId必须有值
        if (dto.getSellerId() != null && dto.getSellerId() > 0) {
            recordsFiltered = reportBusinessService.countBuyerOrderDetailByDto(dto);
            if (recordsFiltered > 0) {
                list = reportBusinessService.queryBuyerOrderDetailByDto(dto);
            }
        }
        return buildPageResult(list, recordsFiltered);
    }


    /**
     * 导出买家采购明细数据
     *
     * @param dto
     */
    @RequestMapping(value = "/buyerorderdetailexcel")
    public ModelAndView exportBuyerOrderDetail(BuyerOrderDetailDto dto) {
        List<BuyerOrderDetailDto> list = null;

        //设置数据权限条件
        dto.setUserIdList(getUserIds());

        //主界面传来的sellerId必须有值
        if (dto.getSellerId() != null && dto.getSellerId() > 0) {
            //查询满足条件的所有数据（不分页）
            list = reportBusinessService.queryBuyerOrderDetailByDto(dto);
        }

        //excel表头和数据
        Map<String, Object> dataMap = new HashMap<>();

        //表头
        dataMap.put("titles", buildTitlesList("卖家全称", "开单时间", "代运营交易单号", "买家全称", "品名", "材质", "规格"
                , "单价（元）", "总重量（吨）", "实提总重量（吨）", "总金额（元）", "实提总金额（元）"));

        //数据
        dataMap.put("varList", buildBuyerOrderDetailPageDataList(list));

        ObjectExcelView erv = new ObjectExcelView();
        return new ModelAndView(erv, dataMap);
    }


    private List<PageData> buildBuyerOrderDetailPageDataList(List<BuyerOrderDetailDto> list) {
        List<PageData> varList = new ArrayList<>();
        PageData vpd;
        if (list != null) {
            for (BuyerOrderDetailDto item : list) {
                vpd = new PageData();
                vpd.put("var1", item.getSellerName());
                vpd.put("var2", item.getOrderDateTime());
                vpd.put("var3", item.getOrderNumber());
                vpd.put("var4", item.getBuyerName());
                vpd.put("var5", item.getNsortName());
                vpd.put("var6", item.getMaterial());
                vpd.put("var7", item.getSpec());
                vpd.put("var8", Tools.valueOfBigDecimal(item.getCostPrice()));
                vpd.put("var9", Tools.valueOfBigDecimal(item.getWeight()));
                vpd.put("var10", Tools.valueOfBigDecimal(item.getActualPickWeight()));
                vpd.put("var11", Tools.valueOfBigDecimal(item.getAmount()));
                vpd.put("var12", Tools.valueOfBigDecimal(item.getActualPickAmount()));
                varList.add(vpd);
            }
        }
        return varList;
    }


    private List<String> buildTitlesList(String... titles) {
        List<String> titlesList = new ArrayList<>();
        if (titles != null) {
            for (String title : titles) {
                titlesList.add(title);
            }
        }
        return titlesList;
    }

    private PageResult buildPageResult(List<?> list, Integer recordsFiltered) {
        PageResult result = new PageResult();
        result.setData(list == null ? new ArrayList<>() : list);
        result.setRecordsTotal(list == null ? 0 : list.size());
        result.setRecordsFiltered(recordsFiltered);
        return result;
    }

    /*
     * 设置默认时间段：当前月
     * @param out
     */
    private void setDefaultTime(ModelMap out) {
        // 日期:开始时间、结束时间(当前月第一天、当前时间)
        out.put("startTime", getFirstDayOfMonth());
        out.put("endTime", Tools.dateToStr(new Date(), Constant.TIME_FORMAT_DAY));
    }

    /**
     * 返回当月第一天
     * 格式:yyyy-MM-dd
     *
     * @return String
     */
    private String getFirstDayOfMonth() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return Tools.dateToStr(cal.getTime(), Constant.TIME_FORMAT_DAY);
    }

    /**
     * string类型转date类型
     * controller直接接收data类型会出错
     *
     * @param time
     * @return
     * @throws ParseException
     */
    private Date parseTime(String time) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(Constant.TIME_FORMAT_DAY);
        Date d2 = sdf.parse(time);//将String to Date类型
        return d2;
    }

    /**
     * 交易单明细报表
     *
     * @param out
     * @return
     */
    @RequestMapping("/orderdetails.html")
    public void orderDetails(ModelMap out) {
        setDefaultTime(out);
    }

    /**
     * 交易单明细报表
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/getdetailsdata.html")
    public PageResult getDetailsData(@RequestParam(value = "orgid", required = false) Long orgId,
                                     @RequestParam(value = "uid", required = false) Long uid,
                                     @RequestParam(value = "buyerid", required = false) Long buyerid,
                                     @RequestParam(value = "sellId", required = false) Long sellId,
                                     @RequestParam(value = "secsdate", required = false) String secSdate,
                                     @RequestParam(value = "secedate", required = false) String secEdate,
                                     @RequestParam(value = "sdate", required = false) String sdate,
                                     @RequestParam(value = "edate", required = false) String edate,
                                     @RequestParam(value = "status", required = false) String status,
                                     @RequestParam(value = "start", required = false) Integer start,
                                     @RequestParam(value = "length", required = false) Integer length) {
        PageResult result = new PageResult();
        
        ConsignOrderDetailQuery query = buildOrderDetailQuery(orgId, uid, buyerid, sdate, edate, status,sellId,secSdate,secEdate);
        
        int total = reportBusinessService.queryOrdersCount(query);
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
                query.setLength(15);
            }
            List<ConsignOrderWithDetailsDto> list = reportBusinessService.queryOrderDetailReport(query);
            result.setData(list);
            result.setRecordsTotal(list.size());
        } else {
            result.setData(new ArrayList<>());
            result.setRecordsTotal(0);
        }
        return result;
    }

    @RequestMapping("/orderdetailtoexcel.html")
    @ResponseBody
    public ModelAndView orderdetailtoexcel(@RequestParam(value = "orgid", required = false) Long orgId,
                                           @RequestParam(value = "uid", required = false) Long uid,
                                           @RequestParam(value = "buyerid", required = false) Long buyerid,
                                           @RequestParam(value = "sellId", required = false) Long sellId,
                                           @RequestParam(value = "secsdate", required = false) String secSdate,
                                           @RequestParam(value = "secedate", required = false) String secEdate,
                                           @RequestParam(value = "sdate", required = false) String sdate,
                                           @RequestParam(value = "edate", required = false) String edate,
                                           @RequestParam(value = "status", required = false) String status) {
        ModelAndView mv = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            ConsignOrderDetailQuery query = buildOrderDetailQuery(orgId, uid, buyerid, sdate, edate, status,sellId,secSdate,secEdate);
            query.setLength(MAX_RECORDS_LENGTH);//最大数据条数限制
            List<ConsignOrderWithDetailsDto> list;
            //为解决性能问题，只有输入卖家时才会出现订单明细为空的情况
            //if(query.getSellId() != null) {
                list = reportBusinessService.queryOrderDetailReport(query);
           /* }else{
                list = reportBusinessService.queryOrderDetailReportSecond(query);
            }*/

            Map<String, Object> dataMap = new HashMap<String, Object>();
            List<String> titles = new ArrayList<String>();
            titles.add("时间");
            titles.add("代运营交易单号");
            titles.add("买家全称");
            titles.add("买家交易员");
            titles.add("买家合同号");
            titles.add("卖家合同号");
            titles.add("卖家全称");
            titles.add("卖家交易员");
            titles.add("品名");
            titles.add("规格");
            titles.add("材质");
            titles.add("厂家");
            titles.add("仓库");
            titles.add("销售价(元/吨)");
            titles.add("采购价(元/吨)");
            titles.add("件数");
            titles.add("重量(吨)");
            titles.add("销售金额(元)");
            titles.add("采购金额(元)");
            titles.add("实提重量(吨)");
            titles.add("销售实提金额(元)");
            titles.add("采购实提金额(元)");

            titles.add("买家折扣金额(元)");
            titles.add("卖家折扣金额(元)");
            titles.add("销售结算金额(元)");
            titles.add("采购结算金额(元)");
            titles.add("毛利(元)");
            titles.add("银票支付");
            titles.add("结算时间");
            titles.add("开票时间");
            titles.add("融资订单");
            titles.add("交易状态");

            dataMap.put("titles", titles);
            List<PageData> varList = new ArrayList<PageData>();
            for (ConsignOrderWithDetailsDto item : list) {

                for (ConsignOrderItemsInfoDto subitem : item.getConsignOrderItems()) {
                    PageData vpd = new PageData();
                    vpd.put("var1", format.format(item.getCreated()));
                    vpd.put("var2", item.getCode());
                    vpd.put("var3", item.getAccountName());
                    vpd.put("var4", item.getOwnerName());
                    vpd.put("var5", item.getContractCode());

                    vpd.put("var6", subitem.getContractCodeAuto());
                    vpd.put("var7", subitem.getSellerName());
                    vpd.put("var8", subitem.getSellerTraderName());
                    vpd.put("var9", subitem.getNsortName());
                    vpd.put("var10", subitem.getSpec());
                    vpd.put("var11", subitem.getMaterial());
                    vpd.put("var12", subitem.getFactory());
                    vpd.put("var13", subitem.getWarehouse());
                    vpd.put("var14", Tools.formatBigDecimal(subitem.getDealPrice(), AMOUNT_DIGIT));
                    vpd.put("var15", Tools.formatBigDecimal(subitem.getCostPrice(), AMOUNT_DIGIT));
                    vpd.put("var16", String.valueOf(subitem.getQuantity()));
                    vpd.put("var17", Tools.formatBigDecimal(subitem.getWeight(), WEIGHT_DIGIT));
                    vpd.put("var18", Tools.formatBigDecimal(subitem.getAmount(), AMOUNT_DIGIT));
                    vpd.put("var19", Tools.formatBigDecimal(subitem.getCostAmount(), AMOUNT_DIGIT));
                    vpd.put("var20", Tools.formatBigDecimal(subitem.getActualPickWeightServer(), WEIGHT_DIGIT));
                    vpd.put("var21", Tools.formatBigDecimal(subitem.getActualPickWeightServer().multiply(subitem.getDealPrice()), AMOUNT_DIGIT));
                    vpd.put("var22", Tools.formatBigDecimal(subitem.getActualPickWeightServer().multiply(subitem.getCostPrice()), AMOUNT_DIGIT));


                    vpd.put("var23", Tools.formatBigDecimal(subitem.getAllowanceBuyerAmount(), WEIGHT_DIGIT));
                    vpd.put("var24", Tools.formatBigDecimal(subitem.getAllowanceAmount(), WEIGHT_DIGIT));
                    BigDecimal buyerDiscount = subitem.getActualPickWeightServer().multiply(subitem.getDealPrice()).add(subitem.getAllowanceBuyerAmount());
                    BigDecimal sellerDiscount = subitem.getActualPickWeightServer().multiply(subitem.getCostPrice()).add(subitem.getAllowanceAmount());
                    vpd.put("var25", Tools.formatBigDecimal(buyerDiscount, AMOUNT_DIGIT));
                    vpd.put("var26", Tools.formatBigDecimal(sellerDiscount, AMOUNT_DIGIT));
                    vpd.put("var27", Tools.formatBigDecimal(buyerDiscount.subtract(sellerDiscount), AMOUNT_DIGIT));
                    vpd.put("var28", subitem.getAcceptDraft());
                    vpd.put("var29", subitem.getSettlementTime());
                    vpd.put("var30", subitem.getInvoiceTime());
                    vpd.put("var31", subitem.getFinanceOrder() == 1 ? "是" : "否");
                    vpd.put("var32", item.getStatus() + " " + item.getPayStatus());

                    varList.add(vpd);
                }
            }
            dataMap.put("varList", varList);
            ObjectExcelView erv = new ObjectExcelView();                //执行excel操作
            mv = new ModelAndView(erv, dataMap);
        } catch (Exception e) {
            // handle error
        }
        return mv;
    }

    private ConsignOrderDetailQuery buildOrderDetailQuery(Long orgId, Long uid, Long buyerid, String sdate, String edate, String status,Long sellId,String secSdate,String secEdate) {
        ConsignOrderDetailQuery query = new ConsignOrderDetailQuery();

        User user = getLoginUser();
        if (orgId != null && orgId > 0) {
            query.setOrgId(orgId);
        } else if (orgId == null) {//为空值只显示当前服务中心的数据
            query.setOrgId(user.getOrgId());
        }
        if (uid != null && uid > 0) {
            query.setBuyerTrader(uid);
        } else if (uid == null) { //等于0为显示全部，其它值时只显示当前交易员的数据
            query.setBuyerTrader(user.getId());
        }
        if (buyerid != null) {
            if (buyerid == 0)
                buyerid = -1l;//为0不查全部
            query.setBuyerId(buyerid);
        }
        if (sellId != null) {
            if (sellId == 0)
                sellId = -1l;//为0不查全部
            query.setSellId(sellId);
        }
        if (StringUtils.isNotEmpty(sdate)) {
            query.setBeginTime(Tools.strToDate(sdate, Constant.TIME_FORMAT_DAY));
        }
        if (StringUtils.isNotEmpty(edate)) {
            query.setEndTime(Tools.getAfterDate(Tools.strToDate(edate, Constant.TIME_FORMAT_DAY),1));
        }
        if (StringUtils.isNotEmpty(secSdate)) {
            query.setSecBeginTime(Tools.strToDate(secSdate, Constant.TIME_FORMAT_DAY));
        }
        if (StringUtils.isNotEmpty(secEdate)) {
            query.setSecEndTime(Tools.getAfterDate(Tools.strToDate(secEdate, Constant.TIME_FORMAT_DAY), 1));
        }
        if (StringUtils.isNotEmpty(status)) {
            String[] multStatus = status.split("\\|");
            List<String> statusList = new ArrayList<>();
            List<String> payStatusList = new ArrayList<>();
            List<String> fillupStatusList = new ArrayList<>();
            for(int i=0;i<multStatus.length;i++) {
                if (multStatus[i].startsWith(ORDERSTATUSPREFIX_PAY)) {
                    multStatus[i] = multStatus[i].substring(4);
                    payStatusList.add(multStatus[i]);
                    //modify by wangxianjun 现在交易状态支持多选，不再需要已关联和待二结的状态
                    /*if(!statusList.contains(ConsignOrderStatus.RELATED.getCode()))
                        statusList.add(ConsignOrderStatus.RELATED.getCode());
                    if(!statusList.contains(ConsignOrderStatus.SECONDSETTLE.getCode()))
                        statusList.add(ConsignOrderStatus.SECONDSETTLE.getCode());*/
                } else if (multStatus[i].startsWith(ORDERSTATUSPREFIX_FillUP)) {
                    multStatus[i] = multStatus[i].substring(7);
                    for(String fillStatus :multStatus[i].split(",")){
                        if(!fillupStatusList.contains(fillStatus))
                             fillupStatusList.add(fillStatus);
                    }
                    //modify by wangxianjun 现在交易状态支持多选，不再需要待二结的状态
                    /*if(!statusList.contains(ConsignOrderStatus.RELATED.getCode()))
                        statusList.add(ConsignOrderStatus.RELATED.getCode());*/
                } else {
                    if (StringUtils.isNotEmpty(multStatus[i])) {
                        for(String str: multStatus[i].split(",")){
                            if(!statusList.contains(str))
                                statusList.add(str);
                        }
                    }
                }
            }
            query.setPayStatusList(payStatusList);
            query.setFillUpStatusList(fillupStatusList);
            query.setStatusList(statusList);
        }
        return query;
    }


    /**
     * 买家交易报表
     *
     * @param out
     * @return
     */
    @RequestMapping("/buyerstatistics.html")
    public void buyerStatistics(ModelMap out) {
        setDefaultTime(out);
    }

    /**
     * 买家交易报表数据
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/buyerstatisticsdata.html")
    public PageResult buyerStatisticsData(@RequestParam(value = "orgid", required = false) Long orgId,
                                          @RequestParam(value = "uid", required = false) Long uid,
                                          @RequestParam(value = "buyerid", required = false) Long buyerid,
                                          @RequestParam(value = "sdate", required = true) String sdate,
                                          @RequestParam(value = "edate", required = true) String edate,
                                          @RequestParam(value = "status", required = false) String status,
                                          @RequestParam(value = "start", required = true) Integer start,
                                          @RequestParam(value = "length", required = true) Integer length) {
        PageResult result = new PageResult();
        BuyerTradeStatisticsQuery query = buildBuyerStatisticsQuery(orgId, uid, buyerid, sdate, edate,status);
        int total = reportBusinessService.queryBuyerTradeCount(query);
        result.setRecordsFiltered(total);
        if (total > 0) {
            query.setStart(start);
            query.setLength(length);
            List<BuyerTradeStatisticsDto> list = reportBusinessService.queryBuyerTrade(query);
            result.setData(list);
            result.setRecordsTotal(list.size());
        } else {
            result.setData(new ArrayList<>());
            result.setRecordsTotal(0);
        }
        return result;
    }

    /**
     * 买家交易报表导出excel
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/buyerstatisticstoexcel.html")
    public ModelAndView buyerStatisticsToExcel(@RequestParam(value = "orgid", required = false) Long orgId,
                                               @RequestParam(value = "uid", required = false) Long uid,
                                               @RequestParam(value = "buyerid", required = false) Long buyerid,
                                               @RequestParam(value = "sdate", required = true) String sdate,
                                               @RequestParam(value = "edate", required = true) String edate,
                                               @RequestParam(value = "status", required = false) String status) {
        ModelAndView mv = null;
        BuyerTradeStatisticsQuery query = buildBuyerStatisticsQuery(orgId, uid, buyerid, sdate, edate,status);
        query.setLength(MAX_RECORD_LENGTH);
        try {

            Map<String, Object> dataMap = new HashMap<>();
            List<String> titles = new ArrayList<>();
            titles.add("买家全称");
            titles.add("买家联系人");
            titles.add("代运营交易单笔数");
            titles.add("非代运营交易单笔数");
            titles.add("买家联系人下单总计");
            titles.add("买家联系人下单频率(天/次)");
            titles.add("公司下单总计");
            titles.add("公司下单频率(天/次)");
            titles.add("第一次采购时间");
            titles.add("最近一次采购时间");
            titles.add("首次发生采购");
            dataMap.put("titles", titles);
            List<PageData> varList = new ArrayList<>();
            List<BuyerTradeStatisticsDto> list = reportBusinessService.queryBuyerTrade(query);
            for (BuyerTradeStatisticsDto item : list) {
                PageData vpd = new PageData();
                vpd.put("var1", item.getCompanyName());
                vpd.put("var2", item.getContactName());
                vpd.put("var3", item.getOrderCount().toString());
                vpd.put("var4", item.getTempOrderCount().toString());
                vpd.put("var5", item.getContactOrderCount().toString());
                vpd.put("var6", item.getFrequencyOfContact().toString());
                vpd.put("var7", item.getTotalOrderCount().toString());
                vpd.put("var8", item.getFrequencyOfCompany().toString());
                vpd.put("var9", Tools.dateToStr(item.getFirstTradeTime(), "yyyy-MM-dd"));
                vpd.put("var10", Tools.dateToStr(item.getLastestTradeTime(), "yyyy-MM-dd"));
                vpd.put("var11", item.getIsFirst());
                varList.add(vpd);
            }
            dataMap.put("varList", varList);
            ObjectExcelView erv = new ObjectExcelView();                //执行excel操作
            mv = new ModelAndView(erv, dataMap);
        } catch (Exception e) {
            // handle error
        }
        return mv;
    }

    private BuyerTradeStatisticsQuery buildBuyerStatisticsQuery(Long orgId, Long uid, Long buyerid, String sdate, String edate,String status) {
        BuyerTradeStatisticsQuery query = new BuyerTradeStatisticsQuery();

        User user = getLoginUser();
        if (orgId != null && orgId > 0) {
            query.setOrgId(orgId);
        } else if (orgId == null) {//等于0为显示全部，其它值时只显示当前服务中心的数据
            query.setOrgId(user.getOrgId());
        }
        if (uid != null && uid > 0) {
            query.setBuyerTrader(uid);
        } else if (uid == null) { //等于0为显示全部，其它值时只显示当前交易员的数据
            query.setBuyerTrader(user.getId());
        }
        if (buyerid != null) {
            if (buyerid == 0)
                buyerid = -1l;//为0不查全部
            query.setBuyerId(buyerid);
        }
        if (status != null) {
            query.setStatusCode(status);
        }
        if (StringUtils.isNotEmpty(sdate)) {
            query.setBeginTime(Tools.strToDate(sdate, Constant.TIME_FORMAT_DAY));
        }
        if (StringUtils.isNotEmpty(edate)) {
            query.setEndTime(Tools.strToDate(edate, Constant.TIME_FORMAT_DAY));
        }
        List<String> statusList = new ArrayList<>();
        statusList.add(ConsignOrderStatus.INVOICEREQUEST.getCode());
        statusList.add(ConsignOrderStatus.INVOICE.getCode());
        statusList.add(ConsignOrderStatus.FINISH.getCode());
        query.setStatusList(statusList);  //仅查询二次结算后的数据
        return query;
    }

    /**
     * 买家返利报表页面
     *
     * @param out
     */
    @RequestMapping("/buyerRebate.html")
    public void buyerRebate(ModelMap out) {
        setDefaultTime(out);
    }

    /**
     * 加载买家返利数据
     *
     * @param query
     * @return
     */
    @RequestMapping("/loadbuyerrebatedata.html")
    @ResponseBody
    public PageResult loadBuyerRebateData(BuyerRebateQuery query) {

        //查询条件处理
        setBuyerRebateQuery(query);

        //查询数据
        int recordsFiltered = reportBusinessService.countBuyerRebate(query);
        List buyerRebateList = null;
        if (recordsFiltered > 0) {
            buyerRebateList = reportBusinessService.getBuyerRebateList(query);
        }

        return buildPageResult(buyerRebateList, recordsFiltered);
    }

    /*
     * 服务中心统计报表
     *
     * @param out
     * @return
     */
    @RequestMapping("/tradestatistics.html")
    public void tradeStatistics(ModelMap out) {
        List<String> timeList = new ArrayList<>();
        Calendar now = Calendar.getInstance();
        //系统开始使用月份
        Calendar systemStart = Calendar.getInstance();
        systemStart.set(Calendar.YEAR, 2015);
        systemStart.set(Calendar.MONTH, 4);
        systemStart.set(Calendar.DATE, 1);
        do {
            timeList.add(Tools.dateToStr(now.getTime(), "yyyyMM"));
            now.add(Calendar.MONTH, -1);
        } while (now.after(systemStart));
        out.put("timeList", timeList);
    }

    /**
     * 服务中心统计报表数据
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/tradestatisticsdata.html")
    public PageResult tradeStatisticsData(@RequestParam(value = "timespan", required = true) String timeSpan,
                                          @RequestParam(value = "start", required = true) Integer start,
                                          @RequestParam(value = "length", required = true) Integer length) {
        PageResult result = new PageResult();
        TradeStatisticsQuery query = buildTradeStatisticsQuery(timeSpan);
        int total = reportBusinessService.queryTradeStatisticsCount(query);
        result.setRecordsFiltered(total);
        if (total > 0) {
            query.setStart(start);
            query.setLength(length);
            List<TradeStatisticsWithDetailDto> list = reportBusinessService.queryTradeStatistics(query, false);
            result.setData(list);
            result.setRecordsTotal(list.size());
        } else {
            result.setData(new ArrayList<>());
            result.setRecordsTotal(0);
        }
        return result;
    }

    /**
     * 服务中心统计报表数据
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/tradestatisticsdetaildata.html")
    public Result tradeStatisticsDetailData(@RequestParam(value = "timespan", required = true) String timeSpan,
                                            @RequestParam(value = "orgid", required = true) Long orgid) {
        Result result = new Result();
        TradeStatisticsQuery query = buildTradeStatisticsQuery(timeSpan);
        query.setOrgId(orgid);
        List<TradeStatisticsDetailDto> list = reportBusinessService.queryTradeStatisticsDetail(query);
        result.setData(list);
        result.setSuccess(true);
        return result;
    }

    /**
     * 服务中心统计报表导出excel
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/tradestatisticstoexcel.html")
    public ModelAndView tradeStatisticsToExcel(@RequestParam(value = "timespan", required = true) String timeSpan) {
        ModelAndView mv = null;
        TradeStatisticsQuery query = buildTradeStatisticsQuery(timeSpan);
        query.setLength(MAX_RECORD_LENGTH);
        try {
            Map<String, Object> dataMap = new HashMap<>();
            List<String> titles = new ArrayList<>();
            titles.add("服务中心");
            titles.add("备用金额(元)");
            titles.add("备用金已用额度");
            titles.add("交易员人数");
            titles.add("交易总笔数");
            titles.add("代运营交易笔数占比");
            titles.add("交易总金额(元)");
            titles.add("交易总重量(吨)");
            titles.add("平均每笔交易重量(吨)");
            titles.add("当月采购三次以上买家");
            titles.add("上月采购三次以上买家");
            titles.add("新增买家数");
            titles.add("新增卖家数");
            titles.add("代运营交易卖家数");
            titles.add("代运营卖家交易笔数");
            titles.add("代运营卖家交易总重量(吨)");
            titles.add("非代运营交易卖家数");
            titles.add("非代运营卖家交易笔数");
            titles.add("非代运营卖家交易总重量(吨)");
            dataMap.put("titles", titles);
            List<PageData> varList = new ArrayList<>();
            List<TradeStatisticsWithDetailDto> list = reportBusinessService.queryTradeStatistics(query, true);
            for (TradeStatisticsWithDetailDto item : list) {
                PageData vpd = new PageData();
                vpd.put("var1", item.getOrgName());
                vpd.put("var2", Tools.formatBigDecimal(item.getCreditLimit(), AMOUNT_DIGIT));
                vpd.put("var3", Tools.formatBigDecimal(item.getCreditLimitUsed(), AMOUNT_DIGIT));
                vpd.put("var4", item.getTraderCount().toString());
                vpd.put("var5", item.getOrderCount().toString());
                vpd.put("var6", item.getConsignOrderPercent().toString());
                vpd.put("var7", Tools.formatBigDecimal(item.getOrderTotalAmount(), AMOUNT_DIGIT));
                vpd.put("var8", Tools.formatBigDecimal(item.getOrderTotalWeight(), WEIGHT_DIGIT));
                vpd.put("var9", item.getOrderAvgWeight().toString());
                vpd.put("var10", item.getFrequentTradeCurMonthCount().toString());
                vpd.put("var11", item.getFrequentTradePrevMonthCount().toString());
                vpd.put("var12", item.getBuyerInCreaseCount().toString());
                vpd.put("var13", item.getSellerInCreaseCount().toString());
                vpd.put("var14", item.getTradeSellerCount().toString());
                vpd.put("var15", item.getSellerTradeCount().toString());
                vpd.put("var16", Tools.formatBigDecimal(item.getSellerTradeTotalWeight(), WEIGHT_DIGIT));
                vpd.put("var17", item.getTempTradeSellerCount().toString());
                vpd.put("var18", item.getTempSellerTradeCount().toString());
                vpd.put("var19", Tools.formatBigDecimal(item.getTempSellerTradeTotalWeight(), WEIGHT_DIGIT));
                varList.add(vpd);
                if (item.getTradeStatisticsDetailList() != null) {
                    for (TradeStatisticsDetailDto subitem : item.getTradeStatisticsDetailList()) {
                        vpd.put("var1", "");
                        vpd.put("var2", "");
                        vpd.put("var3", "");
                        vpd.put("var4", subitem.getUserName());
                        vpd.put("var5", subitem.getOrderCount().toString());
                        vpd.put("var6", subitem.getConsignOrderPercent().toString());
                        vpd.put("var7", Tools.formatBigDecimal(subitem.getOrderTotalAmount(), AMOUNT_DIGIT));
                        vpd.put("var8", Tools.formatBigDecimal(subitem.getOrderTotalWeight(), WEIGHT_DIGIT));
                        vpd.put("var9", subitem.getOrderAvgWeight().toString());
                        vpd.put("var10", subitem.getFrequentTradeCurMonthCount().toString());
                        vpd.put("var11", subitem.getFrequentTradePrevMonthCount().toString());
                        vpd.put("var12", subitem.getBuyerInCreaseCount().toString());
                        vpd.put("var13", subitem.getSellerInCreaseCount().toString());
                        vpd.put("var14", subitem.getTradeSellerCount().toString());
                        vpd.put("var15", subitem.getSellerTradeCount().toString());
                        vpd.put("var16", Tools.formatBigDecimal(subitem.getSellerTradeTotalWeight(), WEIGHT_DIGIT));
                        vpd.put("var17", subitem.getTempTradeSellerCount().toString());
                        vpd.put("var18", subitem.getTempSellerTradeCount().toString());
                        vpd.put("var19", Tools.formatBigDecimal(subitem.getTempSellerTradeTotalWeight(), WEIGHT_DIGIT));
                        varList.add(vpd);
                    }
                }
            }
            dataMap.put("varList", varList);
            ObjectExcelView erv = new ObjectExcelView();                //执行excel操作
            mv = new ModelAndView(erv, dataMap);
        } catch (Exception e) {
            // handle error
        }
        return mv;
    }

    private TradeStatisticsQuery buildTradeStatisticsQuery(String curMonth) {
        TradeStatisticsQuery query = new TradeStatisticsQuery();
        query.setCurMonth(curMonth);
        Date cur = Tools.strToDate(curMonth, "yyyyMM");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(cur);
        calendar.add(Calendar.MONTH, -1);
        query.setPrevMonth(Tools.dateToStr(calendar.getTime(), "yyyyMM"));
        if (permissionLimit.hasPermission(Constant.PERMISSION_ORG_LIMIT)) {
            User user = getLoginUser();
            query.setOrgId(user.getOrgId());
        }

        List<String> list = new ArrayList<>();
        list.add(ConsignOrderStatus.INVOICEREQUEST.getCode());
        list.add(ConsignOrderStatus.INVOICE.getCode());
        list.add(ConsignOrderStatus.FINISH.getCode());
        query.setStatus(list);
        return query;
    }

    /**
     * 加载买家的所有联系人返利数据
     *
     * @param query
     * @return
     */
    @RequestMapping("/loadallcontactsrebatedata.html")
    @ResponseBody
    public Result loadAllContactsRebateData(BuyerRebateQuery query) {
        Result result = new Result();
        if (query != null && query.getBuyerId() > 0) {
            List list = reportBusinessService.getAllContactsRebateListOfBuyer(query);
            result.setSuccess(Boolean.TRUE);
            result.setData(list);
        }
        return result;
    }

    /**
     * 导出联系人返利数据
     *
     * @param query
     */
    @RequestMapping(value = "/contactsrebateexcel")
    public ModelAndView exportContactsRebate(BuyerRebateQuery query, @RequestParam("excelTitles") String excelTitles) {

        //查询条件处理
        setBuyerRebateQuery(query);

        //查询满足条件的所有数据（不分页）
        List<ReportRebateDto> list = reportBusinessService.getContactsRebateList(query);

        //excel表头和数据
        Map<String, Object> dataMap = new HashMap<>();

        //表头
        dataMap.put("titles", buildTitlesList(excelTitles));

        //数据
        dataMap.put("varList", buildContactsRebatePageDataList(query, list));

        ObjectExcelView erv = new ObjectExcelView();
        return new ModelAndView(erv, dataMap);
    }

    private List<PageData> buildContactsRebatePageDataList(BuyerRebateQuery query, List<ReportRebateDto> list) {
        List<PageData> varList = new ArrayList<>();
        PageData vpd;
        if (list != null) {
            for (ReportRebateDto item : list) {
                vpd = new PageData();
                vpd.put("var1", StringUtils.trimToEmpty(query.getStrStartTime()) + "至" + StringUtils.trimToEmpty(query.getStrEndTime())); //前台选中的时间段
                vpd.put("var2", item.getAccountName());
                vpd.put("var3", item.getContactName());
                vpd.put("var4", item.getOrgName());
                vpd.put("var5", item.getManagerName());
                vpd.put("var6", Tools.valueOfFormattedBigDecimal(item.getWeight(), Constant.WEIGHT_PRECISION));
                vpd.put("var7", Tools.valueOfFormattedBigDecimal(item.getAmount(), Constant.MONEY_PRECISION));
                vpd.put("var8", Tools.valueOfFormattedBigDecimal(item.getPreviousPeriodBalance(), Constant.MONEY_PRECISION));
                vpd.put("var9", Tools.valueOfFormattedBigDecimal(item.getRebateAmount(), Constant.MONEY_PRECISION));
                vpd.put("var10", Tools.valueOfFormattedBigDecimal(item.getWithdrawAmount(), Constant.MONEY_PRECISION));
                vpd.put("var11", Tools.valueOfFormattedBigDecimal(item.getThisPeriodBalance(), Constant.MONEY_PRECISION));
                varList.add(vpd);
            }
        }
        return varList;
    }


    /**
     * 加载买家的大类返利信息
     *
     * @param query
     * @return
     */
    @RequestMapping("/loadbuyergroupcategoryrebatedata.html")
    @ResponseBody
    public Result loadBuyerGroupCategoryRebateData(BuyerRebateQuery query) {
        Result result = new Result();
        if (query.getBuyerId() != null && query.getBuyerId() > 0) {
            result.setData(reportBusinessService.queryGroupCategoryRebateByBuyerId(query));
            result.setSuccess(Boolean.TRUE);
        }
        return result;
    }

    /**
     * 加载联系人的大类返利信息
     *
     * @param query
     * @return
     */
    @RequestMapping("/loadcontactgroupcategoryrebatedata.html")
    @ResponseBody
    public Result loadContactGroupCategoryRebateData(BuyerRebateQuery query) {
        Result result = new Result();
        if (query.getContactId() != null && query.getContactId() > 0) {
            result.setData(reportBusinessService.queryGroupCategoryRebateByContactId(query));
            result.setSuccess(Boolean.TRUE);
        }
        return result;
    }

    private List<String> buildTitlesList(String titles) {
        List<String> titlesList = new ArrayList<>();
        if (StringUtils.isNotEmpty(titles)) {
            for (String title : titles.split(",")) {
                titlesList.add(title);
            }
        }
        return titlesList;
    }

    /*
     * 处理查询条件
     * @param query 封装了前端的查询条件
     */
    private void setBuyerRebateQuery(BuyerRebateQuery query) {
        //查询条件处理
        if (query != null) {
            //设置权限条件
            query.setUserIdList(getUserIds());

            //屏蔽服务中心查询条件默认查询条件值为当前服务中心
            if (permissionLimit.hasPermission(Constant.PERMISSION_ORG_LIMIT)) {
                User user = getLoginUser();
                query.setOrgId(user.getOrgId());
            }

            //设置交易员模糊查询条件：如果屏蔽交易员查询条件，默认当前用户，否则为前端输入的条件
            if (permissionLimit.hasPermission(Constant.PERMISSION_TRADER_HIDDEN)) {
                User user = getLoginUser();
                query.setBuyerTradeName("%" + user.getName() + "%");
            } else if (StringUtils.isNotEmpty(query.getBuyerTradeName())) {
                query.setBuyerTradeName("%" + query.getBuyerTradeName().trim() + "%");
            }
        }
    }

    /**
     * 销项票清单
     * @param orderItemsQuery
     * @return
     */
    @RequestMapping("/ticketlist.html")
	public String find(ModelMap out, OrderItemsQuery orderItemsQuery) {
//    	OrderItemsDto orderItemsDto = consignOrderItemsService.totalTicketList(orderItemsQuery);
//    	out.put("orderItemsDto", orderItemsDto);
    	setDefaultTime(out);
    	return "/report/business/ticketlist";
	}
    
    @ResponseBody
    @RequestMapping("query/total.html")
   	public Result getTotal(OrderItemsQuery orderItemsQuery) {
    	Result result = new Result();
       	Date dateStart = null;// 起始时间
        Date dateEnd = null;// 起始时间
        try {
			dateStart = getDateStart(orderItemsQuery);
			dateEnd = getDateEnd(orderItemsQuery);
		} catch (ParseException e) {
			logger.error(e.toString());
		}
        orderItemsQuery.setStartDate(dateStart);
        orderItemsQuery.setEndDate(dateEnd);
       	OrderItemsDto orderItemsDto = consignOrderItemsService.totalTicketList(orderItemsQuery);
       	result.setData(orderItemsDto);
       	return result;
   	}
   
    @ResponseBody
    @RequestMapping("query/ticketlist.html")
    public PageResult ticketList(OrderItemsQuery orderItemsQuery) {
        Date dateStart = null;// 起始时间
        Date dateEnd = null;// 起始时间
        try {
			dateStart = getDateStart(orderItemsQuery);
			dateEnd = getDateEnd(orderItemsQuery);
		} catch (ParseException e) {
			logger.error(e.toString());
		}
        orderItemsQuery.setStartDate(dateStart);
        orderItemsQuery.setEndDate(dateEnd);
        List<OrderItemsDto> list = consignOrderItemsService.selectTicketList(orderItemsQuery);
        Integer total = consignOrderItemsService.queryTicketTotal(orderItemsQuery);
        PageResult result = new PageResult();
        result.setData(list);
        result.setRecordsFiltered(total);
        result.setRecordsTotal(list.size());
        return result;
    }

    /**
     * 起始日期
     * @param orderItemsQuery
     * @return
     * @throws ParseException
     */
	private Date getDateStart(OrderItemsQuery orderItemsQuery) throws ParseException {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date dateStart = null;
		if (StringUtils.isNotEmpty(orderItemsQuery.getStartTime())) {
			dateStart = format.parse(orderItemsQuery.getStartTime());
		}
		return dateStart;
	}

    /**
     * 终止日期
     * @param orderItemsQuery
     * @return
     * @throws ParseException
     */
	private Date getDateEnd(OrderItemsQuery orderItemsQuery) throws ParseException {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date dateEnd = null;
		if (StringUtils.isNotEmpty(orderItemsQuery.getEndTime())) {
			dateEnd = new Date(
					format.parse(orderItemsQuery.getEndTime()).getTime() + 24 * 3600 * 1000);
		} 
		return dateEnd;
	}
	
	 /**
     * 销项票清单导出excel
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/pooloutexcel.html")
    public ModelAndView poolOutExcel(OrderItemsQuery orderItemsQuery) {
        ModelAndView mv = null;
        orderItemsQuery.setLength(MAX_RECORD_LENGTH);
        try {
            Map<String, Object> dataMap = new HashMap<>();
            List<String> titles = new ArrayList<>();
            titles.add("开单日期");
            titles.add("交易单号");
            titles.add("买家全称");
            titles.add("品名");
            titles.add("材质");
            titles.add("规格");
            titles.add("实提重量（吨）");
            titles.add("实提金额（元）");
            titles.add("已开销项票重量（吨）");
            titles.add("已开销项票金额（元）");
            titles.add("未开销项票重量（吨）");
            titles.add("未开销项票金额（元）");
            titles.add("折让重量（吨）");
            titles.add("折让买家金额（元）");
            titles.add("状态");
            dataMap.put("titles", titles);
            List<PageData> varList = new ArrayList<>();
            Date dateStart = null;// 起始时间
            Date dateEnd = null;// 起始时间
            try {
    			dateStart = getDateStart(orderItemsQuery);
    			dateEnd = getDateEnd(orderItemsQuery);
    		} catch (ParseException e) {
    			logger.error(e.toString());
    		}
            orderItemsQuery.setStartDate(dateStart);
            orderItemsQuery.setEndDate(dateEnd);
            List<OrderItemsDto> list = consignOrderItemsService.selectTicketList(orderItemsQuery);
            for (OrderItemsDto data : list) {
                PageData page = new PageData();
                page.put("var1", Tools.dateToStr(data.getCreationTime(), "yyyy-MM-dd"));
                page.put("var2", data.getCode());
                if (data.getDepartmentCount() > 1) {
                	page.put("var3", data.getAccountName() +"【"+ data.getDepartmentName()+"】");
                } else {
                	page.put("var3", data.getAccountName());
                }
                page.put("var4", data.getNsortName());
                page.put("var5", data.getMaterial());
                page.put("var6", data.getSpec());
                page.put("var7", Tools.valueOfFormattedBigDecimal(data.getActualPickWeightServer(), Constant.WEIGHT_PRECISION));
                page.put("var8", Tools.valueOfFormattedBigDecimal(data.getBringamount(), Constant.MONEY_PRECISION));
                page.put("var9", Tools.valueOfFormattedBigDecimal(data.getUsedWeight(), Constant.WEIGHT_PRECISION));
                page.put("var10", Tools.valueOfFormattedBigDecimal(data.getUsedAmount(), Constant.MONEY_PRECISION));
                page.put("var11", Tools.valueOfFormattedBigDecimal(data.getNotOpenWeight(), Constant.WEIGHT_PRECISION));
                page.put("var12", Tools.valueOfFormattedBigDecimal(data.getNotOpenAmount(), Constant.MONEY_PRECISION));
                page.put("var13", Tools.valueOfFormattedBigDecimal(data.getAllowanceWeight(), Constant.WEIGHT_PRECISION));
                page.put("var14", Tools.valueOfFormattedBigDecimal(data.getAllowanceBuyerAmount(), Constant.MONEY_PRECISION));
                if (data.getUsedWeight().compareTo(data.getActualPickWeightServer()) == 0 && data.getUsedWeight().doubleValue() != 0) {
                	page.put("var15", "已开具");
                } else if (data.getUsedWeight().doubleValue() == 0) {
                	page.put("var15", "部分开具");
                } else if (data.getUsedWeight().compareTo(data.getActualPickWeightServer()) == -1 && data.getUsedWeight().doubleValue() != 0){
                	page.put("var15", "未开具");
                }
                varList.add(page);
            }
            dataMap.put("varList", varList);
            ObjectExcelView erv = new ObjectExcelView();//执行excel操作
            mv = new ModelAndView(erv, dataMap);
        } catch (Exception e) {
          
        }
        return mv;
    }

    /**
     * 服务中心日报页面
     *
     * @param out
     */
    @RequestMapping("/orgDayReport.html")
    public void orgDayReport(ModelMap out) {
        //日期设置
        setDefaultTime(out);
        List<Organization> list = reportOrgDayService.queryAllBusinessOrg();
        out.put("orgList", list);
        out.put("size", list.size());
    }

    /**
     * 获取服务中心日报数据列
     *
     * @return
     */
    @RequestMapping("/getorgdayreportcolumns.html")
    @ResponseBody
    public Result getOrgDayReportColumns() {
        Result result = new Result();
        List<Column> columns = reportOrgDayService.getOrgDayReportColumns();
        result.setData(columns);
        return result;
    }

    /**
     * 加载服务中心日报数据
     *
     * @return
     */
    @RequestMapping("/loadorgdayreportdata.html")
    @ResponseBody
    public PageResult loadOrgDayReportData(ReportOrgDayQuery query) {
        List<String> dateList = reportOrgDayService.queryAllCalculateDateForPage(query);
        List list = new ArrayList<>();
        if (dateList != null && !dateList.isEmpty()) {
            //设置分页日期参数
            setOrgDayPageDate(dateList, query);
            list = reportOrgDayService.getOrgDayList(query);
        }
        return new PageResult(list.size(),dateList.size(),list);
    }

    /*
     *设置分页的日期 日期列表按时间倒序存放
     * @param dateList
     * @param query
     */
    private void setOrgDayPageDate(List<String> dateList, ReportOrgDayQuery query) {
        int len = dateList.size();
        query.setEndPageTimeStr(dateList.get(query.getStart()));

        int endSize = query.getStart() + query.getLength();
        query.setStartPageTimeStr(dateList.get(endSize <= len ? endSize - 1 : len - 1));
    }


    /**
     * 导出服务中心日报数据
     *
     * @return
     */
    @RequestMapping("/exportorgdayreport.html")
    @ResponseBody
    public ModelAndView exportOrgDayReport(ReportOrgDayQuery query) {

        query.setStartPageTimeStr(query.getStartTimeStr());
        query.setEndPageTimeStr(query.getEndTimeStr());
        List<Map<String, Object>> list = reportOrgDayService.getOrgDayList(query);

        //excel表头和数据
        Map<String, Object> dataMap = new HashMap<>();

        //表头
        dataMap.put("titles", buildOrgDayTitlesList());

        //数据
        dataMap.put("varList", buildOrgDayPageDataList(list));

        ObjectExcelView erv = new ObjectExcelView();
        return new ModelAndView(erv, dataMap);
    }

    private List<String> buildOrgDayTitlesList() {
        List<String> titlesList = new ArrayList<>();
        titlesList.add("日期");
        titlesList.add("交易数据");

        //服务中心
        List<Organization> orgList = reportOrgDayService.queryAllBusinessOrg();
        orgList.forEach(a -> titlesList.add(a.getName()));

        titlesList.add("当日合计");
        //titlesList.add("沉淀资金");
        return titlesList;
    }

    private List<PageData> buildOrgDayPageDataList(List<Map<String, Object>> list) {
        List<Column> columns = reportOrgDayService.getOrgDayReportColumns();
        List<PageData> varList = new ArrayList<>();
        if (list != null) {
            list.forEach(a -> {
                PageData vpd = new PageData();
                columns.forEach(b->{
                    Object o = a.get(b.getData());
                    String val = (o == null ? "" : o.toString());
                    //当日合计列格式化 销售金额、采购金额行四舍五入保留2位小数；交易数量行四舍五入保留6位小数
                    if(Constant.REPORT_DAY_COL_NAME_DAYTOTAL.equals(b.getData())){
                        String rowType = a.get(Constant.REPORT_DAY_ROW_DATA_TYPE).toString();
                        if (StringUtils.equals(rowType, ReportDayRowName.PurchaseAmount.getCode())
                            || StringUtils.equals(rowType, ReportDayRowName.SaleAmount.getCode())){
                            val = formatBigDecimalForExcle((BigDecimal) o, Constant.MONEY_PRECISION);
                        }else if (StringUtils.equals(rowType, ReportDayRowName.Weight.getCode())){
                            val = formatBigDecimalForExcle((BigDecimal)o, Constant.WEIGHT_PRECISION);
                        }
                    }
                    vpd.put(b.getIndex(),val);
                });
                varList.add(vpd);
            });
        }
        return varList;
    }

    /**
     * 手动添加服务中心日报（不包括平台资金） 日报数据算endDate
     * 每调用一次需传相应的补录的那天的时间段（只能补录一天）
     * 代运营卖家数：会插入系统当前时间的最新数据不是补录的时间（查询截止日为止所签约的所有代运营卖家数）
     * @param startDate 开始时间 格式： yyyy-MM-dd HH:mm:ss
     * @param endDate   结束时间
     */
    @RequestMapping("/addOrgDayReport.html")
    public void addOrgDayReport(@RequestParam("startDate") String startDate,@RequestParam("endDate") String endDate,ModelMap out) {
        try{
            //执行成功 更新配置表上次执行时间为date
            SysSetting sysSetting = sysSettingService.queryBySettingType(SysSettingType.ReportOrgDay.getCode());

            String toDay = Tools.dateToStr(Tools.strToDate(endDate, "yyyy-MM-dd"), "yyyy-MM-dd");
            String lastDay = Tools.dateToStr(Tools.strToDate(sysSetting.getDefaultValue(), "yyyy-MM-dd"), "yyyy-MM-dd");
            if (toDay.equals(lastDay)){
                out.put("msg","已经执行过了，当前时间段为" + startDate + "至" + endDate);
            }else {
                reportOrgDayService.addOrgDayByOrder(Tools.strToDate(startDate, "yyyy-MM-dd HH:mm:ss"), Tools.strToDate(endDate, "yyyy-MM-dd HH:mm:ss"),sysSetting);
                out.put("msg", "执行成功：当前时间段为" + startDate + "至" + endDate);
            }
        }catch (Exception e){
            out.put("msg","执行失败，请联系管理员：当前时间段为"+startDate + "至"+endDate);
            logger.error("手动执行插入服务中心日报数据错误：",e);
        }
    }

    /**
     * 进项发票统计报表
     *
     * @param out
     * @return
     */
    @RequestMapping("/invoiceinbordereaux.html")
    public void invoiceInBordereaux(ModelMap out) {
        setDefaultTime(out);
    }
    
    /**
     * 进项发票统计报表合计
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/invoiceInBordereauxsum.html")
   	public Result invoiceInBordereauxSum(InvoiceInBordereauxQuery queryParam) {
    	if(null != queryParam.getStatus()) {
    		if(queryParam.getStatus().contains(InvoiceInBordereauxStatus.Zero.getKey())) {
    			queryParam.setStatusForNull();
    		}
     	}
    	Result result = new Result();
    	InvoiceInBordereauxDto invoiceInBordereauxDto = reportBusinessService.queryInvoiceInBordereauxSum(queryParam);
       	result.setData(invoiceInBordereauxDto);
       	return result;
   	}
    
    /**
     * 进项发票统计报表数据
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/invoiceinbordereauxdata.html")
    public PageResult invoiceInBordereauxData(InvoiceInBordereauxQuery queryParam) {
    	if(null != queryParam.getStatus()) {
    		if(queryParam.getStatus().contains(InvoiceInBordereauxStatus.Zero.getKey())) {
    			queryParam.setStatusForNull();
    		}
     	}
     	PageResult result = new PageResult();
        int total = reportBusinessService.queryInvoiceInBordereauxCount(queryParam);
        result.setRecordsFiltered(total);
        if (total > 0) {
            List<InvoiceInBordereauxDto> list = reportBusinessService.queryInvoiceInBordereauxData(queryParam);
            result.setData(list);
            result.setRecordsTotal(list.size());
        } else {
            result.setData(new ArrayList<>());
            result.setRecordsTotal(0);
        }
        return result;
    }
    
    /**
     * 买家交易报表导出excel
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/invoiceinbordereauxdataexcel.html")
    public ModelAndView invoiceInBordereauxDataExcel(InvoiceInBordereauxQuery queryParam) {
    	if(null != queryParam.getStatus()) {
    		if(queryParam.getStatus().contains(InvoiceInBordereauxStatus.Zero.getKey())) {
    			queryParam.setStatusForNull();
    		}
     	}
        ModelAndView mv = null;
        queryParam.setLength(MAX_RECORD_LENGTH);
        try {
            Map<String, Object> dataMap = new HashMap<>();
            List<String> titles = new ArrayList<>();
            titles.add("日期");
            titles.add("交易单号");
            titles.add("卖家全称");
            titles.add("品名");
            titles.add("材质");
            titles.add("规格");
            titles.add("实提重量（吨）");
            titles.add("实提金额（元）");
            titles.add("折让重量（吨）");
            titles.add("折让金额（元）");
            titles.add("已到票重量（吨）");
            titles.add("已到票金额（元）");
            titles.add("未到票重量（吨）");
            titles.add("未到票金额（元）");
            titles.add("状态");
            dataMap.put("titles", titles);
            List<PageData> varList = new ArrayList<>();
            List<InvoiceInBordereauxDto> list = reportBusinessService.queryInvoiceInBordereauxData(queryParam);
            for (InvoiceInBordereauxDto data : list) {
                PageData vpd = new PageData();
                vpd.put("var1", Tools.dateToStr(data.getOrderTime(), "yyyy-MM-dd"));
                vpd.put("var2", data.getOrderCode());
                if (data.getDepartmentCount() > 1) {
                	vpd.put("var3", data.getSellerName() +"【"+ data.getDepartmentName()+"】");
                } else {
                	vpd.put("var3", data.getSellerName());
                }
                vpd.put("var4", data.getNsortName());
                vpd.put("var5", data.getMaterial());
                vpd.put("var6", data.getSpec());
                setExcelWeight("var7",data.getActualWeight(), vpd);
                setExcelAmount("var8",data.getActualAmount(), vpd);
                setExcelWeight("var9",data.getAllowanceWeight(), vpd);
                setExcelAmount("var10",data.getAllowanceAmount(), vpd);
                setExcelWeight("var11",data.getWeight(), vpd);
                setExcelAmount("var12",data.getAmount(), vpd);
                setExcelWeight("var13",data.getUnWeight(), vpd);
                setExcelAmount("var14",data.getUnAmount(), vpd);
                vpd.put("var15", data.getStatus());
                varList.add(vpd);
            }
            dataMap.put("varList", varList);
            ObjectExcelView erv = new ObjectExcelView();                //执行excel操作
            mv = new ModelAndView(erv, dataMap);
        } catch (Exception e) {
            // handle error
        }
        return mv;
    }

	private void setExcelWeight(String var, BigDecimal data, PageData vpd) {
		if(null != data && data.doubleValue() != 0) {
			vpd.put(var, Tools.valueOfFormattedBigDecimal(data, Constant.WEIGHT_PRECISION));
		}else {
			vpd.put(var,"");
		}
	}
	
	private void setExcelAmount(String var, BigDecimal data, PageData vpd) {
		if(null != data && data.doubleValue() != 0) {
			vpd.put(var, Tools.valueOfFormattedBigDecimal(data, Constant.MONEY_PRECISION));
		}else {
			vpd.put(var,"");
		}
	}
	
    /**
     * 服务中心月报页面
     *
     * @param out
     */
    @RequestMapping("/orgMonthReport.html")
    public void orgMonthReport(ModelMap out) {
        //日期设置
        setDefaultTime(out);
        List<Organization> list = reportOrgDayService.queryAllBusinessOrg();
        out.put("orgList", list);
        out.put("size", list.size());
    }

    /**
     * 获取服务中心月报数据列
     *
     * @return
     */
    @RequestMapping("/getorgmonthreportcolumns.html")
    @ResponseBody
    public Result getOrgMonthReportColumns() {
        Result result = new Result();
        List<Column> columns = reportOrgDayService.getOrgMonthReportColumns();
        result.setData(columns);
        return result;
    }

    /**
     * 加载服务中心月报数据
     *
     * @return
     */
    @RequestMapping("/loadorgmonthreportdata.html")
    @ResponseBody
    public PageResult loadOrgMonthReportData(ReportOrgDayQuery query) {
        query.setOrderStatusList(ConsignOrderStatus.getSecondOrderStatus());
        List<String> dateList = reportOrgDayService.queryMonthReportAllCalculateDateForPage(query);
        List list = new ArrayList<>();
        if (dateList != null && !dateList.isEmpty()) {
            //设置分页日期参数
            setOrgMonthPageDate(dateList, query);
            list = reportOrgDayService.getOrgMonthList(query);
        }
        return new PageResult(list.size(),dateList.size(),list);
    }

    /*
     * 设置分页的日期 日期列表按时间顺序存放
     * @param dateList
     * @param query
     */
    private void setOrgMonthPageDate(List<String> dateList, ReportOrgDayQuery query) {
        int len = dateList.size();
        query.setStartPageTimeStr(dateList.get(query.getStart()));

        int endSize = query.getStart() + query.getLength();
        query.setEndPageTimeStr(dateList.get(endSize <= len ? endSize - 1 : len - 1));
    }


    /**
     * 导出服务中心月报数据
     *
     * @return
     */
    @RequestMapping("/exportorgmonthreport.html")
    @ResponseBody
    public ModelAndView exportOrgMonthReport(ReportOrgDayQuery query) {

        query.setStartPageTimeStr(query.getStartTimeStr());
        query.setEndPageTimeStr(query.getEndTimeStr());
        query.setOrderStatusList(ConsignOrderStatus.getSecondOrderStatus());

        List<Map<String, Object>> list = reportOrgDayService.getOrgMonthList(query);

        //excel表头和数据
        Map<String, Object> dataMap = new HashMap<>();

        //表头
        dataMap.put("titles", buildOrgMonthTitlesList());

        //数据
        dataMap.put("varList", buildOrgMonthPageDataList(list));

        ObjectExcelView erv = new ObjectExcelView();
        return new ModelAndView(erv, dataMap);
    }

    private List<String> buildOrgMonthTitlesList() {
        List<String> titlesList = new ArrayList<>();
        titlesList.add("日期");
        titlesList.add("交易数据");

        //服务中心
        List<Organization> orgList = reportOrgDayService.queryAllBusinessOrg();
        orgList.forEach(a -> titlesList.add(a.getName()));

        titlesList.add("当日合计");
//        titlesList.add("沉淀资金");
        return titlesList;
    }

    private List<PageData> buildOrgMonthPageDataList(List<Map<String, Object>> list) {
        List<Column> columns = reportOrgDayService.getOrgMonthReportColumns();
        List<PageData> varList = new ArrayList<>();
        if (list != null) {
            list.forEach(a -> {
                PageData vpd = new PageData();
                columns.forEach(b->{
                    Object o = a.get(b.getData());
                    String val = (o == null ? "" : o.toString());
                    //当日合计列格式化 销售金额、采购金额行四舍五入保留2位小数；交易数量行四舍五入保留6位小数
                    if(Constant.REPORT_DAY_COL_NAME_DAYTOTAL.equals(b.getData())){
                        String rowType = a.get(Constant.REPORT_DAY_ROW_DATA_TYPE).toString();
                        if (StringUtils.equals(rowType, ReportDayRowName.PurchaseAmount.getCode())
                                || StringUtils.equals(rowType, ReportDayRowName.SaleAmount.getCode())){
                            val = formatBigDecimalForExcle((BigDecimal) o, Constant.MONEY_PRECISION);
                        }else if (StringUtils.equals(rowType, ReportDayRowName.Weight.getCode())){
                            val = formatBigDecimalForExcle((BigDecimal)o, Constant.WEIGHT_PRECISION);
                        }
                    }
                    vpd.put(b.getIndex(),val);
                });
                varList.add(vpd);
            });
        }
        return varList;
    }

    private String formatBigDecimalForExcle(BigDecimal b,int len){
       if (b == null) return "";
       return Tools.valueOfFormattedBigDecimal(b,len) ;
    }
    
    /**
     * 进项票记账报表
     *
     * @param out
     * @return
     */
    @RequestMapping("/invoicekeeping.html")
    public void invoiceKeeping(ModelMap out) {
        setDefaultTime(out);
        InvoiceKeepingQuery queryParam = new InvoiceKeepingQuery();
        if (!permissionLimit.hasPermission(PERMISSION_INVOICEKEEPING)) {
			User user = getLoginUser();
			queryParam.setCheckUserId(user.getId());
		}
        List<InvoiceKeepingDto> list = invoiceInService.queryCheckName(queryParam);
        List<CategoryGroupDto> categories = categoryGroupService.queryAllParentCategoryGroupInner();
        out.put("checkUserlist", list);
        out.put("categories", categories);
    }
    
    @ResponseBody
    @RequestMapping("/invoicekeepingdata.html")
    public PageResult invoiceKeepingData(InvoiceKeepingQuery queryParam) {
		if (!permissionLimit.hasPermission(PERMISSION_INVOICEKEEPING)) {
			User user = getLoginUser();
			queryParam.setCheckUserId(user.getId());
		}
    	try {
			queryParam.setEndDate(getDateEnd(queryParam));
		} catch (ParseException e) {
			logger.error(e.toString());
		}
    	List<InvoiceKeepingDto> list = invoiceInService.invoiceKeeping(queryParam);
    	int count = invoiceInService.invoiceKeepingCount(queryParam);
		PageResult result = new PageResult();
		result.setData(list);
		result.setRecordsFiltered(count);
		result.setRecordsTotal(list.size());
		return result;
    }
    
    /**
     * 买家交易报表导出excel
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/invoicekeepingexcel.html")
    public ModelAndView invoiceKeepingExcel(InvoiceKeepingQuery queryParam) {
    	try {
			queryParam.setEndDate(getDateEnd(queryParam));
		} catch (ParseException e) {
			logger.error(e.toString());
		}
        ModelAndView mv = null;
        queryParam.setLength(MAX_RECORD_LENGTH);
        try {
            Map<String, Object> dataMap = new HashMap<>();
            List<String> titles = new ArrayList<>();
            titles.add("日期：" + (queryParam.getStartDate() == null ? "*" : queryParam.getStartDate()) 
            		+ "~" + (queryParam.getEndDate() == null ? "*" : queryParam.getEndDate()));
            titles.add("");
            titles.add("");
            titles.add("");
            titles.add("");
            titles.add("");
            titles.add("");
            titles.add("");
            titles.add("");
            titles.add("");
            dataMap.put("titles", titles);
            List<String> merges = new ArrayList<>();
            merges.add("0,0,0,3");
            dataMap.put("merges", merges);
            List<PageData> varList = new ArrayList<>();
            varList.addAll(buildTitleData());
            List<InvoiceKeepingDto> list = invoiceInService.invoiceKeeping(queryParam);
            for (InvoiceKeepingDto data : list) {
                PageData vpd = new PageData();
                vpd.put("var1", Tools.dateToStr(data.getInvoiceDate(), "yyyy-MM-dd"));
                vpd.put("var2", data.getCode());
                vpd.put("var3", data.getSellerName());
                vpd.put("var4", Tools.formatBigDecimal(data.getNoTaxAmount(), AMOUNT_LIMIT));
                vpd.put("var5", Tools.formatBigDecimal(data.getTaxAmount(), AMOUNT_LIMIT));
                vpd.put("var6", Tools.formatBigDecimal(data.getAmount(), AMOUNT_LIMIT));
                vpd.put("var7",data.getCheckUserName());
                vpd.put("var8",Tools.dateToStr(data.getCheckDate(), "yyyy-MM-dd"));
                vpd.put("var9", InvoiceInStatus.WAIT.getCode().equals(data.getStatus()) == true ? "待认证" : "已认证");
                vpd.put("var10",data.getName());
                varList.add(vpd);
            }
            dataMap.put("varList", varList);
            ObjectExcelView erv = new ObjectExcelView();                //执行excel操作
            mv = new ModelAndView(erv, dataMap);
        } catch (Exception e) {
        	System.out.println("error:"+e);
            // handle error
        }
        return mv;
    }
    
    private List<PageData> buildTitleData() {
        List<PageData> varList = new ArrayList<>();
        PageData vpd = null;
        vpd = new PageData();
        vpd.put("var1","开票日期");
        vpd.put("var2","进项发票号");
        vpd.put("var3","卖家全称");
        vpd.put("var4","发票金额");
        vpd.put("var5","税额");
        vpd.put("var6","价税合计");
        vpd.put("var7","确认人员");
        vpd.put("var8","确认时间");
        vpd.put("var9","发票状态");
        vpd.put("var10","大类");
        varList.add(vpd);
        return varList;
    }
    

	/**
	 * 终止日期
	 * 
	 * @param queryParam
	 * @return
	 * @throws ParseException
	 */
	private String getDateEnd(InvoiceKeepingQuery queryParam) throws ParseException {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date dateEnd = null;
		if (StringUtils.isNotEmpty(queryParam.getEndDate())) {
			dateEnd = new Date(
					format.parse(queryParam.getEndDate()).getTime() + 24 * 3600 * 1000);
		}
		return format.format(dateEnd);
	}

	/**
     * tuxianming, 销项票清单 
     * @param out
     */
    @RequestMapping("/invoiceoutchecklist.html")
    public void invoiceoutChecklist(ModelMap out) {
        setDefaultTime(out);
        
        if(permissionLimit.hasPermission(CHECKOUT_LIST_ALL_ORG)){
        	List<Organization> orgs = organizationService.queryAllBusinessOrg();
        	out.put("orgs", orgs);
        	out.put("isAll", true);
        }else{
        	List<Organization> orgs  = new ArrayList<Organization>();
        	orgs.add(organizationService.queryById(getLoginUser().getOrgId()));
        	out.put("orgs", orgs);
        }
        
    }
	
	
	/**
     * tuxianming, 销项票清单 
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/loadinvoiceoutchecklist.html")
    public PageResult loadInvoiceoutChecklist(ChecklistDetailQuery query) {
    	
    	if(!permissionLimit.hasPermission(CHECKOUT_LIST_ALL_ORG)){
    		List<String> orgIds = new ArrayList<String>();
    		orgIds.add(getLoginUser().getOrgId()+"");
    		query.setOrgIds(orgIds);
    	}
    	
    	PageResult result = new PageResult();

        int total = invoiceOutCheckListDetailService.countByCondition(query);
        result.setRecordsFiltered(total);

        List<ChecklistDetailDto> list = invoiceOutCheckListDetailService.queryByCondition(query);
        result.setData(list);
        result.setRecordsTotal(list.size());
		return result;
    }
	
    /**
     * tuxianming, 销项票清单 
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/loadinvoiceoutchecklisttotal.html")
    public Result loadInvoiceoutChecklistTotal(ChecklistDetailQuery query) {
    	
    	if(!permissionLimit.hasPermission(CHECKOUT_LIST_ALL_ORG)){
    		List<String> orgIds = new ArrayList<String>();
    		orgIds.add(getLoginUser().getOrgId()+"");
    		query.setOrgIds(orgIds);
    	}
    	
        ChecklistDetailDto dto = invoiceOutCheckListDetailService.queryByConditionTotal(query);
        
        Result result = new Result(dto);
		return result;
    }
    
    @ResponseBody
    @RequestMapping("loadinvoiceoutchecklistexport.html")
    public ModelAndView loadInvoiceoutChecklistExport(ChecklistDetailQuery query){
 
    	if(!permissionLimit.hasPermission(CHECKOUT_LIST_ALL_ORG)){
    		List<String> orgIds = new ArrayList<String>();
    		orgIds.add(getLoginUser().getOrgId()+"");
    		query.setOrgIds(orgIds);
    	}
    	
        int total = invoiceOutCheckListDetailService.countByCondition(query);

        if (total > 0) {
            query.setStart(0);
            query.setLength(Integer.MAX_VALUE);

            List<ChecklistDetailDto> list = invoiceOutCheckListDetailService.queryByCondition(query);
            Map<String, Object> dataMap = new HashMap<>();
            List<String> titles = new ArrayList<>();
            titles.add("清单编号");
            titles.add("开票时间");
            titles.add("买家全称");
            titles.add("品名");
            titles.add("规格");
            titles.add("数量（吨）");
            titles.add("价税合计（元）");
            titles.add("服务中心");
            titles.add("发票类型");
            dataMap.put("titles", titles);
            List<PageData> varList = new ArrayList<>();
            for (ChecklistDetailDto item : list) {
                PageData vpd = new PageData();
                vpd.put("var1", item.getId() != null ? item.getId().toString() : "");
                vpd.put("var2", item.getApplyTime()!=null ? Tools.dateToStr(item.getApplyTime(), "yyyy-MM-dd"):"");
                vpd.put("var3", item.getBuyerName() != null ? item.getBuyerName() : "");
                vpd.put("var4", item.getNsortName() != null ? item.getNsortName() : "");
                vpd.put("var5", item.getSpec() != null ? item.getSpec() : "");
                
                setExcelWeight("var6", item.getWeight(), vpd);
                setExcelAmount("var7", item.getAmount(), vpd);
                
                vpd.put("var8", item.getOrgName() != null ? item.getOrgName() : "");
                vpd.put("var9", item.getInvoiceType() != null ? item.getInvoiceType() : "");
                varList.add(vpd);
            }
            dataMap.put("varList", varList);
            ObjectExcelView erv = new ObjectExcelView();
            ModelAndView mv = new ModelAndView(erv, dataMap);
            return mv;
        }

        return null;
    }

    /**
     * tuxianming, 合同清单 
     * @param out
     */
    @RequestMapping("/contractlist.html")
    public void contractlist(ModelMap out) {
        setDefaultTime(out);
    	List<UserOrgsDto> userOrgs = userOrgService.getConfigOrgsByUserId(getLoginUser().getId());
    	out.put("orgs", userOrgs);
    	out.put("isAll", true);
        
    }
    /**
     * wangxianjun, 合同清单
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/loadcontractlist.html")
    public PageResult loadContractList(ChecklistDetailQuery query) {
    	PageResult result = new PageResult();
        int total = reportBusinessService.countContractList(query);
        result.setRecordsFiltered(total);
        List<ContractListDto> list = reportBusinessService.selectContractList(query);
        result.setData(list);
        result.setRecordsTotal(list.size());
		return result;
    }
    
    //合同清单导出
    @ResponseBody
    @RequestMapping("loadcontractlistexport.html")
    public ModelAndView loadContractListExport(ChecklistDetailQuery query){
        int total =  reportBusinessService.countContractList(query);
        if (total > 0) {
            query.setStart(0);
            query.setLength(0);//不分支，查询全部
            List<ContractListDto> list = reportBusinessService.selectContractList(query);
            Map<String, Object> dataMap = new HashMap<>();
            List<String> titles = new ArrayList<>();
            titles.add("合同类型");
            titles.add("合同编号");
            titles.add("客户名称");
            titles.add("经办部门");
            titles.add("经办人");
            titles.add("签订日期");
            titles.add("金额（元）");
            titles.add("客户标示");
            titles.add("备注");
            dataMap.put("titles", titles);
            List<PageData> varList = new ArrayList<>();
            for (ContractListDto item : list) {
                PageData vpd = new PageData();
                vpd.put("var1", item.getType() != null ? item.getType(): "");
                vpd.put("var2", item.getContractCode()!=null ? item.getContractCode():"");
                vpd.put("var3", item.getAccountName() != null ? item.getAccountName() : "");
                vpd.put("var4", item.getOrderOrgName()!= null ? item.getOrderOrgName() : "");
                vpd.put("var5", item.getOwnerName() != null ? item.getOwnerName() : "");
                vpd.put("var6", item.getCreatedTime() != null ? item.getCreatedTime() : "");
                setExcelAmount("var7", item.getTotalAmount(), vpd);
                vpd.put("var8", item.getCustLabel());
                vpd.put("var9", item.getNote() != null ? item.getNote() : "");
                varList.add(vpd);
            }
            dataMap.put("varList", varList);
            ObjectExcelView erv = new ObjectExcelView();
            ModelAndView mv = new ModelAndView(erv, dataMap);
            return mv;
        }
        return null;
    }
    
    /**
     * 留存销项票报表跳转
     * @date 20160621
     * @param out
     * @author tuxianming
     */
    @RequestMapping("/unsendinvoiceout.html")
    public void unsendInvoiceOut(ModelMap out) {
    	setDefaultTime(out);
    	List<Organization> orgs = organizationService.queryAllBusinessOrg();
    	out.put("orgs", orgs);
    	
    	SysSetting balanceSecondDebtLimit = settingService.queryByType("InvoiceOutApplySecond");
    	out.put("balanceSecondDebtLimit", balanceSecondDebtLimit.getSettingValue());
    	
    }
 
    /**
     * 留存销项票报表数据加载
     * @date 20160621
     * @param query : buyerId, startTime, endTime, checklistId, start, length
     * @author tuxianming
     * @return
     */
    @ResponseBody
    @RequestMapping("/loadunsendinvoiceout.html")
    public PageResult loadUnsendInvoiceOut(ChecklistDetailQuery query) {
    	PageResult result = new PageResult();
    	
    	List<UnSendInvoiceOutDto> datas = checklistService.queryUnsendInvoiceOut(query);
    	int total = checklistService.totalUnsendInvoiceOut(query);
    	
    	result.setData(datas);
        result.setRecordsTotal(datas.size());
        result.setRecordsFiltered(total);
    	
    	return result;
    }
    
    /**
     * 留存销项票报表导出为excel表
     * 20160614
     * @param query query: query : buyerId, startTime, endTime, checklistId
     * @author tuxianming
     * @return
     */
    @RequestMapping("/exportunsendinvoiceout.html")
    public ModelAndView exportUnsendInvoiceout(ChecklistDetailQuery query){
    	query.setLength(-1);
    	query.setStart(-1);
    	List<UnSendInvoiceOutDto> datas = checklistService.queryUnsendInvoiceOut(query);
    	
    	//build excel 数据结构
    	Map<String, Object> dataMap = new HashMap<>();
        //表头
    	List<String> titles = Arrays.asList("客户名称","交易单号","二结账户欠款（元）","买家凭证","卖家凭证",
    			"货物或应税劳务、服务名称", "规格", "重量（吨）", "价税合计（元）", "服务中心", "发票类型", "清单编号", "生成清单时间");
        dataMap.put("titles", titles);
        //数据内容
        List<PageData> varList = new ArrayList<PageData>();
        
        //样式内容
        List<ObjectExcelStyle> styles = new ArrayList<ObjectExcelStyle>();
        
        if(datas!=null){
        	SysSetting balanceSecondDebtLimitSetting = settingService.queryByType("InvoiceOutApplySecond");
        	double balanceSecondDebtLimit = 0;
        	if(balanceSecondDebtLimitSetting!=null && balanceSecondDebtLimitSetting.getSettingValue()!=null){
        		try {
        			balanceSecondDebtLimit = Double.parseDouble(balanceSecondDebtLimitSetting.getSettingValue());
				} catch (Exception e) {
					balanceSecondDebtLimit = 0;
				}
        	}
        		
        	
        	int i=1;  //第0行为header的表头
        	for (UnSendInvoiceOutDto item : datas) {
                PageData vpd = new PageData();
                
                vpd.put("var1", item.getBuyerName());
                vpd.put("var2", item.getOrderCode());
                
                double balanceSecondSettlement = 0;  //二结账户欠款大于0，显示：0.00， 小于0，正常显示
                if(balanceSecondDebtLimit>0 && item.getBalanceSecondSettlement()!=null){
                	balanceSecondSettlement = item.getBalanceSecondSettlement().doubleValue();
                	if(balanceSecondSettlement<0 && Math.abs(balanceSecondSettlement) > balanceSecondDebtLimit){
                		
                		styles.add(
                				ObjectExcelStyle.build()
                					.setType(ObjectExcelStyle.StyleType.FONT_COLOR)
                					.setValue(HSSFColor.RED.index)
                					.setX(3)
                					.setY(i)
                		);
                		
                	}
                }
                
                if(balanceSecondSettlement>0)
                	balanceSecondSettlement = 0;
                else
                	balanceSecondSettlement = Math.abs(balanceSecondSettlement);
                
                setExcelAmountNotnull("var3", new BigDecimal(balanceSecondSettlement), vpd);
                
                vpd.put("var4", item.getBuyerCredentialStatus());
                
                if("审核通过".equals(item.getBuyerCredentialStatus())==false){
                	styles.add(
            				ObjectExcelStyle.build()
            					.setType(ObjectExcelStyle.StyleType.FONT_COLOR)
            					.setValue(HSSFColor.RED.index)
            					.setX(4)
            					.setY(i)
            		);
                }
                
                vpd.put("var5", item.getSellerCredentialStatus());
                if("审核通过".equals(item.getSellerCredentialStatus())==false){
                	styles.add(
            				ObjectExcelStyle.build()
            					.setType(ObjectExcelStyle.StyleType.FONT_COLOR)
            					.setValue(HSSFColor.RED.index)
            					.setX(5)
            					.setY(i)
            		);
                }
                
                vpd.put("var6", item.getNsortName());
                vpd.put("var7", item.getSpec());
                setExcelWeight("var8", item.getWeight(), vpd);
                setExcelAmount("var9", item.getAmount(), vpd);
                vpd.put("var10", item.getOrgName());
                vpd.put("var11", item.getInvoiceType());
                vpd.put("var12", item.getChecklistId()+"");
                vpd.put("var13", item.getCreated());
                
                varList.add(vpd);
                i++;
            }
        }
        dataMap.put("styles", styles);
        dataMap.put("varList", varList);
        ObjectExcelView erv = new ObjectExcelView(); //执行excel操作
        return new ModelAndView(erv, dataMap);
    }
    
    private void setExcelAmountNotnull(String var, BigDecimal data, PageData vpd) {
        if(null != data && data.doubleValue() != 0) {
            vpd.put(var, Tools.valueOfFormattedBigDecimal(data, Constant.MONEY_PRECISION));
        }else {
            vpd.put(var,"0.00");
        }
    }
    
}




