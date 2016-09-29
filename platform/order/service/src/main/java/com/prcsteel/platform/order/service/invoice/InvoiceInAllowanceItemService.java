package com.prcsteel.platform.order.service.invoice;

import com.prcsteel.platform.order.model.dto.InvoiceInAllowanceItemDto;
import com.prcsteel.platform.order.model.model.InvoiceInAllowanceItem;

import java.util.List;

/**
 * Created by lichaowei on 2015/11/30.
 */
public interface InvoiceInAllowanceItemService {
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
}
