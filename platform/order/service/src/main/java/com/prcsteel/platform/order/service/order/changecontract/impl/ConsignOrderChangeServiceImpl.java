package com.prcsteel.platform.order.service.order.changecontract.impl;

import com.prcsteel.framework.nido.engine.Nido;
import com.prcsteel.platform.account.model.enums.AccountTransApplyType;
import com.prcsteel.platform.account.model.model.Account;
import com.prcsteel.platform.account.model.model.AccountBank;
import com.prcsteel.platform.account.persist.dao.AccountBankDao;
import com.prcsteel.platform.account.persist.dao.AccountContactDao;
import com.prcsteel.platform.account.persist.dao.AccountDao;
import com.prcsteel.platform.account.service.AccountFundService;
import com.prcsteel.platform.account.service.AccountService;
import com.prcsteel.platform.acl.model.enums.SysSettingType;
import com.prcsteel.platform.acl.model.model.Organization;
import com.prcsteel.platform.acl.model.model.SysSetting;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.acl.persist.dao.OrganizationDao;
import com.prcsteel.platform.acl.persist.dao.RewardDao;
import com.prcsteel.platform.acl.persist.dao.UserDao;
import com.prcsteel.platform.acl.service.SysSettingService;
import com.prcsteel.platform.api.RestSmartmatchService;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.constants.NidoTaskConstant;
import com.prcsteel.platform.common.dto.ResultDto;
import com.prcsteel.platform.common.enums.ConsignOrderCallBackStatus;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.common.service.FileService;
import com.prcsteel.platform.common.utils.Tools;
import com.prcsteel.platform.core.persist.dao.CategoryGroupDao;
import com.prcsteel.platform.core.persist.dao.ProvinceDao;
import com.prcsteel.platform.core.service.BillSequenceService;
import com.prcsteel.platform.order.model.changecontract.dto.ChangeOrderDto;
import com.prcsteel.platform.order.model.changecontract.dto.ChangeOrderListDto;
import com.prcsteel.platform.order.model.changecontract.dto.QueryChangeOrderDto;
import com.prcsteel.platform.order.model.changecontract.dto.SaveConsignOrderChangeDto;
import com.prcsteel.platform.order.model.changecontract.dto.UpdateChangeOrderDto;
import com.prcsteel.platform.order.model.dto.ConsignOrderSellerInfoDto;
import com.prcsteel.platform.order.model.enums.AppNoticeType;
import com.prcsteel.platform.order.model.enums.AssociationType;
import com.prcsteel.platform.order.model.enums.ConsignOrderAlterStatus;
import com.prcsteel.platform.order.model.enums.ConsignOrderFillUpStatus;
import com.prcsteel.platform.order.model.enums.ConsignOrderPayStatus;
import com.prcsteel.platform.order.model.enums.ConsignOrderPickupStatus;
import com.prcsteel.platform.order.model.enums.ConsignOrderStatus;
import com.prcsteel.platform.order.model.enums.OrderChangeType;
import com.prcsteel.platform.order.model.enums.OrderStatusType;
import com.prcsteel.platform.order.model.enums.PayRequestType;
import com.prcsteel.platform.order.model.enums.PayStatus;
import com.prcsteel.platform.order.model.enums.PayType;
import com.prcsteel.platform.order.model.model.ConsignOrder;
import com.prcsteel.platform.order.model.model.ConsignOrderChange;
import com.prcsteel.platform.order.model.model.ConsignOrderContract;
import com.prcsteel.platform.order.model.model.ConsignOrderItems;
import com.prcsteel.platform.order.model.model.ConsignOrderItemsChange;
import com.prcsteel.platform.order.model.model.ConsignOrderItemsChangedrecord;
import com.prcsteel.platform.order.model.model.ConsignOrderItemsOperatedrecord;
import com.prcsteel.platform.order.model.model.ConsignOrderProcess;
import com.prcsteel.platform.order.model.model.OrderAuditTrail;
import com.prcsteel.platform.order.model.model.PayRequest;
import com.prcsteel.platform.order.model.model.PayRequestItems;
import com.prcsteel.platform.order.model.model.PickupItems;
import com.prcsteel.platform.order.model.nido.NoteMessageContext;
import com.prcsteel.platform.order.persist.dao.AllowanceDao;
import com.prcsteel.platform.order.persist.dao.ConsignOrderAttachmentDao;
import com.prcsteel.platform.order.persist.dao.ConsignOrderChangeDao;
import com.prcsteel.platform.order.persist.dao.ConsignOrderContractDao;
import com.prcsteel.platform.order.persist.dao.ConsignOrderDao;
import com.prcsteel.platform.order.persist.dao.ConsignOrderItemsChangeDao;
import com.prcsteel.platform.order.persist.dao.ConsignOrderItemsChangedrecordDao;
import com.prcsteel.platform.order.persist.dao.ConsignOrderItemsDao;
import com.prcsteel.platform.order.persist.dao.ConsignOrderItemsOperatedrecordDao;
import com.prcsteel.platform.order.persist.dao.ConsignOrderProcessDao;
import com.prcsteel.platform.order.persist.dao.ConsignProcessDao;
import com.prcsteel.platform.order.persist.dao.InvoiceInDetailOrderitemDao;
import com.prcsteel.platform.order.persist.dao.OrderStatusDao;
import com.prcsteel.platform.order.persist.dao.PayRequestDao;
import com.prcsteel.platform.order.persist.dao.PayRequestItemsDao;
import com.prcsteel.platform.order.persist.dao.PickupItemsDao;
import com.prcsteel.platform.order.persist.dao.PoolInDao;
import com.prcsteel.platform.order.persist.dao.PoolInDetailDao;
import com.prcsteel.platform.order.persist.dao.PoolOutDao;
import com.prcsteel.platform.order.persist.dao.PoolOutDetailDao;
import com.prcsteel.platform.order.persist.dao.RebateDao;
import com.prcsteel.platform.order.persist.dao.ReportNewUserRewardDao;
import com.prcsteel.platform.order.persist.dao.ReportRebateRecordDao;
import com.prcsteel.platform.order.persist.dao.ReportRewardRecordDao;
import com.prcsteel.platform.order.service.AppPushService;
import com.prcsteel.platform.order.service.OrderCacheService;
import com.prcsteel.platform.order.service.invoice.PoolInService;
import com.prcsteel.platform.order.service.order.ConsignOrderItemsService;
import com.prcsteel.platform.order.service.order.ConsignOrderService;
import com.prcsteel.platform.order.service.order.OrderStatusService;
import com.prcsteel.platform.order.service.order.changecontract.ConsignOrderChangeService;
import com.prcsteel.platform.order.service.payment.PayRequestService;
import com.prcsteel.platform.order.service.report.ReportFinanceService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 合同变更服务
 * Created by lichaowei on 2016/8/12.
 */
@Service("consignOrderChangeService")
public class ConsignOrderChangeServiceImpl implements ConsignOrderChangeService {

    private Logger log = Logger.getLogger(ConsignOrderChangeServiceImpl.class);

    @Resource
    ConsignOrderChangeDao consignOrderChangeDao;
    @Resource
    ConsignOrderItemsChangeDao consignOrderItemsChangeDao;
    @Resource
    ConsignOrderService consignOrderService;
    @Resource
    ConsignOrderItemsChangedrecordDao consignOrderItemsChangedrecordDao;
    @Resource
    ConsignOrderItemsOperatedrecordDao consignOrderItemsOperatedrecordDao;
    @Resource
    ConsignOrderDao consignOrderDao;
    @Resource
    ConsignOrderItemsDao consignOrderItemsDao;
    @Resource
    AccountDao accountDao;
    @Resource
    AccountBankDao accountBankDao;
    @Resource
    OrderStatusDao orderStatusDao;
    @Resource
    ConsignProcessDao consignProcessDao;
    @Resource
    UserDao userDao;
    @Resource
    BillSequenceService billSequenceService;
    @Resource
    ConsignOrderContractDao consignOrderContractDao;
    @Resource
    PayRequestDao payRequestDao;
    @Resource
    PayRequestItemsDao payRequestItemsDao;
    @Resource
    ConsignOrderItemsService itemsService;
    @Resource
    OrderStatusService orderStatusService;
    @Resource
    SysSettingService sysSettingService;
    @Resource
    PoolInDao poolInDao;
    @Resource
    PoolInDetailDao poolInDetailDao;
    @Resource
    PoolOutDao poolOutDao;
    @Resource
    PoolOutDetailDao poolOutDetailDao;
    @Resource
    OrganizationDao organizationDao;
    @Resource
    AccountContactDao accountContactDao;
    @Resource
    AccountService accountService;
    @Resource
    ConsignOrderAttachmentDao consignOrderAttachmentDao;
    @Resource
    FileService fileService;
    @Resource
    PayRequestService payRequestService;
    @Resource
    ConsignOrderProcessDao consignOrderProcessDao;
    @Resource
    CategoryGroupDao categoryGroupDao;
    @Resource
    RewardDao rewardDao;
    @Resource
    ReportRewardRecordDao reportRewardRecordDao;
    @Resource
    RebateDao rebateDao;
    @Resource
    ReportRebateRecordDao reportRebateRecordDao;
    @Resource
    PoolInService poolInservice;
    @Resource
    ProvinceDao provinceDao;
    @Resource
    ReportNewUserRewardDao newUserRewardDao;
    @Resource
    InvoiceInDetailOrderitemDao invoiceInDetailOrderitemDao;
    @Resource
    AllowanceDao allowanceDao;
    @Resource
    ReportFinanceService reportFinanceService;
    @Resource
    AppPushService appPushService;
    @Resource
    private AccountFundService accountFundService;
    @Resource
    private RestSmartmatchService restSmartmatchService;
    @Resource
    private ReportFinanceService reportFinaceService;
    @Resource
    private OrderCacheService orderCacheService;
    @Resource
    private PickupItemsDao pickupItemsDao;

    @Override
    public int deleteByPrimaryKey(Integer id){
        return consignOrderChangeDao.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(ConsignOrderChange record){
        return consignOrderChangeDao.insert(record);
    }

    @Override
    public int insertSelective(ConsignOrderChange record){
        return consignOrderChangeDao.insertSelective(record);
    }

    @Override
    public ConsignOrderChange selectByPrimaryKey(Integer id){
        return consignOrderChangeDao.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(ConsignOrderChange record){
        return consignOrderChangeDao.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(ConsignOrderChange record){
        return consignOrderChangeDao.updateByPrimaryKey(record);
    }

    @Override
    public List<ConsignOrderItemsChange> queryOrderItemsByChangeOrderId(Integer changeOrderId){
        return consignOrderItemsChangeDao.queryOrderItemsByChangeOrderId(changeOrderId);
    }
    //查询可变更交易单
    @Override
    public List<ChangeOrderListDto> selectByConditions(ChangeOrderDto query){
        return consignOrderChangeDao.selectByConditions(query);

    }
    @Override
    public int countByConditions(ChangeOrderDto query){
        return consignOrderChangeDao.countByConditions(query);
    }
    //查询变更的交易单
    @Override
    public List<ChangeOrderListDto> selectChangeOrderByConditions(ChangeOrderDto query){
        return consignOrderChangeDao.selectChangeOrderByConditions(query);
    }

    @Override
    public int countChangeOrderByConditions(ChangeOrderDto query){
        return consignOrderChangeDao.countChangeOrderByConditions(query);
    }

    @Override
    @Transactional
    public void saveOrderChange(SaveConsignOrderChangeDto saveDto) {
        ConsignOrderChange orderChange = saveDto.getOrderChange();
        Long orderId = orderChange.getOrderId();
        User user = saveDto.getUser();
        List<ConsignOrderItemsChange> orderItemsChanges = saveDto.getOrderItemsChanges();
        ConsignOrder order = consignOrderService.queryById(orderId);

        // 验证
        checkSaveData(orderItemsChanges, order);

        // 查询订单详情数据
        List<ConsignOrderItems> orderItems = consignOrderService.queryOrderItemsById(orderId);

        // 二结后交易单不可变更“数量”、“重量”、“销售单价”、“采购单价”
        Boolean isSecondCount = (Integer.valueOf(order.getStatus()) > 6);//status 大于6是二结后，小于等于6是二结前
        if (isSecondCount) {
            checkData(orderItemsChanges, orderItems);
        }

        // 查询订单变更表是否存在变更记录，不存在需要保存原订单数据
        ConsignOrderChange query = new ConsignOrderChange();
        // query.setStatus(ConsignOrderAlterStatus.PENDING_APPROVAL.getCode());
        query.setOrderId(orderId);
        ConsignOrderChange originOrder = consignOrderChangeDao.selectByConsignOrderChange(query);
        if (originOrder == null) {
            String originAlterStatus = ConsignOrderAlterStatus.ORIGIN_ORDER.getCode();
            // 保存原订单记录
            originOrder = saveConsignOrderChange(order, null, originAlterStatus, user);
            // 保存原订单详情记录
            for (ConsignOrderItems item : orderItems) {
                ConsignOrderItemsChange orderItemsChange = saveConsignOrderItemsChangeByOrderItem(item, originOrder.getId(), originAlterStatus, user);
                // 保存订单明细变更记录表数据
                ConsignOrderItemsChangedrecord changedRecord = new ConsignOrderItemsChangedrecord(item);
                saveConsignOrderItemsChangeRecord(changedRecord, orderItemsChange.getId(), originAlterStatus, user);
            }
        }

        String alterStatus = ConsignOrderAlterStatus.PENDING_APPROVAL.getCode();
        // 保存订单变更表数据
        ConsignOrderChange consignOrderChange = saveConsignOrderChange(order, orderChange, alterStatus, user);

        // 保存订单明细变更表数据 (添加与修改)
        orderItemsChanges.forEach(item -> {
            ConsignOrderItemsChange orderItemsChange = saveConsignOrderItemsChange(item, consignOrderChange.getId(), alterStatus, user);

            Optional<ConsignOrderItems> filterConsignOrderItem = orderItems.stream().filter(a -> a.getId().compareTo(item.getOrderItemId()) == 0).findFirst();
            if (filterConsignOrderItem.isPresent()) {
                // 保存订单明细变更记录表数据
                saveConsignOrderItemsChangeRecord(orderItemsChange, filterConsignOrderItem.get(), alterStatus, user);
                // 从订单集合里面删除存在变更的记录，留下来的就是被删除的变更资源
                orderItems.remove(filterConsignOrderItem.get());
            } else {
                ConsignOrderItemsChangedrecord changedRecord = new ConsignOrderItemsChangedrecord(item);
                changedRecord.setType(OrderChangeType.ADD.getCode());
                saveConsignOrderItemsChangeRecord(changedRecord, orderItemsChange.getId(), alterStatus, user);
            }
        });
        // 保存删除资源的变更记录
        if (orderItems.size() > 0) {
            for (ConsignOrderItems item : orderItems) {
                ConsignOrderItemsChange orderItemChange = new ConsignOrderItemsChange(item);
                orderItemChange.setQuantity(0);
                orderItemChange.setWeight(BigDecimal.ZERO);
                orderItemChange.setCostPrice(BigDecimal.ZERO);
                orderItemChange.setDealPrice(BigDecimal.ZERO);
                orderItemChange = saveConsignOrderItemsChange(orderItemChange, consignOrderChange.getId(), ConsignOrderAlterStatus.PENDING_DEL_APPROVAL.getCode(), user);
                ConsignOrderItemsChangedrecord changedRecord = new ConsignOrderItemsChangedrecord(item);
                changedRecord.setType(OrderChangeType.DEL.getCode());
                saveConsignOrderItemsChangeRecord(changedRecord, orderItemChange.getId(), ConsignOrderAlterStatus.PENDING_DEL_APPROVAL.getCode(), user);
            }
        }

        // 保存订单变更操作记录表数据
        saveConsignOrderItemsOperatedRecord(orderId, consignOrderChange.getId(), alterStatus,"", user);

        // 修改订单表状态
        updateConsignOrderAlterStatus(order, alterStatus, user);
    }

    private void checkSaveData(List<ConsignOrderItemsChange> orderItemsChanges,ConsignOrder order){
//        // 变更的销售金额
//        Double totalChangeSaleAmount = orderItemsChanges.stream().mapToDouble(a -> a.getDealPrice().multiply(a.getWeight()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()).sum();
//        // 变更的采购金额
//        Double totalChangePurchaseAmount = orderItemsChanges.stream().mapToDouble(a -> a.getCostPrice().multiply(a.getWeight()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()).sum();
//        if(totalChangePurchaseAmount >= totalChangeSaleAmount){
//            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "该交易单无利润，请确认后再提交！");
//        }
        if(ConsignOrderAlterStatus.PENDING_APPROVAL.getCode().equals(order.getAlterStatus())
                ||ConsignOrderAlterStatus.PENDING_RELATE.getCode().equals(order.getAlterStatus())
                ||ConsignOrderAlterStatus.PENDING_APPLY.getCode().equals(order.getAlterStatus())){
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "该交易单正在进行合同变更，请变更完成后再进行操作！");
        }
        if(PayStatus.REQUESTED.toString().equals(order.getPayStatus())
                ||PayStatus.APPROVED.toString().equals(order.getPayStatus())
                ||PayStatus.APPLYPRINTED.toString().equals(order.getPayStatus())){
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "该订单尚未完成付款，请完成付款后再变更！");
        }

        //add lixiang  已录入提货的资源明细：变更时变更后数据不得少于已录入数据 2016-9-2
        List<PickupItems> pickupList = pickupItemsDao.getQuantityByOrderId(order.getId());
        orderItemsChanges.forEach(itemChange -> {
            pickupList.forEach(pickup -> {
                if (pickup.getOrderItemId().equals(itemChange.getOrderItemId())) {
                    int takeQuantity = pickup.getPickedQuantity() - pickup.getLeftQuantity();//已录入的提货数量
                    if (pickup.getPickedQuantity() != pickup.getLeftQuantity()) {//如果订单数量与可提数量不相等，表示有录入提货单
                        if (takeQuantity != itemChange.getQuantity()) {//如果变更的数量和已录入的数量不相等
                            if (takeQuantity > itemChange.getQuantity()) {
                                throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "变更后的数量不得少于已录入的提货数量！");
                            }
                            //如果变更的重量小于或是等于已录入的重量，则不允许录入
                            if (itemChange.getWeight().compareTo(pickup.getPickWeight()) <= 0) {
                                throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "变更数量的同时，变更后的重量不得少于已录入的重量！");
                            }
                        }

                        if (itemChange.getWeight().compareTo(pickup.getPickWeight()) != 0) {//如果变更的重量和已录入的重量不相等
                            if (itemChange.getWeight().compareTo(pickup.getPickWeight()) <= 0) {
                                throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "变更后的重量不得少于已录入的重量！");
                            }
                            if (takeQuantity > itemChange.getQuantity()) {
                                throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "变更重量的同时，变更后的数量不得少于已录入的提货数量！");
                            }
                        }
                        //如果与已录入的重量相等并且数量大于已录入的数量
                        if (itemChange.getWeight().compareTo(pickup.getPickWeight()) == 0 && itemChange.getQuantity() > takeQuantity) {
                            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "还有剩余未提货数量时，剩余未提货重量不能为0！");
                        }
                        //如果已录入的数量相等并且重量大于已录入的重量
                        if (takeQuantity == itemChange.getQuantity() && itemChange.getWeight().compareTo(pickup.getPickWeight()) == 1) {
                            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "还有剩余未提货重量时，剩余未提货数量不能为0！");
                        }
                        //已录入提货重量等于资源明细重量时，则变更时不得变更件数,除非先增加重量+++
                        if (pickup.getLeftQuantity() == 0) {
                            if (takeQuantity != itemChange.getQuantity() && itemChange.getWeight().compareTo(pickup.getWeight()) <= 0) {
                                throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "该订单资源存在可提货数量为零，必须要先增加重量才能增加数量！");
                            }
                        }
                    }
                }
            });

        });
    }

    private void checkData(List<ConsignOrderItemsChange> orderItemsChanges, List<ConsignOrderItems> orderItems) {
        orderItemsChanges.forEach(item -> {
            ConsignOrderItems consignOrderItem = orderItems.stream().filter(a -> a.getId().compareTo(item.getOrderItemId()) == 0).findFirst().get();
            if (consignOrderItem.getQuantity().compareTo(item.getQuantity()) != 0
                    || consignOrderItem.getWeight().compareTo(item.getWeight()) != 0
                    || consignOrderItem.getCostPrice().compareTo(item.getCostPrice()) != 0
                    || consignOrderItem.getDealPrice().compareTo(item.getDealPrice()) != 0
                    ) {
                throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "二结后交易单不可变更“数量”、“重量”、“销售单价”、“采购单价”");
            }
        });
    }

    private ConsignOrderChange saveConsignOrderChange(ConsignOrder order, ConsignOrderChange orderChange, String alterStatus, User user) {
        ConsignOrderChange consignOrderChange = new ConsignOrderChange();
        consignOrderChange.setOrderId(order.getId());
        consignOrderChange.setCode(order.getCode());
        consignOrderChange.setDeliveryType(orderChange != null ? orderChange.getDeliveryType() : order.getDeliveryType());
        consignOrderChange.setDeliveryEndDate(orderChange != null ? orderChange.getDeliveryEndDate() : order.getDeliveryEndDate());
        consignOrderChange.setFeeTaker(orderChange != null ? orderChange.getFeeTaker() : order.getFeeTaker());
        consignOrderChange.setShipFee(orderChange != null ? orderChange.getShipFee() : order.getShipFee());
        consignOrderChange.setOutboundTaker(orderChange != null ? orderChange.getOutboundTaker() : order.getOutboundTaker());
        consignOrderChange.setOutboundFee(orderChange != null ? orderChange.getOutboundFee() : order.getOutboundFee());
        consignOrderChange.setContractAddress(orderChange != null ? orderChange.getContractAddress() : order.getContractAddress());
        consignOrderChange.setTransArea(orderChange != null ? orderChange.getTransArea() : order.getTransArea());
        consignOrderChange.setStatus(alterStatus);
        consignOrderChange.setCreated(new Date());
        consignOrderChange.setCreatedBy(user.getLoginId());
        consignOrderChange.setLastUpdated(new Date());
        consignOrderChange.setLastUpdatedBy(user.getLoginId());
        consignOrderChange.setModificationNumber(0);
        int flag = consignOrderChangeDao.insertSelective(consignOrderChange);
        if (flag == 0) {
            throw new BusinessException(Constant.EXCEPTIONCODE_UNKONWN, "保存订单变更信息失败");
        }
        return consignOrderChange;
    }

    private ConsignOrderItemsChange saveConsignOrderItemsChange(ConsignOrderItemsChange orderItemChange, Integer orderChangeId, String alterStatus, User user) {
        orderItemChange.setChangeOrderId(orderChangeId);
        orderItemChange.setPurchaseAmount(orderItemChange.getCostPrice().multiply(orderItemChange.getWeight()).setScale(2, BigDecimal.ROUND_HALF_UP));
        orderItemChange.setSaleAmount(orderItemChange.getDealPrice().multiply(orderItemChange.getWeight()).setScale(2, BigDecimal.ROUND_HALF_UP));
        orderItemChange.setStatus(alterStatus);
        orderItemChange.setCreated(new Date());
        orderItemChange.setCreatedBy(user.getLoginId());
        orderItemChange.setLastUpdated(new Date());
        orderItemChange.setLastUpdatedBy(user.getLoginId());
        orderItemChange.setModificationNumber(0);
        int flag = consignOrderItemsChangeDao.insertSelective(orderItemChange);
        if (flag == 0) {
            throw new BusinessException(Constant.EXCEPTIONCODE_UNKONWN, "保存订单变更详情信息失败");
        }
        return orderItemChange;
    }

    private ConsignOrderItemsChange saveConsignOrderItemsChangeByOrderItem(ConsignOrderItems orderItem, Integer orderChangeId, String alterStatus, User user) {
        ConsignOrderItemsChange orderItemsChange = new ConsignOrderItemsChange();
        orderItemsChange.setOrderItemId(orderItem.getId());
        orderItemsChange.setOrderId(orderItem.getOrderId());
        orderItemsChange.setChangeOrderId(orderChangeId);
        orderItemsChange.setSellerId(orderItem.getSellerId());
        orderItemsChange.setSellerName(orderItem.getSellerName());
        orderItemsChange.setDepartmentId(orderItem.getDepartmentId());
        orderItemsChange.setDepartmentName(orderItem.getDepartmentName());
        orderItemsChange.setContactId(orderItem.getContactId());
        orderItemsChange.setContactName(orderItem.getContactName());
        orderItemsChange.setNsortName(orderItem.getNsortName());
        orderItemsChange.setMaterial(orderItem.getMaterial());
        orderItemsChange.setSpec(orderItem.getSpec());
        orderItemsChange.setFactory(orderItem.getFactory());
        orderItemsChange.setCity(orderItem.getCity());
        orderItemsChange.setWarehouse(orderItem.getWarehouse());
        orderItemsChange.setQuantity(orderItem.getQuantity());
        orderItemsChange.setWeight(orderItem.getWeight());
        orderItemsChange.setWeightConcept(orderItem.getWeightConcept());
        orderItemsChange.setCostPrice(orderItem.getCostPrice());
        orderItemsChange.setDealPrice(orderItem.getDealPrice());
        orderItemsChange.setPurchaseAmount(orderItem.getCostPrice().multiply(orderItem.getWeight()).setScale(2, BigDecimal.ROUND_HALF_UP));
        orderItemsChange.setSaleAmount(orderItem.getDealPrice().multiply(orderItem.getWeight()).setScale(2, BigDecimal.ROUND_HALF_UP));
        orderItemsChange.setIsPayedByAcceptDraft(orderItem.getIsPayedByAcceptDraft());
        orderItemsChange.setAcceptDraftId(orderItem.getAcceptDraftId());
        orderItemsChange.setAcceptDraftCode(orderItem.getAcceptDraftCode());
        orderItemsChange.setStatus(alterStatus);
        orderItemsChange.setCreated(new Date());
        orderItemsChange.setCreatedBy(user.getLoginId());
        orderItemsChange.setLastUpdated(new Date());
        orderItemsChange.setLastUpdatedBy(user.getLoginId());
        orderItemsChange.setModificationNumber(0);
        int flag = consignOrderItemsChangeDao.insertSelective(orderItemsChange);
        if (flag == 0) {
            throw new BusinessException(Constant.EXCEPTIONCODE_UNKONWN, "保存原订单变更详情信息失败");
        }
        return orderItemsChange;
    }

    private void saveConsignOrderItemsChangeRecord(ConsignOrderItemsChange orderItemChange, ConsignOrderItems orderItem, String alterStatus, User user) {
        if (orderItemChange.getWeight() == null) {
            orderItemChange.setWeight(BigDecimal.ZERO);
        }
        if (orderItemChange.getCostPrice() == null) {
            orderItemChange.setCostPrice(BigDecimal.ZERO);
        }
        if (orderItemChange.getDealPrice() == null) {
            orderItemChange.setDealPrice(BigDecimal.ZERO);
        }
        ConsignOrderItemsChangedrecord changedrecord = new ConsignOrderItemsChangedrecord();
        changedrecord.setItemChangeId(orderItemChange.getId());
        changedrecord.setOrderItemId(orderItem.getId());
        changedrecord.setOrderId(orderItemChange.getOrderId());
        changedrecord.setSellerId(orderItemChange.getSellerId());
        changedrecord.setSellerName(orderItemChange.getSellerName());
        changedrecord.setDepartmentId(orderItemChange.getDepartmentId());
        changedrecord.setDepartmentName(orderItemChange.getDepartmentName());
        changedrecord.setContactId(orderItemChange.getContactId());
        changedrecord.setContactName(orderItemChange.getContactName());

        changedrecord.setNsortName(orderItem.getNsortName().equals(orderItemChange.getNsortName()) ? null : orderItemChange.getNsortName());
        changedrecord.setMaterial(orderItem.getMaterial().equals(orderItemChange.getMaterial()) ? null : orderItemChange.getMaterial());
        changedrecord.setSpec(orderItem.getSpec().equals(orderItemChange.getSpec()) ? null : orderItemChange.getSpec());
        changedrecord.setFactory(orderItem.getFactory().equals(orderItemChange.getFactory()) ? null : orderItemChange.getFactory());
        changedrecord.setCity(orderItem.getCity().equals(orderItemChange.getCity()) ? null : orderItemChange.getCity());
        changedrecord.setWarehouse(orderItem.getWarehouse().equals(orderItemChange.getWarehouse()) ? null : orderItemChange.getWarehouse());
        changedrecord.setQuantity(orderItem.getQuantity().equals(orderItemChange.getQuantity()) ? null : orderItemChange.getQuantity());
        changedrecord.setWeight(orderItem.getWeight().compareTo(orderItemChange.getWeight()) == 0 ? null : orderItemChange.getWeight());

        changedrecord.setWeightConcept(orderItem.getWeightConcept().equals(orderItemChange.getWeightConcept()) ? null : orderItemChange.getWeightConcept());
        changedrecord.setWarehouse(orderItem.getWarehouse().equals(orderItemChange.getWarehouse()) ? null : orderItemChange.getWarehouse());
        changedrecord.setCostPrice(orderItem.getCostPrice().compareTo(orderItemChange.getCostPrice()) == 0 ? null : orderItemChange.getCostPrice());
        changedrecord.setDealPrice(orderItem.getDealPrice().compareTo(orderItemChange.getDealPrice()) == 0 ? null : orderItemChange.getDealPrice());

        BigDecimal changePurchaseAmount = orderItemChange.getCostPrice().multiply(orderItemChange.getWeight()).setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal purchaseAmount = orderItem.getCostPrice().multiply(orderItem.getWeight()).setScale(2, BigDecimal.ROUND_HALF_UP);
        changedrecord.setPurchaseAmount(purchaseAmount.compareTo(changePurchaseAmount) == 0 ? null : changePurchaseAmount);

        BigDecimal changeSaleAmount = orderItemChange.getDealPrice().multiply(orderItemChange.getWeight()).setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal saleAmount = orderItem.getDealPrice().multiply(orderItem.getWeight()).setScale(2, BigDecimal.ROUND_HALF_UP);
        changedrecord.setSaleAmount(saleAmount.compareTo(changeSaleAmount) == 0 ? null : changeSaleAmount);

        changedrecord.setIsPayedByAcceptDraft(orderItem.getIsPayedByAcceptDraft().equals(orderItemChange.getIsPayedByAcceptDraft()) ? orderItem.getIsPayedByAcceptDraft() : orderItemChange.getIsPayedByAcceptDraft());
        changedrecord.setAcceptDraftId(orderItem.getAcceptDraftId().equals(orderItemChange.getAcceptDraftId()) ? null : orderItemChange.getAcceptDraftId());
        changedrecord.setAcceptDraftCode(orderItem.getAcceptDraftCode().equals(orderItemChange.getAcceptDraftCode()) ? null : orderItemChange.getAcceptDraftCode());
        if(orderItem.getStrappingNum()==null)orderItem.setStrappingNum("");
        changedrecord.setStrappingNum(orderItem.getStrappingNum().equals(orderItemChange.getStrappingNum()) ? null : orderItemChange.getStrappingNum());
        changedrecord.setStatus(alterStatus);
        changedrecord.setType(OrderChangeType.UPDATE.getCode());
        changedrecord.setCreated(new Date());
        changedrecord.setCreatedBy(user.getName());
        changedrecord.setLastUpdated(new Date());
        changedrecord.setLastUpdatedBy(user.getName());
        changedrecord.setModificationNumber(0);
        int flag = consignOrderItemsChangedrecordDao.insertSelective(changedrecord);
        if (flag == 0) {
            throw new BusinessException(Constant.EXCEPTIONCODE_UNKONWN, "保存订单明细变更记录表失败");
        }
    }

    private void saveConsignOrderItemsChangeRecord(ConsignOrderItemsChangedrecord changedRecord, Integer orderItemChangeId, String alterStatus, User user){
        changedRecord.setItemChangeId(orderItemChangeId);
        changedRecord.setStatus(alterStatus);
        changedRecord.setCreatedBy(user.getName());
        changedRecord.setLastUpdatedBy(user.getName());
        changedRecord.setModificationNumber(0);
        int flag = consignOrderItemsChangedrecordDao.insertSelective(changedRecord);
        if (flag == 0) {
            throw new BusinessException(Constant.EXCEPTIONCODE_UNKONWN, "保存原订单明细变更记录表失败");
        }
    }

    private void saveConsignOrderItemsOperatedRecord(Long orderId, Integer orderChangeId, String alterStatus,String note, User user) {
        ConsignOrderItemsOperatedrecord operatedrecord = new ConsignOrderItemsOperatedrecord();
        operatedrecord.setOrderId(orderId);
        operatedrecord.setChangeOrderId(orderChangeId);
        operatedrecord.setSetToStatus(alterStatus);
        operatedrecord.setOperatorId(user.getId());
        operatedrecord.setOperatorName(user.getName());
        operatedrecord.setNote(note);
        operatedrecord.setLastUpdated(new Date());
        operatedrecord.setCreated(new Date());
        operatedrecord.setCreatedBy(user.getLoginId());
        operatedrecord.setLastUpdated(new Date());
        operatedrecord.setLastUpdatedBy(user.getLoginId());
        operatedrecord.setModificationNumber(0);
        int flag = consignOrderItemsOperatedrecordDao.insertSelective(operatedrecord);
        if (flag == 0) {
            throw new BusinessException(Constant.EXCEPTIONCODE_UNKONWN, "保存订单变更操作记录表失败");
        }
    }

    private void updateConsignOrderAlterStatus(ConsignOrder order, String alterStatus, User user) {
        order.setAlterStatus(alterStatus);
        order.setLastUpdated(new Date());
        order.setLastUpdatedBy(user.getLoginId());
        order.setModificationNumber(order.getModificationNumber() + 1);
        int flag = consignOrderService.updateByPrimaryKeySelective(order);
        if (flag == 0) {
            throw new BusinessException(Constant.EXCEPTIONCODE_UNKONWN, "修改订单表变更状态失败");
        }
    }

    @Override
    @Transactional
    public ResultDto relateOrder(Integer orderChangeId, BigDecimal totalAmount, BigDecimal totalRelatedAmount, Boolean takeoutSecondBalance, User operator, String orderItemsChangeList, Boolean takeoutCreditBalance) {
        ResultDto resultDto = new ResultDto();
        BigDecimal toReleteAmount = totalAmount.subtract(totalRelatedAmount); //待关联金额

        // 查询变更订单信息
        ConsignOrderChange order = selectByPrimaryKey(orderChangeId);
        if (order == null) {
            resultDto.setSuccess(false);
            resultDto.setMessage("没有找到对应的订单！");
            return resultDto;
        }
        if (!ConsignOrderAlterStatus.PENDING_RELATE.getCode().equals(
                order.getStatus())) {
            resultDto.setSuccess(false);
            resultDto.setMessage("该交易单已处理！");
            return resultDto;
        }

        // 原订单信息
        ConsignOrder originalOrder = consignOrderService.queryById(order.getOrderId());
        Long departmentId = originalOrder.getDepartmentId();
        Account account = accountDao.selectByPrimaryKey(departmentId);  //这里应该用部门id edit by rabbit

        BigDecimal usableAmount = account.getBalance();
        if (takeoutSecondBalance) {
            usableAmount = usableAmount.add(account.getBalanceSecondSettlement());
        }
        //勾选可信用额度且资金账户余额不足且可用信用额度为正时使用信用额度
        Boolean isUseCredit = Boolean.FALSE;//是否使用信用额度 界面勾选且资金不足且可用额度大于0
        BigDecimal useCreditAmount = BigDecimal.ZERO;//使用信用额度值
        BigDecimal balanceCreditAmount = consignOrderService.calculateBalanceCreditAmountOfDeptByDeptId(departmentId);
        if (takeoutCreditBalance && usableAmount.compareTo(toReleteAmount) < 0
                && balanceCreditAmount.compareTo(BigDecimal.ZERO) > 0){
            isUseCredit = Boolean.TRUE;
            BigDecimal needUseCreditAmount = toReleteAmount.subtract(usableAmount);//计算订单金额还需使用信用额度值
            useCreditAmount = needUseCreditAmount.compareTo(balanceCreditAmount) > 0 ? balanceCreditAmount : needUseCreditAmount;
            usableAmount = usableAmount.add(useCreditAmount);
        }
        SysSetting sysSetting = sysSettingService.queryBySettingType(SysSettingType.BuyerAllowAmountTolerance.getCode());
        Double allowance = Double.parseDouble(sysSetting.getSettingValue());
        double diffAmount = toReleteAmount.subtract(usableAmount).doubleValue();
        if (diffAmount > 0) {
            if (diffAmount <= allowance) {
                toReleteAmount = usableAmount;
            } else {
                resultDto.setSuccess(false);
                resultDto.setMessage("可用金额小于订单金额，不能关联");
                return resultDto;
            }
        }

        String statusToUpdate;
        // 变更订单资源信息
        List<ConsignOrderItemsChange> orderChangeItems = queryOrderItemsByChangeOrderId(orderChangeId);

        // 原订单没有申请付款，或原订单已付款并且已付金额大于等于变更后的采购金额，直接变更成功
        boolean toSuccess = false;
        boolean isPay = false; //add by wangxianjun 关联变更成功只有已申请付款的订单在变更关联时才需要解锁和支付合同货款
        if(0 == payRequestDao.queryApplyPayCount(originalOrder.getId())){
            toSuccess = true;
        }else {

            // 变更订单采购金额
            BigDecimal costAmountChange = new BigDecimal(orderChangeItems.stream().mapToDouble(a -> a.getPurchaseAmount().doubleValue()).sum()).setScale(2, BigDecimal.ROUND_HALF_UP); //四舍五入保留两位小数做比较
            BigDecimal paymentAmount = payRequestDao.queryPayAmount(originalOrder.getId()).setScale(2, BigDecimal.ROUND_HALF_UP); //已付款金额
            if( null != paymentAmount && paymentAmount.compareTo(costAmountChange) >= 0 ){
                toSuccess = true;
                isPay = true;
            }

        }
        if(toSuccess){
            statusToUpdate = ConsignOrderAlterStatus.CHANGED_SUCCESS2.getCode();
            // 将修改的关联金额、二次结算余额保存到原订单
            //originalOrder.setTotalContractReletedAmount(originalOrder.getTotalContractReletedAmount().add(toReleteAmount));
           // originalOrder.setSecondBalanceTakeout(originalOrder.getSecondBalanceTakeout().add(takeoutSecondBalance ? account.getBalanceSecondSettlement() : BigDecimal.ZERO));
            // 更新订单详情表 modify wangxianjun 在updateOrderByChange里有更新订单详情表
            //updateOriginalOrderItems(orderChangeItems, operator);
        }
        // 否则需要重新申请付款
        else{
            statusToUpdate = ConsignOrderAlterStatus.PENDING_APPLY.getCode();
        }


        // 修改合同变更相关表的状态以及添加操作记录
        order.setChangeRelateAmount(toReleteAmount);//合同关联金额
        order.setSecondBalanceTakeout(takeoutSecondBalance ? account.getBalanceSecondSettlement() : BigDecimal.ZERO);

        //更新原订单数据
        updateOrderByChange(originalOrder, order, statusToUpdate, orderChangeItems, operator);
        updateChangeStatus(order, statusToUpdate, null, operator);

        //银票票号列表
        List<ConsignOrderItemsChange> acceptDraftList = jsonToOrderItems(orderItemsChangeList);
        if(acceptDraftList != null && acceptDraftList.size() > 0){
            if(consignOrderItemsChangeDao.batchUpdateAcceptDraft(acceptDraftList) == 0) {
                throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "银票票号更新失败！");
            }
        }

        if (takeoutSecondBalance && account.getBalanceSecondSettlement().compareTo(BigDecimal.ZERO) > 0) { //二结转入资金账户
            accountFundService.updateAccountFund(departmentId, AssociationType.ORDER_CODE, order.getCode(),
                    AccountTransApplyType.INTO_THE_CAPITAL_ACCOUNT,
                    account.getBalanceSecondSettlement(), BigDecimal.ZERO, BigDecimal.ZERO.subtract(account.getBalanceSecondSettlement()),
                    BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, PayType.BALANCE, operator.getId(),
                    operator.getName(), new Date());
        }
        //如果勾选可用信用额度且使用了信用额度，就先做信用额度金额划转到资金账户
        if (isUseCredit) {
            //调用接口增加最后的判断：可使用的信用额度够不够
            balanceCreditAmount = consignOrderService.calculateBalanceCreditAmountOfDeptByDeptId(departmentId);
            if (useCreditAmount.compareTo(balanceCreditAmount) > 0) {
                throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "可用信用额度值已改变，不够关联订单，请刷新!");
            }
            if (useCreditAmount.compareTo(BigDecimal.ZERO) > 0) {
                accountFundService.updateAccountFund(departmentId, AssociationType.ORDER_CODE, order.getCode(),
                        AccountTransApplyType.CREDITLIMIT_TRANSTO_ACCOUNT,
                        useCreditAmount, BigDecimal.ZERO, BigDecimal.ZERO,
                        BigDecimal.ZERO, useCreditAmount, BigDecimal.ZERO, PayType.BALANCE, operator.getId(),
                        operator.getName(), new Date());    //信用额度金额划转到资金账户
            }
        }
        //合同金额冻结
        accountFundService.updateAccountFund(departmentId, AssociationType.ORDER_CODE, order.getCode(),
                AccountTransApplyType.BALANCES_LOCK, BigDecimal.ZERO.subtract(toReleteAmount),
                toReleteAmount, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, PayType.BALANCE, operator.getId(), operator.getName(), new Date()); //冻结资金账户余额
        //资金还款二结
        if (takeoutSecondBalance && account.getBalanceSecondSettlement().compareTo(BigDecimal.ZERO) < 0) {
            accountFundService.updateAccountFund(departmentId, AssociationType.ORDER_CODE, order.getCode(),
                    AccountTransApplyType.SECONDARY_SETTLEMENT_ACCOUNT_BALANCES,
                    account.getBalanceSecondSettlement(), BigDecimal.ZERO, BigDecimal.ZERO.subtract(account.getBalanceSecondSettlement()),
                    BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, PayType.BALANCE, operator.getId(),
                    operator.getName(), new Date());
        }
        //关联变更成功只有已申请付款的订单在变更关联时才需要解锁和支付合同货款 add by wangxianjun
        if(isPay){
            accountFundService.updateAccountFund(departmentId, AssociationType.ORDER_CODE, order.getCode(),
                    AccountTransApplyType.BALANCES_UNLOCK, toReleteAmount,
                    BigDecimal.ZERO.subtract(toReleteAmount), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, PayType.BALANCE,
                    operator.getId(), operator.getName(), new Date()); //解锁账户
            accountFundService.updateAccountFund(departmentId, AssociationType.ORDER_CODE, order.getCode(),
                    AccountTransApplyType.PAY_THE_CONTRACT_PAYMENT, BigDecimal.ZERO.subtract(toReleteAmount),
                    BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, PayType.BALANCE, operator.getId(), operator.getName(), new Date()); //支付合同货款

        }

        //将金额变化，添加到report_buyer_invoice_out
        //reportFinaceService.pushToReportInvoiceOut(originalOrder, operator, toReleteAmount, ReportBuyerInvoiceOutType.Approved, null);

        String seller_name = orderChangeItems.get(0).getSellerName();
        Account sellerAccount = accountDao.selectByPrimaryKey(orderChangeItems.get(0).getSellerId());
        //如果有卖家支持银票预付发送短信通知
        if("1".equals(sellerAccount.getPaymentLabel())){
            try{
                List<ConsignOrderProcess> orderProcessList = consignOrderProcessDao.getRelateProcessByUserId(operator.getId());
                String content = "银票预付" + Tools.dateToStr(new Date(), "yyyy年MM月dd日 HH:mm:ss")+"，交易单号："+order.getCode()+"（" +seller_name +"） 该交易单发生合同变更，订单金额：" +toReleteAmount+ "订单状态：已关联合同";
                for(ConsignOrderProcess orderProcess : orderProcessList){
                    Nido.start(NidoTaskConstant.SEND_NOTE_MESSAGE, new NoteMessageContext(orderProcess.getOperatorMobile(), content));
                }
                resultDto.setSuccess(true);
                resultDto.setMessage("合同关联成功,已短信通知金融服务部");
            }catch(Exception e){}
        }


        resultDto.setSuccess(true);
        return resultDto;
    }

    /**
     * 解析关联银票票号json字符串
     * @param orderItemsList
     * @return
     */
    public List<ConsignOrderItemsChange> jsonToOrderItems(String orderItemsList) {
        List<ConsignOrderItemsChange> list = new ArrayList<ConsignOrderItemsChange>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode orderNode = mapper.readTree(orderItemsList);
            ConsignOrderItemsChange item;
            for (JsonNode node : orderNode) {
                item = new ConsignOrderItemsChange();
                item.setId(node.path("orderItemsChangeId").asInt());
                item.setAcceptDraftId(node.path("acceptDraftId").asLong());
                item.setAcceptDraftCode(node.path("acceptDraftCode").asText());
                list.add(item);
            }
        }catch (Exception e){
            log.debug("解析关联银票票号json字符串错误：", e);
        }
        return list;
    }

    @Override
    @Transactional
    public ResultDto closeAssociateOrder(Integer orderChangeId, String cause, User operator) {
        ResultDto resultDto = new ResultDto();
        String statusToUpdate = ConsignOrderAlterStatus.CLOSED.getCode();

        ConsignOrderChange order = selectByPrimaryKey(orderChangeId);// 变更订单信息
        if (!ConsignOrderAlterStatus.PENDING_RELATE.getCode().equals(
                order.getStatus())) {
            resultDto.setSuccess(false);
            resultDto.setMessage("该交易单已处理！");
            return resultDto;
        }

        // 修改合同变更相关表的状态以及添加操作记录
        updateChangeStatus(order, statusToUpdate, cause, operator);

        //更新原订单状态
        ConsignOrder originalOrder = consignOrderService.queryById(order.getOrderId());
        originalOrder.setAlterStatus(statusToUpdate);
        originalOrder.setLastUpdated(new Date());
        originalOrder.setLastUpdatedBy(operator.getName());
        if (consignOrderService.updateByPrimaryKeySelective(originalOrder) == 0) {
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "原订单状态更新失败！");
        }

        resultDto.setSuccess(true);
        return resultDto;
    }

    /**
     * 更新订单变更明细记录表状态
     * @param orderChangeItems
     * @param statusToUpdate
     * @param operator
     */
    private void updateItemsChangedrecordStatus(List<ConsignOrderItemsChange> orderChangeItems, String statusToUpdate, User operator){
        //订单变更明细记录表 更新status
        List<ConsignOrderItemsChangedrecord> orderChangeItemsRecords = new ArrayList<ConsignOrderItemsChangedrecord>();
        List<Integer> itemChangeIds = orderChangeItems.stream().map(a -> a.getId()).collect(Collectors.toList());
        orderChangeItemsRecords = consignOrderItemsChangedrecordDao.queryByItemChangeIds(itemChangeIds);
        for(ConsignOrderItemsChangedrecord  changeRecord : orderChangeItemsRecords ){
            changeRecord.setStatus(statusToUpdate);
            changeRecord.setLastUpdated(new Date());
            changeRecord.setLastUpdatedBy(operator.getName());
            if(consignOrderItemsChangedrecordDao.updateByPrimaryKeySelective(changeRecord) == 0){
                throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "订单变更明细记录表更新失败！");
            }
        }
    }

    /**
     * 更新订单详情表
     * @param itemsChangeList
     * @param operator
     */
    private void updateOriginalOrderItems(List<ConsignOrderItemsChange> itemsChangeList, User operator){
        List<ConsignOrderItems> consignOrderItems = consignOrderItemsDao.selectByOrderId(itemsChangeList.get(0).getOrderId());//原订单资源信息
        //待删除数据，变更成功则删除
        Optional<ConsignOrderItemsChange> toDetelteItemsOptional = itemsChangeList.stream().filter(a -> a.getStatus().equals(ConsignOrderAlterStatus.PENDING_DEL_APPROVAL.getCode())).findFirst();
        List<ConsignOrderItemsChange> toDeleteChangeItems = new ArrayList<ConsignOrderItemsChange>();
        if (toDetelteItemsOptional.isPresent()) {
            toDeleteChangeItems.add(toDetelteItemsOptional.get());
        }
        //原订单详情表删除，变更订单详情表改状态为已删除
        if(CollectionUtils.isNotEmpty(toDeleteChangeItems)){
            for(ConsignOrderItemsChange item : toDeleteChangeItems){
                consignOrderItemsDao.deleteByPrimaryKey(item.getOrderItemId());
                item.setStatus(ConsignOrderAlterStatus.DEL.getCode());
                item.setModificationNumber(item.getModificationNumber() + 1);
                consignOrderItemsChangeDao.updateByPrimaryKeySelective(item);
            }
        }

        List<ConsignOrderItemsChange> orderChangeItems = queryOrderItemsExceptStatusByChangeOrderId(itemsChangeList.get(0).getChangeOrderId(), ConsignOrderAlterStatus.DEL.getCode());
        // 新增、修改原订单详情信息
        for(ConsignOrderItemsChange itemsChange : orderChangeItems){
            ConsignOrderItems item = new ConsignOrderItems(itemsChange, false);
            item.setLastUpdated(new Date());
            item.setLastUpdatedBy(operator.getLoginId());
            // 变更若新增详情记录则原订单详情表新增记录
            if(0 == itemsChange.getOrderItemId().intValue()){
                // item 设置卖家、联系人等信息
                item.setOrderId(itemsChange.getOrderId());
                item.setSellerId(consignOrderItems.get(0).getSellerId());
                item.setSellerName(consignOrderItems.get(0).getSellerName());
                item.setDepartmentId(consignOrderItems.get(0).getDepartmentId());
                item.setDepartmentName(consignOrderItems.get(0).getDepartmentName());
                item.setContactId(consignOrderItems.get(0).getContactId());
                item.setContactName(consignOrderItems.get(0).getContactName());
                item.setCreated(new Date());
                item.setCreatedBy(operator.getName());
                int flag = consignOrderItemsDao.insertSelective(item);
                if(flag == 0){
                    throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "订单详情表新增失败！");
                }
                // 把订单详情Id反写到变更详情表，变更详情记录表
                itemsChange.setOrderItemId(item.getId());
                flag = consignOrderItemsChangeDao.updateByPrimaryKeySelective(itemsChange);
                if(flag == 0){
                    throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "变更详情表修改订单Id失败！");
                }
                ConsignOrderItemsChangedrecord changedrecord = new ConsignOrderItemsChangedrecord();
                changedrecord.setOrderItemId(item.getId());
                changedrecord.setItemChangeId(itemsChange.getId());
                changedrecord.setLastUpdated(new Date());
                changedrecord.setLastUpdatedBy(operator.getName());
                flag = consignOrderItemsChangedrecordDao.updateByItemChangeId(changedrecord);
                if(flag == 0){
                    throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "变更详情记录表修改订单Id失败！");
                }
            }
            // 变更若修改详情则将修改信息反写到原订单详情表
            else{
                item.setId(itemsChange.getOrderItemId());
                item.setModificationNumber(consignOrderItemsDao.selectByPrimaryKey(item.getId()).getModificationNumber() + 1);
                int flag = consignOrderItemsDao.updateByPrimaryKeySelective(item);
                if(flag == 0){
                    throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "订单详情表更新失败！");
                }

            }
        }

    }


    @Override
    public List<ConsignOrderSellerInfoDto> getchangeSellerInfo(Long orderId, Integer orderChangId) {
    	List<ConsignOrderSellerInfoDto> sellerInfoList = consignOrderItemsChangeDao.getchangeSellerInfo(orderChangId);
    	List<ConsignOrderSellerInfoDto> sellerPayMentList = consignOrderItemsChangeDao.getPayment(orderId);
    	for (ConsignOrderSellerInfoDto sellerInfoDto : sellerInfoList) {
    		for (ConsignOrderSellerInfoDto sellerPayMent : sellerPayMentList) {
    			if (sellerInfoDto.getOrderId().equals(sellerPayMent.getOrderId())) {
    				sellerInfoDto.setOrderAmount(sellerPayMent.getOrderAmount());
    				sellerInfoDto.setPedingAmount(sellerInfoDto.getContractAmount().subtract(sellerPayMent.getOrderAmount()));
    			}
    		}
		}
        return sellerInfoList;
    }

    /**
     * 审核变更
     */
    @Transactional
    public void auditChange(Integer orderChangeId, Boolean auditStatus, String note, User user) {
        ConsignOrderChange consignOrderChange = consignOrderChangeDao.selectByPrimaryKey(orderChangeId);
        if (consignOrderChange == null) {
            throw new BusinessException(Constant.ERROR_RESOURCE_NOT_FOUND, "不存在对应的变更记录！");
        }
        if(!ConsignOrderAlterStatus.PENDING_APPROVAL.getCode().equals(consignOrderChange.getStatus())){
            throw new BusinessException(Constant.ERROR_RESOURCE_NOT_FOUND, "该变更记录的状态不是已变更待审核");
        }
        // 查询变更详情记录
        List<ConsignOrderItemsChange> orderItemsChanges = consignOrderItemsChangeDao.queryOrderItemsByChangeOrderId(orderChangeId);

        // 查询订单数据
        ConsignOrder order = consignOrderService.queryById(consignOrderChange.getOrderId());

        ConsignOrderAlterStatus alterStatus = ConsignOrderAlterStatus.CHANGED_SUCCESS1; // 默认变更成功
        // 审核通过
        if (auditStatus) {
            Boolean isSecondCount = (Integer.valueOf(order.getStatus()) > 6);//status 大于6是二结后，小于等于6是二结前
            // 已经二结，直接变更成功，状态取默认值，非二结的需要判断
            if (!isSecondCount) {
                // 查询容差
                SysSetting sysSetting = sysSettingService.queryBySettingType(SysSettingType.BuyerAllowAmountToleranceAfterContractChanged.getCode());
                Double buyerToleranceAmount = sysSetting != null ? new BigDecimal(sysSetting.getSettingValue()).doubleValue() : 0;
                // 查询订单详情数据
                List<ConsignOrderItems> orderItems = consignOrderService.queryOrderItemsById(consignOrderChange.getOrderId());
                // 合同关联金额（买家付款金额)
                Double reletedAmount = order.getTotalContractReletedAmount().doubleValue();
                // 变更的销售金额
                Double totalChangeSaleAmount = orderItemsChanges.stream().mapToDouble(a -> a.getSaleAmount().doubleValue()).sum();
                // 变更的采购金额
                Double totalChangePurchaseAmount = orderItemsChanges.stream().mapToDouble(a -> a.getPurchaseAmount().doubleValue()).sum();

                // 容差范围之外
                if (totalChangeSaleAmount - reletedAmount > buyerToleranceAmount) {
                    alterStatus = ConsignOrderAlterStatus.PENDING_RELATE;
                }
                // 容差范围之内
                else {
                    // 查询订单是否有申请付款，如果没有申请付款，则直接变更成功
                    Integer applyPayCount = payRequestDao.queryApplyPayCount(consignOrderChange.getOrderId());
                    if (applyPayCount > 0) {
                        // 如果有已付款，再判断变更后的采购金额是否小于等于已付款金额，如果小于等于，直接变更成功，
                        // 如果采购金额大于已付款金额，则需把状态变成 PENDING_APPLY待申请付款变更
                        BigDecimal payAmount = payRequestDao.queryPayAmount(consignOrderChange.getOrderId());// 已付款金额
                        if (totalChangePurchaseAmount.compareTo(payAmount.doubleValue()) > 0) {
                            alterStatus = ConsignOrderAlterStatus.PENDING_APPLY;
                        }
                    }
                }
            }

            // 生成合同号
            consignOrderChange.setContractCode(generateConsignOrderContract(order, consignOrderChange.getId(), user));

        } else {
            alterStatus = ConsignOrderAlterStatus.DISAPPROVE;   // 审核不通过
        }
        // 修改订单的变更状态
        updateOrderByChange(order,consignOrderChange, alterStatus.getCode(), orderItemsChanges, user);

        // 修改合同变更相关表的状态以及添加操作记录
        updateChangeStatus(consignOrderChange, alterStatus.getCode(), note, user);
    }

    /**
     * 变更合同申请付款
     * @author lixiang
     * @param orderChangeId 变更合同id
     * @param bankAccount 银行信息
     * @param applyMoney 申请付款金额
     * @param checkedMap
     * @param refundCredit
     * @param operator
     * @return
     */
    @Override
    @Transactional
	public void applyPay(Integer orderChangeId, Map<Long, Long> bankAccount, Map<Long, Double> applyMoney,
			Map<Long, Boolean> checkedMap, Map<Long, Boolean> refundCredit, User operator, Boolean settleChecked, Boolean creditChecked) {

        BigDecimal applyMoneyTotalMoney = BigDecimal.ZERO;
        Double totalApplyMoey = 0D;
        for (Object moneys : applyMoney.keySet()) {
			Double money = applyMoney.get(moneys);
			totalApplyMoey += money;
		}
        applyMoneyTotalMoney =new BigDecimal(totalApplyMoey);
        ConsignOrderChange orderChange = consignOrderChangeDao.selectByPrimaryKey(orderChangeId);
        if (orderChange == null) {
        	throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "申请付款失败，没有找到该变更合同信息！");
        }

        if (!ConsignOrderAlterStatus.PAYED_DISAPPROVE.getCode().equals(orderChange.getStatus())
        		&& !ConsignOrderAlterStatus.PENDING_APPLY.getCode().equals(orderChange.getStatus())) {
        	throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "申请付款失败，该变更合同已申请付款！");
        }
        //原订单信息
        ConsignOrder order = consignOrderDao.queryById(orderChange.getOrderId());
        if (order == null) {
        	throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "申请付款失败，没有找到该订单信息！");
        }
		if (!ConsignOrderAlterStatus.PENDING_APPLY.getCode().equals(order.getAlterStatus())
				&& !ConsignOrderAlterStatus.PAYED_DISAPPROVE.getCode().equals(order.getAlterStatus())) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "此订单已申请付款！");
		}
        //原订单详情
        List<ConsignOrderItems> consignOrderItems = consignOrderItemsDao.selectByOrderId(order.getId());
        //订单合同变更后卖家信息
        List<ConsignOrderSellerInfoDto> sellerInfoChangeList = consignOrderItemsChangeDao.getchangeSellerInfo(orderChangeId);
        //订单合同变更详情
        List<ConsignOrderItemsChange> orderItemsChangeList = consignOrderItemsChangeDao.queryOrderItemsByChangeOrderId(orderChangeId);

        //记录第一次变更合同时原订单的状态
        if (StringUtils.isEmpty(orderChange.getPayStatus())) {
        	orderChange.setPayStatus(order.getPayStatus());
        }

        //变更合同的合同付款金额
        BigDecimal contractAmountChange = BigDecimal.ZERO;
        if (sellerInfoChangeList == null || sellerInfoChangeList.size() <= 0) {
        	throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "申请付款失败，没有找到相应的卖家信息！");
        }

        //查询订单已申请付款的金额
        List<ConsignOrderSellerInfoDto> sellerPayMentList = consignOrderItemsChangeDao.getPayment(order.getId());

        for (ConsignOrderSellerInfoDto sellerInfoChange : sellerInfoChangeList) {
            if (!bankAccount.containsKey(sellerInfoChange.getSellerDepartmentId())) {
            	throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "申请付款失败，请正确选择银行账号信息！");
            }
            if (!applyMoney.containsKey(sellerInfoChange.getSellerDepartmentId())) {
            	throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "申请付款失败，请正确填写付款金额！");
            }
            if (applyMoney.get(sellerInfoChange.getSellerDepartmentId()) > sellerInfoChange.getContractAmount().doubleValue()) {
            	throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "申请付款失败，申请付款金额大于应付金额！");
            }
            contractAmountChange = sellerInfoChange.getContractAmount();// 合同金额
            //计算最大可申请金额 = 合同总金额 - 已付款金额
            contractAmountChange = contractAmountChange.subtract(sellerPayMentList.get(0).getOrderAmount());

            if (checkedMap.get(sellerInfoChange.getSellerDepartmentId())) {
                if (sellerInfoChange.getBalanceSecondSettlement().doubleValue() < 0) {
                	contractAmountChange = contractAmountChange.add(sellerInfoChange.getBalanceSecondSettlement());
                }
            }
            if (refundCredit.get(sellerInfoChange.getSellerDepartmentId())) {
                if (sellerInfoChange.getCreditUsed().compareTo(BigDecimal.ZERO) > 0) {
                	contractAmountChange = contractAmountChange.subtract(sellerInfoChange.getCreditUsed());
                }
            }
            if (contractAmountChange.compareTo(BigDecimal.ZERO) < 0) {
            	contractAmountChange = BigDecimal.ZERO;
            }
            if (applyMoney.get(sellerInfoChange.getSellerDepartmentId()) > contractAmountChange.doubleValue()) {
            	throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "申请付款失败，申请付款金额大于应付金额！");
            }
        }
        String orderAlterStatus = ConsignOrderAlterStatus.PENDING_APPR_PAY.getCode(); //默认变更合同状态
        //设置订单的变更合同id
        order.setChangeOrderId(orderChangeId);
        order.setCallBackStatus(ConsignOrderCallBackStatus.Normal.getCode());//申请付款时将打回状态恢复正常和打回理由清空
        order.setCallBackNote("");

        long requestId = 0L;
        //如果申请付款的金额，则需要增加付款申请单
        if (applyMoneyTotalMoney.compareTo(BigDecimal.ZERO) == 1) {
        	//生成付款申请单
        	PayRequest payRequest = getPayRequest(orderChangeId, operator, applyMoneyTotalMoney, orderChange, order);
            requestId = payRequest.getId();
            //是否抵扣二结欠款和还款信用额度
            isDeduct(bankAccount, applyMoney, checkedMap, refundCredit, operator, order, sellerInfoChangeList, requestId, sellerPayMentList.get(0).getOrderAmount());
            //记录订单状态
            getAuditTrail(operator, applyMoneyTotalMoney, order, consignOrderItems);
        } else if (applyMoneyTotalMoney.compareTo(BigDecimal.ZERO) == 0) {
        	if (settleChecked.equals(true) || creditChecked.equals(true)) {
	    		//生成付款申请单
	        	PayRequest payRequest = getPayRequest(orderChangeId, operator, applyMoneyTotalMoney, orderChange, order);
	            requestId = payRequest.getId();
	            //是否抵扣二结欠款和还款信用额度
	            isDeduct(bankAccount, applyMoney, checkedMap, refundCredit, operator, order, sellerInfoChangeList, requestId, sellerPayMentList.get(0).getOrderAmount());
	            //记录订单状态
	            getAuditTrail(operator, applyMoneyTotalMoney, order, consignOrderItems);
        	} else {
        		orderAlterStatus = ConsignOrderAlterStatus.CHANGED_SUCCESS3.getCode();
        	}
        }
        //原定订单更新
        updateOrderByChange(order, orderChange, orderAlterStatus, orderItemsChangeList, operator);

        // 订单变更主表状态更新
        updateChangeStatus(orderChange, orderAlterStatus, null, operator);

    }

	private void getAuditTrail(User operator, BigDecimal applyMoneyTotalMoney, ConsignOrder order, List<ConsignOrderItems> consignOrderItems) {
		//订单状态
		OrderAuditTrail orderAuditTrail = new OrderAuditTrail();
		orderAuditTrail.setOrderId(order.getId());
		orderAuditTrail.setOperatorId(operator.getId());
		orderAuditTrail.setOperatorName(operator.getName());
		orderAuditTrail.setLastUpdated(new Date());
		orderAuditTrail.setLastUpdatedBy(operator.getName());
		orderAuditTrail.setSetToStatus(ConsignOrderPayStatus.REQUESTED.toString());
		orderAuditTrail.setStatusType(OrderStatusType.PAY.toString());
		orderAuditTrail.setCreated(new Date());
		orderAuditTrail.setCreatedBy(operator.getName());
		orderAuditTrail.setModificationNumber(0);

		if (orderStatusDao.insertOrderAuditTrail(orderAuditTrail) > 0) {
			//发送短信通知
		    sendMsg(operator, applyMoneyTotalMoney, order);
		} else {
		    throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "日志记录未成功，申请付款失败!");
		}

		//原订单总数量
		int originalQuantityTotal = 0;
		//合同变更后的总数量
		int nowQuantityTotal = 0;

		for (ConsignOrderItems orderItems : consignOrderItems) {
			originalQuantityTotal += orderItems.getQuantity();
		}

		//如果变更订单的数量大于原订单的数量且订单的状态是已录入，需要把订单状态变成部分录入，待放货状态 变成 未全打印
		if (nowQuantityTotal > originalQuantityTotal && "3".equals(order.getPickupStatus())) {
			order.setPickupStatus(2);
			order.setFillupStatus(0);
		}
		//付款成功后 将变更合同id回写到订单里
        order.setPayStatus(ConsignOrderPayStatus.REQUESTED.toString());
	}
    /**
     * 是否抵扣
     * @param bankAccount
     * @param applyMoney
     * @param checkedMap
     * @param refundCredit
     * @param operator
     * @param order
     * @param sellerInfoChangeList
     * @param requestId
     */
	private void isDeduct(Map<Long, Long> bankAccount, Map<Long, Double> applyMoney, Map<Long, Boolean> checkedMap, Map<Long, Boolean> refundCredit, User operator, ConsignOrder order,
			List<ConsignOrderSellerInfoDto> sellerInfoChangeList, long requestId, BigDecimal orderAmount) {
		for (ConsignOrderSellerInfoDto item : sellerInfoChangeList) {
		    BigDecimal second_balance_takeout = BigDecimal.ZERO;
		    BigDecimal credit_refund_amount = BigDecimal.ZERO;
		    BigDecimal applyMoney_item = BigDecimal.ZERO;
		    BigDecimal contractAmount = item.getContractAmount().subtract(orderAmount);// 合同金额

		    //选择抵扣二次结算账户余额
		    if (checkedMap.get(item.getSellerDepartmentId())) {
		        if (item.getBalanceSecondSettlement().doubleValue() < 0) {
		            Double balanceSecondSettlement = item.getBalanceSecondSettlement().doubleValue();// 抵扣二次结算金额
		            //如果合同金额小于二次结算余额正数则用合同金额
		            if (Math.abs(contractAmount.doubleValue()) < Math.abs(balanceSecondSettlement))
		                second_balance_takeout = contractAmount;
		            else
		                second_balance_takeout = new BigDecimal(Math.abs(balanceSecondSettlement));
		            contractAmount = contractAmount.subtract(second_balance_takeout);
		            //抵扣
		            accountFundService.updateAccountFund(item.getSellerDepartmentId(), AssociationType.ORDER_CODE, order.getCode(),
		                    AccountTransApplyType.SECONDARY_SETTLEMENT_LOCK, BigDecimal.ZERO, BigDecimal.ZERO, second_balance_takeout,
		                    second_balance_takeout.negate(), BigDecimal.ZERO, BigDecimal.ZERO, PayType.BALANCE, operator.getId(), operator.getName(), new Date());
		        }
		    }
		    //选择还信用额度
		    if (contractAmount.compareTo(BigDecimal.ZERO) > 0 && refundCredit.get(item.getSellerDepartmentId())
		    		&& item.getCreditUsed().doubleValue() > 0) {
		        credit_refund_amount = contractAmount.compareTo(item.getCreditUsed()) < 0 ? contractAmount : item.getCreditUsed();
		        contractAmount=contractAmount.subtract(credit_refund_amount);
		        accountFundService.updateAccountFund(item.getSellerDepartmentId(), AssociationType.ORDER_CODE, order.getCode(),
		                AccountTransApplyType.CREDITLIMI_TREPAYMENT, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
		                BigDecimal.ZERO, credit_refund_amount.negate(), BigDecimal.ZERO, PayType.BALANCE, 0L, Constant.SYSTEMNAME, new Date());
		    }
		    applyMoney_item = new BigDecimal(applyMoney.get(item.getSellerDepartmentId()));
		    if (contractAmount.compareTo(BigDecimal.ZERO) <= 0) {
		        applyMoney_item = BigDecimal.ZERO;
		    }
	    	//付款申请单详情
	    	getPayRequestItem(bankAccount, operator, requestId, item, second_balance_takeout, credit_refund_amount, applyMoney_item);

		}
	}

    /**
     * 发送短信通知
     * @param operator
     * @param applyMoneyTotalMoney 申请付款金额
     * @param order
     */
	private void sendMsg(User operator, BigDecimal applyMoneyTotalMoney, ConsignOrder order) {
		//短信通知
		Organization organization = organizationDao.queryById(operator.getOrgId());
		User manageUser = userDao.queryById(organization.getCharger());
		DecimalFormat format = new DecimalFormat("##,###,###,###,###.00");
		String money = format.format(applyMoneyTotalMoney.doubleValue());
		String endCode = order.getCode().substring(order.getCode().length() - 6, order.getCode().length());
        SysSetting template=sysSettingService.queryBySettingType(SysSettingType.SmsTemplateApplyPay.getCode());
		if (template == null) {
		    throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,"申请付款通知审核人短信模板缺失");
		}
		String content =template.getSettingValue();
		content = content.replace("#tradername#", operator.getName());
		content = content.replace("#endcode#", endCode);
        content = content.replace("#applyamount#", money);
		Nido.start(NidoTaskConstant.SEND_NOTE_MESSAGE, new NoteMessageContext(manageUser.getTel(), content));
		String[] pushInfo = orderCacheService.getPushInfoByUserLoginId(manageUser.getLoginId());
		if(pushInfo!=null){
			content = String.format("%s已提交交易单%s元的付款申请，请审核。", operator.getName(), money);
		    appPushService.sendPushNoticfication(pushInfo[0],pushInfo[1], AppNoticeType.ApprovePayApply.getName(),content);
		}
	}

    /**
     * 付款申请单详情
     * @author lixiang
     * @param bankAccount 银行信息
     * @param operator 操作者
     * @param requestId 付款申请单id
     * @param item
     * @param second_balance_takeout 抵扣二次结算余额
     * @param credit_refund_amount 还信用额度
     * @param applyMoney_item 申请付款金额
     */
	private void getPayRequestItem(Map<Long, Long> bankAccount, User operator, long requestId, ConsignOrderSellerInfoDto item,
			BigDecimal second_balance_takeout, BigDecimal credit_refund_amount, BigDecimal applyMoney_item) {
		PayRequestItems payRequestItems = new PayRequestItems();
		// 增加收款方付款申请单号
		Organization organization = organizationDao.queryById(operator.getOrgId());
		String payCode = payRequestService.createdPayCode(organization.getSeqCode(), operator.getOrgId());
		payRequestItems.setPayCode(payCode);
		payRequestItems.setReceiverId(item.getSellerId());
		payRequestItems.setReceiverName(item.getCompanyName());

		AccountBank accountBank = accountBankDao.selectByPrimaryKey(bankAccount.get(item.getSellerDepartmentId()));
		payRequestItems.setReceiverBankName(accountBank.getBankName());
		payRequestItems.setReceiverBankCity(accountBank.getBankCityName());
		payRequestItems.setReceiverBranchBankName(accountBank.getBankNameBranch());
		payRequestItems.setReceiverBankCode(accountBank.getBankCode());
		payRequestItems.setReceiverAccountCode(accountBank.getBankAccountCode());
		payRequestItems.setSecondBalanceTakeout(second_balance_takeout);
		payRequestItems.setCreditUsedRepay(credit_refund_amount);
		payRequestItems.setReceiverTel(item.getMobile());
		payRequestItems.setReceiverRegAddr(item.getAddress());

		payRequestItems.setPayAmount(applyMoney_item);
		payRequestItems.setCreated(new Date());
		payRequestItems.setCreatedBy(operator.getName());
		payRequestItems.setLastUpdatedBy(operator.getName());
		payRequestItems.setLastUpdated(new Date());
		payRequestItems.setModificationNumber(0);
        payRequestItems.setRequestId(requestId);
		payRequestItems.setReceiverDepartmentId(item.getSellerDepartmentId());
		payRequestItems.setReceiverDepartmentName(item.getSellerDepartmentName());
		if (payRequestItemsDao.insertSelective(payRequestItems) == 0) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "生成付款申请单失败，申请付款失败！");
		}
	}

	/**
	 * 付款单信息
	 * @author lixiang
	 * @param orderChangeId 变更合同id
	 * @param operator 操作者
	 * @param applyMoneyTotalMoney 申请付款金额
	 * @param orderChange 变更合同信息
	 * @param order 订单信息
	 * @return
	 */
	private PayRequest getPayRequest(Integer orderChangeId, User operator, BigDecimal applyMoneyTotalMoney,
			ConsignOrderChange orderChange, ConsignOrder order) {
		String code = payRequestService.createCode();
		//还款
		PayRequest payRequest = new PayRequest();
		payRequest.setChangeOrderId(orderChangeId);
		payRequest.setConsignOrderId(order.getId());
		payRequest.setConsignOrderCode(order.getCode());
		payRequest.setContractCode(orderChange.getContractCode());
		payRequest.setRequesterId(operator.getId());
		payRequest.setRequesterName(operator.getName());
		payRequest.setOrgId(operator.getOrgId());
		payRequest.setCode(code);
		payRequest.setBuyerId(order.getAccountId());
		payRequest.setBuyerName(order.getAccountName());
		payRequest.setTotalAmount(applyMoneyTotalMoney);
		payRequest.setCreated(new Date());
		payRequest.setCreatedBy(operator.getName());
		payRequest.setType(PayRequestType.ORDER_CHANGE.getCode());
		payRequest.setPayStatus(ConsignOrderPayStatus.REQUESTED.toString());// add by lixiang 货币资金报表专用
		payRequest.setStatus(PayStatus.REQUESTED.toString());
		payRequest.setLastUpdatedBy(operator.getName());
		payRequest.setLastUpdated(new Date());
		payRequest.setModificationNumber(0);
		payRequest.setPrintTimes(0);
		payRequest.setDepartmentId(order.getDepartmentId());
		payRequest.setDepartmentName(order.getDepartmentName());
		if (payRequestDao.insertSelective(payRequest) == 0) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "生成付款申请单失败，申请付款失败！");
		}
		return payRequest;
	}

	@Override
	@Transactional
	public void closedOrderChange(Integer orderChangeId, String cause, User user) {
        // 查询变更订单信息
		ConsignOrderChange orderChange = consignOrderChangeDao.selectByPrimaryKey(orderChangeId);
		if (orderChange == null) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "没有找到对应的变更合同！");
		}
		if (!ConsignOrderAlterStatus.PAYED_DISAPPROVE.getCode().equals(orderChange.getStatus())
				&& !ConsignOrderAlterStatus.PENDING_APPLY.getCode().equals(orderChange.getStatus())) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "关闭失败，变更订单状态异常！");
		}
		// 订单合同变更详情
		List<ConsignOrderItemsChange> orderItemsChangeList = consignOrderItemsChangeDao
				.queryOrderItemsByChangeOrderId(orderChangeId);
		// 原订单信息
		ConsignOrder order = consignOrderService.queryById(orderChange.getOrderId());
		if (order == null) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "没有找到对应的订单！");
		}
        // 修改变更合同状态
        ConsignOrderChange newOrderChange = new ConsignOrderChange();
        newOrderChange.setStatus(ConsignOrderAlterStatus.CLOSED.getCode());
		newOrderChange.setId(orderChange.getId());
		newOrderChange.setLastUpdated(new Date());
		newOrderChange.setLastUpdatedBy(user.getLoginId());
		if (consignOrderChangeDao.updateByPrimaryKeySelective(newOrderChange) == 0) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "修改合同变更主表失败，保存变更合同失败！");
        }
        // 修改变更合同详情状态
        orderItemsChangeList.forEach(items -> {
            ConsignOrderItemsChange itemsChange = new ConsignOrderItemsChange();
            itemsChange.setStatus(ConsignOrderAlterStatus.CLOSED.getCode());
            itemsChange.setId(items.getId());
            itemsChange.setLastUpdated(new Date());
            itemsChange.setLastUpdatedBy(user.getLoginId());
            if (consignOrderItemsChangeDao.updateByPrimaryKeySelective(itemsChange) == 0) {
                throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "保存变更合同详情失败！");
            }
        });

        // 订单明细变更记录
		updateItemsChangedrecordStatus(orderItemsChangeList, ConsignOrderAlterStatus.CLOSED.getCode(), user);

        // 订单变更操作记录
        saveConsignOrderItemsOperatedRecord(order.getId(), orderChangeId, ConsignOrderAlterStatus.CLOSED.getCode(), cause, user);

        // 原订单alter_status状态修改
        ConsignOrder newOrder = new ConsignOrder();
        newOrder.setAlterStatus(ConsignOrderAlterStatus.CLOSED.getCode());
		newOrder.setChangeOrderId(orderChangeId);
        newOrder.setLastUpdated(new Date());
		newOrder.setLastUpdatedBy(user.getLoginId());
		newOrder.setId(order.getId());
		newOrder.setPayStatus(orderChange.getPayStatus());
		consignOrderDao.updateByPrimaryKeySelective(newOrder);
		if (null != orderChange.getChangeRelateAmount()) {
			// 解锁买家锁定资金账户余额
			accountFundService.updateAccountFund(order.getDepartmentId(), AssociationType.ORDER_CODE, order.getCode(),
					AccountTransApplyType.BALANCES_UNLOCK, orderChange.getChangeRelateAmount(),
					orderChange.getChangeRelateAmount().negate(), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
					BigDecimal.ZERO, PayType.BALANCE, user.getId(), user.getName(), new Date());
		}

		// 如信用额度存在欠款，则发起还款动作
        if (accountDao.selectByPrimaryKey(order.getDepartmentId()).getCreditAmountUsed().compareTo(BigDecimal.ZERO) == 1) {
			accountFundService.payForCredit(order.getDepartmentId(), AssociationType.ORDER_CODE, order.getCode(), 0L,
					Constant.SYSTEMNAME, new Date());
		}

	}

    @Override
    public ConsignOrderItems getSellerTotalItems(List<ConsignOrderItems> orderItems) {
        // 卖家合计
        ConsignOrderItems totalItems = new ConsignOrderItems();
        if (orderItems != null && orderItems.size() > 0) {
            int count = 0;
            BigDecimal weight = BigDecimal.ZERO, costprice = BigDecimal.ZERO, dealprice = BigDecimal.ZERO, amount = BigDecimal.ZERO, actualPickWeight = BigDecimal.ZERO;
            for (ConsignOrderItems item : orderItems) {
                count += item.getQuantity();
                if (item.getWeight() != null) {
                    weight = weight.add(item.getWeight());
                }
                if (item.getCostPrice() != null) {
                    costprice = costprice.add(item.getCostPrice());
                }
                if (item.getDealPrice() != null) {
                    dealprice = dealprice.add(item.getDealPrice());
                }
                if (item.getAmount() != null) {
                    amount = amount.add(item.getAmount());
                }
                if (item.getActualPickWeightServer() != null) {
                    actualPickWeight = actualPickWeight.add(item.getActualPickWeightServer());
                }
            }
            totalItems.setQuantity(count);
            totalItems.setWeight(weight);
            totalItems.setCostPrice(costprice);
            totalItems.setDealPrice(dealprice);
            totalItems.setAmount(amount);
            totalItems.setActualPickWeightServer(actualPickWeight);

        }
        return totalItems;
    }

    @Override
    public ConsignOrderItems getChangeSellerTotalItems(List<ConsignOrderItemsChange> orderItems) {
        // 卖家合计
        ConsignOrderItems totalItems = new ConsignOrderItems();
        if (orderItems != null && orderItems.size() > 0) {
            int count = 0;
            BigDecimal weight = BigDecimal.ZERO, costprice = BigDecimal.ZERO, dealprice = BigDecimal.ZERO, amount = BigDecimal.ZERO;
            for (ConsignOrderItemsChange item : orderItems) {
                count += item.getQuantity();
                if (item.getWeight() != null) {
                    weight = weight.add(item.getWeight());
                }
                if (item.getCostPrice() != null) {
                    costprice = costprice.add(item.getCostPrice());
                }
                if (item.getDealPrice() != null) {
                    dealprice = dealprice.add(item.getDealPrice());
                }
                if (item.getSaleAmount() != null) {
                    amount = amount.add(item.getSaleAmount());
                }

            }
            totalItems.setQuantity(count);
            totalItems.setWeight(weight);
            totalItems.setCostPrice(costprice);
            totalItems.setDealPrice(dealprice);
            totalItems.setAmount(amount);
        }
        return totalItems;
    }
    //通过变更订单ID查询明细
    @Override
    public  List<ConsignOrderItems> selectByChangeOrderId(Integer changeOrderId){
        return consignOrderItemsChangeDao.selectByChangeOrderId(changeOrderId);
    }

    /**
     * 修改合同变更表的状态，并且添加对应的变更记录
     */
    @Override
    public void updateChangeStatus(ConsignOrderChange consignOrderChange, String alterStatus, String note, User user) {
        String originStatus = consignOrderChange.getStatus();
        // 修改变更表状态
        consignOrderChange.setStatus(alterStatus);
        consignOrderChange.setLastUpdated(new Date());
        consignOrderChange.setLastUpdatedBy(user.getLoginId());
        consignOrderChange.setModificationNumber(consignOrderChange.getModificationNumber() + 1);
        Integer flag = consignOrderChangeDao.updateByPrimaryKeySelective(consignOrderChange);
        if (flag == 0) {
            throw new BusinessException(Constant.EXCEPTIONCODE_UNKONWN, "修改订单表变更状态失败");
        }

        UpdateChangeOrderDto updateDto = new UpdateChangeOrderDto();
        updateDto.setChangeOrderId(consignOrderChange.getId());
        updateDto.setAlterStatus(alterStatus);
        updateDto.setOldStatus(originStatus);
        updateDto.setLastUpdatedBy(user.getLoginId());

        // 修改变更详情表状态
        flag = consignOrderItemsChangeDao.updateStatusByChangeOrderId(updateDto);
        if (flag == 0) {
            throw new BusinessException(Constant.EXCEPTIONCODE_UNKONWN, "修改变更详情表状态失败");
        }

        // 修改变更明细记录表状态
        flag = consignOrderItemsChangedrecordDao.updateStatusByChangeOrderId(updateDto);
        if (flag == 0) {
            throw new BusinessException(Constant.EXCEPTIONCODE_UNKONWN, "修改变更详情记录表状态失败");
        }
        // 修改变更明细记录表待删除状态的记录,如果变更成功就删除，失败修改成对应状态，其他状态就不变，保持为待删除审核状态
        updateDto.setOldStatus(ConsignOrderAlterStatus.PENDING_DEL_APPROVAL.getCode());
        if(ConsignOrderAlterStatus.CHANGED_SUCCESS1.getCode().equals(alterStatus)
                ||ConsignOrderAlterStatus.CHANGED_SUCCESS2.getCode().equals(alterStatus)
                ||ConsignOrderAlterStatus.CHANGED_SUCCESS3.getCode().equals(alterStatus)){
            updateDto.setAlterStatus(ConsignOrderAlterStatus.DEL.getCode());
            consignOrderItemsChangeDao.updateStatusByChangeOrderId(updateDto);
            consignOrderItemsChangedrecordDao.updateStatusByChangeOrderId(updateDto);
        }
        else if(ConsignOrderAlterStatus.DISAPPROVE.getCode().equals(alterStatus)
                ||ConsignOrderAlterStatus.CLOSED.getCode().equals(alterStatus)){
            consignOrderItemsChangeDao.updateStatusByChangeOrderId(updateDto);
            consignOrderItemsChangedrecordDao.updateStatusByChangeOrderId(updateDto);
        }

        // 添加变更操作记录
        saveConsignOrderItemsOperatedRecord(consignOrderChange.getOrderId(), consignOrderChange.getId(), alterStatus, note, user);
    }

    /**
     * 合同变更成功以后把对应的数据反写到订单表
     */
    @Override
    public void updateOrderByChange(ConsignOrder consignOrder,ConsignOrderChange orderChange, String alterStatus, List<ConsignOrderItemsChange> orderItemsChanges, User user) {
        if (orderChange == null) {
            throw new BusinessException(Constant.EXCEPTIONCODE_UNKONWN, "订单不存在");
        }
        if (orderItemsChanges == null) {
            // 查询变更详情记录
            orderItemsChanges = consignOrderItemsChangeDao.queryOrderItemsByChangeOrderId(orderChange.getId());
        }

        // 如果变更成功，更新订单详情
        if (ConsignOrderAlterStatus.CHANGED_SUCCESS1.getCode().equals(alterStatus)
                || ConsignOrderAlterStatus.CHANGED_SUCCESS2.getCode().equals(alterStatus)
                || ConsignOrderAlterStatus.CHANGED_SUCCESS3.getCode().equals(alterStatus)) {

            Integer changeTotalQuantity = orderItemsChanges.stream().mapToInt(a -> a.getQuantity()).sum();
            boolean isQuantityIncrease = changeTotalQuantity.compareTo(consignOrder.getTotalQuantity()) > 0 ? true : false ; //件数是否变大
            // 当件数变大，且原订单待提货状态是已录入时，原订单状态变成 已关联，待提货状态变成 部分录入，待放货状态变成 未全打印
            if(isQuantityIncrease && consignOrder.getPickupStatus().equals(ConsignOrderPickupStatus.ALL_ENTRY.getCode())){
                consignOrder.setStatus(ConsignOrderStatus.RELATED.getCode());
                consignOrder.setPickupStatus(ConsignOrderPickupStatus.PART_ENTRY.getCode());
                consignOrder.setFillupStatus(ConsignOrderFillUpStatus.NO_PRINT_ALL.getCode());
            }

            // 当件数变小，且变更后的件数等于原订单已录入提货件数时，则原订单的待提货状态 变成 全部录入
            List<PickupItems> pickupList = pickupItemsDao.getQuantityByOrderId(consignOrder.getId());
            int takeTotalQuantity = pickupList.stream().mapToInt(a -> a.getPickedQuantity()-a.getLeftQuantity()).sum();//已录入的提货数量
            if(changeTotalQuantity.compareTo(consignOrder.getTotalQuantity()) < 0 && changeTotalQuantity.compareTo(takeTotalQuantity) == 0){
                consignOrder.setPickupStatus(ConsignOrderPickupStatus.ALL_ENTRY.getCode());
            }

            // 并将修改内容保存到原订单资源
            consignOrder.setChangeOrderId(orderChange.getId());
            consignOrder.setDeliveryType(orderChange.getDeliveryType());
            consignOrder.setDeliveryEndDate(orderChange.getDeliveryEndDate());
            consignOrder.setFeeTaker(orderChange.getFeeTaker());
            consignOrder.setShipFee(orderChange.getShipFee());
            consignOrder.setOutboundTaker(orderChange.getOutboundTaker());
            consignOrder.setOutboundFee(orderChange.getOutboundFee());
            consignOrder.setContractAddress(orderChange.getContractAddress());
            consignOrder.setTransArea(orderChange.getTransArea());
            consignOrder.setTotalAmount(new BigDecimal(orderItemsChanges.stream().mapToDouble(a -> a.getSaleAmount().doubleValue()).sum()));
            consignOrder.setTotalQuantity(orderItemsChanges.stream().mapToInt(a -> a.getQuantity()).sum());
            consignOrder.setTotalWeight(new BigDecimal(orderItemsChanges.stream().mapToDouble(a -> a.getWeight().doubleValue()).sum()));
            //将修改的关联金额、二次结算余额保存到原订单
            if(orderChange.getChangeRelateAmount() != null)
                consignOrder.setTotalContractReletedAmount(consignOrder.getTotalContractReletedAmount().add(orderChange.getChangeRelateAmount()));
            if(orderChange.getSecondBalanceTakeout() != null)
                consignOrder.setSecondBalanceTakeout(consignOrder.getSecondBalanceTakeout().add(orderChange.getSecondBalanceTakeout()));
            // 更新订单详情表
            updateOriginalOrderItems(orderItemsChanges, user);
            //申请付款或确认付款成功变更成功,且经过变更关联的订单，买家才需要解锁和支付合同货款 add by wangxianjun
            if(ConsignOrderAlterStatus.CHANGED_SUCCESS3.getCode().equals(alterStatus) && orderChange.getChangeRelateAmount()!=null){
                accountFundService.updateAccountFund(consignOrder.getDepartmentId(), AssociationType.ORDER_CODE, consignOrder.getCode(),
                        AccountTransApplyType.BALANCES_UNLOCK, orderChange.getChangeRelateAmount(),
                        BigDecimal.ZERO.subtract(orderChange.getChangeRelateAmount()), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, PayType.BALANCE,
                        user.getId(), user.getName(), new Date()); //解锁账户
                accountFundService.updateAccountFund(consignOrder.getDepartmentId(), AssociationType.ORDER_CODE, consignOrder.getCode(),
                        AccountTransApplyType.PAY_THE_CONTRACT_PAYMENT, BigDecimal.ZERO.subtract(orderChange.getChangeRelateAmount()),
                        BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, PayType.BALANCE, user.getId(), user.getName(), new Date()); //支付合同货款

            }
        }

        //更新原订单状态
        consignOrder.setAlterStatus(alterStatus);
        consignOrder.setLastUpdated(new Date());
        consignOrder.setLastUpdatedBy(user.getName());
        consignOrder.setModificationNumber(consignOrder.getModificationNumber() + 1);
        if(consignOrderService.updateByPrimaryKeySelective(consignOrder) == 0){
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "原订单状态更新失败！");
        }
    }

    /**
     * 根据订单Id查询变更记录
     * @param query 查询参数
     * @return
     */
    @Override
    public ConsignOrderChange selectByConsignOrderChange(ConsignOrderChange query){
        return consignOrderChangeDao.selectByConsignOrderChange(query);
    }

    /**
     * 重新生成合同号
     * @param order 订单
     * @param operator 操作人
     * @return
     */
    private String generateConsignOrderContract(ConsignOrder order,Integer changeOrderId, User operator) {
        String buyerCode = order.getContractCode();
        int number = 0;
        String maxCode = consignOrderChangeDao.queryLastContract(order.getId());//通过订单id查询最新一笔变更合同号
        if("0".equals(maxCode)){
            //没有变更合同号，
            buyerCode = buyerCode+ "001";
        }else{
            number = Integer.valueOf(maxCode)+1;
            buyerCode =  buyerCode + String.format("%03d", number);
        }
        /*String lastCode = buyerCode.substring(buyerCode.length() - 3, buyerCode.length());
        lastCode = String.format("%0" + 3 + "d", Integer.parseInt(lastCode) + 1);
        buyerCode = buyerCode.substring(0, buyerCode.length() - 3) + lastCode;*/

        //生成卖家合同号
        List<ConsignOrderSellerInfoDto> sellerInfoList = consignOrderItemsDao.getSellerInfo(order.getId());
        ConsignOrderSellerInfoDto item = sellerInfoList.get(0);
        String code = "";//卖家合同号
        /*BillSequence busiBillSequence = new BillSequence();
        busiBillSequence.setOrgId(operator.getOrgId());
        busiBillSequence.setAccountId(item.getSellerId());
        busiBillSequence.setSeqType(BillType.DH.toString());
        String code = billSequenceService.generateSequence(busiBillSequence);*/
        ConsignOrderContract sellerContract = consignOrderContractDao.queryByOrderIdAndCustomerId(order.getId(),item.getSellerId());
        maxCode = consignOrderContractDao.queryMaxContract(order.getId());
            if("0".equals(maxCode)){
            //没有变更合同号，
            code = sellerContract.getContractCodeAuto()+ "001";
        }else{
            number = Integer.valueOf(maxCode)+1;
            code =  sellerContract.getContractCodeAuto() + String.format("%03d", number);
        }

        ConsignOrderContract consignOrderContract = new ConsignOrderContract();
        consignOrderContract.setCreated(new Date());
        consignOrderContract.setCreatedBy(operator.getName());
        consignOrderContract.setModificationNumber(0);
        consignOrderContract.setContractCodeAuto(code);
        consignOrderContract.setCustomerId(item.getSellerId());
        consignOrderContract.setCustomerDepartmentId(item.getSellerDepartmentId());
        consignOrderContract.setCustomerDepartmentName(item.getSellerDepartmentName());
        consignOrderContract.setCustomerAddr(item.getAddress());
        consignOrderContract.setCustomerTel(item.getMobile());
        consignOrderContract.setBankNameMain(item.getBankNameMain());
        consignOrderContract.setBankNameBranch(item.getBankNameBranch());
        consignOrderContract.setConsignOrderId(order.getId());
        consignOrderContract.setChangeOrderId(changeOrderId);
        consignOrderContract.setCustomerName(item.getCompanyName());
        consignOrderContract.setLastUpdated(new Date());
        consignOrderContract.setLastUpdatedBy(operator.getName());
        consignOrderContract.setBankAccountCode(item.getBankCode());
        consignOrderContractDao.insertSelective(consignOrderContract);

        return buyerCode;
    }
    //通过订单ID查询合同变更后合同金额
    @Override
    public  BigDecimal countChangeContractAmountById(Long changeOrderId){
        return consignOrderItemsChangeDao.countChangeContractAmountById(changeOrderId);
    }
    //通过订单ID查询合同变更后使用的银票号
    @Override
    public  List<String> selectChangeAcceptDraftCodeById(Long orderId){
        return consignOrderItemsChangeDao.selectChangeAcceptDraftCodeById(orderId);
    }

    @Override
    public int querySuccessCountByOrderId(Long id){
        return consignOrderChangeDao.querySuccessCountByOrderId(id);
    }

    @Override
    public List<ConsignOrderItemsChange> selectOriginalItemsByOrderId(Long orderId){
        return consignOrderItemsChangeDao.selectOriginalItemsByOrderId(orderId);
    }

    @Override
    public List<ConsignOrderItemsChange> queryOrderItemsExceptStatusByChangeOrderId(Integer changeOrderId, String status){
        return consignOrderItemsChangeDao.queryOrderItemsExceptStatusByChangeOrderId(changeOrderId, status);
    }

    @Override
    public List<ConsignOrderChange> selectByQueryDto(QueryChangeOrderDto query){
        return consignOrderChangeDao.selectByQueryDto(query);
    }

    @Override
    public List<ConsignOrderItemsChange> selectOrderItemsChangeByQueryDto(QueryChangeOrderDto query){
        return consignOrderItemsChangeDao.selectOrderItemsChangeByQueryDto(query);
    }
}

