package com.prcsteel.platform.smartmatch.web.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.prcsteel.platform.common.vo.PageResult;
import com.prcsteel.platform.smartmatch.model.model.AbnormalTradingDetail;
import com.prcsteel.platform.smartmatch.service.AbnormalTradingDetailService;

/**
 * Created by Rolyer on 2015/11/27.
 */
@Controller
@RequestMapping("smartmatch/abnormal/")
public class AbnormalTradingDetailController extends BaseController {
    @Resource
    private AbnormalTradingDetailService abnormalTradingDetailService;

    @RequestMapping("/detail/{id}")
    public String detail(ModelMap out,@PathVariable Long id) {
        out.put("id",id);
        return "smartmatch/abnormal/detail";
    }

    @RequestMapping("search.html")
    public @ResponseBody PageResult search(AbnormalTradingDetail query){
        List<AbnormalTradingDetail> list=abnormalTradingDetailService.queryListByReportResourceInventory(query);
        PageResult result=new PageResult();
        result.setData(list);
        return result;
    }
}
