package com.prcsteel.platform.order.persist.dao;

import java.util.List;

import com.prcsteel.platform.order.model.model.ConsignOrderContract;
import org.apache.ibatis.annotations.Param;

public interface ConsignOrderContractDao {
    int deleteByPrimaryKey(Long id);

    int insert(ConsignOrderContract record);

    int insertSelective(ConsignOrderContract record);

    ConsignOrderContract selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ConsignOrderContract record);

    int updateByPrimaryKey(ConsignOrderContract record);

    int countUnUploadByOrderId(Long orderId);
    /**
     * 根据订单号查询合同信息集
     * @param orderId
     * @return
     */
    List<ConsignOrderContract> queryByOrderId(Long orderId);

    /**
     * 根据订单id和卖家客户id查询合同信息
     * @param orderId
     * @param customerId
     * @return
     */
    ConsignOrderContract queryByOrderIdAndCustomerId(@Param("orderId")Long orderId,@Param("customerId")Long customerId);
    //通过订单id查询最新一笔合同号
    String queryMaxContract(Long orderId);
    /**
     * 根据变更订单号查询合同信息集
     * @param changeOrderId
     * @return
     */
    List<ConsignOrderContract>  queryByChangeOrderId(Integer changeOrderId);
    //根据订单id查询变更合同是否上传
    int countUnChangeUploadByOrderId(@Param("orderId")Long orderId,@Param("orderChangId")Integer orderChangId);
}