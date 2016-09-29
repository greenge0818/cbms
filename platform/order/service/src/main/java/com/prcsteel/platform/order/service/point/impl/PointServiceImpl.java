
    /**  
    * @Title: PointService.java
    * @Package com.prcsteel.platform.order.service.point
    * @Description: 积分接口
    * @author Green.Ge
    * @date 2016年2月29日
    * @version V1.0  
    */
    
package com.prcsteel.platform.order.service.point.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;


import com.prcsteel.platform.account.persist.dao.AccountContactDao;
import com.prcsteel.platform.core.model.dto.CategoryGroupDto;
import com.prcsteel.platform.core.model.model.Category;
import com.prcsteel.platform.core.persist.dao.CategoryDao;
import com.prcsteel.platform.core.persist.dao.CategoryGroupDao;

import com.prcsteel.platform.account.model.dto.AccountContactOrgUserDto;
import com.prcsteel.platform.account.service.AccountContactService;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.common.model.EarnPointsInput;
import com.prcsteel.platform.common.model.OrderItem;
import com.prcsteel.platform.common.utils.JsonDateValueProcessor;
import com.prcsteel.platform.common.utils.StringUtil;
import com.prcsteel.platform.common.utils.Tools;
import com.prcsteel.platform.order.model.model.ConsignOrder;
import com.prcsteel.platform.order.model.model.ConsignOrderItems;
import com.prcsteel.platform.order.service.point.PointService;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
    * @ClassName: PointService
    * @Description: 积分调用接口
    * @author Green.Ge
    * @date 2016年2月29日
    *
    */
@Service("pointService")
public class PointServiceImpl implements PointService{
	@Resource
	CategoryGroupDao categoryGroupDao;
	@Resource
	CategoryDao categoryDao;
	@Resource
	AccountContactDao accountContactDao;
	@Resource
	AccountContactService accountContactService;

	@Value("${point.server.domain}")
    private String pointServerDomain;  // 接口服务地址
	
	private static String PAYTYPE_YINPIAO = "yinpiao";
	
	private Logger logger = Logger.getLogger(PointServiceImpl.class);
	/**
	    * @Description: 二结增加积分
	    * @author Green.Ge
	    * @date 2016年2月29日
	    *
	    */
	public void earnPoint(ConsignOrder order,List<ConsignOrderItems> items){
		try {
			HttpClient httpClient = new DefaultHttpClient(new ThreadSafeClientConnManager());
			
			HttpGet get = new HttpGet(pointServerDomain);
			// 获取_xsrf
			HttpResponse httpResponse = httpClient.execute(get);
			String csrf = getCSRFtoken(httpResponse);
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();

			
			
			EarnPointsInput epi = new EarnPointsInput();
			List<OrderItem> itemList = new ArrayList<OrderItem>();
			for (ConsignOrderItems consignOrderItem:items) {
				String categoryName = consignOrderItem.getNsortName();
				CategoryGroupDto group = categoryGroupDao.selectByNsortName(categoryName);
				OrderItem item = new OrderItem();
				if(group!=null){
					item.setGroupUuid(group.getCategoryGroupUuid());
					item.setGroupName(group.getCategoryGroupName());
				}
				Category category = categoryDao.selectByName(categoryName);
				if(category==null){
					throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "品名【"+categoryName+"】在系统定义不存在，无法调用积分接口");
				}else{
					item.setCategoryUuid(category.getUuid());
					item.setCategoryName(category.getName());
				}
				item.setWeight(consignOrderItem.getWeight());
				item.setPayType(consignOrderItem.getIsPayedByAcceptDraft()?PAYTYPE_YINPIAO:"");
				itemList.add(item);
			}

			epi.setGenDate(Tools.dateToStr(order.getSecondaryTime()==null?new Date():order.getSecondaryTime(),Constant.DATEFORMAT_YYYYMMDD_HHMMSS));
			epi.setMobile(order.getContactTel());
			epi.setName(order.getContactName());
			AccountContactOrgUserDto accountContactOrgUserDto = accountContactDao.queryAccountContactByOpenId(order.getContactTel());
			if(accountContactOrgUserDto!=null){
				epi.setOpenId(accountContactOrgUserDto.getOpenId());
				epi.setAccount(accountContactOrgUserDto.getAccountName());
			}
			
			epi.setOrderCode(order.getCode());
			epi.setOrderItems(itemList);
			
			String url = pointServerDomain+"/api/point/earnPoints";
			
//			String jsonString = epi.toString();
			JsonConfig jsonConfig = new JsonConfig();  
		    jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor()); 
			String jsonString = JSONObject.fromObject(epi,jsonConfig).toString();
			HttpEntity se = new StringEntity(jsonString, "UTF-8");
			HashMap<String,String> params = epi.toHashMap();
//		    nvps.add(new BasicNameValuePair("input", jsonString));
//		    params.put("input", jsonString);
		    String timestamp = Tools.dateToStr(new Date(), Constant.DATEFORMAT_YYYYMMDD_HHMMSS);
		    nvps.add(new BasicNameValuePair("timestamp", timestamp));
		    params.put("timestamp", timestamp);
		    
		    String token = StringUtil.getSignature(params, Constant.SECRET);
		    nvps.add(new BasicNameValuePair("token", token));
//		    url+="timestamp="+timestamp+"&token="+token;
		    HttpPost httpPost = new HttpPost(url);
//		    httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
			httpPost.setEntity(se);
			httpPost.setHeader("X-CSRF-TOKEN",csrf);
			httpPost.setHeader("content-type", "application/json;charset=utf-8");
			httpPost.setHeader("timestamp",timestamp);
			httpPost.setHeader("token",token);
			httpResponse = httpClient.execute(httpPost);
			System.out.println("call result:"+EntityUtils.toString(httpResponse.getEntity()));
			httpClient.getConnectionManager().shutdown();
		} catch (Exception e) {
			logger.error("调用积分接口出错：订单编号："+order.getCode());
			logger.error(e.getMessage());
		}
	}
	
	/**
	    * @Description: 二结回滚积分
	    * @author Green.Ge
	    * @date 2016年3月31日
	    *
	    */
	public void rollbackOrderPoint(String orderCode){
		try {
			HttpClient httpClient = new DefaultHttpClient(new ThreadSafeClientConnManager());
			
			HttpGet get = new HttpGet(pointServerDomain);
			// 获取_xsrf
			HttpResponse httpResponse = httpClient.execute(get);
			String csrf = getCSRFtoken(httpResponse);
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("orderCode", orderCode));
			
			String url = pointServerDomain+"/api/point/rollbackOrderPoint";
			
			Map<String,String> params  = new HashMap<String,String>();
			params.put("orderCode", orderCode);

		    String timestamp = Tools.dateToStr(new Date(), Constant.DATEFORMAT_YYYYMMDD_HHMMSS);
		    nvps.add(new BasicNameValuePair("timestamp", timestamp));
		    params.put("timestamp", timestamp);
		    String token = StringUtil.getSignature(params, Constant.SECRET);
		    nvps.add(new BasicNameValuePair("token", token));
		    HttpPost httpPost = new HttpPost(url);
		    httpPost.setHeader("X-CSRF-TOKEN",csrf);
		    httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
			httpResponse = httpClient.execute(httpPost);
			System.out.println("call result:"+EntityUtils.toString(httpResponse.getEntity()));
			httpClient.getConnectionManager().shutdown();
		} catch (Exception e) {
			logger.error("调用积分回滚接口出错：订单编号："+orderCode);
		}
	}
	//
	/**
	 * @Description: 微信绑定后调用一下积分系统的用户信息同步接口
	 * @author wangxianjun
	 * @param mobile 手机号
	 * @param openId 微信端唯一标识
	 * @param accountName 公司名称
	 * @date 2016年3月22日
	 *
	 */
	public void syncMemberInfo( String mobile,String openId,String accountName){
		try {
			HttpClient httpClient = new DefaultHttpClient(new ThreadSafeClientConnManager());

			HttpGet get = new HttpGet(pointServerDomain);
			// 获取_xsrf
			HttpResponse httpResponse = httpClient.execute(get);
			String csrf = getCSRFtoken(httpResponse);
	
			Map<String,String> params  = new HashMap<String,String>();
			params.put("mobile", mobile);
			params.put("account", accountName);
			params.put("openId", openId);
			String timestamp = Tools.dateToStr(new Date(), Constant.DATEFORMAT_YYYYMMDD_HHMMSS);
			params.put("timestamp", timestamp);

			String url = pointServerDomain+"/api/point/sync";
			HttpPost httpPost = new HttpPost(url);
			
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			
			nvps.add(new BasicNameValuePair("mobile", mobile));
			nvps.add(new BasicNameValuePair("openId", openId));
			nvps.add(new BasicNameValuePair("account", accountName));
			nvps.add(new BasicNameValuePair("timestamp", timestamp));
			String token = StringUtil.getSignature(params, Constant.SECRET);
			nvps.add(new BasicNameValuePair("token", token));
			
			httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

			httpPost.setHeader("X-CSRF-TOKEN",csrf);
			
			httpResponse = httpClient.execute(httpPost);
			System.out.println("call result:"+EntityUtils.toString(httpResponse.getEntity()));
			httpClient.getConnectionManager().shutdown();
		} catch (Exception e) {
			logger.error("调用用户信息同步接口出错：：手机号码"+mobile);
		}
	}
	public static String getCSRFtoken(HttpResponse httpResponse){
		Header headers[] = httpResponse.getHeaders("Set-Cookie");
		if (headers == null || headers.length == 0) {
			return null;
		}
		String cookie = "";
		String CSRF_TOKEN = "CSRF-TOKEN=";
		for (int i = 0; i < headers.length; i++) {
			cookie = headers[i].getValue();
			 if(cookie.contains(CSRF_TOKEN)){
				 int begin = cookie.indexOf(CSRF_TOKEN);
				 int end = cookie.lastIndexOf("; Path=/");
				
				 String csrf_token = cookie.substring(begin+CSRF_TOKEN.length(),end);
				 return csrf_token;
			 }	
		}
		return "";
	}
}
