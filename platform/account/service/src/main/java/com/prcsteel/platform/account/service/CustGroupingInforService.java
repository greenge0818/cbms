package com.prcsteel.platform.account.service;

import java.math.BigDecimal;
import java.util.List;

import com.prcsteel.platform.account.model.dto.CustGroupingInforDto;
import com.prcsteel.platform.account.model.dto.GroupInforAccountDto;
import com.prcsteel.platform.account.model.model.CustGroupingInfor;
import com.prcsteel.platform.account.model.model.CustGroupingInforAccount;
import com.prcsteel.platform.account.model.model.CustGroupingInforFlow;
import com.prcsteel.platform.account.model.query.CustGroupingInforQuery;
import com.prcsteel.platform.account.model.query.CustGroupingQuery;
import com.prcsteel.platform.acl.model.model.User;

/**
 * @author lixiang
 * @version V2.0
 * @Title: CustGroupingInforService
 * @Package com.prcsteel.platform.account.service
 * @Description: 客户管理-信用额度组
 * @date 2016-04-07
 */
public interface CustGroupingInforService {

    /**
     * 客户信用额度分组查询(审核列表)
     *
     * @param custGroupingInforQuery
     * @return
     * @author lixiang
     */
    List<CustGroupingInforDto> queryGroupInfoAccount(CustGroupingInforQuery custGroupingInforQuery);

    /**
     * 客户信用额度分组信息
     *
     * @param groupId
     * @return
     * @author caochao
     */
    CustGroupingInforDto queryGroupInfoById(Long groupId);

    /**
     * 客户信用额度分组列表
     * @param custGroupingInforQuery
     * @author caochao
     * @return
     */
    List<CustGroupingInforDto> queryGroupInfo(CustGroupingInforQuery custGroupingInforQuery);

    /**
     * 客户信用额度分组记录数
     *
     * @param custGroupingInforQuery
     * @return
     * @author caochao
     */
    int queryGroupInfoCount(CustGroupingInforQuery custGroupingInforQuery);

    /**
     * 修改客户待审核额度
     *
     * @param custId      客户关联表id
     * @param creditLimit 修改待审核额度
     * @author afeng
     */
    int updateCreditLimit(Long custId, BigDecimal creditLimit, User user);




    /**
     * 审核组信用额度及组下公司信用额度
     *
     * @param groupId     分组id
     * @param groupSerial 分组流水号
     * @param custSerials 客户流水号
     * @param user
     * @author afeng
     */
    void auditCreditLimit(Long groupId, String groupSerial, String custSerials, User user, String status);



    /**
     * @param CustGroupingQuery
     * @return int
     * @description :添加分组信息
     * @author zhoucai@prcsteel.com
     * @version V1.0
     */
    void addGroupAndCustLimit(CustGroupingQuery query);


    /**
     * @description 编辑分组
     * @return void
     * @author zhoucai@prcsteel.com
     * @version V1.0
     */
    void editGroupLimitInfo(CustGroupingInfor custGroupingInfor, User operator);



    /**
     * 删除组
     * @param groupId
     * @param operator
     * @return
     */
    int deleteLimitGroup(Long groupId, User operator);

    /**
     * 删除组内成员
     * @param id
     * @param remark
     * @param operator
     * @return
     */
    int deleteLimitGroupAccount(Long id, String remark, User operator);


	/**
	 * 生成流水号
	 * @return
     */
	String createCode();


    /**
     * 组添加公司成员
     * @param groupId 组名
     * @param accountId 客户ID
     * @param accountName 客户名
     * @param deptId 部门ID
     * @param deptName 部门名称
     * @param creditLimit 信用额度
     * @param operator
     * @return
     */
    int addLimitGroupAccount(Long groupId,Long accountId,String accountName,Long deptId,String deptName,BigDecimal creditLimit,Integer autoRefund,User operator);

    /**
     * 编辑组内客户
     * @param id
     * @param creditLimit
     * @param operator
     * @return
     */
    int editGroupAccount(Long id,BigDecimal creditLimit,Integer autoRefund,User operator);

    /** 
	 * E为“调整信用额度”操作，“160304”为日期，“0001”为流水号初始值，每多一次操作加1
	 * @author lixiang
	 */
    String createdCode();

    /**
     * @description 根据分组id查询分组信息
     * @return void
     * @author zhoucai@prcsteel.com
     * @version V1.0
     */

    CustGroupingInfor queryGroupingInfoById(Long groupId);

    /**
     * 获取分组已用信用额度
     * @param groupId 分组ID
     * @return
     */
    BigDecimal queryGroupUsedById(Long groupId);
    
    /**
     * 客户管理点击查询客户信用额度分组列表
     * @param custGroupingInforQuery
     * @author afeng
     * @return
     */
    List<GroupInforAccountDto> queryaccountGroupInfo(CustGroupingInforQuery custGroupingInforQuery);

    /**
     * 按客户ID查询分组记录数
     * @author chengui
     * @param accountId
     */
    int getGroupCountByAccountId(Long accountId);
}
