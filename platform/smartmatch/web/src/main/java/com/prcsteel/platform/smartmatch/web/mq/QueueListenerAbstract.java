package com.prcsteel.platform.smartmatch.web.mq;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
/**
 * @author xiaohl
 *
 */
public abstract class QueueListenerAbstract implements MessageListener
{
	
	String txtMsgErr = "An error occurred extracting txt message";
	String objMsgErr = "An error occurred extracting obj message";
	private final Logger logger = LoggerFactory.getLogger(getClass());

    protected abstract boolean doProcess(String reqXml) ;
    
    protected abstract boolean doProcess(Object reqObject) ;
    
    
    public void onMessage( final Message message )
    {
        if ( message instanceof TextMessage )
        {
            try  
            {  
                 String msgText = ((TextMessage) message).getText(); 
                 //IntfReceived ir = saveTrack(msgText); //保存报文信息
                 if(!doProcess(msgText)){
                     logger.error("doProcess 处理失败!");
                	 //处理失败
                     //updateTrack(ir);
                 }
            }  
            catch (JMSException jmsEx)
            {  
                 String errMsg = txtMsgErr;  
                 logger.error(errMsg+"\t\r\n{}", jmsEx);
            } 
        }
        if ( message instanceof ObjectMessage )
        {
            try  
            {  
                 Object msgObject = ((ObjectMessage) message).getObject();
                 //IntfReceived ir = saveTrack(msgObject); //保存报文信息
                 if(!doProcess(msgObject)){
                     logger.error("doProcess 处理失败!");
                     //失败
                     //updateTrack(ir);
                 }
            }  
            catch (JMSException jmsEx)
            {  
                 String errMsg = objMsgErr;  
                 logger.error(errMsg+"\t\r\n{}", jmsEx);
            } 
        }
    }
}