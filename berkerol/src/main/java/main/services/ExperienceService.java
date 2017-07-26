package main.services;

import main.entities.Applicant;
import main.entities.Experience;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import main.repositories.ExperienceRepository;

@Service
public class ExperienceService {

    private ExperienceRepository experienceRepository;

    @Autowired
    public void setExperienceRepository(ExperienceRepository experienceRepository) {
        this.experienceRepository = experienceRepository;
    }

    public Iterable<Experience> findAllExperiences() {
        return experienceRepository.findAll();
    }

    public Experience findExperience(Applicant applicant, String linkedinid) {
        return experienceRepository.findOneByApplicantAndLinkedinid(applicant, linkedinid);
    }

    public Experience saveExperience(Experience experience) {
        return experienceRepository.save(experience);
    }

}
