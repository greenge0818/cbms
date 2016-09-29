package com.prcsteel.platform.order.persist.dao;

import java.util.Date;
import java.util.List;

import com.prcsteel.platform.order.model.dto.InvoiceOutApplyDto;
import org.apache.ibatis.annotations.Param;

import com.prcsteel.platform.order.model.dto.InvoiceOutItemDetailDto;
import com.prcsteel.platform.order.model.dto.OutApplyMainDto;
import com.prcsteel.platform.order.model.model.InvoiceOutItemDetail;

public interface OutApplyMainDao {

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
	public List<OutApplyMainDto> queryByApplyId(
			@Param("orgName") String orgName,
			@Param("buyerName") String buyerName,
			@Param("start") Integer start, @Param("length") Integer length);

	/**
	 * 查询销项待开票总记录数
	 * 
	 * @param orgName
	 *            服务中心名
	 * @param buyerName
	 *            客户名
	 * @return
	 */
	public Integer queryByApplyIdCount(@Param("orgName") String orgName,
			@Param("buyerName") String buyerName);

	/**
	 * 查询品规及表所有数据 导出excel表格数据专用
	 * 
	 * @param dateStart
	 *            起始时间
	 * @param dateEnd
	 *            终止时间
	 * @return
	 */
	public List<InvoiceOutItemDetailDto> queryAllDetail(
			@Param("dateStart") Date dateStart, @Param("dateEnd") Date dateEnd);

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
	public List<InvoiceOutItemDetailDto> selectByOutMainId(
			@Param("outMainId") Long outMainId,
			@Param("orgName") String orgName,
			@Param("buyerName") String buyerName,
			@Param("start") Integer start, @Param("length") Integer length,
			@Param("dateStart") Date dateStart, @Param("dateEnd") Date dateEnd);

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
	public Integer selectByOutMainIdCount(@Param("outMainId") Long outMainId,
			@Param("orgName") String orgName,
			@Param("buyerName") String buyerName,
			@Param("dateStart") Date dateStart, @Param("dateEnd") Date dateEnd);
	
	/**
	 * 导出EXCEL表格后修改记录时间
	 * @param invoiceOutItemDetail
	 * @return
	 */
	public int updateDate(InvoiceOutItemDetail invoiceOutItemDetail);

	/**
	 * 查询待生成的开票清单
	 * @param orgIds
	 * @param status
	 * @param start
	 * @param length
	 * @return
	 */
	public List<InvoiceOutApplyDto> selectWaitInvoice(@Param("orgIds") List<String> orgIds,
													   @Param("statusList") List<String> statusList,
													   @Param("start") Integer start,
													   @Param("length") Integer length);

	/**
	 * 查询待生成的开票清单总数
	 * @param orgIds
	 * @param status
	 * @return
	 */
	public int totalWaitInvoice(@Param("orgIds") List<String> orgIds, @Param("statusList") List<String> statusList);
}
