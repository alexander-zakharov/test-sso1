package tech.libra.test.okta.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.session.ConcurrentSessionFilter;
import org.springframework.security.web.session.SimpleRedirectSessionInformationExpiredStrategy;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;

@Configuration
public class SessionConfig {

    @Autowired
    FindByIndexNameSessionRepository sessionRepository;

    @Bean
    public ConcurrentSessionFilter concurrentSessionFilter(){
        SimpleRedirectSessionInformationExpiredStrategy expiredSessionStrategy =
                new SimpleRedirectSessionInformationExpiredStrategy("/");
        return new ConcurrentSessionFilter(sessionRegistry(), expiredSessionStrategy);
    }


    @Bean
    public SessionRegistry sessionRegistry(){
        return new SpringSessionBackedSessionRegistry(sessionRepository);
    }
}
