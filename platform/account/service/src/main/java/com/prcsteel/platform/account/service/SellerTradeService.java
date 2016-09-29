package com.prcsteel.platform.account.service;

import java.util.List;
import java.util.Map;
import com.prcsteel.platform.account.model.dto.SellerTradeDto;
import com.prcsteel.platform.account.model.query.SellerTradeQuery;
/**
 * @author zhoucai@prcsteel.com
 * @version V1.0
 * @Description: 销售记录
 * @date 2016/3/27
 */
public interface SellerTradeService {


    /**
     * 查询卖家交易信息列表
     *
     * @param
     * @return
     */
    List<SellerTradeDto> querySellerTradeListByDto(SellerTradeQuery query);
    

    /**
     * 统计卖家交易信息
     *
     * @param
     * @return
     */
    SellerTradeDto countSellerTradeFlow( Map<String, Object> paramMap);

    /**
     * 统计寄售总条数
     *
     * @param query
     * @return
     */

    Integer countSellerFlow( SellerTradeQuery query);

    /**
     * 为订单增加项目依赖
     *
     * @param query
     * @return
     */

    Integer changeOrderProject( SellerTradeQuery query);


    

    
    
}
