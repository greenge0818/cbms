package com.prcsteel.platform.order.web.controller.report;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.prcsteel.platform.order.service.payment.BankOriginalService;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.prcsteel.platform.common.utils.PageData;
import com.prcsteel.platform.common.utils.Tools;
import com.prcsteel.platform.order.web.controller.BaseController;
import com.prcsteel.platform.account.model.dto.AccountInAndOutDto;
import com.prcsteel.platform.account.model.query.AccountInAndOutQuery;
import com.prcsteel.platform.account.service.AccountTransLogService;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.order.web.support.ObjectExcelView;
import com.prcsteel.platform.common.vo.PageResult;
import com.prcsteel.platform.common.vo.Result;

/**
 * Created by Administrator on 2015/8/25.
 */
@Controller
@RequestMapping("/report/account/")
public class AccountFundsController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(AccountFundsController.class);

    @Resource
    AccountTransLogService accountTransLogService;

    @Resource
    BankOriginalService bankOriginalService;

    private final int AMOUNT_LIMIT = 2;

    /*
     * 设置默认时间段：当前月
	 *
	 * @param out
	 */
    private void setDefaultTime(ModelMap out) {
        // 日期:开始时间、结束时间(当前月第一天、当前时间)
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        // 界面默认显示的开单时间
        out.put("startTime", format.format(cal.getTime()));
        out.put("endTime", format.format(new Date()));
    }

    @RequestMapping("fundsinandout.html")
    public void fundsInAndOut(ModelMap out) {
        setDefaultTime(out);
    }

    @ResponseBody
    @RequestMapping("inandoutdata.html")
    public PageResult inAndOutData(
            @RequestParam(value = "accountName", required = false) String accountName,
            @RequestParam(value = "sdate", required = false) String sdate,
            @RequestParam(value = "edate", required = false) String edate,
            @RequestParam(value = "start", required = false) Integer start,
            @RequestParam(value = "length", required = false) Integer length,
            @RequestParam(value = "order[0][dir]", required = false) String order) {
        PageResult result = new PageResult();
        AccountInAndOutQuery query = new AccountInAndOutQuery();
        if (StringUtils.isNotEmpty(accountName)) {
            query.setAccountName(accountName);
        }
        if(StringUtils.isNotEmpty(order)){
            query.setTimeOrdering(order);
        }
        if (StringUtils.isNotEmpty(sdate)) {
            query.setBeginTime(Tools.strToDate(sdate, Constant.TIME_FORMAT_DAY));
        }
        if (StringUtils.isNotEmpty(edate)) {
            query.setEndTime(Tools.strToDate(edate, Constant.TIME_FORMAT_DAY));
        }

        int total = accountTransLogService.countAccountInAndOut(query);
        result.setRecordsFiltered(total);
        if (total > 0) {
            if (start != null) {
                query.setStart(start);
            }
            if (length != null) {
                query.setLength(length);
            }
            List<AccountInAndOutDto> list = accountTransLogService.queryAccountInAndOut(query);
            result.setData(list);
            result.setRecordsTotal(list.size());
        } else {
            result.setData(new ArrayList<>());
            result.setRecordsTotal(total);
        }

        return result;
    }

    @ResponseBody
    @RequestMapping("inandouttotal.html")
    public Result inAndOutTotal(
            @RequestParam(value = "accountName", required = false) String accountName,
            @RequestParam(value = "sdate", required = false) String sdate,
            @RequestParam(value = "edate", required = false) String edate) {
        Result result = new Result();
        AccountInAndOutQuery query = new AccountInAndOutQuery();
        if (StringUtils.isNotEmpty(accountName)) {
            query.setAccountName(accountName);
        }
        if (StringUtils.isNotEmpty(sdate)) {
            query.setBeginTime(Tools.strToDate(sdate, Constant.TIME_FORMAT_DAY));
        }
        if (StringUtils.isNotEmpty(edate)) {
            query.setEndTime(Tools.strToDate(edate, Constant.TIME_FORMAT_DAY));
        }
        List<AccountInAndOutDto> list = accountTransLogService.queryAccountInAndOutTotal(query);
        if(list.size()>0){
            result.setSuccess(true);
            result.setData(list);
        }
        else{
            result.setSuccess(false);
        }
        return result;
    }

    @RequestMapping("inandouttoexcel.html")
    @ResponseBody
    public ModelAndView inAndOutToExcel(@RequestParam(value = "accountName", required = false) String accountName,
                                        @RequestParam(value = "sdate", required = false) String sdate,
                                        @RequestParam(value = "edate", required = false) String edate,
                                        @RequestParam(value = "order", required = false) String[] order) {
        ModelAndView mv = null;
        try {
            AccountInAndOutQuery query = new AccountInAndOutQuery();
            if (StringUtils.isNotEmpty(sdate)) {
                query.setBeginTime(Tools.strToDate(sdate, Constant.TIME_FORMAT_DAY));
            }
            if (StringUtils.isNotEmpty(edate)) {
                query.setEndTime(Tools.strToDate(edate, Constant.TIME_FORMAT_DAY));
            }
            if (StringUtils.isNotEmpty(accountName)) {
                query.setAccountName(accountName);
            }
            query.setStart(0);
            query.setLength(10000);//最大数据条数限制

            List<AccountInAndOutDto> list = accountTransLogService.queryAccountInAndOut(query);

            Map<String, Object> dataMap = new HashMap<>();
            List<String> titles = new ArrayList<>();
            titles.add("日期：" + (sdate == null ? "*" : sdate) + "~" + (edate == null ? "*" : edate));
            titles.add("");
            titles.add("银行");
            titles.add("");
            titles.add("");
            titles.add("");
            titles.add("");
            titles.add("");
            titles.add("");
            titles.add("");
            dataMap.put("titles", titles);
            List<String> merges = new ArrayList<>();
            merges.add("0,0,0,1");
            merges.add("0,0,2,9");
            merges.add("1,2,0,0");
            merges.add("1,1,2,5");
            merges.add("1,1,6,9");
            merges.add("3,4,0,1");
            dataMap.put("merges", merges);
            List<PageData> varList = new ArrayList<>();
            varList.addAll(buildTitleData());
            PageData vpd = null;
            BigDecimal spdTotalReceipt = BigDecimal.ZERO;
            BigDecimal spdTotalPayment = BigDecimal.ZERO;
            BigDecimal spdTotalDeal = BigDecimal.ZERO;
            BigDecimal icbcTotalReceipt = BigDecimal.ZERO;
            BigDecimal icbcTotalPayment = BigDecimal.ZERO;
            BigDecimal icbcTotalDeal = BigDecimal.ZERO;
            for (AccountInAndOutDto item : list) {
                vpd = new PageData();
                vpd.put("var1", item.getAccountName());
                vpd.put("var2", Tools.dateToStr(item.getCreateTime(), "yy-MM-dd HH:mm:ss"));
                if (item.getTransType() == Constant.TransType.ORDER.getValue()) {
                    vpd.put("var3", "0.00");
                    if (Constant.bankType.SPD.toString().equals(item.getPaymentBank())) {
                    	vpd.put("var4", Tools.formatBigDecimal(item.getPaymentAmount(), AMOUNT_LIMIT));
                    	spdTotalPayment = spdTotalPayment.add(item.getPaymentAmount());
                    } else {
                    	vpd.put("var4", "0.00");
                    }
                    vpd.put("var5", "0.00");
                    vpd.put("var7", "0.00");
                    if (Constant.bankType.ICBC.toString().equals(item.getPaymentBank())) {
                    	vpd.put("var8", Tools.formatBigDecimal(item.getPaymentAmount(), AMOUNT_LIMIT));
                    	icbcTotalPayment = icbcTotalPayment.add(item.getPaymentAmount());
                    } else {
                    	vpd.put("var8", "0.00");
                    }
                    vpd.put("var9", "0.00");
                } else if (item.getTransType() == Constant.TransType.SPD.getValue()) {
                    spdTotalReceipt = spdTotalReceipt.add(item.getReceiptAmount());
                    spdTotalDeal = spdTotalDeal.add(item.getDealAmount());
                    vpd.put("var3", Tools.formatBigDecimal(item.getReceiptAmount(), AMOUNT_LIMIT));
                    vpd.put("var4", "0.00");
                    vpd.put("var5", Tools.formatBigDecimal(item.getDealAmount(), AMOUNT_LIMIT));
                    vpd.put("var7", "0.00");
                    vpd.put("var8", "0.00");
                    vpd.put("var9", "0.00");
                } else if (item.getTransType() == Constant.TransType.ICBC.getValue()) {
                    icbcTotalReceipt = icbcTotalReceipt.add(item.getReceiptAmount());
                    icbcTotalDeal = icbcTotalDeal.add(item.getDealAmount());
                    vpd.put("var3", "0.00");
                    vpd.put("var4", "0.00");
                    vpd.put("var5", "0.00");
                    vpd.put("var7", Tools.formatBigDecimal(item.getReceiptAmount(), AMOUNT_LIMIT));
                    vpd.put("var8", "0.00");
                    vpd.put("var9", Tools.formatBigDecimal(item.getDealAmount(), AMOUNT_LIMIT));
                } else {
                    vpd.put("var3", "0.00");
                    vpd.put("var4", "0.00");
                    vpd.put("var5", "0.00");
                    vpd.put("var7", "0.00");
                    vpd.put("var8", "0.00");
                    vpd.put("var9", "0.00");
                }
                vpd.put("var6", "0.00");
                vpd.put("var10", "0.00");

                varList.add(vpd);
            }
            //统计行
            vpd = new PageData();
            vpd.put("var1", "合计");
            vpd.put("var2", "");
            vpd.put("var3", Tools.formatBigDecimal(spdTotalReceipt, AMOUNT_LIMIT));      
            vpd.put("var4", Tools.formatBigDecimal(spdTotalPayment, AMOUNT_LIMIT));
            vpd.put("var5", Tools.formatBigDecimal(spdTotalDeal, AMOUNT_LIMIT));
            vpd.put("var6", Tools.formatBigDecimal(spdTotalPayment.subtract(spdTotalDeal), AMOUNT_LIMIT));
            vpd.put("var7", Tools.formatBigDecimal(icbcTotalReceipt, AMOUNT_LIMIT));
            vpd.put("var8", Tools.formatBigDecimal(icbcTotalPayment, AMOUNT_LIMIT));
            vpd.put("var9", Tools.formatBigDecimal(icbcTotalDeal, AMOUNT_LIMIT));
            vpd.put("var10", Tools.formatBigDecimal(icbcTotalPayment.subtract(icbcTotalDeal), AMOUNT_LIMIT));
            varList.add(vpd);

            dataMap.put("varList", varList);
            ObjectExcelView erv = new ObjectExcelView();                //执行excel操作
            mv = new ModelAndView(erv, dataMap);
        } catch (Exception e) {
            // handle error
        }
        return mv;
    }

    private List<PageData> buildTitleData() {
        List<PageData> varList = new ArrayList<>();
        PageData vpd = null;
        vpd = new PageData();
        vpd.put("var1", "往来单位");
        vpd.put("var2", "");
        vpd.put("var3", "浦发");
        vpd.put("var4", "");
        vpd.put("var5", "");
        vpd.put("var6", "");
        vpd.put("var7", "工行");
        vpd.put("var8", "");
        vpd.put("var9", "");
        vpd.put("var10", "");
        varList.add(vpd);
        vpd = new PageData();
        vpd.put("var1", "");
        vpd.put("var2", "时间");
        vpd.put("var3", "借方");
        vpd.put("var4", "贷方");
        vpd.put("var5", "贷方");
        vpd.put("var6", "差异");
        vpd.put("var7", "借方");
        vpd.put("var8", "贷方");
        vpd.put("var9", "贷方");
        vpd.put("var10", "差异");
        varList.add(vpd);
        vpd = new PageData();
        vpd.put("var1", "");
        vpd.put("var2", "");
        vpd.put("var3", "收款金额（银企直联）");
        vpd.put("var4", "应付金额（系统）");
        vpd.put("var5", "实付金额（银企直联）");
        vpd.put("var6", "");
        vpd.put("var7", "收款金额（银企直联）");
        vpd.put("var8", "应付金额（系统）");
        vpd.put("var9", "实付金额（银企直联）");
        vpd.put("var10", "");
        varList.add(vpd);
        vpd = new PageData();
        vpd.put("var1", "");
        vpd.put("var2", "");
        vpd.put("var3", "");
        vpd.put("var4", "出纳确认付款");
        vpd.put("var5", "");
        vpd.put("var6", "");
        vpd.put("var7", "");
        vpd.put("var8", "");
        vpd.put("var9", "");
        vpd.put("var10", "");
        varList.add(vpd);
        return varList;
    }
}
