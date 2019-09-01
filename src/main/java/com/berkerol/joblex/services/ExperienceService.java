package com.berkerol.joblex.services;

import com.berkerol.joblex.entities.Experience;
import com.berkerol.joblex.repositories.ExperienceRepository;
import org.springframework.stereotype.Service;

@Service
public class ExperienceService {

    private final ExperienceRepository experienceRepository;

    public ExperienceService(ExperienceRepository experienceRepository) {
        this.experienceRepository = experienceRepository;
    }

    public Experience save(Experience experience) {
        return experienceRepository.save(experience);
    }

    public Iterable<Experience> saveAll(Iterable<Experience> experiences) {
        return experienceRepository.saveAll(experiences);
    }

    public void deleteById(long id) {
        experienceRepository.deleteById(id);
    }
}
