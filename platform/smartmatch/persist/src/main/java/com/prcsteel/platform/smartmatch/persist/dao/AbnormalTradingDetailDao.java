package com.prcsteel.platform.smartmatch.persist.dao;

import java.util.List;

import com.prcsteel.platform.smartmatch.model.model.AbnormalTradingDetail;

public interface AbnormalTradingDetailDao {
    /**
     * 查询指定（价格）交易异常记录
     *
     * @param query 库存监控表ID
     *              库存监控表ID
     *              卖家名称
     *              品名
     *              钢厂
     *              地区，即：仓库
     * @return
     */
    List<AbnormalTradingDetail> queryListByReportResourceInventory(AbnormalTradingDetail query);

    /**
     * 批量插入
     * @param details
     */
    void batchInsert(List<AbnormalTradingDetail> details);
}