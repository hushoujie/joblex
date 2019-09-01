package com.berkerol.joblex.services;

import com.berkerol.joblex.entities.Application;
import com.berkerol.joblex.entities.Job;
import com.berkerol.joblex.entities.User;
import com.berkerol.joblex.repositories.ApplicationRepository;
import org.springframework.stereotype.Service;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;

    public ApplicationService(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    public Application findById(long id) {
        return applicationRepository.findById(id).orElseGet(Application::new);
    }

    public Application save(Application application) {
        return applicationRepository.save(application);
    }

    public void deleteById(long id) {
        applicationRepository.deleteById(id);
    }

    public void deleteByJobAndUser(Job job, User user) {
        applicationRepository.deleteByJobAndUser(job, user);
    }
}
