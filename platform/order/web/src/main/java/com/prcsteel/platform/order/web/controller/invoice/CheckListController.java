package com.prcsteel.platform.order.web.controller.invoice;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.prcsteel.platform.acl.model.model.SysSetting;
import com.prcsteel.platform.acl.model.query.SysSettingQuery;
import com.prcsteel.platform.acl.service.SysSettingService;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.utils.PageData;
import com.prcsteel.platform.common.vo.PageResult;
import com.prcsteel.platform.common.vo.Result;
import com.prcsteel.platform.order.model.dto.ChecklistDetailDto;
import com.prcsteel.platform.order.model.model.InvoiceOutCheckList;
import com.prcsteel.platform.order.model.query.ChecklistDetailQuery;
import com.prcsteel.platform.order.service.invoice.InvoiceOutCheckListDetailService;
import com.prcsteel.platform.order.service.invoice.InvoiceOutCheckListService;
import com.prcsteel.platform.order.web.controller.BaseController;
import com.prcsteel.platform.order.web.support.ObjectExcelView;

/**
 * Created by rolyer on 15-9-16.
 */
@Controller
@RequestMapping("/invoice/out/checklist/")
public class CheckListController extends BaseController {

	private static Logger log = Logger.getLogger(CheckListController.class);
	
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Resource
    private InvoiceOutCheckListDetailService invoiceOutCheckListDetailService;

    @Resource
    private InvoiceOutCheckListService invoiceOutCheckListService;
    
    @Resource
    private SysSettingService sysSettingService;

    /**
     * 开票清单页面
     * @param out
     * @param id 当id=0时，代表查询所以清单
     */
    @RequestMapping("{id}/index")
    public String index(ModelMap out, @PathVariable("id") String id){
        if (StringUtils.isNotEmpty(id) && StringUtils.isNumeric(id)) {
            if(id.equals("0")) { //全部清单
                Calendar calendar = Calendar.getInstance();
                Date endTime = calendar.getTime();

                out.put("endTime", simpleDateFormat.format(endTime));

                calendar=Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_MONTH,1);
                Date beginTime = calendar.getTime();

                out.put("beginTime", simpleDateFormat.format(beginTime));
            } else { //指定清单
                out.put("id", Integer.parseInt(id));

                InvoiceOutCheckList checklist = invoiceOutCheckListService.queryById(Long.parseLong(id));
                if (checklist!=null) {
                    out.put("checklist", checklist);
                }
            }
            SysSettingQuery query = new SysSettingQuery();
            query.setType(Constant.TYPE_OF_SPEC_SETTING_TYPE);
            List<SysSetting> specSignList = sysSettingService.selectByParam(query);
            out.put("specSignList", specSignList);
        } else {
            out.put("error", "error");
        }

        return "invoice/out/checklist/index";
    }

    /**
     * 查询清单
     * @param id 清单编号
     * @param beginTime 开始时间
     * @param endTime 结束时间
     * @param buyerName 买家名称
     * @param start 页码
     * @param length 每页记录数
     * @return
     */
    @ResponseBody
    @RequestMapping("search.html")
    public PageResult search(
            @RequestParam(value = "id", required = false) String id,
            @RequestParam(value = "beginTime", required = false) String beginTime,
            @RequestParam(value = "endTime", required = false) String endTime,
            @RequestParam(value = "buyerName", required = false) String buyerName,
            @RequestParam(value = "start", required = false) Integer start,
            @RequestParam(value = "length", required = false) Integer length){


        PageResult result = new PageResult();
        ChecklistDetailQuery query = new ChecklistDetailQuery();

        if (StringUtils.isNotEmpty(id) && StringUtils.isNumeric(id)) {
            query.setId(Long.parseLong(id));
        }
        if (StringUtils.isNotEmpty(buyerName)) {
            query.setBuyerName(buyerName.trim());
        }
        if (StringUtils.isNotEmpty(beginTime)) {
            query.setBeginTime(beginTime);
        }
        if (StringUtils.isNotEmpty(endTime)) {
            query.setEndTime(endTime);
        }

        int total = invoiceOutCheckListDetailService.countByCondition(query);
        result.setRecordsFiltered(total);

        if (total > 0) {
            if (start != null) {
                query.setStart(start);
            }
            if (length != null) {
                query.setLength(length);
            }
            List<ChecklistDetailDto> list = invoiceOutCheckListDetailService.queryByCondition(query);
            result.setData(list);
            result.setRecordsTotal(list.size());
        }

        return result;
    }


    @ResponseBody
    @RequestMapping("export.html")
    public ModelAndView export(
            @RequestParam(value = "id", required = false) String id,
            @RequestParam(value = "beginTime", required = false) String beginTime,
            @RequestParam(value = "endTime", required = false) String endTime,
            @RequestParam(value = "buyerName", required = false) String buyerName){


        ChecklistDetailQuery query = new ChecklistDetailQuery();

        if (StringUtils.isNotEmpty(id) && StringUtils.isNumeric(id)) {
            query.setId(Long.parseLong(id));
        }
        if (StringUtils.isNotEmpty(buyerName)) {
            query.setBuyerName(buyerName);
        }
        if (StringUtils.isNotEmpty(beginTime)) {
            query.setBeginTime(beginTime);
        }
        if (StringUtils.isNotEmpty(endTime)) {
            query.setEndTime(endTime);
        }

        int total = invoiceOutCheckListDetailService.countByCondition(query);

        if (total > 0) {
            query.setStart(0);
            query.setLength(Integer.MAX_VALUE);

            List<ChecklistDetailDto> list = invoiceOutCheckListDetailService.queryByCondition(query);
            Map<String, Object> dataMap = new HashMap<>();
            List<String> titles = new ArrayList<>();
            titles.add("清单编号");
            titles.add("申请时间");
            titles.add("买家全称");
            titles.add("品名");
            titles.add("材质");
            titles.add("规格");
            titles.add("数量（吨）");
            titles.add("价税合计（元）");
            titles.add("服务中心");
            titles.add("发票类型");
            dataMap.put("titles", titles);
            List<PageData> varList = new ArrayList<>();
            for (ChecklistDetailDto item : list) {
                PageData vpd = new PageData();
                vpd.put("var1", item.getId() != null ? item.getId().toString() : "");
                vpd.put("var2", item.getApplyTime()!=null ? simpleDateFormat.format(item.getApplyTime()) : "");
                vpd.put("var3", item.getBuyerName() != null ? item.getBuyerName() : "");
                vpd.put("var4", item.getNsortName() != null ? item.getNsortName() : "");
                vpd.put("var5", item.getMaterial() != null ? item.getMaterial() : "");
                vpd.put("var6", item.getSpec() != null ? item.getSpec() : "");
                vpd.put("var7", item.getWeight() != null ? item.getWeight().toString() : "");
                vpd.put("var8", item.getAmount() != null ? item.getAmount().toString() : "");
                vpd.put("var9", item.getOrgName() != null ? item.getOrgName() : "");
                vpd.put("var10", item.getInvoiceType() != null ? item.getInvoiceType() : "");
                varList.add(vpd);
            }
            dataMap.put("varList", varList);
            ObjectExcelView erv = new ObjectExcelView();
            ModelAndView mv = new ModelAndView(erv, dataMap);
            return mv;
        }

        return null;
    }
    
    /**
     * 根据账户下面的二结欠款更新销项票清单二结欠款
     * @author tuxianming
     * @date 20160627
     */
    @ResponseBody
    @RequestMapping("update/secondsettlement.html")
    public Result updateSecondsettlement(){
        
    	try {
    		invoiceOutCheckListService.updateDebtSecondSettlement();
    		return new Result();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new Result(e.getMessage(), false);
		}
    	
    }
    
    /**
     * 根据更新寄出状态
     * @author tuxianming
     * @date 20160721
     */
    @ResponseBody
    @RequestMapping("update/send.html")
    public Result updateSend(Long[] ids, Boolean[] sends){
        
    	try {
    		invoiceOutCheckListService.updateSend(Arrays.asList(ids), Arrays.asList(sends));
    		return new Result();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new Result(e.getMessage(), false);
		}
    	
    }
    
}
