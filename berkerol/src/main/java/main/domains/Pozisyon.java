package main.domains;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Pozisyon implements Serializable {

    @Id
    private String id;

    private String aday;

    private String title;

    private String summary;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date startdate;

    private boolean iscurrent;

    private String company;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAday() {
        return aday;
    }

    public void setAday(String aday) {
        this.aday = aday;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Date getStartdate() {
        return startdate;
    }

    public void setStartdate(Date startdate) {
        this.startdate = startdate;
    }

    public boolean isIscurrent() {
        return iscurrent;
    }

    public void setIscurrent(boolean iscurrent) {
        this.iscurrent = iscurrent;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}
