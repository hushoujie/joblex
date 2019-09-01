package com.berkerol.joblex.elasticsearch;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class Job {

    private long id;

    private boolean status;

    private String logo;

    private String position;

    private String company;

    private String location;

    private String description;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date activation;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date deactivation;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
}
