
    /**  
    * @Title: PointService.java
    * @Package com.prcsteel.platform.order.service.point
    * @Description: TODO(用一句话描述该文件做什么)
    * @author Green.Ge
    * @date 2016年2月29日
    * @version V1.0  
    */
    
package com.prcsteel.platform.order.service.point;

import java.util.List;

import com.prcsteel.platform.order.model.model.ConsignOrder;
import com.prcsteel.platform.order.model.model.ConsignOrderItems;

/**
    * @ClassName: PointService
    * @Description: 积分调用接口
    * @author Green.Ge
    * @date 2016年2月29日
    *
    */

public interface PointService {
	public void earnPoint(ConsignOrder order,List<ConsignOrderItems> items);
	public void rollbackOrderPoint(String orderCode);
    public void syncMemberInfo( String mobile,String openId,String accountName);
}
