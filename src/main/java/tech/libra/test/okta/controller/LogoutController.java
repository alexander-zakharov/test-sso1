package tech.libra.test.okta.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class LogoutController {

    @Autowired
    private OAuth2ClientContext oauth2ClientContext;

    // Okta only accepts HttpMethod.GET at this time
    private String logoutUrl = "https://dev-801845.oktapreview.com/oauth2/default/v1/logout?id_token_hint=%s&post_logout_redirect_uri=http://app1:8080/logout";

    @RequestMapping("/sso/logout")
    public RedirectView logout(HttpServletRequest request, HttpServletResponse response) {

        OAuth2AccessToken accessToken = oauth2ClientContext.getAccessToken();

        if (accessToken != null) {
            String idToken = accessToken.getAdditionalInformation().get("id_token").toString();
            return new RedirectView(String.format(logoutUrl, idToken));
        }

        return new RedirectView("/");
    }
}
