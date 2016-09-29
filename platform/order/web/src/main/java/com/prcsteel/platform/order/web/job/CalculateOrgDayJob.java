package com.prcsteel.platform.order.web.job;

import com.prcsteel.platform.common.utils.Tools;
import com.prcsteel.platform.acl.model.enums.SysSettingType;
import com.prcsteel.platform.acl.model.model.SysSetting;
import com.prcsteel.platform.order.service.report.ReportOrgDayService;
import com.prcsteel.platform.acl.service.SysSettingService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;

/**
 * @author dengxiyan
 * @version V1.0
 * @Title: CalculateOrgDayJob
 * @Package com.prcsteel.cbms.web.job
 * @Description: 服务中心日报数据job
 * @date 2015/12/10
 */
@Component
public class CalculateOrgDayJob extends CbmsJob {

    @Resource
    ReportOrgDayService reportOrgDayService;

    @Resource
    SysSettingService sysSettingService;

    @Value("${quartz.job.spdb.systemId}")
    private String systemId;
    @Value("${quartz.job.email.enabled}")
    private boolean emailEnabled;

    private static final Logger logger = Logger.getLogger(CalculateOrgDayJob.class);

    /**
     * 执行内容
     */
    @Override
    public void execute() {
        if (isEnabled()) {
            //从系统配置表获取上一次执行成功时间，当前执行时间 是否执行正确
            SysSetting sysSetting = getSysSetting(SysSettingType.ReportOrgDay.getCode());
            try {
                if ("1".equals(sysSetting.getReportOrgDay().toString())) {
                    String lastDate = sysSetting.getDefaultValue();//系统配置中 上次执行时间
                    String exeTimeStr = sysSetting.getSettingValue() + ":00";//系统设置到时分

                    final Date date = new Date();//本次执行时间

                    Date queryDate; //上次执行时间 用作本次查询

                    String toDay = Tools.dateToStr(date, "yyyy-MM-dd");

                    if (StringUtils.isEmpty(lastDate)) {
                        //第一次执行，上次执行时间设置昨天的时间点
                        queryDate = getLastDate(Tools.strToDate(toDay + " " + exeTimeStr, "yyyy-MM-dd HH:mm:ss"));
                    } else {
                        queryDate = Tools.strToDate(sysSetting.getDefaultValue(), "yyyy-MM-dd HH:mm:ss");
                        String lastDay = Tools.dateToStr(Tools.strToDate(lastDate, "yyyy-MM-dd"), "yyyy-MM-dd");

                        //当天已经执行
                        if (toDay.equals(lastDay)) {
                            return;
                        }
                    }

                    //当天执行时间点
                    Date exeTime = Tools.strToDate(toDay + " " + exeTimeStr);

                    //到执行时间
                    if (date.compareTo(exeTime) != -1) {
                        logger.info("start add org day ");

                        sysSetting.setLastUpdated(new Date());
                        sysSetting.setLastUpdatedBy(systemId);
                        sysSetting.setModificationNumber(sysSetting.getModificationNumber() + 1);


                        //日报数据 平台沉淀资金 更新配置：执行成功 更新配置表上次执行时间为date
                        sysSetting.setDefaultValue(Tools.dateToStr(date, "yyyy-MM-dd HH:mm:ss"));
                        reportOrgDayService.addOrgDayByOrderAndPrecipitatinFunds(queryDate, date, sysSetting);

                        logger.info("end add org day ");

                    }
                } else {
                    logger.warn("add org day fail 系统错误，需要修改程序后，再手动到系统设置中修改是否继续执行为’是‘ ");
                }
            } catch (Exception e) {
                //更新配置表是否继续执行值为0否
                sysSetting.setReportOrgDay(0);
                sysSettingService.updateBySettingTypeSelective(sysSetting);

                logger.error("add org day fail", e);
            }
        }
    }

    private SysSetting getSysSetting(String key) {
        SysSetting set;
        try {
            set = sysSettingService.queryBySettingType(key);
            if (set == null) {
                set = new SysSetting();
                //设置是否继续执行job的值为0
                set.setReportOrgDay(0);
                logger.warn("System config missing.Key:" + key);
            }
        } catch (Exception e) {
            set = new SysSetting();
            //设置是否继续值为0
            set.setReportOrgDay(0);
            logger.error("System config error", e);
        }
        return set;
    }

    private Date getLastDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return cal.getTime();
    }


    /**
     * 是否启用当前Job判断（满足针对特定Job需要单独启用/禁用）。
     *
     * @return
     */
    @Override
    public boolean isEnabled() {
        return emailEnabled;
    }
}
