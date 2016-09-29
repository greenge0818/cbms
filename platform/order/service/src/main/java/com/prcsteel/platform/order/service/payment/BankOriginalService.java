package com.prcsteel.platform.order.service.payment;

import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.order.model.dto.BankOriginalDto;
import com.prcsteel.platform.account.model.model.Account;
import com.prcsteel.rest.bdl.payment.spdb.model.request.RequestBody;
import com.prcsteel.rest.bdl.payment.spdb.model.request.RequestHead;
import com.prcsteel.rest.bdl.payment.spdb.model.response.Body;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by kongbinheng on 2015/7/21.
 */
public interface BankOriginalService {

    public boolean saveBankOriginal(Body body, RequestHead requestHead, RequestBody requestBody, Integer beginNumber);

    /**
     * 更新和保存客户流水
     * @return
     */
    public Boolean saveAccountTransLog(Account account, String seqNo, String payeeAcctNo, String payeeBankNo, String payeeBankName, Date serialTime, BigDecimal txAmount, User operator);

    public int selectBeginNumber(String beginDate);

    /**
     * 查询起初余额
     * @param date
     * @return
     */
    BigDecimal getPreBlance(String date);

    /**
     * 查询期末余额
     * @return
     */
    BigDecimal getLastBlance(String date);
    
    /**
     * 查询付款回执单信息
     * @param txAmount 付款金额
     * @param payeeName 卖家客户名称
     * @param payeeAcctNo 
     * @return
     */
    BankOriginalDto queryBankReceipts(BigDecimal txAmount, String payeeName, String payeeAcctNo);
}
