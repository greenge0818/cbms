package com.prcsteel.platform.order.service.invoice;

import java.util.List;
import java.util.Map;

import com.prcsteel.platform.order.model.dto.PoolInTotalModifier;
import com.prcsteel.platform.order.model.model.PoolIn;
import com.prcsteel.platform.order.model.query.PoolInListQuery;

/**
 * Created by lcw on 2015/8/1.
 */
public interface PoolInService {
    int deleteByPrimaryKey(Long id);

    int insert(PoolIn record);

    int insertSelective(PoolIn record);

    PoolIn selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PoolIn record);

    int updateByPrimaryKey(PoolIn record);

    /**
     * 按公司查询
     *
     * @param paramMap  查询参数
     * @return
     */
    List<PoolIn> queryByCompany(Map<String, Object> query);

    /**
     * 按公司查询总数
     *
     * @param paramMap  查询参数
     * @return
     */
    int queryTotalByCompany(Map<String, Object> query);

    /**
     * 根据卖家ID查询应收发票合计数据
     *
     * @param sellerId  卖家ID
     * @return
     */
    PoolIn querySellerTotal(Long sellerId);

    int modifyTotalReceived(PoolInTotalModifier modifier);
    
    PoolIn queryBySellerId(Long sellerId);
    
    List<PoolIn> queryBySellerIds(List<Long> sellerIds);

    List<PoolIn> queryByDepartmentIds(List<Long> departmentIds);

    PoolIn selectByDepartmentId(Long departmentId);
}
