package com.prcsteel.platform.kuandao.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.prcsteel.platform.kuandao.common.util.DateUtils;
import com.prcsteel.platform.kuandao.model.dto.KuandaoDailyBillDto;
import com.prcsteel.platform.kuandao.model.dto.spdb.SPDBResponseResult;
import com.prcsteel.platform.kuandao.model.enums.KuandaoResultEnum;
import com.prcsteel.platform.kuandao.model.model.KuandaoDailyBill;
import com.prcsteel.platform.kuandao.persist.dao.KuandaoDailyBillDao;
import com.prcsteel.platform.kuandao.service.DailyBillService;
import com.prcsteel.platform.kuandao.service.KuanDaoProxyService;
import com.prcsteel.platform.kuandao.service.KuandaoDailyBillService;
@Service("kuandaoDailyBillService")
public class KuandaoDailyBillServiceImpl implements KuandaoDailyBillService {

	private static final Logger logger = LoggerFactory.getLogger(KuandaoDailyBillServiceImpl.class);

	@Resource(type=DailyBillService.class)
	private DailyBillService dailyBillService;
	
	@Resource(type=KuandaoDailyBillDao.class)
	private KuandaoDailyBillDao kuandaoDailyBillDao;
	
	@Resource
	private KuanDaoProxyService kuanDaoProxyService;
	
	//查询日终对账单总数total
	public Integer queryTotalDailyBill(KuandaoDailyBillDto dto){
		return kuandaoDailyBillDao.queryTotalDailyBill(dto);
	}
	//查询日终对账单明细detail
	public List<KuandaoDailyBillDto> queryDailyBill(KuandaoDailyBillDto dto,Integer start,Integer length){
		Map<String,Object> param=new HashMap<>();
		param.put("startDate", dto.getStartDate());
		param.put("endDate", dto.getEndDate());
		param.put("payeeVirtualAcctName",dto.getPayeeVirtualAcctName());
		param.put("payMerName",dto.getPayMerName());
		param.put("paymentOrderCode",dto.getPaymentOrderCode());
		param.put("paymentStatus", dto.getPaymentStatus());
		param.put("start", start);
		param.put("length",length);
		return kuandaoDailyBillDao.queryDailyBillByCondition(param);
	}
	//日终对账单落地入口
	@Override
	public Integer insertDailyBill(String userName){
		int resultCode;
		SPDBResponseResult result=kuanDaoProxyService.queryDailyBill();
		if(result == null){
			resultCode= KuandaoResultEnum.systemerror.getCode();
			logger.error(String.format("%s日,查询浦发前一日日终对账单失败",DateUtils.getPlainDate()));
		}else if(result.isSuccess()){
			resultCode=KuandaoResultEnum.success.getCode();
			String plain=result.getPlain();
			List<KuandaoDailyBill> list=this.plainToBean(plain);
			for(KuandaoDailyBill dailyBill:list){
				dailyBill.setCreated(new Date());
				dailyBill.setLastUpdated(new Date());
				dailyBill.setCreatedBy(userName);
				dailyBill.setLastUpdatedBy(userName);
				int z=this.insertDailyBill(dailyBill);
				if(z!=1){
					resultCode = KuandaoResultEnum.dataoperateerror.getCode();
					logger.error(String.format("%s日,落地前一日日终对账单存在失败，对应汇入流水：%s",DateUtils.getPlainDate(),dailyBill.getImpAcqSsn()));
				}
			}
		}else{
			resultCode = KuandaoResultEnum.systemerror.getCode();
			logger.error(String.format("%s日,查询浦发前一日日终对账单失败",DateUtils.getPlainDate(new Date())));
		}
		return resultCode;	
	}
	//浦发反馈日终对账单明文信息封装
	private List<KuandaoDailyBill> plainToBean(String plain){
		String[] entrys=plain.split("=");
		String value=entrys[1];
		String[] msgs=value.split("\r\n");
		KuandaoDailyBill dailyBill;
		List<KuandaoDailyBill> list=new ArrayList<>();
		for(int i = 0;i < msgs.length - 1; i++){
			String msg = msgs[i];
			dailyBill=new KuandaoDailyBill();
			String[] words=msg.split("\\|");
			dailyBill.setImpAcqSsn(words[0]);
			dailyBill.setPayeeAcctNo(words[1]);
			dailyBill.setPayeeVirtualAcctNo(words[2]);
			dailyBill.setPayeeVirtualAcctName(words[3]);
			dailyBill.setIsDebit(words[4]);
			dailyBill.setTellerId(words[5]);
			String transactionAmount = words[6];
			if(StringUtils.isEmpty(transactionAmount)){
				transactionAmount = "0";
			}
			dailyBill.setTransactionAmount(new BigDecimal(transactionAmount));
			String balance = words[7];
			if(StringUtils.isEmpty(balance)){
				balance = "0";
			}
			dailyBill.setBalance(new BigDecimal(balance));
			dailyBill.setVoucherNo(words[8]);
			dailyBill.setImpDate(words[9]);
			dailyBill.setTellerSerialNo(words[10]);
			dailyBill.setDigestCode(words[11]);
			dailyBill.setPayMerName(words[12]);
			dailyBill.setPayMerAcctNo(words[13]);
			dailyBill.setPayMerBranchId(words[14]);
			dailyBill.setPaymentOrderCode(words[15]);
			dailyBill.setCorrectionEntriesMark(words[16]);
			dailyBill.setPaymentStatus(words[17]);
			if(words.length > 18){
				dailyBill.setVirtualShareDate(words[18]);
			}
			list.add(dailyBill);
		}
		return list;
	}
	//日终对账单落入本地
	private Integer insertDailyBill(KuandaoDailyBill kuandaoDailyBill){
		return kuandaoDailyBillDao.insertDailyBill(kuandaoDailyBill);
	}
}
