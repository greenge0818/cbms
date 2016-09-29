package com.prcsteel.platform.order.persist.dao;

import com.prcsteel.platform.order.model.dto.InvoiceInAllowanceItemDto;
import com.prcsteel.platform.order.model.model.InvoiceInAllowanceItem;
import com.prcsteel.platform.order.model.query.AllowanceDetailItemQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface InvoiceInAllowanceItemDao {
    int deleteByPrimaryKey(Long id);

    int insert(InvoiceInAllowanceItem record);

    int insertSelective(InvoiceInAllowanceItem record);

    InvoiceInAllowanceItem selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(InvoiceInAllowanceItem record);

    int updateByPrimaryKey(InvoiceInAllowanceItem record);

    /**
     * 根据进项票Id查询
     *
     * @param invoiceInId 进项票Id
     * @return 集合
     */
    List<InvoiceInAllowanceItemDto> selectByInvoiceInId(Long invoiceInId);
    /**
     * 根据账号Id查询已经使用的折让单详情id集合
     *
     * @param detailItemQuery 查询参数
     * @return 集合
     */
    List<Long> selectByAllowanceDetailItemQuery(AllowanceDetailItemQuery detailItemQuery);

    /**
     * 根据进项票Id删除
     *
     * @param invoiceInId   进项票Id
     * @param lastUpdatedBy 最后修改人
     * @return 集合
     */
    Integer deleteByInvoiceInId(@Param("invoiceInId") Long invoiceInId, @Param("lastUpdatedBy") String lastUpdatedBy);
}