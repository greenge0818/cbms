package com.prcsteel.platform.smartmatch.persist.dao;

import java.util.List;

import com.prcsteel.platform.smartmatch.model.dto.WarehouseDto;
import com.prcsteel.platform.smartmatch.model.dto.WarehouseForPuzzyMatchDto;
import com.prcsteel.platform.smartmatch.model.model.Warehouse;
import com.prcsteel.platform.smartmatch.model.query.WarehouseQuery;

public interface WarehouseDao {
    /**
     * 删除仓库信息
     *
     * @param id
     * @return
     */
    int deleteById(Long id);

    /**
     * 插入仓库信息
     *
     * @param warehouse
     * @return
     */
    int insert(Warehouse warehouse);

    /**
     * 插入仓库信息
     *
     * @param warehouse
     * @return
     */
    int insertSelective(Warehouse warehouse);

    /**
     * 更新仓库信息
     *
     * @param id
     * @return
     */
    Warehouse selectByPrimaryKey(Long id);

    /**
     * 更新仓库信息
     *
     * @param warehouse
     * @return
     */
    int updateByPrimaryKeySelective(Warehouse warehouse);

    /**
     * 更新仓库信息
     *
     * @param warehouse
     * @return
     */
    int update(Warehouse warehouse);

    /**
     * 分页
     * @param warehouseQuery
     * @return
     */
    int countByWarehouseNameAndAera(WarehouseQuery warehouseQuery);

    /**
     * 根据条件查询仓库结果集（需要考虑是否要分页）
     * @param warehouseQuery
     * @return
     */
    List<WarehouseDto> queryByConditions(WarehouseQuery warehouseQuery);

    /**
     * 根据仓库名查找仓库信息
     */
    List<Warehouse> queryByName(Warehouse warehouse);


    List<String> listWarehouseByName(String name);

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

    /**
     * 查询仓库别名是否已经被使用
     * @return
     */
    List<Warehouse> selectByAlias(Warehouse warehouse);
    
    String getCityByWarehouseId(Long warehouseId);
    
}