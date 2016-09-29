package com.prcsteel.platform.order.persist.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.prcsteel.platform.account.model.query.PayMentQuery;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.order.model.dto.PayCodeDto;
import com.prcsteel.platform.order.model.dto.PayMentDto;
import com.prcsteel.platform.order.model.dto.PayRequstDto;
import com.prcsteel.platform.order.model.model.PayCustBankInfo;
import com.prcsteel.platform.order.model.model.PayCustInfo;
import com.prcsteel.platform.order.model.model.PayRequest;
import com.prcsteel.platform.order.model.query.PayRequestQuery;

public interface PayRequestDao {
    int deleteByPrimaryKey(Long id);

    int insert(PayRequest record);

    int insertSelective(PayRequest record);

    PayRequest selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PayRequest record);

    int updateByPrimaryKey(PayRequest record);

    /**
     * 查询列表
     *
     * @param paramMap
     * @return
     */
    List<PayRequest> query(Map<String, Object> paramMap);

    /**
     * 查询列表总数
     *
     * @param paramMap
     * @return
     */
    int queryTotal(Map<String, Object> paramMap);

    int updateByOrderId(long orderId);

    PayRequest selectAvailablePaymentByOrderId(Long orderId);

    
    /**
     * 付款申请单打回操作
     * @param id
     * @return
     */
    int callBackPayRequest(PayRequest payRequest);
    
    /**
     * 待确认付款前的付款申请单
     * @author lixiang
     * @param payMentQuery
     * @return
     */
    List<PayMentDto> queryPeyMentRequest(PayMentQuery payMentQuery);
    
    /**
     * 待确认付款前的付款申请单记录数
     * @author lixiang
     * @param payMentQuery
     * @return
     */
    Integer queryPeyMentRequestCount(PayMentQuery payMentQuery);
    
    /**
     * 确认付款的付款单
     * @author lixiang
     * @param payMentQuery
     * @return
     */
    List<PayMentDto> queryPeyMent(PayMentQuery payMentQuery);
    
    /**
     * 确认付款的付款单的记录数
     * @author lixiang
     * @param payMentQuery
     * @return
     */
    Integer queryPeyMentCount(PayMentQuery payMentQuery);
    
    /**
     * 查询最后一条付款申请单记录
     * @param orgId
     * @return
     */
    PayCodeDto queryLastPayCode(Long orgId);

    /** 按部门ID查询该部门人的名字
     * @author chengui
     * @param id 部门ID
     * @return
     */
    List<User> queryUserNamesByOrgId(@Param("id")Long id);
    
    /**
     * 按卖家名称 查询卖家信息
     * @author chengui
     * @param custName 完整卖家名称
     * @return
     */
	List<PayCustInfo> queryCustInfoByCustName(String custName);

	/**
     * 按卖家银行账号 查询银行相关信息
     * @author chengui
     * @param bankId 银行id
     * @return
     */
	PayCustBankInfo queryCustBankInfoByBankAccount(Long bankId);
	
	/**
	 * 预付款待审核、待打印、待确认付款列表查询
	 * @param payRequestQuery
	 * @return
	 */
	List<PayRequstDto> queryPayMentAudit(PayRequestQuery payRequestQuery);
	
	/**
	 * 预付款待审核、待打印、待确认付款列表记录数
	 * @param payRequestQuery
	 * @return
	 */
	Integer queryPayMentAuditCount(PayRequestQuery payRequestQuery);

    /**
     *  根据订单Id查询订单是否有申请付款记录
     * @param orderId 订单Id
     * @return
     */
    Integer queryApplyPayCount(Long orderId);

    /**
     *  根据订单Id查询已付款金额
     * @param orderId 订单Id
     * @return
     */
    BigDecimal queryPayAmount(Long orderId);
    /**
     *  查询变更订单已付款金额
     * @param changeOrderId 变更订单Id
     * @return
     */
    PayRequest selectAvailablePaymentByChangeOrderId(Integer changeOrderId);
    
    /**
     * 通过订单id查询所有的付款申请单
     * @author lixiang
     * 
     */
    List<PayRequest> selectAllPaymentByOrderId(Long orderId);
}
