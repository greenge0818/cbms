package com.prcsteel.platform.order.service.invoice;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.acl.model.query.SysSettingQuery;
import com.prcsteel.platform.order.model.dto.AccountInvoiceNoPassDto;
import com.prcsteel.platform.order.model.dto.AccountSendDto;
import com.prcsteel.platform.order.model.dto.InvoiceInAllowanceItemDto;
import com.prcsteel.platform.order.model.dto.InvoiceInDetailDto;
import com.prcsteel.platform.order.model.dto.InvoiceInDetailFormDto;
import com.prcsteel.platform.order.model.dto.InvoiceInDto;
import com.prcsteel.platform.order.model.dto.InvoiceKeepingDto;
import com.prcsteel.platform.order.model.enums.InvoiceInStatus;
import com.prcsteel.platform.order.model.model.Express;
import com.prcsteel.platform.order.model.model.InvoiceIn;
import com.prcsteel.platform.order.model.model.InvoiceInAllowance;
import com.prcsteel.platform.order.model.query.AccountInvoiceNoPassQuery;
import com.prcsteel.platform.order.model.query.InvoiceInQuery;
import com.prcsteel.platform.order.model.query.InvoiceKeepingQuery;

/**
 * Created by lcw on 2015/8/1.
 */
public interface InvoiceInService {
    int deleteByPrimaryKey(Long id);

    int insert(InvoiceIn record);

    int insertSelective(InvoiceIn record);

    InvoiceIn selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(InvoiceIn record);

    int updateByPrimaryKey(InvoiceIn record);

    /**
     * 根据发票号查询发票
     *
     * @param areaCode    区位码
     * @param invoiceCode 发票号
     * @return
     */
    InvoiceIn selectByCode(String areaCode, String invoiceCode);

    /**
     * 录入发票
     *
     * @param user               操作人
     * @param invoiceIn          发票
     * @param list               发票详情
     * @param invoiceInAllowance 折让
     * @param listAllowanceItem  折让详情
     * @param isCheck   是否为财务核对 true 是 false 否
     * @return
     */
    void inputInvoice(User user, InvoiceIn invoiceIn, List<InvoiceInDetailFormDto> list,
                      InvoiceInAllowance invoiceInAllowance, List<InvoiceInAllowanceItemDto> listAllowanceItem,Boolean isCheck);

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

    /**
     * 登记发票快递单号
     *
     * @param user    操作人
     * @param express 快递单
     * @param ids     发票ID集合
     * @return
     */
    Boolean checkInExpress(User user, Express express, Long[] ids);

    /**
     * 核对发票
     *
     * @param user         操作人
     * @param invoiceInDto 发票
     * @param list         发票详情
     * @return
     */
    Boolean checkInvoice(User user, InvoiceInDto invoiceInDto, List<InvoiceInDetailDto> list);

    /**
     * 更新打印状态
     *
     * @param user 操作人
     * @param ids  发票ID集合
     * @return
     */
    Boolean updatePrintStatus(User user, Long[] ids);

    /**
     * 更新发票状态
     *
     * @param user 操作人
     * @param status  发票状态
     * @param ids  发票ID
     * @return
     */
    int updateStatus(User user, InvoiceInStatus status, List<Long> ids);

    /**
     * 认证通过
     * @param user 操作人
     * @param ids 发票ID
     * @return
     */
    int authInvoiceAccept(User user, List<Long> ids);
    
    /**
     * 进项票打回
     * @param user
     * @param ids
     * @return
     */
    int rollbackStatus(User user, List<Long> ids);

    /**
     * 进项票取消认证
     * @param user
     * @param ids
     */
    void deauthentication(User user, List<Long> ids);
    
    /**
     * 认证失败
     * @param user
     * @param ids
     * @return
     */
    void authInvoiceFailed(User user, List<Long> ids);
    
    InvoiceIn selectLastBySellerId(Long sellerId);

    /**
     * 获取发票详情
     * @param id 发票ID
     * @return
     */
    InvoiceInDto fetchInvoiceInDetailById(Long id);

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
     * 恢复发票
     * @param user
     * @param ids
     */
    void restoreInvoice(User user, List<Long> ids);
    
    /**
     * 删除发票
     * @param user
     * @param ids
     */
    void deleteInvoice(User user, List<Long> ids);

    /**
     * 查询进项票总金额
     *
     * @param inQuery   查询参数
     * @return 总金额
     */
    BigDecimal querySumAmount(InvoiceInQuery inQuery);

    /**
     * 暂缓认证
     *
     * @param ids   查询参数
     * @param isDefer
     * @return int
     */
    int updateIsDefer(List<Long> ids,int isDefer);

    List<Long> checkInvoiceOut(List<Long> invoiceInIds);
    
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
    int queryExpressDeliverDays(String expressName,Long  orgid);
	
	List<AccountSendDto> invoiceInIsSend (Long invoiceId);
    /**
     * 当卖家设置成进项票黑名单，不能提交进项票
     * @author wangxianjun
     * @return
     */
    boolean invoiceIsSubmit(SysSettingQuery query);
    /**
     * 模糊查询进项票票号列表
     * @author zhoucai@prcsteel.com
     * @date:2016-3-24
     * @return
     */
    List<InvoiceIn> queryInvoiceCodeList (Map<String, Object> paramMap);
    
    /**
     * 根据订单id，查询总数
     * @author tuxianming
     * @date 2016-07-07
     */
    int totalInvoiceInDetailItemsByOrderId(Long orderId);
}
