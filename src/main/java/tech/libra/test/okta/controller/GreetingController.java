package tech.libra.test.okta.controller;

import java.security.Principal;

import javax.annotation.Resource;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import tech.libra.test.okta.config.OktaLogoutHandler;

@Controller
public class GreetingController {
	
	@Resource OktaLogoutHandler handler;
	@Resource OAuth2ClientContext clientContext;

	@GetMapping("/")
	public ModelAndView home(Principal principal) {
		
		handler.logout("jdjddjjd");
		
		OAuth2Authentication authentication = (OAuth2Authentication)principal;
		String name = authentication.getName();
	
		ModelAndView mv = new ModelAndView("index");
		mv.addObject("token", getAccessToken());
		mv.addObject("principal", principal);
		return mv;
	
	}
	
    @RequestMapping("/greeting")
    public String greeting(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
    
    	model.addAttribute("name", name);
        return "greeting";
    
    }
    
    private String getAccessToken() {

    	OAuth2AccessToken accessToken = clientContext.getAccessToken();
    	return accessToken.getAdditionalInformation().get("id_token").toString();

    }    
    
    private String getBearerToken() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        if (securityContext == null) return "no_token";
        
        Authentication authentication = securityContext.getAuthentication();
        if (!(authentication instanceof OAuth2Authentication) && !(authentication.getDetails() instanceof OAuth2AuthenticationDetails)) return "no_token";

        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails)authentication.getDetails();
        
        System.out.println(details.getTokenValue());
        
        return details.getTokenValue();    	
    }
}