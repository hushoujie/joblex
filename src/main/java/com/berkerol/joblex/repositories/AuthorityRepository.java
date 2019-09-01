package com.berkerol.joblex.repositories;

import com.berkerol.joblex.entities.Authority;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface AuthorityRepository extends CrudRepository<Authority, Long> {

    List<Authority> findAllByAuthority(String authority);
}
