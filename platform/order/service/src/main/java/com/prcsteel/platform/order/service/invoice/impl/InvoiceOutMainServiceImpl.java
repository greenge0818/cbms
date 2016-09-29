package com.prcsteel.platform.order.service.invoice.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prcsteel.platform.order.model.dto.InvoiceOutMainDto;
import com.prcsteel.platform.order.model.model.InvoiceOutMain;
import com.prcsteel.platform.order.persist.dao.InvoiceOutMainDao;
import com.prcsteel.platform.order.service.invoice.InvoiceOutMainService;

/**
 * @author dengxiyan
 * @version V1.0
 * @Title: InvoiceOutMainServiceImpl
 * @Package com.prcsteel.platform.order.service.impl.invoice
 * @Description:
 * @date 2015/8/4
 */
@Service("invoiceOutMainService")
public class InvoiceOutMainServiceImpl implements InvoiceOutMainService {

    @Autowired
    InvoiceOutMainDao invoiceOutMainDao;

    /**
     * 查询买家客户归属的服务中心财务未开票的列表
     *
     * @param paramMap
     * @return
     */
    @Override
    public List<InvoiceOutMainDto> selectByBuyerAndCreated(Map<String, Object> paramMap) {
        InvoiceOutMainDto dto = (InvoiceOutMainDto) paramMap.get("dto");
        if (dto != null && StringUtils.isNotEmpty(dto.getBuyerName())) {
            dto.setBuyerName("%" + dto.getBuyerName().trim() + "%");
        }
        return invoiceOutMainDao.selectByBuyerAndCreated(paramMap);
    }

    /**
     * 统计买家客户归属的服务中心财务未开票的记录数
     *
     * @param paramMap
     * @return
     */
    @Override
    public int countByBuyerAndCreated(Map<String, Object> paramMap) {
        return invoiceOutMainDao.countByBuyerAndCreated(paramMap);
    }

    @Override
    public InvoiceOutMain getInvoiceOutMainByIds(String ids) {
        InvoiceOutMain invoice = new InvoiceOutMain();
        //处理ids的in条件
        if (StringUtils.isNotEmpty(ids)) {
            String[] idStrArray = ids.split(",");
            List<Long> idList = new ArrayList<Long>();
            for (String idStr : idStrArray) {
                idList.add(new Long(idStr));
            }

            if (!idList.isEmpty()) {
                return invoiceOutMainDao.selectByIds(idList);
            }
        }
        return invoice;
    }
}
