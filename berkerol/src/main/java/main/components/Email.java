package main.components;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Email {

    private static String getStatus(int status) {
        switch (status) {
            case 0:
                return "waiting";
            case 1:
                return "processing";
            case 2:
                return "accepted";
            case 3:
                return "rejected";
        }
        return "";
    }

    public static void send(String to, String advert, String applicant, int status) {
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.starttls.enable", "true");
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.port", "587");
        properties.setProperty("mail.smtp.host", "smtp.gmail.com");
        Session session = Session.getInstance(properties,
                new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("obss.ik.1@gmail.com", "zxcvasdf");
            }
        });
        MimeMessage message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress("obss.ik.1@gmail.com"));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject("Information about your application for the advert " + advert);
            message.setText("Dear " + applicant + ",\n"
                    + "Your application for the advert " + advert + " is " + getStatus(status) + ".\n"
                    + "Thank you for your interest.");
            Transport.send(message);
        } catch (AddressException ex) {
            ex.printStackTrace();
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
    }

}
