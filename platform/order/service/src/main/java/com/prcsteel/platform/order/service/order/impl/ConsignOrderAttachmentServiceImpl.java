package com.prcsteel.platform.order.service.order.impl;

import java.util.List;

import com.prcsteel.platform.order.service.order.ConsignOrderAttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.order.model.dto.BatchUploadingDto;
import com.prcsteel.platform.order.model.model.BusiConsignOrderCredential;
import com.prcsteel.platform.order.model.model.ConsignOrderAttachment;
import com.prcsteel.platform.order.persist.dao.BusiConsignOrderCredentialDao;
import com.prcsteel.platform.order.persist.dao.ConsignOrderAttachmentDao;
import com.prcsteel.platform.order.service.order.ConsignOrderAttachmentService;

/**
 * Created by caochao on 2015/8/5.
 */
@Service("consignOrderAttachmentService")
public class ConsignOrderAttachmentServiceImpl implements ConsignOrderAttachmentService {
    @Autowired
    ConsignOrderAttachmentDao consignOrderAttachmentDao;
    @Autowired
    BusiConsignOrderCredentialDao busiConsignOrderCredentialDao;

    @Override
    public List<ConsignOrderAttachment> getAttachmentByContractId(Long contractId) {
        return consignOrderAttachmentDao.getAttachmentByContractId(contractId);
    }

	@Override
	public ConsignOrderAttachment queryById(Long id) {
		return consignOrderAttachmentDao.selectByPrimaryKey(id);
	}

	@Override
	public void updateSelectiveById(ConsignOrderAttachment attachment) {
		consignOrderAttachmentDao.updateByPrimaryKeySelective(attachment);
		
	}

	@Override
	public int totalByCredentialId(Long id) {
		return consignOrderAttachmentDao.totalByCredentialId(id);
	}

	@Override
	public void delete(List<Long> ids) {
		consignOrderAttachmentDao.delete(ids);
	}
    @Override
    public List<ConsignOrderAttachment> getAttachmentByConsignOrderId(Long consignOrderId) {
        return consignOrderAttachmentDao.getAttachmentByConsignOrderId(consignOrderId);
    }

	@Override
	public int deleteByPrimaryKey(Long id) {
		// TODO Auto-generated method stub
		return consignOrderAttachmentDao.deleteByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(List<ConsignOrderAttachment> record) {
		int index=0;
		for (ConsignOrderAttachment orderAttachment : record) {
			consignOrderAttachmentDao.updateByPrimaryKeySelective(orderAttachment);
			index++;
		}
		ConsignOrderAttachment attachment=consignOrderAttachmentDao.selectByPrimaryKey(record.get(0).getId());
		BatchUploadingDto param= busiConsignOrderCredentialDao.searchBatchUploadingById(attachment.getConsignOrderId());
		BusiConsignOrderCredential busiConsignOrderCredential=new BusiConsignOrderCredential();
		busiConsignOrderCredential.setId(attachment.getConsignOrderId());
		try {
			if(Integer.parseInt(param.getPages())==Integer.parseInt(param.getCredentialNum())){
				
				busiConsignOrderCredential.setUploadStatus(Constant.CREDENTIAL_UPLOAD_STATUS.ALREADY_COLLECT.toString());
				busiConsignOrderCredentialDao.updateByPrimaryKeySelective(busiConsignOrderCredential);
			}else if(Integer.parseInt(param.getPages())<Integer.parseInt(param.getCredentialNum())){
				busiConsignOrderCredential.setUploadStatus(Constant.CREDENTIAL_UPLOAD_STATUS.PENDING_COLLECT.toString());
				busiConsignOrderCredentialDao.updateByPrimaryKeySelective(busiConsignOrderCredential);
			}
		} catch (Exception e) {
			return index;
		}
		return index;
		
	}

	@Override
	public ConsignOrderAttachment getAttachmentById(Long consignOrderId) {
		// TODO Auto-generated method stub
		return consignOrderAttachmentDao.selectByPrimaryKey(consignOrderId);
	}

	@Override
	public List<ConsignOrderAttachment> findByCredentialIds(List<Long> ids) {
		return consignOrderAttachmentDao.selectCertAttacByConsignOrderIds(ids);
	}

}
