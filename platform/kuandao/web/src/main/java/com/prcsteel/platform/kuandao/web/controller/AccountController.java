package com.prcsteel.platform.kuandao.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.prcsteel.platform.account.model.enums.AccountTagForSearch;
import com.prcsteel.platform.acl.service.OrganizationService;
import com.prcsteel.platform.kuandao.model.dto.KuandaoAccountDto;
import com.prcsteel.platform.kuandao.model.dto.SynchronizeLogDto;
import com.prcsteel.platform.kuandao.model.enums.KuandaoResultEnum;
import com.prcsteel.platform.kuandao.service.KuandaoAccountService;
import com.prcsteel.platform.kuandao.web.job.SyncKuandaoAccountJob;
import com.prcsteel.platform.kuandao.web.vo.PageResult;
import com.prcsteel.platform.kuandao.web.vo.Result;

@Controller("kuandaoAccountController")
@RequestMapping("/kuandao/account/")
public class AccountController extends BaseController{

	@Resource(name="kuandao.service.AccountService")
	private KuandaoAccountService kuandaoAccountService;
	
	@Resource
	private OrganizationService organizationService;
	
	@Resource
	private SyncKuandaoAccountJob syncKuandaoAccountJob;
	
	@RequestMapping("index")
	public String index(ModelMap out){
		out.put("tab", "index");
		return "/kuandao/account/index";
	}

	@RequestMapping("unopenaccount")
	public String unOpenAccount(ModelMap out){
		out.put("tab", "unopenaccount");
		out.put("accountTags", AccountTagForSearch.getList());
		return "/kuandao/account/unopenaccount";
	}
	
	@RequestMapping("synchronizelog")
	public String synchronizeLog(ModelMap out){
		out.put("tab", "synchronizelog");
		return "/kuandao/account/synchronizelog";
	}
	
	@RequestMapping("queryOpenedAccount.html")
	@ResponseBody
	public PageResult queryOpenedAccount(KuandaoAccountDto accountDto,
			@RequestParam("start") Integer start,
			@RequestParam("length") Integer length){

		int total = kuandaoAccountService.totalOpenedAccount(accountDto);
		List<KuandaoAccountDto> list = kuandaoAccountService.queryOpenedAccount(accountDto,start,length);
		PageResult result = new PageResult();
		result.setData(list);
		result.setRecordsFiltered(total);
		result.setRecordsTotal(list.size());
		
		return result;
	}
	
	
	@RequestMapping("queryUnOpenedAccount.html")
	@ResponseBody
	public PageResult queryUnOpenedAccount(KuandaoAccountDto accountDto,
			@RequestParam("start") Integer start,
			@RequestParam("length") Integer length){
		int total = kuandaoAccountService.totalUnOpenedAccount(accountDto);
		List<KuandaoAccountDto> list = kuandaoAccountService.queryUnOpenedAccount(accountDto,start,length);
		PageResult result = new PageResult();
		result.setData(list);
		result.setRecordsFiltered(total);
		result.setRecordsTotal(list.size());
		
		return result;
	}
	
	@RequestMapping("querySynchronizeLog.html")
	@ResponseBody
	public PageResult querySynchronizeLog(SynchronizeLogDto synLogDto,
			@RequestParam("start") Integer start,
			@RequestParam("length") Integer length){
		int total = kuandaoAccountService.totalSynchronizeLog(synLogDto);
		List<SynchronizeLogDto> list = kuandaoAccountService.querySynchronizeLog(synLogDto,start,length);
		PageResult result = new PageResult();
		result.setData(list);
		result.setRecordsFiltered(total);
		result.setRecordsTotal(list.size());
		
		return result;
	}
	
	@RequestMapping("queryAllBusinessOrg.html")
	@ResponseBody
	public Result queryAllBusinessOrg(){
		Result result = new Result();
		result.setData(organizationService.queryAllBusinessOrg());
		return result;
	}
	
	@RequestMapping("modifyCustAccount.html")
	@ResponseBody
	public Result modifyCustAccount(@RequestParam("acctId") Long acctId,@RequestParam("memeberName") String memeberName,
			@RequestParam("idNo") String idNo, @RequestParam("mobile") String mobile){
		
		Result result = new Result();

		String userName = getLoginUser().getName();
		
		KuandaoAccountDto kuandaoAccountDto = new KuandaoAccountDto();
		kuandaoAccountDto.setAcctId(acctId);
		kuandaoAccountDto.setMemeberName(memeberName);
		kuandaoAccountDto.setIdNo(idNo);
		kuandaoAccountDto.setMobile(mobile);
		kuandaoAccountDto.setCreatedBy(userName);
		
		Integer count = kuandaoAccountService.modifyCustAccount(kuandaoAccountDto);
		
		if(KuandaoResultEnum.success.getCode().compareTo(count) == 0){
			result.setSuccess(true);
			result.setData("修改客户信息成功");
		}else if(KuandaoResultEnum.businesserror.getCode().compareTo(count) == 0){
			result.setSuccess(false);
			result.setData("客户名已存在");
		}else if(KuandaoResultEnum.notnull.getCode().compareTo(count) == 0){
			result.setSuccess(false);
			result.setData("客户组织机构代码不能为空");
		}else if(KuandaoResultEnum.readonly.getCode().compareTo(count) == 0){
			result.setSuccess(false);
			result.setData("客户已锁定不能修改信息");
		}else if(KuandaoResultEnum.nodata.getCode().compareTo(count) == 0){
			result.setSuccess(false);
			result.setData("客户不存在");
		}else if(KuandaoResultEnum.timeout.getCode().compareTo(count) == 0){
			result.setSuccess(false);
			result.setData("查询客户信息超时，请联系管理员");
		}else if(KuandaoResultEnum.systemerror.getCode().compareTo(count) == 0){
			result.setSuccess(false);
			result.setData("系统异常，请联系管理员");
		}else{
			result.setSuccess(false);
			result.setData("数据异常，请联系管理员");
		}
		return result;
	}
	
	@RequestMapping("openAccount.html")
	@ResponseBody
	public Result openAccount(@RequestParam("acctId") Long acctId){
		
		Result result = new Result();

		String userName = getLoginUser().getName();
		
		int resultCode = kuandaoAccountService.openAccount(acctId,userName);
		if(KuandaoResultEnum.success.getCode().compareTo(resultCode) == 0){
			result.setSuccess(true);
		}else if(KuandaoResultEnum.notnull.getCode().compareTo(resultCode) == 0){
			result.setSuccess(false);
			result.setData("请完善客户信息后再开户");
		}else if(KuandaoResultEnum.systemerror.getCode().compareTo(resultCode) == 0 ){
			result.setSuccess(false);
			result.setData("开户失败，请查看同步日志");
		}else if(KuandaoResultEnum.nodata.getCode().compareTo(resultCode) == 0){
			result.setSuccess(false);
			result.setData("客户数据异常，请核实后再开户");
		}
		return result;
	}
	
	
	@RequestMapping("synchronizeAccount.html")
	@ResponseBody
	public Result synchronizeAccount(@RequestParam("acctId") Long acctId){
		Result result = new Result();

		String userName = getLoginUser().getName();
		
		int resultCode = kuandaoAccountService.synchronizeAccount(acctId,userName);
		if(KuandaoResultEnum.success.getCode().compareTo(resultCode) == 0){
			result.setSuccess(true);
		}else if(KuandaoResultEnum.notnull.getCode().compareTo(resultCode) == 0){
			result.setSuccess(false);
			result.setData("请完善客户信息后再同步");
		}else if(KuandaoResultEnum.systemerror.getCode().compareTo(resultCode) == 0){
			result.setSuccess(false);
			result.setData("客户信息同步到浦发失败，请查看同步日志");
		}else if(KuandaoResultEnum.dataoperateerror.getCode().compareTo(resultCode) == 0 ){
			result.setSuccess(false);
			result.setData("客户信息同步到本地失败，请查看同步日志");
		}else if(KuandaoResultEnum.nodata.getCode().compareTo(resultCode) == 0){
			result.setSuccess(false);
			result.setData("客户数据异常，请核实后再同步");
		}
		return result;
	}
	
	@RequestMapping("synchronizeAllToLocal.html")
	@ResponseBody
	public Result synchronizeAllToLocal(){
		
		Result result = new Result();
		String userName = getLoginUser().getName();
		int resultCode = kuandaoAccountService.synchronizeAllToLocal(userName);
		
		if(KuandaoResultEnum.success.getCode().compareTo(resultCode) == 0){
			result.setSuccess(true);
		}else{
			result.setSuccess(false);
			result.setData("查询客户信息失败");
		}
		return result;
	}
	
	@RequestMapping("synchronizeAllToSpdb.html")
	@ResponseBody
	public Result synchronizeAllToSpdb(){
		
		Result result = new Result();
		String userName = getLoginUser().getName();
		int resultCode = kuandaoAccountService.synchronizeAllToSpdb(userName);
		
		if(KuandaoResultEnum.success.getCode().compareTo(resultCode) == 0){
			result.setSuccess(true);
		}else{
			result.setSuccess(false);
			result.setData("同步客户信息失败");
		}
		return result;
	}
	
	
	@RequestMapping("batchOpenAccount.html")
	@ResponseBody
	public Result batchOpenAccount(@RequestParam("acctId") String acctId){
		
		Result result = new Result();
		String userName = getLoginUser().getName();
		
		String[] idArr = acctId.split(",");
		List<Long> idList = new ArrayList<>(idArr.length);
		for(String sId : idArr){
			idList.add(Long.parseLong(sId));
		}
		
		int resultCode = kuandaoAccountService.batchOpenAccount(userName,idList);
		
		if(KuandaoResultEnum.success.getCode().compareTo(resultCode) == 0){
			result.setSuccess(true);
		}else{
			result.setSuccess(false);
			result.setData("批量开户失败");
		}
		return result;
	}
	
	@RequestMapping("closeAccount.html")
	@ResponseBody
	public Result closeAccount(@RequestParam("acctId") Long acctId){
		
		Result result = new Result();
		String userName = getLoginUser().getName();
		
		int resultCode = kuandaoAccountService.closeAccount(userName,acctId);
		
		if(KuandaoResultEnum.success.getCode().compareTo(resultCode) == 0){
			result.setSuccess(true);
		}else if(KuandaoResultEnum.readonly.getCode().compareTo(resultCode) == 0){
			result.setSuccess(false);
			result.setData("已经删除的客户不能再次删除");
		}else if(KuandaoResultEnum.systemerror.getCode().compareTo(resultCode) == 0){
			result.setSuccess(false);
			result.setData("删除客户失败，请查看同步日志");
		}else if(KuandaoResultEnum.nodata.getCode().compareTo(resultCode) == 0){
			result.setSuccess(false);
			result.setData("删除客户失：客户不存在");
		}else{
			result.setSuccess(false);
			result.setData("删除客户失败：存在客户多个客户");
		}
		return result;
	}
	
	@RequestMapping("syncKuandaoAccountJob.html")
	@ResponseBody
	public Result syncKuandaoAccountJob(){
		Result result = new Result();
		syncKuandaoAccountJob.execute();
		return result;
	}
	
	
}
