package com.prcsteel.platform.order.model.dto;

import com.prcsteel.platform.order.model.model.InvoiceIn;

/**
 * @author dengxiyan
 * @version V1.0
 * @Title: InvoiceInUpdateDto
 * @Package com.prcsteel.platform.order.model.dto
 * @Description: 进项发票更新dto
 * @date 2015/10/27
 */
public class InvoiceInUpdateDto extends InvoiceIn {
    private String oldStatus;

    public String getOldStatus() {
        return oldStatus;
    }

    public void setOldStatus(String oldStatus) {
        this.oldStatus = oldStatus;
    }
}
