package main.services;

import main.entities.Education;
import main.repositories.EducationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EducationService {

    private EducationRepository educationRepository;

    @Autowired
    public void setEducationRepository(EducationRepository educationRepository) {
        this.educationRepository = educationRepository;
    }

    public Iterable<Education> findAllEducations() {
        return educationRepository.findAll();
    }

    public Education findEducation(int id) {
        return educationRepository.findOne(id);
    }

    public Education saveEducation(Education education) {
        return educationRepository.save(education);
    }

}
