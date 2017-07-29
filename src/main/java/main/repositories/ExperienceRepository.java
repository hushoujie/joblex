package main.repositories;

import main.entities.Applicant;
import main.entities.Experience;
import org.springframework.data.repository.CrudRepository;

public interface ExperienceRepository extends CrudRepository<Experience, Integer> {

    public Experience findOneByApplicantAndLinkedinid(Applicant applicant, String linkedinid);

}
