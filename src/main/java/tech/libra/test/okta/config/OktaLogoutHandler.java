package tech.libra.test.okta.config;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.okta.spring.oauth.discovery.OidcDiscoveryClient;

@Component
public class OktaLogoutHandler implements LogoutHandler, ApplicationContextAware {
	
	private ApplicationContext applicationContext;
	
	@Value("${okta.oauth2.issuer}") private String issuer;
	
	private String logoutUrl = "https://dev-872798.oktapreview.com/oauth2/default/v1/logout";
	private RestTemplate restTemplate;
	
	@PostConstruct
	public void init() {
		restTemplate = new RestTemplate();
		OidcDiscoveryClient oidDiscoveryClient = new OidcDiscoveryClient(issuer);
		logoutUrl = oidDiscoveryClient.discover().getEndSessionEndpoint();
		
//        HttpMessageConverter formHttpMessageConverter = new FormHttpMessageConverter();
//        HttpMessageConverter stringHttpMessageConverternew = new StringHttpMessageConverter();
//
//        restTemplate.setMessageConverters(Arrays.asList(new HttpMessageConverter[]{formHttpMessageConverter, stringHttpMessageConverternew}));
	}

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

		OAuth2ClientContext context = applicationContext.getBean(OAuth2ClientContext.class);
		
		
//		restTemplate.execute("https://dev-872798.oktapreview.com/oauth2/default/v1/logout", method, requestCallback, responseExtractor, uriVariables)
		if (!(authentication instanceof OAuth2Authentication) && !(authentication.getDetails() instanceof OAuth2AuthenticationDetails)) return ;

        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails)authentication.getDetails();
        ((OAuth2Authentication)authentication).getUserAuthentication().getDetails();
        
        
        String token = context.getAccessToken().getAdditionalInformation().get("id_token").toString();
        
        System.out.println(token);
        
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//        params.add("id_token_hint", token);
//
//        HttpHeaders restHeaders = new HttpHeaders();
////        restHeaders.add("Authorization", "bearer " + token);
//
//        HttpEntity<String> restRequest = new HttpEntity(params, restHeaders);

        
        
//        
//        try {
//            ResponseEntity<String> restResponse = restTemplate.getForEntity(logoutUrl + "?id_token_hint=" + token, String.class);
//            System.out.println(restResponse);
//        } catch(HttpClientErrorException e) {
//        	System.out.println(e.getStatusText());
//        	e.printStackTrace();
////            LOGGER.error("HttpClientErrorException invalidating token with SSO authorization server. response.status code: {}, server URL: {}", e.getStatusCode(), logoutUrl);
//        }
        
        System.out.println(logoutUrl + "?id_token_hint=" + token + "&post_logout_redirect_uri=http://localhost:8080");
        
        response.setStatus(HttpServletResponse.SC_FOUND);

        response.setHeader("Location", logoutUrl + "?id_token_hint=" + token + "&post_logout_redirect_uri=http://localhost:8080");
        response.setContentType(MediaType.TEXT_PLAIN_VALUE);
        try {
			response.getWriter().write(logoutUrl + "?id_token_hint=" + token  + "&post_logout_redirect_uri=http://localhost:8080");
	        response.getWriter().flush();
	        response.getWriter().close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		
		this.applicationContext = applicationContext;
		
	}

}
