package com.prcsteel.platform.order.web.controller.invoice;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.prcsteel.platform.acl.service.OrganizationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.prcsteel.platform.acl.model.model.Organization;
import com.prcsteel.platform.order.web.controller.BaseController;
import com.prcsteel.platform.common.utils.PageData;
import com.prcsteel.platform.order.model.dto.InvoiceOutApplyDto;
import com.prcsteel.platform.order.model.dto.InvoiceOutCheckListDto;
import com.prcsteel.platform.order.model.dto.InvoiceOutItemDetailDto;
import com.prcsteel.platform.order.model.enums.InvoiceOutApplyStatus;
import com.prcsteel.platform.order.service.invoice.InvoiceOutCheckListService;
import com.prcsteel.platform.order.service.invoice.OutApplyMainService;
import com.prcsteel.platform.order.web.support.ObjectExcelView;
import com.prcsteel.platform.common.vo.PageResult;
import com.prcsteel.platform.common.vo.Result;

/**
 * 销项票待开票 Created by lixiang on 2015/8/3.
 */
@Controller
@RequestMapping("/invoice/out/")
public class WaitingInvoiceController extends BaseController {

	@Resource
	private OutApplyMainService outApplyMainService;

	@Resource
	InvoiceOutCheckListService invoiceOutCheckListService;
	@Resource
	private OrganizationService organizationService;
	/**
	 * 销项待开票待生成的开票清单页面
	 */
	@RequestMapping("waitinginvoice.html")
	public void waitinginvoice(ModelMap out) {
		List<Organization> orgs = organizationService.queryAllBusinessOrg();
    	out.put("orgs", orgs);
	}

	/**
	 * 销项待开票已生成的开票清单页面
	 */
	@RequestMapping("generatedinvoice.html")
	public void generatedinvoice(ModelMap out) {
		List<Organization> orgs = organizationService.queryAllBusinessOrg();
    	out.put("orgs", orgs);
	}

	/**
	 * 查询销项待开票待生成的开票清单
	 * @param status
	 * @param start
	 * @param length
	 * @return
	 */
	@RequestMapping("querywaitinginvoice.html")
	public @ResponseBody PageResult querywaitinginvoice(
			@RequestParam("status") String status,
			String orgIds,
			@RequestParam("start") Integer start,
			@RequestParam("length") Integer length) {
		List<String> statusList = new ArrayList<String>();
		statusList.add(InvoiceOutApplyStatus.APPROVED.getValue());
	    statusList.add(InvoiceOutApplyStatus.PARTIAL_INVOICED.getValue());
	    
	    List<String> orgIdArr = null;
	    if(org.apache.commons.lang.StringUtils.isNotBlank(orgIds)){
	    	orgIdArr = Arrays.asList(orgIds.split(","));
	    }
	    
		List<InvoiceOutApplyDto> list = outApplyMainService.queryWaitInvoice(orgIdArr, statusList, start, length);
//             for (InvoiceOutApplyDto invoiceOutApplyDto:list){
//				 if(invoiceOutApplyDto.getActualAmount().intValue()==0){
//					 invoiceOutApplyDto=null;
//				 }
//			 }
		int total = outApplyMainService.totalWaitInvoice(orgIdArr, statusList);
		PageResult result = new PageResult();
		result.setData(list);
		result.setRecordsFiltered(total);
		result.setRecordsTotal(list.size());
		return result;
	}

	/**
	 * 查询待开票列表的已生成开票清单
	 * @param status
	 * @param start
	 * @param length
	 * @return
	 */
	@RequestMapping("querygeneratedinvoice.html")
	public @ResponseBody PageResult querygeneratedinvoice(
			@RequestParam("status") String status,
			String orgIds,
			@RequestParam("start") Integer start,
			@RequestParam("length") Integer length) {
		
		List<String> orgIdArr = null;
	    if(org.apache.commons.lang.StringUtils.isNotBlank(orgIds)){
	    	orgIdArr = Arrays.asList(orgIds.split(","));
	    }
		
		List<InvoiceOutCheckListDto> list = invoiceOutCheckListService.queryOutChecklistByStatus(orgIdArr, status, start, length);
		for(InvoiceOutCheckListDto invoiceOutCheckListDto: list){
			invoiceOutCheckListDto.getAmount();
		}
		int total = invoiceOutCheckListService.totalOutChecklistByStatus(orgIdArr, status);
		PageResult result = new PageResult();
		result.setData(list);
		result.setRecordsFiltered(total);
		result.setRecordsTotal(list.size());
		return result;
	}

	/**
	 * 导出全部EXCEL
	 * @param dateStart
	 * @param dateEnd
	 * @return
	 */
	@RequestMapping("excel.html")
	public ModelAndView exportExcel(
			@RequestParam(value = "dateStart", required = false) String dateStart,
			@RequestParam(value = "dateEnd", required = false) String dateEnd) {
		ModelAndView mv = this.getModelAndView();
		Map<String, Object> dataMap = new HashMap<String, Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("编号");
		titles.add("品名");
		titles.add("规格");
		titles.add("材质");
		titles.add("数量(吨)");
		titles.add("单价(元)");
		titles.add("金额(元)");
		titles.add("税额(元)");
		titles.add("价税合计(元)");
		titles.add("服务中心");
		titles.add("提交时间");
		dataMap.put("titles", titles);
		Date dateStartStr = null;
		Date dateEndStr = null;
		if (dateStart != null && dateEnd != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

			try {
				dateStartStr = sdf.parse(dateStart);
				dateEndStr = new Date(
						sdf.parse(dateEnd).getTime() + 24 * 3600 * 1000);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		DecimalFormat decimalFormat = new DecimalFormat(".00");// 构造方法的字符格式这里如果小数不足2位,会以0补足.
		DecimalFormat decimalFormats = new DecimalFormat(".0000");// 构造方法的字符格式这里如果小数不足4位,会以0补足.
		List<InvoiceOutItemDetailDto> list = outApplyMainService.findAllDetail(
				dateStartStr, dateEndStr);
		List<PageData> varList = new ArrayList<PageData>();
		for (int i = 0; i < list.size(); i++) {
			PageData vpd = new PageData();
			vpd.put("var1", list.get(i).getCode());// 编号
			vpd.put("var2", list.get(i).getNsortName());// 品名
			vpd.put("var3", list.get(i).getSpec());// 规格
			vpd.put("var4", list.get(i).getMaterial());// 材质
			vpd.put("var5", decimalFormats.format(list.get(i).getWeight()));// 重量
			vpd.put("var6", decimalFormat.format(list.get(i).getPrice()));// 单价
			vpd.put("var7", decimalFormat.format(list.get(i).getNoTaxAmount()));// 金额
			vpd.put("var8", decimalFormat.format(list.get(i).getTaxAmount()));// 税额
			vpd.put("var9", decimalFormat.format(list.get(i).getAmount()));// 价税合计
			vpd.put("var10", list.get(i).getOrgName());// 服务中心
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日  HH:MM:SS");
			Date created = list.get(i).getCreated();
			String dateStarts = sdf.format(created);
			vpd.put("var11", dateStarts);// 提交时间
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView(); // 执行excel操作
		mv = new ModelAndView(erv, dataMap);
		return mv;
	}

	/**
	 * 根据outMainId查询买家名称
	 * @param out
	 * @param id
	 */
	@RequestMapping("waitinginvoicedetail.html")
	public void waitinginvoicedetail(ModelMap out, @RequestParam("id") String id) {
		Long outMainId = Long.parseLong(id);
		List<InvoiceOutItemDetailDto> list = outApplyMainService
				.findByOutMainId(outMainId, null, null, null, null, null, null);
		String buyerName = null;
		for (int i = 0; i < list.size();) {
			buyerName = list.get(i).getBuyerName();
			break;
		}
		int counts = outApplyMainService.findByOutMainIdCount(outMainId, null,
				null, null, null);
		out.put("buyerName", buyerName);
		out.put("id", id);
		out.put("counts", counts);
	}

	/**
	 * 根据ID查询当前买家的开票计算结果
	 * @param id 主键ID
	 * @param start 起始页
	 * @param length 记录数
	 * @param dateStart 起始日期
	 * @param dateEnd 终止日期
	 * @return
	 */
	@RequestMapping("findinvoicedetail.html")
	public @ResponseBody PageResult findinvoicedetail(
			@RequestParam("id") String id,
			@RequestParam("start") Integer start,
			@RequestParam("length") Integer length,
			@RequestParam("dateStart") String dateStart,
			@RequestParam("dateEnd") String dateEnd) {
		Long outMainId = Long.parseLong(id);
		PageResult result = new PageResult();
		Date dateStartStr = null;
		Date dateEndStr = null;
		if (!dateStart.isEmpty() && !dateEnd.isEmpty()) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

			try {
				dateStartStr = sdf.parse(dateStart);
				dateEndStr = new Date(
						sdf.parse(dateEnd).getTime() + 24 * 3600 * 1000);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		List<InvoiceOutItemDetailDto> list = outApplyMainService
				.findByOutMainId(outMainId, null, null, start, length,
						dateStartStr, dateEndStr);
		int counts = outApplyMainService.findByOutMainIdCount(outMainId, null,
				null, dateStartStr, dateEndStr);
		result.setRecordsFiltered(counts);
		result.setRecordsTotal(counts);
		result.setData(list);
		result.setRecordsTotal(list.size());
		return result;
	}

	/**
	 * 按日期导出excel之前先查询有没有数据
	 * @param id
	 * @param dateStart
	 * @param dateEnd
	 * @return
	 */
	@RequestMapping("getbylist.html")
	public @ResponseBody Result getlist(
			@RequestParam("id") String id,
			@RequestParam(value = "dateStart", required = false) String dateStart,
			@RequestParam(value = "dateEnd", required = false) String dateEnd) {
		Result result = new Result();
		Boolean success = false;
		Long outMainId = Long.parseLong(id);
		Date dateStartStr = null;
		Date dateEndStr = null;
		if (dateStart != null && dateEnd != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			try {
				dateStartStr = sdf.parse(dateStart);
				dateEndStr = new Date(
						sdf.parse(dateEnd).getTime() + 24 * 3600 * 1000);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		List<InvoiceOutItemDetailDto> list = outApplyMainService
				.findByOutMainId(outMainId, null, null, null, null,
						dateStartStr, dateEndStr);
		if (list.size() > 0)
			success = true;
		else
			success = false;
		result.setSuccess(success);
		return result;
	}

	/**
	 * 根据买家导出EXCEL
	 * @param dateStart
	 * @param dateEnd
	 * @return
	 */
	@RequestMapping("excelbyid.html")
	public ModelAndView exportExcelById(
			@RequestParam("id") String id,
			@RequestParam(value = "dateStart", required = false) String dateStart,
			@RequestParam(value = "dateEnd", required = false) String dateEnd) {
		Long outMainId = Long.parseLong(id);
		ModelAndView mv = this.getModelAndView();

		Map<String, Object> dataMap = new HashMap<String, Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("编号");
		titles.add("品名");
		titles.add("规格");
		titles.add("材质");
		titles.add("数量(吨)");
		titles.add("单价(元)");
		titles.add("金额(元)");
		titles.add("税额(元)");
		titles.add("价税合计(元)");
		titles.add("服务中心");
		titles.add("提交时间");
		dataMap.put("titles", titles);
		Date dateStartStr = null;
		Date dateEndStr = null;
		if (dateStart != null && dateEnd != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

			try {
				dateStartStr = sdf.parse(dateStart);
				dateEndStr = new Date(
						sdf.parse(dateEnd).getTime() + 24 * 3600 * 1000);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		DecimalFormat decimalFormat = new DecimalFormat(".00");// 构造方法的字符格式这里如果小数不足2位,会以0补足.
		DecimalFormat decimalFormats = new DecimalFormat(".0000");// 构造方法的字符格式这里如果小数不足4位,会以0补足.
		List<InvoiceOutItemDetailDto> list = outApplyMainService
				.findByOutMainId(outMainId, null, null, null, null,
						dateStartStr, dateEndStr);
		List<PageData> varList = new ArrayList<PageData>();
		for (int i = 0; i < list.size(); i++) {
			PageData vpd = new PageData();
			vpd.put("var1", list.get(i).getCode());// 编号
			vpd.put("var2", list.get(i).getNsortName());// 品名
			vpd.put("var3", list.get(i).getSpec());// 规格
			vpd.put("var4", list.get(i).getMaterial());// 材质
			vpd.put("var5", decimalFormats.format(list.get(i).getWeight()));// 重量
			vpd.put("var6", decimalFormat.format(list.get(i).getPrice()));// 单价
			vpd.put("var7", decimalFormat.format(list.get(i).getNoTaxAmount()));// 金额
			vpd.put("var8", decimalFormat.format(list.get(i).getTaxAmount()));// 税额
			vpd.put("var9", decimalFormat.format(list.get(i).getAmount()));// 价税合计
			vpd.put("var10", list.get(i).getOrgName());// 服务中心
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日  HH:MM:SS");
			Date created = list.get(i).getCreated();
			String dateStarts = sdf.format(created);
			vpd.put("var11", dateStarts);// 提交时间
			varList.add(vpd);
			Long s = list.get(i).getInvoiceOutMainId();
//			System.out.println(s);
//			outApplyMainService
//					.updateForDate(list.get(i).getInvoiceOutMainId());
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView(); // 执行excel操作
		mv = new ModelAndView(erv, dataMap);
		return mv;
	}

}
