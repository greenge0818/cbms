package com.prcsteel.platform.smartmatch.persist.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.cache.annotation.Cacheable;

import com.prcsteel.platform.account.model.model.Account;
import com.prcsteel.platform.account.model.model.AccountContact;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.core.model.dto.BaseDataDto;
import com.prcsteel.platform.smartmatch.model.dto.BillHistoryDataDto;
import com.prcsteel.platform.smartmatch.model.dto.InquiryDetailDto;
import com.prcsteel.platform.smartmatch.model.dto.InquiryOrderMessageQueue;
import com.prcsteel.platform.smartmatch.model.dto.PurchaseOrderDto;
import com.prcsteel.platform.smartmatch.model.dto.PurchaseOrderDtoForShow;
import com.prcsteel.platform.smartmatch.model.model.PurchaseOrder;
import com.prcsteel.platform.smartmatch.model.query.PurchaseOrderQuery;

public interface PurchaseOrderDao {
    int deleteByPrimaryKey(Long id);

    int insert(PurchaseOrder record);

    int insertSelective(PurchaseOrder record);

    PurchaseOrder selectByPrimaryKey(Long id);

    PurchaseOrderDtoForShow queryByIdForShow(Long id);

    int updateByPrimaryKeySelective(PurchaseOrder record);

    int updateByPrimaryKey(PurchaseOrder record);

    int updateCodeById(@Param("id") Long id, @Param("code") String code);

    List<BaseDataDto> getAllAccount();

    List<AccountContact> queryAccountContact(@Param("tel") String tel, @Param("name") String name);

    List<Account> queryAccount(@Param("tel") String tel, @Param("name") String name);

    /**
     * 展示采购单列表
     * @param purchaseOrderQuery
     * @return
     */
    List<PurchaseOrderDto> selectPurchaseList(PurchaseOrderQuery purchaseOrderQuery);
    
    /**
     * 根据询价单需求单号查询询价单
     * @param requirementCode
     * @return
     */
    public Long searchPurchaseOrderByrequrimentCode(@Param("requirementCode") String requirementCode);
    /**
     * 查询采购单总数
     * @param purchaseOrderQuery
     * @return
     */
    int countPurchaseOrder(PurchaseOrderQuery purchaseOrderQuery);

    /**
     * 查询指定采购单信息
     * @param id
     * @return
     */
    PurchaseOrderDto queryByPurchaseOrderId(Long id);

    /**
     * 更新指定采购单状态
     * @param id 采购单ID
     * @param status 状态值
     * @param loginId 操作人，即：当前登录人
     * @return
     */
    int updateStatusById(@Param("id")Long id, @Param("status")String status, @Param("loginId")String loginId);

    /**
     * 查询当天的采购单中最小的id
     * @param date
     * @return
     */
    @Cacheable(value = Constant.CACHE_NAME, key = "'" + Constant.CACHE_TODAYS_MIN_PURCHASE_ORDER_ID + "' + #p0")
    Long selectMinIdByDate(String date);

    /**
     * 通过卖家id查询卖家历史交易记录
     * @param accountId
     * @return
     */
    List<BillHistoryDataDto> selectBillHistoryByAccountId(Long accountId);
    
    /**
     * 根据采购单Id获取询价详情列表 
     * @param purchaseOrderId
     * @return
     */
    List<InquiryDetailDto> selectInquiryHistoryByPurchaseId(Long purchaseOrderId);
    
	/**
	 * Mq 消息推送询价单消息保存
	 * @param msg
	 * @return
	 */
	int insertInquiryMsg(InquiryOrderMessageQueue msg);
	/**
     * 根据报价单详情的ID更新其对应的询价单的状态
     * @param id
     * @param string
     * @param loginId
     */
	public void updatePurchaseOrderStatusByQuotataionItemId(@Param("id")Long id, 
			@Param("status")String status, @Param("loginId")String loginId);
}