package main.repositories;

import main.entities.Applicant;
import main.entities.Job;
import org.springframework.data.repository.CrudRepository;

public interface JobRepository extends CrudRepository<Job, Integer> {

    public Job findOneByApplicantAndJobid(Applicant applicant, String jobid);

}
