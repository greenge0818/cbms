package com.prcsteel.platform.order.service.receipt;

/**
 * Created by Rolyer on 2015/9/24.
 */
public interface ReceiptService {

    /**
     * 同步票据数据
     */
    public void syncReceiptData();

    /**
     * 从爱信诺系统里拿出已经开过销项票，同步到本地系统
     */
	void executeSynInvoiceOutBack();

}
