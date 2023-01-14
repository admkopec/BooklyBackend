package pw.react.tuesday_booklybackend.mail.services;

import pw.react.tuesday_booklybackend.models.User;

import java.io.IOException;

public interface MailService {
    void sendWelcomeEmailTo(User user) throws IOException;
}
