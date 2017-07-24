package main.entities;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
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

    @DateTimeFormat(pattern = "MM/dd/yyyy HH:mm")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date activation;

    @DateTimeFormat(pattern = "MM/dd/yyyy HH:mm")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date deactivation;

    @OneToMany(mappedBy = "advert", cascade = CascadeType.ALL)
    private List<Application> applications;

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

    public List<Application> getApplications() {
        return applications;
    }

    public void setApplications(List<Application> applications) {
        this.applications = applications;
    }

    public String getUrl() {
        try {
            return URLEncoder.encode(this.getTitle() + " " + this.getDescription(), "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Advert.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

}
