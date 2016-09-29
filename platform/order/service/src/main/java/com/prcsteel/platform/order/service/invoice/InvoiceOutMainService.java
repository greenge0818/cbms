package com.prcsteel.platform.order.service.invoice;

import com.prcsteel.platform.order.model.dto.InvoiceOutMainDto;
import com.prcsteel.platform.order.model.model.InvoiceOutMain;

import java.util.List;
import java.util.Map;

/**
 * @author dengxiyan
 * @version V1.0
 * @Title: InvoiceOutMainService
 * @Package com.prcsteel.platform.order.service.invoice
 * @Description:
 * @date 2015/8/4
 */
public interface InvoiceOutMainService {

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

    InvoiceOutMain getInvoiceOutMainByIds(String ids);
}
