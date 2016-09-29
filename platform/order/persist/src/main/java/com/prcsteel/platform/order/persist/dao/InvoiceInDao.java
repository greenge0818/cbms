package com.prcsteel.platform.order.persist.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.prcsteel.platform.order.model.dto.AccountInvoiceNoPassDto;
import com.prcsteel.platform.order.model.query.AccountInvoiceNoPassQuery;
import org.apache.ibatis.annotations.Param;

import com.prcsteel.platform.order.model.dto.InvoiceInDto;
import com.prcsteel.platform.order.model.dto.InvoiceInUpdateDto;
import com.prcsteel.platform.order.model.dto.InvoiceKeepingDto;
import com.prcsteel.platform.order.model.model.InvoiceIn;
import com.prcsteel.platform.order.model.query.InvoiceInQuery;
import com.prcsteel.platform.order.model.query.InvoiceKeepingQuery;
import com.prcsteel.platform.order.model.query.ReportSellerInvoiceInQuery;

public interface InvoiceInDao {
    int deleteByPrimaryKey(Long id);

    int insert(InvoiceIn record);

    int insertSelective(InvoiceIn record);

    InvoiceIn selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(InvoiceIn record);

    int updateByPrimaryKey(InvoiceIn record);

    /**
     * 根据发票号查询发票
     *
     * @param invoiceCode 发票号
     * @return
     */
    InvoiceIn selectByCode(@Param("areaCode") String areaCode,@Param("invoiceCode") String invoiceCode);

    /**
     * 查询
     *
     * @param paramMap 查询参数
     * @return
     */
    List<InvoiceInDto> query(Map<String, Object> paramMap);

    /**
     * 查询总数
     *
     * @param paramMap 查询参数
     * @return
     */
    int queryTotal(Map<String, Object> paramMap);

    InvoiceIn selectLastBySellerId(@Param("sellerId") Long sellerId);

    /**
     * 根据销项发票申请ID查询相关的进项票
     *
     * @param inQuery   查询参数
     * @return 进项票集合
     */
    List<InvoiceInDto> queryByInvOutApplyIds(InvoiceInQuery inQuery);

    /**
     * 根据销项发票申请ID查询相关的进项票总数
     *
     * @param inQuery   查询参数
     * @return 总数
     */
    Integer queryByInvOutApplyIdsTotal(InvoiceInQuery inQuery);
    
    /**
     *
     * @param userName
     * @param status
     * @param ids
     * @param limitOldStatus 可作更新的状态
     * @param limitOldRelationStatus 可作更新的详情状态
     * @return
     */
    int updateStatusByIds(@Param("userName")String userName,@Param("status")String status,@Param("ids")List<Long> ids,@Param("limitOldStatus") String limitOldStatus,@Param("limitOldRelationStatus") String limitOldRelationStatus);
    /**
     * 修改认证状态时判断销项清单
     * @param userName
     * @param status
     * @param ids
     * @param limitOldStatus 可作更新的状态
     * @param limitOldRelationStatus 可作更新的详情状态
     * @return
     */
    int updateStatusByIdsWithInvoiceOut(@Param("userName") String userName, @Param("status") String status, @Param("ids") List<Long> ids, @Param("limitOldStatus") String limitOldStatus,@Param("limitOldRelationStatus") String limitOldRelationStatus);
    
    int updateRelationStatusByIds(@Param("userName")String userName,@Param("relationStatus")String relationStatus,@Param("ids")List<Long> ids);
    
    List<InvoiceIn> queryByIds(List<Long> ids);

    /**
     * 查询进项票总金额
     *
     * @param inQuery   查询参数
     * @return 总金额
     */
    BigDecimal querySumAmount(InvoiceInQuery inQuery);
    
    /**
     * 检查进项票列表是否已开销项票，返回的结果为进项票ID列表
     * 如果对应的进项票已开销项则在返回结果中包含该进项票ID
     * @param invoiceInIds
     * @return
     */
    List<Long> checkInvoiceOut(List<Long> invoiceInIds);

	int updateIsDeferForIds(@Param("ids")List<Long> ids, @Param("isDefer")int isDefer);

    int updateByConditionSelective(InvoiceInUpdateDto record);
    
	int deleteByIds(@Param("inIds")List<Long> inIds, @Param("status")String status);

	BigDecimal querySumAmountBySellerId(ReportSellerInvoiceInQuery reportSellerInvoiceInQuery);
	
	/**
	 * 查询进项票记账
	 * @param invoiceKeepingQuery
	 * @return
	 */
	List<InvoiceKeepingDto> invoiceKeeping(InvoiceKeepingQuery invoiceKeepingQuery);
	
	/**
	 * 查询进项票记账记录数
	 * @param invoiceKeepingQuery
	 * @return
	 */
	int invoiceKeepingCount(InvoiceKeepingQuery invoiceKeepingQuery);
	
	/**
	 * 查询相进项票记账确认人员
	 * @return
	 */
	List<InvoiceKeepingDto> queryCheckName(InvoiceKeepingQuery invoiceKeepingQuery);

    /**
     * 查询客户销项票审核未通过原因
     * @author wangxianjun
     * @return
     */
    List<AccountInvoiceNoPassDto> queryNoPassInvoiceReason(AccountInvoiceNoPassQuery accountInvoiceNoPassQuery);

    /**
     * 查询客户销项票审核未通过原因数量
     * @author wangxianjun
     * @return
     */
    int queryNoPassReasonCount(AccountInvoiceNoPassQuery accountInvoiceNoPassQuery);
    /**
     * 查询快递 到服务中的天数
     * @author wangxiao
     * @return
     */

    int queryExpressDeliverDays(@Param("expressName")String expressName,@Param("orgid")Long orgid);
    
    /**
     * 模糊查询进项票票号列表
     * @author zhoucai@prcsteel.com
     * @date:2016-3-24
     * @return
     */
    List<InvoiceIn> queryInvoiceCodeList (Map<String, Object> paramMap);    	
    
}
