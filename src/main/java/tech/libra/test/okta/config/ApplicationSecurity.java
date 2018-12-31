package tech.libra.test.okta.config;

import javax.annotation.Resource;

import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2SsoDefaultConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2SsoProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;



@Configuration
@EnableOAuth2Sso
public class ApplicationSecurity extends OAuth2SsoDefaultConfiguration {
	
	@Resource LogoutHandler logoutHandler;

    public ApplicationSecurity(ApplicationContext applicationContext, OAuth2SsoProperties sso) {
        super(applicationContext, sso);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // In this example we allow anonymous access to the root index page
        // this MUST be configured before calling super.configure
//        http.authorizeRequests().antMatchers("/").authenticated();

        // calling super.configure locks everything else down
        super.configure(http);
        // after calling super, you can change the logout success url
        http
        	.logout()
        		.deleteCookies()
        		.invalidateHttpSession(true)
        		.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
        		.addLogoutHandler(logoutHandler);
//        		.logoutSuccessUrl("/");
    }
}
