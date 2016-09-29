package com.prcsteel.platform.account.persist.dao;

import com.prcsteel.platform.account.model.model.CustGroupingInfor;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

public interface CustGroupingInforDao {
    int deleteByPrimaryKey(Long id);

    int insert(CustGroupingInfor record);

    int insertSelective(CustGroupingInfor record);

    CustGroupingInfor selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CustGroupingInfor record);

    int updateByPrimaryKey(CustGroupingInfor record);

    /**
     * @description :回退分组公司额度
     * @author zhoucai@prcsteel.com
     * @version V1.0
     * @param flowNo
     * @return int
     */

    int deleteGroupLimitByFlowNo(String serial);


    int queryAccountNumByGroupId(Long groupId);

    /**
     * 根据部门id查询部门所在组的信用额度
     * @param deptId
     * @return
     */
    CustGroupingInfor queryGroupLimitByDeptId(Long deptId);

    int groupIsExistByName(String groupName);

    /**
     * 获取ID不为当前ID的组是否存在
     * @param groupName
     * @return
     */
    int groupIsExistByNameExceptId(@Param("groupName") String groupName, @Param("groupId") Long groupId);
    /**
     * 获取分组已用额度
     * @param groupId
     * @return
     */
    BigDecimal queryGroupUsedById(Long groupId);

}