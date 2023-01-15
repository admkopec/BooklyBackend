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
public class SendGridMailService implements MailService {
    private static final Logger logger = LoggerFactory.getLogger(SendGridMailService.class);
    @Autowired
    private SendGrid sendGrid;

    private Email booklyEmail() {
        // the sender email should be the same as we used to Create a Single Sender Verification
        return new Email("noreply@bookly.pw", "Bookly");
    }

    private void sendMail(Mail mail) throws IOException {
        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());
        Response response = sendGrid.api(request);
        logger.info(response.getBody());
    }

    @Override
    public void sendWelcomeEmailTo(User user) throws IOException {
        Email from = booklyEmail();
        String subject = "Welcome to Bookly!";
        Email to = new Email(user.getEmail());
        // TODO: Add proper Welcome email content
        Content content = new Content("text/plain", "TODO: Put here something!");
        Mail mail = new Mail(from, subject, to, content);
        sendMail(mail);
    }

    @Override
    public void sendReservationCancelledEmailTo(User user) throws IOException {
        Email from = booklyEmail();
        String subject = "Your reservation has been cancelled!";
        Email to = new Email(user.getEmail());
        // TODO: Add proper Reservation cancelled email content
        Content content = new Content("text/plain", "TODO: Put here something!");
        Mail mail = new Mail(from, subject, to, content);
        sendMail(mail);
    }
}
