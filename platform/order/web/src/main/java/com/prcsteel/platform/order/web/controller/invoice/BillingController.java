package com.prcsteel.platform.order.web.controller.invoice;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.DateFormat;
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
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.prcsteel.platform.acl.model.model.SysSetting;
import com.prcsteel.platform.common.constants.Constant.InvoiceOutSendStatus;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.acl.service.SysSettingService;
import com.prcsteel.platform.common.constants.Constant.InvoiceOutSendStatus;
import com.prcsteel.platform.common.enums.OpType;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.common.service.OpLog;
import com.prcsteel.platform.common.service.OpParam;
import com.prcsteel.platform.common.utils.PageData;
import com.prcsteel.platform.common.vo.PageResult;
import com.prcsteel.platform.common.vo.Result;
import com.prcsteel.platform.order.model.dto.InvoiceInDto;
import com.prcsteel.platform.order.model.dto.InvoiceOutApplyItemDetailDto;
import com.prcsteel.platform.order.model.enums.InvoiceInDetailStatus;
import com.prcsteel.platform.order.model.enums.InvoiceInIsDefer;
import com.prcsteel.platform.order.model.enums.InvoiceInStatus;
import com.prcsteel.platform.order.model.enums.InvoiceOutIsDefer;
import com.prcsteel.platform.order.model.model.InvoiceOutItemDetail;
import com.prcsteel.platform.order.model.query.InvOutApplyItemDetailQuery;
import com.prcsteel.platform.order.model.query.InvoiceInQuery;
import com.prcsteel.platform.order.model.query.InvoiceOutApplyQuery;
import com.prcsteel.platform.order.service.SysConfigService;
import com.prcsteel.platform.order.service.invoice.InvoiceInDetailService;
import com.prcsteel.platform.order.service.invoice.InvoiceInService;
import com.prcsteel.platform.order.service.invoice.InvoiceOutApplyService;
import com.prcsteel.platform.order.service.invoice.InvoiceOutCheckListService;
import com.prcsteel.platform.order.service.invoice.InvoiceOutItemDetailService;
import com.prcsteel.platform.order.web.controller.BaseController;

import com.prcsteel.platform.order.web.support.ObjectExcelView;
import com.prcsteel.platform.order.web.support.ShiroVelocity;

/**
 * 开票...
 * Created by lcw363 on 2015/9/14.
 */
@Controller
@RequestMapping("/invoice/out/")
public class BillingController extends BaseController {

    private static Logger logger = LogManager.getLogger(InvoiceOutController.class);

    @Resource
    SysSettingService sysSettingService;
    @Resource
    InvoiceInService invoiceInService;
    @Resource
    InvoiceInDetailService invoiceInDetailService;
    @Resource
    InvoiceOutItemDetailService outItemDetailService;
    @Resource
    InvoiceOutApplyService invoiceOutApplyService;
    @Resource
    InvoiceOutCheckListService outCheckListService;
    @Resource
    SysConfigService sysConfigService;

    ShiroVelocity permissionLimit = new ShiroVelocity();

    private static final String PERMISSION_INVOICEI_OUT_GENERATE = "invoice:out:waitinginvoice:generate";  // 生成开票清单

    private static final int DEFAULT_AMOUNT_SCALE = 2; //默认金额保留小数点位数
    private static final int DEFAULT_TAX_SCALE = 4; //默认税率保留小数点位数

    private static final String EXPECTED_TAX_RATE = "invoice_expect_tax_rate";      // 预期税负
    private static final String EXPECTED_TAX_AMOUNT = "invoice_expect_tax_amount";  // 预期税额
    private static final String TAX_RATE = "invoice_tax_rate";      				// 税率

    /**
     * 开票
     *
     * @param out 输出
     * @param ids 待开票清单ID
     */
    @RequestMapping("generate.html")
    public void generate(ModelMap out, @RequestParam("ids") String ids) {
        if (!ids.isEmpty()) {
            out.put("applyIds", ids);
            List<Long> idList = getApplyIds(ids);
            
            getData(out, idList);

            Integer start = 0;
            Integer length = 1000;
            InvoiceInQuery inQuery = new InvoiceInQuery();

            // 已确认未认证的进项票
            inQuery.setApplyIds(idList);
            inQuery.setStatus(Arrays.asList(InvoiceInStatus.WAIT));
            inQuery.setRelationStatus(InvoiceInDetailStatus.HasRelation.getCode());
            inQuery.setIsDefer(InvoiceInIsDefer.NO.getCode());
            inQuery.setStart(start);
            inQuery.setLength(length);
            List<InvoiceInDto> list = invoiceInService.queryByInvOutApplyIds(inQuery);
            out.put("invoiceIns", list);

            // 已选择暂缓认证的发票
            inQuery = new InvoiceInQuery();
            inQuery.setApplyIds(idList);
            inQuery.setIsDefer(InvoiceInIsDefer.YES.getCode());
            inQuery.setStatus(Arrays.asList(InvoiceInStatus.WAIT));
            inQuery.setStart(start);
            inQuery.setLength(length);
            List<InvoiceInDto> suspendList = invoiceInService.queryByInvOutApplyIds(inQuery);
            out.put("suspendInvoiceIns", suspendList);

            // 已选择不开的销项票
            InvOutApplyItemDetailQuery itemDetailQuery = new InvOutApplyItemDetailQuery();
            itemDetailQuery.setApplyIds(idList);
            itemDetailQuery.setIsDefer(InvoiceOutIsDefer.YES.getCode());
            List<InvoiceOutApplyItemDetailDto> notOutItemDetails = outItemDetailService.query(itemDetailQuery);
            out.put("notOutItemDetails", notOutItemDetails);

            // 查询本次开票清单相关的销项票修改次数
            List<InvoiceOutItemDetail> modifyNumList = outItemDetailService.queryModifyNumByApplyIds(idList);
            out.put("modifyNumList", modifyNumList);
            
            SysSetting invoiceOutApplySecondSetting = sysSettingService.queryBySettingType("InvoiceOutApplySecond");
            if(StringUtils.isNotBlank(invoiceOutApplySecondSetting.getSettingValue())){
            	out.put("InvoiceOutApplySecond", Double.parseDouble(invoiceOutApplySecondSetting.getSettingValue()));
            }else{
            	out.put("InvoiceOutApplySecond", 0);
            }
            
            
        }
    }

    /**
     * 已申请开销项票相应未确认的进项票
     */
    @RequestMapping("tobeconfirmedinvin.html")
    public void tobeConfirmedInvIn() {
    }

    /**
     * 已申请开销项票相应未确认的进项票 数据
     *
     * @param start  开始
     * @param length 长度
     * @param ids    申请ID集合
     * @return 未确认的进项票数据
     */
    @ResponseBody
    @RequestMapping(value = "invoiceindata.html", method = RequestMethod.POST)
    public PageResult invoiceInData(@RequestParam("start") Integer start,
                                    @RequestParam("length") Integer length,
                                    @RequestParam("ids") String ids) {
        PageResult result = new PageResult();
        if (!ids.isEmpty()) {
            InvoiceInQuery inQuery = new InvoiceInQuery();
            inQuery.setApplyIds(getApplyIds(ids));
            inQuery.setStatus(Arrays.asList(InvoiceInStatus.SENT));
            inQuery.setStart(start);
            inQuery.setLength(length);
            List<InvoiceInDto> list = invoiceInService.queryByInvOutApplyIds(inQuery);
            Integer total = invoiceInService.queryByInvOutApplyIdsTotal(inQuery);

            result.setData(list);
            result.setRecordsFiltered(total);
            result.setRecordsTotal(list.size());
        }
        return result;
    }

    /**
     * 获取相关数据
     *
     * @param out    输出
     * @param idList 待开票清单ID
     */
    private void getData(ModelMap out, List<Long> idList) {
        // 开始时间、结束时间(当前月第一天、当前时间)
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        InvoiceInQuery inQuery = new InvoiceInQuery();
        inQuery.setRelationStatus(InvoiceInDetailStatus.HasRelation.getCode());

        String startTime = format.format(cal.getTime());
        String endTime = format.format(new Date());
        // 预期税负
        BigDecimal expectedTaxRate = new BigDecimal(sysConfigService.getDouble(EXPECTED_TAX_RATE)).setScale(DEFAULT_TAX_SCALE,BigDecimal.ROUND_HALF_UP);
        // 预期税额
        BigDecimal taxAmount = new BigDecimal(sysConfigService.getDouble(EXPECTED_TAX_AMOUNT)).setScale(DEFAULT_AMOUNT_SCALE, BigDecimal.ROUND_HALF_UP);
        out.put("taxRate", expectedTaxRate.doubleValue());
        out.put("taxAmount", taxAmount);
        
        // 税率
        BigDecimal taxRate = new BigDecimal(sysConfigService.getDouble(TAX_RATE)).setScale(DEFAULT_TAX_SCALE,BigDecimal.ROUND_HALF_UP);
        out.put("taxRatePercent", taxRate);

        // 已到未认证进项票（所有服务中心）
        inQuery.setStatus(Arrays.asList(InvoiceInStatus.RECEIVED, InvoiceInStatus.SENT, InvoiceInStatus.WAIT));
        BigDecimal invoiceInSumAmount = invoiceInService.querySumAmount(inQuery).setScale(DEFAULT_AMOUNT_SCALE,BigDecimal.ROUND_HALF_UP);
        out.put("invoiceInSumAmount", invoiceInSumAmount);

        // 进项票已确认的总额
        inQuery.setApplyIds(idList);
        inQuery.setStatus(Arrays.asList(InvoiceInStatus.WAIT));
        BigDecimal invoiceInConfirmedAmount = invoiceInService.querySumAmount(inQuery).setScale(DEFAULT_AMOUNT_SCALE,BigDecimal.ROUND_HALF_UP);
        out.put("invoiceInConfirmedAmount", invoiceInConfirmedAmount);
        
        // 待开销项票总额
        BigDecimal waitApplyAmount = outItemDetailService.queryApplyWaitAmount(startTime, endTime);
        out.put("waitApplyAmount", waitApplyAmount);

        // 本月已开 (包含所有服务中心的数据)
        BigDecimal openedAmount = outItemDetailService.querySumAmount(startTime, endTime).setScale(DEFAULT_AMOUNT_SCALE,BigDecimal.ROUND_HALF_UP);
        out.put("openedAmount", openedAmount);

        // 本月已认证 (包含所有服务中心的数据)
        inQuery = new InvoiceInQuery();
        inQuery.setStatus(Arrays.asList(InvoiceInStatus.ALREADY));
        inQuery.setInvoiceStartTime(startTime);
        inQuery.setInvoiceEndTime(endTime);
        BigDecimal approvedMonthAmount = invoiceInService.querySumAmount(inQuery).setScale(DEFAULT_AMOUNT_SCALE,BigDecimal.ROUND_HALF_UP);
        out.put("approvedMonthAmount", approvedMonthAmount);

        // 进项总额（元）= 本月已认证进项票（所有服务中心）+ 已到未认证进项票（所有服务中心）
        BigDecimal inAmount = approvedMonthAmount.add(invoiceInSumAmount).setScale(DEFAULT_AMOUNT_SCALE,BigDecimal.ROUND_HALF_UP);
        out.put("inAmount", inAmount);

        // 销项票申请总额（已申请销项但是未开的金额）
        InvoiceOutApplyQuery invoiceOutApplyQuery = new InvoiceOutApplyQuery();
        //invoiceOutApplyQuery.setIds(idList);
        BigDecimal applyTotalAmount = invoiceOutApplyService.queryTotalApplyAmount(invoiceOutApplyQuery).setScale(DEFAULT_AMOUNT_SCALE,BigDecimal.ROUND_HALF_UP);
        out.put("applyTotalAmount", applyTotalAmount);

        // 申请销项（元）= 本月已开总额（所有服务中心）+ 销项票申请总额（所有服务中心）
        BigDecimal applyOutAmount = openedAmount.add(applyTotalAmount).setScale(DEFAULT_AMOUNT_SCALE,BigDecimal.ROUND_HALF_UP);
        out.put("applyOutAmount", applyOutAmount);

        // 待确认的进项票发票总额
        inQuery = new InvoiceInQuery();
        inQuery.setStatus(Arrays.asList(InvoiceInStatus.SENT));
        inQuery.setApplyIds(idList);
        BigDecimal tobeInAmount = invoiceInService.querySumAmount(inQuery).setScale(DEFAULT_AMOUNT_SCALE,BigDecimal.ROUND_HALF_UP);
        out.put("tobeInAmount", tobeInAmount);

        // 暂缓认证的进项票（所有服务中心）
        inQuery = new InvoiceInQuery();
        inQuery.setIsDefer(InvoiceInIsDefer.YES.getCode());
        BigDecimal suspendAmount = invoiceInService.querySumAmount(inQuery).setScale(DEFAULT_AMOUNT_SCALE,BigDecimal.ROUND_HALF_UP);
        out.put("suspendAmount", suspendAmount);
    }

    /**
     * 获取已确认未认证的进项票
     *
     * @param ids 待开票清单ID
     * @return 进项票集合
     */
    private List<Long> getApplyIds(String ids) {
        String[] tempIds = ids.split(",");
        if (tempIds.length > 0) {
            List<Long> idList = new ArrayList<>();
            for (String item : tempIds) {
                idList.add(new Long(item));
            }
            return idList;
        }
        return null;
    }

    /**
     * 根据进项票发票ID查询销项票详情
     *
     * @param invoiceInId 进项票ID
     * @return 进项票详情集合
     */
    @ResponseBody
    @RequestMapping(value = "applyitemdetails.html", method = RequestMethod.POST)
    public Result applyItemDetails(@RequestParam("ids") String ids, @RequestParam("invoiceInId") Long invoiceInId) {
        List<Long> idList = getApplyIds(ids);
        InvOutApplyItemDetailQuery itemDetailQuery = new InvOutApplyItemDetailQuery();
        //itemDetailQuery.setStatus(InvoiceOutMainStatus.NO_INPUT.getCode());
        itemDetailQuery.setApplyIds(idList);
        itemDetailQuery.setInvoiceInId(invoiceInId);
        itemDetailQuery.setIsDefer(InvoiceOutIsDefer.NO.getCode());
        List<InvoiceOutApplyItemDetailDto> list = outItemDetailService.query(itemDetailQuery);
        return new Result(list);
    }

    /**
     * 根据进项票发票ID查询待开销项票详情
     *
     * @param invoicesJson 进项票Id与申请Id
     * @return 进项票详情集合
     */
    @ResponseBody
    @RequestMapping(value = "getoutitemdetails.html", method = RequestMethod.POST)
    public Result getOutItemDetails(@RequestParam("invoicesJson") String invoicesJson) {
        Result result = new Result();
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode jsonNode = mapper.readTree(invoicesJson);

            JsonNode applyIdsNode = jsonNode.path("applyIds");
            JsonNode notInvoiceInIdsNode = jsonNode.path("notInvoiceInIds");
            // 申请的ID集合
            Long[] applyIds = mapper.readValue(applyIdsNode, Long[].class);
            // 已确认未认证的进项票
            Long[] notInvoiceInIds = mapper.readValue(notInvoiceInIdsNode, Long[].class);
            InvOutApplyItemDetailQuery itemDetailQuery = new InvOutApplyItemDetailQuery();
            //itemDetailQuery.setStatus(InvoiceOutMainStatus.NO_INPUT.getCode());
            itemDetailQuery.setApplyIds(Arrays.asList(applyIds));
            if (notInvoiceInIds != null && notInvoiceInIds.length > 0) {
                itemDetailQuery.setNotInvoiceInIds(Arrays.asList(notInvoiceInIds));
            }
            List<String> invInStatusList = new ArrayList<>();
            invInStatusList.add(InvoiceInStatus.WAIT.toString());
            invInStatusList.add(InvoiceInStatus.ALREADY.toString());
            itemDetailQuery.setInvInStatusList(invInStatusList);
            List<String> relationStatusList = new ArrayList<>();
            relationStatusList.add(InvoiceInDetailStatus.HasRelation.getCode());
            relationStatusList.add(InvoiceInDetailStatus.Invoice.getCode());
            itemDetailQuery.setRelationStatusList(relationStatusList);
            itemDetailQuery.setIsDefer(InvoiceOutIsDefer.NO.getCode());
            List<InvoiceOutApplyItemDetailDto> outItemDetails = outItemDetailService.query(itemDetailQuery);
            result.setData(outItemDetails);
        } catch (IOException e) {
            logger.error("生成开票清单转换数据失败。", e);
            result.setData(e.toString());
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 导出excel
     *
     * @param invoicesJson 发票数据集合
     * @return 返回excel文件
     */
    @RequestMapping(value = "importtoexcel.html", produces = {"application/json; charset=utf-8"})
    public ModelAndView importToExcel(@RequestParam("invoicesJson") String invoicesJson) {
        ModelAndView mv = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            invoicesJson=URLDecoder.decode(invoicesJson,"utf-8");
            JsonNode jsonNode = mapper.readTree(invoicesJson);
            JsonNode invoiceInNode = jsonNode.path("invoiceInArray");
            JsonNode suspendNode = jsonNode.path("suspendArray");
            JsonNode waitNode = jsonNode.path("waitArray");

            List<Map<String, Object>> listExcel = new ArrayList<Map<String, Object>>();
            Map<String, Object> invoiceInMap = jsonToInvoiceExcelData(invoiceInNode, false);
            listExcel.add(invoiceInMap);

            Map<String, Object> suspendInMap = jsonToInvoiceExcelData(suspendNode, true);
            listExcel.add(suspendInMap);

            Map<String, Object> waitMap = jsonToInvoiceDetailExcelData(waitNode);
            listExcel.add(waitMap);

            Map<String, Object> dataMap = new HashMap<String, Object>();
            dataMap.put("data", listExcel);
            ObjectExcelView erv = new ObjectExcelView();                //执行excel操作
            mv = new ModelAndView(erv, dataMap);
        } catch (IOException e) {
            // handle error
        }
        return mv;
    }

    /**
     * JSON对象转进项票Excel数据
     *
     * @param jsonNode  json对象
     * @param showIndex 是否显示Index，true：显示，false：隐藏
     * @return excel数据
     */
    private Map<String, Object> jsonToInvoiceExcelData(JsonNode jsonNode, Boolean showIndex) {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        List<String> titles = new ArrayList<String>();
        Integer index = 0;
        if (showIndex) {
            titles.add("编号");
            index++;
        }
        titles.add("开票日期");
        titles.add("发票号");
        titles.add("服务中心");
        titles.add("卖家全称");
        titles.add("发票金额（元）");
        dataMap.put("titles", titles);
        List<PageData> varList = new ArrayList<PageData>();
        for (JsonNode node : jsonNode) {
            PageData vpd = new PageData();
            if (showIndex) {
                vpd.put("var" + index, node.path("index").asText());
            }
            vpd.put("var" + (index + 1), node.path("invoiceDate").asText());
            vpd.put("var" + (index + 2), node.path("code").asText());
            vpd.put("var" + (index + 3), node.path("orgName").asText());
            vpd.put("var" + (index + 4), node.path("sellerName").asText());
            vpd.put("var" + (index + 5), String.valueOf(new BigDecimal(node.path("amount").asText()).setScale(2, BigDecimal.ROUND_HALF_UP)));
            varList.add(vpd);
        }
        dataMap.put("varList", varList);

        return dataMap;
    }

    /**
     * JSON对象转进项票详情Excel数据
     *
     * @param jsonNode json对象
     * @return excel数据
     */
    private Map<String, Object> jsonToInvoiceDetailExcelData(JsonNode jsonNode) {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        List<String> titles = new ArrayList<String>();
        titles.add("编号");
        titles.add("买家全称");
        titles.add("品名");
        titles.add("材质");
        titles.add("规格");
        titles.add("数量（吨）");
        titles.add("价税合计（元）");
        dataMap.put("titles", titles);
        List<PageData> varList = new ArrayList<PageData>();
        for (JsonNode node : jsonNode) {
            PageData vpd = new PageData();
            vpd.put("var1", node.path("index").asText());
            vpd.put("var2", node.path("buyerName").asText());
            vpd.put("var3", node.path("nsortName").asText());
            vpd.put("var4", node.path("material").asText());
            vpd.put("var5", node.path("spec").asText());
            vpd.put("var6", node.path("weight").asText());
            vpd.put("var7", node.path("amount").asText());
            varList.add(vpd);
        }
        dataMap.put("varList", varList);

        return dataMap;
    }

    /**
     * @param invoicesJson 发票数据集合
     * @return 处理结果
     */
    @ResponseBody
    @RequestMapping(value = "generatechecklist.html", method = RequestMethod.POST)
    @OpLog(OpType.GenerateCheckList)
    @OpParam("invoicesJson")
    public Result generateCheckList(@RequestParam("invoicesJson") String invoicesJson) {
        Result result = new Result();
        if ((!permissionLimit.hasPermission(PERMISSION_INVOICEI_OUT_GENERATE))) {
            result.setData("您没有作该操作的权限");
            result.setSuccess(Boolean.FALSE);
            return result;
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode jsonNode = mapper.readTree(invoicesJson);

            JsonNode applyIdsNode = jsonNode.path("applyIds");
            JsonNode invoiceInIdsNode = jsonNode.path("invoiceInIds");
            JsonNode suspendIdsNode = jsonNode.path("suspendIds");
            JsonNode waitArrayNode = jsonNode.path("waitArray");
            JsonNode tobeOutIdIdsNode = jsonNode.path("tobeOutIdIds");
            JsonNode selectedNotOutIdsNode = jsonNode.path("selectedNotOutIds");
            JsonNode modifyNumNode = jsonNode.path("modifyNum");
            // 申请的ID集合
            Long[] applyIds = mapper.readValue(applyIdsNode, Long[].class);
            // 已确认未认证的进项票
            Long[] invoiceInIds = mapper.readValue(invoiceInIdsNode, Long[].class);
            // 已选择暂缓认证的发票ID集合
            Long[] suspendIds = mapper.readValue(suspendIdsNode, Long[].class);
            // 已选择待开的销项票详情集合
            List<InvoiceOutApplyItemDetailDto> itemDetailDtos = jsonToInvOutApplyItemDetail(waitArrayNode);
            // 待开销项ID集合
            Long[] tobeOutIdIds = mapper.readValue(tobeOutIdIdsNode, Long[].class);
            // 已选择不开的销项票ID集合
            Long[] selectedNotOutIds = mapper.readValue(selectedNotOutIdsNode, Long[].class);
            // 修改对象次数集合
            List<InvoiceOutItemDetail> modifyNums = jsonTomodifyNum(modifyNumNode);
            
            //add by tuxianming 20160516
            JsonNode sendStatusNode = jsonNode.path("sendStatus");
            InvoiceOutSendStatus sendStatus = InvoiceOutSendStatus.ALL;
            if(sendStatusNode!=null){
            	String tmp = mapper.readValue(sendStatusNode, String.class);
            	if(tmp!=null && tmp.length()>0){
            		try {
            			sendStatus = InvoiceOutSendStatus.valueOf(tmp);
					} catch (Exception e) {
						logger.info(e.getMessage(), e);
					}
            	}
            }
            
            User user = getLoginUser();
            // 生成开票清单
            outCheckListService.generateCheckList(Arrays.asList(invoiceInIds), Arrays.asList(suspendIds), Arrays.asList(applyIds),
                    itemDetailDtos, Arrays.asList(tobeOutIdIds), Arrays.asList(selectedNotOutIds), user, modifyNums, sendStatus);

        } catch (IOException e) {
            logger.error("生成开票清单转换数据失败。", e);
            result.setData(e.toString());
            result.setSuccess(false);
        } catch (BusinessException ex) {
            logger.error(ex.getMessage(), ex);
            result.setData(ex.getMsg());
            result.setSuccess(false);
        }

        return result;
    }

    /**
     * JSON对象转进项票详情Excel数据
     *
     * @param jsonNode json对象
     * @return excel数据
     */
    private List<InvoiceOutApplyItemDetailDto> jsonToInvOutApplyItemDetail(JsonNode jsonNode) {
        List<InvoiceOutApplyItemDetailDto> list = new ArrayList<InvoiceOutApplyItemDetailDto>();
        for (JsonNode node : jsonNode) {
            InvoiceOutApplyItemDetailDto item = new InvoiceOutApplyItemDetailDto();
            item.setId(node.path("id").asLong());
            item.setActualWeight(new BigDecimal(node.path("weight").asText()));
            item.setActualAmount(new BigDecimal(node.path("amount").asText()));
            item.setOrgId(node.path("orgId").asLong());
            list.add(item);
        }
        return list;
    }

    /**
     * JSON对象转进项票详情Excel数据
     *
     * @param jsonNode json对象
     * @return excel数据
     */
    private List<InvoiceOutItemDetail> jsonTomodifyNum(JsonNode jsonNode) {
        List<InvoiceOutItemDetail> list = new ArrayList<InvoiceOutItemDetail>();
        for (JsonNode node : jsonNode) {
            InvoiceOutItemDetail item = new InvoiceOutItemDetail();
            item.setId(node.path("id").asLong());
            item.setModificationNumber(node.path("modificationNumber").asInt());
            list.add(item);
        }
        return list;
    }
}
