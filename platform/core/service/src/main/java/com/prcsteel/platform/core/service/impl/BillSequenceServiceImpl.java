package com.prcsteel.platform.core.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prcsteel.platform.acl.model.model.Organization;
import com.prcsteel.platform.acl.persist.dao.OrganizationDao;
import com.prcsteel.platform.core.model.enums.BillType;
import com.prcsteel.platform.core.model.model.BillSequence;
import com.prcsteel.platform.core.persist.dao.BillSequenceDao;
import com.prcsteel.platform.core.service.BillSequenceService;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by rolyer on 15-7-20.
 */
@Service("billSequenceService")
public class BillSequenceServiceImpl implements BillSequenceService {

    private static final String SYMBOL = "-";
    private static final String FIRST_SEQUENCE = "1";
    private static final String PREFIX = "0";

    private static final String DATE_FORMAT_YYMM = "yyMM";
    private static final int SEQUENCE_SIZE = 4; //序号长度大小
	private static final String DATE_FORMAT_YYMMDD = "yyyyMMdd";    
	private static final int SEQUENCE_PURCHASE_SIZE = 4; //序号长度大小
    private static final int SEQUENCE_ACCOUNT_ID_SIZE = 6; //客户号长度大小

    @Autowired
    private BillSequenceDao billSequenceDao;

    @Autowired
    private OrganizationDao organizationDao;

    @Transactional
    public String generateSequence(BillSequence billSequence) {
        return generate(billSequence, null);
    }

    @Transactional
    public String generateOrderCode(BillSequence billSequence, Long orderId) {
        return generate(billSequence, orderId);
    }

    @Transactional
    public String generatePurchaseOrderCode(Date date, Long minId, Long curId) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_YYMMDD);
        String seqDate = sdf.format(date);
        Integer count = BigDecimal.valueOf(curId).subtract(BigDecimal.valueOf(minId)).intValue() + 1;
        String seq = format(count, SEQUENCE_PURCHASE_SIZE);

        String str = BillType.CG.toString() + seqDate + seq;

        return str;
    }

    private int insert(BillSequence billSequence) {
        return billSequenceDao.insert(billSequence);
    }

    private BillSequence query(BillSequence billSequence) {
        return billSequenceDao.query(billSequence);
    }

    private String findSeqCodeByOrgId(Long id) {

        Organization org = organizationDao.queryById(id);
        return org.getSeqCode();
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

    private String generate(BillSequence billSequence, Long orderId) {
        SimpleDateFormat sdf=new SimpleDateFormat(DATE_FORMAT_YYMM);
        String seqDate = sdf.format(new Date());

        billSequence.setSeqDate(seqDate);

        String seq = format(Integer.parseInt(FIRST_SEQUENCE), SEQUENCE_SIZE);
        if( orderId != null ) { //生成订单号
            seq = convertDecimalBase62(orderId, 6);
        } else { //生成合同号
            BillSequence sequence = query(billSequence);

            if (sequence == null || sequence.getId() == null && sequence.getId().intValue() <= 0) {
                billSequence.setCreated(new Date());
                billSequence.setCurrentValue(1);

                insert(billSequence);
            } else {
            	billSequenceDao.updateCurrentValueById(sequence.getId());

                Integer i = (sequence.getCurrentValue().intValue() + 1);
                seq = format(i, SEQUENCE_SIZE);
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append(billSequence.getSeqType());
        sb.append(SYMBOL);
        sb.append(findSeqCodeByOrgId(billSequence.getOrgId()));
        sb.append(SYMBOL);
        sb.append(format(billSequence.getAccountId().intValue(), SEQUENCE_ACCOUNT_ID_SIZE));
        sb.append(SYMBOL);
        sb.append(seqDate);
        sb.append(SYMBOL);
        sb.append(seq);

        return sb.toString();
    }

    private static char[] charSet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();

    /**
     *
     * 将10进制转化为62进制
     *
     * @param number 十进制数值
     * @param length 转化成的62进制后，字符长度不足length长度时高位补0，否则不改变什么。
     * @return
     */
    private String convertDecimalBase62(Long number, int length){
        char[] res = new char[length];
        Arrays.fill(res, '0');

        for (int i = res.length-1; i >=0; i--) {
            res[i] = charSet[(int) (number % charSet.length)];
            number /= charSet.length;
        }

        if(number > 0){
            throw new RuntimeException("code overflow");
        }

        return String.valueOf(res);
    }

}
