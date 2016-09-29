package com.prcsteel.platform.order.service.invoice;

import java.util.Date;
import java.util.List;

import com.prcsteel.platform.order.model.dto.InvoiceOutApplyDto;
import com.prcsteel.platform.order.model.dto.InvoiceOutItemDetailDto;
import com.prcsteel.platform.order.model.dto.OutApplyMainDto;

/**
 * Created by lixiang on 2015/8/4.
 */
public interface OutApplyMainService {

	/**
	 * 查询销项待开票
	 * 
	 * @param orgName
	 *            服务中心名
	 * @param buyerName
	 *            客户名
	 * @param start
	 *            起始页
	 * @param length
	 *            每页记录数
	 * @return
	 */
	public List<OutApplyMainDto> findByOrgApplyId(String orgName,
			String buyerName, Integer start, Integer length);

	/**
	 * 查询销项待开票总记录数
	 *
	 * @param orgName
	 *            服务中心名
	 * @param buyerName
	 *            客户名
	 * @return
	 */
	public int findByOrgApplyIdCount(String orgName, String buyerName);

	/**
	 * 查询品规及表所有数据 导出excel表格数据专用
	 * 
	 * @param dateStart
	 *            起始时间
	 * @param dateEnd
	 *            终止时间
	 * @return
	 */
	public List<InvoiceOutItemDetailDto> findAllDetail(Date dateStart,
			Date dateEnd);

	/**
	 * 根据发票编号查询当前买家的开票计算结果
	 * 
	 * @param outMainId
	 *            发票编号ID
	 * @param orgName
	 *            服务中心名
	 * @param buyerName
	 *            买家全称
	 * @param start
	 *            起始页
	 * @param length
	 *            记录数
	 * @param dateStart
	 *            起始时间
	 * @param dateEnd
	 *            终止时间
	 * @return
	 */
	public List<InvoiceOutItemDetailDto> findByOutMainId(Long outMainId,
			String orgName, String buyerName, Integer start, Integer length,
			Date dateStart, Date dateEnd);

	/**
	 * 根据发票编号查询当前买家的开票计算结果记录数
	 * 
	 * @param outMainId
	 *            发票编号ID
	 * @param orgName
	 *            服务中心名
	 * @param buyerName
	 *            买家全称
	 * @param dateStart
	 *            起始时间
	 * @param dateEnd
	 *            终止时间
	 * @return
	 */
	public int findByOutMainIdCount(Long outMainId, String orgName,
			String buyerName, Date dateStart, Date dateEnd);


	/**
	 * 查询待生成的开票清单
	 * @param orgIds
	 * @param status
	 * @param start
	 * @param length
	 * @return
	 */
	public List<InvoiceOutApplyDto> queryWaitInvoice(List<String> orgIds, List<String> statusList2, Integer start, Integer length);

	/**
	 * 查询待生成的开票清单总数
	 * @param orgIds
	 * @param status
	 * @return
	 */
	public int totalWaitInvoice(List<String> orgIds, List<String> statusList);

}
