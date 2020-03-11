package ulcambridge.foundations.viewer.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Allows the sending of email using the properties in the cudl_global.properties and the Amazon Simple
 * Email Service.
 *
 * TODO: use {@code org.springframework.mail} instead
 * @author jlf44
 *
 */
@Component
@Profile("!test")
public class EmailHelper {

    private static final Logger LOG = LoggerFactory.getLogger(EmailHelper.class);
    private final String smtp_host;
    private final String smtp_username;
    private final String smtp_password;
    private final int smtp_port;

    public EmailHelper(
        @Value("${smtp_host}") String smtp_host,
        @Value("${smtp_username}") String smtp_username,
        @Value("${smtp_password}") String smtp_password,
        @Value("${smtp_port}") int smtp_port
    ) {
        this.smtp_host = smtp_host;
        this.smtp_username = smtp_username;
        this.smtp_password = smtp_password;
        this.smtp_port = smtp_port;
    }

    /**
     * Returns true if email is sent successfully and false otherwise.
     *
     * @param emailTo
     * @param emailFrom
     * @param subject
     * @param content
     * @return
     * @throws MessagingException
     */
    public synchronized boolean sendEmail(String emailTo, String emailFrom, String subject, String content) throws MessagingException {

        // Create a Properties object to contain connection configuration information.
        Properties props = new Properties();//System.getProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.port", smtp_port);
        props.put("mail.smtp.auth", "true");

        // Enable SMTPS (TLS Wrapper)
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.ssl.required", "true");

        // Create a Session object to represent a mail session with the specified properties.
        Session session = Session.getDefaultInstance(props);

        // Create a message with the specified information.
        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(emailFrom));
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(emailTo));
        msg.setSubject(subject);
        msg.setContent(content,"text/plain");

        // Create a transport.
        Transport transport = session.getTransport();

        // Send the message.
        try
        {
            // Connect to Amazon SES using the SMTP username and password you specified above.
            transport.connect(smtp_host, smtp_username, smtp_password);

            // Send the email.
            transport.sendMessage(msg, msg.getAllRecipients());
        }
        catch (Exception ex) {
            LOG.error("The email was not sent.", ex);
            return false;
        }
        finally
        {
            // Close and terminate the connection.
            transport.close();
        }

        return true;

    }
}
