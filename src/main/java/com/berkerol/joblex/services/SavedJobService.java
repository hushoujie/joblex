package com.berkerol.joblex.services;

import com.berkerol.joblex.entities.Job;
import com.berkerol.joblex.entities.SavedJob;
import com.berkerol.joblex.entities.User;
import com.berkerol.joblex.repositories.SavedJobRepository;
import org.springframework.stereotype.Service;

@Service
public class SavedJobService {

    private final SavedJobRepository savedJobRepository;

    public SavedJobService(SavedJobRepository savedJobRepository) {
        this.savedJobRepository = savedJobRepository;
    }

    public SavedJob save(SavedJob savedJob) {
        return savedJobRepository.save(savedJob);
    }

    public void deleteByJobAndUser(Job job, User user) {
        savedJobRepository.deleteByJobAndUser(job, user);
    }
}
