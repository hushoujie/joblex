package main;

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

    static String durum(int i) {
        switch (i) {
            case 0:
                return "beklemededir";
            case 1:
                return "işleme alındı";
            case 2:
                return "kabul edildi";
            case 3:
                return "reddedildi";
        }
        return "";
    }

    static void send(String to, String aday, String ilan, int durum) {
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
            message.setSubject(ilan + " ilanına ait başvurunuz hakkında bilgilendirme", "utf-8");
            message.setText("Sayın " + aday + ",\n"
                    + ilan + " ilanına ait başvurunuz " + durum(durum) + ".\n"
                    + "İlginiz için teşekkür ederiz.", "utf-8");
            Transport.send(message);
        } catch (AddressException ex) {
            ex.printStackTrace();
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
    }

}
