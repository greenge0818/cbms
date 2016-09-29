package com.prcsteel.platform.order.service.report.impl;

import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.order.model.dto.ReportPrecipitationFundsDto;
import com.prcsteel.platform.order.model.model.ReportPrecipitationFunds;
import com.prcsteel.platform.order.model.query.ReportOrgDayQuery;
import com.prcsteel.platform.account.persist.dao.AccountDao;
import com.prcsteel.platform.order.persist.dao.ReportPrecipitationFundsDao;
import com.prcsteel.platform.acl.persist.dao.SysSettingDao;
import com.prcsteel.platform.order.service.report.ReportPrecipitationFundsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author dengxiyan
 * @version V1.0
 * @Title: ReportPrecipitationFundsServiceImpl
 * @Package com.prcsteel.platform.order.service.report.impl
 * @Description: 平台沉淀资金
 * @date 2015/12/10
 */
@Service("reportPrecipitationFundsService")
public class ReportPrecipitationFundsServiceImpl implements ReportPrecipitationFundsService {
    @Resource
    ReportPrecipitationFundsDao reportPrecipitationFundsDao;

    @Resource
    AccountDao accountDao;

    @Resource
    SysSettingDao sysSettingDao;

    @Value("${quartz.job.spdb.systemId}")
    private String systemId;

    @Override
    public void add() {
        BigDecimal balance = accountDao.queryBalance();
        String creditLimitStr = sysSettingDao.queryCurrentCreditLimit();

        //沉淀资金=所有资金账户余额 - 信用额度
        BigDecimal creditLimit = (creditLimitStr == null ? BigDecimal.ZERO : new BigDecimal(creditLimitStr));
        BigDecimal precipitationFunds = (balance == null ? BigDecimal.ZERO.subtract(creditLimit) : balance.subtract(creditLimit));

        ReportPrecipitationFunds funds = new ReportPrecipitationFunds(new Date(), balance, creditLimit, precipitationFunds,
                new Date(), systemId, systemId, new Date(), 0l);

        int count = reportPrecipitationFundsDao.insertSelective(funds);
        if (count == 0) throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,"插入沉淀资金数据失败。");
    }

    @Override
    public BigDecimal queryCurrentPrecipitationFunds(){
        BigDecimal balance = accountDao.queryBalance();
        String creditLimitStr = sysSettingDao.queryCurrentCreditLimit();

        //沉淀资金=所有资金账户余额 - 信用额度
        BigDecimal creditLimit = (creditLimitStr == null ? BigDecimal.ZERO : new BigDecimal(creditLimitStr));
        return  (balance == null ? BigDecimal.ZERO.subtract(creditLimit) : balance.subtract(creditLimit));
    }

    @Override
    public List<ReportPrecipitationFundsDto> queryByParam(ReportOrgDayQuery query) {
        return reportPrecipitationFundsDao.queryByParam(query);
    }
}

