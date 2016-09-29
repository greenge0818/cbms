package com.prcsteel.platform.order.persist.dao;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import com.prcsteel.platform.order.model.dto.InvoiceOutApplyDto;
import com.prcsteel.platform.order.model.query.InvoiceOutApplyQuery;
import org.apache.ibatis.annotations.Param;

import com.prcsteel.platform.order.model.model.InvoiceOutApply;

public interface InvoiceOutApplyDao {
    public int deleteByPrimaryKey(Long id);

    public int insert(InvoiceOutApply record);

    public int insertSelective(InvoiceOutApply record);

    public InvoiceOutApply selectByPrimaryKey(Long id);

    public int updateByPrimaryKeySelective(InvoiceOutApply record);

    public int updateByPrimaryKey(InvoiceOutApply record);

    public BigDecimal queryTotalApplyAmount(InvoiceOutApplyQuery invOutApplyQuery);

    public Integer countApplyNumber(@Param("ownerId") Long ownerId, @Param("buyerId") Long buyerId);

    //本次本部门申请人
    public List<HashMap<String, Object>> getApplyersByOrgId(Long orgId);

    //本次某人申请列表
    public List<HashMap<String, Object>> getApplyListByApplyer(Long applyerId);

    public List<InvoiceOutApply> selectApplyByOrgId(@Param("ownerId") Long ownerId, @Param("orgId") Long orgId, @Param("buyerId") Long buyerId);

    public List<InvoiceOutApplyDto> queryInvoiceOutApply(@Param("orgIds") List<Long> orgIds,
                                                         @Param("startTime") String startTime,
                                                         @Param("endTime") String endTime,
                                                         @Param("start") Integer start,
                                                         @Param("length") Integer length,
                                                         @Param("statusList") List<String> statusList);

    public int totalInvoiceOutApply(@Param("orgIds") List<Long> orgIds, 
						    		@Param("startTime") String startTime, 
						    		@Param("endTime") String endTime, 
						    		@Param("statusList") List<String> statusList);

    public int updateStatusById(@Param("id") Long id, @Param("status") String status);

	public int deleteForId(@Param("id")Long id,
			@Param("lastUpdatedBy")String lastUpdatedBy,@Param("statusList") List<String> statusList);

    /**
     * 批量更新状态
     * @param ids 被新的id集合
     * @param status 状态
     * @return
     */
    public int batchUpdateStatusById(@Param("ids") List<Long> ids, @Param("status") String status);

    /**
     * 根据Ids查询集合
     * @param ids id集合
     * @return
     */
    List<InvoiceOutApply> queryInvoiceOutApplyByIds(List<Long> ids);
    
    void decreaseApplyAmount(@Param("amount")double amount,@Param("applyId")Long applyId,@Param("operatorName")String operatorName);

    List<Long> selectOrderIdsByApplyId(Long id);
}
