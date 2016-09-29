package com.prcsteel.platform.order.service.order.impl;

import com.prcsteel.platform.account.model.dto.AccountTransLogDtoForReport;
import com.prcsteel.platform.account.model.model.Account;
import com.prcsteel.platform.account.model.model.AccountTransLog;
import com.prcsteel.platform.account.persist.dao.AccountDao;
import com.prcsteel.platform.account.persist.dao.AccountTransLogDao;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.acl.persist.dao.UserDao;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.order.model.dto.ConsignOrderStatusDto;
import com.prcsteel.platform.order.model.enums.ApplyType;
import com.prcsteel.platform.order.model.enums.ApplyTypeForReport;
import com.prcsteel.platform.order.model.enums.AssociationType;
import com.prcsteel.platform.order.model.enums.OrderStatusType;
import com.prcsteel.platform.order.model.enums.PayType;
import com.prcsteel.platform.order.model.enums.Type;
import com.prcsteel.platform.order.model.model.OrderAuditTrail;
import com.prcsteel.platform.order.persist.dao.OrderStatusDao;
import com.prcsteel.platform.order.service.order.OrderStatusService;
import com.prcsteel.platform.order.service.report.ReportAccountFinancialService;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by kongbinheng on 2015/7/18.
 */
@Service("orderStatusService")
@Transactional
public class OrderStatusServiceImpl implements OrderStatusService {

    @Autowired
    OrderStatusDao orderStatusDao;
    @Autowired
    AccountDao accountDao;
    @Autowired
    AccountTransLogDao accountTransLogDao;
    @Autowired
    UserDao userDao;

    @Autowired
    ReportAccountFinancialService reportAccountFinancialService;

    private static final Logger logger = LoggerFactory.getLogger(OrderStatusServiceImpl.class);

    @Override
    public int insertOrderAuditTrail(OrderAuditTrail orderAuditTrail) {
        return orderStatusDao.insertOrderAuditTrail(orderAuditTrail);
    }

    /**
     * 更新订单状态
     *
     * @param paramMap
     * @return
     */
    @Override
    public boolean updateOrderStatus(Map<String, Object> paramMap, OrderAuditTrail orderAuditTrail) {
        boolean flag = false;
        if (orderStatusDao.updateOrderStatus(paramMap) > 0) {
            if (orderStatusDao.insertOrderAuditTrail(orderAuditTrail) > 0) {
                flag = true;
                return flag;
            }
        }
        return flag;
    }

    /**
     * 提现/付款/关联
     *
     * @param accountId        客户id
     * @param consignOrderCode 关联单号
     * @param serialNumber     流水号
     * @param balanceWithdraw  发生余额
     * @param freeze           二次结算余额
     * @param applyerId        申请人id
     * @param applyerName      申请人姓名
     * @param applyerDate      申请时间
     * @param flag             见Type枚举
     * @return
     */
    public synchronized boolean updatePayment(Long accountId, String consignOrderCode, String serialNumber,
                                              Double balanceWithdraw, Double freeze,
                                              Long applyerId, String applyerName, Date applyerDate, String flag) {
        boolean result = false;
        boolean isSecond = true;
        String payType = "";             //结算类型
        String applyerType = "";         //类型
        String associationType = "";     //关联类型
        Double cashHappenBalance = 0d;   //现金发生余额
        Double cashCurrentBalance = 0d;  //现金当前余额
        Double amount = 0d;
        //二次结算发生金额
        Double currentBalance = 0d;      //二次结算当前余额

        if ("".equals(consignOrderCode) || consignOrderCode == null) consignOrderCode = "-";
        if ("".equals(serialNumber) || serialNumber == null) serialNumber = "-";
        String loginId = applyerId.equals(Long.valueOf(0)) ? applyerName : userDao.queryById(applyerId).getLoginId();
        Account account = accountDao.selectByPrimaryKey(accountId);
        Double balance = account.getBalance().doubleValue();  //现金余额
        Double balanceFreeze = account.getBalanceFreeze().doubleValue();  //现金冻结余额
        Double secondSettlement = account.getBalanceSecondSettlement().doubleValue();  //二次结算余额
        Double secondSettlementFreeze = account.getBalanceSecondSettlementFreeze().doubleValue();  //二次结算冻结余额
        Double balanceRebate = account.getBalanceRebate().doubleValue();  //折让金额

        //报表踩点的流水list
        List<AccountTransLogDtoForReport> transLogList = new ArrayList<>();
        String applyTypeForReport = "";//申请类型用于报表

        //申请提现冻结
        if (Type.WITHDRAWALW_FREEZE.getCode().equals(flag)) {
            payType = PayType.SETTLEMENT.getCode();  //结算类型
            associationType = AssociationType.PAYMEN_ORDER.getCode();  //付款单号
            applyerType = ApplyType.INTO_THE_CAPITAL_ACCOUNT.getCode();  //二次结算账户余额转入资金账户
            applyTypeForReport = applyerType;//申请类型用于报表
            isSecond = false;
            cashCurrentBalance = balance;
            balance -= balanceWithdraw;
            cashHappenBalance = 0d;
            balanceFreeze += balanceWithdraw;  //冻结余额=冻结余额+提现余额
            amount = 0d;
            currentBalance = secondSettlement;
        } else if(Type.WITHDRAWALW_THAW.getCode().equals(flag)) {  //不通过提现解冻
            payType = PayType.FREEZE.getCode();  //冻结类型
            associationType = AssociationType.ORDER_CODE.getCode();//代运营交易单号
            applyerType = ApplyType.BALANCES_UNLOCK.getCode();//解锁资金账户余额
            applyTypeForReport = applyerType;//申请类型用于报表
            balance += balanceWithdraw;
            balanceFreeze -= balanceWithdraw;  //冻结余额=冻结余额-提现余额
            cashHappenBalance = balanceWithdraw;
            cashCurrentBalance = balance;
            amount = 0d;
            currentBalance = secondSettlement;
        } else if (Type.WITHDRAWALW_CONFIRM.getCode().equals(flag)) {  //提现确认
            payType = PayType.BALANCE.getCode();  //余额类型
            associationType = AssociationType.PAYMEN_ORDER.getCode();  //付款单号
            applyerType = ApplyType.CAPITAL_ACCOUNT_TRANSFER.getCode();  //资金账户转出
            applyTypeForReport = applyerType;//申请类型用于报表
            balance -= balanceWithdraw;
            cashHappenBalance = -balanceWithdraw;
            cashCurrentBalance = balance;
            amount = 0d;
            currentBalance = secondSettlement;
            accountTransLogDao.updateStatusById(account.getId(), consignOrderCode);
        } else if (Type.RELATION.getCode().equals(flag)) {  //关联
            //如果关联的时候，二次结算余额大于0，表示可以抵扣
            if (freeze > 0) {
                payType = PayType.SETTLEMENT.getCode();  //结算类型
                applyerType = ApplyType.INTO_THE_CAPITAL_ACCOUNT.getCode();  //二次结算账户余额转入资金账户
                cashCurrentBalance = balance + freeze;
                secondSettlement -= freeze;  //二次结算余额
                cashHappenBalance = freeze;
                amount = -freeze;
                currentBalance = secondSettlement;
            //如果关联的时候，二次结算余额小于0，表示欠款，需要还款
            } else if (freeze < 0) {
                payType = PayType.SETTLEMENT.getCode();  //结算类型
                applyerType = ApplyType.SECONDARY_SETTLEMENT_ACCOUNT_BALANCES.getCode();  //抵扣二次结算账户欠款
                cashCurrentBalance = balance + freeze;
                secondSettlement -= freeze;  //二次结算余额
                cashHappenBalance = freeze;
                amount = -freeze;
                currentBalance = secondSettlement;
            }else{
                payType = PayType.FREEZE.getCode();  //冻结类型
                applyerType = ApplyType.BALANCES_LOCK.getCode();  //锁定资金账户余额
                cashHappenBalance = -balanceWithdraw;
                cashCurrentBalance = balance - balanceWithdraw;
                amount = 0d;
                currentBalance = secondSettlement;
            }
            associationType = AssociationType.ORDER_CODE.getCode();  //代运营交易单号
            balance = balance + freeze - balanceWithdraw;  //现金余额=现金余额+二次结算余额
            balanceFreeze += balanceWithdraw;  //冻结余额=冻结余额+待关联金额

            //报表的申请类型和流水的申请类型有区别
            applyTypeForReport = ApplyTypeForReport.RELETED_CONTRACT.getCode();//申请类型用于报表 关联合同销售类型

        } else if (Type.SECONDARY_SETTLEMENT.getCode().equals(flag)) {  //二次结算
            payType = PayType.SETTLEMENT.getCode();  //结算类型
            associationType = AssociationType.ORDER_CODE.getCode();  //代运营交易单号
            applyerType = ApplyType.SECOND_PAY.getCode();  //二次结算
            applyTypeForReport = applyerType;//申请类型用于报表
            secondSettlement += freeze;//二次结算余额
            cashHappenBalance = 0d;
            cashCurrentBalance = balance;
            amount = freeze;
            currentBalance = secondSettlement;
        } else if (Type.DEDUCTIONS.getCode().equals(flag)) {  //抵扣
            payType = PayType.SETTLEMENT.getCode();  //结算类型
            associationType = AssociationType.PAYMEN_ORDER.getCode();  //付款单号
            applyerType = ApplyType.SECONDARY_SETTLEMENT_ACCOUNT_BALANCES.getCode();  //抵扣二次结算账户欠款
            applyTypeForReport = applyerType;//申请类型用于报表

            balance -= balanceWithdraw;  //现金余额=现金余额-抵扣余额
            if (secondSettlement > 0)
                secondSettlement -= balanceWithdraw;  //二次结算余额=二次结算余额-抵扣余额
            else
                secondSettlement += balanceWithdraw;  //二次结算余额=二次结算余额+抵扣余额

            cashHappenBalance = -balanceWithdraw;
            cashCurrentBalance = balance;
            amount = balanceWithdraw;
            currentBalance = secondSettlement;
        } else if (Type.SECONDARY_SETTLEMENT_FREEZE.getCode().equals(flag)) {  //抵扣二次结算账户欠款冻结
            payType = PayType.FREEZE.getCode();  //冻结类型
            associationType = AssociationType.ORDER_CODE.getCode();//代运营交易单号
            applyerType = ApplyType.SECONDARY_SETTLEMENT_LOCK.getCode();//二次结算账户余额锁定
            applyTypeForReport = applyerType;//申请类型用于报表
            if (secondSettlement > 0) {
                secondSettlementFreeze += balanceWithdraw;  //二次结算冻结余额=二次结算冻结余额+冻结余额
                secondSettlement -= balanceWithdraw;  //二次结算余额=二次结算余额-冻结余额
            }else{
                secondSettlementFreeze -= balanceWithdraw;  //二次结算冻结余额=二次结算冻结余额-冻结余额
                secondSettlement += balanceWithdraw;  //二次结算余额=二次结算余额-冻结余额
            }
            cashHappenBalance = 0d;
            cashCurrentBalance = balance;
            amount = balanceWithdraw;
            currentBalance = secondSettlement;
        } else if(Type.SECONDARY_SETTLEMENT_DIKB.getCode().equals(flag)) {  //抵扣二次结算账户欠款解冻
            payType = PayType.FREEZE.getCode();  //冻结类型
            associationType = AssociationType.ORDER_CODE.getCode();//代运营交易单号
            applyerType = ApplyType.SECONDARY_SETTLEMENT_UNLOCK.getCode();//二次结算账户余额解锁
            applyTypeForReport = applyerType;//申请类型用于报表
            secondSettlementFreeze += balanceWithdraw;  //二次结算冻结余额=二次结算冻结余额-抵扣余额
            secondSettlement -= balanceWithdraw;  //二次结算余额=二次结算余额-抵扣余额
            cashHappenBalance = 0d;
            cashCurrentBalance = balance;
            amount = -balanceWithdraw;
            currentBalance = secondSettlement;
        } else if(Type.SECONDARY_SETTLEMENT_CONFIRM.getCode().equals(flag)) {  //抵扣二次结算账户欠款确认
            payType = PayType.SETTLEMENT.getCode();  //结算类型
            associationType = AssociationType.ORDER_CODE.getCode();//代运营交易单号
            applyerType = ApplyType.SECONDARY_SETTLEMENT_ACCOUNT_BALANCES.getCode();//抵扣二次结算账户欠款
            applyTypeForReport = applyerType;//申请类型用于报表
            balance -= balanceWithdraw;
            secondSettlement += balanceWithdraw;
            //secondSettlementFreeze += balanceWithdraw;  //二次结算冻结余额=二次结算冻结余额-支付余额
            cashHappenBalance = -balanceWithdraw;
            cashCurrentBalance = balance;
            amount = balanceWithdraw;
            currentBalance = secondSettlement;
        } else if (Type.SECONDWITHDRAWALW.getCode().equals(flag)) {  //二次结算提现冻结
            payType = PayType.FREEZE.getCode();  //冻结类型
            associationType = AssociationType.PAYMEN_ORDER.getCode();//付款单号
            applyerType = ApplyType.SECONDARY_SETTLEMENT_LOCK.getCode();//锁定二次结算账户余额
            applyTypeForReport = applyerType;//申请类型用于报表
            if (secondSettlement > 0) {
                secondSettlementFreeze += balanceWithdraw;  //二次结算冻结余额=二次结算冻结余额+提现余额
                secondSettlement -= balanceWithdraw;  //二次结算余额=二次结算余额-提现余额
            }else{
                secondSettlementFreeze += balanceWithdraw;  //二次结算冻结余额=二次结算冻结余额+提现余额
                secondSettlement += balanceWithdraw;  //二次结算余额=二次结算余额-提现余额
            }
            cashHappenBalance = 0d;
            cashCurrentBalance = balance;
            amount = -balanceWithdraw;
            currentBalance = secondSettlement;
        } else if(Type.SECONDWITHDRAWALW_THAW.getCode().equals(flag)) {  //二次结算提现解冻
            payType = PayType.FREEZE.getCode();  //冻结类型
            associationType = AssociationType.PAYMEN_ORDER.getCode();//付款单号
            applyerType = ApplyType.SECONDARY_SETTLEMENT_UNLOCK.getCode();//解锁二次结算账户余额
            applyTypeForReport = applyerType;//申请类型用于报表
            secondSettlementFreeze -= balanceWithdraw;  //二次结算冻结余额=二次结算冻结余额-提现余额
            secondSettlement += balanceWithdraw;  //二次结算余额=二次结算余额+提现余额
            cashHappenBalance = 0d;
            cashCurrentBalance = balance;
            amount = balanceWithdraw;
            currentBalance = secondSettlement;
        } else if(Type.SECONDWITHDRAWALW_CONFIRM.getCode().equals(flag)) {  //二次结算提现确认
            payType = PayType.SETTLEMENT.getCode();  //结算类型
            associationType = AssociationType.PAYMEN_ORDER.getCode();//付款单号
            applyerType = ApplyType.INTO_THE_CAPITAL_ACCOUNT.getCode();//二次结算账户余额转入资金账户
            applyTypeForReport = applyerType;//申请类型用于报表
            balance += balanceWithdraw;
            secondSettlement -= balanceWithdraw;
            cashHappenBalance = balanceWithdraw;
            cashCurrentBalance = balance;
            amount = -balanceWithdraw;
            currentBalance = secondSettlement;
        } else if(Type.SECONDWITHDRAWALW_OUT.getCode().equals(flag)) {  //二次结算提现转出
            payType = PayType.SETTLEMENT.getCode();  //结算类型
            associationType = AssociationType.PAYMEN_ORDER.getCode();//付款单号
            applyerType = ApplyType.CAPITAL_ACCOUNT_TRANSFER.getCode();//资金账户转出
            applyTypeForReport = applyerType;//申请类型用于报表
            balance -= balanceWithdraw;
            cashHappenBalance = -balanceWithdraw;
            cashCurrentBalance = balance;
            amount = 0d;
            currentBalance = secondSettlement;
        } else if(Type.PAYMENT_CONFIRM.getCode().equals(flag)) {  //付款确认
            payType = PayType.BALANCE.getCode();  //余额类型
            associationType = AssociationType.ORDER_CODE.getCode();  //代运营交易单号
            applyerType = ApplyType.PAY_THE_CONTRACT_PAYMENT.getCode();  //支付合同货款
            applyTypeForReport = applyerType;//申请类型用于报表
            balance -= balanceWithdraw;
            cashHappenBalance = -balanceWithdraw;
            cashCurrentBalance = balance;
            amount = 0d;
            currentBalance = secondSettlement;

            accountTransLogDao.updateStatusById(account.getId(), consignOrderCode);
        } else if(Type.ARRIVAL_EXCEPTION.getCode().equals(flag)) { //到账异常
            payType = PayType.BALANCE.getCode();  //余额类型
            associationType = AssociationType.BANK_OF_SERIAL_NUMBER.getCode();  //银行流水号
            applyerType = ApplyType.TO_CAPITAL_ACCOUNT.getCode();  //充值
            applyTypeForReport = applyerType;//申请类型用于报表
            balance += balanceWithdraw;
            cashHappenBalance = balanceWithdraw;
            cashCurrentBalance = balance;
            amount = 0d;
            currentBalance = secondSettlement;
        } else if (Type.SELLER_PAYMENT_APPLY.getCode().equals(flag)) {  //卖家付款申请冻结
            balance += balanceWithdraw;
            balanceFreeze -= balanceWithdraw;
        } else if(Type.SELLER_PAYMENT_THAW.getCode().equals(flag)) {  //卖家付款申请解冻
            balance -= balanceWithdraw;
            balanceFreeze += balanceWithdraw;
        } else if(Type.SELLER_PAYMENT_CONFIRM.getCode().equals(flag)) {  //卖家付款申请确认
            payType = PayType.BALANCE.getCode();  //余额
            associationType = AssociationType.ORDER_CODE.getCode();//代运营交易单号
            applyerType = ApplyType.GETONGKUAN_TO_ACCOUNT.getCode();//合同款到账
            applyTypeForReport = applyerType;//申请类型用于报表
            balance += balanceWithdraw;
            cashHappenBalance = balanceWithdraw;
            cashCurrentBalance = balance;
            amount = 0d;
            currentBalance = secondSettlement;
        } else if(Type.SELLER_PAYMENT_OUT.getCode().equals(flag)) {  //卖家付款申请确认转出
            payType = PayType.BALANCE.getCode();  //余额
            associationType = AssociationType.PAYMEN_ORDER.getCode();//付款单号
            applyerType = ApplyType.CAPITAL_ACCOUNT_TRANSFER.getCode();//资金账户转出
            applyTypeForReport = applyerType;//申请类型用于报表
            balance -= balanceWithdraw;
            cashHappenBalance -= balanceWithdraw;
            cashCurrentBalance = balance;
            amount = 0d;
            currentBalance = secondSettlement;

            accountTransLogDao.updateStatusById(account.getId(), consignOrderCode);
        } else if(Type.SELLER_SECONDARY_BACK.getCode().equals(flag)){  //卖家二次结算回退
            payType = PayType.BALANCE.getCode();  //余额
            associationType = AssociationType.PAYMEN_ORDER.getCode();//付款单号
            applyerType = ApplyType.SECONDARY_BACK.getCode();//二次结算回退
            applyTypeForReport = applyerType;//申请类型用于报表
            secondSettlement += freeze;
            cashHappenBalance = 0d;
            cashCurrentBalance = balance;
            amount = freeze;
            currentBalance = secondSettlement;

        } else if(Type.BALANCES_BACK.getCode().equals(flag)){  //买家资金账户和二次结算回退
            payType = PayType.BALANCE.getCode();  //余额
            associationType = AssociationType.PAYMEN_ORDER.getCode();//付款单号
            applyerType = ApplyType.BALANCES_BACK.getCode();//资金账户回退
            applyTypeForReport = applyerType;//申请类型用于报表
            balance += balanceWithdraw;
            secondSettlement += freeze;
            cashHappenBalance = balanceWithdraw;
            cashCurrentBalance = balance;
            amount = freeze;
            currentBalance = secondSettlement;

        } else if(Type.ACCEPT_DRAFT_RECHARGE.getCode().equals(flag)){  //银票充值
            payType = PayType.BALANCE.getCode();  //余额
            associationType = AssociationType.ACCEPT_DRAFT.getCode();//银票票号
            applyerType = ApplyType.ACCEPT_DRAFT_RECHARGE.getCode();//银票充值
            applyTypeForReport = applyerType;//申请类型用于报表
            balance += balanceWithdraw;
            cashHappenBalance = balanceWithdraw;
            cashCurrentBalance = balance;
            amount = 0d;
            currentBalance = secondSettlement;
        } else if(Type.ACCEPT_DRAFT_BACK.getCode().equals(flag)){  //银票回退
            payType = PayType.BALANCE.getCode();  //余额
            associationType = AssociationType.ACCEPT_DRAFT.getCode();//银票票号
            applyerType = ApplyType.ACCEPT_DRAFT_BACK.getCode();//银票回退
            applyTypeForReport = applyerType;//申请类型用于报表
            balance -= balanceWithdraw;
            cashHappenBalance = -balanceWithdraw;
            cashCurrentBalance = balance;
            amount = 0d;
            currentBalance = secondSettlement;
        } else if(Type.ACCEPT_DRAFT_LOCK.getCode().equals(flag)){  //锁定银票
            payType = PayType.BALANCE.getCode();  //余额
            associationType = AssociationType.ACCEPT_DRAFT.getCode();//银票票号
            applyerType = ApplyType.ACCEPT_DRAFT_LOCK.getCode();//锁定银票
            applyTypeForReport = applyerType;//申请类型用于报表
            balance -= balanceWithdraw;
            balanceFreeze += balanceWithdraw;
            cashHappenBalance = -balanceWithdraw;
            cashCurrentBalance = balance;
            amount = 0d;
            currentBalance = secondSettlement;
        } else if(Type.ACCEPT_DRAFT_UNLOCK.getCode().equals(flag)){  //解锁银票
            payType = PayType.BALANCE.getCode();  //余额
            associationType = AssociationType.ACCEPT_DRAFT.getCode();//银票票号
            applyerType = ApplyType.ACCEPT_DRAFT_UNLOCK.getCode();//解锁银票
            applyTypeForReport = applyerType;//申请类型用于报表
            balance += balanceWithdraw;
            balanceFreeze -= balanceWithdraw;
            cashHappenBalance = balanceWithdraw;
            cashCurrentBalance = balance;
            amount = 0d;
            currentBalance = secondSettlement;
        } else if(Type.REBATE.getCode().equals(flag) || Type.ROLLBACK_REBATE.getCode().equals(flag)){  //折让金额
            payType = PayType.BALANCE.getCode();  //余额
            associationType = AssociationType.REBATE.getCode();//折让单号.
            if (Type.REBATE.getCode().equals(flag)) {
            	applyerType = ApplyType.REBATE.getCode();//二次结算(折让金额)
                applyTypeForReport = applyerType;//申请类型用于报表
            } else if (Type.ROLLBACK_REBATE.getCode().equals(flag)) {
            	applyerType = ApplyType.ROLLBACK_REBATE.getCode();//二次结算(折让金额回滚)
                applyTypeForReport = applyerType;//申请类型用于报表
            }
            secondSettlement += balanceWithdraw;
            balanceRebate += balanceWithdraw;
            cashHappenBalance = 0d;
            cashCurrentBalance = balance;
            amount = balanceWithdraw;
            currentBalance = secondSettlement;
        } else if (Type.ADVANCE_PAYMENT_CONFIRM.getCode().equals(flag)) {  //lixiang 预付款
        	payType = PayType.ADVANCE_PAYMENT.getCode();  //余额
        	associationType = AssociationType.PAYMEN_ORDER.getCode();//付款单号
            applyerType = ApplyType.ADVANCE_PAYMENT.getCode();//预付款（申请类型）
            secondSettlement -= balanceWithdraw;
            amount = -balanceWithdraw;//二结发生额
            currentBalance = secondSettlement;
        }


        AccountTransLog accountTransLog = setAccountTransLog(account, associationType, consignOrderCode,
                serialNumber, amount, currentBalance, applyerId, applyerName, applyerDate,
                applyerType, payType, cashHappenBalance, cashCurrentBalance);
        //更新
        if (accountDao.updatePayment(accountId, BigDecimal.valueOf(balance),
                BigDecimal.valueOf(balanceFreeze), BigDecimal.valueOf(secondSettlement),
                BigDecimal.valueOf(secondSettlementFreeze), BigDecimal.valueOf(balanceRebate),
                applyerDate, loginId) > 0) {
            //不需要现在流水
            if(Type.SELLER_PAYMENT_APPLY.getCode().equals(flag)
                || Type.SELLER_PAYMENT_THAW.getCode().equals(flag)){
                result = true;
                return result;
            }

            if(Type.RELATION.getCode().equals(flag) && freeze == 0){
                //插入公司账目划转详情表
                if (accountTransLogDao.insertSelective(accountTransLog) > 0) {
                    result = true;

                    //添加到踩点列表
                    addAccountTransLogDtoForReport(transLogList,accountTransLog,applyTypeForReport);
                    //调用流水踩点接口
                    reportAccountFinancialService.accountTransLogOperation(transLogList);

                    return result;
                }
            }

            //插入公司账目划转详情表
            if (accountTransLogDao.insertSelective(accountTransLog) > 0) {

                //添加到踩点列表
                addAccountTransLogDtoForReport(transLogList, accountTransLog, applyTypeForReport);
                //调用流水踩点接口
                reportAccountFinancialService.accountTransLogOperation(transLogList);

                if((Type.RELATION.getCode().equals(flag) && freeze != 0) || Type.WITHDRAWALW_FREEZE.getCode().equals(flag)){
                    AccountTransLog accountTransLogRelation = setAccountTransLog(account, associationType, consignOrderCode,
                            serialNumber, 0d, currentBalance, applyerId, applyerName, applyerDate,
                            ApplyType.BALANCES_LOCK.getCode(), PayType.FREEZE.getCode(), -balanceWithdraw, balance);
                    //插入公司账目划转详情表
                    if (accountTransLogDao.insertSelective(accountTransLogRelation) > 0) {
                        result = true;
                        return result;
                    }
                }
                result = true;
                return result;
            }
        }
        return result;
    }

    /**
     * 设置公司账目划转详情表
     *
     * @param account
     * @param associationType
     * @param consignOrderCode
     * @param serialNumber
     * @param amount
     * @param currentBalance
     * @param applyerId
     * @param applyerName
     * @param applyerDate
     * @param applyerType
     * @param payType
     * @return
     */
    private AccountTransLog setAccountTransLog(Account account, String associationType, String consignOrderCode,
                                               String serialNumber, Double amount, Double currentBalance,
                                               Long applyerId, String applyerName, Date applyerDate,
                                               String applyerType, String payType,
                                               Double cashHappenBalance, Double cashCurrentBalance) {
        //插入公司账目划转详情表
        AccountTransLog accountTransLog = new AccountTransLog();
        accountTransLog.setAccountId(account.getId());
        accountTransLog.setConsignOrderCode(consignOrderCode);
        accountTransLog.setSerialNumber(serialNumber);
        accountTransLog.setApplyType(applyerType);  //申请类型
        accountTransLog.setAmount(BigDecimal.valueOf(amount));  //发生金额
        accountTransLog.setApplyerId(applyerId);  //申请人id
        accountTransLog.setApplyerName(applyerName);  //申请人姓名
        accountTransLog.setSerialTime(applyerDate);  //申请人时间
        accountTransLog.setType(account.getType());
        accountTransLog.setCurrentBalance(BigDecimal.valueOf(currentBalance));  //当前余额
        accountTransLog.setPayType(payType);  //结算类型
        accountTransLog.setAssociationType(associationType);  //关联类型
        accountTransLog.setCashHappenBalance(BigDecimal.valueOf(cashHappenBalance));//现金发生余额
        accountTransLog.setCashCurrentBalance(BigDecimal.valueOf(cashCurrentBalance));//现金当前余额
        accountTransLog.setCreated(new Date());
        accountTransLog.setCreatedBy(applyerName);
        accountTransLog.setLastUpdated(new Date());
        accountTransLog.setLastUpdatedBy(applyerName);
        accountTransLog.setModificationNumber(0);
        return accountTransLog;
    }

    /**
     * 获取订单操作详情
     *
     * @param orderId
     * @return
     */
    public List<ConsignOrderStatusDto> getAuditDetailById(Long orderId) {
        List<ConsignOrderStatusDto> list = orderStatusDao.getAuditDetailById(orderId);
        if (list != null && list.size() > 0) {
            for (ConsignOrderStatusDto item : list) {
                if (OrderStatusType.PICKUP.toString().toLowerCase().equals(item.getStatusType().toLowerCase())) {
                    item.setStatus(Constant.ORDERSTATUSNAME_PICKEDUP);
                } else if (OrderStatusType.FILLUP.toString().toLowerCase().equals(item.getStatusType().toLowerCase())) {
                    item.setStatus(Constant.ORDERSTATUSNAME_FILLEDUP);
                }
            }
        }
        return list;
    }

    @Override
    public int insertOrderAuditTrail(Long orderId, String statusType, User user, String status) {
        OrderAuditTrail oat = new OrderAuditTrail();
        oat.setStatusType(statusType);
        oat.setOrderId(orderId);
        oat.setSetToStatus(status);
        oat.setCreated(new Date());
        oat.setLastUpdated(new Date());
        oat.setCreatedBy(user.getLoginId());
        oat.setLastUpdatedBy(user.getLoginId());
        oat.setModificationNumber(0);
        oat.setOperatorId(user.getId());
        oat.setOperatorName(user.getName());
        return orderStatusDao.insertOrderAuditTrail(oat);
    }

    private void addAccountTransLogDtoForReport(List<AccountTransLogDtoForReport> transLogList,AccountTransLog accountTransLog,String applyTypeForReport){
        try {
            AccountTransLogDtoForReport dest = new AccountTransLogDtoForReport(applyTypeForReport);
            PropertyUtils.copyProperties(dest, accountTransLog);
            transLogList.add(dest);
        } catch (Exception e) {
            logger.error("copy properties fail",e);
            e.printStackTrace();
        }
    }

    @Override
    public String queryCloseReasonByOrderId(Long orderId){
        return orderStatusDao.queryCloseReasonByOrderId(orderId);
    }

}