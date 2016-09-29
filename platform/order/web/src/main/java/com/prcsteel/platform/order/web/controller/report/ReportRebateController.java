package com.prcsteel.platform.order.web.controller.report;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.prcsteel.platform.common.utils.Tools;
import com.prcsteel.platform.order.web.controller.BaseController;
import com.prcsteel.platform.account.model.model.Account;
import com.prcsteel.platform.account.model.model.AccountContact;
import com.prcsteel.platform.account.service.AccountContactService;
import com.prcsteel.platform.common.constants.Constant;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.prcsteel.platform.order.model.dto.ReportRebateRecordDto;
import com.prcsteel.platform.order.model.query.ReportRebateQuery;
import com.prcsteel.platform.account.service.AccountService;
import com.prcsteel.platform.order.service.reward.RewardReportService;
import com.prcsteel.platform.common.vo.PageResult;

/**
 * Created by chenchen on 15-8-21.
 */
@Controller
@RequestMapping("/report/rebate/")
public class ReportRebateController extends BaseController {

    @Resource
    RewardReportService rewardReportService;
    @Resource
    AccountService accountService;
    @Autowired
    AccountContactService accountContactService;

    @RequestMapping("detail.html")
    public String showRewardReport(ModelMap out,
                                   @RequestParam("account_id") long account_id,
                                   ReportRebateQuery reportRebateQuery) {
        reportRebateQuery.setAccountId(account_id);
        String accountName = rewardReportService
                .getAccountName(reportRebateQuery);
        out.put("account_id", account_id);
        out.put("accountName", accountName);
        setDefaultTime(out);
        return "report/business/rebateReport";
    }

    @RequestMapping("load.html")
    @ResponseBody
    public PageResult loadOrderData(ReportRebateQuery reportRebateQuery) {
        PageResult result = new PageResult();
        // 条件处理
        reportRebateQuery.setAccountId(reportRebateQuery.getAccountId());
        reportRebateQuery.setStartTimeStr(reportRebateQuery.getStartTimeStr());
        try {
            reportRebateQuery.setEndTimeStr(getDateEndStr(reportRebateQuery));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        reportRebateQuery.setStart(reportRebateQuery.getStart());
        reportRebateQuery.setLength(reportRebateQuery.getLength());
        // 返回数据处理
        List<ReportRebateRecordDto> queryUserRebateDetail = rewardReportService
                .queryUserRebateDetail(reportRebateQuery);
        int total = rewardReportService.queryUserRebateDetailCount(reportRebateQuery);
        result.setData(queryUserRebateDetail);
        result.setRecordsFiltered(total);
        result.setRecordsTotal(queryUserRebateDetail.size());
        return result;
    }

    @RequestMapping("userdetail.html")
    public String userDetail(ModelMap out, @RequestParam("accountid") Long accountId, @RequestParam("contactid") Long contactId,
                             @RequestParam(value = "sdate", required = false) String sdate,
                             @RequestParam(value = "edate", required = false) String edate) {
        if (StringUtils.isNotEmpty(sdate)) {
            out.put("startTime", sdate);
        } else {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.DAY_OF_MONTH, 1);
            out.put("startTime", Tools.dateToStr(cal.getTime(),Constant.TIME_FORMAT_DAY));
        }
        if (StringUtils.isNotEmpty(edate)) {
            out.put("endTime", edate);
        } else {
            out.put("endTime", Tools.dateToStr(new Date(), Constant.TIME_FORMAT_DAY));
        }
        Account account=accountService.queryById(accountId);
        if(account!=null)
        {
            out.put("accountName",account.getName());
            out.put("accountId",account.getId());
        }
        AccountContact accountContact=accountContactService.queryById(contactId.toString());
        if(contactId!=null)
        {
            out.put("contactId",accountContact.getId());
            out.put("contactName",accountContact.getName());
            out.put("contactTel",accountContact.getTel());
        }
        return "report/business/userRebateReport";
    }

    private ReportRebateQuery buildUserDetailQuery(String sdate, String edate, Long accountId, Long contactId) {
        ReportRebateQuery query = new ReportRebateQuery();
        if (accountId != null && accountId != 0) {
            query.setAccountId(accountId);
        }
        if (contactId != null && contactId != 0) {
            query.setContactId(contactId);
        }
        if (StringUtils.isNotEmpty(sdate)) {
            query.setStartTimeStr(sdate);
        } else {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.DAY_OF_MONTH, 1);
            query.setStartTimeStr(Tools.dateToStr(cal.getTime(), Constant.TIME_FORMAT_DAY));
        }
        if (StringUtils.isNotEmpty(edate)) {
            query.setEndTimeStr(edate);
        } else {
            query.setEndTimeStr(Tools.dateToStr(new Date(), Constant.TIME_FORMAT_DAY));
        }
        return query;
    }

    @RequestMapping("userdetaildata.html")
    @ResponseBody
    public PageResult userDetailData( @RequestParam("accountid") Long accountId, @RequestParam("contactid") Long contactId,
                                      @RequestParam(value = "sdate", required = false) String sdate,
                                      @RequestParam(value = "edate", required = false) String edate,
                                      @RequestParam(value = "start", required = true) Integer start,
                                      @RequestParam(value = "length", required = true) Integer length) {
        PageResult result = new PageResult();
        // 条件处理
        ReportRebateQuery query = buildUserDetailQuery(sdate, edate, accountId, contactId);
        // 返回数据处理

        int total = rewardReportService.queryUserRebateDetailCount(query);
        result.setRecordsFiltered(total);
        if (total > 0) {
            query.setStart(start);
            query.setLength(length);
            List<ReportRebateRecordDto> list =rewardReportService.queryUserRebateDetail(query);
            result.setData(list);
            result.setRecordsTotal(list.size());
        } else {
            result.setData(new ArrayList<>());
            result.setRecordsTotal(0);
        }
        return result;
    }

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

    /**
     * 终止日期（String类型）
     *
     * @return
     * @throws ParseException
     */
    private String getDateEndStr(ReportRebateQuery reportRebateQuery)
            throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        try {
            date = sdf.parse(reportRebateQuery.getEndTimeStr());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance(); // 得到日历
        calendar.setTime(date);// 把当前时间赋给日历
        calendar.add(Calendar.DAY_OF_MONTH, +1); // 设置为后一天
        String day = sdf.format(calendar.getTime());
        return day;
    }
}
