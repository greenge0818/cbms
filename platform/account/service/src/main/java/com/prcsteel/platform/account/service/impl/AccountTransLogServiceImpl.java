package com.prcsteel.platform.account.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.prcsteel.platform.account.model.dto.AccountInAndOutDto;
import com.prcsteel.platform.account.model.dto.AccountTransLogDto;
import com.prcsteel.platform.account.model.enums.AccountTransApplyType;
import com.prcsteel.platform.account.model.model.AccountTransLog;
import com.prcsteel.platform.account.model.query.AccountInAndOutQuery;
import com.prcsteel.platform.account.model.query.AccountInfoFlowSearchQuery;
import com.prcsteel.platform.account.persist.dao.AccountTransLogDao;
import com.prcsteel.platform.account.service.AccountTransLogService;
import com.prcsteel.platform.common.utils.ObjectExcelView;
import com.prcsteel.platform.common.utils.PageData;
import com.prcsteel.platform.common.utils.Tools;
import com.prcsteel.platform.order.model.enums.AssociationType;
import com.prcsteel.platform.order.model.enums.PayType;

/**
 * Created by kongbinheng on 2015/7/16.
 */
@Service("accountTransLogService")
public class AccountTransLogServiceImpl implements AccountTransLogService {

    @Autowired
    private AccountTransLogDao accountTransLogDao;
    
    /******金额小数点后预留位数 ****/
    private static final int AMOUNT_LIMIT = 2;

    @Override
    public List<AccountTransLogDto> queryTransLogByAccountId(Long accountId, String consignOrderCode, String applyType,
                                                             String startTime, String endTime, Integer start, Integer length) {
        return accountTransLogDao.queryTransLogByAccountId(accountId, consignOrderCode, applyType, startTime, endTime, start, length);
    }

    @Override
    public int totalTransLog(Long accountId, String consignOrderCode, String applyType, String startTime, String endTime) {
        return accountTransLogDao.totalTransLog(accountId, consignOrderCode, applyType, startTime, endTime);
    }

    /**
     * 查询指定条件的资金往来
     *
     * @param query
     * @return
     */
    @Override
    public List<AccountInAndOutDto> queryAccountInAndOut(AccountInAndOutQuery query) {
        return accountTransLogDao.queryAccountInAndOut(query);
    }

    /**
     * 查询指定条件的资金往来总数
     *
     * @param query
     * @return
     */
    @Override
    public int countAccountInAndOut(AccountInAndOutQuery query) {
        return accountTransLogDao.countAccountInAndOut(query);
    }

    /**
     * 查询指定条件的资金往来合计
     *
     * @param query
     * @return
     */
    @Override
    public List<AccountInAndOutDto> queryAccountInAndOutTotal(AccountInAndOutQuery query) {
        return accountTransLogDao.queryAccountInAndOutTotal(query);
    }

    /**
     * 查询起初余额
     *
     * @param query
     * @return
     */
    @Override
    public BigDecimal queryPreBlance(AccountInAndOutQuery query) {
        return accountTransLogDao.queryPreBlance(query);
    }

    /**
     * 查询期末余额
     *
     * @param query
     * @return
     */
    @Override
    public BigDecimal queryLastBlance(AccountInAndOutQuery query) {
        BigDecimal balance = accountTransLogDao.queryLastBlance(query);
        if (balance == null) {
            balance = BigDecimal.ZERO;
        }
        return balance;
    }
    
    /**
     * 根据查询对象查询账户流水信息
     * @param accountInfoFlowSearchQuery
     * @return
     */
	@Override
	public List<AccountTransLogDto> searchFlowByQuery(AccountInfoFlowSearchQuery accountInfoFlowSearchQuery) {
		if(accountInfoFlowSearchQuery==null||accountInfoFlowSearchQuery.getAccountId()==null){
			return null;
		}
		Long departmentId=accountInfoFlowSearchQuery.getDepartmentId();
		//部门id不为空时查询部门的流水
		if(departmentId!=null){
			accountInfoFlowSearchQuery.setAccountId(departmentId);
		}
		List<AccountTransLogDto> list=accountTransLogDao.searchFlowByQuery(accountInfoFlowSearchQuery);
		//关联类型转换
		list.forEach(e->e.setAssociationType(AssociationType.getNameByCode(e.getAssociationType())));
		//类型转换
		list.forEach(e->e.setApplyType(AccountTransApplyType.getName(e.getApplyType())));
		return list;
	}
	
	/**
	 * 构建导出流水excel
	 */
	@Override
	public ModelAndView doExportExcel(AccountInfoFlowSearchQuery accountInfoFlowSearchQuery) {
		List<AccountTransLogDto> list= searchFlowByQuery(accountInfoFlowSearchQuery);
		/* if(list!=null && list.size()>0){
			 //除去最后一行统计数据
			 list.remove(list.size()-1);
		 }*/
        Map<String, Object> dataMap = new HashMap<>();
        List<String> titles = new ArrayList<>();
        titles.add("流水时间");
        titles.add("关联类型");
        titles.add("关联单号");
        titles.add("类型");
        titles.add("资金账户发生额");
        titles.add("资金账户余额");
        titles.add("二次结算账户发生额");
        titles.add("二次结算账户余额");
        titles.add("信用额度发生额");
        titles.add("信用额度余额");
        titles.add("操作员");
        dataMap.put("titles", titles);
        List<PageData> varList = new ArrayList<>();
        PageData vpd = null;
        if(list!=null && list.size()>0){
	        for (AccountTransLogDto transLogDto : list) {
	            vpd = new PageData();
	            vpd.put("var1", Tools.dateToStr(transLogDto.getSerialTime(), "yyyy-MM-dd HH:mm:ss"));//流水时间
	            vpd.put("var2", transLogDto.getAssociationType());//关联类型
	            vpd.put("var3", transLogDto.getConsignOrderCode());//关联单号
	            vpd.put("var4", transLogDto.getApplyType());//类型
	            vpd.put("var5", Tools.formatBigDecimal(transLogDto.getCashHappenBalance(), AMOUNT_LIMIT));//资金账户发生额
	            vpd.put("var6", Tools.formatBigDecimal(transLogDto.getCashCurrentBalance(), AMOUNT_LIMIT));//资金账户余额
	            vpd.put("var7", Tools.formatBigDecimal(transLogDto.getAmount(), AMOUNT_LIMIT));//二次结算账户发生额
	            vpd.put("var8", Tools.formatBigDecimal(transLogDto.getCurrentBalance(), AMOUNT_LIMIT));//二次结算账户余额
	            vpd.put("var9", Tools.formatBigDecimal(transLogDto.getCredit(), AMOUNT_LIMIT));//信用额度发生额
	            vpd.put("var10", Tools.formatBigDecimal(transLogDto.getCreditBalance(), AMOUNT_LIMIT));//信用额度余额
	            vpd.put("var11", transLogDto.getApplyerName());//操作员
	            varList.add(vpd);
	        }
        }
        dataMap.put("varList", varList);
        ObjectExcelView erv = new ObjectExcelView();//执行excel操作
		return  new ModelAndView(erv, dataMap);
	}

	@Override
	public List<AccountTransLogDto> getAccountInfoSettlementFlows(
			AccountInfoFlowSearchQuery accountInfoFlowSearchQuery) {
		//这里的accountId实际上就是部门id
		if(accountInfoFlowSearchQuery==null||accountInfoFlowSearchQuery.getAccountId()==null){
			return null;
		}
		accountInfoFlowSearchQuery.setPayType(PayType.SETTLEMENT.toString());
		List<AccountTransLogDto> list=accountTransLogDao.searchFlowByQuery(accountInfoFlowSearchQuery);
		//关联类型转换
		list.forEach(e->e.setAssociationType(AssociationType.getNameByCode(e.getAssociationType())));
		//类型转换
		list.forEach(e->e.setApplyType(AccountTransApplyType.getName(e.getApplyType())));
		return list;
	}

    /**
     * 根据查询对象统计账户流水信息
     * @param accountInfoFlowSearchQuery
     * @return
     */
    @Override
    public int countFlowByQuery(AccountInfoFlowSearchQuery accountInfoFlowSearchQuery){
        if(accountInfoFlowSearchQuery==null||accountInfoFlowSearchQuery.getAccountId()==null){
            return 0;
        }
        Long departmentId=accountInfoFlowSearchQuery.getDepartmentId();
        //部门id不为空时查询部门的流水
        if(departmentId!=null){
            accountInfoFlowSearchQuery.setAccountId(departmentId);
        }
        return accountTransLogDao.countFlowByQuery(accountInfoFlowSearchQuery);
    }

	@Override
	public List<AccountTransLog> queryFlowByQuery(AccountInfoFlowSearchQuery accountInfoFlowSearchQuery) {
		if(accountInfoFlowSearchQuery==null||accountInfoFlowSearchQuery.getAccountId()==null){
            return new ArrayList<AccountTransLog>();
        }
		return accountTransLogDao.queryFlowByQuery(accountInfoFlowSearchQuery);
	}
}