package com.prcsteel.platform.account.persist.dao;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.prcsteel.platform.account.model.dto.AccountInAndOutDto;
import com.prcsteel.platform.account.model.dto.AccountTransLogDto;
import com.prcsteel.platform.account.model.model.AccountTransLog;
import com.prcsteel.platform.account.model.query.AccountInAndOutQuery;
import com.prcsteel.platform.account.model.query.AccountInfoFlowSearchQuery;

public interface AccountTransLogDao {

    List<AccountTransLogDto> queryTransLogByAccountId(@Param("accountId") Long accountId,
                                                      @Param("consignOrderCode") String consignOrderCode,
                                                      @Param("applyType") String applyType,
                                                      @Param("startTime") String startTime,
                                                      @Param("endTime") String endTime,
                                                      @Param("start") Integer start,
                                                      @Param("length") Integer length);

    int insert(AccountTransLog accountTransLog);

    int insertSelective(AccountTransLog accountTransLog);

    int totalTransLog(@Param("accountId") Long accountId,
                      @Param("consignOrderCode") String consignOrderCode,
                      @Param("applyType") String applyType,
                      @Param("startTime") String startTime,
                      @Param("endTime") String endTime);

    /**
     * 查询指定条件的资金往来
     * @param query
     * @return
     */
    List<AccountInAndOutDto> queryAccountInAndOut(AccountInAndOutQuery query);

    /**
     * 查询指定条件的资金往来总数
     * @param query
     * @return
     */
    int countAccountInAndOut(AccountInAndOutQuery query);

    /**
     * 查询指定条件的资金往来合计
     * @param query
     * @return
     */
    List<AccountInAndOutDto> queryAccountInAndOutTotal(AccountInAndOutQuery query);

    /**
     * 查询起初余额
     * @param query
     * @return
     */
    BigDecimal queryPreBlance(AccountInAndOutQuery query);

    /**
     * 查询期末余额
     * @param query
     * @return
     */
    BigDecimal queryNextBlance(AccountInAndOutQuery query);

    /**
     * 查询当前余额
     * @return
     */
    BigDecimal queryLastBlance(AccountInAndOutQuery query);

    int updateStatusById(@Param("accountId") Long accountId, @Param("consignOrderCode") String consignOrderCode);
   
    /**
     * 根据查询对象查询账户流水信息
     * @param accountInfoFlowSearchQuery
     * @return
     */
	List<AccountTransLogDto> searchFlowByQuery(AccountInfoFlowSearchQuery accountInfoFlowSearchQuery);

    /**
     * 获取分配资金或撤回资金中最大的关联单号
     * @param type
     * @return
     */
    String selectAccountTransLogByType(String type);
    
    /**
     * 查询最后一条信用流水最后一条记录
     * @author afeng
     * @return
     */
    AccountTransLog queryLastCode();
    
    /**
     * 查询部门资金撤回到公司最后一条记录
     * @author afeng
     * @return
     */
    AccountTransLog queryRevokeLastCode();

    /**
     * 根据查询对象统计账户流水信息
     * @param accountInfoFlowSearchQuery
     * @return
     */
    int countFlowByQuery(AccountInfoFlowSearchQuery accountInfoFlowSearchQuery);
    
    /**
     * 根据查询对象查询账户流水信息（精确查询）
     * @param accountInfoFlowSearchQuery
     * @return
     */
    List<AccountTransLog> queryFlowByQuery(AccountInfoFlowSearchQuery accountInfoFlowSearchQuery);
}