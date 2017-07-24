package main.entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import main.dandelion.Similarity;
import org.springframework.web.client.RestTemplate;

@Entity
public class Application implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    @JoinColumn(name = "advert")
    private Advert advert;

    @ManyToOne
    @JoinColumn(name = "applicant")
    private Applicant applicant;

    private int status;

    private String coverletter;

    private double similarity;

    public void calcSimilarity() {
        String url = "https://api.dandelion.eu/datatxt/sim/v1?text1=" + advert.getUrl()
                + "&text2=" + applicant.getUrl()
                + "&token=a397094f43f840a1ba7f20b875baf5ae&lang=en&bow=always";
        RestTemplate restTemplate = new RestTemplate();
        similarity = restTemplate.getForObject(url, Similarity.class).getSimilarity();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Advert getAdvert() {
        return advert;
    }

    public void setAdvert(Advert advert) {
        this.advert = advert;
    }

    public Applicant getApplicant() {
        return applicant;
    }

    public void setApplicant(Applicant applicant) {
        this.applicant = applicant;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCoverletter() {
        return coverletter;
    }

    public void setCoverletter(String coverletter) {
        this.coverletter = coverletter;
    }

    public double getSimilarity() {
        return similarity;
    }

    public void setSimilarity(double similarity) {
        this.similarity = similarity;
    }

}
