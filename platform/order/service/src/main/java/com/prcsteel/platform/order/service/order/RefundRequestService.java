package com.prcsteel.platform.order.service.order;

import com.prcsteel.platform.order.model.model.BankTransactionInfo;
import com.prcsteel.platform.order.model.model.RefundRequest;

/**
 * Created by Rabbit Mao on 2015/7/21.
 */
public interface RefundRequestService {
    public void insert(RefundRequest refundRequest, BankTransactionInfo bankTransactionInfo);

    public RefundRequest queryByBankSerialNumber(String bankSerialNumber);
}
