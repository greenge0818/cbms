package com.prcsteel.platform.order.persist.dao;

import com.prcsteel.platform.order.model.dto.IcbcBdDto;
import com.prcsteel.platform.order.model.model.IcbcBdlDetail;

import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;

public interface IcbcBdlDetailDao {
    int deleteByPrimaryKey(Long id);

    int insert(IcbcBdlDetail record);

    int insertSelective(IcbcBdlDetail record);

    IcbcBdlDetail selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(IcbcBdlDetail record);

    int updateByPrimaryKey(IcbcBdlDetail record);

    int selectBeginNumber(@Param("beginDate") String beginDate, @Param("endDate") String endDate);

    int selectSequenceNo(@Param("sequenceNo") String sequenceNo);

    String selectNextTag(@Param("beginDate") String beginDate);

    /**
     * ��ѯ�������Ƹ������
     * @param creditAmount
     * @param recipName
     * @param timeStamp
     * @return
     */
    int totalErrorPay(@Param("creditAmount") String creditAmount, @Param("recipName") String recipName, @Param("timeStamp") Date timeStamp);
    
    /**
     * 查询付款回执单信息
     * @param creditAmount 金额
     * @param recipName 客户名
     * @param recipAccNo 账号
     * @return
     */
    IcbcBdDto queryBankReceipts(@Param("creditAmount") BigDecimal creditAmount, @Param("recipName") String recipName, @Param("recipAccNo") String recipAccNo);
}