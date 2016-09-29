package com.prcsteel.platform.order.service.order.impl;

import com.prcsteel.framework.nido.engine.Nido;
import com.prcsteel.platform.account.model.dto.DepartmentDto;
import com.prcsteel.platform.account.model.dto.SaveAccountDto;
import com.prcsteel.platform.account.model.dto.SaveContactDto;
import com.prcsteel.platform.account.model.enums.AccountBusinessType;
import com.prcsteel.platform.account.model.enums.AccountTag;
import com.prcsteel.platform.account.model.enums.AccountTransApplyType;
import com.prcsteel.platform.account.model.enums.AccountType;
import com.prcsteel.platform.account.model.enums.CardInfoStatus;
import com.prcsteel.platform.account.model.model.Account;
import com.prcsteel.platform.account.model.model.AccountBank;
import com.prcsteel.platform.account.model.model.AccountExt;
import com.prcsteel.platform.account.model.model.Contact;
import com.prcsteel.platform.account.model.model.CustGroupingInfor;
import com.prcsteel.platform.account.persist.dao.AccountBankDao;
import com.prcsteel.platform.account.persist.dao.AccountContactDao;
import com.prcsteel.platform.account.persist.dao.AccountDao;
import com.prcsteel.platform.account.persist.dao.AccountExtDao;
import com.prcsteel.platform.account.persist.dao.ContactDao;
import com.prcsteel.platform.account.persist.dao.CustGroupingInforDao;
import com.prcsteel.platform.account.service.AccountContactService;
import com.prcsteel.platform.account.service.AccountFundService;
import com.prcsteel.platform.account.service.AccountService;
import com.prcsteel.platform.account.service.ContactService;
import com.prcsteel.platform.acl.model.enums.RewardStatus;
import com.prcsteel.platform.acl.model.enums.RewardType;
import com.prcsteel.platform.acl.model.enums.SysSettingType;
import com.prcsteel.platform.acl.model.model.Organization;
import com.prcsteel.platform.acl.model.model.Reward;
import com.prcsteel.platform.acl.model.model.SysSetting;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.acl.persist.dao.OrganizationDao;
import com.prcsteel.platform.acl.persist.dao.RewardDao;
import com.prcsteel.platform.acl.persist.dao.SysSettingDao;
import com.prcsteel.platform.acl.persist.dao.UserDao;
import com.prcsteel.platform.acl.service.SysSettingService;
import com.prcsteel.platform.api.AccountDepartmentService;
import com.prcsteel.platform.api.RestPurchaseOrderService;
import com.prcsteel.platform.api.RestQuotationService;
import com.prcsteel.platform.api.RestAccountService;
import com.prcsteel.platform.api.RestSmartmatchService;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.constants.NidoTaskConstant;
import com.prcsteel.platform.common.dto.ResultDto;
import com.prcsteel.platform.common.enums.ConsignOrderCallBackStatus;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.common.service.FileService;
import com.prcsteel.platform.common.utils.CbmsNumberUtil;
import com.prcsteel.platform.common.utils.FileUtil;
import com.prcsteel.platform.common.utils.Tools;
import com.prcsteel.platform.common.vo.Result;
import com.prcsteel.platform.core.model.dto.CategoryGroupDto;
import com.prcsteel.platform.core.model.enums.BillType;
import com.prcsteel.platform.core.model.model.BillSequence;
import com.prcsteel.platform.core.persist.dao.CategoryGroupDao;
import com.prcsteel.platform.core.persist.dao.ProvinceDao;
import com.prcsteel.platform.core.service.BillSequenceService;
import com.prcsteel.platform.order.model.AppOrder;
import com.prcsteel.platform.order.model.AppOrderItems;
import com.prcsteel.platform.order.model.CountOrder;
import com.prcsteel.platform.order.model.EcOrder;
import com.prcsteel.platform.order.model.EcOrderItems;
import com.prcsteel.platform.order.model.dto.AccountOrderDto;
import com.prcsteel.platform.order.model.dto.ConsignOrderDto;
import com.prcsteel.platform.order.model.dto.ConsignOrderSellerInfoDto;
import com.prcsteel.platform.order.model.dto.ConsignOrderStatusDto;
import com.prcsteel.platform.order.model.dto.ConsignOrderUpdateDto;
import com.prcsteel.platform.order.model.dto.InvoiceOutApplyStatusDto;
import com.prcsteel.platform.order.model.dto.OrderContactDto;
import com.prcsteel.platform.order.model.dto.OrderItemDetailDto;
import com.prcsteel.platform.order.model.dto.OrderPayMentDto;
import com.prcsteel.platform.order.model.dto.OrderTradeCertificateDto;
import com.prcsteel.platform.order.model.dto.PoolInAndOutModifier;
import com.prcsteel.platform.order.model.dto.SecondaryDto;
import com.prcsteel.platform.order.model.enums.*;
import com.prcsteel.platform.order.model.model.*;
import com.prcsteel.platform.order.model.nido.AddRebateInMarketContext;
import com.prcsteel.platform.order.model.nido.NoteMessageContext;
import com.prcsteel.platform.order.model.query.OrderEcAppQuery;
import com.prcsteel.platform.order.model.query.OrderPayMentQuery;
import com.prcsteel.platform.order.model.query.OrderTradeCertificateQuery;
import com.prcsteel.platform.order.model.query.ReportRewardQuery;
import com.prcsteel.platform.order.persist.dao.AllowanceDao;
import com.prcsteel.platform.order.persist.dao.ConsignOrderAttachmentDao;
import com.prcsteel.platform.order.persist.dao.ConsignOrderContractDao;
import com.prcsteel.platform.order.persist.dao.ConsignOrderDao;
import com.prcsteel.platform.order.persist.dao.ConsignOrderItemsDao;
import com.prcsteel.platform.order.persist.dao.ConsignOrderProcessDao;
import com.prcsteel.platform.order.persist.dao.ConsignProcessDao;
import com.prcsteel.platform.order.persist.dao.InvoiceInDetailOrderitemDao;
import com.prcsteel.platform.order.persist.dao.OrderStatusDao;
import com.prcsteel.platform.order.persist.dao.PayRequestDao;
import com.prcsteel.platform.order.persist.dao.PayRequestItemsDao;
import com.prcsteel.platform.order.persist.dao.PoolInDao;
import com.prcsteel.platform.order.persist.dao.PoolInDetailDao;
import com.prcsteel.platform.order.persist.dao.PoolOutDao;
import com.prcsteel.platform.order.persist.dao.PoolOutDetailDao;
import com.prcsteel.platform.order.persist.dao.RebateDao;
import com.prcsteel.platform.order.persist.dao.ReportNewUserRewardDao;
import com.prcsteel.platform.order.persist.dao.ReportRebateRecordDao;
import com.prcsteel.platform.order.persist.dao.ReportRewardRecordDao;
import com.prcsteel.platform.order.service.AppPushService;
import com.prcsteel.platform.order.service.FinanceService;
import com.prcsteel.platform.order.service.OrderCacheService;
import com.prcsteel.platform.order.service.invoice.PoolInService;
import com.prcsteel.platform.order.service.order.ConsignOrderItemsService;
import com.prcsteel.platform.order.service.order.ConsignOrderService;
import com.prcsteel.platform.order.service.order.OrderStatusService;
import com.prcsteel.platform.order.service.order.changecontract.ConsignOrderChangeService;
import com.prcsteel.platform.order.service.payment.PayRequestService;
import com.prcsteel.platform.order.service.point.PointService;
import com.prcsteel.platform.order.service.rebate.RebateService;
import com.prcsteel.platform.order.service.report.ReportFinanceService;
import org.apache.commons.lang.StringUtils;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by dengxiyan on 2015/7/18.
 */

@Service("consignOrderService")
@Transactional
public class ConsignOrderServiceImpl implements ConsignOrderService {
    private static Logger logger = LogManager.getLogger(ConsignOrderServiceImpl.class);
    private static final Integer codeLengthForSMS = 6;    //默认发布短信时截取订单code的后6位
    @Value("${ivFinanceService}")
    private String ivFinanceServiceAddress;  // 接口服务地址
    @Value("${ivFinanceServiceKEY}")
    private String ivFinanceServiceKEY; //接口key

    public static final String ATTACHMENTSAVEPATH = "upload" + File.separator + "img" + File.separator + "order" + File.separator;
    public static final long NUM = 3L;
    
    public static final String PENDING_PAYMENT = "待付款";
    public static final String PENDING_SETTLEMENT = "待结算";
    public static final String BEEN_COMPLETED = "已完成";
    public static final String COLSED = "已关闭";
    
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
    private RebateService rebateService;
	@Resource
    ReportFinanceService reportFinanceService;
    @Resource
    AppPushService appPushService;
    @Resource
    private OrderCacheService orderCacheService;
    @Resource
    private ReportNewUserRewardDao reportNewUserRewardDao;
    @Resource
    private ContactDao contactDao;
    @Resource
    private AccountDepartmentService accountDepartmentService;
    @Resource
    private AccountFundService accountFundService;
    @Resource
    private AccountExtDao accountExtDao;
    @Resource
    private ContactService contactService;
    @Resource
    private AccountContactDao accountcontactDao;
    @Resource
    private AccountContactService accountContactService;
    @Resource
    private RestSmartmatchService restSmartmatchService;
    @Resource
    private RestQuotationService restQuotationService;
    @Resource
    private RestAccountService restAccountService;
    @Resource
    private ConsignOrderChangeService consignOrderChangeService;

    /*
	@Resource
    PurchaseOrderDao purchaseOrderDao;
    */
    
    //add by caosulin 增加询价单的rest服务接口
    @Resource
    private RestPurchaseOrderService restPurchaseOrderService;

	@Resource
	private ReportFinanceService reportFinaceService;


    @Resource
    private  CustGroupingInforDao custGroupingInforDao;

    @Resource
    SysSettingDao sysSettingDao;

	
    @Override
    public int deleteByPrimaryKey(Long id) {
        return consignOrderDao.deleteByPrimaryKey(id);
    }

    @Override
    public int insertSelective(ConsignOrder record) {
        return consignOrderDao.insertSelective(record);
    }

    @Override
    public ConsignOrder selectByPrimaryKey(Long id) {
        return consignOrderDao.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(ConsignOrder record) {
        return consignOrderDao.updateByPrimaryKeySelective(record);
    }

    @Override
    public boolean checkIsPayByAcceptDraft(Long orderId) {
        return consignOrderDao.checkIsPayedByAcceptDraft(orderId);
    }

    @Override
    public int updateByPrimaryKey(ConsignOrder record) {
        return consignOrderDao.updateByPrimaryKey(record);
    }

    /**
     * 根据条件分页查询当前登录人的订单列表
     * 默认查询当前登录人的订单列表
     *
     * @param paramMap：map封装参数说明 key:value
     *                           userId： 当前登录人id
     *                           dto   ： 封装的订单dto:ConsignOrderDto
     *                           start：  开始记录（分页参数）
     *                           length： 每页记录数（分页参数）
     *                           array：  订单状态数组，用于in条件
     * @return
     * @author dengxiyan
     */
    @Override
    public List<ConsignOrderDto> selectByConditions(Map<String, Object> paramMap) {
        ConsignOrderDto dto = (ConsignOrderDto) paramMap.get("dto");

        if (dto != null) {
            if (StringUtils.isNotEmpty(dto.getCode())) {
                dto.setCode("%" + dto.getCode().trim() + "%");
            }
            if (StringUtils.isNotEmpty(dto.getAccountName())) {
                dto.setAccountName("%" + dto.getAccountName().trim() + "%");
            }
            //Green.Ge 10/20 for issue 3855 以下处理排序字段，防止前台传入非法字符导致sql执行出错
            if(StringUtils.isNotBlank(dto.getOrderBy())){
    			String[] allowedOrderByFields ={"code","payApprovedTime","account_name","owner_name","seller_name"
    					,"total_quantity","total_weight","pickup_total_weight","total_amount","pickup_total_amount"};
    			if(!Arrays.asList(allowedOrderByFields).contains(dto.getOrderBy())){//排序字段 不在允许查询列表内
    				dto.setOrderBy("");//取消此参数
    				dto.setOrder("");
    			}else if(StringUtils.isNotBlank(dto.getOrder())){//排序方式 正排，倒排
    				if(!"asc".equals(dto.getOrder())&&!"desc".equals(dto.getOrder())){//参数非法
    					dto.setOrder("");//取消此参数
    				}
    			}
            }
        }
        return consignOrderDao.selectByConditions(paramMap);
    }

    /**
     * 根据条件统计订单总数
     *
     * @param paramMap：map封装参数说明 key:value
     *                           userId： 当前登录人id
     *                           dto   ： 封装的订单dto:ConsignOrderDto
     *                           start：  开始记录（分页参数）
     *                           length： 每页记录数（分页参数）
     *                           array：  订单状态数组，用于in条件
     * @return
     * @author dengxiyan
     */
    @Override
    public int totalOrderByConditions(Map<String, Object> paramMap) {
        return consignOrderDao.totalOrderByConditions(paramMap);
    }


    /**
     * 新开交易单
     *
     * @param user         操作人
     * @param consignOrder 订单model
     * @Param list         订单资源model集合
     */
    @Override
    public ResultDto add(User user, ConsignOrder consignOrder, List<ConsignOrderItems> list, Long purchaseOrderId, String quotationIds) {
        ResultDto result = new ResultDto();

        // 获取买家账号信息
        Account buyer = accountDao.selectAccountByName(consignOrder.getAccountName());
        if (buyer != null && buyer.getId() > 0) {
            consignOrder.setAccountId(buyer.getId());

            // 获取部门信息
            if (consignOrder.getDepartmentId() == null || consignOrder.getDepartmentId() == 0l) {
                List<DepartmentDto> departmentList = accountService.queryDeptByCompanyId(buyer.getId());
                if (departmentList == null || departmentList.size() == 0) {
                    throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "没有查询到部门");
                }
                DepartmentDto dept = departmentList.get(0);
                consignOrder.setDepartmentId(dept.getId());
                consignOrder.setDepartmentName(dept.getName());
            }
        }

        // 验证数据
        checkAdd(consignOrder, list);

        Date date = new Date();
        Integer modificationNumber = 0;
        consignOrder.setCreated(date);
        consignOrder.setCreatedBy(user.getName());
        consignOrder.setLastUpdated(date);
        consignOrder.setLastUpdatedBy(user.getName());
        consignOrder.setStatus(ConsignOrderStatus.NEW.getCode());
        consignOrder.setOperatorId(user.getId());
        consignOrder.setOperatorName(user.getName());
        int groupingCount = restAccountService.getGroupCountByAccountId(consignOrder.getAccountId());
        consignOrder.setFinanceOrder(groupingCount > 0 ? 1 : 0);


//        consignOrder.setCode(getOrderCode(user.getOrgId(), user.getId(), consignOrder.getAccountId()));
        // 插入订单表
        int addResult = consignOrderDao.insertSelective(consignOrder);
        if (addResult > 0) {
            // 插入订单资源表
            for (ConsignOrderItems item : list) {
                item.setOrderId(consignOrder.getId());
                item.setCreated(date);
                item.setCreatedBy(user.getName());
                item.setModificationNumber(modificationNumber);
                item.setLastUpdated(date);
                item.setLastUpdatedBy(user.getName());
                Integer addItemResult = consignOrderItemsDao.insertSelective(item);
                if (addItemResult == 0) {
                    throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "创建订单失败，资源数据错误。");
                }
            }

            // 插入订单审核状态表
            User owner = userDao.queryById(consignOrder.getOwnerId());
            Boolean bResult = insertOrderAuditTrail(consignOrder, owner, modificationNumber);
            if (!bResult) {
                throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "创建订单失败，插入订单审核状态表失败。");
            }

            // 更新买家最后下单时间
            if (consignOrder.getAccountId() != null && consignOrder.getAccountId() > 0 && buyer != null ) {
                Account account = new Account();
                account.setLastBillTime(date);
                account.setId(consignOrder.getAccountId());
                account.setIsAcceptDraftCharged(buyer.getIsAcceptDraftCharged() == null ? 0 : buyer.getIsAcceptDraftCharged());
                Integer uLastResult = accountDao.updateByPrimaryKeySelective(account);
                if (uLastResult == 0) {
                    throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "创建订单失败，更新买家最后下单时间失败。");
                }
            }

            if (purchaseOrderId != null) {
                // 更新采购单状态 
            	// modify by caosulin更新询价单的状态为已开单
            	Result updateResult = restPurchaseOrderService.updateStatusById(purchaseOrderId, "BILLED", user.getLoginId());
            	if (!updateResult.isSuccess()) {
            		logger.error( "--------------------------------"+String.format("询价单%s状态修改失败",purchaseOrderId));
                    //throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, String.format("询价单%s状态修改失败",purchaseOrderId));
                }
            	//purchaseOrderDao.updateStatusById(purchaseOrderId, PurchaseOrderStatus.BILLED.getCode(), user.getLoginId());
            }

            //更新订单号
            String code = getOrderCode(user.getOrgId(), user.getId(), consignOrder.getAccountId(), consignOrder.getId());
            consignOrderDao.updateCodeById(consignOrder.getId(), code);

            // 发送短信给服务中心总经理
            Organization organization = organizationDao.queryById(user.getOrgId());
            User manageUser = userDao.queryById(organization.getCharger());
            if (manageUser != null) {
                String endCode = code.substring(code.length() - codeLengthForSMS, code.length());
                SysSetting template=sysSettingService.queryBySettingType(SysSettingType.SmsTemplateAddOrder.getCode());
                if (template == null) {
                    throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,"新开单通知审核人短信模板缺失");
                }
                String content =template.getSettingValue();
                content = content.replace("#tradername#", consignOrder.getOwnerName());
                content = content.replace("#companyname#", consignOrder.getAccountName());
                content = content.replace("#endcode#", endCode);
                Nido.start(NidoTaskConstant.SEND_NOTE_MESSAGE, new NoteMessageContext(manageUser.getTel(), content));
            } else {
                logger.fatal(String.format("不存在服务中心总经理，交易员Id：%s ，名字：%s", consignOrder.getOwnerId(), consignOrder.getOwnerName()));
            }

            String[] pushInfo = orderCacheService.getPushInfoByUserLoginId(manageUser.getLoginId());
            if(pushInfo!=null){
            	String content = String.format("%s已提交寄售交易单审核申请，请审核。", consignOrder.getOwnerName());
                appPushService.sendPushNoticfication(pushInfo[0],pushInfo[1], AppNoticeType.Approve.getName(),content);
                // ADD BY LIXING 订单状态变动时推送消息至钢为掌柜app 待付款
//                appPushService.sendPushNoticfication(pushInfo[0],pushInfo[1], PENDING_PAYMENT, PENDING_PAYMENT);
            }
        } else {
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "插入订单数据失败。");
        }
        result.setSuccess(Boolean.TRUE);

        //chengui 若是报价单开单，且该账户下还有未开单的报价单，此次开单成功后则跳回报价单页面，否则按cbms流程跳到交易单待审核页面
        if(StringUtils.isNotEmpty(quotationIds)){
            try{
                Result restResult = restQuotationService.save(quotationIds, "1", user.getLoginId());
                Integer count = (Integer) restResult.getData();
                if(count > 0){
                    result.setMessage("goToQuotationView"); //用于判断跳回报价单页面，还是待审核页面
                }
            }catch (Exception e){
                throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "更新报价单详情的状态失败。");
            }

        }

        return result;
    }

    private void checkAdd(ConsignOrder consignOrder, List<ConsignOrderItems> list){
        // 销售金额
        //Double totalSaleAmount = list.stream().mapToDouble(a -> a.getDealPrice().multiply(a.getWeight()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()).sum();
        // 采购金额
//        Double totalPurchaseAmount = list.stream().mapToDouble(a -> a.getCostPrice().multiply(a.getWeight()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()).sum();
//        if(totalPurchaseAmount >= totalSaleAmount){
//            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "该交易单无利润，请确认后再提交！");
//        }

        //不能有多个卖家多个部门
        if(list.stream().collect(Collectors.groupingBy(ConsignOrderItems::getDepartmentId, Collectors.toList())).size() > 1){
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "一笔交易单只能添加一个卖家一个部门的资源。");
        }
        //已锁定的买家或卖家，部门，联系人不能提交审核
        if (hasLockedAccount(consignOrder.getAccountId())) {
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, consignOrder.getAccountName() + " 已被锁定！");
        }
        if (hasLockedAccount(consignOrder.getDepartmentId())) {
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, consignOrder.getAccountName() + " " + consignOrder.getDepartmentName() + " 已被锁定！");
        }
        list.forEach(item -> {
            if (hasLockedAccount(item.getSellerId())) {
                throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, item.getSellerName() + " 已被锁定！");
            }
            if (hasLockedAccount(item.getDepartmentId())) {
                throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, item.getSellerName() + " " + item.getDepartmentName() + " 已被锁定！");
            }
        });

        if (selectByCode(consignOrder.getCode()) != null) {
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "不能创建重复的订单号。");
        }

        Contact contact = contactDao.queryByTel(consignOrder.getContactTel());
        if (contact != null) {
            // 联系人名字与输入的买家联系人相同，
        	// 如果getOrigin 不为空，则说明，是从找货项目过来的，如果是从找货过来的，则不需要检测。
            if (consignOrder.getOrigin()==null) {
            	if(contact.getName().equals(consignOrder.getContactName())){
	                consignOrder.setContactId(new Long(contact.getId()));
	            } else {
	                throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "联系人姓名与联系人手机号码不匹配。");
	            }
            }
            
            //已锁定的买家联系人 不能提交审核
	        accountContactService.isLockForAccountContact(contact, consignOrder.getDepartmentId(), consignOrder.getOwnerId(),
	            consignOrder.getAccountName(), consignOrder.getDepartmentName());
            
        }

        Account seller;
        AccountExt accountExt;
        Contact sellerContact = new Contact();
        for (ConsignOrderItems item : list) {
            seller = accountDao.selectAccountByName(item.getSellerName());
            if (seller != null) {
                item.setSellerId(seller.getId());
                accountExt = accountExtDao.selectByAccountId(seller.getId());
                if (accountExt == null || !CardInfoStatus.Approved.getCode().equals(accountExt.getCardInfoStatus())) {
                    throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "卖家公司【" + item.getSellerName() + "】审核暂未通过审核，不能开单！");
                }
                //已锁定的卖家联系人 不能提交审核
                sellerContact.setId(item.getContactId().intValue());
                sellerContact.setName(item.getContactName());
                accountContactService.isLockForAccountContact(sellerContact, item.getDepartmentId(), null, item.getSellerName(), item.getDepartmentName());
            } else {
                throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "卖家公司名称输入错误，不存在对应的卖家！");
            }
        }
    }

    /**
     * 生成订单号code
     *
     * @param orgId     用户组织ID
     * @param userId    用户ID
     * @param accountId 客户ID
     * @return 订单号
     */
    private String getOrderCode(Long orgId, Long userId, Long accountId, Long orderId) {
        BillSequence billSequence = new BillSequence();
        billSequence.setOrgId(orgId);
        billSequence.setUserId(userId);
        billSequence.setAccountId(accountId);
        billSequence.setSeqType(BillType.DD.toString());
        return billSequenceService.generateOrderCode(billSequence, orderId);
    }

    /**
     * 插入订单审核状态表
     *
     * @param consignOrder       订单数据
     * @param owner              交易员
     * @param modificationNumber 修改次数
     * @return true：成功，false：失败
     */
    private Boolean insertOrderAuditTrail(ConsignOrder consignOrder, User owner, Integer modificationNumber) {
        OrderAuditTrail orderAuditTrail = new OrderAuditTrail();
        orderAuditTrail.setOrderId(consignOrder.getId());
        orderAuditTrail.setSetToStatus(consignOrder.getStatus());
        orderAuditTrail.setStatusType(OrderStatusType.MAIN.toString());
        orderAuditTrail.setOperatorId(owner.getId());
        orderAuditTrail.setOperatorName(owner.getName());
        orderAuditTrail.setCreated(new Date());
        orderAuditTrail.setCreatedBy(consignOrder.getCreatedBy());
        orderAuditTrail.setLastUpdated(new Date());
        orderAuditTrail.setLastUpdatedBy(consignOrder.getLastUpdatedBy());
        orderAuditTrail.setModificationNumber(modificationNumber);
        return orderStatusDao.insertOrderAuditTrail(orderAuditTrail) > 0;
    }

    /**
     * 修改订单信息 （暂时弃用）
     *
     * @param consignOrder 订单信息
     * @param list         资源集合
     * @return
     */
    @Override
    public boolean modify(ConsignOrder consignOrder, List<ConsignOrderItems> list) {
        // 修改订单表
        Integer updateResult = consignOrderDao.updateByPrimaryKeySelective(consignOrder);
        if (updateResult == 0)
            return false;

        // 根据订单ID删除订单资源数据
        Integer delResult = consignOrderItemsDao.deleteByOrderId(consignOrder.getId());
        if (delResult == 0)
            return false;

        // 插入订单资源表
        for (ConsignOrderItems item : list) {
            item.setOrderId(consignOrder.getId());
            Integer addItemResult = consignOrderItemsDao.insertSelective(item);
            if (addItemResult == 0)
                return false;
        }
        return true;
    }

    public boolean setNote(ConsignOrder order) {
        return consignOrderDao.updateByPrimaryKeySelective(order) > 0;
    }

    public ConsignOrder queryById(long id) {
        return consignOrderDao.queryById(id);
    }

    public List<ConsignOrderItems> queryOrderItemsById(long id) {
        return consignOrderItemsDao.selectByOrderId(id);
    }

    /**
     * @param id
     * @return
     */
    public ConsignOrderStatusDto getStatusChanges(String id) {
        return new ConsignOrderStatusDto();
    }

    @Override
    public ConsignOrder selectByCode(String code) {
        return consignOrderDao.selectByCode(code);
    }

    /**
     * 获得当前登录人待操作的某状态下的订单总数
     *
     * @param paramMap：map封装参数说明 key:value
     *                           userId：当前登录人id
     *                           list：  订单状态list用于in条件
     * @return
     * @author dengxiyan
     */
    @Override
    public int getOrderTotalByStatus(Map<String, Object> paramMap) {
        return consignOrderDao.countOrderByStatus(paramMap);
    }

    /**
     * 统计当前登录人的某付款状态的订单总数
     *
     * @param paramMap：map封装参数说明 key:value
     *                           userId：当前登录人id
     *                           list：  订单状态list用于in条件
     *                           payStatus：订单的付款状态
     * @return
     * @author dengxiyan
     */
    @Override
    public int getPayOrderByStatus(Map<String, Object> paramMap) {
        return consignOrderDao.countPayOrderByStatus(paramMap);
    }

    /**
     * 检查用户权限
     *
     * @param orderId
     * @param user
     * @param status
     * @return
     */
    @Override
    public String checkUserRight(Long orderId, User user, List<Long> userIds, String status) {
        //验证当前用户是否有查看该订单权限
        ConsignOrder order = consignOrderDao.selectByIdAndStatus(orderId, status);
        if (order != null) {
            if (order.getOwnerId().equals(user.getId())) {
                return UserType.SALESMAN.toString();
            } else {
                ConsignProcess process = consignProcessDao.getOfficeStaff(order.getOwnerId());
                if (process != null && process.getOperatorId().equals(user.getId())) {
                    return UserType.SERVER.toString();
                }
            }
            if (userIds == null || userIds.contains(order.getOwnerId())) {
                return UserType.SUPERIOR.toString();
            }
        }
        return UserType.NOPERM.toString();
    }

    /**
     * 查询订单详情双敲是否全部匹配
     *
     * @param orderId
     * @return
     */
    @Override
    public boolean isAllMatch(Long orderId) {
        if (consignOrderDao.selectCountByIdAndFillupStatus(orderId, 2) == 0) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public ResultDto auditOrder(Long orderId, User operator) {
        ResultDto resultDto = new ResultDto();
        // 查询订单信息
        ConsignOrder order = consignOrderDao.queryById(orderId);
        if (order == null) {
            resultDto.setSuccess(false);
            resultDto.setMessage("没有找到对应的订单！");
            return resultDto;
        }

        if (!ConsignOrderStatus.NEW.getCode().equals(order.getStatus())) {
            resultDto.setSuccess(false);
            resultDto.setMessage("该交易单已处理！");
            return resultDto;
        }
        List<ConsignOrderSellerInfoDto> sellerInfoList = consignOrderItemsDao.getSellerInfo(orderId);
        if (sellerInfoList == null || sellerInfoList.size() <= 0) {
            resultDto.setSuccess(false);
            resultDto.setMessage("审核失败，未找到卖家信息！");
            return resultDto;
        }

        boolean newBuyer = false;
        boolean newContact = false;
        //未注册 ，注册买家信息
        Account buyerAccount = accountDao.selectAccountByName(order.getAccountName());
        Contact contact = contactDao.queryByTel(order.getContactTel());
        User user = userDao.queryById(order.getOwnerId());
        if (buyerAccount == null) {
            newBuyer = true;
            //edit by rabbit 新增客户
            SaveAccountDto saveInfo = new SaveAccountDto();

            Account account = new Account();
            account.setName(order.getAccountName());
            account.setAccountTag(AccountTag.buyer.getCode());
            account.setOrgId(0L);
            account.setOrgName("");
            account.setBusinessType(AccountBusinessType.terminal.toString());

            AccountExt accountExt = new AccountExt();
            accountExt.setName(order.getAccountName());

            saveInfo.setAccount(account);
            saveInfo.setAccountExt(accountExt);
            saveInfo.setUser(user);

            if (contact == null) {
                contact = new Contact();
            }
            contact.setName(order.getContactName());
            contact.setTel(order.getContactTel());
            saveInfo.setContact(contact);
            accountService.save(saveInfo);
            buyerAccount = account;
            contact = saveInfo.getContact();
        }
        else{
            Long accountTag = buyerAccount.getAccountTag();
            if((AccountTag.buyer.getCode() & accountTag) != accountTag){
                buyerAccount.setAccountTag(accountTag | AccountTag.buyer.getCode());
                int flag = accountDao.updateByPrimaryKeySelective(buyerAccount);
                if (flag == 0) {
                    throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "更新买家客户性质失败！");
                }
            }
        }
        Long managerId = order.getOwnerId()!=null?order.getOwnerId():user.getId();
        if(contact == null){
            newContact = true;
            SaveContactDto contactDto = new SaveContactDto();  //新增联系人

            contact = new Contact();
            contact.setName(order.getContactName());
            contact.setTel(order.getContactTel());
            contactDto.setContact(contact);

            
            contactDto.setUser(user);
            contactDto.setManagerIdList( new ArrayList<Long>() {{add(managerId);}});
            contactDto.setDeptId(order.getDepartmentId());
            contactDto.setIsInEditMode(false);
            contactService.saveContact(contactDto);
        }else{  //更新联系人的名字，//add by tuxianming: @see #11145
        	
        	try {
        		if(order.getContactId()!=null){
            		Contact update = new Contact();
                	update.setId(contact.getId());
                	update.setName(order.getContactName());
                	update.setLastUpdated(new Date());
                	update.setLastUpdatedBy(user.getName());
                	contactService.updateById(update);
                	/*
                    if(buyerAccount == null){
                        //因发现特殊情况下客户不存在，故在这里重新查询客户
                        buyerAccount = accountDao.selectAccountByName(order.getAccountName());
                    }
                	int exist = accountContactDao.existAssign(buyerAccount.getId(),contact.getId(), managerId);
                	if(exist == 0){
                		AccountContact ac = new AccountContact();
                		ac.setAccountId(buyerAccount.getId());
                		ac.setContactId(contact.getId());
                		ac.setManager(managerId);
                		ac.setLastUpdated(new Date());
                		ac.setLastUpdatedBy(user.getName());
                		ac.setCreated(new Date());
                		ac.setCreatedBy(user.getName());
                		ac.setModificationNumber(1);
                		accountContactDao.insert(ac);
                	}
                	*/
                	
            	}
			} catch (Exception e) {
				logger.error("更新联系人名称失败", e);
			}
        }

        // 审核订单的时候，如果该交易员与该公司部门的该联系人没有关系，审核时创建关系，当客户不存在时，订单中部门id默认为0
        if(order.getDepartmentId() != 0)
            accountContactService.addAccountContact(order.getDepartmentId(), contact.getId(), order.getOwnerId(), user);

        for (ConsignOrderSellerInfoDto item : sellerInfoList) {
            //生成卖家合同号
            BillSequence busiBillSequence = new BillSequence();
            busiBillSequence.setOrgId(operator.getOrgId());
            busiBillSequence.setAccountId(item.getSellerId());
            busiBillSequence.setSeqType(BillType.DH.toString());
            String code = billSequenceService.generateSequence(busiBillSequence);

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
            consignOrderContract.setConsignOrderId(orderId);
            consignOrderContract.setCustomerName(item.getCompanyName());
            consignOrderContract.setLastUpdated(new Date());
            consignOrderContract.setLastUpdatedBy(operator.getName());
            consignOrderContract.setBankAccountCode(item.getBankCode());
            consignOrderContractDao.insertSelective(consignOrderContract);
        }
        //生成买家合同号
        BillSequence busiBillSequence = new BillSequence();
        busiBillSequence.setOrgId(operator.getOrgId());
        busiBillSequence.setAccountId(order.getAccountId());
        busiBillSequence.setSeqType(BillType.XS.toString());
        String buyerContractCode = billSequenceService.generateSequence(busiBillSequence);

        //订单修改
        ConsignOrderUpdateDto consignOrder = new ConsignOrderUpdateDto();
        consignOrder.setId(orderId);
        if (newBuyer) {
            consignOrder.setAccountId(buyerAccount.getId());
            List<DepartmentDto> list = accountDao.queryDeptByCompanyId(buyerAccount.getId());
            if (list == null || list.size() == 0) {
                throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "该买家没有部门");
            }
            DepartmentDto dept = list.get(0);
            consignOrder.setDepartmentId(dept.getId());
            consignOrder.setDepartmentName(dept.getName());

            consignOrder.setContactId(Long.valueOf(contact.getId()));
        }
        if(newContact){
            consignOrder.setContactId(Long.valueOf(contact.getId()));
        }
        consignOrder.setContractCode(buyerContractCode);
        consignOrder.setStatus(ConsignOrderStatus.NEWAPPROVED.getCode());
        consignOrder.setLastUpdated(new Date());
        consignOrder.setLastUpdatedBy(operator.getName());

        List<String> oldStatusList = new ArrayList<>();
        oldStatusList.add(ConsignOrderStatus.NEW.getCode());
        consignOrder.setOldStatusList(oldStatusList);

        //订单状态
        OrderAuditTrail orderAuditTrail = new OrderAuditTrail();
        orderAuditTrail.setOrderId(orderId);
        orderAuditTrail.setOperatorId(operator.getId());
        orderAuditTrail.setOperatorName(operator.getName());
        orderAuditTrail.setLastUpdated(new Date());
        orderAuditTrail.setLastUpdatedBy(operator.getName());
        orderAuditTrail.setSetToStatus(ConsignOrderStatus.NEWAPPROVED.getCode());
        orderAuditTrail.setStatusType(OrderStatusType.MAIN.toString());
        orderAuditTrail.setCreated(new Date());
        orderAuditTrail.setCreatedBy(operator.getName());
        orderAuditTrail.setModificationNumber(0);
        if (consignOrderDao.updateByConditionSelective(consignOrder) > 0) {
            if (orderStatusDao.insertOrderAuditTrail(orderAuditTrail) > 0) {
                resultDto.setSuccess(true);
                resultDto.setMessage("审核成功！");

                //短信通知
                User trader = userDao.queryById(order.getOwnerId()); //获取交易员
                String endCode = order.getCode().substring(order.getCode().length() - codeLengthForSMS, order.getCode().length());
                SysSetting template=sysSettingService.queryBySettingType(SysSettingType.SmsTemplateOrderAccept.getCode());
                if (template == null) {
                    throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,"订单审核通过通知交易员短信模板缺失");
                }
                String content =template.getSettingValue();
                content = content.replace("#companyname#", order.getAccountName());
                content = content.replace("#endcode#", endCode);
                Nido.start(NidoTaskConstant.SEND_NOTE_MESSAGE, new NoteMessageContext(trader.getTel(), content));
                
                String[] pushInfo = orderCacheService.getPushInfoByUserLoginId(trader.getLoginId());
                if(pushInfo!=null){
                    content = String.format("%s交易单已通过审核，请催促打款。", order.getAccountName());
                    appPushService.sendPushNoticfication(pushInfo[0],pushInfo[1], AppNoticeType.ApprovePass.getName(),content);
                }
            } else {
                throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "日志记录失败，审核订单失败");
            }
        } else {
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "该交易单已处理！");
        }
        return resultDto;
    }

    /**
     * 二次结算金额计算
     *
     * @param orderId
     * @return
     * @throws NullPointerException
     */
    @Override
    public List<SecondaryDto> secondarySum(Long orderId) {
        List<PayRequestItems> payItems = payRequestItemsDao.queryByOrderId(orderId);
        if (payItems.isEmpty()) {
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "还未付款，无法二次结算。");
        }
        List<ConsignOrderItems> itemsList = consignOrderItemsDao.selectByOrderId(orderId);
        // 1、	卖家合同金额=合同重量*底价
        // 2、	卖家应收金额=实提重量*底价
        // 3、	卖家已收金额=申请付款金额+抵扣结算账户欠款金额
        // 4、	卖家结算账户发生额=卖家应收金额-卖家已收金额
        // 5、	卖家结算账户发生额<0时为应收，卖家结算账户发生额>0时为应付
        //将卖家部门id和卖家应收金额组成map，以组装SecondaryDto
        Map<Long, Double> sellerAmountMap = itemsList.stream().collect(Collectors.groupingBy(ConsignOrderItems::getDepartmentId,
                Collectors.summingDouble(c -> c.getActualPickWeightServer().multiply(c.getCostPrice()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue())));

        // 将卖家部门ID与卖家信息组个map，以组装SecondaryDto
        Map<Long, List<ConsignOrderItems>> sellerInfoMap = itemsList.stream().collect(Collectors.groupingBy(ConsignOrderItems::getDepartmentId,
                Collectors.toList()));

        //将卖家部门id和卖家已收金额组成map，以组装SecondaryDto
        Map<Long, BigDecimal> sellerPayAmount = new HashMap<>();
        for (PayRequestItems p : payItems) {
            //modify by wangxianjun 合同变更后会出现多笔付款申请单，需要累加付款金额
            BigDecimal totalWeight = new BigDecimal(0.0);
            if(sellerPayAmount.get(p.getReceiverDepartmentId()) == null) {
                sellerPayAmount.put(p.getReceiverDepartmentId(), p.getPayAmount().add(p.getSecondBalanceTakeout().setScale(2, BigDecimal.ROUND_HALF_UP)).add(p.getCreditUsedRepay()));
            }else{
                totalWeight = sellerPayAmount.get(p.getReceiverDepartmentId()).add(p.getPayAmount().add(p.getSecondBalanceTakeout().setScale(2, BigDecimal.ROUND_HALF_UP)).add(p.getCreditUsedRepay()));
                sellerPayAmount.put(p.getReceiverDepartmentId(),totalWeight);
            }
        }

        // 买家：
        // 1、	买家实付在订单中有记录
        // 2、	买家应付金额=实提重量*成交价
        // 3、	买家结算账户发生额=买家实付金额-买家应付金额
        // 4、	买家结算账户发生额>0时为应付，买家结算账户发生额<0时为应收
        // 买家结算账户发生额=成交价×订单重量(四舍五入)-成交价x二结重量（四舍五入），这个值大于0是应付，小于0是应收
        double buyerShouldPayAmount = itemsList.stream().mapToDouble(c -> c.getDealPrice().multiply(c.getActualPickWeightServer()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()).sum();
        double buyerAmount = consignOrderDao.selectByPrimaryKey(orderId).getTotalContractReletedAmount().doubleValue() - buyerShouldPayAmount;

        List<SecondaryDto> dtos = new LinkedList<SecondaryDto>();
        for (Long sellerDepartmentId : sellerInfoMap.keySet()) {
            dtos.add(new SecondaryDto(sellerInfoMap.get(sellerDepartmentId).get(0).getSellerId(), sellerInfoMap.get(sellerDepartmentId).get(0).getSellerName(),
                    sellerDepartmentId, sellerInfoMap.get(sellerDepartmentId).get(0).getDepartmentName(),
                    AccountType.seller, (new BigDecimal(sellerAmountMap.get(sellerDepartmentId))).subtract(sellerPayAmount.get(sellerDepartmentId)),
                    sellerInfoMap.get(sellerDepartmentId).get(0).getSellerOwnerId(), sellerInfoMap.get(sellerDepartmentId).get(0).getSellerOwnerName()));  //卖家结算账户发生额=卖家应收金额-卖家已收金额
        }
        ConsignOrder order = consignOrderDao.selectByPrimaryKey(orderId);
        dtos.add(0, new SecondaryDto(order.getAccountId(), order.getAccountName(), order.getDepartmentId(), order.getDepartmentName(),
                AccountType.buyer, new BigDecimal(buyerAmount), order.getOwnerId(), order.getOwnerName()));
        return dtos;
    }

    /**
     * 插入进项销项票池&&计算返利和提成
     *
     * @param orderId
     * @param dto
     * @param manager
     * @param buyerManager
     * @param user
     * @return
     */
    private boolean insertPoolInAndPoolOut(Long orderId, SecondaryDto dto, Account buyer, User manager, User buyerManager, User user) {
        List<ConsignOrderItems> items = consignOrderItemsDao.selectByOrderIdAndSellerId(orderId, dto.getCusId());
        ConsignOrder order = consignOrderDao.queryById(orderId);

        if (manager == null || buyerManager == null) {
            return false;
        }
        
        for (ConsignOrderItems item : items) {
            if (item.getActualPickQuantityServer() == 0 && BigDecimal.ZERO.compareTo(item.getActualPickWeightServer()) == 0) {
                continue;
            }
            //计算提成
            computeReward(buyerManager, item, user, orderId, buyerManager, order);

            // 添加进项池信息
            PoolInDetail poolInDetail = addPoolIn(item, user, buyerManager);

            // 添加销项池信息
            addPoolOut(order, buyer, buyerManager, item, user, poolInDetail);
        }
        // 生成返利信息
      //Green comment 20160229 去除返利，改为积分
//        Nido.start(NidoTaskConstant.ADD_REBATE_BY_ORDER, new AddRebateByOrderContext(orderId, user, dto.getCusId()));
        return true;
    }
    
    
    private void computeReward(User buyerManager,ConsignOrderItems item,User user,Long orderId, User manager,ConsignOrder order){
    	String consignType;
    	ReportRewardRecord reward = new ReportRewardRecord(buyerManager.getId(), new Date(), item.getId(), orderId, buyerManager.getName(), Constant.YES,
                buyerManager.getOrgId(), organizationDao.queryById(buyerManager.getOrgId()).getName(), manager.getId(), manager.getName(), organizationDao.queryById(manager.getOrgId()).getName(),
                manager.getOrgId(), consignType = order.getConsignType(), item.getActualPickWeightServer(), user.getLoginId());
        //大类
        CategoryGroupDto categoryGroup = categoryGroupDao.selectByNsortName(item.getNsortName());
        reward.setCategoryGroupUuid(categoryGroup.getCategoryGroupUuid());
        reward.setCategoryGroupName(categoryGroup.getCategoryGroupName());
        //大类提成
        Map<String, Object> param = new HashMap<>();
        param.put("categoryUUID", categoryGroup.getCategoryGroupUuid());
        param.put("rewardStatus", RewardStatus.EFFECT.getName());
        param.put("rewardType", RewardType.CATEGORY.getCode());
        List<Reward> categoryrewardList = rewardDao.queryAll(param);
        if (categoryrewardList.isEmpty()) {
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "无法找到品规" + item.getNsortName() + "对应的大类");
        }
        Reward categoryreward = categoryrewardList.get(0);
        reward.setCategoryRewartRoleId(Long.valueOf(categoryreward.getId()));
        reward.setCategoryRewartRole(categoryreward.getRewardRole());
        //提成百分比
        Reward sellerreward;
        Reward buyerreward;
        param.clear();
        param.put("rewardStatus", RewardStatus.EFFECT.getName());
        param.put("rewardType", RewardType.ORDER.getCode());
        if (!manager.getOrgId().equals(buyerManager.getOrgId())) {   //如果是跨服务中心交易
            if (ConsignType.consign.toString().equals(consignType)) {
                param.put("categoryUUID", UserRewardType.buyerConsignRangeReward.getCode());
                buyerreward = rewardDao.queryAll(param).get(0);
                param.put("categoryUUID", UserRewardType.sellerConsignRangeReward.getCode());
                sellerreward = rewardDao.queryAll(param).get(0);
            } else {
                param.put("categoryUUID", UserRewardType.buyerTempRangeReward.getCode());
                buyerreward = rewardDao.queryAll(param).get(0);
                param.put("categoryUUID", UserRewardType.sellerTempRangeReward.getCode());
                sellerreward = rewardDao.queryAll(param).get(0);
            }
        } else {
            if (ConsignType.consign.toString().equals(consignType)) {
                param.put("categoryUUID", UserRewardType.buyerConsignLocalReward.getCode());
                buyerreward = rewardDao.queryAll(param).get(0);
                param.put("categoryUUID", UserRewardType.sellerConsignLocalReward.getCode());
                sellerreward = rewardDao.queryAll(param).get(0);
            } else {
                param.put("categoryUUID", UserRewardType.buyerTempLocalReward.getCode());
                buyerreward = rewardDao.queryAll(param).get(0);
                param.put("categoryUUID", UserRewardType.sellerTempLocalReward.getCode());
                sellerreward = rewardDao.queryAll(param).get(0);
            }
        }
        reward.setBuyerRewartRoleId(Long.valueOf(buyerreward.getId()));
        reward.setBuyerRewartRole(buyerreward.getRewardRole());
        reward.setSellerRewartRoleId(Long.valueOf(sellerreward.getId()));
        reward.setSellerRewartRole(sellerreward.getRewardRole());
        //提成
        BigDecimal categoryRewardAmount = item.getActualPickWeightServer().multiply(categoryreward.getRewardRole()).setScale(2, BigDecimal.ROUND_HALF_UP);   //吨数提成四舍五入一次
        reward.setBuyerRewardAmount(categoryRewardAmount.multiply(buyerreward.getRewardRole()).divide(BigDecimal.valueOf(100)).setScale(2, BigDecimal.ROUND_HALF_UP));  //买家/卖家业务员分成再次舍五入一次
        reward.setSellerRewardAmount(categoryRewardAmount.multiply(sellerreward.getRewardRole()).divide(BigDecimal.valueOf(100)).setScale(2, BigDecimal.ROUND_HALF_UP));
        if (reportRewardRecordDao.insertSelective(reward) != 1) {
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "插入提成数据失败");
        }
    }
    
    private void addPoolOut(ConsignOrder order, Account buyer,User buyerManager,ConsignOrderItems item,User user,PoolInDetail poolInDetail){
    	Map<String, Object> param = new HashMap<>();
        param.put("departmentId", order.getDepartmentId());  //这里用部门id去搜索
        param.put("ownerId", buyerManager.getId());
        param.put("orgId", buyerManager.getOrgId());
        param.put("checkAmount", false);
        List<PoolOut> poolOuts = poolOutDao.queryByBuyer(param);
        PoolOut poolOut;
        if (poolOuts.isEmpty()) {
            poolOut = new PoolOut(buyerManager.getOrgId(), organizationDao.queryById(buyerManager.getOrgId()).getName(),
                    buyerManager.getId(), buyerManager.getName(), buyer.getId(), buyer.getName(),
                    order.getDepartmentId(), order.getDepartmentName(),
                    item.actualBuyerAmount(), item.getActualPickWeightServer(), user.getLoginId());
            poolOutDao.insertSelective(poolOut);
        } else {
            poolOut = poolOuts.get(0);
            poolOut.setTotalWeight(poolOut.getTotalWeight().add(item.getActualPickWeightServer()));
            poolOut.setTotalAmount(poolOut.getTotalAmount().add(item.actualBuyerAmount()));
            poolOut.setLastUpdated(new Date());
            poolOut.setLastUpdatedBy(user.getLoginId());
            poolOut.setModificationNumber(poolOut.getModificationNumber() + 1);
            poolOutDao.updateByPrimaryKeySelective(poolOut);
        }

        param.clear();
        param.put("poolOutId", poolOut.getId());
        param.put("nsortName", item.getNsortName());
        param.put("material", item.getMaterial());
        param.put("spec", item.getSpec());
        param.put("checkAmount", false);
        List<PoolOutDetail> poolOutDetails = poolOutDetailDao.queryByBuyerAndDetails(param);
        PoolOutDetail poolOutDetail;
        if (poolOutDetails.isEmpty()) {
            poolOutDetail = new PoolOutDetail(poolOut.getId(), item.getNsortName(), item.getMaterial(), item.getSpec(), item.actualBuyerAmount(), item.getActualPickWeightServer(), user.getLoginId());
            poolOutDetailDao.insertSelective(poolOutDetail);
        } else {
            poolOutDetail = poolOutDetails.get(0);
            poolOutDetail.setTotalWeight(poolOutDetail.getTotalWeight().add(item.getActualPickWeightServer()));
            poolOutDetail.setTotalAmount(poolOutDetail.getTotalAmount().add(item.actualBuyerAmount()));
            poolOutDetail.setLastUpdated(new Date());
            poolOutDetail.setLastUpdatedBy(user.getLoginId());
            poolOutDetail.setModificationNumber(poolInDetail.getModificationNumber() + 1);
            poolOutDetailDao.updateByPrimaryKeySelective(poolOutDetail);
        }
    }
    
    private PoolInDetail addPoolIn(ConsignOrderItems item,User user,User manager){
    	Map<String, Object> param = new HashMap<>();
    	param.put("departmentId", item.getDepartmentId());
        param.put("orgId", manager.getOrgId());
        param.put("checkAmount", false);
        List<PoolIn> poolIns = poolInDao.queryByDepartment(param);
        PoolIn poolIn;
        if (poolIns.isEmpty()) {
            poolIn = new PoolIn(manager.getOrgId(), organizationDao.queryById(manager.getOrgId()).getName(), item.getSellerId(), item.getSellerName(),
                    item.getDepartmentId(), item.getDepartmentName(), item.actualSellerAmount(), item.getActualPickWeightServer(), user.getLoginId());
            poolInDao.insertSelective(poolIn);
        } else {
            poolIn = poolIns.get(0);
            poolIn.setTotalAmount(poolIn.getTotalAmount().add(item.actualSellerAmount()));
            poolIn.setTotalWeight(poolIn.getTotalWeight().add(item.getActualPickWeightServer()));
            poolIn.setLastUpdated(new Date());
            poolIn.setLastUpdatedBy(user.getLoginId());
            poolIn.setModificationNumber(poolIn.getModificationNumber() + 1);
            poolInDao.updateByPrimaryKeySelective(poolIn);
        }

        param.clear();
        param.put("poolInId", poolIn.getId());
        param.put("sellerId", item.getSellerId());
        param.put("nsortName", item.getNsortName());
        param.put("material", item.getMaterial());
        param.put("spec", item.getSpec());
        param.put("checkAmount", false);
        List<PoolInDetail> poolInDetails = poolInDetailDao.queryBySellerAndDetails(param);
        PoolInDetail poolInDetail;
        if (poolInDetails.isEmpty()) {
            poolInDetail = new PoolInDetail(poolIn.getId(), item.getNsortName(), item.getMaterial(), item.getSpec(), item.actualSellerAmount(), item.getActualPickWeightServer(), user.getLoginId());
            poolInDetailDao.insertSelective(poolInDetail);
        } else {
            poolInDetail = poolInDetails.get(0);
            poolInDetail.setTotalWeight(poolInDetail.getTotalWeight().add(item.getActualPickWeightServer()));
            poolInDetail.setTotalAmount(poolInDetail.getTotalAmount().add(item.actualSellerAmount()));
            poolInDetail.setLastUpdated(new Date());
            poolInDetail.setLastUpdatedBy(user.getLoginId());
            poolInDetail.setModificationNumber(poolInDetail.getModificationNumber() + 1);
            poolInDetailDao.updateByPrimaryKeySelective(poolInDetail);
        }
        
        return poolInDetail;
    }

    /**
     * 二次结算 直接完成订单
     *
     * @param orderId
     * @param user
     * @return
     */
//    public String secondaryAccomplish(Long orderId, User user, String right){
//        StringBuilder SMSMessage = new StringBuilder();
//        ConsignOrder order = queryById(orderId);
//
//
//        //修改订单状态
//        Map<String, Object> paramMap = new HashMap<String, Object>();
//        paramMap.put("orderId", orderId);
//        paramMap.put("lastUpdated", new Date());
//        paramMap.put("lastUpdatedBy", user.getLoginId());
//        paramMap.put("secondaryTime", new Date()); //更新二结时间
//        if (orderStatusDao.updateOrderStatusForSecondaryAccomplish(paramMap) != 1) {
//            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "该订单已二次结算或正在二次结算");
//        }
//        SMSMessage.append("实提重量为零，订单完成！");
//        return SMSMessage.toString();
//    }

    /**
     * 二次结算
     *
     * @param orderId
     * @param user
     * @return
     */
    @Override
    public String secondary(Long orderId, User user, String right,int actualWeight) {
        StringBuilder SMSMessage = new StringBuilder();
        ConsignOrder order = queryById(orderId);
        //查看订单详情 因合同变更，可能会导致存在未匹配的订单详情，此时不能二结 add lixiang 2016/8/30
        List<ConsignOrderItems> consignOrderItems = consignOrderItemsDao.selectByOrderId(orderId);
        consignOrderItems.forEach(items -> {
			if (NUM != items.getStatus()) {
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "此订单详情存在未录入的实提量，请录入后再试！");
			}
		});
        //修改订单状态
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String  status="7";
        if(actualWeight==0){
            status="10";
        }
        paramMap.put("status", status);
        paramMap.put("orderId", orderId);
        paramMap.put("lastUpdated", new Date());
        paramMap.put("lastUpdatedBy", user.getLoginId());
        paramMap.put("secondaryTime", new Date()); //更新二结时间
        if (orderStatusDao.updateOrderStatusForSecondarySettlement(paramMap) != 1) {
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "该订单已二次结算或正在二次结算");
        }
        OrderAuditTrail orderAuditTrail = buildOrderAuditTrail(orderId, user);
        if (orderStatusService.insertOrderAuditTrail(orderAuditTrail) != 1) {
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "订单状态流水插入失败");
        }

        List<SecondaryDto> dtos = secondarySum(orderId);   //买家放在第0个
        Optional<SecondaryDto> o = dtos.stream().filter(a -> AccountType.buyer.equals(a.getCusType())).findFirst();
        SecondaryDto buyerDto = o.orElseThrow(() -> new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "买家不存在"));
        Account buyer = accountDao.selectByPrimaryKey(buyerDto.getCusId());
//        Account buyerDepartment = accountDao.selectByPrimaryKey(buyerDto.getDepartmentId()); //此处取部门edit by rabbit
        if (buyer == null) {
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "买家不存在");
        }
        User buyerManager = userDao.queryById(order.getOwnerId());   //买家管理员
        if (buyerManager == null) {
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "买家管理员不存在");
        }

       // if(buyer.getBuyerDealTotal()==0 && buyer.getSellerDealTotal() == 0){  //如果是这个用户做的第一笔单子，把新增用户返利设置为激活
            /** 新增客户返利 **/
            addNewUserReward(buyerManager, buyerDto, order.getCreated());
            /** end of user reward **/
       // }
        //修改买家交易次数
        buyer.setBuyerDealTotal(buyer.getBuyerDealTotal() + 1);
        buyer.setLastUpdated(new Date());
        buyer.setLastUpdatedBy(user.getLoginId());
        accountDao.updateByPrimaryKeySelective(buyer);

        Result result ;
        for (int i = 0; i < dtos.size(); i++) {
            SecondaryDto dto = dtos.get(i);
            //对部门做二次结算
            accountFundService.updateAccountFund(dto.getDepartmentId(), AssociationType.ORDER_CODE, order.getCode(),
                    AccountTransApplyType.SECOND_PAY, BigDecimal.ZERO, BigDecimal.ZERO, new BigDecimal(dto.getSecondaryMoney()), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, PayType.BALANCE,
                    user.getId(), user.getName(), new Date());   //调用支付接口 edit by rabbit
            // 在订单二次结算时需要对信用额度进行自动还款\抵扣
            accountFundService.payForCredit(dto.getDepartmentId(), AssociationType.ORDER_CODE, order.getCode(), 0L,
                    Constant.SYSTEMNAME, new Date());
            //更新进项销项池
            Account account = accountDao.selectByPrimaryKey(dto.getCusId());  //用户账户
            if (account == null) {
                throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "卖家不存在");
            }
            User manager = userDao.queryById(dto.getManagerId());         //钢为交易员
            if (manager == null) {
                throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "卖家管理员不存在");
            }

            if (AccountType.seller.equals(dto.getCusType())) {//判断是卖家
                //修改卖家交易次数
                //if(account.getBuyerDealTotal()==0 && account.getSellerDealTotal() == 0){  //如果是这个用户做的第一笔单子，把新增用户返利设置为激活
                    addNewUserReward(buyerManager, dto, order.getCreated());
                //}
                account.setSellerDealTotal(buyer.getSellerDealTotal() + 1);
                account.setLastUpdated(new Date());
                account.setLastUpdatedBy(user.getLoginId());
                accountDao.updateByPrimaryKeySelective(account);

                insertPoolInAndPoolOut(orderId, dto, buyer, manager, buyerManager, user);   //插入进项票池
            }

            //发送短信通知
            String code = consignOrderDao.selectByPrimaryKey(orderId).getCode();
            SysSetting template=sysSettingService.queryBySettingType(SysSettingType.SmsTemplateSettle.getCode());
            if (template == null) {
                throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,"二结完成通知交易员短信模板缺失");
            }
            String content =template.getSettingValue();
            content = content.replace("#endcode#", code.substring(code.length() - codeLengthForSMS));
            content = content.replace("#companyname#", dto.getCusName());
            content = content.replace("#balancetype#", account.getBalanceSecondSettlement().floatValue() >= 0 ? "余额" : "欠款");
            content = content.replace("#balance#", String.valueOf(account.getBalanceSecondSettlement().abs().floatValue()));
            Nido.start(NidoTaskConstant.SEND_NOTE_MESSAGE, new NoteMessageContext(manager.getTel(), content));
            SMSMessage.append("已短信通知" + manager.getName() + "_" + manager.getTel() + "公司" + dto.getCusName() + "二次结算成功<br/>");
        }
        // 供应商进项报表踩点 - 二结踩点
        reportFinanceService.orderOperation(ReportSellerInvoiceInOperationType.SecondSettlement.getOperation(), orderId, user);
        
        //将金额变化，添加到report_buyer_invoice_out
        dtos.forEach(a -> {
            if (AccountType.buyer.equals(a.getCusType())) {
                reportFinaceService.pushToReportInvoiceOut(order, user, a.getSecondaryMoneyBigDecimal(), ReportBuyerInvoiceOutType.SecondSettle, null);
            }
        });
        //ADD BY LIXING 订单状态变动时推送消息至钢为掌柜app 已完成 
//        Organization organization = organizationDao.queryById(user.getOrgId());
//        User manageUser = userDao.queryById(organization.getCharger());
//        if (manageUser != null) {
//        	String[] pushInfo = orderCacheService.getPushInfoByUserLoginId(manageUser.getLoginId());
//        	if(pushInfo!=null){
//        		appPushService.sendPushNoticfication(pushInfo[0],pushInfo[1], BEEN_COMPLETED, BEEN_COMPLETED);
//        	}
//        }
        //积分系统调用
        List<ConsignOrderItems> items = consignOrderItemsDao.selectByOrderId(orderId);
        pointService.earnPoint(order, items);
        
        return SMSMessage.toString();
    }

    /**
     * 在report_new_user_reward 存储account_id(add_new_buyer,add_new_seller)字段，
     * 存储第一次开单first_open_time时间，
     * 在每次二结的时候查询，这个表里在当前是不是有，该买家记录(根据买家account_id)，如果没有，则直接新增
     * 如果有，则判断二结的订单开单时间是否早于先前单的开单的时，如果不是就不做任何事
     *      如果是，判断是不是在当前月里二结，也就是这条记录addDate
     * 		如果是：则修改这条记录提成，把指定到这个二结的用交易员，
     *		如果不是，则不修改
     * @param secondary 
     * @param manager
     * @param secondary
     */
    private void addNewUserReward(User manager, SecondaryDto secondary, Date openOrderDate){
    	
    	Calendar date = Calendar.getInstance();
    	
    	ReportRewardQuery query = new ReportRewardQuery();
    	query.setAccountId(secondary.getCusId());
//    	query.setOpenOrderDate(openOrderDate);
    	List<ReportNewUserReward> reportRewards = reportNewUserRewardDao.queryByAccountAndOpenDate(query);
    	if(reportRewards.isEmpty()){
    		//insert
    		processAddNewUser(manager, secondary, openOrderDate);
    	}else{
    		reportRewards = reportRewards.stream().filter(a-> a.getOpenOrderDate().after(openOrderDate)).collect(Collectors.toList());
            if(reportRewards.size() > 0) {
                for (ReportNewUserReward reportNewUserReward : reportRewards) {
                    Calendar addDate = (Calendar) date.clone();
                    addDate.setTime(reportNewUserReward.getAddDate());
                    if (addDate.get(Calendar.MONTH) == date.get(Calendar.MONTH)) {

                        //delete
                        reportNewUserRewardDao.deleteByAccountId(secondary.getCusId(), null, reportNewUserReward.getModificationNumber());

                        //insert
                        processAddNewUser(manager, secondary, openOrderDate);
                        break;
                    }
                }
            }
    	}
    }

    /**
     * 插入客户返利到report_new_user_reward
     * @param manager
     * @param secondary
     */
    private void processAddNewUser(User manager, SecondaryDto secondary, Date openOrderDate){
    	/** 新增客户返利 **/
        ReportNewUserReward newUserReward = new ReportNewUserReward(new Date(),
                manager.getId(), manager.getName(), manager.getOrgId(), organizationDao
                .queryById(manager.getOrgId()).getName());
        Map<String, Object> param = new HashMap<>();
        param.put("rewardStatus", RewardStatus.EFFECT.getName());
        param.put("rewardType", RewardType.ORDER.getCode());
        if(AccountType.buyer.equals(secondary.getCusType())){
            param.put("categoryUUID", UserRewardType.newSellerReward.getCode());
        }else{
            param.put("categoryUUID", UserRewardType.newBuyerReward.getCode());  
        }
        Reward newBuyerReward = rewardDao.queryAll(param).get(0);  					//查提成金额
        if(AccountType.buyer.equals(secondary.getCusType())) {  						//根据帐户类型不一样去给报表不同的值
            newUserReward.setBuyerRewardRole(newBuyerReward.getRewardRole());
            newUserReward.setAddNewBuyer(secondary.getCusId());
            newUserReward.setBuyerRewardAmount(newBuyerReward.getRewardRole());
        }else{
            newUserReward.setSellerRewardRole(newBuyerReward.getRewardRole());
            newUserReward.setAddNewSeller(secondary.getCusId());
            newUserReward.setSellerRewardAmount(newBuyerReward.getRewardRole());
        }
        newUserReward.setActive(Integer.valueOf(Constant.YES));
        newUserReward.setOpenOrderDate(openOrderDate);
        if (reportNewUserRewardDao.insertSelective(newUserReward) != 1) {
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,
                    "新增用户返利失败");
        }
        /** end of user reward **/
    }
    
    /**
     * 财务确认付款后订单回滚
     * @param orderId
     * @param user
     */
    public void rollBack(Long orderId, User user){
        Date date = new Date();
        ConsignOrder order = consignOrderDao.selectByPrimaryKey(orderId);
        if(order == null){
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "订单不存在，请检查输入的订单号");
        }
        if(!order.getStatus().equals(ConsignOrderStatus.CLOSESECONDARYAPPROVED.getCode())){
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "订单状态不正确");
        }
        PayRequest payRequest = payRequestDao.selectAvailablePaymentByOrderId(orderId);
        if(payRequest == null){
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "该订单还未确认付款，无法使用此接口关闭");
        }

        //判断订单申请关闭前状态来确定是二结后回滚还是支付后回滚
        if (ConsignOrderStatus.RELATED.getCode().equals(order.getOriginStatus())
                || ConsignOrderStatus.SECONDSETTLE.getCode().equals(order.getOriginStatus())) {
            //回滚买/卖家账户
            rollbackAccountInfo_payed(order, user);
            
            //将回滚的金额存入到报表中
            reportFinaceService.pushToReportInvoiceOut(order, user, order.getTotalContractReletedAmount(), ReportBuyerInvoiceOutType.CloseOrder, null);
            
        } else if (ConsignOrderStatus.INVOICEREQUEST.getCode().equals(order.getOriginStatus())) {
            if(invoiceInDetailOrderitemDao.countByOrderId(order.getId()) > 0){
                throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "请取消与该订单关联的进项票后重试");
            }
            //回滚买/卖家账户
            rollbackAccountInfo(order, user);

            List<ReportRebateRecord> rebateRecordList = reportRebateRecordDao.queryByOrderCode(order.getCode());
            if(rebateRecordList != null && rebateRecordList.size() > 0) {    //如果能查到返利数据，说明实提量不为0，系统中应该存在返利和提成记录
                //回滚买家/卖家交易员提成
                if(reportRewardRecordDao.disableRewardByOrderId(order.getId()) <= 0){
                    throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "删除买/卖家交易员提成数据失败");
                }
                //回滚买家返利
                //CBMS
                if(reportRebateRecordDao.disableRebateByOrderCode(order.getCode()) <= 0){
                    throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "删除返利数据失败");
                }
                //IV
                AddRebateInMarketContext rebateCtx = new AddRebateInMarketContext();
                rebateCtx.setKey(ivFinanceServiceKEY);
                rebateCtx.setMoney(0-rebateRecordList.stream().mapToDouble(r -> r.getRebateAmount().doubleValue()).sum());
                rebateCtx.setPhone(order.getContactTel());
                rebateCtx.setUserName(order.getContactName());
                Nido.start(NidoTaskConstant.ADD_REBATE_IN_MARKET, rebateCtx);
            }
            //回滚进项销项池
            rollbackPoolInAndPoolOut(order, user);
            // 供应商进项报表踩点 - 订单关闭踩点
            reportFinanceService.orderOperation(ReportSellerInvoiceInOperationType.OrderClose.getOperation(),orderId, user);
            //回滚积分
            pointService.rollbackOrderPoint(order.getCode());
        } else{
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "订单申请关闭前状态有误，无法回滚");
        }

        //更新订单状态
        Map<String, Object> param = new HashMap<>();
        param.put("status", ConsignOrderStatus.INVOICEREQUEST.getCode().equals(order.getOriginStatus())?ConsignOrderStatus.CLOSESECONDARY.getCode():ConsignOrderStatus.CLOSEPAYED.getCode());
        param.put("lastUpdated", date);
        param.put("lastUpdatedBy", user.getLoginId());
        param.put("modification_number", order.getModificationNumber() + 1);
        param.put("id", order.getId());
        if(!orderStatusService.updateOrderStatus(param, new OrderAuditTrail(order.getId(), ConsignOrderStatus.CLOSESECONDARY.getCode(),
                ConsignOrderStatus.CLOSESECONDARY.getName(), OrderStatusType.MAIN.toString(), user.getId(), user.getName()))){
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "更新订单状态失败");
        }
        
    }

    private void rollbackAccountInfo(ConsignOrder order, User user){
        Date date = new Date();
        String orderCode = order.getCode();
        //回滚买/卖家账户余额
        List<SecondaryDto> list = secondarySum(order.getId());
        List<PayRequestItems> payItems = payRequestItemsDao.queryByOrderId(order.getId());
        if (payItems.isEmpty()) {
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "找不到付款申请单信息");
        }
        //将卖家部门id和卖家已收金额组成map，以组装SecondaryDto
        Map<Long, BigDecimal> sellerPayAmount = new HashMap<>();
        Map<Long, BigDecimal> sellerUseCredit = new HashMap<>();//卖家还信用额度
        for (PayRequestItems p : payItems) {
            //modify by wangxianjun 合同变更后会出现多笔付款申请单，需要累加付款金额和已使用的信用额度
            BigDecimal totalAmount = new BigDecimal(0.0);
            BigDecimal totalUsedCredit = new BigDecimal(0.0);
            if(sellerPayAmount.get(p.getReceiverDepartmentId()) == null) {
                sellerPayAmount.put(p.getReceiverDepartmentId(), p.getPayAmount().add(p.getSecondBalanceTakeout()));
                sellerUseCredit.put(p.getReceiverDepartmentId(),p.getCreditUsedRepay());
            }else{
                totalAmount = sellerPayAmount.get(p.getReceiverDepartmentId()).add(p.getPayAmount().add(p.getSecondBalanceTakeout()));
                totalUsedCredit = sellerUseCredit.get(p.getReceiverDepartmentId()).add(p.getCreditUsedRepay());
                sellerPayAmount.put(p.getReceiverDepartmentId(),totalAmount);
                sellerUseCredit.put(p.getReceiverDepartmentId(),totalUsedCredit);
            }
        }

        Result result = new Result();
        for(SecondaryDto dto : list){
            if(AccountType.buyer.equals(dto.getCusType())){  //买家
                //回滚资金账户和二结账户
                accountFundService.updateAccountFund(dto.getDepartmentId(), AssociationType.ORDER_CODE, order.getCode(),
                        AccountTransApplyType.SECONDARY_BACK, BigDecimal.ZERO,
                        BigDecimal.ZERO, new BigDecimal(dto.getSecondaryMoney()).negate().add(order.getTotalContractReletedAmount()), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, PayType.BALANCE, user.getId(), user.getName(), date);
                if(!result.isSuccess()){
                    throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, String.format("回滚买家%s账户失败--%s", dto.getCusName(), result.getData().toString()));
                }

                //买家调用还款/抵扣接口 add by dengxiyan
                accountFundService.payForCredit(dto.getDepartmentId(), AssociationType.ORDER_CODE, order.getCode(), 0l, Constant.SYSTEMNAME, new Date());
                if (!result.isSuccess()) {
                    throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, String.format("回滚买家%s账户失败,自动还款/抵扣失败--%s",dto.getCusName(), result.getData().toString()));
                }

                //回滚买家交易次数
                Account account = accountDao.selectByPrimaryKey(dto.getCusId());
                account.setBuyerDealTotal(account.getBuyerDealTotal()-1);
                account.setLastUpdated(date);
                account.setLastUpdatedBy(user.getLoginId());
                if(accountDao.updateDealTotal(account) != 1){
                    throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, String.format("回滚买家%s交易次数失败", dto.getCusName()));
                }
                //回滚新增买家客户提成
                ReportNewUserReward reward = newUserRewardDao.queryByAccountId(account.getId());
                if(reward != null){
                    //先删除
                    if(newUserRewardDao.deleteByAccountId(account.getId(), user.getLoginId(), reward.getModificationNumber()) != 1){
                        throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, String.format("回滚新增买家客户%s提成失败", dto.getCusName()));
                    }
                    //如果该客户有其他订单，则把提成给到那一单上
                    ConsignOrder o = consignOrderDao.selectEarliestOrderByAccountIdExceptOrderId(account.getId(), order.getId());
                    if(o != null){
                        SecondaryDto d = new SecondaryDto(account.getId(), null, null, null, (o.getAccountId().equals(account.getId()) ? AccountType.buyer : AccountType.seller), null, null, null);
                        User u = userDao.queryById(order.getOwnerId());
                        addNewUserReward(u, d, order.getCreated());
                    }
                }
                
                //将回滚的金额插入到报表日志
                BigDecimal balance = BigDecimal.ZERO;
                balance = (order.getTotalContractReletedAmount().subtract(new BigDecimal(dto.getSecondaryMoney()))).negate();
                reportFinaceService.pushToReportInvoiceOut(order, user, balance, ReportBuyerInvoiceOutType.CloseOrder, null);
                
            }else{   //卖家
                //回滚二结账户
                BigDecimal amount = sellerPayAmount.get(dto.getDepartmentId());   //回滚抵扣
                accountFundService.updateAccountFund(dto.getDepartmentId(), AssociationType.ORDER_CODE, order.getCode(),
                        AccountTransApplyType.SECONDARY_BACK, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO.subtract(new BigDecimal(dto.getSecondaryMoney())).subtract(amount), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                        PayType.BALANCE, user.getId(), user.getName(), date);
                if(!result.isSuccess()){
                    throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, String.format("回滚卖家%s账户失败--%s", dto.getCusName(), result.getData().toString()));
                }

                //卖家回退还款信用额度 add by dengxiyan
                BigDecimal useCredit = sellerUseCredit.get(dto.getDepartmentId());
                if(BigDecimal.ZERO.compareTo(useCredit) != 0){
                    accountFundService.updateAccountFund(dto.getDepartmentId(), AssociationType.ORDER_CODE,
                            order.getCode(), AccountTransApplyType.CREDITLIMI_BACK,
                            BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, useCredit, BigDecimal.ZERO
                            , PayType.BALANCE, 0l, Constant.SYSTEMNAME, new Date());
                    if (!result.isSuccess()) {
                        throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, String.format("回滚卖家%s账户失败,信用额度回退--%s", dto.getCusName(), result.getData().toString()));
                    }

                }
                //卖家调用自动还款/抵扣接口 add by dengxiyan
                accountFundService.payForCredit(dto.getDepartmentId(),AssociationType.ORDER_CODE,order.getCode(),0l,Constant.SYSTEMNAME,new Date());
                if (!result.isSuccess()) {
                    throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, String.format("回滚卖家%s账户失败,自动还款/抵扣失败--%s ",dto.getCusName(),result.getData().toString()));
                }


                //回滚卖家交易次数
                Account account = accountDao.selectByPrimaryKey(dto.getCusId());
                account.setSellerDealTotal(account.getSellerDealTotal() - 1);
                account.setLastUpdated(date);
                account.setLastUpdatedBy(user.getLoginId());
                if(accountDao.updateDealTotal(account) != 1){
                    throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, String.format("回滚卖家%s交易次数失败", dto.getCusName()));
                }
                //回滚新增卖家客户提成
                ReportNewUserReward reward = newUserRewardDao.queryByAccountId(account.getId());
                if(reward != null){
                    //先删除
                    if(newUserRewardDao.deleteByAccountId(account.getId(), user.getLoginId(), reward.getModificationNumber()) != 1){
                        throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, String.format("回滚新增买家客户%s提成失败", dto.getCusName()));
                    }
                    //如果该客户有其他订单，则把提成给到那一单上
                    ConsignOrder o = consignOrderDao.selectEarliestOrderByAccountIdExceptOrderId(account.getId(), order.getId());
                    if(o != null){
                        SecondaryDto d = new SecondaryDto(account.getId(), null, null, null, (o.getAccountId().equals(account.getId()) ? AccountType.buyer : AccountType.seller), null, null, null);
                        User u = userDao.queryById(order.getOwnerId());
                        addNewUserReward(u, d, order.getCreated());
                    }
                }
            }
        }
    }
    private void rollbackPoolInAndPoolOut(ConsignOrder order, User user){
        Map<String, Object> param = new HashMap<String, Object>();
        List<ConsignOrderItems> items = consignOrderItemsDao.selectByOrderId(order.getId());
        Account buyer = accountDao.selectByPrimaryKey(order.getAccountId());
        User buyerManager = userDao.queryById(order.getOwnerId());

        for(ConsignOrderItems item : items){

            //当实提重量为0时，跳出当前循环，

            if(item.getActualPickWeightServer().compareTo(BigDecimal.ZERO) == 0){
                continue;
            }
            User manager = userDao.queryById(order.getOwnerId());
            param.clear();
            param.put("departmentId", item.getDepartmentId());  //修改成部门id by rabbit
            param.put("orgId", manager.getOrgId());
            param.put("checkAmount", false);
            List<PoolIn> poolIns = poolInDao.queryByCompany(param);
            PoolIn poolIn;
            if (poolIns.isEmpty()) {
                throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "找不到进项池");
            } else {
                poolIn = poolIns.get(0);
                PoolInAndOutModifier modifier = new PoolInAndOutModifier();
                modifier.setId(poolIn.getId());
                modifier.setOriginalWeight(poolIn.getTotalWeight().doubleValue());
                modifier.setOriginalAmount(poolIn.getTotalAmount().doubleValue());
                modifier.setChangeAmount(BigDecimal.ZERO.subtract(item.actualSellerAmount()).doubleValue());
                modifier.setChangeWeight(BigDecimal.ZERO.subtract(item.getActualPickWeightServer()).doubleValue());
                modifier.setLastUpdatedBy(user.getLoginId());
                poolInDao.modifyPoolin(modifier);
            }

            param.clear();
            param.put("poolInId", poolIn.getId());
//            param.put("sellerId", item.getSellerId());
            param.put("nsortName", item.getNsortName());
            param.put("material", item.getMaterial());
            param.put("spec", item.getSpec());
            param.put("checkAmount", false);
            List<PoolInDetail> poolInDetails = poolInDetailDao.queryBySellerAndDetails(param);
            PoolInDetail poolInDetail;
            if (poolInDetails.isEmpty()) {
                throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "找不到进项池明细");
            } else {
                poolInDetail = poolInDetails.get(0);
                PoolInAndOutModifier modifier = new PoolInAndOutModifier();
                modifier.setId(poolInDetail.getId());
                modifier.setOriginalWeight(poolInDetail.getTotalWeight().doubleValue());
                modifier.setOriginalAmount(poolInDetail.getTotalAmount().doubleValue());
                modifier.setChangeAmount(BigDecimal.ZERO.subtract(item.actualSellerAmount()).doubleValue());
                modifier.setChangeWeight(BigDecimal.ZERO.subtract(item.getActualPickWeightServer()).doubleValue());
                modifier.setLastUpdatedBy(user.getLoginId());
                poolInDetailDao.modifyPoolinDetail(modifier);
            }


            //销项池主表+明细
            param.clear();
            param.put("departmentId", order.getDepartmentId());  //修改成部门id by rabbit
            param.put("ownerId", buyerManager.getId());
            param.put("orgId", buyerManager.getOrgId());
            param.put("checkAmount", false);
            List<PoolOut> poolOuts = poolOutDao.queryByBuyer(param);
            PoolOut poolOut;
            if (poolOuts.isEmpty()) {
                throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "找不到销项池");
            } else {
                poolOut = poolOuts.get(0);
                PoolInAndOutModifier modifier = new PoolInAndOutModifier();
                modifier.setId(poolOut.getId());
                modifier.setOriginalWeight(poolOut.getTotalWeight().doubleValue());
                modifier.setOriginalAmount(poolOut.getTotalAmount().doubleValue());
                modifier.setChangeAmount(BigDecimal.ZERO.subtract(item.actualBuyerAmount()).doubleValue());
                modifier.setChangeWeight(BigDecimal.ZERO.subtract(item.getActualPickWeightServer()).doubleValue());
                modifier.setLastUpdatedBy(user.getLoginId());
                poolOutDao.modifyPoolOut(modifier);
            }

            param.clear();
            param.put("poolOutId", poolOut.getId());
            param.put("nsortName", item.getNsortName());
            param.put("material", item.getMaterial());
            param.put("spec", item.getSpec());
            param.put("checkAmount", false);
            List<PoolOutDetail> poolOutDetails = poolOutDetailDao.queryByBuyerAndDetails(param);
            PoolOutDetail poolOutDetail;
            if (poolOutDetails.isEmpty()) {
                throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "找不到销项池明细");
            } else {
                poolOutDetail = poolOutDetails.get(0);
                PoolInAndOutModifier modifier = new PoolInAndOutModifier();
                modifier.setId(poolOutDetail.getId());
                modifier.setOriginalWeight(poolOutDetail.getTotalWeight().doubleValue());
                modifier.setOriginalAmount(poolOutDetail.getTotalAmount().doubleValue());
                modifier.setChangeAmount(BigDecimal.ZERO.subtract(item.actualBuyerAmount()).doubleValue());
                modifier.setChangeWeight(BigDecimal.ZERO.subtract(item.getActualPickWeightServer()).doubleValue());
                modifier.setLastUpdatedBy(user.getLoginId());
                poolOutDetailDao.modifyPoolOutDetail(modifier);
            }
        }
    }

    private void rollbackAccountInfo_payed(ConsignOrder order, User user){
        Date date = new Date();
        String orderCode = order.getCode();
        List<PayRequestItems> payItems = payRequestItemsDao.queryByOrderId(order.getId());
        if (payItems.isEmpty()) {
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "找不到付款申请单信息");
        }

        //买家
        accountFundService.updateAccountFund(order.getDepartmentId(), AssociationType.ORDER_CODE, order.getCode(), AccountTransApplyType.BALANCES_BACK,
                order.getTotalContractReletedAmount().subtract(order.getSecondBalanceTakeout()), BigDecimal.ZERO, order.getSecondBalanceTakeout(),
                BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, PayType.BALANCE, user.getId(), user.getName(), new Date());

        //买家调用还款/抵扣接口 add by dengxiyan
        accountFundService.payForCredit(order.getDepartmentId(), AssociationType.ORDER_CODE, order.getCode(), 0l, Constant.SYSTEMNAME, new Date());

        //卖家
        for(PayRequestItems items : payItems) {
            accountFundService.updateAccountFund(items.getReceiverDepartmentId(), AssociationType.ORDER_CODE, order.getCode(), AccountTransApplyType.BALANCES_BACK,
                    BigDecimal.ZERO, BigDecimal.ZERO, items.getPayAmount().add(items.getSecondBalanceTakeout()).negate(), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, PayType.BALANCE, user.getId(), user.getName(), new Date());

            //卖家回退还款信用额度 add by dengxiyan
            if(BigDecimal.ZERO.compareTo(items.getCreditUsedRepay()) != 0){
                accountFundService.updateAccountFund(items.getReceiverDepartmentId(), AssociationType.ORDER_CODE,
                        order.getCode(), AccountTransApplyType.CREDITLIMI_BACK,
                        BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, items.getCreditUsedRepay(), BigDecimal.ZERO
                        , PayType.BALANCE, 0l, Constant.SYSTEMNAME, new Date());
            }

            //卖家调用自动还款/抵扣接口 add by dengxiyan
            accountFundService.payForCredit(items.getReceiverDepartmentId(),AssociationType.ORDER_CODE,order.getCode(),0l,Constant.SYSTEMNAME,new Date());
        }
    }

    private OrderAuditTrail buildOrderAuditTrail(Long orderId, User user) {
        OrderAuditTrail orderAuditTrail = new OrderAuditTrail();
        orderAuditTrail.setSetToStatus(ConsignOrderStatus.INVOICEREQUEST.getCode());
        orderAuditTrail.setStatusType(OrderStatusType.MAIN.toString());
        orderAuditTrail.setOrderId(orderId);
        orderAuditTrail.setSetToStatus(ConsignOrderStatus.INVOICEREQUEST.getCode());
        orderAuditTrail.setStatusType(OrderStatusType.MAIN.toString());
        orderAuditTrail.setOperatorId(user.getId());
        orderAuditTrail.setOperatorName(user.getName());
        orderAuditTrail.setCreated(new Date());
        orderAuditTrail.setCreatedBy(user.getLoginId());
        orderAuditTrail.setLastUpdated(new Date());
        orderAuditTrail.setLastUpdatedBy(user.getLoginId());
        orderAuditTrail.setModificationNumber(0);
        return orderAuditTrail;
    }

    @Override
    public Date queryApprovedDateByOrderId(Long id) {
        return consignOrderDao.queryApprovedDateByOrderId(id);
    }

    /**
     * 双敲信息输入
     *
     * @param itemNodes
     * @param orderId
     * @param right
     * @param user
     * @return
     */
    @Override
    public Map<String, Object> inputInfo(JsonNode itemNodes, Long orderId, String right, User user) {
        Map<String, Object> result = new HashMap<String, Object>();
        int status;
        int success = 0, skip = 0, fail = 0;
        for (JsonNode node : itemNodes) {
            Long itemId = node.path("id").asLong();
            String quantityString = node.path("quantity").asText();
            String weightString = node.path("weight").asText();
            if (StringUtils.isNotBlank(quantityString) && StringUtils.isNotBlank(weightString)) {
                int quantity = Integer.valueOf(quantityString);
                BigDecimal weight = new BigDecimal(weightString);
                status = itemsService.updateOrderItems(itemId, quantity, weight, right, user);
            } else {
                status = 0;
            }
            if (status == 1) {
                success += 1;
            } else if (status == 0) {
                skip += 1;
            } else if (status == 2 || status == -1) {
                fail += 1;
            } else if (status == -2) {
                throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "其他业务员正在录入，数据写入失败，请重新点击录入");
            }
        }

        //更新订单总状态
        if (consignOrderItemsDao.queryCountNotMatch(orderId) == 0) {
            ConsignOrder order = consignOrderDao.queryById(orderId);
            order.setFillupStatus(ConsignOrderFillUpStatus.ALL_MATCHES.getCode());
            order.setLastUpdatedBy(user.getLoginId());
            order.setLastUpdated(new Date());
            order.setModificationNumber(order.getModificationNumber() + 1);
            if (consignOrderDao.updateByPrimaryKeySelective(order) != 1) {
                throw new BusinessException(Constant.EXCEPTIONCODE_UNKONWN, "更新订单状态失败");
            }
        }
        if (fail > 0) {
            String code = consignOrderDao.selectByPrimaryKey(orderId).getCode();
            User processUser = getProcessUser(orderId, user.getId(), ConsignOrderStatus.SECONDSETTLE.getCode(), right);
            SysSetting template=sysSettingService.queryBySettingType(SysSettingType.SmsTemplateTypeInpickup.getCode());
            if (template == null) {
                throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,"双敲不匹配通知短信模板缺失");
            }
            String content =template.getSettingValue();
            content = content.replace("#endcode#", code.substring(code.length() - codeLengthForSMS));
            Nido.start(NidoTaskConstant.SEND_NOTE_MESSAGE, new NoteMessageContext(processUser.getTel(), content));
        }
        result.put("success", true);
        result.put("message", "录入成功" + success + "条，双敲失败" + fail + "条，忽略" + skip + "条。<br/>");
        return result;
    }


    @Override
    public ResultDto auditRefuse(Long orderId, User operator, String note) {
        ResultDto resultDto = new ResultDto();
        //查询订单信息
        ConsignOrder order = consignOrderDao.queryById(orderId);
        if (order == null) {
            resultDto.setSuccess(false);
            resultDto.setMessage("没有找到对应的订单或您没有作此操作的权限！");
            return resultDto;
        }

        if (!ConsignOrderStatus.NEW.getCode().equals(order.getStatus())) {
            resultDto.setSuccess(false);
            resultDto.setMessage("该交易单已处理！");
            return resultDto;
        }
        //订单修改
        ConsignOrderUpdateDto consignOrder = new ConsignOrderUpdateDto();
        consignOrder.setId(orderId);
        consignOrder.setStatus(ConsignOrderStatus.NEWDECLINED.getCode());
        consignOrder.setLastUpdated(new Date());
        consignOrder.setLastUpdatedBy(operator.getName());

        List<String> oldStatusList = new ArrayList<>();
        oldStatusList.add(ConsignOrderStatus.NEW.getCode());
        consignOrder.setOldStatusList(oldStatusList);

        //订单状态
        OrderAuditTrail orderAuditTrail = new OrderAuditTrail();
        orderAuditTrail.setOrderId(orderId);
        orderAuditTrail.setOperatorId(operator.getId());
        orderAuditTrail.setOperatorName(operator.getName());
        orderAuditTrail.setLastUpdated(new Date());
        orderAuditTrail.setLastUpdatedBy(operator.getName());
        orderAuditTrail.setSetToStatus(ConsignOrderStatus.NEWDECLINED.getCode());
        orderAuditTrail.setStatusType(OrderStatusType.MAIN.toString());
        orderAuditTrail.setCreated(new Date());
        orderAuditTrail.setCreatedBy(operator.getName());
        orderAuditTrail.setModificationNumber(0);
        orderAuditTrail.setExt3(note);//add by dengxiyan不通过理由 统一用ext3字段存关闭理由
        if (consignOrderDao.updateByPrimaryKeySelective(consignOrder) > 0) {
            if (orderStatusDao.insertOrderAuditTrail(orderAuditTrail) > 0) {
                resultDto.setSuccess(true);
                resultDto.setMessage("审核成功，订单已关闭！");

                //短信通知
                User trader = userDao.queryById(order.getOwnerId()); //获取交易员
                String endCode = order.getCode().substring(order.getCode().length() - 6, order.getCode().length());
                SysSetting template=sysSettingService.queryBySettingType(SysSettingType.SmsTemplateOrderRefuse.getCode());
                if (template == null) {
                    throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,"订单审核不通过通知交易员短信模板缺失");
                }
                String content =template.getSettingValue();
                content = content.replace("#companyname#", order.getAccountName());
                content = content.replace("#endcode#", endCode);
                content = content.replace("#tradername#", operator.getName());
                Nido.start(NidoTaskConstant.SEND_NOTE_MESSAGE, new NoteMessageContext(trader.getTel(), content));
                
                String[] pushInfo = orderCacheService.getPushInfoByUserLoginId(trader.getLoginId());
                if(pushInfo!=null){
                	content = String.format("%s交易单未通过审核，请联系%s。", order.getAccountName(), operator.getName());
                    appPushService.sendPushNoticfication(pushInfo[0],pushInfo[1], AppNoticeType.ApproveDeclined.getName(),content);
                    //ADD BY LIXING 订单状态变动时推送消息至钢为掌柜app 已关闭
//                    appPushService.sendPushNoticfication(pushInfo[0],pushInfo[1], COLSED, COLSED);
                }
            } else {
                throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "审核订单不通过失败！");
            }
        } else {
            resultDto.setSuccess(false);
            resultDto.setMessage("该交易单已处理！");
        }
        return resultDto;
    }


    public ResultDto relateOrder(Long orderId, Boolean takeoutSecondBalance, User operator, String orderItemsList,Boolean takeoutCreditBalance) {
        ResultDto resultDto = new ResultDto();

        // 查询订单信息
        ConsignOrder order = consignOrderDao.queryById(orderId);
        if (order == null) {
            resultDto.setSuccess(false);
            resultDto.setMessage("没有找到对应的订单！");
            return resultDto;
        }
        if (!ConsignOrderStatus.NEWAPPROVED.getCode().equals(
                order.getStatus())) {
            resultDto.setSuccess(false);
            resultDto.setMessage("该交易单已处理！");
            return resultDto;
        }
        BigDecimal contactReletedAmount = order.getTotalAmount();
        Account account = accountDao.selectByPrimaryKey(order.getDepartmentId());  //这里应该用部门id edit by rabbit

        BigDecimal usableAmount = account.getBalance();
        if (takeoutSecondBalance) {
            usableAmount = usableAmount.add(account.getBalanceSecondSettlement());
        }

        //勾选可信用额度且资金账户余额不足且可用信用额度为正时使用信用额度
        Boolean isUseCredit = Boolean.FALSE;//是否使用信用额度 界面勾选且资金不足且可用额度大于0
        BigDecimal useCreditAmount = BigDecimal.ZERO;//使用信用额度值
        BigDecimal balanceCreditAmount = calculateBalanceCreditAmountOfDeptByDeptId(order.getDepartmentId());
        if (takeoutCreditBalance && usableAmount.compareTo(contactReletedAmount) < 0
                && balanceCreditAmount.compareTo(BigDecimal.ZERO) > 0){
            isUseCredit = Boolean.TRUE;
            BigDecimal needUseCreditAmount = contactReletedAmount.subtract(usableAmount);//计算订单金额还需使用信用额度值
            useCreditAmount = needUseCreditAmount.compareTo(balanceCreditAmount) > 0 ? balanceCreditAmount : needUseCreditAmount;
            usableAmount = usableAmount.add(useCreditAmount);
        }
        SysSetting sysSetting = sysSettingService.queryBySettingType(SysSettingType.BuyerAllowAmountTolerance.getCode());
        Double allowance = Double.parseDouble(sysSetting.getSettingValue());
        double diffAmount = order.getTotalAmount().subtract(usableAmount).doubleValue();
        if (diffAmount > 0) {
            if (diffAmount <= allowance) {
                contactReletedAmount = usableAmount;
            } else {
                resultDto.setSuccess(false);
                resultDto.setMessage("可用金额小于订单金额，不能关联");
                return resultDto;
            }
        }
        //订单修改
        ConsignOrderUpdateDto consignOrder = new ConsignOrderUpdateDto();
        consignOrder.setId(orderId);
        consignOrder.setStatus(ConsignOrderStatus.RELATED.getCode());
        consignOrder.setLastUpdated(new Date());
        consignOrder.setLastUpdatedBy(operator.getName());

        List<String> oldStatusList = new ArrayList<>();
        oldStatusList.add(ConsignOrderStatus.NEWAPPROVED.getCode());
        consignOrder.setOldStatusList(oldStatusList);
        consignOrder.setTotalContractReletedAmount(contactReletedAmount);//合同关联金额
        consignOrder.setSecondBalanceTakeout(takeoutSecondBalance ? account.getBalanceSecondSettlement() : BigDecimal.ZERO);
        consignOrder.setAlterStatus(ConsignOrderAlterStatus.PENDING_CHANGED.getCode());//关联后的订单就可以，合同变更，这里把状态改成待变更 add by wangxianjun
        //订单状态
        OrderAuditTrail orderAuditTrail = new OrderAuditTrail();
        orderAuditTrail.setOrderId(orderId);
        orderAuditTrail.setOperatorId(operator.getId());
        orderAuditTrail.setOperatorName(operator.getName());
        orderAuditTrail.setLastUpdated(new Date());
        orderAuditTrail.setLastUpdatedBy(operator.getName());
        orderAuditTrail.setSetToStatus(ConsignOrderStatus.RELATED.getCode());
        orderAuditTrail.setStatusType(OrderStatusType.MAIN.toString());
        orderAuditTrail.setCreated(new Date());
        orderAuditTrail.setCreatedBy(operator.getName());
        orderAuditTrail.setModificationNumber(0);

        //银票票号列表
        List<ConsignOrderItems> acceptDraftList = jsonToOrderItems(orderItemsList);
        if(acceptDraftList != null && acceptDraftList.size() > 0){
            if(consignOrderItemsDao.batchUpdateAcceptDraf(acceptDraftList) == 0)
                throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "银票票号更新失败！");
        }
        Result result;
        if (consignOrderDao.updateByConditionSelective(consignOrder) > 0) {
            if (orderStatusDao.insertOrderAuditTrail(orderAuditTrail) > 0) {
                if (takeoutSecondBalance && account.getBalanceSecondSettlement().compareTo(BigDecimal.ZERO) > 0) { //二结转入资金账户
                    accountFundService.updateAccountFund(order.getDepartmentId(), AssociationType.ORDER_CODE, order.getCode(),
                            AccountTransApplyType.INTO_THE_CAPITAL_ACCOUNT,
                            account.getBalanceSecondSettlement(), BigDecimal.ZERO, BigDecimal.ZERO.subtract(account.getBalanceSecondSettlement()),
                            BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, PayType.BALANCE, operator.getId(),
                            operator.getName(), new Date());
                }
                //如果勾选可用信用额度且使用了信用额度，就先做信用额度金额划转到资金账户
                if (isUseCredit) {
                    //调用接口增加最后的判断：可使用的信用额度够不够
                    balanceCreditAmount = calculateBalanceCreditAmountOfDeptByDeptId(order.getDepartmentId());
                    if (useCreditAmount.compareTo(balanceCreditAmount) > 0) {
                        throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "可用信用额度值已改变，不够关联订单，请刷新!");
                    }
                    if (useCreditAmount.compareTo(BigDecimal.ZERO) > 0) {
                        accountFundService.updateAccountFund(order.getDepartmentId(), AssociationType.ORDER_CODE, order.getCode(),
                                AccountTransApplyType.CREDITLIMIT_TRANSTO_ACCOUNT,
                                useCreditAmount, BigDecimal.ZERO, BigDecimal.ZERO,
                                BigDecimal.ZERO, useCreditAmount, BigDecimal.ZERO, PayType.BALANCE, operator.getId(),
                                operator.getName(), new Date());    //信用额度金额划转到资金账户
                    }
                }
                //合同金额冻结
                accountFundService.updateAccountFund(order.getDepartmentId(), AssociationType.ORDER_CODE, order.getCode(),
                        AccountTransApplyType.BALANCES_LOCK, BigDecimal.ZERO.subtract(contactReletedAmount),
                        contactReletedAmount, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, PayType.BALANCE, operator.getId(), operator.getName(), new Date()); //冻结资金账户余额
                //资金还款二结
                if (takeoutSecondBalance && account.getBalanceSecondSettlement().compareTo(BigDecimal.ZERO) < 0) {
                    accountFundService.updateAccountFund(order.getDepartmentId(), AssociationType.ORDER_CODE, order.getCode(),
                            AccountTransApplyType.SECONDARY_SETTLEMENT_ACCOUNT_BALANCES,
                            account.getBalanceSecondSettlement(), BigDecimal.ZERO, BigDecimal.ZERO.subtract(account.getBalanceSecondSettlement()),
                            BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, PayType.BALANCE, operator.getId(),
                            operator.getName(), new Date());
                }
            } else {
                throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "关联失败！");
            }
            //将金额变化，添加到report_buyer_invoice_out
            reportFinaceService.pushToReportInvoiceOut(order, operator, contactReletedAmount, ReportBuyerInvoiceOutType.Approved, null);
            BigDecimal costAmount = new BigDecimal(0);//采购金额
            String seller_name = "";
            List<ConsignOrderItems>  orderItems = consignOrderItemsDao.selectByOrderId(orderId);
            for(ConsignOrderItems item:orderItems){
            		 costAmount = costAmount.add(item.getCostAmount());
            }
            seller_name = orderItems.get(0).getSellerName();
            Account sellerAccount = accountDao.selectByPrimaryKey(orderItems.get(0).getSellerId());
            //如果有卖家支持银票预付发送短信通知
            if("1".equals(sellerAccount.getPaymentLabel())){
            	try{
            		List<ConsignOrderProcess> orderProcessList = consignOrderProcessDao.getRelateProcessByUserId(operator.getId());
            	    String content = "银票预付" + Tools.dateToStr(new Date(), "yyyy年MM月dd日 HH:mm:ss")+"，交易单号："+order.getCode()+"（" +seller_name +"） 订单金额：" +costAmount+ "订单状态：已关联合同";
	            	 for(ConsignOrderProcess orderProcess : orderProcessList){
	            		 Nido.start(NidoTaskConstant.SEND_NOTE_MESSAGE, new NoteMessageContext(orderProcess.getOperatorMobile(), content));
	            	 }
	            	 resultDto.setSuccess(true);
	                 resultDto.setMessage("合同关联成功,已短信通知金融服务部");
            	}catch(Exception e){}
            }
        } else {
            resultDto.setSuccess(false);
            resultDto.setMessage("该交易单已处理！");
        }
        //暂时禁用，待代码合并后启用
        List<ConsignOrderItems>  orderItems = consignOrderItemsDao.selectByOrderId(orderId);
        // 关联成功后 需要推送的信息包括：卖家、品名、材质、规格、钢厂、所在城市、仓库、计重方式、件数、重量、单价、更新时间等到智能找货 add afeng
        if(orderItems!=null && !orderItems.isEmpty()){
        	List<Map<Object,Object>> params = new ArrayList<Map<Object,Object>>();
	        for(ConsignOrderItems citem : orderItems){
	        	Map<Object,Object> item = new HashMap<Object,Object>();
	        	item.put("accountId", citem.getSellerId());
	        	item.put("accountName", citem.getSellerName());
	        	item.put("categoryName", citem.getNsortName());
	        	item.put("factoryName", citem.getFactory());
	        	item.put("materialName", citem.getMaterial());
	        	item.put("spec", citem.getSpec());
	        	item.put("weightConcept", citem.getWeightConcept());
	        	item.put("price", citem.getDealPrice());
	        	item.put("warehouseName", citem.getWarehouse());	
	        	item.put("quantity", citem.getQuantity());
	        	item.put("cityName", citem.getCity());
	        	item.put("weight", citem.getWeight());
	        	item.put("lastUpdated", new Date());
	        	item.put("userIds", operator.getId());
	        	item.put("lastUpdatedBy", operator.getLoginId());
	        	params.add(item);
	        }
	        //调用rest接口
	        Result fruit = new Result();
	        try {
	        	fruit = restSmartmatchService.saveOrderHistoryResource(params);
			} catch (Exception e) {
				logger.error("--------调用接口restSmartmatchService异常:" + fruit.getData().toString(), e);
			}
	        if (!fruit.isSuccess()) {
	        	logger.error("--------保存到资源表失败:" + fruit.getData().toString());
	        }
        }
        //ADD BY LIXING 订单状态变动时推送消息至钢为掌柜app 待结算
//        Organization organization = organizationDao.queryById(operator.getOrgId());
//        User manageUser = userDao.queryById(organization.getCharger());
//        if (manageUser != null) {
//        	String[] pushInfo = orderCacheService.getPushInfoByUserLoginId(manageUser.getLoginId());
//        	if(pushInfo!=null){
//        		appPushService.sendPushNoticfication(pushInfo[0],pushInfo[1], PENDING_SETTLEMENT, PENDING_SETTLEMENT);
//        	}
//        }
        resultDto.setSuccess(true);
        return resultDto;
    }
    /**
     * String转二进制
     * @author afeng
     * @param str
     * @return
     */
    @Override
    public String toBinary (String str) {
    	char[] strChar= str.toCharArray();
		String result = "";
		for (int i = 0; i < strChar.length; i++) {
			result += Integer.toBinaryString(strChar[i]) + ",";
		}
		return result;
    }
    
    /**
     * @param orderId
     * @param bankAccount map<卖家ID,银行账号信息>
     * @param applyMoney  map<卖家ID,申请付款金额> 用于对比数据库金额是否有变动
     * @param operator
     * @param jsonIsDelayPay 是否提货后3日内付款 true 为是 ， 空为否
     * @return
     */
    @Override
    public ResultDto applyPay(Long orderId, Map<Long, Long> bankAccount, Map<Long, Double> applyMoney, Map<Long, Boolean> checkedMap,
                              Map<Long, Boolean> refundCredit, User operator, String jsonIsDelayPay) {
        ResultDto resultDto = new ResultDto();
        // lixiang 因生成收款人付款单号将总金额先合计先增加主表数据，再一条条增加付款申请单详情表的数据
        BigDecimal applyMoneyTotalMoney = BigDecimal.ZERO;
        Double totalApplyMoey = 0D;
        for (Object moneys : applyMoney.keySet()) {
			Double money = applyMoney.get(moneys);
			totalApplyMoey += money;
		}
        applyMoneyTotalMoney =new BigDecimal(totalApplyMoey);
        ConsignOrder order = consignOrderDao.queryById(orderId);
        if (order == null) {
            resultDto.setSuccess(false);
            resultDto.setMessage("申请付款失败，没有找到该订单信息！");
            return resultDto;
        }
        if (!order.getPayStatus().equals(ConsignOrderPayStatus.APPLY.toString()) ||
                (!order.getStatus().equals(ConsignOrderStatus.RELATED.getCode()) &&
                        !order.getStatus().equals(ConsignOrderStatus.SECONDSETTLE.getCode()))) {
            resultDto.setSuccess(false);
            resultDto.setMessage("该交易单已处理！");
            return resultDto;
        }
        List<ConsignOrderSellerInfoDto> sellerInfoList = consignOrderItemsDao.getSellerInfo(orderId);
        if (sellerInfoList == null || sellerInfoList.size() <= 0) {
            resultDto.setSuccess(false);
            resultDto.setMessage("申请付款失败，没有找到相应的卖家信息！");
            return resultDto;
        }
        //modify by wangxj 校验是否上传合同在弹出确认付款信息前
        /*int unUploadCount = consignOrderContractDao.countUnUploadByOrderId(orderId);
        if (unUploadCount > 0) {
            resultDto.setSuccess(false);
            resultDto.setMessage("申请付款失败，请先上传卖家合同！");
            return resultDto;
        }*/
        for (ConsignOrderSellerInfoDto seller : sellerInfoList) {
            if (!bankAccount.containsKey(seller.getSellerDepartmentId())) {
                resultDto.setSuccess(false);
                resultDto.setMessage("申请付款失败，请正确选择银行账号信息！");
                return resultDto;
            }
            if (!applyMoney.containsKey(seller.getSellerDepartmentId())) { //。。 || applyMoney.get(seller.getSellerId()) <= 0 申请付款金额为负数或0时不做控制
                resultDto.setSuccess(false);
                resultDto.setMessage("申请付款失败，请正确填写付款金额！");
                return resultDto;
            }
            if (applyMoney.get(seller.getSellerDepartmentId()) > seller.getContractAmount().doubleValue()) {
                resultDto.setSuccess(false);
                resultDto.setMessage("申请付款失败，申请付款金额大于应付金额！");
                return resultDto;
            }

            //计算最大可申请金额
            BigDecimal contractAmount = seller.getContractAmount();// 合同金额
            if (checkedMap.get(seller.getSellerDepartmentId())) {
                if (seller.getBalanceSecondSettlement().doubleValue() < 0) {
                    contractAmount = contractAmount.add(seller.getBalanceSecondSettlement());
                }
            }
            if (refundCredit.get(seller.getSellerDepartmentId())) {
                if (seller.getCreditUsed().compareTo(BigDecimal.ZERO) > 0) {
                    contractAmount = contractAmount.subtract(seller.getCreditUsed());
                }
            }
            if (contractAmount.compareTo(BigDecimal.ZERO) < 0) {
                contractAmount = BigDecimal.ZERO;
            }
            if (applyMoney.get(seller.getSellerDepartmentId()) > contractAmount.doubleValue()) {
                resultDto.setSuccess(false);
                resultDto.setMessage("申请付款失败，申请付款金额大于应付金额！");
                return resultDto;
            }
        }
        String code = payRequestService.createCode();
        //还款
        PayRequest payRequest = new PayRequest();
        payRequest.setConsignOrderId(order.getId());
        payRequest.setConsignOrderCode(order.getCode());
        payRequest.setContractCode(order.getContractCode());
        payRequest.setRequesterId(operator.getId());
        payRequest.setRequesterName(operator.getName());
        payRequest.setOrgId(operator.getOrgId());
        //payRequest.setOrgName(operator.getorgName());
        payRequest.setPayStatus(ConsignOrderPayStatus.REQUESTED.toString());// add by lixiang 货币资金报表专用
        payRequest.setCode(code);
        payRequest.setBuyerId(order.getAccountId());
        payRequest.setBuyerName(order.getAccountName());
        payRequest.setTotalAmount(applyMoneyTotalMoney);
        payRequest.setCreated(new Date());
        payRequest.setCreatedBy(operator.getName());
        payRequest.setType(PayRequestType.PAYMENT.getCode());
        payRequest.setStatus(PayStatus.REQUESTED.toString());
        payRequest.setLastUpdatedBy(operator.getName());
        payRequest.setLastUpdated(new Date());
        payRequest.setModificationNumber(0);
        payRequest.setPrintTimes(0);
        //增加部门信息
        payRequest.setDepartmentId(order.getDepartmentId());
        payRequest.setDepartmentName(order.getDepartmentName());

        payRequestDao.insertSelective(payRequest);

        long requestId = payRequest.getId();
        
        List<PayRequestItems> forInsertList = new ArrayList<PayRequestItems>();
        for (ConsignOrderSellerInfoDto item : sellerInfoList) {
            BigDecimal second_balance_takeout = BigDecimal.ZERO;
            BigDecimal credit_refund_amount = BigDecimal.ZERO;
            BigDecimal applyMoney_item = BigDecimal.ZERO;
            BigDecimal contractAmount = item.getContractAmount();// 合同金额

            //选择抵扣二次结算账户余额
            if (checkedMap.get(item.getSellerDepartmentId())) {
                if (item.getBalanceSecondSettlement().doubleValue() < 0) {
                    Double balanceSecondSettlement = item.getBalanceSecondSettlement().doubleValue();// 抵扣二次结算金额
                    //如果合同金额小于二次结算余额正数则用合同金额
                    if (Math.abs(contractAmount.doubleValue()) < Math.abs(balanceSecondSettlement))
                        second_balance_takeout = item.getContractAmount();
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
            if (contractAmount.compareTo(BigDecimal.ZERO) > 0
                    && refundCredit.get(item.getSellerDepartmentId())
                    && item.getCreditUsed().doubleValue() > 0) {
                credit_refund_amount = contractAmount.compareTo(item.getCreditUsed()) < 0 ? contractAmount : item.getCreditUsed();
                contractAmount=contractAmount.subtract(credit_refund_amount);
                accountFundService.updateAccountFund(item.getSellerDepartmentId(), AssociationType.ORDER_CODE, order.getCode(),
                        AccountTransApplyType.CREDITLIMI_TREPAYMENT, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                        BigDecimal.ZERO, credit_refund_amount.negate(), BigDecimal.ZERO, PayType.BALANCE, 0l, Constant.SYSTEMNAME, new Date());
            }
            //applyMoneyTotalMoney = applyMoneyTotalMoney.add(new BigDecimal(applyMoney.get(item.getSellerId())));
            applyMoney_item = new BigDecimal(applyMoney.get(item.getSellerDepartmentId())); //item.getOrderAmount(); 付款金额应该为申请付款金额不是合同金额
            if (contractAmount.compareTo(BigDecimal.ZERO) <= 0) {
                applyMoney_item = BigDecimal.ZERO;
            }
            //确认已付款卖家付款申请冻结不需要
            /*
            orderStatusService.updatePayment(item.getSellerId(), order.getCode(), "", applyMoney_item.doubleValue(), BigDecimal.ZERO,
                    operator.getId(), operator.getName(), new Date(), Type.SELLER_PAYMENT_APPLY.getCode());
            */

            PayRequestItems payRequestItems = new PayRequestItems();
            // 增加收款方付款申请单号 lixiang
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
            forInsertList.add(payRequestItems);
            payRequestItems.setRequestId(requestId);
            //添加部门信息
            payRequestItems.setReceiverDepartmentId(item.getSellerDepartmentId());
            payRequestItems.setReceiverDepartmentName(item.getSellerDepartmentName());
            payRequestItemsDao.insertSelective(payRequestItems);
        }

        ConsignOrderUpdateDto updateOrder = new ConsignOrderUpdateDto();
        updateOrder.setId(orderId);
        updateOrder.setCallBackStatus(ConsignOrderCallBackStatus.Normal.getCode());//打回状态修改为正常
        updateOrder.setCallBackNote("");//打回理由修改为空
        updateOrder.setLastUpdated(new Date());
        updateOrder.setLastUpdatedBy(operator.getName());
        updateOrder.setPayStatus(ConsignOrderPayStatus.REQUESTED.toString());
        updateOrder.setOldPayStatus(ConsignOrderPayStatus.APPLY.toString());
        //当是否提货后3日内付款为是时，该笔交易不计算业绩，0 不计算，1计算
        if("true".equals(jsonIsDelayPay)){
            updateOrder.setIsCountAchievement("0");
        }else{
            updateOrder.setIsCountAchievement("1");
        }

        //订单状态
        OrderAuditTrail orderAuditTrail = new OrderAuditTrail();
        orderAuditTrail.setOrderId(orderId);
        orderAuditTrail.setOperatorId(operator.getId());
        orderAuditTrail.setOperatorName(operator.getName());
        orderAuditTrail.setLastUpdated(new Date());
        orderAuditTrail.setLastUpdatedBy(operator.getName());
        orderAuditTrail.setSetToStatus(ConsignOrderPayStatus.REQUESTED.toString());
        orderAuditTrail.setStatusType(OrderStatusType.PAY.toString());
        orderAuditTrail.setCreated(new Date());
        orderAuditTrail.setCreatedBy(operator.getName());
        orderAuditTrail.setModificationNumber(0);
        if (consignOrderDao.updateByConditionSelective(updateOrder) > 0) {
            if (orderStatusDao.insertOrderAuditTrail(orderAuditTrail) > 0) {
                resultDto.setSuccess(true);
                resultDto.setMessage("申请付款成功！");

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
            } else {
                throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "日志记录未成功，申请付款失败!");
            }
        } else {
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "该交易单已处理！");
        }
        return resultDto;
    }

    /**
     * 审核付款通过
     *
     * @param orderId
     * @param operator
     * @return
     */
    @Override
    public ResultDto auditPayAccept(Long orderId, Long payRequestId, User operator) {
        ResultDto resultDto = new ResultDto();
        ConsignOrder order = consignOrderDao.queryById(orderId);
        if (order == null) {
            resultDto.setSuccess(false);
            resultDto.setMessage("没有找到对应的订单！");
            return resultDto;
        }

        if (!PayStatus.REQUESTED.toString().equals(order.getPayStatus())) {
            resultDto.setSuccess(false);
            resultDto.setMessage("该交易单已处理！");
            return resultDto;
        }

        PayRequest request = payRequestDao.selectByPrimaryKey(payRequestId);
        if (request == null) {
            resultDto.setSuccess(false);
            resultDto.setMessage("未找到对应的付款申请！");
            return resultDto;
        }
        //add by wangxianjun 防止并发
        if(!PayStatus.REQUESTED.toString().equals(request.getStatus())){
            resultDto.setSuccess(false);
            resultDto.setMessage("该付款申请单已处理！");
            return resultDto;
        }

        ConsignOrderUpdateDto consignOrder = new ConsignOrderUpdateDto();
        consignOrder.setId(orderId);
        consignOrder.setPayStatus(ConsignOrderPayStatus.APPROVED.toString());
        consignOrder.setLastUpdated(new Date());
        consignOrder.setLastUpdatedBy(operator.getName());
        consignOrder.setOldPayStatus(ConsignOrderPayStatus.REQUESTED.toString());
        //申请付款来自合同变更流程 把状态修改成PENDING_PRINT_PAY待打印付款申请单 add by wangxianjun
        if(ConsignOrderAlterStatus.PENDING_APPR_PAY.getCode().equals(order.getAlterStatus())){
            consignOrder.setAlterStatus(ConsignOrderAlterStatus.PENDING_PRINT_PAY.getCode());
        }

        OrderAuditTrail orderAuditTrail = new OrderAuditTrail();
        orderAuditTrail.setLastUpdatedBy(operator.getName());
        orderAuditTrail.setLastUpdated(new Date());
        orderAuditTrail.setOrderId(orderId);
        orderAuditTrail.setOperatorId(operator.getId());
        orderAuditTrail.setOperatorName(operator.getName());
        orderAuditTrail.setSetToStatus(ConsignOrderPayStatus.APPROVED.toString());
        orderAuditTrail.setStatusType(OrderStatusType.PAY.toString());
        orderAuditTrail.setCreated(new Date());
        orderAuditTrail.setCreatedBy(operator.getName());
        orderAuditTrail.setModificationNumber(0);

        PayRequest payRequest = new PayRequest();
        payRequest.setId(payRequestId);
        payRequest.setStatus(PayStatus.APPROVED.toString());
        payRequest.setLastUpdatedBy(operator.getName());
        payRequest.setLastUpdated(new Date());

        if (consignOrderDao.updateByConditionSelective(consignOrder) > 0) {
            if (orderStatusDao.insertOrderAuditTrail(orderAuditTrail) > 0
                    && payRequestDao.updateByPrimaryKeySelective(payRequest) > 0) {
                //申请付款来自合同变更流程 把状态修改成PENDING_PRINT_PAY待打印付款申请单 add by wangxianjun
                if(ConsignOrderAlterStatus.PENDING_APPR_PAY.getCode().equals(order.getAlterStatus())){
                    ConsignOrderChange consignOrderChange = consignOrderChangeService.selectByPrimaryKey(order.getChangeOrderId());
                    if (consignOrderChange == null) {
                        resultDto.setSuccess(false);
                        resultDto.setMessage("没有找到对应的变更订单！");
                        return resultDto;
                    }
                    //修改合同变更表的状态，并且添加对应的变更记录
                    consignOrderChangeService.updateChangeStatus(consignOrderChange, ConsignOrderAlterStatus.PENDING_PRINT_PAY.getCode(), "", operator);

                }
                resultDto.setSuccess(true);
                resultDto.setMessage("审核成功！");

                //短信通知
                //获取核算会计
                List<ConsignOrderStatusDto> consignOrderStatusDtoList = consignOrderProcessDao.getOrderProcessByUserId(order.getOwnerId());
                ConsignOrderStatusDto finance = null;
                for (ConsignOrderStatusDto item : consignOrderStatusDtoList) {
                    if (item.getRoleName().equals("核算会计")) {
                        finance = item;
                        break;
                    }
                }
                if (finance != null) {
                    DecimalFormat format = new DecimalFormat("##,###,###,###,###.00");
                    String money = format.format(request.getTotalAmount().doubleValue());
                    String endCode = order.getCode().substring(order.getCode().length() - 6, order.getCode().length());
                    SysSetting template=sysSettingService.queryBySettingType(SysSettingType.SmsTemplatePayAccept.getCode());
                    if (template == null) {
                        throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,"审核付款通过通知财务确认短信模板缺失");
                    }
                    String content =template.getSettingValue();
                    content = content.replace("#tradername#", order.getOwnerName());
                    content = content.replace("#endcode#", endCode);
                    content = content.replace("#payamount#", money);
                    Nido.start(NidoTaskConstant.SEND_NOTE_MESSAGE, new NoteMessageContext(finance.getMobile(), content));
                } else {
                    logger.info("付款申请通过，未找到核算会计，短信未发送，订单号" + order.getId());
                }
            } else {
                throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,"审核失败");
            }
        } else {
            resultDto.setSuccess(false);
            resultDto.setMessage("该交易单已处理！");
        }
        return resultDto;
    }

    /**
     * 审核付款不通过
     *
     * @param orderId
     * @param operator
     * @return
     */
    @Override
    public ResultDto auditPayRefuse(Long orderId, Long payRequestId, User operator, String note) {
        ResultDto resultDto = new ResultDto();
        ConsignOrder order = consignOrderDao.queryById(orderId);
        if (order == null) {
            resultDto.setSuccess(false);
            resultDto.setMessage("没有找到对应的订单！");
            return resultDto;
        }

        if (!PayStatus.REQUESTED.toString().equals(order.getPayStatus())) {
            resultDto.setSuccess(false);
            resultDto.setMessage("该交易单已处理！");
            return resultDto;
        }
        PayRequest request = payRequestDao.selectByPrimaryKey(payRequestId);
        if (request == null) {
            resultDto.setSuccess(false);
            resultDto.setMessage("未找到对应的付款申请！");
            return resultDto;
        }
        //add by wangxianjun 防止并发
        if(!PayStatus.REQUESTED.toString().equals(request.getStatus())){
            resultDto.setSuccess(false);
            resultDto.setMessage("该付款申请单已处理！");
            return resultDto;
        }
        ConsignOrderUpdateDto consignOrder = new ConsignOrderUpdateDto();
        consignOrder.setId(orderId);
        consignOrder.setPayStatus(ConsignOrderPayStatus.APPLY.toString());
        consignOrder.setLastUpdated(new Date());
        consignOrder.setLastUpdatedBy(operator.getName());
        //如果该笔订单是在提货后3日内付款的，不计算提成，当审核不通过时，需要把是否计算提成标志改成“是”
        consignOrder.setIsCountAchievement("1");
        //申请付款来自合同变更流程 把状态修改成付款申请审核不通过 add by wangxianjun
        if(ConsignOrderAlterStatus.PENDING_APPR_PAY.getCode().equals(order.getAlterStatus())){
            consignOrder.setAlterStatus(ConsignOrderAlterStatus.PAYED_DISAPPROVE.getCode());
        }

        OrderAuditTrail orderAuditTrail = new OrderAuditTrail();
        orderAuditTrail.setLastUpdatedBy(operator.getName());
        orderAuditTrail.setLastUpdated(new Date());
        orderAuditTrail.setOrderId(orderId);
        orderAuditTrail.setOperatorId(operator.getId());
        orderAuditTrail.setOperatorName(operator.getName());
        orderAuditTrail.setSetToStatus(ConsignOrderPayStatus.APPLY.toString());
        orderAuditTrail.setStatusType(OrderStatusType.PAY.toString());
        orderAuditTrail.setCreated(new Date());
        orderAuditTrail.setCreatedBy(operator.getName());
        orderAuditTrail.setModificationNumber(0);
        orderAuditTrail.setNote(note);//审核不通过理由

        PayRequest payRequest = new PayRequest();
        payRequest.setId(payRequestId);
        payRequest.setStatus(PayStatus.DECLINED.toString());
        payRequest.setLastUpdatedBy(operator.getName());
        payRequest.setLastUpdated(new Date());

        List<PayRequestItems> payRequestItemses = payRequestItemsDao.selectByRequestId(payRequestId);

        if (consignOrderDao.updateByPrimaryKeySelective(consignOrder) > 0
                && orderStatusDao.insertOrderAuditTrail(orderAuditTrail) > 0
                && payRequestDao.updateByPrimaryKeySelective(payRequest) > 0) {
            //申请付款来自合同变更流程 把状态修改成PENDING_PRINT_PAY待打印付款申请单 add by wangxianjun
            if(ConsignOrderAlterStatus.PENDING_APPR_PAY.getCode().equals(order.getAlterStatus())){
                ConsignOrderChange consignOrderChange = consignOrderChangeService.selectByPrimaryKey(order.getChangeOrderId());
                if (consignOrderChange == null) {
                    resultDto.setSuccess(false);
                    resultDto.setMessage("没有找到对应的变更订单！");
                    return resultDto;
                }
                //修改合同变更表的状态，并且添加对应的变更记录
                consignOrderChangeService.updateChangeStatus(consignOrderChange, ConsignOrderAlterStatus.PAYED_DISAPPROVE.getCode(), note, operator);
            }
            {
                for (PayRequestItems item : payRequestItemses) {
                    if (item.getSecondBalanceTakeout().doubleValue() != 0) {
//                        orderStatusService.updatePayment(item.getReceiverId(), order.getCode(), "", item.getSecondBalanceTakeout(),
//                                BigDecimal.ZERO, operator.getId(), operator.getName(), new Date(), Type.SECONDARY_SETTLEMENT_DIKB.getCode());
                        accountFundService.updateAccountFund(item.getReceiverDepartmentId(), AssociationType.ORDER_CODE, order.getCode(),
                                AccountTransApplyType.SECONDARY_SETTLEMENT_UNLOCK, BigDecimal.ZERO, BigDecimal.ZERO, item.getSecondBalanceTakeout().negate(),
                                item.getSecondBalanceTakeout(), BigDecimal.ZERO, BigDecimal.ZERO, PayType.BALANCE, operator.getId(), operator.getName(), new Date());  //解锁
                    }
                    if(item.getCreditUsedRepay().compareTo(BigDecimal.ZERO)>0){
                        accountFundService.updateAccountFund(item.getReceiverDepartmentId(), AssociationType.ORDER_CODE, order.getCode(),
                                AccountTransApplyType.CREDITLIMI_BACK, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                                BigDecimal.ZERO, item.getCreditUsedRepay(), BigDecimal.ZERO, PayType.BALANCE, 0l, Constant.SYSTEMNAME, new Date());
                    }
                    //确认已付款卖家付款申请解冻不需要
                    /*
                    orderStatusService.updatePayment(item.getReceiverId(), order.getCode(), "", item.getPayAmount().doubleValue(), BigDecimal.ZERO,
                            operator.getId(), operator.getName(), new Date(), Type.SELLER_PAYMENT_THAW.getCode());
                    */
                }
                resultDto.setSuccess(true);
                resultDto.setMessage("审核成功！");

                //短信通知
                User trader = userDao.queryById(order.getOwnerId()); //获取交易员
                String endCode = order.getCode().substring(order.getCode().length() - 6, order.getCode().length());
                SysSetting template=sysSettingService.queryBySettingType(SysSettingType.SmsTemplateRefusePay.getCode());
                if (template == null) {
                    throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,"审核付款不通过通知申请者短信模板缺失");
                }
                String content =template.getSettingValue();
                content = content.replace("#companyname#", order.getAccountName());
                content = content.replace("#endcode#", endCode);
                content = content.replace("#managername#", operator.getName());
                Nido.start(NidoTaskConstant.SEND_NOTE_MESSAGE, new NoteMessageContext(trader.getTel(), content));
                
                String[] pushInfo = orderCacheService.getPushInfoByUserLoginId(trader.getLoginId());
                if(pushInfo!=null){
                	content = String.format("%s交易单付款申请未通过审核，请联系%s。", order.getAccountName(), operator.getName());
                    appPushService.sendPushNoticfication(pushInfo[0],pushInfo[1], AppNoticeType.PayApplyDeclined.getName(),content);
                }
            }
        } else {
            resultDto.setSuccess(false);
            resultDto.setMessage("该交易单已处理！");
        }
        return resultDto;
    }

    @Override
    public ResultDto confirmPayment(Long orderId, Long payRequestId, User operator, String paymentBank, Date bankAccountTime) {
        ResultDto resultDto = new ResultDto();
        ConsignOrder order = consignOrderDao.queryById(orderId);
        String alterStatus = order.getAlterStatus();//合同变更状态s
        if (order == null) {
            resultDto.setSuccess(false);
            resultDto.setMessage("确认付款失败，没有找到该订单信息！");
            return resultDto;
        }
        if (!order.getPayStatus().equals(ConsignOrderPayStatus.APPLYPRINTED.toString())) {
            resultDto.setSuccess(false);

            resultDto.setMessage("该交易单已处理！");
            return resultDto;
        }
        PayRequest request = payRequestDao.selectByPrimaryKey(payRequestId);
        if (request == null) {
            resultDto.setSuccess(false);
            resultDto.setMessage("未找到对应的付款申请！");
            return resultDto;
        }
        //add by wangxianjun 防止并发
        if(!PayStatus.APPLYPRINTED.toString().equals(request.getStatus())){
            resultDto.setSuccess(false);
            resultDto.setMessage("该付款申请单已处理！");
            return resultDto;
        }

        ConsignOrderUpdateDto consignOrder = new ConsignOrderUpdateDto();
        consignOrder.setId(orderId);
        consignOrder.setPayStatus(ConsignOrderPayStatus.PAYED.toString());
        consignOrder.setLastUpdated(new Date());
        consignOrder.setLastUpdatedBy(operator.getName());
        consignOrder.setOldPayStatus(ConsignOrderPayStatus.APPLYPRINTED.toString());

        OrderAuditTrail orderAuditTrail = new OrderAuditTrail();
        orderAuditTrail.setLastUpdatedBy(operator.getName());
        orderAuditTrail.setLastUpdated(new Date());
        orderAuditTrail.setOrderId(orderId);
        orderAuditTrail.setOperatorId(operator.getId());
        orderAuditTrail.setOperatorName(operator.getName());
        orderAuditTrail.setSetToStatus(ConsignOrderPayStatus.PAYED.toString());
        orderAuditTrail.setStatusType(OrderStatusType.PAY.toString());
        orderAuditTrail.setCreated(new Date());
        orderAuditTrail.setCreatedBy(operator.getName());
        orderAuditTrail.setModificationNumber(0);

        PayRequest payRequest = new PayRequest();
        payRequest.setId(payRequestId);
        payRequest.setStatus(PayStatus.CONFIRMEDPAY.toString());
        payRequest.setPayStatus(ConsignOrderPayStatus.PAYED.toString());// add by lixiang 货币资金报表专用
        payRequest.setLastUpdatedBy(operator.getName());
        payRequest.setLastUpdated(new Date());

        List<PayRequestItems> payRequestItemses = payRequestItemsDao.selectByRequestId(payRequestId);

        Result result;
        if (consignOrderDao.updateByConditionSelective(consignOrder) > 0) {
            // 查询订单变更表是否存在变更记录，存在需要修改合同变更数据
           /* ConsignOrderChange query = new ConsignOrderChange();
            query.setStatus(ConsignOrderAlterStatus.PENDING_CONFIRM_PAY.getCode()); //待确认付款
            query.setOrderId(orderId);*/
            //modify by wangxianjun 通过变更订单id查询 updateOrderByChange这个方法应该整个原订单信息 只有变更订单才执行
            ConsignOrderChange consignOrderChange = new ConsignOrderChange();
            if(ConsignOrderAlterStatus.PENDING_CONFIRM_PAY.getCode().equals(alterStatus)) {
                consignOrderChange = consignOrderChangeService.selectByPrimaryKey(order.getChangeOrderId());
                order.setPayStatus(ConsignOrderPayStatus.PAYED.toString());
                if (consignOrderChange != null) {
                    consignOrderChangeService.updateOrderByChange(order, consignOrderChange, ConsignOrderAlterStatus.CHANGED_SUCCESS3.getCode(), null, operator);
                    consignOrderChangeService.updateChangeStatus(consignOrderChange, ConsignOrderAlterStatus.CHANGED_SUCCESS3.getCode(), null, operator);
                }else{
                    resultDto.setSuccess(false);
                    resultDto.setMessage("确认付款失败，没有找到该变更订单信息！");
                    return resultDto;
                }
            }

            if (orderStatusDao.insertOrderAuditTrail(orderAuditTrail) > 0
                    && payRequestDao.updateByPrimaryKeySelective(payRequest) > 0 && payRequestItemses != null) {
            	List<Long> itemsIds = payRequestItemses.stream().map(a -> a.getId())
        				.collect(Collectors.toList());
            	payRequestItemsDao.updatePaymentBank(itemsIds, paymentBank, bankAccountTime);//修改付款申请银行
//                orderStatusService.updatePayment(order.getAccountId(), order.getCode(), null, order.getTotalContractReletedAmount().doubleValue(),
//                        BigDecimal.ZERO, operator.getId(), operator.getName(), new Date(), Type.WITHDRAWALW_THAW.getCode());
//                orderStatusService.updatePayment(order.getAccountId(), order.getCode(), "", order.getTotalContractReletedAmount().doubleValue(), BigDecimal.ZERO, operator.getId(), operator.getName(), new Date(), Type.PAYMENT_CONFIRM.getCode());
                //modify by wangxianjun 变更订单解锁关联合同货款并支付合同货款 在updateOrderByChange返写原订单中已处理
                /*if(ConsignOrderAlterStatus.PENDING_CONFIRM_PAY.getCode().equals(alterStatus)) {
                    if(consignOrderChange.getChangeRelateAmount()!=null) {
                        accountFundService.updateAccountFund(order.getDepartmentId(), AssociationType.ORDER_CODE, order.getCode(),
                                AccountTransApplyType.BALANCES_UNLOCK, consignOrderChange.getChangeRelateAmount(),
                                BigDecimal.ZERO.subtract(consignOrderChange.getChangeRelateAmount()), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, PayType.BALANCE,
                                operator.getId(), operator.getName(), new Date()); //解锁账户
                        accountFundService.updateAccountFund(order.getDepartmentId(), AssociationType.ORDER_CODE, order.getCode(),
                                AccountTransApplyType.PAY_THE_CONTRACT_PAYMENT, BigDecimal.ZERO.subtract(consignOrderChange.getChangeRelateAmount()),
                                BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, PayType.BALANCE, operator.getId(), operator.getName(), new Date()); //支付合同货款
                    }
                }else*/
                if(!ConsignOrderAlterStatus.PENDING_CONFIRM_PAY.getCode().equals(alterStatus)) {
                    accountFundService.updateAccountFund(order.getDepartmentId(), AssociationType.ORDER_CODE, order.getCode(),
                            AccountTransApplyType.BALANCES_UNLOCK, order.getTotalContractReletedAmount(),
                            BigDecimal.ZERO.subtract(order.getTotalContractReletedAmount()), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, PayType.BALANCE,
                            operator.getId(), operator.getName(), new Date()); //解锁账户
                    accountFundService.updateAccountFund(order.getDepartmentId(), AssociationType.ORDER_CODE, order.getCode(),
                            AccountTransApplyType.PAY_THE_CONTRACT_PAYMENT, BigDecimal.ZERO.subtract(order.getTotalContractReletedAmount()),
                            BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, PayType.BALANCE, operator.getId(), operator.getName(), new Date()); //支付合同货款
                }

                    for (PayRequestItems item : payRequestItemses) {
                    if (item.getSecondBalanceTakeout().doubleValue() != 0) {
                        //解锁抵扣二次结算账户欠款
                        accountFundService.updateAccountFund(item.getReceiverDepartmentId(), AssociationType.ORDER_CODE, order.getCode(),
                                AccountTransApplyType.SECONDARY_SETTLEMENT_UNLOCK, BigDecimal.ZERO, BigDecimal.ZERO, item.getSecondBalanceTakeout().negate(),
                                item.getSecondBalanceTakeout(), BigDecimal.ZERO, BigDecimal.ZERO, PayType.BALANCE, operator.getId(),
                                operator.getName(), new Date());
                        //合同款到账
                        accountFundService.updateAccountFund(item.getReceiverDepartmentId(), AssociationType.ORDER_CODE, order.getCode(),
                                AccountTransApplyType.GETONGKUAN_TO_ACCOUNT, item.getPayAmount().add(item.getSecondBalanceTakeout()),
                                BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, PayType.BALANCE, operator.getId(), operator.getName(), new Date());
//                      //抵扣二次结算账户欠款
                        accountFundService.updateAccountFund(item.getReceiverDepartmentId(), AssociationType.ORDER_CODE, order.getCode(),
                                AccountTransApplyType.SECONDARY_SETTLEMENT_ACCOUNT_BALANCES, item.getSecondBalanceTakeout().negate(),
                                BigDecimal.ZERO, item.getSecondBalanceTakeout(), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, PayType.BALANCE, operator.getId(), operator.getName(), new Date());
                    } else {
                        //合同款到账
                        accountFundService.updateAccountFund(item.getReceiverDepartmentId(), AssociationType.ORDER_CODE, order.getCode(),
                                AccountTransApplyType.GETONGKUAN_TO_ACCOUNT, item.getPayAmount(), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, PayType.BALANCE,
                                operator.getId(), operator.getName(), new Date());
                    }
                    //资金账户转出
                    accountFundService.updateAccountFund(item.getReceiverDepartmentId(), AssociationType.ORDER_CODE, order.getCode(),
                            AccountTransApplyType.CAPITAL_ACCOUNT_TRANSFER, BigDecimal.ZERO.subtract(item.getPayAmount()),
                            BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, PayType.BALANCE, operator.getId(), operator.getName(), new Date());
                }
                resultDto.setSuccess(true);
                resultDto.setMessage("确认付款成功！");

                //短信通知
                User trader = userDao.queryById(order.getOwnerId()); //获取交易员
                String endCode = order.getCode().substring(order.getCode().length() - 6, order.getCode().length());
                SysSetting template=sysSettingService.queryBySettingType(SysSettingType.SmsTemplatePayConfirm.getCode());
                if (template == null) {
                    throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,"财务确认付款通知交易员短信模板缺失");
                }
                String content =template.getSettingValue();
                content = content.replace("#endcode#", endCode);
                content = content.replace("#companyname#", order.getAccountName());
                Nido.start(NidoTaskConstant.SEND_NOTE_MESSAGE, new NoteMessageContext(trader.getTel(), content));
                
                String[] pushInfo = orderCacheService.getPushInfoByUserLoginId(trader.getLoginId());
                if(pushInfo!=null){
                	content = String.format("请联系%s提货。", order.getAccountName());
                    appPushService.sendPushNoticfication(pushInfo[0],pushInfo[1], AppNoticeType.PayApplyPass.getName(),content);
                }
                // 供应商进项报表踩点 - 初次付款踩点
                reportFinanceService.orderOperation(ReportSellerInvoiceInOperationType.FirstPayment.getOperation(),orderId, operator);
            } else {
                throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "确认付款失败！");
            }
        } else {
            resultDto.setSuccess(false);
            resultDto.setMessage("该交易单已处理！");
        }
        return resultDto;
    }

    /**
     * 订单关闭(确认已付款状态) add by kongbinheng 20150906
     * @param orderId
     * @param payRequestId
     * @param operator
     * @param cause
     * @return
     */
    @Override
    public ResultDto confirmClose(Long orderId, Long payRequestId, User operator,String cause) {
        ResultDto resultDto = new ResultDto();
        ConsignOrder order = consignOrderDao.queryById(orderId);
        if (order == null) {
            resultDto.setSuccess(false);
            resultDto.setMessage("订单关闭失败，没有找到该订单信息！");
            return resultDto;
        }
        if (!order.getPayStatus().equals(ConsignOrderPayStatus.APPROVED.toString())
                && !order.getPayStatus().equals(ConsignOrderPayStatus.APPLYPRINTED.toString())) {
            resultDto.setSuccess(false);
            resultDto.setMessage("该交易单已处理！");
            return resultDto;
        }

        ConsignOrderUpdateDto consignOrder = new ConsignOrderUpdateDto();
        consignOrder.setId(orderId);
        consignOrder.setStatus(ConsignOrderStatus.CLOSEPAY.getCode());//关闭订单
        consignOrder.setOriginStatus(order.getStatus());   //记录订单原先状态，审核不通过需要做恢复
        consignOrder.setLastUpdated(new Date());
        consignOrder.setLastUpdatedBy(operator.getName());

        OrderAuditTrail orderAuditTrail = new OrderAuditTrail();
        orderAuditTrail.setLastUpdatedBy(operator.getName());
        orderAuditTrail.setLastUpdated(new Date());
        orderAuditTrail.setOrderId(orderId);
        orderAuditTrail.setOperatorId(operator.getId());
        orderAuditTrail.setOperatorName(operator.getName());
        orderAuditTrail.setSetToStatus(ConsignOrderStatus.CLOSEPAY.getCode());
        orderAuditTrail.setStatusType(OrderStatusType.MAIN.toString());
        orderAuditTrail.setCreated(new Date());
        orderAuditTrail.setCreatedBy(operator.getName());
        orderAuditTrail.setModificationNumber(0);
        orderAuditTrail.setExt3(cause);

        List<PayRequestItems> payRequestItemses = payRequestItemsDao.selectByRequestId(payRequestId);

        if (consignOrderDao.updateByConditionSelective(consignOrder) > 0) {
            if (orderStatusDao.insertOrderAuditTrail(orderAuditTrail) > 0
                    && payRequestItemses != null) {
                //关闭订单解冻买家
                accountFundService.updateAccountFund(order.getDepartmentId(), AssociationType.ORDER_CODE, order.getCode(),
                        AccountTransApplyType.BALANCES_UNLOCK, order.getTotalContractReletedAmount(),
                        order.getTotalContractReletedAmount().negate(), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                        PayType.BALANCE, operator.getId(), operator.getName(), new Date());

                //买家调用还款/抵扣接口 add by dengxiyan
                accountFundService.payForCredit(order.getDepartmentId(),AssociationType.ORDER_CODE,order.getCode(),0l,Constant.SYSTEMNAME,new Date());

                for (PayRequestItems item : payRequestItemses) {
                    if (item.getSecondBalanceTakeout().doubleValue() != 0) {
                        //关闭订单解锁抵扣二次结算账户欠款
                        accountFundService.updateAccountFund(item.getReceiverDepartmentId(), AssociationType.ORDER_CODE,
                                order.getCode(), AccountTransApplyType.SECONDARY_SETTLEMENT_UNLOCK,
                                BigDecimal.ZERO, BigDecimal.ZERO, item.getSecondBalanceTakeout().negate(), item.getSecondBalanceTakeout(),
                                BigDecimal.ZERO, BigDecimal.ZERO, PayType.BALANCE, operator.getId(), operator.getName(), new Date());
                    }

                    //卖家回退还款信用额度 add by dengxiyan
                    if(BigDecimal.ZERO.compareTo(item.getCreditUsedRepay()) != 0){
                        accountFundService.updateAccountFund(item.getReceiverDepartmentId(), AssociationType.ORDER_CODE,
                                order.getCode(), AccountTransApplyType.CREDITLIMI_BACK,
                                BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, item.getCreditUsedRepay(), BigDecimal.ZERO
                                , PayType.BALANCE, 0l, Constant.SYSTEMNAME, new Date());
                    }

                    //卖家调用自动还款/抵扣接口 add by dengxiyan
                    accountFundService.payForCredit(item.getReceiverDepartmentId(),AssociationType.ORDER_CODE,order.getCode(),0l,Constant.SYSTEMNAME,new Date());
                }
                resultDto.setSuccess(true);
                resultDto.setMessage("订单关闭成功！");

                //短信通知
                User trader = userDao.queryById(order.getOwnerId()); //获取交易员
                String endCode = order.getCode().substring(order.getCode().length() - 6, order.getCode().length());
                SysSetting template=sysSettingService.queryBySettingType(SysSettingType.SmsTemplateOrderClose.getCode());
                if (template == null) {
                    throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,"订单关闭通知交易员短信模板缺失");
                }
                String content =template.getSettingValue();
                content = content.replace("#endcode#", endCode);
                content = content.replace("#managername#", operator.getName());
                Nido.start(NidoTaskConstant.SEND_NOTE_MESSAGE, new NoteMessageContext(trader.getTel(), content));
                
                //将金额变化，添加到report_buyer_invoice_out
                reportFinaceService.pushToReportInvoiceOut(order, operator, order.getTotalContractReletedAmount(), ReportBuyerInvoiceOutType.CloseOrder, null);
                
            } else {
                throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "订单关闭失败");
            }
        } else {
            resultDto.setSuccess(false);
            resultDto.setMessage("该交易单已处理！");
        }
        // 供应商进项报表踩点 - 订单关闭踩点
        reportFinanceService.orderOperation(ReportSellerInvoiceInOperationType.OrderClose.getOperation(), orderId, operator);
        return resultDto;
    }

    @Override
    public ResultDto close(Long orderId, User operator,String cause) {
        ResultDto resultDto = new ResultDto();
        ConsignOrder order = consignOrderDao.queryById(orderId);
        if (order == null) {
            resultDto.setSuccess(false);
            resultDto.setMessage("没有找到对应的订单！");
            return resultDto;
        }
        if ((!ConsignOrderStatus.NEWAPPROVED.getCode().equals(
                order.getStatus())
                && !ConsignOrderStatus.RELATED.getCode().equals(
                order.getStatus())
                && !ConsignOrderStatus.SECONDSETTLE.getCode().equals(
                order.getStatus()))
                || !ConsignOrderPayStatus.APPLY.getCode().equals(order.getPayStatus())) {
            resultDto.setSuccess(false);
            resultDto.setMessage("该交易单已处理！");
            return resultDto;
        }
        String status = "";
        if (ConsignOrderStatus.NEWAPPROVED.getCode().equals(order.getStatus())) {
            status = ConsignOrderStatus.CLOSEREQUEST1.getCode();
        } else {
            status = ConsignOrderStatus.CLOSEREQUEST2.getCode();
        }
        ConsignOrderUpdateDto consignOrder = new ConsignOrderUpdateDto();
        consignOrder.setId(orderId);
        consignOrder.setStatus(status);
        consignOrder.setOriginStatus(order.getStatus());   //记录订单原先状态，审核不通过需要做恢复
        consignOrder.setLastUpdated(new Date());
        consignOrder.setLastUpdatedBy(operator.getName());

        List<String> oldStatusList = new ArrayList<>();
        oldStatusList.add(ConsignOrderStatus.NEWAPPROVED.getCode());
        oldStatusList.add(ConsignOrderStatus.RELATED.getCode());
        oldStatusList.add(ConsignOrderStatus.SECONDSETTLE.getCode());
        consignOrder.setOldStatusList(oldStatusList);

        OrderAuditTrail orderAuditTrail = new OrderAuditTrail();
        orderAuditTrail.setLastUpdatedBy(operator.getName());
        orderAuditTrail.setLastUpdated(new Date());
        orderAuditTrail.setOrderId(orderId);
        orderAuditTrail.setSetToStatus(status);
        orderAuditTrail.setStatusType(OrderStatusType.MAIN.toString());
        orderAuditTrail.setOperatorId(operator.getId());
        orderAuditTrail.setOperatorName(operator.getName());
        orderAuditTrail.setCreated(new Date());
        orderAuditTrail.setCreatedBy(operator.getName());
        orderAuditTrail.setModificationNumber(0);
        orderAuditTrail.setExt3(cause);
        if (consignOrderDao.updateByConditionSelective(consignOrder) > 0) {
            if (orderStatusDao.insertOrderAuditTrail(orderAuditTrail) > 0) {
                resultDto.setSuccess(true);
                resultDto.setMessage("申请关闭已提交成功，请等待审核！");
            } else {
                throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "申请关闭失败");
            }
        } else {
            resultDto.setSuccess(false);
            resultDto.setMessage("该交易单已处理！");
        }
        return resultDto;
    }

    /**
     * 审核关闭订单通过
     *
     * @param orderId  订单号
     * @param operator 操作人
     * @return
     */
    @Override
    public ResultDto auditClose(Long orderId, User operator) {
        ResultDto resultDto = new ResultDto();
        ConsignOrder order = consignOrderDao.queryById(orderId);
        if (order == null) {
            resultDto.setSuccess(false);
            resultDto.setMessage("没有找到对应的订单！");
            return resultDto;
        }
        if (!ConsignOrderStatus.CLOSEREQUEST1.getCode().equals(
                order.getStatus())
                && !ConsignOrderStatus.CLOSEREQUEST2.getCode().equals(
                order.getStatus())) {
            resultDto.setSuccess(false);
            resultDto.setMessage("该交易单已处理！");
            return resultDto;
        }
        ConsignOrderUpdateDto consignOrder = new ConsignOrderUpdateDto();
        consignOrder.setId(orderId);
        consignOrder.setStatus(ConsignOrderStatus.CLOSEAPPROVED.getCode());
        consignOrder.setLastUpdated(new Date());
        consignOrder.setLastUpdatedBy(operator.getName());

        List<String> oldStatusList = new ArrayList<>();
        oldStatusList.add(ConsignOrderStatus.CLOSEREQUEST1.getCode());
        oldStatusList.add(ConsignOrderStatus.CLOSEREQUEST2.getCode());
        consignOrder.setOldStatusList(oldStatusList);

        OrderAuditTrail orderAuditTrail = new OrderAuditTrail();
        orderAuditTrail.setLastUpdatedBy(operator.getName());
        orderAuditTrail.setLastUpdated(new Date());
        orderAuditTrail.setOrderId(orderId);
        orderAuditTrail.setSetToStatus(ConsignOrderStatus.CLOSEAPPROVED.getCode());
        orderAuditTrail.setStatusType(OrderStatusType.MAIN.toString());
        orderAuditTrail.setCreated(new Date());
        orderAuditTrail.setCreatedBy(operator.getName());
        orderAuditTrail.setModificationNumber(0);
        if (consignOrderDao.updateByConditionSelective(consignOrder) > 0) {
            if (orderStatusDao.insertOrderAuditTrail(orderAuditTrail) > 0) {
                //add by dengxiyan 如果是已关联的订单申请了关闭 审核通过需解冻金额
                if (ConsignOrderStatus.CLOSEREQUEST2.getCode().equals(
                        order.getStatus())) {
                    accountFundService.updateAccountFund(order.getDepartmentId(), AssociationType.ORDER_CODE,
                            order.getCode(), AccountTransApplyType.BALANCES_UNLOCK, order.getTotalContractReletedAmount(),
                            BigDecimal.ZERO.subtract(order.getTotalContractReletedAmount()), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                            PayType.BALANCE, operator.getId(), operator.getName(), new Date());
                    resultDto.setSuccess(true);
                    resultDto.setMessage("审核成功,订单已关闭！");

                    //买家调用还款/抵扣接口 add by dengxiyan
                    accountFundService.payForCredit(order.getDepartmentId(),AssociationType.ORDER_CODE,order.getCode(),0l,Constant.SYSTEMNAME,new Date());
                    resultDto.setSuccess(true);
                    resultDto.setMessage("审核成功,订单已关闭！");

                    // 供应商进项报表踩点 - 订单关闭踩点
                    if(order.getPayStatus().equals(ConsignOrderPayStatus.PAYED.getCode())) {
                    	reportFinanceService.orderOperation(ReportSellerInvoiceInOperationType.OrderClose.getOperation(),orderId, operator);
                    }
                } else {
                    resultDto.setSuccess(true);
                    resultDto.setMessage("审核成功,订单已关闭！");
                }
            } else {
                throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "审核关闭失败");
            }
        } else {
            resultDto.setSuccess(false);
            resultDto.setMessage("该交易单已处理！");
        }
        //ADD BY LIXING 订单状态变动时推送消息至钢为掌柜app 已关闭
//        Organization organization = organizationDao.queryById(operator.getOrgId());
//        User manageUser = userDao.queryById(organization.getCharger());
//        if (manageUser != null) {
//        	String[] pushInfo = orderCacheService.getPushInfoByUserLoginId(manageUser.getLoginId());
//        	if(pushInfo!=null){
//        		appPushService.sendPushNoticfication(pushInfo[0],pushInfo[1], COLSED, COLSED);
//        	}
//        }
        //将解冻的资金插入到  客户销项报表
        reportFinaceService.pushToReportInvoiceOut(order, operator, order.getTotalContractReletedAmount(), ReportBuyerInvoiceOutType.CloseOrder, null);
        
        return resultDto;
    }

    /**
     * 审核关闭订单不通过
     *
     * @param orderId  订单号
     * @param operator 操作人
     * @return
     */
    @Override
    public ResultDto auditCloseRefuse(Long orderId, User operator, String note) {
        ResultDto resultDto = new ResultDto();
        ConsignOrder order = consignOrderDao.queryById(orderId);
        if (order == null) {
            resultDto.setSuccess(false);
            resultDto.setMessage("没有找到对应的订单！");
            return resultDto;
        }
        if (!ConsignOrderStatus.CLOSEREQUEST1.getCode().equals(order.getStatus()) && !ConsignOrderStatus.CLOSEREQUEST2.getCode().equals(order.getStatus())) {
            resultDto.setSuccess(false);
            resultDto.setMessage("该交易单已处理！");
            return resultDto;
        }
        String status = order.getOriginStatus();//订单关闭之前状态
        if(status == null || status == ""){
            if (ConsignOrderStatus.CLOSEREQUEST1.getCode().equals(order.getStatus())) {
                status = ConsignOrderStatus.NEWAPPROVED.getCode();
            } else if (ConsignOrderStatus.CLOSEREQUEST2.getCode().equals(order.getStatus())) {
                status = ConsignOrderStatus.RELATED.getCode();
            }
        }
        ConsignOrderUpdateDto consignOrder = new ConsignOrderUpdateDto();
        consignOrder.setId(orderId);
        consignOrder.setStatus(status);
        consignOrder.setLastUpdated(new Date());
        consignOrder.setLastUpdatedBy(operator.getName());

        List<String> oldStatusList = new ArrayList<>();
        oldStatusList.add(ConsignOrderStatus.CLOSEREQUEST1.getCode());
        oldStatusList.add(ConsignOrderStatus.CLOSEREQUEST2.getCode());
        consignOrder.setOldStatusList(oldStatusList);

        OrderAuditTrail orderAuditTrail = new OrderAuditTrail();
        orderAuditTrail.setLastUpdatedBy(operator.getName());
        orderAuditTrail.setLastUpdated(new Date());
        orderAuditTrail.setOrderId(orderId);
        orderAuditTrail.setOperatorId(operator.getId());
        orderAuditTrail.setOperatorName(operator.getName());
        orderAuditTrail.setSetToStatus(status);
        orderAuditTrail.setStatusType(OrderStatusType.MAIN.toString());
        orderAuditTrail.setCreated(new Date());
        orderAuditTrail.setCreatedBy(operator.getName());
        orderAuditTrail.setModificationNumber(0);
        orderAuditTrail.setNote(note);//审核不通过理由
        if (consignOrderDao.updateByConditionSelective(consignOrder) > 0) {
            if (orderStatusDao.insertOrderAuditTrail(orderAuditTrail) > 0) {
                resultDto.setSuccess(true);
                resultDto.setMessage("审核成功！");
            } else {
                throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "审核不关闭失败");
            }
        } else {
            resultDto.setSuccess(false);
            resultDto.setMessage("该交易单已处理！");
        }
        return resultDto;
    }

    /**
     * 回滚订单申请
     * @param orderId
     * @param operator
     * @param cause
     * @return
     */
    @Override
    public ResultDto rollbackApply(Long orderId, User operator,String cause)
    {
        ResultDto resultDto = new ResultDto();
        ConsignOrder order = consignOrderDao.queryById(orderId);
        if (order == null) {
            resultDto.setSuccess(false);
            resultDto.setMessage("没有找到对应的订单！");
            return resultDto;
        }
        //查询相关折让状态
        int num = allowanceDao.selectAllowanceByOrderId(orderId);
        if (num > 0) {
        	resultDto.setSuccess(false);
            resultDto.setMessage("该订单已关联折让单，不能关闭，请先关闭折让单！");
            return resultDto;
        }
        String orderStatus = order.getStatus();
        if(!(ConsignOrderPayStatus.PAYED.getCode().equals(order.getPayStatus()) &&
                (ConsignOrderStatus.RELATED.getCode().equals(orderStatus)
                        || ConsignOrderStatus.SECONDSETTLE.getCode().equals(orderStatus)
                        || ConsignOrderStatus.INVOICEREQUEST.getCode().equals(orderStatus)))){
            resultDto.setSuccess(false);
            resultDto.setMessage("该交易单已处理！");
            return resultDto;
        }
        ConsignOrderUpdateDto consignOrder = new ConsignOrderUpdateDto();
        consignOrder.setId(orderId);
        consignOrder.setStatus(ConsignOrderStatus.CLOSESECONDARYREQUEST.getCode());
        consignOrder.setLastUpdated(new Date());
        consignOrder.setLastUpdatedBy(operator.getLoginId());

        List<String> oldStatusList = new ArrayList<>();
        oldStatusList.add(orderStatus);
        consignOrder.setOldStatusList(oldStatusList);
        consignOrder.setOriginStatus(orderStatus);   //记录订单原先状态，审核不通过需要做恢复

        OrderAuditTrail orderAuditTrail = new OrderAuditTrail();
        orderAuditTrail.setLastUpdatedBy(operator.getLoginId());
        orderAuditTrail.setLastUpdated(new Date());
        orderAuditTrail.setOrderId(orderId);
        orderAuditTrail.setSetToStatus(ConsignOrderStatus.CLOSESECONDARYREQUEST.getCode());
        orderAuditTrail.setStatusType(OrderStatusType.MAIN.toString());
        orderAuditTrail.setCreated(new Date());
        orderAuditTrail.setCreatedBy(operator.getLoginId());
        orderAuditTrail.setModificationNumber(0);
        orderAuditTrail.setExt3(cause);
        if (consignOrderDao.updateByConditionSelective(consignOrder) > 0) {
            if (orderStatusDao.insertOrderAuditTrail(orderAuditTrail) > 0) {
                resultDto.setSuccess(true);
                resultDto.setMessage("申请成功！");
            } else {
                throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "申请关闭订单失败");
            }
        } else {
            resultDto.setSuccess(false);
            resultDto.setMessage("该交易单已处理！");
        }
        return resultDto;
    }

    /**
     * 回滚审核通过(总经理)
     * @param orderId
     * @param reason 不通过原因
     * @param operator
     * @return
     */
    @Override
    public ResultDto rollbackRefuse(Long orderId,String reason, User operator)
    {
        ConsignOrder order = consignOrderDao.selectByPrimaryKey(orderId);
        ResultDto resultDto = new ResultDto();
        ConsignOrderUpdateDto consignOrder = new ConsignOrderUpdateDto();
        consignOrder.setId(orderId);
        consignOrder.setStatus(order.getOriginStatus());
        consignOrder.setLastUpdated(new Date());
        consignOrder.setLastUpdatedBy(operator.getLoginId());

        List<String> oldStatusList = new ArrayList<>();
        oldStatusList.add(ConsignOrderStatus.CLOSESECONDARYREQUEST.getCode());
        oldStatusList.add(ConsignOrderStatus.CLOSESECONDARYAPPROVED.getCode());
        consignOrder.setOldStatusList(oldStatusList);

        OrderAuditTrail orderAuditTrail = new OrderAuditTrail();
        orderAuditTrail.setLastUpdatedBy(operator.getLoginId());
        orderAuditTrail.setLastUpdated(new Date());
        orderAuditTrail.setOrderId(orderId);
        orderAuditTrail.setSetToStatus(order.getOriginStatus());   //回到原来的状态
        orderAuditTrail.setStatusType(OrderStatusType.MAIN.toString());
        orderAuditTrail.setCreated(new Date());
        orderAuditTrail.setCreatedBy(operator.getLoginId());
        orderAuditTrail.setModificationNumber(0);
        if (consignOrderDao.updateByConditionSelective(consignOrder) > 0) {
            if (orderStatusDao.insertOrderAuditTrail(orderAuditTrail) > 0) {
                resultDto.setSuccess(true);
                resultDto.setMessage("审核成功！");
            } else {
                throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "审核回滚失败");
            }
        } else {
            resultDto.setSuccess(false);
            resultDto.setMessage("该交易单已处理！");
        }
        return resultDto;
    }

    /**
     * 回滚审核通过(服总)
     * @param orderId
     * @param operator
     * @return
     */
    @Override
    public ResultDto rollbackAccept(Long orderId, User operator)
    {
        ResultDto resultDto = new ResultDto();
        ConsignOrder order = consignOrderDao.queryById(orderId);
        if (order == null) {
            resultDto.setSuccess(false);
            resultDto.setMessage("没有找到对应的订单！");
            return resultDto;
        }
        ConsignOrderUpdateDto consignOrder = new ConsignOrderUpdateDto();
        consignOrder.setId(orderId);
        consignOrder.setStatus(ConsignOrderStatus.CLOSESECONDARYAPPROVED.getCode());
        consignOrder.setLastUpdated(new Date());
        consignOrder.setLastUpdatedBy(operator.getName());

        List<String> oldStatusList = new ArrayList<>();
        oldStatusList.add(ConsignOrderStatus.CLOSESECONDARYREQUEST.getCode());
        consignOrder.setOldStatusList(oldStatusList);

        OrderAuditTrail orderAuditTrail = new OrderAuditTrail();
        orderAuditTrail.setLastUpdatedBy(operator.getName());
        orderAuditTrail.setLastUpdated(new Date());
        orderAuditTrail.setOrderId(orderId);
        orderAuditTrail.setSetToStatus(ConsignOrderStatus.CLOSESECONDARYAPPROVED.getCode());
        orderAuditTrail.setStatusType(OrderStatusType.MAIN.toString());
        orderAuditTrail.setCreated(new Date());
        orderAuditTrail.setCreatedBy(operator.getName());
        orderAuditTrail.setModificationNumber(0);
        if (consignOrderDao.updateByConditionSelective(consignOrder) > 0) {
            if (orderStatusDao.insertOrderAuditTrail(orderAuditTrail) > 0) {
                    resultDto.setSuccess(true);
                    resultDto.setMessage("审核成功！");
            } else {
                throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "审核回滚通过失败");
            }
        } else {
            resultDto.setSuccess(false);
            resultDto.setMessage("该交易单已处理！");
        }
        return resultDto;
    }

    /**
     * 订单关联银票
     * @param itemId
     * @param draftId 银票ID
     * @param draftCode 银票号
     * @param operator
     * @return
     */
    @Override
    public ResultDto addDraft(Long itemId, Long draftId, String draftCode, User operator) {
        ResultDto resultDto = new ResultDto();
        ConsignOrderItems orderitem = consignOrderItemsDao.selectByPrimaryKey(itemId);
        if (orderitem == null) {
            resultDto.setSuccess(false);
            resultDto.setMessage("没有找到对应的资源信息！");
            return resultDto;
        }
        if(orderitem.getIsPayedByAcceptDraft()){
            resultDto.setSuccess(false);
            resultDto.setMessage("已关联其它银票！");
            return resultDto;
        }
        ConsignOrderItems item = new ConsignOrderItems();
        item.setId(itemId);
        item.setIsPayedByAcceptDraft(true);
        item.setAcceptDraftId(draftId);
        item.setAcceptDraftCode(draftCode);
        item.setLastUpdated(new Date());
        item.setLastUpdatedBy(operator.getName());
        item.setModificationNumber(orderitem.getModificationNumber()+1);

        if (consignOrderItemsDao.updateByPrimaryKeySelective(item) > 0) {
                resultDto.setSuccess(true);
                resultDto.setMessage("关联银票成功！");
        } else {
            resultDto.setSuccess(false);
            resultDto.setMessage("已关联其它银票！");
        }
        return resultDto;
    }

    /**
     * 添加合同附件
     *
     * @param orderId
     * @param contractId
     * @param attachmentfiles
     * @param operator
     * @param contractType
     * @return 成功返回  "附件ID|附件URL"
     */
    @Override
    public ResultDto addCustomerContractAttachments(Long orderId, Long contractId, List<MultipartFile> attachmentfiles, User operator,String contractType) {
        ResultDto resultDto = new ResultDto();
        if (attachmentfiles == null) {
            resultDto.setSuccess(false);
            resultDto.setMessage("没有找到上传的文件");
            return resultDto;
        }
        if("change".equals(contractType)) {
            //合同变更流程 add by wangxianjun
            ConsignOrderChange changeOrder =  consignOrderChangeService.selectByPrimaryKey(Integer.parseInt(orderId.toString()));
            if (changeOrder == null) {
                throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "未找到相关变更订单");
            }
            if (!ConsignOrderAlterStatus.PENDING_APPLY.getCode().equals(changeOrder.getStatus())) {
                resultDto.setSuccess(false);
                resultDto.setMessage("该变更交易单已处理！");
                return resultDto;
            }

        }else{
            ConsignOrder order = consignOrderDao.queryById(orderId);
            if (order == null) {
                throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "未找到相关订单");
            }
            if (!order.getPayStatus().equals(ConsignOrderPayStatus.APPLY.toString()) ||
                    (!order.getStatus().equals(ConsignOrderStatus.RELATED.getCode()) &&
                            !order.getStatus().equals(ConsignOrderStatus.SECONDSETTLE.getCode()))) {
                resultDto.setSuccess(false);
                resultDto.setMessage("该交易单已处理！");
                return resultDto;
            }
        }
       /* String urlPath = ATTACHMENTSAVEPATH + orderId + File.separator;
        String basePath = urlPath;
        String temppath = new Date().getTime() + "."
                + FileUtil.getFileSuffix(attachment.getOriginalFilename());
        String savePath = basePath + temppath;
        String url = "";

        try {
            url = fileService.saveFile(attachment.getInputStream(), savePath);
            if (StringUtils.isEmpty(url)) {
                resultDto.setSuccess(false);
                resultDto.setMessage("文件保存失败");
                return resultDto;
            }
        } catch (Exception ex) {
            logger.error(ex.toString());
            resultDto.setSuccess(false);
            resultDto.setMessage("文件保存失败");
            return resultDto;
        }

        ConsignOrderAttachment consignOrderAttachment = new ConsignOrderAttachment();
        consignOrderAttachment.setConsignOrderId(orderId);
        consignOrderAttachment.setCreated(new Date());
        consignOrderAttachment.setRelateId(contractId);
        consignOrderAttachment.setCreatedBy(operator.getName());
        consignOrderAttachment.setLastUpdated(new Date());
        consignOrderAttachment.setLastUpdatedBy(operator.getName());
        consignOrderAttachment.setModificationNumber(0);
        consignOrderAttachment.setFileUrl(url);
        consignOrderAttachment.setType("合同");
        */
        //支持上传多张图片
        try {
            for (MultipartFile file : attachmentfiles) {
                saveAttachment(file, orderId, operator, "合同", contractId, false);
            }
            ConsignOrderContract orderContract = new ConsignOrderContract();
            orderContract.setId(contractId);
            orderContract.setLastUpdated(new Date());
            orderContract.setLastUpdatedBy(operator.getName());
            orderContract.setStatus(1);
            if (consignOrderContractDao.updateByPrimaryKeySelective(orderContract) <= 0) {
                resultDto.setSuccess(false);
                resultDto.setMessage("合同上传失败");
            }else{
                resultDto.setSuccess(true);
            }
        }catch (BusinessException e) {
            resultDto.setMessage(e.getMsg());
            resultDto.setSuccess(false);
        }
        return resultDto;
    }
    /**
     * 添加合同附件
     *
     * @param orderId
     * @param contractId
     * @param attachment
     * @param operator
     * @return 成功返回  "附件ID|附件URL"
     */
    @Override
    public ResultDto addCustomerContractAttachment(Long orderId, Long contractId, MultipartFile attachment, User operator) {
        ResultDto resultDto = new ResultDto();
        if (attachment == null) {
            resultDto.setSuccess(false);
            resultDto.setMessage("没有找到上传的文件");
            return resultDto;
        }
        ConsignOrder order = consignOrderDao.queryById(orderId);
        if(order==null){
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "未找到相关订单");
        }
        if (!order.getPayStatus().equals(ConsignOrderPayStatus.APPLY.toString()) ||
                (!order.getStatus().equals(ConsignOrderStatus.RELATED.getCode()) &&
                        !order.getStatus().equals(ConsignOrderStatus.SECONDSETTLE.getCode()))) {
            resultDto.setSuccess(false);
            resultDto.setMessage("该交易单已处理！");
            return resultDto;
        }
        String urlPath = ATTACHMENTSAVEPATH + orderId + File.separator;
        String basePath = urlPath;
        String temppath = new Date().getTime() + "."
                + FileUtil.getFileSuffix(attachment.getOriginalFilename());
        String savePath = basePath + temppath;
        String url = "";

        try {
            url = fileService.saveFile(attachment.getInputStream(), savePath);
            if (StringUtils.isEmpty(url)) {
                resultDto.setSuccess(false);
                resultDto.setMessage("文件保存失败");
                return resultDto;
            }
        } catch (Exception ex) {
            logger.error(ex.toString());
            resultDto.setSuccess(false);
            resultDto.setMessage("文件保存失败");
            return resultDto;
        }

        ConsignOrderAttachment consignOrderAttachment = new ConsignOrderAttachment();
        consignOrderAttachment.setConsignOrderId(orderId);
        consignOrderAttachment.setCreated(new Date());
        consignOrderAttachment.setRelateId(contractId);
        consignOrderAttachment.setCreatedBy(operator.getName());
        consignOrderAttachment.setLastUpdated(new Date());
        consignOrderAttachment.setLastUpdatedBy(operator.getName());
        consignOrderAttachment.setModificationNumber(0);
        consignOrderAttachment.setFileUrl(url);
        consignOrderAttachment.setType("合同");
        if (consignOrderAttachmentDao.insertSelective(consignOrderAttachment) > 0) {
            ConsignOrderContract orderContract = new ConsignOrderContract();
            orderContract.setId(contractId);
            orderContract.setLastUpdated(new Date());
            orderContract.setLastUpdatedBy(operator.getName());
            orderContract.setStatus(1);
            if (consignOrderContractDao.updateByPrimaryKeySelective(orderContract) > 0) {
                resultDto.setSuccess(true);
                resultDto.setMessage(consignOrderAttachment.getId() + "|" + url);
            } else {
                resultDto.setSuccess(false);
                resultDto.setMessage("合同上传失败");
            }
        } else {
            resultDto.setSuccess(false);
            resultDto.setMessage("合同上传失败");
        }

        return resultDto;
    }
    /**
     * 删除合同附件
     *
     * @param orderId
     * @param contractId
     * @param id
     * @param operator
     * @return
     */
    public ResultDto deleteCustomerContractAttachment(Long orderId, Long contractId, Long id, User operator,String contractType) {
        ResultDto resultDto = new ResultDto();
        if("change".equals(contractType)){
            //合同变更流程 add by wangxianjun
            ConsignOrderChange changeOrder =  consignOrderChangeService.selectByPrimaryKey(orderId.intValue());
            if (changeOrder == null) {
                throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "未找到相关变更订单");
            }
            if (!ConsignOrderAlterStatus.PENDING_APPLY.getCode().equals(changeOrder.getStatus())) {
                resultDto.setSuccess(false);
                resultDto.setMessage("该变更交易单已处理！");
                return resultDto;
            }
        }else {
            ConsignOrder order = consignOrderDao.queryById(orderId);
            if (order == null) {
                throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "未找到相关订单");
            }
            if (!order.getPayStatus().equals(ConsignOrderPayStatus.APPLY.toString()) ||
                    (!order.getStatus().equals(ConsignOrderStatus.RELATED.getCode()) &&
                            !order.getStatus().equals(ConsignOrderStatus.SECONDSETTLE.getCode()))) {
                resultDto.setSuccess(false);
                resultDto.setMessage("该交易单已处理！");
                return resultDto;
            }
        }
        ConsignOrderAttachment orderAttachment = consignOrderAttachmentDao.selectByPrimaryKey(id);
        if (orderAttachment == null || !orderAttachment.getRelateId().equals(contractId)) {
            resultDto.setSuccess(false);
            resultDto.setMessage("没有附件信息");
            return resultDto;
        }

        //删除附件
        ConsignOrderAttachment consignOrderAttachment = new ConsignOrderAttachment();
        consignOrderAttachment.setId(id);
        consignOrderAttachment.setStatus(1);
        consignOrderAttachment.setLastUpdated(new Date());
        consignOrderAttachment.setLastUpdatedBy(operator.getName());
        if (consignOrderAttachmentDao.updateByPrimaryKeySelective(consignOrderAttachment) > 0) {
            //判断是否还有合同图片
            int countContractAttachement = consignOrderAttachmentDao.countByContractId(contractId);
            if (countContractAttachement <= 0) { //无图片了，修改状态
                ConsignOrderContract orderContract = new ConsignOrderContract();
                orderContract.setId(contractId);
                orderContract.setStatus(0);  //设置为未上传合同
                orderContract.setLastUpdated(new Date());
                orderContract.setLastUpdatedBy(operator.getName());
                if (consignOrderContractDao.updateByPrimaryKeySelective(orderContract) > 0) {
                    resultDto.setSuccess(true);
                    resultDto.setMessage("删除成功");
                } else {
                    resultDto.setSuccess(false);
                    resultDto.setMessage("删除失败");
                }

            } else {
                resultDto.setSuccess(true);
                resultDto.setMessage("删除成功");
            }
        } else {
            resultDto.setSuccess(false);
            resultDto.setMessage("删除失败");
        }

        return resultDto;
    }

    /**
     * 修改合同上传信息
     *
     * @param contractId
     * @param contractNo
     * @param operator
     * @param contractType
     * @return
     */
    public ResultDto uploadContract(Long contractId, String contractNo, User operator,String contractType) {
        ResultDto resultDto = new ResultDto();
        ConsignOrderContract orderContract = consignOrderContractDao.selectByPrimaryKey(contractId);
        if(orderContract==null){
        	throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "未找到指定合同");
        }

        if("change".equals(contractType)){
            //合同变更流程 add by wangxianjun
            ConsignOrderChange changeOrder =  consignOrderChangeService.selectByPrimaryKey(orderContract.getChangeOrderId());
            if (changeOrder == null) {
                throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "未找到相关变更订单");
            }
            if (!ConsignOrderAlterStatus.PENDING_APPLY.getCode().equals(changeOrder.getStatus())) {
                resultDto.setSuccess(false);
                resultDto.setMessage("该变更交易单已处理！");
                return resultDto;
            }
        }else {
            ConsignOrder order = consignOrderDao.queryById(orderContract.getConsignOrderId());
            if(order==null){
                throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "未找到关联的订单");
            }
            if (!order.getPayStatus().equals(ConsignOrderPayStatus.APPLY.toString()) ||
                    (!order.getStatus().equals(ConsignOrderStatus.RELATED.getCode()) &&
                            !order.getStatus().equals(ConsignOrderStatus.SECONDSETTLE.getCode()))) {
                resultDto.setSuccess(false);
                resultDto.setMessage("该交易单已处理！");
                return resultDto;
            }
        }
        orderContract = new ConsignOrderContract();
        orderContract.setId(contractId);
        orderContract.setContractCodeCust(contractNo);
        if (consignOrderContractDao.updateByPrimaryKeySelective(orderContract) > 0) {
            resultDto.setSuccess(true);
            resultDto.setMessage("操作成功");
        } else {
            resultDto.setSuccess(false);
            resultDto.setMessage("操作失败");
        }
        return resultDto;
    }

    /**
     * 查询未付款用于系统自动关闭订单
     *
     * @return
     */
    @Override
    public boolean selectOrderByPayStatus() {
        List<ConsignOrder> list = consignOrderDao.selectOrderByPayStatus();
        for (ConsignOrder order : list) {
            Long id = order.getId();
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("id", id);
            paramMap.put("status", ConsignOrderStatus.SYSCLOSED.getCode());  //系统自动关闭
            paramMap.put("lastUpdated", new Date());
            paramMap.put("lastUpdatedBy", "");
            orderStatusDao.updateOrderStatus(paramMap);
        }
        return true;
    }

    /**
     * 根据条件分页查询买家客户的采购记录
     *
     * @param paramMap map封装参数说明 key:value
     *                 userId： 当前登录人id
     *                 dto   ： 封装的订单dto:ConsignOrderDto
     *                 start：  开始记录（分页参数）
     *                 length： 每页记录数（分页参数）
     * @return
     * @author dengxiyan
     */
    @Override
    public List<ConsignOrderDto> selectPurchaseListByConditions(Map<String, Object> paramMap) {
        setAccountOrderParam(paramMap);
        return consignOrderDao.selectAccountOrderListByConditions(paramMap);
    }

    /**
     * 根据条件统计买家客户的采购记录数
     *
     * @param paramMap map封装参数说明 key:value
     *                 userId： 当前登录人id
     *                 dto   ： 封装的订单dto:ConsignOrderDto
     * @return
     * @author dengxiyan
     */
    @Override
    public int totalPurchaseByConditions(Map<String, Object> paramMap) {
        return consignOrderDao.totalAccountOrderByConditions(paramMap);
    }

    /**
     * 根据条件分页查询卖家客户的销售记录
     *
     * @param paramMap map封装参数说明 key:value
     *                 userId： 当前登录人id
     *                 dto   ： 封装的订单dto:ConsignOrderDto
     *                 start：  开始记录（分页参数）
     *                 length： 每页记录数（分页参数）
     * @return
     * @author dengxiyan
     */
    @Override
    public List<ConsignOrderDto> selectSaleListByConditions(Map<String, Object> paramMap) {
        setAccountOrderParam(paramMap);
        return consignOrderDao.selectAccountOrderListByConditions(paramMap);
    }

    /**
     * 根据条件统计卖家客户的销售记录
     *
     * @param paramMap map封装参数说明 key:value
     *                 userId： 当前登录人id
     *                 dto   ： 封装的订单dto:ConsignOrderDto
     * @return
     * @author dengxiyan
     */
    @Override
    public int totalSaleByConditions(Map<String, Object> paramMap) {
        return consignOrderDao.totalAccountOrderByConditions(paramMap);
    }

    /*
     * 设置客户订单模糊查询条件
     * @param paramMap
     */
    private void setAccountOrderParam(Map<String, Object> paramMap) {
        ConsignOrderDto dto = (ConsignOrderDto) paramMap.get("dto");
        if (dto != null) {
            if (StringUtils.isNotEmpty(dto.getNsortName())) {
                dto.setNsortName("%" + dto.getNsortName().trim() + "%");
            }
            if (StringUtils.isNotEmpty(dto.getAccountName())) {
                dto.setAccountName("%" + dto.getAccountName().trim() + "%");
            }
            if (StringUtils.isNotEmpty(dto.getSellerName())) {
                dto.setSellerName("%" + dto.getSellerName().trim() + "%");
            }
        }
    }


    @Override
    public User getProcessUser(Long orderId, Long curUserId, String orderStatus, String perm) {
        User processUser = null;
        if (perm.equals(UserType.SERVER.toString())) {
            processUser = userDao.queryById(consignOrderDao.queryById(orderId).getOwnerId());
        } else if (perm.equals(UserType.SALESMAN.toString())) {
            processUser = userDao.queryById(consignProcessDao.getOfficeStaff(curUserId).getOperatorId());
        }
        return processUser;
    }

    /**
     * 统计当前登录人的付款情况的已关联订单总数列表
     *
     * @param paramMap ：map封装参数说明 key:value
     *                 userId：当前登录人id
     *                 list：  订单状态list用于in条件
     * @return
     * @author dengxiyan
     */
    @Override
    public List<Map<String, Object>> getPayOrderTotalList(Map<String, Object> paramMap) {
        return consignOrderDao.countOrderGroupByPayStatus(paramMap);
    }

    /**
     * 获取当前登录人能查看的状态订单总数列表
     *
     * @param paramMap ：map封装参数说明 key:value
     *                 userId：当前登录人id
     * @return
     * @author dengxiyan
     */
    @Override
    public List<Map<String, Object>> getOrderTotalGroupByStatus(Map<String, Object> paramMap) {
        return consignOrderDao.countOrderGroupByStatus(paramMap);
    }

    /*
     * 判断是否有锁定的客户
     * @param idList 客户id列表
     * @return true：有 false:没有
     */
    private boolean hasLockedAccount(Long accountId) {
        boolean flag = false;
        if (accountId != null && accountId > 0) {
            flag = accountDao.countLockedAccount(Arrays.asList(accountId)) > 0;
        }
        return flag;
    }

    private List<Long> getAccountIds(ConsignOrder consignOrder, List<ConsignOrderItems> list) {
        List<Long> ids = new ArrayList<>();

        if (list != null && list.size() > 0) {
            ids = list.stream().map(ConsignOrderItems::getSellerId).collect(Collectors.toList());
        }

        if (consignOrder != null) {
            ids.add(consignOrder.getAccountId());
        }
        return ids;
    }

    private List<Long> getDepartmentIds(ConsignOrder consignOrder, List<ConsignOrderItems> list) {
        List<Long> ids = new ArrayList<>();

        if (list != null && list.size() > 0) {
            ids = list.stream().map(ConsignOrderItems::getDepartmentId).collect(Collectors.toList());
        }

        if (consignOrder != null) {
            ids.add(consignOrder.getDepartmentId());
        }
        return ids;
    }
    /**
     * 订单打回
     *
     * @param orderId      订单Id
     * @param payRequestId 付款申请Id
     * @param note         打回原因
     * @param operator     操作用户
     */
    @Override
    public void fightback(Long orderId, Long payRequestId, String note, User operator) {
        ConsignOrder order = consignOrderDao.queryById(orderId);
        if (order == null) {
            throw new BusinessException(Constant.ERROR_MISSING_REQUIRED_PARAM,"没有找到对应的订单");
        }
        PayRequest request = payRequestDao.selectByPrimaryKey(payRequestId);
        if (request == null) {
            throw new BusinessException(Constant.ERROR_MISSING_REQUIRED_PARAM,"未找到对应的付款申请");
        }
        //合同变更流程走到待确认付款 打回
        if(ConsignOrderAlterStatus.PENDING_CONFIRM_PAY.getCode().equals(order.getAlterStatus())){
            if (!PayStatus.APPLYPRINTED.toString().equals(order.getPayStatus())) {
                throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "该交易单已处理！");
            }
            //add by wangxianjun 防止并发
            if(!PayStatus.APPLYPRINTED.toString().equals(request.getStatus())){
                throw new BusinessException(Constant.ERROR_MISSING_REQUIRED_PARAM,"该付款申请单已处理！");
            }
        }else {
            if (!PayStatus.APPROVED.toString().equals(order.getPayStatus())) {
                throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "该交易单已处理！");
            }
            //add by wangxianjun 防止并发
            if(!PayStatus.APPROVED.toString().equals(request.getStatus())){
                throw new BusinessException(Constant.ERROR_MISSING_REQUIRED_PARAM,"该付款申请单已处理！");
            }
        }


        ConsignOrderUpdateDto consignOrder = new ConsignOrderUpdateDto();
        consignOrder.setId(orderId);
        consignOrder.setCallBackStatus(ConsignOrderCallBackStatus.Back.getCode());//打回状态
        consignOrder.setCallBackNote(note);//打回理由
        consignOrder.setPayStatus(ConsignOrderPayStatus.APPLY.toString());
        consignOrder.setLastUpdated(new Date());
        consignOrder.setLastUpdatedBy(operator.getLoginId());
        //如果该笔订单是在提货后3日内付款的，不计算提成，当审核不通过时，需要把是否计算提成标志改成“是”
        consignOrder.setIsCountAchievement("1");
        //合同变更 打回把状态置成付款申请审核不通过，回到变更流程
        if(PayRequestType.ORDER_CHANGE.getCode().equals(request.getType())){
            consignOrder.setAlterStatus(ConsignOrderAlterStatus.PAYED_DISAPPROVE.getCode());
        }

        OrderAuditTrail orderAuditTrail = new OrderAuditTrail();
        orderAuditTrail.setLastUpdatedBy(operator.getLoginId());
        orderAuditTrail.setLastUpdated(new Date());
        orderAuditTrail.setOrderId(orderId);
        orderAuditTrail.setOperatorId(operator.getId());
        orderAuditTrail.setOperatorName(operator.getName());
        orderAuditTrail.setSetToStatus(ConsignOrderPayStatus.APPLY.toString());
        orderAuditTrail.setStatusType(OrderStatusType.PAY.toString());//打回
        orderAuditTrail.setCreated(new Date());
        orderAuditTrail.setCreatedBy(operator.getLoginId());
        orderAuditTrail.setModificationNumber(0);
        orderAuditTrail.setNote(note);//打回理由

        PayRequest payRequest = new PayRequest();
        payRequest.setId(payRequestId);
        payRequest.setStatus(PayStatus.ABANDONED.toString());
        payRequest.setLastUpdatedBy(operator.getLoginId());
        payRequest.setLastUpdated(new Date());

        List<PayRequestItems> payRequestItemses = payRequestItemsDao.selectByRequestId(payRequestId);

        if (consignOrderDao.updateByConditionSelective(consignOrder) > 0
                && orderStatusDao.insertOrderAuditTrail(orderAuditTrail) > 0
                && payRequestDao.updateByPrimaryKeySelective(payRequest) > 0) {
            //合同变更 打回把状态置成付款申请审核不通过，回到变更流程
            if(PayRequestType.ORDER_CHANGE.getCode().equals(request.getType())){
                ConsignOrderChange consignOrderChange = consignOrderChangeService.selectByPrimaryKey(order.getChangeOrderId());
                //修改合同变更表的状态，并且添加对应的变更记录
                if(consignOrderChange != null) {
                    consignOrderChangeService.updateChangeStatus(consignOrderChange, ConsignOrderAlterStatus.PAYED_DISAPPROVE.getCode(), note, operator);
                }else{
                    throw new BusinessException(Constant.ERROR_MISSING_REQUIRED_PARAM,"没有找到对应的变更订单");
                }
            }
            {
                for (PayRequestItems item : payRequestItemses) {
                    if (item.getSecondBalanceTakeout().doubleValue() != 0) {
                        accountFundService.updateAccountFund(item.getReceiverDepartmentId(), AssociationType.ORDER_CODE, order.getCode(),
                                AccountTransApplyType.SECONDARY_SETTLEMENT_UNLOCK, BigDecimal.ZERO, BigDecimal.ZERO, item.getSecondBalanceTakeout().negate(),
                                item.getSecondBalanceTakeout(), BigDecimal.ZERO, BigDecimal.ZERO, PayType.BALANCE,
                                operator.getId(), operator.getName(), new Date()); //解锁账户
                    }
                    if(item.getCreditUsedRepay().compareTo(BigDecimal.ZERO)>0){
                        accountFundService.updateAccountFund(item.getReceiverDepartmentId(), AssociationType.ORDER_CODE, order.getCode(),
                                AccountTransApplyType.CREDITLIMI_BACK, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                                BigDecimal.ZERO, item.getCreditUsedRepay(), BigDecimal.ZERO, PayType.BALANCE, 0l, Constant.SYSTEMNAME, new Date());
                    }
                }
            }
            String code = order.getCode();//交易单号
            String endCode = code.substring(code.length() - codeLengthForSMS, code.length());//截取后6位
            //核算会计打回通知交易员
            SysSetting template = sysSettingService.queryBySettingType(SysSettingType.SmsTemplateCallBack.getCode());
            if (template == null) {
                throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "核算会计打回通知交易员短信模板缺失");
            }
            String content = template.getSettingValue();
            content = content.replace("#tradername#", order.getOwnerName());
            content = content.replace("#endcode#", endCode);
            content = content.replace("#applyamount#", String.valueOf(request.getTotalAmount().setScale(2, BigDecimal.ROUND_HALF_UP)));
            Nido.start(NidoTaskConstant.SEND_NOTE_MESSAGE, new NoteMessageContext(userDao.queryById(order.getOwnerId()).getTel(), content));
        } else {
            throw new BusinessException(Constant.ERROR_MISSING_REQUIRED_PARAM,"打回失败，修改订单状态失败");
        }
    }

    @Override
    public String findByOrderId(Long orderId) {
    	return consignOrderDao.findByOrderId(orderId);
    }

    /**
     * 解析关联银票票号json字符串
     * @param orderItemsList
     * @return
     */
    private List<ConsignOrderItems> jsonToOrderItems(String orderItemsList) {
        List<ConsignOrderItems> list = new ArrayList<ConsignOrderItems>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode orderNode = mapper.readTree(orderItemsList);
            ConsignOrderItems item;
            for (JsonNode node : orderNode) {
                item = new ConsignOrderItems();
                item.setId(node.path("orderItemsId").asLong());
                item.setAcceptDraftId(node.path("acceptDraftId").asLong());
                item.setAcceptDraftCode(node.path("acceptDraftCode").asText());
                list.add(item);
            }
        }catch (Exception e){
            logger.debug("解析关联银票票号json字符串错误：", e);
        }
        return list;
    }

    @Override
	public List<Map<String, Object>> appOrderList(Long userId, String date,Integer start,Integer length) {
		return consignOrderDao.appOrderList(userId, date, start, length);
	}

	@Override
    public ConsignOrder buildConsignOrderByPurchaseOrderId(Long id) {
        return consignOrderDao.buildConsignOrderByPurchaseOrderId(id);
    }

    /**
     * 根据单号查询返利金额
     * @param code
     * @author wangxianjun
     * @return
     */
    @Override
    public OrderContactDto queryOrderContactByCode(String code) {
        return consignOrderDao.queryOrderContactByCode(code);
    }
    /**
     * 根据单号更新买家联系人信息
     * @param record
     * @author wangxianjun
     * @return
     */
    @Override
    public int updateByCodeSelective(ConsignOrder record){
        return consignOrderDao.updateByCodeSelective(record);
    }
    /**
     * 调用lv返利接口
     * @param
     * @author wangxianjun
     * @return
     */
    @Override
    public int correctRebateByPhone(String oldTel,String newTel,Double rebateAmount){
        /** 超市返利接口**/
        //创建客户端代理工厂
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        //注册接口
        factory.setServiceClass(FinanceService.class);
        //设置地址
        factory.setAddress(ivFinanceServiceAddress);
        FinanceService financeService = (FinanceService) factory.create();
        return financeService.correctRebateByPhone(ivFinanceServiceKEY, oldTel, newTel, rebateAmount);
    }
    /**
     * 调用lv添加返利接口
     * @param
     * @author wangxianjun
     * @return
     */
    @Override
    public String addRebateByPhone(Double rebateAmount,String phone,String contactName,Long accountId){
        /** 超市返利接口**/
        //创建客户端代理工厂
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        //注册接口
        factory.setServiceClass(FinanceService.class);
        //设置地址
        factory.setAddress(ivFinanceServiceAddress);
        FinanceService financeService = (FinanceService) factory.create();
        Account buyer = accountDao.selectByPrimaryKey(accountId);
        return financeService.addRebate(ivFinanceServiceKEY, rebateAmount, phone, contactName,
                buyer.getName(),
                buyer.getProvinceId() == null ? null : provinceDao.selectByPrimaryKey(buyer.getProvinceId()).getName()
        );
    }
    /**
     * 根据单号删除返利报表数据
     * @param code
     * @author wangxianjun
     * @return
     */
    @Override
    public int disableRebateByOrderCode(String code){
        List<ReportRebateRecord> rebateRecordList = reportRebateRecordDao.queryByOrderCode(code);
        if(rebateRecordList.size() == 0){
            return  1;
        }
        return reportRebateRecordDao.disableRebateByOrderCode(code);
    }
    /**
     * 申请付款前，查看合同是否上传
     * @param orderId
     * @author wangxianjun
     * @return
     */
    @Override
    public int countUnUploadByOrderId(Long orderId){
        return consignOrderContractDao.countUnUploadByOrderId(orderId);
    }
    
    @Override
    public List<AccountOrderDto> queryAccount(Long invoiceId) {
    	return consignOrderDao.queryByInvoiceId(invoiceId);
    }
    
    @Override
    public InvoiceOutApplyStatusDto queryInvoiceOutApplyStatus(Long invoiceId, List<Long> orderDetailIds) {
    	return consignOrderDao.queryInvoiceOutApplyStatus(invoiceId, orderDetailIds);
    }

	@Override
	public List<OrderPayMentDto> queryFirstPayMent(OrderPayMentQuery orderPayMentQuery) {
		return consignOrderDao.queryFirstPayMent(orderPayMentQuery);
	}

	@Override
	public Integer queryFirstPayMentCount(OrderPayMentQuery orderPayMentQuery) {
		return consignOrderDao.queryFirstPayMentCount(orderPayMentQuery);
	}

	@Override
	public OrderPayMentDto totalFirstPayMent(OrderPayMentQuery orderPayMentQuery) {
		return consignOrderDao.totalFirstPayMent(orderPayMentQuery);
	}

	@Resource
	private PointService pointService;
	@Override
	public void genFebPoint() {
		//获取201602月份已经二结的订单。
		List<ConsignOrder> febOrderList = consignOrderDao.getFebOrder(); 
		for(ConsignOrder order:febOrderList){
			List<ConsignOrderItems> items = consignOrderItemsDao.selectByOrderId(order.getId());
			pointService.earnPoint(order, items);
		}
	}
	
	//通过订单ID查询二结后上传图片
    @Override
    public List<ConsignOrderAttachment> getAttachmentByOrderId(Map map){
        return consignOrderAttachmentDao.getAttachmentByOrderId(map);
    }
    /**
     * 根据订单ID删除二结后上传图片
     * @param orderId
     * @author wangxianjun
     * @return
     */
    @Override
    public int deleteAttachmentByOrderId(Long orderId){
        return consignOrderAttachmentDao.deleteByPrimaryKey(orderId);
    }

    /**
     * 二结后订单详情页面上传回单图片
     * modify by wangxianjun 20160229
     *
     * @return
     */
    @Override
    public List<ConsignOrderAttachment> updateOrderPic(List<MultipartFile> attachmentfiles, User user,Long orderId,String type) {
    	List<ConsignOrderAttachment> consignOrderAttachments = new ArrayList<>();
    	if (attachmentfiles != null) {   //如果没上传图片，则不执行插入
            for(MultipartFile file:attachmentfiles) {
                ConsignOrderAttachment attachment = saveAttachment(file, orderId, user,type,Long.valueOf("0"), true);
                consignOrderAttachments.add(attachment);
            }
        }else {
        	throw new BusinessException(Constant.ERROR_MISSING_REQUIRED_PARAM, "上传的文件为空");
        }
        return consignOrderAttachments;
    }
    
    private ConsignOrderAttachment saveAttachment(MultipartFile file, Long orderId, User user,String type,Long contractId, boolean highQuality) {
        ConsignOrderAttachment consignOrderAttachment = new ConsignOrderAttachment();
        consignOrderAttachment.setConsignOrderId(orderId);
        consignOrderAttachment.setRelateId(contractId);
        consignOrderAttachment.setCreated(new Date());
        consignOrderAttachment.setCreatedBy(user.getName());
        consignOrderAttachment.setLastUpdated(new Date());
        consignOrderAttachment.setLastUpdatedBy(user.getName());
        consignOrderAttachment.setModificationNumber(0);
        consignOrderAttachment.setType(type);

        String saveKey = "";
        String savePath  = "";

        try {
            if("合同".equals(type)){
                String urlPath = ATTACHMENTSAVEPATH + orderId + File.separator;
                String basePath = urlPath;
                String temppath = new Date().getTime() + "."
                        + FileUtil.getFileSuffix(file.getOriginalFilename());
                savePath = basePath + temppath;
            }else{
                    savePath= ATTACHMENTSAVEPATH
                            + orderId
                            + File.separator
                            + type
                            + "."
                            + FileUtil.getFileSuffix(file.getOriginalFilename());
            }
            saveKey = fileService.saveFile(
                    file.getInputStream(),
                    savePath, highQuality
                    );
            if (StringUtils.isEmpty(saveKey)) {
                throw new BusinessException(Constant.EXCEPTIONCODE_SYSTEM, "保存附件" + "出错");
            }

        } catch (IOException e) {
            throw new BusinessException(Constant.EXCEPTIONCODE_SYSTEM, "保存附件" + "出错");
        }
        consignOrderAttachment.setFileUrl(saveKey);
        if (consignOrderAttachmentDao.insertSelective(consignOrderAttachment) == 0) {
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "保存附件失败");
        }
        
        return consignOrderAttachment;
    }
    /**
     * 删除二结后订单详情页面上传的回单图片
     * modify by wangxianjun 20160229
     *
     * @return
     */
    @Override
    public ResultDto deletePic(Long imgId) {
        ResultDto result = new ResultDto();
        if (consignOrderAttachmentDao.deleteByPrimaryKey(imgId) == 0) {
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "删除附件失败");
        }else{
            result.setSuccess(true);
        }
        return result;
    }


    /**
     * 计算部门可用信用额度 = 组内可用信用额度与部门可信用额度较小值
     * @param deptId
     * @return
     */
    @Override
    public BigDecimal calculateBalanceCreditAmountOfDeptByDeptId(Long deptId) {
        Account dept = accountDao.selectByPrimaryKey(deptId);
        if (dept == null){
            return BigDecimal.ZERO;
        }
        BigDecimal deptBalanceCreditAmount = dept.getBalanceCreditAmount();
        BigDecimal groupingBalanceCreditAmount = BigDecimal.ZERO;

        //组可用信用额度 = 组信用额度 - 组内部门的所有已使用额度之和
        CustGroupingInfor groupingInfor = custGroupingInforDao.queryGroupLimitByDeptId(deptId);
        if(groupingInfor != null){
           BigDecimal groupingUsedCreditAmount = accountDao.queryCreditAmountUsedTotalByGroupingId(groupingInfor.getId());
           groupingBalanceCreditAmount = CbmsNumberUtil.buildMoney(groupingInfor.getCreditLimit()).subtract(CbmsNumberUtil.buildMoney(groupingUsedCreditAmount));
        }
        return deptBalanceCreditAmount.min(groupingBalanceCreditAmount);
    }

    @Override
    public List<OrderItemDetailDto> selectOrdersByParams(OrderTradeCertificateQuery query) {
        return consignOrderDao.selectOrdersByParams(query);
    }

    @Override
    public int selectTotalOrdersByParams(OrderTradeCertificateQuery query) {
        return consignOrderDao.selectTotalOrdersByParams(query);
    }
    /**
     *
     * @Author: wangxianjun
     * @Description: 查询设置的凭证名称
     * @Date: 2016年4月8日
     * @param cert 凭证号，id 客户id  type 客户类型
     */
    @Override
    public List<String> findCertnamesByAccount(String cert,Long id,String type){
        Long accountId = null;
        String certType ="";
        List<String> list = null;
        String[] certs = null;
        if("seller".equals(type)){
            //卖家
            accountId = Long.valueOf(id);
            certType ="卖家凭证";
        }else {
            //买家 查询买家对应的任意一个卖家id
            Map<String,Object> buyMap = new HashMap<String,Object>();
            buyMap.put("accountId",Long.valueOf(id));
            buyMap.put("cert",cert);
            accountId = consignOrderDao.findSellerIdBybuyer(buyMap);
            certType = "买家凭证";
        }
        Account account  = accountDao.selectByPrimaryKey(accountId);
        Map<String,String> certMap = new HashMap<String,String>();
        certMap.put("sellerLabel",account.getSupplierLabel());
        certMap.put("certType",certType);
        String certName = sysSettingDao.queryCertNameByLabel(certMap);
        if(!"".equals(certName) && certName != null){
            certs = certName.split("、");
            list = Arrays.asList(certs);
        }
        return  list;
    }

    @Override
    public List<OrderTradeCertificateDto> selectTradeCertificateList(OrderTradeCertificateQuery query) {
        return consignOrderDao.selectTradeCertificateList(query);
    }
    //根据凭证号查询所有对应的订单id
    @Override
    public List<Long> findOrderIdIdByCert(String cert){
        return consignOrderDao.findOrderIdIdByCert(cert);
    }
    /**
     *
     * @Author: wangxianjun
     * @Description: 查询设置的凭证名称
     * @Date: 2016年5月16日
     * @param orderId accountType 凭证类型
     */
    @Override
    public List<String> findCertnames(Long orderId,String accountType){
        String certType = "";
        if("buyer".equals(accountType)){
            certType = "买家凭证";
        }else{
            certType = "卖家凭证";
        }
        List<String> list = null;
        String[] certs = null;
        ConsignOrderItems orderItems = consignOrderItemsDao.selectByOrderId(orderId).get(0);
        Account account  = accountDao.selectByPrimaryKey(orderItems.getSellerId());
        Map<String,String> certMap = new HashMap<String,String>();
        certMap.put("sellerLabel",account.getSupplierLabel());
        certMap.put("certType",certType);
        String certName = sysSettingDao.queryCertNameByLabel(certMap);
        if(!"".equals(certName) && certName != null){
            certs = certName.split("、");
            list = Arrays.asList(certs);
        }
        return  list;
    }

    /**
     *
     * @Author: chengui
     * @Description: 按买家账户ID、订单状态查询订单信息
     * @param accountId 买家账户ID
     * @param status 订单状态
     */
    @Override
    public List<ConsignOrder> selectByAccountIdAndStatus(Long accountId, String status){
        return consignOrderDao.selectByAccountIdAndStatus(accountId, status);
    }
    /**
     *
     * @Author: wangxianjun
     * @Description: 超市用户ID查询对应的订单信息
     * @param query 超市用户ID
     */
    @Override
    public List<AppOrder> selectOrderByecUserId(OrderEcAppQuery query){
        List<AppOrder> orders = consignOrderDao.selectOrderByecUserId(query);
        if(orders.size() > 0){
            for(AppOrder appOrder : orders){
                appOrder.setPayType("null".equals(appOrder.getPayType())==true?"":appOrder.getPayType());
                List<AppOrderItems> items = consignOrderItemsDao.selectItemsByorderId(appOrder.getId());
                appOrder.setItems(items);
            }
        }
        return orders;
    }
    /**
     *
     * @Author: wangxianjun
     * @Description: 超市用户ID查询对应的订单数量
     * @param query 超市用户ID
     */
    @Override
    public int countOrderByecUserId(OrderEcAppQuery query){
        return consignOrderDao.countOrderByecUserId(query);
    }
    /**
     *
     * @Author: wangxianjun
     * @Description:订单ID查询对应的订单
     * @param orderIds 订单列表
     */
    @Override
    public List<EcOrder> selectByOrderIds(List<Long> orderIds){
        List<EcOrder> orders = consignOrderDao.selectEcOrdersByOrderIds(orderIds);
        if(orders.size() > 0){
            for(EcOrder ecOrder : orders){
                List<EcOrderItems> items = consignOrderItemsDao.selectEcItemsByorderId(Long.valueOf(ecOrder.getRemoteCode()));
                items.forEach(i -> {
                    if (!"".equals(i.getSpec1()) && i.getSpec1() != null) {
                        String[] spec = i.getSpec1().split("\\*");
                        if (spec.length == 1)
                            i.setSpec1(spec[0]);
                        if (spec.length == 2) {
                            i.setSpec1(spec[0]);
                            i.setSpec2(spec[1]);
                        }
                        if (spec.length > 2) {
                            i.setSpec1(spec[0]);
                            i.setSpec2(spec[1]);
                            i.setSpec3(spec[2]);
                        }

                    }

                });
                ecOrder.setAmount(new BigDecimal(items.stream().mapToDouble(a -> a.getAmount().doubleValue()).sum()).setScale(2, BigDecimal.ROUND_HALF_UP));
                ecOrder.setItems(items);
            }
        }
        return orders;
    }
    /**
     *
     * @Author: wangxianjun
     * @Description: 超市用户ID查询对应的订单条数
     * @param ecUserId 超市用户ID
     */
    @Override
    public CountOrder countEcOrder(Integer ecUserId){
        CountOrder order = consignOrderDao.countEcOrder(ecUserId);
        return order;
    }
    /**
     * 申请付款前，查看变更合同是否上传
     * @param orderId
     * @author wangxianjun
     * @return
     */
    @Override
    public int countUnChangeUploadByOrderId(Long orderId,Integer orderChangId){
        return consignOrderContractDao.countUnChangeUploadByOrderId(orderId,orderChangId);
    }
}


	

