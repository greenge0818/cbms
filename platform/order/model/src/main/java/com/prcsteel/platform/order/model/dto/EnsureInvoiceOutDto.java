package com.prcsteel.platform.order.model.dto;

import com.prcsteel.platform.order.model.model.Express;
import com.prcsteel.platform.order.model.model.InvoiceOut;

/**
 * Created by Rabbit Mao on 2015/8/3.
 */
public class EnsureInvoiceOutDto {
    InvoiceOut invoiceOut;
    Express express;

    public InvoiceOut getInvoiceOut() {
        return invoiceOut;
    }

    public void setInvoiceOut(InvoiceOut invoiceOut) {
        this.invoiceOut = invoiceOut;
    }

    public Express getExpress() {
        return express;
    }

    public void setExpress(Express express) {
        this.express = express;
    }
}
