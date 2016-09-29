package com.prcsteel.platform.order.service.order;

import java.util.List;
import java.util.Map;

import com.prcsteel.platform.acl.model.model.Organization;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.order.model.dto.*;
import com.prcsteel.platform.common.dto.ResultDto;
import com.prcsteel.platform.order.model.model.BusiConsignOrderCertificateInvoice;
import com.prcsteel.platform.order.model.model.BusiConsignOrderCredential;
import com.prcsteel.platform.order.model.query.BatchUploadingQuery;
import com.prcsteel.platform.order.model.query.CertificateInvoiceQuery;
import com.prcsteel.platform.order.model.query.OrderTradeCertificateQuery;
import com.prcsteel.platform.order.model.query.TradeCredentialQuery;


/**
 * 
    * @ClassName: CertificateService
    * @Description: 凭证服务
    * @author Green.Ge
    * @date 2016年4月8日
    *
 */
public interface CertificateService {
	BusiConsignOrderCredential findById(Long id);

	/**
	 * @Author: Green.Ge
	 * @Description: 获取上传凭证列表数据
	 * @Date: 2016年4月8日
	 */
	public List<OrderTradeCertificateForUploadDto> loadcertificateforupload(OrderTradeCertificateQuery query);

	/**
	 * @Author: Green.Ge
	 * @Description: 求上面方法查询的总条数，用于分页
	 * @Date: 2016年4月8日
	 */
    public Integer loadcertificateforuploadTotal(OrderTradeCertificateQuery query);

	/**
	 *
	 * @Author: wangxianjun
	 * @Description: 根据条件查询买家凭证，用于凭证上传界面，分页
	 * @Date: 2016年4月8日
	 */
	public List<OrderTradeCertificateForUploadDto> loadcertificateforuploadbuyer(OrderTradeCertificateQuery query);
	/**
	 *
	 * @Author: wangxianjun
	 * @Description: 取上面方法的总条数
	 * @Date: 2016年4月8日
	 */
	public Integer loadcertificateforuploadbuyerTotal(OrderTradeCertificateQuery query);
    
	public int save(BusiConsignOrderCredential credential);

	/**
	 * tuxianming
	 * 根据服务中心和当前月份查询最后一条
	 *
	 * @param map
	 * @return
	 */
	public String queryLastRecord(Map<String, String> map);

	//add by wangxianjun 通过凭证号更新打印信息
	public int updateByCertSelective(BusiConsignOrderCredential record);

	//add by wangxianjun 通过凭证号查询凭证信息
	public BusiConsignOrderCredential selectByCert(String cert);

	//add by wangxianjun 通过凭证id更新打印信息
	public int updateByIdSelective(BusiConsignOrderCredential record);

	/**
	 * @Author: Green.Ge
	 * @Description: 上传凭证模块中的订单信息列表
	 * @Date: 2016年4月11日
	 */
	List<OrderInfoForCertificateDto> selectOrdersByParamsForUploadAdd(OrderTradeCertificateQuery query);

	/**
	 * @Author: Green.Ge
	 * @Description: 在批量上传凭证页面删除一个订单
	 * @Date: 2016年4月11日
	 */
	void removeBatchCertificateNO(String accountType, Long accountId, Long orderId);

	/**
	 * @return 新生成的凭证编号
	 * @Author: Green.Ge
	 * @Description: 保存批量上传页面的订单列表
	 * @Date: 2016年4月12日
	 */
	Long saveBatchCertificateNO(User user,Organization org,String accountType,Long accountId,String certificateNO,String addArr,String delArr, String originArr );
	/**
	 * 
		* @Author: Green.Ge
	    * @Description: 生成凭证号
		* @Date: 2016年4月12日
	 */
	public String generateCertificateCode(String orgCode,String credentialType);
	/**
	 *
	 * @Author: wangxianjun
	 * @Description: 保存凭证信息
	 * @Date: 2016年4月12日
	 */
	public BusiConsignOrderCredential savePrintcateInfo(String certificateCode,OrderTradeCertificateQuery query,User user,String ip,int printNum, Integer credentialNum);
	/**
	 *
	 * @param printQRCode 
	 * @param credentialNum 
	 * @Author: wangxianjun
	 * @Description: 更新凭证信息
	 * @Date: 2016年4月12日
	 */
	public void updatePrintcateInfo(String certificateCode,User user,String ip, Boolean printQRCode, Integer credentialNum);
	//add by wangxianjun 凭证提交审核
	public ResultDto submitCertById(Long certId,User user,String status,String cause);
	//根据买家凭证号查询买家对应卖家凭证
	BusiConsignOrderCredential findSellerCertBybuyerCert(String cert);
	//根据买家凭证号查询买家对应卖家凭证
	BusiConsignOrderCredential findBuyerCertBySellerCert(String cert);

	List<ConsignOrderCredentialDto> queryChecklist(TradeCredentialQuery query);
	int queryTotalChecklist(TradeCredentialQuery query);

	/**
	 * @author peanut
	 * @description 需补齐买、卖家凭证的已开票订单job
	 * @date 2016/4/12
	 */
	void executeCertificateInvoiceJob();

	/**
	 * 根据查询对象检索数据
	 * @param query
	 * @author peanut
	 * @return
	 * @see CertificateInvoiceQuery
     */
	List<BusiConsignOrderCertificateInvoice> searchCertificateInvoice(CertificateInvoiceQuery query);
	
	/**
	 * 根据凭证号查询凭证单号
	 * tuxianming
	 * @param code
	 * @return
	 */
	List<BusiConsignOrderCredential> findByCode(String code);

	/**
	 * 根据一堆凭证号查询 凭证单号
	 * tuxianming 20160525
	 * @param credentialCodes
	 * @return
	 */
	List<BusiConsignOrderCredential> findByCodes(List<String> credentialCodes);


	/**
	 * 根据查询对象 查询 批量上传凭证图片 tab页
	 * @param query
	 * @author wangxiao
	 * @return
     */
	List<BatchUploadingDto> searchBatchUploading(BatchUploadingQuery query);
	List<BatchUploadingDto>  countBatchUploading(BatchUploadingQuery query);
	 BusiConsignOrderCredential findByCredentialCode(String code);

	/**
	 * 更新凭证号凭证条码: 将dto.updates里面的图片与凭证号进行绑定，并且更新凭证号页码状态
	 * 	将dto.removes里面的图片进行删除 
	 * tuxianming
	 * @return
	 */
	void updateCredentialImages(UpdateCredentialImageDto dto);

	/**
	 *
	 * @Author: wangxianjun
	 * @Description: 清空订单或订单明细凭证号
	 * @param certCode 凭证号
	 */
	void cleanOrdersByCertCode(Long id,String type,String isBatch,String certCode);
}

	
