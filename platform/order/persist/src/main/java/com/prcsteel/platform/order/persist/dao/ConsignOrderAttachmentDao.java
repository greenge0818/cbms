package com.prcsteel.platform.order.persist.dao;

import java.util.List;
import java.util.Map;

import com.prcsteel.platform.order.model.model.ConsignOrderAttachment;
import org.apache.ibatis.annotations.Param;

public interface ConsignOrderAttachmentDao {
    int deleteByPrimaryKey(Long id);

    int insert(ConsignOrderAttachment record);

    int insertSelective(ConsignOrderAttachment record);

    ConsignOrderAttachment selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ConsignOrderAttachment record);

    int updateByPrimaryKey(ConsignOrderAttachment record);

    int countByContractId(Long contractId);

    List<ConsignOrderAttachment> getAttachmentByContractId(Long contractId);

    //通过订单ID查询二结后上传图片
    List<ConsignOrderAttachment> getAttachmentByOrderId(Map map);
    //通过path删除图片
    int deleteByPath(Map map);
    //根据提货单号和图片url查询图片
    int selectCountByPath(Map map);

    /**
     * 根据consignOrderId集查询附件对象集
     * @param orderIds
     * @author peanut
     * @date 2016/04/12
     * @return
     */
    List<ConsignOrderAttachment> selectCertAttacByConsignOrderIds(@Param("list") List<Long> orderIds);

    /**
	 * tuxianming 20160525
     * 根据 凭证id 统计 即：consign_order_id 字段
     * @param id
     * @return
     */
	int totalByCredentialId(Long id);

	void delete(List<Long> ids);

    
    List<ConsignOrderAttachment> getAttachmentByConsignOrderId(Long consignOrderId);
    
    ConsignOrderAttachment selectByConsignOrderId(Long consignOrderId);









}