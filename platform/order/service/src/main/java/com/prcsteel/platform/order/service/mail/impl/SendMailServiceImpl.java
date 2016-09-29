package com.prcsteel.platform.order.service.mail.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.prcsteel.platform.order.service.report.ReportOrgDayService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prcsteel.platform.common.utils.ExcelUtil;
import com.prcsteel.platform.common.utils.SendMailHelper;
import com.prcsteel.platform.common.utils.Tools;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.order.model.datatable.Column;
import com.prcsteel.platform.acl.model.dto.ReportMailSettingDto;
import com.prcsteel.platform.order.model.enums.ReportDayRowName;
import com.prcsteel.platform.acl.model.enums.ReportMailAttachmentType;
import com.prcsteel.platform.acl.model.enums.ReportMailPeriodType;
import com.prcsteel.platform.acl.model.model.Organization;
import com.prcsteel.platform.acl.model.model.ReportMailSetting;
import com.prcsteel.platform.order.model.query.ReportOrgDayQuery;
import com.prcsteel.platform.order.service.mail.SendMailService;
import com.prcsteel.platform.acl.service.ReportMailSettingService;

@Service("sendMailService")
public class SendMailServiceImpl implements SendMailService{
private static Logger log = Logger.getLogger(SendMailServiceImpl.class);
	
	@Resource
	private ReportMailSettingService reportMailSettingService;

	@Resource
    private ReportOrgDayService reportOrgDayService;
	
	private final static int MAIL_SEND_TIME_DIVATION_FOR_SECONDS = 120;
	
	@Transactional
	public void sendReportMail() {
		log.debug("execute ReportMailSettingService...");
		try{
			ReportMailSettingDto query = new ReportMailSettingDto();
			List<ReportMailSettingDto> reportmailSettings = reportMailSettingService.selectByParam(query);
			
	    	Calendar curCal = Calendar.getInstance();

	    	for (ReportMailSettingDto setting : reportmailSettings) {
				
	    		//如果更新不成功，说明正在执行邮件发送操作，所以跳过当前
//	    		setting.setModificationNumber(setting.getModificationNumber()+1);
	    		ReportMailSetting updateObj = new ReportMailSetting();
	    		updateObj.setId(setting.getId());
	    		updateObj.setModificationNumber(setting.getModificationNumber()+1);
	    		boolean isTrans = reportMailSettingService.update(updateObj);
	    		if(!isTrans) continue;
	    		
	    		//按周来发送邮件类型
	    		if(setting.getPeriodType().equals(ReportMailPeriodType.WEEK)){
	    			
	    			//一个星期是哪几天需要发送 : 星期格式： 1,2,3,4,5,6,7 ==> 日 --> 六  一一对应
	    			List<String> weeks = Arrays.asList(setting.getWeeks().split(","));
	    			
	    			//今天是不是需要发送
	    			int c = curCal.get(Calendar.DAY_OF_WEEK);
	    			if(weeks.contains(curCal.get(Calendar.DAY_OF_WEEK)+"")){
	    				//发送邮件附件 
	    				processSendMail(setting, curCal);
	    			}
	    			
	    		}else if(setting.getPeriodType().equals(ReportMailPeriodType.MONTH1)){
	    			//按每月的第几天
	    			
	    			int curDay = curCal.get(Calendar.DAY_OF_MONTH);
	    			//月分间隔，  是不是这一天
	    			if(isDurationMonth(curCal, setting.getLastSend(), setting.getMonthDuration()) 
	    					&& setting.getDay()==curDay){
	    				//发送邮件附件 
	    				processSendMail(setting, curCal);
	    			}
	    		}else if(setting.getPeriodType().equals(ReportMailPeriodType.MONTH2)){
	    			//按每个月的第几个星期的星期几
	    			int weekCount = curCal.getActualMaximum(Calendar.WEEK_OF_MONTH);
	    			int week = curCal.get(Calendar.WEEK_OF_MONTH);	//获取是本月的第几周
	    			
	    			int day = curCal.get(Calendar.DAY_OF_WEEK);
	    			
	    			//是不是到发送的月份
	    			//本周是不是要发送的那周。
	    			//或者 本周是不是最后一周
	    			//今天是不是要发送的日期
	    			if(isDurationMonth(curCal, setting.getLastSend(), setting.getMonthDuration())
	    					&& week == setting.getWeekDuration() 
	    					|| (setting.getWeekDuration()==5 && week==weekCount)){
	    				int settingWeek = -1;
	    				String weekStr = setting.getWeeks();
	    				if(weekStr.contains(",")){
	    					weekStr = weekStr.substring(0, weekStr.indexOf(","));
	    				}
	    				settingWeek = Integer.parseInt(weekStr);
	    				
	    				if(day==settingWeek){
	    					//发送邮件附件 
	    					processSendMail(setting, curCal);
	    				}
	    			}
	    			
	    		}
	    		
			}
		}catch (Exception e){
			log.error(e.getMessage(), e);
		}
	}
	/**
	 * 发送邮件
	 * @param setting
	 * @param curCal
	 * @throws IOException 
	 */
	private void processSendMail(ReportMailSettingDto setting, Calendar curCal) throws IOException{
		//是不是在需要发送的时间区间内
		//是不是已经发送过。
		boolean duration = isDuration(curCal, setting.getSendTime());
		boolean send = isSend(setting.getLastSend(), curCal);
		
//		try {
//			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			log.debug("duration:"+duration+", send:"+send+", currTime:"+sdf1.format(curCal.getTime())+", lastTime："+sdf1.format(setting.getLastSend()));
//		} catch (Exception e) {
//		}
		
		if(duration && send){
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			
			Calendar from = null;
			Calendar to = null;
			String filename = null;
			ReportOrgDayQuery query = new ReportOrgDayQuery();
			if(ReportMailAttachmentType.MONTHLY.getCode().equals(setting.getAttachment())){  //月报, 上月的月报
				from = (Calendar) curCal.clone();
				from.add(Calendar.MONTH, -1);
				from.set(Calendar.DAY_OF_MONTH, 1);
				
				to = (Calendar) from.clone();	
				to.set(Calendar.DAY_OF_MONTH, to.getMaximum(Calendar.MONTH));
				
				query.setStartPageTimeStr(sdf.format(from.getTime()));
		        query.setEndPageTimeStr(sdf.format(to.getTime()));
		        
		        //文件： 
		        filename = ReportMailAttachmentType.MONTHLY.getName()+"("+sdf.format(curCal.getTime())+")";
			}else{//日报：从当月第一天，到发送时刻这一天。
				from = (Calendar) curCal.clone();
				from.set(Calendar.DAY_OF_MONTH, 1);
				
				to = (Calendar) curCal.clone();
				to.add(Calendar.DAY_OF_MONTH, 1);
				
				query.setStartPageTimeStr(sdf.format(from.getTime()));
				query.setEndPageTimeStr(sdf.format(to.getTime()));
		        filename = ReportMailAttachmentType.DAILY.getName()+"("+sdf.format(curCal.getTime())+")";
			}
			
			query.setDateOrderBy("ASC");
			List<Map<String, Object>> list = reportOrgDayService.getOrgDayList(query);
			
	        //表头
	        //数据
			InputStream in = ExcelUtil.generateExcel(buildOrgDayTitlesList(), buildOrgDayPageDataList(list));
			
			File attachment = new File(System.currentTimeMillis()+".xls");
			if(!attachment.exists()){
				try {
					attachment.createNewFile();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
				
			FileOutputStream out = new FileOutputStream(attachment);
			byte bt[] = new byte[1024];
			int bytereate = 0;
			while ((bytereate = in.read(bt)) != -1) {  
				out.write(bt, 0, bytereate);
			}
			in.close();
			out.close();
			
			//发送邮件附件 
			sendMail(setting, attachment, filename+".xls");
			
			//删除临时文件
			attachment.delete();
			
			//更新发送时间
//			setting.setLastSend(new Date());
//			reportMailSettingService.update(setting);
			
			ReportMailSetting updateObj = new ReportMailSetting();
    		updateObj.setId(setting.getId());
    		updateObj.setLastSend(new Date());
    		reportMailSettingService.update(updateObj);
		}
	}
	
	/**
	 * 构建Excel表头
	 * @return
	 */
	private String[] buildOrgDayTitlesList() {
        List<String> titlesList = new ArrayList<>();
        titlesList.add("日期");
        titlesList.add("交易数据");

        //服务中心
        List<Organization> orgList = reportOrgDayService.queryAllBusinessOrg();
        orgList.forEach(a -> titlesList.add(a.getName()));

        titlesList.add("当日合计");
        titlesList.add("沉淀资金");
        
        String[] titles = new String[titlesList.size()];
        for(int i=0; i<titlesList.size(); i++){
        	titles[i]=titlesList.get(i);
        }
        return titles;
    }
	
    private List<Object[]> buildOrgDayPageDataList(List<Map<String, Object>> list) {
        List<Column> columns = reportOrgDayService.getOrgDayReportColumns();
        List<Object[]> rows = new ArrayList<>();
        if (list != null) {
            list.forEach(a -> {
            	Object[] row = new Object[columns.size()];
            	for (int i = 0; i < columns.size(); i++) {
                	Object cell = a.get(columns.get(i).getData());
                	row[i]=cell==null?"":cell.toString();
                	
                	//当日合计列格式化 销售金额、采购金额行四舍五入保留2位小数；交易数量行四舍五入保留6位小数
                    if(Constant.REPORT_DAY_COL_NAME_DAYTOTAL.equals(columns.get(i).getData())){
                        String rowType = a.get(Constant.REPORT_DAY_ROW_DATA_TYPE).toString();
                        if (StringUtils.equals(rowType, ReportDayRowName.PurchaseAmount.getCode())
                            || StringUtils.equals(rowType, ReportDayRowName.SaleAmount.getCode())){
                        	row[i] = formatBigDecimalForExcle((BigDecimal) cell, Constant.MONEY_PRECISION);
                        }else if (StringUtils.equals(rowType, ReportDayRowName.Weight.getCode())){
                        	row[i] = formatBigDecimalForExcle((BigDecimal)cell, Constant.WEIGHT_PRECISION);
                        }
                    }
                	
				}
            	rows.add(row);
            });
        }
        return rows;
    }
    
    /**
     * 得到指定的精度，并转换成string
     * @param b
     * @param len
     * @return
     */
    private String formatBigDecimalForExcle(BigDecimal b,int len){
        if (b == null) return "";
        return Tools.valueOfFormattedBigDecimal(b,len) ;
     }
    
	private void sendMail(ReportMailSettingDto setting, File attachment, String filename){
		if(log.isDebugEnabled()){
			String out = "receiver:"+setting.getReceiver()+", 周期类型："+setting.getPeriodType()+", weeke:"+setting.getWeeks()
			+",";
			log.debug("send report mail..."+out);
		}
		SendMailHelper.sendAttatchment(setting.getContent(), setting.getTitle(), 
				setting.getReceiver(), attachment, filename);
	}
	
	/**
	 * 是不是已经发送过
	 * @param lastupdate
	 * @param currCal
	 * @return
	 */
	private boolean isSend(Date lastupdate, Calendar currCal){
		//lastupdate为空，表示从来没有发送过。
		if(lastupdate==null)
			return true;
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		if(sdf.format(lastupdate).equals(sdf.format(currCal.getTime())))
			return false;
		return true;
		
	}

	/**
	 * 看看是不是到发送的时间了
	 * @param curCal
	 * @param sendTime
	 * @return
	 */
	private boolean isDuration(Calendar curCal, String sendTime){
		
		//取秒
		long curSecond = curCal.get(Calendar.HOUR_OF_DAY)*3600
				+ curCal.get(Calendar.MINUTE)*60;
		
		//时间格式 ：HH:ss
		String[] times = sendTime.split(":");
		long second = Long.parseLong(times[0])*3600+Long.parseLong(times[1])*60;
		
		// 12:01 curr
		// 12:00 set
		
		//是不是在指定的时间之内
		if(curSecond >= second && curSecond < second + MAIL_SEND_TIME_DIVATION_FOR_SECONDS){
			return true;
		}
		return false;
		
	}
	
	/**
	 * 判断发送的月份间隔，是否每间隔的指定月份数
	 * @param curCal
	 * @param lastupdate
	 * @return
	 */
	private boolean isDurationMonth(Calendar curCal, Date lastupdate, int durationMonth){
		
		if(lastupdate==null) return true;
		
		Calendar beforeCal = Calendar.getInstance();
		beforeCal.setTime(lastupdate);
		
		int currMonth = curCal.get(Calendar.MONTH);
		int beforeMonth = beforeCal.get(Calendar.MONTH);
		if(currMonth-beforeMonth >=durationMonth)
			return true;
		return false;
	}
	
}
