package tech.libra.test.okta.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class GreetingController {

	@GetMapping("/")
	public ModelAndView home() {
		ModelAndView mv = new ModelAndView("index");
		mv.addObject("token", getAccessToken());
		return mv;
	}
	
    @RequestMapping("/greeting")
    public String greeting(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "greeting";
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
}