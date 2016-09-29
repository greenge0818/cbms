package com.prcsteel.platform.order.service.invoice;

import com.prcsteel.platform.order.model.dto.InvoiceOutApplyItemDetailDto;
import com.prcsteel.platform.order.model.dto.InvoiceOutOrderDetailDto;
import com.prcsteel.platform.order.model.model.InvoiceOutItemDetail;
import com.prcsteel.platform.order.model.query.InvOutApplyItemDetailQuery;

import java.math.BigDecimal;
import java.util.List;

/** 销项票详情
 * Created by lcw363 on 2015/9/15.
 */
public interface InvoiceOutItemDetailService {
    int deleteByPrimaryKey(Long id);

    int insert(InvoiceOutItemDetail record);

    int insertSelective(InvoiceOutItemDetail record);

    InvoiceOutItemDetail selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(InvoiceOutItemDetail record);

    int updateByPrimaryKey(InvoiceOutItemDetail record);

    /**
     * 根据条件查询相关的销项票详情
     *
     * @param itemDetailQuery   查询参数
     * @return 进项票集合
     */
    List<InvoiceOutApplyItemDetailDto> query(InvOutApplyItemDetailQuery itemDetailQuery);


    /**
     * 查询开票总金额
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 开票金额
     */
    BigDecimal querySumAmount(String startTime, String endTime);
    
    /**
     * 查询申请待开总额
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 开票金额
     */
    BigDecimal queryApplyWaitAmount(String startTime, String endTime);

    /**
     *
     * 查询开票申请详情项（包含订单相关信息）
     * @param invoiceOutApplyId 开票申请编号
     * @return
     */
    List<InvoiceOutOrderDetailDto> queryInvoiceOutOrderDetailByInvoiceOutApplyId(Long invoiceOutApplyId);
    
    /**
     * 暂缓认证
     *
     * @param ids   查询参数
     * @param isDefer
     * @return int
     */
    int updateIsDefer(List<Long> ids,int isDefer);

    /**
     * 根据申请Id查询相关详情的修改次数
     *
     * @param applyIds 申请Id集合
     * @return 销项票详情集合
     */
    List<InvoiceOutItemDetail> queryModifyNumByApplyIds(List<Long> applyIds);
    
    
    /**
     * 检测销项票凭证审核状态
     * @author tuxianming
     * @date 20160516
     */
    public List<InvoiceOutApplyItemDetailDto> processCredential(List<InvoiceOutApplyItemDetailDto> list);
    
}
