package com.prcsteel.platform.order.service.order.impl;

import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.dto.ResultDto;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.order.model.dto.*;
import com.prcsteel.platform.order.model.model.BusiConsignOrderCredential;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.prcsteel.platform.order.model.enums.CertificateStatus;
import com.prcsteel.platform.order.model.model.*;
import com.prcsteel.platform.order.model.query.CertificateInvoiceQuery;
import com.prcsteel.platform.order.persist.dao.*;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prcsteel.platform.account.model.enums.AccountType;
import com.prcsteel.platform.account.model.model.Account;
import com.prcsteel.platform.account.persist.dao.AccountDao;
import com.prcsteel.platform.acl.model.model.Organization;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.common.utils.Tools;
import com.prcsteel.platform.order.model.dto.BatchUploadingDto;
import com.prcsteel.platform.order.model.dto.CertificateInvoiceDto;
import com.prcsteel.platform.order.model.dto.ConsignOrderCredentialDto;
import com.prcsteel.platform.order.model.dto.CredentialDuplicateImage;
import com.prcsteel.platform.order.model.dto.CredentialImageDto;
import com.prcsteel.platform.order.model.dto.OrderInfoForCertificateDto;
import com.prcsteel.platform.order.model.dto.OrderTradeCertificateForUploadDto;

import com.prcsteel.platform.order.model.dto.UpdateCredentialImageDto;
import com.prcsteel.platform.order.model.enums.CertificateStatus;
import com.prcsteel.platform.order.model.model.BusiConsignOrderCertificateInvoice;
import com.prcsteel.platform.order.model.model.BusiConsignOrderCredential;
import com.prcsteel.platform.order.model.model.ConsignOrder;
import com.prcsteel.platform.order.model.model.ConsignOrderAttachment;
import com.prcsteel.platform.order.model.model.ConsignOrderItems;
import com.prcsteel.platform.order.model.query.BatchUploadingQuery;
import com.prcsteel.platform.order.model.query.CertificateInvoiceQuery;
import com.prcsteel.platform.order.model.query.OrderTradeCertificateQuery;
import com.prcsteel.platform.order.model.query.TradeCredentialQuery;
import com.prcsteel.platform.order.persist.dao.BusiConsignOrderCertificateInvoiceDao;
import com.prcsteel.platform.order.persist.dao.BusiConsignOrderCredentialDao;
import com.prcsteel.platform.order.persist.dao.ConsignOrderAttachmentDao;
import com.prcsteel.platform.order.persist.dao.ConsignOrderDao;
import com.prcsteel.platform.order.persist.dao.ConsignOrderItemsDao;
import com.prcsteel.platform.order.service.order.CertificateService;
import com.prcsteel.platform.order.service.order.ConsignOrderAttachmentService;

@Service("certificateService")
/**
 *
    * @ClassName: CertificateServiceImpl
    * @Description: 凭证服务实现
    * @author Green.Ge
    * @date 2016年4月8日
    *
 */

public class CertificateServiceImpl implements CertificateService {
	private Logger log = Logger.getLogger(CertificateServiceImpl.class);
	@Resource
    BusiConsignOrderCredentialDao busiConsignOrderCredentialDao;

	@Resource
	BusiConsignOrderCertificateInvoiceDao busiConsignOrderCertificateInvoiceDao;

	@Resource
	ConsignOrderAttachmentDao consignOrderAttachmentDao;

	@Resource
	ConsignOrderDao consignOrderDao;

	@Resource
	ConsignOrderItemsDao consignOrderItemsDao;
	@Resource
	ConsignOrderAttachmentService consignOrderAttachmentService;
	
	
	@Resource
	AccountDao accountDao;

	@Override
	public List<OrderTradeCertificateForUploadDto> loadcertificateforupload(OrderTradeCertificateQuery query) {
		return busiConsignOrderCredentialDao.loadcertificateforupload(query);
	}

	@Override
	public Integer loadcertificateforuploadTotal(OrderTradeCertificateQuery query) {
		return busiConsignOrderCredentialDao.loadcertificateforuploadTotal(query);
	}
	/**
	 *
	 * @Author: wangxianjun
	 * @Description: 根据条件查询买家凭证，用于凭证上传界面，分页
	 * @Date: 2016年4月8日
	 */
	@Override
	public List<OrderTradeCertificateForUploadDto> loadcertificateforuploadbuyer(OrderTradeCertificateQuery query){
		return busiConsignOrderCredentialDao.loadcertificateforuploadbuyer(query);
	}
	/**
	 *
	 * @Author: wangxianjun
	 * @Description: 取上面方法的总条数
	 * @Date: 2016年4月8日
	 */
	@Override
	public Integer loadcertificateforuploadbuyerTotal(OrderTradeCertificateQuery query){
		return busiConsignOrderCredentialDao.loadcertificateforuploadbuyerTotal(query);
	}

	@Override
	public int save(BusiConsignOrderCredential credential) {
		return busiConsignOrderCredentialDao.insertSelective(credential);
	}

	/**
	 * tuxianming
	 * 根据服务中心和当前月份查询最后一条
	 * @param map
	 * @return
	 */
	@Override
	public String queryLastRecord(Map<String,String> map) {
		return busiConsignOrderCredentialDao.queryLastRecord(map);
	}
	//add by wangxianjun 通过凭证号更新打印信息
	@Override
	public int  updateByCertSelective(BusiConsignOrderCredential record){
		return busiConsignOrderCredentialDao.updateByCertSelective(record);
	}

	@Override
	public BusiConsignOrderCredential findById(Long id) {
		return busiConsignOrderCredentialDao.selectByPrimaryKey(id);
	}

	//add by wangxianjun 通过凭证号查询凭证信息
	@Override
	public BusiConsignOrderCredential selectByCert(String cert){
		return busiConsignOrderCredentialDao.selectByCert(cert);
	}
	//add by wangxianjun 通过凭证id更新打印信息
	@Override
	public int  updateByIdSelective(BusiConsignOrderCredential record){
		return busiConsignOrderCredentialDao.updateByPrimaryKeySelective(record);
	}
	@Override
	public List<OrderInfoForCertificateDto> selectOrdersByParamsForUploadAdd(OrderTradeCertificateQuery query) {
		if("seller".equals(query.getAccountType()))
		return busiConsignOrderCredentialDao.selectOrdersByParamsForUploadAdd(query);
		else
		return busiConsignOrderCredentialDao.selectOrdersByBuyerParamsForUploadAdd(query);
	}
	/**
	 *
		* @Author: Green.Ge
	    * @Description: 批量上传凭证界面，删除一个订单时发生的后台交互，写了但是后来没用。
		* @Date: 2016年4月12日
	 */
	@Override
	public void removeBatchCertificateNO(String accountType, Long accountId,Long orderId) {
		if(!AccountType.seller.toString().equals(accountType)&&AccountType.buyer.toString().equals(accountType)){
			throw new BusinessException(Constant.ERROR_MISSING_REQUIRED_PARAM, "客户类型参数传递有误");
		}
		if(accountId == null){
			throw new BusinessException(Constant.ERROR_MISSING_REQUIRED_PARAM, "客户ID不能为空");
		}
		Account account = accountDao.selectByPrimaryKey(accountId);
		if(account==null){
			throw new BusinessException(Constant.ERROR_RESOURCE_NOT_FOUND, "ID为"+accountId+"的客户并不存在");
		}
		if(orderId == null){
			throw new BusinessException(Constant.ERROR_MISSING_REQUIRED_PARAM, "要删除的订单ID不能为空");
		}
		ConsignOrder order = consignOrderDao.queryById(orderId);
		if(order==null){
			throw new BusinessException(Constant.ERROR_RESOURCE_NOT_FOUND, "ID为"+orderId+"的订单并不存在");
		}

		String certificateCode = "";
		if(AccountType.seller.toString().equals(accountType)){
			//把该订单该卖家的明细捞出来
			List<ConsignOrderItems> itemList = consignOrderItemsDao.selectByOrderId(orderId).stream().filter(e->accountId.equals(e.getSellerId())).collect(Collectors.toList());
			//判断明细中的单独凭证号字段是否不为空
			Boolean isSingleUpload = itemList.stream().anyMatch(e->StringUtils.isNotBlank(e.getSellerCredentialCode()));
			if(isSingleUpload){//如果是，说明这个订单是从新增弹层中选过来的，因为页面刚加载的时候是不会加载单个凭证号不为空的订单的
				//发现这种情况，不用对这个单子做凭证号删除处理，因为还没点保存，不能直接与数据库交互。
				return;
			}else{
				itemList.forEach(e->{
					e.setBatchSellerCredentialCode("");
					consignOrderItemsDao.updateByPrimaryKeySelective(e);
				});
			}
			List<String> batchCertificateNOList = itemList.stream().map(e->e.getBatchSellerCredentialCode()).distinct().collect(Collectors.toList());
			if(batchCertificateNOList.size()>1){
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "订单："+order.getCode()+"的卖家："+account.getName()+"存在不同的批量凭证编号");
			}
			certificateCode = batchCertificateNOList.get(0);
		}else{
			Boolean isSingleUpload = StringUtils.isNotBlank(order.getBuyerCredentialCode());
			if(isSingleUpload){
				order.setBuyerCredentialCode("");
				consignOrderDao.updateByPrimaryKey(order);
			}
			certificateCode = order.getBatchBuyerCredentialCode();
		}
		//TODO 对凭证表里的数据做处理，删除或是作废
	}
	/**
	 *
		* @Author: Green.Ge
	    * @Description: 批量上传凭证页面点击保存按钮
		* @Date: 2016年4月12日
	 */
	@Override
	public Long saveBatchCertificateNO(User user,Organization org,String accountType, Long accountId, String certificateNO, String addArr,
			String delArr,String originArr) {
		if(StringUtils.isBlank(originArr)){
			throw new BusinessException(Constant.ERROR_MISSING_REQUIRED_PARAM, "原订单列表不能为空");
		}
		String ip = "";
		try {
			InetAddress ia= InetAddress.getLocalHost();
			ip=ia.getHostAddress();//当前IP
		} catch (UnknownHostException e) {
			log.error("批量上传凭证时获取IP失败");
		}
		BusiConsignOrderCredential cert = null;
		String newCertificateNO= generateCertificateCode(org.getSeqCode(), accountType);
		if(AccountType.seller.toString().equals(accountType)){
			//判断新增的订单是否已经被其他用户批量操作掉了
			String error = checkAddOrders(addArr, accountId);
			if(StringUtils.isNotBlank(error)){
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, error);
			}
			//处理删除的订单
			dealDelOrderItems(delArr,accountId);
			//处理需要更新成新凭证号的订单列表
			dealNewOrderItems(addArr,delArr,originArr,accountId,newCertificateNO);
			//新增凭证表数据
			OrderTradeCertificateQuery query = new OrderTradeCertificateQuery();
			query.setCredentialType(accountType);
			query.setNeedPage(false);

			cert = savePrintcateInfo(newCertificateNO, query, user, ip, 0, query.getCredentialNum());

		}else if(AccountType.buyer.toString().equals(accountType)){
			//判断新增的订单是否已经被其他用户批量操作掉了
			String error = checkBuyerAddOrders(addArr);
			if(StringUtils.isNotBlank(error)){
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, error);
			}
			//处理删除的订单
			dealBuyerDelOrders(delArr);
			//处理需要更新成新凭证号的订单列表
			dealBuyerNewOrdes(addArr,delArr,originArr,newCertificateNO);
			//新增凭证表数据
			OrderTradeCertificateQuery query = new OrderTradeCertificateQuery();
			query.setCredentialType(accountType);
			query.setNeedPage(false);

			cert = savePrintcateInfo(newCertificateNO, query, user, ip, 0, query.getCredentialNum());
		}
		if(cert == null){
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "没有生成新的凭证号!");
		}

		return cert.getId();
	}

	String checkAddOrders(String addIds,Long sellerId){
		String error = "";
		if(StringUtils.isNotBlank(addIds)){
			List<Long> ids = Arrays.asList(addIds.split(",")).stream().map(e->Long.parseLong(e)).collect(Collectors.toList());
			List<ConsignOrderItems> itemList = new ArrayList<ConsignOrderItems>();
			ids.forEach(e -> {
				itemList.addAll(consignOrderItemsDao.selectByOrderId(e));
			});
			//找出这个卖家的，批量凭证号不为空的单子
			List<Long> orderIdList = itemList.stream().filter(e->sellerId.equals(e.getSellerId())&&StringUtils.isNotBlank(e.getBatchSellerCredentialCode()))
			.collect(Collectors.toList()).stream().map(e->e.getOrderId()).distinct().collect(Collectors.toList());
			//拿着ID去找单据

			if(!orderIdList.isEmpty()){
				List<ConsignOrder> orderList = consignOrderDao.selectByOrderIds(orderIdList);
				error +="交易单号:<br/>";
				for(ConsignOrder order:orderList){
					error+=order.getCode()+"<br/>";
				};
				error+="已被选入批量凭证，请重新选择";
			}
		}
		return error;
	}

	String checkBuyerAddOrders(String addIds){
		String error = "";
		if(StringUtils.isNotBlank(addIds)){
			List<Long> ids = Arrays.asList(addIds.split(",")).stream().map(e->Long.parseLong(e)).collect(Collectors.toList());
			List<ConsignOrder> orderList = consignOrderDao.selectByOrderIds(ids);
			//找出买家的，批量凭证号不为空的单子
			List<Long> orderIdList = orderList.stream().filter(e->StringUtils.isNotBlank(e.getBatchBuyerCredentialCode()))
					.collect(Collectors.toList()).stream().map(e -> e.getId()).distinct().collect(Collectors.toList());
			//拿着ID去找单据
			if(!orderIdList.isEmpty()){
				List<ConsignOrder> orders = consignOrderDao.selectByOrderIds(orderIdList);
				error +="交易单号:<br/>";
				for(ConsignOrder order:orders){
					error+=order.getCode()+"<br/>";
				};
				error+="已被选入批量凭证，请重新选择";
			}
		}
		return error;
	}
	/**
	 *
		* @Author: Green.Ge
	    * @Description: 处理需要删除的订单（不再绑定任何凭证号）
		* @Date: 2016年4月12日
	 */
	private void dealDelOrderItems(String delArr,Long sellerId){
		if(StringUtils.isNotBlank(delArr)){
			List<Long> ids = Arrays.asList(delArr.split(",")).stream().map(e->Long.parseLong(e)).collect(Collectors.toList());
			List<ConsignOrderItems> itemList = new ArrayList<ConsignOrderItems>();
			ids.forEach(e -> {
				itemList.addAll(consignOrderItemsDao.selectByOrderId(e));
			});
			itemList.stream().filter(e->sellerId.equals(e.getSellerId())).collect(Collectors.toList()).forEach(e -> {
				//把原来的凭证号清空，以便让别的订单可以和它批量打印
				e.setBatchSellerCredentialCode("");
				e.setModificationNumber(e.getModificationNumber() + 1);
				consignOrderItemsDao.updateByPrimaryKeySelective(e);
			});
		}
	}
	/**
	 *
	 * @Author: wangxianjun
	 * @Description: 处理需要删除的订单（不再绑定任何凭证号）
	 * @Date: 2016年4月12日
	 */
	private void dealBuyerDelOrders(String delArr){
		if(StringUtils.isNotBlank(delArr)){
			List<Long> ids = Arrays.asList(delArr.split(",")).stream().map(e->Long.parseLong(e)).collect(Collectors.toList());
			List<ConsignOrder> orderList = consignOrderDao.selectByOrderIds(ids);
			orderList.forEach(e -> {
				//把原来的凭证号清空，以便让别的订单可以和它批量打印
				e.setBatchBuyerCredentialCode("");
				e.setModificationNumber(e.getModificationNumber() + 1);
				consignOrderDao.updateByPrimaryKeySelective(e);
			});
		}
	}
	/**
	 *
		* @Author: Green.Ge
	    * @Description: 更新订单的批量明细凭证号
		* @Date: 2016年4月12日
	 */
	private void dealNewOrderItems(String addArr,String delArr,String originArr,Long sellerId,String newCertificateNO){
		//构建需要更新成新凭证号的订单列表 新增的+原来的-删掉的
		List<Long> finalIds = new ArrayList<Long>();
		if(StringUtils.isNotBlank(addArr)){
			List<Long> ids = Arrays.asList(addArr.split(",")).stream().map(e->Long.parseLong(e)).collect(Collectors.toList());
			finalIds.addAll(ids);
		}
		if(StringUtils.isNotBlank(originArr)){
			List<Long> ids = Arrays.asList(originArr.split(",")).stream().map(e->Long.parseLong(e)).collect(Collectors.toList());
			finalIds.addAll(ids);
		}
		if(StringUtils.isNotBlank(delArr)){
			List<Long> ids = Arrays.asList(delArr.split(",")).stream().map(e->Long.parseLong(e)).collect(Collectors.toList());
			finalIds.removeAll(ids);
		}
		finalIds = finalIds.stream().distinct().collect(Collectors.toList());
		List<ConsignOrderItems> itemList = new ArrayList<ConsignOrderItems>();
		finalIds.forEach(e->{
			itemList.addAll(consignOrderItemsDao.selectByOrderId(e));
		});
		itemList.stream().filter(e->sellerId.equals(e.getSellerId())).collect(Collectors.toList()).forEach(e->{
			//更新成新的凭证号
			e.setBatchSellerCredentialCode(newCertificateNO);
			e.setModificationNumber(e.getModificationNumber()+1);
			consignOrderItemsDao.updateByPrimaryKeySelective(e);
		});
	}
	/**
	 *
	 * @Author: wangxianjun
	 * @Description: 更新订单的批量买家凭证号
	 * @Date: 2016年4月12日
	 */
	private void dealBuyerNewOrdes(String addArr,String delArr,String originArr,String newCertificateNO){
		//构建需要更新成新凭证号的订单列表 新增的+原来的-删掉的
		List<Long> finalIds = new ArrayList<Long>();
		if(StringUtils.isNotBlank(addArr)){
			List<Long> ids = Arrays.asList(addArr.split(",")).stream().map(e->Long.parseLong(e)).collect(Collectors.toList());
			finalIds.addAll(ids);
		}
		if(StringUtils.isNotBlank(originArr)){
			List<Long> ids = Arrays.asList(originArr.split(",")).stream().map(e->Long.parseLong(e)).collect(Collectors.toList());
			finalIds.addAll(ids);
		}
		if(StringUtils.isNotBlank(delArr)){
			List<Long> ids = Arrays.asList(delArr.split(",")).stream().map(e->Long.parseLong(e)).collect(Collectors.toList());
			finalIds.removeAll(ids);
		}
		finalIds = finalIds.stream().distinct().collect(Collectors.toList());

		List<ConsignOrder> orderList = consignOrderDao.selectByOrderIds(finalIds);
		orderList.forEach(e -> {
			//更新成新的凭证号
			e.setBatchBuyerCredentialCode(newCertificateNO);
			e.setModificationNumber(e.getModificationNumber() + 1);
			consignOrderDao.updateByPrimaryKeySelective(e);
		});
	}
	/**
	 * tuxianming
	 * 生成打印凭证code
	 * @return
	 */
	public String generateCertificateCode(String orgCode,String credentialType){

		Date date = new Date();
		String dateStr = Tools.dateToStr(date, "yyMM");
		String month = Tools.dateToStr(date, "yyyyMM");
		String type = "";
		if("seller".equals(credentialType)){
			type = "02";
		}else{
			type = "03";
		}
		Map<String,String> orgMap = new HashMap<String,String>();
		orgMap.put("orgCode",type+orgCode);
		orgMap.put("month",month);
		//根据服务中心和当前月份查询最后一条,若不存在，流水号从1开始，若存在流水号加1
		String maxCode = queryLastRecord(orgMap);
		int number = 1;
		if(!"0".equals(maxCode)){
			//当前月有凭证号
			//List lastCodeChips = parseCertificateCode(lastRecord.getCredentialCode());

			//if(lastCodeChips!=null && lastCodeChips.size() > 0){
				//如果时间相等，则在原有的流水号上面+1,
//				if(dateStr.equals(lastCodeChips.get(0))){
			number = Integer.valueOf(maxCode)+1;		
//				}
			//}

		}

		return type+orgCode+dateStr+String.format("%05d", number);
	}

	/**
	 * tuxianming
	 * 解析打印凭证code
	 * List index[0]:number
	 * @return
	 */
	private List parseCertificateCode(String code){
		if(code==null || code.length()<13)
			return null;
		String number = code.substring(code.length() - 5);
		//String date = code.substring(code.length() - (5 + 4));
		List chips =  new ArrayList();
		//chips.add(date);
		chips.add(number);
		return chips;
	}
	/**
	 *
	 * @Author: wangxianjun
	 * @Description: 保存凭证信息
	 * @Date: 2016年4月12日
	 */
	@Override
	public BusiConsignOrderCredential savePrintcateInfo(String certificateCode,OrderTradeCertificateQuery query,
			User user,String ip,int printNum, Integer credentialNum){
		
		    BusiConsignOrderCredential credential = new BusiConsignOrderCredential();
			credential.setCredentialCode(certificateCode);
			credential.setType(query.getCredentialType());
			setCredentialName(credential, query);
			credential.setCredentialNum(credentialNum);
			
			if(query.getPrintQRCode()){ //如果是打印是打印凭证号二维码： 则把次数设置为0， 由于返回的数据类型不一样，所以需要单独提交打印次数。
				credential.setPrintCodeNumber(0);
				credential.setPrintCodeIp(ip);
				credential.setPrintCodeDate(new Date());
				credential.setPrintCodeBy(user.getName());
			}else{
				credential.setPrintNum(printNum);
				credential.setPrintIp(ip);
				credential.setPrintDate(new Date());
				credential.setPrintedBy(user.getName());
			}
			
			credential.setStatus("PENDING_SUBMIT");
		    credential.setUploadStatus(Constant.CREDENTIAL_UPLOAD_STATUS.PENDING_REVISION.toString());
			credential.setIsDeleted(false);
			credential.setCreated(new Date());
			if (save(credential) <= 0) {
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "凭证号："+certificateCode+"的相关信息保存失败！");
			}
			return selectByCert(certificateCode);
	}
	/**
	 * wangxianjun
	 * 设置凭证名称
	 * @param c
	 * @param query
	 */
	private void setCredentialName(BusiConsignOrderCredential c, OrderTradeCertificateQuery query){
		if(query.getCredentialType().equals("buyer")){

			if(query.getNeedPage()){
				c.setName("收货确认函");

			}else{
				c.setName("(批)收货确认函");
			}

		}else{
			if(query.getNeedPage()){
				c.setName("销售单");
			}else{
				c.setName("(批)销售单");
			}

		}
	}
	/**
	 *
	 * @Author: wangxianjun
	 * @Description: 更新凭证信息
	 * @Date: 2016年4月12日
	 */
	@Override
	public void updatePrintcateInfo(String certificateCode, User user, String ip, Boolean printQRCode, Integer credentialNum){
		BusiConsignOrderCredential credential = new BusiConsignOrderCredential();
		credential.setCredentialCode(certificateCode);
		credential.setCredentialNum(credentialNum);
		// 通过certificate_code 凭证号查询凭证信息
		BusiConsignOrderCredential  newCredential =selectByCert(certificateCode);
		//  在通过凭证id 查询已关联的附件
		List<ConsignOrderAttachment> attachment=consignOrderAttachmentService.getAttachmentByConsignOrderId(newCredential.getId());
		//  如果 修改的总页码大于 小于附件总数，且之前数据是已集齐的并且不是老数据，则修改为 已校对待集齐
		if(credentialNum>attachment.size()&&Constant.CREDENTIAL_UPLOAD_STATUS.ALREADY_COLLECT.toString().equals(newCredential.getUploadStatus())){
			credential.setUploadStatus(Constant.CREDENTIAL_UPLOAD_STATUS.PENDING_COLLECT.toString());
		}else if(credentialNum<attachment.size()&&Constant.CREDENTIAL_UPLOAD_STATUS.ALREADY_COLLECT.toString().equals(newCredential.getUploadStatus())){
			credential.setUploadStatus(Constant.CREDENTIAL_UPLOAD_STATUS.PENDING_COLLECT.toString());
		} else	if(credentialNum==attachment.size()&&!Constant.CREDENTIAL_UPLOAD_STATUS.OLD_DATA.toString().equals(newCredential.getUploadStatus())){
			credential.setUploadStatus(Constant.CREDENTIAL_UPLOAD_STATUS.ALREADY_COLLECT.toString());
		}
		if(printQRCode){
			credential.setPrintCodeNumber(-1);  //这里加1，只要传入的参数不为空，
			credential.setPrintCodeDate(new Date());
			credential.setPrintCodeIp(ip);
			credential.setPrintCodeBy(user.getName());
		}else{
			credential.setPrintNum(-1);
			credential.setPrintDate(new Date());
			credential.setPrintIp(ip);
			credential.setPrintedBy(user.getName());
		} 
		
		if (updateByCertSelective(credential) <= 0) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "凭证号："+certificateCode+"的相关信息更新失败！");
		}
	}
	//add by wangxianjun 凭证提交审核,审核通过、审核不通过
	@Override
	public ResultDto submitCertById(Long certId, User user, String status, String cause){
		ResultDto result = new ResultDto();
		BusiConsignOrderCredential record = busiConsignOrderCredentialDao.selectByPrimaryKey(certId);
		record.setId(certId);
		if("CANCEL".equals(status)) {
			record.setStatus("DISAPPROVE");//取消审核即不通过
		}else{
			record.setStatus(status);
		}
        if(cause != null){
        	record.setNote(cause);
        }
		String msg = "";
		String msgError = "";
		if("PENDING_APPROVAL".equals(status)) {
			//待审核
			msg = "凭证号："+record.getCredentialCode()+"提交审核成功！";
			msgError = "凭证号："+record.getCredentialCode()+"提交审核失败！";
			record.setSubmitDate(new Date());
			record.setSubmitedBy(user.getName());
		}else{
			//审核通过 APPROVE   审核不通过 DISAPPROVE
			if("APPROVED".equals(status)) {
				msg = "凭证号："+record.getCredentialCode()+"审核通过成功！";
				msgError = "凭证号："+record.getCredentialCode()+"审核通过失败！";
			}else if("DISAPPROVE".equals(status))  {
				msg = "凭证号："+record.getCredentialCode()+"审核不通过成功！";
				msgError = "凭证号："+record.getCredentialCode()+"审核不通过失败！";
			}else {
				msg = "凭证号："+record.getCredentialCode()+"取消审核通过成功！";
				msgError = "凭证号："+record.getCredentialCode()+"取消审核通过失败！";
			}
			record.setAuditDate(new Date());
			record.setAuditBy(user.getName());
		}
		if (busiConsignOrderCredentialDao.updateByPrimaryKeySelective(record) <= 0){
			result.setMessage(msgError);
			result.setSuccess(false);
		} else {
			result.setMessage(msg);
			result.setSuccess(true);
		}
		return result;
	}
	//根据买家凭证号查询买家对应卖家凭证
	@Override
	public BusiConsignOrderCredential findSellerCertBybuyerCert(String cert){
		BusiConsignOrderCredential orderCert = null;
		String sellerCert = consignOrderDao.findSellerCertBybuyerCert(cert);
		if(sellerCert != null && !"".equals(sellerCert)){
			orderCert = selectByCert(sellerCert);
		}
		return orderCert;
	}
	//根据买家凭证号查询买家对应卖家凭证
	@Override
	public BusiConsignOrderCredential findBuyerCertBySellerCert(String cert){
		BusiConsignOrderCredential orderCert = null;
		String buyerCert = consignOrderDao.findBuyerCertBySellerCert(cert);
		if(buyerCert != null && !"".equals(buyerCert)){
			orderCert = selectByCert(buyerCert);
		}
		return orderCert;
	}

	@Override
	public List<ConsignOrderCredentialDto> queryChecklist(TradeCredentialQuery query) {
		return busiConsignOrderCredentialDao.queryChecklist(query);
	}

	@Override
	public int queryTotalChecklist(TradeCredentialQuery query) {
		return busiConsignOrderCredentialDao.queryTotalChecklist(query);
	}

	/**
	 * @author peanut
	 * @description 需补齐买、卖家凭证的已开票订单job
	 * @date 2016/4/12
	 */
	@Override
	@Transactional
	public void executeCertificateInvoiceJob() {
		Calendar cal=Calendar.getInstance();
		int year=cal.get(Calendar.YEAR);
		int month=cal.get(Calendar.MONTH);
		//当前日期的上一个月1号
		String openOrderDate=year+"-"+month+"-01";
		//String openOrderDate="2016-04-13";
		List<CertificateInvoiceDto> resultList=new ArrayList<>();
		List<CertificateInvoiceDto> buyerList=doCertificateInvoiceJobDetail(openOrderDate,"buyer");
		List<CertificateInvoiceDto> sellerList=doCertificateInvoiceJobDetail(openOrderDate,"seller");
		if(buyerList!=null && !buyerList.isEmpty()){
			resultList=ListUtils.union(resultList,buyerList);
		}
		if(sellerList!=null && !sellerList.isEmpty()){
			resultList=ListUtils.union(resultList,sellerList);
		}
		busiConsignOrderCertificateInvoiceDao.doBatchInsert(resultList);
	}

    /**
     * job详情
     *
     * @param openOrderDate 开单日期
     * @param accountType   客户类型:买家，buyer;卖家：seller
     * @return
     */
    private List<CertificateInvoiceDto> doCertificateInvoiceJobDetail(String openOrderDate, String accountType) {
        if (StringUtils.isBlank(openOrderDate) || StringUtils.isBlank(accountType)) {
            throw new BusinessException(Constant.ERROR_MISSING_REQUIRED_PARAM, "开单日期或客户类型为空！");
        }
        List<CertificateInvoiceDto> orderList = consignOrderDao.selectOrderUpOpenDate(openOrderDate, accountType);
        if (orderList != null && !orderList.isEmpty()) {
            //最终结果集
            List<CertificateInvoiceDto> resultList = new ArrayList<>();
            //无凭证号的订单:待打印
            List<CertificateInvoiceDto> tbPrintedList = orderList.stream().filter(e -> StringUtils.isBlank(e.getCredentialCode())).collect(Collectors.toList());
            tbPrintedList.forEach(e -> e.setCertificateStatus(CertificateStatus.PENDING_PRINT.toString()));

            resultList.addAll(tbPrintedList);
            //其余有凭证号的订单
            List<CertificateInvoiceDto> restList = ListUtils.removeAll(orderList, tbPrintedList);
            if (!restList.isEmpty()) {
                //凭证号list
                List<String> credentialCodeList = restList.stream().map(e -> e.getCredentialCode()).collect(Collectors.toList());

                //打印次数等于0的还是处于待打印状态

                List<BusiConsignOrderCredential> cList = busiConsignOrderCredentialDao.selectByCertList(credentialCodeList);
                List<BusiConsignOrderCredential> zeroPrintedCertList = cList.stream().filter(e -> e.getPrintNum() == 0).collect(Collectors.toList());

                if (!zeroPrintedCertList.isEmpty()) {
                    //打印次数等于0的凭证号
                    List<String> zeroCertCodeList = zeroPrintedCertList.stream().map(e -> e.getCredentialCode()).collect(Collectors.toList());
                    List<CertificateInvoiceDto> restTBPrintedList = restList.stream().filter(e -> zeroCertCodeList.contains(e.getCredentialCode())).collect(Collectors.toList());
                    restTBPrintedList.forEach(e -> e.setCertificateStatus(CertificateStatus.PENDING_PRINT.toString()));
                    resultList.addAll(restTBPrintedList);
                    restList = ListUtils.removeAll(restList, restTBPrintedList);
                }

                //打印次数大于0，检查凭证号对应主健ID，是否存在于附件表，否为待上传,是则上传完了,上传完了，待提交和审核不通过都属于待提交

                List<BusiConsignOrderCredential> printedUpZeroCertList = ListUtils.removeAll(cList, zeroPrintedCertList);
                List<Long> printedUpZeroCertIdList = printedUpZeroCertList.stream().map(e -> e.getId()).collect(Collectors.toList());

                if (printedUpZeroCertIdList != null && !printedUpZeroCertIdList.isEmpty()) {
                    //此处查询是因为凭证号记录表id存于附件表的consign_order_id之中
                    List<ConsignOrderAttachment> attachmentList = consignOrderAttachmentDao.selectCertAttacByConsignOrderIds(printedUpZeroCertIdList);
                    List<Long> existsAttaCertIds = attachmentList.stream().map(e -> e.getConsignOrderId()).collect(Collectors.toList());
                    List<Long> noExistsAttaCertIds = ListUtils.removeAll(printedUpZeroCertIdList, existsAttaCertIds);
                    //不存在附件的凭证号
                    List<String> noExistsAttaCertCodes = printedUpZeroCertList.stream().filter(e -> noExistsAttaCertIds.contains(e.getId())).map(e -> e.getCredentialCode()).collect(Collectors.toList());
                    //存在附件的凭证号
                    List<String> existsAttaCertCodes = printedUpZeroCertList.stream().filter(e -> existsAttaCertIds.contains(e.getId())).map(e -> e.getCredentialCode()).collect(Collectors.toList());

                    //待上传
                    List<CertificateInvoiceDto> restTBUploadList = restList.stream().filter(e -> noExistsAttaCertCodes.contains(e.getCredentialCode())).collect(Collectors.toList());
                    restTBUploadList.forEach(e -> e.setCertificateStatus(CertificateStatus.PENDING_UPLOAD.toString()));

                    resultList.addAll(restTBUploadList);

                    //待提交(待提交+审核不通过) 、待审核、通过审核
                    List<CertificateInvoiceDto> restTBSubOrApprList = restList.stream().filter(e -> existsAttaCertCodes.contains(e.getCredentialCode())).collect(Collectors.toList());

                    List<String> restTBSubOrApprCertCodes = restTBSubOrApprList.stream().map(e -> e.getCredentialCode()).collect(Collectors.toList());

                    if (restTBSubOrApprCertCodes != null && !restTBSubOrApprCertCodes.isEmpty()) {
                        List<BusiConsignOrderCredential> restTBSubOrApprCertList = busiConsignOrderCredentialDao.selectByCertList(restTBSubOrApprCertCodes);

                        Map<String, List<BusiConsignOrderCredential>> statusGroup = restTBSubOrApprCertList.stream().collect(Collectors.groupingBy(e -> e.getStatus()));

                        if (statusGroup != null && !statusGroup.isEmpty()) {
                            List<BusiConsignOrderCredential> tbSubCertList = new ArrayList<>();
                            if (statusGroup.get(CertificateStatus.PENDING_SUBMIT.toString()) != null) {
                                tbSubCertList.addAll(statusGroup.get(CertificateStatus.PENDING_SUBMIT.toString()));
                            }
                            if (statusGroup.get(CertificateStatus.DISAPPROVE.toString()) != null) {
                                tbSubCertList.addAll(statusGroup.get(CertificateStatus.DISAPPROVE.toString()));
                            }
                            List<String> tbSubCertCodes = tbSubCertList.stream().map(e -> e.getCredentialCode()).collect(Collectors.toList());
                            //待提交
                            List<CertificateInvoiceDto> tbSubList = restList.stream().filter(e -> tbSubCertCodes.contains(e.getCredentialCode())).collect(Collectors.toList());
                            tbSubList.forEach(e -> e.setCertificateStatus(CertificateStatus.PENDING_SUBMIT.toString()));

                            resultList.addAll(tbSubList);

                            //待审核
                            List<BusiConsignOrderCredential> tbApprCertList = statusGroup.get(CertificateStatus.PENDING_APPROVAL.toString());
                            if (tbApprCertList != null && !tbApprCertList.isEmpty()) {
                                List<String> tbApprCertCodes = tbApprCertList.stream().map(e -> e.getCredentialCode()).collect(Collectors.toList());

                                List<CertificateInvoiceDto> tbApprList = restList.stream().filter(e -> tbApprCertCodes.contains(e.getCredentialCode())).collect(Collectors.toList());
                                tbApprList.forEach(e -> e.setCertificateStatus(CertificateStatus.PENDING_APPROVAL.toString()));

                                resultList.addAll(tbApprList);
                            }
                            //去除通过审核的记录
                            List<BusiConsignOrderCredential> approvedCertList = statusGroup.get(CertificateStatus.APPROVED.toString());
                            if (approvedCertList != null && !approvedCertList.isEmpty() && !resultList.isEmpty()) {
                                List<String> approvedCertCodes = approvedCertList.stream().map(e -> e.getCredentialCode()).collect(Collectors.toList());
                                //结果集中通过审核的交易单号集
                                List<String> resultApprovedCodeList = restList.stream().filter(e -> approvedCertCodes.contains(e.getCredentialCode())).map(e -> e.getCode()).collect(Collectors.toList());
                                resultList = ListUtils.removeAll(resultList, resultList.stream().filter(e -> resultApprovedCodeList.contains(e.getCode())).collect(Collectors.toList()));
                            }
                        }
                    }
                }
            }
            resultList.forEach(e -> e.setAccountType(accountType));
            return resultList;
        }
        return null;
    }

    @Override
    public List<BusiConsignOrderCertificateInvoice> searchCertificateInvoice(CertificateInvoiceQuery query) {
        if (query == null) return null;
        List<BusiConsignOrderCertificateInvoice> list = busiConsignOrderCertificateInvoiceDao.searchCertificateInvoice(query);

        if (list == null || list.isEmpty()) return null;

        list.forEach(e -> {
            //设置状态
            if (e.getStatus() != null)
                e.setStatus(CertificateStatus.getNameByCode(e.getStatus()));

            //距开单时间已有天数
            if (StringUtils.isNotBlank(e.getOpenOrderDate())) {
                Calendar currDate = Calendar.getInstance();
                Calendar opendDate = (Calendar) currDate.clone();
                try {
                    opendDate.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(e.getOpenOrderDate()));
                    e.setDurationDay((int) ((currDate.getTimeInMillis() - opendDate.getTimeInMillis()) / (1000 * 3600 * 24)));
                } catch (ParseException e1) {
                    throw new BusinessException(Constant.FAIL, "日期转换出错!");
                }
            }

            //回收截止日期
            if (e.getCreated() != null) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(e.getCreated());
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH) + 1;
                //默认创建记录的那个月的15号
                e.setExpiryDay(year + "-" + month + "-15");
            }
        });
        return list;
    }

	@Override
	public List<BusiConsignOrderCredential> findByCode(String code) {
		return busiConsignOrderCredentialDao.selectByCertList(Arrays.asList(code));
	}
	/**
	 * 根据查询对象 查询 批量上传凭证图片 tab页
     */
    public 	List<BatchUploadingDto> searchBatchUploading(BatchUploadingQuery query){
		return busiConsignOrderCredentialDao.searchBatchUploading(query);
	}
    
    public List<BatchUploadingDto> countBatchUploading(BatchUploadingQuery query){
    	return busiConsignOrderCredentialDao.countBatchUploading(query);
    }

	@Override
	public List<BusiConsignOrderCredential> findByCodes(List<String> credentialCodes) {
		return busiConsignOrderCredentialDao.selectByCertList(credentialCodes);
	}
	@Override
	public BusiConsignOrderCredential findByCredentialCode(String code) {
		return busiConsignOrderCredentialDao.selectByPrimaryCredentialCode(code);
	}

	/**
	 * 更新凭证号凭证条码: 将dto.updates里面的图片与凭证号进行绑定，并且更新凭证号页码状态
	 * 	将dto.removes里面的图片进行删除 
	 * tuxianming
	 * @return
	 */
	@Transactional
	public void updateCredentialImages(UpdateCredentialImageDto dto){
		if(dto.getUpdates()==null && dto.getRemoves()==null){
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "待更新的信息为空，请确认至少有一个输入了凭证号和页码数的");
		}
		
		try{
			//保存绑定
			/**
			 * 根据dto.getUpdateObjs()得到凭证与图片相关信息
			 * 根据凭证号，把有的凭证查询出来，然后把凭证id设置到attachment表里。再更新凭证表图片上传状态 
			 */
			if(dto.getUpdates() != null){
				//得到所有不重复的code集合
				List<String> credentialCodes = dto.getUpdates().stream()
						.map(updateObj -> updateObj.getCredentialCode()).distinct().collect(Collectors.toList());
				
				//根据code集合查询到BusiConsignOrderCredential集合
				List<BusiConsignOrderCredential> certis = this.findByCodes(credentialCodes);
				if(certis!=null){
					
					//将BusiConsignOrderCredential集合按code分组
					//key: code, value: credential
					Map<String, BusiConsignOrderCredential> certiMap = new HashMap<>();
					//key:　code, value: totalPage
					for (BusiConsignOrderCredential certi : certis) {
						certiMap.put(certi.getCredentialCode(), certi);
					}
					
					//将凭证号：按新老数据分组
					List<CredentialImageDto> olds = new ArrayList<>();
					List<CredentialImageDto> news = new ArrayList<>();
					for (CredentialImageDto credentialImageDto : dto.getUpdates()) {
						
						String code = credentialImageDto.getCredentialCode();
						BusiConsignOrderCredential tmp = certiMap.get(code);
						
						if(tmp ==null)
							continue;
						
						if(Constant.CREDENTIAL_UPLOAD_STATUS.OLD_DATA.toString().equals(tmp.getUploadStatus())){
							if(olds.indexOf(credentialImageDto)==-1){
								olds.add(credentialImageDto);
							}
						}else{
							if(news.indexOf(credentialImageDto)==-1){
								news.add(credentialImageDto);
							}
						}
						
					}
					
					//根据集合certiId 集合查询所有的ConsignOrderAttachment对象 
					List<ConsignOrderAttachment> attachments = consignOrderAttachmentService.findByCredentialIds(
							certis.stream().map(cert -> cert.getId()).collect(Collectors.toList())
						);
					
					//将ConsignOrderAttachment集合按 certiId分组
					Map<Long, List<ConsignOrderAttachment>> attachmentsByCertiId = attachments.stream().collect(Collectors.groupingBy(ConsignOrderAttachment::getConsignOrderId));
					
					if(olds.size()>0){
						//直接绑定
						doUpdateUpdateInfo(olds, false, certiMap, null);
					}
					
					if(news.size()>0){
						
						Map<String, CredentialDuplicateImage> duplicateMap = new HashMap<>();
						
						Map<String, List<CredentialImageDto>> updatesByCode 
							= news.stream().collect(Collectors.groupingBy(CredentialImageDto::getCredentialCode));
						
						//检测到数据的合法性
						checkUpdateImageInfo(updatesByCode, attachmentsByCertiId, certiMap, news, dto.getReplace(), duplicateMap);
						
						//执行更新
						doUpdateUpdateInfo(news, dto.getReplace(), certiMap, duplicateMap);
						
						//如果要替换掉重复的数据，则删除之前的记录
						if(dto.getReplace() !=null && dto.getReplace()){
							List<Long> ids = new ArrayList<Long>();
							for (CredentialDuplicateImage value : duplicateMap.values()) {
								ids.addAll(value.getAttachmentIds());
							}
							consignOrderAttachmentService.delete(ids);
						}
						
						doUpdateCertiUpdateStatus(certis);
					}//end news.size>0
					
				} // certis!=null
				
			}//end dto.getUpdates() !=null
			
			//删除指定的图片
			if(dto.getRemoves()!=null && dto.getRemoves().size()>0){
				consignOrderAttachmentService.delete(dto.getRemoves());
			}
			
		}catch (BusinessException e){
			log.info(e.getMsg(), e);
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, e.getMsg());
		}catch (Exception e){
			log.error(e.getMessage(), e);
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "更新失败！");
		}
	}

	/**
	 * tuxianming20160603
	 * 检查数据凭证图片的合法性
	 */
	private void checkUpdateImageInfo(Map<String, List<CredentialImageDto>> updatesByCode, 
			Map<Long, List<ConsignOrderAttachment>> attachmentsByCertiId,
			Map<String, BusiConsignOrderCredential> certiMap,
			List<CredentialImageDto> news,
			Boolean replace, 
			Map<String, CredentialDuplicateImage> duplicateMap){
		
		/**
		 * 这里面存放要从dto.getUpdateObjs()里面删除的对象 
		 * 检测页码是不是重复
		 */
		//如果有重复的，将数据库里面重复的对象入在这里面
		
		for(String code : updatesByCode.keySet()){
			try{
				List<CredentialImageDto> updates = updatesByCode.get(code);
				List<ConsignOrderAttachment> existed = attachmentsByCertiId.get(certiMap.get(code).getId());
				if(existed!=null){
					for (CredentialImageDto imgageDto : updates) {
						for (ConsignOrderAttachment attachment : existed) {
							int pageNumber = imgageDto.getPageNum();
							if(pageNumber ==  attachment.getPageNumber()){
								CredentialDuplicateImage duplicate = duplicateMap.get(code);
								if(duplicate==null){
									duplicate = new CredentialDuplicateImage();
									duplicateMap.put(code, duplicate);
									duplicate.setCertiId(attachment.getConsignOrderId());
									duplicate.setCode(code);
								}
								duplicate.getAttachmentIds().add(attachment.getId());
								duplicate.getPageNums().add(attachment.getPageNumber()); 
								break;
							}
							
						}
					}
				}
				
			}catch(NullPointerException e){
				log.info(e.getMessage(), e);
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "凭证号："+code+" 不存在！");
			}
			
		}
		
		//系统中凭证号02HZ160500001，已存在页码1、2；凭证号02HZ160500002，已存在页码2、3；是否替换？  
		if(duplicateMap.keySet().size()>0 && replace==null){
			String result =  "系统中凭证号 ";
			
			for (String code : duplicateMap.keySet()) {
				CredentialDuplicateImage duplicate =  duplicateMap.get(code);
				result += duplicate.getCode()+"， 已存在页码"+duplicate.getPageNums().toString()+"；";
			}
			result+= "是否替换？";
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,  "1||"+result);
		}
		
		
		//检测是不是大于总数
		List<String> moreThanTotalCodes = new ArrayList<>();
		for(String code : updatesByCode.keySet()){
			List<CredentialImageDto> updates = updatesByCode.get(code);
			
			int updateTotal = updates!=null? updates.size() : 0;
			//根据certiCode从certiMap里面得到：BusiConsignOrderCredential对象，
			//再从BusiConsignOrderCredential得到：certiId
			//最后根据：certiId从attachmentsByCertiId 里得到，按certiId分组的集合对象，
			//求合就是：total 相当于： consignOrderAttachmentService.totalByCredentialId(certi.getId());
			
			List<ConsignOrderAttachment> exists = attachmentsByCertiId.get(certiMap.get(code).getId());
			CredentialDuplicateImage duplicates = duplicateMap.get(code);
			int duplicteLength = duplicates!=null?duplicates.getPageNums().size():0;
			
			int existTotal = exists==null? 0: exists.size();
			
			//实际总条数： （提交的-重复的）+（数据库-重复的）
			if(certiMap.get(code).getCredentialNum()<((updateTotal -duplicteLength) + (existTotal-duplicteLength))){
				moreThanTotalCodes.add(code);
			}
			
		}
		
		if(moreThanTotalCodes.size()>0){
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, 
					"凭证号：“"+moreThanTotalCodes.toString().replace("[", "").replace("]", "")+"”已输入凭证页码大于该凭证总页码数，请确认后再提交"
					);
		}
		
	}
	
	/**
	 * 执行将图片信息与凭证号绑定起来
	 */
	private void doUpdateUpdateInfo(List<CredentialImageDto> updates,
			Boolean replace,
			Map<String, BusiConsignOrderCredential> certiMap,
			Map<String, CredentialDuplicateImage> duplicateMap){
		for (CredentialImageDto updateObj : updates) {
			try{
				Long certiId =  certiMap.get(updateObj.getCredentialCode()).getId();
				
				//如果dto.getReplace==null, duplicateMap.size==0，则直接提交
				//如果dto.getReplace==false duplicateMap!=null 刚不替换原数据，也就是当duplicateMap里面存在的时候，跳过
				//如果dto.getReplace==true, duplicateMap!=null 换原数据，也就是直接提交，等会再删除duplicateMap里面的数据 
				if(replace !=null && replace==false 
					&& duplicateMap!=null && duplicateMap.get(updateObj.getCredentialCode())!=null){
						continue;
				}
				
				if(certiId != null ){
					//将图片与凭证号绑定
					ConsignOrderAttachment attachment = new ConsignOrderAttachment();
					
					attachment.setId(updateObj.getId());
					attachment.setConsignOrderId(certiId);
					attachment.setPageNumber(updateObj.getPageNum());
					
					consignOrderAttachmentService.updateSelectiveById(attachment); 
				}
			}catch(NullPointerException e){
				log.info(e.getMessage(), e);
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "凭证号："+updateObj.getCredentialCode()+" 不存在！");
			}
			
		}
	}
	
	/**
	 * 更新凭证状态
	 */
	private void doUpdateCertiUpdateStatus(List<BusiConsignOrderCredential> certis){
		for (BusiConsignOrderCredential certi: certis) {
			//根据：certiId从attachmentsByCertiId 里得到，按certiId分组的集合对象，
			//求合就是：total 相当于： consignOrderAttachmentService.totalByCredentialId(certi.getId());
			//int total = attachmentsByCertiId.get(certi.getId()).size();
			
			int total = consignOrderAttachmentService.totalByCredentialId(certi.getId());
			
			Constant.CREDENTIAL_UPLOAD_STATUS uploadStatus = null;
			if(total>0 && certi.getCredentialNum()>total){
				uploadStatus = Constant.CREDENTIAL_UPLOAD_STATUS.PENDING_COLLECT;
			}else if(certi.getCredentialNum() == total){
				uploadStatus = Constant.CREDENTIAL_UPLOAD_STATUS.ALREADY_COLLECT;
			}
			
			if(uploadStatus!=null){
				BusiConsignOrderCredential updateCerti = new BusiConsignOrderCredential();
				updateCerti.setUploadStatus(uploadStatus.toString());
				updateCerti.setCredentialCode(certi.getCredentialCode());
				this.updateByCertSelective(updateCerti);
			}
			
		}
	}
	/**
	 *
	 * @Author: wangxianjun
	 * @Description: 清空订单或订单明细凭证号
	 * @param certCode 凭证号
	 */
	@Override
	public void cleanOrdersByCertCode(Long id,String type,String isBatch,String certCode){
		try {
			if( busiConsignOrderCredentialDao.deleteByPrimaryKey(id) <=0 ){
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "凭证号："+certCode+" 该笔凭证记录删除失败！");
			}
			Map certMap = new HashMap();
			certMap.put("isBatch",isBatch);
			certMap.put("credentialCode","");
			certMap.put("certCode",certCode);
		  if("0".equals(isBatch)) {
			  if ("buyer".equals(type)) {//清空买家凭证
				  if (consignOrderDao.updateOrderByCertCode(certMap) <= 0) {
					  throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "清空订单凭证号失败！");
				  }
			  } else {//清空卖家凭证
				  if (consignOrderItemsDao.updateOrderItemsByCertCode(certMap) <= 0) {
					  throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "清空订单明细凭证号失败！");
				  }
			  }
		  }else{
			  if ("buyer".equals(type)) {//清空买家凭证
				  if (consignOrderDao.updateOrderByBatchCertCode(certMap) <= 0) {
					  throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "清空订单凭证号失败！");
				  }
			  } else {//清空卖家凭证
				  if (consignOrderItemsDao.updateOrderItemsByBatchCertCode(certMap) <= 0) {
					  throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "清空订单明细凭证号失败！");
				  }
			  }
		  }
		}catch(Exception e){
			log.info(e.getMessage(), e);
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "清空订单或订单明细凭证号错误!");
		}
	}
	
}
