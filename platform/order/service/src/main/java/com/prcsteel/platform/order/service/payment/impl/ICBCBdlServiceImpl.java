package com.prcsteel.platform.order.service.payment.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import com.prcsteel.platform.acl.model.model.User;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prcsteel.cxfrs.service.bdl.model.history.Rd;
import com.prcsteel.platform.common.utils.Tools;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.order.model.dto.IcbcBdDto;
import com.prcsteel.platform.order.model.enums.ApplyType;
import com.prcsteel.platform.order.model.enums.AssociationType;
import com.prcsteel.platform.order.model.enums.BankTransactionType;
import com.prcsteel.platform.order.model.enums.BankType;
import com.prcsteel.platform.order.model.enums.PayType;
import com.prcsteel.platform.account.model.model.Account;
import com.prcsteel.platform.account.model.model.AccountBank;
import com.prcsteel.platform.account.model.model.AccountTransLog;
import com.prcsteel.platform.order.model.model.BankTransactionInfo;
import com.prcsteel.platform.order.model.model.IcbcBdlDetail;
import com.prcsteel.platform.account.persist.dao.AccountBankDao;
import com.prcsteel.platform.account.persist.dao.AccountDao;
import com.prcsteel.platform.account.persist.dao.AccountTransLogDao;
import com.prcsteel.platform.order.persist.dao.BankOriginalDetailDao;
import com.prcsteel.platform.order.persist.dao.BankTransactionInfoDao;
import com.prcsteel.platform.order.persist.dao.IcbcBdlDetailDao;
import com.prcsteel.platform.order.persist.dao.IcbcBdlHeaderDao;
import com.prcsteel.platform.order.service.payment.BankOriginalService;
import com.prcsteel.platform.order.service.payment.BdlService;

/**
 * Created by kongbinheng on 2015/7/28.
 */
@Service("icbcBdlService")
@Transactional
public class ICBCBdlServiceImpl implements BdlService {

    private static final Logger logger = LoggerFactory.getLogger(ICBCBdlServiceImpl.class);
    private static final String QRY_DRCRF = "2"; //转入

    @Value("${quartz.job.spdb.systemId}")
    private String systemId;
    @Value("${quartz.job.spdb.systemCode}")
    private String systemCode;

    @Resource
    private IcbcBdlHeaderDao icbcBdlHeaderDao;
    @Resource
    private IcbcBdlDetailDao icbcBdlDetailDao;
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
    private BankOriginalService bankOriginalService;

    @Override
    public boolean saveQuery(List<Object> list, String nextTag) {
        try{
            //logger.info("icbc bdl data job execute5");
            if(list != null){
                //logger.info("icbc bdl data job execute6");
                for(Object obj: list){
                    Rd rd = (Rd) obj;
                    //logger.info("icbc bdl data job execute7:" + icbcBdlDetailDao.selectSequenceNo(rd.getSequenceNo()) + ",序列号："+rd.getSequenceNo());
                    if(icbcBdlDetailDao.selectSequenceNo(rd.getSequenceNo()) == 0 && !StringUtils.isBlank(rd.getSequenceNo())){
                        //设置银企直连返回详细数据
                        //logger.info("icbc bdl data job execute8");
                        IcbcBdlDetail icbcBdlDetail = setIcbcBdlDetail(rd, nextTag);
                        try{
                            //logger.info("icbc bdl data job execute9");
                            icbcBdlDetailDao.insertSelective(icbcBdlDetail);
                            //logger.info("icbc bdl data job execute10");
                        }catch (Exception e){
                            logger.error("工行银企直连-保存工行银企直连返回详细数据错误：", e);
                            return false;
                        }
                        //转入
                        if(QRY_DRCRF.equals(rd.getDrcrf())){
                            BigDecimal txAmount = BigDecimal.valueOf(Double.parseDouble(fromFenToYuan(rd.getAmount())));  //交易金额
                            Date serialTime = Tools.strToDate(rd.getTimeStamp().substring(0,10) + " " + rd.getTimeStamp().substring(11, 13) + ":" + rd.getTimeStamp().substring(14,16) + ":" + rd.getTimeStamp().substring(17,19), "yyyy-MM-dd HH:mm:ss");  //交易时间
                            String transactionStatus = "";//到账异常表状态
                            Account account = accountDao.selectAccountByName(Tools.toDBC(rd.getRecipName()));//客户表 ,客户名称全角转换到半角 modify by wangxianjun
                            //查询对方户名与公司抬头是否匹配
                            if(null != account){
                                transactionStatus = BankTransactionType.NORMAL.getCode();//到账异常表状态是正常
                            }else{
                                transactionStatus = BankTransactionType.UNPROCESSED.getCode();//到账异常表状态是未处理
                            }
                            int totalError = icbcBdlDetailDao.totalErrorPay(rd.getAmount(), Tools.toDBC(rd.getRecipName()), serialTime);//客户名称全角转换到半角 modify by wangxianjun
                            if(totalError > 0){
                                transactionStatus = BankTransactionType.PENDING.getCode();//疑似付款错误待处理
                            }
                            try{
                                //设置到账异常表
                                BankTransactionInfo bankTransactionInfo = setBankTransactionInfo(rd, transactionStatus, txAmount, serialTime);
                                bankTransactionInfoDao.insertSelective(bankTransactionInfo);
                                if(totalError > 0){
                                    continue;
                                }
                            }catch (Exception e){
                                logger.error("工行银企直连-保存到账异常表错误：", e);
                                return false;
                            }
                            if(null != account){
                                User user = new User();
                                user.setId(0l);
                                user.setName(Constant.SYSTEMNAME);
                                Boolean flag = bankOriginalService.saveAccountTransLog(account, rd.getSequenceNo(), rd.getRecipAccNo(), rd.getRecipBkNo(), "", serialTime, txAmount, user);
                                if(!flag){
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
            return true;
        }catch (Exception e){
            logger.error("工行银企直连错误：", e);
            return false;
        }
    }

    /**
     * 设置工行银企直连返回明细表数据
     * @param rd
     * @return
     */
    private IcbcBdlDetail setIcbcBdlDetail(Rd rd, String nextTag){
        IcbcBdlDetail icbcBdlDetail = new IcbcBdlDetail();
        try {
            Date timeStamp = Tools.strToDate(rd.getTimeStamp().substring(0, 10) + " " + rd.getTimeStamp().substring(11, 13) + ":" + rd.getTimeStamp().substring(14, 16) + ":" + rd.getTimeStamp().substring(17, 19), "yyyy-MM-dd HH:mm:ss");  //交易时间
            if(StringUtils.isBlank(nextTag)) nextTag = "";
            icbcBdlDetail.setDrcrf(rd.getDrcrf());
            icbcBdlDetail.setVouhNo(rd.getVouhNo());
            icbcBdlDetail.setCreditAmount(rd.getAmount());
            icbcBdlDetail.setRecipBkNo(rd.getRecipBkNo());
            icbcBdlDetail.setRecipAccNo(rd.getRecipAccNo());
            icbcBdlDetail.setRecipName(Tools.toDBC(rd.getRecipName()));//客户名称全角转换到半角 modify by wangxianjun
            icbcBdlDetail.setSummary(rd.getSummary());
            icbcBdlDetail.setUseCN(rd.getUseCN());
            icbcBdlDetail.setPostScript(rd.getPostScript());
            icbcBdlDetail.setRef(rd.getRef());
            icbcBdlDetail.setBusCode(rd.getBusCode());
            icbcBdlDetail.setOref(rd.getOref());
            icbcBdlDetail.setEnSummary(rd.getEnSummary());
            icbcBdlDetail.setBusType(rd.getBusType());
            icbcBdlDetail.setAddInfo(rd.getAddInfo());
            icbcBdlDetail.setTimeStamp(timeStamp);
            icbcBdlDetail.setRepReserved3(rd.getRepReserved3());
            icbcBdlDetail.setRepReserved4(rd.getRepReserved4());
            icbcBdlDetail.setUpDtranf(rd.getUpDtranf());
            icbcBdlDetail.setValueDate(rd.getValueDate());
            icbcBdlDetail.setTrxCode(rd.getTrxCode());
            icbcBdlDetail.setSequenceNo(rd.getSequenceNo());
            icbcBdlDetail.setCashf(rd.getCashf());
            icbcBdlDetail.setSubAcctSeq(rd.getSubAcctSeq());
            icbcBdlDetail.settHCurrency(rd.gettHCurrency());
            icbcBdlDetail.setNextTag(nextTag);
            icbcBdlDetail.setStatus(0);//未处理
            icbcBdlDetail.setCreated(new Date());
            icbcBdlDetail.setCreatedBy(systemCode);
            icbcBdlDetail.setLastUpdated(new Date());
            icbcBdlDetail.setLastUpdatedBy(systemCode);
            icbcBdlDetail.setModificationNumber(0);
        } catch (Exception e) {
            logger.error("设置工行银企直连返回明细表数据出错", e);
        }
        return icbcBdlDetail;
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
        return accountBank;
    }

    /**
     * 设置到账查询
     * @param rd
     * @param transactionStatus
     * @param txAmount
     * @param serialTime
     * @return
     */
    private BankTransactionInfo setBankTransactionInfo(Rd rd, String transactionStatus, BigDecimal txAmount, Date serialTime){
        String voucherNo = rd.getSequenceNo();
        String payeeBankName = "-";
        String payeeName = Tools.toDBC(rd.getRecipName());//客户名称全角转换到半角 modify by wangxianjun
        String payeeAcctNo = rd.getRecipAccNo();
        String payeeBankNo = rd.getRecipBkNo();
        if(StringUtils.isBlank(voucherNo)) voucherNo = "-";
        if(StringUtils.isBlank(payeeBankName)) payeeBankName = "-";
        if(StringUtils.isBlank(payeeName)) payeeName = "-";
        if(StringUtils.isBlank(payeeAcctNo)) payeeAcctNo = "-";
        if(StringUtils.isBlank(payeeBankNo)) payeeBankNo = "-";
        BankTransactionInfo bankTransactionInfo = new BankTransactionInfo();
        bankTransactionInfo.setTransactionNumber(voucherNo);
        bankTransactionInfo.setSerialNumber(rd.getSequenceNo());
        bankTransactionInfo.setPayeeBankName(payeeBankName);
        bankTransactionInfo.setBankCode(payeeBankNo);
        bankTransactionInfo.setPayeeName(payeeName);
        bankTransactionInfo.setPayeeAcctountNumber(payeeAcctNo);
        bankTransactionInfo.setTransactionAmount(txAmount);
        bankTransactionInfo.setTransactionTime(serialTime);
        bankTransactionInfo.setNote(rd.getUseCN());
        bankTransactionInfo.setIsRelated(false);
        bankTransactionInfo.setStatus(transactionStatus);
        bankTransactionInfo.setBankType(BankType.ICBC.getCode().toString());//工商银行
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
    public int queryBeginNumber(String beginDate, String endDate) {
        return icbcBdlDetailDao.selectBeginNumber(beginDate, endDate);
    }

    @Override
    public String queryNextTag(String beginDate) {
        return icbcBdlDetailDao.selectNextTag(beginDate);
    }

    /**
     * 分转换为元.
     * @param fen 分
     * @return 元
     */
    private String fromFenToYuan(String fen) {
        String yuan = "";
        final int MULTIPLIER = 100;
        Pattern pattern = Pattern.compile("^[1-9][0-9]*{1}");
        Matcher matcher = pattern.matcher(fen);
        if (matcher.matches()) {
            yuan = new BigDecimal(fen).divide(new BigDecimal(MULTIPLIER)).setScale(2).toString();
        } else {
            logger.error("数据:"+fen+"转成元格式不正确!");
            //System.out.println("参数格式不正确!");
        }
        return yuan;
    }

	@Override
	public IcbcBdDto queryBankReceipts(BigDecimal creditAmount, String recipName, String recipAccNo) {
		return icbcBdlDetailDao.queryBankReceipts(creditAmount, recipName.replaceAll("[\\s\\-]*", ""), recipAccNo.replaceAll("[\\s\\-]*", ""));
	}

}
