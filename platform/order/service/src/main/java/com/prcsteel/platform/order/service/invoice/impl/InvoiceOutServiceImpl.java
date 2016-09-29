package com.prcsteel.platform.order.service.invoice.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.prcsteel.platform.order.service.invoice.InvoiceOutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prcsteel.platform.order.model.dto.ConfirmedInvoiceOutDto;
import com.prcsteel.platform.order.model.dto.EnsureInvoiceOutDto;
import com.prcsteel.platform.order.model.dto.InvoiceOutProcessCountDto;
import com.prcsteel.platform.order.model.enums.InvoiceOutMainStatus;
import com.prcsteel.platform.order.model.enums.InvoiceOutStatus;
import com.prcsteel.platform.order.model.enums.PrintStatus;
import com.prcsteel.platform.order.model.model.Express;
import com.prcsteel.platform.order.model.model.InvoiceOut;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.order.persist.dao.ExpressDao;
import com.prcsteel.platform.order.persist.dao.InvoiceOutDao;
import com.prcsteel.platform.order.persist.dao.InvoiceOutMainDao;

/**
 * Created by Rabbit Mao on 2015/8/3.
 */

@Service("invoiceOutService")
@Transactional
public class InvoiceOutServiceImpl implements InvoiceOutService {
    @Autowired
    InvoiceOutDao outDao;
    @Autowired
    ExpressDao expressDao;
    @Autowired
    InvoiceOutMainDao invoiceOutMainDao;
    @Autowired
    private InvoiceOutDao invoiceOutDao;

    @Override
    public InvoiceOutProcessCountDto getTabCount(List<Long> userIds){
        InvoiceOutProcessCountDto countDto = new InvoiceOutProcessCountDto();
        Map<String, Object> param = new HashMap<>();

        param.put("userIds", userIds);
        //待寄出
        param.put("status", InvoiceOutStatus.INPUT.getCode());
        countDto.setExpress(invoiceOutDao.countByParams(param));

        //待确认
        param.replace("status", InvoiceOutStatus.SENT.getCode());
        countDto.setConfirm(invoiceOutDao.countByParams(param));

        //已确认,待打印
        param.replace("status", InvoiceOutStatus.CONFIRMED_SUCCESS.getCode());
        param.put("printStatus", PrintStatus.NOPRINT.ordinal());
        countDto.setConfirmed(invoiceOutDao.countByParams(param));

        return countDto;
    }

    public List<EnsureInvoiceOutDto> listDataForShow(Map<String, Object> param) {
        List<EnsureInvoiceOutDto> dtos = new ArrayList<EnsureInvoiceOutDto>();
        List<InvoiceOut> outs = outDao.selectByParams(param);
        for (InvoiceOut out : outs) {
            EnsureInvoiceOutDto dto = new EnsureInvoiceOutDto();
            dto.setExpress(expressDao.selectByPrimaryKey(out.getExpressId()));
            dto.setInvoiceOut(out);
            dtos.add(dto);
        }
        return dtos;
    }

    public Integer getDataCountByParam(Map<String, Object> param) {
        return outDao.countByParams(param);
    }

    public synchronized boolean confirm(String invoiceOutCode, BigDecimal invoiceOutAmount, User user) {
        InvoiceOut invoiceOut = outDao.selectByCode(invoiceOutCode);
        if (invoiceOut != null && invoiceOutAmount.doubleValue() == invoiceOut.getAmount().doubleValue()) {
            invoiceOut.setAmount(invoiceOutAmount);
            invoiceOut.setCode(invoiceOutCode);
            invoiceOut.setStatus(InvoiceOutStatus.CONFIRMED_SUCCESS.getCode());   //确认通过
            invoiceOut.setCheckAmount(invoiceOutAmount);
            invoiceOut.setCheckUserId(user.getId());
            invoiceOut.setCheckUserName(user.getName());
            invoiceOut.setCheckDate(new Date());
            invoiceOut.setLastUpdated(new Date());
            invoiceOut.setLastUpdatedBy(user.getLoginId());
            invoiceOut.setModificationNumber(invoiceOut.getModificationNumber() + 1);
            if (outDao.updateByPrimaryKeySelective(invoiceOut) > 0) {
                return true;
            }
        }
        return false;
    }


    /**
     * 保存财务录入的发票信息并更新状态为财务已录入
     *
     * @param user        操作的用户
     * @param invoiceList 发票列表
     * @param outMainIds  待更新状态的ids
     * @return
     */
    @Override
    public boolean saveInvoice(User user, List<InvoiceOut> invoiceList, Long[] outMainIds) {
        if (invoiceList != null && !invoiceList.isEmpty()) {
            Integer modificationNumber = 0;

            //保存发票
            for (InvoiceOut invoice : invoiceList) {
                //业务
                invoice.setInputUserId(user.getId());
                invoice.setInputUserMobil(user.getTel());
                invoice.setInputUserName(user.getName());
                invoice.setInvoiceDate(new Date());//发票日期
                invoice.setStatus(InvoiceOutStatus.INPUT.getCode());//待寄出

                //系统
                invoice.setCreated(new Date());
                invoice.setCreatedBy(user.getName());
                invoice.setLastUpdated(new Date());
                invoice.setLastUpdatedBy(user.getName());
                invoice.setModificationNumber(modificationNumber);

                Integer addResult = outDao.insertSelective(invoice);
                if (addResult == 0) {
                    return false;
                }
            }

            //更新发票状态为财务已开票
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("status", InvoiceOutMainStatus.INPUT.getCode());
            paramMap.put("user", user);
            paramMap.put("array", outMainIds);
            paramMap.put("time", new Date());
            return invoiceOutMainDao.updateStatusByIds(paramMap) > 0;
        }
        return false;
    }

    @Override
    public synchronized List<ConfirmedInvoiceOutDto> getPrintTableData(List<Long> ids, User user){
        List<ConfirmedInvoiceOutDto> dtos = outDao.selectByIds(ids);
        for(ConfirmedInvoiceOutDto dto : dtos){
            InvoiceOut out = outDao.selectByPrimaryKey(dto.getId());
            out.setPrintStatus("1");
            out.setLastUpdated(new Date());
            out.setLastUpdatedBy(user.getLoginId());
            out.setModificationNumber(out.getModificationNumber() + 1);
            outDao.updateByPrimaryKeySelective(out);
        }
        return dtos;
    }

    @Override
    public int updateExpressIdById(Long id, Long expressId) {
        return invoiceOutDao.updateExpressIdById(id, expressId);
    }

    @Override
    public List<Express> query(Long orgId, String buyerName, String startTime, String endTime) {
        return invoiceOutDao.query(orgId, buyerName, startTime, endTime);
    }
}

