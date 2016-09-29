package com.prcsteel.platform.order.service.order.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;

import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.order.model.model.*;
import com.prcsteel.platform.order.persist.dao.*;
import com.prcsteel.platform.common.service.FileService;
import com.prcsteel.platform.order.service.order.DeliveryBillService;
import com.prcsteel.platform.order.service.order.OrderStatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.prcsteel.platform.common.utils.FileUtil;
import com.prcsteel.platform.common.utils.Tools;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.order.model.dto.DeliveryItemDto;
import com.prcsteel.platform.order.model.enums.ConsignOrderFillUpStatus;
import com.prcsteel.platform.order.model.enums.ConsignOrderStatus;
import com.prcsteel.platform.order.model.enums.DeliveryOrderType;
import com.prcsteel.platform.order.model.enums.OrderStatusType;

/**
 * Created by Green.Ge on 2015/7/28.
 */

@Service("DeliveryBillService")
public class DeliveryBillServiceImpl implements DeliveryBillService {

	@Resource
	DeliveryBillDao deliveryBillDao;
	@Resource
	DeliveryItemsDao dliveryItemsDao;
	@Resource
	ConsignOrderItemsDao orderItemDao;
	@Resource
	PickupPersonDao pickupPersonDao;
	@Resource
	ConsignOrderDao consignOrderDao;
	@Resource
	PickupBillDao pickupBillDao;
	@Resource
	FileService fileService;
	@Resource
	OrderStatusDao orderStatusDao;
	@Resource
	OrderStatusService orderStatusService;
	@Resource
	DeliveryPrintTimesDao deliveryPrintTimesDao;

	Logger logger = LoggerFactory.getLogger(this.getClass().getName());

	@Override
	public int getPrintedDeliveryBillCountByPickupId(Long pickupId) {
		return deliveryBillDao.getPrintedDeliveryBillCountByPickupId(pickupId);
	}

	@Override
	public List<HashMap<String, Object>> getBillByPickupId(Long pickupId) {
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		List<DeliveryBill> deliveryList = deliveryBillDao
				.getListByPickupId(pickupId);
		for (DeliveryBill db : deliveryList) {
			HashMap<String, Object> deliveryBill = new HashMap<String, Object>();
			HashMap<String, Object> head = deliveryBillDao.getHead(db.getId());
			deliveryBill.put("head", head);
			List<HashMap<String, Object>> detail = dliveryItemsDao
					.getListByDeliveryId(db.getId());
			deliveryBill.put("detail", detail);
			list.add(deliveryBill);
		}
		return list;
	}

	@Override
	public List<DeliveryItemDto> selectByBillIdForEdit(Long billId) {
		List<DeliveryItemDto> dtos = new LinkedList<DeliveryItemDto>();
		List<DeliveryItems> items = dliveryItemsDao.selectByBillId(billId);
		for (int i = 0; i < items.size(); i++) {
			DeliveryItems dItems = items.get(i);
			ConsignOrderItems oItems = orderItemDao.selectByPrimaryKey(dItems
					.getConsignOrderItemId());

			DeliveryItemDto dto = new DeliveryItemDto();
			dto.setdItems(dItems);
			dto.setoItems(oItems);
			dtos.add(dto);
		}
		return dtos;
	}

	@Override
	public DeliveryBill selectById(Long id) {
		return deliveryBillDao.selectByPrimaryKey(id);
	}

	@Override
	public List<PickupPerson> selectPickupPersonByPickupBillId(Long id) {
		return pickupPersonDao.selectByBillId(id);
	}

	@Override
	public boolean save(DeliveryBill db, List<PickupPerson> persons,
			MultipartFile file, User user) {
		/* 放货单保存开始 */
		String savePath = "";
		try {
			//由于现在支持多张图片上传，不需要点提交保存图片，点选择图片支接上传
			/*if (file != null) {
				savePath = "/upload/img/order/pickup/"
						+ new SimpleDateFormat("yyyyMMddHHmmssSSS")
								.format(new Date()) + "."
						+ FileUtil.getFileSuffix(file.getOriginalFilename());
				// FileUtil.saveFile(file, rootPath+savePath);
				String key = "";
				try {
					key = fileService.saveFile(file.getInputStream(), savePath);
				} catch (IOException e) {
					logger.error("保存放货单附件失败：" + e.getMessage());
					throw new BusinessException(
							Constant.EXCEPTIONCODE_BUSINESS, "保存放货单附件失败");
				}
				db.setBuyerPickupbillPath(key);
			}*/
			deliveryBillDao.updateByPrimaryKeySelective(db);

			Long pickupBillId = db.getPickupId();
			pickupPersonDao.deleteByBillId(pickupBillId); // 全删全插，暴利处理
			for (PickupPerson pp : persons) {
				pp.setPickupBillId(pickupBillId);
				pickupPersonDao.insertSelective(pp);
			}
		} catch (Exception e) {
			logger.error("变更放货单失败:" + e.getMessage());
			throw new BusinessException(Constant.EXCEPTIONCODE_UNKONWN,
					"变更放货单失败");
		}
		return true;
	}

	@Override
	public List<HashMap<String, Object>> getBillByOrderId(Long orderId, String deliveryType) {
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		List<PickupBill> pickupList = pickupBillDao.selectByOrderId(orderId);
		for (PickupBill pb : pickupList) {
			List<DeliveryBill> deliveryList = deliveryBillDao
					.getListByPickupId(pb.getId());
			for (DeliveryBill db : deliveryList) {
				HashMap<String, Object> deliveryBill = new HashMap<String, Object>();
				HashMap<String, Object> head = deliveryBillDao.getHead(db.getId());
				head.put("print_times", deliveryPrintTimesDao.countByDeliveryIdAndType(db.getId(), deliveryType));
				
				//deliveryOrderCode  //这个字段其它地没有被引用过
				if(DeliveryOrderType.PICKUPFORBUYER.toString().equals(deliveryType)) {
		        	//查询给买家的提货号，如果没有则生成一个保存起来。
					//TODO 
					Object deliveryOrderCode = head.get("deliveryOrderCode");
					if(deliveryOrderCode==null){
						//生成一个新的提货单号
						String doc = generateDeliveryOrderCode((String)head.get("seq_code"));
						head.put("deliveryOrderCode", doc.substring(2));
						
						//保存到数据库
						DeliveryBill bill = new DeliveryBill();
						bill.setId((Long)head.get("id"));
						bill.setDeliveryOrderCode(doc);
						bill.setDeliveryOrderCodeCreated(new Date());
						deliveryBillDao.updateByPrimaryKeySelective(bill);
						
					}else{
						head.put("deliveryOrderCode", ((String)deliveryOrderCode).substring(2));
					}
		        }
				
				
				deliveryBill.put("head", head);
				List<HashMap<String, Object>> detail = dliveryItemsDao
						.getListByDeliveryId(db.getId());
				deliveryBill.put("detail", detail);
				List<PickupPerson> persons = pickupPersonDao.selectByBillId(pb
						.getId());
				deliveryBill.put("persons", persons);
				// deliveryBill.put("now", new
				// SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()));
				list.add(deliveryBill);
			}
		}
		return list;
	}
	
	/**
	 * 生成新的提货同单号，用于买家提货单
	 * 新的提单号生成规则：类型：07 + 服务中心代码 + 6位年月日 + 5位流水号
	 * 例如：杭州服务中心在2016年03月03日生成的第5个提单号，后台编号为07HZ16030300005；前台显示为：HZ16030300005
	 * 前台显示时，取新的放货单号
	 * tuxianming
	 */
	private String generateDeliveryOrderCode(String orgCode) {
		String typeCode = "07";  //一个类型，区别于，提货号，放货号，之类 的
		
		Date date = new Date();
		
		//得到最后的一条记录，并解析单号，如果是当天，则在原有的基本上加+1
		//如果不是当天，则重新生成。
		
		DeliveryBill lastRecord = deliveryBillDao.queryLastRecord();
		
		String lastDeliveryOrderCode = lastRecord.getDeliveryOrderCode();
		List<Object> lastDeliveryOrderCodeChips = parseDeliveryOrderCode(lastDeliveryOrderCode);
		
		int number = 1;
		String currDateStr = Tools.dateToStr(date, "yyMMdd");
		if(lastDeliveryOrderCodeChips!=null){
			
			//如果时间相等，则在原有的流水号上面+1,
			if(currDateStr.equals(lastDeliveryOrderCodeChips.get(2))){
				number = ((int) lastDeliveryOrderCodeChips.get(3))+1;
			}
			
		}

		String newCode = typeCode+orgCode+currDateStr+String.format("%05d", number);
		return newCode;
	
	}
	
	/**
	 * 解析提货单号并返回一个数组(特指买家提货单号)
	 * 提单号格式：类型：07 + 服务中心代码 + 6位年月日 + 5位流水号
	 * @param lastDeliveryOrderCode
	 * @return List<Object> 为四条：
	 * 		index=0 : String	类型
	 * 		index=1 : String	服务中心代码
	 * 		index=2 : String	6位年月日yyMMdd
	 * 		index=3 : Integer	流水号
	 */
	private List<Object> parseDeliveryOrderCode(String lastDeliveryOrderCode) {
		
		//由于服务中心代码长度不一定，所以先截取其它的，但服务中心的代码至少大于等于两位
		if(lastDeliveryOrderCode==null || lastDeliveryOrderCode.length()<15)
			return null;
		
		List<Object> chips =  new ArrayList<Object>();
		
		String type = lastDeliveryOrderCode.substring(0,2);
		lastDeliveryOrderCode = lastDeliveryOrderCode.substring(2);
		
		int number = Integer.parseInt(lastDeliveryOrderCode.substring(lastDeliveryOrderCode.length()-5));
		lastDeliveryOrderCode = lastDeliveryOrderCode.substring(0, lastDeliveryOrderCode.length()-5);
		
		String dateStr = lastDeliveryOrderCode.substring(lastDeliveryOrderCode.length()-6);
		
		//最后的剩下的就是服务中心code
		String orgCode = lastDeliveryOrderCode.substring(0, lastDeliveryOrderCode.length()-6);
		
		chips.add(0, type);
		chips.add(1, orgCode);
		chips.add(2, dateStr);
		chips.add(3, number);
		
		return chips;
	}
	
	@Override
	public void printDelivery(Long id, String deliveryType, User user) {
		DeliveryBill db = deliveryBillDao.selectByPrimaryKey(id);
		db.setPrintTimes((db.getPrintTimes() == null ? 0 : db.getPrintTimes()) + 1);
		db.setLastUpdated(new Date());
		db.setLastUpdatedBy(user.getLoginId());
		db.setModificationNumber((db.getModificationNumber() == null ? 0 : db
				.getModificationNumber()) + 1);
		deliveryBillDao.updateByPrimaryKeySelective(db);    //主表打印次数

		//分类型插入四张不同类型的放货单打印次数
		if(deliveryPrintTimesDao.countByDeliveryIdAndType(id, deliveryType) == 0){
			DeliveryPrintTimes printTimes = new DeliveryPrintTimes(id, deliveryType, user.getLoginId());
			if(deliveryPrintTimesDao.insertSelective(printTimes) != 1){
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "记录放货单打印次数出错");
			}
		}else{
			if(deliveryPrintTimesDao.addPrintTimes(id, deliveryType) != 1){
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "增加放货单打印次数出错");
			}
		}

		ConsignOrder co = consignOrderDao.queryById(db.getConsignOrderId());
		if (deliveryBillDao.checkIfAllPrinted(co.getId()) == 0 &&
				co.getFillupStatus().intValue() == ConsignOrderFillUpStatus.NO_PRINT_ALL.getCode()) {

			co.setFillupStatus(ConsignOrderFillUpStatus.PRINT_ALL
					.getCode());// 已全打印

			OrderAuditTrail last = orderStatusDao.getLastStatus(co.getId(),
					OrderStatusType.FILLUP.toString());
			// 根据放货单的上一次插入记录判断是否需要插auditTrial表
			if (last == null
					|| !String.valueOf(ConsignOrderFillUpStatus.PRINT_ALL
									.getCode()).equals(last.getSetToStatus())) {
				orderStatusService
						.insertOrderAuditTrail(
								co.getId(),
								OrderStatusType.FILLUP.toString(),
								user,
								String.valueOf(ConsignOrderFillUpStatus.PRINT_ALL
										.getCode()));
			}
			co.setStatus(ConsignOrderStatus.SECONDSETTLE.getCode());// 6
																				// SECONDSETTLE
																				// 待二次结算
			last = orderStatusDao.getLastStatus(co.getId(),
					OrderStatusType.MAIN.toString());
			// 根据放货单的上一次插入记录判断是否需要插auditTrial表
			if (last == null
					|| !ConsignOrderStatus.SECONDSETTLE.getCode()
							.equals(last.getSetToStatus())) {
				orderStatusService.insertOrderAuditTrail(co.getId(),
						OrderStatusType.MAIN.toString(), user,
						ConsignOrderStatus.SECONDSETTLE.getCode());
			}

			co.setLastUpdated(new Date());
			co.setLastUpdatedBy(user.getLoginId());
			co.setModificationNumber((co.getModificationNumber() == null ? 0 : co
					.getModificationNumber()) + 1);
			consignOrderDao.updateByPrimaryKeySelective(co);
		}
		// 不可逆
		// else{
		// co.setFillupStatus(0);//未全打印
		//
		// }

	}

}