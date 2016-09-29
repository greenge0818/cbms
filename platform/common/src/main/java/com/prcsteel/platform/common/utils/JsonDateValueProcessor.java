
    /**  
    * @Title: JsonDateValueProcessor.java
    * @Package com.prcsteel.platform.common.utils
    * @Description: Json 日期格式化工具
    * @author Green.Ge
    * @date 2016年3月2日
    * @version V1.0  
    */
    
package com.prcsteel.platform.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

/**
    * @ClassName: JsonDateValueProcessor
    * @Description: TODO(这里用一句话描述这个类的作用)
    * @author Green.Ge
    * @date 2016年3月2日
    *
    */

public class JsonDateValueProcessor implements JsonValueProcessor{
    private String format ="yyyy-MM-dd";  
     
    public JsonDateValueProcessor() {  
        super();  
    }  
       
    public JsonDateValueProcessor(String format) {  
        super();  
        this.format = format;  
    }  
   
    public Object processArrayValue(Object paramObject, JsonConfig paramJsonConfig) {  
        return process(paramObject);  
    }  
   
    public Object processObjectValue(String paramString, Object paramObject,  
            JsonConfig paramJsonConfig) {  
        return process(paramObject);  
    }  
       
       
    private Object process(Object value){  
        if(value instanceof Date){    
            SimpleDateFormat sdf = new SimpleDateFormat(format);    
            return sdf.format(value);  
        }    
        return value == null ? "" : value.toString();    
    }  
   
}