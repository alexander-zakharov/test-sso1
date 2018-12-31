package tech.libra.test.okta.security.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

@Component
public class CustomPreZuulFilter extends ZuulFilter {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.addZuulRequestHeader("Authorization", "Bearer " + getAccessToken());

        return null;
    }

    private String getAccessToken() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        if (securityContext == null) return "no_token";
        
        Authentication authentication = securityContext.getAuthentication();
        if (!(authentication instanceof OAuth2Authentication) && !(authentication.getDetails() instanceof OAuth2AuthenticationDetails)) return "no_token";

        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails)authentication.getDetails();
        
        System.out.println(details.getTokenValue());
        
        return details.getTokenValue();

    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public int filterOrder() {
        return -2;
    }

    @Override
    public String filterType() {
        return "pre";
    }

}
