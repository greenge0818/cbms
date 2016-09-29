package com.prcsteel.platform.order.web.job;

import com.prcsteel.platform.common.utils.Tools;
import com.prcsteel.platform.account.model.model.AccountContact;
import com.prcsteel.platform.order.model.model.WithDraw;
import com.prcsteel.platform.account.service.AccountContactService;
import com.prcsteel.platform.order.service.withdraw.WithdrawService;
import com.prcsteel.donet.iv.finance.model.response.DealDetail;
import com.prcsteel.donet.iv.finance.model.response.DealDetailList;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by caochao on 2015/9/9.
 */
@Component
public class SyncWithDrawJob extends CbmsJob {
    private static final Logger logger = Logger.getLogger(SyncWithDrawJob.class);
    private static final int LIMITRECORDCOUNT = 500; //每次同步的最大记录数

    @Autowired
    WithdrawService withdrawService;

    @Autowired
    AccountContactService accountContactService;

    @Value("${quartz.job.spdb.systemId}")
    private String systemId;

    @Override
    public void execute() {
        if (isEnabled()) {
            logger.debug("SyncWithDraw job execute start");
            try {
                int curCount = 0;
                do {
                    int maxId = withdrawService.queryMaxSyncId();
                    DealDetailList dealDetailList = withdrawService.getLatestWithdrawRecord(maxId, LIMITRECORDCOUNT);
                    if (dealDetailList == null || dealDetailList.getDealDetails() == null) {
                        curCount = 0;
                    } else {
                        for (DealDetail item : dealDetailList.getDealDetails()) {
                            String mobile = item.getMobilePhone();
                            if (StringUtils.isNotEmpty(mobile)) {
                                AccountContact contact = accountContactService.queryByTel(mobile);
                                WithDraw withDraw = new WithDraw();
                                if (contact != null) {
                                    withDraw.setAccountId(contact.getAccountId());
                                    withDraw.setContactId(contact.getId().toString());
                                    withDraw.setContactName(contact.getName());
                                }
                                withDraw.setContactTel(mobile);
                                withDraw.setWithdrawAmount(new BigDecimal(item.getMoney()));
                                withDraw.setWithdrawDate(Tools.strToDate(item.getDealDate()));
                                withDraw.setBalance(new BigDecimal(item.getBalance()));
                                withDraw.setSyncId(item.getPayInID());
                                withDraw.setCreated(new Date());
                                withDraw.setCreatedBy(systemId); //系统创建
                                withDraw.setLastUpdated(new Date());
                                withDraw.setLastUpdatedBy(systemId);
                                withDraw.setModificationNumber(0);
                                withdrawService.insert(withDraw);
                            }
                        }
                        curCount = dealDetailList.getDealDetails().size();
                    }
                }
                while (curCount == LIMITRECORDCOUNT);
            } catch (Exception e) {
                logger.error("Quartz SyncWithDrawJob Exception", e);
            }
            logger.debug("SyncWithDraw job execute end");
        }
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
