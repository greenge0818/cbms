package com.prcsteel.platform.account.service;

import com.prcsteel.platform.account.model.enums.AccountTransApplyType;
import com.prcsteel.platform.order.model.enums.AssociationType;
import com.prcsteel.platform.order.model.enums.PayType;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Rabbit
 * @version V1.0
 * @Title: AccountFundService.java
 * @Package com.prcsteel.platform.account.service
 * @Description: 客户账户服务
 * @date 2016-3-10 14:54:53
 */
public interface AccountFundService {
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
    void updateAccountFund(Long accountId, AssociationType associationType, String code, AccountTransApplyType accountTransApplyType,
                           BigDecimal balance, BigDecimal balanceFreeze, BigDecimal secondSettlement, BigDecimal secondSettlementFreeze,
                           BigDecimal credit, BigDecimal creditLimit, PayType payType, Long operatorId, String operatorName, Date operateDate);

    /**
     * 信用额度还款
     * @param accountId               客户id
     * @param associationType         关联类型(涉及订单的需要给这个参数)
     * @param code                    订单号(涉及订单的需要给这个参数)
     * @param operatorId              操作者id
     * @param operatorName            操作者姓名
     * @param operateDate             操作时间(操作时间或者银行流水时间)
     */
    void payForCredit(Long accountId, AssociationType associationType, String code, Long operatorId, String operatorName, Date operateDate);
}
