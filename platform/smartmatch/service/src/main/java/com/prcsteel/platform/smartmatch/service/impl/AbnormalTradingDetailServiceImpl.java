package com.prcsteel.platform.smartmatch.service.impl;

import com.prcsteel.platform.smartmatch.model.model.AbnormalTradingDetail;
import com.prcsteel.platform.smartmatch.persist.dao.AbnormalTradingDetailDao;

import com.prcsteel.platform.smartmatch.service.AbnormalTradingDetailService;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Rolyer on 2015/11/27.
 */
@Service("abnormalTradingDetailService")
public class AbnormalTradingDetailServiceImpl implements AbnormalTradingDetailService {
    @Resource
    private AbnormalTradingDetailDao abnormalTradingDetailDao;


    public List<AbnormalTradingDetail> queryListByReportResourceInventory(AbnormalTradingDetail query) {
        return abnormalTradingDetailDao.queryListByReportResourceInventory(query);
    }
}
