package com.prcsteel.platform.order.service.allowance;

import java.util.List;

import com.prcsteel.platform.order.model.dto.AllowanceOrderItemsDto;
import com.prcsteel.platform.order.model.dto.RebateDetailDto;
import com.prcsteel.platform.order.model.query.AllowanceDetailItemQuery;

/**
 * 
* @Title: AllowanceOrderDetailItemService.java 
* @Package com.prcsteel.platform.order.service.allowance
* @Description: 折让单详情
* @author lixiang   
* @date 2015年11月17日 上午11:53:40 
* @version V1.0
 */
public interface AllowanceOrderDetailItemService {

    /**
     * 查询折让单详情
     *
     * @param detailItemQuery 查询参数
     * @return
     */
    List<AllowanceOrderItemsDto> queryDetails (AllowanceDetailItemQuery detailItemQuery);

    /**
     * 查询未开进项折让单详情
     *
     * @param detailItemQuery 查询参数
     * @return
     */
    List<AllowanceOrderItemsDto> queryNoInvoiceDetails (AllowanceDetailItemQuery detailItemQuery);

}
