package com.berkerol.joblex.util;

import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import com.mailjet.client.resource.Emailv31;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MailSender {

    @Value("${MJ_APIKEY_PUBLIC}")
    private String publicKey;

    @Value("${MJ_APIKEY_PRIVATE}")
    private String privateKey;

    public void send(String email, String name, String subject, String message) {
        MailjetClient client = new MailjetClient(publicKey, privateKey, new ClientOptions("v3.1"));
        MailjetRequest request = new MailjetRequest(Emailv31.resource)
                .property(Emailv31.MESSAGES, new JSONArray()
                        .put(new JSONObject()
                                .put(Emailv31.Message.FROM, new JSONObject()
                                        .put("Email", "berk.erol@boun.edu.tr")
                                        .put("Name", "Joblex Admin"))
                                .put(Emailv31.Message.TO, new JSONArray()
                                        .put(new JSONObject()
                                                .put("Email", email)
                                                .put("Name", name)))
                                .put(Emailv31.Message.SUBJECT, subject)
                                .put(Emailv31.Message.TEXTPART, message)));
        try {
            client.post(request);
        } catch (MailjetException | MailjetSocketTimeoutException e) {
            System.out.println(e);
        }
    }
}
