package com.prcsteel.platform.order.service.invoice.impl;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prcsteel.platform.account.model.enums.InvoiceDataStatus;
import com.prcsteel.platform.account.model.model.Account;
import com.prcsteel.platform.account.persist.dao.AccountDao;
import com.prcsteel.platform.account.service.AccountService;
import com.prcsteel.platform.acl.model.model.Organization;
import com.prcsteel.platform.acl.model.model.SysSetting;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.acl.model.query.SysSettingQuery;
import com.prcsteel.platform.acl.persist.dao.OrganizationDao;
import com.prcsteel.platform.acl.persist.dao.SysSettingDao;
import com.prcsteel.platform.acl.persist.dao.UserDao;
import com.prcsteel.platform.acl.service.OrganizationService;
import com.prcsteel.platform.acl.service.SysSettingService;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.constants.Constant.ControlPinSettings;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.common.utils.CbmsNumberUtil;
import com.prcsteel.platform.core.model.model.CategoryAlias;
import com.prcsteel.platform.core.model.query.CategoryAliasQuery;
import com.prcsteel.platform.core.persist.dao.CategoryAliasDao;
import com.prcsteel.platform.order.model.dto.AccountInvoiceNoPassDto;
import com.prcsteel.platform.order.model.dto.AccountOrderDto;
import com.prcsteel.platform.order.model.dto.AccountSendDto;
import com.prcsteel.platform.order.model.dto.AllowanceOrderItemsDto;
import com.prcsteel.platform.order.model.dto.IncreaseInvoiceInDto;
import com.prcsteel.platform.order.model.dto.InvoiceInAllowanceItemDto;
import com.prcsteel.platform.order.model.dto.InvoiceInDetailDto;
import com.prcsteel.platform.order.model.dto.InvoiceInDetailFormDto;
import com.prcsteel.platform.order.model.dto.InvoiceInDetailOrderitemFormDto;
import com.prcsteel.platform.order.model.dto.InvoiceInDto;
import com.prcsteel.platform.order.model.dto.InvoiceInUpdateDto;
import com.prcsteel.platform.order.model.dto.InvoiceKeepingDto;
import com.prcsteel.platform.order.model.dto.InvoiceOutApplyStatusDto;
import com.prcsteel.platform.order.model.dto.PoolInTotalModifier;
import com.prcsteel.platform.order.model.enums.AllowanceStatus;
import com.prcsteel.platform.order.model.enums.AllowanceType;
import com.prcsteel.platform.order.model.enums.InvoiceInDetailStatus;
import com.prcsteel.platform.order.model.enums.InvoiceInStatus;
import com.prcsteel.platform.order.model.enums.InvoiceOutApplyStatus;
import com.prcsteel.platform.order.model.enums.InvoiceType;
import com.prcsteel.platform.order.model.enums.PrintStatus;
import com.prcsteel.platform.order.model.enums.ReportSellerInvoiceInOperationType;
import com.prcsteel.platform.order.model.model.ConsignOrderItems;
import com.prcsteel.platform.order.model.model.Express;
import com.prcsteel.platform.order.model.model.InvoiceIn;
import com.prcsteel.platform.order.model.model.InvoiceInAllowance;
import com.prcsteel.platform.order.model.model.InvoiceInDetail;
import com.prcsteel.platform.order.model.model.InvoiceInDetailOrderItem;
import com.prcsteel.platform.order.model.model.InvoiceInFlowLog;
import com.prcsteel.platform.order.model.model.PoolIn;
import com.prcsteel.platform.order.model.model.PoolInDetail;
import com.prcsteel.platform.order.model.model.PoolInIdAndInvoiceInDetail;
import com.prcsteel.platform.order.model.query.AccountInvoiceNoPassQuery;
import com.prcsteel.platform.order.model.query.AllowanceDetailItemQuery;
import com.prcsteel.platform.order.model.query.InvoiceInQuery;
import com.prcsteel.platform.order.model.query.InvoiceKeepingQuery;
import com.prcsteel.platform.order.persist.dao.AllowanceOrderDetailItemDao;
import com.prcsteel.platform.order.persist.dao.ExpressDao;
import com.prcsteel.platform.order.persist.dao.InvoiceInDao;
import com.prcsteel.platform.order.persist.dao.InvoiceInDetailDao;
import com.prcsteel.platform.order.persist.dao.InvoiceInDetailOrderitemDao;
import com.prcsteel.platform.order.persist.dao.InvoiceInFlowLogDao;
import com.prcsteel.platform.order.persist.dao.PoolInDao;
import com.prcsteel.platform.order.service.SysConfigService;
import com.prcsteel.platform.order.service.invoice.InvoiceInAllowanceItemService;
import com.prcsteel.platform.order.service.invoice.InvoiceInAllowanceService;
import com.prcsteel.platform.order.service.invoice.InvoiceInDetailService;
import com.prcsteel.platform.order.service.invoice.InvoiceInService;
import com.prcsteel.platform.order.service.invoice.InvoiceOutApplyService;
import com.prcsteel.platform.order.service.invoice.PoolInDetailService;
import com.prcsteel.platform.order.service.invoice.PoolInService;
import com.prcsteel.platform.order.service.order.ConsignOrderItemsService;
import com.prcsteel.platform.order.service.order.ConsignOrderService;
import com.prcsteel.platform.order.service.report.ReportFinanceService;

/**
 * Created by lcw on 2015/8/1.
 */
@Service("invoiceInService")
@Transactional
public class InvoiceInServiceImpl implements InvoiceInService {

    private static final Logger logger = LoggerFactory.getLogger(InvoiceInServiceImpl.class);

    @Resource
    private InvoiceInDao invoiceInDao;
    @Resource
    private InvoiceInDetailDao invoiceInDetailDao;
    @Resource
    private InvoiceInDetailOrderitemDao invoiceInDetailOrderitemDao;
    @Resource
    private OrganizationService organizationService;
    @Resource
    private ExpressDao expressDao;
    @Resource
    private PoolInDao poolInDao;
    @Resource
    private AccountDao accountDao;
    @Resource
    private UserDao userDao;
    @Resource
    private OrganizationDao organizationDao;
    @Resource
    private ConsignOrderItemsService consignOrderItemsService;
    @Resource
    private PoolInService poolInService;
    @Resource
    private InvoiceInDetailService invoiceInDetailService;
    @Resource
    private SysConfigService sysConfigService;
    @Resource
    private PoolInDetailService poolInDetailService;
    @Resource
    private InvoiceOutApplyService invoiceOutApplyService;
    @Resource
    private InvoiceInAllowanceService inAllowanceService;
    @Resource
    private InvoiceInAllowanceItemService inAllowanceItemService;
    @Resource
    ReportFinanceService reportFinanceService;
    @Resource
    ConsignOrderService consignOrderService;
    @Resource
    AccountService accountService;
    @Resource
    SysSettingService sysSettingService;
    @Resource
    InvoiceInFlowLogDao invoiceInFlowLogDao;
    @Resource
    CategoryAliasDao categoryAliasDao;
    @Resource
    SysSettingDao sysSettingDao;
    @Resource
    AllowanceOrderDetailItemDao allowanceOrderDetailItemDao;
    
    private final String type = "InvoiceOutApplySecond";
    
    @Override
    public int deleteByPrimaryKey(Long id) {
        return invoiceInDao.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(InvoiceIn record) {
        return invoiceInDao.insert(record);
    }

    @Override
    public int insertSelective(InvoiceIn record) {
        return invoiceInDao.insertSelective(record);
    }

    @Override
    public InvoiceIn selectByPrimaryKey(Long id) {
        return invoiceInDao.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(InvoiceIn record) {
        return invoiceInDao.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(InvoiceIn record) {
        return invoiceInDao.updateByPrimaryKey(record);
    }

    /**
     * 根据发票号查询发票
     *
     * @param invoiceCode 发票号
     * @return
     */
    @Override
    public InvoiceIn selectByCode(String areaCode, String invoiceCode) {
        return invoiceInDao.selectByCode(areaCode, invoiceCode);
    }

    /**
     * 录入发票
     *
     * @param user               操作人
     * @param invoiceIn          发票
     * @param list               发票详情
     * @param invoiceInAllowance 折让
     * @param listAllowanceItem  折让详情
     * @return
     */
    @Override
    @Transactional
    public void inputInvoice(User user, InvoiceIn invoiceIn, List<InvoiceInDetailFormDto> list,
                             InvoiceInAllowance invoiceInAllowance, List<InvoiceInAllowanceItemDto> listAllowanceItem, Boolean isCheck) {
    	boolean isInAddModel = invoiceIn.getId() == null || invoiceIn.getId() == 0;
        Organization organization = checkInputParams(user, invoiceIn, isInAddModel, isCheck);
        // 查询发票信息
        PoolIn poolIn = null;
        List<PoolIn> poolInList = poolInService.queryBySellerIds(Arrays.asList(invoiceIn.getSellerId()));
        poolIn = poolInList.stream().filter(a->a.getDepartmentId().compareTo(invoiceIn.getDepartmentId())==0).findFirst().get();
        // 由于进项票池的数据是根据部门保存的，当一张发票包括多个部门的发票时，会超出该部门的总应收，所以用该公司的应收总额
        BigDecimal totalSellerAmount=new BigDecimal(poolInList.stream().mapToDouble(item -> item.getTotalAmount().doubleValue()).sum());
        BigDecimal totalSellerWeight=new BigDecimal(poolInList.stream().mapToDouble(item -> item.getTotalWeight().doubleValue()).sum());
        poolIn.setTotalAmount(totalSellerAmount);
        poolIn.setTotalWeight(totalSellerWeight);

        //填充发票信息
        fillInvoiceIn(user, invoiceIn, organization, list,invoiceInAllowance);
        //检查金额是否合法
        PoolInTotalModifier poolInModifier = checkAmountValidity(invoiceIn, isInAddModel, poolIn, list);

        if (isInAddModel) {
            // 插入发票表
            invoiceInDao.insertSelective(invoiceIn);

            // 插入发票详情表
            addInvoiceDetail(user, invoiceIn, list);
            
            // 插入关联信息
            addInvoiceDetailOrderitems(user, list, listAllowanceItem);

            // 更新订单详情，增加已开重量和金额
            updateOrderitems(list, user,listAllowanceItem);

            // 使用了折让单
            if (invoiceInAllowance != null && invoiceInAllowance.getAmount().compareTo(BigDecimal.ZERO) != 0) {
                // 添加折让关联关系
                addInvoiceInAllowance(user, invoiceIn, invoiceInAllowance);
                // 添加折让详情关联关系
                addListAllowanceItem(user, invoiceInAllowance, listAllowanceItem);
            }
            //lixiang 插入流水日志
            try {
            	insertFlowLog(user, invoiceIn.getStatus(), invoiceIn.getId());
            } catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
        }else{
        	InvoiceIn dbInvoice = invoiceInDao.selectByPrimaryKey(invoiceIn.getId());
        	if(dbInvoice == null){
        		throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "找不到发票信息");
        	}
        	
			//修改发票待寄出界面并发控制
            if (!isCheck && !StringUtils.equals(dbInvoice.getStatus(),InvoiceInStatus.RECEIVED.getCode())){
                throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "状态已更改，请刷新重试");
            }

        	// 更新发票详情及新增关联关系
        	List<InvoiceInDetailFormDto> newAddOrderItemForm = new LinkedList<>();
			updateDetailAndAddRelations(user, list, dbInvoice, newAddOrderItemForm, isCheck, listAllowanceItem,poolInModifier);

        	// 更新订单详情，增加/减少已开重量和金额
			updateOrderitems(newAddOrderItemForm, user,listAllowanceItem);
			
			// 更新发票信息
        	updateInvoiceInfo(user, invoiceIn, dbInvoice,isCheck);
        	try {
        		//如果在待确认那做了修改取消关联，视为做了打回操作,如果在待认证那被财务打回到待确认，而待确认做了修改取消关联，视为操作打回，如没做修改就提交到待认证，此时认为没有做过打回操作。
//				if ("toberelation".equals(dbInvoice.getRelationStatus())) {
//					insertFlowLog(user, InvoiceInStatus.ROLLBACK.toString(), dbInvoice.getId());
//				}
				//lixiang 插入流水日志
	            insertFlowLog(user, dbInvoice.getStatus(), dbInvoice.getId());
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
            // 删除原折让关系
            inAllowanceService.deleteByInvoiceInId(invoiceIn.getId(), user.getName());

            // 使用了折让单
            if (invoiceInAllowance != null && invoiceInAllowance.getAmount().compareTo(BigDecimal.ZERO) != 0) {
                // 添加折让关联关系
                addInvoiceInAllowance(user, invoiceIn, invoiceInAllowance);
                if (listAllowanceItem != null && listAllowanceItem.size() > 0) {
                    // 添加折让详情关联关系
                    addListAllowanceItem(user, invoiceInAllowance, listAllowanceItem);
                }
            }
        }
        
        // 更新发票已收总额信息
    	poolInModifier.setOriginalAmount(poolIn.getTotalReceivedAmount().doubleValue());
    	poolInModifier.setOriginalWeight(poolIn.getTotalReceivedWeight().doubleValue());
    	poolInService.modifyTotalReceived(poolInModifier);
    }
    
    private void insertFlowLog(User user, String status, Long invoiceId) {
    	InvoiceInFlowLog invoiceInFlowLog = new InvoiceInFlowLog();
    	invoiceInFlowLog.setCreated(new Date());
		invoiceInFlowLog.setCreatedBy(user.getName());
		invoiceInFlowLog.setInvoiceId(invoiceId);
		invoiceInFlowLog.setOperatorId(user.getId());
		invoiceInFlowLog.setOperatorName(user.getName());
		invoiceInFlowLog.setInvoiceStatus(status);
		invoiceInFlowLog.setLastUpdated(new Date());
		invoiceInFlowLog.setLastUpdatedBy(user.getName());
		invoiceInFlowLog.setModificationNumber(1);
		invoiceInFlowLogDao.insert(invoiceInFlowLog);
    }

    /**
     * 添加折让关联关系
     *
     * @param user               操作人
     * @param invoiceIn          发票
     * @param invoiceInAllowance 折让
     */
    private void addInvoiceInAllowance(User user, InvoiceIn invoiceIn, InvoiceInAllowance invoiceInAllowance) {
        invoiceInAllowance.setAmount(invoiceInAllowance.getTaxAmount().add(invoiceInAllowance.getNoTaxAmount()));
        invoiceInAllowance.setInvoiceInId(invoiceIn.getId());
        invoiceInAllowance.setCreated(new Date());
        invoiceInAllowance.setCreatedBy(user.getName());
        invoiceInAllowance.setLastUpdated(new Date());
        invoiceInAllowance.setLastUpdatedBy(user.getName());
        int flag = inAllowanceService.insertSelective(invoiceInAllowance);
        if (flag == 0) {
            throw new BusinessException(Constant.ERROR_RESOURCE_NOT_FOUND, "添加折让关联数据失败");
        }

    }

    /**
     * 添加折让关联详情关系
     *
     * @param user               操作人
     * @param invoiceInAllowance 折让
     * @param listAllowanceItem  折让详情
     */
    private void addListAllowanceItem(User user, InvoiceInAllowance invoiceInAllowance, List<InvoiceInAllowanceItemDto> listAllowanceItem) {
        listAllowanceItem.forEach(item -> {
            item.setInvoiceInAllowanceId(invoiceInAllowance.getId());
            item.setCreated(new Date());
            item.setCreatedBy(user.getName());
            item.setLastUpdated(new Date());
            item.setLastUpdatedBy(user.getName());
            int flag = inAllowanceItemService.insertSelective(item);
            if (flag == 0) {
                throw new BusinessException(Constant.ERROR_RESOURCE_NOT_FOUND, "添加折让关联详情数据失败");
            }
        });
    }

    private void updateInvoiceInfo(User user, InvoiceIn invoiceIn, InvoiceIn dbInvoice,boolean isCheck) {
    	
    	dbInvoice.setMemo(invoiceIn.getMemo());
		if (!isCheck && StringUtils.equals(dbInvoice.getStatus(), InvoiceInStatus.RECEIVED.getCode())) {
            //待寄出状态
            dbInvoice.setCode(invoiceIn.getCode());
            dbInvoice.setInvoiceDate(invoiceIn.getInvoiceDate());
            dbInvoice.setAreaCode(invoiceIn.getAreaCode());
            dbInvoice.setTotalWeight(invoiceIn.getTotalWeight());
            dbInvoice.setTotalAmount(invoiceIn.getTotalAmount());
            dbInvoice.setRelationStatus(InvoiceInDetailStatus.HasRelation.getCode());
        } else {
			dbInvoice.setStatus(InvoiceInStatus.WAIT.getCode());
			if(StringUtils.isBlank(dbInvoice.getCheckUserName())){
				// 财务确认操作
				dbInvoice.setCheckUserId(user.getId());
				dbInvoice.setCheckUserMobil(user.getTel());
				dbInvoice.setCheckUserName(user.getName());
				dbInvoice.setCheckDate(new Date());
				//#9515 待确认的核对进项发票页面，修改区位码及发票号No字段值，点击提交后，这两个字段值没有修改成功 add afeng
				dbInvoice.setCode(invoiceIn.getCode());
	            dbInvoice.setAreaCode(invoiceIn.getAreaCode());
				
			}
			dbInvoice.setCheckTotalAmount(invoiceIn.getTotalAmount());
			dbInvoice.setCheckTotalWeight(invoiceIn.getTotalWeight());
			
			// 供应商进项报表踩点 - 进项票财务确认踩点 和 进项票重新关联踩点
        	if(InvoiceInStatus.WAIT.getCode().equals(dbInvoice.getStatus())
        			&& InvoiceInDetailStatus.HasRelation.getCode().equals(dbInvoice.getRelationStatus())) {
        		reportFinanceService.invoiceOperation(ReportSellerInvoiceInOperationType.InvoiceIn.getOperation(), invoiceIn.getId(),user );
        	}
		}
		this.updateByPrimaryKey(dbInvoice);
	}
	
	private boolean isChanged(InvoiceInDetail db,InvoiceInDetailFormDto input){
		double amount = db.getAmount().doubleValue();
		double weight = db.getWeight().doubleValue();
		double taxAmount = db.getTaxAmount().doubleValue();
		double noTaxAmount = db.getNoTaxAmount().doubleValue();
		if(db.getCheckAmount() != null && db.getCheckAmount().doubleValue() > 0){
			amount = db.getCheckAmount().doubleValue();
			weight = db.getCheckWeight().doubleValue();
			taxAmount = db.getCheckTaxAmount().doubleValue();
			noTaxAmount = db.getCheckNoTaxAmount().doubleValue();
		}
		if(amount != input.getAmount()||
				weight != input.getWeight() ||
				taxAmount != input.getTaxAmount() ||
				noTaxAmount != input.getNoTaxAmount() ||
				!db.getNsortName().equals(input.getNsortName()) ||
				!db.getMaterial().equals(input.getMaterial()) ||
				!db.getNsortNameComb().equals(input.getNsortNameComb()) ||
				!db.getSpec().equals(input.getSpec()) ||
				!db.getTypeOfSpec().equals(input.getTypeOfSpec())){
			return true;
		}
		return false;
	}
	private Organization checkInputParams(User user, InvoiceIn invoiceIn, boolean isInAddModel, boolean isCheck) {
		InvoiceIn dbInvoiceIn = selectByCode(invoiceIn.getAreaCode(), invoiceIn.getCode());
        if (isInAddModel && dbInvoiceIn != null) {
            throw new BusinessException(Constant.ERROR_MISSING_REQUIRED_PARAM, "发票号已存在");
        }

        //在待寄出修改发票时，本身需排除检查
        if (!isInAddModel && !isCheck && dbInvoiceIn != null && !dbInvoiceIn.getId().equals(invoiceIn.getId())) {
            throw new BusinessException(Constant.ERROR_MISSING_REQUIRED_PARAM, "发票号已存在");
        }
        Organization organization = organizationService.queryById(user.getOrgId());
        if (organization == null) {
            throw new BusinessException(Constant.ERROR_RESOURCE_NOT_FOUND, "找不到服务中心");
        }
        return organization;
    }

    private PoolInTotalModifier checkAmountValidity(InvoiceIn invoiceIn, boolean isInAddModel, PoolIn poolIn, List<InvoiceInDetailFormDto> list) {
        double needChangeAmount = invoiceIn.getTotalAmount().doubleValue();
        double needChangeWeight = invoiceIn.getTotalWeight().doubleValue();
        if (!isInAddModel) {
            InvoiceIn dbInvoice = invoiceInDao.selectByPrimaryKey(invoiceIn.getId());
            if (dbInvoice.getCheckTotalAmount() != null && dbInvoice.getCheckTotalAmount().doubleValue() > 0) {
                needChangeAmount = invoiceIn.getTotalAmount().subtract(dbInvoice.getCheckTotalAmount()).doubleValue();
                needChangeWeight = invoiceIn.getTotalWeight().subtract(dbInvoice.getCheckTotalWeight()).doubleValue();
            } else {
                needChangeAmount = invoiceIn.getTotalAmount().subtract(dbInvoice.getTotalAmount()).doubleValue();
                needChangeWeight = invoiceIn.getTotalWeight().subtract(dbInvoice.getTotalWeight()).doubleValue();
            }
        }
        double alloWeightDeviation = CbmsNumberUtil.formatWeight(sysConfigService.getDouble(Constant.INVOICE_IN_ALLOW_WEIGHT_DEVIATION));
        double allowAmountDeviation = CbmsNumberUtil.formatMoney(sysConfigService.getDouble(Constant.INVOICE_IN_ALLOW_AMOUNT_DEVIATION));

        PoolInTotalModifier md = new PoolInTotalModifier();
        md.setSellerId(invoiceIn.getSellerId());
        md.setDepartmentId(invoiceIn.getDepartmentId());
        md.setChangeWeight(needChangeWeight);
        md.setChangeAmount(needChangeAmount);
        double deviation = CbmsNumberUtil.formatMoney(poolIn.getTotalAmount().subtract(poolIn.getTotalReceivedAmount()).subtract(BigDecimal.valueOf(md.getChangeAmount())));
        if (deviation < 0 && Math.abs(deviation) > allowAmountDeviation) {
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "录入发票金额大于该卖家未开票金额");
        }
        deviation = CbmsNumberUtil.formatWeight(poolIn.getTotalWeight().subtract(poolIn.getTotalReceivedWeight()).subtract(BigDecimal.valueOf(md.getChangeWeight())));
        if (deviation < 0 && Math.abs(deviation) > alloWeightDeviation) {
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "录入发票重量大于该卖家未开票重量");
        }

        // 暂不考虑进项详情的容差校验，后续需要再加
        //  list.forEach(detail -> {
        //
        //  });
    	return md;
	}

	private void updateDetailAndAddRelations(User user,List<InvoiceInDetailFormDto> list, 
			InvoiceIn dbInvoice,List<InvoiceInDetailFormDto> newAddDetailForm, boolean isCheck, List<InvoiceInAllowanceItemDto> listAllowanceItem,PoolInTotalModifier poolInModifier) {
		//获取发票关联的所有详情信息
		List<InvoiceInDetail> dbDetails = invoiceInDetailService.selectDetailByInvoiceInId(dbInvoice.getId());
		if(dbDetails.size() != list.size() && isCheck){
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "修改中只允许修改发票条目中的值，不允许添加/删除条目");
		}
		List<Long> detailIds = list.stream().map(a -> a.getInvoiceDetailId()).collect(Collectors.toList());
		List<InvoiceInDetailOrderItem> detailItems = invoiceInDetailOrderitemDao.selectByDetailIds(detailIds);

		List<InvoiceInDetailOrderItem> newAddOrderitemList = new LinkedList<>();
		boolean[] invoiceOutNegatived = {false};
		//待寄出修改发票
        if (!isCheck){
            //删除进项发票详情 取消订单关联关系  删除相关联的销项票 日志记录删除的进项发票详情
            int  deleteCount = deleteInvoiceDetailAndRelations(user,list,dbInvoice,dbDetails);
            invoiceOutNegatived[0] = deleteCount > 0;//判断是否删除过进项发票详情及相关联的销项票
        }

		list.forEach(item -> {
            long dbRelationCount = detailItems.stream().filter(a -> a.getInvoiceDetailId().equals(item.getInvoiceDetailId())).count();
            long inputRelationCount = item.getOrderItems() == null ? 0 : item.getOrderItems().size();
            boolean isUnbind = false;//解绑标志

            //新增的 插入发票详情表 添加关联关系(后面逻辑有做)
            if (!isCheck && (item.getInvoiceDetailId() == null || item.getInvoiceDetailId().equals(0))) {
                // 插入发票详情表
                addInvoiceDetail(user, dbInvoice, item);
            } else {
                //对比是否有变动
                InvoiceInDetail dbDetail = dbDetails.stream().filter(a -> a.getId().equals(item.getInvoiceDetailId())).findFirst().get();
                List<InvoiceInDetailOrderItem> currentDbDetailItems = detailItems.stream().filter(a -> a.getInvoiceDetailId().equals(item.getInvoiceDetailId())).collect(Collectors.toList());

                // 数据库中有关联关系，而提交过来的数据没有关联关系，则需要取消掉关联
                if ((isCheck && isChanged(dbDetail, item)) || isChangedRelations(currentDbDetailItems, item.getOrderItems(), listAllowanceItem)) {
                    if (!invoiceOutNegatived[0]) {
                        // 删除相关联的销项票
                        invoiceOutApplyService.invoiceInNegative(dbInvoice.getId(), user);
                        invoiceOutNegatived[0] = true;
                    }

                    //设置解绑
                    isUnbind = true;

                    // 取消关联
                    invoiceInDetailService.unbindOrderitemByDetailIds(Arrays.asList(dbDetail.getId()), user);
                    dbInvoice.setRelationStatus(InvoiceInDetailStatus.ToBeRelation.getCode());

                    // 进项票池减去取消关联的重量与金额
                    poolInModifier.setChangeWeight(poolInModifier.getChangeWeight() - dbDetail.getWeight().doubleValue());
                    poolInModifier.setChangeAmount(poolInModifier.getChangeAmount() - dbDetail.getAmount().doubleValue());
                }

                InvoiceInDetail upd = new InvoiceInDetail();
                upd.setId(item.getInvoiceDetailId());
                upd.setLastUpdatedBy(user.getName());

                //插入别名映射
                //如果categoryId为空，则说明前台没有传大类过来
            	//一般出现的情况是：老数据：
            	//	 在没有这个别名映射之前已经录入了进项票，在有这个功能之后再修改进项票，但是没有修改别名的时候。就会出现这个问题categoryId为null
            	//	目前主要表现在：当我修改老数据的备注的时候，因为老数据本身没有categoryId,所以category为空，这种情况下会报错
                if(item.getCategoryId()!=null){
                	addCategoryAlias(user, item);
                	upd.setAliasId(item.getAliasId());
                }
                
                if (isCheck) {
                    upd.setCheckAmount(new BigDecimal(item.getAmount()));
                    upd.setCheckNoTaxAmount(new BigDecimal(item.getNoTaxAmount()));
                    upd.setCheckTaxAmount(new BigDecimal(item.getTaxAmount()));
                    upd.setCheckWeight(new BigDecimal(item.getWeight()));
                } else {
                    upd.setAmount(new BigDecimal(item.getAmount()));
                    upd.setNoTaxAmount(new BigDecimal(item.getNoTaxAmount()));
                    upd.setTaxAmount(new BigDecimal(item.getTaxAmount()));
                    upd.setWeight(new BigDecimal(item.getWeight()));
                }
                upd.setMaterial(item.getMaterial());
                upd.setNsortName(item.getNsortName());
                upd.setSpec(item.getSpec());
                upd.setNsortNameComb(item.getNsortNameComb());
                upd.setTypeOfSpec(item.getTypeOfSpec());
                invoiceInDetailDao.updateByPrimaryKeySelective(upd);
            }

            // 如果数据库中没有关联关系，且提交过来的数据有关联关系，则需要将新的关联数据添加到数据库中，解绑在前面已经做了
            if (inputRelationCount > 0 && (dbRelationCount == 0 || isUnbind)) {
                buildDetailOrderItem(user, newAddOrderitemList, item, listAllowanceItem);
                newAddDetailForm.add(item);
            }
        });

		if(newAddOrderitemList.size() > 0){
			invoiceInDetailOrderitemDao.batchInsert(newAddOrderitemList);
		}

		// 更新发票状态
		long noRelatedCount = list.stream().filter(a -> a.getOrderItems() == null || a.getOrderItems().size() == 0).count();
		if(noRelatedCount > 0){
			dbInvoice.setRelationStatus(InvoiceInDetailStatus.ToBeRelation.getCode());
		}else if(newAddOrderitemList.size() > 0){
			dbInvoice.setRelationStatus(InvoiceInDetailStatus.HasRelation.getCode());
		}

	}
	
	private int deleteInvoiceDetailAndRelations(User user, List<InvoiceInDetailFormDto> list, InvoiceIn dbInvoice,List<InvoiceInDetail> dbDetails) {
        //取消关联 删除相关联的销项票 删除详情
        List<Long> needDeteledDetailIds = dbDetails.stream().map(a -> a.getId()).distinct().collect(Collectors.toList());
        List<Long> inputDetailIds = list.stream().map(b -> b.getInvoiceDetailId()).collect(Collectors.toList());
        needDeteledDetailIds.removeIf(a -> inputDetailIds.contains(a));

        if (needDeteledDetailIds != null && needDeteledDetailIds.size() > 0) {
            //记录待删除的内容
            logDeteleDetailItems(needDeteledDetailIds,dbDetails);

            //取消关联
            invoiceInDetailService.unbindOrderitemByDetailIds(needDeteledDetailIds, user);

            // 删除相关联的销项票
            invoiceOutApplyService.invoiceInNegative(dbInvoice.getId(), user);

            int deleteCount = invoiceInDetailService.deleteByIds(needDeteledDetailIds);
            if (deleteCount != needDeteledDetailIds.size()) {
                throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "删除进项发票详情条目失败");
            }
            return deleteCount;
        }
        return 0;
    }

    private void logDeteleDetailItems(List<Long> needDeteledDetailIds,List<InvoiceInDetail> dbDetails){
        needDeteledDetailIds.forEach(detailId ->
                        logger.info("删除的进项详情项数据,进项票详情id:" + detailId + "详细数据:" + dbDetails.stream().filter(b -> detailId.equals(b.getId())).findFirst().get().toString())
        );
    }


    /*
     * 检查关联是否改变(用来判断是否需要解绑)
     * @param dbDetailItems 数据库中的关联订单
     * @param detailItems   提交过来的关联订单
     * @return true 改变 false 未改变
     */
    private boolean isChangedRelations(List<InvoiceInDetailOrderItem> dbDetailItems,
    		List<InvoiceInDetailOrderitemFormDto> detailItems, 
    		List<InvoiceInAllowanceItemDto> listAllowanceItem){
        long inputSize = (detailItems == null ? 0 : detailItems.size());
        long dbSize = (dbDetailItems == null ? 0 : dbDetailItems.size());

        if(dbSize > 0){
            if(dbSize != inputSize){
                return true;
            }else{
                //删除掉相同资源(资源项id重量金额)
                dbDetailItems.removeIf(
                		a -> detailItems.stream().anyMatch(
            				b -> {
            					//这里加上折让金额 add by 20160616
            					double allowanceAmount = 0;
            					if(listAllowanceItem!=null && listAllowanceItem.size()>0){
            						allowanceAmount = listAllowanceItem.stream()
            								.filter(item -> item.getOrderItemId().equals(a.getOrderitemId()))
            								.mapToDouble(item -> item.getAllowanceAmount().doubleValue()).sum();
            					}
            					
            					return a.getOrderitemId().equals(b.getOrderitemId()) 
            							&& a.getWeight().doubleValue() == b.getIncreaseWeight().doubleValue() 
    									&& a.getAmount().doubleValue() == b.getIncreaseAmount().doubleValue()+allowanceAmount;
            				}
            			)
                );
                return dbDetailItems.size() > 0;
            }
        }
        return false;
    }
	
	private void updateOrderitems(List<InvoiceInDetailFormDto> list,User user,List<InvoiceInAllowanceItemDto> listAllowanceItem) {
		List<IncreaseInvoiceInDto> increaseInvoiceList = new LinkedList<>();
		List<Long> invDetIds = list.stream().map(a -> a.getInvoiceDetailId()).collect(Collectors.toList());
		if(invDetIds != null && invDetIds.size() > 0) {
            List<PoolInIdAndInvoiceInDetail> poolInDetails = poolInDao.queryByInvoiceInDetailIds(invDetIds);

            List<PoolInDetail> modifierList = new ArrayList<>();
            list.stream().forEach(item -> {
                Optional<PoolInIdAndInvoiceInDetail> pid = poolInDetails.stream().filter(a -> a.getInvoiceInDetailId().equals(item.getInvoiceDetailId())).findFirst();
                Long poolInId = pid.get().getPoolInId();
                List<Long> itemIds = item.getOrderItems().stream().map(a -> a.getOrderitemId()).collect(Collectors.toList());
                List<ConsignOrderItems> orderItems = consignOrderItemsService.queryByIds(itemIds);

                item.getOrderItems().stream().forEach(ordItem -> {
                    IncreaseInvoiceInDto updInvoice = new IncreaseInvoiceInDto();
                    updInvoice.setOrderItemId(ordItem.getOrderitemId());
                    // 加上折让金额
                    BigDecimal allowanceAmount = BigDecimal.ZERO;
                    if (listAllowanceItem != null && listAllowanceItem.size() > 0) {
                        Optional<InvoiceInAllowanceItemDto> allowanceItem = listAllowanceItem.stream().filter(a -> a.getOrderItemId().equals(ordItem.getOrderitemId())).findFirst();
                        if (allowanceItem.isPresent()) {
                            allowanceAmount = allowanceItem.get().getAllowanceAmount();
                        }
                    }
                    updInvoice.setGoalAmount(ordItem.getOriginalAmount() + ordItem.getIncreaseAmount() + allowanceAmount.doubleValue());
                    updInvoice.setGoalWeight(ordItem.getOriginalWeight() + ordItem.getIncreaseWeight());
                    updInvoice.setOriginalAmount(ordItem.getOriginalAmount());
                    updInvoice.setOriginalWeight(ordItem.getOriginalWeight());
                    increaseInvoiceList.add(updInvoice);

                    // 合并相同详情的更新
                    mergeIfNecessary(user, poolInId, orderItems, modifierList, ordItem, allowanceAmount);
                });
            });
			modifierList.forEach(m -> poolInDetailService.modifyPoolinDetailReceivedAmount(m));
		}
		increaseInvoiceList.sort((a, b) -> a.getOriginalWeight().compareTo(b.getOriginalWeight()));
		increaseInvoiceList.forEach(updInvoice -> {
            if (consignOrderItemsService.increaseInvoiceIn(updInvoice) != 1) {
                logger.info("consign order item's invoiceAmount or invoiceWeight was changed,add invoice in canceled.");
                throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "发票项的订单详情的已开金额已发生变化，请重新关联后再提交");
            }
        });

    }

	private void mergeIfNecessary(User user, Long poolInId, List<ConsignOrderItems> orderItems,
			List<PoolInDetail> modifierList, InvoiceInDetailOrderitemFormDto ordItem, BigDecimal allowanceAmount) {
		ConsignOrderItems coi = orderItems.stream().filter(a -> a.getId().equals(ordItem.getOrderitemId())).findFirst().get();
		Optional<PoolInDetail> pd = modifierList.stream().filter(a -> a.getPoolInId().equals(poolInId)
				&& a.getNsortName().equals(coi.getNsortName())
				&& a.getMaterial().equals(coi.getMaterial())
				&& a.getSpec().equals(coi.getSpec())).findFirst();


		PoolInDetail modifier = null;
		if(pd.isPresent()){
			modifier = pd.get();
		}else{
			modifier = new PoolInDetail();
			modifier.setPoolInId(poolInId);
		    modifier.setNsortName(coi.getNsortName());
		    modifier.setMaterial(coi.getMaterial());
		    modifier.setSpec(coi.getSpec());
		    modifier.setLastUpdatedBy(user.getName());
		    modifier.setReceivedAmount(BigDecimal.ZERO);
		    modifier.setReceivedWeight(BigDecimal.ZERO);
		    modifierList.add(modifier);
		}

        modifier.setReceivedAmount(new BigDecimal(ordItem.getIncreaseAmount()).add(modifier.getReceivedAmount()).add(allowanceAmount));
        modifier.setReceivedWeight(new BigDecimal(ordItem.getIncreaseWeight()).add(modifier.getReceivedWeight()));
    }

    private void addInvoiceDetailOrderitems(User user, List<InvoiceInDetailFormDto> list,List<InvoiceInAllowanceItemDto> listAllowanceItem) {
        List<InvoiceInDetailOrderItem> orderitemList = new LinkedList<>();
        list.forEach(item -> {
            buildDetailOrderItem(user, orderitemList, item, listAllowanceItem);
        });
        invoiceInDetailOrderitemDao.batchInsert(orderitemList);
    }

    private void buildDetailOrderItem(User user, List<InvoiceInDetailOrderItem> orderitemList, InvoiceInDetailFormDto item,List<InvoiceInAllowanceItemDto> listAllowanceItem) {
        item.getOrderItems().forEach(ordItem -> {
            InvoiceInDetailOrderItem io = new InvoiceInDetailOrderItem();
            try {
                BeanUtils.copyProperties(io, ordItem);
            } catch (Exception e) {
                logger.error("copy invoice detail failed.", e);
                throw new BusinessException(Constant.EXCEPTIONCODE_SYSTEM, "copy invoice detail failed");
            }
            io.setWeight(new BigDecimal(ordItem.getIncreaseWeight()));
            // 加上折让金额
            BigDecimal allowanceAmount = BigDecimal.ZERO;
            if (listAllowanceItem != null && listAllowanceItem.size() > 0) {
                Optional<InvoiceInAllowanceItemDto> allowanceItem = listAllowanceItem.stream().filter(a -> a.getOrderItemId().equals(ordItem.getOrderitemId())).findFirst();
                if (allowanceItem.isPresent()) {
                    allowanceAmount = allowanceItem.get().getAllowanceAmount();
                    io.setAllowanceAmount(allowanceAmount);
                }
            }
            io.setAmount(new BigDecimal(ordItem.getIncreaseAmount() + allowanceAmount.doubleValue()));
            io.setActive(Constant.INVOICE_IN_DETAIL_ORDER_ITEM_ACTIVE);
            io.setInvoiceDetailId(item.getInvoiceDetailId());
            io.setCreatedBy(user.getName());
            io.setLastUpdatedBy(user.getName());
            orderitemList.add(io);
        });
    }

    private void addInvoiceDetail(User user, InvoiceIn invoiceIn, List<InvoiceInDetailFormDto> list) {
        list.forEach(item -> {
            InvoiceInDetail id = new InvoiceInDetail();
            try {
                BeanUtils.copyProperties(id, item);
            } catch (IllegalAccessException | InvocationTargetException e) {
                logger.error("can not copy form properties to model.", e);
                throw new BusinessException(Constant.EXCEPTIONCODE_SYSTEM, "can not copy form properties to model.");
            }
            id.setInvoiceInId(invoiceIn.getId());
            id.setCreated(new Date());
            id.setCreatedBy(user.getName());
            id.setModificationNumber(0);
            id.setLastUpdated(new Date());
            id.setLastUpdatedBy(user.getName());

            //插入别名映射
            addCategoryAlias(user, item);
            id.setAliasId(item.getAliasId());

            invoiceInDetailDao.insert(id);// 需要获取主键ID，无法使用批量插入
            item.setInvoiceDetailId(id.getId());

        });
    }

    /**
     * tuxinaming,lixiang
     * 插入别名映射
     * @param user
     * @param item
     */
    private void addCategoryAlias(User user, InvoiceInDetailFormDto item) {
    	
		CategoryAliasQuery query = new CategoryAliasQuery();
    	query.setAliasName(item.getNsortName());
    	//query.setCategoryId(item.getCategoryId());
    	
    	List<CategoryAlias> list = categoryAliasDao.seletcByParams(query);
		if (list.size() > 0) {
			CategoryAlias alias = list.get(0);
			
			if(alias.getCategoryName().equals(item.getCategoryName())){
				item.setAliasId(alias.getId());
			} else {
				throw new BusinessException(Constant.EXCEPTIONCODE_SYSTEM, "输入的别名("+item.getNsortNameComb()+")已映射到品名("+alias.getCategoryName()+")，请确认后再提交！");
			}
		} else {
    		//add new 
    		Date date = new Date();
    		CategoryAlias ca = new CategoryAlias().setAliasName(item.getNsortName()).setCategoryId(item.getCategoryId())
    				.setCategoryName(item.getCategoryName()).setCreated(date).setLastUpdated(date)
    				.setCreatedBy(user.getName()).setLastUpdatedBy(user.getName()).setModificationNumber(0);
    		
    		try {
    			categoryAliasDao.insertSelective(ca);
    			item.setAliasId(ca.getId());
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				throw new BusinessException(Constant.EXCEPTIONCODE_SYSTEM, "保存失败！");
			}
    	}
    	
	}

	private void addInvoiceDetail(User user, InvoiceIn invoiceIn, InvoiceInDetailFormDto item) {
        InvoiceInDetail id = new InvoiceInDetail();
        try {
            BeanUtils.copyProperties(id, item);
        } catch (IllegalAccessException | InvocationTargetException e) {
            logger.error("can not copy form properties to model.", e);
            throw new BusinessException(Constant.EXCEPTIONCODE_SYSTEM, "can not copy form properties to model.");
        }
        id.setInvoiceInId(invoiceIn.getId());
        id.setCreated(new Date());
        id.setCreatedBy(user.getName());
        id.setModificationNumber(0);
        id.setLastUpdated(new Date());
        id.setLastUpdatedBy(user.getName());
        invoiceInDetailDao.insert(id);// 需要获取主键ID，无法使用批量插入
        item.setInvoiceDetailId(id.getId());
    }


    private void fillInvoiceIn(User user, InvoiceIn invoiceIn, Organization organization, List<InvoiceInDetailFormDto> list,InvoiceInAllowance invoiceInAllowance) {
        invoiceIn.setOrgId(organization.getId());
        invoiceIn.setOrgName(organization.getName());
        invoiceIn.setCheckTotalAmount(BigDecimal.ZERO);
        invoiceIn.setCheckTotalWeight(BigDecimal.ZERO);
        invoiceIn.setInputUserId(user.getId());
        invoiceIn.setInputUserName(user.getName());
        invoiceIn.setInputUserMobil(user.getTel());
        invoiceIn.setCreated(new Date());
        invoiceIn.setCreatedBy(user.getName());
        invoiceIn.setLastUpdated(new Date());
        invoiceIn.setLastUpdatedBy(user.getName());
        invoiceIn.setModificationNumber(0);
        invoiceIn.setStatus(InvoiceInStatus.RECEIVED.toString());   // 待寄出
        invoiceIn.setRelationStatus(InvoiceInDetailStatus.HasRelation.getCode());

        double totalAmount = list.stream().mapToDouble(a -> CbmsNumberUtil.formatMoney(a.getAmount())).sum();
        double totalWeight = list.stream().mapToDouble(a -> CbmsNumberUtil.formatWeight(a.getWeight())).sum();
        double allowDeviation = sysConfigService.getDouble(Constant.INVOICE_IN_ALLOW_AMOUNT_DEVIATION);
        double allowanceAmount = 0;
        if (invoiceInAllowance != null && invoiceInAllowance.getAmount() != null) {
            allowanceAmount = invoiceInAllowance.getAmount().doubleValue();
        }
        if (Math.abs(totalAmount - invoiceIn.getTotalAmount().doubleValue()) + allowanceAmount > allowDeviation) {
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "输入的价税合计与实际的价税合计总额不匹配，请检查输入是否有误");
        }
        invoiceIn.setTotalAmount(CbmsNumberUtil.buildMoney(totalAmount + allowanceAmount));
        invoiceIn.setTotalWeight(CbmsNumberUtil.buildWeight(totalWeight));
    }

    /**
     * 查询
     *
     * @param paramMap 查询参数
     * @return
     */
    @Override
    public List<InvoiceInDto> query(Map<String, Object> paramMap) {
        List<InvoiceInDto> res = invoiceInDao.query(paramMap);
        if (res == null || res.size() == 0) {
            return res;
        }
        
        //add by tuxianming 20160517
        //添加开关，如果为1，则启动判定，如果不为1，则不开启
        String consumeApplyControlSwitch = null;
        		List<SysSetting> syss = sysSettingService.getControlPinSettings();
        for (SysSetting setting : syss) {
        	if(ControlPinSettings.with_consume_apply_control.toString().equals(setting.getSettingName())){
        		consumeApplyControlSwitch = setting.getSettingValue();
        	}
		}
       
        //删除开票资料审核代码 
        //删除买家欠款金额审核代码
        //remove by : tuxianming/20160523
        
        if("1".equals(consumeApplyControlSwitch)){
        	 int statistics = 0;
        	 
        	 //remove by : tuxianming/20160523
             /*
        	 SysSetting sysSetting = sysSettingService.queryByType(type);
             Double settingValue = Double.parseDouble(sysSetting.getSettingValue());
             */
             AccountSendDto accountSendDto = new AccountSendDto();
             for (InvoiceInDto invoiceInDto : res) {
             	List<AccountOrderDto> accountOrderList = consignOrderService.queryAccount(invoiceInDto.getId());
             	if (accountOrderList.size() == 1) {//进项票关联订单如果只存在一个买家
             		//remove by : tuxianming/20160523
             		/*
             		Account account = accountService.queryById(accountOrderList.get(0).getAccountId());
             		if (accountOrderList.get(0).getAccountId().equals(account.getId())) {
             			//进项票所对应订单的买家客户的二结账户欠款>销项票申请开票二次结算额度控制
                 		if (-account.getBalanceSecondSettlement().doubleValue() > settingValue) {
                 			accountSendDto.setAccountId(account.getId());
                 			accountSendDto.setSendType("不可寄出");
                 			accountSendDto.setAccountName(account.getName());
                 			accountSendDto.setBalanceSecondSettlement(account.getBalanceSecondSettlement());
                 		} else {
                 			accountSendDto.setAccountId(account.getId());
                 			accountSendDto.setSendType("可寄出");
                 		}
             		}
             		accountSendDto = InvoiceDatastatus(account, accountOrderList.get(0).getAccountId(), statistics);//进项票所对应订单的买家客户的开票资料状态
             		*/
             		//销项票开票申请状态
             		accountSendDto = InvoiceApplyStatus (accountOrderList.get(0).getOrderId() ,accountOrderList.get(0).getAccountId(), accountOrderList.get(0).getInvoiceId(), statistics);
             		if (accountOrderList.get(0).getAccountId().equals(accountSendDto.getAccountId())) {
             			accountSendDto.setInvoiceId(accountOrderList.get(0).getInvoiceId());
                 	}
             		if (invoiceInDto.getId().equals(accountSendDto.getInvoiceId())) {
             			invoiceInDto.setInvoiceIsSend(accountSendDto.getSendType());
             		}
             	} else {//进项票关联订单存在多个买家
             		for (AccountOrderDto accountOrderDto : accountOrderList) {
             			int num = 0;
             			Account account = accountService.queryById(accountOrderDto.getAccountId());
             			if (accountOrderDto.getAccountId().equals(account.getId())) {
             				
             				//
             				/*
             				//如进项票所对应订单的买家客户的二结账户欠款大于销项票申请开票二次结算额度控制
                 			if (-account.getBalanceSecondSettlement().doubleValue() > settingValue) {
                 				num += 1;
                 			}
                 			accountSendDto = InvoiceDatastatus(account, accountOrderDto.getAccountId(), statistics);//进项票所对应订单的买家客户的开票资料状态
                 			num += accountSendDto.getStatistics();
                 			*/
                 			//销项票开票申请状态
                 			accountSendDto = InvoiceApplyStatus (accountOrderDto.getOrderId() ,accountOrderDto.getAccountId(), accountOrderDto.getInvoiceId(), statistics);
                 			num += accountSendDto.getStatistics();
                 			if (num > 0) {
                 				statistics += 1;
                 			}
                 			if (statistics == accountOrderList.size()) {
                 				accountSendDto.setAccountId(accountOrderDto.getAccountId());
                 				accountSendDto.setSendType("不可寄出");
                 				accountSendDto.setAccountName(account.getName());
                 				accountSendDto.setBalanceSecondSettlement(account.getBalanceSecondSettlement());
                 			} else {
                 				accountSendDto.setAccountId(accountOrderDto.getAccountId());
                 				accountSendDto.setSendType("可寄出");
                 			}
             			}
             			if (accountOrderDto.getAccountId().equals(accountSendDto.getAccountId())) {
                 			accountSendDto.setInvoiceId(accountOrderList.get(0).getInvoiceId());
                     	}
             			if (invoiceInDto.getId().equals(accountSendDto.getInvoiceId())) {
                 			invoiceInDto.setInvoiceIsSend(accountSendDto.getSendType());
                 		}
     				}
             	}
     		}
        } //end switch
        
        if (paramMap.containsKey("status") && paramMap.get("status") != null && paramMap.get("status").equals(InvoiceInStatus.ALREADY.getCode())) {
            // 查询已认证的发票时，检查这些发票是否已开销项票，如果已开是不允许再取消认证操作的
            List<Long> invoiceIds = res.stream().map(a -> a.getId()).collect(Collectors.toList());
            List<Long> invoiceOutIds = invoiceInDao.checkInvoiceOut(invoiceIds);
            if (invoiceOutIds != null && invoiceOutIds.size() > 0) {
                res.forEach(invoiceIn -> {
                    if (invoiceOutIds.contains(invoiceIn.getId())) {
                        invoiceIn.setInvoiceOut(true);
                    }
                });
            }
        }
        return res;
    }
    
    private AccountSendDto InvoiceDatastatus(Account account, Long accountId, int statistics){
    	AccountSendDto accountSendDto = new AccountSendDto();
    	if (accountId.equals(account.getId())) {
	    	if ("1".equals(account.getInvoiceDataStatus())) {//状态为1审核通过，2待审核，4审核不通过，为空则是待提交
	    		accountSendDto.setAccountId(account.getId());
				accountSendDto.setInvoiceInformation("审核通过");
				accountSendDto.setSendType("可寄出");
			} else if ("2".equals(account.getInvoiceDataStatus())) {
				accountSendDto.setAccountId(account.getId());
				accountSendDto.setInvoiceInformation("待审核");
				accountSendDto.setSendType("不可寄出");
				statistics += 1;
			} else if ("4".equals(account.getInvoiceDataStatus())) {
				accountSendDto.setAccountId(account.getId());
				accountSendDto.setInvoiceInformation("审核不通过");
				accountSendDto.setSendType("不可寄出");
				statistics += 1;
			} else if ("3".equals(account.getInvoiceDataStatus())) {
				accountSendDto.setAccountId(account.getId());
				accountSendDto.setInvoiceInformation("待提交");
				accountSendDto.setSendType("不可寄出");
				statistics += 1;
			}
    	}
    	accountSendDto.setStatistics(statistics);
    	return accountSendDto;
    }
    
    private AccountSendDto InvoiceApplyStatus (Long orderId ,Long accountId, Long invoiceId, int statistics) {
    	AccountSendDto accountSendDto = new AccountSendDto();
    	List<ConsignOrderItems> orderItemList = consignOrderItemsService.selectByOrderId(orderId);
		List<Long> orderItemIds = orderItemList.stream().map(a -> a.getId()).collect(Collectors.toList());
		InvoiceOutApplyStatusDto invoiceOutApplyStatusDto = consignOrderService.queryInvoiceOutApplyStatus(invoiceId, orderItemIds);
    	if (invoiceOutApplyStatusDto ==  null) {
    		accountSendDto.setAccountId(accountId);
    		accountSendDto.setApplyInvocieType("待提交");
			accountSendDto.setSendType("不可寄出");
			statistics += 1;
    	} else if (accountId.equals(invoiceOutApplyStatusDto.getAccountId())) {
			if (InvoiceOutApplyStatus.PENDING_SUBMIT.toString().equals(invoiceOutApplyStatusDto.getStatus())
					|| InvoiceOutApplyStatus.REVOKED.toString().equals(invoiceOutApplyStatusDto.getStatus())) {
				accountSendDto.setAccountId(invoiceOutApplyStatusDto.getAccountId());
				accountSendDto.setApplyInvocieType("待提交");
				accountSendDto.setSendType("不可寄出");
				statistics += 1;
			} else if (InvoiceOutApplyStatus.APPROVED.toString().equals(invoiceOutApplyStatusDto.getStatus())
					|| InvoiceOutApplyStatus.INVOICED.toString().equals(invoiceOutApplyStatusDto.getStatus())
					|| InvoiceOutApplyStatus.PARTIAL_INVOICED.toString().equals(invoiceOutApplyStatusDto.getStatus())) {
				accountSendDto.setAccountId(invoiceOutApplyStatusDto.getAccountId());
				accountSendDto.setApplyInvocieType("已审核通过");
				accountSendDto.setSendType("可寄出");
			} else if (InvoiceOutApplyStatus.DISAPPROVE.toString().equals(invoiceOutApplyStatusDto.getStatus())) {
				accountSendDto.setAccountId(invoiceOutApplyStatusDto.getAccountId());
				accountSendDto.setApplyInvocieType("审核不通过");
				accountSendDto.setSendType("不可寄出");
				statistics += 1;
			} else if (InvoiceOutApplyStatus.PENDING_APPROVAL.toString().equals(invoiceOutApplyStatusDto.getStatus())) {
				accountSendDto.setAccountId(invoiceOutApplyStatusDto.getAccountId());
				accountSendDto.setApplyInvocieType("待审核");
				accountSendDto.setSendType("不可寄出");
				statistics += 1;
			}
		}
    	accountSendDto.setStatistics(statistics);
    	return accountSendDto;
    }

    /**
     * 查询总数
     *
     * @param paramMap 查询参数
     * @return
     */
    @Override
    public int queryTotal(Map<String, Object> paramMap) {
        return invoiceInDao.queryTotal(paramMap);
    }
    
    @Override
    public List<AccountSendDto> invoiceInIsSend (Long invoiceId) {
    	List <AccountSendDto> accountSendList = new ArrayList<AccountSendDto>();
    	int statistics = 0;
    	List<AccountOrderDto> accountOrderList = consignOrderService.queryAccount(invoiceId);
    	for (AccountOrderDto accountOrderDto : accountOrderList) {
    		Account account = accountService.queryById(accountOrderDto.getAccountId());
    		//remove by : tuxianming/20160523
    		//AccountSendDto accountSendDto = InvoiceDatastatus(account, accountOrderDto.getAccountId(), statistics);
    		
    		AccountSendDto accountSendDto = InvoiceApplyStatus(accountOrderDto.getOrderId() ,accountOrderDto.getAccountId(), accountOrderDto.getInvoiceId(), statistics);
    		accountSendDto.setApplyInvocieType(accountSendDto.getApplyInvocieType());
    		accountSendDto.setAccountId(account.getId());
    		accountSendDto.setAccountName(account.getName());
    		//remove by : tuxianming/20160523
    		/*
    		if (account.getBalanceSecondSettlement().doubleValue() >= 0) {
    			accountSendDto.setBalanceSecondSettlementStr("0.00");
    		} else {
    			accountSendDto.setBalanceSecondSettlementStr(account.getBalanceSecondSettlement().negate().setScale(2).toString());
    		}
    		 */
    		accountSendList.add(accountSendDto);
		}
		return accountSendList;
    }
    
    /**
     * 登记发票快递单号
     *
     * @param user    操作人
     * @param express 快递单
     * @param ids     发票ID集合
     * @return
     */
    @Override
    public Boolean checkInExpress(User user, Express express, Long[] ids) {
        /*if (expressDao.selectByName(express.getExpressName()) != null) {
            return false;
        }*/

        Date date = new Date();
        express.setType(InvoiceType.IN.toString());
        express.setSendTime(date);
        express.setCreated(date);
        express.setCreatedBy(user.getName());
        express.setLastUpdated(date);
        express.setLastUpdatedBy(user.getName());
        // 插入快递单表
        int addResult = expressDao.insertSelective(express);
        if (addResult > 0) {
            InvoiceInUpdateDto invoiceIn;
            //TODO:插入
            // 更新发票表的快递单号
            for (Long id : ids) {
                invoiceIn = new InvoiceInUpdateDto();
                invoiceIn.setExpressId(express.getId());
                invoiceIn.setStatus(InvoiceInStatus.SENT.toString());   // 发票状态为已寄出/待确认
                invoiceIn.setId(id);
                invoiceIn.setLastUpdated(date);
                invoiceIn.setLastUpdatedBy(user.getName());
                invoiceIn.setOldStatus(InvoiceInStatus.RECEIVED.toString());//老状态 待寄出
                Integer addItemResult = invoiceInDao.updateByConditionSelective(invoiceIn);
                if (addItemResult > 0) {
                	 //lixiang 插入流水日志
                    //TODO:为什么
                	for (int i = 0; i < ids.length; i++) {
                        insertFlowLog(user, InvoiceInStatus.SENT.toString(), invoiceIn.getId());
					}  
                }
                if (addItemResult == 0) {
                    throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "选中发票已绑定快递单号，请重新选择发票");
                }
            }
        }
        return true;
    }

    /**
     * 核对发票
     *
     * @param user         操作人
     * @param invoiceInDto 发票
     * @param list         发票详情
     * @return
     */
    @Override
    public Boolean checkInvoice(User user, InvoiceInDto invoiceInDto, List<InvoiceInDetailDto> list) {
        Date date = new Date();

        Account account = accountDao.selectByPrimaryKey(invoiceInDto.getSellerId());
        if (account == null) {
            return false;
        }
        User manager = userDao.queryById(account.getManagerId());
        if (manager == null) {
            return false;
        }
        Organization organization = organizationDao.queryById(user.getOrgId());
        if (organization == null) {
            return false;
        }

        // 修改发票主表
        invoiceInDto.setCheckUserId(user.getId());
        invoiceInDto.setCheckUserName(user.getName());
        invoiceInDto.setCheckUserMobil(user.getTel());
        invoiceInDto.setCheckDate(date);
        invoiceInDto.setLastUpdated(date);
        invoiceInDto.setLastUpdatedBy(user.getName());
        invoiceInDto.setStatus(InvoiceInStatus.WAIT.toString());   // 已确认
        int uInvoiceInResult = invoiceInDao.updateByPrimaryKeySelective(invoiceInDto);
        if (uInvoiceInResult == 0) {
            return false;
        }
        Integer uInvDetailResult;
        for (InvoiceInDetailDto item : list) {
            // 修改发票详情表
            item.setLastUpdated(date);
            item.setLastUpdatedBy(user.getName());

            uInvDetailResult = invoiceInDetailDao.updateByPrimaryKeySelective(item);
            if (uInvDetailResult == 0)
                return false;
        }
        return true;
    }

    /**
     * 更新打印状态
     *
     * @param user 操作人
     * @param ids  发票ID集合
     * @return
     */
    @Override
    public Boolean updatePrintStatus(User user, Long[] ids) {
        InvoiceIn invoiceIn;
        for (Long id : ids) {
            invoiceIn = new InvoiceIn();
            invoiceIn.setPrintStatus(PrintStatus.PRINTED.toString());
            invoiceIn.setId(id);
            invoiceIn.setLastUpdated(new Date());
            invoiceIn.setLastUpdatedBy(user.getName());
            Integer addItemResult = invoiceInDao.updateByPrimaryKeySelective(invoiceIn);
            if (addItemResult == 0) {
                return false;
            }
        }
        return true;
    }


    /**
     * 更新发票状态
     *
     * @param user 操作人
     * @param ids  发票ID集合
     * @return
     */
    @Override
    public int updateStatus(User user, InvoiceInStatus status, List<Long> ids) {
        return invoiceInDao.updateStatusByIds(user.getName(), status.getCode(), ids, null, null);
    }

    @Override
    @Transactional
    public int authInvoiceAccept(User user, List<Long> ids) {
        int result = invoiceInDao.updateStatusByIds(user.getName(), InvoiceInStatus.ALREADY.getCode(), ids, InvoiceInStatus.WAIT.getCode(), InvoiceInDetailStatus.HasRelation.getCode());
        //lixiang 增加流水日志
        try{
	        for (int i = 0; i < ids.size(); i++) {
	             insertFlowLog(user, InvoiceInStatus.ALREADY.toString(), ids.get(i));
			}
	    } catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
        if (result != ids.size()) {
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "你所选中发票的状态已发生改变，请刷新页面后重试！");
        } else
            return result;
    }

    @Override
    @Transactional
    public int rollbackStatus(User user, List<Long> ids) {
        // 取消所有详情的关联关系
        // 不用取消关联，财务打回后再去确认一遍
        /*List<InvoiceInDetail> details = invoiceInDetailDao.queryByInvoiceInIds(ids);
    	details.forEach(a -> invoiceInDetailService.unbindOrderitemByDetailId(a.getId()));*/
        int result = invoiceInDao.updateStatusByIdsWithInvoiceOut(user.getLoginId(), InvoiceInStatus.SENT.getCode(), ids, InvoiceInStatus.WAIT.getCode(), InvoiceInDetailStatus.HasRelation.getCode());
        if (result != ids.size()) {
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "你所选择的发票关联的交易单已生成开票清单或状态已发生改变，不能打回，请重新选择或刷新页面后重试！");
        } else {
        	ids.forEach(id -> {
        		// 供应商进项报表踩点 - 进项票打回踩点
        		reportFinanceService.invoiceOperation(ReportSellerInvoiceInOperationType. UnInvoiceIn.getOperation(),id ,user );
        	});
        	return result;
        }
    }

    @Override
    @Transactional
    public void authInvoiceFailed(User user, List<Long> ids) {
        List<InvoiceInDetail> details = invoiceInDetailDao.queryByInvoiceInIds(ids);
        // 更新发票状态
        int result = invoiceInDao.updateStatusByIdsWithInvoiceOut(user.getLoginId(), InvoiceInStatus.CANCEL.getCode(), ids, InvoiceInStatus.WAIT.getCode(), InvoiceInDetailStatus.HasRelation.getCode());
        if (result != ids.size()) {
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "你所选择的发票关联的交易单已生成开票清单或状态已发生改变，不能作废，请重新选择或刷新页面后重试！");
        }

        // 扣除买家已开票总金额
        changePoolInAmount(ids, true);

        // 取消掉进项票关联关系
        List<Long> detailIds = details.stream().map(a -> a.getId()).collect(Collectors.toList());
        invoiceInDetailService.unbindOrderitemByDetailIds(detailIds, user);

        
        ids.forEach(id -> {
        	// 取消掉已开销项票
        	invoiceOutApplyService.invoiceInNegative(id, user);
    		// 供应商进项报表踩点 - 进项票认证不通过踩点
    		reportFinanceService.invoiceOperation(ReportSellerInvoiceInOperationType. UnInvoiceIn.getOperation(),id ,user );
    	});
    }

    @Override
    public void deauthentication(User user, List<Long> ids) {
        int result = invoiceInDao.updateStatusByIdsWithInvoiceOut(user.getLoginId(), InvoiceInStatus.WAIT.getCode(), ids, InvoiceInStatus.ALREADY.getCode(), null);
        if (result != ids.size()) {
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "你所选择的发票关联的交易单已生成开票清单或状态已发生改变，不能取消认证，请重新选择或刷新页面后重试！");
        }
        //lixiang 增加流水日志
        try{
	        for (int i = 0; i < ids.size(); i++) {
	             insertFlowLog(user, InvoiceInStatus.WAIT.toString(), ids.get(i));
			}
	    } catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
    }

    @Override
    @Transactional
    public void restoreInvoice(User user, List<Long> ids) {
        // 更新发票状态
        int result = invoiceInDao.updateStatusByIds(user.getName(), InvoiceInStatus.WAIT.getCode(), ids, InvoiceInStatus.CANCEL.getCode(), null);
        if (result != ids.size()) {
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "选中发票的状态已发生改变，请刷新页面后重试！");
        }
        // 更新关联
        invoiceInDao.updateRelationStatusByIds(user.getName(), InvoiceInDetailStatus.ToBeRelation.getCode(), ids);
        // 还原订单已开金额
        // 关联关系在作废的时候已经取消，修改订单关联信息
//    	List<InvoiceInDetail> details = invoiceInDetailDao.queryByInvoiceInIds(ids);
//    	details.forEach(detail -> {
//            consignOrderItemsService.reEffectiveInvoiceIn(detail.getId());
//        });
        // 增加买家已开票总额
        changePoolInAmount(ids, false);

    }

    private void changePoolInAmount(List<Long> ids, boolean isDeduct) {
        List<InvoiceIn> invoiceInList = invoiceInDao.queryByIds(ids);
        List<Long> departmentIds = invoiceInList.stream().map(a -> a.getDepartmentId()).distinct().collect(Collectors.toList());
        List<PoolIn> poolIns = poolInService.queryByDepartmentIds(departmentIds);
        poolIns.forEach(a -> {
            PoolInTotalModifier md = new PoolInTotalModifier();
            md.setSellerId(a.getSellerId());
            md.setDepartmentId(a.getDepartmentId());
            md.setOriginalAmount(a.getTotalReceivedAmount().doubleValue());
            md.setOriginalWeight(a.getTotalReceivedWeight().doubleValue());
            double totalWeight = invoiceInList.stream().filter(i -> i.getSellerId().equals(a.getSellerId()))
                    .mapToDouble(i -> i.getCheckTotalWeight() != null && i.getCheckTotalWeight().doubleValue() > 0 ? i.getCheckTotalWeight().doubleValue() : i.getTotalWeight().doubleValue()).sum();
            double totalAmount = invoiceInList.stream().filter(i -> i.getSellerId().equals(a.getSellerId()))
                    .mapToDouble(i -> i.getCheckTotalAmount() != null && i.getCheckTotalAmount().doubleValue() > 0 ? i.getCheckTotalAmount().doubleValue() : i.getTotalAmount().doubleValue()).sum();
            if (isDeduct) {
                totalWeight *= -1;
                totalAmount *= -1;
            }
            md.setChangeWeight(CbmsNumberUtil.formatWeight(totalWeight));
            md.setChangeAmount(CbmsNumberUtil.formatMoney(totalAmount));
            poolInService.modifyTotalReceived(md);
        });
    }

    @Override
    @Transactional
    public void deleteInvoice(User user, List<Long> ids) {
        List<InvoiceIn> invoiceInList = invoiceInDao.queryByIds(ids).stream().filter(
                a -> a.getStatus().equals(InvoiceInStatus.CANCEL.getCode())).collect(Collectors.toList());
        if (null == invoiceInList || invoiceInList.size() < 1) {
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "取已作废发票删除失败！");
        }
        List<Long> inIds = invoiceInList.stream().map(a -> a.getId()).distinct().collect(Collectors.toList());
        int result = invoiceInDao.deleteByIds(inIds, InvoiceInStatus.CANCEL.getCode());
        if (result != inIds.size()) {
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "取已作废发票删除失败！");
        }
        result = invoiceInDetailDao.deleteByInvoiceIds(inIds);
        if (result < inIds.size()) {
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "取已作废发票删除失败！");
        }
        for (InvoiceIn invoiceIn : invoiceInList) {
            logger.info("已作废发票删除:{}", invoiceIn.toString());
        }
    }

    @Override
    public InvoiceIn selectLastBySellerId(Long sellerId) {
        return invoiceInDao.selectLastBySellerId(sellerId);
    }

    @Override
    public InvoiceInDto fetchInvoiceInDetailById(Long id) {
        InvoiceInDto invoiceInDto = new InvoiceInDto();

        InvoiceIn invoiceIn = invoiceInDao.selectByPrimaryKey(id);
        invoiceInDto.setMemo(invoiceIn.getMemo());
        if (invoiceIn != null) {
            List<InvoiceInDetailDto> details = invoiceInDetailDao.queryByInvoiceInId(id);
            try {
                BeanUtils.copyProperties(invoiceInDto, invoiceIn);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new BusinessException(Constant.EXCEPTIONCODE_SYSTEM, "copy properties failed!");
            }
            
            //add by tuxianming 20160718
            //查询折让信息
            InvoiceInAllowance allowance = inAllowanceService.selectByInvoiceInId(id);
            
            //查询关联的折让信息
            List<InvoiceInAllowanceItemDto> allaownceDetails = inAllowanceItemService.selectByInvoiceInId(id);
            
            //查询所有的折让信息
            AllowanceDetailItemQuery query = new AllowanceDetailItemQuery();
            query.setAccountId(invoiceIn.getSellerId());
            query.setAllowanceType(AllowanceType.Seller.getKey());
            query.setListStatus(Arrays.asList(AllowanceStatus.Approved.getKey()));
            query.setAllowanceUnused(Boolean.TRUE);
            List<AllowanceOrderItemsDto> list = allowanceOrderDetailItemDao.queryDetails(query);
            
            /*
            list.removeIf(
        		a ->  allaownceDetails.stream().anyMatch(b-> {
        			return a.getId().doubleValue() != b.getAllowanceId();
        		})
    		);
            */
            List<AllowanceOrderItemsDto> listNew = new ArrayList<>();
            for (AllowanceOrderItemsDto a : list) {
				
            	for (InvoiceInAllowanceItemDto d : allaownceDetails) {
            		if(a.getId()!=null && d.getAllowanceId()!=null && a.getId().intValue() == d.getAllowanceId().intValue()){
            			listNew.add(a);
            			a.setAllowanceAmount(d.getAllowanceAmount());
            		}
				}
            	
			}
            
            invoiceInDto.setAllowance(allowance);
            invoiceInDto.setAllaownceDetails(listNew);
            
            //end 
            
            double totalNoTaxAmount = details.stream().mapToDouble(a -> a.getCheckNoTaxAmount() != null && a.getCheckNoTaxAmount().doubleValue() > 0 ? a.getCheckNoTaxAmount().doubleValue() : a.getNoTaxAmount().doubleValue()).sum();
            double totalTaxAmount = details.stream().mapToDouble(a -> a.getCheckTaxAmount() != null && a.getCheckTaxAmount().doubleValue() > 0 ? a.getCheckTaxAmount().doubleValue() : a.getTaxAmount().doubleValue()).sum();
            
            //加上折让
            if(allowance!=null){
            	if(allowance.getNoTaxAmount()!=null)
            		totalNoTaxAmount += allowance.getNoTaxAmount().doubleValue();
            	if(allowance.getTaxAmount()!=null)
            		totalTaxAmount += allowance.getTaxAmount().doubleValue();
            }
            
            invoiceInDto.setDetails(details);
            invoiceInDto.setTotalNoTaxAmount(new BigDecimal(totalNoTaxAmount));
            invoiceInDto.setTotalTaxAmount(new BigDecimal(totalTaxAmount));
        }
        List<Long> invoiceIds = new ArrayList<Long>();
        invoiceIds.add(id);
        List<Long> invoiceOutIds = invoiceInDao.checkInvoiceOut(invoiceIds);
        if (invoiceOutIds != null && invoiceOutIds.size() > 0) {
        	invoiceInDto.setInvoiceOutIds(invoiceOutIds);
        }
        return invoiceInDto;
    }

    /**
     * 根据销项发票申请ID查询相关的进项票
     *
     * @param inQuery 查询参数
     * @return 进项票集合
     */
    @Override
    public List<InvoiceInDto> queryByInvOutApplyIds(InvoiceInQuery inQuery) {
        return invoiceInDao.queryByInvOutApplyIds(inQuery);
    }

    /**
     * 根据销项发票申请ID查询相关的进项票总数
     *
     * @param inQuery 查询参数
     * @return 总数
     */
    @Override
    public Integer queryByInvOutApplyIdsTotal(InvoiceInQuery inQuery) {
        return invoiceInDao.queryByInvOutApplyIdsTotal(inQuery);
    }

    /**
     * 查询进项票总金额
     *
     * @param inQuery 查询参数
     * @return 总金额
     */
    @Override
    public BigDecimal querySumAmount(InvoiceInQuery inQuery) {
        return invoiceInDao.querySumAmount(inQuery);
    }

    /**
     * 暂缓认证
     *
     * @param ids     查询参数
     * @param isDefer
     * @return int
     */
    @Override
    public int updateIsDefer(List<Long> ids, int isDefer) {
        return invoiceInDao.updateIsDeferForIds(ids, isDefer);
    }

    @Override
    public List<Long> checkInvoiceOut(List<Long> invoiceInIds) {
        return invoiceInDao.checkInvoiceOut(invoiceInIds);
    }
    
    @Override
	public List<InvoiceKeepingDto> invoiceKeeping(InvoiceKeepingQuery invoiceKeepingQuery) {
		return invoiceInDao.invoiceKeeping(invoiceKeepingQuery);
    }
	
    @Override
	public int invoiceKeepingCount(InvoiceKeepingQuery invoiceKeepingQuery) {
		return invoiceInDao.invoiceKeepingCount(invoiceKeepingQuery);
	}
	
    @Override
	public List<InvoiceKeepingDto> queryCheckName(InvoiceKeepingQuery invoiceKeepingQuery) {
		return invoiceInDao.queryCheckName(invoiceKeepingQuery);
	}
    @Override
    public List<AccountInvoiceNoPassDto> queryNoPassInvoiceReason(AccountInvoiceNoPassQuery accountInvoiceNoPassQuery) {
        List<AccountInvoiceNoPassDto> accountInvoiceNoPassDtoList = invoiceInDao.queryNoPassInvoiceReason(accountInvoiceNoPassQuery);
        for(AccountInvoiceNoPassDto accountInvoiceNoPassDto : accountInvoiceNoPassDtoList){
            String invoiceStatusName = "";//开票资料状态
            String invoiceDataStatus = accountInvoiceNoPassDto.getInvoiceDataStatus();
            String status = accountInvoiceNoPassDto.getStatus();// 开票申请状态
            String statusName = "";
            //资料不足相当于未提交
            if("3".equals(invoiceDataStatus)){
                invoiceDataStatus = "5";
            }
            //撤销相当于未提交
            if("REVOKED".equals(status) || "DISAPPROVE".equals(status)){
                status = "PENDING_SUBMIT";
            }
            invoiceStatusName = InvoiceDataStatus.getNameByCode(invoiceDataStatus);
            statusName = InvoiceOutApplyStatus.getName(status);
            accountInvoiceNoPassDto.setInvoiceDataStatus(invoiceStatusName);
            accountInvoiceNoPassDto.setStatus(statusName);
        }
        return accountInvoiceNoPassDtoList;
    }
    @Override
    public int queryNoPassReasonCount(AccountInvoiceNoPassQuery accountInvoiceNoPassQuery) {
        return invoiceInDao.queryNoPassReasonCount(accountInvoiceNoPassQuery);
    }
    @Override
    public  int  queryExpressDeliverDays(String expressName,Long  orgid){
        return invoiceInDao.queryExpressDeliverDays(expressName, orgid);
    }
    /**
     * 当卖家设置成进项票黑名单，不能提交进项票
     * @author wangxianjun
     * @return
     */
    @Override
    public boolean invoiceIsSubmit(SysSettingQuery query){
        boolean isSubmit = true;
       int count = sysSettingDao.selectByParamTotal(query);
        if(count > 0){
            isSubmit = false;
        }
        return isSubmit;
    }
    /**
     * 模糊查询进项票票号列表
     * @author zhoucai@prcsteel.com
     * @date:2016-3-24
     * @return
     */
    
    
   public List<InvoiceIn> queryInvoiceCodeList (Map<String, Object> paramMap){
    	return invoiceInDao.queryInvoiceCodeList(paramMap);
    	
    }

	@Override
	public int totalInvoiceInDetailItemsByOrderId(Long orderId) {
		return invoiceInDetailOrderitemDao.countByOrderId(orderId);
	}
}
