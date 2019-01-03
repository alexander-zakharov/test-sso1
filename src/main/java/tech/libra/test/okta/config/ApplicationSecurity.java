package tech.libra.test.okta.config;

import org.apache.commons.lang.StringUtils;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2SsoDefaultConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2SsoProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;



@Configuration
@EnableOAuth2Sso
public class ApplicationSecurity extends OAuth2SsoDefaultConfiguration {

    public ApplicationSecurity(ApplicationContext applicationContext, OAuth2SsoProperties sso) {
        super(applicationContext, sso);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        super.configure(http);

        http
        	.logout()
        		.deleteCookies()
        		.invalidateHttpSession(true)
        		.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
        		.logoutSuccessHandler((rq,rs, a) -> {
        			String postLogoutRedirect = rq.getParameter("post_logout_redirect");
        			if (StringUtils.isEmpty(postLogoutRedirect)) postLogoutRedirect = "/";
        			rs.sendRedirect(postLogoutRedirect);
        		});
    }
}
