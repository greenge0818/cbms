package com.prcsteel.platform.order.service.invoice.impl;

import com.prcsteel.platform.order.model.model.InvoiceInAllowance;
import com.prcsteel.platform.order.persist.dao.InvoiceInAllowanceDao;
import com.prcsteel.platform.order.persist.dao.InvoiceInAllowanceItemDao;
import com.prcsteel.platform.order.service.invoice.InvoiceInAllowanceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by lichaowei on 2015/11/30.
 */
@Service("invoiceInAllowanceService")
public class InvoiceInAllowanceServiceImpl implements InvoiceInAllowanceService {

    @Resource
    private InvoiceInAllowanceDao inAllowanceDao;
    @Resource
    private InvoiceInAllowanceItemDao inAllowanceItemDao;

    @Override
    public int deleteByPrimaryKey(Long id){
        return inAllowanceDao.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(InvoiceInAllowance record){
        return inAllowanceDao.insert(record);
    }

    @Override
    public int insertSelective(InvoiceInAllowance record){
        return inAllowanceDao.insertSelective(record);
    }

    @Override
    public InvoiceInAllowance selectByPrimaryKey(Long id){
        return inAllowanceDao.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(InvoiceInAllowance record){
        return inAllowanceDao.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(InvoiceInAllowance record){
        return inAllowanceDao.updateByPrimaryKey(record);
    }

    /**
     * 根据进项票Id查询
     *
     * @param invoiceInId 进项票Id
     * @return 集合
     */
    @Override
    public InvoiceInAllowance selectByInvoiceInId(Long invoiceInId){
        return inAllowanceDao.selectByInvoiceInId(invoiceInId);
    }

    /**
     * 根据进项票Id删除折让关系
     *
     * @param invoiceInId   进项票Id
     * @param lastUpdatedBy 最后修改人
     * @return 集合
     */
    @Override
    public void deleteByInvoiceInId(Long invoiceInId, String lastUpdatedBy){
        Integer flag = inAllowanceDao.deleteByInvoiceInId(invoiceInId, lastUpdatedBy);
        if (flag > 0) {
            inAllowanceItemDao.deleteByInvoiceInId(invoiceInId, lastUpdatedBy);
        }
    }
}
