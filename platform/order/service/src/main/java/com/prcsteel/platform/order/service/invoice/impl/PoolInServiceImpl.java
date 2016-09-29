package com.prcsteel.platform.order.service.invoice.impl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.prcsteel.platform.order.service.invoice.PoolInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prcsteel.platform.order.model.dto.PoolInTotalModifier;
import com.prcsteel.platform.order.model.model.PoolIn;
import com.prcsteel.platform.order.persist.dao.PoolInDao;

/**
 * Created by lcw on 2015/8/1.
 */
@Service("poolInService")
public class PoolInServiceImpl implements PoolInService {
    @Autowired
    private PoolInDao poolInDao;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return poolInDao.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(PoolIn record) {
        return poolInDao.insert(record);
    }

    @Override
    public int insertSelective(PoolIn record) {
        return poolInDao.insertSelective(record);
    }

    @Override
    public PoolIn selectByPrimaryKey(Long id) {
        return poolInDao.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(PoolIn record) {
        return poolInDao.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(PoolIn record) {
        return poolInDao.updateByPrimaryKey(record);
    }

    /**
     * 按公司查询
     *
     * @param paramMap 查询参数
     * @return
     */
    @Override
    public List<PoolIn> queryByCompany(Map<String, Object> query) {
        return poolInDao.queryByCompany(query);
    }

    /**
     * 按公司查询总数
     *
     * @param paramMap 查询参数
     * @return
     */
    @Override
    public int queryTotalByCompany(Map<String, Object> query) {
        return poolInDao.queryTotalByCompany(query);
    }

    /**
     * 根据卖家ID查询应收发票合计数据
     *
     * @param sellerId 卖家ID
     * @return
     */
    @Override
    public PoolIn querySellerTotal(Long sellerId) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        Integer start = 0;
        Integer length = 100;
        paramMap.put("start", start);
        paramMap.put("length", length);
        paramMap.put("sellerId", sellerId);
        List<PoolIn> list = poolInDao.queryByCompany(paramMap);
        PoolIn poolIn = new PoolIn();
        if (list != null && list.size() > 0) {
            poolIn = list.get(0);
            BigDecimal totalAmount = BigDecimal.ZERO;
            BigDecimal totalWeight = BigDecimal.ZERO;
            BigDecimal totalReceivedAmount = BigDecimal.ZERO;
            BigDecimal totalReceivedWeight = BigDecimal.ZERO;
            for (PoolIn item : list) {
                totalAmount = totalAmount.add(item.getTotalAmount());
                totalWeight = totalWeight.add(item.getTotalWeight());
                totalReceivedAmount = totalReceivedAmount.add(item.getTotalReceivedAmount());
                totalReceivedWeight = totalReceivedWeight.add(item.getTotalReceivedWeight());
            }
            poolIn.setTotalAmount(totalAmount);
            poolIn.setTotalWeight(totalWeight);
            poolIn.setTotalReceivedAmount(totalReceivedAmount);
            poolIn.setTotalReceivedWeight(totalReceivedWeight);
        }
        return poolIn;
    }

    @Override
    public int modifyTotalReceived(PoolInTotalModifier modifier){
    	return poolInDao.modifyTotalReceived(modifier);
}
    
    @Override
    public PoolIn queryBySellerId(Long sellerId){
    	List<PoolIn> list = queryBySellerIds(Arrays.asList(sellerId));
    	if(list != null && list.size() > 0){
    		return list.get(0);
    	}
    	return null;
    }
    
    @Override
    public List<PoolIn> queryBySellerIds(List<Long> sellerIds){
    	return poolInDao.queryBySellerIds(sellerIds);
    }

    @Override
    public List<PoolIn> queryByDepartmentIds(List<Long> departmentIds){
        return poolInDao.queryByDepartmentIds(departmentIds);
    }

    @Override
    public PoolIn selectByDepartmentId(Long departmentId){
        return poolInDao.selectByDepartmentId(departmentId);
    }
}
