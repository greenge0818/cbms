package com.prcsteel.platform.smartmatch.persist.dao;

import java.util.List;

import com.prcsteel.platform.smartmatch.model.query.SyncResourceQuery;
import com.prcsteel.platform.smartmatch.model.dto.ResourceDto;
import org.apache.ibatis.annotations.Param;

import com.prcsteel.platform.smartmatch.model.dto.CustBasePriceDto;
import com.prcsteel.platform.smartmatch.model.model.CustBasePrice;
import com.prcsteel.platform.smartmatch.model.model.CustBasePriceSubscriberRelation;

public interface CustBasePriceDao {
    int deleteByPrimaryKey(Long id);

    int insert(CustBasePrice record);

    int insertSelective(CustBasePrice record);

    CustBasePrice selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CustBasePrice record);
    /**
     * 根据ID修改基价
     * @param record
     * @return
     */
    int updateBasePriceById(CustBasePrice record);

    int updateByPrimaryKey(CustBasePrice record);
    
    /**
     * 查询所有基价信息
     * @return
     */
    List<CustBasePriceDto> selectBasePrice(CustBasePrice custBasePrice);
    
    /**
     * 逻辑删除基价信息
     * @param id
     * @return
     */
    int deleteBybasePriceId(Long id);
    
    /**
     * 查询基价名称是否存在
     * @param basePriceName
     * @return
     */
    CustBasePrice selectByName(@Param("basePriceName") String basePriceName,@Param("cityName") String cityName);
    
    /**
     * 获取所有的基价的类别
     * @return
     */
    List<String> getAllBasePriceType(@Param("id")Long id);

    /**
     * 获取当前基价订阅的信息
     * @param id 基价ID
     */
	List<CustBasePriceSubscriberRelation> getSubByBasePriceId(Long id);

    /**
     * 根据基价ID列表更新询价资源
     * @param basepriceIds
     * @return
     */
    int syncResourceByBasePriceIds(SyncResourceQuery query);

    /**
     * 根据基价ID列表查询关联资源
     * @param basepriceIds
     * @return
     */
    List<ResourceDto> queryRelateResourceByBasePriceIds(List<Long> basepriceIds);

}