package com.prcsteel.platform.order.persist.dao;

import com.prcsteel.platform.order.model.dto.InvoiceOutApplyItemDetailDto;
import com.prcsteel.platform.order.model.dto.InvoiceOutItemDetailDto;
import com.prcsteel.platform.order.model.dto.InvoiceOutOrderDetailDto;
import com.prcsteel.platform.order.model.model.InvoiceOutItemDetail;
import com.prcsteel.platform.order.model.query.AllowanceOrderQuery;
import com.prcsteel.platform.order.model.query.InvOutApplyItemDetailQuery;
import com.prcsteel.platform.order.model.query.InvoiceoutExpectQuery;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface InvoiceOutItemDetailDao {
    int deleteByPrimaryKey(Long id);

    int insert(InvoiceOutItemDetail record);

    int insertSelective(InvoiceOutItemDetail record);

    InvoiceOutItemDetail selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(InvoiceOutItemDetail record);

    int updateByPrimaryKey(InvoiceOutItemDetail record);

    /**
     * 根据条件查询相关的销项票
     *
     * @param itemDetailQuery   查询参数
     * @return 销项票详情集合
     */
    List<InvoiceOutApplyItemDetailDto> query(InvOutApplyItemDetailQuery itemDetailQuery);

    int batchInsert(List<InvoiceOutItemDetail> records);

    List<InvoiceOutItemDetailDto> queryInvoiceoutExpect(InvoiceoutExpectQuery query);

    int queryInvoiceoutExpectCount(InvoiceoutExpectQuery query);
    
	int deleteByIds(@Param("ids")List<Long> ids,@Param("lastUpdatedBy")String lastUpdatedBy );
	
	int deleteByDetailIds(@Param("detailIds")List<Long> detailIds,@Param("lastUpdatedBy")String lastUpdatedBy );

    /**
     * 查询开票总金额
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 开票金额
     */
    BigDecimal querySumAmount(@Param("startTime") String startTime, @Param("endTime") String endTime);
    
    /**
     * 查询申请待开总额
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 开票金额
     */
    BigDecimal queryApplyWaitAmount(@Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     *
     * 查询开票申请详情项（包含订单相关信息）
     * @param invoiceOutApplyId 开票申请编号
     * @return
     */
    List<InvoiceOutOrderDetailDto> queryInvoiceOutOrderDetailByInvoiceOutApplyId(Long invoiceOutApplyId);

    /**
     * 根据进项票ID查询开票详情
     *
     * @param invoiceInId 进项票ID
     * @return 进项票集合
     */
    List<InvoiceOutApplyItemDetailDto> queryByInvoiceInId(Long invoiceInId);

    List<Long> queryOutItemDetailIdsByInvoiceId(@Param("invoiceId") Long invoiceId, @Param("statusList") List<String> statusList);
	
	List<InvoiceOutItemDetail> queryOutItemDetailOrderByIds(@Param("invoiceId") Long invoiceId, @Param("outItemDetailIds")List<Long> outItemDetailIds);

    List<InvoiceOutItemDetailDto> queryByChecklistId(@Param("checklistId") Long checklistId);
    
    int updateIsDeferForIds(@Param("ids")List<Long> ids, @Param("isDefer")int isDefer);

	int queryCountByInvoiceOrderitemIds(List<Long> invoiceOrderitemIds);

    /**
     * 根据申请Id查询相关详情的修改次数
     *
     * @param applyIds 申请Id集合
     * @return 销项票详情集合
     */
    List<InvoiceOutItemDetail> queryModifyNumByApplyIds(@Param("applyIds") List<Long> applyIds);
    
    List<Long> queryOrderItemIds(AllowanceOrderQuery allowanceOrderQuery);
}
