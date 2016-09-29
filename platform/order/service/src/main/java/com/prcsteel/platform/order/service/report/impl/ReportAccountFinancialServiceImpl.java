package com.prcsteel.platform.order.service.report.impl;

import com.prcsteel.platform.account.model.model.Account;
import com.prcsteel.platform.account.persist.dao.AccountDao;
import com.prcsteel.platform.common.utils.CbmsNumberUtil;
import com.prcsteel.platform.account.model.dto.AccountTransLogDtoForReport;
import com.prcsteel.platform.order.model.dto.BankOriginalDto;
import com.prcsteel.platform.order.model.dto.IcbcBdDto;
import com.prcsteel.platform.order.model.dto.ReportAccountFundDetailDto;

import com.prcsteel.platform.order.model.dto.ReportAccountFundDto;
import com.prcsteel.platform.order.model.enums.*;
import com.prcsteel.platform.order.model.model.*;
import com.prcsteel.platform.order.model.query.ReportAccountFundQuery;
import com.prcsteel.platform.order.persist.dao.*;
import com.prcsteel.platform.order.service.report.ReportAccountFinancialService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * @author dengxiyan
 * @version V1.0
 * @Title: ReportAccountFinancialServiceImpl
 * @Package com.prcsteel.platform.order.service.report.impl
 * @Description: 往来单位财务报表服务
 * @date 2015/12/21
 */
@Service("reportAccountFinancialService")
public class ReportAccountFinancialServiceImpl implements ReportAccountFinancialService {
    @Resource
    AccountDao accountDao;

    @Resource
    ReportAccountFinancialDao reportAccountFinancialDao;

    @Resource
    ConsignOrderContractDao consignOrderContractDao;

    @Resource
    ConsignOrderDao consignOrderDao;

    @Resource
    AllowanceOrderItemDao allowanceOrderItemDao;

    @Resource
    IcbcBdlDetailDao icbcBdlDetailDao;

    @Resource
    BankOriginalDetailDao bankOriginalDetailDao;

    @Resource
    AllowanceDao allowanceDao;

    @Resource
    ConsignOrderItemsDao consignOrderItemsDao;

    private static final Logger logger = LoggerFactory.getLogger(ReportAccountFinancialServiceImpl.class);

    private static final String TEXT_INIT_BALANCE = "期初余额（元）";
    private static final String TEXT_ENDING_BALANCE = "期末余额（元）";

    /**
     * 流水操作踩点
     *
     * @param transLogList
     */
    @Override
    public void accountTransLogOperation(List<AccountTransLogDtoForReport> transLogList) {
        //收款、付款、采购、销售、二结（买卖家采购、销售） 、订单关闭（流水类型区分 采购、销售）、
        // 折让需根据流水：判断插入多少条数据 销售调价、销售回滚、采购调价、采购回滚（买卖家）（当前余额和发生额一样）
        try {
            if (transLogList != null && transLogList.size() > 0) {
                transLogList.forEach(log -> {
                    try {
                        Account account = accountDao.selectByPrimaryKey(log.getAccountId());
                        //收款/付款
                        if (ApplyTypeForReport.TO_CAPITAL_ACCOUNT.getCode().equals(log.getApplyTypeForReport())
                                || ApplyTypeForReport.ACCEPT_DRAFT_RECHARGE.getCode().equals(log.getApplyTypeForReport())
                                || ApplyTypeForReport.CAPITAL_ACCOUNT_TRANSFER.getCode().equals(log.getApplyTypeForReport())
                                || ApplyTypeForReport.ACCEPT_DRAFT_BACK.getCode().equals(log.getApplyTypeForReport())) {
                            // 收款/付款（银行交易流水）
                            addBankTrade(log, account);
                        } else if (ApplyTypeForReport.REBATE.getCode().equals(log.getApplyTypeForReport()) || ApplyTypeForReport.ROLLBACK_REBATE.getCode().equals(log.getApplyTypeForReport())) {
                            //折让调价 折让回滚类型
                            addAllowance(log, account);
                        } else if (ApplyTypeForReport.RELETED_CONTRACT.getCode().equals(log.getApplyTypeForReport())
                                || ApplyTypeForReport.BALANCES_UNLOCK.getCode().equals(log.getApplyTypeForReport())
                                || ApplyTypeForReport.GETONGKUAN_TO_ACCOUNT.getCode().equals(log.getApplyTypeForReport())
                                || ApplyTypeForReport.SECOND_PAY.getCode().equals(log.getApplyTypeForReport())
                                || ApplyTypeForReport.SECONDARY_BACK.getCode().equals(log.getApplyTypeForReport())
                                || ApplyTypeForReport.BALANCES_BACK.getCode().equals(log.getApplyTypeForReport())) {
                            //订单类
                            addOrder(log, account);
                        }
                    } catch (Exception e) {
                        logger.error("流水踩点失败：流水id" + log.getId(), e);
                    }
                });
            }
        } catch (Exception e) {
            logger.error("流水踩点失败", e);
        }
    }

    /**
     * 收款/付款(银行交易流水)
     *
     * @return
     */
    private void addBankTrade(AccountTransLogDtoForReport log, Account account) {
        ReportAccountFinancial accountFinancial = null;

        //收款1
        if (ApplyTypeForReport.TO_CAPITAL_ACCOUNT.getCode().equals(log.getApplyTypeForReport())) {
            accountFinancial = buildReportAccountFinancial(log, account);
            accountFinancial.setOperateType(ReportAccountFinancialOperationType.Receive.getCode());
            accountFinancial.setRemark(ReportAccountFinancialOperationType.Receive.getName());
            accountFinancial.setReceivedAmount(log.getCashHappenBalance());
            accountFinancial.setSerialCode(log.getSerialNumber());
        }

        //银票充值16
        if (ApplyTypeForReport.ACCEPT_DRAFT_RECHARGE.getCode().equals(log.getApplyTypeForReport())){
            accountFinancial = buildReportAccountFinancial(log, account);
            accountFinancial.setOperateType(ReportAccountFinancialOperationType.Receive.getCode());
            accountFinancial.setRemark(ReportAccountFinancialOperationType.Receive.getName());
            accountFinancial.setReceivedAmount(log.getCashHappenBalance());
            accountFinancial.setSerialCode(log.getConsignOrderCode());
        }

        //付款4
        if (ApplyTypeForReport.CAPITAL_ACCOUNT_TRANSFER.getCode().equals(log.getApplyTypeForReport())) {
            accountFinancial = buildReportAccountFinancial(log, account);
            accountFinancial.setOperateType(ReportAccountFinancialOperationType.Payment.getCode());
            accountFinancial.setRemark(ReportAccountFinancialOperationType.Payment.getName());
            accountFinancial.setPayedAmount(log.getCashHappenBalance());

            //银行流水号：根据客户名称 + 银行账号 + 金额 重复取最后一个
            accountFinancial.setSerialCode(getBankSerialCode(log.getCashHappenBalance(), account));
        }

        //取消银票充值 17
        if (ApplyTypeForReport.ACCEPT_DRAFT_BACK.getCode().equals(log.getApplyTypeForReport())) {
            accountFinancial = buildReportAccountFinancial(log, account);
            accountFinancial.setOperateType(ReportAccountFinancialOperationType.Payment.getCode());
            accountFinancial.setRemark(ReportAccountFinancialOperationType.Payment.getName());
            accountFinancial.setPayedAmount(log.getCashHappenBalance());
            accountFinancial.setSerialCode(log.getConsignOrderCode());
        }

        if (accountFinancial != null) {
            int count = reportAccountFinancialDao.insertSelective(accountFinancial);
            if (count != 1) {
                logger.warn("流水踩点: 收款/付款踩点，插入数据失败,插入对象：" + accountFinancial.toString());
            }
        }
    }


    /**
     * 订单：销售、采购、二结（买卖家采购、销售） 、订单关闭（流水类型区分 采购、销售）
     *
     * @return
     */
    private void addOrder(AccountTransLogDtoForReport log, Account account) {
        ReportAccountFinancial accountFinancial = null;

        //查询订单 卖家合同
        ConsignOrder consignOrder = consignOrderDao.selectByCode(log.getConsignOrderCode());

        //销售-1 关联合同时 金额取订单关联合同金额（没有真正打款 流水不好取）
        if (ApplyTypeForReport.RELETED_CONTRACT.getCode().equals(log.getApplyTypeForReport())) {
            accountFinancial = buildReportAccountFinancial(log, account);
            accountFinancial.setOrderCode(log.getConsignOrderCode());//订单号
            accountFinancial.setOperateType(ReportAccountFinancialOperationType.Sale.getCode());
            accountFinancial.setRemark(ReportAccountFinancialOperationType.Sale.getName());
            accountFinancial.setContractCode(consignOrder.getContractCode());//销售合同号
            accountFinancial.setSaleAmount(consignOrder.getTotalContractReletedAmount());//买家合同关联金额
        }

        //采购8
        if (ApplyTypeForReport.GETONGKUAN_TO_ACCOUNT.getCode().equals(log.getApplyTypeForReport())) {
            accountFinancial = buildReportAccountFinancial(log, account);
            accountFinancial.setOrderCode(log.getConsignOrderCode());//订单号
            //获取卖家的合同
            ConsignOrderContract consignOrderContract = consignOrderContractDao.queryByOrderIdAndCustomerId(consignOrder.getId(), log.getAccountId());
            accountFinancial.setOperateType(ReportAccountFinancialOperationType.Purchase.getCode());
            accountFinancial.setRemark(ReportAccountFinancialOperationType.Purchase.getName());
            accountFinancial.setContractCode(consignOrderContract.getContractCodeAuto());//采购合同号
            accountFinancial.setPurchaseAmount(log.getCashHappenBalance());
        }

        //二结3 二结发生额（多提为正：少提为负）（采购、销售同样逻辑）
        if (ApplyTypeForReport.SECOND_PAY.getCode().equals(log.getApplyTypeForReport())) {
            accountFinancial = buildReportAccountFinancial(log, account);
            accountFinancial.setOrderCode(log.getConsignOrderCode());//订单号
            accountFinancial.setOperateType(ReportAccountFinancialOperationType.SecondSettlement.getCode());
            accountFinancial.setRemark(ReportAccountFinancialOperationType.SecondSettlement.getName());

            if (log.getAccountId().equals(consignOrder.getAccountId())) {
                //买家 多提为正：少提为负
                accountFinancial.setContractCode(consignOrder.getContractCode());
                accountFinancial.setSaleAmount(log.getAmount().negate());
            } else {
                //卖家 获取卖家的合同 多提为正：少提为负
                ConsignOrderContract consignOrderContract = consignOrderContractDao.queryByOrderIdAndCustomerId(consignOrder.getId(), log.getAccountId());
                accountFinancial.setContractCode( consignOrderContract.getContractCodeAuto());
                accountFinancial.setPurchaseAmount(log.getAmount());
            }
        }

        //订单关闭：买家解锁资金账户余额(订单操作是最开始更新的订单状态)关联合同后的关闭（ 确认付款前的关闭 -2、-4） （没有真正打款 流水不好取）
        if (ApplyTypeForReport.BALANCES_UNLOCK.getCode().equals(log.getApplyTypeForReport())
                && consignOrder != null && (ConsignOrderStatus.CLOSEAPPROVED.getCode().equals(consignOrder.getStatus() )
                                          || ConsignOrderStatus.CLOSEPAY.getCode().equals(consignOrder.getStatus()))){
            accountFinancial = buildReportAccountFinancial(log, account);
            accountFinancial.setOrderCode(log.getConsignOrderCode());//订单号
            accountFinancial.setOperateType(ReportAccountFinancialOperationType.OrderCloseSale.getCode());
            accountFinancial.setRemark(ReportAccountFinancialOperationType.OrderCloseSale.getName());
            accountFinancial.setContractCode(consignOrder.getContractCode());//销售合同号
            //订单关闭：-关联合同金额
            accountFinancial.setSaleAmount(consignOrder.getTotalContractReletedAmount().negate());//合同关联金额回滚
        }


        //订单关闭 卖家14 采购金额 二次结算发生金额   确认付款后及二结后的关闭
        if (ApplyTypeForReport.SECONDARY_BACK.getCode().equals(log.getApplyTypeForReport())) {
            accountFinancial = buildReportAccountFinancial(log, account);
            accountFinancial.setOrderCode(log.getConsignOrderCode());//订单号

            //获取卖家的合同
            ConsignOrderContract consignOrderContract = consignOrderContractDao.queryByOrderIdAndCustomerId(consignOrder.getId(), log.getAccountId());
            accountFinancial.setOperateType(ReportAccountFinancialOperationType.OrderClosePurchase.getCode());
            accountFinancial.setRemark(ReportAccountFinancialOperationType.OrderClosePurchase.getName());
            accountFinancial.setContractCode(consignOrderContract.getContractCodeAuto());//采购合同号

            //订单关闭：-确认付款金额+（±二结发生额）
            accountFinancial.setPurchaseAmount(log.getAmount());
        }

        //订单关闭：买家15 销售金额  确认付款后及二结后的关闭
        if (ApplyTypeForReport.BALANCES_BACK.getCode().equals(log.getApplyTypeForReport())) {
            accountFinancial = buildReportAccountFinancial(log, account);
            accountFinancial.setOrderCode(log.getConsignOrderCode());//订单号
            accountFinancial.setOperateType(ReportAccountFinancialOperationType.OrderCloseSale.getCode());
            accountFinancial.setRemark(ReportAccountFinancialOperationType.OrderCloseSale.getName());
            accountFinancial.setContractCode(consignOrder.getContractCode());//销售合同号

            //确认付款后的关闭(订单操作是最后更新的订单状态) -关联合同金额
            if (ConsignOrderStatus.RELATED.getCode().equals(consignOrder.getOriginStatus())
                  || ConsignOrderStatus.SECONDSETTLE.getCode().equals(consignOrder.getOriginStatus())){
                accountFinancial.setSaleAmount(consignOrder.getTotalContractReletedAmount().negate());
            }

            // 二次结算关闭(订单操作是最后更新的订单状态) -关联合同金额 + (订单二结发生额)
            if(ConsignOrderStatus.INVOICEREQUEST.getCode().equals(consignOrder.getOriginStatus())){
                //二次结算发生金额
                BigDecimal secondHappenAmount = getBuyerSecondAmount(consignOrder);
                accountFinancial.setSaleAmount(consignOrder.getTotalContractReletedAmount().negate().add(secondHappenAmount));
            }
        }

        if (accountFinancial != null) {
            int count = reportAccountFinancialDao.insertSelective(accountFinancial);
            if (count != 1) {
                logger.warn("流水踩点：订单类踩点插入数据失败,插入对象：" + accountFinancial.toString());
            }
        }
    }

    /**
     * 折让调价/回滚 销售调价、销售回滚、采购调价、采购回滚（买卖家）
     *
     * @return
     */
    private void addAllowance(AccountTransLogDtoForReport log, Account account) {
        Allowance allowance = allowanceDao.queryByAllowanceCode(log.getConsignOrderCode());
        if (allowance != null) {
            try {
                ReportAccountFinancial accountFinancial = null;

                //调价
                if (ApplyTypeForReport.REBATE.getCode().equals(log.getApplyTypeForReport())) {
                    accountFinancial = buildReportAccountFinancial(log, account);
                    //销售调价：提价为正，降价为负
                    if (AllowanceType.Buyer.getKey().equals(allowance.getAllowanceType())) {
                        accountFinancial.setSaleAmount(log.getAmount().negate());
                        accountFinancial.setOperateType(ReportAccountFinancialOperationType.AllowanceSale.getCode());
                        accountFinancial.setRemark(ReportAccountFinancialOperationType.AllowanceSale.getName(log.getConsignOrderCode()));
                    }
                    //采购调价：提价为正，降价为负
                    if (AllowanceType.Seller.getKey().equals(allowance.getAllowanceType())) {
                        accountFinancial.setPurchaseAmount(log.getAmount());
                        accountFinancial.setOperateType(ReportAccountFinancialOperationType.AllowancePurchase.getCode());
                        accountFinancial.setRemark(ReportAccountFinancialOperationType.AllowancePurchase.getName(log.getConsignOrderCode()));
                    }
                }

                //回滚
                if (ApplyTypeForReport.ROLLBACK_REBATE.getCode().equals(log.getApplyTypeForReport())) {
                    accountFinancial = buildReportAccountFinancial(log, account);
                    //销售调回滚：与该订单销售调价金额之和为0
                    if (AllowanceType.Buyer.getKey().equals(allowance.getAllowanceType())) {
                        accountFinancial.setSaleAmount(log.getAmount().negate());
                        accountFinancial.setOperateType(ReportAccountFinancialOperationType.AllowanceRollBackSale.getCode());
                        accountFinancial.setRemark(ReportAccountFinancialOperationType.AllowanceRollBackSale.getName(log.getConsignOrderCode()));
                    }
                    //采购调价回滚：与该订单采购调价金额之和为0
                    if (AllowanceType.Seller.getKey().equals(allowance.getAllowanceType())) {
                        accountFinancial.setPurchaseAmount(log.getAmount());
                        accountFinancial.setOperateType(ReportAccountFinancialOperationType.AllowanceRollBackPurchase.getCode());
                        accountFinancial.setRemark(ReportAccountFinancialOperationType.AllowanceRollBackPurchase.getName(log.getConsignOrderCode()));
                    }
                }

                if (accountFinancial != null) {
                    int count = reportAccountFinancialDao.insertSelective(accountFinancial);
                    if (count != 1) {
                        logger.warn("流水踩点：折让踩点插入数据失败：流水id" + log.getId() + " 折让单号：" + log.getConsignOrderCode() +
                                " 插入对象：" + accountFinancial.toString());
                    }
                }
            } catch (Exception e) {
                logger.error("流水踩点：折让踩点插入数据失败：流水id" + log.getId() + " 折让单号：" + log.getConsignOrderCode(), e);
            }
        }
    }

    private ReportAccountFinancial buildReportAccountFinancial(AccountTransLogDtoForReport log, Account account) {
        ReportAccountFinancial accountFinancial = new ReportAccountFinancial();
        accountFinancial.setAccountId(log.getAccountId());
        accountFinancial.setAccountName(account.getName());
        accountFinancial.setCreatedBy(log.getCreatedBy());
        accountFinancial.setLastUpdatedBy(log.getLastUpdatedBy());
        accountFinancial.setHappenTime(log.getCreated());
        BigDecimal currentBalance = CbmsNumberUtil.buildMoney(log.getCashCurrentBalance()).add(CbmsNumberUtil.buildMoney(log.getCurrentBalance()));
        accountFinancial.setCurrentBalance(currentBalance.negate());//钢为角度：账户余额取反
        return accountFinancial;
    }


    private String getBankSerialCode(BigDecimal amount, Account account) {
        BankOriginalDto bankOriginalDto = bankOriginalDetailDao.queryBankReceipts(amount.abs(), account.getName().replaceAll("[\\s\\-]*", "")
                , account.getAccountCode().replaceAll("[\\s\\-]*", "")
        );
        if (bankOriginalDto != null) {
            return bankOriginalDto.getSeqno();
        } else {
            IcbcBdDto icbcBdDto = icbcBdlDetailDao.queryBankReceipts(amount.abs(), account.getName().replaceAll("[\\s\\-]*", "")
                    , account.getAccountCode().replaceAll("[\\s\\-]*", "")
            );
            if (icbcBdDto != null) {
                return icbcBdDto.getSequenceNo();
            }
        }
        return "";
    }

    @Override
    public Map<String,Object> getAccountFinancialDetail(ReportAccountFundQuery query) {
        Map<String,Object> resultMap = new HashMap<>();
        List<ReportAccountFundDetailDto> list = reportAccountFinancialDao.queryAccountFinancialDetailByParam(query);
        resultMap.put("size",new Integer(list.size())); //记录列表的size 分页中不加期初期末余额行
        resultMap.put("data", list);

        //期初余额行: 应收账款为期初余额
        ReportAccountFundDetailDto initialBalanceRow = buildInitalBalanceRow(query);
        ReportAccountFundDetailDto endingBalanceRow;
        if (list.size() > 0){
            //期末余额行 应收账款为期末余额，实际采购交易金额、实际销售交易金额、银行存款发生金额所有页的合计
            endingBalanceRow = buildEndingBalanceRow(query);
        }else{
            //没有交易数据，期末余额则为期初余额
            endingBalanceRow = new ReportAccountFundDetailDto();
            endingBalanceRow.setDateStr(TEXT_ENDING_BALANCE);
            endingBalanceRow.setCurrentBalance(initialBalanceRow.getCurrentBalance());
        }

        //期初放第一条
        list.add(0, initialBalanceRow);

        //期末余额
        list.add(endingBalanceRow);
        return resultMap;
    }

    private ReportAccountFundDetailDto buildInitalBalanceRow(ReportAccountFundQuery query){
        ReportAccountFundDetailDto dto = new ReportAccountFundDetailDto();
        //期初余额
        //踩点表 已是报表要展现的应收账款
        BigDecimal initialBalance = reportAccountFinancialDao.queryAccountInitialBalance(query);
        dto.setDateStr(TEXT_INIT_BALANCE);
        dto.setCurrentBalance(initialBalance);
        dto.setOperateType(ReportAccountFinancialOperationType.Initial.getCode());
        dto.setRemark(ReportAccountFinancialOperationType.Initial.getName());
        return dto;
    }

    private ReportAccountFundDetailDto buildEndingBalanceRow(ReportAccountFundQuery query){
        //在搜索时间范围内所有页的合计合计值：实际采购交易金额、实际销售交易金额、银行存款发生金额
        ReportAccountFundDetailDto sumDto = reportAccountFinancialDao.sumAccountFinancial(query);
        //期末余额
        BigDecimal endingBalance = reportAccountFinancialDao.queryAccountEndingBalance(query);
        sumDto.setDateStr(TEXT_ENDING_BALANCE);
        sumDto.setCurrentBalance(endingBalance);
        return sumDto;
    }

    @Override
    public int queryAccountFinancialDetailTotalByParam(ReportAccountFundQuery query) {
        return reportAccountFinancialDao.queryAccountFinancialDetailTotalByParam(query);
    }

    private BigDecimal getBuyerSecondAmount(ConsignOrder consignOrder){
        List<ConsignOrderItems> itemsList = consignOrderItemsDao.selectByOrderId(consignOrder.getId());
        double buyerShouldPayAmount = itemsList.stream().mapToDouble(c -> c.getDealPrice().multiply(c.getActualPickWeightServer()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()).sum();
        return new BigDecimal(consignOrder.getTotalContractReletedAmount().doubleValue() - buyerShouldPayAmount);
    }

    @Override
    public int getTotal(ReportAccountFundQuery query) {
        return  reportAccountFinancialDao.queryAccountFinancialTotalByParam(query);
    }

    @Override
    public List<ReportAccountFundDto> getAccountFinancialList(ReportAccountFundQuery query) {
        // 设置流水类型参数
        List<ReportAccountFundDto> list = reportAccountFinancialDao.queryAccountFinancialByParam(query);
        List<ReportAccountFundDto> endingBalanceList = reportAccountFinancialDao.queryAccountEndingBalanceList(query);
        if (list != null && list.size() >0){
            list.forEach(a->{

                //已付金额界面显示为正数
                if (a.getPayedAmount() != null){
                    a.setPayedAmount(a.getPayedAmount().abs());
                }

                //期末余额：期末没有则显示期初值
                Optional<ReportAccountFundDto> op = endingBalanceList.stream().filter(b->a.getAccountId().equals(b.getAccountId())).findFirst();
                if (op.isPresent()){
                    a.setEndingBalance(op.get().getEndingBalance());
                }else{
                    a.setEndingBalance(a.getInitialBalance());
                }

            });
        }
        return list;
    }
}
