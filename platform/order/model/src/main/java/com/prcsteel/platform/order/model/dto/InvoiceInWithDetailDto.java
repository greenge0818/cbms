package com.prcsteel.platform.order.model.dto;

import com.prcsteel.platform.order.model.model.InvoiceInDetail;

/**
 * @author dengxiyan
 * @version V1.0
 * @Title: InvoiceInWithDetailDto
 * @Package com.prcsteel.platform.order.model.dto
 * @Description: 进项票信息和明细
 * @date 2015/9/12
 */
public class InvoiceInWithDetailDto extends InvoiceInDetail {

    private String code;

    private String orgName;

    private String sellerName;

    private String invoiceDate;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }
}
