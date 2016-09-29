package com.prcsteel.platform.order.service.order.impl;

import com.prcsteel.platform.account.model.model.Account;
import com.prcsteel.platform.account.persist.dao.AccountDao;
import com.prcsteel.platform.account.service.ContactService;
import com.prcsteel.platform.acl.model.model.SysSetting;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.acl.service.SysSettingService;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.common.utils.CbmsNumberUtil;
import com.prcsteel.platform.order.model.dto.*;
import com.prcsteel.platform.order.model.enums.OrderItemStatus;
import com.prcsteel.platform.order.model.enums.UserType;
import com.prcsteel.platform.order.model.model.ConsignOrderItems;
import com.prcsteel.platform.order.model.model.InvoiceInDetailOrderItem;
import com.prcsteel.platform.order.model.query.OrderItemsForInvoiceInQuery;
import com.prcsteel.platform.order.model.query.OrderItemsQuery;
import com.prcsteel.platform.order.persist.dao.ConsignOrderItemsDao;
import com.prcsteel.platform.order.persist.dao.InvoiceInDetailOrderitemDao;
import com.prcsteel.platform.order.service.order.ConsignOrderItemsService;
import com.prcsteel.platform.smartmatch.persist.dao.QuotationOrderDao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BinaryOperator;


/**
 * Created by lcw on 2015/7/19.
 */
@Service("consignOrderItemsService")
public class ConsignOrderItemsServiceImpl implements ConsignOrderItemsService {

	private static final Logger logger = LoggerFactory.getLogger(ConsignOrderItemsServiceImpl.class);
	
	@Resource
    ConsignOrderItemsDao consignOrderItemsDao;
    @Resource
    AccountDao accountDao;
    @Resource
    InvoiceInDetailOrderitemDao invoiceInDetailOrderitemDao;
    @Resource
    private QuotationOrderDao quotationOrderDao;
    @Resource
    SysSettingService sysSettingService;
    @Resource
    ContactService contactService;

    private static final int MAX_COMPUTE_RECORDS = 2000;

    private static final String INVOICE_IN_AUTO_RECOMMEND = "invoice_in_auto_recommend";

    @Override
    public int deleteByPrimaryKey(Long id) {
        return consignOrderItemsDao.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(ConsignOrderItems record) {
        return consignOrderItemsDao.insert(record);
    }

    @Override
    public int insertSelective(ConsignOrderItems record) {
        return consignOrderItemsDao.insertSelective(record);
    }

    @Override
    public ConsignOrderItems selectByPrimaryKey(Long id) {
        return consignOrderItemsDao.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(ConsignOrderItems record) {
        return consignOrderItemsDao.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(ConsignOrderItems record) {
        return consignOrderItemsDao.updateByPrimaryKey(record);
    }

    @Override
    public int deleteByOrderId(Long orderId) {
        return consignOrderItemsDao.deleteByOrderId(orderId);
    }

    public List<ConsignOrderItems> selectByOrderId(Long id){
        return consignOrderItemsDao.selectByOrderId(id);
    }

    public List<ItemDto> querySellersItemsByOrderId(Long orderId, String perm){
        List<ItemDto> itemsDtoList = new ArrayList<ItemDto>();
        List<Long> sellerIdList = consignOrderItemsDao.selectSellerIdByOrderId(orderId);
        for (int i = 0; i < sellerIdList.size(); i++) {
            ItemDto dto = new ItemDto();

            List<ConsignOrderItems> itemsList = consignOrderItemsDao.selectByOrderIdAndSellerId(orderId, sellerIdList.get(i));
            dto.setItemsList(itemsList);
            dto.setSellerName(itemsList.get(0).getSellerName());
            dto.setSellerId(itemsList.get(0).getSellerId());
            dto.setStatus(1);
            for (int j = 0; j < itemsList.size(); j++) {
                ConsignOrderItems item = itemsList.get(j);
                if(item.getStatus() == OrderItemStatus.TOBEINPUT.ordinal()){
                    dto.setStatus(0);  //还可以继续输入
                    break;
                }
                if((perm.equals(UserType.SALESMAN.toString()) && item.getStatus() == OrderItemStatus.SERVERINPUT.ordinal()) ||
                        (perm.equals(UserType.SERVER.toString()) && item.getStatus() == OrderItemStatus.SALESMANINPUT.ordinal())){
                    dto.setStatus(0);
                    break;
                }
            }
            itemsDtoList.add(dto);
        }
        return itemsDtoList;
    }

    public List<ConsignOrderItems> getItemsIdForEdit(Long orderId, Long sellerId, String perm){
        List<ConsignOrderItems> itemsList = consignOrderItemsDao.selectByOrderIdAndSellerId(orderId, sellerId);
        for (int i = 0; i < itemsList.size(); i++) {
            ConsignOrderItems item = itemsList.get(i);
            if(item.getStatus() == OrderItemStatus.DEAL.ordinal()) {
                itemsList.remove(i--);
            }else if((perm.equals(UserType.SALESMAN.toString()) && item.getStatus() == OrderItemStatus.SALESMANINPUT.ordinal()) ||
                    (perm.equals(UserType.SERVER.toString()) && item.getStatus() == OrderItemStatus.SERVERINPUT.ordinal())){
                itemsList.remove(i--);
            }
        }
        return itemsList;
    }

    @Override
    public Integer updateOrderItems(Long itemId, int quantity, BigDecimal weight, String perm, User user){
        int status = 0;
        ConsignOrderItems items = consignOrderItemsDao.selectByPrimaryKey(itemId);
        if(perm.equals(UserType.SALESMAN.toString())){
            if (items.getStatus() == OrderItemStatus.TOBEINPUT.ordinal()){   //都未录入
                items.setActualPickQuantitySalesman(quantity);
                items.setActualPickWeightSalesman(weight);
                items.setStatus(OrderItemStatus.SALESMANINPUT.ordinal());  //设置为交易员已录入
                status = 1;
            }else if(items.getStatus() == OrderItemStatus.SERVERINPUT.ordinal()){  //内勤已录入
                if(items.getActualPickWeightServer().doubleValue() == weight.doubleValue() && quantity == items.getActualPickQuantityServer()){
                    items.setActualPickQuantitySalesman(quantity);
                    items.setActualPickWeightSalesman(weight);
                    items.setStatus(OrderItemStatus.DEAL.ordinal());  //设置为已匹配
                    status = 1;
                }else{
                    items.setActualPickQuantityServer(0);
                    items.setActualPickWeightServer(BigDecimal.ZERO);
                    items.setStatus(OrderItemStatus.TOBEINPUT.ordinal());  //清空
                    status = 2;
                }
            }
        }else if(perm.equals(UserType.SERVER.toString())){
            if (items.getStatus() == OrderItemStatus.TOBEINPUT.ordinal()){   //都未录入
                items.setActualPickQuantityServer(quantity);
                items.setActualPickWeightServer(weight);
                items.setStatus(OrderItemStatus.SERVERINPUT.ordinal());  //设置为内勤已录入
                status = 1;
            }else if(items.getStatus() == 1) {  //交易员已录入
                if (items.getActualPickWeightSalesman().doubleValue() == weight.doubleValue() && quantity == items.getActualPickQuantitySalesman()) {
                    items.setActualPickQuantityServer(quantity);
                    items.setActualPickWeightServer(weight);
                    items.setStatus(OrderItemStatus.DEAL.ordinal());  //设置为已匹配
                    status = 1;
                } else {
                    items.setActualPickQuantitySalesman(0);
                    items.setActualPickWeightSalesman(BigDecimal.ZERO);
                    items.setStatus(OrderItemStatus.TOBEINPUT.ordinal());  //清空
                    status = 2;
                }
            }
        }
        if(status != 0) {
            items.setLastUpdated(new Date());
            items.setLastUpdatedBy(user.getLoginId());
            items.setModificationNumber(items.getModificationNumber() + 1);
            if(consignOrderItemsDao.updateByPrimaryKeySelective(items) != 1){
                status = -2;       //modification_number不对导致，并发操作引起的同时修改数据问题
            }
        }
        return status;
    }

    public Integer queryCountNotMatch(Long orderId){
        return consignOrderItemsDao.queryCountNotMatch(orderId);
    }

    public List<ConsignOrderSellerInfoDto> getSellerInfo(Long orderId) {
      return consignOrderItemsDao.getSellerInfo(orderId);
    }

	@Override
	public BigDecimal countContractAmountByOrderId(Long orderId) {
		return consignOrderItemsDao.countContractAmountByOrderId(orderId);
	}


	@Override
	public List<OrderItemsInvoiceInDto> queryOrderItemsForInvoiceIn(OrderItemsForInvoiceInQuery qc) {
		Double weight = qc.getWeight();
		Double priceAndTaxAmount = qc.getPriceAndTaxAmount();
		qc.setLength(MAX_COMPUTE_RECORDS);
		// 查询符合条件的所有资源详情,已根据时间排升序
		List<OrderItemsInvoiceInDto> list;
		if(qc.getInvoiceDetailId() != null && qc.getInvoiceDetailId() > 0){
			// 查询原先匹配的数据
			list = consignOrderItemsDao.queryOrderItemsForInvoiceInByDetailId(qc.getInvoiceDetailId());
			list.forEach(a -> a.setMathced(true));
		}else{
			list = consignOrderItemsDao.queryOrderItemsForInvoiceIn(qc);

            //减掉客户端被取消关联的到票量（如果是待寄出模块才有有这个参数）
            List<InputInvoiceInAssigned> cancelAssigned = qc.getCancelAssigned();
            if(cancelAssigned != null && cancelAssigned.size() > 0){
                list.forEach(item -> {
                    double incWeight = cancelAssigned.stream().filter(a -> a.getOrderitemId().equals(item.getOrderItemId())).mapToDouble(a -> a.getIncreaseWeight().doubleValue()).sum();
                    double incAmount = cancelAssigned.stream().filter(a -> a.getOrderitemId().equals(item.getOrderItemId())).mapToDouble(a -> a.getIncreaseAmount().doubleValue()).sum();
                    item.setInvoiceWeight(item.getInvoiceWeight() - incWeight);
                    item.setInvoiceAmount(item.getInvoiceAmount() - incAmount);
                });

                //添加 客户端取消关联且未开重量和金额为0的
                cancelAssigned.removeIf(a -> list.stream().anyMatch(b -> b.getOrderItemId().equals(a.getOrderitemId())));
                if(cancelAssigned.size() > 0){
                    List<OrderItemsInvoiceInDto> dbCancel = consignOrderItemsDao.queryClientCanceledOrderItemsForInvoiceIn(qc);
					dbCancel.forEach(a -> {
						InputInvoiceInAssigned canceled = cancelAssigned.stream().filter(c -> c.getOrderitemId().equals(a.getOrderItemId())).findFirst().get();
						a.setInvoiceAmount(a.getInvoiceAmount() - canceled.getIncreaseAmount().doubleValue());
						a.setInvoiceWeight(a.getInvoiceWeight() - canceled.getIncreaseWeight().doubleValue());
					});
                    list.addAll(dbCancel);
                }
            }

			// 排除掉客户端已经分配的量
			List<InputInvoiceInAssigned> assigned = qc.getAssigned();
			if(assigned != null && assigned.size() > 0){
				list.forEach(item -> {
					double incWeight = assigned.stream().filter(a -> a.getOrderitemId().equals(item.getOrderItemId())).mapToDouble(a -> a.getIncreaseWeight().doubleValue()).sum();
					double incAmount = assigned.stream().filter(a -> a.getOrderitemId().equals(item.getOrderItemId())).mapToDouble(a -> a.getIncreaseAmount().doubleValue()).sum();
					item.setInvoiceWeight(item.getInvoiceWeight() + incWeight);
					item.setInvoiceAmount(item.getInvoiceAmount() + incAmount);
                });
            }
            list.removeIf(a -> a.getUnInvoiceAmountOfAllowance() <= 0 && a.getUnInvoiceWeightOfAllowance() <= 0);
            SysSetting sysSetting = sysSettingService.queryBySettingType(INVOICE_IN_AUTO_RECOMMEND);
            if (sysSetting != null && Boolean.TRUE.toString().equals(sysSetting.getSettingValue())) {
                addMatchFlag(qc, list);// 为结果计算并添加匹配标记
            }
		}
		qc.setWeight(weight);
		qc.setPriceAndTaxAmount(priceAndTaxAmount);
		
		list.forEach(a -> {
			a.setIncreaseAmount(CbmsNumberUtil.formatMoney(a.getIncreaseAmount()));
			a.setIncreaseWeight(CbmsNumberUtil.formatWeight(a.getIncreaseWeight()));
			a.setInvoiceAmount(CbmsNumberUtil.formatMoney(a.getInvoiceAmount()));
			a.setInvoiceWeight(CbmsNumberUtil.formatWeight(a.getInvoiceWeight()));
			a.setActualPickWeightServer(CbmsNumberUtil.formatWeight(a.getActualPickWeightServer()));
			a.setCostPrice(CbmsNumberUtil.formatMoney(a.getCostPrice()));
		});
		// 将选中的排到最前面
		list.sort((a,b) -> Boolean.compare(b.isMathced(),a.isMathced()));
		return list;
	}

	private void addMatchFlag(OrderItemsForInvoiceInQuery qc, List<OrderItemsInvoiceInDto> list) {
		// 1、重量-未到票重量=0  价税合计-资源价税合计=0  得出的资源排列在最前面，默认勾选上，仅显示该条资源；
		Optional<OrderItemsInvoiceInDto> match = list.stream()
				.filter(a -> a.getUnInvoiceAmount().equals(qc.getPriceAndTaxAmount()) &&
					a.getUnInvoiceWeight().equals(qc.getWeight())).findFirst();
		if(match.isPresent()){
			OrderItemsInvoiceInDto item = match.get();
			item.setMathced(true);
			item.setIncreaseAmount(qc.getPriceAndTaxAmount());
			item.setIncreaseWeight(qc.getWeight());
			return;
		}
		//构建匹配资源的reduce函数
		BinaryOperator<OrderItemsInvoiceInDto> accumulator = (prev,curr) -> {
			if(qc.getWeight().equals(0d) && qc.getPriceAndTaxAmount().equals(0d)){
				return curr;
			}
			curr.setMathced(true);
			curr.setIncreaseAmount(Math.min(qc.getPriceAndTaxAmount(), curr.getUnInvoiceAmount()));
			curr.setIncreaseWeight(Math.min(qc.getWeight(), curr.getUnInvoiceWeight()));
			qc.setWeight(qc.getWeight() - curr.getIncreaseWeight());
			qc.setPriceAndTaxAmount(qc.getPriceAndTaxAmount() - curr.getIncreaseAmount());
			return curr;
		};
		// 2、重量-未到票重量>0  价税合计-资源价税合计>0 （同时出先多条，优先推荐时间最早的）  再继续减，直到=0或接近于0的资源；
		list.stream().filter(a -> a.getUnInvoiceWeight() <= qc.getWeight() && a.getUnInvoiceAmount() <= qc.getPriceAndTaxAmount()).reduce(null, accumulator);
		
		// 3、重量-未到票重量<0  价税合计-资源价税合计<0   ，时间最早的最先显示
		// 即经过以上计算后，重量或金额还未减为0时，需要继续扣减
		if(qc.getWeight() > 0 || qc.getPriceAndTaxAmount() > 0){
			//继续找未匹配过的资源，扣减
			list.stream().filter(a -> !a.isMathced()).reduce(null,accumulator);
		}
	}

	@Override
	public int increaseInvoiceIn(IncreaseInvoiceInDto updInvoice) {
		return consignOrderItemsDao.increaseInvoiceIn(updInvoice);
	}

	/**
	 * 根据进项票详情ID回滚订单详情已开票金额/重量字段
	 * @param invoiceDetailId
	 * @return
	 */
	@Override
	public int restoreOrderitemInvoiceIn(Long invoiceDetailId){
		List<InvoiceInDetailOrderItem> details = invoiceInDetailOrderitemDao.selectByDetailIds(Arrays.asList(invoiceDetailId));
		if(details == null || details.size() == 0){
			return 0;
		}
		int effective = details.stream().mapToInt(it -> consignOrderItemsDao.restoreOrderitemInvoiceIn(it)).sum();
		if(effective != details.size()){
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "回滚进项票绑定失败");
		}
		return effective;
	}
	
	/**
	 * 使进项票详情ID重新生效，会增加进项金额
	 * @param invoiceDetailId
	 * @return
	 */
	@Override
	public int reEffectiveInvoiceIn(Long invoiceDetailId){
		List<InvoiceInDetailOrderItem> details = invoiceInDetailOrderitemDao.selectByDetailIds(Arrays.asList(invoiceDetailId));
		if(details == null || details.size() == 0){
			return 0;
		}
		
		int effective = details.stream().mapToInt(it -> consignOrderItemsDao.reEffectiveInvoiceIn(it)).sum();
		if(effective != details.size()){
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "进项票恢复绑定失败");
		}
		return effective;
	}
	
	@Override
	public List<ConsignOrderItems> queryOrderItemsByDetailIds(List<Long> detailIds){
		return consignOrderItemsDao.queryOrderItemsByDetailIds(detailIds);
	}
	@Override
	public List<ConsignOrderItems> queryByIds(List<Long> ids){
		return consignOrderItemsDao.queryByIds(ids);
	}

    @Override
    public List<String> selectAcceptDraftCodeByOrderId(Long orderId){
        return consignOrderItemsDao.selectAcceptDraftCodeByOrderId(orderId);
    }

	@Override
	public List<OrderItemsDto> selectTicketList(OrderItemsQuery orderItemsQuery) {
		getSatus (orderItemsQuery);
		return consignOrderItemsDao.selectTicketList(orderItemsQuery);
	}
	
	private void getSatus (OrderItemsQuery orderItemsQuery){
		if (orderItemsQuery.getAllStatus() != null && orderItemsQuery.getAllStatus().length() > 0 ) {
			orderItemsQuery.setState(Arrays.asList(orderItemsQuery.getAllStatus().split(",")));
		} 
	}
	
	@Override
	public Integer queryTicketTotal(OrderItemsQuery orderItemsQuery) {
		getSatus (orderItemsQuery);
		return consignOrderItemsDao.queryTicketTotal(orderItemsQuery);
	}

	@Override
	public OrderItemsDto totalTicketList(OrderItemsQuery orderItemsQuery) {
		getSatus (orderItemsQuery);
		return consignOrderItemsDao.totalTicketList(orderItemsQuery);
	}

	@Override
    @Transactional
    public List<ConsignOrderItemsInfoDto> buildOrderItemsByPurchaseOrderId(Long id, User user, List<Long> userIds) {
        Long quotationOrderId =  quotationOrderDao.queryTheLatestQuotationOrderIdByPurchaseOrderId(id);
        if(quotationOrderId != null) {
            List<ConsignOrderItemsInfoDto> items = consignOrderItemsDao.buildOrderItemsByQuotationOrderId(quotationOrderId);
            //chengui 设置卖家的部门及其联系人信息
            items.forEach(
                    a ->{
                     a.setDepartments(contactService.getDeptsAndContactsByCompanyId(a.getSellerId(), userIds));
                     a.setDepartmentId(a.getDepartments().get(0).getId());
                    }

            );
            return items;
        }
        return null;
    }
	 /**
     * 根据资源id查询出所有的开单信息
     * @param ids 资源ID
     * @return
     */
	public List<ConsignOrderItemsInfoDto> buildOrderItemsByResourceId(String ids,List<Long> userIds){
		if(ids == null || ids.equals("")){
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "查询的资源ID不能为空");
		}
		String [] idArray = ids.split("_");
		if (idArray == null || idArray.length == 0) {
			return null;
		}
		
		List<Long> idList = new ArrayList<Long>();
		for(String idStr : idArray){
			if(idStr != null){
				try {
					Long id = Long.parseLong(idStr);
					idList.add(id);
				} catch (NumberFormatException e) {
					logger.error("----------------根据资源id查询资源方法，Id转换成Long型错误："+idStr,e);
				}
			}
		}
		
		List<ConsignOrderItemsInfoDto> items = consignOrderItemsDao.buildOrderItemsByResourceId(idList);
		if (items != null && !items.isEmpty()) {
			items.forEach(a -> {
				a.setDepartments(contactService.getDeptsAndContactsByCompanyId(
						a.getSellerId(), userIds));
				a.setDepartmentId(a.getDepartments().get(0).getId());
			});
		}
		return items;
	}
    /**
     * 根据订单明细ID查询
     * author wangxianjun
     * @param id
     * @return
     */
    @Override
    public ConsignOrderItemsInvoiceDto queryOrderItemsById(Long id){
        return consignOrderItemsDao.queryOrderItemsById(id);
    }

    /**
     * 根据订单明细ID查询关联进项票发票详情
     * author wangxianjun
     * @param orderItemsId
     * @return 详情
     */
    @Override
    public List<OrderItemsInvoiceInInfoDto> queryOrderItemsInInvoice(Long orderItemsId){
        return invoiceInDetailOrderitemDao.queryOrderItemsInInvoice(orderItemsId);
    }


    @Override
    public List<Account> listBuyerBySellerId(Long sellerId) {
        List<Long> buyerIds = consignOrderItemsDao.selectBuyerIdsBySellerId(sellerId);
        if (null == buyerIds || buyerIds.size() == 0) {
            return null;
        }
        return accountDao.queryAccountsByIds(buyerIds);
    }
	@Override
	public void udpateCertificateCode(ConsignOrderItems item) {
		consignOrderItemsDao.updateByOrderIdSelective(item);
	}

	@Override
	public List<ConsignOrderItems> selectByOrderIdAndSellerId(Long orderId, Long sellerId) {
		return consignOrderItemsDao.selectByOrderIdAndSellerId(orderId, sellerId);
	}
	
	@Override
    public AllowanceOrderItemsDto querySellerQuantity(Map<String, Object> paramMap){
        String  month = new SimpleDateFormat ("yyyyMM").format(new Date());
        paramMap.put("month", month);
        List<AllowanceOrderItemsDto> list = consignOrderItemsDao.querySellerQuantity(paramMap);
//        Optional<AllowanceOrderItemsDto>  optional = list.stream().findFirst();
//        if(optional.isPresent())
//            return optional.get();
//        else
//            return null;

        if(!list.isEmpty()){
            return list.get(0);
        }
        return null;
    }
	
	@Override
    public AllowanceOrderItemsDto querySellerOrgQuantity(Map<String, Object> paramMap){
        String  month = new SimpleDateFormat ("yyyyMM").format(new Date());
        paramMap.put("month", month);
        List<AllowanceOrderItemsDto> list = consignOrderItemsDao.querySellerOrgQuantity(paramMap);
        if(!list.isEmpty()){
            return list.get(0);
        }
        return null;

    }
    @Override
    public AllowanceOrderItemsDto queryAllSellerQuantity(Map<String, Object> paramMap){
        paramMap.put("month", null);
        List<AllowanceOrderItemsDto> list = consignOrderItemsDao.querySellerQuantity(paramMap);
        Optional<AllowanceOrderItemsDto>  optional = list.stream().findFirst();
        if(optional.isPresent())
            return optional.get();
        else
            return null;

    }
    /**
     * cbms接口 wangxiao
     * 获取采购历史记录
     * @param phone
     * @return
     */
    @Override
    public  List<PurchaseRecordDto> findPurchaseRecord(String phone){
        List<PurchaseRecordDto> list = consignOrderItemsDao.findPurchaseRecord(phone);
        if(!list.isEmpty()){
            return list;
        }
        return null;
    }

    @Override
    public List<ConsignOrderItems> selectUnionAllByOrderId(Long orderId){
        return consignOrderItemsDao.selectUnionAllByOrderId(orderId);
    }
}
