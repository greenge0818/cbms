package com.prcsteel.platform.smartmatch.web.mq;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.acl.service.UserService;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.smartmatch.model.dto.InquiryOrderMessageQueue;
import com.prcsteel.platform.smartmatch.model.dto.PurchaseOrderItemsAttributeDto;
import com.prcsteel.platform.smartmatch.model.dto.PurchaseOrderItemsDto;
import com.prcsteel.platform.smartmatch.model.model.MqInquiryOrder;
import com.prcsteel.platform.smartmatch.model.model.MqInquiryOrderItem;
import com.prcsteel.platform.smartmatch.model.model.PurchaseOrder;
import com.prcsteel.platform.smartmatch.service.PurchaseOrderService;


/**
 * @description 分检系统询价单监听
 * @author zhoucai
 * @date 2016-6-12
 * @version ：1.0
 * 
 */
@Component
public class AssInquiryLitener extends QueueListenerAbstract {
	
    private Logger logger = LoggerFactory.getLogger(getClass());
	
    @Autowired
    PurchaseOrderService purchaseOrderService;
    
    @Resource
    UserService userService;
	
	@Override
	protected boolean doProcess(String reqContent) {
	    
		logger.info("=====smartmatch接收ass的需求单信息，报文是接收的信息是====:"+reqContent);
        //将接收的json对象转换为实体对象
		ObjectMapper objMap = new ObjectMapper();
		InquiryOrderMessageQueue msg = null;
		try {
			MqInquiryOrder inquiryOrder = objMap.readValue(reqContent, MqInquiryOrder.class);
			if(inquiryOrder != null){
				 msg = createMessageQueue(reqContent, inquiryOrder);
				//推送过来的数据转换成找货保存的数据对象
				PurchaseOrder purchaseOrder = new PurchaseOrder();
				purchaseOrder.setBuyerName(inquiryOrder.getAccountName());
				purchaseOrder.setRequirementCode(inquiryOrder.getRequirementCode());
				purchaseOrder.setTel(inquiryOrder.getContactMobile());
				purchaseOrder.setContact(inquiryOrder.getContactName());
				purchaseOrder.setDeliveryCityId(inquiryOrder.getCityId() == null ? null :Long.parseLong(inquiryOrder.getCityId().toString()));
				//add by caosulin 增加一个交货地名称
				purchaseOrder.setDeliveryCityName(inquiryOrder.getCityName());
				
				purchaseOrder.setRemark(inquiryOrder.getRemark());
				purchaseOrder.setExt1(inquiryOrder.getInquiryCode());//推送过来的询价单号,保存到其他字段
				purchaseOrder.setExt2(inquiryOrder.getReqSource());//记录数据来源,后续使用
				User user = userService.queryByLoginId(inquiryOrder.getLoginId());
				if(inquiryOrder.getInquiryItems() != null && user != null){
					List<PurchaseOrderItemsDto> itemList = new ArrayList<PurchaseOrderItemsDto>();
					PurchaseOrderItemsDto dto = null;
					Date time = Calendar.getInstance().getTime();
					for(MqInquiryOrderItem item:inquiryOrder.getInquiryItems()){
						dto = new PurchaseOrderItemsDto(item.getCategoryId(), item.getMaterialId(),item.getFactoryId(),
								new BigDecimal(item.getWeight()),item.getSaleNumber() == null ? 0 :new Integer(item.getSaleNumber()),
										item.getSpec1(),item.getSpec2(), item.getSpec3(), time, "");
						dto.setAttributeList(pareseAttributeDto(item.getNsortAttribute()));
						dto.setRemark(item.getRemark());//详情备注,
						//add by zhoucai@prcsteel.com 2016-9-14 详情新增计重方式
						dto.setWeightConcept(item.getWeightConcept());
						//品名设置
						dto.setCategoryName(item.getCategoryName());
						//材质设置
						dto.setMaterialName(item.getMaterialName());
						//工厂设置
						dto.setFactoryNames(item.getFactoryName());
						itemList.add(dto);
					}
					purchaseOrder = purchaseOrderService.save(purchaseOrder, itemList, user);
				}
				purchaseOrderService.insertInquiryMsg(msg);
			}
		} catch (Exception e) {
			logger.error("解析参数失败:" + e.getMessage());
			if(msg == null){
				msg = new InquiryOrderMessageQueue();
				msg.setExt1(reqContent);
			}
			//记录异常信息
			if(e instanceof BusinessException){
				msg.setExt2(((BusinessException)e).getMsg());
			}else{
				msg.setExt2(e.getMessage());
			}
			purchaseOrderService.insertInquiryMsg(msg);
		}
        return true;
	}

	@Override
	protected boolean doProcess(Object reqObj) {
		return false;
	}

	/**
	 * 创建日志消息保存对象
	 * @param reqContent
	 * @param inquiryOrder
	 * @return
	 */
	private InquiryOrderMessageQueue createMessageQueue(String reqContent,MqInquiryOrder inquiryOrder){
		InquiryOrderMessageQueue msg = new InquiryOrderMessageQueue();
		msg.setCode(inquiryOrder.getRequirementCode());
		msg.setPushBy(inquiryOrder.getLoginId());
		msg.setReqSource(inquiryOrder.getReqSource());
		msg.setCreated(Calendar.getInstance().getTime());
		msg.setExt1(reqContent);//记录保存的报文
		return msg;
	}
	/**
	 * 资源正则表达转换数据
	 * @param nsortAttribute
	 * @return
	 */
	private List<PurchaseOrderItemsAttributeDto> pareseAttributeDto(String nsortAttribute){
		List<PurchaseOrderItemsAttributeDto> attributeList = new ArrayList<PurchaseOrderItemsAttributeDto>();
		if(StringUtils.isNotEmpty(nsortAttribute)){
			String find = "\"([^\"]*)\":\"([^\"]*)";
	        Pattern pattern = Pattern.compile(find);
	        Matcher matcher = pattern.matcher(nsortAttribute);
	        PurchaseOrderItemsAttributeDto dto;
	        //查询所有名称,根据推送过来的名称获取uuid
	        List<PurchaseOrderItemsAttributeDto> attrList = purchaseOrderService.getAllAttributes();
	        String attrName ="";
	        String attrUuid = "";
	        Map<String,String> allUuitMap = new HashMap<String,String>();
	        for(PurchaseOrderItemsAttributeDto attr:attrList){
	        	allUuitMap.put(attr.getName(), attr.getUuid());
	        }
	        while(matcher.find()){
	        	dto = new PurchaseOrderItemsAttributeDto();
	        	attrName = matcher.group(1);
	        	attrUuid = allUuitMap.get(attrName);
	        	dto.setAttributeUuid(attrUuid);
	        	dto.setValue(matcher.group(2));
	        	attributeList.add(dto);
	        }
		}
		return attributeList;
	}
}

