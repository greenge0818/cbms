package com.prcsteel.platform.kuandao.service.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.prcsteel.platform.acl.model.model.SysSetting;
import com.prcsteel.platform.acl.service.SysSettingService;
import com.prcsteel.platform.common.utils.SendMailHelper;
import com.prcsteel.platform.core.service.CommonService;
import com.prcsteel.platform.kuandao.api.RestAccountService;
import com.prcsteel.platform.kuandao.common.constant.KuandaoAccountConstant;
import com.prcsteel.platform.kuandao.common.util.BeanUtil;
import com.prcsteel.platform.kuandao.model.dto.CustAccountDto;
import com.prcsteel.platform.kuandao.model.dto.KuandaoAccountDto;
import com.prcsteel.platform.kuandao.model.dto.RestResultDto;
import com.prcsteel.platform.kuandao.model.dto.SynchronizeLogDto;
import com.prcsteel.platform.kuandao.model.dto.spdb.BoundNotifyTransaction;
import com.prcsteel.platform.kuandao.model.dto.spdb.MemeberQueryTransaction;
import com.prcsteel.platform.kuandao.model.dto.spdb.MemeberTransaction;
import com.prcsteel.platform.kuandao.model.dto.spdb.SPDBNotifyRequstParam;
import com.prcsteel.platform.kuandao.model.dto.spdb.SPDBResponseResult;
import com.prcsteel.platform.kuandao.model.enums.AccountBoundOperate;
import com.prcsteel.platform.kuandao.model.enums.AccountModifyStatusEnum;
import com.prcsteel.platform.kuandao.model.enums.AccountStatusEnum;
import com.prcsteel.platform.kuandao.model.enums.AccountTranstypeEnum;
import com.prcsteel.platform.kuandao.model.enums.ESBResultStatus;
import com.prcsteel.platform.kuandao.model.enums.ErrorMessage;
import com.prcsteel.platform.kuandao.model.enums.KuandaoResultEnum;
import com.prcsteel.platform.kuandao.model.enums.OperateResultEnum;
import com.prcsteel.platform.kuandao.model.enums.OperateTypeEnum;
import com.prcsteel.platform.kuandao.model.enums.SPDBNotifyResponseStatus;
import com.prcsteel.platform.kuandao.model.enums.SPDBResponseEnum;
import com.prcsteel.platform.kuandao.model.model.KuandaoCustAccount;
import com.prcsteel.platform.kuandao.model.model.SynchronizeLog;
import com.prcsteel.platform.kuandao.persist.dao.KuandaoAccountDao;
import com.prcsteel.platform.kuandao.service.KuanDaoProxyService;
import com.prcsteel.platform.kuandao.service.KuandaoAccountService;
import com.prcsteel.platform.kuandao.service.KuandaoSequenceService;
import com.prcsteel.platform.kuandao.service.MemeberService;

@Service("kuandao.service.AccountService")
public class KuandaoAccountServiceImpl implements KuandaoAccountService {

	private static final Logger logger = LoggerFactory.getLogger(KuandaoAccountServiceImpl.class);
	
	private static final String SYSTEM = "system";
 	
	private static final String START = "start";
 	
	private static final String LENGTH = "length";
 	
	@Resource(type=KuandaoAccountDao.class)
	private KuandaoAccountDao kuandaoAccountDao;
	
	@Resource
	private RestAccountService restAccountService;
	
	@Resource
	private MemeberService memeberService;
	
	@Resource
	private KuandaoSequenceService kuandaoSequenceService;
	
	@Resource
    private CommonService commonService;
	
	@Resource
	private SysSettingService sysSettingService;

	@Resource
	private KuandaoToAccountProcess  kuandaoToAccountProcess;

	@Resource
	private KuanDaoProxyService kuanDaoProxyService;
	
	@Override
	public List<KuandaoAccountDto> queryOpenedAccount(KuandaoAccountDto accountDto,int start,int length) {
		Map<String,Object> param = Maps.newHashMap();
		param.put("memeberName", accountDto.getMemeberName());
		param.put("memeberCode", accountDto.getMemeberCode());
		param.put("virAcctNo", accountDto.getVirAcctNo());
		param.put("status", accountDto.getStatus());
		param.put(START, start);
		param.put(LENGTH, length);
		return kuandaoAccountDao.queryOpenedAccount(param);
	}

	@Override
	public Integer totalOpenedAccount(KuandaoAccountDto accountDto) {
		return kuandaoAccountDao.totalOpenedAccount(accountDto);
	}

	@Override
	public List<KuandaoAccountDto> queryUnOpenedAccount(KuandaoAccountDto accountDto,int start,int length) {
		Map<String,Object> param = Maps.newHashMap();
		param.put("memeberName", accountDto.getMemeberName());
		param.put("orgId", accountDto.getOrgId());
		param.put("custType", accountDto.getCustType());
		param.put("status", accountDto.getStatus());
		param.put(START, start);
		param.put(LENGTH, length);
		List<KuandaoAccountDto> accountDtoList = kuandaoAccountDao.queryUnOpenedAccount(param);
		accountDtoList.forEach(kuandaoAccountDto -> {

			if(kuandaoAccountDto.getMobile() == null)
			kuandaoAccountDto.setMobile(KuandaoAccountConstant.MOBILE);
		});
		return accountDtoList;
	}

	@Override
	public int totalUnOpenedAccount(KuandaoAccountDto accountDto) {
		return kuandaoAccountDao.totalUnOpenedAccount(accountDto);
	}
	
	@Override
	public Integer totalSynchronizeLog(SynchronizeLogDto synLogDto) {
		return kuandaoAccountDao.totalSynchronizeLog(synLogDto);
	}

	@Override
	public List<SynchronizeLogDto> querySynchronizeLog(SynchronizeLogDto synLogDto, Integer start, Integer length) {
		Map<String,Object> param = Maps.newHashMap();
		param.put("memeberName", synLogDto.getMemeberName());
		param.put("orgId", synLogDto.getOrgId());
		param.put("result", synLogDto.getResult());
		param.put(START, start);
		param.put(LENGTH, length);
		return kuandaoAccountDao.querySynchronizeLog(param);
	}

	
	@Override
	public Integer modifyCustAccount(KuandaoAccountDto kuandaoAccountDto) {
		
		Long acctId = kuandaoAccountDto.getAcctId();
		String memeberName = kuandaoAccountDto.getMemeberName();
		String idNo = kuandaoAccountDto.getIdNo();
		String mobile = kuandaoAccountDto.getMobile();
		String userName = kuandaoAccountDto.getCreatedBy();
		
		//款道会员修改状态  0 未修改 1 会员名、手机号被修改 2 组织机构代码被修改 
		Integer modificationStatus = AccountModifyStatusEnum.unmodify.getStatus();
		if(StringUtils.isEmpty(idNo)){ //组织机构代码不能为空
			return KuandaoResultEnum.notnull.getCode();
		}
		//影响条数
		int count;
		String restResult = "";
		try{
			//查询并修改客户信息
			restResult = restAccountService.queryById(acctId);
		}catch(ResourceAccessException e){
			logger.error("rest api request failed", e);
			return KuandaoResultEnum.timeout.getCode();
		}catch(Exception e){
			logger.error("rest api exchange failed", e);
			return KuandaoResultEnum.systemerror.getCode();
		}
		Gson gson = new Gson();
		
		RestResultDto restAccountDto = gson.fromJson(restResult, RestResultDto.class);
		if(!ESBResultStatus.success.getCode().equals(restAccountDto.getStatus()) || StringUtils.isEmpty(restAccountDto.getData()) ){ //会员信息不存在
			return KuandaoResultEnum.nodata.getCode();
		}
		CustAccountDto account = gson.fromJson(restAccountDto.getData(), CustAccountDto.class);
		KuandaoAccountDto queryAccountDto = new KuandaoAccountDto();
		queryAccountDto.setMemeberName(memeberName);
		List<KuandaoAccountDto>  kuandaoAccountDtoList = kuandaoAccountDao.queryAccountByCondition(queryAccountDto);
		if(kuandaoAccountDtoList.size() > 1 || kuandaoAccountDtoList.get(0).getAcctId().compareTo(acctId) != 0)	{//有多条或者修改的名称不是自己
			return KuandaoResultEnum.businesserror.getCode();
		}
			
		String name = account.getName();
		String orgCode = account.getOrgCode();
		if(!StringUtils.equals(memeberName, name)){
			account.setName(memeberName);
			modificationStatus = AccountModifyStatusEnum.namemodify.getStatus();
		}
		if(!StringUtils.equals(idNo, orgCode)){
			account.setOrgCode(idNo);
			modificationStatus = AccountModifyStatusEnum.idnomodify.getStatus();
		}
		if(modificationStatus.compareTo(AccountModifyStatusEnum.unmodify.getStatus()) != 0){
			CustAccountDto custAccountDto = new CustAccountDto();
			custAccountDto.setId(acctId);
			custAccountDto.setName(memeberName);
			custAccountDto.setOrgCode(idNo);
			boolean result = kuandaoToAccountProcess.sendAccount(custAccountDto);
			if(!result){
				return KuandaoResultEnum.systemerror.getCode();
			}
		}
		
		
	
		//查询款道会员信息
		KuandaoAccountDto queryKuandaoAccountDto = new KuandaoAccountDto();
		queryKuandaoAccountDto.setAcctId(acctId);
		List<KuandaoCustAccount> accountList = kuandaoAccountDao.queryKuandaoAccountByCondition(queryKuandaoAccountDto);
		if(accountList == null || accountList.isEmpty()){//无款道账号
			KuandaoCustAccount custAccount = new KuandaoCustAccount();
			custAccount.setAcctId(acctId);
			custAccount.setMobile(mobile);
			custAccount.setCreatedBy(userName);
			custAccount.setLastUpdatedBy(userName);
			custAccount.setStatus(AccountStatusEnum.init.getCode());
			count = this.insert(custAccount);
		}else if(accountList.size() == 1){	//有款道账号
			KuandaoCustAccount custAccount = accountList.get(0);
			//款道电话有变化
			if(!StringUtils.equals(custAccount.getMobile(), mobile)){
				custAccount.setMobile(mobile);
				custAccount.setLastUpdatedBy(userName);
				if(modificationStatus.compareTo(AccountModifyStatusEnum.idnomodify.getStatus()) != 0) {
					modificationStatus = AccountModifyStatusEnum.mobilemodify.getStatus();
				}
				custAccount.setModificationStatus(modificationStatus);
				count = this.update(custAccount);
			}else{
				count = accountList.size();
			}
		}else{
			count = accountList.size();
			logger.error(String.format("款道修改客户信息失败，存在%s笔客户id为%s的账号", count,acctId));
			return count;
		}
		
		return count;
	}

	/**
	 * 更新客户信息
	 * @param custAccount
	 * @return
	 */
	private int update(KuandaoCustAccount custAccount) {
		custAccount.setLastUpdated(new Date());
		return kuandaoAccountDao.update(custAccount);
	}

	/**
	 * 新增客户
	 * 客户类型全部为企业
	 * 证件类型全部为企业组织机构代码
	 * @param custAccount
	 * @return
	 */
	private int insert(KuandaoCustAccount custAccount) {
		custAccount.setType(KuandaoAccountConstant.TYPE);
		custAccount.setIdType(KuandaoAccountConstant.ID_TYPE);
		custAccount.setCreated(new Date());
		custAccount.setLastUpdated(new Date());
		return kuandaoAccountDao.insert(custAccount);
	}


	@Override
	public Integer openAccount(Long acctId, String userName) {
		
		List<KuandaoAccountDto> kuandaoAccountDtoList = getNativeAccount(acctId);
		int resultCode = validAccount(acctId, kuandaoAccountDtoList);
		if(resultCode != KuandaoResultEnum.success.getCode())
			return resultCode;
		
		KuandaoAccountDto kuandaoAccountDto = kuandaoAccountDtoList.get(0);
		String mobile = kuandaoAccountDto.getMobile();
		//没有手机号给默认款道手机号
		if(StringUtils.isEmpty(mobile)) {
			kuandaoAccountDto.setMobile(KuandaoAccountConstant.MOBILE);
		}
		
		SynchronizeLog synchronizeLog = new SynchronizeLog();
		if(SYSTEM.equals(userName)){
			synchronizeLog.setType(OperateTypeEnum.automatic.getType());
		}else{
			synchronizeLog.setType(OperateTypeEnum.manual.getType());
		}
		synchronizeLog.setDescription(AccountTranstypeEnum.open.getText());
		synchronizeLog.setCreatedBy(userName);
		synchronizeLog.setLastUpdatedBy(userName);
		
		
		KuandaoCustAccount kuandaoCustAccount = new KuandaoCustAccount();
		kuandaoCustAccount.setLastUpdatedBy(userName);
		if(kuandaoAccountDto.getId() == null){	//会员表中没有信息
			kuandaoCustAccount.setAcctId(kuandaoAccountDto.getAcctId());
			kuandaoCustAccount.setMobile(kuandaoAccountDto.getMobile());
			kuandaoCustAccount.setCreatedBy(userName);
			int i =this.insert(kuandaoCustAccount);
			if(i != 1){
				resultCode = KuandaoResultEnum.systemerror.getCode();
				synchronizeLog.setErrMsg("新增款道客户失败:生成多条记录");
			}
			kuandaoAccountDto.setId(kuandaoCustAccount.getId()); //返回主键
		}else{
			kuandaoCustAccount.setId(kuandaoAccountDto.getId());
		}
		
		
		if(resultCode == KuandaoResultEnum.success.getCode()){
			resultCode = registerMemeber(kuandaoAccountDto, kuandaoCustAccount, synchronizeLog);
			//更新客户信息
			int i = this.update(kuandaoCustAccount);
			if(i != 1 && resultCode == KuandaoResultEnum.success.getCode()){ //优先展示接口的错误日志
				resultCode = KuandaoResultEnum.systemerror.getCode();
				synchronizeLog.setErrMsg("更新款道客户信息失败");
			}
		}
		
		// 记录日志
		synchronizeLog.setAcctId(kuandaoAccountDto.getId());
		addSynchronizeLog(synchronizeLog);
		return resultCode;
	}

	/***
	 * 会员开户
	 * 实现开户，记录开户信息，记录日志，返回结果码
	 * @param kuandaoAccountDto
	 * @param kuandaoCustAccount
	 * @param synchronizeLog
	 * @return
	 */
	private int registerMemeber(KuandaoAccountDto kuandaoAccountDto,
			KuandaoCustAccount kuandaoCustAccount, SynchronizeLog synchronizeLog) {
		int resultCode = KuandaoResultEnum.systemerror.getCode();
		if(StringUtils.isEmpty(kuandaoAccountDto.getMemeberCode()))
			kuandaoAccountDto.setMemeberCode(kuandaoSequenceService.generateKuandaoAcctCode());//生成会员号
		SPDBResponseResult result = memeberService.regiterMemeber(kuandaoAccountDto);
		if(result == null){
			kuandaoCustAccount.setResult(OperateResultEnum.fail.getResult());
			kuandaoCustAccount.setStatus(AccountStatusEnum.init.getCode());
			synchronizeLog.setResult(OperateResultEnum.fail.getResult());
			synchronizeLog.setErrMsg(ErrorMessage.spdbTransFailed.getErrMsg());
		}else if(result.isSuccess()){
			String plain = result.getPlain();
			BeanUtil<MemeberTransaction> beanUtil = new BeanUtil<>();
			MemeberTransaction transResult = beanUtil.stringToBean(plain, MemeberTransaction.class);
			//客户信息记录到数据库 
			kuandaoCustAccount.setApplyTime(new Date());
			kuandaoCustAccount.setMemeberCode(kuandaoAccountDto.getMemeberCode());
			kuandaoCustAccount.setVirAcctNo(transResult.getVirAcctNo());
			kuandaoCustAccount.setStatus(AccountStatusEnum.open.getCode());
			kuandaoCustAccount.setResult(OperateResultEnum.success.getResult());

			synchronizeLog.setResult(OperateResultEnum.success.getResult());
			resultCode = KuandaoResultEnum.success.getCode();
		}else{
			kuandaoCustAccount.setResult(OperateResultEnum.fail.getResult());
			kuandaoCustAccount.setStatus(AccountStatusEnum.init.getCode());
			synchronizeLog.setResult(OperateResultEnum.fail.getResult());
			synchronizeLog.setErrMsg(result.getErrMsg());
		}
		return resultCode;
	}

	/**
	 * 验证客户数据
	 *  客户名称、组织机构代码
	 * @param acctId
	 * @param kuandaoAccountDtoList
	 * @return
	 */
	private int validAccount(Long acctId, List<KuandaoAccountDto> kuandaoAccountDtoList) {
		if(kuandaoAccountDtoList.size() != 1){
			logger.error(String.format("客户id为%s的数据异常", acctId));
			return KuandaoResultEnum.nodata.getCode();
		}
		KuandaoAccountDto kuandaoAccountDto = kuandaoAccountDtoList.get(0);
		//验证客户名称、组织机构代码如果为空，返回用户界面，完善信息
		String memeberName = kuandaoAccountDto.getMemeberName();
		String idNo = kuandaoAccountDto.getIdNo();
		if(StringUtils.isEmpty(memeberName) || StringUtils.isEmpty(idNo)){
			return KuandaoResultEnum.notnull.getCode();
		}
		return KuandaoResultEnum.success.getCode();
	}

	//获取系统内的客户信息
	private List<KuandaoAccountDto> getNativeAccount(Long acctId) {
		KuandaoAccountDto queryAccountDto = new KuandaoAccountDto();
		queryAccountDto.setAcctId(acctId);
		return kuandaoAccountDao.queryAccountByCondition(queryAccountDto);
	}
	
	
	/**
	 * 记录日志
	 * @param synchronizeLog
	 */
	private void addSynchronizeLog(SynchronizeLog synchronizeLog){
		Date now = new Date();
		synchronizeLog.setCreated(now);
		synchronizeLog.setLastUpdated(now);
		kuandaoAccountDao.insertSynchronizeLog(synchronizeLog);
	}

	@Override
	public Integer synchronizeAccount(Long acctId, String userName) {
		
		List<KuandaoAccountDto> kuandaoAccountDtoList = getNativeAccount(acctId);
		int resultCode = validOpenedAccount(acctId, kuandaoAccountDtoList);
		if(resultCode != KuandaoResultEnum.success.getCode())
			return resultCode;
		KuandaoAccountDto kuandaoAccountDto = kuandaoAccountDtoList.get(0);
		resultCode = synchronize(userName, kuandaoAccountDto);
		return resultCode;
	}

	/**
	 * 会员信息同步
	 *	<ol>查询浦发会员信息</ol>
	 *	<ol>同步会员信息到浦发</ol>
	 *	<ol>同步浦发会员信息到本地</ol>
	 * @param userName				操作人员
	 * @param kuandaoAccountDto
	 * @return
	 */
	private int synchronize(String userName, KuandaoAccountDto kuandaoAccountDto) {
		
			int resultCode = KuandaoResultEnum.systemerror.getCode();
			//记录日志
			SynchronizeLog synchronizeLog = new SynchronizeLog();
			synchronizeLog.setAcctId(kuandaoAccountDto.getId());
			if("system".equals(userName)){
				synchronizeLog.setType(OperateTypeEnum.automatic.getType());
			}else{
				synchronizeLog.setType(OperateTypeEnum.manual.getType());
			}
			synchronizeLog.setCreatedBy(userName);
			synchronizeLog.setLastUpdatedBy(userName);
			synchronizeLog.setDescription(AccountTranstypeEnum.modify.getText());
			SPDBResponseResult result = memeberService.queryMemeberInfo(kuandaoAccountDto.getMemeberCode());
			if(result == null){
				synchronizeLog.setErrMsg(ErrorMessage.spdbTransFailed.getErrMsg());
			}else if(result.isSuccess()){
				String plain = result.getPlain();
				BeanUtil<MemeberQueryTransaction> queryBeanUtil = new BeanUtil<>();
				MemeberQueryTransaction transResult = queryBeanUtil.stringToBean(plain, MemeberQueryTransaction.class);
				//先同步到浦发
				resultCode = synchronizeToSpdb(kuandaoAccountDto, synchronizeLog, transResult);
				if(resultCode == KuandaoResultEnum.success.getCode()){
					kuandaoAccountDto.setFailureCount(0);
				}else{
					int failureCount = kuandaoAccountDto.getFailureCount() == null ? 0 : kuandaoAccountDto.getFailureCount();
					kuandaoAccountDto.setFailureCount(failureCount + 1);
				}
				//再同步到本地（无论浦发同步成功或失败都会同步一次本地）
				int i = synchronizeToLocal(kuandaoAccountDto, transResult);
				if(resultCode == KuandaoResultEnum.success.getCode()) {//如果浦发同步成功记录同步到本地的状态
					resultCode = i; 
				}
			}else{
				synchronizeLog.setErrMsg(result.getErrMsg());
				resultCode = KuandaoResultEnum.systemerror.getCode();
			}
			
			if(resultCode == KuandaoResultEnum.success.getCode()){
				synchronizeLog.setResult(OperateResultEnum.success.getResult());
			}else{
				synchronizeLog.setResult(OperateResultEnum.fail.getResult());
				if(resultCode == KuandaoResultEnum.dataoperateerror.getCode()){
					synchronizeLog.setErrMsg("客户信息从浦发同步到本地失败");
				}
			}
			addSynchronizeLog(synchronizeLog);
			return resultCode;
	}

	/**
	 * 会员信息同步到浦发
	 * <li>组织机构代码变更需要删除会员后再开户</li>
	 * <li>客户名和手机号变更直接调用修改接口</li>
	 * @param kuandaoAccountDto
	 * @param synchronizeLog
	 * @param transResult
	 * @return
	 */
	private int synchronizeToSpdb(KuandaoAccountDto kuandaoAccountDto, SynchronizeLog synchronizeLog,
			MemeberQueryTransaction transResult) {
		String nativeIdNo = kuandaoAccountDto.getIdNo();
		String nativeMemeberName = kuandaoAccountDto.getMemeberName();
		String nativeMobile = kuandaoAccountDto.getMobile();
		
		String remoteIdNo = transResult.getIdNo();
		String remoteMemeberName = transResult.getSubMerName();
		String remoteMobile = transResult.getMobileNo();
		//记录修改内容
		StringBuilder logDescription = new StringBuilder();
		
		kuandaoAccountDto.setVirAcctNo(transResult.getSubMerVirAcctNo());
		kuandaoAccountDto.setStatus(transResult.getStatus()); //同步会员状态
		//记录同步结果
		int resultCode = KuandaoResultEnum.systemerror.getCode();
		//修改状态
		int modificationStatus = 0;
		if(!StringUtils.equals(nativeIdNo, remoteIdNo)){ // 组织机构代码有变化，直接同步
			modificationStatus = modificationStatus | AccountModifyStatusEnum.idnomodify.getStatus();
			logDescription.append("组织机构代码由“").append(remoteIdNo).append("”改为“").append(nativeIdNo).append("”；");
			
		}
		if(!StringUtils.equals(nativeMemeberName, remoteMemeberName)){
			modificationStatus = modificationStatus | AccountModifyStatusEnum.namemodify.getStatus();
			logDescription.append("公司名称由“").append(remoteMemeberName).append("”改为“").append(nativeMemeberName).append("”；");
		}
		if(!StringUtils.equals(nativeMobile, remoteMobile)){
			modificationStatus = modificationStatus | AccountModifyStatusEnum.mobilemodify.getStatus();
			logDescription.append("手机号由“").append(remoteMobile).append("”改为“").append(nativeMobile).append("”；");
		}
		
		if(modificationStatus != AccountModifyStatusEnum.unmodify.getStatus()){//修改信息后调用同步接口
			if(AccountStatusEnum.open.getCode().equals(transResult.getStatus()) || AccountStatusEnum.bound.getCode().equals(transResult.getStatus())){
				if(AccountStatusEnum.bound.getCode().equals(transResult.getStatus())){ //开通状态下不能修改组织机构代码
					kuandaoAccountDto.setIdNo(null);
				}
				SPDBResponseResult modifyResult = memeberService.modifyMemeber(kuandaoAccountDto);
				if(modifyResult == null){
					synchronizeLog.setErrMsg(ErrorMessage.spdbTransFailed.getErrMsg());
				}else if(modifyResult.isSuccess()){
					String modifyPlain = modifyResult.getPlain();
					BeanUtil<MemeberTransaction> modifyBeanUtil = new BeanUtil<>();
					MemeberTransaction modifyTransaction = modifyBeanUtil.stringToBean(modifyPlain, MemeberTransaction.class);
					if(SPDBResponseEnum.success.getCode().equals(modifyTransaction.getRespCode())){
						resultCode = KuandaoResultEnum.success.getCode();
					}else{
						synchronizeLog.setErrMsg(String.format("修改会员信息交易失败，错误码：%s，描述：%s", modifyTransaction.getRespCode(),SPDBResponseEnum.getTextByCode(modifyTransaction.getRespCode())));
					}
				}else{
					synchronizeLog.setErrMsg("修改会员信息交易失败：" + modifyResult.getErrMsg());
				}
			}else{
				synchronizeLog.setErrMsg("会员不是待绑定或开通状态，不能修改手机号和会员名");
			}
			
		}else{ //没有信息需要修改
			resultCode = KuandaoResultEnum.success.getCode();
		}
		if(resultCode == KuandaoResultEnum.success.getCode()){ //同步到浦发成功后将修改状态改为未修改
			kuandaoAccountDto.setModificationStatus(AccountModifyStatusEnum.unmodify.getStatus());
		}
		if(logDescription.length() > 1) {
			synchronizeLog.setDescription(logDescription.deleteCharAt(logDescription.length() - 1).toString());
		}
		return resultCode;
	}
	
	/***
	 * 会员信息保存到本地
	 * <li>虚账户</li>
	 * <li>开户行</li>
	 * <li>状态</li>
	 * <li>待提取金额</li>
	 * @param kuandaoAccountDto
	 * @param transResult
	 * @return
	 */
	private int synchronizeToLocal(KuandaoAccountDto kuandaoAccountDto,MemeberQueryTransaction transResult){
		int resultCode = KuandaoResultEnum.dataoperateerror.getCode();
		String drawAmt = transResult.getDrawAmt();
		if(StringUtils.isEmpty(drawAmt)) {
			drawAmt = "0";
		}
		KuandaoCustAccount account = new KuandaoCustAccount();
		account.setId(kuandaoAccountDto.getId());
		account.setStatus(kuandaoAccountDto.getStatus());
		account.setVirAcctNo(kuandaoAccountDto.getVirAcctNo());
		account.setAcctNo(transResult.getSubMerAcctNo());
		account.setBranchId(transResult.getSubMerBranchId());
		account.setDepartmentId(transResult.getDeptId());
		account.setDrawAmt(new BigDecimal(drawAmt));
		account.setModificationStatus(kuandaoAccountDto.getModificationStatus());
		if(account.getId() != null){
			int i = update(account);
			if(i == 1) {
				resultCode = KuandaoResultEnum.success.getCode();
			}
		}
		return resultCode;
	}

	/**
	 * 已开户的客户信息校验
	 * 客户名称、组织机构代码、款道电话、会员号不能为空
	 * @param acctId
	 * @param kuandaoAccountDtoList
	 * @return
	 */
	private int validOpenedAccount(Long acctId, List<KuandaoAccountDto> kuandaoAccountDtoList) {
		if(kuandaoAccountDtoList.size() != 1){
			logger.error(String.format("客户id为%s的数据异常", acctId));
			return KuandaoResultEnum.nodata.getCode();
		}
		KuandaoAccountDto kuandaoAccountDto = kuandaoAccountDtoList.get(0);
		//验证客户名称、组织机构代码、款道电话、会员号如果为空，返回用户界面，完善信息
		String memeberName = kuandaoAccountDto.getMemeberName();
		String idNo = kuandaoAccountDto.getIdNo();
		String mobile = kuandaoAccountDto.getMobile();
		String memeberCode = kuandaoAccountDto.getMemeberCode();
		if(StringUtils.isEmpty(memeberName) || StringUtils.isEmpty(idNo) || StringUtils.isEmpty(mobile) || StringUtils.isEmpty(memeberCode)){
			return KuandaoResultEnum.notnull.getCode();
		}
		return KuandaoResultEnum.success.getCode();
	}
	
	
	@Override
	public void synchronizeAccountByJob() {
		List<KuandaoAccountDto> openAccountList = kuandaoAccountDao.queryAllOpenAccount();
		List<KuandaoAccountDto> syncFailedList = Lists.newArrayList();
		
		openAccountList.forEach(kuandaoAccountDto -> {
			int resultCode = synchronize(SYSTEM, kuandaoAccountDto);
			if(resultCode != KuandaoResultEnum.success.getCode()){
				syncFailedList.add(kuandaoAccountDto);
			}
		});
		//获取短信接收号码
		List<SysSetting> mobileSettings = sysSettingService.queryByTypeAndValue("KuandaoSmsMobile", null);
		List<String> mobileList = Lists.newArrayList(); 
		mobileSettings.forEach(sysSetting -> mobileList.add(sysSetting.getSettingValue()) );
		//获取邮件接收地址
		List<SysSetting> emailSettings = sysSettingService.queryByTypeAndValue("KuandaoEmailtoAddress", null);
		List<String> emailList = Lists.newArrayList(); 
		emailSettings.forEach(sysSetting -> emailList.add(sysSetting.getSettingValue()));
		
		syncFailedList.forEach( kuandaoAccountDto -> {
			boolean result;
			if(!mobileList.isEmpty()){
				String smsContent = "您好，会员“" + kuandaoAccountDto.getMemeberName() + "”信息同步失败";
				String mobile = StringUtils.join(mobileList, ",");
				result = commonService.sendSMS(mobile, smsContent);
				if(!result){
					logger.error(String.format("kuandao sms send fail, toMobile: %s, content: %s", mobile,smsContent));
				}
			}else{
				logger.warn("not kuandao sms mobile config");
			}
			if(!emailList.isEmpty()){
				String emailSubject = "会员“" + kuandaoAccountDto.getMemeberName() + "”信息同步失败【款道】";
				String emailContent = "您好，会员“" + kuandaoAccountDto.getMemeberName() + "”信息同步失败";
				String toAddress = StringUtils.join(emailList,";");
				result = SendMailHelper.send(emailContent,emailSubject,toAddress);
				if(!result){
					logger.error(String.format("kuandao emial send fail, toAddress: %s, subject: %s", toAddress,emailSubject));
				}else{
					logger.warn("not kuandao emial toAddress config");
				}
			}
		});
	}
	
	
	@Override
	public Integer synchronizeAllToLocal(String userName) {
		List<KuandaoAccountDto> openAccountList = kuandaoAccountDao.queryAllOpenAccount();
		int resultCode = KuandaoResultEnum.success.getCode();

		openAccountList.forEach(

			kuandaoAccountDto -> {
				SynchronizeLog synchronizeLog = new SynchronizeLog();
				synchronizeLog.setAcctId(kuandaoAccountDto.getId());
				if(SYSTEM.equals(userName)){
					synchronizeLog.setType(OperateTypeEnum.automatic.getType());
				}else{
					synchronizeLog.setType(OperateTypeEnum.manual.getType());
				}
				synchronizeLog.setCreatedBy(userName);
				synchronizeLog.setLastUpdatedBy(userName);
				synchronizeLog.setDescription(AccountTranstypeEnum.modify.getText());
				SPDBResponseResult result = memeberService.queryMemeberInfo(kuandaoAccountDto.getMemeberCode());
				if(result == null){
					synchronizeLog.setErrMsg(ErrorMessage.spdbTransFailed.getErrMsg());
					synchronizeLog.setResult(OperateResultEnum.fail.getResult());
					logger.error(String.format("查询浦发会员【%s】信息失败", kuandaoAccountDto.getMemeberCode()));
				}else if(result.isSuccess()){
					String plain = result.getPlain();
					BeanUtil<MemeberQueryTransaction> queryBeanUtil = new BeanUtil<>();
					MemeberQueryTransaction transResult = queryBeanUtil.stringToBean(plain, MemeberQueryTransaction.class);
					kuandaoAccountDto.setStatus(transResult.getStatus());
					kuandaoAccountDto.setVirAcctNo(transResult.getSubMerVirAcctNo());
					int modifyStatus = 0;
					if(!StringUtils.equals(kuandaoAccountDto.getIdNo(), transResult.getIdNo())){
						modifyStatus = modifyStatus | AccountModifyStatusEnum.idnomodify.getStatus();
					}
					if(!StringUtils.equals(kuandaoAccountDto.getMemeberName(), transResult.getSubMerName())) {
						modifyStatus = modifyStatus | AccountModifyStatusEnum.namemodify.getStatus();
					}
					if(!StringUtils.equals(kuandaoAccountDto.getMobile(), transResult.getMobileNo())){
						modifyStatus = modifyStatus | AccountModifyStatusEnum.mobilemodify.getStatus();
					}
					kuandaoAccountDto.setModificationStatus(modifyStatus);
					synchronizeLog.setResult(OperateResultEnum.success.getResult());
					synchronizeToLocal(kuandaoAccountDto, transResult);
				}else{
					synchronizeLog.setResult(OperateResultEnum.fail.getResult());
					synchronizeLog.setErrMsg(result.getErrMsg());
				}
				addSynchronizeLog(synchronizeLog);
		});
		
		return resultCode;
	}
	
	@Override
	public Integer synchronizeAllToSpdb(String userName) {
		
		List<KuandaoAccountDto> openAccountList = kuandaoAccountDao.queryAllOpenAccount();
		int resultCode = KuandaoResultEnum.success.getCode();
		openAccountList.forEach(
			kuandaoAccountDto -> {
				
				SynchronizeLog synchronizeLog = new SynchronizeLog();
				synchronizeLog.setAcctId(kuandaoAccountDto.getId());
				synchronizeLog.setType(OperateTypeEnum.manual.getType());
				synchronizeLog.setCreatedBy(userName);
				synchronizeLog.setLastUpdatedBy(userName);
				synchronizeLog.setDescription(AccountTranstypeEnum.modify.getText());
				Integer innerResultCode = KuandaoResultEnum.systemerror.getCode();
				SPDBResponseResult result = memeberService.queryMemeberInfo(kuandaoAccountDto.getMemeberCode());
				if(result == null){
					synchronizeLog.setErrMsg(ErrorMessage.spdbTransFailed.getErrMsg());
				}else if(result.isSuccess()){
					String plain = result.getPlain();
					BeanUtil<MemeberQueryTransaction> queryBeanUtil = new BeanUtil<>();
					MemeberQueryTransaction transResult = queryBeanUtil.stringToBean(plain, MemeberQueryTransaction.class);
					innerResultCode = synchronizeToSpdb(kuandaoAccountDto, synchronizeLog, transResult);
					if(innerResultCode == KuandaoResultEnum.success.getCode()){
						kuandaoAccountDto.setFailureCount(0);
					}else{
						kuandaoAccountDto.setFailureCount(kuandaoAccountDto.getFailureCount() + 1);
					}
					kuandaoAccountDto.setStatus(transResult.getStatus());
					kuandaoAccountDto.setVirAcctNo(transResult.getSubMerVirAcctNo());
					
					synchronizeToLocal(kuandaoAccountDto, transResult);
				}else{
					synchronizeLog.setErrMsg(result.getErrMsg());
					innerResultCode = KuandaoResultEnum.systemerror.getCode();
				}
				
				if(innerResultCode == KuandaoResultEnum.success.getCode()){
					synchronizeLog.setResult(OperateResultEnum.success.getResult());
				}else{
					synchronizeLog.setResult(OperateResultEnum.fail.getResult());
					if(innerResultCode == KuandaoResultEnum.dataoperateerror.getCode()){
						synchronizeLog.setErrMsg("客户信息从浦发同步到本地失败");
					}
				}
				
				addSynchronizeLog(synchronizeLog);
			
		});
		
		return resultCode;
	}

	@Override
	public Integer batchOpenAccount(String userName,List<Long> acctIdList) {
		acctIdList.forEach(acctId -> openAccount(acctId, userName));
		
		return KuandaoResultEnum.success.getCode();
	}

	@Override
	public Integer closeAccount(String userName, Long acctId) {
		Integer resultCode = KuandaoResultEnum.systemerror.getCode();
		KuandaoAccountDto queryAccount = new KuandaoAccountDto();
		queryAccount.setAcctId(acctId);
		List<KuandaoCustAccount>  kuandaoCustAccountList = kuandaoAccountDao.queryKuandaoAccountByCondition(queryAccount);
		if(kuandaoCustAccountList.size() == 1){
			KuandaoCustAccount kuandaoCustAccount = kuandaoCustAccountList.get(0);
			if(AccountStatusEnum.close.getCode().equals(kuandaoCustAccount.getStatus())){	//已经删除的客户不能再次删除
				resultCode = KuandaoResultEnum.businesserror.getCode();
			}else{
				SynchronizeLog synchronizeLog = new SynchronizeLog();
				synchronizeLog.setAcctId(kuandaoCustAccount.getId());
				synchronizeLog.setType(OperateTypeEnum.manual.getType());
				synchronizeLog.setCreatedBy(userName);
				synchronizeLog.setLastUpdatedBy(userName);
				synchronizeLog.setDescription(AccountTranstypeEnum.delete.getText());
				
				SPDBResponseResult result = memeberService.deleteMemeber(kuandaoCustAccount.getMemeberCode());
				if(result == null){
					synchronizeLog.setErrMsg(ErrorMessage.spdbTransFailed.getErrMsg());
				}else if(result.isSuccess()){
					BeanUtil<MemeberTransaction> beanUtil = new BeanUtil<>();
					MemeberTransaction deleteTransaction = beanUtil.stringToBean(result.getPlain(), MemeberTransaction.class);
					if(SPDBResponseEnum.success.getCode().equals(deleteTransaction.getRespCode())){
						KuandaoCustAccount custAccount = new KuandaoCustAccount();
						custAccount.setId(kuandaoCustAccount.getId());
						custAccount.setStatus(AccountStatusEnum.close.getCode());
						custAccount.setLastUpdatedBy(userName);
						resultCode = this.update(custAccount);
					}else{
						synchronizeLog.setErrMsg(String.format("注销会员交易失败，错误码：%s，描述：%s", deleteTransaction.getRespCode(),SPDBResponseEnum.getTextByCode(deleteTransaction.getRespCode())));
					}
				}else{
					synchronizeLog.setErrMsg(result.getErrMsg());
				}
				if(KuandaoResultEnum.success.getCode().compareTo(resultCode) != 0){
					synchronizeLog.setErrMsg("同步客户信息到款道数据库失败");
					synchronizeLog.setResult(OperateResultEnum.fail.getResult());
				}else{
					synchronizeLog.setResult(OperateResultEnum.success.getResult());
				}
				addSynchronizeLog(synchronizeLog);
			}
		}else{
			resultCode = kuandaoCustAccountList.size();
		}
		return resultCode;
	}

	@Override
	public List<KuandaoAccountDto> queryAccountByCondition(KuandaoAccountDto queryAccountDto) {
		return kuandaoAccountDao.queryAccountByCondition(queryAccountDto);
	}


	@Override
	public String boundNotify(SPDBNotifyRequstParam spdbRequstParam, String membercode, String operType) {
		SPDBResponseResult result = kuanDaoProxyService.boundNotify(spdbRequstParam,membercode,operType);
		String responseStatus = SPDBNotifyResponseStatus.fail.getCode();
		if(result != null && result.isSuccess()){
			BeanUtil<BoundNotifyTransaction> beanUtil = new BeanUtil<>();
			BoundNotifyTransaction boundNotifyTransaction = beanUtil.stringToBean(result.getPlain(), BoundNotifyTransaction.class);
			String memeberCode = boundNotifyTransaction.getSubMerId();
			KuandaoAccountDto kuandaoAccountDto = new KuandaoAccountDto();
			kuandaoAccountDto.setMemeberCode(memeberCode);
			List<KuandaoCustAccount> custAccountList = kuandaoAccountDao.queryKuandaoAccountByCondition(kuandaoAccountDto);
			boolean needUpdate = false;
			if(custAccountList.size() == 1){
				KuandaoCustAccount kuandaoCustAccount = custAccountList.get(0);
				String status = kuandaoCustAccount.getStatus();
				if(AccountStatusEnum.open.getCode().equals(status) && AccountBoundOperate.bound.getCode().equals(boundNotifyTransaction.getOperType())){ //待绑定的才能修改为已绑定
					kuandaoCustAccount.setStatus(AccountStatusEnum.bound.getCode());
					SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
					Date boundTime = new Date();
					String transDateTime = boundNotifyTransaction.getTransDtTm();
					try {
						boundTime = sdf.parse(boundNotifyTransaction.getTransDtTm());
					} catch (ParseException e) {
						logger.error(String.format("parse boundTime[%s] failed", transDateTime), e);
					}
					kuandaoCustAccount.setBoundTime(boundTime);
					needUpdate = true;
					
				}else if(AccountStatusEnum.bound.getCode().equals(status) && AccountBoundOperate.unbound.getCode().equals(boundNotifyTransaction.getOperType())){ //已绑定的改为已开户
					//解绑通知
					kuandaoCustAccount.setStatus(AccountStatusEnum.open.getCode());
					needUpdate = true;
					
				}
				
				if(needUpdate){
					int affectedCount = this.update(kuandaoCustAccount);
					if(affectedCount == 1){
						responseStatus = SPDBNotifyResponseStatus.success.getCode();
					}
				}
			}else{
				logger.error(String.format("query KuandaoAccount by memeberCode[%s] data exception", memeberCode));
			}
			return memeberService.generateResponse(memeberCode,responseStatus);
		}else{
			return memeberService.generateResponse(membercode,responseStatus);
		}
	}

}
