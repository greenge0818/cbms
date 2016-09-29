package com.prcsteel.platform.order.web.job;

import com.prcsteel.cxfrs.service.bdl.model.history.Rd;
import com.prcsteel.platform.common.utils.Tools;
import com.prcsteel.platform.order.service.QuartzJobService;
import com.prcsteel.platform.order.service.payment.BdlService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by kongbinheng on 15-10-28.
 */
@Component
public class ICBCBdlDataJob extends CbmsJob {

    private static final Logger logger = LoggerFactory.getLogger(ICBCBdlDataJob.class);
    private static final String PATH = "/icbc/query";

    @Resource
    private BdlService icbcBdlService;
    @Value("${quartz.job.bdl.address}")
    private String address;
//    @Value("${quartz.job.bdl.enabled}")
//    private boolean enabled;

    @Value("${quartz.job.name.icbcb}")
    private String jobName;
    @Resource
    private QuartzJobService quartzJobService;

    @Override
    public void execute(){
        if (isEnabled()) {
            logger.info("icbc bdl data job execute start");
            try {
                String beginDate = Tools.dateToStr(new Date(), "yyyyMMdd");  //起始时间
                String endDate = beginDate;  //结束时间
                //查询的起始笔数
                //beginNumber = icbcBdlService.queryBeginNumber(beginDate, endDate) + 1;
                //beginDate = "20151010";
                //endDate = "20151022";
                //beginNumber = "";
                //String beginNumber = icbcBdlService.queryNextTag(beginDate);
                String beginNumber = "";
                //if(StringUtils.isBlank(beginNumber) || "null".equals(beginNumber.toLowerCase())) beginNumber = "";
                String paramStr = "?beginDate=" + beginDate + "&endDate=" + endDate + "&beginNumber=" + beginNumber;
                URL url;
                try {
                    url = new URL(address + PATH + paramStr);
                    //logger.info("icbc bdl data job execute：" + url);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoOutput(true);
                    connection.setRequestMethod("POST");
                    OutputStream os = connection.getOutputStream();
                    OutputStreamWriter osw = new OutputStreamWriter(os);
                    osw.flush();
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                    String jsonStr = "";
                    //logger.info("icbc bdl data job execute1：" + url);
                    for (String line = br.readLine(); line != null; line = br.readLine()) {
                        jsonStr = jsonStr + line;
                    }
                    //logger.info("icbc bdl data job execute1");
                    br.close();
                    if(!StringUtils.isBlank(jsonStr)){
                       // logger.info("icbc bdl data job execute2");
                        ObjectMapper mapper = new ObjectMapper();
                        try {
                            JsonNode jsonNode = mapper.readTree(jsonStr);
                            ArrayNode dataArrayNode = (ArrayNode) jsonNode.path("data");
                            String message = jsonNode.path("message").asText();
                            if("Success".equals(message)){
                               // logger.info("icbc bdl data job execute3");
                                ArrayNode ebArrayNode = (ArrayNode) dataArrayNode.get(1).path("eb");
                                ArrayNode outArrayNode = (ArrayNode) ebArrayNode.get(0).path("out");
                                String nextTag = outArrayNode.get(0).path("nextTag").asText();
                                ArrayNode rdArrayNode = (ArrayNode) outArrayNode.get(0).path("rd");
                                List<Object> list = getJSONArrayToList(new Rd(), rdArrayNode.toString());
                                if(list != null){
                                   // logger.info("icbc bdl data job execute4");
                                    icbcBdlService.saveQuery(list, nextTag);
                                   // logger.info("icbc bdl data job execute end");
                                }
                            }
                        } catch (IOException e) {
                            logger.error("工商银企直连转换报文出错：", e);
                        }
                    }
                } catch (Exception e) {
                    logger.error("工商银企直连发送报文出错：", e);
                }
            } catch (Exception e) {
                logger.error("Quartz SPDBSyncTransactionDataJob Exception", e);
            }

            //Job执行（完成）状态更新。
            quartzJobService.finish(jobName);

            logger.info("icbc bdl data job execute end");
        }
    }

    @Override
    public boolean isEnabled() {
        // Job启用检查及执行（进行中）状态更新。
        return quartzJobService.starting(jobName, "");
    }

    /**
     * 将json字符串转换为list
     * @param <T>
     * @param clazz
     * @param jsons
     * @return
     */
    private <T> List<T> getJSONArrayToList(T clazz, String jsons) {
        List<T> objs = null;
        JSONArray jsonArray = (JSONArray) JSONSerializer.toJSON(jsons);
        if(jsonArray != null){
            objs = new ArrayList<T>();
            List list = (List) JSONSerializer.toJava(jsonArray);
            for(Object o : list){
                JSONObject jsonObject = JSONObject.fromObject(o);
                T obj = (T) JSONObject.toBean(jsonObject, clazz.getClass());
                objs.add(obj);
            }
        }
        return objs;
    }
}
