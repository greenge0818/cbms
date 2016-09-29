package com.prcsteel.platform.smartmatch.service.impl;

import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.smartmatch.model.dto.WarehouseDto;
import com.prcsteel.platform.smartmatch.model.dto.WarehouseForPuzzyMatchDto;
import com.prcsteel.platform.smartmatch.model.model.Warehouse;
import com.prcsteel.platform.smartmatch.model.query.WarehouseQuery;
import com.prcsteel.platform.smartmatch.persist.dao.WarehouseDao;
import com.prcsteel.platform.smartmatch.persist.dao.ResourceDao;
import com.prcsteel.platform.smartmatch.service.WarehouseService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Rolyer on 2015/11/12.
 */
@Service("warehouseService")
public class WarehouseServiceImpl implements WarehouseService {
    @Resource
    private WarehouseDao warehouseDao;
    @Resource
    private ResourceDao resourceDao;

    @Override
    public Warehouse query(Long id) {
        return warehouseDao.selectByPrimaryKey(id);
    }

    //仓库的添加
    @Override
    public void add(Warehouse warehouse,User user) {
        if(warehouse.getAlias() != "" && warehouse.getAlias() != null){
            getRepetitionAlias(warehouse);
        }

        warehouse.setCreatedBy(user.getLoginId());
        warehouse.setLastUpdatedBy(user.getLoginId());
        if (warehouseDao.queryByName(warehouse).size()>0 ) {
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "仓库名称已经被使用或仓库名称与别名存在重复，请重新输入！");
        }
        if(Arrays.asList(warehouse.getAlias().split(",")).contains(warehouse.getName())){
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "仓库名称已经被使用或仓库名称与别名存在重复，请重新输入！");
        }

        if (warehouseDao.insertSelective(warehouse) != 1) {
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "添加失败");
        }

    }


    //仓库的删除
    @Override
    public Integer deleteById(Long id) {
        if(warehouseDao.selectByPrimaryKey(id) != null){
            if(resourceDao.selectByWarehouseId(id).size() > 0){
                throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,"该仓库存在相关资源，不能删除！");
            }else{
                return warehouseDao.deleteById(id);
            }
        }else{
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "找不到该仓库信息");
        }
    }

    //仓库更新
    @Override
    public void update(Warehouse warehouse,User user) {
        if(warehouse.getAlias() != "" && warehouse.getAlias() != null){
            getRepetitionAlias(warehouse);
        }
        warehouse.setLastUpdatedBy(user.getLoginId());
        if (warehouseDao.queryByName(warehouse).size()>0 ) {
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "仓库名称与别名存在重复或已经被使用，请重新输入！");
        }

        if (warehouseDao.updateByPrimaryKeySelective(warehouse) != 1) {
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "更新失败");
        }
    }

    //查询仓库总数
    @Override
    public int countByWarehouseNameAndAera(WarehouseQuery warehouseQuery) {

        return warehouseDao.countByWarehouseNameAndAera(warehouseQuery);
    }


    //查询仓库列表
    @Override
    public List<WarehouseDto> queryByConditions(WarehouseQuery warehouseQuery) {
        return warehouseDao.queryByConditions(warehouseQuery);
    }

    @Override
    public Warehouse selectById(Long id) {

        return warehouseDao.selectByPrimaryKey(id);
    }

    @Override
    public List<Warehouse> getAllWarehouse() {
        return warehouseDao.getAllWarehouse();
    }

    /**
     * 获取所有的仓库用于首字母查找
     * @return
     */
    @Override
    public List<WarehouseForPuzzyMatchDto> getAllWarehouseForPuzzyMatch() {
        return warehouseDao.getAllWarehouseForPuzzyMatch();
    }

    /**
     * 仓库别名查重
     * @param warehouse
     */
    private void getRepetitionAlias(Warehouse warehouse){
        List<Warehouse> repeatList = warehouseDao.selectByAlias(warehouse);
        List<String> aliasList = new ArrayList<String>(Arrays.asList(warehouse.getAlias().split(",")));
        if(repeatList.size()>0){
            for(Warehouse repeatWarehouse:repeatList){
                if(repeatWarehouse.getAlias()!=null && repeatWarehouse.getAlias()!=""){
                    List<String> repeatAliasList = new ArrayList<String>(Arrays.asList(repeatWarehouse.getAlias().split(",")));
                    repeatAliasList.retainAll(aliasList);
                    if(repeatAliasList.size()>0){
                        String alias = String.join(",",(String[])repeatAliasList.toArray(new String[repeatAliasList.size()]));
                        throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,"别名[" + alias + "]已经存在于仓库[" + repeatWarehouse.getName() + "]中，请重新输入！");
                    }
                }
                List<String> warehouseName = new ArrayList<String>(Arrays.asList(repeatWarehouse.getName()));
                aliasList.retainAll(warehouseName);
                if(aliasList.size()>0){
                    throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,  aliasList + "已被仓库或者仓库别名使用，请重新输入！");
                }
            }

        }

    }

}
