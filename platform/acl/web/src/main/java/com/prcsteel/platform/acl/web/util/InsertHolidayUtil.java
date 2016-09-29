package com.prcsteel.platform.acl.web.util;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.prcsteel.platform.acl.model.model.AllHoliday;
/**
 * 将2016年内的所有假日插入到假日表里
 * @author lixiang
 * @time 2016-9-18 下午09:55:11
 */
public class InsertHolidayUtil {
  public static void main(String[] args){
    //驱动程序名
     String driver = "com.mysql.jdbc.Driver"; 
     //要插入的数据库，表
     String url = "jdbc:mysql://localhost:3306/steel_cbms";  
     String user = "root"; 
     String password = "root";
     try {    
               //加载驱动程序
               Class.forName(driver);  
               //连续MySQL 数据库
               Connection conn = DriverManager.getConnection(url, user, password);
               if(!conn.isClosed())
               System.out.println("Succeeded connecting to the Database!");
               //statement用来执行SQL语句
               Statement statement = conn.createStatement();
               
               SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
               java.util.Date start = sdf.parse("2016-01-01");//开始时间
               java.util.Date end = sdf.parse("2016-12-31");//结束时间
               List<Date> lists = dateSplit(start, end);
               
               //-------------------插入周末时间---------------
               if (!lists.isEmpty()) {
                   for (Date date : lists) {
                	   Calendar cal = Calendar.getInstance();
                	    cal.setTime(date);
                       if(cal.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY||cal.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY)
                       {
                    	   System.out.println("插入日期:" + sdf.format(date) + ",周末");
                    	   String insertSql = "INSERT INTO fn_all_holiday (id,title,holiday_date) VALUES('"+UUID.randomUUID()+"',"+"'周末','"+sdf.format(date)+"')";
                    	   statement.executeUpdate(insertSql);
                       }
                   }
               }
               
               //---------------插入节假日时间------------------
               List<AllHoliday> holidays = new ArrayList<AllHoliday>();
               holidays.add(new AllHoliday(UUID.randomUUID().toString(),"元旦", sdf.parse("2016-01-01")));
               holidays.add(new AllHoliday(UUID.randomUUID().toString(),"元旦", sdf.parse("2016-01-02")));
               holidays.add(new AllHoliday(UUID.randomUUID().toString(),"元旦", sdf.parse("2016-01-03")));
               
               holidays.add(new AllHoliday(UUID.randomUUID().toString(),"春节", sdf.parse("2016-02-07")));
               holidays.add(new AllHoliday(UUID.randomUUID().toString(),"春节", sdf.parse("2016-02-08")));
               holidays.add(new AllHoliday(UUID.randomUUID().toString(),"春节", sdf.parse("2016-02-09")));
               holidays.add(new AllHoliday(UUID.randomUUID().toString(),"春节", sdf.parse("2016-02-10")));
               holidays.add(new AllHoliday(UUID.randomUUID().toString(),"春节", sdf.parse("2016-02-11")));
               holidays.add(new AllHoliday(UUID.randomUUID().toString(),"春节", sdf.parse("2016-02-12")));
               holidays.add(new AllHoliday(UUID.randomUUID().toString(),"春节", sdf.parse("2016-02-13")));
               
               holidays.add(new AllHoliday(UUID.randomUUID().toString(),"清明节", sdf.parse("2016-04-02")));
               holidays.add(new AllHoliday(UUID.randomUUID().toString(),"清明节", sdf.parse("2016-04-03")));
               holidays.add(new AllHoliday(UUID.randomUUID().toString(),"清明节", sdf.parse("2016-04-04")));
               
               holidays.add(new AllHoliday(UUID.randomUUID().toString(),"劳动节", sdf.parse("2016-04-30")));
               holidays.add(new AllHoliday(UUID.randomUUID().toString(),"劳动节", sdf.parse("2016-05-01")));
               holidays.add(new AllHoliday(UUID.randomUUID().toString(),"劳动节", sdf.parse("2016-05-02")));
               
               holidays.add(new AllHoliday(UUID.randomUUID().toString(),"端午节", sdf.parse("2016-06-09")));
               holidays.add(new AllHoliday(UUID.randomUUID().toString(),"端午节", sdf.parse("2016-06-10")));
               holidays.add(new AllHoliday(UUID.randomUUID().toString(),"端午节", sdf.parse("2016-06-11")));
               
               holidays.add(new AllHoliday(UUID.randomUUID().toString(),"中秋节", sdf.parse("2016-09-15")));
               holidays.add(new AllHoliday(UUID.randomUUID().toString(),"中秋节", sdf.parse("2016-09-16")));
               holidays.add(new AllHoliday(UUID.randomUUID().toString(),"中秋节", sdf.parse("2016-09-17")));
               
               holidays.add(new AllHoliday(UUID.randomUUID().toString(),"国庆节", sdf.parse("2016-10-01")));
               holidays.add(new AllHoliday(UUID.randomUUID().toString(),"国庆节", sdf.parse("2016-10-02")));
               holidays.add(new AllHoliday(UUID.randomUUID().toString(),"国庆节", sdf.parse("2016-10-03")));
               holidays.add(new AllHoliday(UUID.randomUUID().toString(),"国庆节", sdf.parse("2016-10-04")));
               holidays.add(new AllHoliday(UUID.randomUUID().toString(),"国庆节", sdf.parse("2016-10-05")));
               holidays.add(new AllHoliday(UUID.randomUUID().toString(),"国庆节", sdf.parse("2016-10-06")));
               holidays.add(new AllHoliday(UUID.randomUUID().toString(),"国庆节", sdf.parse("2016-10-07")));
               for(AllHoliday day:holidays) {
            	   //跟周末冲突的，不重复插入
                   String sql = "select count(1) as numbers from fn_all_holiday where holiday_date ='" + sdf.format(day.getHolidayDate()) + "'";
                   //结果集
                   ResultSet rs = statement.executeQuery(sql);
                   boolean hasRecord = false;
                   while(rs.next()) {
                	   if(!"0".equals(rs.getString("numbers"))) {
                		   hasRecord = true;
                	   }
                   }
                   if(!hasRecord) {
                	   System.out.println("插入日期：" + sdf.format(day.getHolidayDate()) + "," + day.getTitle());
                	   String insertSql = "INSERT INTO fn_all_holiday (id,title,holiday_date) VALUES('"+day.getId()+"',"+"'"+day.getTitle()+"','"+sdf.format(day.getHolidayDate())+"')";
                	   statement.executeUpdate(insertSql);
                   }
               }
               
               
               //-------------- 剔除补班时间(周末需要补班的)---------------------
               List<AllHoliday> workDays = new ArrayList<AllHoliday>();
               workDays.add(new AllHoliday(UUID.randomUUID().toString(),"补班", sdf.parse("2016-02-06")));
               workDays.add(new AllHoliday(UUID.randomUUID().toString(),"补班", sdf.parse("2016-02-14")));
               workDays.add(new AllHoliday(UUID.randomUUID().toString(),"补班", sdf.parse("2016-06-12")));
               workDays.add(new AllHoliday(UUID.randomUUID().toString(),"补班", sdf.parse("2016-09-18")));
               workDays.add(new AllHoliday(UUID.randomUUID().toString(),"补班", sdf.parse("2016-10-08")));
               workDays.add(new AllHoliday(UUID.randomUUID().toString(),"补班", sdf.parse("2016-10-09")));
               
               for(AllHoliday day:workDays) {
            	   System.out.println("剔除日期：" + sdf.format(day.getHolidayDate()) + "," + day.getTitle());
            	   String delSql = "delete from fn_all_holiday where holiday_date ='" + sdf.format(day.getHolidayDate()) + "'";
            	   statement.executeUpdate(delSql);
               }
               conn.close();
         }
      catch(ClassNotFoundException e) { 
        System.out.println("Sorry,can't find the Driver!");
        e.printStackTrace();
       }
      catch(SQLException e) {
        e.printStackTrace();
      }
      catch(Exception e) {  
        e.printStackTrace(); 
       }
  }
  
  private static List<Date> dateSplit(java.util.Date start, Date end)
	        throws Exception {
	    if (!start.before(end))
	        throw new Exception("开始时间应该在结束时间之后");
	    Long spi = end.getTime() - start.getTime();
	    Long step = spi / (24 * 60 * 60 * 1000);// 相隔天数

	    List<Date> dateList = new ArrayList<Date>();
	    dateList.add(end);
	    for (int i = 1; i <= step; i++) {
	        dateList.add(new Date(dateList.get(i - 1).getTime()
	                - (24 * 60 * 60 * 1000)));// 比上一天减一
	    }
	    return dateList;
	}

}