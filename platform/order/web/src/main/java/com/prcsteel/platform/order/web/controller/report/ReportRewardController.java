package com.prcsteel.platform.order.web.controller.report;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.annotation.Resource;

import com.prcsteel.platform.common.utils.PageData;
import com.prcsteel.platform.common.utils.Tools;
import com.prcsteel.platform.order.model.dto.OrganizationRewardRecordDto;
import com.prcsteel.platform.order.model.dto.RewardNewAcccountWithDetailsDto;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.order.model.model.CommissionExcel;
import com.prcsteel.platform.order.model.query.ReportRewardQuery;
import com.prcsteel.platform.core.service.CategoryGroupService;
import com.prcsteel.platform.order.model.model.ReportCollect;
import com.prcsteel.platform.order.service.order.ConsignOrderService;
import com.prcsteel.platform.order.service.reward.RewardReportService;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.acl.service.RewardService;
import com.prcsteel.platform.order.web.controller.BaseController;
import com.prcsteel.platform.order.web.support.ObjectExcelView;
import com.prcsteel.platform.order.web.support.ShiroVelocity;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


import java.math.BigDecimal;
import java.util.stream.Collectors;


/**
 * Created by chenchen on 15-8-19.
 */
@Controller
@RequestMapping("/report/reward/")
public class ReportRewardController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(ReportRewardController.class);
    @Resource
    RewardReportService rewardReportService;
   // categoryGroupService.selectNoSelectForReward()
    @Resource
   CategoryGroupService categoryGroupService;
    @Resource
    RewardService rewardService;
    ShiroVelocity permissionLimit = new ShiroVelocity();
    @Resource
    ConsignOrderService consignOrderService;

    /**
     * 服务中心提成统计报表界面
     * @param out
     * @param blInit 是否初始进入界面
     * @param queryParam 查询参数
     * @return
     */
    @RequestMapping("commission.html")
    public String showRewardReport(ModelMap out, @RequestParam(value = "init", required = false, defaultValue = "true") boolean blInit, ReportRewardQuery queryParam) {

        //初始进入界面时需设置默认时间为当月
        if (blInit) {
            queryParam.setMonth(getCurrentMonth());
        }
        queryParam.setUserIdList(getUserIds());

        //屏蔽服务中心查询条件,默认查询条件值为当前服务中心
        if(permissionLimit.hasPermission(Constant.PERMISSION_ORG_LIMIT)) {
            User user=getLoginUser();
            queryParam.setOrgId(user.getOrgId());
        }

        int currMonth = Calendar.getInstance().get(Calendar.MONTH)+1;
        if(queryParam.getMonth()!=null && Integer.parseInt(queryParam.getMonth().substring(4))!=currMonth){
        	out.put("isNotCurrMonth", true);
        }

        List<RewardNewAcccountWithDetailsDto> list = rewardReportService.getOrgRewardList(queryParam);
        out.put("months", getMonthList());
        out.put("queryParam", queryParam);
        out.put("list", list);
        return "report/business/rewardReport";
    }

    @RequestMapping(value = "query.html", produces = "text/plain;charset=UTF-8")
    public String queryRewardReport(ModelMap out,
                                    @RequestParam("sorganizationHidden") long org_id,
                                    @RequestParam("startTime") String startTime,
                                    @RequestParam("endTime") String endTime,
                                    @RequestParam(value = "sorganization") String sorganization) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            List<OrganizationRewardRecordDto> list = rewardReportService.getRewardReportByPage(sdf.parse(startTime), sdf.parse(endTime), org_id);
            out.put("list", list);
            out.put("endTime", endTime);
            out.put("startTime", startTime);
            out.put("sorganization", new String((sorganization).getBytes("ISO-8859-1"), "utf-8"));
            out.put("org_id", org_id);
        } catch (ParseException e) {
            logger.error("get reward report failed!", e);
        } catch (UnsupportedEncodingException e) {
            logger.error("get reward report failed!", e);
        }
        return "report/business/rewardReport";
    }

    @RequestMapping(value = "org.html", method = {RequestMethod.GET})
    public String showOrgReward(ModelMap out, @RequestParam("org_id") long org_id,
                                @RequestParam(value = "org_name", required = false) String org_name,
                                @RequestParam("startTime") String startTime,
                                @RequestParam("endTime") String endTime,
                                @RequestParam("managerName") String managerName) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            List<OrganizationRewardRecordDto> list = rewardReportService.getRewardReportByOrg(sdf.parse(startTime), sdf.parse(endTime), org_id, new String(managerName.getBytes("iso8859-1"), "UTF-8"));
            out.put("list", list);
            out.put("endTime", endTime);
            out.put("startTime", startTime);
            out.put("org_id", org_id);
            out.put("org_name", new String(org_name.getBytes("iso8859-1"), "UTF-8"));
            out.put("manager_name", new String(managerName.getBytes("iso8859-1"), "UTF-8"));

        } catch (Exception e) {
            logger.error("get reward report by org failed!", e);
        }
        return "report/business/rewardReportManager";
    }

    /**
     * 交易员提成统计报表界面
     * @param out
     * @param queryParam 查询参数
     * @return exportexcel
     */
    @RequestMapping("showmanagerrewardreport.html")
    public String showManagerRewardReport(ModelMap out,ReportRewardQuery queryParam) {
        if (queryParam != null){
            queryParam.setUserIdList(getUserIds());//数据权限控制

            //屏蔽服务中心查询条件且主界面未传值则默认查询条件值为当前服务中心
            if(permissionLimit.hasPermission(Constant.PERMISSION_ORG_LIMIT) && queryParam.getOrgId() == null) {
                User user = getLoginUser();
                queryParam.setOrgId(user.getOrgId());
            }

            //屏蔽交易员查询条件，默认当前用户
            if (permissionLimit.hasPermission(Constant.PERMISSION_TRADER_HIDDEN)){
                User user = getLoginUser();
                queryParam.setManagerName("%" + user.getName() + "%");
            }else if (StringUtils.isNotEmpty(queryParam.getShowManagerName())){ //交易员查询条件
                queryParam.setManagerName("%"+queryParam.getShowManagerName().trim()+"%");
            }
        }
        List<RewardNewAcccountWithDetailsDto> list = rewardReportService.getManagerRewardList(queryParam);
        out.put("months", getMonthList());
        out.put("queryParam", queryParam);
        out.put("list", list);
        return "report/business/rewardReportManager";
    }

    /**
     * @return
     */
    private List<String> getMonthList() {
        List<String> months = new ArrayList<>();
        Date now = new Date();
        //2015-08-01开始算起
        Date start = Tools.strToDate(Constant.SYS_START_TIME, Constant.TIME_FORMAT_DAY);
        Calendar temp;
        while (!start.after(now)) {
            months.add(0, Tools.dateToStr(start, "yyyyMM"));
            //月份+1
            temp = Calendar.getInstance();
            temp.setTime(start);
            temp.add(Calendar.MONTH, 1);
            start = temp.getTime();
        }
        return months;
    }


    /**
     * 返回当月
     * 格式:yyyyMM
     *
     * @return String
     */
    private String getCurrentMonth() {
        return Tools.dateToStr(new Date(), "yyyyMM");
    }

    /**
     * 服务中心提成统计报表导出功能
     *汇总
     */

    @RequestMapping("collectexcel.html")
    @ResponseBody
    public ModelAndView collectExcel(ModelMap out,ReportRewardQuery queryParam){
        ModelAndView mv = null;
        Map<String, Object> dataMap = new HashMap<>();
        List<String> titles = new ArrayList<>();
        titles.add("服务中心");
        titles.add("工号");
        titles.add("姓名");
        titles.add("交易量提成金额");
        titles.add("新增客户数提成金额");
        titles.add("提成总额");
        dataMap.put("titles", titles);
        List<PageData> varList = new ArrayList<>();
        PageData vpd = null;
        queryParam.setUserIdList(getUserIds());
        //屏蔽服务中心查询条件,默认查询条件值为当前服务中心
        if(permissionLimit.hasPermission(Constant.PERMISSION_ORG_LIMIT)) {
            User user=getLoginUser();
            queryParam.setOrgId(user.getOrgId());
        }
        ReportCollect reportCollect = null;
        List<ReportCollect> repList = new ArrayList<ReportCollect>();
        String nowMonth = getCurrentMonth();
        if(nowMonth.equals(queryParam.getMonth())) {
            //查询当前月服务中心提成
            try {
                List<CommissionExcel> list =  rewardReportService.findByParam(queryParam);
                //通过买家交易员分组
                Map<String, List<CommissionExcel>> commissMap =list.stream().collect(Collectors.groupingBy(CommissionExcel::getBuyerOwnerName));

                for (String s : commissMap.keySet()) {
                    reportCollect = new ReportCollect();
                    BigDecimal buyerDeduct = new BigDecimal(commissMap.get(s).stream().mapToDouble(a -> a.getBuyerDeduct().doubleValue()).sum());//买家交易量提成
                    BigDecimal sellerDeduct = new BigDecimal(commissMap.get(s).stream().mapToDouble(a -> a.getSellerDeduct().doubleValue()).sum());//卖家交易量提成
                    BigDecimal buyerCount = new BigDecimal(commissMap.get(s).stream().mapToDouble(a -> a.getBuyerCount().doubleValue()).sum());//新增买家提成
                    BigDecimal sellerCount = new BigDecimal(commissMap.get(s).stream().mapToDouble(a -> a.getSellerCount().doubleValue()).sum());//新增卖家提成
                    BigDecimal buyerProfit = new BigDecimal(commissMap.get(s).stream().mapToDouble(a -> a.getProfit().doubleValue()).sum());//差价利润佣金
                    reportCollect.setBuyerCount(buyerDeduct.add(sellerDeduct).setScale(2, BigDecimal.ROUND_HALF_UP));//交易量提成金额
                    reportCollect.setSellerCount(buyerCount.add(sellerCount).setScale(2, BigDecimal.ROUND_HALF_UP));//新增客户数提成金额
                    reportCollect.setBuyerSum(buyerDeduct.add(sellerDeduct).add(buyerCount).add(sellerCount).add(buyerProfit).setScale(2, BigDecimal.ROUND_HALF_UP));//提成总额
                    reportCollect.setBuyerOrgName(commissMap.get(s).get(0).getBuyerOrgName());//服务中心
                    reportCollect.setBuyerJobNumber(commissMap.get(s).get(0).getBuyerJobNumber());//工号
                    reportCollect.setBuyerOwnerName(commissMap.get(s).get(0).getBuyerOwnerName());//姓名
                    repList.add(reportCollect);
                }
            } catch (Exception e) {
                logger.debug("服务中心提成报表汇总导出EXCEL出错：", e);
            }
        }else {
            //查询历史服务中心提成报表数据
            repList = rewardReportService.queryAllByMonth(queryParam);
        }
        for(ReportCollect rep : sortReport(repList)){
            vpd = new PageData();
            vpd.put("var1",rep.getBuyerOrgName());
            vpd.put("var2",rep.getBuyerJobNumber());
            vpd.put("var3",rep.getBuyerOwnerName());
            vpd.put("var4",rep.getBuyerCount()+ "");
            vpd.put("var5",rep.getSellerCount()+ "");
            vpd.put("var6",rep.getBuyerSum()+ "");
            varList.add(vpd);
        }
        dataMap.put("varList", varList);
        ObjectExcelView erv = new ObjectExcelView();//执行excel操作
        mv = new ModelAndView(erv, dataMap);
        return mv;
    }
    //按服务中心名称排序
    public List<ReportCollect> sortReport(List<ReportCollect> repList){
        Collections.sort(repList, new Comparator<ReportCollect>() {
            public int compare(ReportCollect arg0, ReportCollect arg1) {
                return arg0.getBuyerOrgName().compareTo(arg1.getBuyerOrgName());
            }
        });
        return  repList;
    }

    //按服务中心名称排序
    public List<CommissionExcel> sortCommission(List<CommissionExcel> list){
        Collections.sort(list, new Comparator<CommissionExcel>() {
            public int compare(CommissionExcel arg0, CommissionExcel arg1) {
                return arg0.getBuyerOrgName().compareTo(arg1.getBuyerOrgName());
            }
        });
        return  list;
    }

    /**
     * 服务中心提成统计报表导出功能
     *明细
     */
    @RequestMapping("exportexcel.html")
    @ResponseBody
    public ModelAndView exportExcel(ModelMap out,ReportRewardQuery queryParam){
        ModelAndView mv = null;
        Map<String, Object> dataMap = new HashMap<>();
        List<String> titles = new ArrayList<>();
        titles.add("二结时间");
        titles.add("开票时间");
        titles.add("买家服务中心");
        titles.add("买家交易员工号");
        titles.add("买家交易员");
        titles.add("卖家服务中心");
        titles.add("卖家交易员工号");
        titles.add("卖家交易员");
        titles.add("买家客户");
        titles.add("卖家客户");
        titles.add("大类");
        titles.add("品名");
        titles.add("材质");
        titles.add("规格");
        titles.add("实提重量");
        titles.add("买家提成系数");
        titles.add("卖家提成系数");
        titles.add("买家交易员提成金额");
        titles.add("卖家交易员提成金额");
        titles.add("交易量提成金额");
        titles.add("新增买家提成金额");
        titles.add("新增卖家提成金额");
        titles.add("差价利润佣金");
        titles.add("提成总金额");
        titles.add("开单时间");
        titles.add("交易单号");
        dataMap.put("titles", titles);
        List<PageData> varList = new ArrayList<>();
        String nowMonth = getCurrentMonth();
        PageData vpd = null;
        queryParam.setUserIdList(getUserIds());
        //屏蔽服务中心查询条件,默认查询条件值为当前服务中心
        if(permissionLimit.hasPermission(Constant.PERMISSION_ORG_LIMIT)) {
            User user=getLoginUser();
            queryParam.setOrgId(user.getOrgId());
        }
        List<CommissionExcel> list = new ArrayList<CommissionExcel>();
        if(nowMonth.equals(queryParam.getMonth())) {
            //查询当前月服务中心提成
            try {
               list = rewardReportService.findByParam(queryParam);
            } catch (Exception e) {
                logger.debug("服务中心提成报表明细导出EXCEL出错：", e);
            }
        }else {
            //查询历史服务中心提成报表数据
            try {
                 list = rewardReportService.selectCommissionListByMonth(queryParam);
            } catch (Exception e) {
                logger.debug("服务中心提成报表明细导出EXCEL出错：", e);
            }
        }
        for (CommissionExcel centerList : sortCommission(list)) {
            if(centerList.getProfit() == null)
                centerList.setProfit(new BigDecimal(0.00));
            vpd = new PageData();
            vpd.put("var1", Tools.dateToStr(centerList.getSecondSettlementTime(), "yyyy-MM-dd HH:mm:ss"));//二次结算时间
            vpd.put("var2", Tools.dateToStr(centerList.getInvoiceTime(), "yyyy-MM-dd HH:mm:ss"));//开票时间
            vpd.put("var3", centerList.getBuyerOrgName());//买家服务中心
            vpd.put("var4", centerList.getBuyerJobNumber());//买家交易员工号
            vpd.put("var5", centerList.getBuyerOwnerName());//买家交易员
            vpd.put("var6", centerList.getSellerOrgName());//卖家服务中心
            vpd.put("var7", centerList.getSellerJobNumber());//卖家交易员工号
            vpd.put("var8", centerList.getSellerOwnerName());//卖家交易员
            vpd.put("var9", centerList.getBuyerAccountName());//买家客户
            vpd.put("var10", centerList.getSellerAccountName());//卖家客户
            vpd.put("var11", centerList.getCbname());//大类
            vpd.put("var12", centerList.getNsortName());//品名
            vpd.put("var13", centerList.getMaterial());//材质
            vpd.put("var14", centerList.getSpec());//规格
            vpd.put("var15", centerList.getActualPickWeightServer() + "");//实提重量
            vpd.put("var16", centerList.getBuyerRewardRole().doubleValue() + "%");//买家提成系数
            vpd.put("var17", centerList.getSellerRewardRole().doubleValue() + "%");//卖家提成系数
            vpd.put("var18", centerList.getBuyerDeduct()+ "");//买家交易员提成金额
            vpd.put("var19", centerList.getSellerDeduct()+ "");//卖家交易员提成金额
            vpd.put("var20", centerList.getBuyerDeduct().add(centerList.getSellerDeduct())+ "");//交易量提成金额
            vpd.put("var21", centerList.getBuyerCount() + "");//新增买家提成金额
            vpd.put("var22", centerList.getSellerCount()+ "");//新增卖家提成金额
            vpd.put("var23", centerList.getProfit()+ "");//差价利润佣金
            vpd.put("var24", centerList.getBuyerDeduct().add(centerList.getBuyerCount()).add(centerList.getSellerDeduct()).add(centerList.getSellerCount()).add(centerList.getProfit()).setScale(2, BigDecimal.ROUND_HALF_UP) + "");//提成总金额
            vpd.put("var25", Tools.dateToStr(centerList.getOrderCreated(), "yyyy-MM-dd HH:mm:ss"));//开单时间d
            vpd.put("var26", centerList.getOrderCode());//交易单号
            varList.add(vpd);
        }
        dataMap.put("varList", varList);
        ObjectExcelView erv = new ObjectExcelView();//执行excel操作
        mv = new ModelAndView(erv, dataMap);
        return mv;
    }
}
