package com.prcsteel.platform.order.web.controller.invoice;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.prcsteel.platform.account.model.enums.InvoiceDataStatus;
import com.prcsteel.platform.account.model.model.Account;
import com.prcsteel.platform.account.service.AccountService;
import com.prcsteel.platform.acl.model.model.Organization;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.acl.service.OrganizationService;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.enums.OpType;
import com.prcsteel.platform.common.service.OpLog;
import com.prcsteel.platform.common.service.OpParam;
import com.prcsteel.platform.common.vo.PageResult;
import com.prcsteel.platform.common.vo.Result;
import com.prcsteel.platform.order.model.dto.ConfirmedInvoiceOutDto;
import com.prcsteel.platform.order.model.dto.EnsureInvoiceOutDto;
import com.prcsteel.platform.order.model.dto.InvoiceOutMainDto;
import com.prcsteel.platform.order.model.enums.InvoiceOutMainStatus;
import com.prcsteel.platform.order.model.enums.InvoiceOutStatus;
import com.prcsteel.platform.order.model.enums.PrintStatus;
import com.prcsteel.platform.order.model.model.InvoiceOut;
import com.prcsteel.platform.order.model.model.InvoiceOutMain;
import com.prcsteel.platform.order.service.invoice.InvoiceOutMainService;
import com.prcsteel.platform.order.service.invoice.InvoiceOutService;
import com.prcsteel.platform.common.aspect.OpAction;
import com.prcsteel.platform.order.web.controller.BaseController;

/**
 * Created by Rabbit Mao on 2015/8/3.
 */
@Controller
@RequestMapping("/invoice/out/")
public class InvoiceOutController extends BaseController {
    private static Logger logger = LogManager.getLogger(InvoiceOutController.class);
    @Resource
    InvoiceOutService outService;
    @Resource
    InvoiceOutMainService invoiceOutMainService;
    @Resource
    OrganizationService organizationService;
    @Resource
    AccountService accountService;

    @RequestMapping("confirm.html")
    public void confirm() {
    }

    @RequestMapping("confirmed.html")
    public void confirmed() {
    }

    @RequestMapping("batchConfirm.html")
    public void batchConfirm() {
    }

    /**
     * 待确认列表
     *
     * @param sbuyerName
     * @param sexpressName
     * @param stimeFrom
     * @param stimeTo
     * @param start
     * @param length
     * @return
     */
    @RequestMapping(value = "list.html", method = RequestMethod.POST)
    @ResponseBody
    public PageResult list(String sbuyerName,
                           String sexpressName,
                           String stimeFrom,
                           String stimeTo,
                           Integer start,
                           Integer length) {
        Map<String, Object> param = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(sbuyerName)) {
            param.put("buyerName", sbuyerName);
        }
        if (StringUtils.isNotBlank(sexpressName)) {
            param.put("expressName", sexpressName);
        }
        if (StringUtils.isNotBlank(stimeFrom)) {
            try {
                param.put("sendTimeFrom", parseTime(stimeFrom));
            } catch (ParseException e) {
                logger.debug(e.getMessage());
                return null;
            }
        }
        if (StringUtils.isNotBlank(stimeTo)) {
            try {
                param.put("sendTimeTo", parseTime(stimeTo));
            } catch (ParseException e) {
                logger.debug(e.getMessage());
                return null;
            }
        }

        param.put("status", InvoiceOutStatus.SENT.getCode());
        param.put("userIds", getUserIds());
        int total = outService.getDataCountByParam(param);

        param.put("start", start);
        param.put("length", length);
        PageResult result = new PageResult();
        List<EnsureInvoiceOutDto> dtos = outService.listDataForShow(param);
        result.setData(dtos);
        result.setRecordsFiltered(dtos.size());
        result.setRecordsTotal(total);
        return result;
    }

    /**
     * 已确认列表
     *
     * @param sbuyerName
     * @param sexpressName
     * @param stimeFrom
     * @param stimeTo
     * @param start
     * @param length
     * @return
     */
    @RequestMapping(value = "confirmedList.html", method = RequestMethod.POST)
    @ResponseBody
    public PageResult confirmedList(String sbuyerName,
                                    String sexpressName,
                                    String stimeFrom,
                                    String stimeTo,
                                    Integer start,
                                    Integer length) {
        Map<String, Object> param = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(sbuyerName)) {
            param.put("buyerName", sbuyerName);
        }
        if (StringUtils.isNotBlank(sexpressName)) {
            param.put("expressName", sexpressName);
        }
        if (StringUtils.isNotBlank(stimeFrom)) {
            try {
                param.put("checkTimeFrom", parseTime(stimeFrom));
            } catch (ParseException e) {
                logger.debug(e.getMessage());
                return null;
            }
        }
        if (StringUtils.isNotBlank(stimeTo)) {
            try {
                param.put("checkTimeTo", parseTime(stimeTo));
            } catch (ParseException e) {
                logger.debug(e.getMessage());
                return null;
            }
        }
        param.put("status", InvoiceOutStatus.CONFIRMED_SUCCESS.getCode());
        param.put("printStatus", PrintStatus.NOPRINT.ordinal());
        param.put("userIds", getUserIds());
        int total = outService.getDataCountByParam(param);

        param.put("start", start);
        param.put("length", length);
        PageResult result = new PageResult();
        List<EnsureInvoiceOutDto> dtos = outService.listDataForShow(param);
        result.setData(dtos);
        result.setRecordsFiltered(dtos.size());
        result.setRecordsTotal(total);
        return result;
    }

    @RequestMapping(value = "getPrintTableData.html", method = RequestMethod.POST)
    @ResponseBody
    public Result getPrintTableData(String invoiceIds) {
        Map<String, Object> param = new HashMap<String, Object>();
        Result result = new Result();

        //处理ids的in条件
        if (StringUtils.isNotEmpty(invoiceIds)) {
            String[] idStrArray = invoiceIds.split(",");
            List<Long> idList = new ArrayList<Long>();
            for (String idStr : idStrArray) {
                idList.add(Long.valueOf(idStr));
            }
            List<ConfirmedInvoiceOutDto> dtos = outService.getPrintTableData(idList, getLoginUser());
            if (dtos.size() > 0) {
                result.setData(dtos);
                result.setSuccess(true);
            } else {
                result.setSuccess(false);
            }
        } else {
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 通过id获取销项票信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "selectById.html", method = RequestMethod.POST)
    @ResponseBody
    public Result selectById(Long id) {
        Result result = new Result();
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("id", id);
        param.put("status", InvoiceOutStatus.SENT.getCode());
        List<EnsureInvoiceOutDto> dtos = outService.listDataForShow(param);
        if (dtos.size() == 1) {
            result.setSuccess(true);
            result.setData(dtos.get(0));
        } else {
            result.setSuccess(false);
            result.setData("获取信息失败，请关闭弹出窗重试");
        }
        return result;
    }

    /**
     * 确认发票
     *
     * @param invoiceOutCode
     * @param invoiceOutAmount
     * @return
     */
    @RequestMapping(value = "confirm.html", method = RequestMethod.POST)
    @OpLog(OpType.ConfirmInvoice)
    @OpParam("invoiceOutCode")
    @OpParam("invoiceOutAmount")
    @ResponseBody
    public Result confirm(String invoiceOutCode, BigDecimal invoiceOutAmount) {
        Result result = new Result();

        if (outService.confirm(invoiceOutCode, invoiceOutAmount, getLoginUser())) {
            result.setSuccess(true);
        } else {
            result.setSuccess(false);
            result.setData("确认发票失败");
        }
        return result;
    }


    /**
     * string类型转date类型
     * controller直接接收data类型会出错
     *
     * @param time
     * @return
     * @throws ParseException
     */
    private Date parseTime(String time) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(Constant.TIME_FORMAT_DAY);
        Date d2 = sdf.parse(time);//将String to Date类型
        return d2;
    }


    /**
     * 进入销项待录入列表
     *
     * @param out
     */
    @RequestMapping("inputinvoicelist.html")
    public void inputInvoiceList(ModelMap out) {

        // 开单日期:开始时间、结束时间(当前月第一天、当前时间)
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        // 界面默认显示的开单时间
        out.put("startTime", format.format(cal.getTime()));
        out.put("endTime", format.format(new Date()));

        //导航数字
        Map<String, Object> paramMap = new HashMap<String, Object>();
        InvoiceOutMainDto dto = new InvoiceOutMainDto();
        dto.setStatus(InvoiceOutMainStatus.NO_INPUT.getCode());
        paramMap.put("dto", dto);
        int total = invoiceOutMainService.countByBuyerAndCreated(paramMap);
        out.put("inputInvoiceTotal", total);
    }

    /**
     * 加载待录入的销项发票列表
     *
     * @param dto
     * @param start
     * @param length
     * @return
     */
    @RequestMapping("loadinputinvoiceoutdata.html")
    @ResponseBody
    public PageResult loadInputInvoiceOutData(InvoiceOutMainDto dto, @RequestParam("start") Integer start
            , @RequestParam("length") Integer length) {

        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("dto", dto);
        paramMap.put("start", start);
        paramMap.put("length", length);

        List<InvoiceOutMainDto> list = invoiceOutMainService.selectByBuyerAndCreated(paramMap);
        int total = invoiceOutMainService.countByBuyerAndCreated(paramMap);
        PageResult result = new PageResult();
        result.setData(list);
        result.setRecordsFiltered(total);
        result.setRecordsTotal(list.size());
        return result;
    }

    /**
     * 进入录入发票页面
     *
     * @param ids
     * @param orgId
     * @param out
     */
    @RequestMapping("inputinvoiceinfo.html")
    public void inputInvoiceinfo(@RequestParam("ids") String ids, @RequestParam("orgId") Long orgId, ModelMap out) {
        InvoiceOutMain invoice = invoiceOutMainService.getInvoiceOutMainByIds(ids);
        Organization o = organizationService.queryById(orgId);
        out.put("invoice", invoice);
        out.put("orgId", orgId);
        out.put("orgName", o.getName());
        out.put("ids", ids);
    }

    /**
     * 提交发票：
     * 保存发票信息并更新财务已开票
     *
     * @param invoiceJson
     * @return
     */
    @RequestMapping(value = "submitinvoice.html", method = RequestMethod.POST)
    @OpLog(OpType.SubmitInvoice)
    @OpParam("invoicesJson")
    @ResponseBody
    public Result submitInvoice(@RequestParam("invoiceJson") String invoiceJson) {
        Result result = new Result();
        boolean success = false;
        logger.info("invoiceJson:" + invoiceJson);

        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode jsonNode = mapper.readTree(invoiceJson);

            //验证发票总额和开票总额是否相等
            BigDecimal totalAmount = new BigDecimal(jsonNode.path("totalAmount").asText());
            BigDecimal totalInvoiceAmount = new BigDecimal(jsonNode.path("totalInvoiceAmount").asText());
            if (!equalsNumber(totalAmount, totalInvoiceAmount)) {
                result.setSuccess(success);
                result.setData("发票金额合计与开票金额合计不相等");
                return result;
            }

            //系统生成发票表的ids
            JsonNode idsNode = jsonNode.path("ids");
            Long[] ids = mapper.readValue(idsNode, Long[].class);

            //发票数据
            List<InvoiceOut> list = jsonToInvoiceOuts(jsonNode);
            User user = getLoginUser();
            success = outService.saveInvoice(user, list, ids);

        } catch (Exception e) {
            success = false;
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        result.setSuccess(success);
        return result;
    }

    /**
     * json转InvoiceOut集合
     *
     * @param jsonNode
     * @return
     */
    private List<InvoiceOut> jsonToInvoiceOuts(JsonNode jsonNode) {
        List<InvoiceOut> list = new ArrayList<InvoiceOut>();
        JsonNode invoiceNode = jsonNode.path("invoiceDetails");
        InvoiceOut invoice;
        for (JsonNode node : invoiceNode) {
            invoice = new InvoiceOut();
            invoice.setBuyerName(node.path("buyerName").asText());
            invoice.setBuyerId(node.path("buyerId").asLong());
            invoice.setOrgId(node.path("orgId").asLong());
            invoice.setOrgName(node.path("orgName").asText());
            invoice.setCode(node.path("code").asText());
            invoice.setAmount(new BigDecimal(node.path("amount").asText()));
            list.add(invoice);
        }
        return list;
    }


    private boolean equalsNumber(BigDecimal b1, BigDecimal b2) {
        if (b1 == null || b2 == null) return false;
        return b1.compareTo(b2) == 0;
    }

    @RequestMapping("invoicedata/unchecklist")
    public void showUncheckList() {

    }

    @RequestMapping("invoicedata/unchecklist/search")
    @ResponseBody
    public Result search(Long orgId, String buyerName) {
        Result result = new Result();
        if (StringUtils.isNotEmpty(buyerName)) {
            buyerName = "%" + buyerName + "%";
        }
        List<Account> list = accountService.selectUncheckedBuyerList(orgId, buyerName);
        result.setSuccess(true);
        result.setData(list);
        return result;
    }

    @RequestMapping("invoicedata/account/{accountId}/show")
    public String showData(ModelMap out, @PathVariable Long accountId) {
        out.put("account", accountService.selectByPrimaryKey(accountId));
        return "invoice/out/invoicedata/show";
    }
    
    @OpAction(key="accountId")
    @RequestMapping("invoicedata/account/{accountId}/approve")
    @OpLog(OpType.UpdateApprove)
    @OpParam("accountId")
    @ResponseBody
    public Result approve(ModelMap out, @PathVariable Long accountId) {
        Result result = new Result();
        accountService.updateInvoiceDataStatusByPrimaryKey(accountId, InvoiceDataStatus.Approved.getCode(), null,getLoginUser().getLoginId());
        result.setSuccess(true);
        result.setData("审核通过操作成功");
        return result;
    }
    
    @OpAction(key="accountId")
    @OpLog(OpType.UpdateDecline)
    @OpParam("accountId")
    @RequestMapping("invoicedata/account/{accountId}/decline")
    @ResponseBody
    public Result decline(ModelMap out, @PathVariable Long accountId,String reason) {
        Result result = new Result();
        accountService.updateInvoiceDataStatusByPrimaryKey(accountId, InvoiceDataStatus.Declined.getCode(), reason,getLoginUser().getLoginId());
        result.setSuccess(true);
        result.setData("审核不通过操作成功");
        return result;
    }
}
