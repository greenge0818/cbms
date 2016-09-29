package com.prcsteel.platform.smartmatch.service.impl;

import com.google.gson.Gson;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.smartmatch.model.dto.InquiryOrderMessageQueue;
import com.prcsteel.platform.smartmatch.persist.dao.PurchaseOrderDao;
import com.prcsteel.platform.smartmatch.service.ResourceChangeForMaketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.TextMessage;
import java.util.Date;

/**
 * Created by caochao on 2016/6/22.
 */

@Service("resourceChangeForMaketService")
public class ResourceChangeForMaketServiceImpl implements ResourceChangeForMaketService {
    //日志
    private final Logger logger = LoggerFactory.getLogger(ResourceChangeForMaketServiceImpl.class);

    private JmsTemplate jmsTemplate;

    private String queue;

    @Resource
    private PurchaseOrderDao purchaseOrderDao;

    @Override
    public void send(Object o,User operator) {
        String messageText = new Gson().toJson(o);
        jmsTemplate.send(queue, s -> {
            final TextMessage message = s.createTextMessage();
            message.setText(messageText);
            return message;
        });
        try {
            InquiryOrderMessageQueue msg = new InquiryOrderMessageQueue();
            msg.setCode("");
            msg.setPushBy(operator == null ? "" : operator.getLoginId());
            msg.setReqSource("SMART");
            msg.setCreated(new Date());
            msg.setExt1(messageText);//记录保存的报文
            purchaseOrderDao.insertInquiryMsg(msg);
        } catch (Exception ex) {
            logger.error("MQ消息日志记录失败:" + ex.toString());
        }
    }


    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }
}
