package com.prcsteel.platform.order.service.order.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.order.model.enums.*;
import com.prcsteel.platform.order.model.model.ConsignOrderChange;
import com.prcsteel.platform.order.service.order.changecontract.ConsignOrderChangeService;
import org.springframework.stereotype.Service;

import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.order.model.dto.PaymentOrderDto;
import com.prcsteel.platform.account.model.model.Account;
import com.prcsteel.platform.order.model.model.BusiPayRequest;
import com.prcsteel.platform.order.model.model.ConsignOrder;
import com.prcsteel.platform.acl.model.model.Organization;
import com.prcsteel.platform.order.model.model.PayRequest;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.account.persist.dao.AccountDao;
import com.prcsteel.platform.order.persist.dao.ConsignOrderDao;
import com.prcsteel.platform.order.persist.dao.ConsignOrderProcessDao;
import com.prcsteel.platform.acl.persist.dao.OrganizationDao;
import com.prcsteel.platform.order.persist.dao.PayRequestDao;
import com.prcsteel.platform.order.persist.dao.PaymentOrderDao;
import com.prcsteel.platform.acl.persist.dao.UserDao;
import com.prcsteel.platform.order.service.order.PaymentOrderService;

@Service("paymentOrderService")
public class PaymentOrderServiceImpl implements PaymentOrderService {

	@Resource
	private PaymentOrderDao paymentOrderDao;

	@Resource
	private ConsignOrderProcessDao consignOrderProcessDao;

	@Resource
	private OrganizationDao organizationDao;

	@Resource
	private UserDao userDao;

	@Resource
	private PayRequestDao payRequestDao;
	
	@Resource
	private ConsignOrderDao consignOrderDao;
	
	@Resource
	private AccountDao accountDao;
	@Resource
	private ConsignOrderChangeService consignOrderChangeService;
	
	@Override
	public  List<PaymentOrderDto> findByOrder(Long id) {
		List<PaymentOrderDto> list = paymentOrderDao.selectByOrderId(id);
		return list;
	}

	@Override
	public String getChangerName(Long orgId) {
		Organization organization = organizationDao.queryById(orgId);
		User manageUser = userDao.queryById(organization.getCharger());
		return manageUser.getName();
	}

	@Override
	public Organization findById(Long id) {
		Organization organization = organizationDao.queryById(id);
		return organization;
	}

	@Override
	public void updateForId(Long id, int printTimes,String ip,User operator) {
		PayRequest old = payRequestDao.selectByPrimaryKey(id);
		if(old==null){
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "指定的付款申请单不存在！");
		}
		List<String> list = new ArrayList<String>();
		list.add(Status.APPROVED.toString());
		list.add(Status.APPLYPRINTED.toString());
		PayRequest payRequest = payRequestDao.selectByPrimaryKey(id);
		if (!list.contains(payRequest.getStatus())) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,
					"该付款申请可能被打回或者还未审核！");
		}
		BusiPayRequest busiPayRequest = new BusiPayRequest();
		busiPayRequest.setId(id);
		busiPayRequest.setPrintTimes(printTimes);
		busiPayRequest.setPrintName(operator.getName());// 打印人
		busiPayRequest.setLastPrintIp(ip);//ip地址
		busiPayRequest.setLastPrintDate(new Date());//最后一次打印时间
		busiPayRequest.setLastUpdatedBy(operator.getLoginId());
		if(PayStatus.APPROVED.toString().equals(old.getStatus())){
			busiPayRequest.setStatus(PayStatus.APPLYPRINTED.toString());//Green added 拆分打印申请单和确认付款两个步骤
		}else{
			//未审核通过可以打印吗，已经确认了可以打印吗？
		}
		//订单付款,更新订单的付款状态
		if(PayRequestType.PAYMENT.getCode().equals(old.getType()) || PayRequestType.ORDER_CHANGE.getCode().equals(old.getType())){
			if(old.getConsignOrderId()==null){
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "付款申请单中不存在订单信息");
			}
			ConsignOrder order = consignOrderDao.queryById(old.getConsignOrderId());
			if(order==null){
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "付款申请单中指定的订单实际不存在！");
			}
			//合同变更
			if(PayRequestType.ORDER_CHANGE.getCode().equals(old.getType())){
				order.setAlterStatus(ConsignOrderAlterStatus.PENDING_CONFIRM_PAY.getCode());
				ConsignOrderChange consignOrderChange = consignOrderChangeService.selectByPrimaryKey(order.getChangeOrderId());
				//修改合同变更表的状态，并且添加对应的变更记录
				consignOrderChangeService.updateChangeStatus(consignOrderChange, ConsignOrderAlterStatus.PENDING_CONFIRM_PAY.getCode(), "", operator);

			}
			order.setPayStatus(ConsignOrderPayStatus.APPLYPRINTED.toString());
			order.setLastUpdated(new Date());
			order.setLastUpdatedBy(operator.getLoginId());
			consignOrderDao.updateByPrimaryKeySelective(order);
			
		}
		paymentOrderDao.updateById(busiPayRequest);
	}
	
	@Override
	public String findByRequestId (Long requestId) {
		return paymentOrderDao.findByRequestId(requestId);
	}
	
	@Override
	public Account selectByPrimaryKey(Long accountId) {
		return accountDao.selectByPrimaryKey(accountId);
	}

	public List<Long> queryReceiverIdByRequestId(Long requestId){
		return paymentOrderDao.queryReceiverIdByRequestId(requestId);
	}
	//通过订单id查询变更后的付款申请单
	@Override
	public List<PaymentOrderDto> selectChangeRequestByOrderId(Long id){
		return paymentOrderDao.selectChangeRequestByOrderId(id);
	}
}
