package com.prcsteel.platform.order.persist.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.prcsteel.platform.order.model.dto.PoolInAndOutModifier;
import com.prcsteel.platform.order.model.dto.PoolOutModifier;

import org.apache.ibatis.annotations.Param;

import com.prcsteel.platform.order.model.dto.InvoiceApplicationDto;
import com.prcsteel.platform.order.model.model.PoolOut;

public interface PoolOutDao {
    int deleteByPrimaryKey(Long id);

    int insert(PoolOut record);

    int insertSelective(PoolOut record);

    PoolOut selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PoolOut record);

    int updateByPrimaryKey(PoolOut record);
    /**
     * 按公司查询
     *
     * @param paramMap  查询参数
     * @return
     */
    List<PoolOut> queryByBuyer(Map<String, Object> paramMap);

    /**
     * 按服务中心查询总数
     *
     * @param orgId  查询参数
     * @return
     */
    HashMap<String,Object> queryTotalByOrg(Long orgId);
    
    /**
     * 获取待开票列表
     * @param orgId　服务中心编号
     * @param ownerIds　用户编号集　
     * @return
     */
    public List<InvoiceApplicationDto> queryInvoiceApplicationDtoByOrgId(@Param("orgId") Long orgId);

    /**
     * 获取开票详情信息列表
     * @param applyId　服务中心编号
     * @return
     */
    public List<InvoiceApplicationDto> queryInvoiceApplicationDtoByApplyId(@Param("applyId") Long applyId);

    /**
     * 根据卖家与交易员查询记录
     * @param buyerId　买家Id
     * @return
     */
    PoolOut queryByBuyerId( @Param("buyerId") Long buyerId);

    int modifyPoolOut(PoolInAndOutModifier modifier);
    
    /**
     * 根据交易员id和买家id查
     * @param ownerId
     * @param buyerDepartmentIds 部门ids
     * @return
     */
    List<PoolOut> selectOwnerIdAndBuyerId (@Param("ownerIds") List<Long> ownerIds, @Param("departmentIds") List<Long> departmentIds);
    
    int updatePoolOut (PoolOutModifier poolOutModifier);

    /**
     * 根据部门id查询未开票完成的记录
     * @param deptId
     * @return
     */
    List<PoolOut> queryInvoicingByDeptId(Long deptId);

}