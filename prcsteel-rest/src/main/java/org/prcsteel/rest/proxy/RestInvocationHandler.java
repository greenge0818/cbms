package org.prcsteel.rest.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.prcsteel.rest.annotation.RestApi;
import org.prcsteel.rest.annotation.RestMapping;
import org.prcsteel.rest.annotation.UrlParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * 
 * @author zhoukun
 */
@Component("restInvocationHandler")
public class RestInvocationHandler implements InvocationHandler,ApplicationContextAware {

	private static final Logger logger = LoggerFactory.getLogger(RestInvocationHandler.class);
	
	private ApplicationContext applicationContext;

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		RestMapping rm = method.getAnnotation(RestMapping.class);
		RestApi restApi = method.getDeclaringClass().getAnnotation(RestApi.class);
		ClientHttpRequestFactory clientRequestFactory = (ClientHttpRequestFactory) applicationContext.getBean(restApi.restServer());
		
		String uri = buildUri(rm,restApi, method, args);
		logger.debug("REST API SERVICE Request Url: ", uri);
		RestTemplate restTemplate = new RestTemplate(clientRequestFactory);
		
		RequestEntity<?> requestEntity = buildRequestEntity(method,args,rm.method(),uri);
		try{
			Class<?> retType = method.getReturnType();
			if(retType.equals(Void.TYPE)){
				retType = null;
			}
			return restTemplate.exchange(uri, requestEntity.getMethod(), requestEntity, retType).getBody();
		}catch(HttpServerErrorException e){
			logger.error("Send request error.\r\n" + e.getResponseBodyAsString(),e);
			throw e;
		}
	}
	
	private RequestEntity<Object> buildRequestEntity(Method method, Object[] args, RequestMethod requestMethod,String url) throws URISyntaxException{
		Parameter[] params = method.getParameters();
		if(args != null && args.length > 0){
			for (int i = 0; i < args.length; i++) {
				if (params[i].getAnnotation(UrlParam.class) == null) {
					return new RequestEntity<Object>(args[i],HttpMethod.valueOf(requestMethod.toString()), new URI(url));
				}
			}
		}
		return new RequestEntity<Object>(HttpMethod.valueOf(requestMethod.toString()), new URI(url));
	}

	private String buildUri(RestMapping rm, RestApi restApi,Method method, Object[] args) {
		Map<String, Object> queryVariables = new HashMap<>();
		String uri = rm.value();
		if(args == null || args.length == 0){
			return uri;
		}
		Parameter[] params = method.getParameters();
		for (int i = 0; i < args.length; i++) {
			Parameter p = params[i];
			Object o = args[i];
			UrlParam up = p.getAnnotation(UrlParam.class);
			if (up != null && o != null) {
				if (StringUtils.isEmpty(up.value())) {
					uri = fillUrlParamsWithObject(o,uri,queryVariables);
				} else {
					uri = replaceUrlPlaceHolder(queryVariables, uri, o, up);
				}
			}
		}
		if(queryVariables.size() > 0){
			List<String> qps = new LinkedList<>();
			for (Entry<String, Object> qv : queryVariables.entrySet()) {
				if(qv.getValue() == null){
					continue;
				}
				qps.add(qv.getKey() + "=" + qv.getValue().toString());
			}
			String query = String.join("&", qps);
			if(query.length() > 0){
				if(uri.contains("?")){
					uri += "&" + query;
				}else{
					uri += "?" + query;
				}
			}
		}
		return uri;
	}

	private String replaceUrlPlaceHolder(Map<String, Object> queryVariables, String url, Object o, UrlParam up) {
		String placeHolder = formatPlaceHolder(up.value());
		if(url.contains(placeHolder)){
			url = url.replace(placeHolder, o.toString());
		}else{
			queryVariables.put(up.value(), o);
		}
		return url;
	}

	private String fillUrlParamsWithObject(Object ins, String url,Map<String, Object> queryVariables) {
		Method[] methods = ins.getClass().getMethods();
		try {
			for (Method method : methods) {
				String mn = method.getName();
				if (!mn.startsWith("get") && !mn.startsWith("is")) {
					continue;
				}
				if(mn.startsWith("getClass")){
					continue;
				}
				Object o = method.invoke(ins);
				if(o == null){
					continue;
				}
				String pn = mn.substring(mn.startsWith("get") ? 3 : 2);
				if(pn.length() == 1){
					pn = pn.toLowerCase();
				}else{
					pn = Character.toLowerCase(pn.charAt(0)) + pn.substring(1);
				}
				String placeHolder = formatPlaceHolder(pn);
				if(url.contains(placeHolder)){
					url = url.replace("{" + pn + "}", o.toString());
				}else{
					queryVariables.put(pn, o);
				}
			}
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			logger.error("Read field value failed!",e);
			throw new RuntimeException("Read field value failed!",e);
		}
		return url;
	}
	
	private String formatPlaceHolder(String key){
		return String.format("{%s}", key);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
