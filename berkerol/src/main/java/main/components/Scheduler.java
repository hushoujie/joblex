package main.components;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import main.entities.Advert;
import main.entities.Applicant;
import main.entities.Education;
import main.entities.Experience;
import main.services.AdvertService;
import main.services.ApplicantService;
import main.solr.Profile;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class Scheduler {

    private AdvertService advertService;

    private ApplicantService applicantService;

    @Autowired
    public void setAdvertService(AdvertService advertService) {
        this.advertService = advertService;
    }

    @Autowired
    public void setApplicantService(ApplicantService applicantService) {
        this.applicantService = applicantService;
    }

    @Scheduled(cron = "0 * * * * *")
    public void setAdvertStatuses() {
        Date date = new Date();
        for (Advert advert : advertService.findAllAdverts()) {
            boolean change = false;
            if (advert.getActivation() != null && advert.getActivation().before(date)) {
                advert.setStatus(true);
                change = true;
            }
            if (advert.getDeactivation() != null && advert.getDeactivation().before(date)) {
                advert.setStatus(false);
                change = true;
            }
            if (change) {
                advertService.saveAdvert(advert);
            }
        }
    }

    @Scheduled(cron = "0 0 * * * *")
    public void updateSolr() throws SolrServerException, IOException {
        SolrClient solrClient = new HttpSolrClient.Builder("http://localhost:8983/solr/applicants").build();
        solrClient.deleteByQuery("*");
        for (Applicant applicant : applicantService.findAllApplicants()) {
            Profile profile = new Profile(applicant.getPhoto(), applicant.getFirstname(), applicant.getLastname(), applicant.getIndustry(), applicant.getLocation(),
                    applicant.getEmail(), applicant.getId(), applicant.getCountry(), applicant.getHeadline(), applicant.getSummary(), applicant.getSkills(), applicant.getHonors(),
                    applicant.getInterests(), applicant.isBlacklist(), applicant.getBlreason());
            LinkedList<Integer> education_id = new LinkedList<>();
            LinkedList<String> education_school = new LinkedList<>();
            LinkedList<String> education_field = new LinkedList<>();
            LinkedList<String> education_degree = new LinkedList<>();
            LinkedList<Date> education_startdate = new LinkedList<>();
            LinkedList<Date> education_enddate = new LinkedList<>();
            LinkedList<String> education_summary = new LinkedList<>();
            for (Education education : applicant.getEducations()) {
                education_id.add(education.getId());
                education_school.add(education.getSchool());
                education_field.add(education.getField());
                education_degree.add(education.getDegree());
                education_startdate.add(education.getStartdate());
                education_enddate.add(education.getEnddate());
                education_summary.add(education.getSummary());
            }
            profile.setEducation_id(education_id);
            profile.setEducation_school(education_school);
            profile.setEducation_field(education_field);
            profile.setEducation_degree(education_degree);
            profile.setEducation_startdate(education_startdate);
            profile.setEducation_enddate(education_enddate);
            profile.setEducation_summary(education_summary);
            LinkedList<Integer> experience_id = new LinkedList<>();
            LinkedList<String> experience_linkedinid = new LinkedList<>();
            LinkedList<String> experience_company = new LinkedList<>();
            LinkedList<String> experience_position = new LinkedList<>();
            LinkedList<Date> experience_startdate = new LinkedList<>();
            LinkedList<Date> experience_enddate = new LinkedList<>();
            LinkedList<String> experience_summary = new LinkedList<>();
            for (Experience experience : applicant.getExperiences()) {
                experience_id.add(experience.getId());
                experience_linkedinid.add(experience.getLinkedinid());
                experience_company.add(experience.getCompany());
                experience_position.add(experience.getPosition());
                experience_startdate.add(experience.getStartdate());
                experience_enddate.add(experience.getEnddate());
                experience_summary.add(experience.getSummary());
            }
            profile.setExperience_id(experience_id);
            profile.setExperience_linkedinid(experience_linkedinid);
            profile.setExperience_company(experience_company);
            profile.setExperience_position(experience_position);
            profile.setExperience_startdate(experience_startdate);
            profile.setExperience_enddate(experience_enddate);
            profile.setExperience_summary(experience_summary);
            solrClient.addBean(profile);
        }
        solrClient.commit();
    }

}
