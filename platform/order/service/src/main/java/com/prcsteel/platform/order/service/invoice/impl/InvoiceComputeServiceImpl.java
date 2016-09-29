package com.prcsteel.platform.order.service.invoice.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prcsteel.platform.common.constants.InvoiceConstant;
import com.prcsteel.platform.order.model.dto.InvoiceApplyDto;
import com.prcsteel.platform.order.model.dto.InvoiceComputeDto;
import com.prcsteel.platform.order.model.dto.InvoiceOrderItemsDto;
import com.prcsteel.platform.order.model.enums.InvoiceOutMainStatus;
import com.prcsteel.platform.order.model.enums.InvoiceType;
import com.prcsteel.platform.acl.model.enums.SysSettingType;
import com.prcsteel.platform.order.model.model.InvOrgInvoiceOutApply;
import com.prcsteel.platform.order.model.model.InvoiceOutApply;
import com.prcsteel.platform.order.model.model.InvoiceOutItemDetail;
import com.prcsteel.platform.order.model.model.InvoiceOutMain;
import com.prcsteel.platform.order.model.model.InvoicePool;
import com.prcsteel.platform.acl.model.model.SysSetting;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.acl.model.query.SysSettingQuery;
import com.prcsteel.platform.order.persist.dao.InvOrgInvoiceOutApplyDao;
import com.prcsteel.platform.order.persist.dao.InvoiceComputeDao;
import com.prcsteel.platform.order.persist.dao.InvoiceOutApplyDao;
import com.prcsteel.platform.order.persist.dao.InvoiceOutItemDetailDao;
import com.prcsteel.platform.order.persist.dao.InvoiceOutMainDao;
import com.prcsteel.platform.order.persist.dao.InvoicePoolDao;
import com.prcsteel.platform.acl.persist.dao.OrganizationDao;
import com.prcsteel.platform.acl.persist.dao.SysSettingDao;
import com.prcsteel.platform.order.service.invoice.InvoiceComputeService;

/**
 * Created by kongbinheng on 2015/8/2.
 */
@Service("invoiceComputeService")
@Transactional
public class InvoiceComputeServiceImpl implements InvoiceComputeService {

    private static final Logger logger = LoggerFactory.getLogger(InvoiceComputeServiceImpl.class);

    @Resource
    InvoicePoolDao invoicePoolDao;
    @Resource
    InvoiceComputeDao invoiceComputeDao;
    @Resource
    InvoiceOutApplyDao invoiceOutApplyDao;
    @Resource
    InvoiceOutMainDao invoiceOutMainDao;
    @Resource
    OrganizationDao organizationDao;
    @Resource
    InvOrgInvoiceOutApplyDao invOrgInvoiceOutApplyDao;
    @Resource
    InvoiceOutItemDetailDao invoiceOutItemDetailDao;
    @Resource
    SysSettingDao sysSettingDao;

    @Override
    public List<InvoiceOutApply> compute(Long orgId, String applyJson) {
        List<InvoiceOutApply> retList = new ArrayList<InvoiceOutApply>();

        // TODO 进项-销项
        //获取服务中心发票池可销项额度列表
        List<InvoicePool> invoicePoolList = invoicePoolDao.queryOrgNsortSpec(orgId, InvoiceType.IN.toString());

        //获取服务中心申请发票额度列表
        List<InvoiceApplyDto> applyList = getApplyList(applyJson);

        Map<String, List<Long>> map = getOwnerAndBuyerIdList(applyList);

        // TODO 根据服务中心申请查询订单明细
        //获取服务中心申请发票关联订单明细额度列表
        List<InvoiceOrderItemsDto> orderItemsList = invoiceComputeDao.queryOrderItems(null, null, null, null, null);
        
        // 结果集
        List<InvoiceOrderItemsDto> resultList = new LinkedList<>();
        
        // TODO 规则一
        distributeRuleFirst(invoicePoolList,applyList,orderItemsList,resultList);
        // TODO 规则二
        distributeRuleSecond(invoicePoolList, applyList, orderItemsList, resultList);
        // TODO 规则三

        // TODO 规则四

        // TODO 规则五

        return retList;
    }

    /**
     * 分配规则一
     * @param poolList
     * @param applyList
     * @param computeList
     * @param resultList
     * @return
     */
    private void distributeRuleFirst(
    		List<InvoicePool> poolList, 
    		List<InvoiceApplyDto> applyList, 
    		List<InvoiceOrderItemsDto> computeList,
    		List<InvoiceOrderItemsDto> resultList){
    	
        // 处理计数为1的订单，直接扣除进项金额
        for (int i=0;i<computeList.size();i++) {
        	InvoiceOrderItemsDto item = computeList.get(i);
			if(item.getCount() != 1){
				continue;
			}
			// 根据品规找到对应的发票池对象
			Optional<InvoicePool> invoicePool = poolList.stream()
					.filter(a -> a.getSortName().equals(item.getNsortName()) 
					&& a.getSpec().equals(item.getSpec())
					&& a.getMaterial().equals(item.getMaterial())).findFirst();
			if(!invoicePool.isPresent()){
				continue;
			}
			// 根据买家、交易员找到对应的申请开票对象
			Optional<InvoiceApplyDto> invoiceApply = applyList.stream()
					.filter(a -> a.getBuyerId().equals(item.getBuyerId()) 
					&& a.getOwnerId().equals(item.getOwnerId())).findFirst();
			if(!invoiceApply.isPresent()){
				continue;
			}
			// 计算并设置实际开票金额
			computeAndSetActualAmount(item,invoicePool.get(),invoiceApply.get());
			// 从计算列表中移除，添加到结果列表
			computeList.remove(i--);
			resultList.add(item);
		}
    }

    /**
     * 分配规则二
     * @param poolList
     * @param applyList
     * @param orderItemsList
     * @return
     */
    private void distributeRuleSecond(
    		List<InvoicePool> poolList, 
    		List<InvoiceApplyDto> applyList, 
    		List<InvoiceOrderItemsDto> computeList,
    		List<InvoiceOrderItemsDto> resultList){
        
    	// 根据买家成交品规种类数量排升序
    	computeList.sort((a,b) -> a.getNsortSpecCount().compareTo(b.getNsortSpecCount()));
    	
    }

    /**
     * 分配规则三
     * @param poolList
     * @param applyList
     * @param orderItemsList
     * @return
     */
    private List<InvoiceOrderItemsDto> distributeRuleThird(List<InvoicePool> poolList, List<InvoiceApplyDto> applyList, List<InvoiceOrderItemsDto> orderItemsList){
        List<InvoiceOrderItemsDto> retOrderItemsList = null;
        return retOrderItemsList;
    }

    /**
     * 分配规则四
     * @param poolList
     * @param applyList
     * @param orderItemsList
     * @return
     */
    private List<InvoiceOrderItemsDto> distributeRuleFourth(List<InvoicePool> poolList, List<InvoiceApplyDto> applyList, List<InvoiceOrderItemsDto> orderItemsList){
        List<InvoiceOrderItemsDto> retOrderItemsList = null;
        return retOrderItemsList;
    }

    /**
	 * 分配规则五 -- 未开票金额小的优先  - TBD
	 *
	 * @param poolList
	 * @param applyList
	 * @param orderItemsList
	 * @return
	 */
	private void distributeRuleFifth(List<InvoicePool> poolList,
			List<InvoiceApplyDto> applyList,
			List<InvoiceOrderItemsDto> computeList,
			List<InvoiceOrderItemsDto> resultList) {
		// 按剩余金额额度排序
		Comparator<InvoiceOrderItemsDto> c = (InvoiceOrderItemsDto a,
				InvoiceOrderItemsDto b) -> a.getSurplusAmount().compareTo(
						b.getSurplusAmount());
		computeList.sort(c);
		for (InvoiceOrderItemsDto invoiceOrderItem : computeList) {
			//根据品名、规格、型号找到发票对象
			List<InvoicePool> ipList = poolList
					.stream()
					.filter((InvoicePool ip) -> (
							invoiceOrderItem.getNsortName().equals(ip.getNsortName())
							&& invoiceOrderItem.getMaterial().equals(ip.getMaterial())
							&& invoiceOrderItem.getSpec().equals(ip.getSpec())))
							.collect(Collectors.toList());
			
			
			InvoicePool ip = (ipList == null || ipList.isEmpty())?null:ipList.get(0);
			if(ipList != null && !ipList.isEmpty())
			{
				//获取该交易员对应买家的可开额度
				List<InvoiceApplyDto> iadList = applyList
						.stream()
						.filter((InvoiceApplyDto iad) -> (
								invoiceOrderItem.getOwnerId().equals(iad.getOwnerId())
								&&invoiceOrderItem.getBuyerId().equals(iad.getBuyerId())))
								.collect(Collectors.toList());
				//开始计算
				computeAndSetActualAmount(invoiceOrderItem,ip,iadList.get(0));
			}
			
		}
		
		//清空已经匹配完的数据，并存放到Result集合
		//TODO		

	}
    
    /**
     * 计算并设置该订单实际的开票金额，更新发票池和申请对象
     * @param invoiceOrderItemsDto
     */
    private void computeAndSetActualAmount(
    		InvoiceOrderItemsDto invoiceOrderItemsDto,
    		InvoicePool invoicePool,
    		InvoiceApplyDto invoiceApply){
    	double availableAmount = invoicePool.getAmount().doubleValue();// 可开票金额
    	double applyAmount = invoiceApply.getLeftUninvoiceAmount().doubleValue();// 剩余的申请但未开票金额
    	// 可开票金额 与 剩余申请金额 取小者，即为实际的开票金额
    	double actualAmount = Math.min(availableAmount, applyAmount);
    	// 设置实开金额
    	invoiceOrderItemsDto.setActualAmount(new BigDecimal(actualAmount));
    	// 更新发票池对象的可开金额
    	invoicePool.setAmount(new BigDecimal(availableAmount - actualAmount));
    	// 更新申请中的未开金额
    	invoiceApply.setLeftUninvoiceAmount(new BigDecimal(applyAmount - actualAmount));
    	// 更新申请中的实开金额
    	invoiceApply.setActualAmount(invoiceApply.getActualAmount().add(new BigDecimal(actualAmount)));
    }

    /**
     * 获取申请数据
     * @param applyJson
     * @return
     */
    public List<InvoiceApplyDto> getApplyList(String applyJson) {
        List<InvoiceApplyDto> applyList = new ArrayList<InvoiceApplyDto>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode applyNode = mapper.readTree(applyJson);
            for (JsonNode node : applyNode) {
                String strLeftUninvoiceAmount = "";
                String strApplyAmount = "";
                String strSecondAmount = "";
                if(StringUtils.isEmpty(node.path("leftUninvoiceAmount").asText())){
                    strLeftUninvoiceAmount = "0";
                }
                if(StringUtils.isEmpty(node.path("applyAmount").asText())){
                    strApplyAmount = "0";
                }
                if(StringUtils.isEmpty(node.path("secondAmount").asText())){
                    strSecondAmount = "0";
                }
                Long applyId = node.path("applyId").asLong();
                InvoiceOutApply invoiceOutApply = invoiceOutApplyDao.selectByPrimaryKey(applyId);
//                Long ownerId = invoiceOutApply.getOwnerId();
//                String ownerName = invoiceOutApply.getOwnerName();
                Long buyerId = node.path("buyerId").asLong();
                String buyerName = node.path("buyerName").asText();
                Long monthDiff = node.path("monthDiff").asLong();
                BigDecimal leftUninvoiceAmount = new BigDecimal(strLeftUninvoiceAmount);
                BigDecimal applyAmount = new BigDecimal(strApplyAmount);
                BigDecimal secondAmount = new BigDecimal(strSecondAmount);
//                InvoiceApplyDto invoiceApplyDto = new InvoiceApplyDto(applyId, ownerId, ownerName, buyerId, buyerName,
//                        monthDiff, leftUninvoiceAmount, applyAmount, secondAmount);
//                applyList.add(invoiceApplyDto);
            }
        } catch (IOException e) {
            logger.error("apply json to object error: ", e);
        }
        return applyList;
    }

    /**
     * 根据服务中心申请获取交易员id和买家id列表
     * @param applyList
     * @return
     */
    private Map<String, List<Long>> getOwnerAndBuyerIdList(List<InvoiceApplyDto> applyList){
        Map<String, List<Long>> map = new HashMap<String, List<Long>>();
        List<Long> ownerIdList = new  ArrayList<Long>();  //交易员id列表
        List<Long> buyerIdList = new  ArrayList<Long>();  //买家id列表
        for(InvoiceApplyDto invoiceApplyDto : applyList){
            ownerIdList.add(invoiceApplyDto.getOwnerId());
            buyerIdList.add(invoiceApplyDto.getBuyerId());
        }
        map.put("ownerIdList", ownerIdList);
        map.put("buyerIdList", buyerIdList);
        return map;
    }

    @Override
    @Transactional
    public boolean billingByOrgId(Long orgId, User user, BigDecimal totalActualAmount, String applyJson) {
        String invoiceSetting = getInvoiceSetting();  //获取发票设置
        BigDecimal taxrate =  InvoiceConstant.TAXRATE;  //税率
        //保存服务中心开票申请
        InvOrgInvoiceOutApply invOrgInvoiceOutApply = saveInvOrgInvoiceOutApply(totalActualAmount, orgId, user);
        invOrgInvoiceOutApplyDao.insertSelective(invOrgInvoiceOutApply);

        List<InvoiceApplyDto> applyList = getApplyList(applyJson);
        for(InvoiceApplyDto invoiceApplyDto : applyList){
            Long buyerId = invoiceApplyDto.getBuyerId();  //买家id
            String buyerName = invoiceApplyDto.getBuyerName();  //买家名称
            Long applyId = invoiceApplyDto.getApplyId();  //申请id
        }
        return true;
    }

    /**
     * 保存服务中心开票申请
     * @param totalActualAmount
     * @param orgId
     * @param user
     * @return
     */
    private InvOrgInvoiceOutApply saveInvOrgInvoiceOutApply(BigDecimal totalActualAmount, Long orgId, User user){
        InvOrgInvoiceOutApply invOrgInvoiceOutApply = new InvOrgInvoiceOutApply();
        invOrgInvoiceOutApply.setAmount(totalActualAmount);
        invOrgInvoiceOutApply.setOrgId(orgId);
        invOrgInvoiceOutApply.setOrgName(organizationDao.queryById(orgId).getName());
        invOrgInvoiceOutApply.setSubmitterId(user.getId());
        invOrgInvoiceOutApply.setSubmitterName(user.getName());
        invOrgInvoiceOutApply.setCreated(new Date());
        invOrgInvoiceOutApply.setCreatedBy(user.getLoginId());
        invOrgInvoiceOutApply.setLastUpdated(new Date());
        invOrgInvoiceOutApply.setLastUpdatedBy(user.getLoginId());
        invOrgInvoiceOutApply.setModificationNumber(0);
        return invOrgInvoiceOutApply;
    }

    /**
     * 保存销项票发票号系统生成表
     * @param orgApplyId
     * @param buyerId
     * @param buyerName
     * @param totalAmount
     * @param totalWeight
     * @param user
     * @return
     */
    private InvoiceOutMain saveInvoiceOutMain(Long orgApplyId, Long buyerId, String buyerName, BigDecimal totalAmount, BigDecimal totalWeight, User user){
        InvoiceOutMain invoiceOutMain = new InvoiceOutMain();
        invoiceOutMain.setOrgApplyId(orgApplyId);
        invoiceOutMain.setCode(invoiceOutMainDao.selectInvoiceOutMainCode(orgApplyId));
        invoiceOutMain.setBuyerId(buyerId);
        invoiceOutMain.setBuyerName(buyerName);
        invoiceOutMain.setTotalAmount(totalAmount);
        invoiceOutMain.setTotalWeight(totalWeight);
        invoiceOutMain.setStatus(InvoiceOutMainStatus.NO_INPUT.getCode());//财务未开票
        invoiceOutMain.setCreated(new Date());
        invoiceOutMain.setCreatedBy(user.getLoginId());
        invoiceOutMain.setLastUpdated(new Date());
        invoiceOutMain.setLastUpdatedBy(user.getLoginId());
        invoiceOutMain.setModificationNumber(0);
        return invoiceOutMain;
    }

    /**
     * 保存开票计算结果品规级
     * @param invoiceOutMainId
     * @param nsortName
     * @param material
     * @param spec
     * @param weight
     * @param price
     * @param noTaxAmount
     * @param taxAmount
     * @param amount
     * @param user
     * @return
     */
    private InvoiceOutItemDetail saveInvoiceOutItemDetail(Long invoiceOutMainId, String nsortName, String material, String spec,
                                                          BigDecimal weight, BigDecimal price, BigDecimal noTaxAmount,
                                                          BigDecimal taxAmount, BigDecimal amount, User user){
        InvoiceOutItemDetail invoiceOutItemDetail = new InvoiceOutItemDetail();
//        invoiceOutItemDetail.setInvoiceOutMainId(invoiceOutMainId);
        invoiceOutItemDetail.setNsortName(nsortName);
        invoiceOutItemDetail.setMaterial(material);
        invoiceOutItemDetail.setSpec(spec);
        invoiceOutItemDetail.setWeight(weight);
        invoiceOutItemDetail.setPrice(price);
        invoiceOutItemDetail.setNoTaxAmount(noTaxAmount);
        invoiceOutItemDetail.setTaxAmount(taxAmount);
        invoiceOutItemDetail.setAmount(amount);
        invoiceOutItemDetail.setCreated(new Date());
        invoiceOutItemDetail.setCreatedBy(user.getLoginId());
        invoiceOutItemDetail.setLastUpdated(new Date());
        invoiceOutItemDetail.setLastUpdatedBy(user.getLoginId());
        invoiceOutItemDetail.setModificationNumber(0);
        return invoiceOutItemDetail;
    }

    /**
     * 根据服务中心、交易员、买家、品规查询销项池明细
     * @param orgId
     * @param ownerId
     * @param buyerId
     * @param nsortName
     * @param material
     * @param spec
     * @return
     */
    private List<InvoiceComputeDto> queryComputeList(Long orgId, Long ownerId, Long buyerId, String nsortName, String material, String spec){
        //查询销项池明细
        List<InvoiceComputeDto> computeList = invoiceComputeDao.queryPoolOutByOrgId(orgId, ownerId, buyerId, nsortName, material, spec);
        return computeList;
    }

    /**
     * 根据服务中心查询销项池明细
     * @param orgId
     */
    private void queryComputePoolByOrgId(Long orgId){
        List<InvoiceComputeDto> computePoolList = invoiceComputeDao.queryComputePoolByOrgId(orgId);
        for(InvoiceComputeDto invoiceComputeDto : computePoolList){
            BigDecimal totalAmount = invoiceComputeDto.getTotalAmount();  //未开票总金额
            BigDecimal sentAmount = invoiceComputeDto.getSentAmount();  //已开票金额
            BigDecimal applyAmount = invoiceComputeDto.getApplyAmount();  //申请金额
            BigDecimal actualAmount = invoiceComputeDto.getActualAmount();  //实际开票金额
        }
    }

    /**
     * 获取发票设置
     * @return
     */
    private String getInvoiceSetting(){
        SysSettingQuery query = new SysSettingQuery();
        query.setType(SysSettingType.Invoice.getCode());
        List<SysSetting> list = sysSettingDao.selectByParam(query);
        return list.get(0).getSettingValue();
    }
}
