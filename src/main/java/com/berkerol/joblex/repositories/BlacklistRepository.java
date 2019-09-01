package com.berkerol.joblex.repositories;

import com.berkerol.joblex.entities.Blacklist;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface BlacklistRepository extends CrudRepository<Blacklist, Long> {

    Iterable<Blacklist> findAllByRecruiterId(long recruiterId);

    void deleteByCandidateIdAndRecruiterId(long candidateId, long recruiterId);
}
