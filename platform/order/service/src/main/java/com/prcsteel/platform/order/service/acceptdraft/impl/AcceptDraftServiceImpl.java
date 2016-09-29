package com.prcsteel.platform.order.service.acceptdraft.impl;

import com.prcsteel.framework.nido.engine.Nido;
import com.prcsteel.platform.account.model.dto.DepartmentDto;
import com.prcsteel.platform.account.model.enums.AccountTransApplyType;
import com.prcsteel.platform.account.model.model.Account;
import com.prcsteel.platform.account.model.model.AccountAcceptDraft;
import com.prcsteel.platform.account.model.model.AccountTransLog;
import com.prcsteel.platform.account.persist.dao.AccountAcceptDraftDao;
import com.prcsteel.platform.account.persist.dao.AccountDao;
import com.prcsteel.platform.account.persist.dao.AccountTransLogDao;
import com.prcsteel.platform.account.service.AccountAcceptDraftService;
import com.prcsteel.platform.account.service.AccountFundService;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.acl.persist.dao.OrganizationDao;
import com.prcsteel.platform.acl.persist.dao.UserDao;
import com.prcsteel.platform.api.AccountDepartmentService;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.constants.NidoTaskConstant;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.common.service.FileService;
import com.prcsteel.platform.common.utils.FileUtil;
import com.prcsteel.platform.common.utils.Tools;
import com.prcsteel.platform.order.model.dto.AcceptDraftDto;
import com.prcsteel.platform.order.model.enums.AcceptDraftAttachmentType;
import com.prcsteel.platform.order.model.enums.AcceptDraftStatus;
import com.prcsteel.platform.order.model.enums.AssociationType;
import com.prcsteel.platform.order.model.enums.PayType;
import com.prcsteel.platform.order.model.model.AcceptDraft;
import com.prcsteel.platform.order.model.model.AcceptDraftAttachment;
import com.prcsteel.platform.order.model.nido.NoteMessageContext;
import com.prcsteel.platform.order.model.query.AcceptDraftQuery;
import com.prcsteel.platform.order.persist.dao.AcceptDraftAttachmentDao;
import com.prcsteel.platform.order.persist.dao.AcceptDraftDao;
import com.prcsteel.platform.order.persist.dao.ConsignOrderDao;
import com.prcsteel.platform.order.service.acceptdraft.AcceptDraftService;
import com.prcsteel.platform.order.service.order.OrderStatusService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by myh on 2015/11/11.
 */
@Service("acceptDraftService")
public class AcceptDraftServiceImpl implements AcceptDraftService {
    private static Logger logger = LogManager.getLogger(AcceptDraftServiceImpl.class);
    public static final String ATTACHMENTSAVEPATH = "img" + File.separator + "acceptDraft" + File.separator;
    @Resource
    AcceptDraftDao acceptDraftDao;
    @Resource
    FileService fileService;
    @Resource
    AcceptDraftAttachmentDao acceptDraftAttachmentDao;
    @Resource
    AccountDao accountDao;
    @Resource
    OrderStatusService orderStatusService;
    @Resource
    ConsignOrderDao orderDao;
    @Resource
    UserDao userDao;
    @Resource
    OrganizationDao organizationDao;
    @Resource
    AccountDepartmentService accountDepartmentService;
    @Resource
    AccountAcceptDraftDao accountAcceptDraftDao;
    @Resource
    AccountTransLogDao accountTransLogDao;
    @Resource
    AccountFundService accountFundService;
    @Resource
    AccountAcceptDraftService accountAcceptDraftService;
    
    /**
     * 按条件查询
     *
     * @param query
     * @return
     */
    @Override
    public List<AcceptDraft> queryByParam(AcceptDraftQuery query) {
        return acceptDraftDao.queryByParam(query);
    }


    /**
     * 计数
     *
     * @param query
     * @return
     */
    @Override
    public Integer countByParam(AcceptDraftQuery query) {
        return acceptDraftDao.countByParam(query);
    }

    public int deleteByPrimaryKey(Long id) {
        return acceptDraftDao.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(AcceptDraft record) {
        return acceptDraftDao.insert(record);
    }

    @Override
    public int insertSelective(AcceptDraft record) {
        return acceptDraftDao.insertSelective(record);
    }

    @Override
    public AcceptDraft selectByPrimaryKey(Long id) {
        return acceptDraftDao.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(AcceptDraft record) {
        return acceptDraftDao.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(AcceptDraft record) {
        return acceptDraftDao.updateByPrimaryKey(record);
    }

    @Override
    public boolean checkAcceptDarftIsPayed(Long acceptDarftId) {
        return orderDao.checkAcceptDarftIsPayed(acceptDarftId);
    }

    /**
     * 撤回充值申请
     *
     * @param id 记录Id
     */
    @Override
    @Transactional
    public void withdrawAudit(Long id, User user) {
        AcceptDraft acceptDraft = acceptDraftDao.selectByPrimaryKey(id);
        if (acceptDraft != null && AcceptDraftStatus.SUBMITTED.getCode().equals(acceptDraft.getStatus())) {
            acceptDraft.setStatus(AcceptDraftStatus.NEW.getCode());
            acceptDraft.setLastUpdatedBy(user.getLoginId());
            acceptDraft.setCreatedBy(user.getLoginId());
            acceptDraft.setModificationNumber(acceptDraft.getModificationNumber() + 1);
            Integer flag = acceptDraftDao.updateByPrimaryKeySelective(acceptDraft);
            if (flag == 0) {
                throw new BusinessException(Constant.ERROR_RESOURCE_NOT_FOUND, "撤回充值申请失败。");
            }
        } else {
            throw new BusinessException(Constant.ERROR_RESOURCE_NOT_FOUND, "没有找到对应的记录或者当前状态已经更改，请刷新再试。");
        }
    }

    /**
     * 银票保存&&提交审核
     * modify by wangxianjun 迭代8 银票由只能上传一张照片改为银票可以最多上传10张图片
     *
     * @param acceptDraft
     * @param attachmentfiles
     * @param operation
     * @return
     */
    @Transactional
    public String save(AcceptDraft acceptDraft, List<MultipartFile> attachmentfiles, String operation, User user, String imgsId) {
        String returnMessage;
        AcceptDraftQuery query = new AcceptDraftQuery();
        query.setCode(acceptDraft.getCode());
        List<AcceptDraft> list =acceptDraftDao.queryByParam(query);
        if (list.stream().filter(a -> !a.getId().equals(acceptDraft.getId())).count() != 0) {
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "银票号不能重复");
        }
        if (acceptDraft.getAmount().compareTo(BigDecimal.valueOf(5000000)) >= 1) {
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "银票金额应小于5,000,000");
        }
        Account account = accountDao.selectAccountByName(acceptDraft.getAccountName());
        if (account == null) {
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "客户不存在，请重试");
        }
        if (acceptDraft.getOrgId() == null || acceptDraft.getOrgId() == 0) {
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "服务中心参数有误");
        }
        acceptDraft.setOrgName(organizationDao.queryById(acceptDraft.getOrgId()).getName());
        acceptDraft.setAccountId(account.getId());
        acceptDraft.setCreatedBy(user.getLoginId());
        acceptDraft.setLastUpdatedBy(user.getLoginId());
        if (Constant.OPERATION_SAVE.equals(operation)) {
            acceptDraft.setStatus(AcceptDraftStatus.NEW.getCode());
            returnMessage = "保存成功";
        } else if (Constant.OPERATION_SUBMIT.equals(operation)) {
            acceptDraft.setStatus(AcceptDraftStatus.SUBMITTED.getCode());
            returnMessage = "提交成功，待核算会计审核";
        } else {
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "找不到对应操作");
        }
        if (null == acceptDraft.getId() || "".equals(acceptDraft.getId())) {
            acceptDraft.setModificationNumber(0);
            if (acceptDraftDao.insertSelective(acceptDraft) == 0) {
                throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, acceptDraft.getId() == null ? "保存失败" : "提交失败");
            }
        } else {
            AcceptDraft record = acceptDraftDao.selectByPrimaryKey(acceptDraft.getId());
            acceptDraft.setModificationNumber(record.getModificationNumber() + 1);
            if (!(AcceptDraftStatus.NEW.getCode().equals(record.getStatus()) || AcceptDraftStatus.RENEW.getCode().equals(record.getStatus()))) {
                throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "银票状态不正确，无法保存或提交");
            }

            if (imgsId != null && !"".equals(imgsId)) {   //如果前台没有删除图片，则不删除原来的图片
                ArrayList<Long> attachmentIds = new ArrayList<Long>();
                String[] ids = imgsId.split("\\|");

                Map<String, Object> paramMap = new HashMap<String, Object>();
                paramMap.put("attachmentIds", attachmentIds);

                for (String id : ids) {
                    attachmentIds.add(Long.valueOf(id));
                }
                if (acceptDraftAttachmentDao.deleteByAttachmentIds(paramMap) == 0) {
                    throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "删除原附件失败");
                }
            }
            if (acceptDraftDao.updateByPrimaryKeySelective(acceptDraft) == 0) {
                throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, acceptDraft.getId() == null ? "保存失败" : "提交失败");
            }
        }
        //modify by wangxianjun 迭代8 银票由只能上传一张照片改为银票可以最多上传10张图片
        if (attachmentfiles != null) {   //如果没上传图片，则不执行插入
            for (MultipartFile file : attachmentfiles) {
                saveAttachment(file, acceptDraft, user);
            }
        }
        return returnMessage;
    }

    private void saveAttachment(MultipartFile file, AcceptDraft acceptDraft, User user) {
        AcceptDraftAttachment acceptDraftAttachment = new AcceptDraftAttachment();
        acceptDraftAttachment.setCreatedBy(user.getLoginId());
        acceptDraftAttachment.setLastUpdatedBy(user.getLoginId());
        acceptDraftAttachment.setAcceptDraftId(acceptDraft.getId());
        acceptDraftAttachment.setModificationNumber(0);
        acceptDraftAttachment.setType(AcceptDraftAttachmentType.MAIN.getCode());
        String saveKey;
        try {
            saveKey = fileService.saveFile(
                    file.getInputStream(),
                    ATTACHMENTSAVEPATH
                            + acceptDraft.getId()
                            + File.separator
                            + acceptDraftAttachment.getType()
                            + "."
                            + FileUtil.getFileSuffix(file
                            .getOriginalFilename()));
        } catch (IOException e) {
            throw new BusinessException(Constant.EXCEPTIONCODE_SYSTEM, "保存附件" + "出错");
        }
        acceptDraftAttachment.setUrl(saveKey);
        if (acceptDraftAttachmentDao.insertSelective(acceptDraftAttachment) == 0) {
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "保存附件失败");
        }
    }

    /**
     * 申请取消充值
     *
     * @param id 票据单id
     */
    @Override
    @Transactional
    public void applyCancleCharged(Long id, String reason, User user) {
        if (id != null) {
            AcceptDraft draft = acceptDraftDao.selectByPrimaryKey(id);
            if (draft == null || !StringUtils.equals(draft.getStatus(), AcceptDraftStatus.CHARGED.getCode())) {
                throw new BusinessException(Constant.ERROR_RESOURCE_NOT_FOUND, "没有找到对应的记录或者当前状态已经更改。");
            }
            AcceptDraftDto dto = new AcceptDraftDto(id, AcceptDraftStatus.ROLLBACKREQUEST.getCode(), reason, user.getName(), draft.getModificationNumber() + 1, AcceptDraftStatus.CHARGED.getCode());
            if (acceptDraftDao.updateStatusByPrimaryKeyAndOldStatus(dto) == 0) {
                throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "该票据单已处理。");
            }
            // add afeng 取消银票充值到部门查询 如果该客户只有一个默认部门，则银票充值的金额自动到部门，不在公司账户上
            List<DepartmentDto> departmentList = accountDao.queryDeptByCompanyId(draft.getAccountId());//查询客户下对应的所有部门
            List<Long> departments = departmentList.stream().map(a -> a.getId()).collect(Collectors.toList());
            if (departmentList.size() == 1) {
                if (departmentList.get(0).getBalance().compareTo(draft.getAmountApprove()) < 0) {
                    throw new BusinessException(Constant.ERROR_RESOURCE_NOT_FOUND, "客户账户余额小于银票金额，无法回滚。");
                }
                //锁定资金余额
               accountFundService.updateAccountFund(departmentList.get(0).getId(), AssociationType.ACCEPT_DRAFT, draft.getCode(), AccountTransApplyType.ACCEPT_DRAFT_LOCK,
                		 draft.getAmountApprove().negate(),  draft.getAmountApprove(), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, PayType.BALANCE, user.getId(), user.getName(), new Date());
                
            } else {
            	//客户存在多个部门 通过银票id和部门id求查cust_account_accept_draft表里的数据 afeng
            	//查询公司未分配的金额
            	List<AcceptDraft> acceptDraft = acceptDraftDao.queryByAccountStatus(draft.getAccountId(), null, draft.getCode());
            	//查询公司给部门分配的金额
            	List<AccountAcceptDraft> custAcceptDraftList = accountAcceptDraftDao.queryBydepartmentIdAndAcceptDraftId(departments, id);
            	custAcceptDraftList.removeIf(a -> a.getAmount().doubleValue() == 0);//移除没有分配资金的部门
            	departments.stream().forEach(department -> {
                	Account accountDepartment = accountDao.selectByPrimaryKey(department);                	
                	custAcceptDraftList.stream().forEach(custAcceptDraft -> {                		
                		if (accountDepartment.getId().equals(custAcceptDraft.getAccountId())) {
                			if (accountDepartment.getBalance().compareTo(custAcceptDraft.getAmount()) < 0) {
                				throw new BusinessException(Constant.ERROR_RESOURCE_NOT_FOUND, "客户部门账户余额小于银票金额，无法回滚。");
                			}
                    	}
                	});
                });
            	if (acceptDraft.get(0).getRemainingAmount().compareTo(BigDecimal.ZERO) == 1) {//如果公司的额度没有分配完
            		accountFundService.updateAccountFund(draft.getAccountId(), AssociationType.ACCEPT_DRAFT, draft.getCode(), AccountTransApplyType.ACCEPT_DRAFT_LOCK,
            				acceptDraft.get(0).getRemainingAmount().negate(), acceptDraft.get(0).getRemainingAmount(), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, PayType.BALANCE, user.getId(), user.getName(), new Date());
            		
            	}
            	
            	for (AccountAcceptDraft accountAcceptDraft : custAcceptDraftList) {
            		 for (DepartmentDto departmentDto : departmentList) {
            			 if (departmentDto.getId().equals(accountAcceptDraft.getAccountId())) {
            				 accountFundService.updateAccountFund(departmentDto.getId(), AssociationType.ACCEPT_DRAFT, draft.getCode(), AccountTransApplyType.ACCEPT_DRAFT_LOCK,
                     				accountAcceptDraft.getAmount().negate(), accountAcceptDraft.getAmount(), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, PayType.BALANCE, user.getId(), user.getName(), new Date());	 
            			 }
            		 }
				}
            }	
        } else {
            throw new BusinessException(Constant.ERROR_RESOURCE_NOT_FOUND, "参数不能为空。");
        }
    }
    
    /**
     * 银票充值审核
     *
     * @param acceptDraft 审核数据
     * @param user        用户
     */
    @Override
    @Transactional
    public void confirmRecharge(AcceptDraft acceptDraft, User user) {
        if (acceptDraft == null) {
            throw new BusinessException(Constant.ERROR_RESOURCE_NOT_FOUND, "数据为空。");
        }
        AcceptDraft record = acceptDraftDao.selectByPrimaryKey(acceptDraft.getId());
        int departments = accountDepartmentService.queryDepartmentByName(record.getAccountId());
        if (record != null && AcceptDraftStatus.SUBMITTED.getCode().equals(record.getStatus())) {
            User acceptDraftOwner = userDao.queryByLoginId(record.getCreatedBy());

            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
            Long endTime = record.getEndDate().getTime();
            Long endTimeApprove = acceptDraft.getEndDateApprove().getTime();
            String endDate = sf.format(endTime);
            String endDateApprove = sf.format(endTimeApprove);

            String smsContent;
            String code = record.getCode();
            if (record.getAmount().compareTo(acceptDraft.getAmountApprove()) != 0
                    || !record.getCode().equals(acceptDraft.getCodeApprove())
                    || !endDate.equals(endDateApprove)
                    || record.getDiscountRate().compareTo(acceptDraft.getDiscountRate()) != 0) {
                smsContent = record.getAccountName() + "票号为" + code + "的银票未通过核算会计审核，请确认处理【钢为网】";
//                SysSetting template=sysSettingService.queryBySettingType(SysSettingType.SmsTemplateAddOrder.getCode());
//                if (template == null) {
//                    throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,"新开单通知审核人短信模板缺失");
//                }
//                String content =template.getSettingValue();
//                content = content.replace("#companyname#", consignOrder.getAccountName());
//                content = content.replace("#endcode#", endCode);
                Nido.start(NidoTaskConstant.SEND_NOTE_MESSAGE, new NoteMessageContext(acceptDraftOwner.getTel(), smsContent));
                throw new BusinessException(Constant.ERROR_RESOURCE_NOT_FOUND, "双敲匹配失败，充值失败，请检查输入值！");
            }
            //  2015-12-2  新增贴现率
            record.setDiscountRate(acceptDraft.getDiscountRate());
            record.setAmountApprove(acceptDraft.getAmountApprove());
            record.setCodeApprove(acceptDraft.getCodeApprove());
            record.setEndDateApprove(acceptDraft.getEndDateApprove());
            record.setStatus(AcceptDraftStatus.CHARGED.getCode());
            record.setLastUpdated(new Date());
            record.setLastUpdatedBy(user.getLoginId());
            record.setModificationNumber(record.getModificationNumber() + 1);
            Integer flag = acceptDraftDao.updateByPrimaryKeySelective(record);
            if (flag > 0) {
                accountFundService.updateAccountFund(record.getAccountId(), AssociationType.ACCEPT_DRAFT, record.getCode(), AccountTransApplyType.CHARGE,
                        record.getAmount(), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, PayType.BALANCE, acceptDraftOwner.getId(), acceptDraftOwner.getName(), new Date());
                
                if (departments == 1) {
                    accountFundService.updateAccountFund(record.getAccountId(), AssociationType.MONEYALLOCATION, record.getCode(), AccountTransApplyType.COMPANYMONEY_TRANSTO_DEPART,
                            record.getAmount().negate(), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, PayType.BALANCE, 0L, Constant.SYSTEMNAME, new Date());
                   
                    Long departmentId = accountDepartmentService.queryDepartment(record.getAccountId());
                    accountFundService.updateAccountFund(departmentId, AssociationType.ACCEPT_DRAFT, record.getCode(), AccountTransApplyType.CHARGE,
                            record.getAmount(), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, PayType.BALANCE, acceptDraftOwner.getId(), acceptDraftOwner.getName(), new Date());
                   
                    accountFundService.payForCredit(departmentId, AssociationType.ACCEPT_DRAFT, record.getCode(), 0l, Constant.SYSTEMNAME, new Date());
                    
                    //产生银票金额分配记录
                    accountAcceptDraftService.save(new AccountAcceptDraft(departmentId, record.getId(), record.getAmount()), user);
                    
                }
                Account account = new Account();
                account.setId(record.getAccountId());
                account.setIsAcceptDraftCharged(Integer.parseInt(Constant.YES));
                account.setLastUpdated(new Date());
                account.setLastUpdatedBy(user.getLoginId());
                if (departments == 1) {
                    account.setBalance(new BigDecimal(0.00));
                }
                flag = accountDao.updateByPrimaryKeySelective(account);
                if (flag == 0) {
                    throw new BusinessException(Constant.ERROR_MISSING_REQUIRED_PARAM, "修改买家银票使用状态失败。");
                }

                // 成功发送短信
                smsContent = record.getAccountName() + "票号为" + code + "的银票已通过核算会计审核，并充值到资金账户" + record.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP) + "【钢为网】";
                Nido.start(NidoTaskConstant.SEND_NOTE_MESSAGE, new NoteMessageContext(acceptDraftOwner.getTel(), smsContent));
            } else {
                throw new BusinessException(Constant.ERROR_RESOURCE_NOT_FOUND, "银票充值审核失败。");
            }
        } else {
            throw new BusinessException(Constant.ERROR_RESOURCE_NOT_FOUND, "没有找到对应的记录或者当前状态已经更改，请刷新再试。");
        }
    }

    /**
     * 通过取消充值申请
     *
     * @param id   银票id
     * @param user 用户
     */
    @Override
    @Transactional
    public void rollback(Long id, User user) {
        AcceptDraft acceptDraft = acceptDraftDao.selectByPrimaryKey(id);
        if (acceptDraft == null || !AcceptDraftStatus.ROLLBACKREQUEST.getCode().equals(acceptDraft.getStatus())) {
            throw new BusinessException(Constant.ERROR_RESOURCE_NOT_FOUND, "没有找到对应的记录或者当前状态已经更改，请刷新再试。");
        }
        if (orderDao.checkAcceptDarftIsPayed(id)) {
            throw new BusinessException(Constant.ERROR_RESOURCE_NOT_FOUND, "银票已被使用，无法取消充值。");
        }
        //开始回滚
        //回滚状态
        acceptDraft.setStatus(AcceptDraftStatus.RENEW.getCode());
        acceptDraft.setLastUpdatedBy(user.getLoginId());
        acceptDraft.setCreatedBy(user.getLoginId());
        acceptDraft.setModificationNumber(acceptDraft.getModificationNumber() + 1);
        Integer flag = acceptDraftDao.updateByPrimaryKeySelective(acceptDraft);
        if (flag == 0) {
            throw new BusinessException(Constant.ERROR_RESOURCE_NOT_FOUND, "更新银票状态失败。");
        }

        //add afeng 流水到部门
        List<DepartmentDto> departmentList = accountDao.queryDeptByCompanyId(acceptDraft.getAccountId());
        List<Long> departments = departmentList.stream().map(a -> a.getId()).collect(Collectors.toList());
        List<AccountAcceptDraft> custAcceptDraftList = accountAcceptDraftDao.queryBydepartmentIdAndAcceptDraftId(departments, id);
        custAcceptDraftList.removeIf(a -> a.getAmount().doubleValue() == 0);//移除没有分配资金的部门
        for (DepartmentDto departmentDto : departmentList) {
        	for (AccountAcceptDraft accountAcceptDraft : custAcceptDraftList) {
				if (departmentDto.getId().equals(accountAcceptDraft.getAccountId())) {
					//解锁账户
		        	accountFundService.updateAccountFund(departmentDto.getId(), AssociationType.ACCEPT_DRAFT, acceptDraft.getCode(), AccountTransApplyType.ACCEPT_DRAFT_UNLOCK,
		        			accountAcceptDraft.getAmount(), accountAcceptDraft.getAmount().negate(), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, PayType.BALANCE, user.getId(), user.getName(), new Date());
		     		
		     		//部门资金撤回到公司
		     		accountFundService.updateAccountFund(departmentDto.getId(), AssociationType.MONEYBACK, createdCode(), AccountTransApplyType.DEPARMONEY_BACKTO_COMPANY,
		     				accountAcceptDraft.getAmount().negate(), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, PayType.BALANCE, 0L, Constant.SYSTEMNAME, new Date());
		     		
		     		//此时多个部门操作资金撤回到公司，此时公司也应产生相同的流水
		     		accountFundService.updateAccountFund(acceptDraft.getAccountId(), AssociationType.MONEYBACK, createdCode(), AccountTransApplyType.DEPARMONEY_BACKTO_COMPANY,
		     				accountAcceptDraft.getAmount(), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, PayType.BALANCE, user.getId(), user.getName(), new Date());
		     		
				}
			}
        }
        //查询公司未分配的金额
		List<AcceptDraft> acceptDraftDto = acceptDraftDao.queryByAccountStatus(acceptDraft.getAccountId(), null, acceptDraft.getCode());
        //如果公司的额度没有分配完 add afeng
        if (acceptDraftDto.get(0).getRemainingAmount().compareTo(BigDecimal.ZERO) == 1) {
        	//解锁账户
        	accountFundService.updateAccountFund(acceptDraft.getAccountId(), AssociationType.ACCEPT_DRAFT, acceptDraft.getCode(), AccountTransApplyType.ACCEPT_DRAFT_UNLOCK,
        			acceptDraftDto.get(0).getRemainingAmount(), acceptDraftDto.get(0).getRemainingAmount().negate(), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, PayType.BALANCE, user.getId(), user.getName(), new Date());
     		
        }
  
        //不管公司账户是否有剩余银票，都要产生回退流水记录，而不是从部门产生
        //拿到部门合计退到公司的余额并取反
        Double departmentAmount = custAcceptDraftList.stream().mapToDouble(a -> a.getAmount().doubleValue()).sum();
        BigDecimal departmentMoney = new BigDecimal(departmentAmount.toString());
        if (acceptDraftDto.get(0).getRemainingAmount().compareTo(BigDecimal.ZERO) == 1) {
        	departmentMoney = acceptDraftDto.get(0).getRemainingAmount().add(departmentMoney);
        }
        //回退
        Long userId = 0L;
        String userName = "";
        if (departmentList.size() == 1) {
        	userName = Constant.SYSTEMNAME;
        }  else {
        	userId = user.getId();
        	userName = user.getName();
        }
        //如果资金没有分配到部门，操作者还是当前操作人
        if (custAcceptDraftList.size() == 0) {
        	userId = 0L;
        	userName = Constant.SYSTEMNAME;
        } 
 		accountFundService.updateAccountFund(acceptDraft.getAccountId(), AssociationType.ACCEPT_DRAFT, acceptDraft.getCode(), AccountTransApplyType.ACCEPT_DRAFT_BACK,
 				departmentMoney.negate(), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, PayType.BALANCE, userId, userName, new Date());
 		
 		if (custAcceptDraftList.size() != 0) {
 			if (accountAcceptDraftDao.updateAmountByAcceptDraftId(acceptDraft.getId()) == 0) {
 	            throw new BusinessException(Constant.ERROR_RESOURCE_NOT_FOUND, "回退银票已分配金额失败。");
 	        }
 		}
        
        User acceptDraftOwner = userDao.queryByLoginId(acceptDraft.getCreatedBy());
        String code = acceptDraft.getCode();
        String smsContent = "申请将" + acceptDraft.getAccountName() + "已经充值到资金账户的银票（票号为：" + code + "）取消充值，通过核算会计审核并从资金账户减值" + acceptDraft.getAmountApprove().setScale(2, BigDecimal.ROUND_HALF_UP) + "【钢为网】";
        Nido.start(NidoTaskConstant.SEND_NOTE_MESSAGE, new NoteMessageContext(acceptDraftOwner.getTel(), smsContent));
    }

    /**
     * 不通过取消充值申请
     *
     * @param id   银票id
     * @param user 用户
     */
    @Override
    @Transactional
    public void refuseRollback(Long id, User user) {
        AcceptDraft acceptDraft = acceptDraftDao.selectByPrimaryKey(id);
        if (acceptDraft != null && AcceptDraftStatus.ROLLBACKREQUEST.getCode().equals(acceptDraft.getStatus())) {
            acceptDraft.setStatus(AcceptDraftStatus.CHARGED.getCode());
            acceptDraft.setLastUpdatedBy(user.getLoginId());
            acceptDraft.setCreatedBy(user.getLoginId());
            acceptDraft.setModificationNumber(acceptDraft.getModificationNumber() + 1);
            Integer flag = acceptDraftDao.updateByPrimaryKeySelective(acceptDraft);
            if (flag == 0) {
                throw new BusinessException(Constant.ERROR_RESOURCE_NOT_FOUND, "审核失败。");
            }
            
            //查询公司未分配的金额
    		List<AcceptDraft> acceptDraftDto = acceptDraftDao.queryByAccountStatus(acceptDraft.getAccountId(), null, acceptDraft.getCode());
            //如果公司的额度没有分配完 add afeng
            if (acceptDraftDto.get(0).getRemainingAmount().compareTo(BigDecimal.ZERO) == 1) {
            	//解锁账户
            	accountFundService.updateAccountFund(acceptDraft.getAccountId(), AssociationType.ACCEPT_DRAFT, acceptDraft.getCode(), AccountTransApplyType.ACCEPT_DRAFT_UNLOCK,
            			acceptDraftDto.get(0).getRemainingAmount(), acceptDraftDto.get(0).getRemainingAmount().negate(), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, PayType.BALANCE, user.getId(), user.getName(), new Date());
         		
        	}
            //add afeng 流水到部门
            List<DepartmentDto> departmentList = accountDao.queryDeptByCompanyId(acceptDraft.getAccountId());
            List<Long> departments = departmentList.stream().map(a -> a.getId()).collect(Collectors.toList());
            List<AccountAcceptDraft> custAcceptDraftList = accountAcceptDraftDao.queryBydepartmentIdAndAcceptDraftId(departments, id);
            custAcceptDraftList.removeIf(a -> a.getAmount().doubleValue() == 0);//移除没有分配资金的部门
            for (DepartmentDto departmentDto : departmentList) {
            	for (AccountAcceptDraft accountAcceptDraft : custAcceptDraftList) {
            		if (departmentDto.getId().equals(accountAcceptDraft.getAccountId())) {
            			//解锁账户
    		        	accountFundService.updateAccountFund(departmentDto.getId(), AssociationType.ACCEPT_DRAFT, acceptDraft.getCode(), AccountTransApplyType.ACCEPT_DRAFT_UNLOCK,
    		        			accountAcceptDraft.getAmount(), accountAcceptDraft.getAmount().negate(), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, PayType.BALANCE, user.getId(), user.getName(), new Date());
    		     	
            		}
				}
            }
            User acceptDraftOwner = userDao.queryByLoginId(acceptDraft.getCreatedBy());
            String code = acceptDraft.getCode();
            String smsContent = "申请将" + acceptDraft.getAccountName() + "已经充值到资金账户的银票（票号为：" + code + "）取消充值，未通过核算会计审核，请确认处理【钢为网】";
            Nido.start(NidoTaskConstant.SEND_NOTE_MESSAGE, new NoteMessageContext(acceptDraftOwner.getTel(), smsContent));
        } else {
            throw new BusinessException(Constant.ERROR_RESOURCE_NOT_FOUND, "没有找到对应的记录或者当前状态已经更改，请刷新再试。");
        }
    }

    @Override
    public List<AcceptDraft> queryByAccountStatus(Long accountId, String status) {
        return acceptDraftDao.queryByAccountStatus(accountId, status, null);
    }
    
    /** 
	 * CH为“部门资金撤回到公司”操作，“160304”为日期，“0001”为流水号初始值，每多一次操作加1
	 * @author lixiang
	 */
    private synchronized String createdCode() {
		String typeCode = "CH";  //一个类型
		Date date = new Date();
		
		//得到最后的一条记录，并解析单号，如果是当天，则在原有的基本上加+1
		//如果不是当天，则重新生成。
		AccountTransLog transLog = accountTransLogDao.queryRevokeLastCode();
		List<Object> lastCodeChips = null;
		if (transLog != null) {
			String lastCode = transLog.getConsignOrderCode();
			lastCodeChips = parsePayCode(lastCode);
		}
		String currDateStr = Tools.dateToStr(date, "yyMMdd");//6位年月日
		int number = 1;
		if(lastCodeChips!=null){
			//如果时间相等，则在原有的流水号上面+1,
			if(currDateStr.equals(lastCodeChips.get(1))){
				number = ((int) lastCodeChips.get(2))+1;
			}
		}

		String newCode = typeCode+currDateStr+String.format("%04d", number);
		return newCode;
	}
	
	/**
	 * 解析收款方付款申请单号并返回一个数组
	 * 关联号格式：类型：TE + 6位年月日 + 1位流水号
	 * @return List<Object> 为四条：
	 * 		index=0 : String	类型
	 * 		index=1 : String	6位年月日yyMMdd
	 * 		index=2 : Integer	4位流水号
	 */
	private List<Object> parsePayCode(String lasCode) {
		
		
		if(lasCode == null || lasCode.length() < 12)
			return null;
		
		List<Object> chips =  new ArrayList<Object>();
		
		String type = lasCode.substring(0,2);
		lasCode = lasCode.substring(2);
		
		int number = Integer.parseInt(lasCode.substring(lasCode.length()-4));
		lasCode = lasCode.substring(0, lasCode.length()-4);
		String dateStr = lasCode.substring(lasCode.length()-6);
		
		chips.add(0, type);
		chips.add(1, dateStr);
		chips.add(2, number);
		return chips;
	}
    
}
