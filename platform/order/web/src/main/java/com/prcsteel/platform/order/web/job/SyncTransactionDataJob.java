package com.prcsteel.platform.order.web.job;

import com.prcsteel.platform.common.utils.Tools;
import com.prcsteel.platform.order.service.payment.BankOriginalService;
import com.prcsteel.rest.bdl.payment.spdb.AccountService;
import com.prcsteel.rest.bdl.payment.spdb.model.request.RequestBody;
import com.prcsteel.rest.bdl.payment.spdb.model.request.RequestHead;
import com.prcsteel.rest.bdl.payment.spdb.model.response.Body;
import com.prcsteel.rest.bdl.payment.spdb.model.response.ResponseData;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by rolyer on 15-7-20.
 */
@Component
public class SyncTransactionDataJob extends CbmsJob {

    private static final Logger logger = LoggerFactory.getLogger(SyncTransactionDataJob.class);
    public static final int CXF_CLIENT_CONNECT_TIMEOUT = 60 * 1000;  //与获取服务端连接的超时时间,单位为毫秒
    public static final int CXF_CLIENT_RECEIVE_TIMEOUT = 60 * 1000;  //获取连接后接收数据的超时时间,单位为毫秒

    @Autowired
    private BankOriginalService bankOriginalService;

    @Value("${quartz.job.spdb.bisafe.address}")
    private String spdbBisafeAddress;  //浦发BiSafe软件配置地址
    @Value("${quartz.job.spdb.transCode}")
    private String transCode;  //交易代码
    @Value("${quartz.job.spdb.signFlag}")
    private String signFlag;  //一般默认为1,指定类交易为0
    @Value("${quartz.job.spdb.masterID}")
    private String masterID;  //客户号
    @Value("${quartz.job.spdb.acctNo}")
    private String acctNo;  //账号
    @Value("${quartz.job.spdb.queryNumber}")
    private String queryNumber;  //查询的笔数,默认为20
    @Value("${quartz.job.spdb.packetPrefix}")
    private String packetPrefix;
    private Integer beginNumber;  //查询的起始笔数,默认为1

    @Override
    public void execute(){
        if (isEnabled()) {
            logger.debug("Transaction data synchronization job execute start");
            try {
                String beginDate = Tools.dateToStr(new Date(), "yyyyMMdd");  //起始时间
                String endDate = beginDate;  //结束时间
                //查询的起始笔数
                beginNumber = bankOriginalService.selectBeginNumber(beginDate) + 1;

                //报文流水号当天必须唯一
                String packetID = packetPrefix + System.currentTimeMillis();
                //发送报文时间戳
                String timeStamp = Tools.dateToStr(new Date());

                RequestHead requestHead = new RequestHead();
                requestHead.setTransCode(transCode);  //交易码
                requestHead.setSignFlag(signFlag);  //签名标志1：表示数据经过签名
                requestHead.setMasterID(masterID);  //客户号
                requestHead.setPacketID(packetID);  //对应上送的报文号
                requestHead.setTimeStamp(timeStamp);  //时间戳

                RequestBody requestBody = new RequestBody();
                requestBody.setAcctNo(acctNo);  //账号
                requestBody.setBeginDate(beginDate);  //开始时间
                requestBody.setEndDate(endDate);  //结算时间
                requestBody.setQueryNumber(Integer.parseInt(queryNumber));  //
                requestBody.setBeginNumber(beginNumber);
                requestBody.setTransAmount(0d);
                requestBody.setSubAccount("");
                requestBody.setSubAcctName("");

                //创建客户端代理工厂
                JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
                //注册接口
                factory.setServiceClass(AccountService.class);
                //设置地址
                factory.setAddress(spdbBisafeAddress);
                AccountService accountService = (AccountService) factory.create();
                //设置超时时间
                setTimeout(accountService);
                //返回
                ResponseData responseData = accountService.queryAccountDetail(requestHead, requestBody);

                boolean success = responseData.isSuccess();
                if (success) {
                    Body body = responseData.getBody();
                    bankOriginalService.saveBankOriginal(body, requestHead, requestBody, beginNumber);
                }
                logger.debug("Transaction data synchronization job execute end");
            } catch (Exception e) {
                logger.debug("Quartz SyncTransactionDataJob Exception", e);
            }
        }
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    /**
     * 设置超时时间
     * @param service
     */
    private void setTimeout(Object service) {
        Client proxy = ClientProxy.getClient(service);
        HTTPConduit conduit = (HTTPConduit) proxy.getConduit();
        HTTPClientPolicy policy = new HTTPClientPolicy();
        policy.setConnectionTimeout(CXF_CLIENT_CONNECT_TIMEOUT);
        policy.setReceiveTimeout(CXF_CLIENT_RECEIVE_TIMEOUT);
        conduit.setClient(policy);
    }
}
