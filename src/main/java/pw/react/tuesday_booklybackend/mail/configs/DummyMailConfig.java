package pw.react.tuesday_booklybackend.mail.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import pw.react.tuesday_booklybackend.mail.services.MailService;
import pw.react.tuesday_booklybackend.mail.services.DummyMailService;

@Configuration
@Profile("!sendgrid")
public class DummyMailConfig {
    @Bean
    public MailService mailService() {
        return new DummyMailService();
    }
}
