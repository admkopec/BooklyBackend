package pw.react.tuesday_booklybackend.mail.services;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import pw.react.tuesday_booklybackend.models.User;

@Profile("sendgrid")
public class MailService {
    private static final Logger logger = LoggerFactory.getLogger(MailService.class);
    @Autowired
    private SendGrid sendGrid;

    public String sendWelcomeEmailTo(User user) throws IOException {
        // the sender email should be the same as we used to Create a Single Sender Verification
        Email from = new Email("noreply@bookly...", "Bookly");
        String subject = "Welcome to Bookly!";
        Email to = new Email(user.getEmail());
        // TODO: Add proper Welcome email content
        Content content = new Content("text/plain", "TODO: Put here something!");
        Mail mail = new Mail(from, subject, to, content);

        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());
        Response response = sendGrid.api(request);
        logger.info(response.getBody());
        return response.getBody();
    }
}
