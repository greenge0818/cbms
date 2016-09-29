package com.prcsteel.platform.order.persist.dao;

import java.util.List;

import com.prcsteel.platform.order.model.dto.ConsignOrderDetailsDto;
import com.prcsteel.platform.order.model.query.ConsignOrderDetailQuery;

/**
 * 
 * @author zhoukun
 */
public interface ConsignOrderReportDao {

	List<ConsignOrderDetailsDto> queryOrderDetail(ConsignOrderDetailQuery query);
	
	Long countOrderDetail(ConsignOrderDetailQuery query);
}
