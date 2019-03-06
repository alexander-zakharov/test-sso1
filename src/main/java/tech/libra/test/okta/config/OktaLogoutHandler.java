package tech.libra.test.okta.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.session.ExpiringSession;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;

@Component
public class OktaLogoutHandler implements LogoutHandler {

//    @Autowired
//    private SimpMessagingTemplate webSocket;
	
	@Autowired
	FindByIndexNameSessionRepository sessionRepository;

	@Autowired
	FindByIndexNameSessionRepository<? extends ExpiringSession> sessions;

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

		SpringSessionBackedSessionRegistry sessionRegistry = new SpringSessionBackedSessionRegistry(sessionRepository);

		Collection<? extends ExpiringSession> usersSessions = sessions
				.findByIndexNameAndIndexValue(FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME, (String) authentication.getPrincipal())
				.values();

		usersSessions.forEach((temp) -> {
			String sessionId = temp.getId();
			// sessionRegistry.removeSessionInformation(sessionId);
			SessionInformation info = sessionRegistry.getSessionInformation(sessionId);
			info.expireNow();
		});

//        webSocket.convertAndSend("/topic/refresh", "refresh");
	}
}
