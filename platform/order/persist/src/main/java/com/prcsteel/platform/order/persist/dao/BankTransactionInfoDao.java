package com.prcsteel.platform.order.persist.dao;

import java.util.List;
import java.util.Map;

import com.prcsteel.platform.order.model.dto.BankTransactionInfoDto;

import org.apache.ibatis.annotations.Param;

import com.prcsteel.platform.order.model.dto.LocalTransactionDataJobDto;
import com.prcsteel.platform.order.model.model.BankTransactionInfo;

public interface BankTransactionInfoDao {
    int deleteByPrimaryKey(Long id);

    int insert(BankTransactionInfo record);

    int insertSelective(BankTransactionInfo record);

    BankTransactionInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BankTransactionInfo record);

    int updateByPrimaryKey(BankTransactionInfo record);

    /**
     * 按照条件查询
     * @param param
     * @return
     */
    List<BankTransactionInfoDto> query(Map<String, Object> param);

    /**
     * 统计某种状态下的到账记录数
     * @param status
     * @param settingType 系统设置类型
     * @return
     */
    int countByStatus(@Param("status") String status, @Param("settingType") String settingType);

    /**
     * 获取本地数据库中未处理，但是公司账户已存在的列表
     * @return
     */
    List<LocalTransactionDataJobDto> queryUnprocessedForJob();
    
    /**
     * 根据客户名获取最新到账银行信息
     */
    public String selectByAccountName (String payeeName);

    /**
     * 根据id更新状态
     * @param id
     * @param status
     * @param lastUpdatedBy
     * @return
     */
    public int updateStatusById(@Param("id") Long id, @Param("status") String status, @Param("lastUpdatedBy") String lastUpdatedBy);
}