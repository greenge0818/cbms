package com.prcsteel.platform.order.service.order;

import java.util.List;

import com.prcsteel.platform.order.model.model.BusiConsignOrderCredential;
import com.prcsteel.platform.order.model.model.ConsignOrderAttachment;

/**
 * Created by caochao on 2015/8/5.
 */
public interface ConsignOrderAttachmentService {
    List<ConsignOrderAttachment> getAttachmentByContractId(Long contractId);
    //green.ge added for app api on 2015.9.8
    ConsignOrderAttachment queryById(Long id);
    
    /**
     * tuxianming 
     * 20160525
     * @param attachment
     */
    void updateSelectiveById(ConsignOrderAttachment attachment);
    
    /**
     * ¸ù¾ÝÆ¾Ö¤ºÅidÀ´Í³¼ÆÍ¼Æ¬×ÜÊý tuxianming 20160525
     * @param id
     * @return
     */
	int totalByCredentialId(Long id);
	
	/**
	 * É¾³ý¼ÇÂ¼ 
	 * tuxianming 20160525
	 * @param ids
	 */
	public void delete(List<Long> ids);
    
	 ConsignOrderAttachment getAttachmentById(Long consignOrderId);
    
    List<ConsignOrderAttachment> getAttachmentByConsignOrderId(Long consignOrderId);
    
    int deleteByPrimaryKey(Long id);
    
    int  updateByPrimaryKeySelective(List<ConsignOrderAttachment> record);
    
    /**
     * tuxianming20150531
     * 根据凭证id查询所有的附件
     * @param credentialCodes
     * @return
     */
	List<ConsignOrderAttachment> findByCredentialIds(List<Long> id);


}
