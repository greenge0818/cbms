package com.prcsteel.platform.order.persist.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.prcsteel.platform.order.model.dto.BankOriginalDto;
import com.prcsteel.platform.order.model.dto.BankRechargeSMSDto;
import com.prcsteel.platform.order.model.model.BankOriginalDetail;

public interface BankOriginalDetailDao {
    int deleteByPrimaryKey(Long id);

    int insert(BankOriginalDetail record);

    int insertSelective(BankOriginalDetail record);

    BankOriginalDetail selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BankOriginalDetail record);

    int updateByPrimaryKey(BankOriginalDetail record);

    int selectBeginNumber(String beginDate);

    int updateStatusById(@Param("id") Long id, @Param("status") Integer status, @Param("lastUpdated") Date lastUpdated, @Param("lastUpdatedBy") String lastUpdatedBy);

    List<BankRechargeSMSDto> querySendSMS(@Param("accountId") Long accountId);

    /**
     * 查询起初余额
     * @param date
     * @return
     */
    BigDecimal queryPreBlance(String date);

    /**
     * 查询期末余额
     * @return
     */
    BigDecimal queryLastBlance(String date);

    /**
     * 查询出纳疑似付款错误
     * @param txAmount
     * @param payeeName
     * @param transDate
     * @return
     */
    int totalErrorPay(@Param("txAmount") String txAmount, @Param("payeeName") String payeeName, @Param("transDate") Date transDate);
    
    /**
     * 查询付款回执单信息
     * @param txAmount 付款金额
     * @param payeeName 卖家客户名称
     * @param payeeAcctNo 
     * @return
     */
    BankOriginalDto queryBankReceipts(@Param("txAmount") BigDecimal txAmount, @Param("payeeName") String payeeName, @Param("payeeAcctNo") String payeeAcctNo);
}