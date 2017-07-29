package main.solr;

import java.util.Date;
import java.util.List;
import org.apache.solr.client.solrj.beans.Field;

public class Profile {

    public Profile(String photo, String firstname, String lastname, String industry, String location, String email, String id, String country, String headline, String summary,
            String skills, String honors, String interests, boolean blacklist, String blreason) {
        this.photo = photo;
        this.firstname = firstname;
        this.lastname = lastname;
        this.industry = industry;
        this.location = location;
        this.email = email;
        this.id = id;
        this.country = country;
        this.headline = headline;
        this.summary = summary;
        this.skills = skills;
        this.honors = honors;
        this.interests = interests;
        this.blacklist = blacklist;
        this.blreason = blreason;
    }

    @Field
    private String photo;

    @Field
    private String firstname;

    @Field
    private String lastname;

    @Field
    private String industry;

    @Field
    private String location;

    @Field
    private String email;

    @Field
    private String id;

    @Field
    private String country;

    @Field
    private String headline;

    @Field
    private String summary;

    @Field
    private String skills;

    @Field
    private String honors;

    @Field
    private String interests;

    @Field
    private boolean blacklist;

    @Field
    private String blreason;

    @Field
    private List<Integer> education_id;

    @Field
    private List<String> education_school;

    @Field
    private List<String> education_field;

    @Field
    private List<String> education_degree;

    @Field
    private List<Date> education_startdate;

    @Field
    private List<Date> education_enddate;

    @Field
    private List<String> education_summary;

    @Field
    private List<Integer> experience_id;

    @Field
    private List<String> experience_linkedinid;

    @Field
    private List<String> experience_company;

    @Field
    private List<String> experience_position;

    @Field
    private List<Date> experience_startdate;

    @Field
    private List<Date> experience_enddate;

    @Field
    private List<String> experience_summary;

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

    public List<Integer> getEducation_id() {
        return education_id;
    }

    public void setEducation_id(List<Integer> education_id) {
        this.education_id = education_id;
    }

    public List<String> getEducation_school() {
        return education_school;
    }

    public void setEducation_school(List<String> education_school) {
        this.education_school = education_school;
    }

    public List<String> getEducation_field() {
        return education_field;
    }

    public void setEducation_field(List<String> education_field) {
        this.education_field = education_field;
    }

    public List<String> getEducation_degree() {
        return education_degree;
    }

    public void setEducation_degree(List<String> education_degree) {
        this.education_degree = education_degree;
    }

    public List<Date> getEducation_startdate() {
        return education_startdate;
    }

    public void setEducation_startdate(List<Date> education_startdate) {
        this.education_startdate = education_startdate;
    }

    public List<Date> getEducation_enddate() {
        return education_enddate;
    }

    public void setEducation_enddate(List<Date> education_enddate) {
        this.education_enddate = education_enddate;
    }

    public List<String> getEducation_summary() {
        return education_summary;
    }

    public void setEducation_summary(List<String> education_summary) {
        this.education_summary = education_summary;
    }

    public List<Integer> getExperience_id() {
        return experience_id;
    }

    public void setExperience_id(List<Integer> experience_id) {
        this.experience_id = experience_id;
    }

    public List<String> getExperience_linkedinid() {
        return experience_linkedinid;
    }

    public void setExperience_linkedinid(List<String> experience_linkedinid) {
        this.experience_linkedinid = experience_linkedinid;
    }

    public List<String> getExperience_company() {
        return experience_company;
    }

    public void setExperience_company(List<String> experience_company) {
        this.experience_company = experience_company;
    }

    public List<String> getExperience_position() {
        return experience_position;
    }

    public void setExperience_position(List<String> experience_position) {
        this.experience_position = experience_position;
    }

    public List<Date> getExperience_startdate() {
        return experience_startdate;
    }

    public void setExperience_startdate(List<Date> experience_startdate) {
        this.experience_startdate = experience_startdate;
    }

    public List<Date> getExperience_enddate() {
        return experience_enddate;
    }

    public void setExperience_enddate(List<Date> experience_enddate) {
        this.experience_enddate = experience_enddate;
    }

    public List<String> getExperience_summary() {
        return experience_summary;
    }

    public void setExperience_summary(List<String> experience_summary) {
        this.experience_summary = experience_summary;
    }

}
