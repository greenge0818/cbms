package com.prcsteel.platform.account.persist.dao;

import java.math.BigDecimal;
import java.util.List;

import com.prcsteel.platform.account.model.dto.GroupInforAccountDto;
import com.prcsteel.platform.account.model.model.CustGroupingInforAccount;
import com.prcsteel.platform.account.model.query.CustGroupingInforQuery;

public interface CustGroupingInforAccountDao {
    int deleteByPrimaryKey(Long id);

    int insert(CustGroupingInforAccount record);

    int insertSelective(CustGroupingInforAccount record);

    CustGroupingInforAccount selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CustGroupingInforAccount record);

    int updateByPrimaryKey(CustGroupingInforAccount record);
    
    /**
     * 客户信用额度分组查询(审核列表)
     * @param custGroupingInforQuery
     * @author lixiang
     * @return
     */
    List<GroupInforAccountDto> queryGroupInfoAccount(CustGroupingInforQuery custGroupingInforQuery);

    /**
     * 客户信用额度分组列表
     * @param custGroupingInforQuery
     * @author caochao
     * @return
     */
    List<GroupInforAccountDto> queryGroupInfo(CustGroupingInforQuery custGroupingInforQuery);

    /**
     * 客户信用额度分组数
     * @param custGroupingInforQuery
     * @author caochao
     * @return
     */
    int queryGroupInfoCount(CustGroupingInforQuery custGroupingInforQuery);

    /**
     * @description :回退公司额度
     * @author zhoucai@prcsteel.com
     * @version V1.0
     * @param flowNo
     * @return int
     */
    int deleteCompanyLimitByFlowNo( String serial);

    /**
     * @description :判断当前公司对于部门是否已创建信用额度
     * @author zhoucai@prcsteel.com
     * @version V1.0
     * @param flowNo
     * @return int
     */
    int judgeCompanyIsExit( CustGroupingInforAccount query);
    
    /**
     * 根据分组id查其组下的公司
     * @author afeng
     * @param groupId
     */
    List<CustGroupingInforAccount> queryByGroupId(Long groupId);

    /**
     * 查询分组及组下的公司信息
     * @author afeng
     * @param groupId
     */
    List<GroupInforAccountDto> queryGroupInfoById(Long groupId);

    /**
     * 查询当前分组下最大的公司额度
     * @author zhoucai@prcteel.com
     * @param groupId
     */
    BigDecimal queryGroupMaxCompanyLimt(Long groupId);

    /**
     * 按客户ID查询分组记录数
     * @author chengui
     * @param accountId
     */
    int getGroupCountByAccountId(Long accountId);
}