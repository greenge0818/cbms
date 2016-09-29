package com.prcsteel.platform.order.persist.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.prcsteel.platform.order.model.model.InvoicePool;

public interface InvoicePoolDao {
    int deleteByPrimaryKey(Long id);

    int insert(InvoicePool record);

    int insertSelective(InvoicePool record);

    InvoicePool selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(InvoicePool record);

    int updateByPrimaryKey(InvoicePool record);

    /**
     * 根据服务中心查询发票池
     *
     * @param orgId
     * @param type
     * @return
     */
    List<InvoicePool> queryByOrgId(@Param("orgId") Long orgId, @Param("type") String type);

    /**
     * 根据相关条件（orgId，type，品规）查询是否存在，存在返回对象
     *
     * @param invoicePool 参数
     * @return
     */
    InvoicePool queryIsExist(InvoicePool invoicePool);
    
    /**
     * 查询服务中某月某类型发票总额
     * @param orgId
     * @param type
     * @return
     */
    HashMap<String,Object> queryForOrgMonthSum(@Param("orgId") Long orgId, @Param("type") String type,@Param("month") String month);

    List<InvoicePool> queryOrgNsortSpec(@Param("orgId") Long orgId, @Param("type") String type);
}