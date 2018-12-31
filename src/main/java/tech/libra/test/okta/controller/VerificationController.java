package tech.libra.test.okta.controller;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class VerificationController {

	@RequestMapping(value="/code", method=RequestMethod.GET)
	public String verificationCodeGet(Principal principal) {
		return "code";
	}
	
	@RequestMapping(value="/code", method=RequestMethod.POST)
	public RedirectView verificationCodePost(Principal principal, @RequestParam("verification_code") Long code) {
		System.out.println(principal);
		
		if (principal instanceof UsernamePasswordAuthenticationToken) {
			
			UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken)principal;
			User user = (User)authenticationToken.getPrincipal();
			
			
			if (code == 1111L) {
				
				List<GrantedAuthority> grantedAuthorityList = Arrays.asList(new SimpleGrantedAuthority("ROLE_AUTH_USER"));
				
				User newUser = new User(user.getUsername(), user.getUsername(), grantedAuthorityList);
				newUser.eraseCredentials();
				
				UsernamePasswordAuthenticationToken newToken = new UsernamePasswordAuthenticationToken(newUser, authenticationToken.getCredentials(), grantedAuthorityList);
				
				SecurityContext securityContext = SecurityContextHolder.getContext();
				securityContext.setAuthentication(newToken);
				
				return new RedirectView("index");
			}
			
		}

		return new RedirectView("code");
	}
}
