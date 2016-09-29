package com.prcsteel.platform.order.persist.dao;

import java.util.List;
import java.util.Map;

import com.prcsteel.platform.order.model.dto.BatchUploadingDto;
import com.prcsteel.platform.order.model.dto.ConsignOrderCredentialDto;
import com.prcsteel.platform.order.model.dto.OrderInfoForCertificateDto;
import com.prcsteel.platform.order.model.dto.OrderTradeCertificateForUploadDto;
import com.prcsteel.platform.order.model.model.BusiConsignOrderCredential;
import com.prcsteel.platform.order.model.query.BatchUploadingQuery;
import com.prcsteel.platform.order.model.query.OrderTradeCertificateQuery;
import com.prcsteel.platform.order.model.query.TradeCredentialQuery;

public interface BusiConsignOrderCredentialDao {
    int deleteByPrimaryKey(Long id);

    int insert(BusiConsignOrderCredential record);

    int insertSelective(BusiConsignOrderCredential record);

    BusiConsignOrderCredential selectByPrimaryKey(Long id);
    
    BusiConsignOrderCredential  selectByPrimaryCredentialCode(String code);

    int updateByPrimaryKeySelective(BusiConsignOrderCredential record);

    int updateByPrimaryKey(BusiConsignOrderCredential record);
    /**
     * 
    	* @Author: Green.Ge
        * @Description: 根据条件查询凭证，用于凭证上传界面，分页
    	* @Date: 2016年4月8日
     */
    List<OrderTradeCertificateForUploadDto> loadcertificateforupload(OrderTradeCertificateQuery query);
    /**
     * 
    	* @Author: Green.Ge
        * @Description: 取上面方法的总条数
    	* @Date: 2016年4月8日
     */
    Integer loadcertificateforuploadTotal(OrderTradeCertificateQuery query);

    /**
     *
     * @Author: wangxianjun
     * @Description: 根据条件查询买家凭证，用于凭证上传界面，分页
     * @Date: 2016年4月8日
     */
    List<OrderTradeCertificateForUploadDto> loadcertificateforuploadbuyer(OrderTradeCertificateQuery query);
    /**
     *
     * @Author: wangxianjun
     * @Description: 取上面方法的总条数
     * @Date: 2016年4月8日
     */
    Integer loadcertificateforuploadbuyerTotal(OrderTradeCertificateQuery query);


    /**
	 * tuxianming
	 * 根据服务中心和当前月份查询最后一条
	 * @param map
	 * @return
	 */
	String queryLastRecord(Map<String, String> map);

    //add by wangxianjun 通过凭证号更新打印信息
    int  updateByCertSelective(BusiConsignOrderCredential record);
    //add by wangxianjun 通过凭证号查询凭证信息
    BusiConsignOrderCredential selectByCert(String cert);
    /**
     * 
    	* @Author: Green.Ge
        * @Description: 上传凭证模块中的订单信息列表
    	* @Date: 2016年4月11日
     */
    List<OrderInfoForCertificateDto> selectOrdersByParamsForUploadAdd(OrderTradeCertificateQuery query);

    /**
     *
     * @Author: wangxianjun
     * @Description: 上传买家凭证模块中的订单信息列表
     * @Date: 2016年4月11日
     */
    List<OrderInfoForCertificateDto> selectOrdersByBuyerParamsForUploadAdd(OrderTradeCertificateQuery query);

	List<ConsignOrderCredentialDto> queryChecklist(TradeCredentialQuery query);

	int queryTotalChecklist(TradeCredentialQuery query);
    /**
     * 根据凭证号列表查询出凭证信息集
     * @param list 凭证号列表
     * @author peanut
     * @date 2016/04/12
     * @return
     */
    List<BusiConsignOrderCredential> selectByCertList(List<String> list);
    
    /**
   	 * 根据查询对象 查询 批量上传凭证图片 tab页
   	 * @param query
   	 * @author wangxiao
        */
   	List<BatchUploadingDto> searchBatchUploading(BatchUploadingQuery query);
   	
   	List<BatchUploadingDto> countBatchUploading(BatchUploadingQuery query);
   	
   BatchUploadingDto searchBatchUploadingById(Long id);
}