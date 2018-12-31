package tech.libra.test.okta;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;


@SpringBootApplication
@EnableOAuth2Sso
@EnableZuulProxy
public class App {

	public static void main( String[] args )
    {
        SpringApplication.run(App.class, args);
    }

//	@Bean
//	public FilterRegistrationBean someFilterRegistration() {
//
//		Map<String,String> initParameters = new HashMap<>();
//		initParameters.put("allowableResourcesRoot", "/");
//		initParameters.put("excludeRegex", "^(/[^/]+)?/((img/)|(css/)|(fonts/)|(js/)|(vendors/)|(view/)|(version)|(index))");
//		
//		
//	    FilterRegistrationBean registration = new FilterRegistrationBean();
//	    registration.setFilter(new SecurityWrapper());
//	    registration.addUrlPatterns("/*");
//	    registration.setName("esapi");
//	    registration.setInitParameters(initParameters);
//	    registration.setOrder(5);
//	    return registration;
//	} 
}
