package com.prcsteel.platform.order.service.invoice;

import java.util.List;

import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.common.constants.Constant.InvoiceOutSendStatus;
import com.prcsteel.platform.order.model.dto.InvoiceOutApplyItemDetailDto;
import com.prcsteel.platform.order.model.dto.InvoiceOutCheckListDto;
import com.prcsteel.platform.order.model.dto.UnSendInvoiceOutDto;
import com.prcsteel.platform.order.model.model.InvoiceOutCheckList;
import com.prcsteel.platform.order.model.model.InvoiceOutItemDetail;
import com.prcsteel.platform.order.model.query.ChecklistDetailQuery;

/**
 * Created by kongbinheng on 15-9-17.
 */
public interface InvoiceOutCheckListService {

    /**
     * 查询待开票列表的已生成开票清单
     *
     * @param orgId
     * @param status
     * @param start
     * @param length
     * @return
     */
    List<InvoiceOutCheckListDto> queryOutChecklistByStatus(List<String> orgIds, String status, Integer start, Integer length);

    /**
     * 查询待开票列表的已生成开票清单数
     *
     * @param orgIds
     * @param status
     * @return
     */
    int totalOutChecklistByStatus(List<String> orgIds, String status);

    /**
     * 查询指定编号的清单
     *
     * @param id 清单编号
     * @return
     */
    InvoiceOutCheckList queryById(Long id);

    /**
     * 生成开票清单
     *
     * @param invoiceInIds      需要认证的进项票
     * @param suspendIds        需要暂缓认证的进项票
     * @param applyIds          所有的销项票申请主记录Id
     * @param itemDetailDtos    已选择待开的销项票
     * @param tobeOutIds        待开销项票
     * @param selectedNotOutIds 已选择不开的销项票
     * @param user              用户
     * @param modifyNums        修改对象次数集合
     * @param sendStatus        可寄出（send）、不可寄出（unsend）、全部(all)  add by tuxianming in 20160516
     */
    void generateCheckList(List<Long> invoiceInIds, List<Long> suspendIds, List<Long> applyIds,List<InvoiceOutApplyItemDetailDto> itemDetailDtos,
                           List<Long> tobeOutIds, List<Long> selectedNotOutIds, User user,List<InvoiceOutItemDetail> modifyNums, InvoiceOutSendStatus sendStatus);
    
    /**
     * 客服留存销项报表
     * @author tuxianming
     * @date 20160621
     * @param query
     * @return
     */
    List<UnSendInvoiceOutDto> queryUnsendInvoiceOut(ChecklistDetailQuery query);
    
    /**
     * 客服留存销项报表统计
     * @author tuxianming
     * @date 20160621
     * @param query
     * @return
     */
    int totalUnsendInvoiceOut(ChecklistDetailQuery query);
    
    /**
     * 根据账户下面的二结欠款更新销项票清单二结欠款
     * @author tuxianming
     * @date 20160624
     */
    void updateDebtSecondSettlement();

	void updateSend(List<Long> ids, List<Boolean> sends);
    
}
