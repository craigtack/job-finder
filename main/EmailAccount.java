package main;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailAccount {
    private String email;
    private String password;

    public EmailAccount() {
        email = Configuration.getConfig().get("gmail").get("email");
        password = Configuration.getConfig().get("gmail").get("password");
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void sendEmail(final String body) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(getEmail(), getPassword());
            }
        });
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress("job-finder@job-finder.com"));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(getEmail()));
            message.setSubject("Job-Finder Listings");
            message.setText(body, "UTF-8", "html");

            Transport.send(message);
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
