package com.prcsteel.platform.order.service.rebate.impl;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;


import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.order.model.model.*;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.prcsteel.platform.account.model.model.Account;
import com.prcsteel.platform.account.persist.dao.AccountDao;
import com.prcsteel.platform.acl.model.model.Rebate;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.acl.persist.dao.OrganizationDao;
import com.prcsteel.platform.acl.persist.dao.UserDao;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.enums.OperateStatus;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.core.model.dto.CategoryGroupDto;
import com.prcsteel.platform.core.persist.dao.CategoryGroupDao;
import com.prcsteel.platform.core.persist.dao.ProvinceDao;
import com.prcsteel.platform.order.model.dto.RebateDto;
import com.prcsteel.platform.order.model.enums.RebateStatus;

import com.prcsteel.platform.order.model.model.ConsignOrder;
import com.prcsteel.platform.order.model.model.ConsignOrderItems;
import com.prcsteel.platform.order.model.model.ReportRebateRecord;

import com.prcsteel.platform.order.persist.dao.ConsignOrderDao;
import com.prcsteel.platform.order.persist.dao.ConsignOrderItemsDao;
import com.prcsteel.platform.order.persist.dao.RebateDao;
import com.prcsteel.platform.order.persist.dao.ReportRebateRecordDao;
import com.prcsteel.platform.order.service.FinanceService;
import com.prcsteel.platform.order.service.rebate.RebateService;

/**
 * Created by chenchen on 2015/8/3.
 */
@Service("rebateService")
public class RebateServiceImpl implements RebateService {
	
	private static final Logger logger = Logger.getLogger(RebateServiceImpl.class);
	
	@Value("${ivFinanceService}")
    private String ivFinanceServiceAddress;  // 接口服务地址
	
    @Value("${ivFinanceServiceKEY}")
    private String ivFinanceServiceKEY; //接口key
    
    @Resource
	private RebateDao rebateDao;
	
	@Resource
	private CategoryGroupDao categoryGroupDao;
	
	@Resource
	private ProvinceDao provinceDao;
	
	@Resource
	private OrganizationDao organizationDao;
	
	@Resource
	private ReportRebateRecordDao reportRebateRecordDao;
	
	@Resource
	private ConsignOrderItemsDao consignOrderItemsDao;
	
	@Resource 
	private AccountDao accountDao;
	
	@Resource
	private UserDao userDao;
	
	@Resource 
	private ConsignOrderDao consignOrderDao;

	@Override
	public int addRebate(Rebate rebate) {
		Integer flag = OperateStatus.FAIL.ordinal();
		rebate.setCreated(new Date());
		rebate.setLastUpdated(new Date());
		rebate.setModificationNumber(0);
		/*
		 * 设置下个月1号生效
		 */
		rebate.setEffectiveTime(calcDate());
		if (rebateDao.insert(rebate) > 0) {
			flag = OperateStatus.SUCCESS.ordinal();
		} else {
			flag = OperateStatus.DUPLICATE.ordinal();
		}
		return flag;
	}
	
	private Date calcDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.add(Calendar.MONTH, 1);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTime();
	}

	@Override
	public int cancleEffectRebate() {
		return rebateDao.cancelRebateEffect();
	}

	@Override
	public List<Rebate> getAllRebate() {
		Rebate rebate = new Rebate();
		rebate.setRebateStatus(RebateStatus.EFFECT.getName());
		return rebateDao.queryAll(rebate);
	}

	@Override
	public int refleshRebate(List<Rebate> rebateList) {
		
		int flag=this.cancleEffectRebate();
		for (Rebate rebate : rebateList) {
			this.addRebate(rebate) ;
		}
		return flag;
	}

	@Override
	public List<RebateDto> getAllRebateDto() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("rebateStatus", RebateStatus.EFFECT.getName());
		return this.rebateDao.queryRebateDto(paramMap);
	}

	@Override
	public void startNewRebate() {
		try {
			this.rebateDao.expireOldRebate();
			this.rebateDao.startNewRebate();
		} catch (Exception e) {
			logger.error("start new rebate error");
			
		}
		
	}

	@Override
	public int countNextMonthEffectRecord() {
		
		return rebateDao.countNextMonthEffectRecord();
	}

	public void addRebateByOrder(Long orderId,User user,Long sellerId){
		ConsignOrder order = consignOrderDao.queryById(orderId);
		List<ConsignOrderItems> items = consignOrderItemsDao.selectByOrderIdAndSellerId(order.getId(), sellerId);
		Account buyer = accountDao.selectByPrimaryKey(order.getAccountId());
		User buyerManager = userDao.queryById(order.getOwnerId());
		/** 超市返利接口**/
        //创建客户端代理工厂
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        //注册接口
        factory.setServiceClass(FinanceService.class);
        //设置地址
        factory.setAddress(ivFinanceServiceAddress);
        FinanceService financeService = (FinanceService) factory.create();
    	BigDecimal balance = BigDecimal.valueOf(financeService.rebateAmountByPhone(ivFinanceServiceKEY, order.getContactTel()));   /** 需要调用超市接口**/
        BigDecimal oldBalance = balance;
        for (ConsignOrderItems item : items) {
            if (item.getActualPickQuantityServer() == 0 && BigDecimal.ZERO.compareTo(item.getActualPickWeightServer()) == 0) {
                continue;
            }
            //计算返利
            ReportRebateRecord rebateRecord = addRebateRecord(buyer,buyerManager,order,item,user,balance);
            balance = rebateRecord.getRebateBalance();
        }
        if (balance.compareTo(oldBalance) != 0) {
            String result = financeService.addRebate(
            		ivFinanceServiceKEY, 
            		balance.subtract(oldBalance).doubleValue(), 
            		order.getContactTel(), order.getContactName(), 
            		buyer.getName(), 
            		buyer.getProvinceId() == null ? null : provinceDao.selectByPrimaryKey(buyer.getProvinceId()).getName());
            if (!"充值成功".equals(result)) {
                throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "调用超市接口增加用户返利失败--" + result);
            }   //超市接口增加返利
        }
        
    }

    
    private ReportRebateRecord addRebateRecord(Account buyer, 
    		User buyerManager,ConsignOrder order,ConsignOrderItems item, User user,BigDecimal balance){
        CategoryGroupDto categoryGroup = categoryGroupDao.selectByNsortName(item.getNsortName());
    	Map<String, Object> param = new HashMap<>();
        param.put("rebateStatus", RebateStatus.EFFECT.getName());
        param.put("categoryUUID", categoryGroup.getCategoryGroupUuid());
        RebateDto rebate = rebateDao.queryRebateDto(param).get(0);
        ReportRebateRecord rebateRecord = new ReportRebateRecord(
        		new Date(), 
        		buyer.getId(), 
        		buyer.getName(), 
        		buyerManager.getOrgId(), 
        		organizationDao.queryById(buyerManager.getOrgId()).getName(), 
        		buyerManager.getId(), buyerManager.getName(),
                order.getContactId(), 
                order.getContactName(), 
                categoryGroup.getCategoryGroupUuid(), 
                categoryGroup.getCategoryGroupName(), 
                item.getActualPickWeightServer(), 
                item.actualBuyerAmount(),
                order.getCode(), 
                user.getLoginId());
        rebateRecord.setRebateAmount(rebate.getRebateRole().multiply(item.getActualPickWeightServer()).setScale(2, BigDecimal.ROUND_HALF_UP));
        rebateRecord.setRebateBalance(balance.add(rebateRecord.getRebateAmount()));
        if (reportRebateRecordDao.insertSelective(rebateRecord) != 1) {
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "插入返利数据失败");
        }
        return rebateRecord;
    }
}
