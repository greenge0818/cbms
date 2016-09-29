package com.prcsteel.platform.order.persist.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.prcsteel.platform.order.model.dto.PoolInAndOutModifier;
import com.prcsteel.platform.order.model.dto.PoolInModifier;
import com.prcsteel.platform.order.model.dto.PoolInTotalModifier;
import com.prcsteel.platform.order.model.model.PoolIn;
import com.prcsteel.platform.order.model.model.PoolInIdAndInvoiceInDetail;

public interface PoolInDao {
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
    List<PoolIn> queryByCompany(Map<String, Object> paramMap);

    /**
     * 按公司查询总数
     *
     * @param paramMap  查询参数
     * @return
     */
    int queryTotalByCompany(Map<String, Object> paramMap);

    /**
     * 按服务中心查询总数
     *
     * @param orgId  查询参数
     * @return
     */
    HashMap<String,Object> queryTotalByOrg(Long orgId);
    
    int modifyTotalReceived(PoolInTotalModifier modifier);
    
    List<PoolIn> queryBySellerIds(List<Long> sellerIds);

    List<PoolIn> queryByDepartmentIds(List<Long> departmentIds);
    
    Long queryPoolinIdByInvoiceInDetailId(Long invoiceInDetailId);
    
    List<PoolInIdAndInvoiceInDetail> queryByInvoiceInDetailIds(List<Long> invoiceInDetailIds);

    int modifyPoolin(PoolInAndOutModifier modifier);
    
    PoolIn selectSellerId (@Param("sellerId") Long sellerId, @Param("departmentId") Long departmentId);
    
    int updatePoolIn (PoolInModifier poolInModifier);

    PoolIn selectByDepartmentId(Long departmentId);

    int modifyTotalReceivedByPoolInId(PoolInModifier poolInModifier);

    /**
     * 按部门id查询
     *
     * @param paramMap  查询参数
     * @return
     */
    List<PoolIn> queryByDepartment(Map<String, Object> paramMap);
}