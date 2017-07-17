package main.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Job implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String jobid;

    @ManyToOne
    @JoinColumn(name = "applicant")
    private Applicant applicant;

    private String company;

    private String title;

    private String summary;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date startdate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJobid() {
        return jobid;
    }

    public void setJobid(String jobid) {
        this.jobid = jobid;
    }

    public Applicant getApplicant() {
        return applicant;
    }

    public void setApplicant(Applicant applicant) {
        this.applicant = applicant;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
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

}
