package com.prcsteel.platform.account.persist.dao;

import java.util.List;
import java.util.Map;
import com.prcsteel.platform.account.model.dto.SellerTradeDto;
import com.prcsteel.platform.account.model.query.SellerTradeQuery;
/**
 * @author zhoucai@prcsteel.com
 * @version V1.0
 * @Description: 寄售流水查询DAo
 * @date 2016/3/27
 */
public interface SellerTradeDao {
	/**
	 * 查询寄售销售列表
	 *
	 * @param query
	 * @return
	 */

	List<SellerTradeDto> querySellerTradeListByDto(SellerTradeQuery query);

	/**
	 * 统计寄售信息
	 *
	 * @param paramMap
	 * @return
	 */

	SellerTradeDto countSellerTradeFlow(Map<String, Object> paramMap);


	/**
	 * 统计寄售总条数
	 *
	 * @param query
	 * @return
	 */

	Integer countSellerFlow(SellerTradeQuery query);

	/**
	 * 为订单增加项目依赖
	 *
	 * @param query
	 * @return
	 */

	Integer changeOrderProject( SellerTradeQuery query);
}