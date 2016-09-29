package com.prcsteel.platform.order.service.order.impl;

import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.common.constants.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prcsteel.platform.order.model.model.BankTransactionInfo;
import com.prcsteel.platform.order.model.model.RefundRequest;
import com.prcsteel.platform.order.persist.dao.BankTransactionInfoDao;
import com.prcsteel.platform.order.persist.dao.RefundRequestDao;
import com.prcsteel.platform.order.service.order.RefundRequestService;

/**
 * Created by Rabbit Mao on 2015/7/21.
 */
@Service("refundRequestService")
public class RefundRequestServiceImpl implements RefundRequestService {

    @Autowired
    RefundRequestDao refundRequestDao;

    @Autowired
    BankTransactionInfoDao bankTransactionInfoDao;

    public void insert(RefundRequest refundRequest, BankTransactionInfo bankTransactionInfo){
        if (refundRequestDao.selectByBankSerialNumber(refundRequest.getRefBankSerialNumber()) != null) {
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "该流水号已被处理");
        }
        if (refundRequestDao.insert(refundRequest) != 1) {
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "插入退款申请单数据失败");
        }
        if (bankTransactionInfoDao.updateByPrimaryKey(bankTransactionInfo) != 1) {
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "更新到账表状态失败");
        }
    }

    public RefundRequest queryByBankSerialNumber(String bankSerialNumber){
        return refundRequestDao.selectByBankSerialNumber(bankSerialNumber);
    }
}
