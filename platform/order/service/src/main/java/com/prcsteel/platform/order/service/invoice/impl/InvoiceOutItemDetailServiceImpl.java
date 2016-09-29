package com.prcsteel.platform.order.service.invoice.impl;

import com.prcsteel.platform.order.model.dto.InvoiceOutApplyItemDetailDto;
import com.prcsteel.platform.order.model.dto.InvoiceOutOrderDetailDto;
import com.prcsteel.platform.order.model.model.ConsignOrderItems;
import com.prcsteel.platform.order.model.model.InvoiceOutItemDetail;
import com.prcsteel.platform.order.persist.dao.ConsignOrderItemsDao;
import com.prcsteel.platform.order.model.query.InvOutApplyItemDetailQuery;
import com.prcsteel.platform.order.persist.dao.InvoiceOutItemDetailDao;
import com.prcsteel.platform.order.service.invoice.InvoiceOutItemDetailService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lcw36 on 2015/9/15.
 */

@Service("invoiceOutItemDetailService")
@Transactional
public class InvoiceOutItemDetailServiceImpl implements InvoiceOutItemDetailService {

    @Resource
    private InvoiceOutItemDetailDao invoiceOutItemDetailDao;

    @Resource
    private ConsignOrderItemsDao consignOrderItemsDao;

    @Override
    public int deleteByPrimaryKey(Long id){
        return invoiceOutItemDetailDao.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(InvoiceOutItemDetail record){
        return invoiceOutItemDetailDao.insert(record);
    }

    @Override
    public int insertSelective(InvoiceOutItemDetail record){
        return invoiceOutItemDetailDao.insertSelective(record);
    }

    @Override
    public InvoiceOutItemDetail selectByPrimaryKey(Long id){
        return invoiceOutItemDetailDao.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(InvoiceOutItemDetail record){
        return invoiceOutItemDetailDao.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(InvoiceOutItemDetail record){
        return invoiceOutItemDetailDao.updateByPrimaryKey(record);
    }

    /**
     * 根据条件查询相关的销项票详情
     *
     * @param itemDetailQuery   查询参数
     * @return 进项票集合
     */
    @Override
    public List<InvoiceOutApplyItemDetailDto> query(InvOutApplyItemDetailQuery itemDetailQuery){
        return processCredential(invoiceOutItemDetailDao.query(itemDetailQuery));
    }

    public List<InvoiceOutApplyItemDetailDto> processCredential(List<InvoiceOutApplyItemDetailDto> list){
    	
    	String checkExpectValue = "1";
    	String approved = "APPROVED";
    	
    	for (InvoiceOutApplyItemDetailDto item : list) {
			
    		boolean buyercheck = false;
    		boolean sellercheck = false;
    		if(checkExpectValue.equals(item.getSellerCheckValue())){
    			if(approved.equals(item.getSellerCredentialStatus()) || approved.equals(item.getBatchSellerCredentialStatus()))
    				sellercheck = true;
    		}else{
    			sellercheck = true;
    		}
    		
    		if(checkExpectValue.equals(item.getBuyerCheckValue())){
    			if(approved.equals(item.getBuyerCredentialStatus()) || approved.equals(item.getBatchBuyerCredentialStatus()))
    				buyercheck = true;
    		}else
    			buyercheck = true;
    		
    		if(sellercheck && buyercheck)
    			item.setCredentialStatusStr("审核通过");
    		else{
    			item.setCredentialStatusStr("审核未通过");
    		}
    		
    		item.setCredentialStatus(sellercheck && buyercheck);
		}
    	
    	return list;
    }
    
    /**
     * 查询开票总金额
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 开票金额
     */
    @Override
    public BigDecimal querySumAmount(String startTime, String endTime){
        return invoiceOutItemDetailDao.querySumAmount(startTime, endTime);
    }
    @Override
    public BigDecimal queryApplyWaitAmount(String startTime, String endTime){
    	return invoiceOutItemDetailDao.queryApplyWaitAmount(startTime, endTime);
    }

    @Override
    public List<InvoiceOutOrderDetailDto> queryInvoiceOutOrderDetailByInvoiceOutApplyId(Long invoiceOutApplyId) {
        List<InvoiceOutOrderDetailDto> list = invoiceOutItemDetailDao.queryInvoiceOutOrderDetailByInvoiceOutApplyId(invoiceOutApplyId);

        List<InvoiceOutOrderDetailDto> results = new ArrayList<>();
        // 数据重组
        for (InvoiceOutOrderDetailDto i : list){
            if(duplicate(i.getContractCode(), results)){
                for (InvoiceOutOrderDetailDto result : results) {
                    if(result.getContractCode().equals(i.getContractCode())){
                        result.getItems().add(i);
                        break;
                    }
                }
            } else {
                InvoiceOutOrderDetailDto dto = new InvoiceOutOrderDetailDto();
                dto.setContractCode(i.getContractCode());
                dto.setCreated(i.getCreated());
                dto.setTotalAmount(i.getTotalAmount());
                dto.setOrderId(i.getOrderId());

                List<InvoiceOutOrderDetailDto> items = new ArrayList<>();
                items.add(i);
                dto.setItems(items);

                results.add(dto);
            }
        }

        //计算实提总重量和实提金额
        for (InvoiceOutOrderDetailDto result : results) {
            BigDecimal totalActualPickWeight = BigDecimal.ZERO;   //实提总重量
            BigDecimal totalActualPickAmount = BigDecimal.ZERO;   //实提总金额

            List<ConsignOrderItems> consignOrderItems = consignOrderItemsDao.selectByOrderId(result.getOrderId());
            for (ConsignOrderItems i : consignOrderItems){
                totalActualPickWeight = totalActualPickWeight.add(i.getActualPickWeightServer()!=null?i.getActualPickWeightServer():BigDecimal.ZERO);

                totalActualPickAmount = totalActualPickAmount.add(i.getDealPrice().multiply(i.getActualPickWeightServer()));
            }
            result.setTotalActualPickWeight(totalActualPickWeight);
            result.setTotalActualPickAmount(totalActualPickAmount);
        }

        return results;
    }

    /**
     * 检查重复项
     * @param code
     * @param items
     * @return
     */
    private boolean duplicate(String code,  List<InvoiceOutOrderDetailDto> items){
        for (InvoiceOutOrderDetailDto i : items){
            if (code.equals(i.getContractCode())) {
                return true;
            }
        }

        return false;
    }

    /**
     * 暂缓认证
     *
     * @param ids     查询参数
     * @param isDefer
     * @return int
     */
    @Override
    public int updateIsDefer(List<Long> ids, int isDefer) {
        return invoiceOutItemDetailDao.updateIsDeferForIds(ids, isDefer);
    }

    /**
     * 根据申请Id查询相关详情的修改次数
     *
     * @param applyIds 申请Id集合
     * @return 销项票详情集合
     */
    @Override
    public List<InvoiceOutItemDetail> queryModifyNumByApplyIds(List<Long> applyIds) {
        return invoiceOutItemDetailDao.queryModifyNumByApplyIds(applyIds);
    }
}
