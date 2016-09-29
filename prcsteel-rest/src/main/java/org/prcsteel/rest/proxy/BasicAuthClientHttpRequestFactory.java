package org.prcsteel.rest.proxy;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

/**
 * 使用基础验证的httpClient工厂
 * @author zhoukun
 */
public class BasicAuthClientHttpRequestFactory extends SimpleClientHttpRequestFactory {
	
	private String baseUrl;
	
	private String username;
	
	private String password;
	
	private String charset = "utf-8";
	
	private Map<String, String> header = new HashMap<>();

	public Map<String, String> getHeader() {
		return header;
	}

	public void setHeader(Map<String, String> header) {
		this.header = header;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	@Override
	public ClientHttpRequest createRequest(URI uri, HttpMethod httpMethod) throws IOException {
		try {
			uri = new URI(baseUrl + uri.toString());
		} catch (URISyntaxException e) {
			throw new RuntimeException(String.format("Can't build request uri,baseUrl: %s,originalUrl: %s",baseUrl,uri),e);
		}
		ClientHttpRequest request = super.createRequest(uri, httpMethod);
		String auth = username + ":" + password;
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(charset));
        String authHeader = "Basic " + new String(encodedAuth);
        HttpHeaders httpHeaders = request.getHeaders();
        httpHeaders.add("Authorization", authHeader);
        if(header != null && header.size() > 0){
        	for (Entry<String, String> h : header.entrySet()) {
        		httpHeaders.add(h.getKey(),h.getValue());
			}
        }
        return request;
	}
}
