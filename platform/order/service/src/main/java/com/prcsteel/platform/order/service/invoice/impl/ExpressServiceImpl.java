package com.prcsteel.platform.order.service.invoice.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prcsteel.platform.order.model.model.Express;
import com.prcsteel.platform.order.persist.dao.ExpressDao;
import com.prcsteel.platform.order.service.invoice.ExpressService;

/**
 * Created by lcw on 2015/8/1.
 */
@Service("expressService")
public class ExpressServiceImpl implements ExpressService {

    @Autowired
    private ExpressDao expressDao;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return expressDao.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(Express record) {
        return expressDao.insert(record);
    }

    @Override
    public int insertSelective(Express record) {
        record.setLastUpdatedBy(record.getCreatedBy());
        record.setLastUpdated(new Date());
        record.setModificationNumber(0);
        record.setRowId("");
        record.setParentRowId("");
        record.setExt1("");
        record.setExt2("");
        record.setExt3("");
        record.setExt4(0);
        record.setExt5(0);
        record.setExt6(0);
        record.setExt7(new Date());
        record.setExt8(0L);
        return expressDao.insertSelective(record);
    }

    @Override
    public Express selectByPrimaryKey(Long id) {
        return expressDao.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(Express record) {
        return expressDao.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(Express record) {
        return expressDao.updateByPrimaryKey(record);
    }

    /**
     * 根据发票号查询发票
     *
     * @param expressName 快递单号
     * @return
     */
    @Override
    public Express selectByName(String expressName) {
        return expressDao.selectByName(expressName);
    }
}
