package com.prcsteel.platform.account.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.prcsteel.rest.sdk.activiti.enums.WorkFlowEnum;
import org.prcsteel.rest.sdk.activiti.pojo.ActivitiVariable;
import org.prcsteel.rest.sdk.activiti.pojo.TaskActionInfo;
import org.prcsteel.rest.sdk.activiti.pojo.TaskInfo;
import org.prcsteel.rest.sdk.activiti.query.ListTaskQuery;
import org.prcsteel.rest.sdk.activiti.request.StartProcessRequest;
import org.prcsteel.rest.sdk.activiti.service.ProcessService;
import org.prcsteel.rest.sdk.activiti.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.prcsteel.platform.account.model.dto.AccountDocumentDto;
import com.prcsteel.platform.account.model.dto.BankDto;
import com.prcsteel.platform.account.model.enums.AccountBankDataStatus;
import com.prcsteel.platform.account.model.enums.AttachmentType;
import com.prcsteel.platform.account.model.model.AccountBank;
import com.prcsteel.platform.account.model.model.AccountExt;
import com.prcsteel.platform.account.persist.dao.AccountBankDao;
import com.prcsteel.platform.account.persist.dao.AccountDao;
import com.prcsteel.platform.account.service.AccountBankService;
import com.prcsteel.platform.account.service.AccountDocumentService;
import com.prcsteel.platform.account.service.AccountService;
import com.prcsteel.platform.account.service.WorkFlowService;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.common.service.FileService;
import com.prcsteel.platform.common.utils.FileUtil;
import com.prcsteel.platform.common.utils.StringToReplace;
import com.prcsteel.platform.common.utils.Tools;
import com.prcsteel.platform.core.persist.dao.CityDao;
import com.prcsteel.platform.core.persist.dao.ProvinceDao;

import constant.WorkFlowConstant;

/**
 * Created by lcw on 2015/7/14.
 */
@Service("accountBankService")
public class AccountBankServiceImpl implements AccountBankService {

	private static final Integer IS_DEFAULT = 0; // 否

	private static final Integer IS_DEFAULTS = 1; // 是默认

	public static final String ATTACHMENTSAVEPATH = "upload" + File.separator + "img" + File.separator + "bank" + File.separator;

	@Autowired
	private AccountBankDao accountBankDao;

	@Resource
	private AccountDao accountDao;

	@Resource
	FileService fileService;
	
    @Resource
    AccountBankService accountBankService;
    
    @Resource
    WorkFlowService workFlowService;

    @Resource
    TaskService taskService;
    
	@Resource
	ProcessService processService;
    
    @Resource
    AccountDocumentService accountDocumentService;
    
    @Resource
    AccountService accountService;
    
	private static final Logger logger = Logger.getLogger(AccountBankServiceImpl.class);

	@Resource
	ProvinceDao provinceDao;
	@Resource
	CityDao cityDao;

	@Override
	public int deleteByPrimaryKey(Long id) {
		return accountBankDao.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(AccountBank record) {
		return accountBankDao.insert(record);
	}

	@Override
	public int insertSelective(AccountBank record) {
		return accountBankDao.insertSelective(record);
	}

	@Override
	public AccountBank selectByPrimaryKey(Long id) {
		return accountBankDao.selectByPrimaryKey(id);
	}

	@Override
	public AccountBank selectByAccountCode(String bankAccountCode) {
		return accountBankDao.selectByAccountCode(bankAccountCode);
	}

	@Override
	public int updateByPrimaryKeySelective(AccountBank record) {
		return accountBankDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(AccountBank record) {
		return accountBankDao.updateByPrimaryKey(record);
	}

	/**
	 * 查询银行信息
	 *
	 * @param paramMap
	 * @return
	 */
	@Override
	public List<AccountBank> query(Map<String, Object> paramMap) {
		return accountBankDao.query(paramMap);
	}

	/**
	 * 获取已通过审核且没删除的银行信息，如果有设置默认银行，则默认银行排在第一位
	 * @param accountId
	 * @return
	 */
	@Override
	public List<AccountBank> queryAllEnabledBankByAccountId(Long accountId){
		return accountBankDao.queryAllEnabledBankByAccountId(accountId);
	}

	/**
	 * 根据客户账号ID查询银行信息
	 *
	 * @param accountId
	 * @return
	 */
	@Override
	public List<AccountBank> queryByAccountId(Long accountId) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("accountId", accountId);
		return accountBankDao.query(paramMap);
	}

	@Override
	@Transactional
	public boolean updateBankById(Long accountId, Long bankId)
			throws BusinessException {
		boolean flag = false;
		BankDto bankDto = new BankDto();
		bankDto.setAccountId(accountId);
		bankDto.setIsDefault(IS_DEFAULT);
		try {
			int num = accountBankDao.updateByAccountId(bankDto);
			if (num > 0) {
				updateBankInfo(bankId);
				flag = true;
			}
		} catch (Exception e) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,
					"设置客户默认银行出错了！");
		}
		return flag;
	}

	/**
	 * 修改客户默认银行
	 * 
	 * @param bankId
	 *            银行ID
	 * @return
	 */
	private int updateBankInfo(Long bankId) {
		BankDto bankDto = new BankDto();
		bankDto.setBankId(bankId);
		bankDto.setIsDefault(IS_DEFAULTS);
		return accountBankDao.updateBankById(bankDto);
	}

	/**
	 * 设置默认银行并将其他银行设为非默认银行
	 * 默认银行只能有一个
	 * @param bankId 银行id
	 * @param user 操作用户
	 */
	@Override
	@Transactional
	public void setDefaultBank(Long accountId,Long bankId,User user) {


		AccountBank bank  = accountBankDao.selectByPrimaryKey(bankId);
		if (bank == null) throw  new BusinessException(Constant.ERROR_RESOURCE_NOT_FOUND,"银行信息不存在");

		//检查是否为审核通过的银行，非审核通过的银行不能设置为默认银行
		if(!AccountBankDataStatus.Approved.getCode().equals(bank.getBankDataStatus())){
			throw  new BusinessException(Constant.ERROR_RESOURCE_NOT_FOUND,"该银行审核未通过,不能设置为默认银行");
		}


		//设置客户的其他银行为非默认银行
		BankDto bankDto = new BankDto();
		bankDto.setAccountId(accountId);
		bankDto.setIsDefault(IS_DEFAULT);
		bankDto.setLoginId(user.getLoginId());
		int num = accountBankDao.updateByAccountId(bankDto);
        if (num == 0) throw  new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,"设置默认银行失败");

		//设置当前银行为默认银行
		bank.setModificationNumber(bank.getModificationNumber() + 1);
		bank.setIsDefault(IS_DEFAULTS);//设置默认银行1
		bank.setLastUpdatedBy(user.getLoginId());
		bank.setLastUpdated(new Date());

		if(accountBankDao.updateByPrimaryKeySelective(bank) == 0){
			throw  new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,"设置默认银行失败");
		}
	}

	/**
	 * 保存银行信息
	 *
	 * @param image
	 * @param bank
	 */
	@Override
	@Transactional
	public void saveBankInfo(MultipartFile image, AccountBank bank,User user) {
        AccountExt oldExt = accountService.queryAccountExtByAccountId(bank.getAccountId());
        String oldBankStatus = oldExt == null ?"":oldExt.getBankDataStatus();
		//检查银行账号唯一性
		String bankAccountCode = StringToReplace.toReplaceAll(bank.getBankAccountCode());
		String accountBankStatus = AccountBankDataStatus.Requested.getCode();
		if(StringUtils.isNotEmpty(bankAccountCode)){
			AccountBank accountBank = accountBankDao.selectByAccountCode(bankAccountCode);
			if (accountBank != null && !accountBank.getId().equals(bank.getId())){
				throw  new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,"银行账号已存在");
			}
		}

		//打款资料图片保存: 编辑时 如果有图片未重新选择图片，则image为空,不重新上传图片 添加时必须有image
		if(image == null && StringUtils.isEmpty(bank.getUrl())){
			throw  new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,"打款资料文件不能为空");
		}

		if(image != null){
			saveImg(image, bank);
		}

		//替换掉所有空格
		bank.setBankAccountCode(bankAccountCode);
		bank.setBankCode(StringToReplace.toReplaceAll(bank.getBankCode())); // 开户银行行号
		bank.setBankName(StringToReplace.toReplaceAll(bank.getBankName())); // 开户银行主行
		bank.setBankNameBranch(StringToReplace.toReplaceAll(bank.getBankNameBranch())); // 开户银行支行

		bank.setLastUpdatedBy(user.getLoginId());
		bank.setLastUpdated(new Date());
		//查询客户下其他银行信息,如果有一条记录状态为审核未通过,状态设置为审核未通过,否则设置状态为待审核
    	Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("accountId", bank.getAccountId());
        List<AccountBank> bankList = accountBankDao.query(paramMap);
        bank.setBankDataStatus(AccountBankDataStatus.Requested.getCode());//待审核
        for(AccountBank accountBank: bankList){
			//modify by zhoucai@prcsteel.com 2016-5-12 如果为新增，则直接跳过判断
			if(bank.getId()!=null&& bank.getId() > 0){
				if(accountBank.getId().longValue() == bank.getId().longValue()){
					continue;
				}
			}

        	if(AccountBankDataStatus.Declined.getCode().equals(accountBank.getBankDataStatus())){
        		accountBankStatus = AccountBankDataStatus.Declined.getCode();//审核未通过
        		break;
        	}
        }
		//修改银行信息
		if(bank.getId() != null && bank.getId() > 0){
			AccountBank dbBank = accountBankDao.selectByPrimaryKey(bank.getId());
			if (dbBank == null){
				throw  new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,"银行信息不存在");
			}
			bank.setModificationNumber(dbBank.getModificationNumber() + 1);
			bank.setIsDefault(IS_DEFAULT);//非默认银行
			if (accountBankDao.updateByPrimaryKeySelective(bank) == 0){
				throw  new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,"编辑银行信息失败");
			}

			//如果银行之前是默认银行，则需要顺延一个"审核通过"的银行作为默认银行
			if (IS_DEFAULTS.equals(dbBank.getIsDefault())){
				//获取已通过审核且没删除的银行信息
				List<AccountBank> list = queryAllEnabledBankByAccountId(bank.getAccountId());
				if (list != null && list.size() > 0){
					//审核通过的，不存在默认银行，则顺延一个
					if(list.stream().filter(a->IS_DEFAULTS.equals(a.getIsDefault())).count() == 0){
						//根据id 倒序排序
						list.sort((b1, b2) -> b2.getId().compareTo(b1.getId()));
						AccountBank approvaledBank = list.get(0);
						approvaledBank.setLastUpdatedBy(user.getLoginId());
						approvaledBank.setLastUpdated(new Date());
						approvaledBank.setModificationNumber(approvaledBank.getModificationNumber() + 1);
						approvaledBank.setIsDefault(IS_DEFAULTS);//设置为默认银行
						if (accountBankDao.updateByPrimaryKeySelective(approvaledBank) == 0){
							throw  new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,"编辑银行信息失败");
						}
					}
				}
			}

		}else{
			//添加银行信息
			bank.setCreatedBy(user.getLoginId());
			bank.setCreated(new Date());
			bank.setModificationNumber(0);
			bank.setIsDeleted(0);
			bank.setIsDefault(IS_DEFAULT);//非默认银行

			if (accountBankDao.insertSelective(bank) == 0) {
				throw  new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,"添加银行信息失败");
			}
		}

		//更新客户的打款资料状态为当前银行信息的状态
		if(accountDao.updateBankDataStatusByPrimaryKey(bank.getAccountId(), accountBankStatus, null, null, user.getLoginId()) == 0){
			throw  new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,"更新客户的打款资料状态失败");
		}
		
		executeBankWorkFlow(bank,oldBankStatus);

	}

	/*
	 * 打款资料上传保存
	 *
	 * @return

	 */
	private void saveImg(MultipartFile image, AccountBank bank) {
		checkFile(image);

		String urlPath = ATTACHMENTSAVEPATH + Tools.dateToStr(new Date(), "yyyy-MM") + File.separator;
		String basePath = urlPath;
		String temppath = new Date().getTime() + "."
				+ FileUtil.getFileSuffix(image.getOriginalFilename());
		String savePath = basePath + temppath;
		String url = "";
		try {
			url = fileService.saveFile(image.getInputStream(), savePath);
			if (StringUtils.isEmpty(url)) {
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,
						"文件保存失败");
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,
					"文件保存失败");
		}
		bank.setUrl(url);//设置新url
	}

	private void checkFile(MultipartFile file) {
		String suffix = FileUtil.getFileSuffix(file.getOriginalFilename());

		if (suffix == null || !Constant.IMAGE_SUFFIX.contains(suffix.toLowerCase())) {
			throw new BusinessException(Constant.ERROR_MISSING_REQUIRED_PARAM,
					AttachmentType.valueOf(file.getName()).getName() + "文件格式不正确");
		}
		if (file.getSize() / Constant.M_SIZE > Constant.MAX_IMG_SIZE) {
			throw new BusinessException(Constant.ERROR_MISSING_REQUIRED_PARAM,
					AttachmentType.valueOf(file.getName()).getName() + "超过" + Constant.MAX_IMG_SIZE + "M");
		}
	}

	/**
	 * 逻辑删除银行信息
	 *
	 * @param bankId
	 * @param accountId
	 * @param user
	 */
	@Override
	public void updateDeletedById(Long bankId,Long accountId,User user) {
		AccountBank bank = accountBankDao.selectByPrimaryKey(bankId);
		if (bank == null){
			throw new BusinessException(Constant.ERROR_RESOURCE_NOT_FOUND,"银行信息已不存在" );
		}

		AccountBank updateBank  = new AccountBank();
		updateBank.setId(bankId);
		updateBank.setIsDeleted(1);//删除
		updateBank.setModificationNumber(bank.getModificationNumber() + 1);
		updateBank.setLastUpdatedBy(user.getLoginId());
		updateBank.setLastUpdated(new Date());
		if(accountBankDao.updateByPrimaryKeySelective(updateBank) == 0) {
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,"删除银行信息失败" );
		}else{
			if(accountId != null){
				try {
		            //删除后,执行工作流审核操作
		        	Map<String,Object> paramMap = new HashMap<String,Object>();
		            paramMap.put("accountId",accountId);
		            List<AccountBank> bankList = accountBankService.query(paramMap);
		            int count = 0;
		            int approvedCount = 0;
		            for(AccountBank accountBank:bankList){
		            	if(AccountBankDataStatus.Declined.getCode().equals(accountBank.getBankDataStatus())){
		            		break;
		            	}
		            	if(AccountBankDataStatus.Approved.getCode().equals(accountBank.getBankDataStatus())){
		            		approvedCount++;
		            	}
		            	count++;
		            }
		            //没有审核不通过的数据,推动审核流程
		            if(count == bankList.size()){
		            	ListTaskQuery taskQuery;
		            	List<TaskInfo> taskList;
		            	TaskActionInfo actionInfo;
		            	AccountDocumentDto accountDoc = new AccountDocumentDto();
		            	accountDoc.setId(accountId);
		            	taskQuery =  workFlowService.createEditTaskQuery(WorkFlowConstant.BAN_EDIT_BANK,accountId.toString());
		            	taskList = workFlowService.getAllTaskList(taskQuery);
		            	actionInfo =workFlowService.getNextTaskInfo(taskList, null,accountId.toString());
		            	if(actionInfo.getRequest() != null){
		            		taskService.invokeTaskAction(actionInfo.getTaskId(),actionInfo.getRequest());
		            		accountDoc.setBankDataStatus(AccountBankDataStatus.Requested.getCode());
		            	}
		            	//删除后,全部是审核通过或者没有记录,修改客户状态,继续推动流程
		            	if(approvedCount == bankList.size()){
		            		//更新主表
		                	accountDoc.setBankDataStatus(approvedCount > 0 ?AccountBankDataStatus.Approved.getCode():AccountBankDataStatus.Insufficient.getCode());
		                	//推动流程
		                	taskQuery =  workFlowService.createAuditTaskQuery(WorkFlowEnum.ACCOUNT_AUDIT_BANK.getValue(),accountId.toString());
		                	taskList = workFlowService.getAllTaskList(taskQuery);
		            		List<ActivitiVariable> variables = new ArrayList<ActivitiVariable>();
		            		ActivitiVariable createVar = new ActivitiVariable(WorkFlowConstant.VAR_AUDIT_PASS,"1");
		            		variables.add(createVar);
		                	actionInfo =workFlowService.getNextTaskInfo(taskList, variables,accountId.toString());
		                	if(actionInfo.getRequest() != null){
		                		taskService.invokeTaskAction(actionInfo.getTaskId(),actionInfo.getRequest());
		                	}
		            	}
		            	if(StringUtils.isNotEmpty(accountDoc.getBankDataStatus())){
		            		accountDocumentService.updateAccountExtStatus(accountDoc);
		            	}
		            }
				} catch (Exception e) {
					throw new BusinessException(Constant.EXCEPTIONCODE_UNKONWN, "启动审核流程失败");
				}
			}
		}
	}

	@Override
	public int batchUpdate(List<AccountBank> list) {
		return accountBankDao.batchUpdate(list);
	}

	@Override
	public int updateBankStatusByAccountId(AccountBank bank) {
		return accountBankDao.updateBankStatusByAccountId(bank);
	}
	
	/**
	 * 执行银行工作流
	 */
	private void executeBankWorkFlow(AccountBank bank,String oldBankStatus){
		try {
            StartProcessRequest startRequest;
            ListTaskQuery taskQuery;
            List<TaskInfo> taskList;
            //打款资料
            if(AccountBankDataStatus.Requested.getCode().equals(bank.getBankDataStatus())){
            	Map<String,Object> paramMap = new HashMap<String,Object>();
                paramMap.put("accountId", bank.getAccountId());
                List<AccountBank> bankList = accountBankService.query(paramMap);
                int allBankStatusCount = 0;
                for(AccountBank accountBank: bankList){
                	if(AccountBankDataStatus.Requested.getCode().equals(accountBank.getBankDataStatus())
                			|| AccountBankDataStatus.Approved.getCode().equals(accountBank.getBankDataStatus())){
                		allBankStatusCount++;
                	}
                }
            	//未上传,已通过,-待审核,启用新的任务
            	if(StringUtils.isEmpty(oldBankStatus)|| AccountBankDataStatus.Insufficient.getCode().equals(oldBankStatus) 
            			|| AccountBankDataStatus.Approved.getCode().equals(oldBankStatus)){
            		//所有银行卡信息是待审核或者已审核通过的情况
            		if(allBankStatusCount == bankList.size()){
	            		startRequest = workFlowService.createStartRequest(WorkFlowEnum.ACCOUNT_AUDIT_BANK.getValue().toString(),bank.getAccountId(),
	        					WorkFlowEnum.BANK_DATA,WorkFlowConstant.BAN_PROCESS_DEFINITION_KEY);
	            		processService.startProcess(startRequest);
            		}
            		//审核未通过,待审核-待审核,继续任务
            	}else if(AccountBankDataStatus.Declined.getCode().equals(oldBankStatus)||
            			AccountBankDataStatus.Requested.getCode().equals(oldBankStatus)){
            		//所有银行卡信息是待审核或者已审核通过的情况
            		if(allBankStatusCount == bankList.size()){
                    	taskQuery =  workFlowService.createEditTaskQuery(WorkFlowConstant.BAN_EDIT_BANK,bank.getAccountId().toString());
                    	taskList = workFlowService.getAllTaskList(taskQuery);
                    	TaskActionInfo actionInfo =workFlowService.getNextTaskInfo(taskList, null,bank.getAccountId().toString());
                    	if(actionInfo.getRequest() != null){
                    		taskService.invokeTaskAction(actionInfo.getTaskId(),actionInfo.getRequest());
                    	}
            		}
            	}
            }
		} catch (Exception e) {
			throw new BusinessException(Constant.EXCEPTIONCODE_UNKONWN, "启动审核流程失败");
		}
	}
}
