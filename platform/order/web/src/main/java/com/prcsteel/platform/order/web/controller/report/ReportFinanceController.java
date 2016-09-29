package com.prcsteel.platform.order.web.controller.report;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.prcsteel.platform.account.model.model.Account;
import com.prcsteel.platform.account.service.AccountService;
import com.prcsteel.platform.acl.model.model.Organization;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.acl.service.OrganizationService;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.common.utils.PageData;
import com.prcsteel.platform.common.utils.Tools;
import com.prcsteel.platform.common.vo.PageResult;
import com.prcsteel.platform.common.vo.Result;
import com.prcsteel.platform.order.model.dto.IncomeSummaryDto;
import com.prcsteel.platform.order.model.dto.InvoiceInWithDetailDto;
import com.prcsteel.platform.order.model.dto.InvoiceOutItemDetailDto;
import com.prcsteel.platform.order.model.dto.OrderPayMentDto;
import com.prcsteel.platform.order.model.dto.ReportAccountFundDetailDto;
import com.prcsteel.platform.order.model.dto.ReportAccountFundDto;
import com.prcsteel.platform.order.model.dto.ReportBuyerInvoiceOutDto;
import com.prcsteel.platform.order.model.dto.ReportInvoiceInAndOutDto;
import com.prcsteel.platform.order.model.dto.ReportSellerInvoiceInDto;
import com.prcsteel.platform.order.model.dto.UnInvoicedDto;
import com.prcsteel.platform.order.model.dto.UninvoicedInDto;
import com.prcsteel.platform.order.model.enums.InvoiceOutApplyStatus;
import com.prcsteel.platform.order.model.enums.InvoiceOutMainStatus;
import com.prcsteel.platform.order.model.model.ReportBuyerInvoiceOut;
import com.prcsteel.platform.order.model.model.ReportSellerInvoiceIn;
import com.prcsteel.platform.order.model.query.IncomeSummaryQuery;
import com.prcsteel.platform.order.model.query.InvoiceInListQuery;
import com.prcsteel.platform.order.model.query.InvoiceoutExpectQuery;
import com.prcsteel.platform.order.model.query.OrderPayMentQuery;
import com.prcsteel.platform.order.model.query.ReportAccountFundQuery;
import com.prcsteel.platform.order.model.query.ReportBuyerInvoiceOutQuery;
import com.prcsteel.platform.order.model.query.ReportInvoiceInAndOutQuery;
import com.prcsteel.platform.order.model.query.ReportSellerInvoiceInDataQuery;
import com.prcsteel.platform.order.service.order.ConsignOrderService;
import com.prcsteel.platform.order.service.report.ReportAccountFinancialService;
import com.prcsteel.platform.order.service.report.ReportFinanceService;
import com.prcsteel.platform.order.web.controller.BaseController;
import com.prcsteel.platform.order.web.support.ObjectExcelView;
import com.prcsteel.platform.order.web.support.ShiroVelocity;

/**
 * Created by Rabbit Mao on 2015/8/19.
 */
@Controller
@RequestMapping("/report/finance")
public class ReportFinanceController extends BaseController {
    private static Logger logger = LogManager.getLogger(ReportFinanceController.class);
    @Resource
    private ReportFinanceService reportFinanceService;

    ShiroVelocity permissionLimit = new ShiroVelocity();

    @Resource
    private AccountService accountService;

    @Resource
    ReportAccountFinancialService reportAccountFinancialService;
    
    @Resource
    ConsignOrderService consignOrderService;

    @Resource
    OrganizationService organizationService;
    
    private final int MAX_RECORD_LENGTH = 65530;

    @RequestMapping("/uninvoiced.html")
    public void unInvoiced(ModelMap out) {
        setDefaultTime(out);
    }

    @RequestMapping("/unInvoiced/list.html")
    @ResponseBody
    public PageResult listUnInvoiced(String buyerName,
                                     int organization,
                                     String timeFrom,
                                     String timeTo,
                                     Integer start,
                                     Integer length) {
        PageResult result = new PageResult();
        Map<String, Object> param = new HashMap<>();
        if (StringUtils.isNotBlank(buyerName)) {
            param.put("buyerName", buyerName);
        }

        //屏蔽服务中心查询条件,默认查询条件值为当前服务中心
        if(permissionLimit.hasPermission(Constant.PERMISSION_ORG_LIMIT)) {
            User user = getLoginUser();
            organization = user.getOrgId() != null ? user.getOrgId().intValue() : 0;
        }

        if (organization != 0 && organization != -1) {
            param.put("organization", organization);
        }


        if (StringUtils.isNotBlank(timeFrom)) {
            param.put("timeFrom", timeFrom);
        }
        if (StringUtils.isNotBlank(timeTo)) {
            param.put("timeTo", timeTo);
        }
        param.put("userIds", getUserIds());
        Integer total = reportFinanceService.queryUnInvoicedList(param).size();  //总数

        param.put("start", start);
        param.put("length", length);
        List<UnInvoicedDto> unInvoicedDtoList = reportFinanceService.queryUnInvoicedList(param);
        result.setData(unInvoicedDtoList);
        result.setRecordsTotal(unInvoicedDtoList.size());
        result.setRecordsFiltered(total);

        return result;
    }

    @RequestMapping("/uninvoicedIn.html")
    public void unInvoicedIn(ModelMap out) {
        setDefaultTime(out);
    }

    @RequestMapping("/unInvoicedIn/list.html")
    @ResponseBody
    public PageResult listUnInvoicedIn(String sellerName,
                                     int organization,
                                     String timeFrom,
                                     String timeTo,
                                     Integer start,
                                     Integer length){
        PageResult result = new PageResult();
        Map<String, Object> param = new HashMap<>();
        if (StringUtils.isNotBlank(sellerName)) {
            param.put("sellerName", sellerName);
        }

        //屏蔽服务中心查询条件,默认查询条件值为当前服务中心
        if(permissionLimit.hasPermission(Constant.PERMISSION_ORG_LIMIT)) {
            User user = getLoginUser();
            organization = user.getOrgId() != null ? user.getOrgId().intValue() : 0;
        }

        if (organization != 0 && organization != -1) {
            param.put("organization", organization);
        }
        if(StringUtils.isNotBlank(timeFrom)) {
            param.put("timeFrom", timeFrom);
        }
        if(StringUtils.isNotBlank(timeTo)) {
            param.put("timeTo", timeTo);
        }
        param.put("userIds", getUserIds());
        Integer total = reportFinanceService.countUninvoicedInList(param);  //总数

        param.put("start", start);
        param.put("length", length);
        List<UninvoicedInDto> unInvoicedDtoList = reportFinanceService.queryUninvoicedInList(param);
        result.setData(unInvoicedDtoList);
        result.setRecordsTotal(unInvoicedDtoList.size());
        result.setRecordsFiltered(total);

        return result;
    }

    @RequestMapping("/unInvoiced/output.html")
    @ResponseBody
    public ModelAndView outputUnInvoiced(
            String timeFrom,
            String timeTo,
            String buyerName,
            int organization,
            @RequestParam(value = "selectIds", required = false) String selectIds) {
        ModelAndView mv = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<String, Object> param = new HashMap<>();
            if (StringUtils.isNotBlank(timeFrom)) {
                param.put("timeFrom", timeFrom);
            }
            if (StringUtils.isNotBlank(timeTo)) {
                param.put("timeTo", timeTo);
            }
            if (StringUtils.isNotBlank(buyerName)) {
                param.put("buyerName", new String(buyerName.getBytes("ISO-8859-1"), "UTF-8"));
            }

            //屏蔽服务中心查询条件,默认查询条件值为当前服务中心
            if(permissionLimit.hasPermission(Constant.PERMISSION_ORG_LIMIT)) {
                User user = getLoginUser();
                organization = user.getOrgId() != null ? user.getOrgId().intValue() : 0;
            }

            if (organization != 0 && organization != -1) {
                param.put("organization", organization);
            }
            param.put("userIds", getUserIds());
            if (StringUtils.isNotBlank(selectIds)) {
                JsonNode idsNode = mapper.readTree(selectIds);
                Long[] ids = mapper.readValue(idsNode, Long[].class);
                param.put("selectIds", ids);
            }
            List<UnInvoicedDto> unInvoicedDtoList = reportFinanceService.queryUnInvoicedList(param);

            Map<String, Object> dataMap = new HashMap<String, Object>();
            List<String> titles = new ArrayList<String>();
            titles.add("时间周期");
            titles.add("买家全称");
            titles.add("税号");
            titles.add("品名");
            titles.add("规格");
            titles.add("材质");
            titles.add("数量（吨）");
            titles.add("单价（元）");
            titles.add("金额");
            titles.add("税额");
            titles.add("价税合计");
            titles.add("代运营交易单号");
            titles.add("服务中心");
            dataMap.put("titles", titles);
            List<PageData> varList = new ArrayList<PageData>();
            for (UnInvoicedDto item : unInvoicedDtoList) {
                PageData vpd = new PageData();
                vpd.put("var1", (StringUtils.isNotBlank(timeFrom) ? timeFrom : "2010-1-1") + "至" + (StringUtils.isNotBlank(timeTo) ? timeTo : "今"));
                if (item.getDepartmentCount() > 1) {
                	vpd.put("var2", item.getBuyerName() +"【"+ item.getDepartmentName()+"】");
                } else {
                	vpd.put("var2", item.getBuyerName());
                } 
                vpd.put("var3", item.getTaxCode());
                vpd.put("var4", item.getNsortName());
                vpd.put("var5", item.getSpec());
                vpd.put("var6", item.getMaterial());
                vpd.put("var7", Constant.weightFormat.format(item.getWeight()));
                vpd.put("var8", Constant.amountFormat.format(item.getPrice()));
                vpd.put("var9", Constant.amountFormat.format(item.getNoTaxAmount()));
                vpd.put("var10", Constant.amountFormat.format(item.getTaxAmount()));
                vpd.put("var11", Constant.amountFormat.format(item.getTotalAmount()));
                vpd.put("var12", item.getOrderCode());
                vpd.put("var13", item.getOrgName());
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

    @RequestMapping("/unInvoicedIn/output.html")
    @ResponseBody
    public ModelAndView outputUnInvoicedIn(
            String timeFrom,
            String timeTo,
            String sellerName,
            int organization,
            @RequestParam(value = "selectIds", required = false)String selectIds){
        ModelAndView mv = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<String, Object> param = new HashMap<>();
            if(StringUtils.isNotBlank(timeFrom)) {
                param.put("timeFrom", timeFrom);
            }
            if(StringUtils.isNotBlank(timeTo)) {
                param.put("timeTo", timeTo);
            }
            if (StringUtils.isNotBlank(sellerName)) {
                param.put("sellerName", new String(sellerName .getBytes("ISO-8859-1"),"UTF-8"));
            }

            //屏蔽服务中心查询条件,默认查询条件值为当前服务中心
            if(permissionLimit.hasPermission(Constant.PERMISSION_ORG_LIMIT)) {
                User user = getLoginUser();
                organization = user.getOrgId() != null ? user.getOrgId().intValue() : 0;
            }

            if (organization != 0 && organization != -1) {
                param.put("organization", organization);
            }
            param.put("userIds", getUserIds());
            if(StringUtils.isNotBlank(selectIds)) {
                JsonNode idsNode = mapper.readTree(selectIds);
                Long[] ids = mapper.readValue(idsNode, Long[].class);
                param.put("selectIds", ids);
            }
            List<UninvoicedInDto> unInvoicedDtoList = reportFinanceService.queryUninvoicedInList(param);

            Map<String, Object> dataMap = new HashMap<String, Object>();
            List<String> titles = new ArrayList<String>();
            titles.add("时间周期");
            titles.add("卖家全称");
            titles.add("税号");
            titles.add("品名");
            titles.add("规格");
            titles.add("材质");
            titles.add("数量（吨）");
            titles.add("单价（元）");
            titles.add("金额");
            titles.add("税额");
            titles.add("价税合计");
            titles.add("代运营交易单号");
            titles.add("服务中心");
            dataMap.put("titles", titles);
            List<PageData> varList = new ArrayList<PageData>();
            for (UninvoicedInDto item : unInvoicedDtoList) {
                PageData vpd = new PageData();
                vpd.put("var1", (StringUtils.isNotBlank(timeFrom)?timeFrom:"2010-1-1") + "至" +  (StringUtils.isNotBlank(timeTo)?timeTo:"今"));
                if (item.getDepartmentCount() > 1) {
                	vpd.put("var2", item.getSellerName() +"【"+ item.getDepartmentName()+"】");
                } else {
                	vpd.put("var2", item.getSellerName());
                }
                vpd.put("var3", item.getTaxCode());
                vpd.put("var4", item.getNsortName());
                vpd.put("var5", item.getSpec());
                vpd.put("var6", item.getMaterial());
                vpd.put("var7", Constant.weightFormat.format(item.getWeight()));
                vpd.put("var8", Constant.amountFormat.format(item.getCostPrice()));
                vpd.put("var9", Constant.amountFormat.format(item.getNoTaxAmount()));
                vpd.put("var10", Constant.amountFormat.format(item.getTaxAmount()));
                vpd.put("var11", Constant.amountFormat.format(item.getAmount()));
                vpd.put("var12", item.getOrderCode());
                vpd.put("var13", item.getOrgName());
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

    @RequestMapping("/invoiceoutexpect.html")
    public void invoiceoutExpect(ModelMap out) {
        setDefaultTime(out);
    }

    @RequestMapping("/invoiceoutexpectdata.html")
    @ResponseBody
    public PageResult invoiceoutExpectData(@RequestParam(value = "orgid", required = false) Long orgId,
                                           @RequestParam(value = "buyerid", required = false) Long buyerid,
                                           @RequestParam(value = "sdate", required = true) String sdate,
                                           @RequestParam(value = "edate", required = true) String edate,
                                           @RequestParam(value = "start", required = true) Integer start,
                                           @RequestParam(value = "length", required = true) Integer length) {
        PageResult result = new PageResult();

        InvoiceoutExpectQuery query = buildInvoiceoutExpectQuery(orgId, buyerid, sdate, edate);

        int total = reportFinanceService.queryInvoiceoutExpectCount(query);
        result.setRecordsFiltered(total);
        if (total > 0) {
            query.setStart(start);
            query.setLength(length);
            List<InvoiceOutItemDetailDto> list = reportFinanceService.queryInvoiceoutExpect(query);
            result.setData(list);
            result.setRecordsTotal(list.size());
        } else {
            result.setData(new ArrayList<>());
            result.setRecordsTotal(0);
        }
        return result;
    }

    @RequestMapping("/invoiceoutexpecttoexcel.html")
    @ResponseBody
    public ModelAndView invoiceoutExpectToExcel(@RequestParam(value = "orgid", required = false) Long orgId,
                                                @RequestParam(value = "buyerid", required = false) Long buyerid,
                                                @RequestParam(value = "sdate", required = true) String sdate,
                                                @RequestParam(value = "edate", required = true) String edate) {
        ModelAndView mv = null;
        InvoiceoutExpectQuery query = buildInvoiceoutExpectQuery(orgId, buyerid, sdate, edate);
        List<InvoiceOutItemDetailDto> list = reportFinanceService.queryInvoiceoutExpect(query);

        Map<String, Object> dataMap = new HashMap<>();
        List<String> titles = new ArrayList<>();
        titles.add("编号");
        titles.add("买家全称");
        titles.add("品名");
        titles.add("规格");
        titles.add("材质");
        titles.add("数量（吨）");
        titles.add("单价（元）");
        titles.add("金额（元）");
        titles.add("税额（元）");
        titles.add("价税合计（元）");
        titles.add("代运营交易单号");
        dataMap.put("titles", titles);
        List<PageData> varList = new ArrayList<PageData>();
        for (InvoiceOutItemDetailDto item : list) {
            PageData vpd = new PageData();
            vpd.put("var1", item.getInvoiceOutMainId().toString());
            vpd.put("var2", item.getBuyerName());
            vpd.put("var3", item.getNsortName());
            vpd.put("var4", item.getSpec());
            vpd.put("var5", item.getMaterial());
            vpd.put("var6", item.getWeight().toString());
            vpd.put("var7", item.getPrice().toString());
            vpd.put("var8", item.getNoTaxAmount().toString());
            vpd.put("var9", item.getTaxAmount().toString());
            vpd.put("var10", item.getAmount().toString());
            vpd.put("var11", item.getCode());
            varList.add(vpd);
        }
        dataMap.put("varList", varList);
        ObjectExcelView erv = new ObjectExcelView();                //执行excel操作
        mv = new ModelAndView(erv, dataMap);

        return mv;
    }

    private InvoiceoutExpectQuery buildInvoiceoutExpectQuery(Long orgId, Long buyerid, String sdate, String edate) {
        InvoiceoutExpectQuery query = new InvoiceoutExpectQuery();

        User user = getLoginUser();
        if (orgId != null && orgId > 0) {
            query.setOrgId(orgId);
        } else if (orgId == null || !orgId.equals(0)) {//等于0为显示全部，其它值时只显示当前服务中心的数据
            query.setOrgId(user.getOrgId());
        }
        if (buyerid != null && buyerid != 0) {
            query.setBuyerId(buyerid);
        }
        if (StringUtils.isNotEmpty(sdate)) {
            query.setBeginTime(Tools.strToDate(sdate, Constant.TIME_FORMAT_DAY));
        }
        if (StringUtils.isNotEmpty(edate)) {
            query.setEndTime(Tools.strToDate(edate, Constant.TIME_FORMAT_DAY));
        }
        List<String> itemStatusList = new ArrayList<>();
        itemStatusList.add(InvoiceOutMainStatus.NO_INPUT.getCode());
        query.setItemStatusList(itemStatusList);
        List<String> applyStatusList = new ArrayList<>();
        applyStatusList.add(InvoiceOutApplyStatus.APPROVED.getValue());
        applyStatusList.add(InvoiceOutApplyStatus.PARTIAL_INVOICED.getValue());
        query.setApplyStatusList(applyStatusList);
        return query;
    }

    /*
    * 设置默认时间段：当前月
    * @param out
    */
    private void setDefaultTime(ModelMap out) {
        // 开单日期:开始时间、结束时间(当前月第一天、当前时间)
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        // 界面默认显示的开单时间
        out.put("startTime", format.format(cal.getTime()));
        out.put("endTime", format.format(new Date()));
    }

    /**
     * 进项发票清单界面
     *
     * @param out
     * @author dengxiyan
     */
    @RequestMapping("/invoiceinlist.html")
    public void invoiceInList(ModelMap out) {
        setDefaultTime(out);

    }

    /**
     * 加载进项发票清单数据
     *
     * @param queryParam
     * @return
     */
    @RequestMapping("/loadinvoiceindetailsdata.html")
    @ResponseBody
    public PageResult loadInvoiceInDetailsData(InvoiceInListQuery queryParam) {
        setInvoiceInListQueryParam(queryParam);

        //记录总数
        int recordsFiltered = reportFinanceService.queryInvoiceInDetailTotalByParams(queryParam);
        List<InvoiceInWithDetailDto> list = new ArrayList<>();
        if (recordsFiltered > 0) {
            list = reportFinanceService.queryInvoiceInDetailListByParams(queryParam);
        }
        return buildPageResult(list, recordsFiltered);
    }

    /**
     * 导出进项发票清单
     * 全部导出或导出选中行
     *
     * @param queryParam
     */
    @RequestMapping(value = "/buyerorderdetailexcel.html")
    public ModelAndView exportInvoiceInDetails(InvoiceInListQuery queryParam,@RequestParam("excelTitles") List<String> titlesList) {

        //excel表头和数据
        Map<String, Object> dataMap = new HashMap<>();

        setInvoiceInListQueryParam(queryParam);

        List<InvoiceInWithDetailDto> list = reportFinanceService.queryInvoiceInDetailListByParams(queryParam);

        //表头
        dataMap.put("titles", titlesList);

        //数据
        dataMap.put("varList", buildInvoiceInDetailsPageDataList(list));

        ObjectExcelView erv = new ObjectExcelView();
        return new ModelAndView(erv, dataMap);
    }

    private List<PageData> buildInvoiceInDetailsPageDataList(List<InvoiceInWithDetailDto> list){
        List<PageData> varList = new ArrayList<>();
        PageData vpd;
        if (list != null) {
            for (InvoiceInWithDetailDto item : list) {
                vpd = new PageData();
                vpd.put("var1",item.getCode());
                vpd.put("var2", item.getInvoiceDate());
                vpd.put("var3", item.getSellerName());
                vpd.put("var4", item.getNsortName());
                vpd.put("var5", item.getSpec());
                vpd.put("var6", item.getMaterial());
                vpd.put("var7", Tools.valueOfBigDecimal(item.getWeight()));
                vpd.put("var8", Tools.valueOfFormattedBigDecimal(item.getAmount(), Constant.MONEY_PRECISION));
                vpd.put("var9", item.getOrgName());
                vpd.put("var10", item.getStatus());
                varList.add(vpd);
            }
        }
        return varList;
    }

    private void setInvoiceInListQueryParam(InvoiceInListQuery queryParam) {
        if (queryParam != null) {
            queryParam.setUserIdList(getUserIds());
            if (StringUtils.isNotEmpty(queryParam.getCode())) {
                queryParam.setCode("%" + queryParam.getCode().trim() + "%");
            }
            if (StringUtils.isNotEmpty(queryParam.getSellerName())) {
                queryParam.setSellerName("%" + queryParam.getSellerName() + "%");
            }

            //屏蔽服务中心查询条件 默认查询条件值为当前服务中心
            if(permissionLimit.hasPermission(Constant.PERMISSION_ORG_LIMIT)) {
                queryParam.setOrgId(getLoginUser().getOrgId());
            }
        }
    }

    private PageResult buildPageResult(List<?> list, Integer recordsFiltered) {
        PageResult result = new PageResult();
        result.setData(list == null ? new ArrayList<>() : list);
        result.setRecordsTotal(list == null ? 0 : list.size());
        result.setRecordsFiltered(recordsFiltered);
        return result;
    }

    /**
     * 往来单位财务报表明细界面
     */
    @RequestMapping("/accountFundDetail.html")
    public void accountFundDetail(ReportAccountFundQuery query,ModelMap out){
        if (query != null && query.getAccountId() != null){
            Account account = accountService.queryById(query.getAccountId());
            if (account != null){
                query.setAccountName(account.getName());
                out.put("query", query);
            }
        }
    }

    /**
     * 加载往来单位财务明细数据
     * @param query
     * @return
     */
    @RequestMapping("/loadAccountFundDetail.html")
    @ResponseBody
    public PageResult loadAccountFundDetail(ReportAccountFundQuery query){
        int total = reportAccountFinancialService.queryAccountFinancialDetailTotalByParam(query);
        Map<String,Object> resultMap = reportAccountFinancialService.getAccountFinancialDetail(query);
        int size = ((Integer)resultMap.get("size")).intValue();
        return new PageResult(size,total,(List)resultMap.get("data"));
    }

    /**
     * 往来单位财务明细数据导出
     * @param query
     * @return
     */
    @RequestMapping("/exportAccountFundDetail.html")
    public ModelAndView exportAccountFundDetail(ReportAccountFundQuery query){
    	query.setLength(MAX_RECORD_LENGTH);
        Map<String,Object> resultMap = reportAccountFinancialService.getAccountFinancialDetail(query);
        List<ReportAccountFundDetailDto> detailDtoList = (List<ReportAccountFundDetailDto>)resultMap.get("data");

        //excel表头和数据
        Map<String, Object> dataMap = new HashMap<>();

        //表头
        dataMap.put("titles", Arrays.asList("时间","合同号","订单号","银行流水号","实际采购交易金额（元）"
                                          ,"实际销售交易金额(元)","银行存款发生金额（元）","应收账款（元）","摘要"));
        //数据
        dataMap.put("varList", buildAccountFundDetailDataList(detailDtoList));

        ObjectExcelView erv = new ObjectExcelView();
        return new ModelAndView(erv, dataMap);
    }


    private List<PageData> buildAccountFundDetailDataList(List<ReportAccountFundDetailDto> detailDtoList){
        List<PageData> varList = new ArrayList<>();
        if (detailDtoList != null) {
            detailDtoList.forEach(a->{
                PageData vpd = new PageData();
                vpd.put("var1",a.getDateStr());
                vpd.put("var2",a.getContractCode());
                vpd.put("var3",a.getOrderCode());
                vpd.put("var4", a.getSerialCode());
                setExcelAmount("var5", a.getPurchaseAmount(), vpd);
                setExcelAmount("var6",a.getSaleAmount(),vpd);
                setExcelAmount("var7",a.getBankHappenAmount(),vpd);
                setExcelAmount("var8",a.getCurrentBalance(),vpd);
                vpd.put("var9", a.getRemark());
                varList.add(vpd);
            });
        }
        return varList;
    }

    /**
     * 往来单位财务报表界面
     */
    @RequestMapping("/accountFund.html")
    public void accountFund(ModelMap out){
       setDefaultTime(out);
    }

    /**
     * 加载往来单位财务数据
     * @param query
     * @return
     */
    @RequestMapping("/loadAccountFund.html")
    @ResponseBody
    public PageResult loadAccountFund(ReportAccountFundQuery query){
        int total = reportAccountFinancialService.getTotal(query);
        List list = null;
        if (total > 0){
            list = reportAccountFinancialService.getAccountFinancialList(query);
        }
        return  buildPageResult(list,total);
    }

    /**
     * 往来单位财务数据导出
     * @param query
     * @return
     */
    @RequestMapping("/exportAccountFund.html")
    public ModelAndView exportAccountFund(ReportAccountFundQuery query){

        query.setLength(MAX_RECORD_LENGTH);//导出excel最大条数

        List<ReportAccountFundDto> list  = reportAccountFinancialService.getAccountFinancialList(query);

        //excel表头和数据
        Map<String, Object> dataMap = new HashMap<>();

        //表头
        dataMap.put("titles", Arrays.asList("客户名称","期初余额（元）","实际销售交易金额（元）","已收金额（元）","实际采购交易金额（元）"
                ,"已付金额（元）","期末余额（元）"));
        //数据
        dataMap.put("varList", buildAccountFundDataList(list));

        ObjectExcelView erv = new ObjectExcelView();
        return new ModelAndView(erv, dataMap);
    }


    private List<PageData> buildAccountFundDataList(List<ReportAccountFundDto> list){
        List<PageData> varList = new ArrayList<>();
        if (list != null) {
            list.forEach(a->{
                PageData vpd = new PageData();
                vpd.put("var1", a.getAccountName());
                setExcelAmount("var2", a.getInitialBalance(), vpd);
                setExcelAmount("var3",a.getSaleAmount(),vpd);
                setExcelAmount("var4",a.getReceivedAmount(),vpd);
                setExcelAmount("var5",a.getPurchaseAmount(),vpd);
                setExcelAmount("var6",a.getPayedAmount(),vpd);
                setExcelAmount("var7", a.getEndingBalance(), vpd);
                varList.add(vpd);
            });
        }
        return varList;
    }

    /**
     * 客户销项报表列表跳转
     * @author tuxianming
     */
    @RequestMapping("/buyerinvoiceoutlist.html")
	public void buyerInvoiceOut(ModelMap out) {
    	setDefaultTime(out);
	}
    
    /**
     * 客户销项报表列表
     * @param query: buyName, startTime, endTime, length, start
     * @author tuxianming
     */
    @ResponseBody
    @RequestMapping("/loadbuyerinvoiceoutlist.html")
    public PageResult loadBuyerInvoiceOut(ReportBuyerInvoiceOutQuery query) {

    	logger.debug("execute loadBuyerInvoiceOutList()");
    	
    	query.setEndTime(addOneDay(query.getEndTime()));
    	
    	List<ReportBuyerInvoiceOutDto> data = reportFinanceService.queryReportBuyerInvoiceOutByPage(query);
    	Integer total = reportFinanceService.queryReportBuyerInvoiceOutTotalByPage(query);  //总数

    	PageResult result = new PageResult();
    	result.setData(data);
        result.setRecordsTotal(data.size());
        result.setRecordsFiltered(total);
    	return result;
    }

    private void setExcelAmount(String var, BigDecimal data, PageData vpd) {
        if(null != data && data.doubleValue() != 0) {
            vpd.put(var, Tools.valueOfFormattedBigDecimal(data, Constant.MONEY_PRECISION));
        }else {
            vpd.put(var,"");
        }
    }
    /**
     * 客户销项报表详情跳转
     * @param out
     * @author tuxianming
     */
    @RequestMapping("/buyerinvoiceout.html")
    public void buyerInvoiceOutDetail(ModelMap out, 
    		String startTime,
            String endTime, 
    		@RequestParam(value = "buyerId") String buyerId) {
    	
    	out.put("startTime", startTime);
    	out.put("endTime", endTime);
    	out.put("buyerId", buyerId);
    	
    	try {
    		ReportBuyerInvoiceOut record = reportFinanceService.getBuyerInvoceOut(Long.parseLong(buyerId));
    		out.put("buyName", record.getBuyName());
		} catch (Exception e) {
			logger.info(e.getMessage(), e);
		}
    	
    }
 
    /**
     * 客户销项报表详情
     * @param query query: buyerId, startTime, endTime, start, length
     * @return
     */
    @ResponseBody
    @RequestMapping("/loadbuyerinvoiceout.html")
    public PageResult loadBuyerInvoiceOutDetail(ReportBuyerInvoiceOutQuery query) {

    	logger.debug("execute loadBuyerInvoiceOutDetail()");
    	
    	query.setEndTime(addOneDay(query.getEndTime()));
    	
    	List<ReportBuyerInvoiceOutDto> data = reportFinanceService.queryReportBuyerInvoiceOutDetailByPage(query);
    	data.forEach(d -> d.setDateStr(Tools.dateToStr(d.getCreated(), "yyyy/MM/dd HH:mm:ss")));
    	
    	Integer total = reportFinanceService.queryReportBuyerInvoiceOutDetailTotalByPage(query);  //总数
    	
    	PageResult result = new PageResult();
    	result.setData(data);
        result.setRecordsTotal(data.size());
        result.setRecordsFiltered(total);
    	return result;
    }

    /**
     * 客户销项报表详情: 期初余额，期末余额, 
     * @param query query: buyerId, startTime, endTime
     * @return
     * @author tuxianming
     */
    @ResponseBody
    @RequestMapping("/loadbuyerinvoiceoutsum.html")
    public ReportBuyerInvoiceOutDto loadBuyerInvoiceOutSum(ReportBuyerInvoiceOutQuery query){
    	
    	query.setEndTime(addOneDay(query.getEndTime()));
    	query.setLength(1);
    	query.setStart(0);
    	
    	List<ReportBuyerInvoiceOutDto> data = reportFinanceService.queryReportBuyerInvoiceOutByPage(query);
    	ReportBuyerInvoiceOutDto resDto = null;
    	if(!data.isEmpty()){
    		resDto = data.get(0);
    		if(resDto.getStartAmount()!=null){
    			resDto.setStartAmount(resDto.getStartAmount().setScale(2,BigDecimal.ROUND_HALF_UP));
    		}else
    			resDto.setStartAmount(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP));
    		
    		if(resDto.getUnmakeOutAmount()!=null){
    			resDto.setUnmakeOutAmount(resDto.getUnmakeOutAmount().setScale(2,BigDecimal.ROUND_HALF_UP));
    		}else
    			resDto.setUnmakeOutAmount(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP));
    		
    		if(resDto.getActualOcurAmount()!=null){
    			resDto.setActualOcurAmount(resDto.getActualOcurAmount().setScale(2,BigDecimal.ROUND_HALF_UP));
    		}else
    			resDto.setActualOcurAmount(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP));
    		
    		if(resDto.getMakeOutAmount()!=null){
    			resDto.setMakeOutAmount(resDto.getMakeOutAmount().setScale(2,BigDecimal.ROUND_HALF_UP));
    		}else
    			resDto.setMakeOutAmount(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP));
    	}
    	
    	return resDto;
    }
    
    /**
     * 
     * 客户销项报表列表导出为excel报表
     * @param query query: String buyerName,String startTime,String endTime
     * @return
     * @author tuxianming
     */
    @RequestMapping("/exportbuyerinvoiceoutlistexcel.html")
    public ModelAndView exportBuyerInvoiceOutListExcel(ReportBuyerInvoiceOutQuery query){
    	
    	query.setEndTime(addOneDay(query.getEndTime()));
    	query.setLength(null);
    	query.setStart(null);
    	List<ReportBuyerInvoiceOutDto> datas = reportFinanceService.queryReportBuyerInvoiceOutByPage(query);
    	
    	//build excel 数据结构
    	Map<String, Object> dataMap = new HashMap<>();
        //表头
    	List<String> titles = Arrays.asList("客户名称","期初未开销项金额（元）","销售实提金额（元）","已开销项金额（元）","未开销项金额（元）");
        dataMap.put("titles", titles);
        //数据内容
        List<PageData> varList = new ArrayList<PageData>();
        if(datas!=null){
        	for (ReportBuyerInvoiceOutDto item : datas) {
                PageData vpd = new PageData();
                vpd.put("var1", item.getBuyName());
                setExcelAmount("var2",item.getStartAmount(), vpd);
                setExcelAmount("var3",item.getActualOcurAmount(), vpd);
                setExcelAmount("var4",item.getMakeOutAmount(), vpd);
                setExcelAmount("var5",item.getUnmakeOutAmount(), vpd);
                varList.add(vpd);
            }
        }
        dataMap.put("varList", varList);
        ObjectExcelView erv = new ObjectExcelView(); //执行excel操作
        return new ModelAndView(erv, dataMap);
    }
    
    /**
     * 客户销项报表详情导出为excel报表
     * @param query query: buyerId, startTime, endTime
     * @return
     */
    @RequestMapping("/exportbuyerinvoiceoutexcel.html")
    public ModelAndView exportBuyerInvoiceOutExcel(ReportBuyerInvoiceOutQuery query){
    	
    	query.setEndTime(addOneDay(query.getEndTime()));
    	
    	//得到所有的数据
    	List<ReportBuyerInvoiceOutDto> datas = reportFinanceService.queryReportBuyerInvoiceOutDetailByPage(query);
    	ReportBuyerInvoiceOutDto buyerInvoiceOutCount =  this.loadBuyerInvoiceOutSum(query); 
    	
    	//build excel 数据结构
    	Map<String, Object> dataMap = new HashMap<>();
        //表头
    	List<String> titles = Arrays.asList("时间","合同号","单号","交易发生额（元）","销项票发生额（元）","应开销项余额（元）","摘要");
        dataMap.put("titles", titles);
        //数据内容
        List<PageData> varList = new ArrayList<PageData>();
        if(datas!=null){
        	for (ReportBuyerInvoiceOutDto item : datas) {
                PageData vpd = new PageData();
                vpd.put("var1", Tools.dateToStr(item.getCreated(),"yyyy/MM/dd HH:mm:ss"));
                vpd.put("var2", item.getContractCode());
                vpd.put("var3", item.getOrderCode());
                setExcelAmount("var4",item.getOrderAmount(), vpd);
                setExcelAmount("var5",item.getInvoiceOutAmount(), vpd);
                setExcelAmount("var6",item.getInvoiceOutBalance(), vpd);
                vpd.put("var7", item.getRemark());
                varList.add(vpd);
            }
        }
        if(buyerInvoiceOutCount!=null){
        	//第一条
        	PageData vpd = new PageData();
            vpd.put("var1", "期初余额（元");
            vpd.put("var2", "");
            vpd.put("var3", "");
            vpd.put("var4", "");
            vpd.put("var5", "");
            setExcelAmount("var6",buyerInvoiceOutCount.getStartAmount(), vpd);
            vpd.put("var7", "");
            varList.add(0, vpd);
            
            //最后一条
            PageData vpd1 = new PageData();
            vpd1.put("var1", "期末余额（元）");
            vpd1.put("var2", "");
            vpd1.put("var3", "");
            setExcelAmount("var4",buyerInvoiceOutCount.getActualOcurAmount(), vpd1);
            setExcelAmount("var5",buyerInvoiceOutCount.getMakeOutAmount(), vpd1);
            setExcelAmount("var6",buyerInvoiceOutCount.getUnmakeOutAmount(), vpd1);
            vpd1.put("var7", "");
            varList.add(vpd1);
            
        }
        
        dataMap.put("varList", varList);
        ObjectExcelView erv = new ObjectExcelView(); //执行excel操作
        return new ModelAndView(erv, dataMap);
    }
    
    /**
     * 供应商进项报表
     * @author dq
     */
    @RequestMapping("/reportsellerinvoicein.html")
	public void reportSellerInvoiceIn(ModelMap out) {
        setDefaultTime(out);
    }
    
    /**
     * 供应商进项报表详情页面
     * @author dq
     */
    @RequestMapping("/reportsellerinvoiceindetail.html")
	public void reportSellerInvoiceInDetail(ReportSellerInvoiceInDataQuery query,ModelMap out) {
    	if(null == query.getSellerId() || "".equals(query.getSellerId())) {
    		throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,
					"客户信息错误");
    	}
    	out.put("sellerId", query.getSellerId());
    	Account account = accountService.queryById(query.getSellerId());
    	out.put("sellerName", account.getName());
        out.put("startTime", Tools.dateToStr(query.getStartTime(),"yyyy-MM-dd"));
        out.put("endTime", Tools.dateToStr(query.getEndTime(),"yyyy-MM-dd"));
    }
    
    /**
     * 供应商进项报表数据
     * @author dq
     */
    @ResponseBody
    @RequestMapping("/reportsellerinvoiceindata.html")
    public PageResult reportSellerInvoiceInData(ReportSellerInvoiceInDataQuery query) {
    	PageResult result = new PageResult();
    	Integer total = reportFinanceService.queryReportSellerInvoiceInDataCount(query);  //总数
    	result.setRecordsFiltered(total);

    	List<ReportSellerInvoiceInDto> data = reportFinanceService.queryReportSellerInvoiceInData(query);
    	result.setData(data);
    	result.setRecordsTotal(data.size());
    	
    	return result;
    }
    
    /**
     * 供应商进项报表详情数据
     * @author dq
     */
    @ResponseBody
    @RequestMapping("/reportsellerinvoiceindetaildata.html")
    public PageResult reportSellerInvoiceInDetailData(ReportSellerInvoiceInDataQuery query) {
    	PageResult result = new PageResult();
    	Integer total = reportFinanceService.queryReportSellerInvoiceInDetailDataCount(query);  //总数
    	result.setRecordsFiltered(total);

    	List<ReportSellerInvoiceIn> data = reportFinanceService.queryReportSellerInvoiceInDetailData(query);
    	result.setData(data);
    	result.setRecordsTotal(data.size());
    	
    	return result;
    }
    
    /**
	 * 供应商进项报表 - 条件范围 - 期初期末金额
	 * @author dq
	 */
	@ResponseBody
	@RequestMapping(value = "reportsellerinvoiceinrangeamount.html", method = RequestMethod.POST)
	public Result reportSellerInvoiceInRangeAmount(ReportSellerInvoiceInDataQuery query) {
		Result result = new Result();
		ReportSellerInvoiceInDto reportSellerInvoiceInDto = reportFinanceService.queryReportSellerInvoiceInRangeAmount(query);
		result.setData(reportSellerInvoiceInDto);
		result.setSuccess(Boolean.TRUE);
		return result;
	}
	
	/**
     * 供应商进项报表 导出excel
     * @author dq
     */
    @RequestMapping("/reportsellerinvoiceinexcel.html")
    public ModelAndView reportSellerInvoiceInExcel(ReportSellerInvoiceInDataQuery query){
    	query.setLength(MAX_RECORD_LENGTH);
    	List<ReportSellerInvoiceInDto> datas = reportFinanceService.queryReportSellerInvoiceInData(query);
    	
    	//build excel 数据结构
    	Map<String, Object> dataMap = new HashMap<>();
        //表头
    	List<String> titles = Arrays.asList("供应商名称","期初未收进项金额（元）","采购实提金额（元）","已收进项金额（元）","未收进项金额（元）");
        dataMap.put("titles", titles);
        //数据内容
        List<PageData> varList = new ArrayList<PageData>();
        if(datas!=null){
        	for (ReportSellerInvoiceInDto data : datas) {
                PageData vpd = new PageData();
                vpd.put("var1", data.getSellerName());
                setExcelAmount("var2",data.getPrimeBalance(), vpd);
                setExcelAmount("var3",data.getActualAmount(), vpd);
                setExcelAmount("var4",data.getInvoiceInAmount(), vpd);
                setExcelAmount("var5",data.getTerminalBalance(), vpd);
                varList.add(vpd);
            }
        }
        dataMap.put("varList", varList);
        ObjectExcelView erv = new ObjectExcelView(); //执行excel操作
        return new ModelAndView(erv, dataMap);
    }
    
    /**
     * 供应商进项报表 导出excel
     * @author dq
     */
    @RequestMapping("/reportsellerinvoiceindetailexcel.html")
    public ModelAndView reportSellerInvoiceInDetailExcel(ReportSellerInvoiceInDataQuery query){
    	query.setLength(MAX_RECORD_LENGTH);
    	List<ReportSellerInvoiceIn> datas = reportFinanceService.queryReportSellerInvoiceInDetailData(query);
    	
    	//build excel 数据结构
    	Map<String, Object> dataMap = new HashMap<>();
        //表头
    	List<String> titles = Arrays.asList("时间","合同号","订单号","交易发生额（元）","进项票发生额（元）","应收进项余额（元）","摘要");
        dataMap.put("titles", titles);
        //数据内容
        List<PageData> varList = new ArrayList<PageData>();
        if(datas!=null){
        	for (ReportSellerInvoiceIn data : datas) {
                PageData vpd = new PageData();
                vpd.put("var1", Tools.dateToStr(data.getHappenTime(), "yyyy-MM-dd"));
                vpd.put("var2", data.getContractCode());
                vpd.put("var3", data.getOrderCode());
                setExcelAmount("var4",data.getOrderAmount(), vpd);
                setExcelAmount("var5",data.getInvoiceInAmount(), vpd);
                setExcelAmount("var6",data.getInvoiceInBalance(), vpd);
                vpd.put("var7", data.getRemark());
                varList.add(vpd);
            }
        }
        ReportSellerInvoiceInDto reportSellerInvoiceInDto = reportFinanceService.queryReportSellerInvoiceInRangeAmount(query);
        if(null != reportSellerInvoiceInDto){
        	//第一条
        	PageData vpd = new PageData();
            vpd.put("var1", "期初余额（元）");
            vpd.put("var2", "");
            vpd.put("var3", "");
            vpd.put("var4", "");
            vpd.put("var5", "");
            setExcelAmount("var6", reportSellerInvoiceInDto.getPrimeBalance(),vpd);
            vpd.put("var7", "");
            varList.add(0, vpd);
            //最后一条
            PageData vpd1 = new PageData();
            vpd1.put("var1", "期末余额（元）");
            vpd1.put("var2", "");
            vpd1.put("var3", "");
            setExcelAmount("var4", reportSellerInvoiceInDto.getActualAmount(),vpd1);
            setExcelAmount("var5", reportSellerInvoiceInDto.getInvoiceInAmount(),vpd1);
            setExcelAmount("var6", reportSellerInvoiceInDto.getTerminalBalance(),vpd1);
            vpd1.put("var7", "");
            varList.add(vpd1);
        }
        dataMap.put("varList", varList);
        ObjectExcelView erv = new ObjectExcelView(); //执行excel操作
        return new ModelAndView(erv, dataMap);
    }
    
    private Date addOneDay(Date date){
    	if(date!=null){
    		
    		Calendar cal = Calendar.getInstance();
    		cal.setTime(date);
    		cal.add(Calendar.DAY_OF_MONTH, 1);
    		return cal.getTime();
    	}
    	return null;
    }
 
	/**
     * 利润报表初始化
     * @author tuxianming
     * @param out
     */
    @RequestMapping("/incomesummary.html")
    public void incomesummary(ModelMap out) {
    	setDefaultTime(out);
    	
    	List<Organization> orgs = organizationService.queryAllBusinessOrg();
    	out.put("orgs", orgs);
    }
	
    /**
     * 利润报表：按 卖家 ->品名 统计 
     * 		  按品名 -> 卖家统计 
     * @author tuxianming 
     * @param query  
     * @return
     */
    @RequestMapping("/incomesummarybyseller.html")
    @ResponseBody
    public PageResult loadIncomeSummaryBySeller(IncomeSummaryQuery query) {
    	PageResult result = new PageResult();
    	query.setEndDate(addOneDay(query.getEndDate()));
    	List<IncomeSummaryDto> datas = incomeSummaryConvertToList(reportFinanceService.queryIncomeSummaryForSeller(query));
    	IncomeSummaryDto total = reportFinanceService.totalIncomeSummaryForSeller(query);
    	if(total!=null){
    		total.setName("总计：");
        	datas.add(total);
    	}
    	result.setData(datas);
    	return result;
    }
    
    /**
     *  利润报表：
     *  	按 买家 ->品名 统计 
     *  	按 品名 ->买家 统计 
     * @author tuxianming 
     * @param query
     * @return
     */
    @RequestMapping("/incomesummarybybuyer.html")
    @ResponseBody
    public PageResult loadIncomeSummaryByBuyer(IncomeSummaryQuery query) {
    	PageResult result = new PageResult();
    	query.setEndDate(addOneDay(query.getEndDate()));
    	List<IncomeSummaryDto> datas = incomeSummaryConvertToList(reportFinanceService.queryIncomeSummaryForBuyer(query));
    	IncomeSummaryDto total = reportFinanceService.totalIncomeSummaryForBuyer(query);
    	if(total!=null){
    		total.setName("总计：");
        	datas.add(total);
    	}
    	result.setData(datas);
    	return result;
    }
    
    /**
     * 导出利润报表： query.getGroupType : seller-category, category-seller, buyer-category, category-buyer
     * @param query
     * @return
     * @author tuxianming
     */
    @RequestMapping("/exportincomesummary.html")
    @ResponseBody
    public ModelAndView exportIncomeSummary(IncomeSummaryQuery query) {
    	String account;
    	String[] types = query.getGroupType().split("-");
    	
    	List<String> titles = new ArrayList<String>();
    	titles.add("服务中心");
    	
    	query.setGroupType(types[0]);
    	if(types[0].equals("category")){
    		account = types[1];
    		if(types[1].equals("seller")){
    			titles.add("品名");
    			titles.add("卖家");
    		}else{
    			titles.add("品名");
    			titles.add("买家");
    		}
    		
    	}else{
    		account = types[0];
    		if(types[0].equals("seller")){
    			titles.add("卖家");
    			titles.add("品名");
    		}else{
    			titles.add("买家");
    			titles.add("品名");
    		}
    	}
    	
    	Map<String, Map<String, List<IncomeSummaryDto>>> datas = null;
    	IncomeSummaryDto total = null;
    	if(account.equals("buyer")) {
    		datas = reportFinanceService.queryIncomeSummaryForBuyer(query);
    		total= reportFinanceService.totalIncomeSummaryForBuyer(query);
    	}else{
    		datas = reportFinanceService.queryIncomeSummaryForSeller(query);
    		total= reportFinanceService.totalIncomeSummaryForSeller(query);
    	}
    	
    	Map<String, Object> dataMap = new HashMap<String, Object>();
        titles.add("重量（吨）");
        titles.add("采购金额（元）");
        titles.add("销售金额（元）");
        titles.add("税前利润（元）");
        dataMap.put("titles", titles);
        List<PageData> varList = new ArrayList<PageData>();
        
        List<String> merges = new ArrayList<>();
        //merges.add("0,0,0,3");
        dataMap.put("merges", merges);
        
        int index = 0;
        for (String key : datas.keySet()) {
			
        	int mergeCount = 0;
        	Map<String, List<IncomeSummaryDto>> subDatas = datas.get(key);
        	for (String subKey : subDatas.keySet()) {
				
        		List<IncomeSummaryDto> dtos = subDatas.get(subKey);
        		int subMergeCount  = 0;
        		for (IncomeSummaryDto dto : dtos) {
        			
        			PageData vpd = new PageData();
                	if(mergeCount==0)
                		vpd.put("var1", dto.getName());
                	
                	if(types[0].equals("category")){
             			if(subMergeCount==0)
             				vpd.put("var2", dto.getNsortName());
             			vpd.put("var3", dto.getAccountName());
                 	}else{
                 		if(subMergeCount==0)
                 			vpd.put("var2", dto.getAccountName());
             			vpd.put("var3", dto.getNsortName());
                 	}
                	 
                	setExcelWeight("var4", dto.getWeight(), vpd);
                	setExcelAmount("var5", dto.getAmount(), vpd);
                	setExcelAmount("var6", dto.getSaleAmount(), vpd);
                	setExcelAmount("var7", dto.getIncomeAmount(), vpd);
                	varList.add(vpd);
        			
        			subMergeCount++;
				}
        		mergeCount += dtos.size();
        		index += dtos.size();
        		
        		if(subMergeCount>1)
        			merges.add((index-subMergeCount+1)+","+(index)+",1,1");
        		subMergeCount=0;
			}
        	if(mergeCount>1){
        		int startIndex = index-mergeCount+1;
        		merges.add((startIndex)+","+index+",0,0");
        	}
        	mergeCount = 0;
		}
        
        if(total!=null){
        	PageData vpd = new PageData();
        	vpd.put("var1", "总计：");
    		setExcelWeight("var4", total.getWeight(), vpd);
        	setExcelAmount("var5", total.getAmount(), vpd);
        	setExcelAmount("var6", total.getSaleAmount(), vpd);
        	setExcelAmount("var7", total.getIncomeAmount(), vpd);
        	varList.add(vpd);
        }
        
        dataMap.put("varList", varList);
        ObjectExcelView erv = new ObjectExcelView();
        ModelAndView mv = new ModelAndView(erv, dataMap);
        return mv;
    	
    }
    
    private List<IncomeSummaryDto> incomeSummaryConvertToList(Map<String, Map<String, List<IncomeSummaryDto>>> sortMaps){
    	//转换成数组
		List<IncomeSummaryDto> incomes = new ArrayList<IncomeSummaryDto>();
		for (String key : sortMaps.keySet()) {
			Map<String,List<IncomeSummaryDto>> buyerSortMap = sortMaps.get(key);
			for (String subKey : buyerSortMap.keySet()) {
				incomes.addAll(buyerSortMap.get(subKey));
			}
		}
		return incomes;
    }
    
    private void setExcelWeight(String var, BigDecimal data, PageData vpd) {
		if(null != data && data.doubleValue() != 0) {
			vpd.put(var, Tools.valueOfFormattedBigDecimal(data, Constant.WEIGHT_PRECISION));
		}else {
			vpd.put(var,"");
		}
	}
    
    /**
     * 初次付款信息报表初始化
     * @author lixiang
     * @param out
     */
    @RequestMapping("/initialpayment.html")
    public void initialPayment(ModelMap out) {
    	setDefaultTime(out);
        OrderPayMentQuery orderPayMentQuery = new OrderPayMentQuery();
        orderPayMentQuery.setStartTime(out.get("startTime").toString());
        orderPayMentQuery.setEndTime(out.get("endTime").toString());
    	OrderPayMentDto orderPayMent = consignOrderService.totalFirstPayMent(orderPayMentQuery);
    	out.put("orderPayMent", orderPayMent);
    }
    
    @ResponseBody
    @RequestMapping("/total.html")
   	public Result getTotal(OrderPayMentQuery orderPayMentQuery) {
		Result result = new Result();
		try {
    		orderPayMentQuery.setEndTime(getDateEndStr(orderPayMentQuery));
		} catch (ParseException e) {
			logger.error(e.toString());
		}
		OrderPayMentDto orderPayMent = consignOrderService.totalFirstPayMent(orderPayMentQuery);
		result.setData(orderPayMent);
       	return result;
   	}
    
    /**
     * 初次付款信息报表列表查询
     * @author lixiang
     * @param orderPayMentQuery
     * @return
     */
    @RequestMapping("/select/initialpayment.html")
    @ResponseBody
    public PageResult queryInitialPayment(OrderPayMentQuery orderPayMentQuery) {
    	PageResult result = new PageResult();
    	try {
    		orderPayMentQuery.setEndTime(getDateEndStr(orderPayMentQuery));
		} catch (ParseException e) {
			logger.error(e.toString());
		}
    	List<OrderPayMentDto> list = consignOrderService.queryFirstPayMent(orderPayMentQuery);
    	int counts = consignOrderService.queryFirstPayMentCount(orderPayMentQuery);
    	result.setRecordsFiltered(counts);
		result.setRecordsTotal(counts);
		result.setData(list);
		result.setRecordsTotal(list.size());
		return result;
    }
    
    /**
	 * 终止日期（String类型）
	 * 
	 * @param orderPayMentQuery
	 * @return
	 * @throws ParseException
	 */
	private String getDateEndStr(OrderPayMentQuery orderPayMentQuery)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		try {
			date = sdf.parse(orderPayMentQuery.getEndTime());
		} catch (ParseException e) {
			logger.error(e.toString());
		}
		Calendar calendar = Calendar.getInstance(); // 得到日历
		calendar.setTime(date);// 把当前时间赋给日历
		calendar.add(Calendar.DAY_OF_MONTH, +1); // 设置为后一天
		String day = sdf.format(calendar.getTime());
		return day;
	}
	
	 /**
     * 买家交易报表导出excel
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/initialpaymentexcel.html")
    public ModelAndView invoiceKeepingExcel(OrderPayMentQuery orderPayMentQuery) {
    	try {
    		orderPayMentQuery.setEndTime(getDateEndStr(orderPayMentQuery));
		} catch (ParseException e) {
			logger.error(e.toString());
		}
        ModelAndView mv = null;
        orderPayMentQuery.setLength(MAX_RECORD_LENGTH);
        try {
        	 Map<String, Object> dataMap = new HashMap<>();
             List<String> titles = new ArrayList<>();
             titles.add("买家全称");
             titles.add("卖家全称");
             titles.add("订单号");
             titles.add("初次付款金额");
             titles.add("初次申请时间");
             titles.add("初次付款时间");
             titles.add("订单状态");
             titles.add("核算会计");
             titles.add("买家服务中心");
             titles.add("出款银行");
             dataMap.put("titles", titles);
             List<PageData> varList = new ArrayList<>();
             List<OrderPayMentDto> list = consignOrderService.queryFirstPayMent(orderPayMentQuery);
             for (OrderPayMentDto data : list) {
                 PageData vpd = new PageData();
                 vpd.put("var1", data.getBuyerName());
                 vpd.put("var2", data.getSellerName());
                 vpd.put("var3", data.getCode());
                 setExcelAmount("var4",data.getPayAmount(), vpd);
                 vpd.put("var5", Tools.dateToStr(data.getFirstApplyTime(), "yyyy-MM-dd HH:mm:ss"));
                 vpd.put("var6", Tools.dateToStr(data.getFirstPayTime(), "yyyy-MM-dd HH:mm:ss"));
                 if("4".equals(data.getStatus())) {
                	 vpd.put("var7", "已关联"); 
				 } else if ("5".equals(data.getStatus())) {
					vpd.put("var7", "请求关闭");
				 } else if ("6".equals(data.getStatus())) {
					vpd.put("var7", "待二次结算");
				 } else if ("7".equals(data.getStatus())) {
					vpd.put("var7", "待开票申请");
				 } else if ("8".equals(data.getStatus())) {
					vpd.put("var7", "待开票");
				 } else if ("10".equals(data.getStatus())) {
					vpd.put("var7", "交易完成");
				 } else if ("-3".equals(data.getStatus())) {
					vpd.put("var7", "订单关闭-5点半未关联（待关联）订单");
				 } else if ("-4".equals(data.getStatus())) {
					vpd.put("var7", "订单关闭-确认付款关闭");
				 } else if ("-5".equals(data.getStatus())) {
					vpd.put("var7", "申请关闭订单");
				 } else if ("-6".equals(data.getStatus())) {
					vpd.put("var7", "总经理审核通过关闭订单");
				 } else if ("-7".equals(data.getStatus())) {
					vpd.put("var7", "订单关闭-财务审核通过关闭订单");
				 } else if ("-8".equals(data.getStatus())) {
					vpd.put("var7", "订单关闭-财务审核通过付款后关闭订单");
				 }
                 vpd.put("var8", data.getAccounting());
                 vpd.put("var9", data.getBuyerOrgName()); 
                 if ("SPD".equals(data.getPaymentBank())) {
                	vpd.put("var10", "浦发银行");
                 } else if ("ICBC".equals(data.getPaymentBank())) {
                	 vpd.put("var10", "工商银行");
                 }
                 varList.add(vpd);
            }
            dataMap.put("varList", varList);
            ObjectExcelView erv = new ObjectExcelView(); //执行excel操作
            mv = new ModelAndView(erv, dataMap);
        } catch (Exception e) {
        	System.out.println("error:"+e);
            // handle error
        }
        return mv;
    }
    
    
    /**
     * 应收应付发票报表跳转 
     * 20160614
     * @param out
     * @author tuxianming
     */
    @RequestMapping("/invoiceinandout.html")
    public void invoiceInAndOut(ModelMap out) {
    	setDefaultTime(out);
    }
 
    /**
     * 应收发票报表
     * 20160614
     * @param query query: accountId, startTime, endTime, start, length
     * @author tuxianming
     * @return
     */
    @ResponseBody
    @RequestMapping("/loadinvoicein.html")
    public PageResult loadInvoiceIn(ReportInvoiceInAndOutQuery query) {
    	PageResult result = new PageResult();
    	
    	List<ReportInvoiceInAndOutDto> datas = reportFinanceService.queryInvoiceIn(query);
    	Integer total = reportFinanceService.totalInvoiceIn(query);
    	
    	result.setData(datas);
        result.setRecordsTotal(datas.size());
        result.setRecordsFiltered(total);
    	
    	return result;
    }
    
    /**
     * 应收发票导出为excel表
     * 20160614
     * @param query query: Long accountId,String startTime,String endTime
     * @author tuxianming
     * @return
     */
    @RequestMapping("/exportinvoicein.html")
    public ModelAndView exportInvoiceInForExcel(ReportInvoiceInAndOutQuery query){
    	query.setLength(null);
    	query.setStart(null);
    	List<ReportInvoiceInAndOutDto> datas = reportFinanceService.queryInvoiceIn(query);
    	
    	//build excel 数据结构
    	Map<String, Object> dataMap = new HashMap<>();
        //表头
    	List<String> titles = Arrays.asList("客户名称","期初未到票（元）","应收发票（元）","实收发票（元）","期末未到票（元）");
        dataMap.put("titles", titles);
        //数据内容
        List<PageData> varList = new ArrayList<PageData>();
        if(datas!=null){
        	for (ReportInvoiceInAndOutDto item : datas) {
                PageData vpd = new PageData();
                vpd.put("var1", item.getAccountName());
                setExcelAmount("var2", item.getInitAmount(), vpd);
                setExcelAmount("var3", item.getInvoiceAmount(), vpd);
                setExcelAmount("var4", item.getActInvoiceAmount(), vpd);
                setExcelAmount("var5", item.getFinalAmount(), vpd);
                varList.add(vpd);
            }
        }
        dataMap.put("varList", varList);
        ObjectExcelView erv = new ObjectExcelView(); //执行excel操作
        return new ModelAndView(erv, dataMap);
    }
    
    /**
     * 应付发票报表
     * 20160614
     * @param query query: accountId, startTime, endTime, start, length
     * @author tuxianming
     * @return
     */
    @ResponseBody
    @RequestMapping("/loadinvoiceout.html")
    public PageResult loadInvoiceOut(ReportInvoiceInAndOutQuery query) {
    	PageResult result = new PageResult();
    	List<ReportInvoiceInAndOutDto> datas = reportFinanceService.queryInvoiceOut(query);
    	Integer total = reportFinanceService.totalInvoiceOut(query);
    	
    	result.setData(datas);
        result.setRecordsTotal(datas.size());
        result.setRecordsFiltered(total);
    	
    	return result;
    }
    
    /**
     * 应付发票导出为excel表
     * 20160614
     * @param query query: Long accountId,String startTime,String endTime
     * @author tuxianming
     * @return
     */
    @RequestMapping("/exportinvoiceout.html")
    public ModelAndView exportInvoiceOutForExcel(ReportInvoiceInAndOutQuery query){
    	query.setLength(null);
    	query.setStart(null);
    	List<ReportInvoiceInAndOutDto> datas = reportFinanceService.queryInvoiceOut(query);
    	
    	//build excel 数据结构
    	Map<String, Object> dataMap = new HashMap<>();
        //表头
    	List<String> titles = Arrays.asList("客户名称","期初未开票（元）","应开发票（元）","实开发票（元）","期末未开票（元）");
        dataMap.put("titles", titles);
        //数据内容
        List<PageData> varList = new ArrayList<PageData>();
        if(datas!=null){
        	for (ReportInvoiceInAndOutDto item : datas) {
                PageData vpd = new PageData();
                vpd.put("var1", item.getAccountName());
                setExcelAmount("var2", item.getInitAmount(), vpd);
                setExcelAmount("var3", item.getInvoiceAmount(), vpd);
                setExcelAmount("var4", item.getActInvoiceAmount(), vpd);
                setExcelAmount("var5", item.getFinalAmount(), vpd);
                varList.add(vpd);
            }
        }
        dataMap.put("varList", varList);
        ObjectExcelView erv = new ObjectExcelView(); //执行excel操作
        return new ModelAndView(erv, dataMap);
    }
}
