package com.prcsteel.platform.order.persist.dao;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.prcsteel.platform.order.model.dto.AllowanceDto;
import com.prcsteel.platform.order.model.dto.AllowanceForUpdateDto;
import com.prcsteel.platform.order.model.dto.CustAccountDto;
import com.prcsteel.platform.order.model.model.Allowance;
import com.prcsteel.platform.order.model.query.AllowanceListQuery;
import com.prcsteel.platform.order.model.query.AllowanceUpdate;


public interface AllowanceDao {
    int deleteByPrimaryKey(Long id);

    int insert(Allowance record);

    int insertSelective(Allowance record);

    Allowance selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Allowance record);

    int updateByPrimaryKey(Allowance record);

	String queryMaxAllowanceCode(String allowanceCode);

    List<AllowanceDto> queryAllowanceList(AllowanceListQuery query);

    int countAllowanceList(AllowanceListQuery query);

    
    /**
	 * 修改折让状态
	 * 
	 * @param allowanceId
	 *            折让id
	 * @param status
	 *            状态
	 * @return
	 */
    int updateRebateState (AllowanceUpdate allowanceUpdate);

    int updateByCondition(AllowanceForUpdateDto record);

    List<Allowance> queryByAllowanceId(@Param("allowanceId") Long allowanceId);

    int deleteAllowanceByIds(@Param("allowanceIds") List<Long> allowanceIds,@Param("lastUpdatedBy") String lastUpdatedBy);
    
    List<Allowance> selectAllNeedToClose ();

    /**
     * 修改卖家折让单对应的所有买家折让单的状态
     * @param allowanceUpdate
     * @return
     */
    int updateStatusByAllowanceId(AllowanceForUpdateDto allowanceUpdate);
    
    /**
     * 通过订单id查询折让单状态
     * @param orderId
     * @return
     */
    Integer selectAllowanceByOrderId (Long orderId);

    Allowance queryByAllowanceCode(String allowanceCode);
    
    /**
     * 通过客户id查询其名下部门
     * @param accountId
     * @author lixiang
     * @return
     */
    List<CustAccountDto> queryDepartmentByAccoutId(Long accountId);
}