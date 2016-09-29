package com.prcsteel.platform.order.service.receipt.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prcsteel.platform.acl.model.enums.SysSettingType;
import com.prcsteel.platform.acl.model.model.SysSetting;
import com.prcsteel.platform.acl.persist.dao.SysSettingDao;
import com.prcsteel.platform.acl.service.OrganizationService;
import com.prcsteel.platform.order.model.dto.InvoiceOutItemDetailDto;
import com.prcsteel.platform.order.model.dto.ReportBuyerInvoiceOutDto;
import com.prcsteel.platform.order.model.enums.InvoiceOutCheckListStatus;
import com.prcsteel.platform.order.model.model.InvoiceOutBack;
import com.prcsteel.platform.order.model.model.InvoiceOutCheckList;
import com.prcsteel.platform.order.model.model.InvoiceOutReceipt;
import com.prcsteel.platform.order.model.query.FphcQuery;
import com.prcsteel.platform.order.model.query.InvoiceOutBackQuery;
import com.prcsteel.platform.order.model.query.ReportBuyerInvoiceOutQuery;
import com.prcsteel.platform.order.model.receipt.Fphc;
import com.prcsteel.platform.order.model.receipt.XsdjHead;
import com.prcsteel.platform.order.model.receipt.XsdjMx;
import com.prcsteel.platform.order.persist.dao.InvoiceOutApplyDetailDao;
import com.prcsteel.platform.order.persist.dao.InvoiceOutBackDao;
import com.prcsteel.platform.order.persist.dao.InvoiceOutCheckListDao;
import com.prcsteel.platform.order.persist.dao.InvoiceOutItemDetailDao;
import com.prcsteel.platform.order.persist.dao.InvoiceOutReceiptDao;
import com.prcsteel.platform.order.persist.dao.ReportBuyerInvoiceOutDao;
import com.prcsteel.platform.order.persist.receipt.FphcDao;
import com.prcsteel.platform.order.persist.receipt.XsdjHeadDao;
import com.prcsteel.platform.order.persist.receipt.XsdjMxDao;
import com.prcsteel.platform.order.service.invoice.impl.InvoiceOutServiceImpl;
import com.prcsteel.platform.order.service.receipt.ReceiptService;
import com.prcsteel.platform.order.service.report.ReportFinanceService;

/**
 * Created by Rolyer on 2015/9/24.
 */
@Service("receiptService")
public class ReceiptServiceImpl implements ReceiptService {
	private static Logger logger = Logger.getLogger(InvoiceOutServiceImpl.class);
	
	private final static String FILTER_ORG = "长沙服务中心";
	private final static String INVOICE_TYPE_PRIVATE_TEXT = "专用发票";
    private final static String INVOICE_TYPE_NORMAL_TEXT = "普通发票";
    
//    private final static String INVOICE_TYPE_PRIVATE = "PRIVATE";
    private final static String INVOICE_TYPE_NORMAL = "NORMAL";

    @Value("${receipt.hosts}")
    private String receiptHosts;

    @Resource
    private XsdjHeadDao xsdjHeadDao;

    @Resource
    private XsdjMxDao xsdjMxDao;

    @Resource
    private InvoiceOutCheckListDao invoiceOutCheckListDao;

    @Resource
    private InvoiceOutItemDetailDao invoiceOutItemDetailDao;

    @Resource
    private InvoiceOutReceiptDao invoiceOutReceiptDao;
    @Resource
    InvoiceOutBackDao invoiceOutBackDao;
    @Resource
    ReportFinanceService reportFinanceService;
    @Resource
    SysSettingDao sysSettingDao;
    @Resource
    FphcDao fphcDao;
    
    @Resource
    InvoiceOutApplyDetailDao invoiceOutApplyDetailDao;
    
    @Value("${quartz.job.spdb.systemId}")
    private String systemId;

    @Resource
    ReportBuyerInvoiceOutDao reportBuyerInvoiceOutDao;
    
    /**
     * 添加单据头部信息
     * @param ck
     * @param temp
     * @param djh
     * @param index
     * @return
     */
    private int addXsdjHead(InvoiceOutCheckList ck, InvoiceOutItemDetailTemp temp, String djh, int index) {

        XsdjHead head = new XsdjHead();
        String taxCode = temp.getTaxCode();
        try {
        	head.setKhsh(taxCode.replace(" ", ""));
		} catch (NullPointerException e) {
			String out =  "BuyerId: "+temp.getBuyerId()+", BuyerName: "+temp.getBuyerName()+", taxCode不能为空";
			logger.error(out, e);
			return -1;
		}
        head.setKhmc(temp.getBuyerName());
        head.setKhdz(temp.getAddr());
        head.setKhdh(temp.getTel());
        head.setKhyh(temp.getBankInfo());
        head.setDjrq(ck.getCreated());
        head.setDjh(djh);
        head.setDjzl(StringUtils.isNotBlank(temp.getInvoiceType()) ? retrieveInvoiceType(temp.getInvoiceType()) : INVOICE_TYPE_PRIVATE_TEXT);
        head.setDubz("0");
        head.setBz(StringUtils.isBlank(temp.getOrgName())?" ":((temp.getOrgName().equals(FILTER_ORG)?" ":temp.getOrgName().replace("服务中心", ""))));
        
        //功能 #8211:同步到爱信诺“备注”字段的值原来为服务中心与大类，现修改为服务中心。
        /*
        //得到所有大类，并去重。
        Set<String> categoryNames = new LinkedHashSet<String>();
        if(temp.getDetails()!=null){
        	 for (InvoiceOutItemDetailDto detail:temp.getDetails()){
             	categoryNames.add(detail.getCategoryName());
             }
        }
        
        //将大类以逗号形式分割，拼成字符串
        String categoryNameStr = "";
        for(String c : categoryNames){
        	categoryNameStr += c+",";
        }
        
        //如果大类为空，则不添加
        if(categoryNameStr.length()>0){
        	
        	//去掉最后的一个逗号
        	if(categoryNameStr.endsWith(",")){
            	categoryNameStr = categoryNameStr.substring(0, categoryNameStr.length()-1);
            }
            
            if(StringUtils.isNotBlank(head.getBz()))
            	head.setBz(head.getBz()+","+categoryNameStr);
            else
            	head.setBz(categoryNameStr);
        }
        */
        
        head.setKpjh(assignHosts(index, StringUtils.isNotBlank(temp.getInvoicedHost()) ? temp.getInvoicedHost() : "0"));

        return xsdjHeadDao.insert(head);
    }

    /**
     * 获取发票类型
     * @param type
     * @return 
     */
    private String retrieveInvoiceType(String type) {
    	if(INVOICE_TYPE_NORMAL.endsWith(type)) {
    		return INVOICE_TYPE_NORMAL_TEXT;
    	} else {
    		return INVOICE_TYPE_PRIVATE_TEXT;
    	}
    }

    /**
     * 分配发票主机
     * @param index
     * @param host
     * @return
     */
    public String assignHosts(int index, String host){
        String h = host != null ? host : receiptHosts;
        String[] hosts = h.split(",");
        int len = hosts.length;

        int v = index%len;
        if (v > len) {
            assignHosts(v, host);
        }

        return hosts[v];
    }

    /**
     * 添加单据明细
     * @param detail
     * @param djh
     * @return
     */
    private int addXsdjMx(InvoiceOutItemDetailDto detail, String djh) {
        String sign = detail.getSignOfSpec() != null ? detail.getSignOfSpec().replace("无", "") : "";
        XsdjMx mx = new XsdjMx();
        mx.setDjh(djh);
        mx.setSpmc(detail.getNsortName());
        mx.setJldw("吨");
        mx.setGgxh(sign + detail.getSpec());
        mx.setSlian(detail.getWeight());
        mx.setSl(BigDecimal.valueOf(0.17));
        mx.setHsje(detail.getAmount());
        mx.setHsdj(BigDecimal.ZERO);
        mx.setZkje(BigDecimal.ZERO);

        return xsdjMxDao.insert(mx);
    }

    private int addInvoiceOutReceipt(InvoiceOutItemDetailDto detail, String djh){
        InvoiceOutReceipt ior = new InvoiceOutReceipt();
        ior.setApplyDetailId(detail.getApplyDetailId());
        ior.setItemDetailId(detail.getId());
        ior.setChecklistId(detail.getChecklistId());
        ior.setAmount(detail.getAmount());
        ior.setWeight(detail.getWeight());
        ior.setDjh(djh);

        return invoiceOutReceiptDao.insert(ior);
    }

    @Transactional
    public void syncReceiptData() {

        //读取CheckList
        List<InvoiceOutCheckList> checkLists = invoiceOutCheckListDao.queryByStatus(InvoiceOutCheckListStatus.StayedInvoiced.getValue());
        for (InvoiceOutCheckList ck : checkLists) {

            //读取item
            List<InvoiceOutItemDetailDto> items = invoiceOutItemDetailDao.queryByChecklistId(ck.getId());
            List<InvoiceOutItemDetailTemp> temps = new ArrayList<>();
            //重组数据
            for (InvoiceOutItemDetailDto item : items) {

                int inx = -1;
                for (int i = 0; i < temps.size(); i++) {
                    if (item.getBuyerId().equals(temps.get(i).getBuyerId())) {
                        inx = i;
                        break;
                    }
                }

                if (inx >= 0) {
                    List<InvoiceOutItemDetailDto> details = temps.get(inx).details;
                    details.add(item);
                    temps.get(inx).setDetails(details);
                } else {
                    InvoiceOutItemDetailTemp t = new InvoiceOutItemDetailTemp();
                    t.setBuyerId(item.getBuyerId());
                    t.setBuyerName(item.getBuyerName());
                    t.setAddr(item.getAddr());
                    t.setTel(item.getTel());
                    t.setTaxCode(item.getTaxCode());

                    t.setBankInfo((StringUtils.isNotBlank(item.getBankNameMain()) ? item.getBankNameMain() : "")
                            + (StringUtils.isNotBlank(item.getBankNameBranch()) ? item.getBankNameBranch() : "")
                            + " "
                            + (StringUtils.isNotBlank(item.getBankAccount()) ? item.getBankAccount() : ""));

                    t.setOrgId(item.getOrgId());
                    t.setOrgName(item.getOrgName());
                    t.setInvoicedHost(item.getInvoicedHost());
                    t.setInvoiceType(item.getInvoiceType());
                    t.setCategoryName(item.getCategoryName());

                    List<InvoiceOutItemDetailDto> details = new ArrayList<>();
                    details.add(item);
                    t.setDetails(details);

                    temps.add(t);
                }
            }

            Boolean flag = null;
            int index = 0;
            for (InvoiceOutItemDetailTemp temp : temps){
                String djh = UUID.randomUUID().toString();
                //单据头信息

                int effct = addXsdjHead(ck, temp, djh, index);
                if (effct>0) {

                	BigDecimal totalAmount = BigDecimal.ZERO;
                	
                    //单据项信息
                    for (InvoiceOutItemDetailDto detail:temp.getDetails()){
                    	
                    	totalAmount = totalAmount.add(detail.getAmount());
                    	
                        if(addXsdjMx(detail, djh) == 0 || addInvoiceOutReceipt(detail, djh) == 0){
                            flag = false;

                            if (!flag) {
                                throw new RuntimeException();
                            }
                        } else {
                            if(flag == null || flag) {
                                flag = true;
                            }
                        }
                    }
                
                	if(flag!=null && flag && temp.getDetails().size()>0){
                		InvoiceOutItemDetailDto dto = temp.getDetails().get(0);
                		reportFinanceService.pushInvoiceToReportInvoiceOut(dto, systemId,djh, totalAmount);
                	}
                    
                }
                index++;
            }

            if (flag!=null && flag){
                int effct = invoiceOutCheckListDao.updateStatusById(ck.getId(), InvoiceOutCheckListStatus.Invoiced.getValue());

                if (effct == 0) {
                    throw new RuntimeException();
                }
            }
        }

    }

    class InvoiceOutItemDetailTemp {
        private Long buyerId;
        private String buyerName;
        private String taxCode;
        private String addr;
        private String tel;
        private String bankInfo;
        private Long orgId;
        private String orgName;
        private String invoicedHost;
        private String invoiceType;
        private String categoryName;

        private List<InvoiceOutItemDetailDto> details;

        public String getCategoryName() {
			return categoryName;
		}

		public void setCategoryName(String categoryName) {
			this.categoryName = categoryName;
		}

		public Long getBuyerId() {
            return buyerId;
        }

        public void setBuyerId(Long buyerId) {
            this.buyerId = buyerId;
        }

        public String getBuyerName() {
            return buyerName;
        }

        public void setBuyerName(String buyerName) {
            this.buyerName = buyerName;
        }

        public String getTaxCode() {
            return taxCode;
        }

        public void setTaxCode(String taxCode) {
            this.taxCode = taxCode;
        }

        public String getAddr() {
            return addr;
        }

        public void setAddr(String addr) {
            this.addr = addr;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public String getBankInfo() {
            return bankInfo;
        }

        public void setBankInfo(String bankInfo) {
            this.bankInfo = bankInfo;
        }

        public List<InvoiceOutItemDetailDto> getDetails() {
            return details;
        }

        public void setDetails(List<InvoiceOutItemDetailDto> details) {
            this.details = details;
        }

        public Long getOrgId() {
            return orgId;
        }

        public void setOrgId(Long orgId) {
            this.orgId = orgId;
        }

        public String getOrgName() {
            return orgName;
        }

        public void setOrgName(String orgName) {
            this.orgName = orgName;
        }

        public String getInvoicedHost() {
            return invoicedHost;
        }

        public void setInvoicedHost(String invoicedHost) {
            this.invoicedHost = invoicedHost;
        }

		public String getInvoiceType() {
			return invoiceType;
		}

		public void setInvoiceType(String invoiceType) {
			this.invoiceType = invoiceType;
		}
    }
    
    /**
     * 从爱信诺系统里拿出已经开过销项票，同步到本地系统
     * @author tuxianming
     */
	@Override
	@Transactional
	public void executeSynInvoiceOutBack() {
		//在设置表里面得到上次执行本操作的时候最后一条id
		SysSetting setting = sysSettingDao.queryByType(SysSettingType.InvoiceOutLastRecord.getCode());
		
		//如果小于1000条，则不再次查询，
		//将数据一条一条的插入到数据库，在插入之前先根据单据号来查询些条数据是不是已经存本地。
		//在插入数据库的时候，生成销项票插入到数据库
		
		//分页，每页长度定义
		int start=0, length=1000;

		Calendar currCal = Calendar.getInstance();
		
		//如果有，同步数据： 最大id， 每次获取1000条， 
		if(setting!=null && !"".equals(setting.getSettingValue())){
			
			FphcQuery query = new FphcQuery();
			query.setId(setting.getSettingValue()).setLength(length).setStart(start);
			
			//同步数据
			String lastId = executeSyncFphcToInvoiceOutBack(query, currCal);
			
			//最后一条记录的id保存到设置表内
			//setting.setSettingValue(lastId);
			if(!"".equals(lastId)){  //如果为空，则说明当前没有同步数据，因此不需要更新数据
				SysSetting set = new SysSetting();
				set.setSettingType(setting.getSettingType());
				set.setSettingValue(lastId);
				set.setDefaultValue(lastId);
				sysSettingDao.updateBySettingTypeSelective(set);
			}
			
		}else{ //如果没有， 同样表示第一次同步数据，因此传入查询条的时候，传当日间
			
			FphcQuery query = new FphcQuery().setStart(start).setLength(length);
			//同步数据
			String lastId = executeSyncFphcToInvoiceOutBack(query, currCal);
			
			//说明第一次同步完成，插入最大id到数据库
			if(lastId!=null && !"".equals(lastId)){
				setting = new SysSetting();
				setting.setCreated(currCal.getTime());
				setting.setCreatedBy(systemId);
				setting.setDefaultValue(lastId);
				setting.setDescription(SysSettingType.InvoiceOutLastRecord.getName());
				setting.setDisplayName(SysSettingType.InvoiceOutLastRecord.getName());
				setting.setLastUpdated(currCal.getTime());
				setting.setLastUpdatedBy(systemId);
				setting.setModificationNumber(0);
				setting.setSettingName(SysSettingType.InvoiceOutLastRecord.getName());
				setting.setSettingType(SysSettingType.InvoiceOutLastRecord.getCode());
				setting.setSettingValue(lastId);
				sysSettingDao.insert(setting);
			}
			
		}
	}
	
	/**
	 * 执行同步到发票回传并同步到本地数据库：inv_invoice_out_back表
	 * @param query
	 * @return 返回最后一条记录的id
	 */
	private String executeSyncFphcToInvoiceOutBack(FphcQuery query, Calendar currCal){
		
		List<Fphc> listFphcs = fphcDao.syncSelect(query);
		//key:value -> djh: List<InvoiceOutBack>
		Map<String, List<InvoiceOutBack>> itemsMap = new HashMap<String, List<InvoiceOutBack>>();
		if(!listFphcs.isEmpty()){
			List<String> djhs = listFphcs.stream().map(fphc -> fphc.getDjh()).collect(Collectors.toList());
			List<InvoiceOutBack> items = invoiceOutBackDao.selectByParams(new InvoiceOutBackQuery().setDjhs(djhs));
			for (InvoiceOutBack item : items) {
				List<InvoiceOutBack> temps = itemsMap.get(item.getDjh());
				if(temps==null){
					temps = new ArrayList<InvoiceOutBack>();
					itemsMap.put(item.getDjh(), temps);
				}
				temps.add(item);
			}
		}
		
		String lastId = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		while(!listFphcs.isEmpty()){
			for (Fphc fphc : listFphcs) {
				
				List<InvoiceOutBack> items = itemsMap.get(fphc.getDjh());
				try {
					if(StringUtils.isNotBlank(fphc.getFphm())){
						
						//发票号与开票日期示例： 01431460,01431461,01431462,01431463,01431173,01431174
						//				 20150923,20150923,20150923,20150923,20150923,20150923
						String[] billNos = fphc.getFphm().split(",");
						String[] billCreates = fphc.getKprq().split(",");
						//一般来说这两个长度是一样的，为了防止出现数组越界异常，取长度小的一个。
						int length = (billNos.length>=billCreates.length)?billCreates.length:billNos.length;
						
						List<InvoiceOutBack> newItems = null;
						for (int i=0; i<length; i++) {
							
							//如果为空则说明该条记录还没有同步到本地数据库
							//items 为空则直接插入， 或者items不为空并且，djh在items里面不存在
							if(items==null || !isExistInLocal(items, fphc)){
								InvoiceOutBack result = processSave(billNos, billCreates, i, currCal, fphc, sdf);
								if(result!=null){
									newItems = (newItems!=null)?newItems:new ArrayList<>();
									newItems.add(result);
								}
							}
						}
						if(newItems!=null){
							itemsMap.put(fphc.getDjh(), newItems);
						}
						
						//插入销项票记录到销项表：如果已经作废，则不存入到销项报表数据库。 作废: 0正常开票 1 作废,
						//items 为空则直接插入
						if(fphc.getZf()==0 && items==null){
							//如果在本地已经在在，则不同步到本地
							List<ReportBuyerInvoiceOutDto> reportBuyerInvoiceOuts = reportBuyerInvoiceOutDao.selectByParams(
                                    new ReportBuyerInvoiceOutQuery().setInvoiceNo(fphc.getDjh()));
                            for (ReportBuyerInvoiceOutDto reportBuyerInvoiceOutDto : reportBuyerInvoiceOuts) {
								ReportBuyerInvoiceOutDto newrecord = new ReportBuyerInvoiceOutDto();
								newrecord.setId(reportBuyerInvoiceOutDto.getId());
								String remark = reportBuyerInvoiceOutDto.getRemark();
								if(remark.startsWith("票号：")){
									remark +=remark+","+fphc.getFphm();
								}else{
									remark = "票号："+fphc.getFphm();
								}
								newrecord.setRemark(remark);
								reportBuyerInvoiceOutDao.updateByPrimaryKeySelective(newrecord);
							}
						}
							
						
					}
				} catch (Exception e) {
					logger.debug(e.getMessage(), e);
				}
				
			}
			lastId = listFphcs.get(listFphcs.size()-1).getId()+"";
			listFphcs = fphcDao.syncSelect(query.setStart(query.getStart()+query.getLength()));
		}
		
		return lastId;
	}
	
	/**
	 * 把记录保存在本地数据误国
	 * @param billNos
	 * @param billCreates
	 * @param index
	 * @param currCal
	 * @param fphc
	 * @param sdf
	 * @return
	 */
	private InvoiceOutBack processSave(String[] billNos, String[] billCreates, 
			int index, Calendar currCal, Fphc fphc, SimpleDateFormat sdf){
		
		try {
			InvoiceOutBack item = new InvoiceOutBack();
			item.setBillno(billNos[index].trim());
			item.setUseful(fphc.getZf());
			item.setDjh(fphc.getDjh());
			
			try {
				item.setBillCreate(sdf.parse(billCreates[index]));
			} catch (ParseException e) {
				logger.info(e.getMessage(), e);
			}
			
			item.setCreated(currCal.getTime());
			item.setCreatedBy(systemId);
			item.setLastUpdated(currCal.getTime());
			item.setLastUpdatedBy(systemId);
			item.setModificationNumber(0);
			
			invoiceOutBackDao.insert(item);
			
			return item;
		} catch (Exception e) {
			return null;
		}
	}
    
	/**
	 * 查看是不是存在数组中
	 * @return
	 */
	private boolean isExistInLocal(List<InvoiceOutBack> items, Fphc fphc){
		for (InvoiceOutBack item : items) {
			if(item.getUseful()==item.getUseful().intValue() 
					&& fphc.getFphm().contains(item.getBillno())){
				return true;
			}
		}
		return false;
	}
	
    
}
