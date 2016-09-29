package com.prcsteel.platform.order.web.job;

import com.prcsteel.platform.account.model.dto.DepartmentDto;
import com.prcsteel.platform.account.model.enums.AccountBankDataStatus;
import com.prcsteel.platform.account.model.enums.AccountTransApplyType;
import com.prcsteel.platform.account.model.model.AccountBank;
import com.prcsteel.platform.account.service.AccountFundService;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.account.model.dto.AccountDto;
import com.prcsteel.platform.order.model.dto.LocalTransactionDataJobDto;
import com.prcsteel.platform.order.model.enums.AssociationType;
import com.prcsteel.platform.order.model.enums.PayType;
import com.prcsteel.platform.order.model.enums.TransactionType;
import com.prcsteel.platform.order.model.enums.Type;
import com.prcsteel.platform.account.model.model.Account;
import com.prcsteel.platform.order.model.model.BankTransactionInfo;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.account.service.AccountBankService;
import com.prcsteel.platform.account.service.AccountService;
import com.prcsteel.platform.order.service.QuartzJobService;
import com.prcsteel.platform.order.service.order.BankTransactionInfoService;
import com.prcsteel.platform.order.service.order.OrderStatusService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by Rabbit Mao on 15-8-20.
 */
@Component
public class LocalTransactionDataJob extends CbmsJob {

    private static final Logger logger = Logger.getLogger(LocalTransactionDataJob.class);

    @Value("${quartz.job.spdb.systemId}")
    private String systemId;
    @Value("${quartz.job.spdb.systemCode}")
    private String systemCode;

    @Resource
    BankTransactionInfoService bankTransactionInfoService;
    @Resource
    OrderStatusService orderStatusService;
    @Resource
    AccountService accountService;
    @Resource
    AccountBankService accountBankService;
    @Resource
    AccountFundService accountFundService;

    private String jobName = "LocalTransactionData";
    @Resource
    private QuartzJobService quartzJobService;

    @Override
    @Transactional
    public void execute() {
        if (isEnabled()) {
            logger.info("local transaction data job start");
            try {
                //未处理但公司已存在
                List<LocalTransactionDataJobDto> dtos = bankTransactionInfoService.queryUnprocessedForJob();
                for (LocalTransactionDataJobDto dto : dtos) {
                    // 公司账户自动充值
                    accountFundService.updateAccountFund(dto.getAccountId(), AssociationType.BANK_OF_SERIAL_NUMBER, dto.getSerialNumber(),
                            AccountTransApplyType.CHARGE, dto.getAmount(), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                            BigDecimal.ZERO, PayType.BALANCE, Long.valueOf(systemId), Constant.SYSTEMNAME, new Date());
                    // 如果只有一个部门，默认充值到该部门
                    List<DepartmentDto> departmentList = accountService.queryDeptByCompanyId(dto.getAccountId());
                    if (departmentList != null && departmentList.size() == 1) {
                        DepartmentDto departmentDto = departmentList.get(0);
                        //公司账户转出
                        accountFundService.updateAccountFund(dto.getAccountId(), AssociationType.MONEYALLOCATION, dto.getSerialNumber(),
                                AccountTransApplyType.COMPANYMONEY_TRANSTO_DEPART, BigDecimal.ZERO.subtract(dto.getAmount()),
                                BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, PayType.BALANCE, Long.valueOf(systemId), Constant.SYSTEMNAME, new Date());

                        //部门账户充值
                        accountFundService.updateAccountFund(departmentDto.getId(), AssociationType.BANK_OF_SERIAL_NUMBER, dto.getSerialNumber(),
                                AccountTransApplyType.CHARGE, dto.getAmount(), BigDecimal.ZERO, BigDecimal.ZERO,
                                BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, PayType.BALANCE, Long.valueOf(systemId), Constant.SYSTEMNAME, new Date());
                    }

                    // 更新到账异常状态
                    BankTransactionInfo bankTransactionInfo = bankTransactionInfoService.queryById(dto.getBankTransactionId());
                    bankTransactionInfo.setStatus(TransactionType.CHARGE.toString());
                    if (bankTransactionInfoService.updateStatusById(bankTransactionInfo, Constant.SYSTEMNAME) <= 0) {
                        logger.error("Quartz LocalTransactionDataJob Exception(更新到账异常表失败).");
                    }
                    // 修改银行信息，如果不存在则添加，触发审核流程
                    User user = new User();
                    user.setId(0L);
                    user.setLoginId(systemCode);
                    user.setName(Constant.SYSTEMNAME);
                    AccountBank accountBank = null;
                    String bankAccountNumber=bankTransactionInfo.getPayeeAcctountNumber();
                    if(StringUtils.isNotEmpty(bankAccountNumber)) {
                        // 根据银行账户查询已经存在
                        accountBank = accountBankService.selectByAccountCode(bankAccountNumber);
                        if (accountBank == null) {
                            accountBank = new AccountBank();
                            accountBank.setAccountId(dto.getAccountId());
                            accountBank.setBankCode(bankTransactionInfo.getBankCode());  //开户银行行号
                            accountBank.setBankName(bankTransactionInfo.getPayeeBankName());  //开户银行主行
                            accountBank.setBankAccountCode(bankTransactionInfo.getPayeeAcctountNumber());  //银行账号
                            accountBank.setUrl("无");
                            accountBank.setRowId(Constant.FROMBANKTRANSACTIONJOB);
                            accountBankService.saveBankInfo(null, accountBank, user);
                        }
                        else{
                            // 如果存在并是同一公司，银行信息状态为 非审核通过 ，修改银行账户，触发审核流程
                            if(dto.getAccountId().equals(accountBank.getAccountId())
                               && !AccountBankDataStatus.Approved.getCode().equals(accountBank.getBankDataStatus())){
                                accountBank.setBankCode(bankTransactionInfo.getBankCode());  //开户银行行号
                                accountBank.setBankName(bankTransactionInfo.getPayeeBankName());  //开户银行主行
                                accountBank.setBankAccountCode(bankTransactionInfo.getPayeeAcctountNumber());  //银行账号
                                accountBank.setRowId(Constant.FROMBANKTRANSACTIONJOB);
                                accountBankService.saveBankInfo(null, accountBank, user);
                            }
                        }
                    }
                }
            } catch (BusinessException be) {
                logger.error(be.getMessage());
            }

            //Job执行（完成）状态更新。
            quartzJobService.finish(jobName);

            logger.info("local transaction data job end");
        }
    }

    @Override
    public boolean isEnabled() {
        return quartzJobService.starting(jobName, "");
    }
}
