package main.services;

import main.entities.Applicant;
import main.repositories.ApplicantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApplicantService {

    private ApplicantRepository applicantRepository;

    @Autowired
    public void setApplicantRepository(ApplicantRepository applicantRepository) {
        this.applicantRepository = applicantRepository;
    }

    public Iterable<Applicant> findAllApplicants() {
        return applicantRepository.findAll();
    }

    public Applicant findApplicant(String id) {
        return applicantRepository.findOne(id);
    }

    public Applicant saveApplicant(Applicant applicant) {
        return applicantRepository.save(applicant);
    }

}
