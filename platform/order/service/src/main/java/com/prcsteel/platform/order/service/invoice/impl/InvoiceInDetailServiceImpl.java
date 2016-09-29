package com.prcsteel.platform.order.service.invoice.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.common.utils.CbmsNumberUtil;
import com.prcsteel.platform.order.model.dto.*;
import com.prcsteel.platform.order.service.invoice.PoolInDetailService;
import com.prcsteel.platform.order.service.order.ConsignOrderItemsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.order.model.model.ConsignOrderItems;
import com.prcsteel.platform.order.model.model.InvoiceInDetail;
import com.prcsteel.platform.order.model.model.InvoiceInDetailOrderItem;
import com.prcsteel.platform.order.model.model.PoolInDetail;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.order.persist.dao.InvoiceInDetailDao;
import com.prcsteel.platform.order.persist.dao.InvoiceInDetailOrderitemDao;
import com.prcsteel.platform.order.persist.dao.PoolInDao;
import com.prcsteel.platform.order.service.invoice.InvoiceInDetailService;

/**
 * Created by lcw on 2015/8/1.
 */
@Service("invoiceInDetailService")
public class InvoiceInDetailServiceImpl implements InvoiceInDetailService {

	private static final Logger logger = LoggerFactory.getLogger(InvoiceInDetailServiceImpl.class);
	
	@Resource
    private InvoiceInDetailDao invoiceInDetailDao;
    
    @Resource
    private InvoiceInDetailOrderitemDao invoiceInDetailOrderitemDao;
    
    @Resource
    private ConsignOrderItemsService consignOrderItemsService;
    
    @Resource
    private PoolInDetailService poolInDetailService;
    
    @Resource
    private PoolInDao poolInDao;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return invoiceInDetailDao.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(InvoiceInDetail record) {
        return invoiceInDetailDao.insert(record);
    }

    @Override
    public int insertSelective(InvoiceInDetail record) {
        return invoiceInDetailDao.insertSelective(record);
    }

    @Override
    public InvoiceInDetail selectByPrimaryKey(Long id) {
        return invoiceInDetailDao.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(InvoiceInDetail record) {
        return invoiceInDetailDao.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(InvoiceInDetail record) {
        return invoiceInDetailDao.updateByPrimaryKey(record);
    }

    /**
     * 根据发票号查询详情
     *
     * @param invoiceInId 发票ID
     * @return
     */
    @Override
    public List<InvoiceInDetailDto> selectByInvoiceInId(Long invoiceInId){
        return invoiceInDetailDao.selectByInvoiceInId(invoiceInId);
    }

    @Override
    public List<InvoiceInDetailAndOrdItemDto> selectDetailAndOrdItemByInvId(Long invoiceInId){
    	List<InvoiceInDetail> list = this.selectDetailByInvoiceInId(invoiceInId);
    	List<Long> detailIds = list.stream().map(a -> a.getId()).collect(Collectors.toList());
    	List<InvoiceInDetailOrderItem> details = invoiceInDetailOrderitemDao.selectByDetailIds(detailIds);
    	return list.stream().map(item -> {
    		InvoiceInDetailAndOrdItemDto ri = new InvoiceInDetailAndOrdItemDto();
    		ri.setAmount(item.getAmount());
    		try {
				BeanUtils.copyProperties(item,ri);
			} catch (Exception e) {
				logger.error("can't copy invoice in detail properties.",e);
				throw new BusinessException(Constant.EXCEPTIONCODE_SYSTEM, "查询数据时出错。");
			}
    		List<InvoiceInDetailOrderItem> itemDetails = details.stream().filter(d -> d.getInvoiceDetailId().equals(item.getId())).collect(Collectors.toList());
    		ri.setDetailOrderItems(itemDetails);
    		return ri;
    	}).collect(Collectors.toList());
    	
    }
    
    /**
     * 取消与发票详情绑定的所有订单详情
     * @param invoiceDetailId
     */
    @Override
    public int unbindOrderitemByDetailIds(List<Long> invoiceDetailIds,User user) {
    	// 更新订单详情的已开金额
    	int res = invoiceDetailIds.stream().mapToInt(a -> consignOrderItemsService.restoreOrderitemInvoiceIn(a)).sum();
        
        // 更新卖家对应品规的已收金额
    	res += changePoolinDetailReceived(invoiceDetailIds,false,user);
        
        // 将关联关系项设置成未生效
    	res += invoiceDetailIds.stream().mapToInt(a -> invoiceInDetailOrderitemDao.setInactiveByDetailId(a)).sum();
        return res;
    }

    @Override
	public int changePoolinDetailReceived(List<Long> invoiceDetailIds,boolean isAdd,User user) {
    	if(invoiceDetailIds == null || invoiceDetailIds.size() == 0){
    		return 0;
    	}
    	List<ConsignOrderItems> items = consignOrderItemsService.queryOrderItemsByDetailIds(invoiceDetailIds);
		List<InvoiceInDetailOrderItem> detailItems = invoiceInDetailOrderitemDao.selectByDetailIds(invoiceDetailIds);
    	return detailItems.stream().mapToInt(detail -> {
    		ConsignOrderItems item = items.stream().filter(a -> a.getId().equals(detail.getOrderitemId())).findFirst().get();
    		
    		PoolInDetail modifier = new PoolInDetail();
            Long poolInId = poolInDao.queryPoolinIdByInvoiceInDetailId(detail.getInvoiceDetailId());
            modifier.setPoolInId(poolInId);
            modifier.setNsortName(item.getNsortName());
            modifier.setMaterial(item.getMaterial());
            modifier.setSpec(item.getSpec());
            BigDecimal amount = detail.getAmount();
            BigDecimal weight = detail.getWeight();
            modifier.setReceivedAmount(amount.multiply(new BigDecimal(isAdd ? 1 : -1)));
            modifier.setReceivedWeight(weight.multiply(new BigDecimal(isAdd ? 1 : -1)));
            modifier.setLastUpdatedBy(user.getName());
            return poolInDetailService.modifyPoolinDetailReceivedAmount(modifier);
        }).sum();
    }

    /**
     * 根据发票ID查询详情
     *
     * @param invoiceInId 发票ID
     * @return 详情
     */
    @Override
    public List<InvoiceInDetail> selectDetailByInvoiceInId(Long invoiceInId){
        return invoiceInDetailDao.selectDetailByInvoiceInId(invoiceInId);
    }

    @Override
    public int deleteByIds(List<Long> ids) {
        return invoiceInDetailDao.deleteByIds(ids);
    }
}
