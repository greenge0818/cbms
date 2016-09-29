package com.prcsteel.platform.order.service.order;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.prcsteel.platform.common.dto.ResultDto;
import org.springframework.web.multipart.MultipartFile;

import com.prcsteel.platform.order.model.model.PickupBill;
import com.prcsteel.platform.order.model.model.PickupItems;
import com.prcsteel.platform.order.model.model.PickupPerson;
import com.prcsteel.platform.acl.model.model.User;

/**
 * Created by Green.Ge on 2015/7/27.
 */
public interface PickupBillService {
    //查待开单表体Green.Ge
  	List<HashMap<String, Object>> selectBillDetail(Long orderId);
  	//保存提货单
  	Map<String,Object> save(PickupBill pb,List<MultipartFile> attachmentList,List<PickupItems> detail, List<PickupPerson> persons,User user,String isBillBuyercert,String certCode,String certName,List<MultipartFile> sellerttachmentList,String sellerCertCode,String isBillsellercert,String sellerCertName);

  	PickupBill selectByPrimaryKey(Long id);
  	
  	List<HashMap<String,Object>> selectByOrderId(Long orderId);
  	
  	int disableBill(Long id,User user);
  	
  	//获取提货单打印数据
  	HashMap<String,Object> getBillPrintInfoById(Long id);
  	
  //打印提货单
  	void printPickupBill(Long id,User user);

	/**
	 * 上传买家提单图片
	 * modify by wangxianjun 20160229
	 *
	 * @return
	 */

	 ResultDto updateBillPic(List<MultipartFile> attachmentfiles, User user,Long billId,String billType);

	/**
	 * 上传放货单图片
	 * modify by wangxianjun 20160229
	 *
	 * @return
	 */

	ResultDto updateDeliveryBillPic(List<MultipartFile> attachmentfiles, User user,Long billId);

	/**
	 * 删除买家提单图片
	 * modify by wangxianjun 20160229
	 *
	 * @return
	 */
	 ResultDto deletePic(Long imgId,Long pbId);
	//通过订单ID查找提货单
	List<PickupBill> selectPickupsByOrderId(Long orderId);

}
