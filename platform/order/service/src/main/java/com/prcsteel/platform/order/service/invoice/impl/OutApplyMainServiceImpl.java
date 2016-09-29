package com.prcsteel.platform.order.service.invoice.impl;

import java.util.Date;
import java.util.List;

import com.prcsteel.platform.order.model.dto.InvoiceOutApplyDto;
import com.prcsteel.platform.order.service.invoice.OutApplyMainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prcsteel.platform.order.model.dto.InvoiceOutItemDetailDto;
import com.prcsteel.platform.order.model.dto.OutApplyMainDto;
import com.prcsteel.platform.order.persist.dao.OutApplyMainDao;

/**
 * Created by lixiang on 2015/8/4.
 */

@Service("outApplyMainService")
public class OutApplyMainServiceImpl implements OutApplyMainService {

	@Autowired
	private OutApplyMainDao outApplyMainDao;

	@Override
	public List<OutApplyMainDto> findByOrgApplyId(String orgName,
												  String buyerName, Integer start, Integer length) {
		List<OutApplyMainDto> list = outApplyMainDao.queryByApplyId(orgName,
				buyerName, start, length);
		return list;
	}

	@Override
	public int findByOrgApplyIdCount(String orgName, String buyerName) {
		return outApplyMainDao.queryByApplyIdCount(orgName, buyerName);
	}

	@Override
	public List<InvoiceOutItemDetailDto> findAllDetail(Date dateStart,
													   Date dateEnd) {
		List<InvoiceOutItemDetailDto> list = outApplyMainDao.queryAllDetail(
				dateStart, dateEnd);
		return list;
	}

	@Override
	public List<InvoiceOutItemDetailDto> findByOutMainId(Long outMainId,
														 String orgName, String buyerName, Integer start, Integer length,
														 Date dateStart, Date dateEnd) {
		List<InvoiceOutItemDetailDto> list = outApplyMainDao.selectByOutMainId(
				outMainId, orgName, buyerName, start, length, dateStart,
				dateEnd);
		return list;
	}

	@Override
	public int findByOutMainIdCount(Long outMainId, String orgName,
									String buyerName, Date dateStart, Date dateEnd) {
		return outApplyMainDao.selectByOutMainIdCount(outMainId, orgName,
				buyerName, dateStart, dateEnd);
	}

	/**
	 * 查询待生成的开票清单
	 * @param orgIds
	 * @param statusList
	 * @param start
	 * @param length
	 * @return
	 */
	public List<InvoiceOutApplyDto> queryWaitInvoice(List<String> orgIds, List<String> statusList, Integer start, Integer length){
		return outApplyMainDao.selectWaitInvoice(orgIds, statusList, start, length);
	}

	/**
	 * 查询待生成的开票清单总数
	 * @param orgIds
	 * @param status
	 * @return
	 */
	public int totalWaitInvoice(List<String> orgIds, List<String> statusList){
		return outApplyMainDao.totalWaitInvoice(orgIds,statusList);
	}

}
