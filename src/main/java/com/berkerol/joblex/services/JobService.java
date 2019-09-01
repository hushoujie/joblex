package com.berkerol.joblex.services;

import com.berkerol.joblex.entities.Job;
import com.berkerol.joblex.repositories.JobRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobService {

    private final JobRepository jobRepository;

    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public Iterable<Job> findAll() {
        return jobRepository.findAll();
    }

    public List<Job> findAllByStatus(boolean status) {
        return jobRepository.findAllByStatus(status);
    }

    public Job findById(long id) {
        return jobRepository.findById(id).orElseGet(Job::new);
    }

    public Job save(Job job) {
        return jobRepository.save(job);
    }

    public void deleteById(long id) {
        jobRepository.deleteById(id);
    }
}
