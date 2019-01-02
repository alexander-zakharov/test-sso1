package tech.libra.test.okta.config;

import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.okta.spring.oauth.discovery.OidcDiscoveryClient;

@Component
public class OktaLogoutHandler implements ApplicationContextAware {
	
	private ApplicationContext applicationContext;
	
	@Value("${okta.oauth2.issuer}") private String issuer;
	
	private OidcDiscoveryClient oidcDiscoveryClient;
	private RestTemplate restTemplate;
	
	@PostConstruct
	public void init() {
		restTemplate = new RestTemplate();

		oidcDiscoveryClient = new OidcDiscoveryClient(issuer);
		
        HttpMessageConverter formHttpMessageConverter = new FormHttpMessageConverter();
        HttpMessageConverter stringHttpMessageConverternew = new StringHttpMessageConverter();

        restTemplate.setMessageConverters(Arrays.asList(new HttpMessageConverter[]{formHttpMessageConverter, stringHttpMessageConverternew}));
	}

	public void logout(String token) {

	  OAuth2ClientContext context = applicationContext.getBean(OAuth2ClientContext.class);

      MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
      params.add("client_id", "0oaeayzl7x7X0mC5A0h7");
      params.add("client_secret", "hfBvLEI662_d8gTLw_QoNLXR_zOSLotf5Fp_WRqC");
      params.add("token", token);

      HttpHeaders restHeaders = new HttpHeaders();

      HttpEntity<String> restRequest = new HttpEntity(params, restHeaders);
		
		try {
			ResponseEntity<String> response = restTemplate.exchange(issuer + "/v1/introspect", HttpMethod.POST, restRequest, String.class);

			System.out.println(response.getBody());
		} catch (HttpClientErrorException e) {
			System.out.println(e.getStatusText());
			e.printStackTrace();
		}      
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		
		this.applicationContext = applicationContext;
		
	}

}
