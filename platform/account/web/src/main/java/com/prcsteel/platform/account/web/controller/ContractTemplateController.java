package com.prcsteel.platform.account.web.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.prcsteel.platform.account.model.dto.AccountDto;
import com.prcsteel.platform.account.model.model.AccountContractTemplate;
import com.prcsteel.platform.account.model.model.AccountContractTemplateOprt;
import com.prcsteel.platform.account.service.AccountContractTemplateService;
import com.prcsteel.platform.account.service.ContactService;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.common.vo.Result;

/** 
 * 合同模板controller
 * 
 * @author wyx <p>2016年3月16日</p>  
 */
@Controller
@RequestMapping("/contracttemplate/")
public class ContractTemplateController extends BaseController{
	@Resource
	private ContactService contactService;
	@Resource
	private AccountContractTemplateService accountContractTemplateService;
	
	private static final Logger logger = LoggerFactory.getLogger(ContractTemplateController.class);
	/**
     * 企业基本信息-合同模板
     *
     * @param out
     * @param accountId
     * @return
     */
    @RequestMapping("{accountId}/list")
    public String accountInfo(ModelMap out, @PathVariable("accountId") Long accountId) {
    	  AccountDto accountdto = contactService.getCompanyById(accountId);
          out.put("accountdto", accountdto);
		  return "account/contracttemplate/list";
    }
    
    /**
	 * 合同模板列表查询
	 * @param accountInfoFlowSearchQuery
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "search", method = RequestMethod.POST)
	public Result search(Long accountId){
		Result result = new Result();
        try {
        	Map<String, List<AccountContractTemplate>> accountContractTemplates = accountContractTemplateService.selectAllContractTemplateById(accountId);
        	result.setData(accountContractTemplates);
        } catch (BusinessException e) {
            result.setSuccess(false);
            result.setData(e.getMsg());
        }
        return result;
	}
	
	/**
	 * 设置默认合同模板
	 * @param contractTemplateId
	 * @param contractTemplateType
	 * @param accountId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "setdefault", method = RequestMethod.POST)
	public Result setDefault(Long contractTemplateId, String contractTemplateType,Long accountId){
		Result result = new Result();
		try {
        	accountContractTemplateService.setDefaultContractTemplate(contractTemplateId, contractTemplateType, accountId);
        } catch (BusinessException e) {
            result.setSuccess(false);
            result.setData(e.getMsg());
        }
		return result;
	}
	
	/**
	 * 处理模版:添加，编辑，查看
	 * @param out
	 * @param param  参数内容
	 * @return
	 */
	@RequestMapping("dotemplate")
	public String doTemplateOprt(ModelMap out,String param){
		try{
			AccountContractTemplateOprt actOprt=decodeUrlParam(param);
			out.put("actOprt", actOprt);
			//合同编号
			out.put("templateNo", "20 160323 00001");
	        out.put("detail",accountContractTemplateService.doTemplateOprt(actOprt));
		}catch(BusinessException e){
			logger.debug("模板处理错误",e);
		} 
		return "account/contracttemplate/template_detail";
	}
	
	/**
	 * 解码url参数
	 * @param param  url地址
	 * @return 账户合同模板处理参数对象
	 */
	private AccountContractTemplateOprt decodeUrlParam (String param){
		AccountContractTemplateOprt actOprt=null;
		if(!StringUtils.isBlank(param)){
			try {
				//解码
				String content=URLDecoder.decode(param, "utf-8");
				//构建json
				if(content.indexOf("=")>=0){
					content=content.replaceAll("=", "\":\"");
				}
				if(content.indexOf("&")>=0){
					content=content.replaceAll("&", "\",\"");
				}
				content="{\""+content+"\"}";
				
				actOprt=new Gson().fromJson(content, AccountContractTemplateOprt.class);
			} catch (UnsupportedEncodingException e) {
				logger.debug("url解码处理错误",e);
			} 
		}
		return actOprt;
	}
	
	/**
	 * 保存合同模板对象
	 * @param p
	 * @return
	 */
	@ResponseBody
	@RequestMapping("save.html")
    public Result save(AccountContractTemplate act){
        Result result = new Result();
        String loginId=this.getLoginUser().getLoginId();
        act.setCreatedBy(loginId);
        act.setLastUpdatedBy(loginId);
        result.setSuccess(accountContractTemplateService.saveContractTemplate(act));
        return result;
    }
	
	/**
	 * 删除合同模板
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/del.html")
    public Result deleteTemplate(Long id){
        return new Result(null,accountContractTemplateService.deleteTemplateById(id));
    }
}
