package main.services;

import main.entities.Applicant;
import main.entities.Job;
import main.repositories.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobService {

    private JobRepository jobRepository;

    @Autowired
    public void setJobRepository(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public Iterable<Job> findAllJobs() {
        return jobRepository.findAll();
    }

    public Job findJob(Applicant applicant, String jobid) {
        return jobRepository.findOneByApplicantAndJobid(applicant, jobid);
    }

    public Job saveJob(Job job) {
        return jobRepository.save(job);
    }

}