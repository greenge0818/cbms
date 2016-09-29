package com.prcsteel.platform.order.web.job;

import com.prcsteel.platform.common.utils.Tools;
import com.prcsteel.platform.order.service.QuartzJobService;
import com.prcsteel.platform.order.service.payment.BankOriginalService;
import com.prcsteel.rest.bdl.payment.spdb.model.request.RequestBody;
import com.prcsteel.rest.bdl.payment.spdb.model.request.RequestHead;
import com.prcsteel.rest.bdl.payment.spdb.model.response.Body;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

/**
 * Created by kongbinheng on 15-10-28.
 */
@Component
public class SPDBBdlDataJob extends CbmsJob {

    private static final Logger logger = LoggerFactory.getLogger(SPDBBdlDataJob.class);
    private static final String PATH = "/spdb/query";

    @Autowired
    private BankOriginalService bankOriginalService;

//    @Value("${quartz.job.bdl.enabled}")
//    private boolean enabled;
    @Value("${quartz.job.bdl.address}")
    private String address;
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

    @Value("${quartz.job.name.spdbb}")
    private String jobName;
    @Resource
    private QuartzJobService quartzJobService;

    @Override
    public void execute(){
        if (isEnabled()) {
            logger.info("spdb bdl data job execute start");
            try {
                String beginDate = Tools.dateToStr(new Date(), "yyyyMMdd");  //起始时间
                String endDate = beginDate;  //结束时间
                //查询的起始笔数
                beginNumber = bankOriginalService.selectBeginNumber(beginDate) + 1;
                //beginDate = "20151010";
                //endDate = "20151022";
                //beginNumber = 1;
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

                String paramStr = "?beginDate=" + beginDate + "&endDate=" + endDate + "&beginNumber=" + beginNumber;
                URL url;
                try {
                    url = new URL(address + PATH + paramStr);
                   // logger.info("spdb bdl data job execute:" + url);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoOutput(true);
                    connection.setRequestMethod("POST");
                    OutputStream os = connection.getOutputStream();
                    OutputStreamWriter osw = new OutputStreamWriter(os);
                    osw.flush();
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                    String jsonStr = "";
                    //logger.info("spdb bdl data job execute1:" + url);
                    for (String line = br.readLine(); line != null; line = br.readLine()) {
                        jsonStr = jsonStr + line;
                    }
                    br.close();
                    if(!StringUtils.isBlank(jsonStr)){
                        //logger.info("spdb bdl data job execute1");
                        ObjectMapper mapper = new ObjectMapper();
                        try {
                            JsonNode jsonNode = mapper.readTree(jsonStr);
                            ArrayNode dataArrayNode = (ArrayNode) jsonNode.path("data");
                            String message = jsonNode.path("message").asText();
                            //logger.info("spdb bdl data job execute2");
                            if("Success".equals(message)){
                                //logger.info("spdb bdl data job execute3");
                                JsonNode bodyJsonNode = dataArrayNode.get(1);
                                JSONObject jsonObject = new JSONObject().fromObject(bodyJsonNode.toString());
                                Body body = (Body) JSONObject.toBean(jsonObject, Body.class);
                                //logger.info("spdb bdl data job execute4");
                                if(body != null){
                                  //  logger.info("spdb bdl data job execute5");
                                    bankOriginalService.saveBankOriginal(body, requestHead, requestBody, beginNumber);
                                   // logger.info("spdb bdl data job execute end");
                                }else{
                                    logger.info("浦发银企直连返回报文为空！");
                                }
                            }else{
                                logger.info("浦发银企直连返回报文不成功！");
                            }
                        } catch (IOException ioe) {
                            logger.error("浦发银企直连转换报文出错：", ioe);
                        }
                    }
                } catch (Exception e) {
                    logger.error("浦发银企直连发送报文出错：", e);
                }
            } catch (Exception ex) {
                logger.error("Quartz SyncTransactionDataJob Exception", ex);
            }

            //Job执行（完成）状态更新。
            quartzJobService.finish(jobName);
            logger.info("spdb bdl data job execute end");
        }
    }

    @Override
    public boolean isEnabled() {
        // Job启用检查及执行（进行中）状态更新。
        return quartzJobService.starting(jobName, "");
    }
}
