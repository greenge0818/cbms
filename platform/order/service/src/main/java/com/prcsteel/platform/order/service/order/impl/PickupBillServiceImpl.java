package com.prcsteel.platform.order.service.order.impl;

import com.prcsteel.platform.account.model.model.Account;
import com.prcsteel.platform.account.service.AccountService;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.acl.persist.dao.OrganizationDao;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.dto.ResultDto;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.common.service.FileService;
import com.prcsteel.platform.common.utils.FileUtil;
import com.prcsteel.platform.order.model.dto.ConsignOrderStatusDto;
import com.prcsteel.platform.order.model.enums.ConsignOrderFillUpStatus;
import com.prcsteel.platform.order.model.enums.ConsignOrderPickupStatus;
import com.prcsteel.platform.order.model.enums.OrderStatusType;
import com.prcsteel.platform.order.model.model.BusiConsignOrderCredential;
import com.prcsteel.platform.order.model.model.ConsignOrder;
import com.prcsteel.platform.order.model.model.ConsignOrderAttachment;
import com.prcsteel.platform.order.model.model.ConsignOrderItems;
import com.prcsteel.platform.order.model.model.DeliveryBill;
import com.prcsteel.platform.order.model.model.DeliveryItems;
import com.prcsteel.platform.order.model.model.OrderAuditTrail;
import com.prcsteel.platform.order.model.model.PickupBill;
import com.prcsteel.platform.order.model.model.PickupItems;
import com.prcsteel.platform.order.model.model.PickupPerson;
import com.prcsteel.platform.order.persist.dao.BusiConsignOrderCredentialDao;
import com.prcsteel.platform.order.persist.dao.ConsignOrderAttachmentDao;
import com.prcsteel.platform.order.persist.dao.ConsignOrderDao;
import com.prcsteel.platform.order.persist.dao.ConsignOrderItemsDao;
import com.prcsteel.platform.order.persist.dao.ConsignOrderProcessDao;
import com.prcsteel.platform.order.persist.dao.ConsignProcessDao;
import com.prcsteel.platform.order.persist.dao.DeliveryBillDao;
import com.prcsteel.platform.order.persist.dao.DeliveryItemsDao;
import com.prcsteel.platform.order.persist.dao.OrderStatusDao;
import com.prcsteel.platform.order.persist.dao.PickupBillDao;
import com.prcsteel.platform.order.persist.dao.PickupItemsDao;
import com.prcsteel.platform.order.persist.dao.PickupPersonDao;
import com.prcsteel.platform.order.service.order.OrderStatusService;
import com.prcsteel.platform.order.service.order.PickupBillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Green.Ge on 2015/7/25.
 */

@Service("pickupBillService")
public class PickupBillServiceImpl implements PickupBillService {
    @Resource
    PickupBillDao pickupBillDao;
    @Resource
    PickupItemsDao pickupItemsDao;
    @Resource
    ConsignOrderDao consignOrderDao;
    @Resource
    ConsignOrderItemsDao consignOrderItemsDao;
    @Resource
    PickupPersonDao pickupPersonDao;
    @Resource
    ConsignProcessDao consignProcessDao;
    @Resource
    AccountService accountService;
    @Resource
    DeliveryBillDao deliveryBillDao;
    @Resource
    DeliveryItemsDao deliveryItemDao;
    @Resource
    OrderStatusDao orderStatusDao;
    @Resource
	OrderStatusService orderStatusService;
    @Resource
    FileService fileService;
    @Resource
    ConsignOrderProcessDao consignOrderProcessDao;
	@Resource
	ConsignOrderAttachmentDao consignOrderAttachmentDao;
	@Resource
	OrganizationDao organizationDao;
	@Resource
	BusiConsignOrderCredentialDao busiConsignOrderCredentialDao;

    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Override
   	public List<HashMap<String, Object>> selectBillDetail(Long orderId) {
   		return pickupItemsDao.selectBillDetail(orderId);
   	}
    //保存提货单green.ge 7/25
	@Override
	@Transactional
	public Map<String,Object> save(PickupBill pb, List<MultipartFile> attachmentfiles,List<PickupItems> detail,
			List<PickupPerson> persons,User user ,String isBillBuyercert,String certCode,String certName,List<MultipartFile> sellerttachmentList,String sellerCertCode,String isBillsellercert,String sellerCertName) {
		boolean isNew = pb.getId()==null;
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("success", true);
		String certificateCode = "";//凭证号
		String sellerCertificateCode = "";//卖家凭证号
		/*提货单保存开始*/
		Long orderId = null;
		if(pb.getId()==null){
			orderId = pb.getConsignOrderId();
		}else{
			orderId = pickupBillDao.selectByPrimaryKey(pb.getId()).getConsignOrderId();
		}
		ConsignOrder order = consignOrderDao.selectByPrimaryKey(orderId);
		certificateCode =order.getBuyerCredentialCode();
		if("1".equals(isBillBuyercert) && !"".equals(certificateCode) &&  certificateCode != null) {
			logger.error("该笔订单已有买家凭证号，提单不能作为买家凭证");
			throw new BusinessException(Constant.EXCEPTIONCODE_UNKONWN, "该订单已在系统中生成交易凭证号:" + certificateCode+"；如要将此“提货单”作为买家凭证，请取消选中“作为买家凭证”确认提交该提货单后，到“交易凭证”处上传该凭证；");
		}
		List<ConsignOrderItems> items =  consignOrderItemsDao.selectByOrderId(orderId);
		sellerCertificateCode = items.get(0).getSellerCredentialCode();
		if("1".equals(isBillsellercert) && !"".equals(sellerCertificateCode) &&  sellerCertificateCode != null) {
			logger.error("该笔订单已有卖家凭证号，提单不能作为卖家凭证");
			throw new BusinessException(Constant.EXCEPTIONCODE_UNKONWN, "该订单已在系统中生成交易凭证号:" + sellerCertificateCode+"；如要将此“提货单”作为卖家凭证，请取消选中“作为卖家凭证”确认提交该提货单后，到“交易凭证”处上传该凭证；");
		}
		PickupBill old = null;
		if(pb.getId()==null){
        	pb.setCreatedBy(user.getLoginId());
        	String seq = "";
        	synchronized(pickupBillDao){
        		seq = pickupBillDao.getPickupBillSeq(orderId);
        	}
             
            pb.setCode("TH-"+pb.getConsignOrderCode()+"-"+seq);
    		pb.setBuyerId(order.getAccountId());
    		pb.setBuyerName(order.getAccountName());
        }else{
        	old = pickupBillDao.selectByPrimaryKey(pb.getId());
        	old.setDeliveryType(pb.getDeliveryType());
        	old.setNote(pb.getNote());
        	old.setModificationNumber(old.getModificationNumber()+1);
        	pb = old;
        }
		pb.setLastUpdatedBy(user.getLoginId());
		
		//String savePath= "";
		/*if(file!=null){
			savePath ="/img/order/pickup/"+new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date())+"."+FileUtil.getFileSuffix(file.getOriginalFilename());
//			FileUtil.saveFile(file, rootPath+savePath);
			String key="";
			try {
				key = fileService.saveFile(file.getInputStream(), savePath);
			} catch (IOException e) {
				logger.error("保存提货单附件出错",e);
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,"保存提货单附件出错");
			}
			pb.setBuyerPickupbillPath(key);
		}*/


		//提货单表头保存
		if(isNew){
			pickupBillDao.insertSelective(pb);
		}else{
			pickupBillDao.updateByPrimaryKeySelective(pb);
		}
		Long pickupBillId = pb.getId();
		//提货人信息保存
		if(!isNew){//全删全插，暴利处理
			pickupPersonDao.deleteByBillId(pickupBillId);
		}
		for(PickupPerson pp:persons){
			pp.setPickupBillId(pickupBillId);
			pickupPersonDao.insertSelective(pp);
		}
		boolean IamPickupBillCreator = pb.getCreatedBy().equals(user.getLoginId());
        boolean IamOrderOwner = order.getOwnerId().equals(user.getLoginId());
		
		int totalQuantity=0;
		BigDecimal totalWeight=new BigDecimal(0);
		BigDecimal totalAmount=new BigDecimal(0);
		try{
			//上传多张图片
			boolean isUpload = true;
			List<String> strPaths =  new ArrayList<String>();
			List<String> sellerPaths =  new ArrayList<String>();
			if (isNew && (attachmentfiles != null || sellerttachmentList != null)) {   //如果没上传图片，则不执行插入
				for(MultipartFile file:attachmentfiles) {
					saveAttachment(file, pickupBillId, user,"ladbill",strPaths);
				}
				for(MultipartFile file:sellerttachmentList) {
					saveAttachment(file, pickupBillId, user,"sellerladbill",sellerPaths);
				}
				isUpload = true;

			}else {
				isUpload = false;
			}
			//买家提单是否作为买家凭证 0 否 1是 
			if("1".equals(isBillBuyercert)){
				//买家凭证提交审核
				//certificateCode =order.getBuyerCredentialCode();
				if("".equals(certificateCode)|| certificateCode == null) {
					//如果凭证没有生成，则重新生成
					certificateCode = certCode;
					ConsignOrder record = new ConsignOrder();
					record.setId(orderId);
					record.setBuyerCredentialCode(certificateCode);
					consignOrderDao.updateByPrimaryKeySelective(record);
					BusiConsignOrderCredential credential = savePrintcateInfo(certificateCode, user, "buyer", certName);
					if (isNew) {   //新增
						for(String strPath:strPaths) {
							saveDeliveyAttachment(credential.getId(), user, "cert", strPath);
						}
					}else{//修改
						    Map billMap = new HashMap();
			                billMap.put("orderId",pb.getId());
			                billMap.put("type","ladbill");
			                List<ConsignOrderAttachment> attachments =  consignOrderAttachmentDao.getAttachmentByOrderId(billMap);
			                for(ConsignOrderAttachment  attachment:attachments) {
								saveDeliveyAttachment(credential.getId(), user, "cert", attachment.getFileUrl());
							}
					}
				}
			}
			//卖家提单是否作为卖家凭证 0 否 1是
			if("1".equals(isBillsellercert)){
				//卖家凭证提交审核
				//certificateCode =order.getBuyerCredentialCode();
				if("".equals(sellerCertificateCode)|| sellerCertificateCode == null) {
					//如果凭证没有生成，则重新生成
					sellerCertificateCode = sellerCertCode;
					ConsignOrderItems item = new ConsignOrderItems();
					item.setOrderId(orderId);
					item.setSellerId(items.get(0).getSellerId());
					item.setSellerCredentialCode(sellerCertificateCode);
					consignOrderItemsDao.updateByOrderIdSelective(item);
					BusiConsignOrderCredential sellerCert = savePrintcateInfo(sellerCertificateCode, user, "seller", sellerCertName);
					if (isNew) {   //新增
						for(String strPath:sellerPaths) {
							saveDeliveyAttachment(sellerCert.getId(), user, "cert", strPath);
						}
					}else{//修改
						Map billMap = new HashMap();
						billMap.put("orderId",pb.getId());
						billMap.put("type","sellerladbill");
						List<ConsignOrderAttachment> attachments =  consignOrderAttachmentDao.getAttachmentByOrderId(billMap);
						for(ConsignOrderAttachment  attachment:attachments) {
							saveDeliveyAttachment(sellerCert.getId(), user, "cert", attachment.getFileUrl());
						}
					}
				}
			}
			
			//表体权限控制，只有提货单创建人本人或订单交易员才可以修改提货数量
			if(IamPickupBillCreator||IamOrderOwner){
				//放货单数据准备 有几家客户，生成几张放货单
				Set<Long> sellers = new HashSet<Long>();
				//提货单表体保存
				if(!isNew){//全删全插，暴利处理
					pickupItemsDao.deleteByBillId(pickupBillId);
				}
				for(PickupItems pi:detail){
					Long orderItemId = pi.getConsignOrderItemId();
					ConsignOrderItems original = consignOrderItemsDao.selectByPrimaryKey(orderItemId);
					
					//准备卖家数据
					sellers.add(original.getSellerId());
					pi.setBillId(pickupBillId);
					pi.setSellerId(original.getSellerId());
					pi.setSellerName(original.getSellerName());
					if(original.getQuantity()-pi.getPickedQuantity()==pi.getPickQuantity()){
						pi.setPickWeight(original.getWeight().subtract(pickupItemsDao.selectPickedWeightByOrderItemId(orderItemId)));//.setScale(6, BigDecimal.ROUND_HALF_DOWN)
					}else{
						pi.setPickWeight(original.getWeight().divide(new BigDecimal(original.getQuantity()),6,BigDecimal.ROUND_HALF_DOWN).multiply(new BigDecimal(pi.getPickQuantity())));
					}
					
					pi.setConsignOrderItemId(original.getId());
					result = checkRealLeftQty(pi,pickupBillId,original,result);
					if(!((Boolean)result.get("success"))) return result;
					totalQuantity+=pi.getPickQuantity();
					totalWeight= totalWeight.add(pi.getPickWeight());
					totalAmount=totalAmount.add(pi.getPickWeight().multiply(original.getDealPrice()));
					
					pickupItemsDao.insertSelective(pi);
				}
				//更新总数量
				pb.setTotalQuantity(totalQuantity);
				pb.setTotalWeight(totalWeight);
				pb.setTotalAmount(totalAmount);
				pickupBillDao.updateByPrimaryKeySelective(pb);
				//是否全录入
				if(pickupBillDao.checkIfAllInput(pb.getConsignOrderId())==0){
					order.setPickupStatus(ConsignOrderPickupStatus.ALL_ENTRY.getCode());
					OrderAuditTrail last = orderStatusDao.getLastStatus(orderId, OrderStatusType.PICKUP.toString());
					//根据提货单的上一次插入记录判断是否需要插auditTrial表
					if(last==null||!String.valueOf(ConsignOrderPickupStatus.ALL_ENTRY.getCode()).equals(last.getSetToStatus())){
						orderStatusService.insertOrderAuditTrail(pb.getConsignOrderId(), OrderStatusType.PICKUP.toString(), user, String.valueOf(ConsignOrderPickupStatus.ALL_ENTRY.getCode()));
					}
				}else{
					order.setPickupStatus(ConsignOrderPickupStatus.PART_ENTRY.getCode());
					OrderAuditTrail last = orderStatusDao.getLastStatus(orderId, OrderStatusType.PICKUP.toString());
					//根据提货单的上一次插入记录判断是否需要插auditTrial表
					if(last==null||!String.valueOf(ConsignOrderPickupStatus.PART_ENTRY.getCode()).equals(last.getSetToStatus())){
						orderStatusService.insertOrderAuditTrail(pb.getConsignOrderId(), OrderStatusType.PICKUP.toString(), user, String.valueOf(ConsignOrderPickupStatus.PART_ENTRY.getCode()));
					}
				}
				result.put("orderPickUpStatus", order.getPickupStatus());
				consignOrderDao.updateByPrimaryKeySelective(order);
				//更新订单提货单状态字段
				int totalPickedQuantity = pickupBillDao.getOrderPickedQty(orderId);
				int totalOrderQuantity = order.getTotalQuantity();
				Map<String,Object> paramMap =new HashMap<String, Object>();
				paramMap.put("id", orderId);
				paramMap.put("lastUpdated", new Date());
				paramMap.put("lastUpdatedBy", user.getLoginId());
				
				if(totalPickedQuantity==totalOrderQuantity){
					paramMap.put("pickupStatus", ConsignOrderPickupStatus.ALL_ENTRY.getCode());//全部录入
				}else{
					paramMap.put("pickupStatus", ConsignOrderPickupStatus.PART_ENTRY.getCode());//部分录入
				}
				orderStatusDao.updateOrderStatus(paramMap);
			
				/*提货单保存结束*/
				//if(isUpload){//买家提货单已经录入 modify by wangxianjun 提货单变化了，对应的放货单也要变化
					
					/*放货单保存开始*/
					if(!isNew){//全删全插，暴利处理
						deliveryBillDao.deleteByPickupId(pickupBillId);
						deliveryItemDao.deleteByPickupId(pickupBillId);
					}
					for(Long sellerId:sellers){
						totalQuantity=0;
						totalWeight=new BigDecimal(0);
						totalAmount=new BigDecimal(0);
						Account seller = accountService.queryById(sellerId);
						DeliveryBill db = new DeliveryBill();
						synchronized (deliveryBillDao) {//放货单号
							db.setCode(pb.getCode().replaceFirst("TH-", "FH-")+"-"+deliveryBillDao.getDeliveryBillSeq(pickupBillId));
						}
						db.setConsignOrderId(order.getId());//订单号
						db.setConsignOrderCode(order.getCode());
						db.setPickupId(pb.getId());//提单号
						db.setPickupCode(pb.getCode());
						db.setBuyerId(order.getAccountId());//买家
						db.setBuyerName(order.getAccountName());
						db.setSellerId(seller.getId());//卖家
						db.setSellerName(seller.getName());
						db.setNote(pb.getNote());
						db.setPrintTimes(0);//打印次数
						db.setDeliveryType(pb.getDeliveryType());
						db.setCreatedBy(user.getLoginId());
						db.setLastUpdatedBy(user.getLoginId());
						/*if(file!=null){
							db.setBuyerPickupbillPath(savePath);//买家提单保存地址
						}*/

						deliveryBillDao.insertSelective(db);
						Long deliveryBillId = db.getId();
						if (isNew && (strPaths.size() > 0 || sellerPaths.size() > 0)) {   //如果没上传图片，则不执行插入 买家提单保存地址
							for(String strPath:strPaths) {
								saveDeliveyAttachment(deliveryBillId, user, "deliverybill", strPath);
							}
							for(String strPath:sellerPaths) {
								saveDeliveyAttachment(deliveryBillId, user, "deliverybill", strPath);
							}
						}
						for(PickupItems pi:detail){
							if(seller.getId().equals(pi.getSellerId())){
								DeliveryItems di = new DeliveryItems();
								di.setBillId(deliveryBillId);
								di.setPickupId(pickupBillId);
								di.setConsignOrderItemId(pi.getConsignOrderItemId());
								di.setPickedQuantity(pi.getPickedQuantity());
								di.setPickQuantity(pi.getPickQuantity());
								di.setPickWeight(pi.getPickWeight());
								di.setCreatedBy(user.getLoginId());
								di.setLastUpdatedBy(user.getLoginId());
								deliveryItemDao.insertSelective(di);
								totalQuantity+=pi.getPickQuantity();
								totalWeight=totalWeight.add(pi.getPickWeight());
								Long orderItemId = pi.getConsignOrderItemId();
								ConsignOrderItems original = consignOrderItemsDao.selectByPrimaryKey(orderItemId);
								totalAmount=totalAmount.add(pi.getPickWeight().multiply(original.getDealPrice()));
							}
						}
						//更新表头总量
						db.setTotalQuantity(totalQuantity);
						db.setTotalWeight(totalWeight);
						db.setTotalAmount(totalAmount);
						deliveryBillDao.updateByPrimaryKeySelective(db);
						//订单放货单状态为未全打印
						order.setFillupStatus(ConsignOrderFillUpStatus.NO_PRINT_ALL.getCode());
						OrderAuditTrail last = orderStatusDao.getLastStatus(orderId, OrderStatusType.FILLUP.toString());
						//根据放货单的上一次插入记录判断是否需要插auditTrial表
						if(last==null||!String.valueOf(ConsignOrderFillUpStatus.NO_PRINT_ALL.getCode()).equals(last.getSetToStatus())){
							orderStatusService.insertOrderAuditTrail(pb.getConsignOrderId(), OrderStatusType.FILLUP.toString(), user, String.valueOf(ConsignOrderFillUpStatus.NO_PRINT_ALL.getCode()));
						}
						consignOrderDao.updateByPrimaryKeySelective(order);
					}
				//}
			}
			if(isUpload){
				ConsignOrder co = consignOrderDao.selectByPrimaryKey(orderId);
				ConsignOrderStatusDto cosd = consignOrderProcessDao.getByUserIdAndStatus(co.getOwnerId(), "FILLEDUP");
				if(cosd==null){
					throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "请先设置业务员【"+co.getOwnerName()+"】的【待开放货涵】操作人员");
				}else{
					result.put("data","提货单已经成功录入<br/>"+cosd.getOperaterName()+"会及时打印对应的【放货单】");
				}
				
			}else{
				result.put("data","提货单已经成功录入");
			}
			
		}catch(BusinessException be){
			throw be;
		}catch(Exception e){
			logger.error("保存提货单出错",e);
			throw new BusinessException(Constant.EXCEPTIONCODE_UNKONWN, "保存提货单出错");
		}
		/*放货单保存结束*/
		return result;
	}
	
	/**
	 *
	 * @Author: wangxianjun
	 * @Description: 保存凭证信息
	 * @Date: 2016年5月17日
	 */
	public BusiConsignOrderCredential savePrintcateInfo(String certificateCode,User user,String type,String certName){
		    BusiConsignOrderCredential credential = new BusiConsignOrderCredential();
			credential.setCredentialCode(certificateCode);
			credential.setType(type);
			credential.setName(certName);
			credential.setStatus("PENDING_APPROVAL");
			credential.setIsBillBuyercert(true);
			credential.setIsDeleted(false);
			credential.setCreated(new Date());
			credential.setSubmitDate(new Date());
			credential.setSubmitedBy(user.getName());
			credential.setPrintedBy(user.getName());
			if (busiConsignOrderCredentialDao.insertSelective(credential) <= 0) {
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "凭证号："+certificateCode+"的相关信息保存失败！");
			}
			return busiConsignOrderCredentialDao.selectByCert(certificateCode);
	}
	private void saveAttachment(MultipartFile file, Long orderId, User user,String type,List<String> strPaths) {
		ConsignOrderAttachment consignOrderAttachment = new ConsignOrderAttachment();
		consignOrderAttachment.setConsignOrderId(orderId);
		consignOrderAttachment.setCreated(new Date());
		consignOrderAttachment.setCreatedBy(user.getName());
		consignOrderAttachment.setLastUpdated(new Date());
		consignOrderAttachment.setLastUpdatedBy(user.getName());
		consignOrderAttachment.setModificationNumber(0);
		consignOrderAttachment.setType(type);

		String savePath = "";
		String saveKey="";
		if(file!=null) {
			savePath = "/img/order/pickup/" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + "." + FileUtil.getFileSuffix(file.getOriginalFilename());
			try {
				saveKey = fileService.saveFile(file.getInputStream(), savePath);
			} catch (IOException e) {
				logger.error("保存附件出错", e);
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "保存附件出错");
			}
		}
		strPaths.add(saveKey);
		consignOrderAttachment.setFileUrl(saveKey);
		if (consignOrderAttachmentDao.insertSelective(consignOrderAttachment) == 0) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "保存附件失败");
		}
	}
	private void saveDeliveyAttachment(Long deliveryId, User user,String type,String strPath) {
		ConsignOrderAttachment consignOrderAttachment = new ConsignOrderAttachment();
		consignOrderAttachment.setConsignOrderId(deliveryId);
		consignOrderAttachment.setCreated(new Date());
		consignOrderAttachment.setCreatedBy(user.getName());
		consignOrderAttachment.setLastUpdated(new Date());
		consignOrderAttachment.setLastUpdatedBy(user.getName());
		consignOrderAttachment.setModificationNumber(0);
		consignOrderAttachment.setType(type);
		consignOrderAttachment.setFileUrl(strPath);
		if (consignOrderAttachmentDao.insertSelective(consignOrderAttachment) == 0) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "保存放货单附件失败");
		}
	}

	private Map<String,Object> checkRealLeftQty(PickupItems pi,Long billId,ConsignOrderItems original,Map<String,Object> result){
		//提交时的已提货数量
		int realPickedQty = pickupItemsDao.selectPickedQtyByOrderItemId(original.getId());
		pi.setPickedQuantity(realPickedQty);
		int realLeftQty = original.getQuantity() - realPickedQty;
		if(realLeftQty<pi.getPickQuantity()){
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,"商品："+original.getNsortName()+" "+original.getMaterial() +" "+ original.getSpec() +"实际已提数量为："+realPickedQty+",不能再提"+pi.getPickQuantity()+"件");
		}
		return result;
	}
	@Override
	public PickupBill selectByPrimaryKey(Long id) {
		return pickupBillDao.selectByPrimaryKey(id);
	}
	@Override
	public List<HashMap<String, Object>> selectByOrderId(Long orderId) {
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String,Object>>();
		List<PickupBill> pickupBills = pickupBillDao.selectByOrderId(orderId);
		for(PickupBill pb:pickupBills){
			HashMap<String,Object> pickupInfo = new HashMap<String, Object>();
			//是否有已打印的放货单
			pb.setExt4(deliveryBillDao.getPrintedDeliveryBillCountByPickupId(pb.getId()));
			//放货单张数
			pb.setExt5(deliveryBillDao.getListByPickupId(pb.getId()).size());
			pickupInfo.put("head", pb);
			
			List<HashMap<String,Object>> items = pickupItemsDao.selectByBillIdForEdit(pb.getId());
			pickupInfo.put("detail", items);
			list.add(pickupInfo);
		}
		return list;
	}

	@Override
	@Transactional
	public int disableBill(Long id,User user) {
		try{
			pickupBillDao.disableBill(id);
			List<DeliveryBill> billList = deliveryBillDao.getListByPickupId(id);
			deliveryBillDao.disableBillByPickupId(id);
			//删除提货单图片
			Map billMap = new HashMap();
			billMap.put("dbId", id);
			billMap.put("type", "ladbill");
			if (consignOrderAttachmentDao.selectCountByPath(billMap) > 0 && consignOrderAttachmentDao.deleteByPath(billMap) == 0) {
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "删除图片失败");
			}else {
				//删除放货单图片
				for (DeliveryBill db : billList) {
					billMap.put("dbId", db.getId());
					billMap.put("type", "deliverybill");
					if (consignOrderAttachmentDao.selectCountByPath(billMap) > 0 && consignOrderAttachmentDao.deleteByPath(billMap) == 0) {
						throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "删除图片失败！");
					}
				}
			}
			PickupBill pb = pickupBillDao.selectByPrimaryKey(id);
			List<PickupBill> list = pickupBillDao.selectByOrderId(pb.getConsignOrderId());
    		//如果该订单下没有有效的提单，则置提货单状态为未录入
			HashMap<String,Object> paramMap =new HashMap<String, Object>();
			paramMap.put("id", pb.getConsignOrderId());
			paramMap.put("lastUpdated", new Date());
			paramMap.put("lastUpdatedBy", user.getLoginId());
    		if(list.size()==0){
    			paramMap.put("pickupStatus", ConsignOrderPickupStatus.NO_ENTRY.getCode());
    			OrderAuditTrail last = orderStatusDao.getLastStatus(pb.getConsignOrderId(), OrderStatusType.PICKUP.toString());
				//根据提货单的上一次插入记录判断是否需要插auditTrial表
				if(last==null||!String.valueOf(ConsignOrderPickupStatus.NO_ENTRY.getCode()).equals(last.getSetToStatus())){
					orderStatusService.insertOrderAuditTrail(pb.getConsignOrderId(), OrderStatusType.PICKUP.toString(), user, String.valueOf(ConsignOrderPickupStatus.NO_ENTRY.getCode()));
				}
    		}else{//pickupBillDao.checkIfAllInput(pb.getConsignOrderId())>0
    			paramMap.put("pickupStatus",ConsignOrderPickupStatus.PART_ENTRY.getCode());
    			OrderAuditTrail last = orderStatusDao.getLastStatus(pb.getConsignOrderId(), OrderStatusType.PICKUP.toString());
				//根据提货单的上一次插入记录判断是否需要插auditTrial表
				if(last==null||!String.valueOf(ConsignOrderPickupStatus.PART_ENTRY.getCode()).equals(last.getSetToStatus())){
					orderStatusService.insertOrderAuditTrail(pb.getConsignOrderId(), OrderStatusType.PICKUP.toString(), user, String.valueOf(ConsignOrderPickupStatus.PART_ENTRY.getCode()));
				}
    		}
    		orderStatusDao.updateOrderStatus(paramMap);
    		List<DeliveryBill> deliveryList = deliveryBillDao.getListByOrderId(pb.getConsignOrderId());
    		//如果该订单下没有有效的放货单，则置放货单状态为初始化-1
    		paramMap =new HashMap<String, Object>();
			paramMap.put("id", pb.getConsignOrderId());
			paramMap.put("lastUpdated", new Date());
			paramMap.put("lastUpdatedBy", user.getLoginId());
    		if(deliveryList.size()==0){
    			paramMap.put("fillupStatus", ConsignOrderFillUpStatus.INITIAL.getCode());
    			OrderAuditTrail last = orderStatusDao.getLastStatus(pb.getConsignOrderId(), OrderStatusType.FILLUP.toString());
				//根据放货单的上一次插入记录判断是否需要插auditTrial表
				if(last==null||!String.valueOf(ConsignOrderFillUpStatus.INITIAL.getCode()).equals(last.getSetToStatus())){
					orderStatusService.insertOrderAuditTrail(pb.getConsignOrderId(), OrderStatusType.FILLUP.toString(), user, String.valueOf(ConsignOrderFillUpStatus.INITIAL.getCode()));
				}
    		}else{//pickupBillDao.checkIfAllInput(pb.getConsignOrderId())>0
    			paramMap.put("fillupStatus", ConsignOrderFillUpStatus.NO_PRINT_ALL.getCode());
    			OrderAuditTrail last = orderStatusDao.getLastStatus(pb.getConsignOrderId(), OrderStatusType.FILLUP.toString());
				//根据货单的上一次插入记录判断是否需要插auditTrial表
				if(last==null||!String.valueOf(ConsignOrderFillUpStatus.NO_PRINT_ALL.getCode()).equals(last.getSetToStatus())){
					orderStatusService.insertOrderAuditTrail(pb.getConsignOrderId(), OrderStatusType.FILLUP.toString(), user, String.valueOf(ConsignOrderFillUpStatus.NO_PRINT_ALL.getCode()));
				}
    		}
		}catch(Exception e){
			logger.error("删除提货单出错:"+e.getMessage());
			throw new BusinessException(Constant.EXCEPTIONCODE_UNKONWN, "删除提货单出错");
		}
		return 1;
	}
	@Override
	public HashMap<String,Object> getBillPrintInfoById(Long id) {
		HashMap<String,Object> pickupBill = new HashMap<String, Object>();
		HashMap<String,Object> head = pickupBillDao.getHead(id);
		pickupBill.put("head", head);
		List<HashMap<String,Object>> detail = pickupItemsDao.getPrintListById(id);
		pickupBill.put("detail", detail);
		List<PickupPerson> persons = pickupPersonDao.selectByBillId(id);
		pickupBill.put("persons", persons);
//		pickupBill.put("now", new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()));
		return pickupBill;
	}
	@Override
	public void printPickupBill(Long id, User user) {
		PickupBill pb = pickupBillDao.selectByPrimaryKey(id);
		pb.setPrintTimes((pb.getPrintTimes()==null?0:pb.getPrintTimes())+1);
		pb.setLastUpdated(new Date());
		pb.setLastUpdatedBy(user.getLoginId());
		pb.setModificationNumber((pb.getModificationNumber() == null ? 0 : pb.getModificationNumber()) + 1);
		pickupBillDao.updateByPrimaryKeySelective(pb);
	}

	/**
	 * 上传买家提单图片
	 * modify by wangxianjun 20160229
	 *
	 * @return
	 */
	@Override
	public ResultDto updateBillPic(List<MultipartFile> attachmentfiles, User user,Long billId,String billType) {
		ResultDto result = new ResultDto();
		List<String> strPaths = new ArrayList<String>();
		if (attachmentfiles != null) {   //如果没上传图片，则不执行插入 上传提货单
			for(MultipartFile file:attachmentfiles) {
				saveAttachment(file, billId, user,billType,strPaths);
			}
			List<DeliveryBill> billList = deliveryBillDao.getListByPickupId(billId);
			for(DeliveryBill db :billList){
				for(String strPath:strPaths){
					saveDeliveyAttachment(db.getId(), user, "deliverybill", strPath);
				}
			}
			result.setSuccess(true);
		}else {
			result.setSuccess(false);
		}
		return result;
	}
	/**
	 * 上传放货单图片
	 * modify by wangxianjun 20160229
	 *
	 * @return
	 */
	@Override
	public ResultDto updateDeliveryBillPic(List<MultipartFile> attachmentfiles, User user,Long billId) {
		ResultDto result = new ResultDto();
		List<String> strPaths = new ArrayList<String>();
		if (attachmentfiles != null) {   //如果没上传图片，则不执行插入 上传提货单
			for(MultipartFile file:attachmentfiles) {
				saveAttachment(file, billId, user,"deliverybill",strPaths);
			}

			result.setSuccess(true);
		}else {
			result.setSuccess(false);
		}
		return result;
	}
	/**
	 * 删除买家提单图片
	 * modify by wangxianjun 20160229
	 *
	 * @return
	 */
	@Override
	public ResultDto deletePic(Long imgId,Long pbId) {
		ResultDto result = new ResultDto();
		Map billMap = null;
		ConsignOrderAttachment orderAttachment = consignOrderAttachmentDao.selectByPrimaryKey(imgId);
		//删除提货单图片
		if (consignOrderAttachmentDao.deleteByPrimaryKey(imgId) == 0) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "删除图片失败");
		}else{
			List<DeliveryBill> billList = deliveryBillDao.getListByPickupId(pbId);
			//删除放货单图片
			for(DeliveryBill db :billList){
				billMap = new HashMap();
				billMap.put("dbId", db.getId());
				billMap.put("type","deliverybill");
				billMap.put("strPath", orderAttachment.getFileUrl());
				if(consignOrderAttachmentDao.selectCountByPath(billMap) > 0 && consignOrderAttachmentDao.deleteByPath(billMap) == 0){
					throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "删除图片失败！");
				}
			}
			result.setSuccess(true);
		}
		return result;
	}
	//通过订单ID查找提货单
	@Override
	public List<PickupBill> selectPickupsByOrderId(Long orderId){
		return pickupBillDao.selectByOrderId(orderId);
	}
}