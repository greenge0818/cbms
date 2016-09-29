package com.prcsteel.platform.order.service.invoice;

import com.prcsteel.platform.order.model.dto.InvoiceInDetailAndOrdItemDto;
import com.prcsteel.platform.order.model.dto.InvoiceInDetailDto;
import com.prcsteel.platform.order.model.model.InvoiceInDetail;
import com.prcsteel.platform.acl.model.model.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by lcw on 2015/8/1.
 */
public interface InvoiceInDetailService {
    int deleteByPrimaryKey(Long id);

    int insert(InvoiceInDetail record);

    int insertSelective(InvoiceInDetail record);

    InvoiceInDetail selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(InvoiceInDetail record);

    int updateByPrimaryKey(InvoiceInDetail record);

    /**
     * 根据发票号查询详情
     *
     * @param invoiceInId 发票ID
     * @return
     */
    List<InvoiceInDetailDto> selectByInvoiceInId(Long invoiceInId);
    
    List<InvoiceInDetailAndOrdItemDto> selectDetailAndOrdItemByInvId(Long invoiceInId);
    
    int unbindOrderitemByDetailIds(List<Long> invoiceDetailId,User user);

    /**
     * 根据发票ID查询详情
     *
     * @param invoiceInId 发票ID
     * @return 详情
     */
    List<InvoiceInDetail> selectDetailByInvoiceInId(Long invoiceInId);
    
    int changePoolinDetailReceived(List<Long> invoiceDetailIds,boolean isAdd,User user);

    int deleteByIds(List<Long> ids);
}
