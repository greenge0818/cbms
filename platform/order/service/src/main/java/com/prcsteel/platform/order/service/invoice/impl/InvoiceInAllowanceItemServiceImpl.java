package com.prcsteel.platform.order.service.invoice.impl;

import com.prcsteel.platform.order.model.dto.InvoiceInAllowanceItemDto;
import com.prcsteel.platform.order.model.model.InvoiceInAllowanceItem;
import com.prcsteel.platform.order.persist.dao.InvoiceInAllowanceItemDao;
import com.prcsteel.platform.order.service.invoice.InvoiceInAllowanceItemService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by lichaowei on 2015/11/30.
 */
@Service("invoiceInAllowanceItemService")
public class InvoiceInAllowanceItemServiceImpl implements InvoiceInAllowanceItemService {

    @Resource
    private InvoiceInAllowanceItemDao inAllowanceItemDao;

    @Override
    public int deleteByPrimaryKey(Long id){
        return inAllowanceItemDao.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(InvoiceInAllowanceItem record){
        return inAllowanceItemDao.insert(record);
    }

    @Override
    public int insertSelective(InvoiceInAllowanceItem record){
        return inAllowanceItemDao.insertSelective(record);
    }

    @Override
    public InvoiceInAllowanceItem selectByPrimaryKey(Long id){
        return inAllowanceItemDao.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(InvoiceInAllowanceItem record){
        return inAllowanceItemDao.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(InvoiceInAllowanceItem record){
        return inAllowanceItemDao.updateByPrimaryKey(record);
    }

    /**
     * 根据进项票Id查询
     *
     * @param invoiceInId 进项票Id
     * @return 集合
     */
    @Override
    public List<InvoiceInAllowanceItemDto> selectByInvoiceInId(Long invoiceInId){
        return inAllowanceItemDao.selectByInvoiceInId(invoiceInId);
    }
}
