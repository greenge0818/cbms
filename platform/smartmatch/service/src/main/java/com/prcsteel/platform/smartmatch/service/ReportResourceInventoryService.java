package com.prcsteel.platform.smartmatch.service;

import com.prcsteel.platform.smartmatch.model.dto.ReportResourceInventoryDto;

/**
 * Created by Rolyer on 2015/11/27.
 */
public interface ReportResourceInventoryService {
    /**
     * 获取库存信息
     * @param areaId 地区ID
     * @param dt 时间
     * @return
     */
    ReportResourceInventoryDto queryReportResourceInventory(Long areaId, String dt);

    /**
     * 每日统计
     */
    void dailyStatistics();
}
