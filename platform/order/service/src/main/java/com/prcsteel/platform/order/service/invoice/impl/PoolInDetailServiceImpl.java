package com.prcsteel.platform.order.service.invoice.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prcsteel.platform.order.model.dto.PoolInDetailDto;
import com.prcsteel.platform.order.model.model.PoolInDetail;
import com.prcsteel.platform.order.model.query.InvoiceDetailQuery;
import com.prcsteel.platform.order.persist.dao.PoolInDetailDao;
import com.prcsteel.platform.order.service.invoice.PoolInDetailService;

/**
 * Created by lcw on 2015/8/1.
 */
@Service("poolInDetailService")
public class PoolInDetailServiceImpl implements PoolInDetailService {
    @Autowired
    private PoolInDetailDao poolInDetailDao;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return poolInDetailDao.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(PoolInDetail record) {
        return poolInDetailDao.insert(record);
    }

    @Override
    public int insertSelective(PoolInDetail record) {
        return poolInDetailDao.insertSelective(record);
    }

    @Override
    public PoolInDetail selectByPrimaryKey(Long id) {
        return poolInDetailDao.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(PoolInDetail record) {
        return poolInDetailDao.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(PoolInDetail record) {
        return poolInDetailDao.updateByPrimaryKey(record);
    }

    /**
     * 查询
     *
     * @param paramMap 查询参数
     * @return
     */
    @Override
    public List<PoolInDetailDto> query(InvoiceDetailQuery invoiceDetailQuery) {
        return poolInDetailDao.query(invoiceDetailQuery);
    }

    /**
     * 查询应收总吨位
     *
     * @param paramMap 查询参数
     * @return
     */
    @Override
    public BigDecimal queryShouldTotalWeight(Map<String, Object> paramMap) {
        return poolInDetailDao.queryShouldTotalWeight(paramMap);
    }

    /**
     * 查询应收总金额
     *
     * @param paramMap 查询参数
     * @return
     */
    @Override
    public BigDecimal queryShouldTotalAmount(Map<String, Object> paramMap) {
        return poolInDetailDao.queryShouldTotalAmount(paramMap);
    }

    /**
     * 查询总数
     *
     * @param paramMap  查询参数
     * @return
     */
    @Override
    public int queryTotal(InvoiceDetailQuery invoiceDetailQuery){
        return poolInDetailDao.queryTotal(invoiceDetailQuery);
    }

    /**
     * 按大类，品名查询
     *
     * @param paramMap  查询参数
     * @return
     */
    @Override
    public List<PoolInDetail> queryByNsort(Map<String, Object> paramMap){
        return poolInDetailDao.queryByNsort(paramMap);
    }

    /**
     * 按大类，品名查询总数
     *
     * @param paramMap  查询参数
     * @return
     */
    @Override
    public int queryTotalByNsort(Map<String, Object> paramMap){
        return poolInDetailDao.queryTotalByNsort(paramMap);
    }
    
    @Override
    public int modifyPoolinDetailReceivedAmount(PoolInDetail modifier){
    	return poolInDetailDao.modifyPoolinDetailReceivedAmount(modifier);
    }
    
    @Override
	public PoolInDetailDto queryCombined(InvoiceDetailQuery invoiceDetailQuery) {
    	return poolInDetailDao.queryCombined(invoiceDetailQuery);
    }

	@Override
	public List<PoolInDetailDto> queryDetailByNsort(Map<String, Object> paramMap) {
		return poolInDetailDao.queryDetailByNsort(paramMap);
	}

	@Override
	public int queryTotalDetailByNsort(Map<String, Object> paramMap) {
		return poolInDetailDao.queryTotalDetailByNsort(paramMap);
	}

	@Override
	public PoolInDetailDto queryStatisDetailByNsort(Map<String, Object> paramMap) {
		return poolInDetailDao.queryStatisDetailByNsort(paramMap);
	}
	
	
}
