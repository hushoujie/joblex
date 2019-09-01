package com.berkerol.joblex.repositories;

import com.berkerol.joblex.entities.Job;
import com.berkerol.joblex.entities.SavedJob;
import com.berkerol.joblex.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface SavedJobRepository extends CrudRepository<SavedJob, Long> {

    void deleteByJobAndUser(Job job, User User);
}
