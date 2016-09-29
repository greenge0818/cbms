package com.prcsteel.platform.smartmatch.service;

import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.smartmatch.model.dto.WarehouseDto;
import com.prcsteel.platform.smartmatch.model.dto.WarehouseForPuzzyMatchDto;
import com.prcsteel.platform.smartmatch.model.model.Warehouse;
import com.prcsteel.platform.smartmatch.model.query.WarehouseQuery;

import java.util.List;


/**
 * Created by Rolyer on 2015/11/12.
 */
public interface WarehouseService {
    /**
     * 根据ID查询数据
     *
     * @param id
     * @return
     */
    Warehouse query(Long id);

    /**
     * 添加
     *
     * @param warehouse
     * @return
     */
    public void add(Warehouse warehouse,User user);

    /**
     * 删除
     *
     * @param id
     * @return
     */
    Integer deleteById(Long id);

    /**
     * 修改
     *
     * @param warehouse
     * @return
     */
    public void update(Warehouse warehouse,User user);

    int countByWarehouseNameAndAera(WarehouseQuery warehouseQuery);

    /**
     * 查询
     * @param warehouseQuery
     * @return
     */
    List<WarehouseDto> queryByConditions(WarehouseQuery warehouseQuery);

    public Warehouse selectById(Long id);

    /**
     * 获取所有的仓库
     *
     * @return
     */
    List<Warehouse> getAllWarehouse();

    /**
     * 获取所有的仓库用于首字母查找
     *
     * @return
     */
    List<WarehouseForPuzzyMatchDto> getAllWarehouseForPuzzyMatch();
}
