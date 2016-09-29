package com.prcsteel.platform.order.service.payment.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prcsteel.platform.order.model.dto.PayRequestItemsInfoDto;
import com.prcsteel.platform.order.model.model.PayRequestItems;
import com.prcsteel.platform.order.persist.dao.PayRequestItemsDao;
import com.prcsteel.platform.order.service.payment.PayRequestItemsService;

/**
 * Created by lcw on 2015/7/27.
 */
@Service("payRequestItemsService")
public class PayRequestItemsServiceImpl implements PayRequestItemsService {
    @Autowired
    private PayRequestItemsDao payRequestItemsDao;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return payRequestItemsDao.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(PayRequestItems record) {
        return payRequestItemsDao.insert(record);
    }

    @Override
    public int insertSelective(PayRequestItems record) {
        return payRequestItemsDao.insertSelective(record);
    }

    @Override
    public PayRequestItems selectByPrimaryKey(Long id) {
        return payRequestItemsDao.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(PayRequestItems record) {
        return payRequestItemsDao.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(PayRequestItems record) {
        return payRequestItemsDao.updateByPrimaryKey(record);
    }

    /**
     * 根据付款ID查找付款详情记录
     *
     * @param requestId
     * @return
     */
    @Override
    public List<PayRequestItems> selectByRequestId(Long requestId) {
        return payRequestItemsDao.selectByRequestId(requestId);
    }

    /**
     * 根据付款ID查找付款详情记录(包含卖家合同信息)
     *
     * @param requestId
     * @return
     */
    @Override
    public List<PayRequestItemsInfoDto> selectPayInfoByRequestId(Long requestId) {
        return payRequestItemsDao.selectPayInfoByRequestId(requestId);
    }
    //add by wangxianjun 查询变更流程付款信息
    @Override
    public List<PayRequestItemsInfoDto> selectChangePayInfoByRequestId(Long requestId){
        return payRequestItemsDao.selectChangePayInfoByRequestId(requestId);
    }

	@Override
	public List<PayRequestItemsInfoDto> selectAllPayInfoByRequestId(List<Long> requestIds) {
		return payRequestItemsDao.selectAllPayInfoByRequestId(requestIds);
	}
}
