package main.components;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;

public class Dandelion {

    public static String extractKeywords(String content) {
        String labels = "";
        if (content != null) {
            HttpPost httpPost = new HttpPost("https://api.dandelion.eu/datatxt/nex/v1");
            List<NameValuePair> params = new ArrayList<>(4);
            params.add(new BasicNameValuePair("lang", "en"));
            params.add(new BasicNameValuePair("min_confidence", "0.7"));
            params.add(new BasicNameValuePair("token", "a397094f43f840a1ba7f20b875baf5ae"));
            try {
                params.add(new BasicNameValuePair("text", URLEncoder.encode(Jsoup.parse(content).text(), "UTF-8")));
                httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(Dandelion.class.getName()).log(Level.SEVERE, null, ex);
            }
            JSONArray annotations = new JSONArray();
            try {
                annotations = (JSONArray) ((JSONObject) new JSONParser().parse(EntityUtils.toString(HttpClients.createDefault().execute(httpPost).getEntity(), "UTF-8"))).get("annotations");
            } catch (ParseException | IOException ex) {
                Logger.getLogger(Dandelion.class.getName()).log(Level.SEVERE, null, ex);
            }
            for (Object annotation : annotations) {
                labels += ((JSONObject) annotation).get("spot") + "+";
            }
        }
        return labels;
    }

    public static double calcSimilarity(String advert, String applicant) {
        HttpPost httpPost = new HttpPost("https://api.dandelion.eu/datatxt/sim/v1");
        List<NameValuePair> params = new ArrayList<>(5);
        params.add(new BasicNameValuePair("text1", advert));
        params.add(new BasicNameValuePair("text2", applicant));
        params.add(new BasicNameValuePair("lang", "en"));
        params.add(new BasicNameValuePair("bow", "never"));
        params.add(new BasicNameValuePair("token", "a397094f43f840a1ba7f20b875baf5ae"));
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Dandelion.class.getName()).log(Level.SEVERE, null, ex);
        }
        Double similarity = new Double(0);
        try {
            similarity = (Double) ((JSONObject) new JSONParser().parse(EntityUtils.toString(HttpClients.createDefault().execute(httpPost).getEntity(), "UTF-8"))).get("similarity");
        } catch (ParseException | IOException ex) {
            Logger.getLogger(Dandelion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return similarity;
    }

}
