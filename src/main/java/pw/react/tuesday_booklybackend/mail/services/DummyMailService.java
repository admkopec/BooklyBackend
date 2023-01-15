package pw.react.tuesday_booklybackend.mail.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import pw.react.tuesday_booklybackend.models.User;

import java.io.IOException;

@Profile("!sendgrid")
public class DummyMailService implements MailService {
    private static final Logger logger = LoggerFactory.getLogger(DummyMailService.class);
    @Override
    public void sendWelcomeEmailTo(User user) throws IOException {
        logger.info("This server uses a dummy mail sender. No emails will be sent.");
    }
    @Override
    public void sendReservationCancelledEmailTo(User user) throws IOException {
        logger.info("This server uses a dummy mail sender. No emails will be sent.");
    }
}
