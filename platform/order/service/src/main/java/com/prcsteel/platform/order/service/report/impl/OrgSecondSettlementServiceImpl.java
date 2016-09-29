package com.prcsteel.platform.order.service.report.impl;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.prcsteel.platform.order.service.report.OrgSecondSettlementService;
import org.springframework.stereotype.Service;

import com.prcsteel.platform.order.model.dto.OrgListDto;
import com.prcsteel.platform.order.model.dto.OrgSecondSettlementDto;
import com.prcsteel.platform.order.model.dto.OrganizationsDto;
import com.prcsteel.platform.account.model.enums.AccountTransApplyType;
import com.prcsteel.platform.order.model.query.OrgSecondSettlementQuery;
import com.prcsteel.platform.order.persist.dao.OrgSecondSettlementDao;

/**
 * @author lixiang
 * @version V1.1
 * @Title: OrgSecondSettlementServiceImpl
 * @Package com.prcsteel.platform.order.service.impl.report
 * @Description: 服务中心二次结算储备金日报
 * @date 2015/8/26
 */

@Service("orgSecondSettlementService")
public class OrgSecondSettlementServiceImpl implements
		OrgSecondSettlementService {

	@Resource
	private OrgSecondSettlementDao orgSecondSettlementDao;

	@Override
	public List<OrgListDto> queryByOrg(OrgSecondSettlementQuery org) {
		// 查服务中心及服务中心信用额
		List<OrganizationsDto> orgList = orgSecondSettlementDao.queryByOrg(org);
		List<Long> orgIds = orgList.stream().map(a -> a.getId()).collect(Collectors.toList());
		Date serialTimeStart = null;
		Date serialTimeEnd = null;
		try {
			serialTimeStart = getSerialTimeStart(org);
			serialTimeEnd = getSerialTimeEnd(org);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		org.setOrgIds(orgIds);
		org.setSerialTimeStart(serialTimeStart);
		org.setSerialTimeEnd(serialTimeEnd);
		org.setApplyTypes(Arrays.asList(AccountTransApplyType.SECONDARY_SETTLEMENT_ACCOUNT_BALANCES.getCode(),
				AccountTransApplyType.INTO_THE_CAPITAL_ACCOUNT.getCode()));// 抵扣二次结算账户欠款，二次结算账户余额转入资金账户
		// 默认查询当日的应收应付金额
		List<OrgSecondSettlementDto> amountList = orgSecondSettlementDao.queryByOrgId(org);
		org.setSerialTimeStart(null);
		org.setSerialTimeEnd(serialTimeStart);
		// 查询统计截至到昨日23:59:59的应收应付总计
		List<OrgSecondSettlementDto> amountListYesterday = orgSecondSettlementDao.queryByOrgId(org);
		List<OrgListDto> list = new LinkedList<OrgListDto>();// 拼接查出来的服务中心和当日增加当日减少的数据放入OrgListDto
		for (OrganizationsDto orgItem : orgList) {
			OrgListDto orgListDto = new OrgListDto();
			orgListDto.setId(orgItem.getId());
			orgListDto.setName(orgItem.getName());
			orgListDto.setCreditLimit(orgItem.getCreditLimit());
			orgListDto.setCreditLimitUsed(orgItem.getCreditLimitUsed());
			for (OrgSecondSettlementDto dto : amountListYesterday) {
				if (dto.getBaseId().equals(orgItem.getId())) {
					BigDecimal amount = dto.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP);
					// 昨日余额加发生额
					orgListDto.setYesterdayAmount(orgListDto.getYesterdayAmount().setScale(2,BigDecimal.ROUND_HALF_UP).add(amount));
				}
			}
			// 计算昨日余额（等于昨日发生额+服务中心信用额）
			orgListDto.setYesterdayAmount(orgListDto.getYesterdayAmount().add(orgListDto.getCreditLimit().setScale(2,BigDecimal.ROUND_HALF_UP)));
			//默认设置当日余额等于昨日余额
			orgListDto.setTodayAmount(orgListDto.getYesterdayAmount().setScale(2,BigDecimal.ROUND_HALF_UP));
			for (OrgSecondSettlementDto orgDto : amountList) {
				if (orgDto.getBaseId() == orgItem.getId()) {
					// 如果申请类型为抵扣二次结算账户欠款，则是今日增加
					if (AccountTransApplyType.SECONDARY_SETTLEMENT_ACCOUNT_BALANCES.getCode().equals(orgDto.getApplyType())) {
						orgListDto.setAmountAdd(orgDto.getAmount());
						// 如果申请类型为二次结算账户余额转入资金账户，则是今日减少
					} else if (AccountTransApplyType.INTO_THE_CAPITAL_ACCOUNT.getCode().equals(orgDto.getApplyType())) {
						orgListDto.setAmountReduce(orgDto.getAmount());
					}
					// 计算当日余额（等于昨日余额+今日增加-今日减少）
					orgListDto.setTodayAmount(orgListDto.getYesterdayAmount().add(orgListDto.getAmountAdd().setScale(2,BigDecimal.ROUND_HALF_UP))
							.subtract(scale(orgListDto.getAmountReduce()).setScale(2,BigDecimal.ROUND_HALF_UP)));
				}
			}
			list.add(orgListDto);
		}
		return list;
	}
	
	/**
	 * BigDecimal取绝对值方法
	 * 
	 * @param limit
	 * @return
	 */
	private BigDecimal scale(BigDecimal limit) {
		BigDecimal bigDecimal = new BigDecimal(limit.toString());
		BigDecimal limits = bigDecimal.abs();
		return limits;
	}

	@Override
	public Integer queryByOrgCount(
			OrgSecondSettlementQuery orgSecondSettlementQuery) {
		return orgSecondSettlementDao.queryByOrgCount(orgSecondSettlementQuery);
	}

	/**
	 * 流水开始日期
	 * @param org
	 * @return
	 * @throws ParseException
	 */
	private Date getSerialTimeStart(OrgSecondSettlementQuery org)
			throws ParseException {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date SerialTimeStart = format.parse(org.getSerialTimeStr());
		return SerialTimeStart;
	}

	/**
	 * 流水终止日期
	 * @param org
	 * @return
	 * @throws ParseException
	 */
	private Date getSerialTimeEnd(OrgSecondSettlementQuery org)
			throws ParseException {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date SerialTimeEnd = new Date(format.parse(org.getSerialTimeStr())
				.getTime() + 24 * 3600 * 1000);
		return SerialTimeEnd;
	}

}
