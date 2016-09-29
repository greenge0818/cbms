package com.prcsteel.platform.account.service;

import com.prcsteel.platform.account.model.dto.AccountAllDto;
import com.prcsteel.platform.account.model.dto.AccountCreditDto;
import com.prcsteel.platform.account.model.dto.AccountDto;
import com.prcsteel.platform.account.model.dto.AccountDtoForUpdate;
import com.prcsteel.platform.account.model.dto.AccountForAppDto;
import com.prcsteel.platform.account.model.dto.CompanyDto;
import com.prcsteel.platform.account.model.dto.CustAccountDto;
import com.prcsteel.platform.account.model.dto.DepartmentDto;
import com.prcsteel.platform.account.model.dto.SaveAccountDto;
import com.prcsteel.platform.account.model.dto.SellerAccountDto;
import com.prcsteel.platform.account.model.model.Account;
import com.prcsteel.platform.account.model.model.AccountAttachment;
import com.prcsteel.platform.account.model.model.AccountContact;
import com.prcsteel.platform.account.model.model.AccountContractTemplate;
import com.prcsteel.platform.account.model.model.AccountExt;
import com.prcsteel.platform.account.model.query.CompanyQuery;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.order.model.query.WXContractQuery;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface AccountService {

    List<Account> list();
    int deleteByPrimaryKey(Long id);
    //新增客户
    HashMap<String,Object> insert(AccountDto record, List<MultipartFile> attachments, AccountContact contact) throws BusinessException;
    //int insertSelective(Account record);
    AccountDto	selectByCode(String code);
    AccountDto selectByPrimaryKey(Long id);
    //更新客户
    HashMap<String, Object> updateByPrimaryKeySelective(AccountDto record,List<MultipartFile> attachments, AccountContact contact) throws BusinessException;
    int updateByPrimaryKey(Account record);
    Account selectByTel(String tel);

    //只修改客户类型
    public int updateType(Long id, String type);

    //保存合同模板
    int saveContractTemplate(AccountContractTemplate act, MultipartFile thumbnailFile);
    List<AccountContractTemplate> selectCTByModel(AccountContractTemplate act);
    boolean updateStatusByPrimaryKey(Long id, Integer status, String loginId);
    boolean updateInvoiceDataStatusByPrimaryKey(Long id, String status, String reason,String loginId);
    /**
     * 卖家客户分组查询
     *
     * @param paramMap
     * @return
     */
    List<SellerAccountDto> listSellerSearch(Map<String, Object> paramMap,List<Long> userIds);

    /**
     * 卖家客户分组查询总数
     *
     * @param paramMap
     * @return
     */
    int totalSellerSearch(Map<String, Object> paramMap);

    List<Map<String,Object>> selectByAccountNameAndContactName(Map<String, Object> paramMap);

    int totalAccount(Map<String, Object> paramMap);

    /**
     * 根据公司名称模糊查询
     *
     * @param paramMap
     * @return
     */
    List<Account> listAccountByName(Map<String, Object> paramMap);
    
    /**
     * 根据公司名称模糊查询
     *
     * @param paramMap
     * @return
     */
    List<Account> listDeparmentAccount(Map<String, Object> paramMap);

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

    Account queryById(Long id);

    /**
     * 添加账户
     * @param
     * @return
     */
    boolean registerCompany(String companyName,String contact,String mobile,User operator);

    String getCode();

    List<Account> selectUncheckedBuyerList(Long orgId,String name);

    /**
     * 通过客户ID查询客户二次结算金额
     * @param accountIds
     * @return
     */
    public List<AccountDto> queryForBalance(List<Long> accountIds);

    /**
     * 查客户二次结算余额
     *
     * @param accountId
     * @return
     */
    public BigDecimal selectSecondBalance(Long accountId);

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
    public List<Account> selectBankCodeVerifyList(String name, String beginTime, String endTime, String orderBy, String order, Integer start, Integer length);

    /**
     * 客户银行账号审核列表总数
     * @param name
     * @param beginTime
     * @param endTime
     * @return
     */
    public int selectBankCodeVerifyTotal(String name, String beginTime, String endTime);

    /**
     * 客户银行账号审核更新状态和理由
     * @param id
     * @param bankDataStatus
     * @param bankDataReason
     * @param lastUpdatedBy
     * @return
     */
    public int updateBankDataStatusByPrimaryKey(Long id, String bankDataStatus, String bankDataReason, String bankDataReminded, String lastUpdatedBy);

    /**
     * 获取所有卖家数据
     * @return
     */
    public List<AccountAllDto> selectAllSellerAccount();

    void fossilizedImage(User user ,List<AccountAttachment> list);

    /**
     * 添加账号
     * @param saveInfo 账号信息
     */
    SaveAccountDto save(SaveAccountDto saveInfo);

    /**
     * 上传用户附件公用方法
     *
     * @param file 文件
     * @return
     */
    String uploadFile( MultipartFile file);

    /**
     * 保存修改
     * @param saveInfo 账号信息
     */
    void saveEdit(SaveAccountDto saveInfo);

    /**
     * 保存证件资料
     * @param saveInfo 账号信息
     */
    void saveCredentials(SaveAccountDto saveInfo);

    /**
     * 批量修改状态
     * @param param
     */
    void batchUpateStatusByPrimaryKeys(AccountDtoForUpdate param);

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
     * 保存资金分配或撤回
     * @param accountList
     * @param user
     */
    List<Account> saveFundAllocations(String accountList,User user,Long accountId);

    /**
     * 查询公司的所有部门
     * @param companyId
     * @return
     */
    List<DepartmentDto> queryDeptByCompanyId(Long companyId);

    /**
     * 查询操作员有权限的 归属服务中心的部门
     * @param companyId
     * @return
     */
    List<DepartmentDto> queryDeptByCompanyIdAndBelongOrg(Long companyId, Long operatorOrgId);


    /**
     * 根据交易员ID查询相关联的公司列表
     * @param query
     * @return
     */
    List<Account> queryAccountByManager(Account query);
    /**
     *
     *根据客户名称查询 有几个部门
     * @param accountId  客户名称
     * @return
     */
    int queryDepartmentByName(Long accountId);

    /**
     *
     *根据客户名称查询 客户仅有的一个部门
     * @param id  客户名称id
     * @return
     */
    Long queryDepartment(Long id);

    /**
     * 根据账号Id修改
     *
     * @param accountExt 修改对象
     * @param user       操作用户
     */
    Integer updateByAccountIdSelective(AccountExt accountExt, User user);

    /**
     * 绑定客户，将微信与本系统客户相互绑定
     * 也就是通过手机查询本系统，将open_id存入到与之对应的记录中。
     *
     * @param query：
     * 		token:
     * 		openid: 要绑定的号码
     * 		phone： 用来在本系系统查找联系人是不是存在
     * @throws BusinessException 如果抛出异常刚说明绑定失败，e.getMessage()可得到保存失败原因
     * @author tuxianming
     * @date 2016-03-04
     */
    public void wxBindContract(WXContractQuery query) throws BusinessException;

    /**
     * 解除绑定的客户，也就是绑定客户的逆向操作
     * @param query
     * 		token:
     * 		openid: 要绑定的号码
     * 		phone： 用来在本系系统查找联系人是不是存在
     * @throws BusinessException 如果抛出异常刚说明解绑失败，e.getMessage()可得到保存失败原因
     * @author tuxianming
     * @date 2016-03-04
     */
    public void wxUnbindContract(WXContractQuery query) throws BusinessException;

    /**
     * 二结应付客户详情
     * @param id
     * @return
     */
    CustAccountDto queryForAccountId(Long id);

    AccountExt queryAccountExtByAccountId(Long accountId);

    /**
     * 抵扣或归还信用额度
     * @param accountId
     * @param amount
     * @param option
     * @param user
     * @return
     */
    AccountCreditDto payForCredit(Long accountId, BigDecimal amount, String option, User user);


    /**
     * 保存协议上传的图片，更新协议状态为“已上传待审核” chengui
     * @param user
     * @param saveAccount
     * @return
     */
    void saveAgreementUploadFiles(User user, SaveAccountDto saveAccount);

    /**
     * 重新计算AccountTag
     * @param souceAccountTag 原accountTag
     * @param subAccountTag 需要减掉的accountTag
     * @param addAccountTag 需要添加accountTag
     * @return
     */
    Long reCaluteAccountTag(Long souceAccountTag,Long subAccountTag,Long addAccountTag);


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
     * 按部门Id查询客户信息（若Id本身为客户Id，则返回当前客户信息）
     * @param id
     * @return Account
     * @author chengui
     */
    Account selectParentById(Long id);

    /**
     *  款道2.0会员体系会修改客户的公司全称和组织机构代码；
     *  支付体系会修改会员的账户余额。
     * @param account
     * @return
     */
    int updateAccount(Account account);

    //超市根据超市用户ID查询相关联的客户名称
    List<String> selectAccountNameByEcId(Integer ecId);

}
