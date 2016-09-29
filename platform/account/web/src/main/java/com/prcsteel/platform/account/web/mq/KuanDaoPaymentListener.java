package com.prcsteel.platform.account.web.mq;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.prcsteel.platform.account.model.dto.AccountKuanDaoDto;
import com.prcsteel.platform.account.model.enums.AccountTransApplyType;
import com.prcsteel.platform.account.model.model.AccountTransLog;
import com.prcsteel.platform.account.model.query.AccountInfoFlowSearchQuery;
import com.prcsteel.platform.account.service.AccountFundService;
import com.prcsteel.platform.account.service.AccountService;
import com.prcsteel.platform.account.service.AccountTransLogService;
import com.prcsteel.platform.account.service.impl.MD5SignVerifyUtil;
import com.prcsteel.platform.order.model.enums.AssociationType;
import com.prcsteel.platform.order.model.enums.PayType;

/**
 * 需求说明：
 *款道2.0支付单流程处理完成之后会对客户进行充值操作，
 * 修改客户账户余额，需要账户体系提供接口。
 *处理方式：
 *款道2.0以发送MQ方式，通知账户体系进行充值操作。
 * @author wangxiao
 *
 */
@Component
public class KuanDaoPaymentListener extends QueueListenerAbstract {
    private Logger logger = LoggerFactory.getLogger(getClass());
	
    @Autowired
	AccountService accountService;
	@Resource
	AccountFundService accountFundService;
	@Value("${kuandao.domain}")
	private String kuanDaoDomain;  // 接口服务地址

	@Value("${kuandao.sign.cbms.secret}")
	private String kuandaoSignCbmsSecret;  // 接口服务地址
    @Autowired
    AccountTransLogService accountTransLogService;
	
	@Override
	protected boolean doProcess(String reqContent) {
	    
        //将接收的json对象转换为实体对象
		ObjectMapper objMap = new ObjectMapper();
		boolean processFlag = true;
		AccountKuanDaoDto dto = null;
		String status ="00";
		try{

			dto = objMap.readValue(reqContent, AccountKuanDaoDto.class);
			/**
			 * 修改客户账户余额
			 * 如果已经充值，直接通知款道
			 */
			AccountInfoFlowSearchQuery accountInfoFlowSearchQuery = new AccountInfoFlowSearchQuery();
			accountInfoFlowSearchQuery.setAccountId(dto.getAccountId());
			accountInfoFlowSearchQuery.setApplyType(AccountTransApplyType.KUANDAO_PAYMENT.getCode());
			accountInfoFlowSearchQuery.setConsignOrderCode(dto.getKuandaoPayorderId());
			accountInfoFlowSearchQuery.setPayType(PayType.KUANDAO_PAYMENT.name().toLowerCase());
			List<AccountTransLog> accountTransLogList = accountTransLogService.queryFlowByQuery(accountInfoFlowSearchQuery);
			if(accountTransLogList.isEmpty()){
				accountFundService.updateAccountFund(dto.getAccountId(), AssociationType.KUANDAO_PAYMENT,dto.getKuandaoPayorderId(), AccountTransApplyType.KUANDAO_PAYMENT,dto.getAmount(),
						BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO, PayType.KUANDAO_PAYMENT,dto.getOperatorId(),dto.getOperatorName(),new Date(dto.getTransDate()));
			}

		} catch (Exception e) {
			status ="01";
			processFlag = false;
            logger.error("款道充值失败,reqContent="+reqContent, e);
        }
		if(dto != null)
		  noticeKuanDao(dto.getKuandaoPayorderId(),dto.getAccountId(),new Date().getTime(),status);
        return processFlag;
	}

	@Override
	protected boolean doProcess(Object reqObj) {
		return false;
	}
	/**
	 * @Description:款道充值通知接口
	 * @author wangxianjun
	 * @param kuandaoPayorderId	款道订单ID	String	10
	 * @param accountId	客户Id	Long	20
	 * @param transDate	交易时间	Long	20
	 * @param status	充值结果	String	2	00：成功 01：失败
	 * @date 2016年7月15日
	 *
	 */

	public void noticeKuanDao( String kuandaoPayorderId,Long accountId,Long transDate,String status){
		try {
			HttpClient httpClient = new DefaultHttpClient(new ThreadSafeClientConnManager());
			String url = kuanDaoDomain+"/notify/chargResultNotify.html";
			HttpPost post = new HttpPost(url);
			Map<String,String> parmMap = new HashMap<String,String>();
			parmMap.put("kuandaoPayorderId", kuandaoPayorderId);
			parmMap.put("accountId", accountId.toString());
			parmMap.put("transDate", transDate.toString());
			parmMap.put("status", status);
			parmMap.put("sign", MD5SignVerifyUtil.sign(parmMap, kuandaoSignCbmsSecret, HTTP.UTF_8));
			List<BasicNameValuePair> params = new ArrayList<>();
			parmMap.forEach((key,value) -> params.add(new BasicNameValuePair(key, value)));
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
			post.setEntity(entity);
			HttpResponse response = httpClient.execute(post);
			logger.debug("call result:" + EntityUtils.toString(response.getEntity()));
			httpClient.getConnectionManager().shutdown();
		} catch (Exception e) {
			logger.error("款道充值通知接口出错：：款道订单号："+kuandaoPayorderId + "客户ID：" +accountId + "状态：" +status);
		}
	}

}

