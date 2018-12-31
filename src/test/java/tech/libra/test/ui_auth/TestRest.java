package tech.libra.test.ui_auth;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

public class TestRest {

	private static final String grantType = "password";
	private static final String username = "john";
	private static final String password = "123";
	private static final String clientId = "fooClientIdPassword";
	private static final String clientSecret = "secret";
	private static final String getAccessTokenEndpointUrl = "http://localhost:8081/spring-security-oauth-server/oauth/token";

	public static void main(String[] args) {
		
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
			@Override
			public boolean hasError(ClientHttpResponse response) throws IOException {
				HttpStatus httpStatus = response.getStatusCode();

				if (httpStatus == HttpStatus.BAD_REQUEST || httpStatus == HttpStatus.UNAUTHORIZED) return false;
				
				return super.hasError(response);
			}
		});
		
		MultiValueMap<String, String> formData = new LinkedMultiValueMap<String, String>();
		formData.add("grant_type", grantType);
		formData.add("username", username);
		formData.add("password", password);
		
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", getAuthorizationHeader(clientId, clientSecret));
		
		try {
			Map<String, Object> map = postForMap(getAccessTokenEndpointUrl, formData, headers);
			
		} catch (Exception _ex) {
			_ex.printStackTrace();
			throw new BadCredentialsException("username, password, verification code is not correct", _ex);
		}
		
	}
	
	
	private static Map<String, Object> postForMap(String path, MultiValueMap<String, String> formData, HttpHeaders headers) {
		if (headers.getContentType() == null) {
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		}
		
		Map<String, Object> httpErrorResponseMap = new HashMap<>();
		
		RestTemplate restTemplate = new RestTemplate();
		
		restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
			@Override
			public boolean hasError(ClientHttpResponse response) throws IOException {
				HttpStatus httpStatus = response.getStatusCode();

				httpErrorResponseMap.put("httpstatus",httpStatus.name());
				
				if (httpStatus == HttpStatus.BAD_REQUEST || httpStatus == HttpStatus.UNAUTHORIZED) return false;
				
				return super.hasError(response);
			}
		});
		
//		try {
			@SuppressWarnings("rawtypes") 
			Map map = restTemplate.exchange(path, HttpMethod.POST, new HttpEntity<MultiValueMap<String, String>>(formData, headers), Map.class).getBody();
			@SuppressWarnings("unchecked")
			Map<String, Object> result = map;
			
			result.put("httpstatus", httpErrorResponseMap.get("httpstatus"));
			
			return result;
//		} catch (Exception _ex) {
			
//			Map map = CollectionUtils.toMultiValueMap(httpErrorResponseMap);
			
//			return (Map<String, Object>)map;
//		}
	}		
	
	private static String getAuthorizationHeader(String clientId, String clientSecret) {



		String creds = String.format("%s:%s", clientId, clientSecret);
		try {
			return "Basic " + new String(Base64.encode(creds.getBytes("UTF-8")));
		}
		catch (UnsupportedEncodingException e) {
			throw new IllegalStateException("Could not convert String");
		}
	}	
 	
}
