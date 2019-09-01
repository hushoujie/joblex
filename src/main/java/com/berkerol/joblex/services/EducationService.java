package com.berkerol.joblex.services;

import com.berkerol.joblex.entities.Education;
import com.berkerol.joblex.repositories.EducationRepository;
import org.springframework.stereotype.Service;

@Service
public class EducationService {

    private final EducationRepository educationRepository;

    public EducationService(EducationRepository educationRepository) {
        this.educationRepository = educationRepository;
    }

    public Education save(Education education) {
        return educationRepository.save(education);
    }

    public Iterable<Education> saveAll(Iterable<Education> educations) {
        return educationRepository.saveAll(educations);
    }

    public void deleteById(long id) {
        educationRepository.deleteById(id);
    }
}
