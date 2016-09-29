package com.prcsteel.platform.order.web.controller;

import javax.annotation.Resource;

import com.prcsteel.platform.order.web.job.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.prcsteel.platform.common.vo.Result;
import com.prcsteel.platform.core.service.CommonService;
import com.prcsteel.platform.order.service.AppPushService;

/**
 * Created by Rolyer on 2016/1/12.
 */
@RestController
@RequestMapping("/jobService")
public class JobServiceController {
    private static final Logger logger =  LoggerFactory.getLogger(JobServiceController.class);

    @Resource
    private AppPushService appPushService;
    @Resource
    private CommonService commonService;
    @Resource
    private CalculateOrgDayJob calculateOrgDayJob;
    @Resource
    private CloseAllowanceJob closeAllowanceJob;
    @Resource
    private CloseOrderJob closeOrderJob;
    @Resource
    private ICBCBdlDataJob icbcBdlDataJob;
    @Resource
    private LocalTransactionDataJob localTransactionDataJob;
    @Resource
    private ReceiptDataToThisSysJob receiptDataToThisSysJob;
    @Resource
    private SendReportMailJob sendReportMailJob;
    @Resource
    private SPDBBdlDataJob spdbBdlDataJob;
    @Resource
    private StartRebateAndReward startRebateAndReward;
    @Resource
    private SyncReceiptDataJob syncReceiptDataJob;
    @Resource
    private SyncTransactionDataJob syncTransactionDataJob;
    @Resource
    private SyncWithDrawJob syncWithDrawJob;

    @Resource
    private InsertCommissionJob insertCommissionJob;
    @Resource
    private CopyResourceDataToHistoryJob copyResourceDataToHistoryJob;
    @Resource
    private  HistoryTransactionJob historyTransactionJob;


    @RequestMapping(value = "/calculateOrgDayJob", method = RequestMethod.GET)
    public Result calculateOrgDayJob() {
        calculateOrgDayJob.execute();

        return new Result();
    }

    @RequestMapping(value = "/closeAllowanceJob", method = RequestMethod.GET)
    public Result closeAllowanceJob() {
        closeAllowanceJob.execute();

        return new Result();
    }

    @RequestMapping(value = "/closeOrderJob", method = RequestMethod.GET)
    public Result closeOrderJob() {
        closeOrderJob.execute();

        return new Result();
    }

    @RequestMapping(value = "/icbcBdlDataJob", method = RequestMethod.GET)
    public Result icbcBdlDataJob() {
        icbcBdlDataJob.execute();

        return new Result();

    }

    @RequestMapping(value = "/localTransactionDataJob", method = RequestMethod.GET)
    public Result localTransactionDataJob() {
        localTransactionDataJob.execute();

        return new Result();

    }

    @RequestMapping(value = "/receiptDataToThisSysJob", method = RequestMethod.GET)
    public Result receiptDataToThisSysJob() {
        receiptDataToThisSysJob.execute();

        return new Result();

    }

    @RequestMapping(value = "/sendReportMailJob", method = RequestMethod.GET)
    public Result sendReportMailJob() {
        sendReportMailJob.execute();

        return new Result();

    }

    @RequestMapping(value = "/spdbBdlDataJob", method = RequestMethod.GET)
    public Result spdbBdlDataJob() {
        spdbBdlDataJob.execute();

        return new Result();

    }

    @RequestMapping(value = "/startRebateAndReward", method = RequestMethod.GET)
    public Result startRebateAndReward() {
        startRebateAndReward.execute();

        return new Result();

    }

    @RequestMapping(value = "/syncReceiptDataJob", method = RequestMethod.GET)
    public Result syncReceiptDataJob() {
        syncReceiptDataJob.execute();

        return new Result();

    }

    @RequestMapping(value = "/syncTransactionDataJob", method = RequestMethod.GET)
    public Result syncTransactionDataJob() {
        syncTransactionDataJob.execute();

        return new Result();

    }

    @RequestMapping(value = "/syncWithDrawJob", method = RequestMethod.GET)
    public Result syncWithDrawJob() {
        syncWithDrawJob.execute();

        return new Result();

    }

    @RequestMapping(value = "/push/notification", method = RequestMethod.GET)
    public Result pushNotification(String alias, String deviceType, String title, String content) {

        Assert.hasText(alias);
        Assert.hasText(deviceType);
        Assert.hasText(title);
        Assert.hasText(content);

        logger.debug("start push notification");
        appPushService.sendPushNoticfication(alias, deviceType, title, content);
        logger.debug("end push notification");

        return new Result();
    }

    @RequestMapping(value = "/push/message", method = RequestMethod.POST)
    public Result pushMessage(String alias, String content) {

        Assert.hasText(alias);
        Assert.hasText(content);

        logger.debug("start push message");
        appPushService.sendPushMessage(alias, content);
        logger.debug("end push message");

        return new Result();

    }

    @RequestMapping(value = "/sms/send", method = RequestMethod.GET)
    public Result smsSend(String phone, String content) {

        Assert.hasText(phone);
        Assert.hasText(content);

        logger.debug("start push message");
        commonService.sendSMS(phone, content);
        logger.debug("end push message");

        return new Result();
    }
    @RequestMapping(value = "/insertCommissionJob", method = RequestMethod.GET)
    public Result InsertCommissionJob() {
        insertCommissionJob.execute();

        return new Result();

    }
    @RequestMapping(value = "/copyResourceDataToHistoryJob", method = RequestMethod.GET)
    public Result CopyResourceDataToHistoryJob() {
        copyResourceDataToHistoryJob.execute();

        return new Result();

    }
    @RequestMapping(value = "/historyTransactionJob", method = RequestMethod.GET)
    public Result HistoryTransactionJob() {
        historyTransactionJob.execute();

        return new Result();

    }

}
