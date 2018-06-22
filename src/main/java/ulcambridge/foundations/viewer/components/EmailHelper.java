package ulcambridge.foundations.viewer.components;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Allows the sending of email using the properties in the cudl_global.properties and the Amazon Simple
 * Email Service.
 *
 * TODO: use {@code org.springframework.mail} instead
 * @author jlf44
 *
 */
@Component
public class EmailHelper {

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
        java.util.Properties props = System.getProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.port", smtp_port);


        // Set properties indicating that we want to use STARTTLS to encrypt the connection.
        // The SMTP session will begin on an unencrypted connection, and then the client
        // will issue a STARTTLS command to upgrade to an encrypted connection.
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.enable", "true");
        //props.put("mail.smtp.starttls.required", "true");

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
            System.err.println("The email was not sent.");
            System.err.println("Error message: " + ex.getMessage());
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
