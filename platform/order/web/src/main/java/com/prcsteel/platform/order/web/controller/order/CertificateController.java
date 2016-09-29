package com.prcsteel.platform.order.web.controller.order;

import com.prcsteel.platform.account.model.enums.AccountType;
import com.prcsteel.platform.acl.model.dto.UserOrgsDto;
import com.prcsteel.platform.acl.model.model.Organization;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.acl.service.OrganizationService;
import com.prcsteel.platform.acl.service.UserOrgService;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.dto.ResultDto;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.common.utils.PageData;
import com.prcsteel.platform.common.utils.Tools;
import com.prcsteel.platform.common.vo.PageResult;
import com.prcsteel.platform.common.vo.Result;
import com.prcsteel.platform.order.model.dto.*;
import com.prcsteel.platform.order.model.enums.CertificateStatus;
import com.prcsteel.platform.order.model.model.BusiConsignOrderCertificateInvoice;
import com.prcsteel.platform.order.model.model.BusiConsignOrderCredential;
import com.prcsteel.platform.order.model.model.ConsignOrder;
import com.prcsteel.platform.order.model.model.ConsignOrderAttachment;
import com.prcsteel.platform.order.model.model.ConsignOrderItems;
import com.prcsteel.platform.order.model.query.BatchUploadingQuery;
import com.prcsteel.platform.order.model.query.CertificateInvoiceQuery;
import com.prcsteel.platform.order.model.query.OrderTradeCertificateQuery;
import com.prcsteel.platform.order.model.query.TradeCredentialQuery;
import com.prcsteel.platform.order.service.order.CertificateService;
import com.prcsteel.platform.order.service.order.ConsignOrderAttachmentService;
import com.prcsteel.platform.order.service.order.ConsignOrderItemsService;
import com.prcsteel.platform.order.service.order.ConsignOrderService;
import com.prcsteel.platform.order.web.controller.BaseController;
import com.prcsteel.platform.order.web.support.ObjectExcelView;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 
    * @ClassName: CertificateController
    * @Description: 凭证控制器
    * @author Green.Ge
    * @date 2016年4月8日
    *
 */
@Controller
@RequestMapping("/order/certificate/")
public class CertificateController extends BaseController {

	private Logger log = Logger.getLogger(CertificateController.class);
	
    @Autowired
    private CertificateService certificateService;

    @Autowired
    private QueryOrderController queryOrderController;
    
    @Autowired
    private ConsignOrderService consignOrderService;
    
    @Autowired
    private ConsignOrderItemsService consignOrderItemsService;
    
    @Autowired
    private OrganizationService organizationService;

    @Resource
	private UserOrgService userOrgService;
    
    @Resource
    private ConsignOrderAttachmentService consignOrderAttachmentService;
    /**
     * 加载  交易凭证审核 页面
     * tuxianming
     * @return
     */
    @RequestMapping("checklist.html")
    public String checklist(ModelMap out,String isAudit){
    	List<UserOrgsDto> userOrgs = userOrgService.getConfigBusinessOrgByUserId(getLoginUser().getId());
    	out.put("userOrgs", userOrgs);
    	out.put("isAudit", isAudit);//isAudit uncheck  来自不需审核风控列表，check 来自需要审核风控列表
    	out.put("startTime", Tools.getFirstDayOfMonth());
        out.put("endTime", Tools.dateToStr(new Date(), Constant.TIME_FORMAT_DAY));
    	return "/order/risk/checklist";
    }
    
    /**
     * 加载 交易凭证审核 列表 -- 才能必须审核通过
     * tuxianming
     * @return
     */
    @ResponseBody
	@RequestMapping("loadchecklist.html")
    public PageResult loadChecklist(TradeCredentialQuery query){
    	
    	List<ConsignOrderCredentialDto> credetials = certificateService.queryChecklist(query);
    	
    	credetials.forEach(credetial ->{
    		credetial.setSubmitDateStr(Tools.dateToStr(credetial.getSubmitDate(), "yyyy/MM/dd HH:mm:ss"));
    		if(query.getCheck()){
    			long curr = new Date().getTime();
    			long open = credetial.getSubmitDate().getTime();
    			credetial.setDurationDay((int)((curr-open)/(3600*24*1000)));
    		}
    		
    		/*if(credetial.getBatchBuyerCredentialCode()!=null || credetial.getBatchSellerCredentialCode()!=null){
    			credetial.setOrderId(null);
    		}*/
    		
    	});
    	
    	PageResult result = new PageResult();
		result.setData(credetials);
		int total = certificateService.queryTotalChecklist(query);
		result.setRecordsFiltered(total);
		result.setRecordsTotal(credetials.size());
    	
    	return result;
    }
    
	/**
	 * 保存打印凭证
	 * tuxianming
	 * @param query:	needPage(是不是批量打印: false批量打印， true单独打印)，
	 * 			 	  	credentialType 打印的凭证类型（seller/buyer）
	 * 					orderIds 要打印凭证号的订单号数组
	 * 					sellerId 如果type=seller的时候，sellerId必须要有值
	 * 					credentialNum：凭证总页数
	 * @return
	 */
	@ResponseBody
	@RequestMapping("saveprintinfo.html")
	public Result savePrintInfo(OrderTradeCertificateQuery query) {
		Result result = new Result();
		String certificateCode = "";
		if(query.getOrderIds()==null || query.getOrderIds().size()==0)
			return new Result("order ids不为空", false);
		InetAddress ia=null;
		try {
			ia=InetAddress.getLocalHost();
			String localip=ia.getHostAddress();//当前IP
			User user = getLoginUser();
			Organization org = organizationService.queryById(user.getOrgId());
			
			//更新卖家凭证
			if(query.getCredentialType().equals("seller")){
				List<ConsignOrderItems> items = consignOrderItemsService.selectByOrderIdAndSellerId(query.getOrderIds().get(0),query.getSellerId());
				ConsignOrderItems item = items.get(0);
				if(query.getNeedPage()){
					//单独打印， 更新卖家打印凭证
					certificateCode =item.getSellerCredentialCode();
					if("".equals(certificateCode) || certificateCode == null) {
						//如果凭证没有生成，则重新生成
						certificateCode = certificateService.generateCertificateCode(org.getSeqCode(), query.getCredentialType());
						processSellerUpdateCertificate(certificateCode, query.getOrderIds().get(0), user, query);
						certificateService.savePrintcateInfo(certificateCode,query,user,localip,1, query.getCredentialNum());
					}else {
						//如果凭证已生成，则更新打印次数
						certificateService.updatePrintcateInfo(certificateCode, user, localip, query.getPrintQRCode(), query.getCredentialNum());
					}
					
				}else{
					//批量打印，更新卖家打印凭证
					//保存到Certificate
					certificateCode =item.getBatchSellerCredentialCode();
					if("".equals(certificateCode) || certificateCode == null) {
						//如果凭证没有生成，则重新生成
						certificateCode = certificateService.generateCertificateCode(org.getSeqCode(), query.getCredentialType());
						for (Long orderId : query.getOrderIds()) {
							processSellerUpdateCertificate(certificateCode, orderId,user, query);
						}
						certificateService.savePrintcateInfo(certificateCode,query,user,localip,1,query.getCredentialNum());
					}else {
						//如果凭证已生成，则更新打印次数
						certificateService.updatePrintcateInfo(certificateCode, user, localip, query.getPrintQRCode(),query.getCredentialNum());
					}

				}

			}else{  //更新买家凭证
				ConsignOrder order  = consignOrderService.selectByPrimaryKey(query.getOrderIds().get(0));
				if(query.getNeedPage()){
					//单独打印， 更新买家打印凭证
					//保存到Certificate
					certificateCode =order.getBuyerCredentialCode();
					if("".equals(certificateCode)|| certificateCode == null) {
						//如果凭证没有生成，则重新生成
						certificateCode = certificateService.generateCertificateCode(org.getSeqCode(), query.getCredentialType());
						processBuyerUpdateCertificate(certificateCode, query.getOrderIds().get(0), user, query);
						certificateService.savePrintcateInfo(certificateCode, query, user, localip, 1, query.getCredentialNum());
					}else {
						//如果凭证已生成，则更新打印次数
						certificateService.updatePrintcateInfo(certificateCode, user, localip, query.getPrintQRCode(),query.getCredentialNum());
					}

				}else{
					//批量打印，更新买家打印凭证
					//保存到Certificate
					certificateCode =order.getBatchBuyerCredentialCode();
					if("".equals(certificateCode)|| certificateCode == null) {
						//如果凭证没有生成，则重新生成
						certificateCode = certificateService.generateCertificateCode(org.getSeqCode(), query.getCredentialType());
						for (Long orderId : query.getOrderIds()) {
							processBuyerUpdateCertificate(certificateCode, orderId,user,  query);
						}
						 certificateService.savePrintcateInfo(certificateCode,query,user,localip,1, query.getCredentialNum());
					}else {
						//如果凭证已生成，则更新打印次数
						certificateService.updatePrintcateInfo(certificateCode,user,localip, query.getPrintQRCode(),query.getCredentialNum());
					}

				}
			}
			//保存打印信息
			result.setData(certificateCode + "|" + Tools.getQRCodeImgBase64(certificateCode,50,50));
			return result;
		}catch (BusinessException e){
			log.error(e.getMsg(),e);
			return new Result(e.getMsg(), false);
		}catch (Exception e1) {
			log.error(e1.getMessage(), e1);
			return new Result(e1.getMessage(), false);
		}
		
	}
	/**
	 * wangxianjun
	 * 保存打印信息
	 */
	/*public Result savePrintcateInfo(String certificateCode,OrderTradeCertificateQuery query,User user,String ip,int printNum) {
		//保存到Certificate
		Result result = new Result();
		try {
			BusiConsignOrderCredential credential = new BusiConsignOrderCredential();
			credential.setCredentialCode(certificateCode);
			credential.setType(query.getCredentialType());
			setCredentialName(credential, query);
			credential.setPrintNum(printNum);
   			credential.setPrintIp(ip);
			credential.setStatus("PENDING_SUBMIT");
			credential.setIsDeleted(false);
			credential.setPrintDate(new Date());
			credential.setPrintedBy(user.getName());
			if (certificateService.save(credential) > 0) {
				result.setSuccess(true);
				result.setData("保存打印信息成功");
			} else {
				result.setSuccess(false);
				result.setData("保存打印信息失败");
			}
		}catch (Exception e){
			result.setSuccess(false);
			result.setData("保存打印信息失败：" + e.getMessage());
		}
		return result;
	}
	*//**
	 * wangxianjun
	 * 更新打印信息
	 *//*
	public Result updatePrintcateInfo(String certificateCode,User user,String ip) {
		//保存到Certificate
		Result result = new Result();
		try {
			BusiConsignOrderCredential credential = new BusiConsignOrderCredential();
			credential.setCredentialCode(certificateCode);
			credential.setPrintDate(new Date());
			credential.setPrintIp(ip);
			credential.setPrintedBy(user.getName());
			if (certificateService.updateByCertSelective(credential) > 0) {
				result.setSuccess(true);
				result.setData("更新打印信息成功");
			} else {
				result.setSuccess(false);
				result.setData("更新打印信息失败");
			}
		}catch (Exception e){
			result.setSuccess(false);
			result.setData("更新打印信息失败：" + e.getMessage());
		}
		return result;
	}*/
	/**
	 * tuxianming
	 * 保存卖家打印凭证 到Certificate
	 */
	public void processSellerUpdateCertificate(String certificateCode, Long orderId, User user, OrderTradeCertificateQuery query){
		ConsignOrderItems item = new ConsignOrderItems();
		item.setOrderId(orderId);
		item.setSellerId(query.getSellerId());
		if(query.getNeedPage()) //单独打印
			item.setSellerCredentialCode(certificateCode);
		else
			item.setBatchSellerCredentialCode(certificateCode);
		
		consignOrderItemsService.udpateCertificateCode(item);
	}
	
	/**
	 * tuxianming
	 * 保存买家 到Certificate
	 */
	public void processBuyerUpdateCertificate(String certificateCode, Long orderId, User user, OrderTradeCertificateQuery query){
		
		ConsignOrder order = new ConsignOrder();
		order.setId(orderId);
		if (query.getNeedPage()) //单独打印
			order.setBuyerCredentialCode(certificateCode);
		else
			order.setBatchBuyerCredentialCode(certificateCode);
		
		consignOrderService.updateByPrimaryKeySelective(order);

	}



	/**
	 *
	 * @Author: wangxianjun
	 * @Description: 加载
	 * @Date: 2016年4月8日
	 * @param id 凭证id，accountId 客户id  accountType 客户类型
	 */
	@RequestMapping("/{id}/{accountType}/{accountId}/b.html")
	public String showCeritificateBatch(ModelMap out,@PathVariable Long id,@PathVariable Long accountId,@PathVariable String accountType) {

		if(id == null){
			throw new BusinessException(Constant.ERROR_RESOURCE_NOT_FOUND, "凭证id找不到");
		}
		BusiConsignOrderCredential orderCert= certificateService.findById(id);

		Map certMap = new HashMap();
		if(orderCert != null) {
			//已生成凭证
			List<String> certNames = consignOrderService.findCertnamesByAccount(orderCert.getCredentialCode(), accountId, accountType);
			out.put("certNames",certNames);

			out.put("printDate",Tools.dateToStr(orderCert.getPrintDate()));
			out.put("printCodeDate",Tools.dateToStr(orderCert.getPrintCodeDate()));
			certMap.put("orderId", id);
			certMap.put("type", "cert");
			List<ConsignOrderAttachment> attachments = consignOrderService.getAttachmentByOrderId(certMap);

			if (attachments.size() > 0) {
				out.put("attachments",attachments);
				String cName = orderCert.getName();//取凭证名称
				if (cName.indexOf("批") > 0) {
					cName = cName.substring(3);
				}
				out.put("cName",cName);
			}
			List<Long> orderIds = consignOrderService.findOrderIdIdByCert(orderCert.getCredentialCode());
			out.put("orderIds",orderIds);
		}

		out.put("accountId", accountId);
		return  "order/query/uploadcredentialbatch";

	}

	/**
	 *
	 * @Author: wangxianjun
	 * @Description: 加载
	 * @Date: 2016年4月8日
	 * @param orderId 订单号，accountId 客户id  accountType 客户类型
	 */
	@RequestMapping("/{orderId}/{sellerId}/{accountType}/a.html")
	public String showCeritificate(ModelMap out,@PathVariable Long orderId,@PathVariable Long sellerId,@PathVariable String accountType) {
		Long accountId = sellerId;
		if(orderId == null){
			throw new BusinessException(Constant.ERROR_RESOURCE_NOT_FOUND, "凭证id找不到");
		}
		BusiConsignOrderCredential orderCert = null;
		BusiConsignOrderCredential orderBuyerCert = null;
		String certCode = "";
		if("seller".equals(accountType)) {
			//卖家
			List<ConsignOrderItems> items = consignOrderItemsService.selectByOrderIdAndSellerId(orderId, accountId);
			ConsignOrderItems item = items.get(0);
			certCode = item.getSellerCredentialCode();
		}else{
			//买家
			ConsignOrder order  = consignOrderService.selectByPrimaryKey(orderId);
			certCode = order.getBuyerCredentialCode();
			accountId = order.getAccountId();

		}
		orderCert = certificateService.selectByCert(certCode);

		Map certMap = new HashMap();
		if(orderCert != null) {
			//已生成凭证
			List<String> certNames = consignOrderService.findCertnamesByAccount(orderCert.getCredentialCode(), accountId, accountType);
			out.put("certNames",certNames);
			out.put("orderCert",orderCert);
			out.put("printDate",Tools.dateToStr(orderCert.getPrintDate()));
			out.put("printCodeDate",Tools.dateToStr(orderCert.getPrintCodeDate()));
			certMap.put("orderId", orderCert.getId());
			certMap.put("type", "cert");
			List<ConsignOrderAttachment> attachments = consignOrderService.getAttachmentByOrderId(certMap);

			if (attachments.size() > 0) {
				out.put("attachments",attachments);
				String cName = orderCert.getName();//取凭证名称
				if (cName.indexOf("批") > 0) {
					cName = cName.substring(3);
				}
				out.put("cName",cName);
			}

		}
		if("seller".equals(accountType)){
			//卖家
			orderBuyerCert = certificateService.findBuyerCertBySellerCert(orderCert.getCredentialCode());

		}else{
			//买家
			orderBuyerCert = certificateService.findSellerCertBybuyerCert(orderCert.getCredentialCode());
		}
		if(orderBuyerCert != null && ("PENDING_APPROVAL".equals(orderCert.getStatus()) || "APPROVED".equals(orderCert.getStatus()))) {
			//只有当前凭证待审核和审核通过状态才展示
			out.put("auditCert",orderBuyerCert);
			certMap.put("orderId", orderBuyerCert.getId());
			certMap.put("type", "cert");
			List<ConsignOrderAttachment> auditAttachments = consignOrderService.getAttachmentByOrderId(certMap);

			if (auditAttachments.size() > 0) {
				out.put("auditAttachments",auditAttachments);
				String auditName = orderBuyerCert.getName();//取凭证名称
				if (auditName.indexOf("批") > 0) {
					auditName = auditName.substring(3);
				}
				out.put("auditName",auditName);
			}
		}
		List<Long> orderIds = new ArrayList<Long>();
		orderIds.add(orderId);
		//consignOrderService.findOrderIdIdByCert(orderCert.getCredentialCode());
		out.put("orderIds",orderIds);
		out.put("accountId", sellerId);
		return  "order/query/uploadcredential";

	}
	
	/**
	 * 
		* @Author: Green.Ge
	    * @Description: 跳转到上传卖家交易凭证
		* @Date: 2016年4月8日
	 */
	@RequestMapping("uploadcertificatelist/{accountType}.html")
	public String uploadcertificatelist(@PathVariable String accountType,ModelMap out) {
		queryOrderController.processOutData(Constant.ConsignOrderTab.TRADECERTIFICATE.toString(), out);
		/*List<Long> ids = getUserIds();
		Map<String, Object> userIds = new HashMap<>();
		userIds.put("userIds", ids);
		List<User> users = userService.queryByParam(userIds);

		out.put("users", users);*/
		out.put("user", getLoginUser());
		out.put("accountType", accountType);
		return "order/query/uploadcertificatelist";
	}
	
	/**
	 * 
		* @Author: Green.Ge
	    * @Description: 加载上传凭证 列表 数据（分页）
		* @Date: 2016年4月8日
	 */
	@ResponseBody
	@RequestMapping("loadcertificateforupload.html")
	public PageResult loadcertificateforupload(OrderTradeCertificateQuery query) {
		
		PageResult result = new PageResult();
		String accountType = query.getAccountType();
		List<OrderTradeCertificateForUploadDto> list = null;
		Integer total = 0;
		List<Long> ids = getUserIds();
		query.setUserIds(ids);
		if("管理员".equals(query.getOwnerName())){
			query.setOwnerName(null);
		}
		if(AccountType.seller.toString().equals(accountType)){
			//加载上传卖家凭证tab
			list = certificateService.loadcertificateforupload(query);
			total = certificateService.loadcertificateforuploadTotal(query);
			result.setData(list);
			result.setRecordsTotal(list.size());
			result.setRecordsFiltered(total);
		}else{
			//加载上传买家凭证tab
			list = certificateService.loadcertificateforuploadbuyer(query);
			total = certificateService.loadcertificateforuploadbuyerTotal(query);
			result.setData(list);
			result.setRecordsTotal(list.size());
			result.setRecordsFiltered(total);
		}
		
		return result;
	}
	/**
	 * 
		* @Author: Green.Ge
	    * @Description: 凭证明细-->单个订单
		* @Date: 2016年4月8日
	 */
	@RequestMapping("{orderId}/{sellerId}/{accountType}/{audit}/detail.html")
	public String certificatedetail(@PathVariable Long orderId, @PathVariable Long sellerId,@PathVariable String accountType,@PathVariable String audit,ModelMap out) {
		ConsignOrder originOrder = consignOrderService.selectByPrimaryKey(orderId);
		if(originOrder==null){
			throw new BusinessException(Constant.ERROR_RESOURCE_NOT_FOUND, "id为"+orderId+"的交易单不存在");
		}
		out.put("audit", audit);
		out.put("accountType", accountType);
		List<Long> ids = new ArrayList<Long>();
		ids.add(orderId);
		List<OrderItemDetailDto> orderitems = new ArrayList<OrderItemDetailDto>();
		if("seller".equals(accountType)) {
			//上传卖家凭证详情
			orderitems = consignOrderService.selectOrdersByParams(
					new OrderTradeCertificateQuery().setOrderIds(ids).setSellerId(sellerId)
			);
		}else{
			//上传买家凭证详情
			orderitems = consignOrderService.selectOrdersByParams(
					new OrderTradeCertificateQuery().setOrderIds(ids));
		}
		
		if(orderitems.size()>0){
			OrderItemDetailDto order = orderitems.get(0);
			
			order.setCreatedStr(Tools.dateToStr(order.getCreated()));
			order.getItems().forEach(item -> {
				order.setSellerName(item.getSellerName());
				//采购重量
				/*order.setTotalWeight((order.getTotalWeight()!=null?order.getTotalWeight():BigDecimal.ZERO)
						.add(item.getWeight()!=null?item.getWeight():BigDecimal.ZERO));
				*/
				//折让后的 实提重量
				BigDecimal actualWeight = item.getActualPickWeightServer();
				//用allowanceBuyerAmount 代替 实提销售金额   allowanceAmount 代替 实提采购金额 
				if(actualWeight.compareTo(BigDecimal.ZERO) != 0){
					//实提采购单价
					item.setCostPrice(item.getAllowanceAmount().divide(actualWeight,2,BigDecimal.ROUND_HALF_UP)
							);
					//实提销售单价
					item.setDealPrice(item.getAllowanceBuyerAmount().divide(actualWeight,2,BigDecimal.ROUND_HALF_UP)
							);
				}
				//采购重量
				/*order.setTotalWeight((order.getTotalWeight() != null ? order.getTotalWeight() : BigDecimal.ZERO)
						.add(item.getWeight() != null ? item.getWeight() : BigDecimal.ZERO));

				//折让后的 实提重量
				item.setActualPickWeightServer(
						(item.getActualPickWeightServer() != null ? item.getActualPickWeightServer() : BigDecimal.ZERO)
								.add(item.getAllowanceWeight() != null ? item.getAllowanceWeight() : BigDecimal.ZERO)
				);

				//实提总重量
				order.setActualPickTotalWeight(
						(order.getActualPickTotalWeight() != null ? order.getActualPickTotalWeight() : BigDecimal.ZERO)
								.add((item.getActualPickWeightServer() != null ? item.getActualPickWeightServer() : BigDecimal.ZERO))
				);


				BigDecimal actualPurchaseAmount = (item.getActualPickWeightServer() != null ? item.getActualPickWeightServer() : BigDecimal.ZERO)
						.multiply(item.getCostPrice() != null ? item.getCostPrice() : BigDecimal.ZERO)
						.setScale(BigDecimal.ROUND_HALF_UP, Constant.MONEY_PRECISION);

				//用invoiceAmount 代用  实提采购金额
				item.setInvoiceAmount(actualPurchaseAmount);

				//实提采购总金额
				order.setTotalAmount((order.getTotalAmount() != null ? order.getTotalAmount() : BigDecimal.ZERO)
						.add(actualPurchaseAmount));


				BigDecimal amount = (item.getActualPickWeightServer() != null ? item.getActualPickWeightServer() : BigDecimal.ZERO)
						.multiply(item.getDealPrice() != null ? item.getDealPrice() : BigDecimal.ONE)
						.setScale(BigDecimal.ROUND_HALF_UP, Constant.MONEY_PRECISION);

				//实提销售金额
				item.setAmount(
						amount.add(item.getAllowanceBuyerAmount() != null ? item.getAllowanceBuyerAmount() : BigDecimal.ZERO)
				);

				//实提销售总金额
				order.setActualPickTotalAmount(
						(order.getActualPickTotalAmount() != null ? order.getActualPickTotalAmount() : BigDecimal.ZERO)
								.add(item.getAmount() != null ? item.getAmount() : BigDecimal.ZERO)
				);*/
				

			});
			order.setTotalWeight(new BigDecimal(order.getItems().stream().mapToDouble(a -> a.getWeight().doubleValue()).sum()));//采购重量合计
			order.setActualPickTotalWeight(new BigDecimal(order.getItems().stream().mapToDouble(a -> a.getActualPickWeightServer().doubleValue()).sum()));//实提总重量合计
			order.setTotalAmount(new BigDecimal(order.getItems().stream().mapToDouble(a -> a.getAllowanceAmount().doubleValue()).sum()));//实提采购金额合计
			order.setActualPickTotalAmount(new BigDecimal(order.getItems().stream().mapToDouble(a -> a.getAllowanceBuyerAmount().doubleValue()).sum()));//实提销售金额合计
			
			out.put("order", order);
			//打印 上传
			Long accountId = sellerId;

			BusiConsignOrderCredential orderCert = null;
			BusiConsignOrderCredential orderBuyerCert = null;
			String certCode = "";
			if("seller".equals(accountType)) {
				//卖家
				List<ConsignOrderItems> items = consignOrderItemsService.selectByOrderIdAndSellerId(orderId, accountId);
				ConsignOrderItems item = items.get(0);
				certCode = item.getSellerCredentialCode();
			}else{
				//买家
				//ConsignOrder order  = consignOrderService.selectByPrimaryKey(orderId);
				certCode = originOrder.getBuyerCredentialCode();
				accountId = order.getAccountId();

			}
			orderCert = certificateService.selectByCert(certCode);

			Map certMap = new HashMap();
			if(orderCert != null) {
				//已生成凭证
				List<String> certNames = consignOrderService.findCertnamesByAccount(orderCert.getCredentialCode(), accountId, accountType);
				out.put("certNames",certNames);
				out.put("orderCert",orderCert);
				out.put("qrcode", Tools.getQRCodeImgBase64(certCode, 50, 50));
				out.put("printDate",Tools.dateToStr(orderCert.getPrintDate()));
				out.put("printCodeDate",Tools.dateToStr(orderCert.getPrintCodeDate()));
				certMap.put("orderId", orderCert.getId());
				certMap.put("type", "cert");
				certMap.put("orderByPageNumber", "orderByPageNumber");
				List<ConsignOrderAttachment> attachments = consignOrderService.getAttachmentByOrderId(certMap);

				if (attachments.size() > 0) {
					out.put("attachments",attachments);
					String cName = orderCert.getName();//取凭证名称
					if (cName.indexOf("批") > 0) {
						cName = cName.substring(3);
					}
					out.put("cName",cName);
				}

			}
			if("seller".equals(accountType)){
				//卖家
				orderBuyerCert = certificateService.findBuyerCertBySellerCert(orderCert.getCredentialCode());

			}else{
				//买家
				orderBuyerCert = certificateService.findSellerCertBybuyerCert(orderCert.getCredentialCode());
			}
			if(orderBuyerCert != null && ("PENDING_APPROVAL".equals(orderCert.getStatus()) || "APPROVED".equals(orderCert.getStatus()))) {
				//只有当前凭证待审核和审核通过状态才展示
				out.put("auditCert",orderBuyerCert);
				certMap.put("orderId", orderBuyerCert.getId());
				certMap.put("type", "cert");
				certMap.put("orderByPageNumber", "orderByPageNumber");
				List<ConsignOrderAttachment> auditAttachments = consignOrderService.getAttachmentByOrderId(certMap);

				if (auditAttachments.size() > 0) {
					out.put("auditAttachments",auditAttachments);
					String auditName = orderBuyerCert.getName();//取凭证名称
					if (auditName.indexOf("批") > 0) {
						auditName = auditName.substring(3);
					}
					out.put("auditName",auditName);
				}
			}

			
			//consignOrderService.findOrderIdIdByCert(orderCert.getCredentialCode());
			out.put("orderIds",ids);
			out.put("accountId", sellerId);
		}
		return "order/query/uploadcertificatedetail";
	}
	
	
	/**
	 * 
		* @Author: Green.Ge
	    * @Description: 凭证明细-->批量订单
		* @Date: 2016年4月8日
	 */
	@RequestMapping("{id}/{accountType}/{accountId}/{audit}/detailbatch.html")
	public String certificatedetailBatch(@PathVariable Long id,@PathVariable String accountType,@PathVariable Long accountId,@PathVariable String audit, ModelMap out) {
		BusiConsignOrderCredential orderCert = certificateService.findById(id);
		if(orderCert==null){
			throw new BusinessException(Constant.ERROR_RESOURCE_NOT_FOUND, "id为"+id+"的凭证不存在");
		}
		out.put("audit", audit);
		out.put("accountType", accountType);
		out.put("accountId", accountId);
		out.put("certificateNO", orderCert.getCredentialCode());
		//打印 上传

		Map certMap = new HashMap();
		if(orderCert != null) {
			//已生成凭证
			List<String> certNames = consignOrderService.findCertnamesByAccount(orderCert.getCredentialCode(), accountId, accountType);
			out.put("certNames",certNames);
			out.put("orderCert",orderCert);
			out.put("qrcode", Tools.getQRCodeImgBase64(orderCert.getCredentialCode(), 50, 50));
			out.put("printDate",Tools.dateToStr(orderCert.getPrintDate()));
			certMap.put("orderId", id);
			certMap.put("type", "cert");
			certMap.put("orderByPageNumber", "orderByPageNumber");
			List<ConsignOrderAttachment> attachments = consignOrderService.getAttachmentByOrderId(certMap);

			if (attachments.size() > 0) {
				out.put("attachments",attachments);
				String cName = orderCert.getName();//取凭证名称
				if (cName.indexOf("批") > 0) {
					cName = cName.substring(3);
				}
				out.put("cName",cName);
			}
			List<Long> orderIds = consignOrderService.findOrderIdIdByCert(orderCert.getCredentialCode());
			out.put("orderIds",orderIds);
		}
		return "order/query/uploadcertificatedetailbatch";
	}
	
	/**
	 * 
		* @Author: Green.Ge
	    * @Description: 打开新增要关联到凭证号的订单列表
		* @Date: 2016年4月11日
	 */
	@RequestMapping("openAddDialog/{accountType}/{cerfiticateNo}/{accountId}.html")
	public String openAddDialogForUpload(@PathVariable String accountType,
			@PathVariable String cerfiticateNo,
			@PathVariable Long accountId, 
			ModelMap out) {
		if(StringUtils.isBlank(accountType)){
			throw new BusinessException(Constant.ERROR_MISSING_REQUIRED_PARAM, "缺少客户类型参数");
		}
		if(StringUtils.isBlank(cerfiticateNo)){
			throw new BusinessException(Constant.ERROR_MISSING_REQUIRED_PARAM, "缺少凭证编号参数");
		}
		if(accountId==null||accountId<=0){
			throw new BusinessException(Constant.ERROR_MISSING_REQUIRED_PARAM, "缺少客户ID参数或者客户ID不合法");
		}
		List<Long> ids = getUserIds();
		Map<String, Object> userIds = new HashMap<>();
		userIds.put("userIds", ids);
		List<User> users = userService.queryByParam(userIds);

		out.put("users", users);
		out.put("accountType", accountType);
		out.put("cerfiticateNo", cerfiticateNo);
		out.put("accountId", accountId);
		return "order/query/uploadcertificatedetailbatch_add";
	}
	
	/**
	 * 加载订单列表 --> 批量上传凭证之新增凭证订单
	 * query:isNeedPage-true 批量加载
	 * Green.Ge 
	 */
	@ResponseBody
	@RequestMapping("loadtradecertificateForAdd.html")
	public PageResult loadSellerTradeCertificate(OrderTradeCertificateQuery query) {
		
		query.setUserIds(getUserIds());	//查看权限
		if("管理员".equals(query.getOwnerName())){
			query.setOwnerName(null);
		}
		query.setCloseStatus(true);					
		List<OrderInfoForCertificateDto> orderitems = certificateService.selectOrdersByParamsForUploadAdd(query);
		
		PageResult result = new PageResult();
		result.setData(orderitems);
		
		return result;
	}
	
	
	/**
	 * 在批量上传凭证页面删除一个订单
	 * Green.Ge 
	 * 
	 */
	@ResponseBody
	@RequestMapping("removeBatchCertificateNO/{accountType}/{accountId}/{orderId}.html")
	public Result removeBatchCertificateNO(@PathVariable String accountType,@PathVariable Long accountId,@PathVariable Long orderId) {
		
		Result result = new Result();
		try{
			//最后还是决定不要做这个交互
//			certificateService.removeBatchCertificateNO(accountType,accountId, orderId);
			result.setSuccess(true);
		}catch(BusinessException e){
			result.setSuccess(false);
			result.setData(e.getMsg());
		}
		return result;
	}
	
	/**
	 * 保存批量上传提交凭证
	 * Green.Ge 
	 * 
	 */
	@ResponseBody
	@RequestMapping("saveBatchUploadCertificateNO.html")
	public Result saveBatchUploadCertificateNO(String accountType,Long accountId,String certificateNO,String addIds,String delIds,String originIds) {
		
		Result result = new Result();
		User user = getLoginUser();
		Organization org = organizationService.queryById(user.getOrgId());
		try{
			Long certId = certificateService.saveBatchCertificateNO(user,org,accountType,accountId, certificateNO,addIds,delIds,originIds);
			result.setSuccess(true);
			result.setData(certId);
		}catch(BusinessException e){
			result.setSuccess(false);
			result.setData(e.getMsg());
		}
		return result;
	}
	/**
	 * 凭证提交审核
	 * modify by wangxianjun 20160412
	 * @param certId 凭证id,credentName 凭证名称，isBatch 是否批量 ，certtype 凭证类型  cause审核不通过原因
	 * @return
	 */
	@RequestMapping("submitCert.html")
	@ResponseBody
	public Result submitCert(@RequestParam("certid") Long certId,@RequestParam("status") String status, String cause) {
		Result result = new Result();
		if(certId == null){
			result.setData("凭证提交审核失败,凭证id为空");
			result.setSuccess(false);
		}

		BusiConsignOrderCredential certi = certificateService.findById(certId);
		List<ConsignOrderAttachment> attachment=consignOrderAttachmentService.getAttachmentByConsignOrderId(certId);
		//老数据直接提交
		//非集齐的不让提交
		if(StringUtils.isNotBlank(certi.getUploadStatus())
				&& status.equals("PENDING_APPROVAL") 
				&& Constant.CREDENTIAL_UPLOAD_STATUS.OLD_DATA.toString().equals(certi.getUploadStatus())==false
				&& Constant.CREDENTIAL_UPLOAD_STATUS.ALREADY_COLLECT.toString().equals(certi.getUploadStatus())==false){
			result.setData("凭证有多页，未全部上传，请全部上传后再提交给风控人员审核");
			result.setSuccess(false);
			return result;
		}
		if("PENDING_APPROVAL".equals(status)&&Constant.CREDENTIAL_UPLOAD_STATUS.OLD_DATA.toString().equals(certi.getUploadStatus())==false){
			if(certi.getCredentialNum()>attachment.size()){
				result.setData("凭证有多页，未全部上传，请全部上传后再提交给风控人员审核");
				result.setSuccess(false);
				return result;
			}
		}
		try {
			ResultDto res =certificateService.submitCertById(certId, getLoginUser(), status, cause);
			if(res.isSuccess()){
				result.setData(res.getMessage());
				result.setSuccess(true);
			}else {
				result.setData(res.getMessage());
				result.setSuccess(false);
			}


		} catch (BusinessException e) {
			result.setData(e.getMsg());
			result.setSuccess(false);
		}
		return result;
	}
/**
	 * 测试job
	 * @author peanut
	 * @date 2016/04/13
	 * @return
	 */
	@ResponseBody
	@RequestMapping("dotestjob.html")
	public void doJobTest(){
		certificateService.executeCertificateInvoiceJob();
	}

	/**
	 * 买、卖家凭证的已开票订单页面中转
	 * @param out
	 * @param accountType  客户类型 seller,buyer
	 * @author peanut
     * @return
     */
	@RequestMapping("certificateinvoiceorder/{accountType}.html")
	public String certificateInvoice(ModelMap out,@PathVariable String accountType){
		List<Long> ids = getUserIds();
		Map<String, Object> userIds = new HashMap<>();
		userIds.put("userIds", ids);
		List<User> users = userService.queryByParam(userIds);

		out.put("users", users);
		out.put("user", getLoginUser());
		out.put("accountType",accountType);
		out.put("certificateStatus",CertificateStatus.values());
		//得到数据
		queryOrderController.processOutData(Constant.ConsignOrderTab.TRADECERTIFICATE.toString(), out);
		return "order/query/certificateinvoice";
	}

	/**
	 * 查询需补齐买、卖家凭证的已开票订单
	 * @author peanut
	 * @date 2016/04/12
	 * @param query   CertificateInvoiceQuery查询对象
	 * @see     CertificateInvoiceQuery
	 * @return
	 * */
	@ResponseBody
	@RequestMapping("searchCertificateInvoice.html")
	public PageResult searchCertificateInvoice(CertificateInvoiceQuery query){
		List<BusiConsignOrderCertificateInvoice> list=certificateService.searchCertificateInvoice(query);
		PageResult pr= new PageResult();

		if(list!=null && list.size()>1){
			Integer total=list.get(list.size()-1).getTotal();
			//除去最后一行统计数据
			list.remove(list.size()-1);
			pr.setData(list);
			pr.setRecordsFiltered(total);
			pr.setRecordsTotal(total);
		}
		return pr;
	}
	
	/**
	 * 更新凭证号凭证条码次数
	 * tuxianming
	 * @param code 凭证号 
	 * @param type 打印次数类型：true：更新打印凭证号次数，false ：更新单子的次数
	 * @param isResult: 是不是要返回更新后的的数据,如果是：则返回更新后的BusiConsignOrderCredential对象 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("updateprinttimes.html")
	public Result updatePrintTimes(String code, 
			@RequestParam(value = "type", defaultValue = "false") Boolean type,
			@RequestParam(value = "isResult", defaultValue = "false") Boolean isResult,
			@RequestParam(value = "credentialNum") int credentialNum
			){
		
		try {
			String localip=InetAddress.getLocalHost().getHostAddress();//当前IP
			User user = getLoginUser();
			
			certificateService.updatePrintcateInfo(code, user, localip, type, credentialNum);
			
			Result result = new Result();
			if(isResult){
				List<BusiConsignOrderCredential> list = certificateService.findByCode(code);
				if(list.size()>0){
					BusiConsignOrderCredential dto = list.get(0);
					dto.setPrintCodeDateStr(Tools.dateToStr(dto.getPrintCodeDate()));
					result.setData(dto);
				}
			}
			return result;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new Result("更新失败！", false);
		}
		
	}
	

	
	/**
	 * 更新凭证号凭证条码: 将dto.updates里面的图片与凭证号进行绑定，并且更新凭证号页码状态
	 * 将dto.removes里面的图片进行删除 
	 * tuxianming
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "updateCredentialImages.html", method = RequestMethod.POST)
	public Result updateCredentialImages(UpdateCredentialImageDto dto){
		try {
			certificateService.updateCredentialImages(dto);
			return new Result("更新成功！");
		} catch (BusinessException e) {
			return new Result(e.getMsg(), false);
		} 
	}

	/**
	 * 买、卖家凭证的已开票订单页面中转
	 * @param out
	 * @author peanut
     * @return
     */
	@RequestMapping("batchuploading.html")
	public String batchUploading(ModelMap out){

		return "order/query/batchuploading";
	}
	/**
	 * 查询 批量上传凭证图片 tab页
	 * @author wangxiao
	 * @date 2016/05/23
	 * @param query   BatchUploadingQuery查询对象
	 * @return
	 * */
	@ResponseBody
	@RequestMapping("getbatchuploading.html")
	public PageResult getBatchUploading(BatchUploadingQuery query){
		List<Long> ids = getUserIds();
		query.setUserIds(ids);
		List<BatchUploadingDto> list=certificateService.searchBatchUploading(query);
		PageResult pr= new PageResult();
		if(list!=null && list.size()>0){
			Integer total=list.size();
			pr.setData(list);
			pr.setRecordsFiltered(certificateService.countBatchUploading(query).size());
			pr.setRecordsTotal(total);
		}
		return pr;
	}

	
	@ResponseBody
	@RequestMapping("getorderattachment.html")
	public Result getOrderAttachment(@RequestParam("id") Long id,@RequestParam("code") String code, @RequestParam(value = "orderByPageNumber",defaultValue = "") String orderByPageNumber){
		Result result = new Result();
		if(code.equals("-")){
			ConsignOrderAttachment achment = consignOrderAttachmentService.getAttachmentById(id);
			result.setData(achment);
			result.setSuccess(false);
			return result;
		}

		Map certMap = new HashMap();
		certMap.put("orderId", id);
		certMap.put("type", "cert");
		certMap.put("orderByPageNumber", orderByPageNumber);

		List<ConsignOrderAttachment> attachment=consignOrderService.getAttachmentByOrderId(certMap);
		if(attachment!=null&&attachment.size()>0){
			result.setData(attachment);
		}
		return result;
	}

	@ResponseBody
	@RequestMapping("delectcertificate.html")
	public Result delectCertificate(@RequestParam("id") Long id){
		Result result = new Result();
		ConsignOrderAttachment chment=consignOrderAttachmentService.queryById(id);
		int number=consignOrderAttachmentService.deleteByPrimaryKey(id);
		if(number==1){
			result.setData("删除成功！");
			if(chment!=null&&chment.getConsignOrderId()!=0){
			BusiConsignOrderCredential busiConsignOrderCredential=new BusiConsignOrderCredential();
			busiConsignOrderCredential.setId(chment.getConsignOrderId());
		    busiConsignOrderCredential.setUploadStatus(Constant.CREDENTIAL_UPLOAD_STATUS.PENDING_COLLECT.toString());
		    certificateService.updateByIdSelective(busiConsignOrderCredential);
			}
		}else{
			result.setSuccess(false);
			result.setData("删除失败！");
		}
		return result;
	}
	@ResponseBody
	@RequestMapping("editcertificate.html")
	public Result editCertificate(@RequestParam("id") String id,@RequestParam("pageNumber") String pageNumber){
		 String[]certificateId= id.split("、");
		 String[]number=pageNumber.split("、");
		 Result result = new Result();
		 List<ConsignOrderAttachment> orderAttachmentList=new ArrayList<ConsignOrderAttachment>();

		 ConsignOrderAttachment orderAttachment=null;
		 int numberInt=0;
		 for (int i = 0; i < number.length; i++) {
			 BusiConsignOrderCredential credential=null;
			 try {
			  credential =certificateService.findById(consignOrderAttachmentService.queryById(Long.parseLong(certificateId[i])).getConsignOrderId());
			 } catch (Exception e) {
				 result.setSuccess(false);
				 result.setData("数据不存在！");
				 return result;
			 }
			 if(!"OLD_DATA".equals(credential.getUploadStatus().toString())) {
				 try {
					 if (i != 0 && Integer.parseInt(number[i]) == numberInt) {
						 result.setSuccess(false);
						 result.setData("凭证当前页码不能重复！");
						 return result;
					 }
					 numberInt = Integer.parseInt(number[i]);
				 } catch (Exception e) {
					 result.setSuccess(false);
					 result.setData("凭证号错误！");
					 return result;
				 }
				 if (credential.getCredentialNum() < Integer.parseInt(number[i])) {
					 result.setSuccess(false);
					 result.setData("凭证当前页码不能大于凭证总页码 ！");
					 return result;
				 }
				 if (Integer.parseInt(number[i]) == 0) {
					 result.setSuccess(false);
					 result.setData("凭证当前页码必须大于0 ！");
					 return result;
				 }
			 }
			 orderAttachment=new ConsignOrderAttachment();
			 orderAttachment.setId(Long.parseLong(certificateId[i]));
			 orderAttachment.setPageNumber(Integer.parseInt(number[i]));
			 orderAttachmentList.add(orderAttachment);
		}

			
		if( consignOrderAttachmentService.updateByPrimaryKeySelective(orderAttachmentList)>0){
			result.setData("保存成功！");
		}
		return result;
	}
	@ResponseBody
	@RequestMapping("matecertificate.html")
	public Result mateCertificate(@RequestParam("id") String id,@RequestParam("code") String code,@RequestParam("pageNumber") String pageNumber){
		Result result = new Result();
		BusiConsignOrderCredential credential =certificateService.findByCredentialCode(code);
		if(pageNumber==""&&code==""){
			result.setSuccess(false);
			result.setData("不能为空！");
			return result;
		}
		if(credential==null){
			result.setSuccess(false);
			result.setData("凭证号错误！");
			return result;
		}
      if(credential.getUploadStatus()!=null&&!"".equals(credential.getUploadStatus())) {
		  if (!"OLD_DATA".equals(credential.getUploadStatus())) {
			  List<ConsignOrderAttachment> attachment = consignOrderAttachmentService.getAttachmentByConsignOrderId(credential.getId());
			  if (credential.getCredentialNum() == attachment.size() || credential.getCredentialNum() < attachment.size()) {
				  result.setSuccess(false);
				  result.setData("该凭证号已集齐 ！");
				  return result;
			  }

			  try {
				  Integer.parseInt(pageNumber);
			  } catch (NumberFormatException e) {
				  result.setSuccess(false);
				  result.setData("凭证当前页码只输入数字 ！");
				  return result;
			  }
			  if (credential.getCredentialNum() < Integer.parseInt(pageNumber)) {
				  result.setSuccess(false);
				  result.setData("凭证当前页码不能大于凭证总页码 ！");
				  return result;
			  }
			  if (Integer.parseInt(pageNumber) == 0) {
				  result.setSuccess(false);
				  result.setData("凭证当前页码不能为零 ！");
				  return result;
			  }
			  List<ConsignOrderAttachment> attachment1 = attachment.stream().filter(a -> !a.getPageNumber().toString().equals(pageNumber)).collect(Collectors.toList());
			  if (attachment1.size() < attachment.size()) {
				  result.setSuccess(false);
				  result.setData("凭证当前页码已重复 ！");
				  return result;
			  }
			  if (credential.getCredentialNum() == attachment.size() + 1) {

				  BusiConsignOrderCredential busiConsignOrderCredential = new BusiConsignOrderCredential();
				  busiConsignOrderCredential.setId(credential.getId());
				  busiConsignOrderCredential.setUploadStatus(Constant.CREDENTIAL_UPLOAD_STATUS.ALREADY_COLLECT.toString());
				  certificateService.updateByIdSelective(busiConsignOrderCredential);
			  }
		  }
	  }
		ConsignOrderAttachment consignOrderAttachment=new ConsignOrderAttachment();
		consignOrderAttachment.setId(Long.parseLong(id));
		consignOrderAttachment.setConsignOrderId(credential.getId());
		consignOrderAttachment.setPageNumber(Integer.parseInt(pageNumber));
		consignOrderAttachmentService.updateSelectiveById(consignOrderAttachment);
		
		result.setData("修改成功！");
		return result;
	}
	
	@ResponseBody
	@RequestMapping("blurcertificate.html")
	public Result blurCertificate(@RequestParam("code") String code){
		Result result = new Result();
		BusiConsignOrderCredential credential =certificateService.findByCredentialCode(code);
		if(credential==null){
			result.setSuccess(false);
			result.setData("凭证号不存在！");
			return result;
		}
		result.setData(credential.getCredentialNum());
		return result;
	}
	
	/**
	 * tuxianming  20150606
	 * 得到一个完整的Certificate对象，通过，Certificate id或者code
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("get/{id}.html")
	public Result getCertificate(@PathVariable String id){
		Result result = new Result();
		try {
			Long lid = Long.parseLong(id);
			BusiConsignOrderCredential credential =certificateService.findById(lid);
			if(credential==null){
				result.setSuccess(false);
				result.setData("凭证号不存在！");
			}else{
				result.setData(credential);
			}
			
		} catch (Exception e) {
			BusiConsignOrderCredential credential =certificateService.findByCredentialCode(id);
			if(credential==null){
				result.setSuccess(false);
				result.setData("凭证号不存在！");
			}else{
				result.setData(credential);
			}
		}
		return result;
	}

	/**
	 * wangxianjun  20160620
	 * 交易单实提数据为0，无法打印凭证,要删除该凭证，且清空交易单中关联的凭证号
	 * @param certCode 凭证号，isBatch 0 单个凭证 1 批量凭证
	 * @return
	 */
	@ResponseBody
	@RequestMapping("deleteCert.html")
	public Result getCertificate(@RequestParam("certCode") String certCode,@RequestParam("isBatch") String isBatch,@RequestParam("type") String type){
		Result result = new Result();
		try {

			BusiConsignOrderCredential credential =certificateService.findByCredentialCode(certCode);
			if(credential==null){
				result.setSuccess(false);
				result.setData("凭证号不存在！");
			}else{
			    //清空订单或订单明细凭证号
				certificateService.cleanOrdersByCertCode(credential.getId(), type, isBatch,certCode);
				result.setSuccess(true);
				result.setData("删除该凭证成功！");
			}

		} catch (Exception e) {
			result.setSuccess(false);
			result.setData(e.getMessage());
		}
		return result;
	}


	@RequestMapping(value="exportuploadcertificagelist.html")
	public ModelAndView exportUploadCertificagelist(OrderTradeCertificateQuery query){
		String accountType = query.getAccountType();
		List<OrderTradeCertificateForUploadDto> list = null;

		List<Long> ids = getUserIds();
		query.setUserIds(ids);
		if("管理员".equals(query.getOwnerName())){
			query.setOwnerName(null);
		}

		//excel表头
		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put("titles", buildTitlesList("凭证号", "创建凭证时间", "寄售交易单号", "开单时间","买家全称", "服务中心","钢为交易员", "卖家全称", "数量(件)"
				, "总重量(吨)", "实提总重量(吨)", "总金额（元）", "实提总金额（元）", "类型", "状态"));


		//表内容
		if(AccountType.seller.toString().equals(accountType)){
			//加载上传卖家凭证tab
			list = certificateService.loadcertificateforupload(query);
		}else{
			//加载上传买家凭证tab
			list = certificateService.loadcertificateforuploadbuyer(query);
		}

		//数据
		dataMap.put("varList",buildUploadCertificagelistPageDataList(list));//每项数据只能是string,否则不能导出

		ObjectExcelView erv = new ObjectExcelView();
		return new ModelAndView(erv, dataMap);
	}


	private List<String> buildTitlesList(String... titles) {
		List<String> titlesList = new ArrayList<>();
		if (titles != null) {
			for (String title : titles) {
				titlesList.add(title);
			}
		}
		return titlesList;
	}

	private List<PageData> buildUploadCertificagelistPageDataList(List<OrderTradeCertificateForUploadDto> list) {
		List<PageData> varList = new ArrayList<>();
		PageData vpd;
		if (list != null) {
			DecimalFormat amountFormat = new DecimalFormat("0.00");// 构造方法的字符格式这里如果小数不足2位,会以0补足.
			DecimalFormat weightFormat = new DecimalFormat("0.0000");// 构造方法的字符格式这里如果小数不足4位,会以0补足.
			String tempActualPickTotalWeight;
			String tempActualPickTotalAmount;
			for (OrderTradeCertificateForUploadDto dto : list) {
				//实提重量、金额不为0时 显示值，否则显示-
				if (BigDecimal.ZERO.compareTo(dto.getActualPickTotalWeight()) != 0){
					tempActualPickTotalWeight = weightFormat.format(dto.getActualPickTotalWeight());
				}else{
					tempActualPickTotalWeight = "-";
				}

				if (BigDecimal.ZERO.compareTo(dto.getActualPickTotalAmount()) != 0){
					tempActualPickTotalAmount = weightFormat.format(dto.getActualPickTotalAmount());
				}else{
					tempActualPickTotalAmount = "-";
				}

				vpd = new PageData();
				vpd.put("var1",dto.getCertificateNO()); //凭证号
				vpd.put("var2",dto.getCertificateCreated()); //凭证创建时间
				vpd.put("var3",dto.getCode()); //交易单号
				vpd.put("var4",dto.getOrderCreated()); //开单时间
				vpd.put("var5",dto.getBuyerName()); //买家全称
				vpd.put("var6",dto.getOrderOrgName()); //服务中心
				vpd.put("var7",dto.getOwnerName()); //交易员
				vpd.put("var8",dto.getSellerName()); //卖家全称
				vpd.put("var9",dto.getQuantity()+"");  //件数
				vpd.put("var10", weightFormat.format(dto.getTotalWeight())); //总重量
				vpd.put("var11",tempActualPickTotalWeight); //实提总重量
				vpd.put("var12", amountFormat.format(dto.getTotalAmount()));  //总金额
				vpd.put("var13",tempActualPickTotalAmount); //实提总金额
				vpd.put("var14",dto.getApprovalRequired() != null && dto.getApprovalRequired() ? "必须审核通过才能开票" : "不须审核通过也能开票" ); //类型
				vpd.put("var15",CertificateStatus.getNameByCode(dto.getStatus())); //状态
				varList.add(vpd);
			}
		}
		return varList;
	}

}
