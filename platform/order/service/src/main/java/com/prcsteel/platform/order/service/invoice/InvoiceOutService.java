package com.prcsteel.platform.order.service.invoice;

import com.prcsteel.platform.order.model.dto.ConfirmedInvoiceOutDto;
import com.prcsteel.platform.order.model.dto.EnsureInvoiceOutDto;
import com.prcsteel.platform.order.model.dto.InvoiceOutProcessCountDto;
import com.prcsteel.platform.order.model.model.Express;
import com.prcsteel.platform.order.model.model.InvoiceOut;
import com.prcsteel.platform.acl.model.model.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by Rabbit Mao on 2015/8/3.
 */
public interface InvoiceOutService {
    List<EnsureInvoiceOutDto> listDataForShow(Map<String, Object> param);
    boolean confirm(String invoiceOutCode, BigDecimal invoiceOutAmount, User user);

    InvoiceOutProcessCountDto getTabCount(List<Long> userIds);
    /**
     * 保存财务录入的发票信息并更新状态为财务已录入
     * @param user 操作的用户
     * @param invoiceList 发票列表
     * @param outMainIds 待更新状态的ids
     * @return
     */
    boolean saveInvoice(User user,List<InvoiceOut> invoiceList,Long[] outMainIds);


    List<ConfirmedInvoiceOutDto> getPrintTableData(List<Long> ids, User user);

    Integer getDataCountByParam(Map<String, Object> param);

    /**
     * 关联快递信息
     * @param id
     * @param expressId
     * @return
     */
    public int updateExpressIdById(Long id, Long expressId);

    /**
     * 获取发票列表
     * @param orgId　服务中心编号
     * @param buyerName　公司名称
     * @return
     */
    public List<Express> query(Long orgId, String buyerName, String startTime, String endTime);
}

