package com.prcsteel.platform.smartmatch.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.smartmatch.model.dto.ContactDto;
import com.prcsteel.platform.smartmatch.model.dto.DepartmentDto;
import com.prcsteel.platform.smartmatch.model.dto.PurchaseOrderTableDto;
import com.prcsteel.platform.smartmatch.model.dto.QuotationInfoDto;
import com.prcsteel.platform.smartmatch.model.dto.QuotationOrderItemsDto;
import com.prcsteel.platform.smartmatch.model.model.QuotationOrderItems;
import com.prcsteel.platform.smartmatch.persist.dao.QuotationOrderDao;
import com.prcsteel.platform.smartmatch.persist.dao.QuotationOrderItemsDao;
import com.prcsteel.platform.smartmatch.service.QuotationOrderItemsService;

@Service("quotationOrderItemsService")
public class QuotationOrderItemsServiceImpl implements QuotationOrderItemsService {
    @Resource
    private QuotationOrderItemsDao quotationOrderItemsDao;
    @Resource
    private QuotationOrderDao quotationOrderDao;

    public List<QuotationOrderItemsDto> getDtoByOrderId(Long id) {
        List<QuotationOrderItemsDto> list = quotationOrderItemsDao.getDtoByOrderId(id);
        return list;
    }



	/**
	 * 根据报价单获取报价单详情的DTO集合
	 * @param id
	 * @return
	 */
	public List<QuotationInfoDto> getQuotationInfoOrderItems(Long id,
															 List<Long> userIds) {
		return getQuotationInfoDto(quotationOrderItemsDao.buildOrderItemsByQuotationOrderId(id), userIds);
	}


	/**
	 * 根据报价单详情id获取报价单详情的DTO集合
	 * @param ids
	 * @param userIds
	 * @return
	 */
	public List<QuotationInfoDto> getQuotationInfoOrderItemsByItemIds(List<Long> ids, List<Long> userIds) {
		return getQuotationInfoDto(quotationOrderItemsDao.buildOrderItemsByIds(ids), userIds);
	}

	/**
	 * 根据QuotationInfoDto获取QuotationInfoDto集合
	 * @param items
	 * @param userIds
     * @return
     */
	public List<QuotationInfoDto> getQuotationInfoDto(List<QuotationInfoDto> items, List<Long> userIds) {
		// 封装一个{companyId,depts}的MAP，用于保存查出来的部门
		Map<Long, List<DepartmentDto>> depts = new HashMap<Long, List<DepartmentDto>>();
		// 设置卖家的部门及其联系人信息
		items.forEach(a -> {
			List<DepartmentDto> deptList = depts.get(a.getSellerId());
			if (deptList != null && !deptList.isEmpty()) {
				a.setDepartments(deptList);
			} else {
				a.setDepartments(this.getDeptsAndContactsByCompanyId(
						a.getSellerId(), userIds));
				depts.put(a.getSellerId(), a.getDepartments());
			}
			if( a.getDepartmentId() == null || a.getDepartmentId() == 0){
				a.setDepartmentId(a.getDepartments().get(0).getId());
			}
			if(a.getContactId() == null || a.getContactId() == 0){//设置联系人Id
				if( a.getDepartmentId()!= null && a.getDepartmentId() != 0){
					if(a.getDepartments().get(0).getContacts() != null 
							&& !a.getDepartments().get(0).getContacts().isEmpty()){
						a.setContactId(a.getDepartments().get(0).getContacts().get(0).getId());
					}
					
				}
			}
		}

		);
		return items;
	}
	
    public List<DepartmentDto> getDeptsAndContactsByCompanyId(Long companyId, List<Long> userIds) {
        List<DepartmentDto> depts = quotationOrderItemsDao.queryDeptByCompanyId(companyId);
        if (depts != null && depts.size() > 0) {
            //查询所有联系人
            List<ContactDto> contactDtos = quotationOrderItemsDao.queryContactsByCompanyId(companyId);
            if (contactDtos != null && contactDtos.size() > 0) {
                //控制字段显示:如果当前登录人不包含在交易员中，则不显示联系人的信息和不允许操作联系人
                contactDtos.forEach(a -> {
                    if (!isContain(a.getManagerIds(), userIds)) {
                        //联系信息不为空，展示为加密*号
                        if(StringUtils.isNotEmpty(a.getTel())){
                            a.setTel("***");
                        }
                        if(StringUtils.isNotEmpty(a.getQq())){
                            a.setQq("******");
                        }
                        if(StringUtils.isNotEmpty(a.getEmail())){
                            a.setEmail("******");
                        }
                        a.setHiddenBtn(true);
                    }
                });

                depts.forEach(a -> {
                    //过滤出来部门的所有联系人
                    List<ContactDto> contacts = contactDtos.stream().filter(b -> a.getId().equals(b.getDeptId())).collect(Collectors.toList());
                    if (contacts != null) {
                        a.setContacts(contacts);
                    }
                });
            }

        }
        return depts;
    }
    
    private boolean isContain(String managerIds, List<Long> userIds) {

        //管理员看全部
        if (userIds == null) {
            return true;
        }

        if (StringUtils.isEmpty(managerIds)) {
            return false;
        }

        //包含返回true
        String[] array = managerIds.split(",");
        for (String s : array) {
            if (userIds.contains(Long.parseLong(s))) {
                return true;
            }
        }

        return false;
    }
    public PurchaseOrderTableDto selectByQuotationId(Integer id) {
        PurchaseOrderTableDto dto = quotationOrderItemsDao.selectByQuotationId(id);
        return dto;
    }
    
	/**
	 * 保存报价单详情信息
	 * @param etDtoList
	 */
	public void saveQuotationOrderItems(List<QuotationInfoDto> etDtoList){
		if(etDtoList == null || etDtoList.isEmpty()){
			return;
		}
		for(QuotationInfoDto info : etDtoList){
			QuotationOrderItems item = new QuotationOrderItems();
			item.setId(info.getId());
			item.setDealPrice(info.getDealPrice());
			item.setCostPrice(info.getCostPrice());
			item.setWeightConcept(info.getWeightConcept());
			item.setDepartmentId(info.getDepartmentId());
			item.setContactId(info.getContactId());
			quotationOrderItemsDao.updateByPrimaryKeySelective(item);
		}
	}

    /**
     * 根据报价单id查询历史报价单数据
     *
     * @param quotationOrderId 报价单id
     * @return
     * @author peanut
     * @date 2016/06/15
     */
    public List<List<QuotationOrderItemsDto>> selectHistoryQuotationOrder(Long quotationOrderId) {

        if (quotationOrderId == null) return null;

        //根据报价单id找到对应采购单所对应所有报价单id
        List<Long> quotationOrderIds = quotationOrderDao.selectAllQuotationIdByQuotationOrderId(quotationOrderId);

        if (quotationOrderIds == null) return null;

        //去除当前报价
        quotationOrderIds.remove(quotationOrderId);

        //根据报价单集获取报价详情
        return selectQuotationItemsByQuotationOrderIds(quotationOrderIds);
    }
    
    /**
     * 根据询价单获取询价信息
     * @param purchaseId
     * @return
     */
	public PurchaseOrderTableDto selectQuotationByPurchaseId(Integer purchaseId){
		PurchaseOrderTableDto dto = null;
		if (purchaseId != null) {
			dto = quotationOrderItemsDao.selectQuotationByPurchaseId(purchaseId);
		}
		return dto;
	}
	/**
	 * 根据询价单ID获取报价历史。
	 * @param parseLong
	 * @return
	 */
	public List<List<QuotationOrderItemsDto>> selectHistoryQuotationOrderByPurchaseId(Integer purchaseId,Integer quotationOrderId){
		if (purchaseId == null)
			return null;
		
		// 根据询价单ID获取所有的报价详情
		List<QuotationOrderItemsDto> allQuotaionOrderInfos = quotationOrderItemsDao.
				selectQuotationItemsByPurchaseId(purchaseId);

		// 转换数据，根据报价单分成各个集合，并且去除当前最新报价
		List<List<QuotationOrderItemsDto>>  result = getHistoryQuotationOrder(allQuotaionOrderInfos,quotationOrderId);
		return result;
	}
	
	/**
	 * 转换数据，根据报价单分成各个集合，并且去除当前最新报价
	 * @param allQuotaionOrderInfos 所有的报价信息
	 * @param quotationOrderId 当前的报价单ID
	 * @return
	 */
	private List<List<QuotationOrderItemsDto>> getHistoryQuotationOrder(
			List<QuotationOrderItemsDto> allQuotaionOrderInfos,
			Integer quotationOrderId) {
		
		if(allQuotaionOrderInfos == null || allQuotaionOrderInfos.isEmpty()){
			return null;
		}
		Map<Integer,List<QuotationOrderItemsDto>> infos = new HashMap<Integer,List<QuotationOrderItemsDto>>();
		for(QuotationOrderItemsDto dto : allQuotaionOrderInfos){
			//如果集合中的报价单等于当前的报价单，不做处理
			if(quotationOrderId != null && quotationOrderId == dto.getQuotationOrderId() ){
				continue;
			}
			List<QuotationOrderItemsDto> historyInfo = infos.get(dto.getQuotationOrderId());
			if(historyInfo == null){//如果不存在当前ID的历史的报价单集合，则new一个新的集合存放该报价单详情信息
				historyInfo = new ArrayList<QuotationOrderItemsDto>();
				historyInfo.add(dto);
				infos.put(dto.getQuotationOrderId(), historyInfo);
			}else{//如果已经存在当前ID的历史的报价单，直接放入该报价单信息的集合中
				historyInfo.add(dto);
				infos.put(dto.getQuotationOrderId(), historyInfo);
			}
			
		}
		
		//把MAP数据放入LIST集合中
		List<List<QuotationOrderItemsDto>> datas= new ArrayList<List<QuotationOrderItemsDto>>();
		for (Iterator<Map.Entry<Integer, List<QuotationOrderItemsDto>>> it = infos
				.entrySet().iterator(); it.hasNext();) {
			Map.Entry<Integer, List<QuotationOrderItemsDto>> entry = it.next();
			datas.add(entry.getValue());
		}
		
		Collections.reverse(datas);
		return datas;
	}


	/**
	 * 获取最新的报价单ID
	 * @param purchaseId
	 * @return
	 */
	public Integer selectQuotationLastUpdateByPurchaseId(Integer purchaseId){
		return quotationOrderDao.selectQuotationLastUpdateByPurchaseId(purchaseId);
	}

    /**
     * 根据报价单id集查询报价单详情数据
     *
     * @param quotationOrderIds 报价单id集
     * @return
     * @author peanut
     * @date 2016/06/15
     */
    public List<List<QuotationOrderItemsDto>> selectQuotationItemsByQuotationOrderIds(List<Long> quotationOrderIds) {
        if (quotationOrderIds == null || quotationOrderIds.isEmpty()) return null;
        List<List<QuotationOrderItemsDto>> result = new ArrayList<>();
        result.addAll(quotationOrderIds.stream().map(this::getDtoByOrderId).collect(Collectors.toList()));
        return result;
    }

    /**
     * 根据采购单id查询报价单详情数据
     *
     * @param purchaseOrderId 采购单id
     * @return
     * @author peanut
     * @date 2016/06/15
     */
    public List<List<QuotationOrderItemsDto>> selectQuotationByPurchaseOrderId(Long purchaseOrderId) {

        if (purchaseOrderId == null) return null;

        //根据采购单id找到对应所有报价单id
        List<Long> quotationOrderIds = quotationOrderDao.selectAllQuotationIdByPurchaseOrderId(purchaseOrderId);

        if (quotationOrderIds == null) return null;

        //根据报价单集获取报价详情
        return selectQuotationItemsByQuotationOrderIds(quotationOrderIds);
    }
    /**
     * 根据ID更新报价单详情的状态
     * @param orderItemsIds 当前需要修改的数据的id集合
     * @param statusStr 1：已开单      0：未开单
     * @param loginId  操作人登录名
     */
	public void updateStatusById(List<Long> orderItemsIds, String statusStr, String loginId){
		if(orderItemsIds == null || orderItemsIds.isEmpty()){
			return;
		}
		for(Long orderItemsId : orderItemsIds){
			if(orderItemsId == null || orderItemsId == 0){
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "ID不能为空");
			}
			if(StringUtils.isBlank(statusStr)){
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "状态值不能为空");
			}
			int status = 0;
			if(Constant.YES.equals(statusStr)||Constant.NO.equals(statusStr)){
				status =Integer.parseInt(statusStr);
			}else{
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "状态值不正确:"+statusStr);
			}
			QuotationOrderItems item = new QuotationOrderItems();
			item.setId(orderItemsId);
			item.setLastUpdated(new Date());
			item.setLastUpdatedBy(loginId);
			item.setStatus(status);
			quotationOrderItemsDao.updateByPrimaryKeySelective(item);
		}
	}
	
	 /**
     * 获取报价单详情还未开单的数量
     * @param id  当前的询价单的ID
     * @return
     */
	public Integer getQuotationItemsUnbilledCount(Long id){
		Integer count = 0;
		List<QuotationInfoDto> items = quotationOrderItemsDao.getQuotationItemsUnbilledItems(id);
		if(items != null){
			count = items.size();
		}
		return count;
	}
}
