package com.prcsteel.platform.account.persist.dao;

import com.prcsteel.platform.account.model.model.AccountAcceptDraft;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AccountAcceptDraftDao {

    int insert(AccountAcceptDraft record);

    int insertSelective(AccountAcceptDraft record);

    int updateByPrimaryKeySelective(AccountAcceptDraft record);

    int updateByPrimaryKey(AccountAcceptDraft record);

    AccountAcceptDraft selectByAccountIdAndAcceptDraftId(AccountAcceptDraft record);
    
    /**
     * 通过部门id和银票id查询分配银票记录
     * @author afeng
     * @param departmentIds 部门id
     * @param acceptDraftId 银票id
     * @return
     */
    List<AccountAcceptDraft> queryBydepartmentIdAndAcceptDraftId(@Param("departmentIds") List<Long> departmentIds, @Param("acceptDraftId") Long acceptDraftId);

    /**
     * 通过银票id回退银票已分配金额（部门已分配金额设置为0）
     * @author chengui
     * @param acceptDraftId 银票id
     * @return
     */
    int updateAmountByAcceptDraftId(@Param("acceptDraftId") Long acceptDraftId);
}