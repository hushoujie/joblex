package main.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Applicant implements Serializable {

    private String firstname;

    private String lastname;

    private String industry;

    private String location;

    private String email;

    @Id
    private String id;

    private String country;

    private String headline;

    private String summary;

    private boolean blacklist;

    private String blreason;

    @OneToMany(mappedBy = "applicant", cascade = CascadeType.ALL)
    private List<Application> applications;

    @OneToMany(mappedBy = "applicant", cascade = CascadeType.ALL)
    private List<Job> jobs;

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public boolean isBlacklist() {
        return blacklist;
    }

    public void setBlacklist(boolean blacklist) {
        this.blacklist = blacklist;
    }

    public String getBlreason() {
        return blreason;
    }

    public void setBlreason(String blreason) {
        this.blreason = blreason;
    }

    public List<Application> getApplications() {
        return applications;
    }

    public void setApplications(List<Application> applications) {
        this.applications = applications;
    }

    public List<Job> getJobs() {
        return jobs;
    }

    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
    }

}
