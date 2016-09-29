package com.prcsteel.platform.order.service.order;

import com.prcsteel.platform.order.model.CountOrder;
import com.prcsteel.platform.order.model.EcOrder;
import com.prcsteel.platform.order.model.query.OrderEcAppQuery;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.common.dto.ResultDto;
import com.prcsteel.platform.order.model.AppOrder;
import com.prcsteel.platform.order.model.dto.AccountOrderDto;
import com.prcsteel.platform.order.model.dto.ConsignOrderDto;
import com.prcsteel.platform.order.model.dto.ConsignOrderStatusDto;
import com.prcsteel.platform.order.model.dto.InvoiceOutApplyStatusDto;
import com.prcsteel.platform.order.model.dto.OrderContactDto;
import com.prcsteel.platform.order.model.dto.OrderItemDetailDto;
import com.prcsteel.platform.order.model.dto.OrderPayMentDto;
import com.prcsteel.platform.order.model.dto.OrderTradeCertificateDto;
import com.prcsteel.platform.order.model.dto.SecondaryDto;
import com.prcsteel.platform.order.model.model.ConsignOrder;
import com.prcsteel.platform.order.model.model.ConsignOrderAttachment;
import com.prcsteel.platform.order.model.model.ConsignOrderItems;
import com.prcsteel.platform.order.model.query.OrderPayMentQuery;
import com.prcsteel.platform.order.model.query.OrderTradeCertificateQuery;
import org.codehaus.jackson.JsonNode;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by dengxiyan on 2015/7/18.
 */
public interface ConsignOrderService {
    int deleteByPrimaryKey(Long id);

    int insertSelective(ConsignOrder record);

    ConsignOrder selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ConsignOrder record);

    int updateByPrimaryKey(ConsignOrder record);

    boolean checkIsPayByAcceptDraft(Long orderId);
    /**
     * 根据条件分页查询当前登录人的订单列表
     * 默认查询当前登录人的订单列表
     *
     * @param paramMap：map封装参数说明 key:value
     *                           userId： 当前登录人id
     *                           dto   ： 封装的订单dto:ConsignOrderDto
     *                           start：  开始记录（分页参数）
     *                           length： 每页记录数（分页参数）
     *                           array：  订单状态数组，用于in条件
     * @return
     * @author dengxiyan
     */
    List<ConsignOrderDto> selectByConditions(Map<String, Object> paramMap);

    /**
     * 根据条件统计订单总数
     *
     * @param paramMap：map封装参数说明 key:value
     *                           userId： 当前登录人id
     *                           dto   ： 封装的订单dto:ConsignOrderDto
     *                           start：  开始记录（分页参数）
     *                           length： 每页记录数（分页参数）
     *                           array：  订单状态数组，用于in条件
     * @return
     * @author dengxiyan
     */
    int totalOrderByConditions(Map<String, Object> paramMap);

    /**
     * 添加订单
     *
     * @param user              操作人
     * @param consignOrder      订单信息
     * @param consignOrderItems 资源集合
     * @param purchaseOrderId 采购单ID
     * @return
     */
    ResultDto add(User user, ConsignOrder consignOrder, List<ConsignOrderItems> consignOrderItems, Long purchaseOrderId, String quotationIds);

    /**
     * 修改订单
     *
     * @param consignOrder 订单信息
     * @param list         资源集合
     * @return
     */
    boolean modify(ConsignOrder consignOrder, List<ConsignOrderItems> list);

    /**
     * 根据单号查询交易单
     *
     * @param code ：单号
     * @return
     * @Param loginId 当前登录人ID
     */
    ConsignOrder selectByCode(String code);

    boolean setNote(ConsignOrder order);

    ConsignOrder queryById(long id);

    List<ConsignOrderItems> queryOrderItemsById(long id);

    /**
     * 获取订单状态变化
     *
     * @param orderId 订单ID
     * @return
     */
    ConsignOrderStatusDto getStatusChanges(String orderId);

    /**
     * 获得当前登录人待操作的某状态下的订单总数
     *
     * @param paramMap：map封装参数说明 key:value
     *                           userId：当前登录人id
     *                           list：  订单状态list用于in条件
     * @return
     * @author dengxiyan
     */
    int getOrderTotalByStatus(Map<String, Object> paramMap);

    /**
     * 统计当前登录人的某付款状态的订单总数
     *
     * @param paramMap：map封装参数说明 key:value
     *                           userId：当前登录人id
     *                           list：  订单状态list用于in条件
     *                           payStatus：订单的付款状态
     * @return
     * @author dengxiyan
     */
    int getPayOrderByStatus(Map<String, Object> paramMap);

    /**
     * 判断用户是否有查看该订单权限
     *
     * @param orderId
     * @param user
     * @param status
     * @return
     */
    String checkUserRight(Long orderId, User user, List<Long> userIds, String status);

    /**
     * 判断订单详情双敲是否已经全部匹配
     *
     * @param orderId
     * @return
     */
    boolean isAllMatch(Long orderId);

    /**
     * 二次结算金额计算
     *
     * @param orderId
     * @return
     */
    List<SecondaryDto> secondarySum(Long orderId);

    /**
     * 二次结算以后的订单回滚
     *
     * @param orderId
     * @return
     */
    void rollBack(Long orderId, User user);

    /**
     * 查询未付款用于系统自动关闭订单
     * @return
     */
    boolean selectOrderByPayStatus();

    /**
     * 审核订单通过
     * 修改订单状态->记录订单状态变化 ->生成所有卖家的合同号，添加合同记录
     *
     * @return
     */
    ResultDto auditOrder(Long orderId, User operator);

    /**
     * 审核订单不通过
     *
     * @param orderId
     * @param operator
     * @return
     */
    ResultDto auditRefuse(Long orderId, User operator, String note);

    /**
     * 关联订单
     *
     * @param orderId
     * @param operator
     * @return
     */
    ResultDto relateOrder(Long orderId,Boolean takeoutSecondBalance, User operator, String orderItemsList,Boolean takeoutCreditBalance);

    /**
     * 申请付款
     *
     * @return
     */
    ResultDto applyPay(Long orderId, Map<Long, Long> bankAccount, Map<Long, Double> applyMoney, Map<Long, Boolean> checkedMap, Map<Long, Boolean> isRefundCredit, User operator,String jsonIsDelayPay);

    /**
     * 审核付款申请通过
     *
     * @return
     */
    ResultDto auditPayAccept(Long orderId, Long payRequestId, User operator);

    /**
     * 审核付款申请不通过
     *
     * @return
     */
    ResultDto auditPayRefuse(Long orderId, Long payRequestId, User operator, String note);

    /**
     * 确认已付款
     *
     * @return
     */
    ResultDto confirmPayment(Long orderId, Long payRequestId, User operator, String paymentBank, Date bankAccountTime);

    /**
     * 订单关闭(确认已付款状态) add by kongbinheng 20150906
     * @param orderId
     * @param payRequestId
     * @param operator
     * @param cause
     * @return
     */
    public ResultDto confirmClose(Long orderId, Long payRequestId, User operator,String cause);

    /**
     * 关闭订单
     * @param orderId
     * @param operator
     * @param cause
     * @return
     */
    ResultDto close(Long orderId, User operator,String cause);

    /**
     * 审核订单关闭申请通过
     *
     * @param orderId
     * @param operator
     * @return
     */
    ResultDto auditClose(Long orderId, User operator);

    /**
     * 审核订单关闭申请不通过
     *
     * @param orderId
     * @param operator
     * @return
     */
    ResultDto auditCloseRefuse(Long orderId, User operator, String note);

    /**
     * 回滚订单申请
     * @param orderId
     * @param operator
     * @param cause
     * @return
     */
    ResultDto rollbackApply(Long orderId, User operator,String cause);

    /**
     * 回滚审核不通过
     *
     * @param orderId
     * @param reason
     * @param operator
     * @return
     */
    ResultDto rollbackRefuse(Long orderId,String reason, User operator);

    /**
     * 回滚审核通过(总经理)
     *
     * @param orderId
     * @param operator
     * @return
     */
    ResultDto rollbackAccept(Long orderId, User operator);

    /**
     * 订单关联银票
     * @param itemId
     * @param draftId 银票ID
     * @param draftCode 银票号
     * @param operator
     * @return
     */
    ResultDto addDraft(Long itemId, Long draftId, String draftCode, User operator);

    /**
     * 添加合同附件
     * @param orderId
     * @param contractId
     * @param attachmentfiles
     * @param operator
     * @return
     */
     ResultDto addCustomerContractAttachments(Long orderId, Long contractId, List<MultipartFile> attachmentfiles, User operator,String contractType);
    /**
     * 添加合同附件
     * @param orderId
     * @param contractId
     * @param attachmentfile
     * @param operator
     * @return
     */
    ResultDto addCustomerContractAttachment(Long orderId, Long contractId, MultipartFile attachmentfile, User operator);

    /**
     * 删除合同附件
     * @param orderId
     * @param contractId
     * @param id
     * @param operator
     * @return
     */
     ResultDto deleteCustomerContractAttachment(Long orderId, Long contractId, Long id, User operator,String contractType);

    /**
     * 修改合同上传信息
     * @param contractId
     * @param contractNo
     * @param operator
     * @return
     */
     ResultDto uploadContract(Long contractId, String contractNo, User operator,String contractType);

    /**
     * 根据条件分页查询买家客户的采购记录
     *
     * @param paramMap map封装参数说明 key:value
     *                 userId： 当前登录人id
     *                 dto   ： 封装的订单dto:ConsignOrderDto
     *                 start：  开始记录（分页参数）
     *                 length： 每页记录数（分页参数）
     * @return
     * @author dengxiyan
     */
    List<ConsignOrderDto> selectPurchaseListByConditions(Map<String, Object> paramMap);

    /**
     * 根据条件统计买家客户的采购记录数
     *
     * @param paramMap map封装参数说明 key:value
     *                 userId： 当前登录人id
     *                 dto   ： 封装的订单dto:ConsignOrderDto
     * @return
     * @author dengxiyan
     */
    int totalPurchaseByConditions(Map<String, Object> paramMap);

    /**
     * 根据条件分页查询卖家客户的销售记录
     *
     * @param paramMap map封装参数说明 key:value
     *                 userId： 当前登录人id
     *                 dto   ： 封装的订单dto:ConsignOrderDto
     *                 start：  开始记录（分页参数）
     *                 length： 每页记录数（分页参数）
     * @return
     * @author dengxiyan
     */
    List<ConsignOrderDto> selectSaleListByConditions(Map<String, Object> paramMap);

    /**
     * 根据条件统计卖家客户的销售记录
     *
     * @param paramMap map封装参数说明 key:value
     *                 userId： 当前登录人id
     *                 dto   ： 封装的订单dto:ConsignOrderDto
     * @return
     * @author dengxiyan
     */
    int totalSaleByConditions(Map<String, Object> paramMap);

    /**
     * 根据订单id，当前登录用户，订单状态和用户权限来得到对应的操作员
     *
     * @param orderId
     * @param curUserId
     * @param orderStatus
     * @param perm
     * @return
     */
    User getProcessUser(Long orderId, Long curUserId, String orderStatus, String perm);

    /**
     * 双敲输入
     *
     * @param itemNodes
     * @param orderId
     * @param right
     * @param user
     * @return
     */
    Map<String, Object> inputInfo(JsonNode itemNodes, Long orderId, String right, User user);

    /**
     * 二次结算
     *
     * @param orderId
     * @param user
     * @return
     */
    public String secondary(Long orderId, User user, String right,int actualWeight);




    public Date queryApprovedDateByOrderId(Long id);


    /**
     * 获取当前登录人的付款情况的已关联订单总数列表
     *
     * @param paramMap：map封装参数说明 key:value
     *                           userId：当前登录人id
     *                           list：  订单状态list用于in条件
     * @return
     * @author dengxiyan
     */
    List<Map<String, Object>> getPayOrderTotalList(Map<String, Object> paramMap);


    /**
     * 获取当前登录人能查看的状态订单总数列表
     *
     * @param paramMap：map封装参数说明 key:value
     *                           userId：当前登录人id
     * @return
     * @author dengxiyan
     */
    List<Map<String, Object>> getOrderTotalGroupByStatus(Map<String, Object> paramMap);

    /**
     * 订单打回
     *
     * @param orderId      订单Id
     * @param payRequestId 付款申请Id
     * @param note         打回原因
     * @param operator     操作用户
     */
    void fightback(Long orderId, Long payRequestId, String note, User operator);
    /**
	 * 查询订单的状态
	 * 
	 * @param orderId
	 *            订单id
	 * @return
	 */
    public String findByOrderId(Long orderId);

    /**
     * 查询订单列表
     * @param userId
     * @param date
     * @param start
     * @param length
     * @return
     */
    List<Map<String,Object>> appOrderList(Long userId,String date,Integer start,Integer length);

    /**
     * 根据采购单编号查询
     * @param id 采购单编号
     * @return
     */
    ConsignOrder buildConsignOrderByPurchaseOrderId(Long id);
    /**
     * 根据单号查询返利金额
     * @param code
     * @author wangxianjun
     * @return
     */
    OrderContactDto queryOrderContactByCode(String code);
    /**
     * 根据单号更新买家联系人信息
     * @param record
     * @author wangxianjun
     * @return
     */
    int updateByCodeSelective(ConsignOrder record);
    /**
     * 调用lv返利接口
     * @param
     * @author wangxianjun
     * @return
     */
    int correctRebateByPhone(String oldTel,String newTel,Double rebateAmount);

    /**
     * 调用lv添加返利接口
     * @param
     * @author wangxianjun
     * @return
     */
     String addRebateByPhone(Double rebateAmount,String phone,String contactName,Long accountId);

    /**
     * 根据单号删除返利报表数据
     * @param code
     * @author wangxianjun
     * @return
     */
    int disableRebateByOrderCode(String code);

    /**
     * 申请付款前，查看合同是否上传
     * @param orderId
     * @author wangxianjun
     * @return
     */
    int countUnUploadByOrderId(Long orderId);
    
    /**
     * 查进项票所关联到的订单的买家
     * @param invoiceId
     * @return
     */
    List<AccountOrderDto> queryAccount(Long invoiceId);
    
    /**
     * 根据订单id和invoiceId查询销项票开票状态
     * @param invoiceId
     * @param orderDetailIds
     * @return
     */
    InvoiceOutApplyStatusDto queryInvoiceOutApplyStatus(Long invoiceId, List<Long> orderDetailIds);
    
    /**
     * 初次付款信息报表
     * @author lixiang
     * @param orderPayMentQuery
     * @return
     */
    List<OrderPayMentDto> queryFirstPayMent(OrderPayMentQuery orderPayMentQuery);
    
    /**
     * 初次付款信息报表总记录数
     * @author lixiang
     * @param orderPayMentQuery
     * @return
     */
    Integer queryFirstPayMentCount(OrderPayMentQuery orderPayMentQuery);
    
    /**
     * 合计初次付款信息报表的初次付款金额
     * @author lixiang
     * @param orderPayMentQuery
     * @return
     */
    OrderPayMentDto totalFirstPayMent(OrderPayMentQuery orderPayMentQuery);

    void genFebPoint();
	
	 //通过订单ID查询二结后上传图片
    List<ConsignOrderAttachment> getAttachmentByOrderId(Map map);

    /**
     * 根据订单ID删除二结后上传图片
     * @param orderId
     * @author wangxianjun
     * @return
     */
    int deleteAttachmentByOrderId(Long orderId);

    /**
     * 二结后订单详情页面上传回单图片
     * modify by wangxianjun 20160229
     *
     * @return
     */
     List<ConsignOrderAttachment> updateOrderPic(List<MultipartFile> attachmentfiles, User user,Long orderId,String type);

    /**
     * 删除二结后订单详情页面上传的回单图片
     * modify by wangxianjun 20160229
     *
     * @return
     */
    ResultDto deletePic(Long imgId);
    /**
     * 二次结算 直接完成订单
     *
     * @param orderId
     * @param user
     * @return
     */
//    public String secondaryAccomplish(Long orderId, User user, String right);


    /**
     * 计算部门可用信用额度 = 组内可用信用额度与部门可信用额度较小值
     * @param deptId
     * @return
     */
    BigDecimal calculateBalanceCreditAmountOfDeptByDeptId(Long deptId);

    List<OrderItemDetailDto> selectOrdersByParams(OrderTradeCertificateQuery query);
    int selectTotalOrdersByParams(OrderTradeCertificateQuery query);
    /**
     *
     * @Author: wangxianjun
     * @Description: 查询设置的凭证名称
     * @Date: 2016年4月8日
     * @param cert 凭证号，id 客户id  type 客户类型
     */
    List<String> findCertnamesByAccount(String cert,Long id,String type);

    /**
     * 卖家，买家凭证列表 
     * @param query
     * @return
     */
    List<OrderTradeCertificateDto> selectTradeCertificateList(OrderTradeCertificateQuery query);
    //根据凭证号查询所有对应的订单id
    List<Long> findOrderIdIdByCert(String cert);
    /**
     *
     * @Author: wangxianjun
     * @Description: 查询设置的凭证名称
     * @Date: 2016年5月16日
     * @param orderId certType 凭证类型
     */
    public List<String> findCertnames(Long orderId,String certType);

    /**
     *
     * @Author: chengui
     * @Description: 按买家账户ID、订单状态查询订单信息
     * @param accountId 买家账户ID
     * @param status 订单状态
     */
    List<ConsignOrder> selectByAccountIdAndStatus(Long accountId, String status);
    /**
     *
     * @Author: wangxianjun
     * @Description: 超市用户ID查询对应的订单信息
     * @param query 超市用户ID
     */
    List<AppOrder> selectOrderByecUserId(OrderEcAppQuery query);
    /**
     *
     * @Author: wangxianjun
     * @Description: 超市用户ID查询对应的订单数量
     * @param query 超市用户ID
     */
    int countOrderByecUserId(OrderEcAppQuery query);
    /**
     *
     * @Author: wangxianjun
     * @Description:订单ID查询对应的订单
     * @param orderIds 订单列表
     */
     List<EcOrder> selectByOrderIds(List<Long> orderIds);

    /**
     *
     * @Author: wangxianjun
     * @Description: 超市用户ID查询对应的订单各状态数量
     * @param userId 超市用户ID
     */
    CountOrder countEcOrder(Integer userId);

    String toBinary (String str);
    //根据订单id查询变更合同是否上传
    int countUnChangeUploadByOrderId(Long orderId,Integer orderChangId);
}
