package com.prcsteel.platform.kuandao.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prcsteel.platform.kuandao.common.constant.KuandaoSequencePrefix;
import com.prcsteel.platform.kuandao.model.enums.KuandaoSequenceTypeEnum;
import com.prcsteel.platform.kuandao.model.enums.OperateTypeEnum;
import com.prcsteel.platform.kuandao.model.model.BillSequence;
import com.prcsteel.platform.kuandao.persist.dao.KuandaoSequenceDao;
import com.prcsteel.platform.kuandao.service.KuandaoSequenceService;

@Service
public class KuandaoSequenceServiceImpl implements KuandaoSequenceService {

    private static final String PREFIX = "0";
    
    private static final long SEQUENCE_ORGID = 0L;
    
    private static final String SEQUENCE_DATE = "0000";
    
    private static final long SEQUENCE_ACCOUNTID = 0L;
    
    private static final long SEQUENCE_USERID = 0L;
    
    private static final int SEQUENCE_INITVALUE = 1;

    private static final int SEQUENCE_MAX_PAYORDERCODE = 99999;
    @Autowired
    private KuandaoSequenceDao kuandaoSequenceDao;

    @Autowired
    private KuandaoSequencePrefix kuandaoSequencePrefix;
    
	@Override
	public synchronized String generateKuandaoAcctCode() {
		String type = KuandaoSequenceTypeEnum.accountCode.getType();
		String prefix = kuandaoSequencePrefix.getAccountCode();
		return generateKuandaoCode(type,prefix,8);
	}

	@Override
	public synchronized String generateKuandaoPayOrderCode(int occurType) {
		String type = KuandaoSequenceTypeEnum.payOrderCode.getType();
		String prefix = "";
		if(occurType == OperateTypeEnum.automatic.getType()) {
			prefix = kuandaoSequencePrefix.getPayOrderCode(); //系统匹配生成的需要加前缀1
		}
		return generateKuandaoCode(type,prefix,5);
	}
	
	private String generateKuandaoCode(String type,String prefix,int length){
		BillSequence billSequence = new BillSequence();
		billSequence.setOrgId(SEQUENCE_ORGID);
		billSequence.setSeqDate(SEQUENCE_DATE);
		billSequence.setSeqType(type);
		BillSequence sequence = query(billSequence);

        if (sequence == null) {
        	billSequence.setAccountId(SEQUENCE_ACCOUNTID);
        	billSequence.setUserId(SEQUENCE_USERID);
            billSequence.setCreated(new Date());
            billSequence.setCurrentValue(SEQUENCE_INITVALUE);

            insert(billSequence);
        } else {
        	if(KuandaoSequenceTypeEnum.payOrderCode.getType().equals(type) && sequence.getCurrentValue().compareTo(SEQUENCE_MAX_PAYORDERCODE) >= 0){//大于等于99999，重新开始
    			sequence.setCurrentValue(SEQUENCE_INITVALUE);
        	}
        	kuandaoSequenceDao.updateCurrentValueById(sequence.getId());
        	billSequence.setCurrentValue(sequence.getCurrentValue() + 1);
           
        }
        
        String seq = format(billSequence.getCurrentValue(), length);
		return prefix + seq;
	}
	
	@Override
	public String generateKuandaoRefundCode() {
		String type = KuandaoSequenceTypeEnum.refundCode.getType();
		String prefix = kuandaoSequencePrefix.getRefundCode();
		return generateKuandaoCode(type,prefix,6);
	}
	
	
	 private int insert(BillSequence billSequence) {
	        return kuandaoSequenceDao.insert(billSequence);
    }

    private BillSequence query(BillSequence billSequence) {
        return kuandaoSequenceDao.query(billSequence);
    }
    
    /**
     *
     * format number</br>
     *
     * <i>e.g.<i/> </br>
     *      format(2, 3) return "002"</br>
     *      format(12, 6) return "000012"
     * @param id number
     * @param length length of string
     * @return
     */
    private String format(Integer id, int length) {
        StringBuilder sb = new StringBuilder();

        int len = length-id.toString().length();
        for (int i = 0; i < len; i++) {
            sb.append(PREFIX);
        }
        sb.append(id.toString());

        return sb.toString();
    }

	
}
