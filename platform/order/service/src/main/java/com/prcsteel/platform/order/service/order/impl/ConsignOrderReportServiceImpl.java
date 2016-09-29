package com.prcsteel.platform.order.service.order.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.prcsteel.platform.order.model.dto.ConsignOrderDetailReportDto;
import com.prcsteel.platform.order.model.dto.ConsignOrderDetailsCombinationDto;
import com.prcsteel.platform.order.model.dto.ConsignOrderDetailsDto;
import com.prcsteel.platform.account.model.dto.TraderInfoDto;
import com.prcsteel.platform.order.model.query.ConsignOrderDetailQuery;
import com.prcsteel.platform.account.persist.dao.AccountDao;
import com.prcsteel.platform.order.persist.dao.ConsignOrderReportDao;
import com.prcsteel.platform.order.service.order.ConsignOrderReportService;

/**
 * 
 * @author zhoukun
 */
@Service("consignOrderReportService")
public class ConsignOrderReportServiceImpl implements ConsignOrderReportService {

	private static final Logger logger = LoggerFactory.getLogger(ConsignOrderReportServiceImpl.class);
	
	@Resource
	ConsignOrderReportDao consignOrderReportDao;
	
	@Resource
	AccountDao accountDao;
	
	@Override
	public ConsignOrderDetailReportDto queryOrderDetailReport(ConsignOrderDetailQuery query) {
		ConsignOrderDetailReportDto res = new ConsignOrderDetailReportDto();
		Long totalRows = consignOrderReportDao.countOrderDetail(query);
		if(totalRows == null || totalRows == 0){
			return res;
		}
		List<ConsignOrderDetailsDto> list = consignOrderReportDao.queryOrderDetail(query);
		// 查询所有卖家交易员姓名
		Set<Long> ids = new HashSet<Long>();
		for (ConsignOrderDetailsDto cod : list) {
			ids.add(cod.getSellerId());
		}
		List<TraderInfoDto> traderList = accountDao.selectManagerInfoByIds(Arrays.asList(ids.toArray(new Long[0])));
		Map<Long, TraderInfoDto> traderMap = new HashMap<Long, TraderInfoDto>();
		for (TraderInfoDto traderInfoDto : traderList) {
			traderMap.put(traderInfoDto.getId(), traderInfoDto);
		}
		// 组合数据
		List<ConsignOrderDetailsCombinationDto> listData = new LinkedList<ConsignOrderDetailsCombinationDto>();
		for (ConsignOrderDetailsDto c : list) {
			ConsignOrderDetailsCombinationDto p = new ConsignOrderDetailsCombinationDto();
			try {
				BeanUtils.copyProperties(p, c);
				if(traderMap.containsKey(c.getSellerId())){
					p.setSellerTrader(traderMap.get(c.getSellerId()).getName());
				}
				listData.add(p);
			} catch (Exception e) {
				logger.error("copy ConsignOrderDetailsCombinationPojo properties failed!",e);
			}
		}
		res.setListData(listData);
		res.setQuery(query);
		res.setTotalRows(totalRows);
		return res;
	}

}
