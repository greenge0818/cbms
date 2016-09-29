package com.prcsteel.platform.order.persist.dao;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.prcsteel.platform.order.model.dto.AllowanceOrderItemsDto;
import com.prcsteel.platform.order.model.model.AllowanceOrderDetailItem;
import com.prcsteel.platform.order.model.query.AllowanceDetailItemQuery;
import com.prcsteel.platform.order.model.query.AllowanceOrderQuery;

public interface AllowanceOrderDetailItemDao {
    int deleteByPrimaryKey(Long id);

    int insert(AllowanceOrderDetailItem record);

    int insertSelective(AllowanceOrderDetailItem record);

    AllowanceOrderDetailItem selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AllowanceOrderDetailItem record);

    int updateByPrimaryKey(AllowanceOrderDetailItem record);
    
    /**
	 * 查询折让单详情
	 * 
	 * @param detailItemQuery 查询参数
	 * @return
	 */
    List<AllowanceOrderItemsDto> queryDetails (AllowanceDetailItemQuery detailItemQuery);
    
	List<Long> queryOrderDetailIds(AllowanceOrderQuery allowanceOrderQuery);

	int batchInsert(List<AllowanceOrderDetailItem> details);

    int updateDeletedByAllowanceId(@Param("allowanceIds") List<Long> allowanceIds,@Param("lastUpdatedBy") String lastUpdatedBy);
    
    BigDecimal queryAllowanceAmount(@Param("buyerId") Long buyerId);
}
