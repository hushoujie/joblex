package main.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import main.components.Dandelion;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Advert implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String hr;

    private boolean status;

    private String title;

    private String description;

    private String qualifications;

    @DateTimeFormat(pattern = "MM/dd/yyyy hh:mm a")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date activation;

    @DateTimeFormat(pattern = "MM/dd/yyyy hh:mm a")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date deactivation;

    private String keywords;

    @OneToMany(mappedBy = "advert", cascade = CascadeType.ALL)
    private List<Application> applications;

    public void extractKeywords() {
        keywords = Dandelion.extractKeywords(title) + Dandelion.extractKeywords(description) + Dandelion.extractKeywords(qualifications);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHr() {
        return hr;
    }

    public void setHr(String hr) {
        this.hr = hr;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getQualifications() {
        return qualifications;
    }

    public void setQualifications(String qualifications) {
        this.qualifications = qualifications;
    }

    public Date getActivation() {
        return activation;
    }

    public void setActivation(Date activation) {
        this.activation = activation;
    }

    public Date getDeactivation() {
        return deactivation;
    }

    public void setDeactivation(Date deactivation) {
        this.deactivation = deactivation;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public List<Application> getApplications() {
        return applications;
    }

    public void setApplications(List<Application> applications) {
        this.applications = applications;
    }

}
