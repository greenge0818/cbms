package com.prcsteel.platform.order.service.reward.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.prcsteel.platform.acl.model.dto.RewardDto;
import com.prcsteel.platform.acl.model.dto.UserOrgDto;
import com.prcsteel.platform.acl.model.enums.RewardType;
import com.prcsteel.platform.acl.model.model.Reward;
import com.prcsteel.platform.acl.model.query.ReportOrgQuery;
import com.prcsteel.platform.acl.persist.dao.RewardDao;
import com.prcsteel.platform.acl.persist.dao.UserDao;
import com.prcsteel.platform.common.utils.Tools;
import com.prcsteel.platform.core.model.dto.CategoryGroupDto;
import com.prcsteel.platform.core.persist.dao.CategoryGroupDao;
import com.prcsteel.platform.order.model.dto.*;
import com.prcsteel.platform.acl.model.enums.RewardStatus;
import com.prcsteel.platform.order.model.model.*;
import com.prcsteel.platform.order.persist.dao.*;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.prcsteel.platform.acl.model.model.Organization;
import com.prcsteel.platform.acl.persist.dao.OrganizationDao;
import com.prcsteel.platform.order.model.enums.RebateType;
import com.prcsteel.platform.order.model.query.ReportRebateQuery;
import com.prcsteel.platform.order.model.query.ReportRewardQuery;
import com.prcsteel.platform.order.service.reward.RewardReportService;

/**
 * Created by chenchen on 2015/8/20.
 */
@Service("rewardReportService")
public class RewardReportServiceImpl implements RewardReportService {
    private static final Logger logger = Logger.getLogger(RewardReportServiceImpl.class);
    private static double amount = 0.00;//买家提成金额
    @Resource
    OrganizationDao organizationDao;
    @Resource
    ReportRewardRecordDao reportRewardRecordDao;
    @Resource
    ReportNewUserRewardDao reportNewUserRewardDao;
    @Resource
    ReportRebateRecordDao reportRebateRecordDao;
    @Resource
    RewardDao rewardDao;
    @Resource
    CategoryGroupDao categoryGroupDao;
    @Resource
    UserDao userDao;

    private static final String subtotalText = "小计";

    /*
     * (non-Javadoc)
     *
     * @see RewardReportService#
     * getRewardReportByPage(int, int)
     */
    @Override
    public List<OrganizationRewardRecordDto> getRewardReportByPage(Date start,
                                                                   Date end, long org_id) {
        List<Long> org_ids = new ArrayList<Long>();
        org_ids.add(org_id);
        if (org_id != 0) {
            /*
             * 如果选择了服务中心，则查出该服务中心下所有子集
			 */
            List<Organization> orgIDList = organizationDao
                    .getOrgByParentID(org_id);
            for (Organization organization : orgIDList) {
                org_ids.add(organization.getId());
            }
        } else {
            List<Organization> orgIDList = organizationDao.getAllSecendOrg();
            for (Organization organization : orgIDList) {
                org_ids.add(organization.getId());
            }
        }
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("start", start);
        paramMap.put("end", end);
        paramMap.put("list", org_ids);
        List<RewardReportDto> reportList = reportRewardRecordDao
                .queryRewardDtoByOrgAndDate(paramMap);
        List<Organization> orgList = new ArrayList<Organization>();
        List<OrganizationRewardRecordDto> orgRecordList = new ArrayList<OrganizationRewardRecordDto>();
        if (org_id == 0) {
            /**
             * 按服务中心分配
             */
            orgList = organizationDao.getAllSecendOrg();

        } else {
			/*
			 * 如果只查询指定的中心
			 */
			Organization org = organizationDao.queryById(org_id);
			if (org.getParentId() == 0) {
				// 如果为第一级中心，则查出所有子中心
				orgList = organizationDao.getOrgByParentID(org_id);
			} else {
				// 如果为第二级中心
				orgList.add(org);
			}
		}
		for (Organization organization : orgList) {
			OrganizationRewardRecordDto orgTemp = new OrganizationRewardRecordDto();
			orgTemp.setOrganizationId(organization.getId());
			orgTemp.setOrgName(organization.getName());
			// 统计新增用户提成
			ReportNewUserRewardDto addDto = this.getReportNewUserRewardDto(
					start, end, organization.getId());
			orgTemp.setReportNewUserRewardDto(addDto);
			List<RewardReportDto> rewardReportDtoList = new ArrayList<RewardReportDto>();// 存放该中心的返利详情
			for (RewardReportDto report : reportList) {
				if (report.getOrgId() == organization.getId()) {
					// 如果该提成属于这个中心
					rewardReportDtoList.add(report);
				}
				orgTemp.setRewardReportDtoList(rewardReportDtoList);
			}
			orgRecordList.add(orgTemp);
		}

		return orgRecordList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * RewardReportService#getRewardReportByOrg
	 * (java.util.Date, java.util.Date, long)
	 */
	@Override
	public List<OrganizationRewardRecordDto> getRewardReportByOrg(Date start,
			Date end, long org_id, String managerName) {
		Map<String, Object> paramMap1 = new HashMap<String, Object>();
		paramMap1.put("org_id", org_id);
		paramMap1.put("manager_name", managerName);
		List<OrganizationRewardRecordDto> mangerList = reportRewardRecordDao
				.queryManager(paramMap1);// 获取该中心下所有用户
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("start", start);
		paramMap.put("end", end);
		paramMap.put("org_id", org_id);
		paramMap.put("manager_name", managerName);
		List<RewardReportDto> reportList = reportRewardRecordDao
				.queryRewardDtoByManager(paramMap);
		for (OrganizationRewardRecordDto organizationRewardRecordDto : mangerList) {
			List<RewardReportDto> rewardReportDtoList = new ArrayList<RewardReportDto>();
			for (RewardReportDto rewardReportDto : reportList) {
				if (organizationRewardRecordDto.getOrganizationId() == rewardReportDto
						.getManagerId()) {
					rewardReportDtoList.add(rewardReportDto);
				}
				organizationRewardRecordDto
						.setRewardReportDtoList(rewardReportDtoList);
				organizationRewardRecordDto
						.setReportNewUserRewardDto(this
                                .getReportNewUserRewardDtoByManager(start, end,
                                        organizationRewardRecordDto
                                                .getOrganizationId()));
			}
		}
		return mangerList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * RewardReportService#getRebateReportByPage
	 * (java.util.Map)
	 */
	@Override
	public List<ReportRebateRecordDto> getRebateReportByPage(
			ReportRebateQuery reportRebateQuery) {
		List<ReportRebateRecord> list = this.reportRebateRecordDao
				.queryComRebateByPage(reportRebateQuery);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<ReportRebateRecordDto> listShow = new ArrayList<ReportRebateRecordDto>();
		for (ReportRebateRecord reportRebateRecord : list) {
			buildReportRebateRecordDto(sdf, listShow, reportRebateRecord);
		}
		return listShow;
	}

	private void buildReportRebateRecordDto(SimpleDateFormat sdf,
			List<ReportRebateRecordDto> listShow,
			ReportRebateRecord reportRebateRecord) {
		ReportRebateRecordDto temp = new ReportRebateRecordDto();
		temp.setRebateTime(sdf.format(reportRebateRecord.getRebateTime()));
		temp.setType(RebateType.REBATE.toString());// 返利
		temp.setCode(reportRebateRecord.getCode());
		temp.setContactName(reportRebateRecord.getContactName());
		temp.setWeight(reportRebateRecord.getWeight());
		temp.setAmount(reportRebateRecord.getAmount());
		temp.setRebateAmount(reportRebateRecord.getRebateAmount());
		temp.setMoneyReduce(new BigDecimal("0.00"));
		temp.setRebateBalance(reportRebateRecord.getRebateBalance());
		listShow.add(temp);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * RewardReportService#totalRebateReport
	 * (java.util.Map)
	 */
	@Override
	public int totalRebateReport(ReportRebateQuery reportRebateQuery) {
		return reportRebateRecordDao.totalComRebate(reportRebateQuery);
	}

	public String getAccountName(ReportRebateQuery reportRebateQuery) {
		List<ReportRebateRecord> list = reportRebateRecordDao
				.queryComRebateByPage(reportRebateQuery);
		if (list != null && list.size() > 0) {
			return list.get(0).getAccountName();
		}
		return null;
	}

	/**
	 * 查询用户返利详情数据
	 * @param query
	 * @return
	 */
	public List<ReportRebateRecordDto> queryUserRebateDetail(ReportRebateQuery query) {
		return reportRebateRecordDao.queryUserRebateDetail(query);
	}

	/**
	 * 查询用户返利详情数据总数
	 * @param query
	 * @return
	 */
	public int queryUserRebateDetailCount(ReportRebateQuery query) {
		return reportRebateRecordDao.queryUserRebateDetailCount(query);
	}


    /*
     * (non-Javadoc)
     *
     * @see RewardReportService#
     * getReportNewUserRewardDto(java.util.Date, java.util.Date, long)
     */
    @Override
    public ReportNewUserRewardDto getReportNewUserRewardDto(Date start,
                                                            Date end, long org_id) {
        List<Long> org_ids = new ArrayList<Long>();
        org_ids.add(org_id);
        List<Organization> orgIDList = organizationDao.getOrgByParentID(org_id);
        for (Organization organization : orgIDList) {
            org_ids.add(organization.getId());
        }

        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("start", start);
        paramMap.put("end", end);
        paramMap.put("list", org_ids);
        return reportNewUserRewardDao.queryReportNewUserReward(paramMap);
    }

    /*
     * (non-Javadoc)
     *
     * @see RewardReportService#
     * getReportNewUserRewardDtoByManager(java.util.Date, java.util.Date, long)
     */
    @Override
    public ReportNewUserRewardDto getReportNewUserRewardDtoByManager(
            Date start, Date end, long manager_id) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("start", start);
        paramMap.put("end", end);
        paramMap.put("manager_id", manager_id);
        return this.reportNewUserRewardDao
                .queryReportNewUserRewardByManager(paramMap);
    }


    /**
     * 获得服务中心提成列表
     *
     * @param queryParam
     * @return
     * @author dengxiyan
     */
    @Override
    public List<RewardNewAcccountWithDetailsDto> getOrgRewardList(ReportRewardQuery queryParam) {
        //结果集
        List<RewardNewAcccountWithDetailsDto> resultList = new ArrayList<>();
        ReportOrgQuery reportOrgQuery = new ReportOrgQuery();
        reportOrgQuery.setOrgId(queryParam.getOrgId());
        reportOrgQuery.setOrgName(queryParam.getOrgName());
        reportOrgQuery.setUserIdList(queryParam.getUserIdList());
        reportOrgQuery.setManagerName(queryParam.getManagerName());
        reportOrgQuery.setManagerId(queryParam.getManagerId());
        //查询所有服务中心
        List<Organization> orgList = organizationDao.queryAllOrg(reportOrgQuery);
        //查询所有大类
         List<CategoryGroupDto> bigTypeList=categoryGroupDao.queryAllCategoryGroupInner();
        //服务中心所有大类详情
        //List<RewardDetailDto> details = reportRewardRecordDao.queryOrgRewardDetails(queryParam);
        List<RewardDetailDto> details  = null;
        //服务中心提成报表明细数据
        List<CommissionExcel> list = null;
        List<ReportNewUserReward> newUserRewardList = null;
        //如果是当前则不查询提成
        int currMonth = Calendar.getInstance().get(Calendar.MONTH)+1;
        //query.getMonth: yyyyMM
        if(queryParam.getMonth()!=null && Integer.parseInt(queryParam.getMonth().substring(4))!=currMonth){
        	//新增客户提成规则及提成金额
        	newUserRewardList = reportNewUserRewardDao.queryNewUserRewardByOrgId(queryParam);
            //查询历史提成数据
            list = selectCommissionListByMonth(queryParam);
        }else{
            //查询当前提成数据
             list = findByParam(queryParam);
        }
        Map<String, List<RewardDetailDto>>  groupMap = new HashMap<String, List<RewardDetailDto>>();
        Map<String, List<CommissionExcel>> commissMap = new HashMap<String, List<CommissionExcel>>();
        if (list != null && list.size() > 0) {
            //按服务中心分组 key:${orgid} + "_" + ${orgName}
            //groupMap = details.stream().peek(a -> a.setGroupBy(a.getOrgId() + "_" + a.getOrgName())).collect(Collectors.groupingBy(RewardDetailDto::getGroupBy));
            commissMap = list.stream().peek(a -> a.setGroupBy(a.getBuyerOrgId() + "_" + a.getBuyerOrgName())).collect(Collectors.groupingBy(CommissionExcel::getGroupBy));
        }
        for (Organization org : orgList) {
                String s = org.getId() + "_" + org.getName();
                List<CommissionExcel> comList = commissMap.get(s);
                details = new ArrayList<>();
                if (comList != null) {
                    //当前服务中心有数据
                    //按大类分组
                    Map<String, List<CommissionExcel>> bigTypeMap = comList.stream().collect(Collectors.groupingBy(CommissionExcel::getCbname));
                    for (CategoryGroupDto categoryGroupDto : bigTypeList) {
                        String type = categoryGroupDto.getCategoryGroupName();
                        RewardDetailDto rewardDetailDto = new RewardDetailDto();
                        rewardDetailDto.setOrgId(Long.valueOf(StringUtils.substringBefore(s, "_")));
                        rewardDetailDto.setOrgName(StringUtils.substringAfter(s, "_"));
                        rewardDetailDto.setGroupName(type);
                        rewardDetailDto.setGroupBy(s);
                        rewardDetailDto.setCurrOrgSellerConsignWeight(new BigDecimal(0.00));
                        rewardDetailDto.setCurrOrgSellerTempWeight(new BigDecimal(0.00));
                        rewardDetailDto.setOtherOrgSellerConsignWeight(new BigDecimal(0.00));
                        rewardDetailDto.setOtherOrgSellerTempWeight(new BigDecimal(0.00));
                        List<CommissionExcel> commissionList = bigTypeMap.get(type);
                        if (commissionList != null) {
                            rewardDetailDto.setCurrOrgBuyerConsignWeight(new BigDecimal(commissionList.stream().mapToDouble(a -> a.getCurrOrgBuyerConsignWeight().doubleValue()).sum()));
                            rewardDetailDto.setCurrOrgBuyerTempWeight(new BigDecimal(commissionList.stream().mapToDouble(a -> a.getCurrOrgBuyerTempWeight().doubleValue()).sum()));
                            rewardDetailDto.setOtherOrgBuyerConsignWeight(new BigDecimal(commissionList.stream().mapToDouble(a -> a.getOtherOrgBuyerConsignWeight().doubleValue()).sum()));
                            rewardDetailDto.setOtherOrgBuyerTempWeight(new BigDecimal(commissionList.stream().mapToDouble(a -> a.getOtherOrgBuyerTempWeight().doubleValue()).sum()));
                            rewardDetailDto.setBuyerRewardAmount(new BigDecimal(commissionList.stream().mapToDouble(a -> a.getBuyerDeduct().doubleValue()).sum()));
                            rewardDetailDto.setSellerRewardAmount(new BigDecimal(commissionList.stream().mapToDouble(a -> a.getSellerDeduct().doubleValue()).sum()));
                        } else {
                            rewardDetailDto.setCurrOrgBuyerConsignWeight(new BigDecimal(0.00));
                            rewardDetailDto.setCurrOrgBuyerTempWeight(new BigDecimal(0.00));
                            rewardDetailDto.setOtherOrgBuyerConsignWeight(new BigDecimal(0.00));
                            rewardDetailDto.setOtherOrgBuyerTempWeight(new BigDecimal(0.00));
                            rewardDetailDto.setBuyerRewardAmount(new BigDecimal(0.00));
                            rewardDetailDto.setSellerRewardAmount(new BigDecimal(0.00));
                        }
                        details.add(rewardDetailDto);
                    }

                }else{
                    //当前服务中心没有数据
                    for (CategoryGroupDto categoryGroupDto : bigTypeList) {
                        String type = categoryGroupDto.getCategoryGroupName();
                        RewardDetailDto rewardDetailDto = new RewardDetailDto();
                        rewardDetailDto.setOrgId(Long.valueOf(StringUtils.substringBefore(s, "_")));
                        rewardDetailDto.setOrgName(StringUtils.substringAfter(s, "_"));
                        rewardDetailDto.setGroupName(type);
                        rewardDetailDto.setGroupBy(s);
                        rewardDetailDto.setCurrOrgSellerConsignWeight(new BigDecimal(0.00));
                        rewardDetailDto.setCurrOrgSellerTempWeight(new BigDecimal(0.00));
                        rewardDetailDto.setOtherOrgSellerConsignWeight(new BigDecimal(0.00));
                        rewardDetailDto.setOtherOrgSellerTempWeight(new BigDecimal(0.00));
                        rewardDetailDto.setCurrOrgBuyerConsignWeight(new BigDecimal(0.00));
                        rewardDetailDto.setCurrOrgBuyerTempWeight(new BigDecimal(0.00));
                        rewardDetailDto.setOtherOrgBuyerConsignWeight(new BigDecimal(0.00));
                        rewardDetailDto.setOtherOrgBuyerTempWeight(new BigDecimal(0.00));
                        rewardDetailDto.setBuyerRewardAmount(new BigDecimal(0.00));
                        rewardDetailDto.setSellerRewardAmount(new BigDecimal(0.00));
                        details.add(rewardDetailDto);
                    }
                }
                groupMap.put(s, details);
            }
        if(groupMap != null) {
            for (Map.Entry<String, List<RewardDetailDto>> entry : groupMap.entrySet()) {
                resultList.add(buildOrgRewardNewAcccountWithDetails(entry, newUserRewardList));
            }
        }
        return resultList;
    }


    private RewardNewAcccountWithDetailsDto buildOrgRewardNewAcccountWithDetails(Map.Entry<String, List<RewardDetailDto>> entry, List<ReportNewUserReward> newUserRewardList) {
        RewardNewAcccountWithDetailsDto dto = new RewardNewAcccountWithDetailsDto();
        List<RewardDetailDto> groupDetails = entry.getValue();

        dto.setOrgId(Long.valueOf(StringUtils.substringBefore(entry.getKey(), "_")));
        dto.setOrgName(StringUtils.substringAfter(entry.getKey(), "_"));

        //构建小计行
        RewardDetailDto subtotal = getSubtotalRewardDetailDto(groupDetails);
        groupDetails.add(subtotal);

        //添加明细行
        dto.setDetails(groupDetails);

        //新增客户提成规则及提成金额
        if (newUserRewardList != null && newUserRewardList.size() > 0) {
            Optional<ReportNewUserReward> op = newUserRewardList.stream().filter(e -> dto.getOrgId().equals(e.getOrgId())).findFirst();
            if (op.isPresent()) {
                dto.setAddNewBuyer(op.get().getAddNewBuyer());
                dto.setAddNewSeller(op.get().getAddNewSeller());
                dto.setBuyerRewardRole(op.get().getBuyerRewardRole());
                dto.setSellerRewardRole(op.get().getSellerRewardRole());
                dto.setBuyerRewardAmount(op.get().getBuyerRewardAmount());
                dto.setSellerRewardAmount(op.get().getSellerRewardAmount());
            }
        }

        //提成合计
        // 提成合计计算过程及结果： 提成合计=买家订单提成总额 + 卖家订单提成总额 + 新增买家提成 + 新增卖家提成
        dto.setCalFormula(getCalFormula(subtotal.getBuyerRewardAmount(), subtotal.getSellerRewardAmount(), dto.getBuyerRewardAmount(), dto.getSellerRewardAmount()));

        return dto;
    }

    /**
     * 获得提成合计计算过程
     * 提成合计计算过程及结果： 提成合计=买家订单提成总额 + 卖家订单提成总额 + 新增买家提成 + 新增卖家提成
     *
     * @return
     */
    private String getCalFormula(BigDecimal... numbers) {
        StringBuilder sb = new StringBuilder("");
        BigDecimal total = BigDecimal.ZERO;
        BigDecimal temp;
        if (numbers != null) {
            for (BigDecimal num : numbers) {
                temp = toZero(num);
                sb.append(temp.doubleValue() + "+");
                total = total.add((temp));
            }
        }
        return StringUtils.substringBeforeLast(sb.toString(), "+") + "=" + total.doubleValue();
    }

    private RewardDetailDto getSubtotalRewardDetailDto(List<RewardDetailDto> list) {
        //小计行
        RewardDetailDto dto = new RewardDetailDto();
        dto.setGroupName(subtotalText);

        dto.setCurrOrgBuyerTempWeight(list.stream().map(RewardDetailDto::getCurrOrgBuyerTempWeight).reduce(BigDecimal.ZERO, BigDecimal::add));
        dto.setCurrOrgBuyerConsignWeight(list.stream().map(RewardDetailDto::getCurrOrgBuyerConsignWeight).reduce(BigDecimal.ZERO, BigDecimal::add));
        dto.setOtherOrgBuyerTempWeight(list.stream().map(RewardDetailDto::getOtherOrgBuyerTempWeight).reduce(BigDecimal.ZERO, BigDecimal::add));
        dto.setOtherOrgBuyerConsignWeight(list.stream().map(RewardDetailDto::getOtherOrgBuyerConsignWeight).reduce(BigDecimal.ZERO, BigDecimal::add));

        dto.setCurrOrgSellerTempWeight(list.stream().map(RewardDetailDto::getCurrOrgSellerTempWeight).reduce(BigDecimal.ZERO, BigDecimal::add));
        dto.setCurrOrgSellerConsignWeight(list.stream().map(RewardDetailDto::getCurrOrgSellerConsignWeight).reduce(BigDecimal.ZERO, BigDecimal::add));
        dto.setOtherOrgSellerTempWeight(list.stream().map(RewardDetailDto::getOtherOrgSellerTempWeight).reduce(BigDecimal.ZERO, BigDecimal::add));
        dto.setOtherOrgSellerConsignWeight(list.stream().map(RewardDetailDto::getOtherOrgSellerConsignWeight).reduce(BigDecimal.ZERO, BigDecimal::add));

        dto.setBuyerRewardAmount(list.stream().map(RewardDetailDto::getBuyerRewardAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
        dto.setSellerRewardAmount(list.stream().map(RewardDetailDto::getSellerRewardAmount).reduce(BigDecimal.ZERO, BigDecimal::add));

        return dto;
    }

    private BigDecimal toZero(BigDecimal decimal) {
        return decimal == null ? BigDecimal.ZERO : decimal;
    }

    /**
     * 获得交易员的提成列表
     *
     * @param queryParam
     * @return
     * @author dengxiyan
     */
    @Override
    public List<RewardNewAcccountWithDetailsDto> getManagerRewardList(ReportRewardQuery queryParam) {
        //结果集
        List<RewardNewAcccountWithDetailsDto> resultList = new ArrayList<>();
        //查询所有大类
        List<CategoryGroupDto> bigTypeList=categoryGroupDao.queryAllCategoryGroupInner();
        ReportOrgQuery reportOrgQuery = new ReportOrgQuery();
        reportOrgQuery.setOrgId(queryParam.getOrgId());
        reportOrgQuery.setOrgName(queryParam.getOrgName());
        reportOrgQuery.setUserIdList(queryParam.getUserIdList());
        reportOrgQuery.setManagerName(queryParam.getManagerName());
        reportOrgQuery.setManagerId(queryParam.getManagerId());
        //查询服务中心下所有的交易员
        List<UserOrgDto> userList = userDao.queryUsersByOrgId(reportOrgQuery);
        List<RewardDetailDto> details  = null;
        //交易员所有大类详情
        //List<RewardDetailDto> details = reportRewardRecordDao.queryManagerRewardDetails(queryParam);

        //新增客户提成规则及提成金额
        List<ReportNewUserReward> newUserRewardList = reportNewUserRewardDao.queryManagerNewUserReward(queryParam);
        //服务中心提成报表明细数据
        List<CommissionExcel> list = null;
        //如果是当前则不查询提成
        int currMonth = Calendar.getInstance().get(Calendar.MONTH)+1;
        //query.getMonth: yyyyMM
        if(queryParam.getMonth()!=null && Integer.parseInt(queryParam.getMonth().substring(4))!=currMonth){
            //查询历史提成数据
            list = selectCommissionListByMonth(queryParam);
        }else{
            //查询当前提成数据
            list = findByParam(queryParam);
        }
        Map<String, List<RewardDetailDto>> groupMap = new HashMap<String, List<RewardDetailDto>>();
        Map<String, List<CommissionExcel>> commissMap = new HashMap<String, List<CommissionExcel>>();
        if (list != null && list.size() > 0) {
            //按交易员分组 key:${managerid} + "_" + ${managerName}
            commissMap = list.stream().peek(a -> a.setGroupBy(a.getBuyerOwnerId() + "_" + a.getBuyerOwnerName())).collect(Collectors.groupingBy(CommissionExcel::getGroupBy));
        }
        for (UserOrgDto user : userList) {
                details  = new ArrayList<>();
                String s = user.getId() + "_" + user.getName();
                List<CommissionExcel> commList = commissMap.get(s);
                //当前交易员下有数据
                if(commList != null) {
                    //按大类分组
                    Map<String, List<CommissionExcel>> bigTypeMap = commList.stream().collect(Collectors.groupingBy(CommissionExcel::getCbname));
                    for (CategoryGroupDto categoryGroupDto : bigTypeList) {
                        String type = categoryGroupDto.getCategoryGroupName();
                        RewardDetailDto rewardDetailDto = new RewardDetailDto();
                        rewardDetailDto.setManagerId(Long.valueOf(StringUtils.substringBefore(s, "_")));
                        rewardDetailDto.setManagerName(StringUtils.substringAfter(s, "_"));
                        rewardDetailDto.setGroupName(type);
                        rewardDetailDto.setGroupBy(s);
                        rewardDetailDto.setCurrOrgSellerConsignWeight(new BigDecimal(0.00));
                        rewardDetailDto.setCurrOrgSellerTempWeight(new BigDecimal(0.00));
                        rewardDetailDto.setOtherOrgSellerConsignWeight(new BigDecimal(0.00));
                        rewardDetailDto.setOtherOrgSellerTempWeight(new BigDecimal(0.00));
                        List<CommissionExcel> commissionList = bigTypeMap.get(type);
                        if (commissionList != null) {
                            rewardDetailDto.setCurrOrgBuyerConsignWeight(new BigDecimal(commissionList.stream().mapToDouble(a -> a.getCurrOrgBuyerConsignWeight().doubleValue()).sum()));
                            rewardDetailDto.setCurrOrgBuyerTempWeight(new BigDecimal(commissionList.stream().mapToDouble(a -> a.getCurrOrgBuyerTempWeight().doubleValue()).sum()));
                            rewardDetailDto.setOtherOrgBuyerConsignWeight(new BigDecimal(commissionList.stream().mapToDouble(a -> a.getOtherOrgBuyerConsignWeight().doubleValue()).sum()));
                            rewardDetailDto.setOtherOrgBuyerTempWeight(new BigDecimal(commissionList.stream().mapToDouble(a -> a.getOtherOrgBuyerTempWeight().doubleValue()).sum()));
                            rewardDetailDto.setBuyerRewardAmount(new BigDecimal(commissionList.stream().mapToDouble(a -> a.getBuyerDeduct().doubleValue()).sum()));
                            rewardDetailDto.setSellerRewardAmount(new BigDecimal(commissionList.stream().mapToDouble(a -> a.getSellerDeduct().doubleValue()).sum()));
                        } else {
                            rewardDetailDto.setCurrOrgBuyerConsignWeight(new BigDecimal(0.00));
                            rewardDetailDto.setCurrOrgBuyerTempWeight(new BigDecimal(0.00));
                            rewardDetailDto.setOtherOrgBuyerConsignWeight(new BigDecimal(0.00));
                            rewardDetailDto.setOtherOrgBuyerTempWeight(new BigDecimal(0.00));
                            rewardDetailDto.setBuyerRewardAmount(new BigDecimal(0.00));
                            rewardDetailDto.setSellerRewardAmount(new BigDecimal(0.00));
                        }
                        details.add(rewardDetailDto);
                    }
                }else{
                    //当前交易员下没有数据
                    for (CategoryGroupDto categoryGroupDto : bigTypeList) {
                        String type = categoryGroupDto.getCategoryGroupName();
                        RewardDetailDto rewardDetailDto = new RewardDetailDto();
                        rewardDetailDto.setManagerId(Long.valueOf(StringUtils.substringBefore(s, "_")));
                        rewardDetailDto.setManagerName(StringUtils.substringAfter(s, "_"));
                        rewardDetailDto.setGroupName(type);
                        rewardDetailDto.setGroupBy(s);
                        rewardDetailDto.setCurrOrgSellerConsignWeight(new BigDecimal(0.00));
                        rewardDetailDto.setCurrOrgSellerTempWeight(new BigDecimal(0.00));
                        rewardDetailDto.setOtherOrgSellerConsignWeight(new BigDecimal(0.00));
                        rewardDetailDto.setOtherOrgSellerTempWeight(new BigDecimal(0.00));
                        rewardDetailDto.setCurrOrgBuyerConsignWeight(new BigDecimal(0.00));
                        rewardDetailDto.setCurrOrgBuyerTempWeight(new BigDecimal(0.00));
                        rewardDetailDto.setOtherOrgBuyerConsignWeight(new BigDecimal(0.00));
                        rewardDetailDto.setOtherOrgBuyerTempWeight(new BigDecimal(0.00));
                        rewardDetailDto.setBuyerRewardAmount(new BigDecimal(0.00));
                        rewardDetailDto.setSellerRewardAmount(new BigDecimal(0.00));
                        details.add(rewardDetailDto);
                    }
                }
                groupMap.put(s,details);
            }
        if(groupMap != null) {
            for (Map.Entry<String, List<RewardDetailDto>> entry : groupMap.entrySet()) {
                resultList.add(buildManagerRewardNewAcccountWithDetails(entry, newUserRewardList));
            }
        }
        return resultList;
    }

    private RewardNewAcccountWithDetailsDto buildManagerRewardNewAcccountWithDetails(Map.Entry<String, List<RewardDetailDto>> entry, List<ReportNewUserReward> newUserRewardList) {
        RewardNewAcccountWithDetailsDto dto = new RewardNewAcccountWithDetailsDto();
        List<RewardDetailDto> groupDetails = entry.getValue();

        dto.setManagerId(Long.valueOf(StringUtils.substringBefore(entry.getKey(), "_")));
        dto.setManagerName(StringUtils.substringAfter(entry.getKey(), "_"));

        //构建小计行
        RewardDetailDto subtotal = getSubtotalRewardDetailDto(groupDetails);
        groupDetails.add(subtotal);

        //添加明细行
        dto.setDetails(groupDetails);

        //新增客户提成规则及提成金额
        if (newUserRewardList != null && newUserRewardList.size() > 0) {
           Optional<ReportNewUserReward> op = newUserRewardList.stream().filter(e -> dto.getManagerId().equals(e.getManagerId())).findFirst();
            if (op.isPresent()) {
                dto.setAddNewBuyer(op.get().getAddNewBuyer());
                dto.setAddNewSeller(op.get().getAddNewSeller());
                dto.setBuyerRewardRole(op.get().getBuyerRewardRole());
                dto.setSellerRewardRole(op.get().getSellerRewardRole());
                dto.setBuyerRewardAmount(op.get().getBuyerRewardAmount());
                dto.setSellerRewardAmount(op.get().getSellerRewardAmount());
            }
        }

        //提成合计
        // 提成合计计算过程及结果： 提成合计=买家订单提成总额 + 卖家订单提成总额 + 新增买家提成 + 新增卖家提成
        dto.setCalFormula(getCalFormula(subtotal.getBuyerRewardAmount(), subtotal.getSellerRewardAmount(), dto.getBuyerRewardAmount(), dto.getSellerRewardAmount()));

        return dto;
    }
    /**
     * 根据单号更新买家联系人信息
     * @param order
     * @author wangxianjun
     * @return
     */
    @Override
    public int updateByCodeSelective(ConsignOrder order){
        List<ReportRebateRecord> rebateRecords = new ArrayList<ReportRebateRecord>();
        List<ReportRebateRecord> rebateRecordList = reportRebateRecordDao.queryByOrderCode(order.getCode());
        if(rebateRecordList.size()==0){//如果返利表中没有数据，就不需要更新
            return 1;
        }else {
            for(ReportRebateRecord reportRebateRecord:rebateRecordList){
                reportRebateRecord.setContactId(order.getContactId());
                reportRebateRecord.setContactName(order.getContactName());
                rebateRecords.add(reportRebateRecord);
            }
        }
        return reportRebateRecordDao.batchUpdateReportRebateList(rebateRecords);
    }

    @Override
     public List<CommissionExcel>  findByParam(ReportRewardQuery queryParam){
        List<CommissionExcel> list = new ArrayList<CommissionExcel>();
        try {
            RewardDto rewardDto = rewardDao.queryReward();
            List<RewardDto> rewardDtoList = rewardDao.queryRewardDtoList();
            //采购累积量计算规则 0 全部，1 按年，2 按月

            Date minYearDate = null;
            Date maxYearDate = null;
            Date minMonthDate = Tools.strToDate(queryParam.getMonth() + "01", "yyyyMMdd");
            Date maxMonthDate = Tools.getAfterMonth(minMonthDate,1);

            if ("0".equals(rewardDto.getCategoryUuid())) {
                minYearDate = null;
                maxYearDate = null;
            } else if ("1".equals(rewardDto.getCategoryUuid())) {
                minYearDate= Tools.strToDate(queryParam.getMonth().substring(0,4) + "0101", "yyyyMMdd");
                maxYearDate = Tools.getAfterYear(minYearDate,1);
            } else if ("2".equals(rewardDto.getCategoryUuid())) {
                minYearDate = minMonthDate;
                maxYearDate = maxMonthDate;
            }
            queryParam.setMinYearDate(minYearDate);
            queryParam.setMaxYearDate(maxYearDate);
            queryParam.setMinMonthDate(minMonthDate);
            queryParam.setMaxMonthDate(maxMonthDate);

            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("rewardStatus", RewardStatus.EFFECT.getName());
            paramMap.put("rewardType", RewardType.ORDER.getCode());
            Map<String, List<BigDecimal>> settings = rewardDao.queryAll(paramMap).stream().collect(Collectors.groupingBy(Reward::getCategoryUuid, Collectors.mapping(Reward::getRewardRole, Collectors.toList())));
            BigDecimal buyerConsign = settings.get("buyer_consign_local_reward").get(0);
            BigDecimal sellerConsign = settings.get("seller_consign_local_reward").get(0);
            BigDecimal buyerTemp = settings.get("buyer_temp_local_reward").get(0);
            BigDecimal sellerTemp = settings.get("seller_temp_local_reward").get(0);
            BigDecimal buyerConsignRange = settings.get("buyer_consign_range_reward").get(0);
            BigDecimal sellerConsignRange = settings.get("seller_consign_range_reward").get(0);
            BigDecimal buyerTempRange = settings.get("buyer_temp_range_reward").get(0);
            BigDecimal sellerTempRange = settings.get("seller_temp_range_reward").get(0);

            list = reportRebateRecordDao.findByParam(queryParam);
            Map<String,BigDecimal> saveMap = new HashMap<String,BigDecimal>();//保存对应买家的累积采购量

            for (CommissionExcel centerList : list) {
                amount = 0.00;
                BigDecimal actualWeight = centerList.getActualPickWeightServer();//当前订单明细交易量
                BigDecimal lastWeight = centerList.getLastWeight();//买家历史采购量
                BigDecimal standard = centerList.getRewardRole().multiply(rewardDto.getRewardRole());//品类系数*佣金标准
                BigDecimal totalWeight = new BigDecimal(0.0);
                if(saveMap == null){
                    //saveMap为空，把当前买家和累积采购量写入
                    totalWeight = lastWeight.add(actualWeight);
                    saveMap.put(centerList.getBuyerAccountName(),totalWeight);
                }else{
                    if(saveMap.get(centerList.getBuyerAccountName()) == null){
                        //saveMap不为空，且不存在当前买家的累积采购量，则把当前买家和累积采购量写入
                        totalWeight = lastWeight.add(actualWeight);
                        saveMap.put(centerList.getBuyerAccountName(), totalWeight);
                    }else{
                        //saveMap不为空，且存在当前买家的累积采购量，则把存在的买家累积采购量加上当前明细交易量存入当前买家对应的saveMap
                        lastWeight =saveMap.get(centerList.getBuyerAccountName());
                        totalWeight = lastWeight.add(actualWeight);
                        saveMap.put(centerList.getBuyerAccountName(),totalWeight);
                    }
                }

                if (actualWeight == null) {
                    centerList.setActualPickWeightServer(new BigDecimal(0.00).setScale(2, BigDecimal.ROUND_HALF_UP));
                }

                if (centerList.getBuyerOrgName().equals(centerList.getSellerOrgName())) {//
                    if ("consign".equals(centerList.getBuyConsign())) {//本地服务中心品牌店
                        centerList.setBuyerRewardRole(buyerConsign);
                        centerList.setSellerRewardRole(sellerConsign);
                        centerList.setCurrOrgBuyerConsignWeight(actualWeight);
                        centerList.setCurrOrgBuyerTempWeight(new BigDecimal(0.00));
                        centerList.setOtherOrgBuyerConsignWeight(new BigDecimal(0.00));
                        centerList.setOtherOrgBuyerTempWeight(new BigDecimal(0.00));
                    } else {// 临采
                        centerList.setBuyerRewardRole(buyerTemp);
                        centerList.setSellerRewardRole(sellerTemp);
                        centerList.setCurrOrgBuyerTempWeight(actualWeight);
                        centerList.setCurrOrgBuyerConsignWeight(new BigDecimal(0.00));
                        centerList.setOtherOrgBuyerConsignWeight(new BigDecimal(0.00));
                        centerList.setOtherOrgBuyerTempWeight(new BigDecimal(0.00));
                    }
                } else {
                    if ("consign".equals(centerList.getSellConsign())) {//跨地服务中心品牌店
                        centerList.setBuyerRewardRole(buyerConsignRange);
                        centerList.setSellerRewardRole(sellerConsignRange);
                        centerList.setCurrOrgBuyerTempWeight(new BigDecimal(0.00));
                        centerList.setCurrOrgBuyerConsignWeight(new BigDecimal(0.00));
                        centerList.setOtherOrgBuyerConsignWeight(actualWeight);
                        centerList.setOtherOrgBuyerTempWeight(new BigDecimal(0.00));

                    } else {//临采
                        centerList.setBuyerRewardRole(buyerTempRange);
                        centerList.setSellerRewardRole(sellerTempRange);
                        centerList.setCurrOrgBuyerTempWeight(new BigDecimal(0.00));
                        centerList.setCurrOrgBuyerConsignWeight(new BigDecimal(0.00));
                        centerList.setOtherOrgBuyerConsignWeight(new BigDecimal(0.00));
                        centerList.setOtherOrgBuyerTempWeight(actualWeight);
                    }
                }
                int index=0;//累积量所在的区间索引
                //获取累积量所在的区间索引
                for(int i=0;i<rewardDtoList.size();i++){
                    if(rewardDtoList.get(i).getRewardRole() == null){
                        index = i;
                        break;
                    }else if(lastWeight.compareTo(rewardDtoList.get(i).getSellerLimit()) == 1 &&  lastWeight.compareTo(rewardDtoList.get(i).getRewardRole()) <= 0){
                        index = i;
                        break;
                    }else if(rewardDtoList.get(i).getSellerLimit().compareTo(lastWeight) == 0 && lastWeight.compareTo(new BigDecimal(0))==0){
                        index = i;
                        break;
                    }
                }
                if(rewardDtoList.get(index).getRewardRole() == null){
                    //累积量在最后一个区间
                    amount += actualWeight.multiply(rewardDtoList.get(index).getBuyRadio()).multiply(standard).doubleValue();
                }else{
                    //累积量不在最后一个区间
                    BigDecimal diff = rewardDtoList.get(index).getRewardRole().subtract(lastWeight);
                    if (rewardDtoList.get(index).getRewardRole().compareTo(lastWeight) == 0){
                        //累积量与自己所在区间的最大值相同
                        countVal(rewardDtoList, index + 1, actualWeight,standard);
                    } else if (rewardDtoList.get(index).getRewardRole().compareTo(lastWeight) == 1){
                        //累积量小于自己所在区间的最大值
                        if(actualWeight.compareTo(diff)==1){
                            //当前量大于累积量所在区间剩余重量
                            amount += diff.multiply(rewardDtoList.get(index).getBuyRadio()).multiply(standard).doubleValue();//累积量所在区间剩余重量的提成
                            countVal(rewardDtoList, index + 1, actualWeight.subtract(diff),standard);
                        }else{
                            amount += actualWeight.multiply(rewardDtoList.get(index).getBuyRadio()).multiply(standard).doubleValue() ;//当前重量的提成
                        }
                    }
                }
                centerList.setLastWeight(totalWeight);//把买家累积量保存
                centerList.setBuyerDeduct(new BigDecimal(amount).setScale(2, BigDecimal.ROUND_HALF_UP));
                centerList.setSellerDeduct(centerList.getRewardRole().multiply(centerList.getSellerRewardRole()).multiply(actualWeight).setScale(2, BigDecimal.ROUND_HALF_UP));
                if(rewardDto.getBuyRadio() != null && centerList.getDiffPrice().multiply(rewardDto.getBuyRadio()).compareTo(new BigDecimal(0)) == 1)
                    centerList.setProfit(centerList.getDiffPrice().multiply(rewardDto.getBuyRadio()).divide(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP));//差价利润佣金
                else
                    centerList.setProfit(new BigDecimal(0.00));
                //目前新增买家和卖家提成都为0，不需要再判断是否是同一笔订单
               /* if (centerList.getBuyerCount().compareTo(new BigDecimal(80.0)) == 0) {//同一笔订单  给一次提成
                    if (buyerCodeStr == null) {
                        vpd.put("var20", centerList.getBuyerCount() + "");//新增买家提成金额
                        buyerCodeStr = centerList.getOrderCode();
                    } else {
                        if (centerList.getOrderCode().equals(buyerCodeStr)) {
                            vpd.put("var20", "0.00");
                        } else {
                            vpd.put("var20", centerList.getBuyerCount() + "");//新增买家提成金额
                            buyerCodeStr = null;
                        }
                    }
                } else {
                    vpd.put("var20", centerList.getBuyerCount() + "");//新增买家提成金额
                    buyerCodeStr = null;
                }
                if (centerList.getSellerCount().compareTo(new BigDecimal(80.0)) == 0) { //同一笔订单  给一次提成
                    if (sellerCodeStr == null) {
                        vpd.put("var21", centerList.getSellerCount() + "");//新增卖家提成金额
                        sellerCodeStr = centerList.getOrderCode();
                    } else {
                        if (centerList.getOrderCode().equals(sellerCodeStr)) {
                            vpd.put("var21", "0.00");
                        } else {
                            vpd.put("var21", centerList.getSellerCount() + "");
                            sellerCodeStr = null;
                        }
                    }
                } else {
                    vpd.put("var21", centerList.getSellerCount() + "");//新增卖家提成金额   BigDecimal(Double.parseDouble(newBuyerReward.toString())
                    sellerCodeStr = null;
                }*/
            }

        }catch (Exception e) {
            logger.debug("服务中心提成报表明细导出EXCEL出错："+e.getMessage());
        }
        return list;
    }
    public void countVal(List<RewardDto> list, int index, BigDecimal a,BigDecimal standard){

        BigDecimal p = a.add(list.get(index).getSellerLimit());
        //在当前区间，则直接用重量乘以当前系数
        if( list.get(index).getRewardRole() ==null || (p.compareTo(list.get(index).getSellerLimit())==1 && p.compareTo(list.get(index).getRewardRole())<=0))
            amount += a.multiply(list.get(index).getBuyRadio()).multiply(standard).doubleValue();
        else{
            //超出当前区间，则用当前区间的重量乘以当前系数
            amount += (list.get(index).getRewardRole().subtract(list.get(index).getSellerLimit())).multiply(list.get(index).getBuyRadio()).multiply(standard).doubleValue();
            //继续往下计算
            countVal(list, index+1,p.subtract(list.get(index).getRewardRole()),standard);
        }
    }
    @Override
    public List<ReportCollect>  findSunParam(ReportRewardQuery queryParam){

        return reportRebateRecordDao.findSunParam(queryParam);
    }
    @Override
    public List<ReportCollect>  findSellerSunParam(ReportRewardQuery queryParam){

        return reportRebateRecordDao.findSellerSunParam(queryParam);
    }

    /**
     *
     * 服务中心提成历史数据统计明细报表导出功能
     * @param queryParam
     * @return
     */
    @Override
    public List<CommissionExcel>  selectCommissionListByMonth(ReportRewardQuery queryParam){
        List<CommissionExcel>  list= reportRebateRecordDao.selectCommissionListByMonth(queryParam);
        for (CommissionExcel centerList : list) {
            BigDecimal actualWeight = centerList.getActualPickWeightServer();
            if (centerList.getBuyerOrgName().equals(centerList.getSellerOrgName())) {//
                if ("consign".equals(centerList.getBuyConsign())) {//本地服务中心品牌店
                    centerList.setCurrOrgBuyerConsignWeight(actualWeight);
                    centerList.setCurrOrgBuyerTempWeight(new BigDecimal(0.00));
                    centerList.setOtherOrgBuyerConsignWeight(new BigDecimal(0.00));
                    centerList.setOtherOrgBuyerTempWeight(new BigDecimal(0.00));
                } else {// 临采
                    centerList.setCurrOrgBuyerTempWeight(actualWeight);
                    centerList.setCurrOrgBuyerConsignWeight(new BigDecimal(0.00));
                    centerList.setOtherOrgBuyerConsignWeight(new BigDecimal(0.00));
                    centerList.setOtherOrgBuyerTempWeight(new BigDecimal(0.00));
                }
            } else {
                if ("consign".equals(centerList.getSellConsign())) {//跨地服务中心品牌店
                    centerList.setCurrOrgBuyerTempWeight(new BigDecimal(0.00));
                    centerList.setCurrOrgBuyerConsignWeight(new BigDecimal(0.00));
                    centerList.setOtherOrgBuyerConsignWeight(actualWeight);
                    centerList.setOtherOrgBuyerTempWeight(new BigDecimal(0.00));

                } else {//临采
                    centerList.setCurrOrgBuyerTempWeight(new BigDecimal(0.00));
                    centerList.setCurrOrgBuyerConsignWeight(new BigDecimal(0.00));
                    centerList.setOtherOrgBuyerConsignWeight(new BigDecimal(0.00));
                    centerList.setOtherOrgBuyerTempWeight(actualWeight);
                }
            }
        }
        return list;
    }

    /**
     *
     * 服务中心提成历史数据统计汇总报表导出功能
     * @param queryParam
     * @return
     */
    @Override
    public List<ReportCollect>  queryAllByMonth(ReportRewardQuery queryParam){
        return reportRebateRecordDao.queryAllByMonth(queryParam);
    }
    /**
     *
     * 服务中心提成历史数据插入表中
     * @param
     * @return
     */
    public boolean insertCommission(){
        ReportRewardQuery queryParam = new ReportRewardQuery();
        queryParam.setMonth(Tools.dateToStr(Tools.getAfterMonth(new Date(), -1), "yyyyMM"));
        List<CommissionExcel> commissionList =  findByParam(queryParam);
        for(CommissionExcel commissionExcel : commissionList){
            commissionExcel.setMonth(queryParam.getMonth());
        }
        reportRebateRecordDao.batchInsertCommission(commissionList);
        return true;
    }
}
