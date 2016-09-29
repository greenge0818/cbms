package com.prcsteel.platform.core.service;

import com.prcsteel.platform.core.model.model.BillSequence;

import java.util.Date;

/**
 * Created by rolyer on 15-7-20.
 */
public interface BillSequenceService {
	/**
	 *
	 * 生成合同号
	 *
	 * 合同号格式: 编号：类型-服务中心-编码-时间-流水号； 
	 * 当类型、服务中心、时间不变化时，即时编码变化，后面的流水号接着往下，不重新从001开始；
	 * 当类型、服务中心、时间任一变化时，流水号重新从001开始。 
	 * 前缀＋序号。 e.g. JS-HZ-000001-1506-300, JS-HZ-000001-1506-301, XS-HZ-000002-1506-301, DH-HZ-000003-1506-302
	 *
	 * @param billSequence
	 * @return
	 */
	public String generateSequence(BillSequence billSequence);

    /**
     *
     * 生成订单号
     *
     *  订单号格式： 前缀＋62进制。
     *      e.g. DD-HZ-000001-1506-02NaWL
     *
     * @param billSequence
     * @return
     */
    public String generateOrderCode(BillSequence billSequence, Long orderId);

    /**
     * 生成采购单号
     * @return
     */
    public String generatePurchaseOrderCode(Date date, Long minId, Long curId);
}
