package com.prcsteel.platform.order.service.order.impl;

import com.prcsteel.platform.account.model.dto.SaveAccountDto;
import com.prcsteel.platform.account.model.dto.SaveContactDto;
import com.prcsteel.platform.account.model.enums.AccountBankDataStatus;
import com.prcsteel.platform.account.model.enums.AccountBusinessType;
import com.prcsteel.platform.account.model.enums.AccountTag;
import com.prcsteel.platform.account.model.enums.AccountTransApplyType;
import com.prcsteel.platform.account.model.model.*;
import com.prcsteel.platform.account.persist.dao.AccountContactDao;
import com.prcsteel.platform.account.service.*;
import com.prcsteel.platform.acl.model.enums.RoleType;
import com.prcsteel.platform.acl.model.model.Role;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.acl.persist.dao.RoleDao;
import com.prcsteel.platform.api.AccountDepartmentService;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.order.model.dto.BankTransactionInfoDto;
import com.prcsteel.platform.order.model.dto.LocalTransactionDataJobDto;
import com.prcsteel.platform.order.model.enums.AssociationType;
import com.prcsteel.platform.order.model.enums.BankType;
import com.prcsteel.platform.order.model.enums.PayType;
import com.prcsteel.platform.order.model.enums.TransactionType;
import com.prcsteel.platform.order.model.model.BankTransactionInfo;
import com.prcsteel.platform.order.persist.dao.BankTransactionInfoDao;
import com.prcsteel.platform.order.service.order.BankTransactionInfoService;
import com.prcsteel.platform.order.service.order.OrderStatusService;
import com.prcsteel.platform.order.service.payment.BankOriginalService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by dengxiyan on 2015/7/19.
 */
@Transactional
@Service("bankTransactionInfoService")
public class BankTransactionInfoServiceImpl implements BankTransactionInfoService {
    @Resource
    BankTransactionInfoDao bankTransactionInfoDao;
    @Resource
    AccountService accountService;
    @Resource
    OrderStatusService orderStatusService;
    @Resource
    AccountContactDao accountContactDao;
    @Resource
    BankOriginalService bankOriginalService;
    @Resource
    AccountDepartmentService accountDepartmentService;
    @Resource
    AccountFundService accountFundService;
    @Resource
    ContactService contactService;
    @Resource
    AccountContactService accountContactService;
    @Resource
    RoleDao roleDao;
    @Resource
    AccountBankService accountBankService;

    /**
     * 统计某种状态下的到账记录数
     *
     * @param status
     * @param settingType 系统设置类型
     * @return
     */
    public int countByStatus(String status, String settingType) {
        return bankTransactionInfoDao.countByStatus(status, settingType);
    }


    /**
     * 按条件搜索
     *
     * @param param
     * @return
     */
    @Override
    public List<BankTransactionInfoDto> query(Map<String, Object> param) {
        List<BankTransactionInfoDto> list = bankTransactionInfoDao.query(param);
        for (BankTransactionInfoDto bankTransactionInfoDto : list) {
            bankTransactionInfoDto.setBankTypeName(BankType.getName(bankTransactionInfoDto.getBankType()));
        }
        return list;
    }

    @Override
    public BankTransactionInfo queryById(Long id) {
        return bankTransactionInfoDao.selectByPrimaryKey(id);
    }

    @Override
    public int updateStatusById(BankTransactionInfo bankTransactionInfo, String curUser) {
        bankTransactionInfo.setLastUpdated(new Date());
        bankTransactionInfo.setLastUpdatedBy(curUser);
        bankTransactionInfo.setModificationNumber(bankTransactionInfo.getModificationNumber() + 1);
        return bankTransactionInfoDao.updateByPrimaryKey(bankTransactionInfo);
    }


    private void chargeUpdateStatusById(String curUser, BankTransactionInfo bankTransactionInfo) {
        if (updateStatusById(bankTransactionInfo, curUser) != 1) {
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "更新到账异常状态失败");
        }
    }

    public void charge(Long id, String name, String tel, Long departmentId, User user) {
        BankTransactionInfo bankTransactionInfo = queryById(id);
        //修改状态
        bankTransactionInfo.setOldStatus(TransactionType.UNPROCESSED.toString());
        bankTransactionInfo.setStatus(TransactionType.CHARGE.toString());
        chargeUpdateStatusById(user.getLoginId(), bankTransactionInfo);

        String accountName = bankTransactionInfo.getPayeeName().replaceAll(" ", "");
        Account account = accountService.selectAccountByName(accountName);
        // 公司不存在则先注册，添加联系人，建立与联系人的关系
        if (account == null) {
            SaveAccountDto saveInfo = createAccount(name, tel, user, bankTransactionInfo);
            departmentId = saveInfo.getDepartmentId();
            account = saveInfo.getAccount();
        }
        // 公司存在，如果联系人不存在则先添加联系人，并且建立关系
        else {
            createContact(name, tel, departmentId, user);

            // 修改银行信息，如果不存在则添加，触发审核流程
            String bankAccountNumber = bankTransactionInfo.getPayeeAcctountNumber();
            // 根据银行账户查询已经存在
            AccountBank accountBank = accountBankService.selectByAccountCode(bankAccountNumber);
            if (accountBank == null) {
                accountBank = new AccountBank();
                accountBank.setAccountId(account.getId());
                accountBank.setBankCode(bankTransactionInfo.getBankCode());  //开户银行行号
                accountBank.setBankName(bankTransactionInfo.getPayeeBankName());  //开户银行主行
                accountBank.setBankAccountCode(bankAccountNumber);  //银行账号
                accountBank.setUrl("无");
                accountBank.setRowId(Constant.FROMBANKTRANSACTIONJOB);
                accountBankService.saveBankInfo(null, accountBank, user);
            }
            else{
                // 如果存在并是同一公司，银行信息状态为 非审核通过 ，修改银行账户，触发审核流程
                if(account.getId().equals(accountBank.getAccountId())
                        && !AccountBankDataStatus.Approved.getCode().equals(accountBank.getBankDataStatus())){
                    accountBank.setBankCode(bankTransactionInfo.getBankCode());  //开户银行行号
                    accountBank.setBankName(bankTransactionInfo.getPayeeBankName());  //开户银行主行
                    accountBank.setBankAccountCode(bankTransactionInfo.getPayeeAcctountNumber());  //银行账号
                    accountBank.setRowId(Constant.FROMBANKTRANSACTIONJOB);
                    accountBankService.saveBankInfo(null, accountBank, user);
                }
            }
        }

        Long accountId = account.getId();
        //公司账户充值
        accountFundService.updateAccountFund(accountId, AssociationType.BANK_OF_SERIAL_NUMBER, bankTransactionInfo.getSerialNumber(),
                AccountTransApplyType.CHARGE, bankTransactionInfo.getTransactionAmount(), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                BigDecimal.ZERO, PayType.BALANCE, user.getId(), user.getName(), new Date());
        //公司账户转出
        accountFundService.updateAccountFund(accountId, AssociationType.MONEYALLOCATION, bankTransactionInfo.getSerialNumber(),
                AccountTransApplyType.COMPANYMONEY_TRANSTO_DEPART, BigDecimal.ZERO.subtract(bankTransactionInfo.getTransactionAmount()),
                BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, PayType.BALANCE, user.getId(), user.getName(), new Date());

        //部门账户充值
        accountFundService.updateAccountFund(departmentId, AssociationType.BANK_OF_SERIAL_NUMBER, bankTransactionInfo.getSerialNumber(),
                AccountTransApplyType.CHARGE, bankTransactionInfo.getTransactionAmount(), BigDecimal.ZERO, BigDecimal.ZERO,
                BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, PayType.BALANCE, user.getId(), user.getName(), new Date());
    }

    private SaveAccountDto createAccount(String name, String tel, User user,BankTransactionInfo bankTransactionInfo){
        SaveAccountDto saveInfo = new SaveAccountDto();
        Account account = new Account();
        account.setName(bankTransactionInfo.getPayeeName());
        account.setAccountTag(AccountTag.buyer.getCode());
        account.setOrgId(0L);
        account.setOrgName("");
        account.setBusinessType(AccountBusinessType.terminal.toString());
        saveInfo.setAccount(account);

        AccountExt accountExt = new AccountExt();
        accountExt.setName(bankTransactionInfo.getPayeeName());
        accountExt.setAccountCode(bankTransactionInfo.getPayeeAcctountNumber());
        accountExt.setBankNameMain(bankTransactionInfo.getPayeeBankName());
        accountExt.setBankCode(bankTransactionInfo.getBankCode());
        saveInfo.setAccountExt(accountExt);

        Contact contact = new Contact();
        contact.setName(name);
        contact.setTel(tel);
        saveInfo.setContact(contact);

        saveInfo.setUser(user);
        //新增客户
        try {
            saveInfo = accountService.save(saveInfo);
        } catch (BusinessException be) {
            throw be;
        }
        return saveInfo;
    }

    private void createContact(String name, String tel, Long departmentId, User user){
        Long managerId = 0L;
        Role role = roleDao.queryById(user.getRoleId());
        // 只有交易员才能建立交易员关系
        if (role.getRoleType() != null && role.getRoleType().equals(RoleType.Trader.toString())) {
            managerId = user.getId();
        }
        Contact contact = contactService.queryByTel(tel);
        if(contact == null){
            SaveContactDto contactDto = new SaveContactDto();  //新增联系人

            contact = new Contact();
            contact.setName(name);
            contact.setTel(tel);
            contactDto.setContact(contact);

            contactDto.setUser(user);
            contactDto.setManagerIdList(new ArrayList<>(Arrays.asList(managerId)));
            contactDto.setDeptId(departmentId);
            contactDto.setIsInEditMode(false);
            contactService.saveContact(contactDto);
        }
        else {
            if(!name.equals(contact.getName())) {
                throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "该手机号已经存在，请输入该手机号正确的联系人姓名！");
            }
            // 如果联系人与交易员、部门关系不存在则添加
            accountContactService.addAccountContact(departmentId, contact.getId(), managerId, user);
        }
    }

    public List<LocalTransactionDataJobDto> queryUnprocessedForJob() {
        return bankTransactionInfoDao.queryUnprocessedForJob();
    }

	@Override
	public String selectByAccountName(String payeeName) {
		return bankTransactionInfoDao.selectByAccountName(payeeName);
	}

    /**
     * 根据id更新状态
     * @param id
     * @return
     */
    @Override
    public int updateStatusById(Long id, String status, String loginId) {
        return bankTransactionInfoDao.updateStatusById(id, status, loginId);
    }

    /**
     * 疑似支付错误立即处充值
     * @param id
     * @return
     */
    @Override
    public Boolean errorRecharge(Long id, User user) {
        BankTransactionInfo bankTransactionInfo = bankTransactionInfoDao.selectByPrimaryKey(id);
        return bankOriginalService.saveAccountTransLog(accountService.selectAccountByName(bankTransactionInfo.getPayeeName()),
                bankTransactionInfo.getSerialNumber(), bankTransactionInfo.getPayeeAcctountNumber(),
                bankTransactionInfo.getBankCode(), bankTransactionInfo.getPayeeBankName(),
                bankTransactionInfo.getTransactionTime(), bankTransactionInfo.getTransactionAmount(), user);
    }
}
