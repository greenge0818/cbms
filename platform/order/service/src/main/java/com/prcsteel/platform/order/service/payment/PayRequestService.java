package com.prcsteel.platform.order.service.payment;

import com.prcsteel.platform.account.model.dto.PaymentCreateDto;
import com.prcsteel.platform.account.model.query.PayMentQuery;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.order.model.dto.PayMentDto;
import com.prcsteel.platform.order.model.dto.PayRequstDto;
import com.prcsteel.platform.order.model.model.PayCustBankInfo;
import com.prcsteel.platform.order.model.model.PayCustInfo;
import com.prcsteel.platform.order.model.model.PayRequest;
import com.prcsteel.platform.order.model.query.PayRequestQuery;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by lcw on 2015/7/25.
 */
public interface PayRequestService {
    int deleteByPrimaryKey(Long id);

    int insert(PayRequest record);

    int insertSelective(PayRequest record);

    PayRequest selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PayRequest record);

    int updateByPrimaryKey(PayRequest record);

    /**
     * 申请提现
     *
     * @param user   操作用户对象
     * @param bankId 提现银行账号的ID
     * @param money  申请提现的金额
     * @param balance  原始金额
     * @return
     */
    void applyWithdraw(User user, Long departmentId, Long bankId, BigDecimal money, BigDecimal balance);

    /**
     * 生成Code
     *
     * @return
     */
    String createCode();
    
	/**
	 * 生成付款申请单收款方的付款申请单编号 新的提单号生成规则：类型：01 + 服务中心代码 + 6位年月日 + 5位流水号
	 * 例：杭州服务中心在2016年03月07日先生成了一个付款申请单 无锡服务中心在2016年03月07日后生成了一个付款申请单
	 * 杭州服务中心在2016年03月07日又生成了第二个付款申请单 则：杭州服务中心付款申请单号：01HZ16030700001
	 * 前台显示时隐藏类型号01，显示为HZ16030700001 无锡服务中心付款申请单号：01WX16030700002
	 * 前台显示时隐藏类型号01，显示为WX16030700002 杭州服务中心付款申请单号：01HZ16030700002
	 * 前台显示时隐藏类型号01，显示为HZ16030700002
	 * @author lixiang
	 */
    String createdPayCode(String orgCode, Long orgId);

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

    /**
     * 审核提现
     *
     * @param user  审核人
     * @param id    付款申请记录ID
     * @param check true：通过，false：不通过
     * @param declineReason 不通过原因
     * @return 成功 true，失败 false
     */
    Boolean checkWithdraw(User user, Long id, Boolean check,String declineReason);

    /**
     * 确认提现
     *
     * @param user  审核人
     * @param id    付款申请记录ID
     * @return 成功 true，失败 false
     */
    Boolean confirmWithdraw(User user, Long id, String paymentBank, Date bankAccountTime);

    /**
     * 更新打印状态
     *
     * @param user  审核人
     * @param id    付款申请记录ID
     * @return 成功 true，失败 false
     */
    void updatePrintStatus(User user, Long id, String ip);

    PayRequest selectAvailablePaymentByOrderId(Long orderId);
    
    /**
     * 二次结算申请单审核状态查询
     * @param requestId
     * @return
     */
    public void selectStatus (Long requestId);
    
    void callBackPayRequest(Long requestId, User user);
    
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
     * 关闭付款申请单
     * @param requestId 付款申请单id
     * @param user
     */
    void closedPayment(Long requestId, User user);

    /**
     * 按部门ID查询该部门人的名字
     * @author chengui
     * @param id 部门ID
     * @return
     */
	List<User> queryUserNamesByOrgId(Long id);

	 /**
     * 按卖家名称 模糊查询卖家信息
     * @author chengui
     * @param custName 卖家名称
     * @return
     */
	List<PayCustInfo> queryCustInfoByCustName(String custName);
	
	 /**
     * 添加 新增付款记录
     * @author chengui
     * @param paymentCreateDto 新增付款参数实体
     * @return
     */
	boolean insertPayRequest(PaymentCreateDto paymentCreateDto);
	
	/**
     * 按卖家银行账号 查询银行相关信息
     * @author chengui
     * @param bankId 银行ID
     * @return
     */
	PayCustBankInfo queryCustBankInfoByBankAccount(Long bankId);
	
	/**
	 * 预付款待审核、待打印、待确认付款列表查询
	 * @param payMentQuery
	 * @return
	 */
	List<PayRequstDto> queryPayMentAudit(PayRequestQuery payRequestQuery);
	
	/**
	 * 预付款待审核、待打印、待确认付款列表记录数
	 * @param payMentQuery
	 * @return
	 */
	Integer queryPayMentAuditCount(PayRequestQuery payRequestQuery);
	
	/**
     * 审核预付款
     *
     * @param user  审核人
     * @param id    付款申请记录ID
     * @param check true：通过，false：不通过
     * @param declineReason 不通过原因
     * @return 成功 true，失败 false
     */
    void checkAdvance(User user, Long requestId, Boolean check,String declineReason);
    
    /**
     * 确认预付款付款
     *
     * @param user  审核人
     * @param id    付款申请记录ID
     * @return 成功 true，失败 false
     */
    void confirmRequest(User user, Long id, String paymentBank, Date bankAccountTime);

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
