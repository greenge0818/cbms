package com.prcsteel.platform.order.service.invoice.impl;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prcsteel.platform.acl.model.model.SysSetting;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.acl.service.SysSettingService;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.constants.Constant.InvoiceOutSendStatus;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.order.model.dto.InvoiceOutApplyItemDetailDto;
import com.prcsteel.platform.order.model.dto.InvoiceOutCheckListDetailDto;
import com.prcsteel.platform.order.model.dto.InvoiceOutCheckListDto;
import com.prcsteel.platform.order.model.dto.UnSendInvoiceOutDto;
import com.prcsteel.platform.order.model.enums.ConsignOrderStatus;
import com.prcsteel.platform.order.model.enums.InvoiceInIsDefer;
import com.prcsteel.platform.order.model.enums.InvoiceInStatus;
import com.prcsteel.platform.order.model.enums.InvoiceOutApplyStatus;
import com.prcsteel.platform.order.model.enums.InvoiceOutCheckListStatus;
import com.prcsteel.platform.order.model.enums.OrderStatusType;
import com.prcsteel.platform.order.model.model.ConsignOrderItems;
import com.prcsteel.platform.order.model.model.InvoiceOutApply;
import com.prcsteel.platform.order.model.model.InvoiceOutCheckList;
import com.prcsteel.platform.order.model.model.InvoiceOutCheckListDetail;
import com.prcsteel.platform.order.model.model.InvoiceOutItemDetail;
import com.prcsteel.platform.order.model.model.OrderAuditTrail;
import com.prcsteel.platform.order.model.model.PoolOut;
import com.prcsteel.platform.order.model.model.PoolOutDetail;
import com.prcsteel.platform.order.model.query.ChecklistDetailQuery;
import com.prcsteel.platform.order.model.query.ConsignOrderItemsQuery;
import com.prcsteel.platform.order.model.query.ConsignOrderQuery;
import com.prcsteel.platform.order.model.query.InvOutApplyItemDetailQuery;
import com.prcsteel.platform.order.persist.dao.ConsignOrderDao;
import com.prcsteel.platform.order.persist.dao.ConsignOrderItemsDao;
import com.prcsteel.platform.order.persist.dao.InvoiceOutApplyDao;
import com.prcsteel.platform.order.persist.dao.InvoiceOutCheckListDao;
import com.prcsteel.platform.order.persist.dao.InvoiceOutCheckListDetailDao;
import com.prcsteel.platform.order.persist.dao.InvoiceOutItemDetailDao;
import com.prcsteel.platform.order.persist.dao.OrderStatusDao;
import com.prcsteel.platform.order.persist.dao.PoolOutDao;
import com.prcsteel.platform.order.persist.dao.PoolOutDetailDao;
import com.prcsteel.platform.order.service.invoice.InvoiceInService;
import com.prcsteel.platform.order.service.invoice.InvoiceOutCheckListService;
import com.prcsteel.platform.order.service.invoice.InvoiceOutItemDetailService;
import com.prcsteel.platform.order.service.report.ReportFinanceService;

/**
 * Created by kongbinheng on 15-9-17.
 */
@Service("invoiceOutCheckListService")
public class InvoiceOutCheckListServiceImpl implements InvoiceOutCheckListService {
    @Resource
    private InvoiceOutCheckListDao invoiceOutCheckListDao;
    @Resource
    InvoiceOutItemDetailDao outItemDetailDao;
    @Resource
    InvoiceOutCheckListDao outCheckListDao;
    @Resource
    InvoiceOutCheckListDetailDao outCheckListDetailDao;
    @Resource
    ConsignOrderItemsDao consignOrderItemsDao;
    @Resource
    InvoiceOutApplyDao invoiceOutApplyDao;
    @Resource
    PoolOutDao poolOutDao;
    @Resource
    PoolOutDetailDao poolOutDetailDao;
    @Resource
    InvoiceInService invoiceInService;
    @Resource
    InvoiceOutItemDetailService invoiceOutItemDetailService;
    @Resource
    ConsignOrderDao consignOrderDao;
    @Resource
    OrderStatusDao orderStatusDao;
    @Resource
    ReportFinanceService reportFinanceService;
    
    @Resource
    SysSettingService sysSettingService;
    
    @Override
    public List<InvoiceOutCheckListDto> queryOutChecklistByStatus(List<String> orgIds, String status, Integer start, Integer length) {
        List<InvoiceOutCheckListDto> list = invoiceOutCheckListDao.selectOutChecklistByStatus(orgIds, status, start, length);
        for (InvoiceOutCheckListDto checkListDto : list) {
            checkListDto.setStatusName(InvoiceOutCheckListStatus.getName(checkListDto.getStatus()));
        }
        return list;
    }

    @Override
    public int totalOutChecklistByStatus(List<String> orgIds, String status) {
        return invoiceOutCheckListDao.totalOutChecklistByStatus(orgIds, status);
    }

    @Override
    public InvoiceOutCheckList queryById(Long id) {
        return invoiceOutCheckListDao.selectByPrimaryKey(id);
    }

    /**
     * 创建开票清单数据
     *
     * @param item   销项票数据
     * @param user   操作用户
     * @param status 状态
     * @param now    时间
     * @return 开票清单
     */
    private InvoiceOutCheckList addCheckList(InvoiceOutApplyItemDetailDto item, User user, InvoiceOutSendStatus sendStatus, InvoiceOutCheckListStatus status, Date now) {
        InvoiceOutCheckList checkList = new InvoiceOutCheckList();
        checkList.setOrgId(item.getOrgId());
        checkList.setSendStatus(sendStatus);
        checkList.setCode(item.getCode());
        checkList.setAmount(item.getActualAmount());
        checkList.setStatus(status.getValue());
        checkList.setCreated(now);
        checkList.setCreatedBy(user.getLoginId());
        checkList.setLastUpdated(now);
        checkList.setLastUpdatedBy(user.getLoginId());
        checkList.setModificationNumber(0);
        Integer flag = outCheckListDao.insertSelective(checkList);
        if (flag > 0) {
            createCheckListDetail(checkList.getId(), item, user, now);
        } else {
            throw new BusinessException(Constant.ERROR_RESOURCE_NOT_FOUND, "插入清单失败。");
        }
        return checkList;
    }

    /**
     * 创建开票清单关联表数据
     *
     * @param checkListId 清单Id
     * @param item        销项票数据
     * @param user        操作用户
     * @param now         时间
     * @return 清单关联表数据
     */
    private void createCheckListDetail(Long checkListId, InvoiceOutApplyItemDetailDto item, User user, Date now) {
        InvoiceOutCheckListDetail checkListDetail = new InvoiceOutCheckListDetail();
        checkListDetail.setChecklistId(checkListId);
        checkListDetail.setItemDetailId(item.getId());
        checkListDetail.setAmount(item.getActualAmount());
        checkListDetail.setWeight(item.getActualWeight());
        checkListDetail.setCreated(now);
        checkListDetail.setCreatedBy(user.getLoginId());
        checkListDetail.setLastUpdated(now);
        checkListDetail.setLastUpdatedBy(user.getLoginId());
        Integer flag = outCheckListDetailDao.insertSelective(checkListDetail);
        if (flag == 0) {
            throw new BusinessException(Constant.ERROR_RESOURCE_NOT_FOUND, "插入清单详情失败。");
        }
    }

    /**
     * 更新订单已用金额与已用重量
     *
     * @param item 详情数据
     */
    private void updateOrderUsed(InvoiceOutApplyItemDetailDto item, User user) {
        ConsignOrderItems consignOrderItems = consignOrderItemsDao.selectByPrimaryKey(item.getOrderDetailId());
        // 更新订单详情记录的已用发票金额和已用开票重量
        BigDecimal usedAmount = consignOrderItems.getUsedAmount() == null ? item.getActualAmount() : consignOrderItems.getUsedAmount().add(item.getActualAmount());
        BigDecimal usedWeight = consignOrderItems.getUsedWeight() == null ? item.getActualWeight() : consignOrderItems.getUsedWeight().add(item.getActualWeight());
        BigDecimal allowanceWeight = consignOrderItems.getAllowanceWeight() == null ? BigDecimal.ZERO : consignOrderItems.getAllowanceWeight();
        if (usedWeight.compareTo(consignOrderItems.getActualPickWeightServer().add(allowanceWeight)) > 0) {
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "已用发票重量不能大于订单资源实提重量。");
        }
        consignOrderItems.setUsedAmount(usedAmount);
        consignOrderItems.setUsedWeight(usedWeight);
        consignOrderItems.setLastUpdated(new Date());
        consignOrderItems.setLastUpdatedBy(user.getLoginId());
        consignOrderItems.setId(item.getOrderDetailId());
        consignOrderItems.setModificationNumber(consignOrderItems.getModificationNumber() + 1);
        Integer flag = consignOrderItemsDao.updateByPrimaryKeySelective(consignOrderItems);
        if (flag == 0) {
            throw new BusinessException(Constant.ERROR_RESOURCE_NOT_FOUND, "更新订单详情数据失败。");
        }
    }

    /**
     * 修改订单的状态
     *
     * @param itemDetailDtos 销项票详情集合
     * @param user           操作用户
     */
    private void updateOrderStatus(List<InvoiceOutApplyItemDetailDto> itemDetailDtos, User user, Date now) {
        if (itemDetailDtos != null && itemDetailDtos.size() > 0) {
            // 查询相关的订单ID集合
            List<Long> orderIds = itemDetailDtos.stream().map(a -> a.getOrderId()).distinct().collect(Collectors.toList());

            // 根据订单ID集合查询状态为待开票的订单
            ConsignOrderItemsQuery consignOrderItemsQuery = new ConsignOrderItemsQuery();
            consignOrderItemsQuery.setConsignOrderStatus(Arrays.asList(ConsignOrderStatus.INVOICE.getCode()));
            consignOrderItemsQuery.setOrderIds(orderIds);
            List<Long> ids = consignOrderItemsDao.queryWeightByConsignOrderItemsQuery(consignOrderItemsQuery);

            // 修改订单状态为已开票
            if (ids.size() > 0) {
                ConsignOrderQuery consignOrderQuery = new ConsignOrderQuery();
                consignOrderQuery.setOrderIds(ids);
                consignOrderQuery.setStatus(ConsignOrderStatus.FINISH.getCode());
                consignOrderQuery.setOldStatus(ConsignOrderStatus.INVOICE.getCode());
                consignOrderQuery.setLastUpdatedBy(user.getLoginId());
                Integer successTotal = consignOrderDao.updateStatusByConsignOrderQuery(consignOrderQuery);
                if (successTotal == ids.size()) {
                    // 添加订单状态记录
                    List<OrderAuditTrail> records = new ArrayList<>();
                    ids.forEach(id -> {
                        OrderAuditTrail orderAuditTrail = new OrderAuditTrail();
                        orderAuditTrail.setOrderId(id);
                        orderAuditTrail.setOperatorId(user.getId());
                        orderAuditTrail.setOperatorName(user.getName());
                        orderAuditTrail.setLastUpdated(now);
                        orderAuditTrail.setLastUpdatedBy(user.getLoginId());
                        orderAuditTrail.setSetToStatus(ConsignOrderStatus.FINISH.getCode());
                        orderAuditTrail.setStatusType(OrderStatusType.MAIN.toString());
                        orderAuditTrail.setCreated(now);
                        orderAuditTrail.setCreatedBy(user.getLoginId());
                        orderAuditTrail.setModificationNumber(0);
                        records.add(orderAuditTrail);
                    });
                    successTotal = orderStatusDao.batchInsert(records);
                    if (successTotal != ids.size()) {
                        throw new BusinessException(Constant.EXCEPTIONCODE_UNKONWN, "插入订单状态失败。");
                    }
                } else {
                    throw new BusinessException(Constant.EXCEPTIONCODE_UNKONWN, "修改订单失败。");
                }
            }
        }
    }

    /**
     * 更新 开票申请详情项 记录
     *
     * @param item 开票申请详情项
     * @param user 操作用户
     * @param now  操作时间
     */
    private void updateOutItemDetail(InvoiceOutApplyItemDetailDto item, User user, Date now) {
        item.setLastUpdated(now);
        item.setLastUpdatedBy(user.getLoginId());
        item.setModificationNumber(item.getModificationNumber() + 1);
        Integer flag = outItemDetailDao.updateByPrimaryKeySelective(item);
        if (flag == 0) {
            throw new BusinessException(Constant.ERROR_RESOURCE_NOT_FOUND, "更新销项票详情失败。");
        }
    }

    /**
     * 更新申请开票记录数据
     *
     * @param invoiceOutApplies 所有申请开票记录
     */
    private void updateInvOutApply(List<InvoiceOutApply> invoiceOutApplies, List<Long> selectedNotOutIds) {
        Integer flag;
        for (InvoiceOutApply applyItem : invoiceOutApplies) {
            // 该申请的记录申请发票已全部开完
            if (applyItem.getAmount().compareTo(applyItem.getActualAmount()) == 0) {
                applyItem.setStatus(InvoiceOutApplyStatus.INVOICED.getValue());
            } else {
                applyItem.setStatus(InvoiceOutApplyStatus.PARTIAL_INVOICED.getValue());
            }
            // 更新申请单
            flag = invoiceOutApplyDao.updateByPrimaryKeySelective(applyItem);
            if (flag == 0) {
                throw new BusinessException(Constant.ERROR_RESOURCE_NOT_FOUND, "更新申请开票记录失败。");
            }
        }
    }

    /**
     * 生成开票清单
     *
     * @param invoiceInIds      需要认证的进项票
     * @param suspendIds        需要暂缓认证的进项票
     * @param applyIds          所有的销项票申请主记录Id
     * @param itemDetailDtos    已选择待开的销项票
     * @param tobeOutIds        待开销项票
     * @param selectedNotOutIds 已选择不开的销项票
     * @param user              操作用户
     * @param modifyNums        修改对象次数集合
     * @param sendStatus        可寄出（send）、不可寄出（unsend）、全部(all)  add by tuxianming in 20160516
     */
    @Override
    @Transactional
    public void generateCheckList(List<Long> invoiceInIds, 
    			List<Long> suspendIds, List<Long> applyIds, 
    			List<InvoiceOutApplyItemDetailDto> itemDetailDtos,
                List<Long> tobeOutIds, List<Long> selectedNotOutIds, 
                User user,List<InvoiceOutItemDetail> modifyNums,
                InvoiceOutSendStatus sendStatus) {
        // 验证数据是否被修改
        checkModificationNumber(applyIds, modifyNums);
        // 发票入池并更新发票状态为已认证
        if (invoiceInIds.size() > 0) {
            invoiceInService.updateStatus(user, InvoiceInStatus.ALREADY, invoiceInIds);
            invoiceInService.updateIsDefer(invoiceInIds, InvoiceInIsDefer.NO.getCode());
        }
        // 更新已选择暂缓认证的发票
        if (suspendIds.size() > 0) {
            invoiceInService.updateIsDefer(suspendIds, InvoiceInIsDefer.YES.getCode());
        }

        // 更新是否选择已开的销项票状态
        if (selectedNotOutIds.size() > 0) {
            outItemDetailDao.updateIsDeferForIds(selectedNotOutIds, InvoiceInIsDefer.YES.getCode());
        }

        Date now = new Date();
        InvOutApplyItemDetailQuery itemDetailQuery = new InvOutApplyItemDetailQuery();
        
        // 新建的清单
        List<InvoiceOutCheckList> newCheckLists = new ArrayList<>();

        // 所有的销项票申请主记录
        List<InvoiceOutApply> allApplyLists = invoiceOutApplyDao.queryInvoiceOutApplyByIds(applyIds);
        
        BigDecimal actualAmount = BigDecimal.ZERO; // 数据库保存的实开金额
        BigDecimal actualWeight = BigDecimal.ZERO; // 数据库保存的实开重量
        BigDecimal tempWeight = BigDecimal.ZERO;   // 本次实开重量
        
        if (itemDetailDtos.size() > 0) {
            // 查询对应的记录
            List<Long> ids = itemDetailDtos.stream().map(a -> a.getId()).collect(Collectors.toList());
            itemDetailQuery.setIds(ids);
            List<InvoiceOutApplyItemDetailDto> itemDetails = outItemDetailDao.query(itemDetailQuery);
            
            for (InvoiceOutApplyItemDetailDto item : itemDetails) {
                for (InvoiceOutApplyItemDetailDto outItem : itemDetailDtos) {
                    if (item.getId().equals(outItem.getId())) {
                        // 已开金额与已开重量累加
                        tempWeight = outItem.getActualAmount().divide(item.getPrice(), 10, BigDecimal.ROUND_HALF_UP).setScale(6, BigDecimal.ROUND_HALF_UP);
                        actualWeight = item.getActualWeight().add(tempWeight);
                        actualAmount = item.getActualAmount().add(outItem.getActualAmount());
                        item.setActualAmount(outItem.getActualAmount());
                        item.setActualWeight(tempWeight);
                        break;
                    }
                }
                // 创建清单或与清单建立关联
                addToCheckList(newCheckLists, item, InvoiceOutCheckListStatus.RespiteInvoiced, sendStatus, user, now);

                // 更新PoolOut
                updatePoolOut(item, user, now);

                // 累加开票申请主表的已开金额
                accumulateApplyActualAmount(allApplyLists, item);

                // 更新订单详情数据
                updateOrderUsed(item, user);

                // 更新对应的记录
                item.setActualAmount(actualAmount);
                item.setActualWeight(actualWeight);
                updateOutItemDetail(item, user, now);
                
                
            }
            // 修改订单状态
            updateOrderStatus(itemDetails, user, now);
            
        }

        // 待开销项票生成 待开 清单
        if (tobeOutIds.size() > 0) {
            // 更新是否选择不开的状态
            outItemDetailDao.updateIsDeferForIds(tobeOutIds, InvoiceInIsDefer.NO.getCode());

            itemDetailQuery = new InvOutApplyItemDetailQuery();
            itemDetailQuery.setIds(tobeOutIds);
            List<InvoiceOutApplyItemDetailDto> tobeItemDetails = outItemDetailDao.query(itemDetailQuery);
            List<InvoiceOutApplyItemDetailDto> tobeItemDetailsFilted = null;
            
            SysSetting secondAmountSetting = sysSettingService.queryBySettingType("InvoiceOutApplySecond");
            
            final int invoiceOutApplySecondValue = 
            		(secondAmountSetting!=null && StringUtils.isNotBlank(secondAmountSetting.getSettingValue()))?
            				Integer.parseInt(secondAmountSetting.getSettingValue()):0;
            
            //add by tuxianming in 20160516
            // 已选择待开的销项票 生成 暂缓 开票清单
            //根据状态得到,过滤list,
            //如果状态为：ALL，则不需要过滤
            if(sendStatus.equals(InvoiceOutSendStatus.SEND)){
            	//先得凭证审核状态： CredentialStatus
            	tobeItemDetails = invoiceOutItemDetailService.processCredential(tobeItemDetails);
            	
            	//二结金额大于等于0， 凭证审核状态为通过的时候，就是可寄出开票清单并进项票提交 清单
            	tobeItemDetailsFilted = tobeItemDetails.stream().filter(a -> {
            		BigDecimal secamount = a.getBalanceSecondSettlement();
            		double secamountD = secamount == null?0: secamount.doubleValue();
            		
            		return (secamountD+invoiceOutApplySecondValue) >= 0 && a.getCredentialStatus();
            	}).collect(Collectors.toList());
            	
            }else if(sendStatus.equals(InvoiceOutSendStatus.UNSEND)){
            	//先得凭证审核状态： CredentialStatus
            	tobeItemDetails = invoiceOutItemDetailService.processCredential(tobeItemDetails);
            	
        		//二结金额小于0， 或者凭证审核状态为不通过的时候，就是不可寄出开票清单并进项票提交 清单
            	tobeItemDetailsFilted = tobeItemDetails.stream().filter(a -> {
            		BigDecimal secamount = a.getBalanceSecondSettlement();
            		double secamountD = secamount == null?0: secamount.doubleValue();
            		return (secamountD+invoiceOutApplySecondValue) < 0 || !a.getCredentialStatus();
            	}).collect(Collectors.toList());
            	
            }else
            	tobeItemDetailsFilted = tobeItemDetails;
            //end add by tuxianming in 20160516
            
            if(tobeItemDetailsFilted.size()==0){
            	throw new BusinessException(Constant.ERROR_MISSING_REQUIRED_PARAM, "没有需要生成的清单数据！");
            }
            
            // 销项票添加到清单
            for (InvoiceOutApplyItemDetailDto item : tobeItemDetailsFilted) {
                // 本次实开金额与重量就是剩余所有未开的金额与重量
                item.setActualWeight(item.getWeight().subtract(item.getActualWeight()));
                item.setActualAmount(item.getAmount().subtract(item.getActualAmount()));

                // 创建清单或与清单建立关联
                addToCheckList(newCheckLists, item, InvoiceOutCheckListStatus.StayedInvoiced, sendStatus, user, now);

                // 更新PoolOut
                updatePoolOut(item, user, now);

                // 累加开票申请主表的已开金额
                accumulateApplyActualAmount(allApplyLists, item);

                // 更新订单详情数据
                updateOrderUsed(item, user);

                // 所有的申请金额开完了，实开金额与实开重量等于申请的金额与重量
                item.setActualAmount(item.getAmount());
                item.setActualWeight(item.getWeight());
                // 更新对应的记录
                updateOutItemDetail(item, user, now);
                
            }
            // 修改订单状态
            updateOrderStatus(tobeItemDetailsFilted, user, now);
        }

        // 更新清单的的数据，金额有累加，避免多次操作数据库，这里一次性批量update
        newCheckLists.forEach(checkList -> {
            Integer outFlag = outCheckListDao.updateByPrimaryKeySelective(checkList);
            if (outFlag == 0) {
                throw new BusinessException(Constant.ERROR_RESOURCE_NOT_FOUND, "更新清单失败。");
            }
        });

        // 更新申请开票记录的可用金额，金额有累加，批量更新，防止多次操作
        updateInvOutApply(allApplyLists, selectedNotOutIds);
    }

    /**
     * 添加到清单列表
     *
     * @param newCheckLists   清单列表集合
     * @param item            销项票
     * @param checkListStatus 清单状态
     * @param user            操作用户
     * @param now             操作时间
     */
    private void addToCheckList(List<InvoiceOutCheckList> newCheckLists,
                                InvoiceOutApplyItemDetailDto item,
                                InvoiceOutCheckListStatus checkListStatus,
                                InvoiceOutSendStatus sendStatus, 
                                User user, Date now) {
        Boolean isExist = false;
        for (InvoiceOutCheckList checkList : newCheckLists) {
            // 新建清单列表里面存在同一个服务中心的数据，并且状态一致 金额累加，创建对应的关联
            if (item.getOrgId().equals(checkList.getOrgId()) && checkListStatus.getValue().equals(checkList.getStatus())) {
                checkList.setAmount(checkList.getAmount().add(item.getActualAmount()));
                createCheckListDetail(checkList.getId(), item, user, now);
                isExist = true;
                break;
            }
        }
        // 新建清单列表里面不存在，新建开票清单与对应的关联，（必须先建开票清单，才能创建与清单的关联表）
        if (!isExist) {
            InvoiceOutCheckList tempList = addCheckList(item, user, sendStatus, checkListStatus, now);
            newCheckLists.add(tempList);
        }
    }

    /**
     * 修改销项池主表+明细
     *
     * @param item 销项票
     * @param user 操作用户
     */
    private void updatePoolOut(InvoiceOutApplyItemDetailDto item, User user, Date now) {
        Map<String, Object> param = new HashMap<String, Object>();
        //销项池主表+明细
        param.clear();
        param.put("buyerId", item.getBuyerId());
        param.put("ownerId", item.getOwnerId());
        param.put("orgId", item.getOrgId());
        List<PoolOut> poolOuts = poolOutDao.queryByBuyer(param);
        if (!poolOuts.isEmpty()) {
            PoolOut poolOut = poolOuts.get(0);
            poolOut.setTotalSentWeight(poolOut.getTotalSentWeight().add(item.getActualWeight()));
            poolOut.setTotalSentAmount(poolOut.getTotalSentAmount().add(item.getActualAmount()));
            poolOut.setLastUpdated(now);
            poolOut.setLastUpdatedBy(user.getLoginId());
            poolOut.setModificationNumber(poolOut.getModificationNumber() + 1);
            Integer flag = poolOutDao.updateByPrimaryKeySelective(poolOut);
            if (flag > 0) {
                param.clear();
                param.put("poolOutId", poolOut.getId());
                param.put("nsortName", item.getNsortName());
                param.put("material", item.getMaterial());
                param.put("spec", item.getSpec());
                List<PoolOutDetail> poolOutDetails = poolOutDetailDao.queryByBuyerAndDetails(param);
                if (!poolOutDetails.isEmpty()) {
                    PoolOutDetail poolOutDetail = poolOutDetails.get(0);
                    poolOutDetail.setSentWeight(poolOutDetail.getSentWeight().add(item.getActualWeight()));
                    poolOutDetail.setSentAmount(poolOutDetail.getSentAmount().add(item.getActualAmount()));
                    poolOutDetail.setLastUpdated(now);
                    poolOutDetail.setLastUpdatedBy(user.getLoginId());
                    poolOutDetail.setModificationNumber(poolOutDetail.getModificationNumber() + 1);
                    flag = poolOutDetailDao.updateByPrimaryKeySelective(poolOutDetail);
                    if (flag == 0) {
                        throw new BusinessException(Constant.ERROR_RESOURCE_NOT_FOUND, "更新销项池明细表(poolOutDetail)失败。");
                    }
                }
            } else {
                throw new BusinessException(Constant.ERROR_RESOURCE_NOT_FOUND, "更新销项池主表(PoolOut)失败。");
            }
        }
    }

    /**
     * 累加申请主表的开票金额
     *
     * @param allApplyLists 申请记录
     * @param item          销项详情
     */
    private void accumulateApplyActualAmount(List<InvoiceOutApply> allApplyLists, InvoiceOutApplyItemDetailDto item) {
        allApplyLists.forEach(applyItem -> {
            if (applyItem.getId().equals(item.getApplyId())) {
                applyItem.setActualAmount(applyItem.getActualAmount() != null ? applyItem.getActualAmount().add(item.getActualAmount()) : item.getActualAmount());
            }
        });
    }

    /**
     * 检查数据是否被修改
     *
     * @param applyIds   申请Id
     * @param itemDetais 修改之前的值
     */
    private void checkModificationNumber(List<Long> applyIds, List<InvoiceOutItemDetail> itemDetais) {
        List<InvoiceOutItemDetail> list = outItemDetailDao.queryModifyNumByApplyIds(applyIds);
        if (list.size() == itemDetais.size()) {
            list.forEach(item -> {
                itemDetais.forEach(detail -> {
                    if (item.getId().equals(detail.getId()) && !item.getModificationNumber().equals(detail.getModificationNumber())) {
                        throw new BusinessException(Constant.ERROR_RESOURCE_NOT_FOUND, "该申请已处理，请重试。");
                    }
                });
            });
        }
        else{
            throw new BusinessException(Constant.ERROR_RESOURCE_NOT_FOUND, "该申请已处理。");
        }
    }

	@Override
	public List<UnSendInvoiceOutDto> queryUnsendInvoiceOut(ChecklistDetailQuery query) {
		return outCheckListDetailDao.queryUnsendInvoiceOut(query);
	}

	@Override
	public int totalUnsendInvoiceOut(ChecklistDetailQuery query) {
		return outCheckListDetailDao.totalUnsendInvoiceOut(query);
	}
	
	/**
	 * @desc: 更新买家生成的销项清单在当前时间二结欠款数，已经是不是超过系统最大限额。
	 * 		如果超过：当前二结欠款数没有超过系统设置的最大欠款数，则设置为：  true, 表示没有超过系统设置的最大限额， 并且固化当前的欠款额度。
	 * 		如果没有超过： 当前二结欠款数超过系统设置的最欠款数，则设置为：  false, 表示超过系统设置的最大限额， 
	 * 		如果没有欠款：  则设置为：true, 表示没有超过系统设置的最大限额， 并且固化额度为：0
	 * @author: tuxianming
	 * @date: 20160713
	 */
	@Override
	public void updateDebtSecondSettlement() {
		
		//得到所有不可寄出的。
		List<InvoiceOutCheckListDetailDto> list = outCheckListDetailDao.queryDetail(new ChecklistDetailQuery().setIsLimitDebet(false));
		//得到系统最大限额
		SysSetting limitSecondSettlement = sysSettingService.queryByType("InvoiceOutApplySecond");
		if(limitSecondSettlement!=null && limitSecondSettlement.getSettingValue()!=null){
			
			Double limitSecondSettlementD = Double.parseDouble(limitSecondSettlement.getSettingValue());
			for (InvoiceOutCheckListDetailDto detail : list) {
				
				if(detail.getIsLimitDebet()==null || detail.getIsLimitDebet()==false){
					
					InvoiceOutCheckListDetail updateObj = new InvoiceOutCheckListDetail();
					updateObj.setId(detail.getId());
					
					double balanceSecondSettlement = detail.getBalanceSecondSettlement() == null? 0 : detail.getBalanceSecondSettlement().doubleValue();
					
					//当二结欠款大于系统设置的最大欠款值得时候，isLimitDebet = false
					//否则：isLimitDebet = true
					if( balanceSecondSettlement<0){
						double duration = limitSecondSettlementD - (Math.abs(balanceSecondSettlement));
						if(duration < 0){
							updateObj.setIsLimitDebet(false);
						}else{
							updateObj.setIsLimitDebet(true);
							updateObj.setDebtSecondSettlement(detail.getBalanceSecondSettlement());
						}
					}else{
						updateObj.setIsLimitDebet(true);
						updateObj.setDebtSecondSettlement(BigDecimal.ZERO);
					}
					
					if(updateObj.getIsLimitDebet() || detail.getIsLimitDebet()==null){
						outCheckListDetailDao.updateByPrimaryKeySelective(updateObj);
					}
					
				}
				
			}
			
		}
		
	}

	@Override
	public void updateSend(List<Long> ids, List<Boolean> sends) {	
		
		Map<Boolean, List<Long>> updates = new HashMap<Boolean, List<Long>>();
		for (int i = 0; i < ids.size(); i++) {
			List<Long> tmpIds = null;
			if( null == (tmpIds=updates.get(sends.get(i)))){
				tmpIds = new ArrayList<Long>();
				updates.put(sends.get(i), tmpIds);
			}
			tmpIds.add(ids.get(i));
			
		}
		if(updates.get(true)!=null && updates.get(true).size()>0)
			outCheckListDetailDao.updateSend(true, updates.get(true));
		if(updates.get(false)!=null && updates.get(false).size()>0)
			outCheckListDetailDao.updateSend(false, updates.get(false));
	}
}
