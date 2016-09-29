package com.prcsteel.platform.order.service.payment.impl;

import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.Resource;

import com.prcsteel.platform.account.model.enums.AccountBankDataStatus;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prcsteel.framework.nido.engine.Nido;
import com.prcsteel.platform.account.model.dto.DepartmentDto;
import com.prcsteel.platform.account.model.enums.AccountTransApplyType;
import com.prcsteel.platform.account.model.model.Account;
import com.prcsteel.platform.account.model.model.AccountBank;
import com.prcsteel.platform.account.model.model.AccountTransLog;
import com.prcsteel.platform.account.persist.dao.AccountBankDao;
import com.prcsteel.platform.account.persist.dao.AccountDao;
import com.prcsteel.platform.account.persist.dao.AccountTransLogDao;
import com.prcsteel.platform.account.service.AccountFundService;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.acl.persist.dao.UserDao;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.constants.NidoTaskConstant;
import com.prcsteel.platform.common.utils.NumberTool;
import com.prcsteel.platform.common.utils.Tools;
import com.prcsteel.platform.order.model.dto.BankOriginalDto;
import com.prcsteel.platform.order.model.dto.BankRechargeSMSDto;
import com.prcsteel.platform.order.model.enums.AppNoticeType;
import com.prcsteel.platform.order.model.enums.ApplyType;
import com.prcsteel.platform.order.model.enums.AssociationType;
import com.prcsteel.platform.order.model.enums.BankTransactionType;
import com.prcsteel.platform.order.model.enums.BankType;
import com.prcsteel.platform.order.model.enums.PayType;
import com.prcsteel.platform.order.model.model.BankOriginalDetail;
import com.prcsteel.platform.order.model.model.BankOriginalHeader;
import com.prcsteel.platform.order.model.model.BankTransactionInfo;
import com.prcsteel.platform.order.model.nido.NoteMessageContext;
import com.prcsteel.platform.order.persist.dao.BankOriginalDetailDao;
import com.prcsteel.platform.order.persist.dao.BankOriginalHeaderDao;
import com.prcsteel.platform.order.persist.dao.BankTransactionInfoDao;
import com.prcsteel.platform.order.service.AppPushService;
import com.prcsteel.platform.order.service.OrderCacheService;
import com.prcsteel.platform.order.service.payment.BankOriginalService;
import com.prcsteel.rest.bdl.payment.spdb.model.request.RequestBody;
import com.prcsteel.rest.bdl.payment.spdb.model.request.RequestHead;
import com.prcsteel.rest.bdl.payment.spdb.model.response.Body;
import com.prcsteel.rest.bdl.payment.spdb.model.response.List;

/**
 * Created by kongbinheng on 2015/7/21.
 */
@Service("bankOriginalService")
@Transactional
public class BankOriginalServiceImpl implements BankOriginalService {

    private static final Logger logger = LoggerFactory.getLogger(BankOriginalServiceImpl.class);

    @Value("${quartz.job.spdb.systemId}")
    private String systemId;
    @Value("${quartz.job.spdb.systemCode}")
    private String systemCode;

    @Resource
    private BankOriginalHeaderDao bankOriginalHeaderDao;
    @Resource
    private BankOriginalDetailDao bankOriginalDetailDao;
    @Resource
    private BankTransactionInfoDao bankTransactionInfoDao;
    @Resource
    private AccountDao accountDao;
    @Resource
    private AccountTransLogDao accountTransLogDao;
    @Resource
    private AccountBankDao accountBankDao;
    
    @Resource
    private AppPushService appPushService;
    @Resource
    private OrderCacheService orderCacheService;
    @Resource
    private UserDao userDao;
    @Resource
    private AccountFundService accountFundService;

    @Override
    public boolean saveBankOriginal(Body body, RequestHead requestHead, RequestBody requestBody, Integer beginNumber) {
        try{
            //设置银企直连返回头部数据
            //logger.info("spdb bdl data job execute6");
            BankOriginalHeader bankOriginalHeader = setBankOriginalHeader(body, requestHead, requestBody);
            //logger.info("spdb bdl data job execute7");
            try{
                bankOriginalHeaderDao.insertSelective(bankOriginalHeader);
               // logger.info("spdb bdl data job execute8");
            }catch (Exception e){
                logger.error("银企直连-保存返回头部数据错误：", e);
                return false;
            }
            //logger.info("spdb bdl data job execute9");
            List[] lists = (List[]) body.getLists().getList();
            for (int i = 0; i < lists.length; i++) {
                //logger.info("spdb bdl data job execute10");
                List list = lists[i];
                String tranFlag = list.getTranFlag(); //借贷标志0借1贷
                Date serialTime = Tools.strToDate(list.getTransDate() + Tools.leftFillZero(Integer.parseInt(list.getTransTime()), 6), "yyyyMMddHHmmss");  //交易时间
                BigDecimal txAmount = BigDecimal.valueOf(Double.parseDouble(list.getTxAmount()));  //交易金额
               // logger.info("spdb bdl data job execute11");
                //设置银企直连返回详细数据
                BankOriginalDetail bankOriginalDetail = setBankOriginalDetail(bankOriginalHeader.getId(), list, beginNumber, requestBody.getBeginDate());
                try{
                    bankOriginalDetailDao.insertSelective(bankOriginalDetail);
                   // logger.info("spdb bdl data job execute12");
                }catch (Exception e){
                    logger.error("银企直连-保存返回详细数据错误：", e);
                    return false;
                }

                //贷是增加金额时候才新增
                if(Constant.YES.equals(tranFlag)){
                    String transactionStatus = "";  //到账异常表状态
                    Account account = accountDao.selectAccountByName(Tools.toDBC(list.getPayeeName()));//客户名称全角转换到半角 modify by wangxianjun
                    if(null != account){
                        transactionStatus = BankTransactionType.NORMAL.getCode();//到账异常表状态是正常
                    }else{
                        transactionStatus = BankTransactionType.UNPROCESSED.getCode();//到账异常表状态是未处理
                    }
                    int totalError = bankOriginalDetailDao.totalErrorPay(list.getTxAmount(), Tools.toDBC(list.getPayeeName()), serialTime);//客户名称全角转换到半角 modify by wangxianjun
                    if(totalError > 0){
                        transactionStatus = BankTransactionType.PENDING.getCode();//疑似付款错误待处理
                    }
                    try{
                        //logger.info("spdb bdl data job execute13");
                        BankTransactionInfo bankTransactionInfo = setBankTransactionInfo(list, transactionStatus, txAmount, serialTime);//设置到账异常表
                        bankTransactionInfoDao.insertSelective(bankTransactionInfo);
                        //logger.info("spdb bdl data job execute14");
                        if(totalError > 0){
                            continue;
                        }
                        //logger.info("spdb bdl data job execute15");
                    }catch (Exception e){
                        logger.error("银企直连-保存到账异常表错误：", e);
                        return false;
                    }
                    if(null != account){
                        User user = new User();
                        user.setId(0l);
                        user.setName(Constant.SYSTEMNAME);
                        Boolean flag = saveAccountTransLog(account, list.getSeqNo(), list.getPayeeAcctNo(), list.getPayeeBankNo(), list.getPayeeBankName(), serialTime, txAmount, user);
                        if(!flag){
                            return false;
                        }
                        bankOriginalDetailDao.updateStatusById(bankOriginalDetail.getId(), Integer.parseInt(Constant.YES), new Date(), systemCode);
                    }
                }
                beginNumber ++;
            }
            return true;
        }catch (Exception e){
            logger.error("银企直连错误：", e);
            return false;
        }
    }

    /**
     * 更新和保存客户流水
     * @return
     */
    public Boolean saveAccountTransLog(Account account, String seqNo, String payeeAcctNo, String payeeBankNo, String payeeBankName, Date serialTime, BigDecimal txAmount, User operator){
        //查询对方户名与公司抬头是否匹配
        if(null != account){
            BigDecimal balance = new BigDecimal(0);
            balance = txAmount;
            Long accountId = account.getId();
            //设置客户账户流水表 modify by wangxianjun 防止流水重复
           /* AccountTransLog accountTransLog = setAccountTransLog(account, seqNo, serialTime, txAmount, balance);
            try{
                accountTransLogDao.insertSelective(accountTransLog);
            }catch (Exception e){
                logger.error("银企直连-保存客户账户流水表错误：", e);
                return false;
            }*/
            //更新公司现金余额
            //公司账户充值
            accountFundService.updateAccountFund(accountId, AssociationType.BANK_OF_SERIAL_NUMBER, seqNo,
                    AccountTransApplyType.CHARGE, txAmount, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                    BigDecimal.ZERO, PayType.BALANCE, operator.getId(), operator.getName(), new Date());

            //如果单部门就充值到部门 edit by rabbit
            java.util.List<DepartmentDto> departmentDtos = accountDao.queryDeptByCompanyId(account.getId());
            if(departmentDtos.size() == 1) {
                DepartmentDto department = departmentDtos.get(0);
                //公司账户转出
                accountFundService.updateAccountFund(accountId, AssociationType.BANK_OF_SERIAL_NUMBER, seqNo,
                        AccountTransApplyType.COMPANYMONEY_TRANSTO_DEPART, BigDecimal.ZERO.subtract(txAmount),
                        BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,  BigDecimal.ZERO,
                        PayType.BALANCE, operator.getId(), operator.getName(), new Date());
                //部门账户充值
                accountFundService.updateAccountFund(department.getId(), AssociationType.BANK_OF_SERIAL_NUMBER, seqNo,
                        AccountTransApplyType.CHARGE, txAmount, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                        BigDecimal.ZERO, BigDecimal.ZERO, PayType.BALANCE, operator.getId(), operator.getName(), new Date());
                //自动还款
                accountFundService.payForCredit(department.getId(), AssociationType.BANK_OF_SERIAL_NUMBER, seqNo,
                        0l, Constant.SYSTEMNAME, new Date());
            }
            //如果该笔银行账号在银行信息表不存在则新增一笔银行信息
            if (accountBankDao.totalBankAccountCode(payeeAcctNo) == 0) {
                //设置客户银行
                AccountBank accountBank = setAccountBank(accountId, payeeBankNo, payeeBankName, payeeAcctNo);
                try {
                    accountBankDao.insertSelective(accountBank);
                } catch (Exception e) {
                    logger.error("银企直连-保存客户银行错误：", e);
                    return false;
                }
            }
            //发送短信
            try{
                java.util.List<BankRechargeSMSDto> smsList = bankOriginalDetailDao.querySendSMS(account.getId());
                for(BankRechargeSMSDto smsDto : smsList){
                    String content = "已收到" + smsDto.getAccountName() + "货款" + balance + "元，请关联合同!";
                    Nido.start(NidoTaskConstant.SEND_NOTE_MESSAGE, new NoteMessageContext(smsDto.getUserTel(), content));
                    User user = userDao.queryById(smsDto.getUserId());
                    String[] pushInfo = orderCacheService.getPushInfoByUserLoginId(user.getLoginId());
                    if(pushInfo!=null){
                        content = String.format("已收到%s货款%s元，请关联合同。", smsDto.getAccountName(), NumberTool.toThousandth(balance));
                        appPushService.sendPushNoticfication(pushInfo[0],pushInfo[1], AppNoticeType.ChargeSuccess.getName(),content);
                    }
                }
            }catch (Exception e){
                logger.error("银企直连-发送短信错误：", e);
                return false;
            }
        }
        return true;
    }

    /**
     * 设置银企直连返回头部数据
     * @param body
     * @param requestHead
     * @param requestBody
     * @return
     */
    private BankOriginalHeader setBankOriginalHeader(Body body, RequestHead requestHead, RequestBody requestBody){
        String beginDate = requestBody.getBeginDate().substring(0, 4) + "-" + requestBody.getBeginDate().substring(5, 6) + "-" + requestBody.getBeginDate().substring(7, 8);
        String endDate = requestBody.getEndDate().substring(0, 4) + "-" + requestBody.getEndDate().substring(5, 6) + "-" + requestBody.getEndDate().substring(7, 8);
        BankOriginalHeader bankOriginalHeader = new BankOriginalHeader();
        bankOriginalHeader.setReqAcctno(requestBody.getAcctNo());  //账号
        bankOriginalHeader.setReqBegindate(Tools.strToDate(beginDate, "yyyy-MM-dd"));  //开始日期
        bankOriginalHeader.setReqEnddate(Tools.strToDate(endDate, "yyyy-MM-dd"));  //结束日期
        bankOriginalHeader.setReqQuerynumber(requestBody.getQueryNumber());  //查询的笔数
        bankOriginalHeader.setReqBeginnumber(requestBody.getBeginNumber());  //查询的起始笔数
        bankOriginalHeader.setReqTransamount(BigDecimal.valueOf(requestBody.getTransAmount()));  //交易金额
        bankOriginalHeader.setReqSubaccount(requestBody.getSubAccount());  //对方帐号
        bankOriginalHeader.setReqSubacctname(requestBody.getSubAcctName());  //对方户名
        bankOriginalHeader.setResTotalcount(Integer.parseInt(body.getTotalCount()));  //交易总笔数
        bankOriginalHeader.setResAcctno(body.getAcctNo());  //账号
        bankOriginalHeader.setResAcctname(body.getAcctName());  //账号名称
        bankOriginalHeader.setResCurrency(Integer.parseInt(body.getCurrency()));  //账户币种
        bankOriginalHeader.setCreated(new Date());
        bankOriginalHeader.setCreatedBy(systemCode);
        bankOriginalHeader.setLastUpdated(new Date());
        bankOriginalHeader.setLastUpdatedBy(systemCode);
        bankOriginalHeader.setModificationNumber(0);
        bankOriginalHeader.setExt1(requestHead.getPacketID());
        return bankOriginalHeader;
    }

    /**
     * set BankOriginalDetail
     * @param headerId
     * @param list
     * @param beginNumber
     * @param beginDate
     * @return
     */
    private BankOriginalDetail setBankOriginalDetail(Long headerId, List list, Integer beginNumber, String beginDate){
        BankOriginalDetail bankOriginalDetail = new BankOriginalDetail();
        bankOriginalDetail.setHeaderId(headerId);
        bankOriginalDetail.setVoucherno(list.getVoucherNo());  //凭证号
        bankOriginalDetail.setSeqno(list.getSeqNo());  //交易流水号
        bankOriginalDetail.setTxamount(list.getTxAmount());  //交易金额
        bankOriginalDetail.setBalance(list.getBalance());
        bankOriginalDetail.setTranflag(list.getTranFlag());
        bankOriginalDetail.setTransdate(list.getTransDate());
        bankOriginalDetail.setTranstime(list.getTransTime());
        bankOriginalDetail.setNote(list.getNote());
        bankOriginalDetail.setRemark(list.getRemark());
        bankOriginalDetail.setPayeebankno(list.getPayeeBankNo());  //对方行号
        bankOriginalDetail.setPayeebankname(list.getPayeeBankName());  //对方行名
        bankOriginalDetail.setPayeeacctno(list.getPayeeAcctNo());  //对方账号
        bankOriginalDetail.setPayeename(Tools.toDBC(list.getPayeeName()));  //对方户名  客户名称全角转换到半角 modify by wangxianjun
        bankOriginalDetail.setTranscode(list.getTransCode());
        bankOriginalDetail.setBranchid(list.getBranchId());
        bankOriginalDetail.setCustomeracctno(list.getCustomerAcctNo());
        bankOriginalDetail.setPayeeaccttype(list.getPayeeAcctType());
        bankOriginalDetail.setTranscounter(list.getTransCounter());
        bankOriginalDetail.setAuthcounter(list.getAuthCounter());
        bankOriginalDetail.setOtherchar10(list.getOtherChar10());
        bankOriginalDetail.setOtherchar40(list.getOtherChar40());
        bankOriginalDetail.setSeqnum(list.getSeqNum());
        bankOriginalDetail.setRevflag(list.getRevFlag());
        bankOriginalDetail.setBeginNumber(beginNumber);  //查询起始笔数
        bankOriginalDetail.setBeginDate(beginDate); //查询起始时间
        bankOriginalDetail.setCreated(new Date());
        bankOriginalDetail.setCreatedBy(systemCode);
        bankOriginalDetail.setLastUpdated(new Date());
        bankOriginalDetail.setLastUpdatedBy(systemCode);
        bankOriginalDetail.setModificationNumber(0);
        return bankOriginalDetail;
    }

    /**
     * 设置客户账户流水表
     * @param account
     * @param seqNo
     * @param serialTime
     * @param txAmount
     * @param balance
     * @return
     */
    private AccountTransLog setAccountTransLog(Account account, String seqNo, Date serialTime, BigDecimal txAmount, BigDecimal balance){
        //如果公司抬头匹配插入公司账目划转详情表
        AccountTransLog accountTransLog = new AccountTransLog();
        accountTransLog.setAccountId(account.getId());
        accountTransLog.setConsignOrderCode(seqNo);  //代运营单号
        accountTransLog.setSerialNumber(seqNo);
        accountTransLog.setApplyType(ApplyType.TO_CAPITAL_ACCOUNT.getCode());  //充值
        accountTransLog.setAmount(BigDecimal.ZERO);  //二次结算发生金额
        accountTransLog.setApplyerId(Long.parseLong(systemId));  //申请人id为系统
        accountTransLog.setApplyerName(Constant.SYSTEMNAME);  //申请人id为系统
        accountTransLog.setSerialTime(serialTime);
        accountTransLog.setType(account.getType());
        accountTransLog.setCurrentBalance(account.getBalanceSecondSettlement());  //二次结算当前金额
        accountTransLog.setPayType(PayType.BALANCE.getCode());  //余额类型
        accountTransLog.setAssociationType(AssociationType.BANK_OF_SERIAL_NUMBER.getCode());    //银行流水号
        accountTransLog.setCashHappenBalance(txAmount);//现金发生余额
        accountTransLog.setCashCurrentBalance(BigDecimal.valueOf(balance.doubleValue() + account.getBalance().doubleValue()));//现金当前余额
        accountTransLog.setCreated(new Date());
        accountTransLog.setCreatedBy(systemCode);
        accountTransLog.setLastUpdated(new Date());
        accountTransLog.setLastUpdatedBy(systemCode);
        accountTransLog.setModificationNumber(0);
        return accountTransLog;
    }

    /**
     * 设置客户银行
     * @param accountId
     * @param payeeBankNo
     * @param payeeBankName
     * @param payeeAcctNo
     * @return
     */
    private AccountBank setAccountBank(Long accountId, String payeeBankNo, String payeeBankName, String payeeAcctNo){
        if(StringUtils.isBlank(payeeBankNo)) payeeBankNo = "-";
        if(StringUtils.isBlank(payeeBankName)) payeeBankName = "-";
        if(StringUtils.isBlank(payeeAcctNo)) payeeAcctNo = "-";
        AccountBank accountBank = new AccountBank();
        accountBank.setAccountId(accountId);  //公司ID
        accountBank.setBankCode(payeeBankNo);  //开户行编号
        accountBank.setBankName(payeeBankName);  //开户行名称
        accountBank.setBankAccountCode(payeeAcctNo);  //银行账号
        accountBank.setCreated(new Date());
        accountBank.setCreatedBy(systemCode);
        accountBank.setLastUpdated(new Date());
        accountBank.setLastUpdatedBy(systemCode);
        accountBank.setModificationNumber(0);
        accountBank.setBankDataStatus(AccountBankDataStatus.Insufficient.getCode());//未上传 add by wangxianjun 状态为空，不能触发工作流
        return accountBank;
    }

    /**
     * 设置到账查询
     * @param list
     * @param transactionStatus
     * @param txAmount
     * @param serialTime
     * @return
     */
    private BankTransactionInfo setBankTransactionInfo(List list, String transactionStatus, BigDecimal txAmount, Date serialTime){
        String voucherNo = list.getVoucherNo();
        String payeeBankName = list.getPayeeBankName();
        String payeeName = Tools.toDBC(list.getPayeeName());//客户名称全角转换到半角 modify by wangxianjun
        String payeeAcctNo = list.getPayeeAcctNo();
        String payeeBankNo = list.getPayeeBankNo();
        if(StringUtils.isBlank(voucherNo)) voucherNo = "-";
        if(StringUtils.isBlank(payeeBankName)) payeeBankName = "-";
        if(StringUtils.isBlank(payeeName)) payeeName = "-";
        if(StringUtils.isBlank(payeeAcctNo)) payeeAcctNo = "-";
        if(StringUtils.isBlank(payeeBankNo)) payeeBankNo = "-";
        BankTransactionInfo bankTransactionInfo = new BankTransactionInfo();
        bankTransactionInfo.setTransactionNumber(voucherNo);
        bankTransactionInfo.setSerialNumber(list.getSeqNo());
        bankTransactionInfo.setPayeeBankName(payeeBankName);
        bankTransactionInfo.setBankCode(payeeBankNo);
        bankTransactionInfo.setPayeeName(payeeName);
        bankTransactionInfo.setPayeeAcctountNumber(payeeAcctNo);
        bankTransactionInfo.setTransactionAmount(txAmount);
        bankTransactionInfo.setTransactionTime(serialTime);
        bankTransactionInfo.setNote(list.getNote());
        bankTransactionInfo.setIsRelated(false);
        bankTransactionInfo.setStatus(transactionStatus);
        bankTransactionInfo.setBankType(BankType.SPDB.getCode().toString());//浦发银行
        bankTransactionInfo.setCreated(new Date());
        bankTransactionInfo.setCreatedBy(systemCode);
        bankTransactionInfo.setLastUpdated(new Date());
        bankTransactionInfo.setLastUpdatedBy(systemCode);
        bankTransactionInfo.setModificationNumber(0);
        return bankTransactionInfo;
    }

    /**
     * 查询起始笔数
     * @param beginDate
     * @return
     */
    @Override
    public int selectBeginNumber(String beginDate) {
        return bankOriginalDetailDao.selectBeginNumber(beginDate);
    }

    /**
     * 查询起初余额
     * @param date
     * @return
     */
    @Override
    public BigDecimal getPreBlance(String date){
        if(StringUtils.isEmpty(date))
        {
            return BigDecimal.ZERO;
        }
        BigDecimal balance=bankOriginalDetailDao.queryPreBlance(date);
        if (balance == null) {
            balance=BigDecimal.ZERO;
        }
        return balance;
    }

    /**
     * 查询期末余额
     * @return
     */
    @Override
    public BigDecimal getLastBlance(String date){
        if(StringUtils.isEmpty(date))
        {
            return BigDecimal.ZERO;
        }
        BigDecimal balance=bankOriginalDetailDao.queryLastBlance(date);
        if (balance == null) {
            balance=BigDecimal.ZERO;
        }
        return balance;
    }
    
    @Override
    public BankOriginalDto queryBankReceipts(BigDecimal txAmount, String payeeName, String payeeAcctNo) {
		return bankOriginalDetailDao.queryBankReceipts(txAmount, payeeName.replaceAll("[\\s\\-]*", ""), payeeAcctNo.replaceAll("[\\s\\-]*", ""));
    }
}
