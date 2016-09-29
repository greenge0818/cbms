package com.prcsteel.platform.order.persist.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.prcsteel.platform.order.model.dto.InvoiceOutMainDto;
import com.prcsteel.platform.order.model.model.InvoiceOutMain;

public interface InvoiceOutMainDao {
    int deleteByPrimaryKey(Long id);

    int insert(InvoiceOutMain record);

    int insertSelective(InvoiceOutMain record);

    InvoiceOutMain selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(InvoiceOutMain record);

    int updateByPrimaryKey(InvoiceOutMain record);

    /**
     * 查询买家客户归属的服务中心财务未开票的列表
     *
     * @param paramMap
     * @return
     */
    List<InvoiceOutMainDto> selectByBuyerAndCreated(Map<String, Object> paramMap);

    /**
     * 统计买家客户归属的服务中心财务未开票的记录数
     *
     * @param paramMap
     * @return
     */
    int countByBuyerAndCreated(Map<String, Object> paramMap);

    /**
     * 查询买家客户财务未开票的销项发票信息
     *
     * @param idList
     * @return
     */
    InvoiceOutMain selectByIds(List<Long> idList);

    /**
     * 更新财务开票状态
     * @param paramMap
     * @return
     */
    int updateStatusByIds(Map<String, Object> paramMap);

    /**
     * 系统生成发票编号
     * @param orgApplyId
     * @return
     */
    String selectInvoiceOutMainCode(@Param("orgApplyId") Long orgApplyId);
}
