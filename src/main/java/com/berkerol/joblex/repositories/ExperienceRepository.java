package com.berkerol.joblex.repositories;

import com.berkerol.joblex.entities.Experience;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface ExperienceRepository extends CrudRepository<Experience, Long> {
}
