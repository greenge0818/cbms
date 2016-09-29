package com.prcsteel.platform.order.web.controller.order;

import com.prcsteel.platform.acl.model.model.Organization;
import com.prcsteel.platform.acl.service.OrganizationService;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.enums.OpType;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.common.service.OpLog;
import com.prcsteel.platform.common.service.OpParam;
import com.prcsteel.platform.common.utils.FileUtil;
import com.prcsteel.platform.common.vo.Result;
import com.prcsteel.platform.order.model.enums.DeliveryOrderType;
import com.prcsteel.platform.order.model.enums.PickupType;
import com.prcsteel.platform.order.model.model.BusiConsignOrderCredential;
import com.prcsteel.platform.order.model.model.ConsignOrder;
import com.prcsteel.platform.order.model.model.ConsignOrderAttachment;
import com.prcsteel.platform.order.model.model.ConsignOrderItems;
import com.prcsteel.platform.order.model.model.DeliveryBill;
import com.prcsteel.platform.order.model.model.PickupBill;
import com.prcsteel.platform.order.model.model.PickupItems;
import com.prcsteel.platform.order.model.model.PickupPerson;
import com.prcsteel.platform.order.service.order.CertificateService;
import com.prcsteel.platform.order.service.order.ConsignOrderItemsService;
import com.prcsteel.platform.order.service.order.ConsignOrderService;
import com.prcsteel.platform.order.service.order.DeliveryBillService;
import com.prcsteel.platform.order.service.order.PickupBillService;
import com.prcsteel.platform.order.service.order.PickupItemService;
import com.prcsteel.platform.order.service.order.PickupPersonService;
import com.prcsteel.platform.order.web.controller.BaseController;
import com.prcsteel.platform.order.web.support.ShiroVelocity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Green.Ge on 2015/7/21.
 * 提货单处理
 */
@Controller
@RequestMapping("/order/pickup")
public class PickUpBillController extends BaseController {

    ShiroVelocity permissionLimit = new ShiroVelocity();
    @Resource
    ConsignOrderService cos;
    @Resource
    ConsignOrderItemsService cois;
    @Resource
    PickupBillService pbs;
    @Resource
    PickupItemService pis;
    @Resource
    PickupPersonService pps;
    @Resource
    DeliveryBillService dbs;
    @Resource
    DeliveryBillService deliveryBillService;
    @Resource
    OrganizationService organizationService;
    @Resource
    CertificateService certificateService;
    //打开新建提货单界面
    @RequestMapping("/order/{orderId}/create")
    public String create(ModelMap out,@PathVariable Long orderId) {
        ConsignOrder co = cos.selectByPrimaryKey(orderId);
        if(!"4".equals(co.getStatus())){
        	sendError(out, "该订单尚未到开提货单阶段");
        }
    	out.put("order", co);
        List<HashMap<String, Object>> detail = pbs.selectBillDetail(orderId);
        out.put("detail", detail);
        out.put("pickupTypes", PickupType.values());
        out.put("certNames", cos.findCertnames(orderId, "buyer"));
        out.put("sellerCertNames", cos.findCertnames(orderId, "seller"));

        // 只查看融资订单权限（页面只能查看，不能操作，隐藏所有按钮）
        if(permissionLimit.hasPermission(Constant.PERMISSION_FINANCE_ORDER)){
            out.put("financeorder", true);
        }
        
        return "order/pickup/create";
    }

    //保存提货单
    @OpLog(OpType.SavePickup)
    @OpParam("pb")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> save(MultipartHttpServletRequest req ,HttpServletRequest request, ModelMap out, PickupBill pb) {
    	
    	Map<String,Object> result = new HashMap<String, Object>();
    	if(pb.getId()!=null&&dbs.getPrintedDeliveryBillCountByPickupId(pb.getId())>0){
    		result.put("success", false);
    		result.put("data", "当前提货单已有关联打印过的放货单，不允许修改");
    		return result;
    	}
    	//获取买家提单是否作为买家凭证 0 否 1是
    	String isBillBuyercert = request.getParameter("isBuyercert");
    	String certName = request.getParameter("certName");

        //获取卖家提单是否作为卖家凭证 0 否 1是
        String isBillsellercert = request.getParameter("isSellercert");
        String sellerCertName = request.getParameter("sellerCertName");
    	//构建明细
        //代运营明细ID
        String consignOrderItemId[] = request.getParameterValues("consignOrderItemId");
        //提货件数
        String pickQuantity[] = request.getParameterValues("pickQuantity");
        //已提货件数
        String pickedQuantity[] = request.getParameterValues("pickedQuantity");
        List<PickupItems> detail = new ArrayList<PickupItems>();
        if(pickQuantity!=null){
        	for (int i = 0; i < pickQuantity.length; i++) {
            	//提货数量大于0才存库
            	if(Integer.valueOf(pickQuantity[i])>0){
            		PickupItems pi = new PickupItems();
                    pi.setCreatedBy(getLoginUser().getLoginId());
                    pi.setLastUpdatedBy(getLoginUser().getLoginId());
                    pi.setConsignOrderItemId(Long.valueOf(consignOrderItemId[i]));
                    pi.setPickedQuantity(Integer.valueOf(pickedQuantity[i]));
                    pi.setPickQuantity(Integer.valueOf(pickQuantity[i]));
                    detail.add(pi);
            	}
            }
        }
        
        if(pb.getId()==null&&detail.isEmpty()){
        	result.put("success",false);
        	result.put("data", "提货件数不能都为0");
        	return result;
        }
        //买家提单
        List<MultipartFile> orderPicList = req
                .getFiles("file");
        List<MultipartFile> attachmentList = new ArrayList<MultipartFile>();

        if (pb.getId()==null && orderPicList != null && !orderPicList.isEmpty()) {
            for (MultipartFile file : orderPicList) {
                if (!checkUploadAttachment(file, result, attachmentList)) {
                    return result;
                }
            }

        }
        //卖家提单
        List<MultipartFile> sellerPicList = req
                .getFiles("seller-cert-file");
        List<MultipartFile> sellerAttachmentList = new ArrayList<MultipartFile>();
        if (pb.getId()==null && sellerPicList != null && !sellerPicList.isEmpty()) {
            for (MultipartFile file : sellerPicList) {
                if (!checkUploadAttachment(file, result, sellerAttachmentList)) {
                    return result;
                }
            }

        }
     /*   if(file!=null&&!file.isEmpty()){
        	String suffix = FileUtil.getFileSuffix(file.getOriginalFilename());
        	if(!Constant.IMAGE_SUFFIX.contains(suffix.toLowerCase())){
        		result.put("success", false);
        		result.put("data", "买家提单文件格式不正确");
            	return result;
        	}
        	if(file.getSize()/Constant.M_SIZE>Constant.MAX_IMG_SIZE){
        		result.put("success", false);
        		result.put("data", "买家提单文件超过"+Constant.MAX_IMG_SIZE+"M");
            	return result;
        	}
        }*/
        //构建提货人信息
        String validCode[] = request.getParameterValues("validCode");
        String name[] = request.getParameterValues("name");
        String mobil[] = request.getParameterValues("mobil");
        String idcardNumber[] = request.getParameterValues("idcardNumber");
        List<PickupPerson> persons = new ArrayList<PickupPerson>();
        for (int i = 0; i < validCode.length; i++) {
            PickupPerson pp = new PickupPerson();
            pp.setCreatedBy(getLoginUser().getLoginId());
            pp.setLastUpdatedBy(getLoginUser().getLoginId());
            pp.setValidCode(validCode[i]);
            pp.setName(name[i]);
            pp.setMobil(mobil[i]);
            if( idcardNumber != null && !"".equals(idcardNumber[0]) && idcardNumber.length>0)
            pp.setIdcardNumber(idcardNumber[i]);
            persons.add(pp);
        }
        String sellerCertCode = "";
        String certificateCode = "";
        Organization org = organizationService.queryById(getLoginUser().getOrgId());
        if("1".equals(isBillBuyercert))
            certificateCode = certificateService.generateCertificateCode(org.getSeqCode(), "buyer");
        if("1".equals(isBillsellercert))
            sellerCertCode = certificateService.generateCertificateCode(org.getSeqCode(), "seller");
        try{
        	result =  pbs.save(pb, attachmentList, detail, persons,getLoginUser(),isBillBuyercert,certificateCode,certName,sellerAttachmentList,sellerCertCode,isBillsellercert,sellerCertName);
        }catch(BusinessException e){
        	result.put("success", false);
        	result.put("data",e.getMsg());
        	return result;
        }
        
        return result;
    }
    private boolean checkUploadAttachment(MultipartFile file, Map<String,Object> result,List<MultipartFile> attachmentList) {
        String suffix = FileUtil.getFileSuffix(file.getOriginalFilename());

        if (suffix == null
                || !Constant.IMAGE_SUFFIX.contains(suffix.toLowerCase())) {
            result.put("success", false);
            result.put("data", "提单文件格式不正确");
            return false;
        }
        if (file.getSize() / Constant.M_SIZE > Constant.MAX_IMG_SIZE) {
            result.put("success", false);
            result.put("data", "提单超过" + Constant.MAX_IMG_SIZE + "M");
            return false;
        }
        attachmentList.add(file);
        return true;
    }
    
    //编辑提货单界面
    @RequestMapping("/{id}/edit")
    public String edit(ModelMap out,@PathVariable Long id) {
        PickupBill pb = pbs.selectByPrimaryKey(id);
        
        if(pb==null){
        	return "redirect:/404.html";
        }
        boolean IamPickupBillCreator = pb.getCreatedBy().equals(getLoginUser().getLoginId());
        boolean IamOrderOwner = cos.selectByPrimaryKey(pb.getConsignOrderId()).getOwnerId().equals(getLoginUser().getLoginId());
        if(IamPickupBillCreator||IamOrderOwner){
        	out.put("canEditDetail", true);
        }
        //通过提单ID查询买家提单
        Map map = new HashMap();
        map.put("orderId",pb.getId());
        map.put("type","ladbill");
        List<ConsignOrderAttachment> attachments = cos.getAttachmentByOrderId(map);
        if (attachments != null) {
            out.put("attachments", attachments);
        }
        //通过提单ID查询卖家提单
        map.put("type","sellerladbill");
        List<ConsignOrderAttachment> sellerAttachments = cos.getAttachmentByOrderId(map);
        if (sellerAttachments != null) {
            out.put("sellerAttachments", sellerAttachments);
        }
        List<HashMap<String, Object>> detail = pis.selectByBillIdForEdit(id);
        out.put("pb", pb);
        out.put("detail", detail);
        out.put("pickupTypes", PickupType.values());
        out.put("persons", pps.selectByBillId(id));
        //判断当前订单是否存在买家凭证号
        ConsignOrder order = cos.selectByPrimaryKey(pb.getConsignOrderId());
        out.put("certNames", cos.findCertnames(pb.getConsignOrderId(), "buyer"));
        out.put("sellerCertNames", cos.findCertnames(pb.getConsignOrderId(), "seller"));
        if(!"".equals(order.getBuyerCredentialCode()) &&  order.getBuyerCredentialCode() != null) {
			BusiConsignOrderCredential cert =certificateService.selectByCert(order.getBuyerCredentialCode());
			if(cert != null){
				out.put("cName", cert.getName());
				out.put("isBillBuyercert", cert.getIsBillBuyercert());
			}
		}
        //判断当前订单是否存在卖家凭证号
        ConsignOrderItems item = cois.selectByOrderId(order.getId()).get(0);
        if(!"".equals(item.getSellerCredentialCode()) && item.getSellerCredentialCode() != null) {
            BusiConsignOrderCredential sellerCert =certificateService.selectByCert(item.getSellerCredentialCode());
            if(sellerCert != null){
                out.put("cSellerName", sellerCert.getName());
                out.put("isBillSellercert", sellerCert.getIsBillBuyercert());
            }
        }
        return "order/pickup/edit";
    }
  //查看提货单 单个
    @RequestMapping("/{id}")
    public String view(ModelMap out,@PathVariable Long id) {
    	
        PickupBill pb = pbs.selectByPrimaryKey(id);
        if(pb==null){
        	return "redirect:/404.html";
        }
        List<HashMap<String, Object>> detail = pis.selectByBillIdForEdit(id);
        out.put("pb", pb);
        out.put("detail", detail);
        out.put("pickupTypes", PickupType.values());
        out.put("persons", pps.selectByBillId(id));
        return "order/pickup/view";
    }
  //查看提货单 按订单
    @RequestMapping("/order/{orderId}")
    public String viewByOrderId(ModelMap out,@PathVariable Long orderId) {
    	
    	ConsignOrder order = cos.selectByPrimaryKey(orderId);
    	out.put("order", order);
        List<HashMap<String,Object>> list = pbs.selectByOrderId(orderId);
        out.put("list", list);
        return "order/pickup/list";
    }
    
  //删除提货单，打标志逻辑删除
    @OpLog(OpType.DeletePickup)
    @OpParam("id")
    @RequestMapping("/{id}/delete")
    @ResponseBody
    public Result delete(ModelMap out,@PathVariable Long id) {
    	Result result = new Result();
    	int i=0;
    	try{
    		i = pbs.disableBill(id,getLoginUser());
    	}catch(BusinessException e){
    		result.setSuccess(false);
    		result.setData(e.getMsg());
    		return result;
    	}
    	
    	if(i==1){
    		result.setSuccess(true);
    		result.setData("删除成功");
    		if(pbs.selectByOrderId(pbs.selectByPrimaryKey(id).getConsignOrderId()).isEmpty()){
    			result.setData("clear");
    		}
    		return result;
    	}
    	result.setData("保存失败");
    	return result;
    }
    
    //进入放货单列表页面，根据提货单ID
    @RequestMapping("/{pickupId}/delivery")
    public String deliveryListByPickupId(ModelMap out,@PathVariable Long pickupId) {
    	
    	List<HashMap<String,Object>> deliveryList = dbs.getBillByPickupId(pickupId);
    	out.put("deliveryList", deliveryList);
    	ConsignOrder order = cos.selectByPrimaryKey(pbs.selectByPrimaryKey(pickupId).getConsignOrderId());
    	out.put("order", order);
    	return "order/pickup/delivery/list";
    }
    
  //进入放货单列表页面，根据订单ID
    @RequestMapping("/order/{orderId}/delivery")
    public String deliveryListByOrderId(ModelMap out,@PathVariable Long orderId) {
    	
    	List<HashMap<String,Object>> deliveryList = dbs.getBillByOrderId(orderId, null);
    	out.put("deliveryList", deliveryList);
    	ConsignOrder order = cos.selectByPrimaryKey(orderId);
    	out.put("order", order);
    	return "order/pickup/delivery/list";
    }
    //变更放货单界面
    @RequestMapping("/delivery/{id}/edit")
    public String editDelivery(ModelMap out,@PathVariable Long id) {
    	
        DeliveryBill bill = deliveryBillService.selectById(id);
//        if(bill==null){
//            return "redirect:/404.html";
//        }
        //通过放货单ID查询图片
        Map billMap = new HashMap();
        billMap.put("orderId",id);
        billMap.put("type","deliverybill");
        List<ConsignOrderAttachment> attachments = cos.getAttachmentByOrderId(billMap);
        if (attachments != null) {
            out.put("attachments", attachments);
        }
        out.put("bill", bill);
        out.put("pickupTypes", PickupType.values());
        out.put("items", deliveryBillService.selectByBillIdForEdit(id));
        out.put("persons", deliveryBillService.selectPickupPersonByPickupBillId(bill.getPickupId()));
        return "order/pickup/delivery/edit";
    }

    //变更放货单
    @OpLog(OpType.SaveDelivery)
    @OpParam("db")
    @RequestMapping(value = "/delivery/save", method = RequestMethod.POST)
    @ResponseBody
    public Result save(HttpServletRequest request, DeliveryBill db, MultipartFile file) {
        Result result = new Result();

        if(db.getId()==null){
            result.setSuccess(false);
            result.setData("没有该放货单");
            return result;
        }
        //构建提货人信息
        String validCode[] = request.getParameterValues("validCode");
        String name[] = request.getParameterValues("name");
        String mobil[] = request.getParameterValues("mobil");
        String idcardNumber[] = request.getParameterValues("idcardNumber");
        List<PickupPerson> persons = new ArrayList<PickupPerson>();
        for (int i = 0; i < validCode.length; i++) {
            PickupPerson pp = new PickupPerson();
            pp.setCreatedBy(getLoginUser().getLoginId());
            pp.setLastUpdatedBy(getLoginUser().getLoginId());
            pp.setValidCode(validCode[i]);
            pp.setName(name[i]);
            pp.setMobil(mobil[i]);
            if( idcardNumber != null && !"".equals(idcardNumber[0]) && idcardNumber.length>0)
                pp.setIdcardNumber(idcardNumber[i]);
            persons.add(pp);
        }
        try{
        	if(deliveryBillService.save(db, persons, file, getLoginUser())){
                result.setSuccess(true);
                result.setData("保存成功");
            }else{
                result.setSuccess(false);
                result.setData("保存失败");
            }
        }catch(BusinessException e){
        	result.setSuccess(false);
        	result.setData(e.getMsg());
        	return result;
        }
        
        return result;
    }
    
    //进入打印提货单页面
    @RequestMapping("/{id}/showprint")
    public String showPrintPickup(ModelMap out,@PathVariable Long id) {
    	
    	HashMap<String, Object> printInfo = pbs.getBillPrintInfoById(id);
    	out.put("printInfo", printInfo);
    	return "order/pickup/print";
    }
    //进入打印放货单页面
    @RequestMapping("/delivery/order/{orderId}/showprint")
    public String showPrintDelivery(ModelMap out,@PathVariable Long orderId,Long deliveryId, String delivery) {
        String url="order/pickup/delivery/print";    //默认为放货函
        delivery = delivery==null? DeliveryOrderType.DELIVERYLETTER.toString():delivery;
       
        List<HashMap<String,Object>> printInfo = dbs.getBillByOrderId(orderId, delivery);
        if(delivery.equals(DeliveryOrderType.DELIVERYORDER.toString()) 
        		|| delivery.equals(DeliveryOrderType.TRANSFERORDER.toString())
        		|| delivery.equals(DeliveryOrderType.PICKUPFORBUYER.toString())) {
            for (HashMap<String, Object> p : printInfo) {
                ArrayList<HashMap<String, Object>> map = (ArrayList) p.get("detail");
                HashMap<String, ArrayList<HashMap<String, Object>>> detailList = new HashMap<>();
                for (HashMap<String, Object> detail : map) {
                    String warehouse = (String) detail.get("warehouse");
                    if (detailList.get(warehouse) == null) {
                        ArrayList<HashMap<String, Object>> temp = new ArrayList<HashMap<String, Object>>();
                        temp.add(detail);
                        detailList.put(warehouse, temp);
                    } else {
                        ArrayList<HashMap<String, Object>> temp = detailList.get(warehouse);
                        temp.add(detail);
                        detailList.put(warehouse, temp);
                    }
                }
                p.put("details", detailList);
            }
        }
        
        out.put("printInfo", printInfo);
        out.put("deliveryId", deliveryId);
        out.put("orderId", orderId);
        out.put("order", cos.selectByPrimaryKey(orderId));

        // 只查看融资订单权限（页面只能查看，不能操作，隐藏所有按钮）
        if(permissionLimit.hasPermission(Constant.PERMISSION_FINANCE_ORDER)){
            out.put("financeorder", true);
        }

        List<String> settings = organizationService.selectDeliverySettingByOrgId(getLoginUser().getOrgId());
        out.put("settings", settings);
        if(!settings.isEmpty() && settings.contains(delivery)){
            if(delivery.equals(DeliveryOrderType.DELIVERYLETTER.toString())){
                url = "order/pickup/delivery/print";
            }else if(delivery.equals(DeliveryOrderType.DELIVERYORDER.toString())){
                url = "order/pickup/delivery/printDeliveryOrder";
            }else if(delivery.equals(DeliveryOrderType.TRANSFERORDER.toString())){
                url = "order/pickup/delivery/printTransferOrder";
            }else if(delivery.equals(DeliveryOrderType.TRANSFERLETTER.toString())){
                url = "order/pickup/delivery/printTransferLetter";
            }else if(delivery.equals(DeliveryOrderType.PICKUPFORBUYER.toString())){
            	//TODO: 生成条形码图片
            	

            	//掌柜热线：动态获取, 在head里面， key值为：telphone
                url = "order/pickup/delivery/printDeliveryOrderForBuyer";
            }
        }
        return url;
    }
    //打印提货单
    @OpLog(OpType.PrintPickup)
    @OpParam("id")
    @RequestMapping("/{id}/print")
    @ResponseBody
    public void printPickup(ModelMap out,@PathVariable Long id) {
    	pbs.printPickupBill(id, getLoginUser());
    }
    //打印放货单
    @OpLog(OpType.PrintDelivery)
    @OpParam("db")
    @RequestMapping("/delivery/{id}/{deliveryType}/print")
    @ResponseBody
    public Result printDelivery(ModelMap out,@PathVariable Long id, @PathVariable String deliveryType) {
    	Result result = new Result();
    	DeliveryBill db = dbs.selectById(id);
    	if(db==null){
    		result.setData("提货单发生变化，重新加载后请重试");
    	}else{
    		dbs.printDelivery(id, deliveryType, getLoginUser());
    		result.setSuccess(true);
    	}
    	return result;
    }
    
    //已提重量
    @RequestMapping("/orderitem/{itemId}/pickedweight")
    @ResponseBody
    public String pickedweight(ModelMap out,@PathVariable Long itemId) {
//    	System.out.println();
    	return pis.selectPickedWeightByOrderItemId(itemId).toString();
    }

    /**
     * 上传买家提单图片
     * modify by wangxianjun 20160229
     *
     * @return
     */
    @RequestMapping("uploadPic.html")
    @ResponseBody
    public Result uploadPic(MultipartHttpServletRequest req,HttpServletRequest request) {
        Result result = new Result();
        //String checkResult;
        Long billId = null;
        String billType = "";
        boolean isSave = true;//上传是否成功
        List<MultipartFile> orderPicList = null;
        try {
            billId = Long.valueOf(request.getParameter("id"));
            billType = request.getParameter("billType");
            if("sellerladbill".equals(billType)) {
                //卖家提单
                orderPicList = req
                        .getFiles("seller-cert-file");
            }else{
                orderPicList = req
                        .getFiles("file");
            }
            List<MultipartFile> attachmentFiles = new ArrayList<MultipartFile>();
            if (orderPicList != null && !orderPicList.isEmpty()) {
                for (MultipartFile file : orderPicList) {
                    if (!checkUpAttachment(file, result, attachmentFiles,billType)) {
                        return result;
                    }
                }

            }
                    if("ladbill".equals(billType)  || "sellerladbill".equals(billType)) {
                        //上传买家提单或卖家提单 billType 这个为ladbill 买家提单  sellerladbill 卖家提单
                        isSave = pbs.updateBillPic(attachmentFiles, getLoginUser(), billId,billType).isSuccess();
                    }else{
                        //上传放货单

                        isSave = pbs.updateDeliveryBillPic(attachmentFiles, getLoginUser(), billId).isSuccess();
                    }
            if(isSave){
                Map billMap = new HashMap();
                billMap.put("orderId",billId);
                billMap.put("type",billType);
                result.setData(cos.getAttachmentByOrderId(billMap));
                result.setSuccess(true);

            }else{
                result.setData("提单上传失败！");
                result.setSuccess(false);
            }

        } catch (BusinessException e) {
            result.setData(e.getMsg());
            result.setSuccess(false);
        }
        return result;
    }


    private boolean checkUpAttachment(MultipartFile file, Result result,List<MultipartFile> attachmentList,String billType) {
        String suffix = FileUtil.getFileSuffix(file.getOriginalFilename());
        String msg = "";

        if (suffix == null
                || !Constant.IMAGE_SUFFIX.contains(suffix.toLowerCase())) {
            if("ladbill".equals(billType) || "sellerladbill".equals(billType)) {
                //上传买家提单
                msg = "提单图片格式不正确！";
            }else{
                //上传放货单
                msg = "放货单图片格式不正确！";
            }
            result.setData(msg);
            result.setSuccess(false);
            return false;
        }
        if (file.getSize() / Constant.M_SIZE > Constant.MAX_IMG_SIZE) {
            if("ladbill".equals(billType) || "sellerladbill".equals(billType)) {
                //上传买家提单
                msg = "提单超过" + Constant.MAX_IMG_SIZE + "M";
            }else{
                //上传放货单
                msg = "放货单超过" + Constant.MAX_IMG_SIZE + "M";
            }
            result.setData(msg);
            result.setSuccess(false);
            return false;
        }
        attachmentList.add(file);
        return true;
    }

    /**
     * 删除二结后订单详情页面的回单图片
     * modify by wangxianjun 20160229
     *
     * @return
     */
    @RequestMapping("deletePic.html")
    @ResponseBody
    public Result deletePic(@RequestParam("imgId") Long imgId,@RequestParam("pbId") Long pbId,@RequestParam("bType") String billType) {
        Result result = new Result();
        boolean isSave = true;//删除是否成功
        try {
            if("ladbill".equals(billType) || "sellerladbill".equals(billType)) {
                //删除买家提单或卖家提单
                 isSave = pbs.deletePic(imgId,pbId).isSuccess();
            }else{
                //删除放货单
                isSave =cos.deletePic(imgId).isSuccess();
            }

            if (isSave) {
                Map billMap = new HashMap();
                billMap.put("orderId",pbId);
                billMap.put("type",billType);
                result.setData(cos.getAttachmentByOrderId(billMap));
                result.setSuccess(true);

            }
        } catch (BusinessException e) {
            result.setData(e.getMsg());
            result.setSuccess(false);
        }
        return result;
    }
    @RequestMapping(value = "pickupattachmentview.html", method = RequestMethod.GET)
    public void queryPickupAttachments(ModelMap out, @RequestParam("orderid") Long orderId) {
        List<ConsignOrderAttachment> attachmentList = new ArrayList<ConsignOrderAttachment>();
        if (orderId != 0) {
            List<PickupBill> pickupBills = pbs.selectPickupsByOrderId(orderId);
            if(pickupBills.size() > 0) {
                Map billMap = new HashMap();
                billMap.put("orderId",pickupBills.get(0).getId());
                billMap.put("type","ladbill");
                attachmentList = cos.getAttachmentByOrderId(billMap);
                out.put("attachmentlist", attachmentList);
            }
        }
        if(attachmentList.size() == 0){
            out.put("msg", "当前订单没有上传买家提单！");
        }
    }
}
