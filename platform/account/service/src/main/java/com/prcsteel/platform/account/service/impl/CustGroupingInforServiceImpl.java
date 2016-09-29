package com.prcsteel.platform.account.service.impl;

import com.prcsteel.platform.account.model.dto.CustGroupingInforDto;
import com.prcsteel.platform.account.model.dto.GroupInforAccountDto;
import com.prcsteel.platform.account.model.enums.AccountTransApplyType;
import com.prcsteel.platform.account.model.enums.CustGroupStatus;
import com.prcsteel.platform.account.model.enums.GroupAccountOwerType;
import com.prcsteel.platform.account.model.enums.OwerType;
import com.prcsteel.platform.account.model.model.Account;
import com.prcsteel.platform.account.model.model.AccountTransLog;
import com.prcsteel.platform.account.model.model.CustGroupingInfor;
import com.prcsteel.platform.account.model.model.CustGroupingInforAccount;
import com.prcsteel.platform.account.model.model.CustGroupingInforFlow;
import com.prcsteel.platform.account.model.query.CustGroupingInforQuery;
import com.prcsteel.platform.account.model.query.CustGroupingQuery;
import com.prcsteel.platform.account.persist.dao.AccountDao;
import com.prcsteel.platform.account.persist.dao.AccountTransLogDao;
import com.prcsteel.platform.account.persist.dao.CustGroupingInforAccountDao;
import com.prcsteel.platform.account.persist.dao.CustGroupingInforDao;
import com.prcsteel.platform.account.persist.dao.CustGroupingInforFlowDao;
import com.prcsteel.platform.account.service.AccountFundService;
import com.prcsteel.platform.account.service.CustGroupingInforService;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.common.utils.Tools;
import com.prcsteel.platform.common.vo.Result;
import com.prcsteel.platform.order.model.dto.PayCodeDto;
import com.prcsteel.platform.order.model.enums.AssociationType;
import com.prcsteel.platform.order.model.enums.PayType;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service("custGroupingInforService")
public class CustGroupingInforServiceImpl implements CustGroupingInforService {

	@Resource
	CustGroupingInforAccountDao custGroupingInforAccountDao;

	@Resource
	CustGroupingInforDao custGroupingInforDao;

	@Resource
	CustGroupingInforFlowDao custGroupingInforFlowDao;

	@Resource
	AccountDao accountDao;

	@Resource
	AccountFundService accountFundService;
	
	@Resource
	AccountTransLogDao accountTransLogDao;

	private static final Logger logger = Logger.getLogger(CustGroupingInforServiceImpl.class);
	
	@Override
	public List<CustGroupingInforDto> queryGroupInfoAccount(CustGroupingInforQuery custGroupingInforQuery) {
		List<GroupInforAccountDto> groupInCustfoList = custGroupingInforAccountDao.queryGroupInfoAccount(custGroupingInforQuery);
		//排除掉已审核通过和审核未通过的组
		groupInCustfoList.removeIf(a -> a.getGroupInfoStatus().equals(CustGroupStatus.APPROVED.toString()));
		groupInCustfoList.removeIf(a -> a.getGroupInfoStatus().equals(CustGroupStatus.DECLINED.toString()));
		Long numberId = 0L;//设置临时id，判断groupInCustfoList循环的分组id(groupInfoId)是否相等
		List<CustGroupingInforDto> list = new ArrayList<CustGroupingInforDto>();
		CustGroupingInforDto model = null;
		for (GroupInforAccountDto groupInforAccountDto : groupInCustfoList) {
			if (numberId != groupInforAccountDto.getGroupInfoId()) {
				//如果不相等，将groupingInfoId赋给临时id，并new一个新的对象，给这个对象设置主表组的信息，把子表公司的信息
				Optional obj= list.stream().filter(a->a.getGroupId()==groupInforAccountDto.getGroupInfoId()).findFirst();
				if(obj.isPresent()){//如果存在这个分组，就不需要再建立
					model=(CustGroupingInforDto)obj.get();
					numberId = model.getGroupId();
				} else {
					numberId = groupInforAccountDto.getGroupInfoId();
					model = new CustGroupingInforDto();
					model.setGroupId(groupInforAccountDto.getGroupInfoId());				
					//如果分组状态是待审核和审核不通过显示未生效额度
					if (CustGroupStatus.DECLINED.toString().equals(groupInforAccountDto.getGroupInfoStatus())
							|| CustGroupStatus.REQUESTED.toString().equals(groupInforAccountDto.getGroupInfoStatus())) {
						model.setGroupLimit(groupInforAccountDto.getGroupCreditLimitAudit());
					//如果状态是通过审核则显示已生效额度
					} else if (CustGroupStatus.APPROVED.toString().equals(groupInforAccountDto.getGroupInfoStatus())) {
						model.setGroupLimit(groupInforAccountDto.getGroupCreditLimit());
					}
					if (null != groupInforAccountDto.getGroupNameAudit()) {
						model.setGroupNameAudit(groupInforAccountDto.getGroupNameAudit());
					}
					if (null != groupInforAccountDto.getGroupName()) {
						model.setGroupName(groupInforAccountDto.getGroupName());
					}
					model.setGroupInfoSerial(groupInforAccountDto.getGroupInfoSerial());
					model.setGroupInfoStatus(groupInforAccountDto.getGroupInfoStatus());
					model.setCompanyList(new ArrayList<GroupInforAccountDto>());//初始化list
					list.add(model);
				}
			}
			model.getCompanyList().add(groupInforAccountDto);
			model.setNum(model.getNum()+1);
		}
		return list;
	}

	@Override
	public CustGroupingInforDto queryGroupInfoById(Long groupId){
		List<GroupInforAccountDto> groupInCustfoList = custGroupingInforAccountDao.queryGroupInfoById(groupId);
		Long numberId = 0L;//设置临时id，判断groupInCustfoList循环的分组id(groupInfoId)是否相等
		CustGroupingInforDto model = null;
		List<CustGroupingInforDto> list = new ArrayList<CustGroupingInforDto>();
		for (GroupInforAccountDto groupInforAccountDto : groupInCustfoList) {
			if (numberId != groupInforAccountDto.getGroupInfoId()) {
				//如果不相等，将groupingInfoId赋给临时id，并new一个新的对象，给这个对象设置主表组的信息，把子表公司的信息
				Optional obj= list.stream().filter(a->a.getGroupId()==groupInforAccountDto.getGroupInfoId()).findFirst();
				if(obj.isPresent()){//如果存在这个分组，就不需要再建立
					model=(CustGroupingInforDto)obj.get();
					numberId = model.getGroupId();
				} else {
					numberId = groupInforAccountDto.getGroupInfoId();
					model = new CustGroupingInforDto();
					model.setGroupId(groupInforAccountDto.getGroupInfoId());
					//如果分组状态是待审核和审核不通过显示未生效额度
					if (CustGroupStatus.DECLINED.toString().equals(groupInforAccountDto.getGroupInfoStatus())
							|| CustGroupStatus.REQUESTED.toString().equals(groupInforAccountDto.getGroupInfoStatus())) {
						model.setGroupLimit(groupInforAccountDto.getGroupCreditLimitAudit());
						//如果状态是通过审核则显示已生效额度
					} else if (CustGroupStatus.APPROVED.toString().equals(groupInforAccountDto.getGroupInfoStatus())) {
						model.setGroupLimit(groupInforAccountDto.getGroupCreditLimit());
					}
					if (null != groupInforAccountDto.getGroupNameAudit()) {
						model.setGroupNameAudit(groupInforAccountDto.getGroupNameAudit());
					}
					if (null != groupInforAccountDto.getGroupName()) {
						model.setGroupName(groupInforAccountDto.getGroupName());
					}
					BigDecimal totalused = new BigDecimal(groupInCustfoList.stream().filter(a -> a.getGroupInfoId().equals(groupInforAccountDto.getGroupInfoId())).mapToDouble(a -> a.getCreditUsed().doubleValue()).sum());
					model.setGroupCreditUsed(totalused);
					model.setGroupCreditBalance(model.getGroupLimit().subtract(totalused));
					model.setGroupInfoSerial(groupInforAccountDto.getGroupInfoSerial());
					model.setGroupInfoStatus(groupInforAccountDto.getGroupInfoStatus());
					model.setCompanyList(new ArrayList<GroupInforAccountDto>());//初始化list
				}
			}
			groupInforAccountDto.setCreditBalance(groupInforAccountDto.getCreditLimit().subtract(groupInforAccountDto.getCreditUsed()));
			groupInforAccountDto.setCreditBalance(model.getGroupCreditBalance().compareTo(groupInforAccountDto.getCreditBalance()) < 0 ? model.getGroupCreditBalance() : groupInforAccountDto.getCreditBalance());
			model.getCompanyList().add(groupInforAccountDto);
			model.setNum(model.getNum() + 1);
		}
		return model;
	}

    @Override
    public List<CustGroupingInforDto> queryGroupInfo(CustGroupingInforQuery custGroupingInforQuery) {
        List<GroupInforAccountDto> groupInCustfoList = custGroupingInforAccountDao.queryGroupInfo(custGroupingInforQuery);
        Long numberId = 0L;//设置临时id，判断groupInCustfoList循环的分组id(groupInfoId)是否相等
        List<CustGroupingInforDto> list = new ArrayList<>();
        CustGroupingInforDto model = null;
        for (GroupInforAccountDto groupInforAccountDto : groupInCustfoList) {
            if (numberId != groupInforAccountDto.getGroupInfoId()) {
                //如果不相等，将groupingInfoId赋给临时id，并new一个新的对象，给这个对象设置主表组的信息，把子表公司的信息
                numberId = groupInforAccountDto.getGroupInfoId();
                model = new CustGroupingInforDto();
                model.setGroupId(groupInforAccountDto.getGroupInfoId());
                //如果分组状态是待审核和审核不通过显示未生效额度
                if (CustGroupStatus.DECLINED.toString().equals(groupInforAccountDto.getGroupInfoStatus())
                        || CustGroupStatus.REQUESTED.toString().equals(groupInforAccountDto.getGroupInfoStatus())) {
                    model.setGroupLimit(groupInforAccountDto.getGroupCreditLimitAudit());
					model.setGroupName(groupInforAccountDto.getGroupNameAudit());
                    //如果状态是通过审核则显示已生效额度
                } else if (CustGroupStatus.APPROVED.toString().equals(groupInforAccountDto.getGroupInfoStatus())) {
                    model.setGroupLimit(groupInforAccountDto.getGroupCreditLimit());
					model.setGroupName(groupInforAccountDto.getGroupName());
				}
                BigDecimal totalused = new BigDecimal(groupInCustfoList.stream().filter(
                        a -> a.getGroupInfoId() != null && a.getGroupInfoId().equals(groupInforAccountDto.getGroupInfoId())
                                && a.getCreditUsed() != null).mapToDouble(
                        a -> a.getCreditUsed().doubleValue()).sum());
                model.setGroupCreditUsed(totalused);
                model.setGroupCreditBalance(model.getGroupLimit().subtract(totalused));
                model.setGroupInfoSerial(groupInforAccountDto.getGroupInfoSerial());
                model.setGroupInfoStatus(groupInforAccountDto.getGroupInfoStatus());
                model.setCompanyList(new ArrayList<GroupInforAccountDto>());//初始化list
                list.add(model);
            }
			if(!CustGroupStatus.APPROVED.toString().equals(groupInforAccountDto.getStatus())){
				groupInforAccountDto.setCreditLimit(groupInforAccountDto.getCreditLimitAudit());
				groupInforAccountDto.setIsAutoSecondPayment(groupInforAccountDto.getIsAutoSecondPaymentAudit());
			}
            if (groupInforAccountDto.getAccountId() != null) {
                groupInforAccountDto.setCreditBalance(groupInforAccountDto.getCreditLimit().subtract(groupInforAccountDto.getCreditUsed()));
                groupInforAccountDto.setCreditBalance(model.getGroupCreditBalance().compareTo(groupInforAccountDto.getCreditBalance()) < 0 ? model.getGroupCreditBalance() : groupInforAccountDto.getCreditBalance());
                model.getCompanyList().add(groupInforAccountDto);
                model.setNum(model.getNum() + 1);
            }
        }
        return list;
    }

	@Override
    public int queryGroupInfoCount(CustGroupingInforQuery custGroupingInforQuery) {
        return custGroupingInforAccountDao.queryGroupInfoCount(custGroupingInforQuery);
    }

    @Override
    @Transactional
    public int deleteLimitGroup(Long groupId, User operator) {
		if(groupId==null){
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "删除失败！");
		}
        if (custGroupingInforDao.queryAccountNumByGroupId(groupId) > 0) {
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "小组下有客户存在，无法删除！");
        }
        CustGroupingInfor custGroupingInfor = custGroupingInforDao.selectByPrimaryKey(groupId);
		if(custGroupingInfor==null){
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "删除失败！");
		}
        CustGroupingInforFlow custGroupingInforFlow = new CustGroupingInforFlow();
        custGroupingInforFlow.setSerial(createCode());
        custGroupingInforFlow.setOwerType(OwerType.DELETE_GROUP.getCode());
        custGroupingInforFlow.setGroupingInforId(custGroupingInfor.getId());
        custGroupingInforFlow.setGroupingInforName(custGroupingInfor.getName());
        custGroupingInforFlow.setUserId(custGroupingInfor.getUserId());
        custGroupingInforFlow.setUserName(custGroupingInfor.getUserName());
        custGroupingInforFlow.setCreated(new Date());
        custGroupingInforFlow.setCreatedBy(operator.getName());
        custGroupingInforFlow.setLastUpdated(new Date());
        custGroupingInforFlow.setLastUpdatedBy(operator.getName());
        custGroupingInforFlowDao.insert(custGroupingInforFlow);
        CustGroupingInfor model = new CustGroupingInfor();
        model.setId(groupId);
        model.setIsDelete(true);
        model.setLastUpdated(new Date());
        model.setLastUpdatedBy(operator.getName());
        return custGroupingInforDao.updateByPrimaryKeySelective(model);
    }

    @Override
    @Transactional
    public int deleteLimitGroupAccount(Long id, String remark, User operator) {
        CustGroupingInforAccount custGroupingInforAccount = custGroupingInforAccountDao.selectByPrimaryKey(id);
        if (custGroupingInforAccount == null) {
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "客户已被删除，请刷新页面！");
        }
        Account account = accountDao.selectByPrimaryKey(custGroupingInforAccount.getDepartmentId());
        if (account.getCreditAmountUsed().doubleValue() > 0) {
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "该客户有信用额度在使用，无法删除！");
        }
        CustGroupingInfor custGroupingInfor = custGroupingInforDao.selectByPrimaryKey(custGroupingInforAccount.getGroupingInforId());
        CustGroupingInforFlow custGroupingInforFlow = new CustGroupingInforFlow();
        custGroupingInforFlow.setSerial(createCode());
        custGroupingInforFlow.setOwerType(OwerType.DELETE_CUST.getCode());
        custGroupingInforFlow.setGroupingInforId(custGroupingInforAccount.getGroupingInforId());
        custGroupingInforFlow.setGroupingInforName(custGroupingInfor.getName());
        custGroupingInforFlow.setAccountId(custGroupingInforAccount.getAccountId());
        custGroupingInforFlow.setAccountName(custGroupingInforAccount.getAccountName());
        custGroupingInforFlow.setDepartmentId(custGroupingInforAccount.getDepartmentId());
        custGroupingInforFlow.setDepartmentName(custGroupingInforAccount.getDepartmentName());
        custGroupingInforFlow.setUserId(custGroupingInforAccount.getUserId());
        custGroupingInforFlow.setUserName(custGroupingInforAccount.getUserName());
        custGroupingInforFlow.setRemark(remark);
        custGroupingInforFlow.setCreated(new Date());
        custGroupingInforFlow.setCreatedBy(operator.getName());
        custGroupingInforFlow.setLastUpdated(new Date());
        custGroupingInforFlow.setLastUpdatedBy(operator.getName());
        if(custGroupingInforFlowDao.insert(custGroupingInforFlow)<=0){
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "删除失败！");
		}
		if (account.getCreditAmount() != null && account.getCreditAmount().compareTo(BigDecimal.ZERO) > 0) {//额度大于0，下调整信用额度流水
			accountFundService.updateAccountFund(account.getId(), AssociationType.CREDIT_SERIAL, createdCode(),
					AccountTransApplyType.DOWN_CREDITLIMI, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
					BigDecimal.ZERO, BigDecimal.ZERO, account.getCreditAmount().negate(), PayType.BALANCE, operator.getId(), operator.getName(), new Date());
		}
		Account newAccount=new Account();
		newAccount.setId(account.getId());
		newAccount.setCreditAmount(BigDecimal.ZERO);
		newAccount.setIsAutoSecondPayment(0);
		newAccount.setLastUpdated(new Date());
		newAccount.setLastUpdatedBy(operator.getName());
		if (accountDao.updateByPrimaryKeySelective(newAccount) <= 0) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "删除失败！");
		}
        CustGroupingInforAccount model = new CustGroupingInforAccount();
        model.setId(id);
        model.setIsDelete(true);
        model.setLastUpdated(new Date());
        model.setLastUpdatedBy(operator.getName());
        return custGroupingInforAccountDao.updateByPrimaryKeySelective(model);
    }

	@Override
	public int updateCreditLimit(Long custId, BigDecimal creditLimit, User user) {
		CustGroupingInforAccount custGroupingInforAccount = new CustGroupingInforAccount();
		custGroupingInforAccount.setId(custId);
		custGroupingInforAccount.setCreditLimitAudit(creditLimit);
	    int num = custGroupingInforAccountDao.updateByPrimaryKeySelective(custGroupingInforAccount);
	    if (num == 0) {
	    	throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "修改客户审核额度失败！");
	    } else {
	    	//执行成功插入流水
	    	custGroupingInforAccount = custGroupingInforAccountDao.selectByPrimaryKey(custId);
	    	CustGroupingInforFlow custGroupingInforFlow = new CustGroupingInforFlow();
	    	custGroupingInforFlow = setFlow(OwerType.EDIT_CUST.getCode(), custGroupingInforAccount.getGroupingInforId(), null,
	    			null, null, custGroupingInforAccount.getAccountId(), custGroupingInforAccount.getAccountName(),
					custGroupingInforAccount.getDepartmentId(), custGroupingInforAccount.getDepartmentName(), user, createCode(), new BigDecimal("0"), creditLimit,
					new BigDecimal("0"), CustGroupStatus.REQUESTED.toString());
	    	return custGroupingInforFlowDao.insertSelective(custGroupingInforFlow);
	    }
	}



	@Override
	@Transactional(isolation = Isolation.SERIALIZABLE)
	public void auditCreditLimit(Long groupId, String groupSerial, String custSerials, User user, String status) {
		String newCustSerials[] = custSerials.split(",");
		List<String> custSerialList = new ArrayList<String>();
		for (int i = 0; i < newCustSerials.length; i++) {
			custSerialList.add(newCustSerials[i]);
		}
		CustGroupingInfor custGroupingInfor = custGroupingInforDao.selectByPrimaryKey(groupId);//查询分组信息
		if (CustGroupStatus.APPROVED.toString().equals(status)) {
			if (CustGroupStatus.APPROVED.toString().equals(custGroupingInfor.getStatus())) {
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "该组信息已审核通过，无需再次操作！");
			}
		}
		if (CustGroupStatus.DECLINED.toString().equals(status)) {
			if (CustGroupStatus.DECLINED.toString().equals(custGroupingInfor.getStatus())) {
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "该组信息已审核不通过，无需再次操作！");
			}
		}
		if (!groupSerial.equals(custGroupingInfor.getSerial())) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "该组信息已发生变更，请刷新页面重试！");
		}

		//额度判断
//		if(CustGroupStatus.APPROVED.toString().equals(status)&&GroupAccountOwerType.EDIT.getCode().equals(custGroupingInfor.getOwerType())){
//			BigDecimal companyMax=custGroupingInforAccountDao.queryGroupMaxCompanyLimt(custGroupingInfor.getId());
//			if ((custGroupingInfor.getCreditLimitAudit()).compareTo(companyMax) == -1) {
//				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "审核失败，分组额度小于组内公司额度！");
//			}
//			BigDecimal groupUsed = custGroupingInforDao.queryGroupUsedById(custGroupingInfor.getId());
//			if ((custGroupingInfor.getCreditLimitAudit()).compareTo(groupUsed) == -1) {
//				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "审核失败，分组额度小于组内已用额度！");
//			}
//		}

		//修改组的状态
		CustGroupingInfor custGroupInfo = new CustGroupingInfor();
		custGroupInfo.setId(groupId);
		if (CustGroupStatus.APPROVED.toString().equals(status)) {
			//第一次新增组的时(此时可能是新增了公司，如果是新增了公司，组的待审核组名、待审核额度还原，也有可能是编辑了分组)
			if (GroupAccountOwerType.INSERT.getCode().equals(custGroupingInfor.getOwerType()) ||
					GroupAccountOwerType.EDIT.getCode().equals(custGroupingInfor.getOwerType())) {
				custGroupInfo.setName(custGroupingInfor.getNameAudit());
				custGroupInfo.setNameAudit(" ");
				custGroupInfo.setCreditLimit(custGroupingInfor.getCreditLimitAudit());
				custGroupInfo.setCreditLimitAudit(new BigDecimal("0"));
				custGroupInfo.setStatus(CustGroupStatus.APPROVED.toString());
				custGroupInfo.setOwerType(GroupAccountOwerType.EDIT.getCode());
			}
		} else {
			if (GroupAccountOwerType.INSERT.getCode().equals(custGroupingInfor.getOwerType())) {//第一次新增审核不通过
				custGroupInfo.setStatus(CustGroupStatus.DECLINED.toString());
			} else {
				//如果是该组已审核过的，再次编辑申请调额度如果是审核通过可以按正常逻辑执行
				//如果是不同意操作 那就要维持原先的不变,将待审核额度去掉 
				custGroupInfo.setCreditLimitAudit(new BigDecimal("0"));
				custGroupInfo.setNameAudit(" ");
				custGroupInfo.setStatus(CustGroupStatus.APPROVED.toString());
			}
		}
		custGroupInfo.setLastUpdated(new Date());
		custGroupInfo.setLastUpdatedBy(user.getName());
		custGroupingInforDao.updateByPrimaryKeySelective(custGroupInfo);
		
		//设置组操作类型
		String groupOwerType = (custGroupingInfor.getCreditLimit().compareTo(new BigDecimal(0))==0)  ?  OwerType.INSERT_CUST.getCode() : OwerType.EDIT_CUST.getCode();

		CustGroupingInfor newCustGroupingInfor = custGroupingInforDao.selectByPrimaryKey(groupId);

		CustGroupingInforFlow custGroupingInforFlow = new CustGroupingInforFlow();
		//判断这种情况：在查看分组那边新增了客户，要对这个客户单独审核，此时只要将组的状态改变即可，无需添加流水
		//这种情况存在两种：
		//1、在该组下新增公司，此时未生效额度和已生效额度，未生效组名和已生效组名一致，这种情况不需要插入流水
		//2、可能是用户在界面操作了组编辑，并没有改变分组名和额度，此时也可以不用增加流水 
		if (GroupAccountOwerType.INSERT.getCode().equals(custGroupingInfor.getOwerType()) ||
				(!custGroupingInfor.getCreditLimit().equals(custGroupingInfor.getCreditLimitAudit()) && 
				!custGroupingInfor.getName().equals(custGroupingInfor.getNameAudit()))) {
			//插入审核组的流水信息
			if (CustGroupStatus.APPROVED.toString().equals(status)) {
				custGroupingInforFlow = setFlow(groupOwerType, custGroupingInfor.getId(), null,
						custGroupInfo.getName(), custGroupInfo.getName(), null, null,
						null, null, user, custGroupingInfor.getSerial(), newCustGroupingInfor.getCreditLimit(), newCustGroupingInfor.getCreditLimit(),
						newCustGroupingInfor.getCreditLimit(), CustGroupStatus.APPROVED.toString());
			} else {
				custGroupingInforFlow = setFlow(groupOwerType, custGroupingInfor.getId(), null,
						custGroupInfo.getName(), custGroupInfo.getName(), null, null,
						null, null, user, custGroupingInfor.getSerial(), new BigDecimal("0"), newCustGroupingInfor.getCreditLimit(),
						new BigDecimal("0"), CustGroupStatus.DECLINED.toString());
			}
			custGroupingInforFlowDao.insertSelective(custGroupingInforFlow);
		}


		List<CustGroupingInforAccount> groupInfoAccountList = custGroupingInforAccountDao.queryByGroupId(groupId);//查询公司信息
		for (CustGroupingInforAccount groupInforAccount : groupInfoAccountList) {
			//如果该公司已审核过额度则无需再次审核
			if (!CustGroupStatus.APPROVED.toString().equals(groupInforAccount.getStatus())) {
				if (!custSerialList.contains(groupInforAccount.getSerial())) {
					throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "该组所在的公司信息已发生变更，请刷新页面重试！");
				} 
				
				//修改分组下公司信息
				CustGroupingInforAccount custGroupInforAccount = new CustGroupingInforAccount();
				custGroupInforAccount.setId(groupInforAccount.getId());
				if (CustGroupStatus.APPROVED.toString().equals(status)) {
					custGroupInforAccount.setStatus(CustGroupStatus.APPROVED.toString());
					custGroupInforAccount.setCreditLimit(groupInforAccount.getCreditLimitAudit());
					custGroupInforAccount.setCreditLimitAudit(new BigDecimal("0"));
					custGroupInforAccount.setOwerType(GroupAccountOwerType.EDIT.getCode());
					custGroupInforAccount.setIsAutoSecondPayment(groupInforAccount.getIsAutoSecondPaymentAudit());
				} else {
					//如果是该公司已审核过的，再次编辑申请调额度如果是审核通过可以按正常逻辑执行
					//如果是不同意操作 那就要维持原先的不变,将待审核额度去掉 ，此时判断下已生效公司额度是否大于0，如果大于0就表示之前已经对该公司审核过了
					if (GroupAccountOwerType.EDIT.getCode().equals(groupInforAccount.getOwerType())) {// 返回的结果是int类型,-1表示小于,0是等于,1是大于.
						custGroupInforAccount.setCreditLimitAudit(new BigDecimal("0"));
						custGroupInforAccount.setStatus(CustGroupStatus.APPROVED.toString());
					} else {//初次新增公司，已生效额度是0
						custGroupInforAccount.setStatus(CustGroupStatus.DECLINED.toString());
					}
				}
				custGroupInforAccount.setLastUpdated(new Date());
				custGroupInforAccount.setLastUpdatedBy(user.getName());
				custGroupingInforAccountDao.updateByPrimaryKeySelective(custGroupInforAccount);

				//设置公司操作类型
				String custOwerType = (groupInforAccount.getCreditLimit().compareTo(new BigDecimal(0))==0)  ?  OwerType.INSERT_CUST.getCode() : OwerType.EDIT_CUST.getCode();
				
				CustGroupingInforAccount newGroupInforAccount = custGroupingInforAccountDao.selectByPrimaryKey(groupInforAccount.getId());

				custGroupingInforFlow = new CustGroupingInforFlow();
				//插入审核客户的流水信息
				if (CustGroupStatus.APPROVED.toString().equals(status)) {
					custGroupingInforFlow = setFlow(custOwerType, custGroupingInfor.getId(), custGroupingInfor.getNameAudit(),
							custGroupingInfor.getNameAudit(), custGroupingInfor.getNameAudit(), groupInforAccount.getAccountId(), groupInforAccount.getAccountName(),
							groupInforAccount.getDepartmentId(), groupInforAccount.getDepartmentName(), user, groupInforAccount.getSerial(), newGroupInforAccount.getCreditLimit(), newGroupInforAccount.getCreditLimit(),
							newGroupInforAccount.getCreditLimit(), CustGroupStatus.APPROVED.toString());
				} else {
					custGroupingInforFlow = setFlow(custOwerType, custGroupingInfor.getId(), custGroupingInfor.getNameAudit(),
							custGroupingInfor.getNameAudit(), custGroupingInfor.getNameAudit(), groupInforAccount.getAccountId(), groupInforAccount.getAccountName(),
							groupInforAccount.getDepartmentId(), groupInforAccount.getDepartmentName(), user, newGroupInforAccount.getSerial(), new BigDecimal("0"), newGroupInforAccount.getCreditLimit(),
							new BigDecimal("0"), CustGroupStatus.DECLINED.toString());
				}
				custGroupingInforFlowDao.insertSelective(custGroupingInforFlow);
				
				//修改客户的信用额度及是否自动抵扣二结
				Account newAccount = new Account();
				newAccount.setId(groupInforAccount.getDepartmentId());
				newAccount.setIsAutoSecondPayment(groupInforAccount.getIsAutoSecondPaymentAudit());
				accountDao.updateByPrimaryKeySelective(newAccount);
				
				if (CustGroupStatus.APPROVED.toString().equals(status)) {
					Account account = accountDao.selectByPrimaryKey(groupInforAccount.getDepartmentId());
					//如果客户本身的额度大于设置的额度，额度下调，反之上调，增加客户账户流水
					AccountTransApplyType applyType = null;
					if (account.getCreditAmount().subtract(custGroupInforAccount.getCreditLimit()).doubleValue() > 0) {
						//增加下调流水
						applyType = AccountTransApplyType.DOWN_CREDITLIMI;

					} else {
						//增加上调流水
						applyType = AccountTransApplyType.UP_CREDITLIMI;
					}
					
					//额度没发生变化不产生流水
					if (account.getCreditAmount().subtract(custGroupInforAccount.getCreditLimit()).doubleValue() != 0) {
						//信用额度发生额
						BigDecimal amount = custGroupInforAccount.getCreditLimit().subtract(account.getCreditAmount());
						accountFundService.updateAccountFund(groupInforAccount.getDepartmentId(), AssociationType.CREDIT_SERIAL, 
								createdCode(), applyType, 
								new BigDecimal("0"),  new BigDecimal("0"),  new BigDecimal("0"),  new BigDecimal("0"),  new BigDecimal("0"), amount, 
								PayType.BALANCE, user.getId(), user.getName(), new Date());

					}
					
					//判断二结余额是否自动信用额度还款/ 抵扣
					if (groupInforAccount.getIsAutoSecondPaymentAudit() == 1) {
						accountFundService.payForCredit(groupInforAccount.getDepartmentId(), null, null, 0L,Constant.SYSTEMNAME, new Date());
					}
				}
			}
		}
	}

	/**
	 * 额度操作流水表修改增加实体类操作
	 * @author afeng
	 * @param owerType 操作类型
	 * @param groupingInforId 分组id
	 * @param groupingInforName 当前生效分组名称
	 * @param groupingInforNameBefore 编辑录入分组名称、新增录入分组名称
	 * @param groupingInforNameAfter 审核通过分组名称
	 * @param accountId 客户id
	 * @param accountName 客户名称
	 * @param departmentId 部门id
	 * @param departmentName 部门名称
	 * @param user
	 * @param serial 流水号
	 * @param beforeLimit 当前生效信用额度
	 * @param afterLimit 编辑录入信用额度、新增录入信用额度
	 * @param finalLimit 审核通过信用额度
	 * @param groupingInforStatus 状态:REQUESTED(待审核),APPROVED(正常,审核通过),DECLINED(审核不通过)
	 * @return
	 */
	private CustGroupingInforFlow setFlow (String owerType, Long groupingInforId, String groupingInforName,
			String groupingInforNameBefore, String groupingInforNameAfter, Long accountId, String accountName,
			Long departmentId, String departmentName, User user, String serial, BigDecimal beforeLimit, BigDecimal afterLimit,
			BigDecimal finalLimit, String groupingInforStatus) {
		CustGroupingInforFlow custGroupingInforFlow = new CustGroupingInforFlow();
		custGroupingInforFlow.setOwerType(owerType);
		custGroupingInforFlow.setGroupingInforId(groupingInforId);
		custGroupingInforFlow.setGroupingInforName(groupingInforName);
		custGroupingInforFlow.setGroupingInforNameBefore(groupingInforNameBefore);
		custGroupingInforFlow.setGroupingInforNameAfter(groupingInforNameAfter);
		custGroupingInforFlow.setAccountId(accountId);
		custGroupingInforFlow.setAccountName(accountName);
		custGroupingInforFlow.setDepartmentId(departmentId);
		custGroupingInforFlow.setDepartmentName(departmentName);
		custGroupingInforFlow.setSerial(serial);
		custGroupingInforFlow.setBeforeLimit(beforeLimit);
		custGroupingInforFlow.setAfterLimit(afterLimit);
		custGroupingInforFlow.setFinalLimit(finalLimit);
		custGroupingInforFlow.setGroupingInforStatus(groupingInforStatus);
		custGroupingInforFlow.setCreated(new Date());
		custGroupingInforFlow.setCreatedBy(user.getName());
		custGroupingInforFlow.setUserId(user.getId());
		custGroupingInforFlow.setUserName(user.getName());
		custGroupingInforFlow.setLastUpdated(new Date());
		custGroupingInforFlow.setLastUpdatedBy(user.getName());
		return custGroupingInforFlow;
	}
	@Override
	public CustGroupingInfor queryGroupingInfoById(Long groupId){
		CustGroupingInfor groupInCustfoList = custGroupingInforDao.selectByPrimaryKey(groupId);
		return groupInCustfoList;
	}

	@Override
	@Transactional
    public int addLimitGroupAccount(Long groupId, Long accountId, String accountName, Long deptId, String deptName, BigDecimal creditLimit, Integer autoRefund, User operator) {
        if (groupId == null || groupId == 0 || accountId == null || accountId == 0 || deptId == null || deptId == 0
                || creditLimit == null || BigDecimal.ZERO.compareTo(creditLimit) > 0 || autoRefund == null) {
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "添加失败，参数错误！");
        }
		String newAccountSerial = createCode();
		CustGroupingInforAccount account = new CustGroupingInforAccount();
		account.setCreditLimitAudit(creditLimit);
        account.setGroupingInforId(groupId);
        account.setAccountId(accountId);
        account.setDepartmentId(deptId);
        account.setAccountName(accountName);
        account.setDepartmentName(deptName);
        account.setSerial(newAccountSerial);
        account.setStatus(CustGroupStatus.REQUESTED.toString());
		account.setIsAutoSecondPaymentAudit(autoRefund);
        account.setUserId(operator.getId());
        account.setUserName(operator.getName());
        account.setCreated(new Date());
        account.setCreatedBy(operator.getName());
        account.setLastUpdated(new Date());
        account.setLastUpdatedBy(operator.getName());
        if (custGroupingInforAccountDao.judgeCompanyIsExit(account) > 0) {
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "客户被重复添加，请刷新重试！");
        }
        CustGroupingInfor group = custGroupingInforDao.selectByPrimaryKey(groupId);
        if (group == null) {
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "添加失败，分组不存在，可能已被删除，请刷新重试！");
        }
		if ((CustGroupStatus.APPROVED.toString().equals(group.getStatus()) && creditLimit.compareTo(group.getCreditLimit()) > 0)
				|| (!CustGroupStatus.APPROVED.toString().equals(group.getStatus()) && creditLimit.compareTo(group.getCreditLimitAudit()) > 0)) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "客户信用额度不能大于组信用额度！");
		}
		CustGroupingInfor changeGroup = new CustGroupingInfor();
        changeGroup.setId(groupId);
        changeGroup.setStatus(OwerType.INSERT_CUST.getCode());
		if(group.getStatus().equals(CustGroupStatus.APPROVED.toString())){
			changeGroup.setNameAudit(group.getName());
			changeGroup.setCreditLimitAudit(group.getCreditLimit());
		}
        changeGroup.setLastUpdatedBy(operator.getName());
        changeGroup.setLastUpdated(new Date());
        changeGroup.setStatus(CustGroupStatus.REQUESTED.toString());
        if (custGroupingInforDao.updateByPrimaryKeySelective(changeGroup) <= 0) {
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "添加失败！");
        }
        CustGroupingInforFlow addAcountFlow = new CustGroupingInforFlow();
        addAcountFlow.setSerial(newAccountSerial);
		addAcountFlow.setGroupingInforId(group.getId());
		addAcountFlow.setGroupingInforName(group.getName());
		addAcountFlow.setGroupingInforStatus(group.getStatus());
        addAcountFlow.setDepartmentName(deptName);
        addAcountFlow.setDepartmentId(deptId);
        addAcountFlow.setAfterLimit(creditLimit);
        addAcountFlow.setAccountId(accountId);
        addAcountFlow.setAccountName(accountName);
        addAcountFlow.setOwerType(OwerType.INSERT_CUST.getCode());
        addAcountFlow.setUserId(operator.getId());
        addAcountFlow.setUserName(operator.getName());
        addAcountFlow.setCreated(new Date());
        addAcountFlow.setCreatedBy(operator.getName());
        addAcountFlow.setLastUpdated(new Date());
        addAcountFlow.setLastUpdatedBy(operator.getName());
        if (custGroupingInforFlowDao.insertSelective(addAcountFlow) <= 0) {
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "添加失败！");
        }
        return custGroupingInforAccountDao.insertSelective(account);
    }

    @Override
	@Transactional
    public int editGroupAccount(Long id,BigDecimal creditLimit,Integer autoRefund,User operator){
		if (id == null || id == 0 || creditLimit == null || BigDecimal.ZERO.compareTo(creditLimit) > 0 || autoRefund == null) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "保存失败，参数错误！");
		}
        CustGroupingInforAccount account=custGroupingInforAccountDao.selectByPrimaryKey(id);
		if (account == null) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "保存失败，客户可能已被删除，请刷新重试！");
		}
		CustGroupingInfor group = custGroupingInforDao.selectByPrimaryKey(account.getGroupingInforId());
		if ((CustGroupStatus.APPROVED.toString().equals(group.getStatus()) && creditLimit.compareTo(group.getCreditLimit()) > 0)
				|| (!CustGroupStatus.APPROVED.toString().equals(group.getStatus()) && creditLimit.compareTo(group.getCreditLimitAudit()) > 0)) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "客户信用额度不能大于组信用额度！");
		}
//		Account custAccount=accountDao.selectByPrimaryKey(account.getDepartmentId());
//		if(creditLimit.compareTo(custAccount.getCreditAmountUsed())<0){
//			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "客户信用额度不能小于已用额度！");
//		}
		CustGroupingInfor newGroup = new CustGroupingInfor();
        newGroup.setId(account.getGroupingInforId());
        newGroup.setLastUpdatedBy(operator.getName());
        newGroup.setLastUpdated(new Date());
        newGroup.setStatus(CustGroupStatus.REQUESTED.toString());
		if(group.getStatus().equals(CustGroupStatus.APPROVED.toString())){
			newGroup.setNameAudit(group.getName());
			newGroup.setCreditLimitAudit(group.getCreditLimit());
		}
        if (custGroupingInforDao.updateByPrimaryKeySelective(newGroup) <=0)
        {
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "保存失败！");
        }
		String newAccountSerial = createCode();
        CustGroupingInforFlow changeAcountFlow=new CustGroupingInforFlow();
        changeAcountFlow.setSerial(newAccountSerial);
		changeAcountFlow.setGroupingInforId(group.getId());
		changeAcountFlow.setGroupingInforName(group.getName());
		changeAcountFlow.setGroupingInforStatus(group.getStatus());
        changeAcountFlow.setDepartmentName(account.getDepartmentName());
        changeAcountFlow.setDepartmentId(account.getDepartmentId());
		changeAcountFlow.setBeforeLimit(account.getCreditLimit());
        changeAcountFlow.setAfterLimit(creditLimit);
        changeAcountFlow.setAccountId(account.getAccountId());
        changeAcountFlow.setAccountName(account.getAccountName());
        changeAcountFlow.setOwerType(OwerType.EDIT_CUST.getCode());
        changeAcountFlow.setUserId(operator.getId());
        changeAcountFlow.setUserName(operator.getName());
        changeAcountFlow.setCreated(new Date());
        changeAcountFlow.setCreatedBy(operator.getName());
        changeAcountFlow.setLastUpdated(new Date());
        changeAcountFlow.setLastUpdatedBy(operator.getName());
        if (custGroupingInforFlowDao.insertSelective(changeAcountFlow) <= 0) {
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "保存失败！");
        }
        CustGroupingInforAccount newAccount = new CustGroupingInforAccount();
        newAccount.setId(id);
		newAccount.setSerial(newAccountSerial);
		newAccount.setStatus(CustGroupStatus.REQUESTED.toString());
		newAccount.setIsAutoSecondPaymentAudit(autoRefund);
        newAccount.setLastUpdated(new Date());
        newAccount.setLastUpdatedBy(operator.getName());
        newAccount.setCreditLimitAudit(creditLimit);
        return custGroupingInforAccountDao.updateByPrimaryKeySelective(newAccount);
    }

	/**
	 *
	 * @author zhoucai@prcsteel.com
	 * @version V1.0
	 * @param
	 * @return String
	 */

	public synchronized String createCode(){
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		return sdf.format(date);
	}

	@Override
	public synchronized String createdCode() {
		String typeCode = "TE";  //一个类型
		Date date = new Date();
		
		//得到最后的一条记录，并解析单号，如果是当天，则在原有的基本上加+1
		//如果不是当天，则重新生成。
		AccountTransLog transLog = accountTransLogDao.queryLastCode();
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
		
		int number = Integer.parseInt(lasCode.substring(lasCode.length() - 4));
		lasCode = lasCode.substring(0, lasCode.length()-4);
		String dateStr = lasCode.substring(lasCode.length()-6);
		
		chips.add(0, type);
		chips.add(1, dateStr);
		chips.add(2, number);
		
		return chips;
	}
	/**
	 * @description 编辑分组
	 * @return void
	 * @author zhoucai@prcsteel.com
	 * @version V1.0
	 */
	public void editGroupLimitInfo(CustGroupingInfor custGroupingInfor, User operator) {
		if (custGroupingInforDao.groupIsExistByNameExceptId(custGroupingInfor.getNameAudit(), custGroupingInfor.getId()) > 0) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "修改失败，分组名称已被占用！");
		}
		CustGroupingInforAccount custGroupingInforAccount = new CustGroupingInforAccount();
		custGroupingInforAccount.setGroupingInforId(custGroupingInfor.getId());
//		if ((custGroupingInfor.getCreditLimitAudit()).compareTo(custGroupingInforAccountDao.queryGroupMaxCompanyLimt(custGroupingInfor.getId())) == -1) {
//			logger.error("更新分组失败，修改后分组额度小于组内公司额度！");
//			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "更新分组失败，修改后分组额度小于组内公司额度！");
//		}
//		BigDecimal groupUsed = custGroupingInforDao.queryGroupUsedById(custGroupingInfor.getId());
//		if ((custGroupingInfor.getCreditLimitAudit()).compareTo(groupUsed) == -1) {
//			logger.error("更新分组失败，修改后分组额度小于组内已用额度！");
//			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "更新分组失败，修改后分组额度小于组内已用额度！");
//		}
		custGroupingInfor.setLastUpdated(new Date());
		custGroupingInfor.setLastUpdatedBy(operator.getName());
		int total = custGroupingInforDao.updateByPrimaryKeySelective(custGroupingInfor);
		if (total > 0) {
			custGroupingInfor = custGroupingInforDao.selectByPrimaryKey(custGroupingInfor.getId());
			//新增分组插入流水号
			String groupFlowNo = createCode();
			Long userId = operator.getId();
			String userName = operator.getName();
			//新建分组录入插入流水信息
			CustGroupingInforFlow custGroupingInforFlow = new CustGroupingInforFlow();
			custGroupingInforFlow.setGroupingInforId(custGroupingInfor.getId());
			custGroupingInforFlow.setGroupingInforName(custGroupingInfor.getName());
			custGroupingInforFlow.setGroupingInforNameBefore(custGroupingInfor.getNameAudit());
			custGroupingInforFlow.setAfterLimit(custGroupingInfor.getCreditLimitAudit());
			custGroupingInforFlow.setBeforeLimit(custGroupingInfor.getCreditLimit());
			custGroupingInforFlow.setGroupingInforStatus(custGroupingInfor.getStatus());
			custGroupingInforFlow.setUserId(userId);
			custGroupingInforFlow.setUserName(userName);
			custGroupingInforFlow.setSerial(groupFlowNo);
			custGroupingInforFlow.setCreatedBy(userName);
			custGroupingInforFlow.setLastUpdatedBy(userName);
			custGroupingInforFlow.setOwerType(OwerType.EDIT_GROUP.getCode());
			custGroupingInforFlowDao.insertSelective(custGroupingInforFlow);
		} else {
			logger.error("更新分组失败，该分组不存在，请刷新重试！");
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "更新分组失败，该分组不存在，请刷新重试！");
		}
	}

	/**
	 * @param CustGroupingQuery
	 * @return String
	 * @description :添加分组，新建分组和公司
	 * @author zhoucai@prcsteel.com
	 * @version V1.0	 */

	public void addGroupAndCustLimit(CustGroupingQuery query) {
		//新增分组插入流水号
		String groupFlowNo = createCode();
		//公司流水
		String accoutFlowNo = "";
		Long userId = query.getUserId();
		String userName = query.getUserName();
		try {
			if(custGroupingInforDao.groupIsExistByName(query.getGroupingInforName())>0) {
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "添加失败，分组名称已被占用！");
			}
			//插入新建分组信息
			CustGroupingInfor custGroupingInfor = new CustGroupingInfor();
			custGroupingInfor.setNameAudit(query.getGroupingInforName());
			custGroupingInfor.setCreditLimitAudit(query.getLimitAudit());
			custGroupingInfor.setUserId(userId);
			custGroupingInfor.setUserName(userName);
			custGroupingInfor.setCreatedBy(userName);
			custGroupingInfor.setLastUpdatedBy(userName);
			custGroupingInfor.setSerial(groupFlowNo);
			custGroupingInfor.setStatus(query.getStatus());
			custGroupingInforDao.insertSelective(custGroupingInfor);
			//新建分组录入插入流水信息
			CustGroupingInforFlow custGroupingInforFlow = new CustGroupingInforFlow();
			custGroupingInforFlow.setGroupingInforId(custGroupingInfor.getId());
			custGroupingInforFlow.setGroupingInforNameBefore(query.getGroupingInforName());
			custGroupingInforFlow.setAfterLimit(query.getLimitAudit());
			custGroupingInforFlow.setGroupingInforStatus(query.getStatus());
			custGroupingInforFlow.setUserId(userId);
			custGroupingInforFlow.setUserName(userName);
			custGroupingInforFlow.setSerial(groupFlowNo);
			custGroupingInforFlow.setCreatedBy(userName);
			custGroupingInforFlow.setLastUpdatedBy(userName);
			custGroupingInforFlow.setOwerType(OwerType.INSERT_GROUP.getCode());

			custGroupingInforFlowDao.insertSelective(custGroupingInforFlow);

			//新建公司，循环生成公司信誉额度信息
			CustGroupingInforAccount custGroupingInforAccount = new CustGroupingInforAccount();
			custGroupingInforAccount.setUserName(userName);
			custGroupingInforAccount.setUserId(userId);
			custGroupingInforAccount.setCreatedBy(userName);
			custGroupingInforAccount.setLastUpdatedBy(userName);
			custGroupingInforAccount.setGroupingInforId(custGroupingInfor.getId());
			custGroupingInforAccount.setStatus(query.getStatus());

			//待插入公司的已录入待审核额度
			BigDecimal creditlimit;
			//待插入公司的˾id
			long accountId;
			//部门name
			String departName;
			//待插入部门id
			long departId;

			//自动还款状态
			Integer isShowAuto;

			//待插入公司的name
			String accountName;

			//循环生成公司信誉额度信息，以及记录录入相关流水信息
			for (int i = 0; i < query.getCreditLimitList().size(); i++) {
				//流水号
				String flowNo = createCode();
				//当前待录入公司的信用额度
				creditlimit = query.getCreditLimitList().get(i);
				//当前待录入公司的公司id
				accountId = query.getAccountIdList().get(i);
				//当前待录入公司的公司名称
				accountName = query.getAccountNameList().get(i);
				isShowAuto = query.getIsShowAutoList().get(i);
				departId = query.getDepartIdList().get(i);
				departName = query.getDepartNameList().get(i);
				//插入分组公司信用额度
				custGroupingInforAccount.setGroupingInforId(custGroupingInfor.getId());
				custGroupingInforAccount.setAccountId(accountId);
				custGroupingInforAccount.setAccountName(accountName);
				custGroupingInforAccount.setCreditLimitAudit(creditlimit);
				custGroupingInforAccount.setStatus(query.getStatus());

				custGroupingInforAccount.setSerial(flowNo);
				custGroupingInforAccount.setDepartmentId(departId);
				custGroupingInforAccount.setDepartmentName(departName);
				custGroupingInforAccount.setIsAutoSecondPaymentAudit(isShowAuto);
				if (custGroupingInforAccountDao.judgeCompanyIsExit(custGroupingInforAccount) > 0) {

					custGroupingInforDao.deleteGroupLimitByFlowNo(groupFlowNo);
					custGroupingInforFlowDao.deleteLimitFlowByFlowNo(groupFlowNo);
					if (!("".equals(accoutFlowNo))) {
						custGroupingInforAccountDao.deleteCompanyLimitByFlowNo(accoutFlowNo);
						custGroupingInforFlowDao.deleteLimitFlowByFlowNo(accoutFlowNo);
					}
					query.getAccountIdList().remove(i);
					query.getDepartIdList().remove(i);
					if (query.getAccountIdList().contains(accountId) && query.getDepartIdList().contains(departId)) {
						logger.error("添加失败，该客户：" + accountName + "，部门名称：" + departName + "已存在其他组中！");
						throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "添加失败，该客户：" + accountName + "，部门名称：" + departName + "已存在其他组中！");
					} else {
						logger.error("客户名：" + accountName + "，部门名称：" + departName + "已存在!");
						throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "添加失败，该客户：" + accountName + "，部门名称：" + departName + "已存在其他组中！");
					}


				}
				if (i == 0) {
					accoutFlowNo = accoutFlowNo.concat(flowNo);
				} else {
					accoutFlowNo = accoutFlowNo.concat(",").concat(flowNo);
				}
				custGroupingInforAccountDao.insertSelective(custGroupingInforAccount);
				//插入公司流水信息
				custGroupingInforFlow.setSerial(flowNo);
				custGroupingInforFlow.setDepartmentName(departName);
				custGroupingInforFlow.setDepartmentId(departId);
				custGroupingInforFlow.setAfterLimit(creditlimit);
				custGroupingInforFlow.setAccountId(accountId);
				custGroupingInforFlow.setAccountName(accountName);
				//新建公司
				custGroupingInforFlow.setOwerType(OwerType.INSERT_CUST.getCode());
				custGroupingInforFlowDao.insertSelective(custGroupingInforFlow);
			}
		} catch (BusinessException e) {
			//如果在执行的过程中发生异常，回退所有的插入数据
			custGroupingInforDao.deleteGroupLimitByFlowNo(groupFlowNo);
			custGroupingInforFlowDao.deleteLimitFlowByFlowNo(groupFlowNo);
			if (!("".equals(accoutFlowNo))) {
				custGroupingInforAccountDao.deleteCompanyLimitByFlowNo(accoutFlowNo);
				custGroupingInforFlowDao.deleteLimitFlowByFlowNo(accoutFlowNo);
			}
			logger.error("新建分组失败" +e.getMsg());
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, e.getMsg());
		}
	}

	@Override
	public BigDecimal queryGroupUsedById(Long groupId) {
		return custGroupingInforDao.queryGroupUsedById(groupId);
	}

	@Override
	public List<GroupInforAccountDto> queryaccountGroupInfo(CustGroupingInforQuery custGroupingInforQuery) {
		List<GroupInforAccountDto> groupInforAccountDto = custGroupingInforAccountDao.queryGroupInfo(custGroupingInforQuery);
		return groupInforAccountDto;
	}

	/**
	 * 按客户ID查询分组记录数
	 * @author chengui
	 * @param accountId
	 */
	@Override
	public int getGroupCountByAccountId(Long accountId){
		return custGroupingInforAccountDao.getGroupCountByAccountId(accountId);
	}

}
