package com.prcsteel.platform.account.persist.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.prcsteel.platform.account.model.dto.AccountAllDto;
import com.prcsteel.platform.account.model.dto.AccountCreditDto;
import com.prcsteel.platform.account.model.dto.AccountDto;
import com.prcsteel.platform.account.model.dto.AccountDtoForUpdate;
import com.prcsteel.platform.account.model.dto.AccountForAppDto;
import com.prcsteel.platform.account.model.dto.CompanyDto;
import com.prcsteel.platform.account.model.dto.ContactWithPotentialCustomerDto;
import com.prcsteel.platform.account.model.dto.CustAccountDto;
import com.prcsteel.platform.account.model.dto.CustomerTransferDto;
import com.prcsteel.platform.account.model.dto.DepartmentDto;
import com.prcsteel.platform.account.model.dto.SellerAccountDto;
import com.prcsteel.platform.account.model.dto.TraderInfoDto;
import com.prcsteel.platform.account.model.model.Account;
import com.prcsteel.platform.account.model.query.CompanyQuery;
import com.prcsteel.platform.account.model.query.ContactQuery;
import com.prcsteel.platform.account.model.query.ContactWithPotentialCustomerQuery;
import com.prcsteel.platform.account.model.query.CustomerTransferQuery;
import com.prcsteel.platform.account.model.query.UpdatePaymentQuery;
import com.prcsteel.platform.common.dto.BuyerOrderDetailDto;

public interface AccountDao {
	String getAccountCode();

	int deleteByPrimaryKey(Long id);

	int insert(Account record);

	int insertSelective(Account record);

	Account selectByPrimaryKey(Long id);

	Account selectByCode(String code);

	Account selectByTel(String tel);

	int updateByPrimaryKeySelective(Account record);

	int updateByPrimaryKeySelectiveExcludeBankInfo(Account record);

	int updateByPrimaryKey(Account record);

	int updateStatusByPrimaryKey(@Param("id") Long id,
			@Param("status") Integer status, @Param("loginId") String loginId);

	int updateInvoiceDataStatusByPrimaryKey(@Param("id") Long id,
			@Param("status") String status,@Param("reason") String reason, @Param("loginId") String loginId);

	/**
	 * 卖家客户分组查询
	 *
	 * @param paramMap
	 * @return
	 */
	List<SellerAccountDto> listSellerSearch(Map<String, Object> paramMap);

	/**
	 * 卖家客户分组查询总数
	 *
	 * @param paramMap
	 * @return
	 */
	int totalSellerSearch(Map<String, Object> paramMap);

	int totalAccount(Map<String, Object> paramMap);

	List<Map<String, Object>> selectByAccountNameAndContactName(
			Map<String, Object> paramMap);

	/**
	 * 根据公司名称模糊查询
	 *
	 * @param paramMap
	 * @return
	 */
	List<Account> listAccountByName(Map<String, Object> paramMap);

	/**
	 * 根据公司名称模糊查询总数
	 *
	 * @param paramMap
	 * @return
	 */
	int totalListAccountByName(Map<String, Object> paramMap);

	/**
	 * 根据公司名称查询
	 *
	 * @param name
	 * @return
	 */
	Account selectAccountByName(String name);

	/**
	 * 更新现金余额
	 * 
	 * @param paramMap
	 * @return
	 */
	int updateBalanceById(Map<String, Object> paramMap);

	/**
	 * 支付
	 * 
	 * @param id
	 *            客户id
	 * @param balance
	 *            现金余额
	 * @param balanceFreeze
	 *            冻结余额
	 * @param secondSettlement
	 *            二次结算记账余额
	 * @param secondSettlementFreeze
	 *            二次结算冻结余额
	 * @param lastUpdated
	 *            最后修改时间
	 * @param lastUpdatedBy
	 *            最后修改人
	 * @return
	 */
	int updatePayment(@Param("id") Long id,
			@Param("balance") BigDecimal balance,
			@Param("balanceFreeze") BigDecimal balanceFreeze,
			@Param("secondSettlement") BigDecimal secondSettlement,
			@Param("secondSettlementFreeze") BigDecimal secondSettlementFreeze,
			@Param("balanceRebate") BigDecimal balanceRebate,
			@Param("lastUpdated") Date lastUpdated,
			@Param("lastUpdatedBy") String lastUpdatedBy);

	/**
	 * 支付（需要传入原值，做并发处理）
	 * @param query
	 * @return
	 */
	int updatePaymentV2(UpdatePaymentQuery query);

	List<Account> selectUncheckedBuyerList(@Param("orgId") Long orgId,
			@Param("name") String name);

	/**
	 * 根据account id列表查询对应的交易员信息
	 * 
	 * @param ids
	 * @return
	 */
	List<TraderInfoDto> selectManagerInfoByIds(List<Long> ids);

	/**
	 * 根据客户id 查询客户及归属的交易员的相关信息
	 * 
	 * @param accountId
	 *            客户id
	 * @return
	 */
	BuyerOrderDetailDto queryAccountAndManagerInfoByAccountId(
			@Param("accountId") Long accountId);

	/**
	 * 统计已锁定的客户记录数
	 * 
	 * @param idList
	 *            待统计的客户id
	 * @return 返回记录数
	 */
	int countLockedAccount(List<Long> idList);

	/**
	 * 通过客户ID查询客户二次结算欠款金额
	 * 
	 * @param accountIds
	 * @return
	 */
	List<AccountDto> queryForBalance(@Param("accountIds") List<Long> accountIds);

	/**
	 * 更新客户交易次数
	 * 
	 * @param account
	 * @return
	 */
	int updateDealTotal(Account account);

	/**
	 * 查客户二次结算余额
	 * 
	 * @param accountId
	 * @return
	 */
	public BigDecimal selectSecondBalance(Long accountId);

	List<Account> queryAccountsByIds(@Param("accountIds") List<Long> accountIds);
	/**
	 * 客户银行账号审核列表数据
	 * @param name
	 * @param beginTime
	 * @param endTime
	 * @param orderBy
	 * @param order
	 * @param start
	 * @param length
	 * @return
	 */
	public List<Account> selectBankCodeVerifyList(@Param("name") String name,
											@Param("beginTime") String beginTime,
											@Param("endTime") String endTime,
											@Param("orderBy") String orderBy,
											@Param("order") String order,
											@Param("start") Integer start,
											@Param("length") Integer length);

	/**
	 * 客户银行账号审核列表总数
	 * @param name
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public int selectBankCodeVerifyTotal(@Param("name") String name,
										 @Param("beginTime") String beginTime,
										 @Param("endTime") String endTime);


	/**
	 * 客户银行账号审核更新状态和理由
	 * @param id
	 * @param bankDataStatus
	 * @param bankDataReason
	 * @param lastUpdatedBy
	 * @return
	 */
	public int updateBankDataStatusByPrimaryKey(@Param("id") Long id,
									     		@Param("bankDataStatus") String bankDataStatus,
										 		@Param("bankDataReason") String bankDataReason,
												@Param("bankDataReminded") String bankDataReminded,
										 		@Param("lastUpdatedBy") String lastUpdatedBy);

	/**
	 * 查询当前所有资金账户余额
	 * @return
	 */
	BigDecimal queryBalance();


	/**
	 * 获取所有卖家数据
	 * @return
	 */
	public List<AccountAllDto> selectAllSellerAccount();

	/**
	 * 获取所有客户数据
	 * @return
	 */
	List<CustomerTransferDto> querCustomerTransferList(CustomerTransferQuery query);

	/**
	 * 获取所有客户数据的总量
	 * @return
	 */
	Integer querCustomerTransferCount(CustomerTransferQuery query);


	/**
	 * 查询公司的所有部门
	 * @param companyId
	 * @return
	 */
	List<DepartmentDto> queryDeptByCompanyId(Long companyId);

	int updateIsDeletedById(@Param("id")Long id,@Param("userName")String userName);

	BigDecimal queryCreditAmountTotalByParentId(@Param("parentId")Long parentId ,@Param("deptId")Long deptId);

	int queryDeptCountByNameAndParentId(ContactQuery query);

	int batchUpateStatusByPrimaryKeys(AccountDtoForUpdate param);

	/**
	 * 根据条件查询公司列表
	 * @param query
	 * @return
	 */
    List<CompanyDto> queryCompanyByCompanyQuery(CompanyQuery query);

	/**
	 * 根据条件查询公司列表总数
	 * @param query
	 * @return
	 */
	int queryCompanyTotalByCompanyQuery(CompanyQuery query);
	
	/**
	 * 根据公司Id查询公司信息及公司下面的部门信息
	 * @param accountId
	 * @return
	 */
	List<Account> selectAccountInfoById(Long accountId);

	/**
	 * 根据交易员ID查询相关联的公司列表
	 * @param query
	 * @return
	 */
	List<Account> queryAccountByManager(Account query);
	/**
	 *
	 *根据客户名称查询 有几个部门
	 * @param  accountId  客户名称
	 * @return
	 */
	int queryDepartmentByName(Long accountId);

	/**
	 *
	 *根据客户名称查询 客户仅有的一个部门
	 * @param id  客户id
	 * @return
	 */
	Long queryDepartment(Long id);
	/**
	 * 产线当前登陆客户是否是管理员身份 
	 * auth： zhocai@prcsteel.com
	 * date :2016-3-16
	 * @param loginId
	 * @return integer
	 */
	public int queryIsAdminCount(String loginId);
	
	/**
	 * 二结应付客户详情
	 * @param id
	 * @return
	 */
	CustAccountDto queryForAccountId(Long id);

	/**
	 * 查询组的信用额度使用总额
	 * @param groupingId 组ID
	 * @return
	 */
	BigDecimal queryCreditAmountUsedTotalByGroupingId(Long groupingId);

	/**
	 * 获取包含实际信用额度余额的账户信息
	 * @param id
	 * @return
     */
	AccountCreditDto selectActualCreditBalanceById(Long id);

	/**
	 * 获取客户服务中心ID列表
	 * @param accountId
	 * @return
	 */
	List<Long> queryOrgIdsByAccountId(Long accountId);

	/**
	 * 获取客户信息（联系人任取其一）
	 * @param query
	 * @return
     */
	List<AccountForAppDto> queryAccountWithSingleContactByManager(CompanyQuery query);

	/**
	 * 获取部门的客户信息
	 * @param deptId
	 * @return
	 */
	Account queryParentByDeptId(@Param("deptId")Long deptId);

	List<Account> listDeparmentAccount(Map<String, Object> paramMap);
}

