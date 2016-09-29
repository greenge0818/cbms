package com.prcsteel.platform.kuandao.service.impl;

import java.util.Calendar;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.prcsteel.platform.kuandao.common.constant.SPDBTransNameConstant;
import com.prcsteel.platform.kuandao.common.util.DateUtils;
import com.prcsteel.platform.kuandao.common.util.SpdbHttpsPost;
import com.prcsteel.platform.kuandao.model.dto.spdb.DailyBillTransaction;
import com.prcsteel.platform.kuandao.model.dto.spdb.SPDBRequstParam;
import com.prcsteel.platform.kuandao.model.dto.spdb.SPDBResponseResult;
import com.prcsteel.platform.kuandao.service.DailyBillService;

@Service("dailyBillService")
public class DailyBillServiceImpl implements DailyBillService{
	@Resource
	private SpdbHttpsPost spdbHttpsPost;

	@Value("${kuandao.mercCode}")
	private String mercCode;
	
	//日终对账单查询
	public SPDBResponseResult queryDailyBill(){
		//构造交易参数
		String tranAbbr=SPDBTransNameConstant.LSXZ;
		DailyBillTransaction dybillTransaction=new DailyBillTransaction();
		dybillTransaction.setTranAbbr(tranAbbr);
		dybillTransaction.setMercCode(mercCode);
		
		Calendar calendar =Calendar.getInstance();
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		calendar.set(Calendar.DAY_OF_MONTH,day - 1);
		Date date=new Date(calendar.getTimeInMillis());
		String osttDate=DateUtils.getPlainDate(date);
		dybillTransaction.setoSttDate(osttDate);
		
		SPDBRequstParam spdbequstParam = new SPDBRequstParam(tranAbbr,dybillTransaction.toString());
		return spdbHttpsPost.post(spdbequstParam);
	}
}
