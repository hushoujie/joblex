package main.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import main.components.Dandelion;

@Entity
public class Applicant implements Serializable {

    private String photo;

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

    private String skills;

    private String honors;

    private String interests;

    private boolean blacklist;

    private String blreason;

    private String keywords;

    @OneToMany(mappedBy = "applicant", cascade = CascadeType.ALL)
    private List<Application> applications;

    @OneToMany(mappedBy = "applicant", cascade = CascadeType.ALL)
    private List<Education> educations;

    @OneToMany(mappedBy = "applicant", cascade = CascadeType.ALL)
    private List<Experience> experiences;

    public void extractKeywords() {
        keywords = Dandelion.extractKeywords(industry) + Dandelion.extractKeywords(location) + Dandelion.extractKeywords(headline)
                + Dandelion.extractKeywords(summary) + Dandelion.extractKeywords(skills);
        for (Education education : educations) {
            keywords += Dandelion.extractKeywords(education.getField()) + Dandelion.extractKeywords(education.getDegree()) + Dandelion.extractKeywords(education.getSummary());
        }
        for (Experience experience : experiences) {
            keywords += Dandelion.extractKeywords(experience.getPosition()) + Dandelion.extractKeywords(experience.getSummary());
        }
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

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

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getHonors() {
        return honors;
    }

    public void setHonors(String honors) {
        this.honors = honors;
    }

    public String getInterests() {
        return interests;
    }

    public void setInterests(String interests) {
        this.interests = interests;
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

    public List<Education> getEducations() {
        return educations;
    }

    public void setEducations(List<Education> educations) {
        this.educations = educations;
    }

    public List<Experience> getExperiences() {
        return experiences;
    }

    public void setExperiences(List<Experience> experiences) {
        this.experiences = experiences;
    }

}
