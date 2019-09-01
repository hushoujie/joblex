package com.berkerol.joblex.repositories;

import com.berkerol.joblex.entities.Job;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface JobRepository extends CrudRepository<Job, Long> {

    List<Job> findAllByStatus(boolean status);
}
