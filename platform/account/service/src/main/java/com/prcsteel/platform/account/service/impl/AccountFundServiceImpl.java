package com.prcsteel.platform.account.service.impl;

import com.prcsteel.platform.account.model.dto.AccountCreditDto;
import com.prcsteel.platform.account.model.enums.AccountTransApplyType;
import com.prcsteel.platform.account.model.model.AccountTransLog;
import com.prcsteel.platform.account.model.query.UpdatePaymentQuery;
import com.prcsteel.platform.account.persist.dao.AccountDao;
import com.prcsteel.platform.account.persist.dao.AccountTransLogDao;
import com.prcsteel.platform.account.service.AccountFundService;
import com.prcsteel.platform.account.service.CustGroupingInforService;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.order.model.enums.AssociationType;
import com.prcsteel.platform.order.model.enums.PayType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Rabbit
 * @version V1.0
 * @Title: AccountFundServiceImpl.java
 * @Package com.prcsteel.platform.account.service.impl
 * @Description: 客户账户服务实现
 * @date 2016-3-10 14:54:53
 */
@Service("accountFundService")
public class AccountFundServiceImpl implements AccountFundService{
    @Resource
    AccountDao accountDao;
    @Resource
    AccountTransLogDao accountTransLogDao;
    @Resource
    CustGroupingInforService custGroupingInforService;

    /**
     * 客户账户余额变动接口（credit和creditLimit不能同时为0）
     * @param accountId               客户id
     * @param associationType         关联类型
     * @param code                    关联单号(银行流水号或者交易单号)
     * @param accountTransApplyType   类型(见枚举)
     * @param balance                 资金账户发生额
     * @param balanceFreeze           资金冻结账户发生额
     * @param secondSettlement        二次结算账户发生额
     * @param secondSettlementFreeze  二次结算冻结账户发生额
     * @param credit                  已使用信用额度发生额
     * @param creditLimit             信用额度总额发生额
     * @param payType                 支付类型(见枚举)
     * @param operatorId              操作者id
     * @param operatorName            操作者姓名
     * @param operateDate             操作时间(操作时间或者银行流水时间)
     */
    @Override
    @Transactional
    public void updateAccountFund(Long accountId, AssociationType associationType, String code, AccountTransApplyType accountTransApplyType,
                                  BigDecimal balance, BigDecimal balanceFreeze, BigDecimal secondSettlement, BigDecimal secondSettlementFreeze,
                                  BigDecimal credit, BigDecimal creditLimit, PayType payType, Long operatorId, String operatorName, Date operateDate) {
        AccountCreditDto account = accountDao.selectActualCreditBalanceById(accountId);
        if(account == null){
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "该客户不存在");
        }
        if(credit.compareTo(BigDecimal.ZERO) != 0 && creditLimit.compareTo(BigDecimal.ZERO) != 0){
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "不能同时修改信用额度总额和已使用信用额度");
        }

        BigDecimal accountBalance = account.getBalance().setScale(2, BigDecimal.ROUND_HALF_UP); //chengui 账户余额（余额小数点后第三位不为0时，四舍五入保留两位小数）
        BigDecimal newBalance = accountBalance.add(balance);  //现金余额
        BigDecimal newBalanceFreeze = account.getBalanceFreeze().add(balanceFreeze);  //现金冻结余额
        BigDecimal newSecondSettlement = account.getBalanceSecondSettlement().add(secondSettlement);  //二次结算余额
        BigDecimal newSecondSettlementFreeze = account.getBalanceSecondSettlementFreeze().add(secondSettlementFreeze);  //二次结算冻结余额
        BigDecimal newCreditUsed = account.getCreditAmountUsed().add(credit);   //部门已使用信用额度
        BigDecimal depCreditLimit = account.getCreditAmount().add(creditLimit);   //部门信用额度总额
        BigDecimal groupCreditLimit = account.getGroupCreditBalance();    //信用额度组额度总额剩余
        BigDecimal actualCreditLimit = depCreditLimit;
        if(groupCreditLimit != null){
            actualCreditLimit = BigDecimal.valueOf(Math.min(depCreditLimit.doubleValue(), groupCreditLimit.doubleValue())); //信用额度取部门额度和组额度的最小值
        }
        BigDecimal newCreditBalance = actualCreditLimit.subtract(newCreditUsed);  //信用额度余额=额度-已使用
        if(newBalance.compareTo(BigDecimal.ZERO) <0){
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "资金账户余额不足");
        }
        if(newBalanceFreeze.compareTo(BigDecimal.ZERO) < 0){
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "资金冻结账户余额不足");
        }
        /**if(newCreditUsed.compareTo(BigDecimal.ZERO) < 0){
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "已使用信用额度不能为负数");
        }
        if(actualCreditLimit.compareTo(BigDecimal.ZERO) < 0){
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "信用额度不能为负数");
        }*/
        if(AccountTransApplyType.CREDITLIMIT_TRANSTO_ACCOUNT.equals(accountTransApplyType)
                && newCreditBalance.compareTo(BigDecimal.ZERO) < 0){  //如果类型是信用额度转入资金账户，需要判断剩余额度大于0
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "信用额度余额不足");
        }
        //信用额度余额有可能出现负值，先不判断>0
        //更新客户账户
        UpdatePaymentQuery query = new UpdatePaymentQuery(accountId, newBalance, newBalanceFreeze, newSecondSettlement,
                newSecondSettlementFreeze, newCreditUsed, depCreditLimit, operateDate, operatorName,
                account.getBalance(), account.getBalanceFreeze(), account.getBalanceSecondSettlement(),
                account.getBalanceSecondSettlementFreeze(), account.getCreditAmountUsed(), account.getCreditAmount());   //传入原值做并发处理
        if (accountDao.updatePaymentV2(query) <= 0) {
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "更新客户账户失败");
        }
        //添加流水
        //credit和creditLimit只有一个可能为0
        AccountTransLog accountTransLog = new AccountTransLog(accountId, operatorId, operatorName, operateDate, associationType.getCode(), code,
                accountTransApplyType.getCode(), balance, newBalance, secondSettlement, newSecondSettlement,
                ((credit.compareTo(BigDecimal.ZERO) != 0) ? credit.negate() : creditLimit), newCreditBalance, payType.getCode());
        if (accountTransLogDao.insertSelective(accountTransLog) <= 0) {
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "添加流水失败");
        }
    }

    /**
     * 信用额度还款
     * @param accountId               客户id
     * @param associationType         关联类型(涉及订单的需要给这个参数)
     * @param code                    订单号(涉及订单的需要给这个参数)
     * @param operatorId              操作者id
     * @param operatorName            操作者姓名
     * @param operateDate             操作时间(操作时间或者银行流水时间)
     */
    @Override
    @Transactional
    public void payForCredit(Long accountId, AssociationType associationType, String code, Long operatorId, String operatorName, Date operateDate){
        AccountCreditDto account = accountDao.selectActualCreditBalanceById(accountId);
        if(account == null){
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "该客户不存在");
        }
        BigDecimal creditUsed = account.getCreditAmountUsed();   //已使用信用额度
        BigDecimal balance = account.getBalance();   //资金账户余额
        BigDecimal secondSettlement = account.getBalanceSecondSettlement();   //二次结算余额
        BigDecimal creditLimit = account.getCreditAmount();   //部门信用额度总额
        BigDecimal groupCreditLimit = account.getGroupCreditBalance();    //信用额度组额度总额剩余
        if(groupCreditLimit != null){
            creditLimit = BigDecimal.valueOf(Math.min(creditLimit.doubleValue(), groupCreditLimit.doubleValue())); //信用额度取部门额度和组额度的最小值
        }
        BigDecimal creditBalance = creditLimit.subtract(creditUsed);    //信用额度余额
        if(associationType == null){
            associationType = AssociationType.CREDIT_SERIAL;
            code = custGroupingInforService.createCode();
        }
        //资金账户金额自动还款“已使用额度”
        if(balance.compareTo(BigDecimal.ZERO) > 0 && creditUsed.compareTo(BigDecimal.ZERO) > 0){
            BigDecimal restitutionAmount = creditUsed.compareTo(balance) >= 0 ? balance : creditUsed;  //归还的金额
            creditUsed = creditUsed.subtract(restitutionAmount);   //归还后的已使用额度
            creditBalance = creditBalance.add(restitutionAmount);  //信用额度余额上升
            updateAccountFund(accountId, associationType, code, AccountTransApplyType.CREDITLIMI_TREPAYMENT,
                    restitutionAmount.negate(), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, restitutionAmount.negate(),
                    BigDecimal.ZERO, PayType.BALANCE, operatorId, operatorName, operateDate);   //资金账户还款
        }
        //如果设置二结自动还款
        if (Integer.valueOf(Constant.YES).equals(account.getIsAutoSecondPayment())) {
            //1)二结余额为正时，“已使用额度”不为0，二结余额自动还款“已使用额度”；
            if (secondSettlement.compareTo(BigDecimal.ZERO) > 0 && creditUsed.compareTo(BigDecimal.ZERO) > 0) {   //还款
                BigDecimal restitutionAmount = creditUsed.compareTo(secondSettlement) >= 0 ? secondSettlement : creditUsed;  //归还的金额
                updateAccountFund(accountId, associationType, code, AccountTransApplyType.INTO_THE_CAPITAL_ACCOUNT,
                        restitutionAmount, BigDecimal.ZERO, restitutionAmount.negate(), BigDecimal.ZERO, BigDecimal.ZERO,
                        BigDecimal.ZERO, PayType.BALANCE, operatorId, operatorName, operateDate);   //二结转入资金账户
                updateAccountFund(accountId, associationType, code, AccountTransApplyType.CREDITLIMI_TREPAYMENT,
                        restitutionAmount.negate(), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, restitutionAmount.negate(),
                        BigDecimal.ZERO, PayType.BALANCE, operatorId, operatorName, operateDate);   //资金账户抵扣
            }
            //2)二结为负，“信用可用额度”大于0时，“信用可用额度”自动抵扣二结欠款
            if (secondSettlement.compareTo(BigDecimal.ZERO) < 0 && creditBalance.compareTo(BigDecimal.ZERO) > 0) {   //抵扣
                BigDecimal deductionAmount = creditBalance.compareTo(secondSettlement.abs()) >= 0 ? secondSettlement.abs() : creditBalance;  //抵扣的金额
                updateAccountFund(accountId, associationType, code, AccountTransApplyType.CREDITLIMIT_TRANSTO_ACCOUNT,
                        deductionAmount, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, deductionAmount,
                        BigDecimal.ZERO, PayType.BALANCE, operatorId, operatorName, operateDate);   //信用额度转入资金账户
                updateAccountFund(accountId, associationType, code, AccountTransApplyType.SECONDARY_SETTLEMENT_ACCOUNT_BALANCES,
                        deductionAmount.negate(), BigDecimal.ZERO, deductionAmount, BigDecimal.ZERO, BigDecimal.ZERO,
                        BigDecimal.ZERO, PayType.BALANCE, operatorId, operatorName, operateDate);   //资金账户抵扣二次结算账户
            }
        }
    }
}
