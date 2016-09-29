package com.prcsteel.platform.account.web.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;

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
                 if(!doProcess(msgText)){
                     logger.error("doProcess 处理失败!");

            }
            }  
            catch (JMSException jmsEx_p)  
            {  
                 String errMsg = txtMsgErr;  
                 logger.error(errMsg+"\t\r\n{}", jmsEx_p);  
            } 
        }
        if ( message instanceof ObjectMessage )
        {
            try  
            {  
                 Object msgObject = ((ObjectMessage) message).getObject();
                 if(!doProcess(msgObject)){
                     logger.error("doProcess 处理失败!");
                 }
            }  
            catch (JMSException jmsEx_p)  
            {  
                 String errMsg = objMsgErr;  
                 logger.error(errMsg+"\t\r\n{}", jmsEx_p);  
            } 
        }
    }
}