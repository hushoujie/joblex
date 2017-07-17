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
import javax.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Advert implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String hr;

    private boolean status;

    @Size(min = 5, max = 50, message = "length should be between 5 and 50")
    private String title;

    @Size(min = 5, max = 200, message = "length should be between 5 and 200")
    private String description;

    @Size(min = 5, max = 200, message = "length should be between 5 and 200")
    private String properties;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date activation;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
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

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
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

}
