package com.prcsteel.platform.order.persist.dao;

import com.prcsteel.platform.order.model.model.InvoiceOutReceipt;
import org.apache.ibatis.annotations.Param;

/**
 * Created by Rolyer on 2015/9/28.
 */
public interface InvoiceOutReceiptDao {

    public int insert(InvoiceOutReceipt invoiceOutReceipt);

    public InvoiceOutReceipt query(@Param("itemDetailId") Long itemDetailId,
                                   @Param("checklistId") Long checklistId);
}
