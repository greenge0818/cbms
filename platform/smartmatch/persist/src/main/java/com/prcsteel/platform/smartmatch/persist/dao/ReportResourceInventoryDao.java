package com.prcsteel.platform.smartmatch.persist.dao;

import org.apache.ibatis.annotations.Param;

import com.prcsteel.platform.smartmatch.model.model.ReportResourceInventory;

import java.util.List;

public interface ReportResourceInventoryDao {
    /**
     * 添加
     * @param record
     * @return
     */
    int insertSelective(ReportResourceInventory record);

    /**
     * 获取库存信息列表集
     * @param areaId 地区ID
     * @param dt 时间
     * @return
     */
    List<ReportResourceInventory> queryReportResourceInventory(@Param("areaId")Long areaId, @Param("dt")String dt);
}