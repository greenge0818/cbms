package com.prcsteel.platform.order.service.invoice.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prcsteel.platform.order.model.model.OrgInvoiceOutBalanceMonthly;
import com.prcsteel.platform.order.persist.dao.OrgInvoiceOutBalanceMonthlyDao;
import com.prcsteel.platform.order.service.invoice.OrgInvoiceOutBalanceMonthlyService;

/**
 * Created by rolyer on 15-8-4.
 */
@Service("orgInvoiceOutBalanceMonthlyService")
public class OrgInvoiceOutBalanceMonthlyServiceImpl implements OrgInvoiceOutBalanceMonthlyService {
    public static final Logger logger = Logger.getLogger(OrgInvoiceOutBalanceMonthlyServiceImpl.class);

    @Autowired
    private OrgInvoiceOutBalanceMonthlyDao orgInvoiceOutBalanceMonthlyDao;

    public void averageMonthlyBalance() {

        //获取日期
        String month = getMonth(-1);
        String previousMonth = getMonth(-2);

        logger.debug("Begin average the balance of month: " + month);
        logger.debug("Previous Month:" + previousMonth);

        //读取上月所有服务中心余额记录
        List<OrgInvoiceOutBalanceMonthly> biobms = orgInvoiceOutBalanceMonthlyDao.queryByMonthAndOrgId(month, null);
        for (OrgInvoiceOutBalanceMonthly biobm : biobms) {

            //上上月的余额
            BigDecimal previous =  getPreviousBalance(previousMonth, biobm.getOrgId());

            //计算该服务中心当前每月余额
            double balance = (biobm.getInvoiceInAmount().doubleValue()-biobm.getInvoiceOutBalance().doubleValue()) + previous.doubleValue();

            //更新给服务中心余额
            orgInvoiceOutBalanceMonthlyDao.updateInvoiceOutBalance(BigDecimal.valueOf(balance), month, biobm.getOrgId());
        }

        logger.debug("Completed average the balance of month: " + month);
    }

    private BigDecimal getPreviousBalance(String month, Long orgId){
        List<OrgInvoiceOutBalanceMonthly> previous = orgInvoiceOutBalanceMonthlyDao.queryByMonthAndOrgId(month, orgId);
        if (previous == null || previous.size() == 0) {
            return BigDecimal.ZERO;
        } else {
            return previous.get(0).getInvoiceOutBalance();
        }
    }

    /**
     *
     * 获取前N个月年月字符
     *
     * @return　返回年月格式为yyyyMM的字符，如:201508
     */
    private String getMonth(int n){
        SimpleDateFormat format = new SimpleDateFormat("yyyyMM");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, n);

        return format.format(cal.getTime());
    }
}
