package com.prcsteel.platform.account.web.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.prcsteel.platform.account.model.model.AccountContractTemplate;
import com.prcsteel.platform.account.service.AccountContractTemplateService;
import com.prcsteel.platform.common.vo.Result;

import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import java.util.List;

/**
 * Created by Rolyer on 2016/1/26.
 */
@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api/template")
public class RestTemplateController {

    @Resource
    private AccountContractTemplateService accountContractTemplateService;

    @RequestMapping(value = "/sys/list.html", method = RequestMethod.GET)
    public List<AccountContractTemplate> list(){
        return accountContractTemplateService.querySysTemplate();
    }

    @RequestMapping(value = "/save.html", method = RequestMethod.POST)
    public Result save(@RequestBody String p){
        Gson gson = new Gson();
        AccountContractTemplate act = gson.fromJson(p, AccountContractTemplate.class);

        Result result = new Result();
        result.setSuccess(accountContractTemplateService.saveContractTemplate(act));
        return result;
    }

    @RequestMapping(value = "/query/{id}.html", method = RequestMethod.GET)
    public Result query(@PathVariable Long id){
        Result result = new Result();
        result.setData(accountContractTemplateService.queryAccountContractTemplate(id));
        return result;
    }

    @RequestMapping(value = "/delete/{id}.html", method = RequestMethod.GET)
    public Result delete(@PathVariable Long id){
        Result result = new Result();
        result.setSuccess(accountContractTemplateService.deleteTemplateById(id));
        return result;
    }

    @RequestMapping(value = "/enabled/{enabled}/{id}.html", method = RequestMethod.POST)
    public Result enabled(@PathVariable ("id") Long id,@PathVariable ("enabled")  Boolean enabled){
        Result result = new Result();
        result.setSuccess(accountContractTemplateService.enabledContractTemplate(id, enabled));
        return result;
    }

    @RequestMapping(value = "/resolve/{accountId}/{type}/{id}.html", method = RequestMethod.GET)
    public Result resolve(@PathVariable Long accountId, @PathVariable Long id, @PathVariable String type){
        Result result = new Result();
        result.setData(accountContractTemplateService.resolveTemplate(id, type, accountId));
        return result;
    }
    
    @RequestMapping(value = "/release/{type}/{id}.html", method = RequestMethod.POST)
    public Result releaseTemplate(@PathVariable ("id") Long id, @PathVariable String type){
        Result result = new Result();
        result.setSuccess(accountContractTemplateService.releaseTemplate(id, type));
        return result;
    }

    @RequestMapping(value = "/doNotRelease/{id}.html", method = RequestMethod.POST)
    public Result doNotReleaseTemplate(@PathVariable ("id") Long id){
        Result result = new Result();
        result.setSuccess(accountContractTemplateService.doNotReleaseTemplate(id));
        return result;
    }
}
