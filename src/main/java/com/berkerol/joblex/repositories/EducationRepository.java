package com.berkerol.joblex.repositories;

import com.berkerol.joblex.entities.Education;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface EducationRepository extends CrudRepository<Education, Long> {
}
