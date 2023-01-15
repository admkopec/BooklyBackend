package pw.react.tuesday_booklybackend.configs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pw.react.tuesday_booklybackend.services.ServicesIntegrationService;

import javax.annotation.PostConstruct;

@Configuration
@ConfigurationProperties(prefix = "bookly")
public class ServicesIntegrationConfig {
    private static final Logger log = LoggerFactory.getLogger(ServicesIntegrationConfig.class);

    private String user;
    private String password;

    private String parklyUrl;
    private String carlyUrl;
    private String flatlyUrl;

    @PostConstruct
    private void init() {
        log.debug("************** Services Integration properties **************");
        log.debug("Bookly user: {}", user);
        log.debug("Bookly password: {}", password);

        log.debug("Parkly url: {}", parklyUrl);
        log.debug("Carly url: {}", carlyUrl);
        log.debug("Flatly url: {}", flatlyUrl);
    }

    @Bean
    ServicesIntegrationService servicesIntegrationService() {
        return new ServicesIntegrationService(user, password, parklyUrl, carlyUrl, flatlyUrl);
    }
}
